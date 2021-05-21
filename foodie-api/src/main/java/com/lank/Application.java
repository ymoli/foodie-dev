package com.lank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/*
* springboot的启动类
*
* */
@SpringBootApplication
//扫描mybatis通用mapper所在的包
@MapperScan(basePackages = "com.lank.mapper")
//org.n3r.idworker包保证数据库主键全程唯一，方便后期分库分表
@ComponentScan(basePackages = {"com.lank","org.n3r.idworker"})
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
