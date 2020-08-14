package com.product.demo.register;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;


public class RgisteToZk implements ApplicationRunner, ApplicationContextAware {


    private static final Logger LOG = Logger.getLogger(RgisteToZk.class);

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

    //需要注册的服务
    private static final List<String> services = new LinkedList<>();

    @Value("${server.port}")
    private int port;

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
        client.delete();
        client.close();
    }


    //注册服务
    public void registe() {
        try {
            Stat pathStat = client.checkExists().forPath(BASE_SERVICES);
            if (pathStat == null) {
                //创建根路径
                client.create().orSetData().withMode(CreateMode.PERSISTENT).forPath(BASE_SERVICES, "services".getBytes());
            }

            services.forEach(service -> {
                try {
                    //创建临时有顺序的节点，因为存在节点挂掉和节点服务一致的情况，所以需要临时并且有顺序
                    client.create().orSetData().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(BASE_SERVICES + "/", service.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("注册失败!");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("节点创建失败!");
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        registe();
    }

    //获取服务
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String address = "";
        try {
            address = InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //获取容器中所有的服务
        String[] allService = applicationContext.getBeanNamesForAnnotation(RestController.class);
        for (int i = 0; i < allService.length; i++) {
            String service = allService[i];
            Class bean = applicationContext.getBean(service).getClass();
            Method[] methods = bean.getMethods();
            for (Method method : methods) {
                GetMapping annotation = method.getAnnotation(GetMapping.class);
                if (annotation != null) {
                    String[] path = annotation.value();
                    services.add(address + "/" + path[0]);
                }
            }
        }
    }
}
