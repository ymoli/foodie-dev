package com.lank.cbk.apiUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    //在天天商城中这么写
//    @Bean
//    public RestTemplate restTemplateWeb(RestTemplateBuilder builder) {
//        return builder.build();
//    }
}
