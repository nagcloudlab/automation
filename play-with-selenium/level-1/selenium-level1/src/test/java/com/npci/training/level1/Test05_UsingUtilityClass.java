package com.npci.training.level1;

import com.npci.training.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 1 - Test 05: Using Utility Class
 * 
 * Topics Covered:
 * - Code reusability with utility methods
 * - Cleaner test code
 * - Better organization
 * - Cross-browser testing capability
 * 
 * Duration: 15 minutes
 */
public class Test05_UsingUtilityClass {

    WebDriver driver;
    String baseUrl = "http://127.0.0.1:5500/level-0/banking-portal-final/login.html";
    
    @BeforeEach
    public void setup() {
        TestUtils.log("Initializing Chrome browser");
        driver = TestUtils.initializeDriver("chrome");
        driver.get(baseUrl);
        TestUtils.logSuccess("Browser initialized and navigated to login page");
    }
    
    @AfterEach
    public void teardown() {
        if (driver != null) {
            TestUtils.log("Closing browser");
            driver.quit();
        }
    }
    
    @Test
    @DisplayName("Login test using utility methods")
    public void testLoginWithUtils() {
        TestUtils.printTestHeader("Login Test with Utility Methods");
        
        // Enter username
        driver.findElement(By.id("username")).sendKeys("admin");
        TestUtils.logSuccess("Username entered");
        
        // Enter password
        driver.findElement(By.id("password")).sendKeys("admin123");
        TestUtils.logSuccess("Password entered");
        
        // Select user type
        Select userType = new Select(driver.findElement(By.id("userType")));
        userType.selectByVisibleText("Customer");
        TestUtils.logSuccess("User type selected: Customer");
        
        // Accept terms
        driver.findElement(By.id("terms")).click();
        TestUtils.logSuccess("Terms accepted");
        
        TestUtils.sleep(1);
        
        // Click login
        driver.findElement(By.id("loginBtn")).click();
        TestUtils.logSuccess("Login button clicked");
        
        TestUtils.sleep(2);
        
        // Verify
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("dashboard.html")) {
            TestUtils.logSuccess("Login successful! Dashboard loaded");
            TestUtils.log("Current URL: " + currentUrl);
        } else {
            TestUtils.logError("Login failed! Still on login page");
        }
        
        assertTrue(currentUrl.contains("dashboard.html"));
    }
    
    @Test
    @DisplayName("Validation test with clean logging")
    public void testValidationWithUtils() {
        TestUtils.printTestHeader("Validation Test");
        
        // Try to login with empty fields
        driver.findElement(By.id("loginBtn")).click();
        TestUtils.log("Clicked login with empty fields");
        
        TestUtils.sleep(1);
        
        // Check username error
        String usernameError = driver.findElement(By.id("usernameError")).getText();
        if (!usernameError.isEmpty()) {
            TestUtils.logSuccess("Username validation working");
            TestUtils.log("Error message: " + usernameError);
        } else {
            TestUtils.logError("Username validation not working");
        }
        
        assertFalse(usernameError.isEmpty());
        
        // Check password error
        String passwordError = driver.findElement(By.id("passwordError")).getText();
        if (!passwordError.isEmpty()) {
            TestUtils.logSuccess("Password validation working");
            TestUtils.log("Error message: " + passwordError);
        } else {
            TestUtils.logError("Password validation not working");
        }
        
        assertFalse(passwordError.isEmpty());
        
        TestUtils.sleep(1);
    }
}
