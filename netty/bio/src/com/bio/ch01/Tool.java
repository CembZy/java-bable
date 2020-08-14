package com.websocket.ch01;


/**
 * 工具类
 */
public class Tool {

    //端口号
    public static int PORT = 12345;
    public static String IP = "localhost";

    //返回给客户端的应答
    public static String response(String msg) {
        return "Hello," + msg + ",Now is " + new java.util.Date(
                System.currentTimeMillis()).toString();
    }

}
