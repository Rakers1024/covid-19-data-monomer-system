package com.rakers.covid19datamonomersystem.services;

import com.rakers.covid19datamonomersystem.Constants;
import com.rakers.covid19datamonomersystem.exceptions.APIRuntimeException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@Service
public class TranslateServiceImpl implements TranslateService, Constants {
    private static final Logger logger = LoggerFactory.getLogger(TranslateServiceImpl.class);
    private static final String URL_TRANS = "https://translate.google.cn/translate_a/single?client=gtx&sl=en&tl=zh&dt=t&q=";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public String usToCn(String text) {
        //一些特殊专有词
        if("US".equals(text))
            return "美国";
        if("Turkey".equals(text))
            return "土耳其";


        String url = null;
        try {
            url = URL_TRANS+ URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("字符转换异常", e);
            e.printStackTrace();
        }
        return getData(url);
    }

    /**
     * 通过连接获取数据
     * @param url
     * @return
     */
    public String getData(String url){
        ValueOperations<String, String> vot = stringRedisTemplate.opsForValue();
        if (stringRedisTemplate.hasKey(url)) {
            try {
                logger.info("在redis中找到key="+url);
                return vot.get(url);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
                throw new APIRuntimeException("从redis获取数据时发生错误!");
            }
        }else {
            logger.info("在redis中未找到key");
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                HttpGet getRequest = new HttpGet(url);
                HttpResponse response = null;
                response = httpClient.execute(getRequest);
                int statusCode = NOT_FOUND;
                if (response != null && response.getStatusLine() != null)
                    statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != SUCCESS) {
                    throw new APIRuntimeException("Failed with HTTP error code : " + statusCode);
                }
                HttpEntity httpEntity = response.getEntity();
                String apiOutput = EntityUtils.toString(httpEntity);
                apiOutput = apiOutput.substring(4, apiOutput.indexOf(",") - 1);
                vot.set(url, apiOutput);
                return apiOutput;
            } catch (IOException | APIRuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
