package org.shaoshuai.middleware.boot.service;

import java.util.Map;
import java.util.Set;

public interface RedisService {
    void set(String key, String value);

    void mset(Map<String, String> ketValues);

    void setEx(String key, String value, Long expire);

    void del(String key);

    void delKeys(String... keys);

    String get(String key);

    Map<String, String> mget(String... keys);

    void sadd(String key, String... members);

    Set<String> smembers(String key);

    boolean sismember(String key, String value);

    Map<String, Boolean> smismember(String key, String... value);

    void hset(String key, String field, String value);

    void hmset(String key, Map<String, String> fieldValues);

    String hget(String key, String field);

    Map<String, String> hmget(String key, String... fields);
}
