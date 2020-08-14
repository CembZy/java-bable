package com.cemb.jwt;

import com.cemb.jwt.common.intercepter.AppLoginIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private AppLoginIntercepter appLoginIntercepter;


    //配置静态资源访问路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }


    //将自定义的拦截器加入到拦截器队列中,并且配置需要拦截和不需要拦截的接口
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(appLoginIntercepter).addPathPatterns("/**").
                excludePathPatterns("/test/login", "test/logout", "/static/login.html");
    }


}
