package com.concurrent.ch07.fw1;

import com.concurrent.SleepTools;
import com.concurrent.ch07.fw1.po.Result;
import com.concurrent.ch07.fw1.util.PendingJobPool;

import java.util.List;
import java.util.Random;

/**
 * 类说明：模拟一个应用程序，提交工作和任务，并查询任务进度
 */
public class AppTest {

    //任务名称
    private final static String JOB_NAME = "计算数值";

    //任务执行的次数
    private final static int JOB_LENGTH = 1000;

    //查询任务进度的线程
    private static class QueryResult implements Runnable {

        private PendingJobPool pool;

        public QueryResult(PendingJobPool pool) {
            super();
            this.pool = pool;
        }

        @Override
        public void run() {
            int i = 0;//查询次数

            //循环350次，去拿任务执行的结果
            while (i < 350) {
                //获取任务执行的结果信息
                List<Result<String>> taskDetail = pool.getResult(JOB_NAME);

                if (!taskDetail.isEmpty()) {
                    //任务执行的进度
                    System.out.println(pool.getJobProgess(JOB_NAME));
                    System.out.println(taskDetail);
                }
                SleepTools.ms(100);
                i++;
            }
        }

    }

    public static void main(String[] args) {
        //自定义的任务执行逻辑
        MyTask myTask = new MyTask();

        //拿到框架的实例
        PendingJobPool pool = PendingJobPool.newInstance();

        //注册job，设置任务完成后过期时间为5S
        pool.registJobs(JOB_NAME, JOB_LENGTH, 1000 * 5, myTask);
        Random r = new Random();
        for (int i = 0; i < JOB_LENGTH; i++) {
            //依次推入Task，交给线程池执行，传入随机的整形数据作为任务数据
            pool.submitJob(JOB_NAME, r.nextInt(1000));
        }

        //开启查询任务执行结果的线程
        Thread t = new Thread(new QueryResult(pool));
        t.start();
    }
}
