package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.service.OrderSelectService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderSelectController {
    @Reference
    private OrderSelectService orderSelectService;

    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows) {
        PageResult result = orderSelectService.findPage(null, page, rows);
        return result;
    }
}

