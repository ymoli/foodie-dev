package com.lank.mapper;

import com.lank.pojo.vo.MyOrdersVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {
    //获取商品分类的子分类
    public List<MyOrdersVo> queryMyOrders(@Param("paramsMap") Map<String,Object> map);
    public List<MyOrdersVo> queryMyOrdersNotUse(@Param("paramsMap") Map<String,Object> map);

}