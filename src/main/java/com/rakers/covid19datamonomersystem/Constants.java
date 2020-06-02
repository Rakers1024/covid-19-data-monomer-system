package com.rakers.covid19datamonomersystem;

public interface Constants {

    String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    String DEATH_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
    int NOT_FOUND = 404;
    int SUCCESS = 200;
    String STATE = "Province/State";
    String COUNTRY = "Country/Region";
    String LATITUDE = "Lat";
    String LONGITUDE = "Long";
}
