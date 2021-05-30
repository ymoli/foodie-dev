package com.lank.service;

import com.lank.pojo.Carousel;

import java.util.List;

public interface CarouselService {

    //查询所有轮播图列表
    public List<Carousel> queryAll(Integer isShow);
}
