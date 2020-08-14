package com.concurrent.ch05.delaydb;

import java.util.concurrent.DelayQueue;

/**
 * 类说明：测试延时队列
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {

        DelayQueue<Item<Order>> queue = new DelayQueue<>();
        new Thread(new PutItems(queue)).start();
        new Thread(new GetItems(queue)).start();

        //每隔500毫秒，打印个数字
        for (int i = 1; i < 15; i++) {
            Thread.sleep(500);
            System.out.println(i * 500);
        }
    }
}
