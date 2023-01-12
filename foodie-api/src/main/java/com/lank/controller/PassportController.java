package com.lank.controller;

import com.lank.pojo.Users;
import com.lank.pojo.bo.UserBo;
import com.lank.service.UserService;
import com.lank.utils.CookieUtils;
import com.lank.utils.JSONResult;
import com.lank.utils.JsonUtils;
import com.lank.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(value = "注册登录",tags = {"用于注册登录的相关接口"})
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;

    // @RequestParam 代表是请求类型的参数，而不是路径类型的参数
    @GetMapping("/usernameIsExist")
    @ApiOperation(value = "用户是否存在",notes = "用户是否存在",httpMethod = "GET")
    public JSONResult usernameIsExist(@RequestParam String username){

        if (StringUtils.isBlank(username)){
            return JSONResult.errorMsg("用户名不能为空");
        }
        Boolean isExist = userService.queryUserNameIsExist(username);
        if (isExist){
            return JSONResult.errorMsg("用户名已存在");
        }
        return JSONResult.ok();

    }

    @PostMapping("/regist")
    @ApiOperation(value = "用户注册",notes = "用户注册",httpMethod = "POST")
    public JSONResult regist(@RequestBody UserBo userBo, HttpServletRequest request, HttpServletResponse response){
        String username = userBo.getUsername();
        String password = userBo.getPassword();
        String confirmPwd = userBo.getConfirmPassword();

        //0.判断用户名、密码不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPwd)){
            return JSONResult.errorMsg("用户名或密码不能为空");
        }

        //1.判断用户名不存在
        Boolean isExist = userService.queryUserNameIsExist(username);
        if (isExist){
            return JSONResult.errorMsg("用户名已存在");
        }

        //2.判断密码长度大于6位
        if(password.length() < 6){
            return JSONResult.errorMsg("密码不能小于6位");
        }

        //3.判断两次密码一致
        if (!password.equals(confirmPwd)){
            return JSONResult.errorMsg("两次密码不一致");
        }

        //4.实现注册
        Users userResult = userService.creatUser(userBo);

        //将用户的敏感信息设置为null
        userResult = setPropertyNull(userResult);

        //将用户的信息保存在cookie中，传到前端
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);

        return JSONResult.ok(userResult);
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "POST")
    public JSONResult login(@RequestBody UserBo userBo, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String username = userBo.getUsername();
        String password = userBo.getPassword();

        //0.判断用户名、密码不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return JSONResult.errorMsg("用户名或密码不能为空");
        }
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));

        if (userResult == null){
            return JSONResult.errorMsg("用户名或密码不正确");
        }

        //将用户的敏感信息设置为null
        userResult = setPropertyNull(userResult);

        //将用户的信息保存在cookie中，传到前端
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);

        return JSONResult.ok(userResult);
    }

    private Users setPropertyNull(Users user){
        user.setPassword(null);
        user.setRealname(null);
        user.setBirthday(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setEmail(null);
        user.setMobile(null);
        return user;
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户退出",notes = "用户退出",httpMethod = "POST")
    public JSONResult logout(@RequestParam String userId,HttpServletRequest request, HttpServletResponse response){

        //退出登录，清除用户cookie信息
        CookieUtils.deleteCookie(request,response,"user");

        //用户退出登录，清空购物车
        //分布式会话中清除用户数据

        return JSONResult.ok();
    }
}
