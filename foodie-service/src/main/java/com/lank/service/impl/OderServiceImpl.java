package com.lank.service.impl;

import com.lank.enums.OrderStatusEnum;
import com.lank.enums.YesOrNo;
import com.lank.mapper.OrderItemsMapper;
import com.lank.mapper.OrderStatusMapper;
import com.lank.mapper.OrdersMapper;
import com.lank.pojo.*;
import com.lank.pojo.bo.SubmitOrderBo;
import com.lank.pojo.vo.MerchantOrdersVo;
import com.lank.pojo.vo.OrderVo;
import com.lank.service.AddressService;
import com.lank.service.ItemService;
import com.lank.service.OrderService;
import com.lank.utils.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderVo createOrder(SubmitOrderBo submitOrderBo) {
        String userId = submitOrderBo.getUserId();
        String itemSpecIds = submitOrderBo.getItemSpecIds();
        String addressId = submitOrderBo.getAddressId();
        Integer payMethod = submitOrderBo.getPayMethod();
        String leftMsg = submitOrderBo.getLeftMsg();
        Integer postAmount = 0; //包邮费用设置为0
        //1.新订单数据保存
        Orders newOrder = new Orders();
        String orderId = sid.nextShort();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);
        UserAddress address = addressService.queryUserAddress(userId,addressId);
        newOrder.setReceiverName(address.getReceiver());
        newOrder.setReceiverMobile(address.getMobile());
        newOrder.setReceiverAddress(address.getProvince() + " "
                                    + address.getCity() + " "
                                    + address.getDistrict() + " "
                                    + address.getDetail());
        newOrder.setPostAmount(postAmount);
        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);
        newOrder.setIsComment(YesOrNo.No.type);
        newOrder.setIsDelete(YesOrNo.No.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());
        //2.根据itemSpecIds循环商品id保存商品信息表
        String[] itemSpecIdArr=itemSpecIds.split(",");
        Integer totalAmount = 0;
        Integer realAmount = 0;
        for (String itemSpecId:itemSpecIdArr) {
            //2.1根据规格id，获取规格信息：价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
            //TODO 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCounts = 1;
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realAmount += itemsSpec.getPriceDiscount() * buyCounts;
            //2.2根据规格id，获得商品信息及商品图片
            String itemID = itemsSpec.getItemId();
            Items item = itemService.queryItemById(itemID);
            String url = itemService.queryItemMainImgById(itemID);
            //2.3循环保存子订单数据到数据库
            OrderItems subOrderItem = new OrderItems();
            String subOrderId = sid.nextShort();
            subOrderItem.setId(subOrderId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemID);
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setItemImg(url);
            subOrderItem.setBuyCounts(buyCounts);
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setItemSpecName(itemsSpec.getName());
            subOrderItem.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);
            //2.4在用户提交订单后，规格表中需要扣除库存
            itemService.decreaseItemSpecStock(itemSpecId,buyCounts);
        }
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realAmount);
        ordersMapper.insert(newOrder);
        //3.保存订单状态表
        OrderStatus waitOrderStatus = new OrderStatus();
        waitOrderStatus.setOrderId(orderId);
        waitOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitOrderStatus);
        //4.构建商户订单，用于传给支付中心
        MerchantOrdersVo merchantOrdersVo = new MerchantOrdersVo();
        merchantOrdersVo.setMerchantOrderId(orderId);
        merchantOrdersVo.setMerchantUserId(userId);
        merchantOrdersVo.setAmount(realAmount + postAmount);
        merchantOrdersVo.setPayMethod(payMethod);
        //5.构建自定义订单Vo
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderId(orderId);
        orderVo.setMerchantOrdersVo(merchantOrdersVo);

        return orderVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus newOrderStatus = new OrderStatus();
        newOrderStatus.setOrderId(orderId);
        newOrderStatus.setOrderStatus(orderStatus);
        newOrderStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(newOrderStatus);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int  closeOrder() {
        //查询所有未付款订单，判断是否超时（1天）
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> orderStatusList = orderStatusMapper.select(orderStatus);
        int count = 0;
        for(OrderStatus os:orderStatusList){
            Date createTime = os.getCreatedTime();
            int days = DateUtil.daysBetween(createTime,new Date());
            if(days >= 1){
                //超过一天则关闭订单
                count++;
                doCloseOrder(os.getOrderId());
            }
        }
        return count;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.CLOSE.type);
        orderStatus.setOrderId(orderId);
        orderStatus.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }
}