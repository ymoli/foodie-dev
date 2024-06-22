package com.lank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//swagger实现自动生成前端api
public class Swagger2 {

    //原地址 http://localhost:8088/swagger-ui.html
    //bootstrap地址 http://localhost:8088/doc.html

    //配置swagger2的核心配置 docket
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2) //指定api类型为swagger2
                .apiInfo(apiInfo()) //用于定义api文档汇总信息
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lank.controller")) //指定controller
                .paths(PathSelectors.any())  //所有controller
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("天天吃货 电商平台api接口") //文档标题
                .contact(new Contact("lank","","250754396@qq.com"))//作者信息
                .description("天天吃货api文档") //描述
                .version("1.0")//版本号
                .termsOfServiceUrl("")//网站地址
                .build();  // 构建ApiInfoBuilder对象
    }

}
