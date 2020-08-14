package cn.enjoy.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;


public class ZookeeperDistrbuteLock extends ZookeeperAbstractLock {
    private CountDownLatch countDownLatch = null;

    @Override
    //尝试获得锁
    public boolean tryLock() {
        try {
            //创建一个lock的节点，表示一把分布式锁
            zkClient.createEphemeral(PATH);
            return true;
        } catch (Exception e) {
            //如果创建失败报出异常
//			e.printStackTrace();
            return false;
        }

    }

    @Override
    public void waitLock() {
        IZkDataListener izkDataListener = new IZkDataListener() {

            //监听到lock节点被删除，即是释放了锁
            public void handleDataDeleted(String path) throws Exception {
                // 唤醒被等待的线程
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }

            public void handleDataChange(String path, Object data) throws Exception {

            }
        };
        // 注册事件
        zkClient.subscribeDataChanges(PATH, izkDataListener);

        //如果节点存
        if (zkClient.exists(PATH)) {
            countDownLatch = new CountDownLatch(1);
            try {
                //等待，一直等到接受到事件通知
                countDownLatch.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 删除监听
        zkClient.unsubscribeDataChanges(PATH, izkDataListener);
    }

    public void unLock() {
        //释放锁
        if (zkClient != null) {
            zkClient.delete(PATH);
            zkClient.close();
            System.out.println("释放锁资源...");
        }
    }
}