package cn.enjoyedu.rebalance;

import cn.enjoyedu.config.BusiConst;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 往期课程咨询芊芊老师  QQ：2130753077 VIP课程咨询 依娜老师  QQ：2133576719
 * 类说明：设置了再均衡监听器的消费者
 */
public class RebalanceConsumer {

    public static final String GROUP_ID = "rebalanceconsumer";

    private static ExecutorService executorService
            = Executors.newFixedThreadPool(
            BusiConst.CONCURRENT_PARTITIONS_COUNT);


    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i<BusiConst.CONCURRENT_PARTITIONS_COUNT; i++){
            executorService.submit(new ConsumerWorker(false));
        }
        Thread.sleep(5000);
        //用来被停止，观察保持运行的消费者情况
        new Thread(new ConsumerWorker(true)).start();
    }
}
