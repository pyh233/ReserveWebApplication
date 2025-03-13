package com.example.psychroomserver.config;

import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * NOTE:保留热部署的原因：由于热部署会新建一个ClassLoader，而MybatisRedis默认存储的缓存是引用类型Object，
 * NOTE:使用了JDK序列化器，缓存读取的时候读取的是引用类型，因此会出现无法强转类型的情况
 * NOTE:因此想要使用MybatisRedis和热部署，只能将MybatisRedis存储的数据转化为JSON数据
 * NOTE:即改变MybatisRedis的序列化器
 * @CacheNamespace(implementation=MyBatisRedisCache.class)
 */
public class MyBatisRedisCache implements Cache {

    private final String id;
    private static RedisTemplate<Object, Object> redisTemplate;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public MyBatisRedisCache(final String id) {
        if (id == null) throw new IllegalArgumentException("Cache ID cannot be null");
        this.id = id;
    }

    public static void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
        MyBatisRedisCache.redisTemplate = redisTemplate;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Object removeObject(Object key) {
        redisTemplate.delete(key);
        return null;
    }

    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Override
    public int getSize() {
        return redisTemplate.keys("*").size();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }
}
