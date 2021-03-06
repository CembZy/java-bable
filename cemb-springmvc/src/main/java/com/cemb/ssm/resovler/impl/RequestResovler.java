package com.cemb.ssm.resovler.impl;

import com.cemb.ssm.annotation.CemBService;
import com.cemb.ssm.resovler.ArgumentResovler;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

//解析器
@CemBService("requestResovler")
public class RequestResovler implements ArgumentResovler {

    //判断是否是当前解析器
    @Override
    public synchronized boolean support(Class<?> clazz, int paramIndex, Method method) {
        return ServletRequest.class.isAssignableFrom(clazz);
    }

    //获取参数值(如果是HttpServletRequest直接返回)
    @Override
    public synchronized Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex, Method method) {
        return request;
    }
}
