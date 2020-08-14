package com.master.demo.vote;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


@Component
public class NodeVote implements InitializingBean, DisposableBean {

    // 创建zk连接
    //ZooKeeper服务地址
    private static final String SERVER = "127.0.0.1:2181";

    private static ZkClient zkClient = null;
    private final String PATH = "/election";

    @Value("${server.port}")
    private String serverPort;

    public static volatile boolean isMatser = true;

    private AtomicReference<HttpServletRequest> request = null;

    public void vote(HttpServletRequest request) {
        createNode(request);
        ScheduledExecutorService scheService = Executors.newScheduledThreadPool(1);
        //监听节点
        zkClient.subscribeDataChanges(PATH, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                //防止假死情况，所以需要定时PING，心跳检测
                scheService.schedule(new Runnable() {
                    @Override
                    public void run() {
                        //被删除的时候创建新节点
                        createNode(request);
                    }
                }, 5, TimeUnit.SECONDS);
            }
        });

    }

    //创建节点
    public void createNode(HttpServletRequest request) {
        try {
            zkClient.createEphemeral(PATH, "test");
            if (this.request == null) {
                this.request = new AtomicReference<>(request);
            } else {
                this.request.set(request);
            }

        } catch (Exception e) {
            if (this.request.get() == request) {
                isMatser = true;
            } else {
                isMatser = false;
            }
        }
    }


    @Override
    public void destroy() throws Exception {
        zkClient.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        zkClient = new ZkClient(SERVER);
    }
}
