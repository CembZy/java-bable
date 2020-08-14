package com.concurrent.ch05.delaydb;


import java.math.BigDecimal;
import java.util.concurrent.DelayQueue;

/**
 * 生产者线程，往阻塞队列中添加商品
 */
public class PutItems implements Runnable {

    private DelayQueue<Item<Order>> delayQueue;

    public PutItems(DelayQueue<Item<Order>> delayQueue) {
        this.delayQueue = delayQueue;
    }

    @Override
    public void run() {

        //添加5S过期的商品
        Order order = new Order("苹果", new BigDecimal(100));
        delayQueue.offer(new Item<>(5000, order));
        System.out.println("商品：" + order.getName() + "....5秒后到期....");


        //添加8S过期的商品
        Order order2 = new Order("香蕉", new BigDecimal(200));
        delayQueue.offer(new Item<>(8000, order2));
        System.out.println("商品：" + order2.getName() + "....8秒后到期....");
    }
}
