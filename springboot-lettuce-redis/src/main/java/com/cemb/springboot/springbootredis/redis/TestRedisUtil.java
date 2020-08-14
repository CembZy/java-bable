package com.cemb.springboot.springbootredis.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: TestRedisUtil
 * @Auther: CemB
 * @Description: Redis工具组件
 * @Email: 729943791@qq.com
 * @Date: 2018/7/12 09:20
 */

@Component
public class TestRedisUtil {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private ValueOperations<Object, Object> valueOperations;


    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    public final static long NOT_EXPIRE = -1;


    public void set(String key, Object value, long expire) throws JsonProcessingException {
        expireJob(key, expire);
        valueOperations.set(key, new ObjectMapper().writeValueAsString(value));
    }

    public void set(String key, Object value) throws JsonProcessingException {
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) throws IOException {
        String value = (String) valueOperations.get(key);
        expireJob(key, expire);
        return value != null ? new ObjectMapper().readValue(value, clazz) : null;
    }

    public <T> T get(String key, Class<T> clazz) throws IOException {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long exprie) throws IOException {
        return get(key, String.class, exprie);
    }

    public String get(String key) throws IOException {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key, long expire) {
        expireJob(key, expire);
        redisTemplate.delete(key);
    }

    public void delete(String key) {
        delete(key, NOT_EXPIRE);
    }

    private void expireJob(String key, long expire) {
        if (expire != NOT_EXPIRE) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

}
