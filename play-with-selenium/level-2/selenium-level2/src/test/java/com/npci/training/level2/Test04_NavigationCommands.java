package com.npci.training.level2;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 2 - Test 04: Navigation & Browser Commands
 * 
 * Topics Covered:
 * - driver.navigate().to()
 * - driver.navigate().back()
 * - driver.navigate().forward()
 * - driver.navigate().refresh()
 * - driver.get() vs navigate().to()
 * - Current URL and Title
 * - Page source
 * 
 * Duration: 20 minutes
 */
public class Test04_NavigationCommands {

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
    @DisplayName("Navigate: to(), back(), forward()")
    public void testNavigateCommands() throws InterruptedException {
        System.out.println("\n=== Navigate Commands ===");
        
        // Navigate to login page
        driver.navigate().to("http://127.0.0.1:5500/level-0/banking-portal-final/login.html");
        System.out.println("✓ navigate().to(login.html)");
        System.out.println("  Current URL: " + driver.getCurrentUrl());
        Thread.sleep(1500);
        
        // Navigate to register page
        driver.navigate().to("http://127.0.0.1:5500/level-0/banking-portal-final/register.html");
        System.out.println("\n✓ navigate().to(register.html)");
        System.out.println("  Current URL: " + driver.getCurrentUrl());
        Thread.sleep(1500);
        
        // Go back
        driver.navigate().back();
        System.out.println("\n✓ navigate().back()");
        System.out.println("  Current URL: " + driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().contains("login.html"));
        Thread.sleep(1500);
        
        // Go forward
        driver.navigate().forward();
        System.out.println("\n✓ navigate().forward()");
        System.out.println("  Current URL: " + driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().contains("register.html"));
        Thread.sleep(1500);
    }
    
    @Test
    @DisplayName("Navigate: refresh()")
    public void testRefresh() throws InterruptedException {
        System.out.println("\n=== Refresh Page ===");
        
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/register.html");
        
        // Fill some data
        driver.findElement(By.id("fullName")).sendKeys("Test User");
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        System.out.println("✓ Filled form fields");
        Thread.sleep(1000);
        
        // Refresh page
        driver.navigate().refresh();
        System.out.println("✓ Page refreshed");
        Thread.sleep(1500);
        
        // Verify fields are cleared
        String fullName = driver.findElement(By.id("fullName")).getAttribute("value");
        assertEquals("", fullName, "Field should be empty after refresh");
        System.out.println("✓ Form cleared after refresh");
        
        Thread.sleep(1000);
    }
    
    @Test
    @DisplayName("get() vs navigate().to()")
    public void testGetVsNavigate() throws InterruptedException {
        System.out.println("\n=== driver.get() vs navigate().to() ===");
        
        System.out.println("\nUsing driver.get():");
        long startTime = System.currentTimeMillis();
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/login.html");
        long getTime = System.currentTimeMillis() - startTime;
        System.out.println("  Time taken: " + getTime + "ms");
        System.out.println("  URL: " + driver.getCurrentUrl());
        Thread.sleep(1000);
        
        System.out.println("\nUsing navigate().to():");
        startTime = System.currentTimeMillis();
        driver.navigate().to("http://127.0.0.1:5500/level-0/banking-portal-final/register.html");
        long navigateTime = System.currentTimeMillis() - startTime;
        System.out.println("  Time taken: " + navigateTime + "ms");
        System.out.println("  URL: " + driver.getCurrentUrl());
        Thread.sleep(1000);
        
        System.out.println("\nKey Differences:");
        System.out.println("  driver.get():");
        System.out.println("    - Waits for page to load completely");
        System.out.println("    - Simpler, direct navigation");
        System.out.println("    - No browser history maintained");
        System.out.println("\n  navigate().to():");
        System.out.println("    - Part of Navigation interface");
        System.out.println("    - Maintains browser history");
        System.out.println("    - Can use back() and forward()");
        System.out.println("    - More flexible navigation");
    }
    
