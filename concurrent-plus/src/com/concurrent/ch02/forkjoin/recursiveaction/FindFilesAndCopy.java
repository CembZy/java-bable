package io.renren.modules;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 使用无返回值的ForkJoin，同时是异步的，遍历目录，搜寻目录下的所有符合条件的文件病复制到指定目录
 */
public class FindFilesAndCopy extends RecursiveAction {

    //要搜寻的目录
    private File dir;

    public FindFilesAndCopy(File dir) {
        this.dir = dir;
    }

    @Override
    protected void compute() {
        File[] files = dir.listFiles();
        if (files != null) {
            List<FindFilesAndCopy> list = new ArrayList<>();
            for (File file : files) {
                //如果是目录，就需要分割任务，交给ForkJoinPool去执行，因为任务数目不确定，所以需要定义一个集合
                if (file.isDirectory()) {
                    FindFilesAndCopy findFiles = new FindFilesAndCopy(file);
                    list.add(findFiles);


                    //不是目录，是文件就执行自己的逻辑
                } else {
                    if (file.getAbsolutePath().endsWith(".docx")) {
                        System.out.println(file.getAbsolutePath());
                        try {
                            copyFile(file.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            //如果任务
            if (list.size() > 0) {
                Collection<FindFilesAndCopy> findFiles = invokeAll(list);
                for (FindFilesAndCopy findFiles1 : findFiles) {
                    //等待所有的任务执行完成
                    findFiles1.join();

                    //所有的任务都执行完了才会执行
                    System.out.println(Thread.currentThread().getName() + "....join end..");
                }
            }
        }
    }


    /**
     * 复制文件到指定目录
     *
     * @param sourcePath
     * @throws IOException
     */
    public static void copyFile(String sourcePath) throws IOException {
        File src = new File(sourcePath);
        String targetPath = "C:\\Users\\86199\\Desktop\\clean\\" + src.getName();
        File dest = new File(targetPath);
        // 判断拼接成的路径是否存在
        if (!dest.exists()) {
            dest.createNewFile();
        }
        // 开始复制文件
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dest);
        byte[] b = new byte[1024];
        int len;
        while ((len = fis.read(b)) != -1) {
            fos.write(b, 0, len);
        }
        fos.close();
        fis.close();

    }


    private static void testFork() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FindFilesAndCopy findFiles = new FindFilesAndCopy(new File("D:\\clean\\xiangxue\\VIP"));

        //execute方法是异步的
        forkJoinPool.execute(findFiles);

        //阻塞，等待ForkJoin执行完，主线程才往下执行
        findFiles.join();

        System.out.println("end.....");
    }


    public static void main(String[] args) {
        testFork();
    }

}
