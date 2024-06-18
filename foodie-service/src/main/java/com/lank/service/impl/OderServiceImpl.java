package com.lank.service.impl;

import com.lank.enums.OrderStatusEnum;
import com.lank.enums.YesOrNo;
import com.lank.mapper.OrderItemsMapper;
import com.lank.mapper.OrderStatusMapper;
import com.lank.mapper.OrdersMapper;
import com.lank.pojo.*;
import com.lank.pojo.bo.SubmitOrderBo;
import com.lank.service.AddressService;
import com.lank.service.ItemService;
import com.lank.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
    public String createOrder(SubmitOrderBo submitOrderBo) {
        String userId = submitOrderBo.getUserId();
        String itemSpecIds = submitOrderBo.getItemSpecIds();
        String addressId = submitOrderBo.getAddressId();
        Integer payMethod = submitOrderBo.getPayMethod();
        String leftMsg = submitOrderBo.getLeftMsg();
        Integer postAmount = 0; //包邮费用设置为0
        //新订单数据保存
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
        //根据itemSpecIds循环商品id保存商品信息表
        String[] itemSpecIdArr=itemSpecIds.split(",");
        Integer totalAmount = 0;
        Integer realAmount = 0;
        for (String itemSpecId:itemSpecIdArr) {
            //根据规格id，获取规格信息：价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
            //TODO 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCounts = 1;
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realAmount += itemsSpec.getPriceDiscount() * buyCounts;
            //根据规格id，获得商品信息及商品图片
            String itemID = itemsSpec.getItemId();
            Items item = itemService.queryItemById(itemID);
            String url = itemService.queryItemMainImgById(itemID);
            //循环保存子订单数据到数据库
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
            //在用户提交订单后，规格表中需要扣除库存
            itemService.decreaseItemSpecStock(itemSpecId,buyCounts);
        }
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realAmount);
        ordersMapper.insert(newOrder);
        //保存订单状态表
        OrderStatus waitOrderStatus = new OrderStatus();
        waitOrderStatus.setOrderId(orderId);
        waitOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitOrderStatus);

        return orderId;
    }


}