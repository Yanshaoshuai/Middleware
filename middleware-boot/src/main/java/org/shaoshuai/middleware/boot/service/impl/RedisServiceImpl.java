package org.shaoshuai.middleware.boot.service.impl;

import io.lettuce.core.KeyValue;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import lombok.AllArgsConstructor;
import org.shaoshuai.middleware.boot.service.RedisService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author yan
 * @Date 2026/1/9
 */
@Service
@AllArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisClusterClient redisClusterClient;

    @Override
    public void set(String key, String value) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            connection.sync().set(key, value);
        }
    }

    @Override
    public void mset(Map<String, String> ketValues) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            connection.sync().mset(ketValues);
        }
    }

    @Override
    public void setEx(String key, String value, Long expire) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            connection.sync().setex(key, expire, value);
        }
    }

    @Override
    public void del(String key) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            connection.sync().del(key);
        }
    }

    @Override
    public void delKeys(String... keys) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            connection.sync().del(keys);
        }
    }

    @Override
    public String get(String key) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            return connection.sync().get(key);
        }
    }

    @Override
    public Map<String, String> mget(String... keys) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            List<KeyValue<String, String>> keyValueList = connection.sync().mget(keys);
            if (!CollectionUtils.isEmpty(keyValueList)) {
                return keyValueList.stream().collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue));
            }
        }
        return Collections.emptyMap();
    }

    @Override
    public void sadd(String key, String... members) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            connection.sync().sadd(key, members);
        }
    }

    @Override
    public Set<String> smembers(String key) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            return connection.sync().smembers(key);
        }
    }

    @Override
    public boolean sismember(String key, String value) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            return connection.sync().sismember(key, value);
        }
    }

    @Override
    public Map<String, Boolean> smismember(String key, String... value) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            if (value == null || value.length == 0) {
                return Collections.emptyMap();
            }
            List<Boolean> results = connection.sync().smismember(key, value);
            Map<String, Boolean> resultMap = new HashMap<>();
            for (int i = 0; i < value.length; i++) {
                resultMap.put(value[i], results.get(i));
            }
            return resultMap;
        }
    }

    public void hset(String key, String field, String value) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            connection.sync().hset(key, field, value);
        }
    }


    public void hmset(String key, Map<String, String> fieldValues) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            connection.sync().hmset(key, fieldValues);
        }
    }

    public String hget(String key, String field) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            return connection.sync().hget(key, field);
        }
    }

    public Map<String, String> hmget(String key, String... fields) {
        try (StatefulRedisClusterConnection<String, String> connection = redisClusterClient.connect()) {
            List<KeyValue<String, String>> fieldValues = connection.sync().hmget(key, fields);
            if (CollectionUtils.isEmpty(fieldValues)) {
                return Collections.emptyMap();
            }
            return fieldValues.stream().collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue));
        }
    }
}
