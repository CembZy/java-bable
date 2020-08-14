package com.enjoy.controller;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;


@Controller
public class AsyncOrderController {

    //第一种方式，其实相当于我们说的tomcat的线程1，来处理用户请求，并将请求的操作放到Queue队列里
    @ResponseBody
    @RequestMapping("/createOrder")
    public DeferredResult<Object> createOrder() throws Exception {
        DeferredResult<Object> deferredResult = new DeferredResult<Object>((long) 5000, "create fail...");

        JamesDeferredQueue.save(deferredResult);

        //需要开启线程
        Callable<Object> callable = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(5000);
                String order = UUID.randomUUID().toString();//模拟从订单服务获取的订单信息（免调接口）
                DeferredResult<Object> deferredResult = JamesDeferredQueue.get();
                deferredResult.setResult(order);
                return order;
            }
        };
        FutureTask futureTask = new FutureTask(callable);
        new Thread(futureTask).start();
        return deferredResult;
    }

    //第二种方式
    @RequestMapping("/createOrder2")
    @ResponseBody
    public WebAsyncTask<String> webAsyncHandle() {

        //不需要开启线程
        Callable<String> asyncTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                return "Callableresult";//超时之后不会执行返回操作，但是return之前的能够执行完成
            }
        };
        WebAsyncTask<String> webAsyncTask = new WebAsyncTask<String>(5000, asyncTask);
        return webAsyncTask;
    }


    ////其实相当于我们说的tomcat的线程N，来处理用户请求，并将请求的操作放到Queue队列里
    @ResponseBody
    @RequestMapping("/create")
    public String create() {
        //创建订单（按真实操作应该是从订单服务取，这里直接返回）
        String order = UUID.randomUUID().toString();//模拟从订单服务获取的订单信息（免调接口）
        DeferredResult<Object> deferredResult = JamesDeferredQueue.get();
        deferredResult.setResult(order);
        return "create success, orderId == " + order;
    }


    @ResponseBody
    @RequestMapping("/order01")
    public Callable<String> order01() {
        System.out.println("主线程开始..." + Thread.currentThread() + "==>" + System.currentTimeMillis());

        //异步处理
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("副线程开始..." + Thread.currentThread() + "==>" + System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println("副线程开始..." + Thread.currentThread() + "==>" + System.currentTimeMillis());
                return "order buy successful........";
            }
        };

        System.out.println("主线程结束..." + Thread.currentThread() + "==>" + System.currentTimeMillis());
        return callable;
    }

}
