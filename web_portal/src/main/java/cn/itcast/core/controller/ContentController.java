package cn.itcast.core.controller;

import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ContentService;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 展示广告
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;

    @Reference
    private ItemCatService itemCatService;

    /**
     * 根据分类id查询广告集合
     * @param categoryId
     * @return
     */
    @RequestMapping("/findByCategoryId")
    public List<Content> findByCategoryId(Long categoryId) {
        return contentService.findByCategoryIdFromRedis(categoryId);
    }

    /**
     * 商品分类
     * @return
     */
    @RequestMapping("/showItemCat")
    public List<ItemCat> showItemCat(){
        List<ItemCat> itemCatList = itemCatService.findItemCatList();
        return itemCatList;
    }

    @RequestMapping("/findByCategoryId1")
    public List<Content> findByCategoryId1(Long categoryId) {
        return contentService.findByCategoryIdFromRedis(categoryId);
    }

    @RequestMapping("/findByCategoryId2")
    public List<Content> findByCategoryId2(Long categoryId) {
        return contentService.findByCategoryIdFromRedis(categoryId);
    }



}
