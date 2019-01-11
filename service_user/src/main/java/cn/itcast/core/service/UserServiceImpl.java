package cn.itcast.core.service;


import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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

import cn.itcast.core.pojo.entity.ActiveUsers;

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
    public List<ArrayList<String>> findAllUsers() {
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
    public ActiveUsers findActiveUsers() {

        ActiveUsers activeUsers = new ActiveUsers();
        List<User> userList = userDao.selectByExample(null);

        //用户总数量
        int size = userList.size();

        //活跃用户数量
        int activeCount=0;

        //非活跃用户数量
        int unactiveCount=0;

        for (User user : userList) {
            //获取每个用户的经验值
            Integer experienceValue = user.getExperienceValue();

            if (experienceValue!=null && experienceValue>2){
                //活跃用户数量+1
                activeCount ++;
            }else {
                //经验值小于2,非活跃用户+1
                unactiveCount++;
            }
        }

        activeUsers.setAllUsers(size);
        activeUsers.setActiveUsers(activeCount);
        activeUsers.setUntiveUsers(unactiveCount);

        return activeUsers;
    }

    @Override
    public void updateStatus(final Long id, String status) {
        //修改用户状态
        User user = new User();
        user.setId(id);
        user.setAuditstatus(status);
        userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public void creatExperienceValue(String userName) {
        //用户登录时候,经验值加1

        if (userName!=null){

            UserQuery query = new UserQuery();
            UserQuery.Criteria criteria = query.createCriteria();
            criteria.andUsernameEqualTo(userName);

            //获取该用户民对应的用户(列表)
            List<cn.itcast.core.pojo.user.User> userList = userDao.selectByExample(query);

            for (cn.itcast.core.pojo.user.User user : userList) {

                if (user.getExperienceValue()==null){

                    user.setExperienceValue(1);
                }else {
                    user.setExperienceValue(user.getExperienceValue()+1);
                }
                //更新到数据库
                userDao.updateByPrimaryKeySelective(user);
            }
        }
    }

    //展示该用户未支付的订单
    @Override
    public List<Order> showUnPayOrders(String userName) {

        //根据用户名查询该用户所有订单
        OrderQuery query = new OrderQuery();
        OrderQuery.Criteria criteria = query.createCriteria();
        criteria.andUserIdEqualTo(userName);

        List<Order> orderList = orderDao.selectByExample(query);

        if (orderList!=null){
            for (Order order : orderList) {
                //每一个订单
                OrderItemQuery orderItemQuery = new OrderItemQuery();
                OrderItemQuery.Criteria criteria1 = orderItemQuery.createCriteria();
                criteria1.andOrderIdEqualTo(order.getOrderId());

                List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);

                order.setOrderItemList(orderItemList);
            }
            return orderList;
        }

        return null;
    }

    @Override
    public User findOne(String username) {
        UserQuery query = new UserQuery();
        UserQuery.Criteria criteria = query.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<User> users = userDao.selectByExample(query);
        if (users.size()>0){
            return users.get(0);
        } else return null;
    }
    @Override
    public PageResult findPage(User user, Integer page, Integer rows) {
        //使用分页助手, 传入当前页和每页查询多少条数据
        PageHelper.startPage(page, rows);
        //创建查询对象
        UserQuery query = new UserQuery();
        //创建where查询条件
        UserQuery.Criteria criteria = query.createCriteria();
        if (user != null) {
            if (user.getUsername() != null && !"".equals(user.getUsername())){
                criteria.andUsernameLike("%"+user.getUsername()+"%");
            }
        }
        //使用分页助手的page对象接收查询到的数据, page对象继承了ArrayList所以可以接收查询到的结果集数据.
        Page<User> userList = (Page<User>)userDao.selectByExample(query);
        return new PageResult(userList.getTotal(), userList.getResult());
    }
}
