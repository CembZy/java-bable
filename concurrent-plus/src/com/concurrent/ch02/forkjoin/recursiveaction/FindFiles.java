package com.concurrent.ch02.forkjoin.recursiveaction;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用无返回值的ForkJoin，同时是异步的，遍历目录，搜寻目录下的所有文件
 */
public class FindFiles extends RecursiveAction {

    //要搜寻的目录
    private File dir;
    public static AtomicInteger num = new AtomicInteger(0);

    public FindFiles(File dir) {
        this.dir = dir;
    }

    @Override
    protected void compute() {
        File[] files = dir.listFiles();
        if (files != null) {
            List<FindFiles> list = new ArrayList<>();
            for (File file : files) {
                //如果是目录，就需要分割任务，交给ForkJoinPool去执行，因为任务数目不确定，所以需要定义一个集合
                if (file.isDirectory()) {
                    FindFiles findFiles = new FindFiles(file);
                    list.add(findFiles);


                    //不是目录，是文件就执行自己的逻辑
                } else {
                    if (file.getAbsolutePath().endsWith("dll")) {
                        System.out.println(file.getAbsolutePath());
                        num.incrementAndGet();
                    }
                }
            }
            //如果任务
            if (list.size() > 0) {
                Collection<FindFiles> findFiles = invokeAll(list);
                for (FindFiles findFiles1 : findFiles) {
                    //等待所有的任务执行完成
                    findFiles1.join();

                    //所有的任务都执行完了才会执行
                    System.out.println(Thread.currentThread().getName() + "....join end..");
                }
            }
        }
    }


    private static void testFork() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FindFiles findFiles = new FindFiles(new File("d://"));

        //execute方法是异步的
        forkJoinPool.execute(findFiles);

        //阻塞，等待ForkJoin执行完，主线程才往下执行
        findFiles.join();

        System.out.println("end....." + num.get());
    }


    public static void main(String[] args) {
        testFork();
    }

}
