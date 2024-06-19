package com.lank.service;

import com.lank.pojo.OrderStatus;
import com.lank.pojo.bo.SubmitOrderBo;
import com.lank.pojo.vo.OrderVo;

public interface OrderService {

    //用于创建订单相关信息
    public OrderVo createOrder(SubmitOrderBo submitOrderBo);

    //修改订单状态
    public void updateOrderStatus(String orderId,Integer orderStatus);

    //查询订单状态
    public OrderStatus queryOrderStatusInfo(String orderId);

    //关闭超时未支付订单
    public int closeOrder();

}
