package com.npci.training.level6;

import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import com.npci.training.providers.CSVArgumentsProvider;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 6 (JUnit 5) - Test 01: CSV Data-Driven Testing
 * 
 * Topics Covered:
 * - @CsvFileSource for CSV file data
 * - @CsvSource for inline CSV data
 * - @ArgumentsSource with custom CSV provider
 * - Data-driven testing patterns
 * 
 * Duration: 20 minutes
 */
@DisplayName("CSV Data-Driven Testing")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test01_CSVDataDriven extends BaseTest {
    
    /**
     * Test using @CsvFileSource (reading from file)
     */
    @ParameterizedTest(name = "CSV file test #{index}: user={0}, expected={3}")
    @CsvFileSource(
        resources = "/testdata/login-data.csv",
        numLinesToSkip = 1
    )
    @Order(1)
    @Tag("smoke")
    @Tag("csv")
    @DisplayName("Login with CSV file data")
    public void testLoginWithCsvFile(String username, String password, 
                                     String usertype, String expected) {
        System.out.println("\n=== CSV File Test ===");
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
     * Test using @CsvSource (inline data)
     */
    @ParameterizedTest(name = "Inline CSV: user={0}")
    @CsvSource({
        "admin, admin123, Customer, success",
        "user1, user123, Customer, success",
        "merchant1, merchant123, Merchant, success",
        "wrong, wrong, Customer, fail"
    })
    @Order(2)
    @Tag("smoke")
    @DisplayName("Login with inline CSV data")
    public void testLoginWithInlineCsv(String username, String password, 
                                      String usertype, String expected) {
        System.out.println("\n=== Inline CSV Test: " + username + " ===");
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        if ("success".equals(expected)) {
            DashboardPage dashboard = loginPage.loginAs(username, password, usertype);
            assertTrue(dashboard.isDashboardPageDisplayed());
            System.out.println("✓ Success");
        } else {
            loginPage.enterUsername(username)
                    .enterPassword(password)
                    .selectUserType(usertype)
                    .acceptTerms()
                    .clickLoginExpectingError();
            assertTrue(loginPage.isLoginPageDisplayed());
            System.out.println("✓ Failed as expected");
        }
    }
    
    /**
     * Test using custom ArgumentsProvider
     */
    @ParameterizedTest(name = "Custom provider: user={0}")
    @ArgumentsSource(CSVArgumentsProvider.LoginDataProvider.class)
    @Order(3)
    @Tag("regression")
    @DisplayName("Login with custom CSV ArgumentsProvider")
    public void testLoginWithCustomProvider(String username, String password, 
                                           String usertype, String expected) {
        System.out.println("\n=== Custom Provider Test: " + username + " ===");
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        if ("success".equals(expected)) {
            if (!username.isEmpty() && !password.isEmpty() && !usertype.isEmpty()) {
                DashboardPage dashboard = loginPage.loginAs(username, password, usertype);
                assertTrue(dashboard.isDashboardPageDisplayed());
                System.out.println("✓ Success");
            }
        } else {
            if (!username.isEmpty()) {
                loginPage.enterUsername(username)
                        .enterPassword(password);
                if (!usertype.isEmpty()) {
                    loginPage.selectUserType(usertype);
                }
                loginPage.acceptTerms()
                        .clickLoginExpectingError();
            } else {
                loginPage.clickLoginExpectingError();
            }
            assertTrue(loginPage.isLoginPageDisplayed());
            System.out.println("✓ Failed as expected");
        }
    }
}

/*
 * CSV DATA-DRIVEN TESTING NOTES:
 * 
 * 1. @CsvFileSource - Best for external test data
 *    - Data in separate CSV file
 *    - Easy to modify without code changes
 *    - Non-technical people can add test cases
 * 
 * 2. @CsvSource - Best for small static datasets
 *    - Data inline in code
 *    - Quick to write
 *    - Good for few test cases
 * 
 * 3. @ArgumentsSource - Best for complex logic
 *    - Custom data provider
 *    - Can add validation
 *    - Can read from multiple sources
 * 
 * Run Commands:
 * mvn test -Dtest=Test01_CSVDataDriven
 * mvn test -Dgroups="csv"
 */
