package com.company.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;


/**
 * 模拟Exchanger实现两个线程之间的数据的交换
 */
public class ExchangeTest {

    private static final Exchanger<List<String>> exchange = new Exchanger<List<String>>();

    public static void main(String[] args) {

        new Thread() {
            @Override
            public void run() {
                List<String> setA = new ArrayList<>();//存放数据的容器
                try {
                    setA.add("cb1");
                    setA.add("cb2");
                    setA = exchange.exchange(setA);//交换set
                    setA.forEach(it -> System.out.println(it + "..." + Thread.currentThread().getName()));

                } catch (InterruptedException e) {
                }
            }
        }.start();


        new Thread() {
            @Override
            public void run() {
                List<String> setB = new ArrayList<String>();//存放数据的容器
                try {
                    setB.add("cb11");
                    setB.add("cb22");
                    setB = exchange.exchange(setB);//交换set
                    setB.forEach(it -> System.out.println(it + "..." + Thread.currentThread().getName()));
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }

}
