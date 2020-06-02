package com.xiaobu.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaobu
 * @version JDK1.8.0_171
 * @date on  2020/5/29 16:12
 * @description
 */
@Component
public class JedisLockUtils {


    /**
     * 不建议
     */
    public boolean lockSetnx(Jedis jedis,String key,String value,int timeout){
       long result= jedis.setnx(key, value);
        if (result==1L) {
            //若在这里程序突然崩溃，则无法设置过期时间，将发生死锁
           return jedis.expire(key, timeout)==1L;
        }else {
            return false;
        }
    }



    /**
     * 使用Lua脚本，脚本中使用setnex+expire命令进行加锁操作
     */
    public boolean lockWithLua(Jedis jedis, String key, String uniqueId, int seconds) {
        String luaScripts = "if redis.call('setnx',KEYS[1],ARGV[1]) == 1 then" +
                "redis.call('expire',KEYS[1],ARGV[2]) return 1 else return 0 end";
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        keys.add(key);
        values.add(uniqueId);
        values.add(String.valueOf(seconds));
        Object result = jedis.eval(luaScripts, keys, values);
        //判断是否成功
        return result.equals(1L);
    }

    /**
     * 在Redis的2.6.12及以后中,使用 set key value [NX] [EX] 命令
     */
    public boolean lock(Jedis jedis,String key, String value, int timeout, TimeUnit timeUnit) {
        long seconds = timeUnit.toSeconds(timeout);
        return "OK".equals(jedis.set(key, value, "NX", "EX", seconds));
    }





    /**
     * 使用Lua脚本进行解锁操纵，解锁的时候验证value值
     */
    public boolean unlock(Jedis jedis,String key,String value) {
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then " +
                "return redis.call('del',KEYS[1]) else return 0 end";
        return jedis.eval(luaScript, Collections.singletonList(key), Collections.singletonList(value)).equals(1L);
    }



}