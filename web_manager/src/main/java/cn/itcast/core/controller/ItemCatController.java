package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import cn.itcast.core.util.ExcelUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @RequestMapping({"/findAll"})
    public List<ItemCat> findAll() {
        List<ItemCat> itemCatList = catService.findAll();
        return itemCatList;
    }


    @RequestMapping("/updateStatus")
    public Result updateStatus(long[] ids,String status){
        try {
            if (ids != null) {
                for (Long id : ids) {
                    catService.updateStatus(id, status);
                }
            }
            return new Result(true, "状态修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败!");
        }
    }



    @RequestMapping("/findPage")
    public PageResult  findPage(Integer page, Integer rows){
        return catService.findPage(page, rows);
    }


    @RequestMapping("/updateItemCas")
    public Result updateItemCas(String excelUrl){
        try {
            List<ItemCat> ItemCat = ExcelUtil.readExcelItemCas(excelUrl);
            catService.addItemCas(ItemCat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true,"保存成功");
    }
}
