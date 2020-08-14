package com.concurrent.ch05.comps;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类说明：使用CompletionService测试任务返回顺序
 */
public class CompletionCase {
    private final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private final int TOTAL_TASK = Runtime.getRuntime().availableProcessors() * 10;

    // 方法一，自己写集合来实现获取线程池中任务的返回结果
    public void testByQueue() throws Exception {
        long start = System.currentTimeMillis();

        //定义一个线程池
        ExecutorService pool = new ThreadPoolExecutor(POOL_SIZE, TOTAL_TASK, 1, TimeUnit.SECONDS
                , new ArrayBlockingQueue<Runnable>(TOTAL_TASK), new ThreadPoolExecutor.DiscardPolicy());

        //存放 线程池中执行的任务
        BlockingQueue<Future<Integer>> tasks = new LinkedBlockingDeque<>();

        //总耗时，因为这个count是栈封闭的，所以可以不用使用原子操作类，也是线程安全的
        AtomicInteger count = new AtomicInteger(0);

        for (int i = 0; i < TOTAL_TASK; i++) {
            Future<Integer> submit = pool.submit(new WorkTask("任务." + i));
            //将当前任务的结果放入阻塞队列
            tasks.put(submit);
        }


        for (int i = 0; i < TOTAL_TASK; i++) {
            //从阻塞队列中拿去结果
            Integer integer = tasks.take().get();
            System.out.println("当前任务耗时...." + integer);
            count.addAndGet(integer);
        }


        System.out.println("总任务耗时：..." + count + "..........执行耗时:...." + (System.currentTimeMillis() - start));
    }

    // 方法二，通过CompletionService来实现获取线程池中任务的返回结果
    public void testByCompletion() throws Exception {
        long start = System.currentTimeMillis();

        //定义一个线程池
        ExecutorService pool = new ThreadPoolExecutor(POOL_SIZE, TOTAL_TASK, 1, TimeUnit.SECONDS
                , new ArrayBlockingQueue<Runnable>(TOTAL_TASK), new ThreadPoolExecutor.DiscardPolicy());
        //存放 线程池中执行的任务，内部也是一个阻塞队列
        CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(pool);

        //总耗时
        AtomicInteger count = new AtomicInteger(0);

        for (int i = 0; i < TOTAL_TASK; i++) {
            completionService.submit(new WorkTask("任务." + i));
        }


        for (int i = 0; i < TOTAL_TASK; i++) {
            //从CompletionService中拿去结果
            Integer integer = completionService.take().get();
            System.out.println("当前任务耗时...." + integer);
            count.addAndGet(integer);
        }


        System.out.println("总任务耗时：..." + count + "..........执行耗时:...." + (System.currentTimeMillis() - start));
    }

    public static void main(String[] args) throws Exception {
        CompletionCase t = new CompletionCase();
        t.testByQueue();
        t.testByCompletion();
    }
}
