package com.cemb.mybatis.bingding;

import com.cemb.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;


//代理增强类
public class MapperProxy<T> implements InvocationHandler {

    private final SqlSession sqlSession;

    private final Class<T> mapper;

    MapperProxy(SqlSession sqlSession, Class<T> mapper) {
        this.sqlSession = sqlSession;
        this.mapper = mapper;
    }

    //判断是否是集合类型
    protected <T> boolean isCollection(Class<T> mapper) {
        return Collection.class.isAssignableFrom(mapper);
    }

    //增强
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果是Object本身的方法就不增强
        if (Object.class.equals(method.getDefaultValue())) {
            return method.invoke(proxy, args);
        }
        Class<?> returnType = method.getReturnType();
        // 根据不同的返回参数类型调用不同的sqlsession不同的方法
        if (isCollection(returnType)) {
            return sqlSession.selectList(mapper.getName() + "." + method.getName(), args);
        }
        return sqlSession.selectOne(mapper.getName() + "." + method.getName(), args);
    }


}
