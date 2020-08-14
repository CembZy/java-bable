package com.company.query;


import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时处理业务的阻塞队列
 *
 * @param <T>
 */
public class ItemsQuery<T> implements Delayed {

    private T item;

    private long activeTime;

    public T getItem() {
        return item;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public ItemsQuery(T item, long activeTime) {
        this.item = item;
        //将毫秒转为纳秒,加上当前时刻的纳秒值,即是过期时间的纳秒
        this.activeTime = TimeUnit.NANOSECONDS.convert(activeTime, TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    /**
     * 返回元素的剩余时间
     *
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        long d = unit.convert(this.activeTime - System.nanoTime(),
                TimeUnit.NANOSECONDS);
        return d;
    }

    /**
     * 自定义队列排序规则
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        long time = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return time == 0 ? 0 : (time > 0 ? 1 : -1);
    }
}
