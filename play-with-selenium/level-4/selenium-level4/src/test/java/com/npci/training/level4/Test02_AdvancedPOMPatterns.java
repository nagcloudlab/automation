package com.npci.training.level4;

import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import com.npci.training.utils.TestUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 4 - Test 02: Advanced POM Patterns & Screenshots
 * 
 * Topics Covered:
 * - Screenshot capture
 * - Taking screenshots on failure
 * - Data-driven testing basics
 * - Reusable test methods
 * - Test organization
 * - Utility class usage
 * 
 * Duration: 25 minutes
 */
public class Test02_AdvancedPOMPatterns {

    WebDriver driver;
    
    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        TestUtils.log("Browser started");
    }
    
    @AfterEach
    public void teardown() {
        if (driver != null) {
            TestUtils.log("Closing browser");
            driver.quit();
        }
    }
    
    @Test
    @DisplayName("Screenshot: Capture during test")
    public void testScreenshotCapture() {
        TestUtils.printTestHeader("Screenshot Capture");
        
        // Navigate and perform actions
        LoginPage loginPage = new LoginPage(driver).open();
        TestUtils.logSuccess("Opened login page");
        
        // Take screenshot
        String screenshotPath = TestUtils.captureScreenshot(driver, "login_page");
        assertNotNull(screenshotPath);
        TestUtils.logSuccess("Screenshot captured");
        
        // Perform login
        DashboardPage dashboard = loginPage.loginAs("admin", "admin123", "Customer");
        TestUtils.logSuccess("Logged in successfully");
        
        // Take another screenshot
        screenshotPath = TestUtils.captureScreenshot(driver, "dashboard_page");
        assertNotNull(screenshotPath);
        TestUtils.logSuccess("Dashboard screenshot captured");
        
        assertTrue(dashboard.isDashboardPageDisplayed());
        
        System.out.println("\n💡 Screenshots saved in target/screenshots/");
    }
    
    @Test
    @DisplayName("Screenshot: Capture on test failure")
    public void testScreenshotOnFailure() {
        TestUtils.printTestHeader("Screenshot on Failure");
        
        try {
            LoginPage loginPage = new LoginPage(driver).open();
            
            // Intentionally cause a failure
            DashboardPage dashboard = loginPage.loginAs("wrong", "credentials", "Customer");
            
            // This will fail - dashboard won't load
            assertTrue(dashboard.isDashboardPageDisplayed());
            
        } catch (AssertionError e) {
            // Capture screenshot on failure
            TestUtils.captureScreenshotOnFailure(driver, "login_failure");
            TestUtils.logError("Test failed - screenshot captured");
            
            // Re-throw to mark test as failed
            throw e;
        }
    }
    
    @Test
    @DisplayName("Data-driven: Multiple login scenarios")
    public void testMultipleLoginScenarios() {
        TestUtils.printTestHeader("Multiple Login Scenarios");
        
        // Test data
        String[][] testData = {
            {"admin", "admin123", "Customer", "true"},   // Valid
            {"", "", "", "false"},                        // Empty fields
            {"admin", "", "Customer", "false"},          // Missing password
            {"", "admin123", "Customer", "false"}        // Missing username
        };
        
        for (int i = 0; i < testData.length; i++) {
            String[] data = testData[i];
            String username = data[0];
            String password = data[1];
            String userType = data[2];
            boolean shouldSucceed = Boolean.parseBoolean(data[3]);
            
            System.out.println("\n--- Scenario " + (i + 1) + " ---");
            System.out.println("Username: " + (username.isEmpty() ? "[empty]" : username));
            System.out.println("Password: " + (password.isEmpty() ? "[empty]" : password));
            System.out.println("Expected: " + (shouldSucceed ? "Success" : "Failure"));
            
            LoginPage loginPage = new LoginPage(driver).open();
            
            if (shouldSucceed) {
                // Should login successfully
                DashboardPage dashboard = loginPage.loginAs(username, password, userType);
                assertTrue(dashboard.isDashboardPageDisplayed());
                TestUtils.logSuccess("Login successful as expected");
                dashboard.logout(); // Logout for next iteration
            } else {
                // Should show errors
                if (!username.isEmpty()) loginPage.enterUsername(username);
                if (!password.isEmpty()) loginPage.enterPassword(password);
                if (!userType.isEmpty()) loginPage.selectUserType(userType);
                
                loginPage.clickLoginExpectingError();
                
                // Verify still on login page
                assertTrue(loginPage.isLoginPageDisplayed());
                TestUtils.logSuccess("Login failed as expected");
            }
        }
        
        System.out.println("\n✓ All scenarios tested!");
        System.out.println("💡 Data-driven testing allows testing multiple scenarios easily!");
    }
    
    @Test
    @DisplayName("Reusable methods: Login helper")
    public void testReusableLoginHelper() {
        TestUtils.printTestHeader("Reusable Login Helper");
        
        // Use helper method
        DashboardPage dashboard = performLogin("admin", "admin123", "Customer");
        
        assertTrue(dashboard.isDashboardPageDisplayed());
        TestUtils.logSuccess("Login using helper method");
        
        String welcome = dashboard.getWelcomeMessage();
        System.out.println("Welcome message: " + welcome);
        
        System.out.println("\n💡 Helper methods make tests DRY (Don't Repeat Yourself)!");
    }
    
    @Test
    @DisplayName("Complete workflow: Login → Navigate → Logout")
    public void testCompleteWorkflow() {
        TestUtils.printTestHeader("Complete Workflow");
        
        // Step 1: Login
        TestUtils.log("Step 1: Performing login");
        DashboardPage dashboard = performLogin("admin", "admin123", "Customer");
        TestUtils.captureScreenshot(driver, "01_dashboard");
        
        // Step 2: Navigate to transactions
        TestUtils.log("Step 2: Viewing transactions");
        var transactionsPage = dashboard.goToTransactions();
        assertTrue(transactionsPage.isTransactionsPageDisplayed());
        TestUtils.captureScreenshot(driver, "02_transactions");
        
        // Step 3: Check transaction count
        int count = transactionsPage.getTransactionCount();
        TestUtils.log("Transaction count: " + count);
        
        // Step 4: Back to dashboard
        TestUtils.log("Step 3: Returning to dashboard");
        dashboard = transactionsPage.goToDashboard();
        TestUtils.captureScreenshot(driver, "03_dashboard_return");
        
        // Step 5: Logout
        TestUtils.log("Step 4: Logging out");
        LoginPage loginPage = dashboard.logout();
        assertTrue(loginPage.isLoginPageDisplayed());
        TestUtils.captureScreenshot(driver, "04_logout");
        
        TestUtils.logSuccess("Complete workflow executed!");
        
        System.out.println("\n✓ Workflow documented with screenshots!");
    }
    
    @Test
    @DisplayName("Page verification methods")
    public void testPageVerifications() {
        TestUtils.printTestHeader("Page Verification Methods");
        
        // Login
        DashboardPage dashboard = performLogin("admin", "admin123", "Customer");
        
        // Multiple verifications
        System.out.println("\nVerifying Dashboard:");
        assertTrue(dashboard.isDashboardPageDisplayed(), "Dashboard page should be displayed");
        TestUtils.logSuccess("Page displayed check");
        
        assertTrue(dashboard.isLogoutLinkVisible(), "Logout link should be visible");
        TestUtils.logSuccess("Logout link check");
        
        assertTrue(dashboard.isTransactionTableDisplayed(), "Transaction table should be displayed");
        TestUtils.logSuccess("Transaction table check");
        
        assertNotNull(dashboard.getWelcomeMessage(), "Welcome message should exist");
        TestUtils.logSuccess("Welcome message check");
        
        assertTrue(dashboard.getTransactionCount() > 0, "Should have transactions");
        TestUtils.logSuccess("Transaction count check");
        
        System.out.println("\n✓ All verifications passed!");
        System.out.println("💡 Page Objects provide dedicated verification methods!");
    }
    
    @Test
    @DisplayName("Error handling in Page Objects")
    public void testErrorHandling() {
        TestUtils.printTestHeader("Error Handling");
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        // Test all validation errors
        loginPage.clickLoginExpectingError();
        
        // Username error
        if (loginPage.isUsernameErrorDisplayed()) {
            String error = loginPage.getUsernameError();
            TestUtils.log("Username error: " + error);
            assertFalse(error.isEmpty());
        }
        
        // Password error
        if (loginPage.isPasswordErrorDisplayed()) {
            String error = loginPage.getPasswordError();
            TestUtils.log("Password error: " + error);
            assertFalse(error.isEmpty());
        }
        
        // Capture error state
        TestUtils.captureScreenshot(driver, "validation_errors");
        
        TestUtils.logSuccess("Error handling verified!");
        
        System.out.println("\n💡 Page Objects handle error checking gracefully!");
    }
    
    @Test
    @DisplayName("Method chaining examples")
    public void testMethodChainingExamples() {
        TestUtils.printTestHeader("Method Chaining Examples");
        
        // Example 1: Simple chain
        System.out.println("\nExample 1: Simple chain");
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .enterUsername("admin")
            .enterPassword("admin123")
            .selectUserType("Customer")
            .acceptTerms()
            .clickLogin();
        
        TestUtils.logSuccess("Simple chain executed");
        
        // Example 2: Complete method
        dashboard.logout();
        dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        TestUtils.logSuccess("Complete method executed");
        
        // Example 3: Navigation chain
        dashboard.goToTransactions()
                 .goToDashboard()
                 .goToAccounts()
                 .goToDashboard()
                 .logout();
        
        TestUtils.logSuccess("Navigation chain executed");
        
        System.out.println("\n✓ Method chaining makes code elegant!");
    }
    
    // Helper method for reusable login
    private DashboardPage performLogin(String username, String password, String userType) {
        return new LoginPage(driver)
            .open()
            .loginAs(username, password, userType);
    }
}
