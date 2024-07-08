package com.lank.mapper;

import com.lank.pojo.vo.CategoryVO;
import com.lank.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom{
    //获取商品分类的子分类
    public List<CategoryVO> getSubCatList(Integer rootCatId);

    //获取一级分类下最新的子分类
    public List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String,Object> map);

}