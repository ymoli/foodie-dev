package com.lank.service;

import com.lank.pojo.bo.SubmitOrderBo;

public interface OrderService {

    //用于创建订单相关信息
    public String createOrder(SubmitOrderBo submitOrderBo);

}
