package com.npci.training.level3;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 3 - Test 02: Explicit Waits and WebDriverWait
 * 
 * Topics Covered:
 * - What are explicit waits
 * - WebDriverWait class
 * - until() method
 * - Basic Expected Conditions
 * - Explicit vs Implicit waits
 * - When to use explicit waits
 * 
 * Duration: 25 minutes
 */
public class Test02_ExplicitWaits {

    WebDriver driver;
    WebDriverWait wait;
    
    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
        // Create WebDriverWait object
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @DisplayName("Basic Explicit Wait - Wait for element to be clickable")
    public void testBasicExplicitWait() {
        System.out.println("\n=== Basic Explicit Wait ===");
        
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/login.html");
        
        // Wait for login button to be clickable
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("loginBtn")));
        
        System.out.println("✓ Login button is clickable");
        System.out.println("  Element found: " + loginBtn.getText());
        
        assertNotNull(loginBtn);
    }
    
    @Test
    @DisplayName("Wait for element to be visible")
    public void testWaitForVisibility() {
        System.out.println("\n=== Wait for Element Visibility ===");
        
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/login.html");
        
        // Wait for username field to be visible
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        
        System.out.println("✓ Username field is visible");
        System.out.println("  isDisplayed: " + username.isDisplayed());
        
        assertTrue(username.isDisplayed());
    }
    
    @Test
    @DisplayName("Wait for element to be present (in DOM)")
    public void testWaitForPresence() {
        System.out.println("\n=== Wait for Element Presence ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for element to be present in DOM (may not be visible)
        WebElement password = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        
        System.out.println("✓ Password field is present in DOM");
        System.out.println("  Tag: " + password.getTagName());
        System.out.println("  Type: " + password.getAttribute("type"));
        
        assertNotNull(password);
    }
    
    @Test
    @DisplayName("Wait for alert to be present")
    public void testWaitForAlert() throws InterruptedException {
        System.out.println("\n=== Wait for Alert ===");
        
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/login.html");
        
        // Perform login
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin123");
        driver.findElement(By.id("userType")).click();
        driver.findElement(By.cssSelector("option[value='customer']")).click();
        driver.findElement(By.id("terms")).click();
        driver.findElement(By.id("loginBtn")).click();
        
        System.out.println("✓ Login submitted");
        
        // Wait for alert to appear (instead of Thread.sleep)
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        
        System.out.println("✓ Alert is present");
        System.out.println("  Alert text: " + alert.getText());
        
        alert.accept();
        Thread.sleep(1000);
        
        assertTrue(driver.getCurrentUrl().contains("dashboard.html"));
        System.out.println("✓ Navigated to dashboard");
    }
    
    @Test
    @DisplayName("Wait for URL to contain specific text")
    public void testWaitForUrl() throws InterruptedException {
        System.out.println("\n=== Wait for URL Change ===");
        
        driver.get("http://localhost:8000/login.html");
        System.out.println("Current URL: " + driver.getCurrentUrl());
        
        // Perform login
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin123");
        driver.findElement(By.id("userType")).click();
        driver.findElement(By.cssSelector("option[value='customer']")).click();
        driver.findElement(By.id("terms")).click();
        driver.findElement(By.id("loginBtn")).click();
        
        // Handle alert
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
        
        // Wait for URL to change
        boolean urlChanged = wait.until(ExpectedConditions.urlContains("dashboard.html"));
        
        System.out.println("✓ URL changed");
        System.out.println("  New URL: " + driver.getCurrentUrl());
        
        assertTrue(urlChanged);
    }
    
    @Test
    @DisplayName("Wait for title to contain text")
    public void testWaitForTitle() {
        System.out.println("\n=== Wait for Page Title ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for title to contain "Login"
        boolean titleCorrect = wait.until(ExpectedConditions.titleContains("Login"));
        
        System.out.println("✓ Title contains 'Login'");
        System.out.println("  Full title: " + driver.getTitle());
        
        assertTrue(titleCorrect);
    }
    
    @Test
    @DisplayName("Wait for element text to be specific value")
    public void testWaitForText() {
        System.out.println("\n=== Wait for Element Text ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for heading to contain specific text
        boolean textPresent = wait.until(
            ExpectedConditions.textToBePresentInElementLocated(By.tagName("h1"), "Banking Portal")
        );
        
        System.out.println("✓ Heading contains 'Banking Portal'");
        
        WebElement heading = driver.findElement(By.tagName("h1"));
        System.out.println("  Full text: " + heading.getText());
        
        assertTrue(textPresent);
    }
    
    @Test
    @DisplayName("Wait with custom timeout")
    public void testCustomTimeout() {
        System.out.println("\n=== Custom Timeout for Specific Element ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Create wait with different timeout
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        
        System.out.println("✓ Created short wait (5s) and long wait (20s)");
        
        // Use short wait for fast elements
        WebElement username = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        System.out.println("✓ Username found with short wait");
        
        // Use long wait for slow elements
        WebElement loginBtn = longWait.until(ExpectedConditions.elementToBeClickable(By.id("loginBtn")));
        System.out.println("✓ Login button found with long wait");
    }
    
    @Test
    @DisplayName("Chaining waits - Multiple conditions")
    public void testChainingWaits() {
        System.out.println("\n=== Chaining Multiple Waits ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait and interact in sequence
        System.out.println("\nStep 1: Wait for username and enter data");
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        username.sendKeys("admin");
        System.out.println("✓ Username entered");
        
        System.out.println("\nStep 2: Wait for password and enter data");
        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        password.sendKeys("admin123");
        System.out.println("✓ Password entered");
        
        System.out.println("\nStep 3: Wait for button to be clickable and click");
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("loginBtn")));
        // Don't click yet, just verify it's clickable
        System.out.println("✓ Button is clickable");
    }
    
    @Test
    @DisplayName("Explicit wait vs Implicit wait comparison")
    public void testExplicitVsImplicit() {
        System.out.println("\n=== Explicit vs Implicit Wait ===");
        
        System.out.println("\n📊 Comparison:");
        System.out.println("\nIMPLICIT WAIT:");
        System.out.println("  ✓ Set once, applies globally");
        System.out.println("  ✓ Simple to use");
        System.out.println("  ✓ Good for static pages");
        System.out.println("  ✗ Less flexible");
        System.out.println("  ✗ Cannot wait for specific conditions");
        System.out.println("  ✗ Can slow down tests");
        
        System.out.println("\nEXPLICIT WAIT:");
        System.out.println("  ✓ Wait for specific conditions");
        System.out.println("  ✓ Very flexible");
        System.out.println("  ✓ Can use different timeouts");
        System.out.println("  ✓ Better for dynamic content");
        System.out.println("  ✗ More code required");
        System.out.println("  ✗ Need to add for each element");
        
        System.out.println("\n💡 Best Practice:");
        System.out.println("  Use EXPLICIT waits for modern web apps");
        System.out.println("  Avoid mixing implicit and explicit waits");
    }
    
    @Test
    @DisplayName("Handle timeout exception")
    public void testTimeoutException() {
        System.out.println("\n=== Handling Timeout Exception ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Try to wait for element that doesn't exist
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nonExistentElement")));
            fail("Should have thrown TimeoutException");
        } catch (TimeoutException e) {
            System.out.println("✓ TimeoutException caught (expected)");
            System.out.println("  Message: Element not found within 3 seconds");
        }
        
        System.out.println("\n💡 Best Practice:");
        System.out.println("  Always handle TimeoutException for explicit waits");
        System.out.println("  Use try-catch when element might not appear");
    }
    
    @Test
    @DisplayName("Complete login with explicit waits only")
    public void testCompleteLoginWithExplicitWaits() throws InterruptedException {
        System.out.println("\n=== Complete Login with Explicit Waits ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Use explicit waits for everything
        WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        username.sendKeys("admin");
        System.out.println("✓ Username entered");
        
        WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        password.sendKeys("admin123");
        System.out.println("✓ Password entered");
        
        WebElement userType = wait.until(ExpectedConditions.elementToBeClickable(By.id("userType")));
        userType.click();
        
        WebElement customerOption = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("option[value='customer']")));
        customerOption.click();
        System.out.println("✓ User type selected");
        
        WebElement terms = wait.until(ExpectedConditions.elementToBeClickable(By.id("terms")));
        terms.click();
        System.out.println("✓ Terms accepted");
        
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("loginBtn")));
        loginBtn.click();
        System.out.println("✓ Login clicked");
        
        // Wait for alert
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        System.out.println("✓ Alert appeared: " + alert.getText());
        alert.accept();
        
        // Wait for navigation
        wait.until(ExpectedConditions.urlContains("dashboard.html"));
        System.out.println("✓ Navigated to dashboard");
        
        assertTrue(driver.getCurrentUrl().contains("dashboard.html"));
        System.out.println("\n✅ Login successful using only explicit waits!");
    }
}
