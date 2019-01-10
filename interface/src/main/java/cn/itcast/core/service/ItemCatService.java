package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;
import java.util.Map;

public interface ItemCatService {

    public List<ItemCat> findByParentId(Long parentId);


    public ItemCat findOne(Long id);

    public List<ItemCat> findAll();


    List<Map> selectOptionList();

    List<Map> selectTypeList();

    void add(ItemCat itemCat);

    public void  updateStatus(Long id, String  status);

    public PageResult findPage(Integer pageNum, Integer pageSize);

}
