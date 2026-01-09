package org.shaoshuai.middleware.boot.config.redis;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author yan
 * @Date 2026/1/9
 */
@Configuration
public class RedisClusterConfig {
    @Bean
    @ConfigurationProperties(prefix = "redis.cluster")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    @Bean(destroyMethod = "shutdown")
    public RedisClusterClient redisClusterClient(RedisProperties redisProperties) {
        List<RedisURI> redisURIS = new ArrayList<>(redisProperties.getNodes().size());

        for (RedisProperties.RedisNode node : redisProperties.getNodes()) {
            RedisURI.Builder builder = RedisURI.builder()
                    .withHost(node.getHost())
                    .withPort(node.getPort())
                    .withDatabase(node.getDb())
                    .withTimeout(Duration.ofMillis(redisProperties.getTimeout()));
            if (node.getPassword() != null) {
                builder.withPassword(node.getPassword().toCharArray());
            }
            redisURIS.add(builder.build());
        }
        return RedisClusterClient.create(redisURIS);
    }
}
