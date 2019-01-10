package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.TotalOrder;

public interface TotalOrderService {

    PageResult findPageBySellerId(TotalOrder totalOrder, int page, int rows, String sellerId);
}
