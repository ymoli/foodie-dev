package com.lank.service;

import com.lank.pojo.Items;
import com.lank.pojo.ItemsImg;
import com.lank.pojo.ItemsParam;
import com.lank.pojo.ItemsSpec;
import com.lank.pojo.vo.CommentLevelCountVo;
import com.lank.utils.PagedGridResult;

import java.util.List;

public interface ItemService {

    //根据商品id查询详情
    public Items queryItemById(String itemId);

    //根据商品id查询图片列表
    public List<ItemsImg> queryItemImgList(String itemId);

    //根据商品id查询商品规格
    public List<ItemsSpec> queryItemSpecList(String itemId);

    //根据商品id查询商品参数
    public ItemsParam queryItemParam(String itemId);

    //根据商品id查询商品的评价等级数量
    public CommentLevelCountVo queryCommentCounts(String itemId);

    //根据商品id查询商品评价（分页）
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);
}
