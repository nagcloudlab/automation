package com.npci.training.level1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Level 1 - Test 01: Your First Selenium Script
 * 
 * Topics Covered:
 * - WebDriver setup with WebDriverManager
 * - Browser launch
 * - Navigation to URL
 * - Getting page properties (title, URL)
 * - Browser cleanup
 * 
 * Duration: 10 minutes
 */
public class Test01_FirstScript {

    @Test
    @DisplayName("Launch browser and navigate to banking portal")
    public void testLaunchBrowser() {
        System.out.println("\n========== TEST STARTED ==========");
        
        // Step 1: Setup WebDriver
        System.out.println("Step 1: Setting up ChromeDriver");
        WebDriverManager.chromedriver().setup();
        
        // Step 2: Create driver instance  
        System.out.println("Step 2: Creating Chrome browser instance");
        WebDriver driver = new ChromeDriver();
        
        try {
            // Step 3: Maximize window
            System.out.println("Step 3: Maximizing browser window");
            driver.manage().window().maximize();
            
            // Step 4: Navigate to application
            String url = "http://127.0.0.1:5500/level-0/banking-portal-final/login.html";
            System.out.println("Step 4: Navigating to " + url);
            driver.get(url);
            
            // Step 5: Get page title
            String pageTitle = driver.getTitle();
            System.out.println("Step 5: Page Title = " + pageTitle);
            
            // Step 6: Get current URL
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Step 6: Current URL = " + currentUrl);
            
            // Step 7: Verify page title
            if (pageTitle.contains("Banking Portal")) {
                System.out.println("✓ SUCCESS: Correct page loaded!");
            } else {
                System.out.println("✗ FAILED: Unexpected page!");
            }
            
            // Wait to see the page
            Thread.sleep(3000);
            
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Step 8: Close browser
            System.out.println("Step 8: Closing browser");
            driver.quit();
            System.out.println("========== TEST COMPLETED ==========\n");
        }
    }
}
