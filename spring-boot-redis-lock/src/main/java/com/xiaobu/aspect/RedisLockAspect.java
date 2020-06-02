package com.xiaobu.aspect;

import com.xiaobu.annotation.RedisLock;
import com.xiaobu.util.JedisLockUtils;
import com.xiaobu.util.JedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author xiaobu
 * @version JDK1.8.0_171
 * @date on  2020/6/2 15:58
 * @description
 */
@Aspect
@Component
@Slf4j
public class RedisLockAspect {
    @Autowired
    private JedisLockUtils jedisLockUtils;
    @Autowired
    private JedisUtils jedisUtils;

    @Around("@annotation(com.xiaobu.annotation.RedisLock)")
    public Object around(ProceedingJoinPoint joinPoint) {
        Jedis jedis = jedisUtils.getJedis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        String value = UUID.randomUUID().toString();
        String key = redisLock.key();
        try {
            final boolean islock = jedisLockUtils.lock(jedis,key, value, redisLock.expire(), redisLock.timeUnit());
            log.info("isLock : {}",islock);
            if (!islock) {
                log.error("获取锁失败");
                throw new RuntimeException("获取锁失败");
            }
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException("系统异常");
            }
        }  finally {
            log.info("释放锁");
            if (jedisLockUtils.unlock(jedis,key, value)){
                log.info("锁释放成功");
            };
            jedis.close();
        }
    }
}