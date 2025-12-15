package com.npci.training.level2;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 2 - Test 05: Multiple Windows and Tabs
 * 
 * Topics Covered:
 * - Getting window handles
 * - Switching between windows
 * - Closing windows
 * - Window handle management
 * - Working with multiple tabs
 * 
 * Duration: 20 minutes
 */
public class Test05_MultipleWindows {

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
            driver.quit();  // Closes all windows
        }
    }
    
    @Test
    @DisplayName("Get current window handle")
    public void testGetWindowHandle() throws InterruptedException {
        System.out.println("\n=== Get Window Handle ===");
        
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/login.html");
        
        // Get current window handle
        String mainWindow = driver.getWindowHandle();
        System.out.println("✓ Main window handle: " + mainWindow);
        System.out.println("  Handle length: " + mainWindow.length());
        
        assertNotNull(mainWindow);
        assertFalse(mainWindow.isEmpty());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Get all window handles")
    public void testGetWindowHandles() throws InterruptedException {
        System.out.println("\n=== Get All Window Handles ===");
        
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/reports.html");
        
        // Get all handles (currently just one)
        Set<String> handles = driver.getWindowHandles();
        System.out.println("✓ Total windows: " + handles.size());
        
        for (String handle : handles) {
            System.out.println("  Window: " + handle);
        }
        
        assertEquals(1, handles.size());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Open new window and switch")
    public void testSwitchToNewWindow() throws InterruptedException {
        System.out.println("\n=== Switch to New Window ===");
        
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/reports.html");
        
        // Store main window handle
        String mainWindow = driver.getWindowHandle();
        System.out.println("✓ Main window: " + mainWindow);
        System.out.println("  URL: " + driver.getCurrentUrl());
        
        // Click button that opens new window
        driver.findElement(By.xpath("//button[contains(text(), 'Open Help in New Window')]")).click();
        Thread.sleep(2000);
        
        // Get all window handles
        Set<String> allWindows = driver.getWindowHandles();
        System.out.println("\n✓ Total windows now: " + allWindows.size());
        
        // Switch to new window
        for (String window : allWindows) {
            if (!window.equals(mainWindow)) {
                driver.switchTo().window(window);
                System.out.println("✓ Switched to new window");
                System.out.println("  URL: " + driver.getCurrentUrl());
                break;
            }
        }
        
        Thread.sleep(2000);
        
        // Close new window
        driver.close();
        System.out.println("✓ New window closed");
        
        // Switch back to main window
        driver.switchTo().window(mainWindow);
        System.out.println("✓ Switched back to main window");
        System.out.println("  URL: " + driver.getCurrentUrl());
        
        Thread.sleep(1000);
    }
    
    @Test
    @DisplayName("Work with multiple windows")
    public void testMultipleWindows() throws InterruptedException {
        System.out.println("\n=== Multiple Windows Management ===");
        
        driver.get("http://localhost:8000/index.html");
        String mainWindow = driver.getWindowHandle();
        
        // Open multiple windows by opening links in new tabs
        // Using JavaScript to open new windows
        ((JavascriptExecutor) driver).executeScript("window.open('http://localhost:8000/login.html', '_blank');");
        Thread.sleep(1000);
        
        ((JavascriptExecutor) driver).executeScript("window.open('http://localhost:8000/register.html', '_blank');");
        Thread.sleep(1000);
        
        // Get all windows
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        System.out.println("✓ Total windows: " + windows.size());
        
        // Switch to each window and print URL
        for (int i = 0; i < windows.size(); i++) {
            driver.switchTo().window(windows.get(i));
            System.out.println("\nWindow " + (i + 1) + ":");
            System.out.println("  Handle: " + windows.get(i).substring(0, 10) + "...");
            System.out.println("  URL: " + driver.getCurrentUrl());
            System.out.println("  Title: " + driver.getTitle());
            Thread.sleep(1000);
        }
        
        // Close all windows except main
        for (String window : windows) {
            if (!window.equals(mainWindow)) {
                driver.switchTo().window(window);
                driver.close();
                System.out.println("\n✓ Closed window: " + window.substring(0, 10) + "...");
            }
        }
        
        // Switch back to main
        driver.switchTo().window(mainWindow);
        System.out.println("\n✓ Back to main window");
        assertEquals(1, driver.getWindowHandles().size());
    }
    
    @Test
    @DisplayName("Handle window titles")
    public void testWindowTitles() throws InterruptedException {
        System.out.println("\n=== Window Titles ===");
        
        driver.get("http://localhost:8000/index.html");
        String mainWindow = driver.getWindowHandle();
        
        // Open new windows
        ((JavascriptExecutor) driver).executeScript("window.open('http://localhost:8000/login.html', '_blank');");
        Thread.sleep(1000);
        
        ((JavascriptExecutor) driver).executeScript("window.open('http://localhost:8000/dashboard.html', '_blank');");
        Thread.sleep(1000);
        
        // Find and switch to window by title
        Set<String> allWindows = driver.getWindowHandles();
        
        for (String window : allWindows) {
            driver.switchTo().window(window);
            String title = driver.getTitle();
            System.out.println("\nWindow title: " + title);
            
            if (title.contains("Login")) {
                System.out.println("✓ Found Login window");
                assertTrue(driver.getCurrentUrl().contains("login.html"));
            } else if (title.contains("Dashboard")) {
                System.out.println("✓ Found Dashboard window");
                assertTrue(driver.getCurrentUrl().contains("dashboard.html"));
            }
        }
        
        // Close all except main
        for (String window : allWindows) {
            if (!window.equals(mainWindow)) {
                driver.switchTo().window(window);
                driver.close();
            }
        }
        
        driver.switchTo().window(mainWindow);
        Thread.sleep(1000);
    }
    
    @Test
    @DisplayName("Parent and child window pattern")
    public void testParentChildWindows() throws InterruptedException {
        System.out.println("\n=== Parent-Child Window Pattern ===");
        
        // Parent window
        driver.get("http://localhost:8000/reports.html");
        String parentWindow = driver.getWindowHandle();
        System.out.println("✓ Parent window opened");
        System.out.println("  Title: " + driver.getTitle());
        
        // Open child window
        driver.findElement(By.xpath("//button[contains(text(), 'Open New Window')]")).click();
        Thread.sleep(2000);
        
        // Get child window handle
        Set<String> allWindows = driver.getWindowHandles();
        String childWindow = null;
        
        for (String window : allWindows) {
            if (!window.equals(parentWindow)) {
                childWindow = window;
                break;
            }
        }
        
        assertNotNull(childWindow, "Child window should exist");
        System.out.println("✓ Child window detected");
        
        // Switch to child
        driver.switchTo().window(childWindow);
        System.out.println("\n✓ Switched to child window");
        System.out.println("  URL: " + driver.getCurrentUrl());
        Thread.sleep(1500);
        
        // Perform action in child
        System.out.println("  Performing actions in child window...");
        Thread.sleep(1000);
        
        // Close child
        driver.close();
        System.out.println("\n✓ Child window closed");
        
        // Switch back to parent
        driver.switchTo().window(parentWindow);
        System.out.println("✓ Back to parent window");
        System.out.println("  URL: " + driver.getCurrentUrl());
        
        // Verify only parent remains
        assertEquals(1, driver.getWindowHandles().size());
        System.out.println("✓ Only parent window remains");
        
        Thread.sleep(1000);
    }
    
    @Test
    @DisplayName("Window management best practices")
    public void testWindowManagementBestPractices() throws InterruptedException {
        System.out.println("\n=== Window Management Best Practices ===");
        
        driver.get("http://localhost:8000/index.html");
        
        // BEST PRACTICE 1: Always store main window handle
        String mainWindow = driver.getWindowHandle();
        System.out.println("✓ Best Practice 1: Store main window handle");
        
        // BEST PRACTICE 2: Use ArrayList for easier access
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        System.out.println("✓ Best Practice 2: Use ArrayList for indexed access");
        
        // Open new window
        ((JavascriptExecutor) driver).executeScript("window.open('http://localhost:8000/login.html', '_blank');");
        Thread.sleep(1000);
        
        // BEST PRACTICE 3: Update window list after changes
        tabs = new ArrayList<>(driver.getWindowHandles());
        System.out.println("✓ Best Practice 3: Update window list after changes");
        System.out.println("  Total windows: " + tabs.size());
        
        // BEST PRACTICE 4: Switch using ArrayList index
        driver.switchTo().window(tabs.get(1));
        System.out.println("✓ Best Practice 4: Switch using index: tabs.get(1)");
        
        // BEST PRACTICE 5: Always close windows you opened
        driver.close();
        System.out.println("✓ Best Practice 5: Close windows you opened");
        
        // BEST PRACTICE 6: Switch back to main window
        driver.switchTo().window(mainWindow);
        System.out.println("✓ Best Practice 6: Switch back to main window");
        
        System.out.println("\n📋 Best Practices Summary:");
        System.out.println("  1. Store main window handle at start");
        System.out.println("  2. Use ArrayList for easier management");
        System.out.println("  3. Update window list after opening/closing");
        System.out.println("  4. Use index-based switching when possible");
        System.out.println("  5. Always close windows you opened");
        System.out.println("  6. Return to main window when done");
        
        Thread.sleep(2000);
    }
}
