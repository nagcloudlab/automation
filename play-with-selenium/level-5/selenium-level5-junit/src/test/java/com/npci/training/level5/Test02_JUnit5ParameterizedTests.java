package com.npci.training.level5;

import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 5 (JUnit) - Test 02: Parameterized Tests
 * 
 * Topics Covered:
 * - @ParameterizedTest (like TestNG DataProvider)
 * - @ValueSource for simple parameters
 * - @CsvSource for multiple parameters
 * - @CsvFileSource for CSV file data
 * - @MethodSource for complex data
 * - @EnumSource for enum values
 * - Custom display names for parameterized tests
 * 
 * Duration: 30 minutes
 */
@DisplayName("JUnit 5 Parameterized Tests")
public class Test02_JUnit5ParameterizedTests extends BaseTest {
    
    /**
     * Simple parameterized test with @ValueSource
     */
    @ParameterizedTest(name = "Test username entry: {0}")
    @ValueSource(strings = {"admin", "user1", "merchant1", "testuser"})
    @Tag("regression")
    @DisplayName("Username field should accept various usernames")
    public void testUsernameEntry(String username) {
        System.out.println("\n=== Testing username: " + username + " ===");
        
        LoginPage loginPage = new LoginPage(driver)
            .open()
            .enterUsername(username);
        
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Username entered successfully");
    }
    
    /**
     * Parameterized test with @CsvSource - Multiple parameters
     */
    @ParameterizedTest(name = "Login test: user={0}, expected={3}")
    @CsvSource({
        "admin, admin123, Customer, success",
        "user1, user123, Customer, success",
        "merchant1, merchant123, Merchant, success",
        "wrong, wrong, Customer, fail"
    })
    @Tag("smoke")
    @Tag("login")
    @DisplayName("Login with various credentials")
    public void testLoginWithCsvData(String username, String password, 
                                     String usertype, String expected) {
        System.out.println("\n=== Login Test ===");
        System.out.println("Username: " + username);
        System.out.println("Expected: " + expected);
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        if ("success".equals(expected)) {
            DashboardPage dashboard = loginPage.loginAs(username, password, usertype);
            assertTrue(dashboard.isDashboardPageDisplayed(),
                "Login should succeed for: " + username);
            System.out.println("✓ Login successful");
        } else {
            loginPage.enterUsername(username)
                    .enterPassword(password)
                    .selectUserType(usertype)
                    .acceptTerms()
                    .clickLoginExpectingError();
            assertTrue(loginPage.isLoginPageDisplayed(),
                "Login should fail for: " + username);
            System.out.println("✓ Login failed as expected");
        }
    }
    
    /**
     * Parameterized test with @MethodSource for complex data
     */
    @ParameterizedTest(name = "Test with method source: {0}")
    @MethodSource("provideLoginData")
    @Tag("regression")
    @DisplayName("Login with method source data")
    public void testLoginWithMethodSource(String username, String password, 
                                         String usertype, boolean shouldSucceed) {
        System.out.println("\n=== Method Source Test ===");
        System.out.println("Username: " + username);
        System.out.println("Should succeed: " + shouldSucceed);
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        if (shouldSucceed) {
            DashboardPage dashboard = loginPage.loginAs(username, password, usertype);
            assertTrue(dashboard.isDashboardPageDisplayed());
            System.out.println("✓ Login successful");
        } else {
            if (!username.isEmpty()) {
                loginPage.enterUsername(username)
                        .enterPassword(password)
                        .selectUserType(usertype)
                        .acceptTerms()
                        .clickLoginExpectingError();
            } else {
                loginPage.clickLoginExpectingError();
            }
            assertTrue(loginPage.isLoginPageDisplayed());
            System.out.println("✓ Login failed as expected");
        }
    }
    
    /**
     * Method that provides test data for @MethodSource
     */
    static Stream<Arguments> provideLoginData() {
        return Stream.of(
            Arguments.of("admin", "admin123", "Customer", true),
            Arguments.of("user1", "user123", "Customer", true),
            Arguments.of("wrong", "wrong", "Customer", false),
            Arguments.of("", "", "", false)
        );
    }
    
