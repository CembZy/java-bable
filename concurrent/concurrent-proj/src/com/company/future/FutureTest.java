package com.company.future;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 测试Future的使用
 */
public class FutureTest {

    public static class PayFood implements Callable<Food> {

        @Override
        public Food call() throws Exception {
            Food food = new Food();
            food.setName("西红柿");
            return food;
        }
    }


    public static void main(String[] args) throws InterruptedException {

        //开启烧水的线程
        Runnable target = new Runnable() {
            @Override
            public void run() {
                System.out.println("开始烧水.....");
            }
        };
        Thread thread = new Thread(target);
        thread.start();


        //买食材的线程
        Callable payFood = new PayFood();
        //因为是Callable类型，所以要通过FutureTask转换为Runnable类型
        FutureTask<Food> foodFutureTask = new FutureTask<Food>(payFood);

        Thread thread1 = new Thread(foodFutureTask);
        thread1.start();


        //开始做饭，但是再做饭之前必须要先等水烧开了和食材买到了，所以存在一个其他子线程先执行完的情况
        try {
            // get方法，必须是当前线程执行完之后才能获取到值
            Food food = foodFutureTask.get();
            System.out.println("开始做饭，" + food.getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
