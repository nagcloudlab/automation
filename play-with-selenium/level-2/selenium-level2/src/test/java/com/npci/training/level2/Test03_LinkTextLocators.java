package com.npci.training.level2;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 2 - Test 03: Link Text Locators
 * 
 * Topics Covered:
 * - By.linkText() - Exact match
 * - By.partialLinkText() - Partial match
 * - Navigation using links
 * - When to use link text vs other locators
 * 
 * Duration: 15 minutes
 */
public class Test03_LinkTextLocators {

    WebDriver driver;
    
    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @DisplayName("LinkText: Exact Match")
    public void testLinkText() throws InterruptedException {
        System.out.println("\n=== By.linkText() - Exact Match ===");
        
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/login.html");
        
        // Find link by exact text
        WebElement registerLink = driver.findElement(By.linkText("Register"));
        System.out.println("✓ Found link: 'Register here'");
        System.out.println("  href: " + registerLink.getAttribute("href"));
        System.out.println("  text: " + registerLink.getText());
        
        // Click the link
        registerLink.click();
        Thread.sleep(2000);
        
        // Verify navigation
        assertTrue(driver.getCurrentUrl().contains("register.html"));
        System.out.println("✓ Navigated to register page");
        
        Thread.sleep(1000);
    }
    
    @Test
    @DisplayName("PartialLinkText: Partial Match")
    public void testPartialLinkText() throws InterruptedException {
        System.out.println("\n=== By.partialLinkText() - Partial Match ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Find link by partial text
        WebElement forgotPassword = driver.findElement(By.partialLinkText("Forgot"));
        System.out.println("✓ Found link containing 'Forgot'");
        System.out.println("  Full text: " + forgotPassword.getText());
        
        // Using partial match is helpful when text is long
        WebElement registerPartial = driver.findElement(By.partialLinkText("Register"));
        System.out.println("✓ Found link containing 'Register'");
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Navigation using links")
    public void testNavigationUsingLinks() throws InterruptedException {
        System.out.println("\n=== Navigation Using Links ===");
        
        // Start at login page
        driver.get("http://localhost:8000/login.html");
        System.out.println("Starting URL: " + driver.getCurrentUrl());
        
        // Navigate to register
        driver.findElement(By.linkText("Register here")).click();
        Thread.sleep(1500);
        assertTrue(driver.getCurrentUrl().contains("register.html"));
        System.out.println("✓ Step 1: Navigated to register.html");
        
        // Navigate back to login
        driver.findElement(By.linkText("Login here")).click();
        Thread.sleep(1500);
        assertTrue(driver.getCurrentUrl().contains("login.html"));
        System.out.println("✓ Step 2: Back to login.html");
        
        Thread.sleep(1000);
    }
    
    @Test
    @DisplayName("Dashboard navigation links")
    public void testDashboardLinks() throws InterruptedException {
        System.out.println("\n=== Dashboard Navigation Links ===");
        
        // Login first
        driver.get("http://localhost:8000/login.html");
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin123");
        driver.findElement(By.id("userType")).click();
        driver.findElement(By.cssSelector("option[value='customer']")).click();
        driver.findElement(By.id("terms")).click();
        driver.findElement(By.id("loginBtn")).click();
        
        Thread.sleep(2000);
        
        // Handle alert
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
            Thread.sleep(1000);
        } catch (NoAlertPresentException e) {
            // No alert
        }
        
        // Should be on dashboard
        assertTrue(driver.getCurrentUrl().contains("dashboard.html"));
        System.out.println("✓ Logged in to dashboard");
        
        // Click Transactions link
        driver.findElement(By.linkText("Transactions")).click();
        Thread.sleep(1500);
        assertTrue(driver.getCurrentUrl().contains("transactions.html"));
        System.out.println("✓ Navigated to Transactions");
        
        // Click Dashboard link
        driver.findElement(By.linkText("Dashboard")).click();
        Thread.sleep(1500);
        assertTrue(driver.getCurrentUrl().contains("dashboard.html"));
        System.out.println("✓ Back to Dashboard");
        
        // Click Accounts link
        driver.findElement(By.linkText("Accounts")).click();
        Thread.sleep(1500);
        assertTrue(driver.getCurrentUrl().contains("accounts.html"));
        System.out.println("✓ Navigated to Accounts");
        
        // Click Reports link
        driver.findElement(By.linkText("Reports")).click();
        Thread.sleep(1500);
        assertTrue(driver.getCurrentUrl().contains("reports.html"));
        System.out.println("✓ Navigated to Reports");
        
        Thread.sleep(1000);
    }
    
    @Test
    @DisplayName("LinkText vs Other Locators - When to Use")
    public void testWhenToUseLinkText() throws InterruptedException {
        System.out.println("\n=== When to Use Link Text ===");
        
        driver.get("http://localhost:8000/login.html");
        
        System.out.println("\n✓ USE By.linkText() when:");
        System.out.println("  1. Element is a link (<a> tag)");
        System.out.println("  2. Link text is unique");
        System.out.println("  3. Link text is stable (won't change)");
        System.out.println("  4. You want readable test code");
        
        System.out.println("\n✗ DON'T USE By.linkText() when:");
        System.out.println("  1. Element is not a link");
        System.out.println("  2. Link text changes frequently");
        System.out.println("  3. Link text is not unique");
        System.out.println("  4. Link text contains special characters");
        
        // Example: Link text is best here
        WebElement registerLink = driver.findElement(By.linkText("Register here"));
        System.out.println("\n✓ Good: By.linkText('Register here')");
        System.out.println("  vs");
        System.out.println("  By.xpath(\"//a[contains(@href, 'register')]\")");
        System.out.println("  Link text is more readable!");
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Handle missing links gracefully")
    public void testMissingLink() throws InterruptedException {
        System.out.println("\n=== Handling Missing Links ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Try to find a link that doesn't exist
        try {
            WebElement nonExistent = driver.findElement(By.linkText("This Link Does Not Exist"));
            System.out.println("✗ Found unexpected link");
        } catch (NoSuchElementException e) {
            System.out.println("✓ Link not found - NoSuchElementException caught");
            System.out.println("  This is expected behavior");
        }
        
        // Verify existing link works
        WebElement validLink = driver.findElement(By.linkText("Register here"));
        assertNotNull(validLink);
        System.out.println("✓ Valid link found successfully");
        
        Thread.sleep(2000);
    }
}
