package cn.enjoyedu.refle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *类说明：演示反射的使用
 */
public class RefleDemo {

	public static void main(String[] args) throws ClassNotFoundException, 
	InstantiationException, IllegalAccessException {



        //通过全限定名拿到类的class对象
		Class TechClazz = Class.forName("cn.enjoyedu.refle.Tech");

        //通过class对象拿到类的实例
        Tech shapeInst = (Tech)TechClazz.newInstance();

        //通过class对象拿到方法列表
		Method[] methods = TechClazz.getMethods();
		for(Method method:methods) {
			System.out.println(method.getName());
			if(method.getName().equals("XJ")){//洗脚服务
				try {
				    //执行指定方法
					method.invoke(TechClazz.newInstance(),"king");
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
