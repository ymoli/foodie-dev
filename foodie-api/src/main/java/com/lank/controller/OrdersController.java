package com.lank.controller;

import com.lank.enums.OrderStatusEnum;
import com.lank.enums.PayMethod;
import com.lank.pojo.OrderStatus;
import com.lank.pojo.bo.SubmitOrderBo;
import com.lank.pojo.vo.MerchantOrdersVo;
import com.lank.pojo.vo.OrderVo;
import com.lank.service.OrderService;
import com.lank.utils.CookieUtils;
import com.lank.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "订单相关" , tags = {"订单相关的api"})
@RequestMapping("orders")
public class OrdersController extends BaseController {

    final static Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;


    @ApiOperation(value = "用户下单",notes = "用户下单",httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(@RequestBody SubmitOrderBo submitOrderBo, HttpServletRequest request, HttpServletResponse response){
        if (submitOrderBo.getPayMethod() != PayMethod.WEIXIN.type && submitOrderBo.getPayMethod() != PayMethod.ALIPAY.type ){
            return JSONResult.errorMsg("支付方式不支持");
        }
        //1.创建订单
        OrderVo orderVo = orderService.createOrder(submitOrderBo);
        String orderId = orderVo.getOrderId();
        //创建订单后，移除购物车中已结算（已提交）的商品
        /*
        1001
        2002 -> 用户购买
        3003 -> 用户购买
        4004
        后端将2002，3003从redis购物车中清除，返回给前端，清除相关cookies
         */
        //TODO 2.整合redis后，完善购物车中的已结算商品清除，
        CookieUtils.setCookie(request,response,FOODIE_SHOPCAT,"",true);
        //3.向支付中心发送当前订单，用于保存支付中的订单数据
        MerchantOrdersVo merchantOrdersVo = orderVo.getMerchantOrdersVo();
        merchantOrdersVo.setReturnUrl(payReturnUrl);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("imoocUserId","10010");
        httpHeaders.add("password","123456");

        HttpEntity<MerchantOrdersVo> entity = new HttpEntity<>(merchantOrdersVo,httpHeaders);

        ResponseEntity<JSONResult> responseEntity = restTemplate.postForEntity(paymentUrl,entity,JSONResult.class);
        JSONResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200){
            return JSONResult.errorMsg("支付中心创建订单失败，请联系管理员");
        }

        return JSONResult.ok(orderId);
    }

    //微信支付成功调用支付中心，支付中心调用微信完成支付，然后支付中心调用后台修改后台订单状态
    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId){
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    //微信支付成功，支付中心修改状态成功，后台修改状态成功，前端需要轮循该接口，跳转到支付成功页面
    @PostMapping("/getPaidOrderInfo")
    public JSONResult getPaidOrderInfo(String orderId){
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return JSONResult.ok(orderStatus);
    }
}
