package com.springboot.demo.config;

import com.springboot.demo.plugin.MyPlugin;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.apache.ibatis.session.Configuration;
import java.util.Properties;

/**
 * @ClassName: MapperConfig
 * @Auther: CemB
 * @Description:
 * @Email: 729943791@qq.com
 * @Company: 东方瑞云
 * @Date: 2018/12/23 15:32
 */

@org.springframework.context.annotation.Configuration
@MapperScan({"com.springboot.demo.mapper"})
public class MapperConfig {

    //注册插件
    @Bean
    public MyPlugin myPlugin() {
        MyPlugin myPlugin = new MyPlugin();
        //设置参数，比如阈值等，可以在配置文件中配置，这里直接写死便于测试
        Properties properties = new Properties();
        //这里设置慢查询阈值为1毫秒，便于测试
        properties.setProperty("time", "1");
        myPlugin.setProperties(properties);
        return myPlugin;
    }

    //将插件加入到mybatis插件拦截链中
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(Configuration configuration) {
                //插件拦截链采用了责任链模式，执行顺序和加入连接链的顺序有关
                MyPlugin myPlugin = new MyPlugin();
                //设置参数，比如阈值等，可以在配置文件中配置，这里直接写死便于测试
                Properties properties = new Properties();
                //这里设置慢查询阈值为1毫秒，便于测试
                properties.setProperty("time", "1");
                myPlugin.setProperties(properties);
                configuration.addInterceptor(myPlugin);
            }
        };
    }
}
