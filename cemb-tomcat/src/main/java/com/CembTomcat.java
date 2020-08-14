package com;


import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * 嵌入式Tomcat
 */
public class CembTomcat {

    public static void main(String[] args) throws LifecycleException {

        Tomcat tomcat = new Tomcat();
        //设置端口
        Connector connector = tomcat.getConnector();
        connector.setPort(9091);
        //设置IP和项目存放路径
        Host host = tomcat.getHost();

        host.setName("localhost");
        host.setAppBase("/webapps");

        //加载需要启动的项目
        //获取当前项目路径
        String contextPath = System.getProperty("user.dir");
        Context context = tomcat.addContext(host, "/", contextPath);
        if (context instanceof StandardContext) {
            StandardContext standardContext = (StandardContext) context;

            //设置tomcat默认需要加载的web.xml
            standardContext.setDefaultContextXml("classpath:web.xml");

            //我们要把Servlet设置进去
            Wrapper wrapper = tomcat.addServlet("/", "DemoServlet", new DemoServlet());
            wrapper.addMapping("/test");
        }

        tomcat.start();
        //tomcat挂起，防止tomcat启动后主线程结束。
        tomcat.getServer().await();
    }

}
