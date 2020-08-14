package cn.enjoy.zk;

public abstract class AbstractLock implements Lock {

    public void getLock() {
        for (; ; ) {
            //尝试获得锁资源
            if (tryLock()) {
                System.out.println("##获取lock锁的资源####");
                break;
            } else {
                // 等待
                waitLock();
            }
        }
    }

    // 获取锁资源
    public abstract boolean tryLock();

    // 等待
    public abstract void waitLock();
}
