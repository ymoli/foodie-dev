package com.lank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/*
* springboot的启动类
*
* @SpringBootApplication由@SpringBootConfiguration、@EnableAutoConfiguration、@ComponentScan组成
* @ComponentScan默认扫描Application.class所在包下的所有类及其子类
* @SpringBootConfiguration是一个接口，由@Configuration组成，是一个配置文件的容器
* @EnableAutoConfiguration用于开启自动装备。
* AutoConfigurationImportSelector.class中配置自动装配的相关代码，spring.factories中配置了自动装配的内容
*
* */
@SpringBootApplication
//扫描mybatis通用mapper所在的包
@MapperScan(basePackages = "com.lank.mapper")
//org.n3r.idworker包保证数据库主键全程唯一，方便后期分库分表
@ComponentScan(basePackages = {"com.lank","org.n3r.idworker"})
//开启事务管理的接口，在@SpringBootApplication注解中已经自动装配了，
@EnableTransactionManagement
//开启定时任务
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
