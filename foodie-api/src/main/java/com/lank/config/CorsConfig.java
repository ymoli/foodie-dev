package com.lank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
//跨域前后端联调
public class CorsConfig {

    public CorsConfig(){

    }

    @Bean
    public CorsFilter corsFilter(){
        //添加Cors配置信息
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");
        //configuration.addAllowedOrigin("http://47.106.207.223:8080");
        //configuration.addAllowedOrigin("http://47.106.207.223:8088");
        //configuration.addAllowedOrigin("http://47.106.207.223");
        //设置是否发送cookie
        configuration.setAllowCredentials(true);
        //设置允许的请求方式
        configuration.addAllowedMethod("*");
        //设置允许的Header
        configuration.addAllowedHeader("*");

        //2.为url添加映射路径，第一步的配置适用于所有的路由
        UrlBasedCorsConfigurationSource urlSource = new UrlBasedCorsConfigurationSource();
        urlSource.registerCorsConfiguration("/**",configuration);

        //3.返回重新定义好的urlSource
        return new CorsFilter(urlSource);
    }

}