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

    /**
     * 商品分类
     * @return
     */
    @Override
    public List<ItemCat> findItemCatList() {
        //从缓存中查询首页商品分类
        List<ItemCat> itemCatList = (List<ItemCat>) redisTemplate.boundHashOps("itemCatList").get("indexItemCat");

        //如果缓存中没有数据，则从数据库查询再存入缓存
        if(itemCatList==null){
            //查询出1级商品分类的集合
            ItemCatQuery query=new ItemCatQuery();
            query.createCriteria().andParentIdEqualTo(0L);

            itemCatList = catDao.selectByExample(query);

            //遍历1级商品分类的集合
            for(ItemCat itemCat1:itemCatList){
                //查询2级商品分类的集合(将1级商品分类的id作为条件)
                ItemCatQuery query1=new ItemCatQuery();
                query1.createCriteria().andParentIdEqualTo(itemCat1.getId());
                List<ItemCat> itemCatList2=catDao.selectByExample(query1);

                //遍历2级商品分类的集合
                for(ItemCat itemCat2:itemCatList2){
                    //查询3级商品分类的集合(将2级商品分类的父id作为条件)
                    ItemCatQuery query2=new ItemCatQuery();
                    query2.createCriteria().andParentIdEqualTo(itemCat2.getId());
                    List<ItemCat> itemCatList3 = catDao.selectByExample(query2);
                    //将2级商品分类的集合封装到2级商品分类实体中
                    itemCat2.setItemCatList(itemCatList3);
                }
                /*到这一步的时候，3级商品分类已经封装到2级分类中*/
                //将2级商品分类的集合封装到1级商品分类实体中
                itemCat1.setItemCatList(itemCatList2);
            }
            //存入缓存
            redisTemplate.boundHashOps("itemCatList").put("indexItemCat",itemCatList);

            return itemCatList;
        }
        //到这一步，说明缓存中有数据，直接返回
        return itemCatList;

    }

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
