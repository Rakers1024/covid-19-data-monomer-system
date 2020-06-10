package com.rakers.covid19datamonomersystem.services;

import com.rakers.covid19datamonomersystem.models.CoronaCountryModel;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface DataService {

    Map<String, CoronaCountryModel> getData();
}
