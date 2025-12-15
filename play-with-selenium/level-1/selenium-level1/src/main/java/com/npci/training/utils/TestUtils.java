package com.npci.training.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Utility class for common Selenium operations
 */
public class TestUtils {
    
    /**
     * Initialize WebDriver for specified browser
     */
    public static WebDriver initializeDriver(String browserName) {
        WebDriver driver;
        
        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
                
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }
        
        driver.manage().window().maximize();
        return driver;
    }
    
    /**
     * Simple sleep method (for demo purposes only)
     * Use explicit waits in production
     */
    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Print formatted log message
     */
    public static void log(String message) {
        System.out.println("[LOG] " + message);
    }
    
    /**
     * Print success message
     */
    public static void logSuccess(String message) {
        System.out.println("[SUCCESS] ✓ " + message);
    }
    
    /**
     * Print error message
     */
    public static void logError(String message) {
        System.out.println("[ERROR] ✗ " + message);
    }
    
    /**
     * Print test header
     */
    public static void printTestHeader(String testName) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  " + testName);
        System.out.println("=".repeat(50));
    }
}
