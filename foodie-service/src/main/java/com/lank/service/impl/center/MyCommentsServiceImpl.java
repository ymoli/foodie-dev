package com.lank.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.lank.enums.YesOrNo;
import com.lank.mapper.ItemsCommentsMapperCustom;
import com.lank.mapper.OrderItemsMapper;
import com.lank.mapper.OrderStatusMapper;
import com.lank.mapper.OrdersMapper;
import com.lank.pojo.OrderItems;
import com.lank.pojo.OrderStatus;
import com.lank.pojo.Orders;
import com.lank.pojo.bo.center.OrderItemsCommentBO;
import com.lank.pojo.vo.center.MyCommentVO;
import com.lank.service.center.MyCommentsService;
import com.lank.service.impl.BasicService;
import com.lank.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyCommentsServiceImpl extends BasicService implements MyCommentsService {
    @Autowired
    public OrderItemsMapper orderItemsMapper;

    @Autowired
    public OrdersMapper ordersMapper;

    @Autowired
    public OrderStatusMapper orderStatusMapper;

    @Autowired
    public ItemsCommentsMapperCustom itemsCommentsMapperCustom;
//
//    @Autowired
//    public IUserService userService;

    @Autowired
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> list = itemsCommentsMapperCustom.queryMyComments(map);

        return setterPagedGrid(list, page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems query = new OrderItems();
        query.setOrderId(orderId);
        return orderItemsMapper.select(query);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList) {
        // 1. 保存评价 items_comments
        for (OrderItemsCommentBO oic : commentList) {
            //设置主键
            oic.setCommentId(sid.nextShort());
        }
//        Users currentCommentUser = (Users) userService.queryUserById(userId).getData();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
//        map.put("userFakeNickname",desensitizedName(currentCommentUser.getUsername()));
//        map.put("userFace",currentCommentUser.getFace());
        map.put("commentList", commentList);
        itemsCommentsMapperCustom.saveComments(map);

        // 2. 修改订单表改已评价 orders
        Orders order = new Orders();
        order.setId(orderId);
        order.setIsComment(YesOrNo.Yes.type);
        ordersMapper.updateByPrimaryKeySelective(order);

        // 3. 修改订单状态表的留言时间 order_status
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    private static String desensitizedName(String fullName){
        String name = fullName;
        if (StringUtils.isNotBlank(fullName)) {
            name = StringUtils.left(fullName, 1);
            return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
        }
        return name;
    }
}
