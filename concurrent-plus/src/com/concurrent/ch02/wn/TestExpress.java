package com.concurrent.ch02.wn;


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
            TestKmClass testKmClass = new TestKmClass();
            new Thread(testKmClass).start();
            TestSiteClass testSiteClass = new TestSiteClass();
            new Thread(testSiteClass).start();
        }
        SleepTools.second(1);
        express.changeSite();
    }

}
