package com.concurrent.ch02.pool;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试并发从连接池中获取连接
 */
public class TestConnection {

    //闭锁，模拟并发
    private static CountDownLatch countDownLatch;
    //连接池
    private static ConnectionPool connectionPool = new ConnectionPool(10);


    public static void main(String[] args) throws InterruptedException {
        //定义50个线程
        int threadNum = 50;
        //每个线程的操作数
        int count = 20;

        //获取到连接的次数
        AtomicInteger get = new AtomicInteger();
        //没有获取到连接的次数
        AtomicInteger none = new AtomicInteger();

        countDownLatch = new CountDownLatch(threadNum);

        for (int i = 0; i < threadNum; i++) {
            new Thread(new Worker(get, none, count), "Thread-" + i).start();
        }
        //让主线程停住，等待50个线程执行完
        countDownLatch.await();

        System.out.println("总线程数：" + threadNum * count);
        System.out.println("获取到连接：" + get.get());
        System.out.println("没有获取的连接：" + none.get());

    }

    //获取连接的工作线程
    private static class Worker implements Runnable {

        private AtomicInteger get;

        private AtomicInteger none;

        private int count;

        public Worker(AtomicInteger get, AtomicInteger none, int count) {
            this.none = none;
            this.get = get;
            this.count = count;
        }

        @Override
        public void run() {
            while (count > 0) {
                try {
                    Connection connection = connectionPool.getConnection(1000);
                    if (connection != null) {
                        //模拟数据库操作
                        connection.getAutoCommit();
                        //如果获取到连接，计数器加1
                        get.incrementAndGet();
                        connectionPool.relaseConnection(connection);
                    } else {
                        none.incrementAndGet();
                        System.out.println(Thread.currentThread().getName()
                                + "等待超时!");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    count--;
                }
            }
            countDownLatch.countDown();
        }
    }

}
