package cn.itcast.core.service;


import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.user.User;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JmsTemplate jmsTemplate;

    //点对点发送, 向这个目标中发送
    @Autowired
    private ActiveMQQueue smsDestination;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${template_code}")
    private String template_code;

    @Value("${sign_name}")
    private String sign_name;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderDao orderDao;
    @Override
    public void sendCode(final String phone) {

        //1. 生成随机6为数字作为验证码
        StringBuffer sb = new StringBuffer();
        for(int i = 1; i< 7; i++) {
            sb.append(new Random().nextInt(10));
        }

        final String smsCode = sb.toString();

        //2. 手机号作为key, 验证码最为value存入redis中, 有效时间为10分钟
        redisTemplate.boundValueOps(phone).set(sb.toString(), 60*10, TimeUnit.SECONDS);

        //3. 将手机号, 验证码, 模板编号, 签名封装成Map类型的消息发送给消息服务器
        jmsTemplate.send(smsDestination, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setString("mobile", phone);//手机号
                message.setString("template_code", template_code);//模板编码
                message.setString("sign_name", sign_name);//签名
                Map map=new HashMap();
                map.put("code", smsCode);	//验证码
                message.setString("param", JSON.toJSONString(map));
                return (Message) message;
            }
        });

    }

    @Override
    public Boolean checkSmsCode(String phone, String smsCode) {
        //1. 判断如果手机号或者验证码为空, 直接返回false
        if (phone == null || smsCode == null || "".equals(phone) || "".equals(smsCode)) {
            return false;
        }
        //2. 根据手机号到redis中获取我们自己的验证码
        String redisSmsCode = (String)redisTemplate.boundValueOps(phone).get();

        //3. 根据页面传入的验证码和我们自己存的验证码对比是否正确
        if (smsCode.equals(redisSmsCode)) {
            return true;
        }

        return false;
    }

    @Override
    public void add(User user) {
        userDao.insertSelective(user);
    }

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        for(int i = 1; i< 7; i++) {
            sb.append(new Random().nextInt(10));
        }

        System.out.println("======" + sb.toString());
    }
    //查询用户及商品订单信息
    public List<ArrayList<String>> findAllUser() {
        List<User> users = userDao.selectByExample(null);
        List<ArrayList<String>> lists=new ArrayList<>();

        for (User user : users) {
            ArrayList<String> list = new ArrayList<>();
            list.add(user.getId()+"");
            list.add(user.getUsername());
            list.add(user.getPhone());
            //查询订单表
            OrderQuery orderQuery = new OrderQuery();
            OrderQuery.Criteria criteria1 = orderQuery.createCriteria();
            criteria1.andUserIdEqualTo(user.getUsername());
            List<Order> orders = orderDao.selectByExample(orderQuery);
            if (orders.size() > 0) {
                for (Order order : orders) {
                    ArrayList<String> list1 = new ArrayList<>(list);
                    list1.add(order.getReceiverAreaName());
                    list1.add(order.getReceiver());
                    list1.add(order.getOrderId()+"");
                    OrderItemQuery query = new OrderItemQuery();
                    OrderItemQuery.Criteria criteria = query.createCriteria();
                    criteria.andOrderIdEqualTo(order.getOrderId());
                    List<OrderItem> orderItems = orderItemDao.selectByExample(query);
                    if (orderItems.size()>0){
                        for (OrderItem orderItem : orderItems) {
                            ArrayList<String> orderItemList = new ArrayList<>(list1);
                            orderItemList.add(orderItem.getTitle());
                            orderItemList.add(orderItem.getSellerId());
                            orderItemList.add(orderItem.getNum()+"");
                            orderItemList.add(orderItem.getTotalFee()+"");
                            orderItemList.add(getStatus(order));
                            orderItemList.add(getPayment(order));
                            lists.add(orderItemList);

                        }
                    }else{
                        list1.add("无");
                        list1.add("无");
                        list1.add("无");
                        list1.add("无");
                        list1.add(getStatus(order));
                        list1.add(getPayment(order));
                        lists.add(list1);
                    }


                }
            }else {
                lists.add(list);
            }


        }
        return lists;
    }
    //获取支付状态
    private String getStatus(Order order){
        //订单状态  状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
        if ("1".equals(order.getStatus())){
            return "未付款";
        }else if ("2".equals(order.getStatus())){
            return "已付款";
        }else if ("3".equals(order.getStatus())){
            return "未发货";
        }else if ("4".equals(order.getStatus())){
            return "已发货";
        }else if ("5".equals(order.getStatus())){
            return "交易成功";
        }else if ("6".equals(order.getStatus())){
            return "未付款";
        }else if ("7".equals(order.getStatus())){
            return "待评价";
        }else {
            return "订单状态无效";
        }
    }
    /**
     * 支付类型，1、在线支付，2、货到付款
     */
    private String getPayment(Order order){
        if ("1".equals(order.getPaymentType())){
            return "在线支付";
        }else {
            return "货到付款";
        }
    }
//查询sql列名
    /*public List<String> findTitle() {
        List<String> title1 = userDao.findTitle();
        List<String> title=new ArrayList<>();
        return title;
    }*/

    @Override
    public Map<String, Integer> findActiveUsers( ) {

        Map<String, Integer> map = new HashMap<>();
        List<User> userList = userDao.selectByExample(null);

        //用户总数量
        int size = userList.size();

        //活跃用户数量
        int activeCount=0;

        //非活跃用户数量
        int unactiveCount=0;

        for (User user : userList) {
            Integer experienceValue = user.getExperienceValue();
            if (experienceValue>2 && experienceValue!=null){
                activeCount ++;
            }else {
                unactiveCount++;
            }
        }

        map.put("totalCount", size);
        map.put("activeCount", activeCount);
        map.put("unactiveCount",unactiveCount);

        return map;
    }

}
