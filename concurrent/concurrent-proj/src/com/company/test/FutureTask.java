package com.company.test;

import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class FutureTask<V> implements RunnableFuture<V> {

    private final Sync sync;
    //当前执行的线程
    private volatile Thread runner;
    //抛出的异常
    private Throwable exception;

    public FutureTask(Callable<V> callable) {
        if (callable == null) {
            throw new NullPointerException();
        }
        this.sync = new Sync(callable);
    }

    @Override
    public void run() {
        sync.innerRun();
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return sync.innerGet();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return sync.innerGet(unit.toNanos(timeout));
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    private final class Sync extends AbstractQueuedSynchronizer {
        private Callable<V> callable;
        private V result;

        public Sync(Callable<V> callable) {
            this.callable = callable;
        }

        @Override
        protected int tryAcquireShared(int arg) {
            //当前状态为已执行或已取消
            boolean ranOrCancelled = (getState()==2 || getState()==4);
            //且当前线程为null
            boolean innerIsDone = ranOrCancelled && runner == null;
            //获取共享锁锁成功
            return innerIsDone ? 1 : -1;
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            //任务线程置为null
            runner = null;
            return true;
        }

        public V innerGet() throws InterruptedException, ExecutionException {
            //接受中断的共享锁获取
            acquireSharedInterruptibly(0);
            //线程状态为取消的，抛出取消异常
            if (getState() == 4) {
                throw new CancellationException();
            }
            //发生异常的，抛出执行异常
            if (exception != null) {
                throw new ExecutionException(exception);
            }
            //返回Callable执行结果
            return result;
        }

        public V innerGet(long nanosTimeout) throws InterruptedException, ExecutionException, TimeoutException {
            //在超时时间内未获取到共享锁的抛出超时异常
            if (!tryAcquireSharedNanos(0, nanosTimeout)) {
                throw new TimeoutException();
            }
            //线程状态为取消的，抛出取消异常
            if (getState() == 4) {
                throw new CancellationException();
            }
            //发生异常的，抛出执行异常
            if (exception != null) {
                throw new ExecutionException(exception);
            }
            //返回Callable执行结果
            return result;
        }

        private void innerRun() {
            //CAS方法实现线程初始化状态0到线程运行中状态1
            if (this.compareAndSetState(0, 1)) {
                try {
                    //获取当前线程
                    runner = Thread.currentThread();
                    //确认线程状态为运行中
                    if (getState() == 1) {
                        innerSet(callable.call());
                    } else {
                        releaseShared(0);
                    }
                } catch (Throwable e) {
                    this.innerSetException(e);
                }
            }
        }


        //为线程运行结果result赋值，v为callback执行结果
        void innerSet(V v) {
            //自旋
            for (;;) {
                //获取线程状态
                int s = getState();
                //线程状态已执行完成，直接返回
                if (s == 2) {
                    return;
                }
                //线程状态已取消
                if (s == 4) {
                    //AQS的模板方法，释放共享锁
                    releaseShared(0);
                    //直接返回
                    return;
                }
                //CAS原子操作，线程状态从执行中改为已完成
                if (compareAndSetState(s, 2)) {
                    //将callback返回结果赋值给FutureTask的result
                    result = v;
                    //AQS的模板方法，释放共享锁
                    releaseShared(0);
                    //后续操作
                    done();
                    //返回
                    return;
                }
            }
        }
        //Callable中抛出异常
        void innerSetException(Throwable t) {
            //自旋
            for (;;) {
                //获取当前线程的状态
                int s = getState();
                //状态为已执行，直接返回
                if (s == 2) {
                    return;
                }
                //状态为已取消，调用AQS模板方法释放共享锁，直接返回
                if (s == 4) {
                    releaseShared(0);
                    return;
                }
                //CAS操作将线程状态从运行中1改为已完成2，为FutureTask的exception赋值，为result赋值为null，释放共享锁，返回
                if (compareAndSetState(s, 2)) {
                    exception = t;
                    result = null;
                    releaseShared(0);
                    done();
                    return;
                }
            }
        }

        //可覆盖的方法，在执行完成获取结果之后可以执行的方法
        protected void done() {
            System.out.println("FutureTask的result已赋值完成");
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 1;
            }
        };
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();
        System.out.println(futureTask.get());

    }
}