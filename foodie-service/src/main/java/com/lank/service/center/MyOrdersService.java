package com.lank.service.center;

import com.lank.pojo.Orders;
import com.lank.pojo.vo.center.OrderStatusCountsVO;
import com.lank.utils.PagedGridResult;

public interface MyOrdersService {
    //个人中心 - 查询用户订单数
    OrderStatusCountsVO getOrderStatusCounts(String userId);
    //个人中心 - 查询订单去向
    PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize);
    //查询我的订单列表
    PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);
    //订单状态 -> 商家发货
    public void updateDeliverOrderStatus(String orderId);
    //查询我的订单
    public Orders queryMyOrder(String userId, String orderId);
    //更新订单状态 -> 确认收货
    public boolean updateReceiveOrderStatus(String orderId);
    //删除订单
    public boolean deleteOrder(String userId, String orderId);
}
