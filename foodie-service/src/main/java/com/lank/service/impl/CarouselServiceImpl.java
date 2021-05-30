package com.lank.service.impl;

import com.lank.mapper.CarouselMapper;
import com.lank.pojo.Carousel;
import com.lank.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Carousel> queryAll(Integer isShow) {
        //使用mybatis的mapper默认的example查询
        Example carouselExample = new Example(Carousel.class);
        //设置查询的排序，默认是asc，可以不写
        carouselExample.orderBy("sort").asc();
        Example.Criteria carouselCriteris = carouselExample.createCriteria();
        //设置查询条件
        carouselCriteris.andEqualTo("isShow",isShow);

        List<Carousel> result = carouselMapper.selectByExample(carouselExample);

        return result;
    }
}