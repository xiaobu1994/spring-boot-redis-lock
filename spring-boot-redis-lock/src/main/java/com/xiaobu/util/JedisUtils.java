package com.xiaobu.util;

import com.xiaobu.properties.RedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaobu
 * @version JDK1.8.0_171
 * @date on  2020/5/29 14:56
 * @description jedis工具类
 */
@Component
@Slf4j
public class JedisUtils {

    @Autowired
    private RedisProperties redisProperties;

    private Map<String, JedisPool> map = new ConcurrentHashMap<>();

    /**
     * 功能描述:1.8 getOrDefault方法  也可以先判断是否含有该key map.containsKey(key) 然后在考虑是put还是直接返回
     *
     * @return redis.clients.jedis.JedisPool
     * @author xiaobu
     * @date 2020/5/29 15:26
     * @version 1.0
     */
    private JedisPool getJedisPool() {
        String key = redisProperties.getHost() + ":" + redisProperties.getHost();
        JedisPool jedisPool;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisProperties.getMaxIdle());
        config.setMaxWaitMillis(redisProperties.getMaxWait());
        //在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(true);
        //在连接对象返回时，是否测试对象的有效性,默认false
        config.setTestOnReturn(true);
        jedisPool = new JedisPool(config, redisProperties.getHost(), redisProperties.getPort(), redisProperties.getTimeOut()
                , redisProperties.getPassword());
        jedisPool = map.getOrDefault(key, jedisPool);
        return jedisPool;
    }



    public Jedis getJedis() {
        Jedis jedis = null;
        AtomicInteger count = new AtomicInteger(0);
        do {
            try {
                jedis = getJedisPool().getResource();
                count.incrementAndGet();
            } catch (Exception e) {
                log.error("get jedis failed ", e);
            }
        } while (jedis == null && count.intValue() < redisProperties.getRetryNum());
        return jedis;
    }

}