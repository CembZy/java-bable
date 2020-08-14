package com.product.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


@Configuration
//扫描dao
@MapperScan({"com.product.demo.dao"})
public class MapperConfig {
}
