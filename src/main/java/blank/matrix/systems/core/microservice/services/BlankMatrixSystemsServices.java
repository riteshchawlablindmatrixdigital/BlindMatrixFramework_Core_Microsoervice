package blank.matrix.systems.core.microservice.services;

import blank.matrix.systems.core.microservice.databases.BlankMatrixSystemsSnowflakeJDBC;
import blank.matrix.systems.core.microservice.dtos.BlankMatrixSystemsRequestData;
import blank.matrix.systems.core.microservice.exception.UniversalExceptionHandler;
import blank.matrix.systems.core.microservice.helpers.BlankMatrixSystemsHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BlankMatrixSystemsServices {

    private static AtomicInteger atomicInteger = new AtomicInteger(100);

    @Autowired
    private BlankMatrixSystemsSnowflakeJDBC blankMatrixSystemsSnowflakeJDBC;

    private static Map<Integer, String> preFilledDataMap = new LinkedHashMap<>();

    public String getDataColumns(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestBody BlankMatrixSystemsRequestData blankMatrixSystemsRequestData) throws Exception {
        BlankMatrixSystemsSnowflakeJDBC.DataWrapper wrapped = null;
        String finalStr = "";
        String intermediateStr = "";
        String finalString = "";
        try {
            wrapped = blankMatrixSystemsSnowflakeJDBC.getData(blankMatrixSystemsRequestData.getQuery(),
                    blankMatrixSystemsRequestData.getNumberOfResults());
            if(wrapped != null && wrapped.getDataList() != null && wrapped.getColumnMap() != null) {
                for (String columnMapValue : wrapped.getColumnMap().keySet()) {
                    intermediateStr = intermediateStr.concat(String.format(" { \"title\" : \"%s\" },", columnMapValue));
                }
                int length = intermediateStr.length();
                finalString = String.format("[ %s ]", intermediateStr.substring(0, length - 1));
            } else {
                finalString = "No Data Available for the Query";
            }
        } catch (Exception e) {
            throw e;
        }
        return finalString;
    }

    public String executeQueryResults(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    BlankMatrixSystemsRequestData blankMatrixSystemsRequestData) throws Exception {
        BlankMatrixSystemsSnowflakeJDBC.DataWrapper wrappedData = null;
        List<String> resultDataList = new ArrayList<>();
        String finalDataStr = null;
        try {
            wrappedData = blankMatrixSystemsSnowflakeJDBC.getData(blankMatrixSystemsRequestData.getQuery(), blankMatrixSystemsRequestData.getNumberOfResults());
            if(wrappedData != null && wrappedData.getDataList() != null && wrappedData.getColumnMap() != null) {
                String finalStr = "";
                String intermediateStr = "";
                for (String columnMapValue : wrappedData.getColumnMap().keySet()) {
                    intermediateStr = intermediateStr.concat(String.format(" { \"title\" : \"%s\" },", columnMapValue));
                }
                int length = intermediateStr.length();
                resultDataList.add(String.format("[ %s ]", intermediateStr.substring(0, length - 1)));
                List<List<String>> valuesToSend = new ArrayList<>();
                List<Map<String, Object>> allData = wrappedData.getDataList();
                for (Map<String, Object> mapObj : allData) {
                    List<String> valuesList = new ArrayList<>();
                    for (Map.Entry<String, Object> entry : mapObj.entrySet()) {
                        entry.setValue(String.valueOf(entry.getValue()));
                        valuesList.add(String.valueOf(entry.getValue()));
                    }
                    valuesToSend.add(valuesList);
                }
                String dataJsonStringVal = new GsonBuilder().create().toJson(valuesToSend);
                String finalString = String.format("{ \"draw\" : \"%s\", \"recordsTotal\" : %s, \"recordsFiltered\" : \"%s\", \"data\": %s }",
                        4, wrappedData.getDataList().size(), wrappedData.getDataList().size(), dataJsonStringVal);
                resultDataList.add(finalString);
                finalDataStr = String.format("{ \"headers\" :  %s , \"queryData\" :  %s  }", resultDataList.get(0), resultDataList.get(1));
                preFilledDataMap.put(atomicInteger.getAndIncrement(), resultDataList.get(1));
            } else {
                finalDataStr = "No Data Available for the Query";
            }
        } catch (Exception ex) {
            throw ex;
        }
        return finalDataStr;
    }

    public String getData(HttpServletRequest request,
                                        HttpServletResponse response,
                                        BlankMatrixSystemsRequestData blankMatrixSystemsRequestData) throws Exception {
        BlankMatrixSystemsSnowflakeJDBC.DataWrapper wrapped = null;
        List<List<String>> valuesToSend = new ArrayList<>();
        String finalString = "";
        try {
            wrapped = blankMatrixSystemsSnowflakeJDBC.getData(blankMatrixSystemsRequestData.getQuery(), blankMatrixSystemsRequestData.getNumberOfResults());
            if(wrapped != null && wrapped.getDataList() != null && wrapped.getColumnMap() != null) {
                List<Map<String, Object>> allData = wrapped.getDataList();
                for (Map<String, Object> mapObj : allData) {
                    List<String> valuesList = new ArrayList<>();
                    for (Map.Entry<String, Object> entry : mapObj.entrySet()) {
                        entry.setValue(String.valueOf(entry.getValue()));
                        valuesList.add(String.valueOf(entry.getValue()));
                    }
                    valuesToSend.add(valuesList);
                }
                String dataJsonStringVal = new GsonBuilder().create().toJson(valuesToSend);
                finalString = String.format("{ \"draw\" : \"%s\", \"recordsTotal\" : %s, \"recordsFiltered\" : \"%s\", \"data\": %s }",
                        1, allData.size(), allData.size(), dataJsonStringVal);
            } else {
                finalString = "No Data Available for the Query";
            }
        } catch (Exception e) {
            throw e;
        }
        return finalString;
    }

    public String getQueryData() {
        Integer integer = preFilledDataMap.keySet().stream().max(Integer::compareTo).get();
        String resultStre = preFilledDataMap.get(integer);
        return resultStre;
    }

}
