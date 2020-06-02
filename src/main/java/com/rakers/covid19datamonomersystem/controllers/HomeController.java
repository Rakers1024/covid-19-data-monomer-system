package com.rakers.covid19datamonomersystem.controllers;

import com.rakers.covid19datamonomersystem.components.CoronaVirusData;
import com.rakers.covid19datamonomersystem.models.CoronaCountryModel;
import com.rakers.covid19datamonomersystem.services.TranslateService;
import com.rakers.covid19datamonomersystem.services.TranslateServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    CoronaVirusData virusData;

    @Autowired
    TranslateService tls;

    @GetMapping("/")
    public String home(Model model) {
        try {
            Map<String, CoronaCountryModel> dataMap = virusData.getCountryDataMap();
            List<CoronaCountryModel> countryStats = new ArrayList<>();
            for (Map.Entry<String, CoronaCountryModel> map : dataMap.entrySet()) {
                CoronaCountryModel coronaCountryModel = new CoronaCountryModel(map.getValue());
                coronaCountryModel.setCountry(tls.usToCn(coronaCountryModel.getCountry()));
                countryStats.add(coronaCountryModel);
            }
            int totalReportedCases = dataMap.entrySet().stream().mapToInt(stat -> stat.getValue().getLatestCases()).sum();
            int totalNewCases = dataMap.entrySet().stream().mapToInt(stat -> stat.getValue().getDiffFromPrevDay()).sum();
            int totalDeaths = dataMap.entrySet().stream().mapToInt(stat -> stat.getValue().getDeath()).sum();
            int totalDeathsToday = dataMap.entrySet().stream().mapToInt(stat -> stat.getValue().getDeathDiffFromPrevDay()).sum();
            model.addAttribute("locationsStats", countryStats);
            model.addAttribute("totalReportedCases", totalReportedCases);
            model.addAttribute("totalNewCases", totalNewCases);
            model.addAttribute("totalDeaths", totalDeaths);
            model.addAttribute("totalDeathsToday", totalDeathsToday);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "home";
    }

    @GetMapping("/data")
    @ResponseBody
    public Map<String, CoronaCountryModel> data(){
        Map<String, CoronaCountryModel> dataMap=null;
        try {
            dataMap = virusData.getCountryDataMap();

        }catch (Exception e){
            e.printStackTrace();
        }
        return dataMap;
    }
}
