package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.entity.SpecEntity;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @Reference
    private SpecificationService  specificationService;

    @RequestMapping("/search")
    public PageResult findPage(@RequestBody Specification specification, Integer page, Integer rows){
        return specificationService.findPage(specification,page, rows);
    }

    //这里传过来的对象应该是specifition 但是我们用自定义的接受 页面使用的是specifition我们也用
    @RequestMapping("/add")
    public Result add(@RequestBody SpecEntity specifition){
        try {
            specifition.getSpecification().setStatus("0");
            specificationService.add(specifition);
            return new Result(true,"添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败!");
        }
    }

    @RequestMapping("/findOne")
    public SpecEntity findOne(Long id){
        return specificationService.findOne(id);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody SpecEntity specifition){
        try {
            specificationService.update(specifition);
            return new Result(true,"添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败!");
        }
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try {
            specificationService.delete(ids);
            return new Result(true,"添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败!");
        }
    }


    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList(){
        List<Map> maps = specificationService.selectOptionList();
        return maps;
    }




}
