package blind.matrix.systems.core.application.rest.controller;

import blind.matrix.systems.core.application.databases.SnowflakeJDBC;
import blind.matrix.systems.core.application.dtos.RequestData;
import blind.matrix.systems.core.application.helpers.BlindMatrixSystemsHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@ResponseBody
@RequestMapping("/fiserv/white-data/apis")
public class RestUploadController {

    private final Logger logger = LoggerFactory.getLogger(RestUploadController.class);

    @Autowired
    private BlindMatrixSystemsHelper blindMatrixSystemsHelper;

    private static AtomicInteger atomicInteger = new AtomicInteger(100);

    @Autowired
    private SnowflakeJDBC snowflakeJDBC;

    private static Map<Integer, String> preFilledDataMap = new LinkedHashMap<>();

    private static String UPLOADED_FOLDER = "C:\\Users\\RiteshNasibAnjlinaYS\\Documents";

    @PostMapping("/api/getDataColumns")
    public @ResponseBody String getHeaders() throws JsonProcessingException {
        SnowflakeJDBC.DataWrapper wrapped = snowflakeJDBC.getData(SnowflakeJDBC.selectSQL, "20000");
        String finalStr = "";
        String intermediateStr = "";
        for(String columnMapValue : wrapped.getColumnMap().keySet()) {
            intermediateStr = intermediateStr.concat(String.format(" { \"title\" : \"%s\" },", columnMapValue));
        }
        int length = intermediateStr.length();
        String finalString = String.format("[ %s ]", intermediateStr.substring(0, length - 1));
        System.out.println(finalString);
        return finalString;
    }
    @PostMapping("/getQueryData")
    public @ResponseBody String getQueryData() {
        Integer integer = preFilledDataMap.keySet().stream().max(Integer::compareTo).get();
        String resultStre = preFilledDataMap.get(integer);
        System.out.println(resultStre);
        return resultStre;
    }

    @PostMapping("/execute-query/results")
    public @ResponseBody String executeQueryResults(@RequestBody RequestData requestData) {
        SnowflakeJDBC.DataWrapper wrappedData = snowflakeJDBC.getData(requestData.getQuery(), requestData.getNumberOfResults());
        List<String> resultDataList = new ArrayList<>();
        if(wrappedData != null) {
            String finalStr = "";
            String intermediateStr = "";
            for(String columnMapValue : wrappedData.getColumnMap().keySet()) {
                intermediateStr = intermediateStr.concat(String.format(" { \"title\" : \"%s\" },", columnMapValue));
            }
            int length = intermediateStr.length();
            resultDataList.add(String.format("[ %s ]", intermediateStr.substring(0, length - 1)));
            List<List<String>> valuesToSend = new ArrayList<>();
            List<Map<String, Object>> allData = wrappedData.getDataList();
            for(Map<String, Object> mapObj : allData) {
                List<String> valuesList = new ArrayList<>();
                for(Map.Entry<String, Object> entry : mapObj.entrySet()) {
                    entry.setValue(String.valueOf(entry.getValue()));
                    valuesList.add(String.valueOf(entry.getValue()));
                }
                valuesToSend.add(valuesList);
            }
            String dataJsonStringVal = new GsonBuilder().create().toJson(valuesToSend);
            String finalString = String.format("{ \"draw\" : \"%s\", \"recordsTotal\" : %s, \"recordsFiltered\" : \"%s\", \"data\": %s }",
                    1, wrappedData.getDataList().size(), wrappedData.getDataList().size(), dataJsonStringVal);
            resultDataList.add(finalString);
        }
        String finalDataStr = String.format("{ \"headers\" :  %s , \"queryData\" :  %s  }", resultDataList.get(0), resultDataList.get(1));
        System.out.println(finalDataStr);
        preFilledDataMap.put(atomicInteger.getAndIncrement(), resultDataList.get(1));
        return finalDataStr;
    }

    @PostMapping("/api/getData")
    public @ResponseBody String getData() throws JsonProcessingException {
        SnowflakeJDBC.DataWrapper wrapped = snowflakeJDBC.getData(SnowflakeJDBC.selectSQL, "20000");
        List<List<String>> valuesToSend = new ArrayList<>();
        List<Map<String, Object>> allData = wrapped.getDataList();
        for(Map<String, Object> mapObj : allData) {
            List<String> valuesList = new ArrayList<>();
            for(Map.Entry<String, Object> entry : mapObj.entrySet()) {
                entry.setValue(String.valueOf(entry.getValue()));
                valuesList.add(String.valueOf(entry.getValue()));
            }
            valuesToSend.add(valuesList);
        }
        String dataJsonStringVal = new GsonBuilder().create().toJson(valuesToSend);
        String finalString = String.format("{ \"draw\" : \"%s\", \"recordsTotal\" : %s, \"recordsFiltered\" : \"%s\", \"data\": %s }",
                1, wrapped.getDataList().size(), wrapped.getDataList().size(), dataJsonStringVal);
        System.out.println(finalString);
        return finalString;
    }

    @PostMapping("/api/upload")
    @ResponseBody
    public String uploadFile(
            @RequestParam("file") MultipartFile uploadfile) {
        logger.debug("Single file upload!");
        if (uploadfile.isEmpty()) {
            return "please select a file!";
        }
        try {
            saveUploadedFiles(Arrays.asList(uploadfile));
        } catch (IOException e) {
            return HttpStatus.BAD_REQUEST.getReasonPhrase();
        }
        return "Successfully uploaded - " +
                uploadfile.getOriginalFilename();
    }


    @PostMapping("/api/getjSONData")
    public String getJsonData() throws JsonProcessingException {

        logger.debug("Multiple file upload! With UploadModel");
        String result = null;
        List<List<String>> resultData = null;
        try {
            //saveUploadedFiles(Arrays.asList(uploadfiles));
            resultData = blindMatrixSystemsHelper.getJsonDataFromExcelFile(BlindMatrixSystemsHelper.filePathExcel);
            List<String> headers = blindMatrixSystemsHelper.getHeadersFromExcelFile(null);
            List<Map<String, String>> resultMapList = new ArrayList<>();
            int z = 0;
            int k = 0;
            for (List<String> element : resultData) {
                z = 0;
                //if (k != 0) {
                    Map<String, String> linkedHashMap = new LinkedHashMap<>();
                    for (String dataStr : element) {
                        if(StringUtils.isEmpty(dataStr)) {
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
            int y=0;
            for (String dataStr : headers) {
                if(StringUtils.isEmpty(dataStr)) {
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

    //save file
    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

        }

    }
}
