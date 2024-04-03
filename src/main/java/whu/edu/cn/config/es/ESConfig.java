package whu.edu.cn.config.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ESConfig {
    ///**
// * 配置RestClient
// */
    @Bean
    public RestHighLevelClient cilent() {
        return new RestHighLevelClient(
                RestClient.builder(HttpHost.create("http://192.168.88.100:9200"))
        );
    }
}
