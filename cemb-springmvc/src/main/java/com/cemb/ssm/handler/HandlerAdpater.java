package com.cemb.ssm.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

//执行方法的适配器
public interface HandlerAdpater {

    //执行方法
    Object[] handlerMethod(HttpServletRequest request,
                           HttpServletResponse response,
                           //要执行的方法
                           Method method,
                           //容器中的bean
                           Map<Object, Object> beans) throws InstantiationException, IllegalAccessException;

}
