package com.lank.controller;

import com.lank.pojo.Items;
import com.lank.pojo.ItemsImg;
import com.lank.pojo.ItemsParam;
import com.lank.pojo.ItemsSpec;
import com.lank.pojo.vo.CommentLevelCountVo;
import com.lank.pojo.vo.ItemInfoVo;
import com.lank.service.ItemService;
import com.lank.utils.JSONResult;
import com.lank.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "商品" , tags = {"用于商品的相关接口"})
@RestController
@RequestMapping("/items")
public class ItemsController extends BaseController{

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "商品详情页",notes = "商品详情页",httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JSONResult info(
            @ApiParam(name = "itemId",value = "商品id",required = true)
            @PathVariable String itemId){
        if (StringUtils.isBlank(itemId)){
            return JSONResult.errorMsg("商品id不能为空!");
        }
        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImg = itemService.queryItemImgList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        List<ItemsSpec> itemsSpec = itemService.queryItemSpecList(itemId);
        ItemInfoVo itemInfoVo = new ItemInfoVo();
        itemInfoVo.setItem(items);
        itemInfoVo.setItemImgList(itemsImg);
        itemInfoVo.setItemParams(itemsParam);
        itemInfoVo.setItemSpecList(itemsSpec);
        return JSONResult.ok(itemInfoVo);
    }

    @ApiOperation(value = "查询商品评价等级",notes = "查询商品评价等级",httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JSONResult commentLevel(
            @ApiParam(name = "itemId",value = "商品id",required = true)
            @RequestParam String itemId){
        if (StringUtils.isBlank(itemId)){
            return JSONResult.errorMsg("商品id不能为空!");
        }
        CommentLevelCountVo countVo = itemService.queryCommentCounts(itemId);
        return JSONResult.ok(countVo);
    }

    @ApiOperation(value = "查询商品评论",notes = "查询商品评论",httpMethod = "GET")
    @GetMapping("/comments")
    public JSONResult comments(
            @ApiParam(name = "itemId",value = "商品id",required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level",value = "评价等级",required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page",value = "查询下一页的第几页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "查询每一页的内容",required = false)
            @RequestParam Integer pageSize){
        if (StringUtils.isBlank(itemId)){
            return JSONResult.errorMsg("商品id不能为空!");
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = COMMENT_PAGE_SIZE;
        }
        PagedGridResult grid = itemService.queryPagedComments(itemId,level,page,pageSize);
        return JSONResult.ok(grid);
    }

}
