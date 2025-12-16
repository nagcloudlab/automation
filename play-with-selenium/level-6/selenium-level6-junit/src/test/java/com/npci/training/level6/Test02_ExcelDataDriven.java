package com.npci.training.level6;

import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import com.npci.training.providers.ExcelArgumentsProvider;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 6 (JUnit 5) - Test 02: Excel Data-Driven Testing
 * 
 * Topics Covered:
 * - Reading test data from Excel files (.xlsx)
 * - Apache POI library for Excel processing
 * - @ArgumentsSource with Excel data
 * - Multiple sheets support
 * 
 * Duration: 25 minutes
 */
@DisplayName("Excel Data-Driven Testing")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test02_ExcelDataDriven extends BaseTest {
    
    /**
     * Test using Excel data via ArgumentsProvider
     */
    @ParameterizedTest(name = "Excel test #{index}: user={0}, expected={3}")
//    @MethodSource("getExcelData")
    @ArgumentsSource(ExcelArgumentsProvider.LoginDataProvider.class)
    @Order(1)
    @Tag("smoke")
    @Tag("excel")
    @DisplayName("Login with Excel file data")
    public void testLoginWithExcelData(String username, String password, 
                                       String usertype, String expected) {
        System.out.println("\n=== Excel Data Test ===");
        System.out.println("Username: " + username);
        System.out.println("Expected: " + expected);
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        if ("success".equals(expected)) {
            if (!username.isEmpty() && !password.isEmpty() && !usertype.isEmpty()) {
                DashboardPage dashboard = loginPage.loginAs(username, password, usertype);
                assertTrue(dashboard.isDashboardPageDisplayed());
                System.out.println("✓ Login successful");
            }
        } else {
            if (!username.isEmpty()) loginPage.enterUsername(username);
            if (!password.isEmpty()) loginPage.enterPassword(password);
            if (!usertype.isEmpty()) loginPage.selectUserType(usertype);
            if (!username.isEmpty() || !password.isEmpty()) {
                loginPage.acceptTerms();
            }
            loginPage.clickLoginExpectingError();
            
            assertTrue(loginPage.isLoginPageDisplayed());
            System.out.println("✓ Login failed as expected");
        }
    }
    
    /**
     * Test verifying Excel data can be read
     */
    @Test
    @Order(2)
    @Tag("verification")
    @DisplayName("Verify Excel data can be read correctly")
    public void testExcelDataReading() {
        System.out.println("\n=== Excel Data Reading Test ===");
        
        String filePath = "src/main/resources/testdata/test-data.xlsx";
        long dataCount = ExcelArgumentsProvider.getExcelData(filePath, "LoginData").count();
        
        assertTrue(dataCount > 0, "Excel should have data rows");
        
        System.out.println("✓ Excel file read successfully");
        System.out.println("✓ Total test scenarios: " + dataCount);
        
        // Print data for verification
        ExcelArgumentsProvider.printExcelData(filePath, "LoginData");
    }
    
    /**
     * Nested tests for Excel validation
     */
    @Nested
    @DisplayName("Excel Data Validation")
    class ExcelValidation {
        
        @Test
        @DisplayName("Verify Excel file exists")
        public void testExcelFileExists() {
            System.out.println("\n=== Excel File Existence Test ===");
            
            String filePath = "src/main/resources/testdata/test-data.xlsx";
            java.io.File file = new java.io.File(filePath);
            
            assertTrue(file.exists(), "Excel file should exist at: " + filePath);
            System.out.println("✓ Excel file exists");
        }
        
        @Test
        @DisplayName("Verify Excel has correct structure")
        public void testExcelStructure() {
            System.out.println("\n=== Excel Structure Test ===");
            
            String filePath = "src/main/resources/testdata/test-data.xlsx";
            var data = ExcelArgumentsProvider.getExcelData(filePath, "LoginData")
                .findFirst();
            
            assertTrue(data.isPresent(), "Excel should have at least one data row");
            
            Object[] firstRow = data.get().get();
            assertEquals(4, firstRow.length, "Each row should have 4 columns");
            
            System.out.println("✓ Excel structure verified");
            System.out.println("✓ Columns: username, password, usertype, expected");
        }
    }
}

/*
 * EXCEL DATA-DRIVEN TESTING NOTES:
 * 
 * 1. Excel File Structure:
 *    - First row: Headers (username, password, usertype, expected)
 *    - Remaining rows: Test data
 *    - Save as .xlsx format
 *    - Multiple sheets supported
 * 
 * 2. Creating Excel Files:
 *    - Microsoft Excel: Create → Save as .xlsx
 *    - Google Sheets: Create → Download as .xlsx
 *    - LibreOffice Calc: Create → Save as .xlsx
 *    - Place in: src/main/resources/testdata/
 * 
 * 3. Multiple Sheets Example:
 *    test-data.xlsx:
 *    ├── LoginData (username, password, usertype, expected)
 *    ├── RegistrationData (fullname, email, password, expected)
 *    └── PaymentData (amount, from, to, expected)
 * 
 * 4. Benefits:
 *    - Multiple test types in one file
 *    - Familiar tool for non-technical users
 *    - Data validation in Excel
 *    - Formulas and calculations
 * 
 * Run Commands:
 * mvn test -Dtest=Test02_ExcelDataDriven
 * mvn test -Dgroups="excel"
 */
