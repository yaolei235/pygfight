package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.TotalOrder;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class TotalOrderServiceImpl implements TotalOrderService {
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public PageResult findPageBySellerId(TotalOrder totalOrder, int page, int rows, String sellerId) {
       /* PageHelper.startPage(page, rows);
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria orderCriteria = orderQuery.createCriteria();
        if (totalOrder.getStartTime() != null && !"".equals(totalOrder.getStartTime()) ||
                totalOrder.getEndTime() != null && !"".equals(totalOrder.getEndTime())) {
            orderCriteria.andCreateTimeBetween(totalOrder.getStartTime(), totalOrder.getEndTime());
        }
        List<Order> orderList = orderDao.selectByExample(orderQuery);
        OrderItemQuery orderItemQuery = new OrderItemQuery();
        OrderItemQuery.Criteria criteria = orderItemQuery.createCriteria();
        for (Order order : orderList) {
            Long orderId = order.getOrderId();
            criteria.andOrderIdEqualTo(orderId);
            if (totalOrder.getGoodsName() != null && !"".equals(totalOrder.getGoodsName())) {
                criteria.andTitleLike(totalOrder.getGoodsName());
            }
        }
        List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
        Page<OrderItem> pageR = (Page<OrderItem>)orderItemDao.selectByExample(orderItemQuery);
        return new PageResult(,totalOrderList);*/
        PageHelper.startPage(page, rows);
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria orderCriteria = orderQuery.createCriteria();
        orderCriteria.andSellerIdEqualTo(sellerId);
        if (totalOrder.getStartTime() != null && !"".equals(totalOrder.getStartTime()) &&
                totalOrder.getEndTime() != null && !"".equals(totalOrder.getEndTime())) {
            orderCriteria.andCreateTimeBetween(totalOrder.getStartTime(), totalOrder.getEndTime());
        }
        List<Order> orderList = orderDao.selectByExample(orderQuery);
        OrderItemQuery orderItemQuery = new OrderItemQuery();
        OrderItemQuery.Criteria criteria = orderItemQuery.createCriteria();
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Order order : orderList) {
            Long orderId = order.getOrderId();
            criteria.andOrderIdEqualTo(orderId);
            if (totalOrder.getGoodsName()!=null&&!"".equals(totalOrder.getGoodsName())) {
                criteria.andTitleLike(totalOrder.getGoodsName());
            }
            orderItemList.addAll(orderItemDao.selectByExample(orderItemQuery));
        }
        List<OrderItem> totalList = getTotalList(orderItemList);
        List<TotalOrder> finalList = new ArrayList<>();
        for (OrderItem orderItem : totalList) {
            TotalOrder totalOrder1 = new TotalOrder();
            totalOrder1.setGoodsName(orderItem.getTitle());
            totalOrder1.setNumOfGoods(orderItem.getNum());
            totalOrder1.setTotalFee(orderItem.getTotalFee());
            totalOrder1.setOrderId(String.valueOf(orderItem.getOrderId()));
            finalList.add(totalOrder1);
        }
        return new PageResult(Long.parseLong(String.valueOf(finalList.size())),finalList);
    }

    private List<OrderItem> getTotalList(List<OrderItem> orderItemList) {
        Map<String, OrderItem> map = new HashMap<>();
        for (OrderItem orderItem : orderItemList) {
            String title = orderItem.getTitle();
            if (map.containsKey(title)) {
                OrderItem againOrderItem = map.get(title);
                againOrderItem.setTotalFee(againOrderItem.getTotalFee().add(orderItem.getTotalFee()));
                againOrderItem.setNum(againOrderItem.getNum()+orderItem.getNum());
            } else {
                map.put(title, orderItem);
            }
        }
        List<OrderItem> newOrderItemList = new ArrayList<>();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            newOrderItemList.add(map.get(key));
        }
        return newOrderItemList;
    }

}
