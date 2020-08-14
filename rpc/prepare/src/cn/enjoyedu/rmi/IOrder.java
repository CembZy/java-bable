package cn.enjoyedu.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMI 订单接口，继承Remote类
 */
public interface IOrder extends Remote{
    //付款的方法
    public String  pay(String id) throws RemoteException;
}
