package com.lank.cbk.apiUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class ApiSample {
    static Logger logger = LoggerFactory.getLogger(ApiSample.class);

    @Autowired
    private RestApiUtil restApiUtil;

    public SampleModel callApi(){
        Map<String,Object> requestMap = new HashMap<>();
        //header
        requestMap.put("businessCode","GCB");
        requestMap.put("channelId","MBK");
        int hashCode = java.util.UUID.randomUUID().toString().hashCode();
        if (hashCode < 0){
            hashCode = -hashCode;
        }
        String uuid = String.format("%010d",hashCode).substring(0,10);
        requestMap.put("uuid",uuid);
        requestMap.put("accept","1");
        //requestbody
        requestMap.put("url","localhost");
        requestMap.put("port","8080");
        requestMap.put("uri","/test");
        requestMap.put("reqBody","test Request Body");
        //call api
        String result = "";
        SampleModel sampleModel = null;
        try {
            result = restApiUtil.postApi(requestMap);
            Gson gson = new Gson();
            Map<String,SampleModel> resMap = gson.fromJson(result,new TypeToken<Map<String,SampleModel>>(){}.getType());
            sampleModel = (SampleModel)requestMap.get("ResponseBody");
            return sampleModel;
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }
}
