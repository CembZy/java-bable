package com.concurrent.ch08.test1;

import com.concurrent.SleepTools;

public class Test2 {

    static int num = 0;

    public static class Sh extends Thread {


        @Override
        public void run() {
            num++;
            SleepTools.ms(10);
            System.out.println(num);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new Sh()).start();
        }


    }

}
