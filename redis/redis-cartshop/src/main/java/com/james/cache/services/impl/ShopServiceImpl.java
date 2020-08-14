package com.james.cache.services.impl;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.james.cache.services.ShopService;
import com.james.cache.utils.JedisUtils;

@Service
public class ShopServiceImpl implements ShopService{


	@Resource
	private JedisUtils jedis;
	/**
	 * 用户新登录后，更新用户的TOKEN值
	 */
	@Override
	public void updateToken(String token, String user, String itemCode) {
        long timestamp = System.currentTimeMillis() / 1000;	//获取当前时间戳1521434935812
        jedis.hset("login:info", token, user); //记录token与已登录用户之间的映射
        jedis.zadd("recent:info", timestamp, token);  //记录token最后一次出现的时间
        if (itemCode != null) { 
        	jedis.zadd("viewed:" + token, timestamp, itemCode);  //记录用户浏览过的商品
            jedis.zremrangeByRank("viewed:" + token, 0, -26); //分数升序排列，删除第0个与第-26——移除旧的记录,只保留用户最近浏览过的25个商品
        }
	}
	
	/**
	 * 尝试获取并返回令牌token对应的用户
	 */
	@Override
	public String checkToken(String token) {
		 return jedis.hget("login:info", token);
	}
	/**
	 * 将商品加入购物车
	 */
	@Override
	public Long addToCart(String token, String item, int count) {
		//count为用户订购此商品的数量,如果用户订购的数量为0,为无效,从购物车移除
        if (count <= 0) {
        	//从购物车移除商品 
            return 0L;
        } else {
        	//将指定的商品加到购物车, cart:token_1将item商品加入购物车,加入的数量为count
            return jedis.hset("cart:" + token, item, String.valueOf(count));
        }
	}

	@Override
	public long hlen(String key) {
		// TODO Auto-generated method stub
		return jedis.hlen(key);
	}

	@Override
	public Map<String, String> hgetAll(String key) {
		// TODO Auto-generated method stub
		return jedis.hgetAll(key);
	} 
	/**
	 * 删除用户旧token
	 */
	@Override
	public boolean removeOldTokens(long limit){
		long size = jedis.zcard("recent:info"); // 查找目前已有令牌token的个数
		if (size <= limit) { // 如果令牌没有超过limit限制个数 3
			return false;
		}

		long endIndex = size - limit; // 最多只保留10个旧令牌，剩余的删除
		Set<String> tokenSet = jedis.zrange("recent:info", 0L, endIndex - 1);// 获取需要移除的令牌ID
		String[] tokens = tokenSet.toArray(new String[tokenSet.size()]);// 将被移除的令牌转成String[]数组
       
		ArrayList<String> sessionKeys = new ArrayList<String>();
		for (String token : tokens) {
			sessionKeys.add("viewed:" + token); // 为即将被移除的令牌构建KEY键名
												// viewed:xxxxxxx01
		}

		jedis.del(sessionKeys.toArray(new String[sessionKeys.size()])); // 移除最旧令牌
		jedis.hdel("login:info", tokens); // 移除登录相关的令牌
		jedis.zrem("recent:info", tokens); // 移除最近令牌
		return true;
	}
} 
