package com.lank.service;


import com.lank.pojo.Users;
import com.lank.pojo.bo.UserBO;

public interface UserService {
    //判断用户名是否存在
    public boolean queryUserNameIsExist(String username);

    //创建用户
    public Users creatUser(UserBO userBo);

    //用户登录
    public Users queryUserForLogin(String username,String password);
}
