package com.example.psychroomserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTests {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @DisplayName("测试Redis字符串类型存储")
    @Test
    public void testForRedisString() {
        redisTemplate.opsForValue().set("s1", "value1");
        redisTemplate.opsForValue().set("s2", "value2");
        redisTemplate.opsForValue().set("now", LocalDateTime.now());

        Object obj = redisTemplate.opsForValue().get("now");
        LocalDateTime now = (LocalDateTime) obj;

        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Assertions.assertNotNull(now);
    }

    @DisplayName("测试Redis存储数据过期时间")
    @Test
    public void testForRedisStringExpire() {
        redisTemplate.opsForValue().set("s4", "value2", Duration.ofSeconds(10));
        redisTemplate.opsForValue().set("s3", "value3", 15, TimeUnit.SECONDS);
    }

    @DisplayName("测试List")
    @Test
    public void test3ForRedisList() {
        redisTemplate.opsForList().leftPush("list1", List.of("1", "2", "3"));
        redisTemplate.opsForList().leftPush("list1", List.of("1", "2", "3"));
        redisTemplate.opsForList().set("list1", 1, "ddd");
    }

    @DisplayName("测试Set")
    @Test
    public void testForRedisSet() {
        redisTemplate.opsForSet().add("set1", "1", "2", "3", "2", "1");
        Long size = redisTemplate.opsForSet().size("set1");
        Assertions.assertEquals(3, size);
    }
    @DisplayName("Zset")
    @Test
    public void testForRedisZSet() {
        redisTemplate.opsForZSet().add("zset1","001",1);
        redisTemplate.opsForZSet().add("zset1","002",2);
        redisTemplate.opsForZSet().add("zset1","003",3);
        redisTemplate.opsForZSet().add("zset1","004",4);
        redisTemplate.opsForZSet().add("zset1","005",5);
        redisTemplate.opsForZSet().add("zset1","006",6);
    }
    @DisplayName("测试Hash")
    @Test
    public void testForRedisHash() {
        redisTemplate.opsForHash().put("hash1", "key1", "value1");
        redisTemplate.opsForHash().put("hash1", "key2", "value2");
        redisTemplate.opsForHash().put("hash1", "key3", "value3");
        redisTemplate.opsForHash().put("hash1", "key4", "value4");
    }
}
