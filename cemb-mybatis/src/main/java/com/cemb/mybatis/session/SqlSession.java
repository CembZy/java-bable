package com.cemb.mybatis.session;

import java.sql.SQLException;
import java.util.List;

//对外提供的接口
public interface SqlSession {

    //根据传入的条件查询结果
    <T> T selectOne(String statementPath, Object parameter) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException;

    //根据条件经过查询，返回泛型集合
    <E> List<E> selectList(String statementPath, Object parameter) throws SQLException, IllegalAccessException, ClassNotFoundException, InstantiationException;

    //根据mapper接口获取接口对应的动态代理实现
    <T> T getMapper(Class<T> type);

}
