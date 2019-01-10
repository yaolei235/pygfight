package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import cn.itcast.core.util.Constants;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao catDao;

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private TypeTemplateDao typeTemplateDao;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        //1. 查询所有分类数据
        List<ItemCat> itemAllList = catDao.selectByExample(null);
        //2. 将所有分类数据以分类名称为key, 对应的模板id为value, 存入redis中
        for (ItemCat itemCat : itemAllList) {
            redisTemplate.boundHashOps(Constants.CATEGORY_LIST_REDIS).put(itemCat.getName(), itemCat.getTypeId());
        }

        //3. 到数据库中查询数据返回到页面展示
        ItemCatQuery query = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = query.createCriteria();
        //根据父级id查询
        criteria.andParentIdEqualTo(parentId);
        List<ItemCat> itemCats = catDao.selectByExample(query);
        return itemCats;
    }

    @Override
    public ItemCat findOne(Long id) {
        return catDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {

        List<ItemCat> itemCatList = catDao.findAll();
        return itemCatList;
    }

    @Override
    public List<Map> selectOptionList() {
      return   catDao.selectOptionList();
    }

    @Override
    public List<Map> selectTypeList() {
        return typeTemplateDao.selectOptionList();
    }

    @Override
    public void add(ItemCat itemCat) {
        itemCat.setStatus(0l);
        catDao.insertSelective(itemCat);
    }

    @Override
    public void updateStatus(final Long id, String status) {
        //1. 修改商品状态
        ItemCat itemCat  = new ItemCat();
        itemCat.setId(id);
        itemCat.setStatus(Long.parseLong(status));
        catDao.updateByPrimaryKeySelective(itemCat);
    }




    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<ItemCat> page=   (Page<ItemCat>) catDao.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
