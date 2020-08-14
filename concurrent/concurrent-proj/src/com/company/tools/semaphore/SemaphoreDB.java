package com.company.tools.semaphore;


import java.sql.Connection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * 模拟Semaphore实现数据库连接池
 */
public class SemaphoreDB {

    private final static int POOL_SIZE = 10;
    private final Semaphore useful, useless;//useful表示可用的数据库连接，useless表示已用的数据库连接

    public SemaphoreDB() {
        this.useful = new Semaphore(POOL_SIZE);
        this.useless = new Semaphore(0);
    }

    //存放数据库连接的容器
    private static LinkedList<Connection> pool = new LinkedList<Connection>();

    //初始化池
    static {
        for (int i = 0; i < POOL_SIZE; i++) {
            pool.addLast(SqlConn.getConnection());
        }
    }

    /*从池子拿连接*/
    public Connection getConnect() throws InterruptedException {
        //获取可用的数据库连接的许可证
        useful.acquire();

        Connection conn;
        synchronized (pool) {
            conn = pool.removeFirst();
        }
        //归还数据库连接的许可证
        useless.release();

        return conn;
    }


    /*归还连接*/
    public void returnConnect(Connection connection) throws InterruptedException {
        if(connection!=null) {
            System.out.println("当前有"+useful.getQueueLength()+"个线程等待数据库连接！！"
                    +"可用连接数:"+useful.availablePermits());

            //获取可用的数据库连接的许可证
            useless.acquire();

            synchronized (pool) {
                //归还数据库连接
                pool.addLast(connection);
            }

            //归还数据库连接的许可证
            useful.release();
        }
    }

}
