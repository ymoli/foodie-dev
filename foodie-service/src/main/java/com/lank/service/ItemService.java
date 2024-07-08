package com.lank.service;

import com.lank.pojo.Items;
import com.lank.pojo.ItemsImg;
import com.lank.pojo.ItemsParam;
import com.lank.pojo.ItemsSpec;
import com.lank.pojo.vo.CommentLevelCountVO;
import com.lank.pojo.vo.ShopcatVO;
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
    public CommentLevelCountVO queryCommentCounts(String itemId);

    //根据商品id查询商品评价（分页）
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

    //根据关键字搜索商品列表
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);
    //根据三级目录搜索商品列表
    public PagedGridResult searchItems(int catId, String sort, Integer page, Integer pageSize);
    //根据规格ids查询最新的购物车中的商品数据（用于刷新渲染购物车中的商品数据）
    public List<ShopcatVO> queryItemsBySpecIds(String specIds);
    //根据商品规格id获取规格对象的具体信息
    public ItemsSpec queryItemSpecById(String specId);
    //根据商品id获取商品主图url
    public String queryItemMainImgById(String itemId);
    //减少库存
    public void decreaseItemSpecStock(String specId,Integer buyCounts);
}
