package com.npci.training.tests;

import com.npci.training.utils.TestUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * BaseTest - Parent class for all Test classes
 * 
 * Uses TestNG annotations for setup/teardown
 * Provides WebDriver instance to all tests
 * Handles screenshot on failure
 */
public class BaseTest {
    
    protected WebDriver driver;
    
    @BeforeSuite
    public void beforeSuite() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   NPCI SELENIUM TEST SUITE STARTING   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Setup WebDriver once for entire suite
        WebDriverManager.chromedriver().setup();
    }
    
    @BeforeClass
    public void beforeClass() {
        System.out.println("\n=== Starting Test Class: " + this.getClass().getSimpleName() + " ===");
    }
    
    @BeforeMethod
    public void setUp() {
        // Create new browser for each test
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        System.out.println("✓ Browser started");
    }
    
    @AfterMethod
    public void tearDown(ITestResult result) {
        // Capture screenshot on failure
        if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("✗ TEST FAILED: " + result.getName());
            TestUtils.captureScreenshotOnFailure(driver, result.getName());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            System.out.println("✓ TEST PASSED: " + result.getName());
        }
        
        // Close browser
        if (driver != null) {
            driver.quit();
            System.out.println("✓ Browser closed");
        }
    }
    
    @AfterClass
    public void afterClass() {
        System.out.println("=== Completed Test Class: " + this.getClass().getSimpleName() + " ===\n");
    }
    
    @AfterSuite
    public void afterSuite() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   NPCI SELENIUM TEST SUITE COMPLETED  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
}
