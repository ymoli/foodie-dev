package com.lank.service.impl;

import com.lank.enums.Sex;
import com.lank.mapper.UsersMapper;
import com.lank.pojo.Users;
import com.lank.pojo.bo.UserBO;
import com.lank.service.UserService;
import com.lank.utils.DateUtil;
import com.lank.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUserNameIsExist(String username) {
        Example userExample = new Example(Users.class);
        // 设置判断
        Example.Criteria userCriteris = userExample.createCriteria();
        // 判断的条件是相等，property是数据库判断的列映射到User类中对应的属性名，value是判断的值
        userCriteris.andEqualTo("username",username);

        // usersMapper.selectOneByExample是mybatis逆向工具自动生成的mapper本身包含的方法
        Users result = usersMapper.selectOneByExample(userExample);
        return result == null ? false : true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteris = userExample.createCriteria();

        userCriteris.andEqualTo("username",username);
        userCriteris.andEqualTo("password",password);

        Users result = usersMapper.selectOneByExample(userExample);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users creatUser(UserBO userBo) {
        Users user = new Users();
        user.setId(sid.nextShort());
        user.setUsername(userBo.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBo.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置用户默认昵称、头像、生日、性别
        user.setNickname(userBo.getUsername());
        user.setFace(USER_FACE);
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.secret.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);

        return user;
    }
}
