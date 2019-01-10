package cn.itcast.core.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TotalOrder implements Serializable {
    private String goodsName;
    private String orderId;
    private Integer numOfGoods;
    private BigDecimal totalFee;
    private Date startTime;
    private Date endTime;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getNumOfGoods() {
        return numOfGoods;
    }

    public void setNumOfGoods(Integer numOfGoods) {
        this.numOfGoods = numOfGoods;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
