package com.npci.training.providers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVDataProvider - Read test data from CSV files
 * 
 * Provides methods to read CSV files and convert to Object[][]
 * for TestNG DataProvider
 */
public class CSVDataProvider {
    
    /**
     * Read CSV file and return as Object[][]
     * @param filePath Path to CSV file
     * @return Test data as Object[][]
     */
    public static Object[][] getCSVData(String filePath) {
        List<Object[]> data = new ArrayList<>();
        
        try (FileReader reader = new FileReader(filePath);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            for (CSVRecord record : parser) {
                String username = record.get("username");
                String password = record.get("password");
                String usertype = record.get("usertype");
                String expected = record.get("expected");
                
                data.add(new Object[]{username, password, usertype, expected});
            }
            
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
        
        return data.toArray(new Object[0][]);
    }
    
    /**
     * Get login test data from CSV
     */
    public static Object[][] getLoginData() {
        String filePath = "src/main/resources/testdata/login-data.csv";
        return getCSVData(filePath);
    }
}
