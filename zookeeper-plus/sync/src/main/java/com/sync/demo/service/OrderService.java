package com.sync.demo.service;


import com.sync.demo.lock.Lock;
import com.sync.demo.lock.ZkLock;
import com.sync.demo.lock.ZkLock2;

//订单生成--模拟并发
public class OrderService implements Runnable {

    public Lock lock = new ZkLock();

    public void run() {
        try {
            lock.lock();

            String number = OrderNumGenerator.getNumber();
            System.out.println("获取订单号...." + number);
        } finally {

            lock.unlock();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new OrderService()).start();
        }
    }


}
