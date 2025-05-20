package blind.matrix.systems.core.application.helpers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class BlindMatrixSystemsHelper {

    public static final String filePathExcel = "C:\\Users\\RiteshNasibAnjlinaYS\\Downloads\\CustomerData_2025-05-19_Excel.xlsx";


    public List<List<String>> getJsonDataFromExcelFile(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePathExcel);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> data = new ArrayList<>();

        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        for (Cell cell : headerRow) {
            headers.add(cell.toString());
        }
        data.add(headers);

        return getJsonDataFromExcelFileWithoutHeaders(sheet, data);
    }

    private List<List<String>> getJsonDataFromExcelFileWithoutHeaders(Sheet sheet, List<List<String>> data) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                rowData.add(cell.toString());
            }
            data.add(rowData);
        }
        return data;
    }

    public List<String> getHeadersFromExcelFile(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> data = new ArrayList<>();

        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        for (Cell cell : headerRow) {
            headers.add(cell.toString());
        }
        return headers;
    }
}
