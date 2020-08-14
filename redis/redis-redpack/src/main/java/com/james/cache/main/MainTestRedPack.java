package com.james.cache.main;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.springframework.util.StopWatch;

import com.alibaba.fastjson.JSONObject;
import com.james.cache.redpack.GenRedPack;
import com.james.cache.redpack.GetRedPack;

import redis.clients.jedis.Jedis;

public class MainTestRedPack {
	

	public static void main(String[] args) throws InterruptedException {
		GenRedPack.genHongBao();//初始化红包
		
		GetRedPack.getHongBao();//从红包池抢红包
	
	}
}