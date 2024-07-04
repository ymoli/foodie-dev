package com.lank.service.center;

import com.lank.pojo.OrderItems;
import com.lank.pojo.bo.center.OrderItemsCommentBO;
import com.lank.utils.PagedGridResult;

import java.util.List;

public interface MyCommentsService {
    //我的评价查询分页
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
    //根据订单id查询关联的商品
    public List<OrderItems> queryPendingComment(String orderId);
    //保存用户评论
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);
}
