package com.npci.training.level6;

import com.npci.training.extensions.ExtentReportExtension;
import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 6 (JUnit 5) - Test 04: ExtentReports Integration
 * 
 * Topics Covered:
 * - JUnit 5 Extensions for reporting
 * - ExtentReports HTML reports
 * - Test logging and documentation
 * - Professional test reports
 * 
 * Duration: 20 minutes
 */
@ExtendWith(ExtentReportExtension.class)
@DisplayName("ExtentReports Integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test04_ExtentReportsIntegration extends BaseTest {
    
    @Test
    @Order(1)
    @Tag("smoke")
    @DisplayName("Login page should load successfully")
    public void testLoginPageLoad() {
        ExtentReportExtension.info("Opening login page");
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        ExtentReportExtension.info("Verifying login page is displayed");
        assertTrue(loginPage.isLoginPageDisplayed());
        
        ExtentReportExtension.pass("Login page loaded successfully");
        System.out.println("✓ Login page load verified");
    }
    
    @Test
    @Order(2)
    @Tag("smoke")
    @Tag("critical")
    @DisplayName("Successful login with valid credentials")
    public void testSuccessfulLogin() {
        ExtentReportExtension.info("Starting login test");
        ExtentReportExtension.info("Credentials: admin / admin123");
        
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        ExtentReportExtension.info("Verifying dashboard is displayed");
        assertTrue(dashboard.isDashboardPageDisplayed());
        
        String welcomeMsg = dashboard.getWelcomeMessage();
        ExtentReportExtension.info("Welcome message: " + welcomeMsg);
        assertNotNull(welcomeMsg);
        
        ExtentReportExtension.pass("Login successful");
        System.out.println("✓ Login test passed");
    }
    
    @Test
    @Order(3)
    @Tag("regression")
    @DisplayName("Validation errors for empty fields")
    public void testValidationErrors() {
        ExtentReportExtension.info("Testing validation for empty fields");
        
        LoginPage loginPage = new LoginPage(driver)
            .open()
            .clickLoginExpectingError();
        
        ExtentReportExtension.info("Checking username error");
        assertTrue(loginPage.isUsernameErrorDisplayed());
        
        ExtentReportExtension.info("Checking password error");
        assertTrue(loginPage.isPasswordErrorDisplayed());
        
        ExtentReportExtension.pass("Validation errors displayed correctly");
        System.out.println("✓ Validation test passed");
    }
    
    @ParameterizedTest(name = "Login test: {0}")
    @CsvSource({
        "admin, admin123, Customer, success",
        "user1, user123, Customer, success",
        "wrong, wrong, Customer, fail"
    })
    @Order(4)
    @Tag("smoke")
    @DisplayName("Parameterized login tests with reporting")
    public void testParameterizedLogin(String username, String password, 
                                      String usertype, String expected) {
        ExtentReportExtension.info("Testing login: " + username);
        ExtentReportExtension.info("Expected result: " + expected);
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        if ("success".equals(expected)) {
            DashboardPage dashboard = loginPage.loginAs(username, password, usertype);
            assertTrue(dashboard.isDashboardPageDisplayed());
            ExtentReportExtension.pass("Login successful for: " + username);
        } else {
            loginPage.enterUsername(username)
                    .enterPassword(password)
                    .selectUserType(usertype)
                    .acceptTerms()
                    .clickLoginExpectingError();
            assertTrue(loginPage.isLoginPageDisplayed());
            ExtentReportExtension.pass("Login failed as expected for: " + username);
        }
        
        System.out.println("✓ Test completed for: " + username);
    }
    
    @Test
    @Order(5)
    @Tag("detailed")
    @DisplayName("Test with detailed step-by-step logging")
    public void testDetailedLogging() {
        ExtentReportExtension.info("Starting detailed test");
        
        ExtentReportExtension.info("Step 1: Opening login page");
        LoginPage loginPage = new LoginPage(driver).open();
        
        ExtentReportExtension.info("Step 2: Entering username");
        loginPage.enterUsername("admin");
        
        ExtentReportExtension.info("Step 3: Entering password");
        loginPage.enterPassword("admin123");
        
        ExtentReportExtension.info("Step 4: Selecting user type");
        loginPage.selectUserType("Customer");
        
        ExtentReportExtension.info("Step 5: Accepting terms");
        loginPage.acceptTerms();
        
        ExtentReportExtension.info("Step 6: Clicking login");
        DashboardPage dashboard = loginPage.clickLogin();
        
        ExtentReportExtension.info("Step 7: Verifying dashboard");
        assertTrue(dashboard.isDashboardPageDisplayed());
        
        ExtentReportExtension.pass("All steps completed successfully");
        System.out.println("✓ Detailed test passed");
    }
    
    @Nested
    @DisplayName("Navigation Tests with Reporting")
    @ExtendWith(ExtentReportExtension.class)
    class NavigationTests {
        
        @Test
        @Tag("regression")
        @DisplayName("Navigate to Transactions page")
        public void testNavigateToTransactions() {
            ExtentReportExtension.info("Testing navigation to transactions");
            
            DashboardPage dashboard = new LoginPage(driver)
                .open()
                .loginAs("admin", "admin123", "Customer");
            
            ExtentReportExtension.info("Navigating to transactions page");
            var transactions = dashboard.goToTransactions();
            
            assertTrue(transactions.isTransactionsPageDisplayed());
            ExtentReportExtension.pass("Navigation successful");
            System.out.println("✓ Navigation test passed");
        }
    }
}

/*
 * EXTENTREPORTS WITH JUNIT 5 NOTES:
 * 
 * 1. Enable ExtentReports:
 *    @ExtendWith(ExtentReportExtension.class)
 *    
 * 2. Logging Methods:
 *    ExtentReportExtension.info("Information message");
 *    ExtentReportExtension.pass("Success message");
 *    ExtentReportExtension.fail("Failure message");
 *    ExtentReportExtension.warning("Warning message");
 *    
 * 3. View Reports:
 *    After test execution:
 *    - Location: target/extent-reports/TestReport.html
 *    - Open in browser
 *    - Professional dashboard with charts
 *    - Timeline view
 *    - Detailed logs
 *    
 * 4. Benefits:
 *    - Beautiful HTML reports
 *    - Automatic test lifecycle tracking
 *    - Pass/Fail/Skip statistics
 *    - Stakeholder-ready presentation
 *    - Historical tracking
 *    
 * Run Commands:
 * mvn test -Dtest=Test04_ExtentReportsIntegration
 * open target/extent-reports/TestReport.html
 */
