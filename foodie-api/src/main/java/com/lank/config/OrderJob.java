package com.lank.config;

import com.lank.service.OrderService;
import com.lank.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;

    /**
     * 定时任务存在佷多的弊端，比较明显的弊端是：
     * 1.会有时间差，不严谨：
     *      10:39下单，11:00检查不足1小时，12:00检查超过一小时，多余39分钟
     * 2.不支持集群，或者说需要其他的条件支撑才能在集群条件下跑，否则会出现多个服务器同时关闭相同订单的现象
     *      解决方案：使用一台计算机节点，运算所有的定时任务
     * 3.效率低。全表扫描极大浪费mysql的查询性能。导致其他业务阻塞。所以定时扫描这种在稍微大一点的体量时就不适用了。
     *      解决方案：用消息队列MQ来做一个延时队列出来处理。后续将会进行改进。
     *      10:12分下单，10，11:12分检查当前为10，直接关闭订单
     *  定时任务仅适用于小型轻量型项目/传统项目
     */
    @Scheduled(cron = "* * * 1 * ?")
    public void autoCloseOrder(){
        System.out.println("定时任务执行时间："+ DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
        orderService.closeOrder();
    }
}
