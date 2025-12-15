package com.npci.training.level1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 1 - Test 04: Complete Login Tests
 * 
 * Topics Covered:
 * - Multiple test methods in one class
 * - Positive testing (valid login)
 * - Negative testing (validations)
 * - Assertions
 * - @BeforeEach and @AfterEach
 * - @Order for test sequence
 * 
 * Duration: 25 minutes
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test04_CompleteLoginTests {

    WebDriver driver;
    String baseUrl = "http://127.0.0.1:5500/level-0/banking-portal-final/login.html";
    
    @BeforeEach
    public void setup() {
        System.out.println("\n[SETUP] Initializing browser...");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(baseUrl);
        System.out.println("[SETUP] Navigated to: " + baseUrl);
    }
    
    @AfterEach
    public void teardown() {
        if (driver != null) {
            System.out.println("[TEARDOWN] Closing browser...");
            driver.quit();
        }
    }
    
    @Test
    @Order(1)
    @DisplayName("TC01: Valid login with correct credentials")
    public void testValidLogin() throws InterruptedException {
        System.out.println("\n========== TC01: Valid Login ==========");
        
        // Enter username
        WebElement username = driver.findElement(By.id("username"));
        username.sendKeys("admin");
        System.out.println("✓ Entered username: admin");
        
        // Enter password
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("admin123");
        System.out.println("✓ Entered password: ********");
        
        // Select user type
        Select userType = new Select(driver.findElement(By.id("userType")));
        userType.selectByVisibleText("Customer");
        System.out.println("✓ Selected user type: Customer");
        
        // Accept terms
        driver.findElement(By.id("terms")).click();
        System.out.println("✓ Accepted terms and conditions");
        
        Thread.sleep(500);
        
        // Click login
        driver.findElement(By.id("loginBtn")).click();
        System.out.println("✓ Clicked login button");
        
        // Wait for navigation
        Thread.sleep(2000);
        
        // Verify navigation to dashboard
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
        
        assertTrue(currentUrl.contains("dashboard.html"), 
                  "Should navigate to dashboard after successful login");
        
        System.out.println("✓ TEST PASSED: Successfully logged in!");
        
        Thread.sleep(1000);
    }
    
    @Test
    @Order(2)
    @DisplayName("TC02: Login with empty username")
    public void testEmptyUsername() throws InterruptedException {
        System.out.println("\n========== TC02: Empty Username ==========");
        
        // Leave username empty, fill other fields
        driver.findElement(By.id("password")).sendKeys("admin123");
        
        Select userType = new Select(driver.findElement(By.id("userType")));
        userType.selectByVisibleText("Customer");
        
        driver.findElement(By.id("terms")).click();
        
        Thread.sleep(500);
        
        // Click login
        driver.findElement(By.id("loginBtn")).click();
        System.out.println("✓ Clicked login with empty username");
        
        Thread.sleep(1000);
        
        // Verify error message appears
        WebElement errorMsg = driver.findElement(By.id("usernameError"));
        String errorText = errorMsg.getText();
        
        System.out.println("Error message: " + errorText);
        assertFalse(errorText.isEmpty(), "Error message should be displayed");
        assertTrue(errorText.toLowerCase().contains("required"), 
                  "Error should mention field is required");
        
        System.out.println("✓ TEST PASSED: Validation error displayed correctly!");
        
        Thread.sleep(1000);
    }
    
    @Test
    @Order(3)
    @DisplayName("TC03: Login with empty password")
    public void testEmptyPassword() throws InterruptedException {
        System.out.println("\n========== TC03: Empty Password ==========");
        
        // Fill username, leave password empty
        driver.findElement(By.id("username")).sendKeys("admin");
        
        Select userType = new Select(driver.findElement(By.id("userType")));
        userType.selectByVisibleText("Customer");
        
        driver.findElement(By.id("terms")).click();
        
        Thread.sleep(500);
        
        driver.findElement(By.id("loginBtn")).click();
        System.out.println("✓ Clicked login with empty password");
        
        Thread.sleep(1000);
        
        // Verify password error
        WebElement passwordError = driver.findElement(By.id("passwordError"));
        String errorText = passwordError.getText();
        
        System.out.println("Error message: " + errorText);
        assertFalse(errorText.isEmpty());
        
        System.out.println("✓ TEST PASSED: Password validation working!");
        
        Thread.sleep(1000);
    }
    
    @Test
    @Order(4)
    @DisplayName("TC04: Login without selecting user type")
    public void testNoUserType() throws InterruptedException {
        System.out.println("\n========== TC04: No User Type ==========");
        
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin123");
        driver.findElement(By.id("terms")).click();
        
        // Don't select user type - leave default
        
        Thread.sleep(500);
        
        driver.findElement(By.id("loginBtn")).click();
        System.out.println("✓ Clicked login without user type");
        
        Thread.sleep(1000);
        
        // Verify error for user type
        WebElement userTypeError = driver.findElement(By.id("userTypeError"));
        String errorText = userTypeError.getText();
        
        System.out.println("Error message: " + errorText);
        assertFalse(errorText.isEmpty());
        
        System.out.println("✓ TEST PASSED: User type validation working!");
        
        Thread.sleep(1000);
    }
    
    @Test
    @Order(5)
    @DisplayName("TC05: Login without accepting terms")
    public void testTermsNotAccepted() throws InterruptedException {
        System.out.println("\n========== TC05: Terms Not Accepted ==========");
        
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin123");
        
        Select userType = new Select(driver.findElement(By.id("userType")));
        userType.selectByVisibleText("Customer");
        
        // Don't click terms checkbox
        
        Thread.sleep(500);
        
        driver.findElement(By.id("loginBtn")).click();
        System.out.println("✓ Clicked login without accepting terms");
        
        Thread.sleep(1000);
        
        // Verify terms error
        WebElement termsError = driver.findElement(By.id("termsError"));
        String errorText = termsError.getText();
        
        System.out.println("Error message: " + errorText);
        assertFalse(errorText.isEmpty());
        
        System.out.println("✓ TEST PASSED: Terms validation working!");
        
        Thread.sleep(1000);
    }
    
    @Test
    @Order(6)
    @DisplayName("TC06: Test clear button functionality")
    public void testClearButton() throws InterruptedException {
        System.out.println("\n========== TC06: Clear Button ==========");
        
        // Fill all fields
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin123");
        
        Select userType = new Select(driver.findElement(By.id("userType")));
        userType.selectByVisibleText("Customer");
        
        driver.findElement(By.id("rememberMe")).click();
        driver.findElement(By.id("terms")).click();
        
        System.out.println("✓ Filled all fields");
        Thread.sleep(1000);
        
        // Click clear button
        driver.findElement(By.xpath("//button[contains(text(), 'Clear')]")).click();
        System.out.println("✓ Clicked clear button");
        
        Thread.sleep(1000);
        
        // Verify fields are cleared
        String usernameValue = driver.findElement(By.id("username")).getAttribute("value");
        String passwordValue = driver.findElement(By.id("password")).getAttribute("value");
        boolean termsChecked = driver.findElement(By.id("terms")).isSelected();
        
        assertEquals("", usernameValue, "Username should be empty");
        assertEquals("", passwordValue, "Password should be empty");
        assertFalse(termsChecked, "Terms should be unchecked");
        
        System.out.println("✓ TEST PASSED: Form cleared successfully!");
        
        Thread.sleep(1000);
    }
}
