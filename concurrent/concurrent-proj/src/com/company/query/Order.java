package com.company.query;


/**
 * 订单
 */
public class Order {

    private String orderNum;

    public Order(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
