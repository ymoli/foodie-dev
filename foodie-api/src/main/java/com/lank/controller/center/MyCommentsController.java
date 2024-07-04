package com.lank.controller.center;

import com.lank.controller.BaseController;
import com.lank.enums.YesOrNo;
import com.lank.pojo.OrderItems;
import com.lank.pojo.Orders;
import com.lank.pojo.bo.center.OrderItemsCommentBO;
import com.lank.service.center.MyCommentsService;
import com.lank.utils.JSONResult;
import com.lank.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "用户中心我的评价" , tags = {"用户中心我的评价展示相关的api"})
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;

    @ApiOperation(value = "查询订单评论",notes = "查询订单评论",httpMethod = "POST")
    @PostMapping("/pending")
    public JSONResult pending(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId",value = "订单id",required = true)
            @RequestParam String orderId){
        //判断用户和订单是否关联
        JSONResult jsonResult = checkUserOrder(orderId, userId);
        if ( jsonResult.getStatus() != HttpStatus.OK.value()){
            return jsonResult;
        }
        //判断该订单是否已经评价
        Orders myOrders = (Orders) jsonResult.getData();
        if (myOrders.getIsComment() == YesOrNo.Yes.type){
            return JSONResult.errorMsg("该订单已经评价");
        }

        List<OrderItems> result = myCommentsService.queryPendingComment(orderId);
        return JSONResult.ok(result);
    }

    @ApiOperation(value = "保存评论列表",notes = "保存评论列表",httpMethod = "POST")
    @PostMapping("/saveList")
    public JSONResult saveList(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId",value = "订单id",required = true)
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commonList){
        //判断用户和订单是否关联
        JSONResult jsonResult = checkUserOrder(orderId, userId);
        if ( jsonResult.getStatus() != HttpStatus.OK.value()){
            return jsonResult;
        }
        //判断评论内容不能为空
        if (commonList == null || commonList.isEmpty() || commonList.size() == 0){
            return JSONResult.errorMsg("评论列表不能为空");
        }

        myCommentsService.saveComments(orderId,userId,commonList);
        return JSONResult.ok();
    }

    @ApiOperation(value = "查询我的评价",notes = "查询我的评价",httpMethod = "POST")
    @PostMapping("/query")
    public JSONResult query(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "page",value = "查询下一页的第几页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "查询每一页的内容",required = false)
            @RequestParam Integer pageSize){
        if ( StringUtils.isBlank(userId)){
            return JSONResult.errorMsg(null);
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = COMMENT_PAGE_SIZE;
        }

        PagedGridResult grid = myCommentsService.queryMyComments(userId,page,pageSize);
        return JSONResult.ok(grid);
    }


}
