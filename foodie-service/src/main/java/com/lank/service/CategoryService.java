package com.lank.service;

import com.lank.pojo.Category;
import com.lank.pojo.vo.CategoryVo;

import java.util.List;

public interface CategoryService {

    //查询所有一级分类
    public List<Category> queryAllRootCats();

    //查询商品分类（二级）
    public List<CategoryVo> getSubCatList(Integer rootCatId);
}
