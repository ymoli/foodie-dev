package com.lank.mapper;

import com.lank.pojo.vo.center.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom {
    public void saveComments(Map<String,Object> map);
    public List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String,Object> map);
}