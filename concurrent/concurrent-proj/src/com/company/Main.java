package com.company;

import com.company.thread.ExThread;
import com.company.thread.ImplCallable;
import com.company.thread.ImplRunnble;
import com.company.thread.InterruptEx;
import java.util.*;
import java.util.concurrent.FutureTask;

/**
 * java并发编程
 */
public class Main {


    public static void main(String[] args) throws Exception {

    }

    //去重List<Map<String,Object>>
    private static void hashSetTest() {
        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();

        Set<Set<String>> keysSet = new HashSet<Set<String>>();

        List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : list1) {
            Set<String> keys = map.keySet();
            int size1 = keysSet.size();
            keysSet.add(keys);
            int size2 = keysSet.size();
            if (size2 == size1 + 1) {
                list2.add(map);
            }
        }
    }


    //测试三种方式实现多线程
    public static void test1() {

        //1,Thread
        ExThread exThread = new ExThread();
        new Thread(exThread).start();

        //2,Runnable
        ImplRunnble implRunnble = new ImplRunnble();
        new Thread(implRunnble).start();

        //2,Callable
        ImplCallable implCallable = new ImplCallable();
        //通过FutureTask，将Callable转换为Runnable类型
        FutureTask<ImplCallable> futureTask = new FutureTask<ImplCallable>(implCallable);
        new Thread(futureTask).start();
    }


    //测试中断线程
    public static void test2() throws InterruptedException {
        ExThread exThread = new ExThread();
        Thread thread = new Thread(exThread);
        thread.start();

        //主线程休眠一会
        Thread.sleep(20);

        //中断线程
        thread.interrupt();
    }


    //测试InterruptedException对中断标志位的影响
    public static void test3() throws InterruptedException {
        InterruptEx target = new InterruptEx();
        Thread thread = new Thread(target);
        thread.start();

        //主线程休眠一会
        Thread.sleep(500);

        //中断线程
        thread.interrupt();
    }

}
