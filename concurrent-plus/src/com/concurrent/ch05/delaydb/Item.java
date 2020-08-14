package com.concurrent.ch05.delaydb;


import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 放在延迟阻塞队列中的商品
 */
public class Item<T> implements Delayed {

    //到期时间，单位：毫秒
    private long activeTime;
    //具体商品
    private T date;

    public T getDate() {
        return date;
    }

    //传入的时候过期时长
    public Item(long activeTime, T date) {
        //因为阻塞队列中的延迟时间是纳秒，所以将毫秒转为纳秒
        long nanoTime = TimeUnit.NANOSECONDS.convert(activeTime, TimeUnit.MILLISECONDS);
        //到期时间
        this.activeTime = nanoTime + System.nanoTime();
        this.date = date;
    }

    //获取元素剩余时间
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(activeTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    //根据剩余时间排序
    @Override
    public int compareTo(Delayed o) {
        long time = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return time == 0 ? 0 : (time > 0 ? 1 : -1);
    }
}
