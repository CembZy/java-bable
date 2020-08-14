package com.company.fork;


import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 模拟多线程下，计算一个数组中元素的总和的时长
 */
public class ThreadSum {


    /**
     * RecursiveTask是一个 分而治之 算法思想的多线程任务执行工具 这里实现的是同步有返回值的用法，继承RecursiveTask
     */
    public static class SumTask extends RecursiveTask<Integer> {

        private final static int THRESHOLD = MakeArray.ARRAY_LENGTH / 10; //自定义的下标阈值
        private int[] src; //表示我们要实际统计的数组
        private int fromIndex;//开始统计的下标
        private int toIndex;//统计到哪里结束的下标

        public SumTask(int[] src, int fromIndex, int toIndex) {
            this.src = src;
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        }

        @Override
        protected Integer compute() {
            //挡在阈值范围内，则不切分
            if (toIndex - fromIndex < THRESHOLD) {
                int count = 0;
                for (int i = fromIndex; i < toIndex; i++) {
                    count = count + src[i];
                }
                return count;
            } else { //超出阈值，则切分
                int mid = (fromIndex + toIndex) / 2;
                SumTask left = new SumTask(src, fromIndex, mid);
                SumTask right = new SumTask(src, mid + 1, toIndex);
                invokeAll(left, right);
                return left.join() + right.join();
            }
        }
    }


    public static void main(String[] args) {

        ForkJoinPool pool = new ForkJoinPool();
        int[] src = MakeArray.makeArray();

        SumTask innerFind = new SumTask(src, 0, src.length - 1);

        long start = System.currentTimeMillis();

        //同步
        pool.invoke(innerFind);
        System.out.println("Task is Running.....");

        System.out.println("The count is " + innerFind.join()
                + " spend time:" + (System.currentTimeMillis() - start) + "ms");

    }

}
