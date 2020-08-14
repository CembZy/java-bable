package com.cemb.springboot.springbootredis.test;

import com.cemb.springboot.springbootredis.model.User;
import com.cemb.springboot.springbootredis.redis.TestRedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @ClassName: RedisTest
 * @Auther: CemB
 * @Description: 测试
 * @Email: 729943791@qq.com
 * @Date: 2018/7/11 17:54
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;

    @Autowired
    private TestRedisUtil testRedisUtil;


    @Test
    public void test1() {
        // TODO 测试并发
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        IntStream.range(0, 1000).forEach(i ->
                executorService.execute(()
                        ->
                        stringRedisTemplate.opsForValue().increment("num", 1))
        );//increment --> x++

        stringRedisTemplate.opsForValue().set("k1", "v1");
        final String v1 = stringRedisTemplate.opsForValue().get("k1");
        System.out.println("字符缓存结果：" + v1);


        String key = "cb";
        redisCacheTemplate.opsForValue().set(key, new User("CemB", 18));
        final User user = (User) redisCacheTemplate.opsForValue().get(key);

        System.out.println("对象缓存结果：name==" + user.getName() + "age====" + user.getAge());
    }


    @Test
    public void test2() throws Exception {
        // TODO 测试并发
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        IntStream.range(0, 1000).forEach(i ->
                executorService.execute(()
                        ->
                {
                    try {
                        testRedisUtil.set("num", "1");
                        testRedisUtil.delete("num");
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                })
        );

        testRedisUtil.set("k1", "v1");
        final String v1 = testRedisUtil.get("k1");
        System.out.println("字符缓存结果：" + v1);


        String key = "cb";
        testRedisUtil.set(key, new User("CemB", 18));
        final User user = testRedisUtil.get(key, User.class);

        System.out.println("对象缓存结果：name==" + user.getName() + "age====" + user.getAge());
    }
}
