package com.xiaobu.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaobu
 * @version JDK1.8.0_171
 * @date on  2020/5/29 14:47
 * @description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLock {

    /**
     * 业务键
     *
     * @return
     */
    String key() default "redis-lock";
    /**
     * 锁的过期秒数,默认是5秒
     *
     */
    int expire() default 5;

    /**
     * 尝试加锁，最多等待时间
     *
     */
    long waitTime() default Long.MIN_VALUE;
    /**
     * 锁的超时时间单位
     *
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
