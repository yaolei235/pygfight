package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 分类管理
 *
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService catService;

    /**
     * 根据父级id查询它对应的子集数据
     * @param parentId   父级id
     * @return
     */
    @RequestMapping("/findByParentId")
    public List<ItemCat> findByParentId(Long parentId) {
        List<ItemCat> list = catService.findByParentId(parentId);
        return list;
    }

    @RequestMapping("/findOne")
    public ItemCat findOne(Long id) {
        return catService.findOne(id);
    }

    @RequestMapping("/findAll")
    public  List<ItemCat> findAll() {
        return catService.findAll();
    }


    @RequestMapping({"/selectOptionList"})
    public List<Map> selectOptionList() {
        return this.catService.selectOptionList();
    }

    @RequestMapping({"/selectTypeList"})
    public List<Map> selectTypeList() {
        return this.catService.selectTypeList();
    }

    @RequestMapping({"/add"})
    public Result add(@RequestBody ItemCat itemCat) {
        try {
            this.catService.add(itemCat);
            return new Result(true, "保存成功");
        } catch (Exception var3) {
            var3.printStackTrace();
            return new Result(false, "保存失败");
        }
    }
}
