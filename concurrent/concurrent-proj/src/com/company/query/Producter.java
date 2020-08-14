package com.company.query;


import java.util.concurrent.DelayQueue;

/**
 * 生产者
 */
public class Producter implements Runnable {

    //订单队列
    private DelayQueue<ItemsQuery<Order>> query;

    public Producter(DelayQueue<ItemsQuery<Order>> query) {
        this.query = query;
    }

    @Override
    public void run() {

        Order order = new Order("123");
        //5秒到期时间
        ItemsQuery<Order> itemsQuery = new ItemsQuery<>(order, 5000);
        query.offer(itemsQuery);

        Order order2 = new Order("456");
        //8秒到期时间
        ItemsQuery<Order> itemsQuery2 = new ItemsQuery<>(order2, 8000);
        query.offer(itemsQuery2);

    }
}
