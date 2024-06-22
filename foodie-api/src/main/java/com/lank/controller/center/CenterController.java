package com.lank.controller.center;

import com.lank.pojo.Users;
import com.lank.service.center.CenterUserService;
import com.lank.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "center - 用户中心" , tags = {"用户中心展示相关的api"})
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取用户信息",notes = "获取用户信息",httpMethod = "POST")
    @PostMapping("/userInfo")
    public JSONResult userInfo(
            @ApiParam(name = "userId",value = "用户id,required = true")
            @RequestParam String userId){
        if (StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("");
        }

        Users users = centerUserService.queryUserInfo(userId);
        return JSONResult.ok(users);
    }
}
