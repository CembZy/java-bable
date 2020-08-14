package com.company.thread;

/**
 * 测试发生InterruptException异常的时候，中断标志位发生的变化
 */

public class InterruptEx extends Thread {


    @Override
    public void run() {
        //处于中断情况，即是中断标志位为true
        while (!isInterrupted()) {
            try {
                Thread.sleep(100);
                //发生了InterruptedException异常，在catch执行结束后会自动将中断标志位变为true，这时可能需要我们手动
                //通过 interrupt()将中断标志位变为false
            } catch (InterruptedException e) {
                System.out.println("isInterrupted:" + isInterrupted());
                interrupt();
                e.printStackTrace();
            }
            System.out.println("isInterrupted2:" + isInterrupted());
        }
    }
}
