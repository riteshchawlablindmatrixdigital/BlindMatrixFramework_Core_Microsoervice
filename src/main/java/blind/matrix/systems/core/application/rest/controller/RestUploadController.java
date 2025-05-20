package blind.matrix.systems.core.application.rest.controller;

import blind.matrix.systems.core.application.helpers.BlindMatrixSystemsHelper;
import blind.matrix.systems.core.application.models.UploadModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@ResponseBody
public class RestUploadController {

    private final Logger logger = LoggerFactory.getLogger(RestUploadController.class);

    @Autowired
    private BlindMatrixSystemsHelper blindMatrixSystemsHelper;

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
            /*@RequestParam("extraField") String extraField,
            @RequestParam("files") MultipartFile[] uploadfiles*/) throws JsonProcessingException {

        logger.debug("Multiple file upload!");

        /*String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
        */List<List<String>> resultData = null;
        try {
            //saveUploadedFiles(Arrays.asList(uploadfiles));
            resultData = blindMatrixSystemsHelper.getJsonDataFromExcelFile(BlindMatrixSystemsHelper.filePathExcel);
            resultData.get(0).forEach(System.out::println);
            resultData.get(1).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<List<String>> dataList = new ArrayList<>();
        dataList.add(resultData.get(0));

        dataList.add(resultData.get(1));

        //dataList.add(resultData.get(2));
        String result = new ObjectMapper().writeValueAsString(dataList);
        return result;

    }

    // maps html form to a Model
    @PostMapping("/api/upload/multi/model")
    public String multiUploadFileModel(@ModelAttribute UploadModel model) {

        logger.debug("Multiple file upload! With UploadModel");

        try {

            saveUploadedFiles(Arrays.asList(model.getFiles()));

        } catch (IOException e) {
            return HttpStatus.BAD_REQUEST.getReasonPhrase();
        }

        return "Successfully uploaded!";

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
