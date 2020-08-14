package cn.enjoyedu.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 接口实现类，继承UnicastRemoteObject
 */
public class OrderImpl extends UnicastRemoteObject implements  IOrder {
    protected OrderImpl() throws RemoteException{
        super();
    }
    @Override
    public  String pay(String id ) throws  RemoteException{
        //默认返回成功
        return "支付成功！商品订单号:"+id;
    }
}
