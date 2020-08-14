package com.cemb.ssm.resovler.impl;

import com.cemb.ssm.annotation.CemBRequestParam;
import com.cemb.ssm.annotation.CemBService;
import com.cemb.ssm.resovler.ArgumentResovler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

//解析器
@CemBService("resqurestParamResovler")
public class ResqurestParamResovler implements ArgumentResovler {

    //判断是否是当前解析器
    @Override
    public synchronized boolean support(Class<?> clazz, int paramIndex, Method method) {
        Annotation[][] an = method.getParameterAnnotations();
        if (an == null) {
            return false;
        }
        Annotation[] paramAns = an[paramIndex];
        for (Annotation annotation : paramAns) {
            if (CemBRequestParam.class.isAssignableFrom(annotation.getClass())) {
                return true;
            }
        }
        return false;
    }

    //获取参数值
    @Override
    public synchronized Object argumentResolver(HttpServletRequest request, HttpServletResponse response,
                                                Class<?> clazz, int paramIndex, Method method) throws IllegalAccessException, InstantiationException {
        Annotation[][] an = method.getParameterAnnotations();
        if (an == null) {
            return false;
        }
        Annotation[] paramAns = an[paramIndex];
        for (Annotation annotation : paramAns) {
            if (CemBRequestParam.class.isAssignableFrom(annotation.getClass())) {
                CemBRequestParam cemBRequestMapping = (CemBRequestParam) annotation;
                if ("".equals(cemBRequestMapping.value())) {
                    Parameter parameter = (Parameter) clazz.newInstance();
                    return request.getParameter(parameter.getName());
                } else {
                    return request.getParameter(cemBRequestMapping.value());
                }

            }
        }
        return null;
    }
}
