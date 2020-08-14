package com.concurrent.ch02.tools;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Exchanger;

/**
 * Exchanger测试，Exchanger是两个仅有两个线程之间的数据交换
 */
public class ExchangerDemo {

    private static Exchanger<Set<String>> exchanger = new Exchanger<>();

    private static class ExchangerClassO extends Thread {
        private Set<String> set;

        public ExchangerClassO(Set<String> set) {
            this.set = set;
        }

        @Override
        public void run() {
            try {
                exchange(set);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private static class ExchangerClassT extends Thread {
        private Set<String> set;

        public ExchangerClassT(Set<String> set) {
            this.set = set;
        }

        @Override
        public void run() {
            try {
                exchange(set);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void exchange(Set<String> set) throws InterruptedException {
        //交换数据，阻塞
        System.out.println("线程：" + Thread.currentThread().getName() + "交换前得值....");
        for (String s : set) {
            System.out.println(s);
        }
        exchanger.exchange(set);
        System.out.println("线程：" + Thread.currentThread().getName() + "交换后得值....");
        for (String s : set) {
            System.out.println(s);
        }
    }


    public static void main(String[] args) {
        Set<String> setA = new HashSet<>();
        Set<String> setB = new HashSet<>();
        setA.add("a1");
        setA.add("b1");
        setA.add("c1");

        setB.add("a2");
        setB.add("b2");
        setB.add("c2");

        new ExchangerClassO(setA).start();
        new ExchangerClassT(setB).start();


    }

}
