package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.entity.OrderSellerCountEntity;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.seller.Seller;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderSellerCountServiceImpl implements OrderSellerCountService {

    @Autowired
    private SellerDao sellerDao;

    @Autowired
    private OrderItemDao orderItemDao;



    @Override
    public List<OrderSellerCountEntity> findAll() {
        //创建订单统计对象
        List<OrderSellerCountEntity> orderItemCountEntitys = new ArrayList<>();
        //查询出所有的商家列表
        List<Seller> sellers = sellerDao.selectByExample(null);
        //创建查询对象
        OrderItemQuery query = new OrderItemQuery();
        OrderItemQuery.Criteria criteria = query.createCriteria();

        for (OrderSellerCountEntity orderItemCountEntity : orderItemCountEntitys) {
            //遍历商家,将对应商家的订单集合统计出来
            for (Seller seller : sellers) {
                criteria.andSellerIdEqualTo(seller.getSellerId());
                List<OrderItem> items = orderItemDao.selectByExample(query);
                orderItemCountEntity.setSeller(seller);
                orderItemCountEntity.setOrderItemList(items);
                orderItemCountEntity.setCountNum(getCountNum(items));
                orderItemCountEntity.setTotalCount(getTotalCount(items));
            }
        }
        //将统计出来的数据添加到OrderItemCountEntity返回
        return orderItemCountEntitys;
    }

    //获取对应商家的订单数量
    private long getCountNum(List<OrderItem> items) {
        long countNum = 0;
        for (OrderItem item : items) {
            countNum += item.getNum();
        }
        return countNum;
    }


    //获取对应商家订单总额
    private BigDecimal getTotalCount(List<OrderItem> items) {
        double t = 0;
        BigDecimal tt = BigDecimal.valueOf(t);
        for (OrderItem item : items) {
            BigDecimal totalFee = item.getTotalFee();
            tt = tt.add(totalFee);
        }
        return tt;
    }

}

