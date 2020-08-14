package com.enjoy.dao.impl;

import com.enjoy.dao.OrderDao;
import com.enjoy.entity.OrderEntiry;
import org.springframework.stereotype.Service;

@Service
public class OrderDaoImpl implements OrderDao {
    @Override
    public OrderEntiry getDetail(String id) {

        OrderEntiry orderEntiry = new OrderEntiry();
        orderEntiry.setId("O0001");
        orderEntiry.setMoney(1000);
        orderEntiry.setUserId("U0001");

        return orderEntiry;
    }

    @Override
    public OrderEntiry submit(OrderEntiry order) {
        System.out.println("余额不足，请充值");
        order.setStatus(0);
        return order;
    }

    @Override
    public boolean cancel(OrderEntiry order) {
        System.out.println("取消订单成功");
        order.setStatus(2);
        return true;
    }
}
