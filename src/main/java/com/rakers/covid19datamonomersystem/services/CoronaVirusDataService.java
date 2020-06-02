package com.rakers.covid19datamonomersystem.services;

import com.rakers.covid19datamonomersystem.exceptions.APIRuntimeException;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.Iterator;

public interface CoronaVirusDataService {

    String fetchVirusData(String uri) throws APIRuntimeException, IOException;

    Iterator<CSVRecord> parseCSVIterator(String uri);

}
