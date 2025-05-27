package blank.matrix.systems.core.microservice.helpers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class BlankMatrixSystemsHelper {

    public static final String filePathExcel = "";


    public List<List<String>> getJsonDataFromExcelFile(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePathExcel);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> data = new ArrayList<>();
        inputStream.close();
        return getJsonDataFromExcelFileWithoutHeaders(sheet, data);
    }

    private List<List<String>> getJsonDataFromExcelFileWithoutHeaders(Sheet sheet, List<List<String>> data) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                String cellValue = "";
                if(cell.toString() == null) {
                    cellValue = "";
                } else {
                    if (cell.getCellType() == CellType.FORMULA) {
                        switch (cell.getCachedFormulaResultType()) {
                            case BOOLEAN:
                                System.out.println(cell.getBooleanCellValue());
                                cellValue = String.valueOf(cell.getBooleanCellValue());                                break;
                            case NUMERIC:
                                System.out.println(cell.getNumericCellValue());
                                cellValue = String.valueOf(cell.getNumericCellValue());
                                break;
                            case STRING:
                                System.out.println(cell.getRichStringCellValue());
                                cellValue = String.valueOf(cell.getStringCellValue());
                                break;
                        }
                    } else if (cell.getCellType() == CellType.BOOLEAN) {
                        System.out.println(cell.getBooleanCellValue());
                        cellValue = String.valueOf(cell.getBooleanCellValue());
                    } else if (cell.getCellType() == CellType.NUMERIC) {
                        System.out.println(cell.getNumericCellValue());
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    } else if (cell.getCellType() == CellType.STRING) {
                        System.out.println(cell.getRichStringCellValue());
                        cellValue = String.valueOf(cell.getStringCellValue());
                    }
                }
                rowData.add(cellValue);
            }
            data.add(rowData);
        }
        return data;
    }

    public List<String> getHeadersFromExcelFile(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePathExcel);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> data = new ArrayList<>();

        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        for (Cell cell : headerRow) {
            headers.add(cell.toString());
        }
        inputStream.close();
        return headers;
    }
}
