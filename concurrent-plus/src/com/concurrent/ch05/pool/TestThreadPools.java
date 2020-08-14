package com.concurrent.ch05.pool;


import com.concurrent.SleepTools;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 测试使用线程池
 */
public class TestThreadPools {

    //工作线程
    static class Worker implements Runnable {
        private String taskName;
        private Random r = new Random();

        public Worker(String taskName) {
            this.taskName = taskName;
        }

        public String getName() {
            return taskName;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()
                    + " 执行Worker任务.....: " + taskName);
            SleepTools.second(1);
        }
    }

    static class CallWorker implements Callable<String> {

        private String taskName;
        private Random r = new Random();

        public CallWorker(String taskName) {
            this.taskName = taskName;
        }

        public String getName() {
            return taskName;
        }

        @Override
        public String call() throws Exception {
            System.out.println(Thread.currentThread().getName()
                    + " 执行CallWorker任务.....: " + taskName);
            return Thread.currentThread().getName() + ":" + r.nextInt(100) * 5;
        }

    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //自定义线程池参数
        ExecutorService pool = new ThreadPoolExecutor(2, 10, 4, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10), new ThreadPoolExecutor.DiscardPolicy());

//        ExecutorService pool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 6; i++) {
            pool.execute(new Worker("任务" + i));
        }

        for (int i = 0; i < 6; i++) {
            CallWorker callWorker = new CallWorker("任务" + i);
            Future<String> submit = pool.submit(callWorker);
            System.out.println("该任务的结果：" + submit.get());
        }
        pool.shutdown();

        Runtime.getRuntime().availableProcessors();
    }


}
