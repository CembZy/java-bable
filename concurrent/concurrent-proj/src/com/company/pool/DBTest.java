package com.company.pool;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试获取DB连接
 */
public class DBTest {
    //获取10个连接
    static DBPool pool = new DBPool(10);

    // 控制器:控制main线程将会等待所有Woker结束后才能继续执行
    static CountDownLatch end;

    //用于获取连接的线程
    static class Worker implements Runnable {
        //要去执行获取连接的操作次数
        int count;
        //AtomicInteger是一个计数器，这里got是记录获取到连接的线程
        AtomicInteger got;
        AtomicInteger notGot;

        public Worker(int count, AtomicInteger got,
                      AtomicInteger notGot) {
            this.count = count;
            this.got = got;
            this.notGot = notGot;
        }

        public void run() {
            while (count > 0) {
                try {
                    // 从线程池中获取连接，如果1000ms内无法获取到，将会返回null
                    // 分别统计连接获取的数量got和未获取到的数量notGot
                    Connection connection = pool.getConnection(1000);
                    if (connection != null) {
                        try {
                            //模拟数据库执行，不用关心
                            connection.createStatement();
                            connection.commit();
                        } finally {
                            //将获取到的线程释放
                            pool.releaseConn(connection);

                            //增加计数器获取到线程的次数
                            got.incrementAndGet();
                        }
                    } else {
                        notGot.incrementAndGet();
                        System.out.println(Thread.currentThread().getName()
                                + "等待超时!");
                    }
                } catch (Exception ex) {
                } finally {
                    count--;
                }
            }
            //结束本次线程，直到所有线程都结束了，主线程才能继续执行
            end.countDown();
        }
    }


    public static void main(String[] args) throws Exception {
        // 线程数量
        int threadCount = 50;
        end = new CountDownLatch(threadCount);
        int count = 20;//每个线程的操作次数
        AtomicInteger got = new AtomicInteger();//计数器：统计可以拿到连接的线程
        AtomicInteger notGot = new AtomicInteger();//计数器：统计没有拿到连接的线程
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new Worker(count, got, notGot),
                    "worker_"+i);
            thread.start();
        }
        end.await();// main线程在此处等待
        System.out.println("总共尝试了: " + (threadCount * count));
        System.out.println("拿到连接的次数：  " + got);
        System.out.println("没能连接的次数： " + notGot);
    }

}
