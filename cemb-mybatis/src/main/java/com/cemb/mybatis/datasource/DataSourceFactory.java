package com.cemb.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

//创建数据源的工厂类
public interface DataSourceFactory {

    //设置属性
    void setProperties();

    //获取数据源
    DataSource getDataSource();
}
