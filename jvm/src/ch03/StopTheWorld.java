package ch03;


import java.util.LinkedList;
import java.util.List;

/**
 * 演示stoptheworld现象，即是在GC发生的过程中，会发生
 * -Xmx300m -Xms300m -XX:+UseSerialGC -XX:+PrintGCDetails
 */
public class StopTheWorld {

    /*不停往list中填充数据*/
    public static class FillListThread implements Runnable {
        List<Byte[]> list = new LinkedList<>();

        @Override
        public void run() {
            try {
                for (; ; ) {

                    if (list.size() * 512 / 1024 / 1024 >= 990) {
                        list.clear();
                        System.out.println("list is clear");
                    }

                    list.add(new Byte[512]);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    /*每100ms定时打印*/
    public static class TimerThread implements Runnable {

        public final static long startTime = System.currentTimeMillis();

        @Override
        public void run() {
            try {
                while (true) {
                    long t = System.currentTimeMillis() - startTime;
                    System.out.println(t / 1000 + "." + t % 1000);
                    Thread.sleep(100);
                }

            } catch (Exception e) {
            }
        }
    }


    public static void main(String[] args) {
        Runnable fillListThread = new FillListThread();
        Runnable timerThread = new TimerThread();
        new Thread(fillListThread).start();
        new Thread(timerThread).start();
    }


}
