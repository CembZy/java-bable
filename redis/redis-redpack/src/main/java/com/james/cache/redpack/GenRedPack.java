package com.james.cache.redpack;

import java.util.concurrent.CountDownLatch;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import com.james.cache.basic.Basic;
import com.james.cache.utils.JedisUtils;

public class GenRedPack {
	/**
	 * 多线程模拟红包池初始化  Jedis类
	 */
	public static void genHongBao() throws InterruptedException {
		JedisUtils jedis = new JedisUtils(Basic.ip, Basic.port, Basic.auth);
		jedis.flushall();  //清空,线上不要用.....

		//发枪器
		final CountDownLatch latch = new CountDownLatch(Basic.threadCount);
		
		for(int i = 0 ; i < Basic.threadCount; i++){
			final int page = i;
			Thread thread = new Thread(){
				public void run(){
					//每个线程要初始化多少个红包
					int per = Basic.honBaoCount/Basic.threadCount;
					
					JSONObject object = new JSONObject();
					
					for(int j = page * per ; j < (page+1) * per; j++){ //从0开始，直到
						object.put("id", "rid_"+j); //红包ID
						object.put("money", j);   //红包金额
						//lpush key value===lpush hongBaoPoolKey {id:rid_1, money:23}
						jedis.lpush("hongBaoPoolKey", object.toJSONString());
					}
					latch.countDown(); //发枪器递减
				}
			};
			thread.start();
		}
		latch.await();//所有线程处于等状态
	}
}
