package org.shaoshuai.middleware.boot.config.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yan
 * @Date 2026/1/5
 * https 问题
 * 1. 下载/home/elasticsearch/elasticsearch-8.17.0/config/certs/http_ca.crt
 * 2. sudo keytool -import -file http_ca.crt  -storepass changeit -keystore $JAVA_HOME/lib/security/cacerts -alias escert
 * ApiKey创建: kibana -> Management -> Stack management -> API Keys
 */
@Configuration
public class ElasticSearchConfig {
    @Bean
    public ElasticsearchClient restClient() {
        RestClient restClient = RestClient.builder(HttpHost.create("https://192.168.1.21:9200"),
                        HttpHost.create("https://192.168.1.22:9200"),
                        HttpHost.create("https://192.168.1.23:9200"))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + "SFlyc2pwc0IybE5rRjQ0OUYtQVI6V3V3Rk9fS05SMk95UFF6OU50UnRIQQ==")
                })
                .build();
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
