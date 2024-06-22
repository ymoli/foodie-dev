package com.lank.service.center;

import com.lank.pojo.Users;
import com.lank.pojo.bo.center.CenterUserBO;

public interface CenterUserService {
    //根据用户id查询用户信息
    public Users queryUserInfo(String userId);
    //修改用户信息
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO);
    //修改用户头像
    public Users updateUserFace(String userId, String userUrl);
}
