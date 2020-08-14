package cn.enjoyedu.rmi;

import java.rmi.Naming;

/**
 * RMI-客户端
 */
public class Client {
    public static void main(String[] args)throws  Exception {
        //通过RMI发现服务并且转成一个对象
        IOrder iOrder = (IOrder)Naming.lookup("rmi://localhost:6666/order");
        //远程调用下
        System.out.println(iOrder.pay("168888"));
    }
}
