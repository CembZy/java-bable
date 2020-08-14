package com.company.fork;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 遍历指定目录（含子目录）找寻指定类型文件
 */
public class ThreadDirs {


    /**
     * RecursiveAction是没有返回值的fork
     */
    public static class FindDirsFiles extends RecursiveAction {
        private File path;//当前任务需要搜寻的目录

        public FindDirsFiles(File path) {
            this.path = path;
        }

        @Override
        protected void compute() {

            File[] files = path.listFiles();

            List<FindDirsFiles> findDirsFiles = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        //如果是目录，加入到集合中
                        //这里采用多线程任务，不采用递归
                        findDirsFiles.add(new FindDirsFiles(file));

                    } else {
                        //遇到文件，检查
                        if (file.getAbsolutePath().endsWith("txt")) {
                            System.out.println("文件：" + file.getAbsolutePath());
                        }
                    }
                }

                if (findDirsFiles.size() > 0) {
                    //invokeAll(findDirsFiles) fork任务执行结果
                    for (FindDirsFiles subTask : invokeAll(findDirsFiles)) {
                        subTask.join();//等待子任务执行完成
                    }
                }
            }

        }
    }


    public static void main(String[] args) {
        try {
            // 用一个 ForkJoinPool 实例调度总任务
            ForkJoinPool pool = new ForkJoinPool();
            FindDirsFiles task = new FindDirsFiles(new File("F:/"));

            pool.execute(task);//异步调用

            System.out.println("Task is Running......");
            Thread.sleep(1);
            int otherWork = 0;
            for (int i = 0; i < 100; i++) {
                otherWork = otherWork + i;
            }
            System.out.println("Main Thread done sth......,otherWork=" + otherWork);
            task.join();//阻塞的方法
            System.out.println("Task end");
        } catch (Exception e) {
            e.printStackTrace();
        }


        List<String> strings = Arrays.asList(new String[]{});
    }

}
