package com.company.query;

import java.util.concurrent.DelayQueue;

public class TestQuery {

    public static void main(String[] args) throws InterruptedException {
        DelayQueue<ItemsQuery<Order>> queue = new DelayQueue<>();
        new Thread(new Producter(queue)).start();
        new Thread(new Consumer(queue)).start();

        //每隔500毫秒，打印个数字
        for (int i = 1; i < 15; i++) {
            Thread.sleep(500);
            System.out.println(i * 500);
        }
    }
}
