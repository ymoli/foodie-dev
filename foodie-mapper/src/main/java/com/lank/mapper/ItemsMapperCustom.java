package com.lank.mapper;

import com.lank.pojo.vo.ItemCommentVo;
import com.lank.pojo.vo.SearchItemsVo;
import com.lank.pojo.vo.ShopcatVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {
    public List<ItemCommentVo> queryItemComments(@Param("paramsMap") Map<String,Object> map);
    public List<SearchItemsVo> searchItems(@Param("paramsMap") Map<String,Object> map);
    public List<SearchItemsVo> searchItemsByThirdCat(@Param("paramsMap") Map<String,Object> map);
    public List<ShopcatVo> queryItemsBySpecIds(@Param("paramsList") List specIdsList);

}