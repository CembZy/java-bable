package com.cemb.mybatis.executor.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//处理数据库操作类
public interface StatmentHandler {

    //从连接中获取一个Statement
    PreparedStatement prepare(Connection connection) throws SQLException;

    //执行select语句
    ResultSet query(PreparedStatement statement) throws SQLException;
}
