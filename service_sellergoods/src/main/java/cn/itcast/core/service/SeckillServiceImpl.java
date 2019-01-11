package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Service
@Transactional
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    @Override
    public void add(SeckillGoods seckillGoods) {
        Date date = new Date();
        String startTime2 = seckillGoods.getStartTime2();
        String endTime2 = seckillGoods.getEndTime2();
        startTime2=startTime2.replace("T"," ");
        endTime2=endTime2.replace("T"," ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startTime=null;
        Date endTime=null;
        try {
             startTime = sdf.parse(startTime2);
            endTime= sdf.parse(endTime2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        seckillGoods.setEndTime(endTime);
        seckillGoods.setEndTime(startTime);
        seckillGoods.setCreateTime(date);
        seckillGoodsDao.insertSelective(seckillGoods);



    }
}
