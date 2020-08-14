package com.websocket.ch01;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import static com.websocket.ch01.Tool.response;

/**
 * 服务端线程池的单个线程执行任务
 */
public class ServerHandler implements Runnable {

    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        ) {
            String line = null;
            String result = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("Server accept message:" + line);
                result = response(line);
                //将业务结果通过输出流返回给客户端
                printWriter.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
