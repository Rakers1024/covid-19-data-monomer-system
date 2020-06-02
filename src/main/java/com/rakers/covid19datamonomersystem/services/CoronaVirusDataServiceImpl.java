package com.rakers.covid19datamonomersystem.services;

import com.rakers.covid19datamonomersystem.Constants;
import com.rakers.covid19datamonomersystem.exceptions.APIRuntimeException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
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
import java.io.StringReader;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Service
public class CoronaVirusDataServiceImpl implements CoronaVirusDataService, Constants {
    private static final Logger logger = LoggerFactory.getLogger(CoronaVirusDataServiceImpl.class);
    private final static long TIME_OUT = 1800L;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String fetchVirusData(String uri) {
        logger.info("开始获取数据");
        ValueOperations<String, String> vot = stringRedisTemplate.opsForValue();
        if (stringRedisTemplate.hasKey(uri)) {
            logger.info("在redis中找到key="+uri);
            try {
                return vot.get(uri);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
                throw new APIRuntimeException("从redis获取数据时发生错误!");
            }
        }else{
            logger.info("在redis中未找到key");
            String apiOutput = null;
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                HttpGet getRequest = new HttpGet(uri);
                HttpResponse response = null;
                response = httpClient.execute(getRequest);
                int statusCode = NOT_FOUND;
                if (response != null && response.getStatusLine() != null)
                    statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != SUCCESS) {
                    throw new APIRuntimeException("Failed with HTTP error code : " + statusCode);
                }
                HttpEntity httpEntity = response.getEntity();
                apiOutput = EntityUtils.toString(httpEntity);
                vot.set(uri, apiOutput, TIME_OUT, TimeUnit.SECONDS);
            } catch (IOException | APIRuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return apiOutput;
        }
//        String apiOutput = null;
//        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
//            HttpGet getRequest = new HttpGet(uri);
//            HttpResponse response = null;
//            response = httpClient.execute(getRequest);
//            int statusCode = NOT_FOUND;
//            if (response != null && response.getStatusLine() != null)
//                statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode != SUCCESS) {
//                throw new APIRuntimeException("Failed with HTTP error code : " + statusCode);
//            }
//            HttpEntity httpEntity = response.getEntity();
//            apiOutput = EntityUtils.toString(httpEntity);
//        } catch (IOException | APIRuntimeException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return apiOutput;
    }

    @Override
    public Iterator<CSVRecord> parseCSVIterator(String uri) {
        Iterable<CSVRecord> records = null;
        try {
            StringReader csvReader = new StringReader(fetchVirusData(uri));
            records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records.iterator();
    }
}
