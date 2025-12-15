package org.example.excel;

/*

Workbook
 ├── Sheet
 │    ├── Row
 │    │    ├── Cell


 */

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

public class Example1 {
    public static void main(String[] args) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employees");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Salary");

        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue(101);
        row1.createCell(1).setCellValue("Nag");
        row1.createCell(2).setCellValue(90000);


        // Styling Example
//        CellStyle style = workbook.createCellStyle();
//
//        Font font = workbook.createFont();
//        font.setBold(true);
//        font.setFontHeightInPoints((short) 12);
//
//        style.setFont(font);
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//        Cell cell = header.getCell(0);
//        cell.setCellStyle(style);

        // auto-size columns
//        for (int i = 0; i < 3; i++) {
//            sheet.autoSizeColumn(i);
//        }

        // Different Data Types
//        cell.setCellValue("Text");          // String
//        cell.setCellValue(123);             // Numeric
//        cell.setCellValue(true);            // Boolean
//        cell.setCellValue(LocalDate.now()); // Date


        // Date Formatting
//        CellStyle dateStyle = workbook.createCellStyle();
//        CreationHelper helper = workbook.getCreationHelper();
//
//        dateStyle.setDataFormat(
//                helper.createDataFormat().getFormat("dd-MM-yyyy")
//        );
//        cell.setCellStyle(dateStyle);



        FileOutputStream fos = new FileOutputStream("employees.xlsx");
        workbook.write(fos);

        fos.close();
        workbook.close();


    }
}
