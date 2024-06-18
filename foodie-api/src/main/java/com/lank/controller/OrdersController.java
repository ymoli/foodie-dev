package com.lank.controller;

import com.lank.enums.PayMethod;
import com.lank.pojo.bo.SubmitOrderBo;
import com.lank.service.OrderService;
import com.lank.utils.CookieUtils;
import com.lank.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "订单相关" , tags = {"订单相关的api"})
@RequestMapping("orders")
public class OrdersController extends BaseController {

    final static Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrderService orderService;


    @ApiOperation(value = "用户下单",notes = "用户下单",httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(@RequestBody SubmitOrderBo submitOrderBo, HttpServletRequest request, HttpServletResponse response){
        if (submitOrderBo.getPayMethod() != PayMethod.WEIXIN.type && submitOrderBo.getPayMethod() != PayMethod.ALIPAY.type ){
            return JSONResult.errorMsg("支付方式不支持");
        }
        //创建订单
        String orderId = orderService.createOrder(submitOrderBo);
        //创建订单后，移除购物车中已结算（已提交）的商品
        /*
        1001
        2002 -> 用户购买
        3003 -> 用户购买
        4004
        后端将2002，3003从redis购物车中清除，返回给前端，清除相关cookies
         */
        //TODO 整合redis后，完善购物车中的已结算商品清除，
        CookieUtils.setCookie(request,response,FOODIE_SHOPCAT,"",true);
        //向支付中心发送当前订单，用于保存支付中的订单数据
        return JSONResult.ok(orderId);
    }


}
