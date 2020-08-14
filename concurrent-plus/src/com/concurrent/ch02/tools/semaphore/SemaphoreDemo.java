package com.concurrent.ch02.tools.semaphore;


import java.sql.Connection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * 用Semaphore信号量，模拟数据库连接池，Semaphore信号量是可以控制多个线程访问同一资源
 */
public class SemaphoreDemo {

    //线程池
    private static LinkedList<Connection> list = new LinkedList<>();

    //线程池的大小
    private static final int POOL_SIZE = 10;

    //useful：可以使用的数据库连接数   userless：不能使用的数据库连接数
    private static Semaphore useful, userless;

    public SemaphoreDemo() {
        useful = new Semaphore(POOL_SIZE);
        userless = new Semaphore(0);
    }

    //初始化线程池
    static {
        for (int i = 0; i < POOL_SIZE; i++) {
            list.add(new ConnectionImpl());
        }
    }


    //获取数据库连接
    public static Connection getConnection() {
        Connection connection = null;
        try {
            //可用的信号获取许可证，阻塞方法，只有当前线程获取到许可证才能放行
            useful.acquire();

            synchronized (list) {
                //取出连接池中的第一个
                connection = list.removeFirst();
            }
            //不可用的信号释放许可证，将其返回到信号量
            userless.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return connection;
    }


    //释放连接
    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            System.out.println("当前有" + useful.getQueueLength() + "个线程等待数据库连接！！"
                    + "可用连接数:" + useful.availablePermits());
            try {
                //不可用的信号获取许可证
                userless.acquire();
                //放回池
                synchronized (list) {
                    list.addLast(connection);
                }
                //可用的信号释放许可证
                useful.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
