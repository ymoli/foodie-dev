package com.lank.service;


import com.lank.pojo.Users;
import com.lank.pojo.bo.UserBo;
import org.apache.catalina.User;

public interface UserService {
    //判断用户名是否存在
    public boolean queryUserNameIsExist(String username);

    //创建用户
    public Users creatUser(UserBo userBo);

    //用户登录
    public Users queryUserForLogin(String username,String password);
}
