package com.rakers.covid19datamonomersystem.controllers;

import com.rakers.covid19datamonomersystem.services.TranslateService;
import com.rakers.covid19datamonomersystem.services.TranslateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TranslateController {

    @Autowired
    TranslateServiceImpl tsi;

    @GetMapping("/tl/{text}")
    @ResponseBody
    public String tl(@PathVariable("text") String text){
        return tsi.usToCn(text);
    }

}
