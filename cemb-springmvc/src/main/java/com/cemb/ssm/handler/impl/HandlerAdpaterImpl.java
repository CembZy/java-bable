package com.cemb.ssm.handler.impl;

import com.cemb.ssm.annotation.CemBService;
import com.cemb.ssm.handler.HandlerAdpater;
import com.cemb.ssm.resovler.ArgumentResovler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//适配器的实现类
@CemBService("handlerAdpater")
public class HandlerAdpaterImpl implements HandlerAdpater {


    //执行方法
    @Override
    public synchronized Object[] handlerMethod(HttpServletRequest request, HttpServletResponse response,
                                               Method method, Map<Object, Object> beans) throws InstantiationException, IllegalAccessException {

        if (beans.size() <= 0) {
            return null;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null) {
            return null;
        }
        //根据参数的个数,new 一个参数的数组,将方法里的所有参数赋值到args来
        Object[] args = new Object[parameterTypes.length];

        //1、要拿到所有实现了ArgumentResolver这个接口的实现类
        Map<Object, Object> argumentResolvers = getBeansOfType(beans,
                ArgumentResovler.class);

        //参数列表的位置下标
        int paramIndex = 0;

        //用于记录得到参数解析结果的数组的下标
        int i = 0;

        for (Class<?> param : parameterTypes) {
            //找出容器中所有的解析器
            for (Map.Entry<Object, Object> bean : argumentResolvers.entrySet()) {

                ArgumentResovler argumentResovler = (ArgumentResovler) bean.getValue();
                //判断是否是当前参数注解,用策略模式来找用哪个解析器执行
                if (argumentResovler.support(param, paramIndex, method)) {
                    //解析结果
                    args[i++] = argumentResovler.argumentResolver(request, response, param, paramIndex, method);
                }
            }
            paramIndex++;
        }

        return args;
    }


    //获取实现了ArgumentResolver接口的所有实例(其实就是每个参数的注解实例)
    private synchronized Map<Object, Object> getBeansOfType(Map<Object, Object> beans,//所有bean
                                                            Class<?> intfType) //类型的实例
    {

        Map<Object, Object> resultBeans = new ConcurrentHashMap<>();

        for (Map.Entry<Object, Object> entry : beans.entrySet()) {
            //拿到实例-->反射对象-->它的接口(接口有多实现,所以为数组)
            Class<?>[] intfs = entry.getValue().getClass().getInterfaces();

            if (intfs != null && intfs.length > 0) {
                for (Class<?> intf : intfs) {
                    //接口的类型与传入进来的类型一样,把实例加到resultBeans里来
                    if (intf.isAssignableFrom(intfType)) {
                        resultBeans.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        return resultBeans;
    }
}
