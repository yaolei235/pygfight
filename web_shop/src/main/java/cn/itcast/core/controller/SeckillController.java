package cn.itcast.core.controller;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckill")
public class SeckillController {
    @RequestMapping("/add")
    public void add(Long[] ids,@RequestBody SeckillGoods seckillGoods){
        for (Long id : ids) {
            seckillGoods.setId(id);
        }
    }
}
