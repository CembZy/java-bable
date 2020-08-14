package com.concurrent.ch03.atomic;


import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * AtomicStampedReference演示，可以避免ABA问题，内部是int类型的版本号
 */
public class AtomicStampedReferenceDemo {

    public static void main(String[] args) throws InterruptedException {
        //设置初始化版本号是0
        AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference("a1", 0);

        //初始的值和版本号
        String reference = atomicStampedReference.getReference();
        int stamp = atomicStampedReference.getStamp();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("目前的值：" + reference + "............版本号：" + stamp
                        + "，修改结果：" + atomicStampedReference.compareAndSet(reference, "a2", stamp, stamp + 1));
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String reference = atomicStampedReference.getReference();
                System.out.println("目前的值：" + reference + "............版本号：" + atomicStampedReference.getStamp()
                        + "，修改结果：" + atomicStampedReference.compareAndSet(reference, "a2", stamp, stamp + 1));
            }
        });

        thread1.start();
        thread1.join();
        thread2.start();
        thread2.join();
    }

}
