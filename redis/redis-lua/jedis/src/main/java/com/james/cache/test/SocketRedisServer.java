package com.james.cache.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//伪的redis  6379
public class SocketRedisServer {
	public static void main(String args[]){
		
		try {
			ServerSocket serverSocket = new ServerSocket(6379);
			
			Socket rec = serverSocket.accept();
			byte[] result = new byte[2048];
			rec.getInputStream().read(result);
			System.out.println(new String(result));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
