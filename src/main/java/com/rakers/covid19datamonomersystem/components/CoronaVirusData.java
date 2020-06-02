package com.rakers.covid19datamonomersystem.components;


import com.rakers.covid19datamonomersystem.models.CoronaCountryModel;

import java.util.Map;

public interface CoronaVirusData {

    void setData(String uri);

    Map<String, CoronaCountryModel> getCountryDataMap();

    void setCountryDataMap();

}
