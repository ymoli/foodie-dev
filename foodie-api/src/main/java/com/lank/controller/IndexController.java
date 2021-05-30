package com.lank.controller;

import com.lank.enums.YesOrNo;
import com.lank.pojo.Carousel;
import com.lank.service.CarouselService;
import com.lank.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "首页",tags = {"首页展示的相关接口"})
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping("/carousel")
    @ApiOperation(value = "获取首页轮播图",notes = "获取首页轮播图",httpMethod = "GET")
    public JSONResult carousel(){

        List<Carousel> list = carouselService.queryAll(YesOrNo.Yes.type);

        return JSONResult.ok(list);
    }


}
