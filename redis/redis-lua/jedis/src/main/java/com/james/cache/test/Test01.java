package com.james.cache.test;

import redis.clients.jedis.Jedis;

public class Test01 {
	public static void main(String[] args){
		
		Jedis jedis  = new Jedis("127.0.0.1",6379);//只是创建了个对象
		jedis.set("lisonLength", "3");
		
		jedis.close();
		
	}
}
