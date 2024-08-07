package com.lank.service.impl;

import com.lank.mapper.CategoryMapper;
import com.lank.mapper.CategoryMapperCustom;
import com.lank.pojo.Category;
import com.lank.pojo.vo.CategoryVO;
import com.lank.pojo.vo.NewItemsVO;
import com.lank.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Category> queryAllRootCats() {
        Example categoryExample = new Example(Category.class);
        categoryExample.orderBy("id");
        Example.Criteria categoryCriterias = categoryExample.createCriteria();
//        categoryCriterias.andEqualTo("fatherId",0);
        categoryCriterias.andEqualTo("type",1);
        List<Category> result= categoryMapper.selectByExample(categoryExample);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CategoryVO>  getSubCatList(Integer rootCatId) {
        List<CategoryVO>  result = categoryMapperCustom.getSubCatList(rootCatId);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        Map<String,Object> map = new HashMap<>();
        map.put("rootCatId",rootCatId);
        List<NewItemsVO> result = categoryMapperCustom.getSixNewItemsLazy(map);
        return result;
    }
}
