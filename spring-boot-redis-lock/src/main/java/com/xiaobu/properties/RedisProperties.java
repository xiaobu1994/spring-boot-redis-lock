package com.xiaobu.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xiaobu
 * @version JDK1.8.0_171
 * @date on  2020/5/29 14:21
 * @description
 */
@Component
@Data
public class RedisProperties {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.database}")
    private Integer database;

    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer maxActive;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer maxIdle;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private Long maxWait;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer minIdle;
    @Value("${spring.redis.jedis.time-out}")
    private Integer timeOut;
    @Value("${spring.redis.jedis.retry-num}")
    private Integer retryNum;
}