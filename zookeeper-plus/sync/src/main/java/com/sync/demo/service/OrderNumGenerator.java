package com.sync.demo.service;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderNumGenerator {

    private OrderNumGenerator() {
    }

    //全局订单id
    public static int count = 0;

    //生成订单ID
    public static String getNumber() {
        SimpleDateFormat simpt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return simpt.format(new Date()) + "-" + (++count);
    }

}