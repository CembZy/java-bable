package com.concurrent.ch02.pool;


import java.sql.Connection;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 数据库连接池
 */
public class ConnectionPool {


    //定义一个链表，装数据库连接
    private LinkedList<Connection> pool = new LinkedList<>();

    //连接池初始大小
    private final static int INITAL_SIZE = 10;

    //初始化连接池
    public ConnectionPool(int initalValue) {
        if (initalValue > 0) {
            for (int i = 0; i < initalValue; i++) {
                pool.add(ConnectionImpl.getConnection());
            }
        } else {
            for (int i = 0; i < INITAL_SIZE; i++) {
                pool.add(ConnectionImpl.getConnection());
            }
        }
    }


    //获取数据库连接，有超时时间
    public Connection getConnection(long express) throws InterruptedException {
        //因为是多个线程竞争拿池里的连接，所以需要对池进行加锁
        synchronized (pool) {
            //没有设置超时时间
            if (express < 0) {
                while (pool.isEmpty()) {
                    pool.wait();
                }
                //取第一个
                return pool.removeFirst();
            } else {
                //超时时刻：当前时间+超时时间
                long expressTime = System.currentTimeMillis() + express;
                //剩余的超时时间（初始值是最初的超时时间）
                long remain = express;
                while (pool.isEmpty() && remain > 0) {
                    //等待一定的超时时间，如果等待完了，就会拿到执行权，再继续往下执行
                    pool.wait(remain);
                    remain = expressTime - System.currentTimeMillis();
                }

                //因为如果是被唤醒的，可能是超时了或者池中有连接了两种情况，所以需要判断池中是否有数据
                Connection connection = null;
                if (!pool.isEmpty()) {
                    connection = pool.removeFirst();
                }
                return connection;
            }
        }
    }


    //释放连接
    public void relaseConnection(Connection connection) {
        if (connection != null) {
            synchronized (pool) {
                //从链表末尾放入
                pool.addLast(connection);
                //唤醒所有等待拿连接的线程
                pool.notifyAll();
            }
        }
    }


    public static class test implements Callable<Void> {

        @Override
        public Void call() throws Exception {
            System.out.println(Thread.currentThread().getName() + "....");
            return null;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test test = new test();
        FutureTask<Void> futureTask = new FutureTask<>(test);
        Thread thread = new Thread(futureTask);
        thread.start();
        thread.join();
        System.out.println(Thread.currentThread().getName() + "....");
    }


}
