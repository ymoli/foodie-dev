package com.lank.controller;

import com.lank.pojo.Items;
import com.lank.pojo.ItemsImg;
import com.lank.pojo.ItemsParam;
import com.lank.pojo.ItemsSpec;
import com.lank.pojo.vo.ItemInfoVo;
import com.lank.service.ItemService;
import com.lank.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(value = "商品" , tags = {"用于商品的相关接口"})
@RestController
@RequestMapping("/items")
public class ItemsController {

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

}
