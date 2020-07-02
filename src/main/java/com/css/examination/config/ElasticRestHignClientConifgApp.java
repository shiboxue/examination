package com.css.examination.config;

import com.google.gson.GsonBuilder;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticRestHignClientConifgApp {
    public static final Logger logger = LoggerFactory.getLogger(ElasticRestHignClientConifgApp.class);
    @Value("${es.server.ip}")
    private String esServerIp;
    @Value("${es.server.port}")
    private String esServerPort;
    @Bean
    public JestClient jestClient() {
        JestClientFactory factory = new JestClientFactory();
        final String serverUrl = "http://"+esServerIp+":"+esServerPort;
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(serverUrl)
                .gson(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create())
                .multiThreaded(true)
                .readTimeout(10000)
                .build());
        return factory.getObject();
    }
    @Bean(name="LocalRestHighLevelClient")
    public RestHighLevelClient restHighLevelClient() {
        final RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(esServerIp, Integer.parseInt(esServerPort), "http")));
        return client;
    }
}
