package enjoyedu.register;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类说明：服务注册中心
 */
public class RegisterCenter {
    //线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    //定义注册中心的静态对象
    private static final HashMap<String, Class> serviceRegistry = new HashMap<String, Class>();

    private static boolean isRunning = false;

    private static int port;

    public RegisterCenter(int port) {

        this.port = port;
    }

    //服务注册中心启动
    public void start() throws IOException {
        //服务器监听
        ServerSocket server = new ServerSocket();
        //监听绑定端口
        server.bind(new InetSocketAddress(port));
        System.out.println("start server");
        try {
            while (true) {
                // 1.监听客户端的TCP连接，接到TCP连接后将其封装成task，由线程池执行,并且同时将socket送入(server.accept()=socket)
                executor.execute(new ServiceTask(server.accept()));
            }
        } finally {
            server.close();
        }
    }

    //服务的注册:socket通讯+反射
    public void register(Class serviceInterface, Class impl) {

        serviceRegistry.put(serviceInterface.getName(), impl);
    }

    //服务的获取运行
    private static class ServiceTask implements Runnable {
        //客户端socket
        Socket clent = null;

        public ServiceTask(Socket client) {
            this.clent = client;
        }

        //远程请求达到服务端，我们需要执行请求结果，并且把请求结果反馈至客户端，使用Socket通讯
        public void run() {
            //反射
            //同样适用object流
            ObjectInputStream inputStream = null;
            ObjectOutputStream outputStream = null;
            try {
                //1.客户端发送的object对象拿到，2.在采用反射的机制进行调用，3.最后给返回结果
                inputStream = new ObjectInputStream(clent.getInputStream());
                //顺序发送数据：类名、方法名、参数类型、参数值
                //拿到接口名
                String serviceName = inputStream.readUTF();
                //拿到方法名
                String methodName = inputStream.readUTF();
                //拿到参数类型
                Class<?>[] paramTypes = (Class<?>[]) inputStream.readObject();
                //拿到参数值
                Object[] arguments = (Object[]) inputStream.readObject();
                //要到注册中心根据 接口名，获取实现类
                Class serviceClass = serviceRegistry.get(serviceName);
                //使用反射的机制进行调用
                Method method = serviceClass.getMethod(methodName, paramTypes);
                //反射调用方法,把结果拿到
                Object result = method.invoke(serviceClass.newInstance(), arguments);
                //通过执行socket返回给客户端
                outputStream = new ObjectOutputStream(clent.getOutputStream());
                // /把结果返回给客户端
                outputStream.writeObject(result);
                outputStream.flush();
                //记得关闭
                outputStream.close();
                inputStream.close();
                clent.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
