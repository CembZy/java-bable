package com.cemb.mybatis.executor;

import com.cemb.mybatis.config.Configuration;
import com.cemb.mybatis.config.MappedStatement;
import com.cemb.mybatis.datasource.unpool.UnPoolDataSourceFactory;
import com.cemb.mybatis.executor.parameter.DefaultParameterHandler;
import com.cemb.mybatis.executor.parameter.ParameterHandler;
import com.cemb.mybatis.executor.resultset.DefaultResultHandler;
import com.cemb.mybatis.executor.resultset.ResultHandler;
import com.cemb.mybatis.executor.statement.DefaultStatmentHandler;
import com.cemb.mybatis.executor.statement.StatmentHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SimpleExecutor implements Executor {
    //全局配置类
    private final Configuration configuration;

    public SimpleExecutor(Configuration configuration) {
        this.configuration = configuration;
    }

    public <T> T doQuery(String statementPath, Object parameter) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        //获取MappedStatement对象，里面有封装的sql语句
        MappedStatement mappedStatement = configuration.getMappedStatements().get(statementPath);

        //1，获取连接
        Connection connection = getConnection();
        if (null != connection) {
            //2，获取执行数据库操作的StatmentHandler
            StatmentHandler statmentHandler = new DefaultStatmentHandler(mappedStatement);
            //3，获取PreparedStatement
            PreparedStatement preparedStatement = statmentHandler.prepare(connection);
            //4，填充参数值
            ParameterHandler parameterHandler = new DefaultParameterHandler(parameter);
            parameterHandler.setParameters(preparedStatement);
            //5，处理结果集
            ResultHandler resultHandler = new DefaultResultHandler(mappedStatement);
            List<T> resultSets = resultHandler.handleResultSets(statmentHandler.query(preparedStatement));

            return (T) resultSets;
        }

        return null;
    }

    //获取数据库连接
    @Override
    public Connection getConnection() throws SQLException {
        UnPoolDataSourceFactory unPoolDataSource = new UnPoolDataSourceFactory(configuration);
        return unPoolDataSource.getDataSource().getConnection(configuration.getJdbcName(), configuration.getJdbcPassword());
    }
}
