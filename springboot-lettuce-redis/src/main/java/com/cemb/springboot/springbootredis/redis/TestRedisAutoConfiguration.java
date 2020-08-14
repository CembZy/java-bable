package com.cemb.springboot.springbootredis.redis;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.io.Serializable;

/**
 * @ClassName: RedisTemplate
 * @Auther: CemB
 * @Description: redis数据操作模板
 * @Email: 729943791@qq.com
 * @Date: 2018/7/11 17:43
 */

// 从Spring3.0，@Configuration用于定义配置类，可替换xml配置文件，被注解的类内部包含有一个或多个被@Bean注解的方法，
// 这些方法将会被AnnotationConfigApplicationContext或AnnotationConfigWebApplicationContext类进行扫描，并用于构建bean定义，
// 初始化Spring容器。相当于将这个配置文件导入到spring.xml中
@Configuration
// 在RedisAutoConfiguration配置类被加载之后才允许加载
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class TestRedisAutoConfiguration {

    // 这里使用Lettuce客户端程序，Lettuce功能比Jedis更加强大。
    @Bean
    public RedisTemplate<String, Serializable> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    // 加入操作字符串的ValueOperations工具类到IOC
    @Bean
    public ValueOperations valueOperations(RedisTemplate redisTemplate) {
        return redisTemplate.opsForValue();
    }

}
