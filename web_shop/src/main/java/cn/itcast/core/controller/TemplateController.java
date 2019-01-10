package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.TemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TemplateController {

    @Reference
    private TemplateService templateService;

    @RequestMapping("/findOne")
    public TypeTemplate findOne(Long id) {
        TypeTemplate one = templateService.findOne(id);
        return one;
    }

    /**
     * 根据模板id, 获取规格和对应的规格选项集合数据
     * @param id
     * @return
     */
    @RequestMapping("/findBySpecList")
    public List<Map> findBySpecList(Long id) {
        List<Map> list = templateService.findBySpecList(id);
        return list;
    }



    @RequestMapping("/search")
    public PageResult search(@RequestBody TypeTemplate typeTemplate,Integer page,Integer rows){
        PageResult result = templateService.search(typeTemplate, page, rows);

        return result;
    }


    @RequestMapping("/add")
    public Result add(@RequestBody TypeTemplate template){
        try {
            template.setStatus("0");
            templateService.add(template);
            return new Result(true, "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败!");
        }
    }


    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try {
            templateService.delete(ids);
            return new Result(true, "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败!");
        }
    }


}
