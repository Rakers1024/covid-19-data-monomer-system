package com.rakers.covid19datamonomersystem.services;

import com.rakers.covid19datamonomersystem.components.CoronaVirusData;
import com.rakers.covid19datamonomersystem.exceptions.APIRuntimeException;
import com.rakers.covid19datamonomersystem.models.CoronaCountryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DataServiceImpl implements DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

    @Autowired
    CoronaVirusData virusData;

    @Override
    public Map<String, CoronaCountryModel> getData() {
        Map<String, CoronaCountryModel> dataMap;
        try {
            dataMap = virusData.getCountryDataMap();
        }catch (Exception e){
            logger.error("获取数据异常");
            throw new APIRuntimeException(e);
        }
        return dataMap;
    }
}
