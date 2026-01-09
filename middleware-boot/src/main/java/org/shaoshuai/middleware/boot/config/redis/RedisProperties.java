package org.shaoshuai.middleware.boot.config.redis;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author yan
 * @Date 2026/1/9
 */
@Getter
@Setter
public class RedisProperties {
    private List<RedisNode> nodes;
    private Integer timeout;

    @Getter
    @Setter
    public static class RedisNode {
        private String host;
        private Integer port;
        private String password;
        private Integer db;
        public RedisNode() {
            this.db = 0;
        }
    }
}
