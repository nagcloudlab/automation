package com.npci.training.providers;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
/**
 * CSVArgumentsProvider - Custom ArgumentsProvider for JUnit 5
 * 
 * Reads test data from CSV files and provides to @ParameterizedTest
 * 
 * Usage:
 * @ParameterizedTest
 * @ArgumentsSource(CSVArgumentsProvider.LoginDataProvider.class)
 * void test(String user, String pass, String type, String expected) { }
 */
public class CSVArgumentsProvider {
    
    /**
     * Generic CSV data provider
     */
    public static class GenericCSVProvider implements ArgumentsProvider {
        private final String filePath;
        
        public GenericCSVProvider(String filePath) {
            this.filePath = filePath;
        }
        
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return getCSVData(filePath);
        }
    }
    
    /**
     * Login data provider
     */
    public static class LoginDataProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            String filePath = "src/main/resources/testdata/login-data.csv";
            return getCSVData(filePath);
        }
    }
    
    /**
     * Read CSV file and return as Stream of Arguments
     */
    private static Stream<Arguments> getCSVData(String filePath) {
        List<Arguments> data = new ArrayList<>();
        
        try (FileReader reader = new FileReader(filePath);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            
            for (CSVRecord record : parser) {
                String username = record.get("username");
                String password = record.get("password");
                String usertype = record.get("usertype");
                String expected = record.get("expected");
                
                data.add(Arguments.of(username, password, usertype, expected));
            }
            
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
        
        return data.stream();
    }
    
    /**
     * Utility method to get CSV data directly
     */
    public static Stream<Arguments> fromCSV(String filePath) {
        return getCSVData(filePath);
    }
}
