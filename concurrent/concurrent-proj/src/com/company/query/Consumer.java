package com.company.query;


import java.util.concurrent.DelayQueue;

/**
 * 消费者
 */
public class Consumer implements Runnable {

    //订单队列
    private DelayQueue<ItemsQuery<Order>> query;

    public Consumer(DelayQueue<ItemsQuery<Order>> query) {
        this.query = query;
    }

    @Override
    public void run() {
        //自旋效率比while(1)高
        for (; ; ) {
            try {
                ItemsQuery<Order> take = query.take();
                Order item = take.getItem();
                System.out.println("订单号：" + item.getOrderNum());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
