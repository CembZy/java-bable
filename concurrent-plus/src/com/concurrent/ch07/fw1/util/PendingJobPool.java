package com.concurrent.ch07.fw1.util;


import com.concurrent.ch07.fw1.po.JobInfo;
import com.concurrent.ch07.fw1.po.Result;
import com.concurrent.ch07.fw1.po.ResultType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 执行任务的线程池
 */
public class PendingJobPool {

    //获取机器的CPU核心数
    private static final int THREAD_POOL_NUM = Runtime.getRuntime().availableProcessors();

    //存放任务的队列，规定队列大小是5000
    private static BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(5000);

    //自定义线程池，因为需要使用有界的队列，并且使用默认的饱和策略
    private static ExecutorService executorService = new ThreadPoolExecutor(THREAD_POOL_NUM, THREAD_POOL_NUM,
            60, TimeUnit.SECONDS, queue);

    //存放注册的任务集合
    private static ConcurrentHashMap<String, JobInfo<?>> jobs = new ConcurrentHashMap<>();

    //检查执行完成的任务是否过期
    private static CheckJobProcesser checkJobProcesser = CheckJobProcesser.newInstance();


    //对外提供唯一的实例，使用懒加载单例模式
    private PendingJobPool() {
    }

    private static class JobPoolHolder {
        private static PendingJobPool pendingJobPool = new PendingJobPool();
    }

    public static PendingJobPool newInstance() {
        return JobPoolHolder.pendingJobPool;
    }

    //对外暴露注册的任务集合
    public static Map<String, JobInfo<?>> getJobs() {
        return jobs;
    }

    public static void setJobs(ConcurrentHashMap<String, JobInfo<?>> jobs) {
        PendingJobPool.jobs = jobs;
    }

    //包装执行任务为Runnable线程
    private class JobThread<T, R> implements Runnable {

        private JobInfo<R> jobInfo;

        private T data;

        public JobThread(JobInfo<R> jobInfo, T data) {
            this.jobInfo = jobInfo;
            this.data = data;
        }

        @Override
        public void run() {
            Task<T, R> task = (Task<T, R>) jobInfo.getTask();
            //结果信息
            R resultInfo = null;
            Result<R> result = null;

            //健壮性考虑，防止任务执行过程抛出异常
            try {
                //自定义的任务执行逻辑
                result = task.taskExecute(data);
                //健壮性判断
                if (result == null) {
                    result = new Result<>("任务没有结果返回", ResultType.EXCEPTIN, resultInfo);
                } else if (result.getResultType() == null) {
                    if (result.getReason() == null) {
                        result = new Result<>("任务执行结果没有结果类型，并且没有异常原因", ResultType.EXCEPTIN, resultInfo);
                    } else {
                        result = new Result<>("  任务执行结果没有结果类型", ResultType.EXCEPTIN, resultInfo);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                result = new Result<>(e.getMessage(), ResultType.EXCEPTIN, resultInfo);
            } finally {
                //讲任务执行结果信息放入任务实体
                jobInfo.addTaskResult(result, checkJobProcesser);
            }
        }
    }

    //获取任务执行结果详情
    public <R> List<Result<R>> getResult(String jobName) {
        JobInfo<R> job = getJob(jobName);
        return job.getTaskResult();
    }

    //获取任务进度
    public <R> String getJobProgess(String jobName) {
        JobInfo<R> job = getJob(jobName);
        return "失败次数：.." + job.getFailNum() + "" +
                ".......成功次数：.." + job.getSuccessNum() + "" +
                ".......总执行次数：.." + job.getTaskNum();
    }


    //注册任务
    public <R> void registJobs(String jobName, int jobLength, long exprise, Task<?, ?> task) {
        JobInfo<R> jobInfo = new JobInfo<>(jobName, jobLength, exprise, task);

        //健壮性判断，判断是否存在此任务了
        if (jobs.putIfAbsent(jobName, jobInfo) != null) {
            throw new RuntimeException("已经存在此任务了..");
        }
    }

    //提交任务，推入线程池执行
    public <T, R> void submitJob(String jobName, T data) {
        JobInfo<R> job = getJob(jobName);
        //线程池执行的任务是Runnable类型，所以需要将任务包装一下
        JobThread<T, R> jobThread = new JobThread<>(job, data);

        //交给线程池执行，这里规定任务没有返回值，所以用execute
        executorService.execute(jobThread);
    }

    //健壮性判断，判断提交的任务是否注册了
    public <R> JobInfo<R> getJob(String jobName) {
        JobInfo<R> jobInfo = (JobInfo<R>) jobs.get(jobName);
        if (jobInfo == null) {
            throw new RuntimeException("任务非法..");
        }
        return jobInfo;
    }

}
