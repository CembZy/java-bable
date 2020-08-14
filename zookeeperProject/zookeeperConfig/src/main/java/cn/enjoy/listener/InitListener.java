package cn.enjoy.listener;


import cn.enjoy.db.EnjoyDataSource;
import cn.enjoy.utils.MyZkSerializer;
import cn.enjoy.utils.RuntimeContext;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;


public class InitListener implements ServletContextListener {



    private  static final String URL = "/db/url";
    private  static final String PASSWORD = "/db/password";
    private  static final String USERNAME = "/db/username";
    private  static final String DRIVER = "/db/username";

    ZkClient zkClient = new ZkClient("127.0.0.1:2181",5000,500,new MyZkSerializer());

    private   void init()  {

        zkClient.subscribeDataChanges(URL, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                EnjoyDataSource datasource = (EnjoyDataSource)RuntimeContext.getBean("dataSource");
                datasource.setUrl(o.toString());
                datasource.changeDataSource();
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {

            }
        });

    }



    @Override
    public void contextInitialized(ServletContextEvent sce) {
        init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
