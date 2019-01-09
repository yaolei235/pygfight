package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.OrderSellerCountEntity;
import cn.itcast.core.service.OrderSellerCountService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderItemCount")
public class OrderItemCountController {

    @Reference
    private OrderSellerCountService orderSellerCountService;

    public List<OrderSellerCountEntity> findAll() {
        //TODO 1.查询出所有的商家列表

        //TODO 2.根据商家列表的商家ID从OrderItem数据表中查询出对应的商家订单数量

        //TODO 3.根据查询到的商家数量计算出对应的商品总额

        return null;
    }
}
