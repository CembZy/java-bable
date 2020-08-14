package com.concurrent.ch08.test2;

import com.concurrent.SleepTools;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

public class AddArr {

    public static final int[] SRC = MakeArray.makeArray();
    public static final int POINT = SRC.length / 10;
    public static AtomicInteger num = new AtomicInteger(0);

    public static class SumTask extends RecursiveTask<Integer> {

        private int[] src;

        private int start;

        private int end;

        public SumTask(int[] src, int start, int end) {
            this.src = src;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start < POINT) {
                for (int i = start; i <= end; i++) {
                    num.addAndGet(src[i]);
//                    SleepTools.ms(1);
                }
                return num.get();
            } else {

                int mid = (end + start) / 2;

                ForkJoinTask left = new SumTask(src, start, mid);
                ForkJoinTask right = new SumTask(src, mid + 1, end);

                invokeAll(left, right);
                return ((SumTask) left).join() + ((SumTask) right).join();
            }
        }
    }

    private static void testForkJoin() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        SumTask sumTask = new SumTask(SRC, 0, SRC.length - 1);
        long startTime = System.currentTimeMillis();
        forkJoinPool.invoke(sumTask);
        long endTime = System.currentTimeMillis();

        System.out.println("采用forkjoin执行结果是：" + sumTask.join() + "---------用时：" + (endTime - startTime));
    }

    private static void testFor() {
        long startTime = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < SRC.length; i++) {
            count += SRC[i];
//            SleepTools.ms(1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("采用for循环执行结果是：" + count + "---------用时：" + (endTime - startTime));
    }

    public static void main(String[] args) {
        testForkJoin();
        testFor();
    }


}
