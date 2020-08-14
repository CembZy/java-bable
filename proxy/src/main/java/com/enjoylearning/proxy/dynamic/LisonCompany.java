package com.enjoylearning.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LisonCompany implements InvocationHandler {
	
	private Object compy;
	
	
	



	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		doSomeThingBefore();
		Object invoke = method.invoke(compy, args);
		doSomeThingEnd();
		return invoke;
	}
	
	
	public Object getProxyInstance(){
		return Proxy.newProxyInstance(compy.getClass().getClassLoader(), compy.getClass().getInterfaces(), this);
	}
	
	
	private void doSomeThingBefore(){
		System.out.println("根据你的需求，我们帮你挑选一个符合你口味的娃娃！");
	}

	
	private void doSomeThingEnd(){
		System.out.println("精美包装，快递一条龙服务！");
	}
	
	
	public Object getCompy() {
		return compy;
	}


	public void setCompy(Object compy) {
		this.compy = compy;
	}

	

}
