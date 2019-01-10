package cn.itcast.core.pojo.entity;

import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.seller.Seller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OrderSellerCountEntity implements Serializable {

    private Seller seller;

    private List<OrderItem> orderItemList;

    private long countNum;

    private BigDecimal totalCount;

    public long getCountNum() {
        return countNum;
    }

    public void setCountNum(long countNum) {
        this.countNum = countNum;
    }

    public BigDecimal getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigDecimal totalCount) {
        this.totalCount = totalCount;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
