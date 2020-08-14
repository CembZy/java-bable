package com.james.cache.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

//自定义的jedis
public class JamesJedis {

	
	//set key value
	/*
	 * 
	 * 
	 *  *3

		$3
		SET
		
		$11
		lisonLength
		
		$1
		3
	 * 
	 * 
	 * 
	 */
	public static String set(Socket socket, String key, String value) throws IOException{
		StringBuffer str  = new StringBuffer();
		str.append("*3").append("\r\n");
		str.append("$3").append("\r\n");
		str.append("set").append("\r\n");
		//key
		str.append("$").append(key.getBytes().length).append("\r\n");
		str.append(key).append("\r\n");
		//value
		str.append("$").append(value.getBytes().length).append("\r\n");
		str.append(value).append("\r\n");
		
		//向redis发送resp
		socket.getOutputStream().write(str.toString().getBytes());
		byte[] response = new byte[2048];
		socket.getInputStream().read(response);
		
		return new String(response);
	}
	
	
	
	public static String get(Socket socket, String key) throws IOException{
		StringBuffer str  = new StringBuffer();
		str.append("*2").append("\r\n");
		str.append("$3").append("\r\n");
		str.append("get").append("\r\n");
		//key
		str.append("$").append(key.getBytes().length).append("\r\n");
		str.append(key).append("\r\n");
		 
		
		//向redis发送resp
		socket.getOutputStream().write(str.toString().getBytes());
		byte[] response = new byte[2048];
		socket.getInputStream().read(response);
		
		return new String(response);
	}
	
	public static void main(String args[]) throws UnknownHostException, IOException{
		Socket socket = new Socket("127.0.0.1",6379);
		set(socket,"lisonHeight","183");
		System.out.println(get(socket, "lisonHeight"));
	}
	
}
