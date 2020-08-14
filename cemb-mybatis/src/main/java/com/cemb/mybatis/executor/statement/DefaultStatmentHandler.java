package com.cemb.mybatis.executor.statement;

import com.cemb.mybatis.config.MappedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultStatmentHandler implements StatmentHandler {

    private MappedStatement mappedStatment;

    public DefaultStatmentHandler(MappedStatement mappedStatment) {
        this.mappedStatment = mappedStatment;
    }

    @Override
    public PreparedStatement prepare(Connection connection) throws SQLException {
        return connection.prepareStatement(mappedStatment.getSql());
    }

    @Override
    public ResultSet query(PreparedStatement statement) throws SQLException {
        return statement.executeQuery();
    }
}
