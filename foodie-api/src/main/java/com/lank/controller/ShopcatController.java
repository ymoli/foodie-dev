package com.lank.controller;

import com.lank.pojo.bo.ShopcatBo;
import com.lank.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "购物车" , tags = {"用于购物车的相关api"})
@RequestMapping("shopcat")
public class ShopcatController {

    final static Logger logger = LoggerFactory.getLogger(ShopcatController.class);

    @PostMapping("/add")
    @ApiOperation(value = "添加商品到购物车",notes = "添加商品到购物车",httpMethod = "POST")
    public JSONResult add(@RequestParam String userId, @RequestBody ShopcatBo shopcatBo,
                          HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("");
        }
        System.out.println(shopcatBo);
        //TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        return JSONResult.ok();
    }

    @PostMapping("/del")
    @ApiOperation(value = "从购物车中删除商品",notes = "从购物车中删除商品",httpMethod = "POST")
    public JSONResult del(@RequestParam String userId, @RequestParam String itemSpecId,
                          HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)){
            return JSONResult.errorMsg("参数不能为空");
        }

        //TODO 前端用户在登录的情况下，从购物车中删除商品，会同时在后端同步删除购物车中商品
        return JSONResult.ok();
    }
}