    /**
     * Parameterized test with @CsvSource and empty values
     */
    @ParameterizedTest(name = "Validation test: username={0}")
    @CsvSource(value = {
        "''|''|fail",
        "admin|''|fail",
        "''|admin123|fail"
    }, delimiter = '|')
    @Tag("regression")
    @DisplayName("Validation errors for empty fields")
    public void testValidationErrors(String username, String password, String expected) {
        System.out.println("\n=== Validation Test ===");
        System.out.println("Username: " + (username.isEmpty() ? "[empty]" : username));
        System.out.println("Password: " + (password.isEmpty() ? "[empty]" : password));
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        if (!username.isEmpty()) loginPage.enterUsername(username);
        if (!password.isEmpty()) loginPage.enterPassword(password);
        
        loginPage.clickLoginExpectingError();
        
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Validation error displayed as expected");
    }
    
    /**
     * Parameterized test with @NullAndEmptySource
     */
    @ParameterizedTest(name = "Test with null/empty: [{0}]")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @Tag("regression")
    @DisplayName("Empty username should show error")
    public void testEmptyUsername(String username) {
        System.out.println("\n=== Empty Username Test ===");
        System.out.println("Username: [" + username + "]");
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        if (username != null) {
            loginPage.enterUsername(username);
        }
        
        loginPage.clickLoginExpectingError();
        assertTrue(loginPage.isUsernameErrorDisplayed());
        System.out.println("✓ Username error displayed");
    }
    
    /**
     * Regular non-parameterized test
     */
    @Test
    @Tag("smoke")
    @DisplayName("Regular test - Dashboard loads")
    public void testDashboardLoads() {
        System.out.println("\n=== Regular Test: Dashboard ===");
        
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("✓ Dashboard loaded");
    }
    
    /**
     * Nested class for organizing related parameterized tests
     */
    @Nested
    @DisplayName("User Type Tests")
    class UserTypeTests {
        
        @ParameterizedTest(name = "Login as {0}")
        @CsvSource({
            "admin, Customer",
            "merchant1, Merchant",
            "user1, Customer"
        })
        @Tag("smoke")
        @DisplayName("Login with different user types")
        public void testDifferentUserTypes(String username, String userType) {
            System.out.println("\n=== User Type Test: " + userType + " ===");
            
            LoginPage loginPage = new LoginPage(driver)
                .open()
                .enterUsername(username)
                .enterPassword(username + "123")
                .selectUserType(userType)
                .acceptTerms();
            
            DashboardPage dashboard = loginPage.clickLogin();
            assertTrue(dashboard.isDashboardPageDisplayed());
            System.out.println("✓ Logged in as " + userType);
        }
    }
}

/*
 * JUNIT 5 PARAMETERIZED TESTS:
 * 
 * 1. @ValueSource - Simple single parameter
 *    @ParameterizedTest
 *    @ValueSource(strings = {"value1", "value2"})
 *    void test(String param) { }
 * 
 * 2. @CsvSource - Multiple parameters
 *    @ParameterizedTest
 *    @CsvSource({"val1,val2", "val3,val4"})
 *    void test(String p1, String p2) { }
 * 
 * 3. @CsvFileSource - From CSV file
 *    @ParameterizedTest
 *    @CsvFileSource(resources = "/data.csv")
 *    void test(String p1, String p2) { }
 * 
 * 4. @MethodSource - Complex data
 *    @ParameterizedTest
 *    @MethodSource("dataProvider")
 *    void test(String p1, String p2) { }
 *    
 *    static Stream<Arguments> dataProvider() {
 *        return Stream.of(Arguments.of("v1", "v2"));
 *    }
 * 
 * 5. @EnumSource - Enum values
 *    @ParameterizedTest
 *    @EnumSource(UserType.class)
 *    void test(UserType type) { }
 * 
 * 6. Custom Display Names:
 *    @ParameterizedTest(name = "Test {index}: {0}")
 *    
 * BENEFITS:
 * - One test method, multiple executions
 * - Easy to add test data
 * - Clear test reports
 * - Type-safe parameters
 */
