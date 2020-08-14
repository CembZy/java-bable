package com.company.fork;

/**
 * 模拟单线程下，计算一个数组中元素的总和的时长
 */
public class SumNormal {

    public static void main(String[] args) {
        int count = 0;
        int[] src = MakeArray.makeArray();

        long start = System.currentTimeMillis();
        for (int i = 0; i < src.length; i++) {
            //SleepTools.ms(1);
            count = count + src[i];
        }
        System.out.println("The count is " + count
                + " spend time:" + (System.currentTimeMillis() - start) + "ms");
    }

}