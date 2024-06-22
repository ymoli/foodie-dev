package com.lank.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //实现静态资源的映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")//映射swagger2，否则会导致404
                .addResourceLocations("file:E:\\workspace\\images\\");//这个就是映射的路径，下面的文件就可以通过localhost:8088/foodie/face/userId/xxx.png来访问了
    }
}
