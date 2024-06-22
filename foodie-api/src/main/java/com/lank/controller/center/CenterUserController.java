package com.lank.controller.center;

import com.lank.pojo.Users;
import com.lank.pojo.bo.center.CenterUserBO;
import com.lank.service.center.CenterUserService;
import com.lank.utils.CookieUtils;
import com.lank.utils.JSONResult;
import com.lank.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "用户信息接口" , tags = {"用户信息接口相关的api"})
@RequestMapping("userInfo")
public class CenterUserController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取用户信息",notes = "获取用户信息",httpMethod = "POST")
    @PostMapping("/update")
    public JSONResult update(
            @ApiParam(name = "userId",value = "用户id,required = true")
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("");
        }

        //判断是否有错误
        if(bindingResult.hasErrors()){
            Map<String,String> map = getErrors(bindingResult);
            return JSONResult.errorMap(map);
        }

        Users userResult = centerUserService.updateUserInfo(userId,centerUserBO);

        //将用户的敏感信息设置为null
        userResult = setPropertyNull(userResult);

        //将用户的信息保存在cookie中，传到前端
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);

        //TODO 后续更改，增加令牌token，会整合进redis，分布式会话

        return JSONResult.ok();
    }

    private Map<String,String> getErrors(BindingResult bindingResult){
        Map<String,String> result = new HashMap<>();
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError error:errorList) {
            //发生验证错误的属性
            String errorFiled = error.getField();
            // 验证错误信息
            String errorMsg = error.getDefaultMessage();
            result.put(errorFiled,errorMsg);
        }
        return result;
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
}
