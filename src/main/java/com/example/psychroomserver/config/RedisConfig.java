package com.example.psychroomserver.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;


@Configuration
@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableTransactionManagement
public class RedisConfig implements WebMvcConfigurer {

    @Bean("myKeyGenerator")
    public KeyGenerator RedisConfigKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                return method.getName() + params.length + Arrays.deepToString(params);
            }
        };
    }

    // 配置Jackson
    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer(ObjectMapper mapper) {
        //克隆一个新的ObjectMapper实例
        ObjectMapper om = mapper.copy();
        //添加对jdk8日期类型的支持，需要在pom文件中引入jackson-datatype-jsr310
        om.registerModule(new JavaTimeModule());
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //必须进行以下配置，否则会出现java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to xxx的异常
        //详见：https://knife.blog.csdn.net/article/details/122427607
        om.activateDefaultTyping(om.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式：JdkSerializationRedisSerializer）
        return new Jackson2JsonRedisSerializer<>(om, Object.class);
    }

    // RedisTemplate配置(显式设置)
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory, Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 使用Jackson2JsonRedisSerializer来设置value的序列化器
        template.setDefaultSerializer(jackson2JsonRedisSerializer);

        // 设置key的序列化器（一般可以使用StringRedisSerializer）
        template.setKeySerializer(new StringRedisSerializer());

        // 设置hash key和value的序列化器
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        return template;
    }

    //缓存管理器 SpringRedis配置(显式设置)
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory, Jackson2JsonRedisSerializer<Object> valueSerializer) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                .entryTtl(Duration.ofSeconds(3600));// 设置缓存全局统一有效期，ttl表示time to live，即存活时间。
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }
}
