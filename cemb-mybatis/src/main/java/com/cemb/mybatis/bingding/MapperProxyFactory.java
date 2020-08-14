package com.cemb.mybatis.bingding;

import com.cemb.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

//创建代理类的工厂
public class MapperProxyFactory<T> {

    //获取代理类
    public static <T> T getMapperProxy(SqlSession sqlSession, Class<T> type) {
        MapperProxy mapperProxy = new MapperProxy(sqlSession, type);
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, mapperProxy);
    }

}
