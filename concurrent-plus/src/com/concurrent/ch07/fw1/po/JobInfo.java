package com.concurrent.ch07.fw1.po;


import com.concurrent.ch07.fw1.util.CheckJobProcesser;
import com.concurrent.ch07.fw1.util.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Job实体
 */
public class JobInfo<R> {

    //任务名称
    private final String jobName;

    //任务长度
    private final int jobLength;

    //成功次数
    private final AtomicInteger successNum;

    //已经执行次数
    private final AtomicInteger taskNum;

    //过期时间
    private final long exprise;

    //存放任务执行结果的阻塞队列，使用双端队列，从头拿结果，从尾部放结果
    private final LinkedBlockingDeque<Result<R>> queue;

    private final Task<?, ?> task;

    public JobInfo(String jobName, int jobLength, long exprise, Task<?, ?> task) {
        this.jobName = jobName;
        this.jobLength = jobLength;
        this.task = task;
        this.successNum = new AtomicInteger(0);
        this.taskNum = new AtomicInteger(0);
        this.exprise = exprise;
        this.queue = new LinkedBlockingDeque<>();
    }

    public String getJobName() {
        return jobName;
    }

    public int getJobLength() {
        return jobLength;
    }

    public long getExprise() {
        return exprise;
    }

    //交给线程池去执行的自定义的任务执行逻辑
    public Task<?, ?> getTask() {
        return task;
    }

    //获取任务成功次数
    public int getSuccessNum() {
        return successNum.get();
    }

    //获取任务失败次数
    public int getFailNum() {
        return taskNum.get() - successNum.get();
    }

    //获取任务总执行次数
    public int getTaskNum() {
        return taskNum.get();
    }

    //获取任务执行结果
    public List<Result<R>> getTaskResult() {

        List<Result<R>> results = new LinkedList<>();
        Result<R> result = null;

        //从任务队列中的头去拿，如果拿到就放入集合中
        while ((result = queue.pollFirst()) != null) {
            results.add(result);
        }
        return results;
    }


    //将执行完的任务结果放入队列
    public void addTaskResult(Result<R> result, CheckJobProcesser checkJob) {
        queue.offer(result);

        //任务执行成功就成功次数加一
        if (result.getResultType().equals(ResultType.SUCCESS)) {
            successNum.incrementAndGet();
        }
        taskNum.incrementAndGet();

        //当当前任务所有次数都执行完成了，才将当前任务放入延时过期队列
        if (taskNum.get() == jobLength) {
            checkJob.putJob(jobName, exprise);
        }
    }
}
