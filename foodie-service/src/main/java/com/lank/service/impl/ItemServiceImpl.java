package com.lank.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lank.enums.CommentLevel;
import com.lank.enums.YesOrNo;
import com.lank.mapper.*;
import com.lank.pojo.*;
import com.lank.pojo.vo.CommentLevelCountVo;
import com.lank.pojo.vo.ItemCommentVo;
import com.lank.pojo.vo.SearchItemsVo;
import com.lank.pojo.vo.ShopcatVo;
import com.lank.service.ItemService;
import com.lank.utils.DesensitizationUtil;
import com.lank.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example itemsImgExp = new Example(ItemsImg.class);
        Example.Criteria itemsImgCriteria = itemsImgExp.createCriteria();
        itemsImgCriteria.andEqualTo("itemId",itemId);

        return itemsImgMapper.selectByExample(itemsImgExp);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example itemsSpecExp = new Example(ItemsSpec.class);
        Example.Criteria itemsSpecCriteria = itemsSpecExp.createCriteria();
        itemsSpecCriteria.andEqualTo("itemId",itemId);

        return itemsSpecMapper.selectByExample(itemsSpecExp);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsParam queryItemParam(String itemId) {
        Example itemsParamExp = new Example(ItemsParam.class);
        Example.Criteria itemsParamCriteria = itemsParamExp.createCriteria();
        itemsParamCriteria.andEqualTo("itemId",itemId);

        return itemsParamMapper.selectOneByExample(itemsParamExp);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public CommentLevelCountVo queryCommentCounts(String itemId) {
        CommentLevelCountVo commentLevelCountVo = new CommentLevelCountVo();
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;
        commentLevelCountVo.setBadCounts(badCounts);
        commentLevelCountVo.setNormalCounts(normalCounts);
        commentLevelCountVo.setGoodCounts(goodCounts);
        commentLevelCountVo.setTotalCounts(totalCounts);

        return commentLevelCountVo;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId,Integer level){
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        if (level != null){
            condition.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(condition);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryPagedComments(String itemId, Integer level,Integer page,Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("itemId",itemId);
        map.put("level",level);
        //mybatis-pagehelper
        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);

        List<ItemCommentVo> list = itemsMapperCustom.queryItemComments(map);
        list.stream().forEach(item -> {
            item.setNickname(DesensitizationUtil.commonDisplay(item.getNickname()));
        });

        return setterPagedGrid(list,page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("keywords",keywords);
        map.put("sort",sort);
        //mybatis-pagehelper
        PageHelper.startPage(page, pageSize);

        List<SearchItemsVo> list = itemsMapperCustom.searchItems(map);
        return setterPagedGrid(list,page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult searchItems(int catId, String sort, Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("catId",catId);
        map.put("sort",sort);
        //mybatis-pagehelper
        PageHelper.startPage(page, pageSize);

        List<SearchItemsVo> list = itemsMapperCustom.searchItems(map);
        return setterPagedGrid(list,page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ShopcatVo> queryItemsBySpecIds(String specIds) {
        String[] ids = specIds.split(",");
        List<String> specIdsList = new ArrayList<>();
        Collections.addAll(specIdsList,ids);
        return itemsMapperCustom.queryItemsBySpecIds(specIdsList);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.Yes.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result != null ? result.getUrl() : "";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseItemSpecStock(String specId,Integer buyCounts) {
        /*当有多个订单，同时扣除库存时，可能会出现负数，解决办法：
           （1）synchronized,存在问题：效率较低；集群项目的话，关键字没作用
           （2）锁数据库，存在问题：导致数据库性能低下
           （3）分布式锁：zookeeper redis
              lockUtil.getLock() //加锁
              lockUtil.unLock //解锁
           （4）单体项目：乐观锁
         */
        //使用乐观锁实现
        int result = itemsMapperCustom.decreaseItemSpecStock(specId,buyCounts);
        if (result != 1){
            throw new RuntimeException("订单创建失败，由于库存不足不足");
        }
    }

    private PagedGridResult setterPagedGrid(List<?> list, Integer page){
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
