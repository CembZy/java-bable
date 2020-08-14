package com.websocket.ch01;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import static com.websocket.ch01.Tool.IP;
import static com.websocket.ch01.Tool.PORT;

/**
 * 客户端，负责向服务端发送键盘输入
 */
public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket(IP, PORT);

        //开启从服务端获取响应数据的线程
        new Thread(new Result(socket)).start();

        //向服务端发送数据
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        System.out.println("请输入：");
        while (true) {
            printWriter.println(new Scanner(System.in).next());
            printWriter.flush();
        }
    }


    public static class Result implements Runnable {

        private Socket socket;

        public Result(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader bis = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String line = null;
                while ((line = bis.readLine()) != null) {
                    System.out.println("结果是：" + line);
                }
            } catch (IOException e) {
                System.out.printf("%s\n", "服务器断开了你的连接");
                e.printStackTrace();
            } finally {
                clear();
            }
        }

        //必要的资源清理工作
        private void clear() {
            if (socket != null)
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

}
