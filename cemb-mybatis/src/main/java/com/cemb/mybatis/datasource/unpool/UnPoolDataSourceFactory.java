package com.cemb.mybatis.datasource.unpool;

import com.cemb.mybatis.config.Configuration;
import com.cemb.mybatis.datasource.DataSourceFactory;
import com.cemb.mybatis.session.SqlSessionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UnPoolDataSourceFactory implements DataSourceFactory {

    private final DataSource dataSource;

    private final Configuration configuration;


    public UnPoolDataSourceFactory(Configuration configuration) {
        this.configuration = configuration;
        setProperties();
        this.dataSource = new UnPoolDataSource();
    }

    //设置jdbc配置信息
    public void setProperties() {
        InputStream dbIn = SqlSessionFactory.class.getClassLoader().getResourceAsStream(configuration.DB_CONFIG_FILE);
        Properties p = new Properties();
        try {
            p.load(dbIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration.setJdbcDriver(p.get("jdbc.driver").toString());
        configuration.setJdbcPassword(p.get("jdbc.password").toString());
        configuration.setJdbcUrl(p.get("jdbc.url").toString());
        configuration.setJdbcName(p.get("jdbc.username").toString());
    }

    //返回数据源
    public DataSource getDataSource() {
        return dataSource;
    }
}
