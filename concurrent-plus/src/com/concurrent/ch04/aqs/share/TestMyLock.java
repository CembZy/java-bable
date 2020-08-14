package com.concurrent.ch04.aqs.share;

import com.concurrent.SleepTools;
import java.util.concurrent.locks.Lock;


/**
 * 类说明：测试自定义锁
 */
public class TestMyLock {
    public void test() {

        //使用自定义的共享锁
        final Lock lock = new SelfShareSync(3);

        class Worker extends Thread {
            public void run() {
                for (; ; ) {
                    lock.lock();
                    try {
                        SleepTools.second(1);
                        System.out.println("当前线程:" + Thread.currentThread().getName());
                        SleepTools.second(1);
                    } finally {
                        lock.unlock();
                    }
                    SleepTools.second(2);
                }
            }
        }
        // 启动10个子线程
        for (int i = 0; i < 10; i++) {
            Worker w = new Worker();
            w.setDaemon(true);
            w.start();
        }
        // 主线程每隔1秒换行
        for (int i = 0; i < 10; i++) {
            SleepTools.second(1);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        TestMyLock testMyLock = new TestMyLock();
        testMyLock.test();
    }
}
