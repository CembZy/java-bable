package cn.enjoyedu.analysis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class WangMeiLiProxy implements InvocationHandler {
	
	private Girl gilr;
	
	

	public WangMeiLiProxy(Girl gilr) {
		super();
		this.gilr = gilr;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		//事前
		doSomeThingBefore();
		Object ret = method.invoke(gilr, args);
		//事后
		doSomeThingEnd();
		return ret;
	}
	
	private void doSomeThingBefore(){

		System.out.println("王美丽的父母说：我得先调查下这个男孩子的背景！");
	}
	
	private void doSomeThingEnd(){

		System.out.println("王美丽的父母说：他有没有对你动手动脚啊？");
	}
	//代理：调度
	public Object getProxyInstance(){
		return Proxy.newProxyInstance(gilr.getClass().getClassLoader(), gilr.getClass().getInterfaces(), this);
	}

}
