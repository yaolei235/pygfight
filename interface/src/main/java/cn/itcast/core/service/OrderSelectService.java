package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.OrderItem;

public interface OrderSelectService {

    PageResult findPage(OrderItem orderItem, Integer page, Integer rows);

}
