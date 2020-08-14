package com.concurrent.ch02.forkjoin.recursivetask;


import com.concurrent.SleepTools;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 使用有返回值的ForkJoin，同时是同步的，累加数组
 */
public class SumArray {

    //产生一个随机数组
    private static int[] array = MakeArray.makeArray();
    //阈值
    private final static int POINT = MakeArray.ARRAY_LENGTH / 10;


    //有返回值的任务
    private static class SumTask extends RecursiveTask<Integer> {

        //要累加的源数组
        private int[] src;
        //开始角标
        private int startIndex;
        //结束角标
        private int endIndex;

        public SumTask(int[] src, int startIndex, int endIndex) {
            this.src = src;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        //实现具体的累加逻辑和任务分割逻辑
        @Override
        protected Integer compute() {
            //不满足阈值的时候，这里面的逻辑也是当满足阈值的时候，递归执行的逻辑
            if (endIndex - startIndex < POINT) {
                int count = 0;
                for (int i = startIndex; i <= endIndex; i++) {
                    count += src[i];
                    SleepTools.ms(1);
                }
                return count;

                //满足阈值的时候，需要分割任务，然后交给forkjoinpool去执行任务
            } else {

                //当需要分割的时候，采用折中法进行分割
                //startIndex.......mid.......endIndex
                int mid = (startIndex + endIndex) / 2;

                //左任务
                SumTask leftTask = new SumTask(src, startIndex, mid);
                //右任务
                SumTask rigthTask = new SumTask(src, mid + 1, endIndex);

                //交给forkjoinpool去执行任务
                invokeAll(leftTask, rigthTask);

                //将执行结果返回
                return leftTask.join() + rigthTask.join();
            }
        }
    }


    private static void testForkJoin() {
        //创建ForkJoinPool池
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        long startTime = System.currentTimeMillis();
        SumTask task = new SumTask(array, 0, array.length - 1);

        //这个方法是阻塞的，是同步的
        forkJoinPool.invoke(task);

        long endTime = System.currentTimeMillis();
        System.out.println("采用forkjoin执行结果是：" + task.join() + "---------用时：" + (endTime - startTime));
    }

    private static void testFor() {
        long startTime = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            count += array[i];
            SleepTools.ms(1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("采用for循环执行结果是：" + count + "---------用时：" + (endTime - startTime));
    }


    public static void main(String[] args) {
        testForkJoin();
        testFor();
    }


}
