package com.company.taskfw.vo;

import com.company.taskfw.CheckJobProcesser;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类说明：任务实体类
 */
public class JobInfo<R> {
    //区分唯一的工作
    private final String jobName;
    //工作的任务个数
    private final int jobLength;
    //这个工作的任务处理器
    private final ITaskProcesser<?, ?> taskProcesser;
    //成功处理的任务数
    private final AtomicInteger successCount;
    //已处理的任务数
    private final AtomicInteger taskProcesserCount;
    //存放结果的阻塞队列，拿结果从头拿，放结果从尾部放
    private final LinkedBlockingDeque<TaskResult<R>> taskDetailQueue;
    //工作的完成保存的时间，超过这个时间从缓存中清除
    private final long expireTime;

    //阻塞队列不应该由调用者传入，应该内部生成，长度为工作的任务个数
    public JobInfo(String jobName, int jobLength,
                   ITaskProcesser<?, ?> taskProcesser,
                   long expireTime) {
        super();
        this.jobName = jobName;
        this.jobLength = jobLength;
        this.taskProcesser = taskProcesser;
        this.successCount = new AtomicInteger(0);
        this.taskProcesserCount = new AtomicInteger(0);
        this.taskDetailQueue = new LinkedBlockingDeque<TaskResult<R>>(jobLength);
        ;
        this.expireTime = expireTime;
    }

    public ITaskProcesser<?, ?> getTaskProcesser() {
        return taskProcesser;
    }

    //返回成功处理的结果数
    public int getSuccessCount() {
        return successCount.get();
    }

    //返回当前已处理的结果数
    public int getTaskProcesserCount() {
        return taskProcesserCount.get();
    }

    //提供工作中失败的次数
    public int getFailCount() {
        return taskProcesserCount.get() - successCount.get();
    }

    //任务执行成功和任务执行的结果
    public String getTotalProcess() {
        return "执行成功的任务：" + successCount.get() + "总执行任务数："
                + taskProcesserCount.get() + "总任务数：" + jobLength;
    }

    //获得工作中每个任务的处理详情
    public List<TaskResult<R>> getTaskDetail() {
        List<TaskResult<R>> taskList = new LinkedList<>();
        TaskResult<R> taskResult;
        //从阻塞队列中拿任务的结果，反复取，一直取到null为止，说明目前队列中最新的任务结果已经取完，可以不取了
        while ((taskResult = taskDetailQueue.pollFirst()) != null) {
            taskList.add(taskResult);
        }
        return taskList;
    }

    //将任务执行的结果存放进阻塞队列中，从业务应用角度来说，使用原子操作保证最终一致性即可，不需要对方法加锁.
    public void addTaskResult(TaskResult<R> result, CheckJobProcesser checkJob) {
        //如果成功，任务成功执行次数就++
        if (TaskResultType.Success.equals(result.getResultType())) {
            successCount.incrementAndGet();
        }
        //放入阻塞队列中
        taskDetailQueue.addLast(result);
        //任务执行次数++
        taskProcesserCount.incrementAndGet();

        //如果任务执行次数和任务数量相同，证明所有任务都已经执行完成，就需要将任务放入到过期队列中
        if (taskProcesserCount.get() == jobLength) {
            checkJob.putJob(jobName, expireTime);
        }
    }
}
