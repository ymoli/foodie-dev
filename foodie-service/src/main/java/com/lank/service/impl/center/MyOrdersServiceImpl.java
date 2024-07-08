package com.lank.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lank.constants.Constants;
import com.lank.enums.OrderStatusEnum;
import com.lank.enums.YesOrNo;
import com.lank.mapper.OrderItemsMapper;
import com.lank.mapper.OrderStatusMapper;
import com.lank.mapper.OrdersMapper;
import com.lank.mapper.OrdersMapperCustom;
import com.lank.pojo.OrderStatus;
import com.lank.pojo.Orders;
import com.lank.pojo.vo.center.MyOrdersVO;
import com.lank.pojo.vo.center.OrderStatusCountsVO;
import com.lank.service.center.MyOrdersService;
import com.lank.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyOrdersServiceImpl implements MyOrdersService {
    @Autowired
    private OrdersMapperCustom ordersMapperCustom;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);

        //待付款
        map.put("orderStatus", Constants.OrderStatusEnum.NO_PAY.getCode());
        int waitPayCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        //已付款、待发货
        map.put("orderStatus", Constants.OrderStatusEnum.PAID.getCode());
        int waitDeliverCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        //已发货、待收货
        map.put("orderStatus", Constants.OrderStatusEnum.SHIPPED.getCode());
        int waitReceiveCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        //已完成、待评价
        map.put("orderStatus", Constants.OrderStatusEnum.ORDER_SUCCESS.getCode());
        map.put("isComment", YesOrNo.No.type);
        int waitCommentCounts = ordersMapperCustom.getMyOrderStatusCounts(map);

        OrderStatusCountsVO countsVO = new OrderStatusCountsVO();
        countsVO.setWaitCommentCounts(waitPayCounts);
        countsVO.setWaitDeliverCounts(waitDeliverCounts);
        countsVO.setWaitReceiveCounts(waitReceiveCounts);
        countsVO.setWaitCommentCounts(waitCommentCounts);
        return countsVO;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<OrderStatus> list = ordersMapperCustom.getMyOrderTrend(map);

        return setterPagedGrid(list, page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        if (orderStatus != null){
            map.put("orderStatus",orderStatus);
        }
        PageHelper.startPage(page, pageSize);
        List<MyOrdersVO> list = ordersMapperCustom.queryMyOrders(map);
//        for(MyOrdersVo ordersVO : list){
//            String orderNo = ordersVO.getOrderId();
//            List<MySubOrderItemVO> subList = orderItemsMapper.getOrderItemsByOrderId(orderNo);
//            ordersVO.setSubOrderItemList(subList);
//        }
        return setterPagedGrid(list,page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        orderStatus.setDeliverTime(new Date());
        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        criteria.andEqualTo("orderStatus",OrderStatusEnum.WAIT_DELIVER.type);
        orderStatusMapper.updateByExampleSelective(orderStatus,example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setId(orderId);
        orders.setIsDelete(YesOrNo.No.type);

        return ordersMapper.selectOne(orders);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean updateReceiveOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        criteria.andEqualTo("orderStatus",OrderStatusEnum.WAIT_RECEIVE.type);
        int res = orderStatusMapper.updateByExampleSelective(orderStatus,example);
        return res == 1 ? true : false;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean deleteOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsDelete(YesOrNo.Yes.type);
        orders.setUpdatedTime(new Date());

        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",orderId);
        criteria.andEqualTo("userId",userId);
        int res = ordersMapper.updateByExampleSelective(orders,example);
        return res == 1 ? true : false;
    }

    private PagedGridResult setterPagedGrid(List<?> list, Integer page){
        //包含佷多的分页的数据，需要反馈给前端
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        //当前页
        grid.setPage(page);
        //总记录数
        grid.setRecords(pageList.getTotal());
        //每行显示的内容
        grid.setRows(pageList.getList());
        //总页数
        grid.setTotal(pageList.getPages());
        return grid;
    }
}
