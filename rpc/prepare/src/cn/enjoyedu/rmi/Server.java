package cn.enjoyedu.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * RMI--服务端
 */
public class Server {
    public static void main(String[] args)throws  Exception {
        //接口实例化
        IOrder iOrder = new OrderImpl();
        //本地的服务注册到6666端口中
        LocateRegistry.createRegistry(6666);
        //把刚才的实例绑定到本地端口上的一个路径上
        Naming.bind("rmi://localhost:6666/order",iOrder);
        System.out.println("服务器已经启动了!");
    }
}
