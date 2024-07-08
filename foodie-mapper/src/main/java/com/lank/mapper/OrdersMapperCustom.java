package com.lank.mapper;

import com.lank.pojo.OrderStatus;
import com.lank.pojo.vo.center.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {
    //获取商品分类的子分类
    public List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String,Object> map);
    public List<MyOrdersVO> queryMyOrdersNotUse(@Param("paramsMap") Map<String,Object> map);
    public int getMyOrderStatusCounts(@Param("paramsMap") Map<String,Object> map);
    public List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String,Object> map);

}