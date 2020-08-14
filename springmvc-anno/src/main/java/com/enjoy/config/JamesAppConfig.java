package com.enjoy.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.enjoy.controller.JamesInterceptor;


//SpringMVC只扫描Controller；子容器
//useDefaultFilters=false 禁用默认的过滤规则；
@ComponentScan(value = "com.enjoy", includeFilters = {
        @Filter(type = FilterType.ANNOTATION, classes = {Controller.class})
}, useDefaultFilters = false)
@EnableWebMvc
public class JamesAppConfig extends WebMvcConfigurerAdapter {

    //定制视图解析器
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //比如我们想用JSP解析器,默认所有的页面都从/WEB-INF/AAA.jsp
        registry.jsp("/WEB-INF/pages/", ".jsp");
    }

    //静态资源访问,静态资源交给tomcat来处理
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        //开启静态资源访问
        configurer.enable();
    }

    //拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //将自定义的拦截器注册到拦截器队列中
        registry.addInterceptor(new JamesInterceptor()).addPathPatterns("/**");
    }
}
