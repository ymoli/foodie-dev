package com.lank.controller;

import com.lank.enums.YesOrNo;
import com.lank.pojo.Carousel;
import com.lank.pojo.Category;
import com.lank.pojo.vo.CategoryVo;
import com.lank.pojo.vo.NewItemsVo;
import com.lank.service.CarouselService;
import com.lank.service.CategoryService;
import com.lank.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "首页",tags = {"首页展示的相关接口"})
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/carousel")
    @ApiOperation(value = "获取首页轮播图",notes = "获取首页轮播图",httpMethod = "GET")
    public JSONResult carousel(){

        List<Carousel> list = carouselService.queryAll(YesOrNo.Yes.type);

        return JSONResult.ok(list);
    }

    /*
    * 首页分类展示需求：
    * 1.第一次刷新首页展示大分类，且将分类展示在首页
    * 2.如果鼠标移动到大分类上，则加载其子分类类容；如果已存在子分类，则不需加载（懒加载）
    * */
    @GetMapping("/cats")
    @ApiOperation(value = "获取商品分类（一级）",notes = "获取商品分类（一级）",httpMethod = "GET")
    public JSONResult cats(){

        List<Category> list = categoryService.queryAllRootCats();

        return JSONResult.ok(list);
    }

    @GetMapping("/subCat/{rootCatId}")
    @ApiOperation(value = "获取商品子分类",notes = "获取商品子分类",httpMethod = "GET")
    public JSONResult subCat(
            @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
            @PathVariable Integer rootCatId){

        if (rootCatId == null){
            return JSONResult.errorMsg("分类不存在");
        }

        List<CategoryVo> list = categoryService.getSubCatList(rootCatId);

        return JSONResult.ok(list);
    }

    @GetMapping("/sixNewItems/{rootCatId}")
    @ApiOperation(value = "查询一级分类下的最新6条商品数据",notes = "查询一级分类下的最新6条商品数据",httpMethod = "GET")
    public JSONResult sixNewItems(
            @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
            @PathVariable Integer rootCatId){

        if (rootCatId == null){
            return JSONResult.errorMsg("分类不存在");
        }

        List<NewItemsVo> list = categoryService.getSixNewItemsLazy(rootCatId);

        return JSONResult.ok(list);
    }


}
