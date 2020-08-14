package com.company.pool;


import java.sql.Connection;
import java.util.LinkedList;

/**
 * 模拟sql连接池
 */
public class DBPool {

    //数据库的容量
    LinkedList<Connection> pool = new LinkedList<Connection>();

    //初始化判断需要获得的数据库数量
    public DBPool(int initalSize) {
        if (initalSize > 0) {
            for (int i = 0; i < initalSize; i++) {
                pool.addFirst(SqlConn.getConnection());
            }
        }
    }

    //在mills时间内还拿不到数据库连接，返回一个null
    public Connection getConnection(long mills) throws InterruptedException {
        //锁住pool
        synchronized (pool) {

            //mills小于0，表示没有超时时间
            if (mills < 0) {
                //判断是否拿到了连接，如果没有拿到，就等待
                while (pool.isEmpty()) {
                    pool.wait();
                }
                //直到拿到连接，返回
                return pool.removeFirst();
            } else {
                //最初的超时时间
                long overtime = System.currentTimeMillis() + mills;
                long remain = mills;
                //一直没有拿到连接，或者还在规定的超时时间之内，就继续等待
                while (pool.isEmpty() && remain > 0) {
                    pool.wait(remain);
                    remain = overtime - System.currentTimeMillis();
                }
                Connection result = null;
                if (!pool.isEmpty()) {
                    result = pool.removeFirst();
                }
                return result;
            }
        }
    }


    //放回数据库连接
    public void releaseConn(Connection conn) {
        if (conn != null) {
            synchronized (pool) {
                pool.addLast(conn);

                //唤醒所有在等待获取连接的线程，告诉他们有连接了。
                pool.notifyAll();
            }
        }
    }


}
