package cn.enjoy.listener;


import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;
import java.util.TimerTask;


public class InitListener implements ServletContextListener {


    // 创建zk连接
    ZkClient zkClient = new ZkClient("127.0.0.1:2181");
    private String path = "/election";

    @Value("${server.port}")
    private String serverPort;


    private void init() {

        System.out.println("项目启动完成...");
        createEphemeral();
        // 创建事件监听
        zkClient.subscribeDataChanges(path, new IZkDataListener() {

            // 节点被删除
            public void handleDataDeleted(String dataPath) throws Exception {
                // 主节点已经挂了，重新选举
                System.out.println("主节点已经挂了，重新开始选举");
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        createEphemeral();
                    }
                }, 5000);
            }

            public void handleDataChange(String dataPath, Object data) throws Exception {

            }
        });

    }

    private void createEphemeral() {
        try {
            zkClient.createEphemeral(path, serverPort);
            ElectionMaster.isSurvival = true;
            System.out.println("serverPort：" + serverPort + ",选举成功....");
        } catch (Exception e) {
            ElectionMaster.isSurvival = false;
        }
    }


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (zkClient != null) {
            zkClient.close();
        }
    }

}
