package com.lank.mapper;

import com.lank.pojo.vo.CategoryVo;
import com.lank.pojo.vo.NewItemsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom{
    //获取商品分类的子分类
    public List<CategoryVo> getSubCatList(Integer rootCatId);

    //获取一级分类下最新的子分类
    public List<NewItemsVo> getSixNewItemsLazy(@Param("paramsMap") Map<String,Object> map);

}