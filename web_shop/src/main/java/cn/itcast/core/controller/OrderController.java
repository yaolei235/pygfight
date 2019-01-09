package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/search")
    public PageResult search(@RequestBody Order order, int page, int rows) {
        //通过springsecurity获得到登录商家的ID
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        PageResult pageResult = orderService.findPageBySellerId(order, page, rows, sellerId);
        List<Order> orderList = pageResult.getRows();
        return pageResult;
    }

}
