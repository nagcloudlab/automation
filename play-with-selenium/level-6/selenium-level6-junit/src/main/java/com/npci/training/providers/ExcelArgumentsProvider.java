package com.npci.training.providers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * ExcelArgumentsProvider - Custom ArgumentsProvider for JUnit 5
 * 
 * Reads test data from Excel files and provides to @ParameterizedTest
 * 
 * Usage:
 * @ParameterizedTest
 * @ArgumentsSource(ExcelArgumentsProvider.LoginDataProvider.class)
 * void test(String user, String pass, String type, String expected) { }
 */
public class ExcelArgumentsProvider {
    
    /**
     * Login data provider from Excel
     */
    public static class LoginDataProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            String filePath = "src/main/resources/testdata/login-data.xlsx";
            return getExcelData(filePath, "LoginData");
        }
    }

    /**
     * Generic Excel provider
     */
    public static class GenericExcelProvider implements ArgumentsProvider {
        private final String filePath;
        private final String sheetName;
        
        public GenericExcelProvider(String filePath, String sheetName) {
            this.filePath = filePath;
            this.sheetName = sheetName;
        }
        
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return getExcelData(filePath, sheetName);
        }
    }
    
    /**
     * Read Excel file and return as Stream of Arguments
     */
    public static Stream<Arguments> getExcelData(String filePath, String sheetName) {
        List<Arguments> data = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.err.println("Sheet not found: " + sheetName);
                return Stream.empty();
            }
            
            // Skip header row (row 0)
            int rowCount = sheet.getLastRowNum();
            
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                int cellCount = row.getLastCellNum();
                Object[] rowData = new Object[cellCount];
                
                for (int j = 0; j < cellCount; j++) {
                    Cell cell = row.getCell(j);
                    rowData[j] = getCellValue(cell);
                }
                
                data.add(Arguments.of(rowData));
            }
            
        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
        }
        
        return data.stream();
    }
    
    /**
     * Get cell value as String
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Return as integer string if whole number
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (long) numValue) {
                        return String.valueOf((long) numValue);
                    }
                    return String.valueOf(numValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
    
    /**
     * Utility method to get Excel data directly
     */
    public static Stream<Arguments> fromExcel(String filePath, String sheetName) {
        return getExcelData(filePath, sheetName);
    }
    
    /**
     * Print Excel data for debugging
     */
    public static void printExcelData(String filePath, String sheetName) {
        List<Arguments> data = getExcelData(filePath, sheetName)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        System.out.println("\n=== Excel Data from " + sheetName + " ===");
        for (int i = 0; i < data.size(); i++) {
            System.out.print("Row " + (i + 1) + ": ");
            Object[] args = data.get(i).get();
            for (Object obj : args) {
                System.out.print(obj + " | ");
            }
            System.out.println();
        }
    }
}
