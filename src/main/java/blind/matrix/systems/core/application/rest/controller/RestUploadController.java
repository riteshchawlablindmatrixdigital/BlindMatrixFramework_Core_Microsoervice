package blind.matrix.systems.core.application.rest.controller;

import blind.matrix.systems.core.application.databases.SnowflakeJDBC;
import blind.matrix.systems.core.application.helpers.BlindMatrixSystemsHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@ResponseBody
public class RestUploadController {

    private final Logger logger = LoggerFactory.getLogger(RestUploadController.class);

    @Autowired
    private BlindMatrixSystemsHelper blindMatrixSystemsHelper;

    @Autowired
    private SnowflakeJDBC snowflakeJDBC;


    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "C:\\Users\\RiteshNasibAnjlinaYS\\Documents";

    //Single file upload
    @PostMapping("/api/upload")
    // If not @RestController, uncomment this
    //@ResponseBody
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


    // Multiple file upload
    @PostMapping("/api/upload/multi")
    public String uploadFileMulti(
            @RequestParam("extraField") String extraField,
            @RequestParam("files") MultipartFile[] uploadfiles) throws IOException {

        logger.debug("Multiple file upload!");

        try {
            String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                    .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
            saveUploadedFiles(Arrays.asList(uploadfiles));
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return "Success";
    }
    @PostMapping("/api/getDataColumns")
    public @ResponseBody String getHeaders() throws JsonProcessingException {
        SnowflakeJDBC.DataWrapper wrapped = snowflakeJDBC.getData();
        String finalStr = "";
        String intermediateStr = "";
        for(String columnMapValue : wrapped.getColumnMap().keySet()) {
            intermediateStr = intermediateStr.concat(String.format(" { \"title\" : \"%s\" },", columnMapValue));
        }
        int length = intermediateStr.length();
        String finalString = String.format("[ %s ]", intermediateStr.substring(0, length - 1));
        //String dataStr = new GsonBuilder().create().toJson(wrapped.getColumnMap().keySet());
        System.out.println(finalString);
        return finalString;
    }


    @PostMapping("/api/getData")
    public @ResponseBody String getData() throws JsonProcessingException {
        SnowflakeJDBC.DataWrapper wrapped = snowflakeJDBC.getData();
        /*List<Map<String, String>> dataFiltered = wrapped.getDataList().stream().map(stringObjectMap ->
                stringObjectMap.entrySet().stream().map(stringObjectEntry ->
                        stringObjectEntry*/
        //Map<String, String> finalMap = new LinkedHashMap<>();
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

        //String dataJsonString = new GsonBuilder().create().toJson(wrapped.getDataList());
        String dataJsonStringVal = new GsonBuilder().create().toJson(valuesToSend);
        String finalString = String.format("{ \"draw\" : \"%s\", \"recordsTotal\" : %s, \"recordsFiltered\" : \"%s\", \"data\": %s }",
                1, wrapped.getDataList().size(), wrapped.getDataList().size(), dataJsonStringVal);
        //String finalString1 = String.format("{ \n  \"data\" : %s  \n ", dataJsonString);
        System.out.println(finalString);
        return finalString;
    }
    // maps html form to a Model
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
