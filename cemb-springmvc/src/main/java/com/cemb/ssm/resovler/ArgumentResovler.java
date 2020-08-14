package com.cemb.ssm.resovler;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

//使用策略模式--方法解析器
public interface ArgumentResovler {

    //判断是否属于某个解析器
    boolean support(Class<?> clazz, int paramIndex, Method method);

    //解析参数得到参数值
    Object argumentResolver(HttpServletRequest request,
                            HttpServletResponse response, Class<?> type,
                            //参数索引下坐标,有很多注解,你得知道是哪个参数的注解,每个参数的索引顺序不一样
                            int paramIndex,
                            Method method) throws IllegalAccessException, InstantiationException;
}
