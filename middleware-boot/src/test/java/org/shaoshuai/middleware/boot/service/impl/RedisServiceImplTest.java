package org.shaoshuai.middleware.boot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.shaoshuai.middleware.boot.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author yan
 * @Date 2026/1/9
 */
@SpringBootTest
@Slf4j
public class RedisServiceImplTest {
    @Autowired
    private RedisService redisService;

    @Test
    public void testSet(){
        redisService.set("testKey","testValue");
    }

    @Test
    public void testGet(){
        String value = redisService.get("testKey");
        log.info("value={}",value);
    }
}