    @Test
    @DisplayName("Get page properties")
    public void testPageProperties() throws InterruptedException {
        System.out.println("\n=== Page Properties ===");
        
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/login.html");
        
        // Get current URL
        String currentUrl = driver.getCurrentUrl();
        System.out.println("✓ getCurrentUrl(): " + currentUrl);
        
        // Get page title
        String title = driver.getTitle();
        System.out.println("✓ getTitle(): " + title);
        
        // Get page source (HTML)
        String pageSource = driver.getPageSource();
        System.out.println("✓ getPageSource() length: " + pageSource.length() + " characters");
        System.out.println("  Contains 'Banking Portal': " + pageSource.contains("Banking Portal"));
        
        // Verify page source
        assertTrue(pageSource.contains("username"));
        assertTrue(pageSource.contains("password"));
        System.out.println("✓ Page source verified");
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Multi-page navigation flow")
    public void testMultiPageNavigation() throws InterruptedException {
        System.out.println("\n=== Multi-Page Navigation Flow ===");
        
        // Start at home
        driver.navigate().to("http://127.0.0.1:5500/level-0/banking-portal-final/index.html");
        System.out.println("Step 1: index.html");
        System.out.println("  Title: " + driver.getTitle());
        Thread.sleep(1000);
        
        // Go to login
        driver.findElement(By.id("getStartedBtn")).click();
        Thread.sleep(1500);
        System.out.println("\nStep 2: login.html");
        System.out.println("  URL: " + driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().contains("login.html"));
        
        // Go to register
        driver.findElement(By.linkText("Register")).click();
        Thread.sleep(1500);
        System.out.println("\nStep 3: register.html");
        System.out.println("  URL: " + driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().contains("register.html"));
        
        // Back to login
        driver.navigate().back();
        Thread.sleep(1500);
        System.out.println("\nStep 4: Back to login.html");
        System.out.println("  URL: " + driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().contains("login.html"));
        
        // Forward to register
        driver.navigate().forward();
        Thread.sleep(1500);
        System.out.println("\nStep 5: Forward to register.html");
        System.out.println("  URL: " + driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().contains("register.html"));
        
        System.out.println("\n✓ Complete navigation flow tested!");
    }
    
    @Test
    @DisplayName("Handle navigation with alerts")
    public void testNavigationWithAlerts() throws InterruptedException {
        System.out.println("\n=== Navigation with Alerts ===");
        
        // Login
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/login.html");
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin123");
        driver.findElement(By.id("userType")).click();
        driver.findElement(By.cssSelector("option[value='customer']")).click();
        driver.findElement(By.id("terms")).click();
        driver.findElement(By.id("loginBtn")).click();
        
        Thread.sleep(2000);
        
        // Handle login alert
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("✓ Login alert: " + alert.getText());
            alert.accept();
            Thread.sleep(1000);
        } catch (NoAlertPresentException e) {
            // No alert
        }
        
        // Should be on dashboard
        assertTrue(driver.getCurrentUrl().contains("dashboard.html"));
        System.out.println("✓ On dashboard after login");
        
        // Navigate to another page
        driver.navigate().to("http://127.0.0.1:5500/level-0/banking-portal-final/transactions.html");
        Thread.sleep(1500);
        System.out.println("✓ Navigated to transactions");
        
        // Go back
        driver.navigate().back();
        Thread.sleep(1500);
        System.out.println("✓ Back to dashboard");
        assertTrue(driver.getCurrentUrl().contains("dashboard.html"));
    }
    
    @Test
    @DisplayName("Browser history navigation")
    public void testBrowserHistory() throws InterruptedException {
        System.out.println("\n=== Browser History Navigation ===");
        
        String[] pages = {
            "http://localhost:8000/index.html",
            "http://localhost:8000/login.html",
            "http://localhost:8000/register.html"
        };
        
        // Visit multiple pages
        System.out.println("Building browser history...");
        for (String page : pages) {
            driver.navigate().to(page);
            System.out.println("  Visited: " + page);
            Thread.sleep(1000);
        }
        
        // Currently on register.html
        assertTrue(driver.getCurrentUrl().contains("register.html"));
        System.out.println("\n✓ Currently on register.html");
        
        // Go back twice
        driver.navigate().back();
        Thread.sleep(1000);
        assertTrue(driver.getCurrentUrl().contains("login.html"));
        System.out.println("✓ Back once: login.html");
        
        driver.navigate().back();
        Thread.sleep(1000);
        assertTrue(driver.getCurrentUrl().contains("index.html"));
        System.out.println("✓ Back twice: index.html");
        
        // Go forward twice
        driver.navigate().forward();
        Thread.sleep(1000);
        assertTrue(driver.getCurrentUrl().contains("login.html"));
        System.out.println("✓ Forward once: login.html");
        
        driver.navigate().forward();
        Thread.sleep(1000);
        assertTrue(driver.getCurrentUrl().contains("register.html"));
        System.out.println("✓ Forward twice: register.html");
        
        System.out.println("\n✓ Browser history navigation successful!");
    }
}
