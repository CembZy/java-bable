package cn.enjoy.order.listener;

import cn.enjoy.order.utils.LoadBalance;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 2018/6/26.
 */
public class InitListener implements ServletContextListener {


    private  static final String BASE_SERVICES = "/services";
    private static final String  SERVICE_NAME="/products";
    private   ZooKeeper zooKeeper;

    private   void init()  {
        try {
            zooKeeper = new ZooKeeper("127.0.0.1:2181",5000,(watchedEvent)->{
                if(watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged  && watchedEvent.getPath().equals(BASE_SERVICES+SERVICE_NAME)) {
                    updateServerList();
                }
            });
            updateServerList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void updateServerList() {
        List<String> newServerList = new ArrayList<String>();
        try {
            List<String> children = zooKeeper.getChildren(BASE_SERVICES  + SERVICE_NAME, true);
            for(String subNode:children) {
                byte[] data = zooKeeper.getData(BASE_SERVICES  + SERVICE_NAME + "/" + subNode, false, null);
                String host = new String(data, "utf-8");
                System.out.println("host:"+host);
                newServerList.add(host);
            }
            LoadBalance.SERVICE_LIST = newServerList;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
