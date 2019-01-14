package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderSelectServiceImpl implements OrderSelectService {

    @Autowired
    private OrderItemDao orderItemDao;

    @Override
    public PageResult findPage(OrderItem orderItem, Integer page, Integer rows) {
        //使用分页助手, 传入当前页和每页查询多少条数据
        PageHelper.startPage(page, rows);
        //创建查询对象
        OrderItemQuery query = new OrderItemQuery();
        //创建where查询条件
        OrderItemQuery.Criteria criteria = query.createCriteria();
        if (orderItem != null) {
            if (orderItem.getSellerId() != null && !"".equals(orderItem.getSellerId())) {
                criteria.andSellerIdEqualTo(orderItem.getSellerId());
            }
        }
        //使用分页助手的page对象接收查询到的数据, page对象继承了ArrayList所以可以接收查询到的结果集数据.
        Page<OrderItem> orderItems = (Page<OrderItem>)orderItemDao.selectByExample(query);
        return new PageResult(orderItems.getTotal(), orderItems.getResult());
    }
}
