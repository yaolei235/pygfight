package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.TotalOrder;
import cn.itcast.core.service.TotalOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/totalOrder")
public class TotalOrderController {
    @Reference
    private TotalOrderService totalOrderService;
    @RequestMapping("/search")
    public PageResult search(@RequestBody TotalOrder totalOrder, int page, int rows) {
        //通过springsecurity获得到登录商家的ID
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        //通过商家ID得到该商家的所有订单
        System.out.println(totalOrder.getStartTime());
        PageResult pageResult = totalOrderService.findPageBySellerId(totalOrder, page, rows, sellerId);
        return pageResult;
    }
}
