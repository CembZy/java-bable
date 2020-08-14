package com.cemb.mybatis.executor;

import java.sql.Connection;
import java.sql.SQLException;

//执行器
public interface Executor {

    //统一的查询方法
    <T> T doQuery(String statementPath, Object parameter) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException;

    Connection getConnection() throws SQLException;
}
