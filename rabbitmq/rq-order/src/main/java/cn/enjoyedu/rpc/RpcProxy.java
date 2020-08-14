package cn.enjoyedu.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com
 *往期视频咨询芊芊老师  QQ：2130753077  VIP课程咨询 依娜老师  QQ：2470523467
 *类说明：
 */
public class RpcProxy {

    public static<T> T getRmoteProxyObj(final Class<?> serviceInterface,
                                        final InetSocketAddress addr){
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(),
                new Class<?>[] {serviceInterface},
                new Proxy_Depot(serviceInterface,addr));

    }

    private static class Proxy_Depot implements InvocationHandler{

        private final Class<?> serviceInterface;
        private final InetSocketAddress addr;

        public Proxy_Depot(Class<?> serviceInterface, InetSocketAddress addr) {
            this.serviceInterface = serviceInterface;
            this.addr = addr;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Socket socket =  null;
            ObjectOutputStream output = null;
            ObjectInputStream input = null;

            try {
                socket = new Socket();
                socket.connect(addr);

                output = new ObjectOutputStream(socket.getOutputStream());
                /*向服务器输出请求*/
                output.writeUTF(serviceInterface.getName());
                output.writeUTF(method.getName());
                output.writeObject(method.getParameterTypes());
                output.writeObject(args);

                input = new ObjectInputStream(socket.getInputStream());
                String result = input.readUTF();
                if(result.equals(RpcConst.EXCEPTION)){
                    throw new RuntimeException("远程服务器异常！");
                }
                return true;
            } finally {
                socket.close();
                output.close();
                //input.close();
            }


        }
    }

}
