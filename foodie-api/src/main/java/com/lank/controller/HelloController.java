package com.lank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore  // swagger文档中不展示该接口
public class HelloController {

    final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public Object hello(){
        logger.debug("debug:hello~");
        logger.info("info:hello~");
        logger.warn("warn:hello~");
        logger.error("error:hello~");
        return "hello world!";
    }
}
