package com.cemb.mybatis.config;

import com.cemb.mybatis.bingding.MapperProxyFactory;
import com.cemb.mybatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//核心配置类，包含了mybatis整个配置信息，初始化阶段加载
public class Configuration {

    public static Configuration configuration;

    //记录mapper xml文件存放的位置
    public static final String MAPPER_CONFIG_LOCATION = "mapper";
    //记录数据库连接信息文件存放位置
    public static final String DB_CONFIG_FILE = "mybatis/db.properties";

    private String jdbcName;

    private String jdbcPassword;

    private String jdbcUrl;

    private String jdbcDriver;

    //mapper xml解析完以后select节点的信息存放在mappedStatements
    protected final Map<String, MappedStatement> mappedStatements = new ConcurrentHashMap<String, MappedStatement>();

    //mapper接口中的方法
    protected final Map<String, List> mapperMethods = new ConcurrentHashMap<String, List>();


    Configuration() {
    }

    //为mapper接口生成动态代理的方法
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return MapperProxyFactory.getMapperProxy(sqlSession, type);
    }


    public static Configuration newConfiguration() {
        if (configuration == null) {
            synchronized (Configuration.class) {
                if (configuration == null) {
                    configuration = new Configuration();
                }
            }
        }
        return configuration;
    }

    public String getJdbcName() {
        return jdbcName;
    }

    public void setJdbcName(String jdbcName) {
        this.jdbcName = jdbcName;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public Map<String, MappedStatement> getMappedStatements() {
        return mappedStatements;
    }

    public Map<String, List> getMapperMethods() {
        return mapperMethods;
    }


    @Override
    public String toString() {
        return "Configuration:{" +
                "jdbcName='" + jdbcName + '\'' +
                ", jdbcPassword='" + jdbcPassword + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", jdbcDriver='" + jdbcDriver + '\'' +
                '}';
    }
}
