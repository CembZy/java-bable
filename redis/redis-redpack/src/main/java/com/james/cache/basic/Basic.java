package com.james.cache.basic;
/**
 * 常量类
 * 
 * @author 【享学课堂】 James老师 qq ：1076258117
 * @author 【享学课堂】 架构技术QQ群 ：684504192
 * @author 【享学课堂】 往期视频依娜老师 ：2470523467
 */
public class Basic {
	public static String ip = "192.168.42.111";
	public static int port = 6379;
	public static String auth = "12345678";
	public static int honBaoCount = 1000;

	public static int threadCount = 20;
	public static String hongBaoPoolKey = "hongBaoPoolKey"; //LIST类型来模拟红包池子
	public static String hongBaoDetailListKey = "hongBaoDetailListKey";//LIST类型，记录所有用户抢红包的详情
	public static String userIdRecordKey = "userIdRecordKey";//记录已经抢过红包的用户ID,防止重复抢
	
	/*
	 * KEYS[1]:hongBaoPool：                   //键hongBaoPool为List类型，模拟红包池子，用来从红包池抢红包
	 * KEYS[2]:hongBaoDetailList：//键hongBaoDetailList为List类型，记录所有用户抢红包的详情
	 * KEYS[3]:userIdRecord ：           //键userIdRecord为Hash类型，记录所有已经抢过红包的用户ID
	 * KEYS[4]:userid ：                              //模拟抢红包的用户ID
	 * 
	 * 
	 * jedis.eval(  Basic.getHongBaoScript,   4,    Basic.hongBaoPoolKey,  Basic.hongBaoDetailListKey,	Basic.userIdRecordKey,  userid);
	 *                      Lua脚本                                参数个数                  key[1]                     key[2]                       key[3]      key[4]                   
	*/
	public static String getHongBaoScript =   
				     //查询用户是否已抢过红包，如果用户已抢过红包，则直接返回nil 
		            "if redis.call('hexists', KEYS[3], KEYS[4]) ~= 0 then\n"   + 
		                 "return nil\n" + 
		            "else\n"  +
		                  //从红包池取出一个小红包
		                  "local hongBao = redis.call('rpop', KEYS[1]);\n"  +
		                  //判断红包池的红包是否为不空
		            	  "if hongBao then\n"  +
			            	 "local x = cjson.decode(hongBao);\n"  +
		            	     //将红包信息与用户ID信息绑定，表示该用户已抢到红包 
			            	 "x['userId'] = KEYS[4];\n"  +
			            	 "local re = cjson.encode(x);\n"  +
			            	  //记录用户已抢过userIdRecordKey  hset userIdRecordKey  userid 1
			            	 "redis.call('hset', KEYS[3], KEYS[4], '1');\n"  +
			            	  //将抢红包的结果详情存入hongBaoDetailListKey
			            	 "redis.call('lpush', KEYS[2], re);\n" + 
			            	 "return re;\n"  +
		                  "end\n"  +
		            "end\n"  +
		            "return nil";  
}
