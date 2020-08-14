package com.cemb.mybatis.datasource.unpool;

import com.cemb.mybatis.config.Configuration;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

//不使用数据库连接池的连接
public class UnPoolDataSource implements DataSource {

    private ClassLoader driverClassLoader;//驱动类的类加载器
    private static Map<String, Driver> registeredDrivers = new ConcurrentHashMap<String, Driver>();//缓存已注册的数据库驱动类

    private String driver;
    private String url;
    private String username;
    private String password;

    private final Configuration configuration = Configuration.newConfiguration();

    private Boolean autoCommit;//是否自动提交

    static {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            registeredDrivers.put(driver.getClass().getName(), driver);
        }
    }

    public UnPoolDataSource() {
        this.driver = configuration.getJdbcDriver();
        this.url = configuration.getJdbcUrl();
        this.username = configuration.getJdbcName();
        this.password = configuration.getJdbcPassword();
    }

    public UnPoolDataSource(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public UnPoolDataSource(String driver, String url, Properties driverProperties) {
        this.driver = driver;
        this.url = url;
    }

    public UnPoolDataSource(ClassLoader driverClassLoader, String driver, String url, String username, String password) {
        this.driverClassLoader = driverClassLoader;
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public UnPoolDataSource(ClassLoader driverClassLoader, String driver, String url, Properties driverProperties) {
        this.driverClassLoader = driverClassLoader;
        this.driver = driver;
        this.url = url;
    }

    public Connection getConnection() throws SQLException {
        return getConnection(username, password);
    }

    //获取连接
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        //初始化驱动
        initializeDriver();
        //获取连接
        Connection connection = DriverManager.getConnection(url, username, password);
        //设置事务是否自动提交，事务的隔离级别
        configureConnection(connection);
        return connection;
    }


    //初始化驱动
    private synchronized void initializeDriver() throws SQLException {
        if (!registeredDrivers.containsKey(driver)) {
            Class<?> driverType;
            try {
                driverType = Class.forName(driver, true, driverClassLoader);
                Driver driverInstance = (Driver) driverType.newInstance();
                registeredDrivers.put(driver, driverInstance);
            } catch (Exception e) {
                throw new SQLException("Error setting driver on UnpooledDataSource. Cause: " + e);
            }
        }
    }

    //设置事务是否是自动提交
    protected void configureConnection(Connection connection) throws SQLException {
        if (autoCommit != null && autoCommit != connection.getAutoCommit()) {
            connection.setAutoCommit(autoCommit);
        }
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    public void setLoginTimeout(int seconds) throws SQLException {

    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public ClassLoader getDriverClassLoader() {
        return driverClassLoader;
    }

    public void setDriverClassLoader(ClassLoader driverClassLoader) {
        this.driverClassLoader = driverClassLoader;
    }

    public static Map<String, Driver> getRegisteredDrivers() {
        return registeredDrivers;
    }

    public static void setRegisteredDrivers(Map<String, Driver> registeredDrivers) {
        UnPoolDataSource.registeredDrivers = registeredDrivers;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(Boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
}
