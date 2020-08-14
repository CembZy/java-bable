package com.company.taskfw;

import com.company.taskfw.vo.ITaskProcesser;
import com.company.taskfw.vo.JobInfo;
import com.company.taskfw.vo.TaskResult;
import com.company.taskfw.vo.TaskResultType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * 框架的主体类
 */
public class PendingJobPool {

    //线程池的线程初始化数量，一般不确定数量的时候设置为CPU的内核数
    private static final int THREAD_COUNTS =
            Runtime.getRuntime().availableProcessors();

    //任务队列
    private static BlockingQueue<Runnable> taskQueue
            = new ArrayBlockingQueue<>(5000);

    //线程池，固定大小，有界队列
    private static ExecutorService taskExecutor =
            new ThreadPoolExecutor(THREAD_COUNTS, THREAD_COUNTS, 60,
                    TimeUnit.SECONDS, taskQueue);

    //任务的存放容器
    private static ConcurrentHashMap<String, JobInfo<?>> jobInfoMap
            = new ConcurrentHashMap<>();

    //过期任务处理类
    private static CheckJobProcesser checkJob
            = CheckJobProcesser.getInstance();

    public static Map<String, JobInfo<?>> getMap() {
        return jobInfoMap;
    }

    //类加载的单例模式------
    private PendingJobPool() {
    }

    private static class JobPoolHolder {
        public static PendingJobPool pool = new PendingJobPool();
    }

    public static PendingJobPool getInstance() {
        return JobPoolHolder.pool;
    }
    //单例模式------


    //对工作中的任务进行包装，提交给线程池使用，并处理任务的结果，写入缓存以供查询
    private static class PendingTask<T, R> implements Runnable {

        private JobInfo<R> jobInfo;
        private T processData;

        public PendingTask(JobInfo<R> jobInfo, T processData) {
            super();
            this.jobInfo = jobInfo;
            this.processData = processData;
        }

        @Override
        public void run() {
            R r = null;
            ITaskProcesser<T, R> taskProcesser =
                    (ITaskProcesser<T, R>) jobInfo.getTaskProcesser();
            TaskResult<R> result = null;

            //因为可能开发人员会对异常直接抛出，所以这里需要捕捉异常
            try {
                //调用业务人员实现的具体方法
                result = taskProcesser.taskExecute(processData);
                //要做检查，防止开发人员处理不当
                if (result == null) {
                    result = new TaskResult<R>(TaskResultType.Exception, r,
                            "执行结果为null");
                }
                if (result.getResultType() == null) {
                    if (result.getReason() == null) {
                        result = new TaskResult<R>(TaskResultType.Exception, r, "执行结果为null");
                    } else {
                        result = new TaskResult<R>(TaskResultType.Exception, r,
                                "执行结果为null，错误原因是：" + result.getReason());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = new TaskResult<R>(TaskResultType.Exception, r,
                        e.getMessage());
            } finally {
                //需要将任务结果放入到阻塞队列中
                jobInfo.addTaskResult(result, checkJob);
            }
        }
    }

    //根据任务名称获取任务
    private <R> JobInfo<R> getJob(String jobName) {
        JobInfo<R> jobInfo = (JobInfo<R>) jobInfoMap.get(jobName);
        jungleNull(jobName, null == jobInfo);
        return jobInfo;
    }

    //调用者提交工作中的任务
    public <T, R> void putTask(String jobName, T t) {
        JobInfo<R> jobInfo = getJob(jobName);
        jungleNull(jobName, null == jobInfo);
        PendingTask<T, R> task = new PendingTask<T, R>(jobInfo, t);
        //放入到线程池中执行
        taskExecutor.execute(task);
    }

    //调用者注册任务，如任务名，任务的处理器等等
    public <R> void registerJob(String jobName, int jobLength,
                                ITaskProcesser<?, ?> taskProcesser, long expireTime) {
        JobInfo<R> jobInfo = new JobInfo(jobName, jobLength,
                taskProcesser, expireTime);
        //如果不存在已经注册的任务，就放入到容器中，如果有就返回已经注册的任务
        if (jobInfoMap.putIfAbsent(jobName, jobInfo) != null) {
            throw new RuntimeException(jobName + "已经被注册了！");
        }
    }

    //获得每个任务的处理详情
    public <R> List<TaskResult<R>> getTaskDetail(String jobName) {
        JobInfo<R> jobInfo = getJob(jobName);
        jungleNull(jobName, null == jobInfo);
        return jobInfo.getTaskDetail();
    }

    //获得工作的整体处理进度
    public <R> String getTaskProgess(String jobName) {
        JobInfo<R> jobInfo = getJob(jobName);
        jungleNull(jobName, null == jobInfo);
        return jobInfo.getTotalProcess();
    }

    //判空
    private void jungleNull(String jobName, boolean b) {
        if (b) {
            throw new RuntimeException(jobName + "任务不能获取到。");
        }
    }

}
