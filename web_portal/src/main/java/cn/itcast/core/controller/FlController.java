package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/index")
@RestController
public class FlController {
    @Reference
    private ItemCatService itemCatService;


    @RequestMapping("/showItemCat")
    public List<ItemCat> showItemCat(){
        List<ItemCat> itemCatList = itemCatService.findItemCatList();
        return itemCatList;
    }
}
