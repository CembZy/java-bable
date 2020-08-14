package cn.enjoy.mysql;

import cn.enjoy.mapper.LockMapper;
import cn.enjoy.zk.AbstractLock;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;


@Service
public  class MysqlLock extends AbstractLock {
	
	@Resource
	private LockMapper mapper;
	
	//所有的线程都往数据库插入主键值相同的数据
	private static final int LOCK_ID = 1;



	//非阻塞式加锁
	public boolean tryLock() {
		try {
			mapper.insert(LOCK_ID);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	//让当前线程休眠一段时间

	public void waitLock() {
		try {
			Thread.currentThread().sleep(10);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}



	public void unLock() {
		mapper.deleteByPrimaryKey(LOCK_ID);
	}
}
