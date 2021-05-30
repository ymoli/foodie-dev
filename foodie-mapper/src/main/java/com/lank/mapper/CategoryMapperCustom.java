package com.lank.mapper;

import com.lank.pojo.vo.CategoryVo;

import java.util.List;

public interface CategoryMapperCustom{
    //获取商品分类的子分类
    public List<CategoryVo> getSubCatList(Integer rootCatId);
}