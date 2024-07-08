package com.lank.controller;

import com.lank.pojo.Items;
import com.lank.pojo.ItemsImg;
import com.lank.pojo.ItemsParam;
import com.lank.pojo.ItemsSpec;
import com.lank.pojo.vo.CommentLevelCountVO;
import com.lank.pojo.vo.ItemInfoVO;
import com.lank.pojo.vo.ShopcatVO;
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
@RequestMapping("items")
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
        ItemInfoVO itemInfoVo = new ItemInfoVO();
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
        CommentLevelCountVO countVo = itemService.queryCommentCounts(itemId);
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

    //keywords根据关键字搜索查询
    @ApiOperation(value = "搜索商品列表",notes = "搜索商品列表",httpMethod = "GET")
    @GetMapping("/search")
    public JSONResult search(
            @ApiParam(name = "keywords",value = "关键字",required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort",value = "排序",required = false)
            @RequestParam String sort,
            @ApiParam(name = "page",value = "查询下一页的第几页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "查询每一页的内容",required = false)
            @RequestParam Integer pageSize){
        if (StringUtils.isBlank(keywords)){
            return JSONResult.errorMsg(null);
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = PAGE_SIZE;
        }
        PagedGridResult grid = itemService.searchItems(keywords,sort,page,pageSize);
        return JSONResult.ok(grid);
    }

    //catId根据目录等级搜索查询
    @ApiOperation(value = "通过分类id搜索商品列表",notes = "通过分类id搜索商品列表",httpMethod = "GET")
    @GetMapping("/catItems")
    public JSONResult catItems(
            @ApiParam(name = "catId",value = "三级分类id",required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort",value = "排序",required = false)
            @RequestParam String sort,
            @ApiParam(name = "page",value = "查询下一页的第几页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "查询每一页的内容",required = false)
            @RequestParam Integer pageSize){
        if (catId == null){
            return JSONResult.errorMsg(null);
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = PAGE_SIZE;
        }
        PagedGridResult grid = itemService.searchItems(catId,sort,page,pageSize);
        return JSONResult.ok(grid);
    }

    //用于用户长时间未登录网站，刷新购物车中的数据（主要是商品的价格）
    @ApiOperation(value = "根据商品规格ids查找最新的商品数据",notes = "根据商品规格ids查找最新的商品数据",httpMethod = "GET")
    @GetMapping("/refresh")
    public JSONResult refresh(
            @ApiParam(name = "itemSpecIds",value = "拼接的规格ids",required = true,example = "1001,1003,1005")
            @RequestParam String itemSpecIds){
        if (StringUtils.isBlank(itemSpecIds)){
            return JSONResult.ok();
        }

        List<ShopcatVO> list = itemService.queryItemsBySpecIds(itemSpecIds);
        return JSONResult.ok(list);
    }
}
