package com.concurrent.ch05.delaydb;


import java.util.concurrent.DelayQueue;

/**
 * 消费者线程，从阻塞队列中拿商品
 */
public class GetItems implements Runnable {

    //阻塞队列
    private DelayQueue<Item<Order>> delayed;

    public GetItems(DelayQueue delayed) {
        this.delayed = delayed;
    }


    @Override
    public void run() {
        //因为需要一直从队列中拿，所以需要自旋
        for (; ; ) {
            try {
                //take是阻塞方法，直到拿到商品
                Item<Order> take = delayed.take();
                Order order = take.getDate();

                System.out.println("当前拿到的商品是：" + order.getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
