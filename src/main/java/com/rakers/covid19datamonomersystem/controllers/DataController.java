package com.rakers.covid19datamonomersystem.controllers;

import com.rakers.covid19datamonomersystem.models.CoronaCountryModel;
import com.rakers.covid19datamonomersystem.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class DataController {

    @Autowired
    DataService dataService;

    @GetMapping("/data")
    @ResponseBody
    public Map<String, CoronaCountryModel> data(){
        return dataService.getData();
    }
}
