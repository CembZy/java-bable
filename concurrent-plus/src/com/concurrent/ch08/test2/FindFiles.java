package com.concurrent.ch08.test2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;


public class FindFiles extends RecursiveAction {
    private File file;

    public FindFiles(File file) {
        this.file = file;
    }

    public static AtomicInteger num = new AtomicInteger(0);

    @Override
    protected void compute() {
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            List<FindFiles> list = new ArrayList<>();
            for (File file : files) {
                if (!file.isDirectory()) {
                    String name = file.getAbsolutePath();
                    if (name.endsWith("dll")) {
                        System.out.println(name);
                        num.incrementAndGet();
                    }
                } else {
                    FindFiles findFiles = new FindFiles(file);
                    invokeAll(findFiles);
                    findFiles.join();
//                    list.add(new FindFiles(file));
                }
            }
//            if (list.size() > 0) {
//                Collection<FindFiles> findFiles = invokeAll(list);
//                for (FindFiles findFiles1 : findFiles) {
//                    findFiles1.join();
//                }
//            }
        }
    }


    private static void testFork() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FindFiles findFiles = new FindFiles(new File("d://"));

        //execute方法是异步的
        forkJoinPool.execute(findFiles);

        //阻塞，等待ForkJoin执行完，主线程才往下执行
        findFiles.join();

        System.out.println("end....."+num.get());
    }


    public static void main(String[] args) {
        testFork();
    }
}
