package com.company.taskfw;


import com.company.query.ItemsQuery;

import java.util.concurrent.DelayQueue;

/**
 * 类说明：过期任务处理类
 * 任务完成后,在一定的时间供查询，之后为释放资源节约内存，需要定期处理过期的任务
 */
public class CheckJobProcesser {
    //存放已完成任务等待过期的队列
    private static DelayQueue<ItemsQuery<String>> queue
            = new DelayQueue<ItemsQuery<String>>();

    //类加载的单例模式------
    private CheckJobProcesser() {
    }

    private static class ProcesserHolder {
        public static CheckJobProcesser processer = new CheckJobProcesser();
    }

    public static CheckJobProcesser getInstance() {
        return ProcesserHolder.processer;
    }
    //单例模式------

    //处理队列中到期任务的线程
    private static class FetchJob implements Runnable {
        @Override
        public void run() {
            for (; ; ) {
                try {
                    //拿到已经处于过期队列中的任务
                    ItemsQuery<String> item = queue.take();
                    String jobName = item.getItem();
                    //从任务容器中移除
                    PendingJobPool.getMap().remove(jobName);
                    System.out.println(jobName + "已经过期，并且从过期容器中移除!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*任务完成后，放入队列，经过expireTime时间后，从整个框架中移除*/
    public void putJob(String jobName, long expireTime) {
        ItemsQuery<String> item = new ItemsQuery<String>(jobName, expireTime);
        queue.offer(item);
        System.out.println("任务" + jobName + "已经放入了过期检查缓存，过期时长：" + expireTime);
    }

    //在过期任务处理类被加载的时候，同时开启处理过期任务的线程
    static {
        Thread thread = new Thread(new FetchJob());
        //启动守护线程
        thread.setDaemon(true);
        thread.start();
        System.out.println("开启任务过期检查守护线程................");
    }


}
