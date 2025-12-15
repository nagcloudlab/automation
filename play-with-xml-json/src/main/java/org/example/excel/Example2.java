package org.example.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class Example2 {
    public static void main(String[] args) throws IOException {

        FileInputStream fis = new FileInputStream("employees.xlsx");
        Workbook workbook = new XSSFWorkbook(fis);

        Sheet sheet = workbook.getSheet("Employees");

        for (Row row : sheet) {
            for (Cell cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }

        workbook.close();
        fis.close();


    }



    String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getLocalDateTimeCellValue().toString()
                    : String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

}



//Row row = sheet.getRow(1);
//Cell cell = row.getCell(2);
//
//double salary = cell.getNumericCellValue();


//List<Employee> employees = new ArrayList<>();
//
//for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//Row row = sheet.getRow(i);
//
//Employee e = new Employee();
//e.id = (int) row.getCell(0).getNumericCellValue();
//e.name = row.getCell(1).getStringCellValue();
//e.salary = row.getCell(2).getNumericCellValue();
//
//    employees.add(e);
//}


//SXSSFWorkbook workbook = new SXSSFWorkbook(100); // keep 100 rows in memory
//Sheet sheet = workbook.createSheet("BigData");
//
//for (int i = 0; i < 1_000_000; i++) {
//Row row = sheet.createRow(i);
//    row.createCell(0).setCellValue(i);
//}
//
//        workbook.write(new FileOutputStream("huge.xlsx"));
//        workbook.dispose();
