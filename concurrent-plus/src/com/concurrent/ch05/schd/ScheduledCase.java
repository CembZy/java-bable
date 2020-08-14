package com.concurrent.ch05.schd;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 类说明：演示ScheduledThreadPoolExecutor的用法
 */
public class ScheduledCase {
    public static void main(String[] args) {

        ScheduledExecutorService schedule
                = new ScheduledThreadPoolExecutor(1);

        schedule.scheduleAtFixedRate(new ScheduleWorker(ScheduleWorker.HasException),
                1000, 3000, TimeUnit.MILLISECONDS);
        schedule.scheduleAtFixedRate(new ScheduleWorker(ScheduleWorker.Normal),
                1000, 3000, TimeUnit.MILLISECONDS);

    }
}
