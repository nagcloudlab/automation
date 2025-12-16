package com.npci.training.level6;

import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import com.npci.training.providers.CSVDataProvider;
import com.npci.training.tests.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Level 6 - Test 01: DataProvider with CSV
 * 
 * Topics Covered:
 * - TestNG @DataProvider annotation
 * - Reading data from CSV files
 * - Data-driven testing
 * - Parameterized tests
 * - Multiple test scenarios with one test method
 * 
 * Duration: 25 minutes
 */
public class Test01_DataProviderCSV extends BaseTest {
    
    /**
     * DataProvider method - Provides test data to test methods
     * Each row becomes one test execution
     */
    @DataProvider(name = "loginData")
    public Object[][] getLoginTestData() {
        return CSVDataProvider.getLoginData();
    }
    
    /**
     * Hardcoded DataProvider - Simple example
     */
    @DataProvider(name = "hardcodedData")
    public Object[][] getHardcodedData() {
        return new Object[][] {
            {"admin", "admin123", "Customer", "success"},
            {"user1", "user123", "Customer", "success"},
            {"wrong", "wrong", "Customer", "fail"}
        };
    }
    
    @Test(dataProvider = "loginData", description = "Login with CSV data")
    public void testLoginWithCSVData(String username, String password, String usertype, String expected) {
        System.out.println("\n=== Testing Login ===");
        System.out.println("Username: " + username);
        System.out.println("Password: " + (password.isEmpty() ? "[empty]" : "***"));
        System.out.println("Usertype: " + usertype);
        System.out.println("Expected: " + expected);
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        if ("success".equals(expected)) {
            // Should login successfully
            if (!username.isEmpty() && !password.isEmpty() && !usertype.isEmpty()) {
                DashboardPage dashboard = loginPage.loginAs(username, password, usertype);
                assertTrue(dashboard.isDashboardPageDisplayed(), 
                    "Login should succeed for: " + username);
                System.out.println("✓ Login successful as expected");
            }
        } else {
            // Should fail
            if (!username.isEmpty()) loginPage.enterUsername(username);
            if (!password.isEmpty()) loginPage.enterPassword(password);
            if (!usertype.isEmpty()) loginPage.selectUserType(usertype);
            if (!username.isEmpty() || !password.isEmpty()) {
                loginPage.acceptTerms();
            }
            loginPage.clickLoginExpectingError();
            
            assertTrue(loginPage.isLoginPageDisplayed(), 
                "Login should fail for: " + username);
            System.out.println("✓ Login failed as expected");
        }
    }
    
    @Test(dataProvider = "hardcodedData", description = "Login with hardcoded data")
    public void testLoginWithHardcodedData(String username, String password, String usertype, String expected) {
        System.out.println("\n=== Testing with Hardcoded Data ===");
        System.out.println("Username: " + username);
        System.out.println("Expected: " + expected);
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        if ("success".equals(expected)) {
            DashboardPage dashboard = loginPage.loginAs(username, password, usertype);
            assertTrue(dashboard.isDashboardPageDisplayed());
            System.out.println("✓ Test passed");
        } else {
            loginPage.enterUsername(username)
                    .enterPassword(password)
                    .selectUserType(usertype)
                    .acceptTerms()
                    .clickLoginExpectingError();
            assertTrue(loginPage.isLoginPageDisplayed());
            System.out.println("✓ Test passed");
        }
    }
    
    /**
     * DataProvider with parallel execution
     */
    @DataProvider(name = "parallelData", parallel = true)
    public Object[][] getParallelData() {
        return new Object[][] {
            {"admin"},
            {"user1"},
            {"merchant1"}
        };
    }
    
    @Test(dataProvider = "parallelData", description = "Parallel data provider test")
    public void testParallelExecution(String username) {
        System.out.println("\n=== Parallel Test for: " + username + " ===");
        System.out.println("Thread: " + Thread.currentThread().getName());
        
        LoginPage loginPage = new LoginPage(driver).open();
        assertTrue(loginPage.isLoginPageDisplayed());
        
        System.out.println("✓ Test completed for: " + username);
    }
    
    /**
     * Simple DataProvider example
     */
    @DataProvider(name = "usernameData")
    public Object[][] getUsernames() {
        return new Object[][] {
            {"admin"},
            {"user1"},
            {"merchant1"},
            {"testuser"}
        };
    }
    
    @Test(dataProvider = "usernameData", description = "Test username field")
    public void testUsernameEntry(String username) {
        System.out.println("\n=== Testing username: " + username + " ===");
        
        LoginPage loginPage = new LoginPage(driver)
            .open()
            .enterUsername(username);
        
        // Just verify page is still displayed
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Username entered successfully");
    }
}

/*
 * DATAPROVIDER NOTES:
 * 
 * 1. DataProvider Method Signature:
 *    @DataProvider(name = "dataName")
 *    public Object[][] methodName() { return data; }
 * 
 * 2. Using DataProvider in Test:
 *    @Test(dataProvider = "dataName")
 *    public void test(String param1, String param2) { }
 * 
 * 3. Data Format:
 *    Object[][] = {
 *        {param1, param2, param3},  // Test iteration 1
 *        {param1, param2, param3},  // Test iteration 2
 *    }
 * 
 * 4. Parallel Execution:
 *    @DataProvider(parallel = true)
 *    Runs data iterations in parallel
 * 
 * 5. External Data Sources:
 *    - CSV files (as shown)
 *    - Excel files (Level 6 next)
 *    - Database queries
 *    - JSON files
 *    - API responses
 */
