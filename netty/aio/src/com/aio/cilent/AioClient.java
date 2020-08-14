package com.aio.cilent;


import com.aio.Tool;
import java.util.Scanner;


/**
 * AIO客户端主程序
 */
public class AioClient {

    //用于连接服务端的自定义异步处理器
    public static AioClientHandler aioClientHandler;


    public void start() {
        if (aioClientHandler != null)
            return;
        //连接服务端，因为连接操作是异步的，所以需要使用异步处理器
        aioClientHandler = new AioClientHandler(Tool.DEFAULT_PORT, Tool.DEFAULT_SERVER_IP);
        new Thread(aioClientHandler, "client").start();
    }

    public static boolean sendMsg(String msg) {
        if (msg.equals("q")) return false;
        aioClientHandler.sendMessag(msg);
        return true;
    }


    public static void main(String[] args) {
        AioClient aioClient = new AioClient();
        aioClient.start();
        System.out.println("请输入请求消息：");
        Scanner scanner = new Scanner(System.in);
        while (sendMsg(scanner.nextLine())) ;

    }

}
