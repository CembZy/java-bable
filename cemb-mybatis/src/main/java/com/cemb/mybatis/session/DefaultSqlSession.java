package com.cemb.mybatis.session;

import com.cemb.mybatis.config.Configuration;

import java.sql.SQLException;
import java.util.List;

import com.cemb.mybatis.executor.Executor;
import com.cemb.mybatis.executor.SimpleExecutor;

public class DefaultSqlSession implements SqlSession {

    //全局配置类
    private final Configuration configuration;

    //执行器
    private final Executor executor;

    DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        this.executor = new SimpleExecutor(configuration);
    }

    public <T> T selectOne(String statementPath, Object parameter) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        List<T> objects = selectList(statementPath, parameter);
        if (null == objects || objects.size() == 0) {
            return null;
        }
        return objects.get(0);
    }

    public <E> List<E> selectList(String statementPath, Object parameter) throws SQLException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        return executor.doQuery(statementPath, parameter);
    }

    //通过动态代理获取增强后的mapper接口
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }
}
