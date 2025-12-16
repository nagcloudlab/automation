package com.npci.training.level6;

import com.npci.training.listeners.ExtentReportListener;
import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import com.npci.training.tests.BaseTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Level 6 - Test 02: Custom Listeners & ExtentReports
 * 
 * Topics Covered:
 * - TestNG ITestListener interface
 * - Custom test listeners
 * - ExtentReports integration
 * - Beautiful HTML reports
 * - Automatic test logging
 * - Report customization
 * 
 * Duration: 20 minutes
 */
//@Listeners(ExtentReportListener.class)
public class Test02_CustomListeners extends BaseTest {
    
    @Test(priority = 1, description = "Verify login page loads successfully")
    public void testLoginPageLoad() {
        ExtentReportListener.getTest().info("Opening login page");
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        ExtentReportListener.getTest().info("Verifying login page is displayed");
        assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
        
        ExtentReportListener.getTest().pass("Login page loaded successfully");
        System.out.println("✓ Login page load verified");
    }
    
    @Test(priority = 2, description = "Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        ExtentReportListener.getTest().info("Starting login test");
        ExtentReportListener.getTest().info("Credentials: admin / admin123");
        
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        ExtentReportListener.getTest().info("Verifying dashboard is displayed");
        assertTrue(dashboard.isDashboardPageDisplayed(), "Dashboard should be displayed");
        
        String welcomeMsg = dashboard.getWelcomeMessage();
        ExtentReportListener.getTest().info("Welcome message: " + welcomeMsg);
        assertNotNull(welcomeMsg, "Welcome message should not be null");
        
        ExtentReportListener.getTest().pass("Login successful");
        System.out.println("✓ Login test passed");
    }
    
    @Test(priority = 3, description = "Verify validation errors for empty fields")
    public void testValidationErrors() {
        ExtentReportListener.getTest().info("Testing validation for empty fields");
        
        LoginPage loginPage = new LoginPage(driver)
            .open()
            .clickLoginExpectingError();
        
        ExtentReportListener.getTest().info("Checking username error");
        assertTrue(loginPage.isUsernameErrorDisplayed(), "Username error should be displayed");
        
        ExtentReportListener.getTest().info("Checking password error");
        assertTrue(loginPage.isPasswordErrorDisplayed(), "Password error should be displayed");
        
        ExtentReportListener.getTest().pass("Validation errors displayed correctly");
        System.out.println("✓ Validation test passed");
    }
    
    @Test(priority = 4, description = "Verify navigation to register page")
    public void testNavigationToRegister() {
        ExtentReportListener.getTest().info("Testing navigation to register page");
        
        var registerPage = new LoginPage(driver)
            .open()
            .clickRegister();
        
        ExtentReportListener.getTest().info("Verifying register page is displayed");
        assertTrue(registerPage.isRegisterPageDisplayed(), "Register page should be displayed");
        
        ExtentReportListener.getTest().pass("Navigation successful");
        System.out.println("✓ Navigation test passed");
    }
    
    @Test(priority = 5, description = "Verify complete login-logout flow")
    public void testLoginLogoutFlow() {
        ExtentReportListener.getTest().info("Testing complete login-logout flow");
        
        // Login
        ExtentReportListener.getTest().info("Step 1: Logging in");
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        assertTrue(dashboard.isDashboardPageDisplayed());
        ExtentReportListener.getTest().pass("Login successful");
        
        // Logout
        ExtentReportListener.getTest().info("Step 2: Logging out");
        LoginPage loginPage = dashboard.logout();
        
        assertTrue(loginPage.isLoginPageDisplayed());
        ExtentReportListener.getTest().pass("Logout successful");
        
        System.out.println("✓ Login-logout flow completed");
    }
    
    @Test(priority = 6, enabled = false, description = "This test is intentionally disabled")
    public void testDisabledTest() {
        // This test will be marked as SKIPPED in the report
        fail("This should not execute");
    }
    
    @Test(priority = 7, description = "This test intentionally fails for demo")
    public void testIntentionalFailure() {
        ExtentReportListener.getTest().info("This test will fail to demonstrate failure reporting");
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        // Intentional failure
        ExtentReportListener.getTest().warning("About to trigger intentional failure");
        assertFalse(loginPage.isLoginPageDisplayed(), "This assertion will fail!");
    }
    
    @Test(priority = 8, description = "Test with detailed logging")
    public void testWithDetailedLogging() {
        ExtentReportListener.getTest().info("Starting test with detailed logging");
        
        ExtentReportListener.getTest().info("Step 1: Opening login page");
        LoginPage loginPage = new LoginPage(driver).open();
        
        ExtentReportListener.getTest().info("Step 2: Entering username");
        loginPage.enterUsername("admin");
        
        ExtentReportListener.getTest().info("Step 3: Entering password");
        loginPage.enterPassword("admin123");
        
        ExtentReportListener.getTest().info("Step 4: Selecting user type");
        loginPage.selectUserType("Customer");
        
        ExtentReportListener.getTest().info("Step 5: Accepting terms");
        loginPage.acceptTerms();
        
        ExtentReportListener.getTest().info("Step 6: Clicking login");
        DashboardPage dashboard = loginPage.clickLogin();
        
        ExtentReportListener.getTest().info("Step 7: Verifying dashboard");
        assertTrue(dashboard.isDashboardPageDisplayed());
        
        ExtentReportListener.getTest().pass("All steps completed successfully");
        System.out.println("✓ Detailed logging test passed");
    }
}

/*
 * EXTENT REPORTS NOTES:
 * 
 * 1. Report Location:
 *    target/extent-reports/TestReport.html
 *    
 * 2. Opening Report:
 *    After tests run, open TestReport.html in browser
 *    
 * 3. Report Features:
 *    - Test execution summary
 *    - Pass/Fail/Skip counts
 *    - Execution time
 *    - Detailed logs
 *    - Screenshots (can be added)
 *    - System information
 *    - Beautiful charts and graphs
 *    
 * 4. Log Levels:
 *    - test.info("message")    - Information
 *    - test.pass("message")    - Success
 *    - test.fail("message")    - Failure
 *    - test.warning("message") - Warning
 *    - test.skip("message")    - Skipped
 *    
 * 5. Benefits:
 *    - Professional presentation
 *    - Easy to share with stakeholders
 *    - Detailed execution history
 *    - Visual analytics
 *    - Export to PDF/Email
 */
