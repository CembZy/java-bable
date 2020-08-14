package ch01;

import java.util.LinkedList;
import java.util.List;

/**
 * 测试内存溢出。
 * -Xmx5m -Xms5m -XX:+PrintGC
 */
public class OOM {


    public static void main(String[] args) {

        //1，java.lang.OutOfMemoryError: GC overhead limit exceeded，GC的次数超出限制
        //出现这种异常，可能是代码中存在某个循环中，一直在new对象，最终把堆撑爆了
//        List list = new LinkedList();
//        for(;;){
//            list.add(new Object());
//        }


        //2，java.lang.OutOfMemoryError: Java heap space，堆溢出
        String[] strings = new String[1000000000];
    }
}
