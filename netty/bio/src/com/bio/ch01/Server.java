package com.websocket.ch01;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static com.websocket.ch01.Tool.PORT;

/**
 * 服务端
 */
public class Server {
    //服务端使用ServerSocket
    private static ServerSocket socket;

    //采用线程池，因为socket每一个连接都需要一个单独的线程，防止线程太多，所以线程池大小需要自定义，采用fix线程池
    private static ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(5);


    //启动服务端
    private static void start() throws IOException {
        try {
            socket = new ServerSocket(PORT);
            System.out.println("服务器已启动，端口号：" + PORT);

            while (true) {
                //获取客户端的请求
                Socket accept = socket.accept();
                System.out.println("有新的客户端连接----");
                threadPoolExecutor.execute(new ServerHandler(accept));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        start();
    }
}
