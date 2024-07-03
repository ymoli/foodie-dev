package com.lank.controller.center;

import com.lank.controller.BaseController;
import com.lank.pojo.Orders;
import com.lank.service.center.MyOrdersService;
import com.lank.utils.JSONResult;
import com.lank.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "用户中心我的订单" , tags = {"用户中心我的订单展示相关的api"})
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {

    @Autowired
    private MyOrdersService myOrdersService;

    @ApiOperation(value = "查询订单列表",notes = "查询订单列表",httpMethod = "POST")
    @PostMapping("/query")
    public JSONResult query(
            @ApiParam(name = "orderStatus",value = "订单状态",required = false)
            @RequestParam Integer orderStatus,
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
        PagedGridResult grid = myOrdersService.queryMyOrders(userId,orderStatus,page,pageSize);
        return JSONResult.ok(grid);
    }

    //商家发货没有后端，该接口仅用于模拟
    @ApiOperation(value = "商家发货",notes = "商家发货",httpMethod = "GET")
    @GetMapping("/deliver")
    public JSONResult deliver(
            @ApiParam(name = "orderId",value = "订单id",required = true)
            @RequestParam String orderId){
        if ( StringUtils.isBlank(orderId)){
            return JSONResult.errorMsg("订单id不能为空");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return JSONResult.ok();
    }

    @ApiOperation(value = "用户确认收货",notes = "用户确认收货",httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public JSONResult confirmReceive(
            @ApiParam(name = "orderId",value = "订单id",required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId){

        JSONResult jsonResult = checkUserOrder(orderId, userId);
        if ( jsonResult.getStatus() != HttpStatus.OK.value()){
            return jsonResult;
        }
        boolean res = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!res){
            JSONResult.errorMsg("订单确认收货失败");
        }
        return JSONResult.ok();
    }

    @ApiOperation(value = "用户删除订单",notes = "用户删除订单",httpMethod = "POST")
    @PostMapping("/delete")
    public JSONResult delete(
            @ApiParam(name = "orderId",value = "订单id",required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId){

        JSONResult jsonResult = checkUserOrder(orderId, userId);
        if ( jsonResult.getStatus() != HttpStatus.OK.value()){
            return jsonResult;
        }
        boolean res = myOrdersService.deleteOrder(userId,orderId);
        if (!res){
            JSONResult.errorMsg("订单删除失败");
        }
        return JSONResult.ok();
    }

    //用于验证订单和用户是否有关联，避免非法调用
    private JSONResult checkUserOrder(String orderId,String userId){
        Orders orders = myOrdersService.queryMyOrder(userId,orderId);
        if (orders == null){
            return JSONResult.errorMsg("订单不存在");
        }
        return JSONResult.ok();
    }
}
