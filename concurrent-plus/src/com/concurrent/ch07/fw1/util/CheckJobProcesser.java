package com.concurrent.ch07.fw1.util;


import com.concurrent.ch05.delaydb.Item;

import java.util.concurrent.DelayQueue;

/**
 * 检验完成的任务是否过期
 */
public class CheckJobProcesser {

    //延时过期队列
    private static DelayQueue<Item<String>> delayQueue = new DelayQueue<>();

    private CheckJobProcesser() {

    }

    private static class CheckJobHodler {
        private static CheckJobProcesser checkJobProcesser = new CheckJobProcesser();
    }

    public static CheckJobProcesser newInstance() {
        return CheckJobHodler.checkJobProcesser;
    }

    //启动任务线程
    static {
        Thread thread = new Thread(new FetchJob());
        //将这个处理任务的线程设置为守护线程，就不用去管理这个线程的结束了
        thread.setDaemon(true);
        thread.start();
        System.out.println("过期任务线程启动......");
    }


    //处理过期任务的线程
    private static class FetchJob implements Runnable {

        @Override
        public void run() {
            try {
                //从队列中拿到已经过期的任务
                Item<String> take = delayQueue.take();
                String jobName = take.getDate();

                //从任务注册集合中删除已经过期的任务
                PendingJobPool.getJobs().remove(jobName);
                System.out.println(jobName + "已经过期，并且从注册列表中删除!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //将已完成的任务放入延时队列
    public void putJob(String jobName, long expireTime) {
        Item<String> item = new Item<>(expireTime, jobName);

        //放入延时过期队列
        delayQueue.offer(item);
        System.out.println("任务：" + jobName + "已经放入了过期检查缓存，过期时长：" + expireTime);
    }


}
