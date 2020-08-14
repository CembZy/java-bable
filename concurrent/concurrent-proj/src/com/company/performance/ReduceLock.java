package com.company.performance;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 *类说明：缩小锁的范围，有时候增大锁的范围也能提高性能，比如StringBuffer中的appened方法，因为需要频繁的拼接
 *，所以增大锁的范围范围增加了性能
 */
public class ReduceLock {
	
	private Map<String,String> matchMap = new HashMap<>();
	
	public synchronized boolean isMatch(String name,String regexp) {
		String key = "user."+name;
		String job = matchMap.get(key);
		if(job == null) {
			return false;
		}else {
			return Pattern.matches(regexp, job);//很耗费时间
		}
	}

	//减小范围
	public  boolean isMatchReduce(String name,String regexp) {
		String key = "user."+name;
		String job ;
		synchronized(this) {
			job = matchMap.get(key);
		}
	
		if(job == null) {
			return false;
		}else {
			return Pattern.matches(regexp, job);
		}
	}
	
}
