package com.xiaobu.controller;

import com.xiaobu.annotation.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaobu
 * @version JDK1.8.0_171
 * @date on  2020/6/2 16:03
 * @description
 */
@RestController
@Slf4j
public class RedisController {


    @RedisLock(key = "lock1")
    @GetMapping("/index")
    public String index(){
        log.info("进入index方法");
        return "index";
    }
}