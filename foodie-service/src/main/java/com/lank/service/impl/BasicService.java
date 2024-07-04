package com.lank.service.impl;

import com.github.pagehelper.PageInfo;
import com.lank.utils.PagedGridResult;

import java.util.List;

public class BasicService {
    public PagedGridResult setterPagedGrid(List<?> list, Integer page){
        //包含佷多的分页的数据，需要反馈给前端
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        //当前页
        grid.setPage(page);
        //总记录数
        grid.setRecords(pageList.getTotal());
        //每行显示的内容
        grid.setRows(pageList.getList());
        //总页数
        grid.setTotal(pageList.getPages());
        return grid;
    }
}
