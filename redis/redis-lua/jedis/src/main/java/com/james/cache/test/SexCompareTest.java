package com.james.cache.test;

import com.james.cache.jedis.RedisTools;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class SexCompareTest {
	
	public static void main(String[] args ){
		//初始化一批数—— 用来删除
		RedisTools.initRedisData();
		long t = System.currentTimeMillis();
		delPipe(RedisTools.keys);
		System.out.println(System.currentTimeMillis()-t);
	}
	
	
	public static void dels(String... keys){
		Jedis jedis = new Jedis(RedisTools.ip, RedisTools.port);
		for(String id : keys){
			jedis.del(id);
		}
		jedis.close();
	}
	
	public static void delPipe(String... keys){
		Jedis jedis = new Jedis(RedisTools.ip, RedisTools.port);
	    Pipeline pipe = jedis.pipelined();
	    
		for(String id : keys){
			pipe.del(id);   //提交一次， append
		}
		pipe.sync(); //真正提交
		jedis.close();
		
	}
	
}
