package com.cusomer.demo.pull;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//拉取服务
public class PullServices implements ApplicationRunner {


    private static final Logger LOG = Logger.getLogger(PullServices.class);

    //ZooKeeper服务地址
    private static final String SERVER = "127.0.0.1:2181";

    //会话超时时间
    private static final int SESSION_TIMEOUT = 30000;

    //连接超时时间
    private static final int CONNECTION_TIMEOUT = 5000;

    //创建连接实例
    private CuratorFramework client = null;

    //服务注册的节点路径
    private static final String BASE_SERVICES = "/services";

    public static final Map<String, String> services = new ConcurrentHashMap<>();


    @PostConstruct
    public void init() {
        //获取zk连接实例
        client = CuratorFrameworkFactory.newClient(SERVER, SESSION_TIMEOUT, CONNECTION_TIMEOUT, new ExponentialBackoffRetry(1000, 3));

        //启动
        client.start();
    }

    @PreDestroy
    public void destory() {
        //关闭
        client.close();
    }


    private void pull() throws Exception {

        List<String> paths = client.getChildren().forPath(BASE_SERVICES);
        if (paths == null || paths.size() == 0) {
            LOG.error("服务不存在!");
            throw new NullPointerException();
        }


        paths.forEach(path -> {
            try {
                byte[] bytes = client.getData().forPath(BASE_SERVICES + "/" + path);
                String pathData = new String(bytes, "UTF-8");
                services.put(path, pathData);

                //监听当前节点
                TreeCache treeCache = new TreeCache(client, "/test");
                //设置监听器和处理过程
                treeCache.getListenable().addListener(new TreeCacheListener() {
                    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                        ChildData data = event.getData();
                        if (data != null) {
                            String currentPath = data.getPath();
                            String pathData = new String(data.getData(), "UTF-8");
                            switch (event.getType()) {
                                case NODE_ADDED:
                                    services.put(currentPath, pathData);
                                    break;
                                case NODE_REMOVED:
                                    services.remove(currentPath, pathData);
                                    break;
                                case NODE_UPDATED:
                                    services.put(currentPath, pathData);
                                    break;

                                default:
                                    break;
                            }
                        } else {
                            LOG.error("data is null : " + event.getType());
                        }
                    }
                });
                //开始监听
                treeCache.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        pull();
    }
}
