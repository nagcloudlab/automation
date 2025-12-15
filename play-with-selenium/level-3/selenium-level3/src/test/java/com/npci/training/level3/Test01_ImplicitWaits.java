package com.npci.training.level3;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 3 - Test 01: Implicit Waits
 * 
 * Topics Covered:
 * - What are implicit waits
 * - Setting implicit wait
 * - How implicit waits work
 * - Implicit wait scope
 * - When to use implicit waits
 * - Implicit vs Thread.sleep()
 * 
 * Duration: 15 minutes
 */
public class Test01_ImplicitWaits {

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
    @DisplayName("Without any wait - May fail")
    public void testWithoutWait() {
        System.out.println("\n=== Test WITHOUT Wait ===");
        
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/login.html");
        
        // Try to find element immediately
        // This usually works for static pages but may fail for dynamic content
        try {
            WebElement username = driver.findElement(By.id("username"));
            System.out.println("✓ Element found immediately");
            username.sendKeys("admin");
        } catch (NoSuchElementException e) {
            System.out.println("✗ Element not found - page not loaded yet");
        }
    }
    
    @Test
    @DisplayName("With Thread.sleep - Not recommended")
    public void testWithThreadSleep() throws InterruptedException {
        System.out.println("\n=== Test WITH Thread.sleep() ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Hard wait - always waits full time
        System.out.println("Waiting 3 seconds...");
        Thread.sleep(3000);
        
        WebElement username = driver.findElement(By.id("username"));
        System.out.println("✓ Element found after sleep");
        username.sendKeys("admin");
        
        System.out.println("\n❌ Problems with Thread.sleep():");
        System.out.println("  1. Always waits full time (3 seconds)");
        System.out.println("  2. Wastes time if element appears sooner");
        System.out.println("  3. May still fail if element takes longer");
        System.out.println("  4. Slows down test execution");
    }
    
    @Test
    @DisplayName("With Implicit Wait - Recommended for basic cases")
    public void testWithImplicitWait() {
        System.out.println("\n=== Test WITH Implicit Wait ===");
        
        // Set implicit wait - applies to all findElement() calls
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        System.out.println("✓ Implicit wait set to 10 seconds");
        
        driver.get("http://localhost:8000/login.html");
        
        // Will wait up to 10 seconds for element
        long startTime = System.currentTimeMillis();
        WebElement username = driver.findElement(By.id("username"));
        long endTime = System.currentTimeMillis();
        
        System.out.println("✓ Element found");
        System.out.println("  Time taken: " + (endTime - startTime) + "ms");
        
        username.sendKeys("admin");
        
        System.out.println("\n✅ Benefits of Implicit Wait:");
        System.out.println("  1. Waits only as long as needed");
        System.out.println("  2. Applies to all elements");
        System.out.println("  3. Set once, works everywhere");
        System.out.println("  4. Faster than Thread.sleep()");
    }
    
    @Test
    @DisplayName("Implicit wait scope - Applies to all elements")
    public void testImplicitWaitScope() {
        System.out.println("\n=== Implicit Wait Scope ===");
        
        // Set implicit wait once
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        System.out.println("✓ Implicit wait set: 10 seconds");
        
        driver.get("http://localhost:8000/login.html");
        
        // All these findElement calls will use implicit wait
        System.out.println("\nFinding multiple elements:");
        
        WebElement username = driver.findElement(By.id("username"));
        System.out.println("✓ Username found (used implicit wait)");
        
        WebElement password = driver.findElement(By.id("password"));
        System.out.println("✓ Password found (used implicit wait)");
        
        WebElement loginBtn = driver.findElement(By.id("loginBtn"));
        System.out.println("✓ Login button found (used implicit wait)");
        
        System.out.println("\n✓ All elements used the same implicit wait setting");
    }
    
    @Test
    @DisplayName("Implicit wait with missing element")
    public void testImplicitWaitWithMissingElement() {
        System.out.println("\n=== Implicit Wait with Missing Element ===");
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        System.out.println("✓ Implicit wait set: 5 seconds");
        
        driver.get("http://localhost:8000/login.html");
        
        // Try to find element that doesn't exist
        long startTime = System.currentTimeMillis();
        try {
            driver.findElement(By.id("nonExistentElement"));
        } catch (NoSuchElementException e) {
            long endTime = System.currentTimeMillis();
            long timeTaken = (endTime - startTime) / 1000;
            
            System.out.println("✓ Element not found (expected)");
            System.out.println("  Waited approximately: " + timeTaken + " seconds");
            System.out.println("  This is close to our 5-second implicit wait");
        }
    }
    
    @Test
    @DisplayName("Changing implicit wait during test")
    public void testChangingImplicitWait() {
        System.out.println("\n=== Changing Implicit Wait ===");
        
        // Start with 5 seconds
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        System.out.println("✓ Initial implicit wait: 5 seconds");
        
        driver.get("http://localhost:8000/login.html");
        
        WebElement username = driver.findElement(By.id("username"));
        System.out.println("✓ Username found with 5-second wait");
        
        // Change to 10 seconds
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        System.out.println("\n✓ Changed implicit wait to: 10 seconds");
        
        WebElement password = driver.findElement(By.id("password"));
        System.out.println("✓ Password found with 10-second wait");
        
        System.out.println("\n⚠️ Note: Changing implicit wait is allowed but not recommended");
        System.out.println("   Best practice: Set once at the beginning");
    }
    
    @Test
    @DisplayName("Complete login with implicit wait")
    public void testCompleteLoginWithImplicitWait() throws InterruptedException {
        System.out.println("\n=== Complete Login with Implicit Wait ===");
        
        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        driver.get("http://localhost:8000/login.html");
        
        // All elements will use implicit wait automatically
        driver.findElement(By.id("username")).sendKeys("admin");
        System.out.println("✓ Username entered");
        
        driver.findElement(By.id("password")).sendKeys("admin123");
        System.out.println("✓ Password entered");
        
        driver.findElement(By.id("userType")).click();
        driver.findElement(By.cssSelector("option[value='customer']")).click();
        System.out.println("✓ User type selected");
        
        driver.findElement(By.id("terms")).click();
        System.out.println("✓ Terms accepted");
        
        driver.findElement(By.id("loginBtn")).click();
        System.out.println("✓ Login clicked");
        
        Thread.sleep(2000);
        
        // Handle alert
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
            Thread.sleep(1000);
        } catch (NoAlertPresentException e) {
            // No alert
        }
        
        // Verify navigation
        assertTrue(driver.getCurrentUrl().contains("dashboard.html"));
        System.out.println("✓ Login successful!");
    }
    
    @Test
    @DisplayName("Best practices for implicit wait")
    public void testBestPractices() {
        System.out.println("\n=== Implicit Wait Best Practices ===");
        
        System.out.println("\n✅ DO:");
        System.out.println("  1. Set implicit wait once in @BeforeEach or setup()");
        System.out.println("  2. Use reasonable timeout (10-15 seconds)");
        System.out.println("  3. Use for simple, static pages");
        System.out.println("  4. Keep the same value throughout test");
        
        System.out.println("\n❌ DON'T:");
        System.out.println("  1. Change implicit wait during test");
        System.out.println("  2. Mix with explicit waits (can cause issues)");
        System.out.println("  3. Use very high values (30+ seconds)");
        System.out.println("  4. Rely on it for dynamic content");
        
        System.out.println("\n📋 Recommended Usage:");
        System.out.println("  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));");
        
        System.out.println("\n⚠️ When to Use Explicit Wait Instead:");
        System.out.println("  - AJAX calls");
        System.out.println("  - Dynamic content loading");
        System.out.println("  - Waiting for specific conditions");
        System.out.println("  - Complex synchronization needs");
    }
}
