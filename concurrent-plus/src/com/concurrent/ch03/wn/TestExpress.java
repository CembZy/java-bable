package com.concurrent.ch03.wn;


import com.concurrent.SleepTools;

/**
 * 测试等待和唤醒
 */
public class TestExpress {

    private static Express express = new Express(0, Express.CITY);

    private static class TestKmClass implements Runnable {

        @Override
        public void run() {
            express.km();
        }
    }


    private static class TestSiteClass implements Runnable {

        @Override
        public void run() {
            express.site();
        }
    }


    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(new TestKmClass()).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(new TestSiteClass()).start();
        }
        SleepTools.second(1);
        express.changeSite();
    }

}
