package com.enjoy.service;

import com.enjoy.entity.OrderEntiry;

public interface OrderService {
    OrderEntiry getDetail(String id);
    OrderEntiry submit(OrderEntiry order);
    boolean cancel(OrderEntiry order);
}
