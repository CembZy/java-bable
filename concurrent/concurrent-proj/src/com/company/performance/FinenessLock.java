package com.company.performance;

import java.util.HashSet;
import java.util.Set;

/**
 * 类说明：减少锁的粒度，比如concurrenthashmap中的并发安全的实现
 */
public class FinenessLock {

    public final Set<String> users = new HashSet<String>();
    public final Set<String> queries = new HashSet<String>();

    public void addUser(String u) {
        //缩小粒度
        synchronized (users) {
            users.add(u);
        }
    }

    public void addQuery(String q) {
        synchronized (users) {
            queries.add(q);
        }
    }

//	public synchronized void removeUser(String u) {
//		users.remove(u);
//	}
//	
//	public synchronized void removeQuery(String q) {
//		queries.remove(q);
//	}	

}
