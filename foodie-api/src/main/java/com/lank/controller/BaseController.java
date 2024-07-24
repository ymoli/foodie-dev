package com.lank.controller;

import com.lank.pojo.Orders;
import com.lank.service.center.MyOrdersService;
import com.lank.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    @Autowired
    private MyOrdersService myOrdersService;

    public static final Integer COMMENT_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;
    public static final String FOODIE_SHOPCAT = "shopcat";

    //微信支付成功 -> 支付中心 -> 天天吃货平台后端
    //                     -> 回调通知的url
    String payReturnUrl = "http://192.168.125.128:8088/foodie-api/orders/notifyMerchantOrderPaid";

    //支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    //用户上传头像的位置,使用File.separator代替/，在不同环境分割符不同
//    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "workspace" +
//                                                            File.separator + "images" +
//                                                            File.separator + "foodie" +
//                                                            File.separator + "face";


    //用于验证订单和用户是否有关联，避免非法调用
    public JSONResult checkUserOrder(String orderId, String userId){
        Orders orders = myOrdersService.queryMyOrder(userId,orderId);
        if (orders == null){
            return JSONResult.errorMsg("订单不存在");
        }
        return JSONResult.ok(orders);
    }

}
