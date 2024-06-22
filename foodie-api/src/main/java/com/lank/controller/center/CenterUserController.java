package com.lank.controller.center;

import com.lank.controller.BaseController;
import com.lank.pojo.Users;
import com.lank.pojo.bo.center.CenterUserBO;
import com.lank.resource.FileUpload;
import com.lank.service.center.CenterUserService;
import com.lank.utils.CookieUtils;
import com.lank.utils.DateUtil;
import com.lank.utils.JSONResult;
import com.lank.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "用户信息接口" , tags = {"用户信息接口相关的api"})
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "用户头像修改",notes = "用户头像修改",httpMethod = "POST")
    @PostMapping("/uploadFace")
    public JSONResult uploadFace(
            @ApiParam(name = "userId",value = "用户id,required = true")
            @RequestParam String userId,
            @ApiParam(name = "file",value = "用户头像,required = true")
            MultipartFile file,
            HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("");
        }
        //定义头像保存的地址
        String fielSpace = fileUpload.getImageUserFaceLocation();
        //在路径上为每个用户增加一个userId，用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;

        //开始文件上传
        if(file != null){
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            try {
                //获取文件上传的文件名称
                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)){
                    //文件重命名，test001.png - > ["test001","001"]
                    String fileNameArr[] = fileName.split("\\.");
                    //获取文件后缀名
                    String suffix = fileNameArr[fileNameArr.length - 1];
                    //校验文件格式
                    if(!suffix.equals("png") && !suffix.equals("jpg") && !suffix.equals("jpeg")){
                        return JSONResult.errorMsg("不支持的图片格式");
                    }
                    //文件名重组，face-{userId}.png，覆盖式上传；增量式：拼接当前时间
                    String newFileName = "face-"+userId+"."+suffix;
                    //上传的头像最终保存的位置
                    String finalFacePath = fielSpace + uploadPathPrefix + File.separator + newFileName;
                    //拼接服务器地址,用于提供给web服务访问的地址
                    uploadPathPrefix += ("/"+newFileName);
                    File outFile = new File(finalFacePath);
                    if(outFile.getParentFile() != null){
                        //创建文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    //文件输出保存到目录
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);

                }
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if(fileOutputStream!=null){
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }

            String imageServerUrl = fileUpload.getImageServerUrl();
            //由于浏览器存在缓存的情况，所以需要加上时间戳保证更新后的图片可以及时刷新
            String finalUserFaceUrl = imageServerUrl + uploadPathPrefix.substring(1) +"?t="+ DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
            // 更新用户头像到数据库uploadPathPrefix
            Users userResult = centerUserService.updateUserFace(userId,finalUserFaceUrl);
            //将用户的敏感信息设置为null
            userResult = setPropertyNull(userResult);
            //将用户的信息保存在cookie中，传到前端
            CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);
            //TODO 后续更改，增加令牌token，会整合进redis，分布式会话

        } else {
            JSONResult.errorMsg("文件不能为空");
        }


        return JSONResult.ok();
    }


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
