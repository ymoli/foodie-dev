package com.lank.cbk.apiUtil;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class RestApiUtil {
    static Logger logger = LoggerFactory.getLogger(RestApiUtil.class);

    @Autowired
    RestTemplate restTemplate;

    public String postApi(Map<String,Object> requestMap){
        Object requestBody = requestMap.get("reqBody");
        String urlStr = (String) requestMap.get("url");
        int port = (int)requestMap.get("port");
        String uri = (String)requestMap.get("uri");
        String url = this.buildUrl(urlStr,port,uri);
        logger.info("url: {}",url);
        Gson gson = new Gson();
        String requestBodyStr = gson.toJson(requestBody);
        logger.info("requestBodyStr: {}",requestBodyStr);
        try {
            Long start = System.currentTimeMillis();
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST,
                    this.buildRequestBody(requestBodyStr,requestMap),String.class);
            Long end = System.currentTimeMillis();
            Long time = end - start;
            logger.info("completed tiem {}",time + "ms");
            logger.info("responseEntity: "+responseEntity);
            int statusCodeValue = responseEntity.getStatusCodeValue();
            String body = responseEntity.getBody();
            logger.info("statusCodeValue: "+statusCodeValue);
            logger.info("responseBody: {} "+body);
            if (statusCodeValue == 200){
                return body;
            }
        } catch (HttpClientErrorException e){
            logger.error(e.getMessage());
            logger.error("resBody: "+e.getResponseBodyAsString());
            logger.error("rawStatusCode: "+e.getRawStatusCode());
            return e.getResponseBodyAsString();
        } catch (Exception e){
            logger.error(e.getMessage());
            return "";
        }
    }
    private String buildUrl(String url,int port,String uri){
        return new StringBuffer()
                .append(url)
                .append(":")
                .append(port)
                .append(uri)
                .toString();
    }
    private HttpEntity<String> buildRequestBody(String requsetBody, Map<String,Object> requestMap){
        String businessCode = (String) requestMap.get("businessCode");
        String channelId = (String) requestMap.get("channelId");
        String uuid = (String) requestMap.get("uuid");
        String accept = (String) requestMap.get("accept");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","application/json");
        if (StringUtils.isNoneBlank(businessCode)){
            httpHeaders.add("businessCode",businessCode);
        }
        if (StringUtils.isNoneBlank(channelId)){
            httpHeaders.add("channelId",channelId);
        }
        if (StringUtils.isNoneBlank(uuid)){
            httpHeaders.add("uuid",uuid);
        }
        if (StringUtils.isNoneBlank(accept)){
            httpHeaders.add("Accept",accept);
        }
        Gson gson = new Gson();
        logger.info("header {}",gson.toJson(httpHeaders));
        return new HttpEntity<String>(requsetBody,httpHeaders);
    }
}
