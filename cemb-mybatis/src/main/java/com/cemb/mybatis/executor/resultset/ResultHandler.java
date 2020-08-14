package com.cemb.mybatis.executor.resultset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ResultHandler {

    //处理结果集
    <E> List<E> handleResultSets(ResultSet resultSet) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException;

}
