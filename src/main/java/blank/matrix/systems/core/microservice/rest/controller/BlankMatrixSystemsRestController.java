package blank.matrix.systems.core.microservice.rest.controller;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@ResponseBody
@RequestMapping("/blank-matrix-systems/apis")
public class BlankMatrixSystemsRestController {

    private final Logger logger = LoggerFactory.getLogger(BlankMatrixSystemsRestController.class);

    @Autowired
    private BlankMatrixSystemsHelper blankMatrixSystemsHelper;

    private static AtomicInteger atomicInteger = new AtomicInteger(100);

    @Autowired
    private BlankMatrixSystemsSnowflakeJDBC blankMatrixSystemsSnowflakeJDBC;

    private static Map<Integer, String> preFilledDataMap = new LinkedHashMap<>();

    private static String UPLOADED_FOLDER = "C:\\Users\\RiteshNasibAnjlinaYS\\Documents";

    @PostMapping("/api/getDataColumns")
    public @ResponseBody String getHeaders() throws JsonProcessingException {
        BlankMatrixSystemsSnowflakeJDBC.DataWrapper wrapped = blankMatrixSystemsSnowflakeJDBC.getData(BlankMatrixSystemsSnowflakeJDBC.selectSQL, "20000");
        String finalStr = "";
        String intermediateStr = "";
        for (String columnMapValue : wrapped.getColumnMap().keySet()) {
            intermediateStr = intermediateStr.concat(String.format(" { \"title\" : \"%s\" },", columnMapValue));
        }
        int length = intermediateStr.length();
        String finalString = String.format("[ %s ]", intermediateStr.substring(0, length - 1));
        return finalString;
    }

    @PostMapping("/execute-query/results")
    public @ResponseBody String executeQueryResults(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    @RequestBody BlankMatrixSystemsRequestData blankMatrixSystemsRequestData) {
        BlankMatrixSystemsSnowflakeJDBC.DataWrapper wrappedData = null;
        List<String> resultDataList = new ArrayList<>();
        String finalDataStr = null;
        try {
            wrappedData = blankMatrixSystemsSnowflakeJDBC.getData(blankMatrixSystemsRequestData.getQuery(), blankMatrixSystemsRequestData.getNumberOfResults());
            if (wrappedData != null) {
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
            }
            finalDataStr = String.format("{ \"headers\" :  %s , \"queryData\" :  %s  }", resultDataList.get(0), resultDataList.get(1));
            preFilledDataMap.put(atomicInteger.getAndIncrement(), resultDataList.get(1));
        } catch (Exception ex) {
            return UniversalExceptionHandler.getControllerException(request, ex).toString();
        }
        return finalDataStr;
    }

    @PostMapping("/api/getData")
    public @ResponseBody String getData(@RequestBody BlankMatrixSystemsRequestData blankMatrixSystemsRequestData) throws JsonProcessingException {
        BlankMatrixSystemsSnowflakeJDBC.DataWrapper wrapped = null;
        List<List<String>> valuesToSend = new ArrayList<>();
        String finalString = "";
        try {
            wrapped = blankMatrixSystemsSnowflakeJDBC.getData(blankMatrixSystemsRequestData.getQuery(), blankMatrixSystemsRequestData.getNumberOfResults());
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return finalString;
    }

    @PostMapping("/getQueryData")
    public @ResponseBody String getQueryData() {
        Integer integer = preFilledDataMap.keySet().stream().max(Integer::compareTo).get();
        String resultStre = preFilledDataMap.get(integer);
        return resultStre;
    }

    @PostMapping("/api/getjSONData")
    public String getJsonData() throws JsonProcessingException {
        String result = null;
        List<List<String>> resultData = null;
        try {
            resultData = blankMatrixSystemsHelper.getJsonDataFromExcelFile(BlankMatrixSystemsHelper.filePathExcel);
            List<String> headers = blankMatrixSystemsHelper.getHeadersFromExcelFile(null);
            List<Map<String, String>> resultMapList = new ArrayList<>();
            int z = 0;
            int k = 0;
            for (List<String> element : resultData) {
                z = 0;
                //if (k != 0) {
                Map<String, String> linkedHashMap = new LinkedHashMap<>();
                for (String dataStr : element) {
                    if (StringUtils.isEmpty(dataStr)) {
                        dataStr = "";
                    }
                    linkedHashMap.put(headers.get(z), dataStr);
                    z++;
                }
                resultMapList.add(linkedHashMap);
                //}
                k++;
            }
            Map<String, String> linkedHashMapA = new LinkedHashMap<>();
            int y = 0;
            for (String dataStr : headers) {
                if (StringUtils.isEmpty(dataStr)) {
                    dataStr = "";
                }
                linkedHashMapA.put(headers.get(y), dataStr);
                y++;
            }
            Object[] dataObject = new Object[2];
            dataObject[0] = resultMapList;
            dataObject[1] = linkedHashMapA;
            result = new ObjectMapper().writeValueAsString(dataObject);

        } catch (IOException e) {
            result = String.format("%s%s", HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return result;
    }
}
