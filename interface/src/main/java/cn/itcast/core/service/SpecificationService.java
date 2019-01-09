package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.SpecEntity;
import cn.itcast.core.pojo.specification.Specification;

import java.util.List;
import java.util.Map;

public interface SpecificationService {

    //分页显示
    public PageResult findPage(Specification specification, Integer page, Integer rows);

    //添加
    public void add(SpecEntity mySpecification);

    //修改
    public SpecEntity findOne(Long id);
    public void update(SpecEntity mySpecifition);

    //删除
    public void delete(Long[] ids);

    //作为模板的下拉菜单
    List<Map> selectOptionList();


    void updateStatus(Long[] ids, String status);
}
