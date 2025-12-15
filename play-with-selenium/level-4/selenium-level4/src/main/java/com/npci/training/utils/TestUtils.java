package com.npci.training.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestUtils - Utility methods for tests
 * 
 * Provides common functionality:
 * - Screenshot capture
 * - Logging
 * - File operations
 * - Date/time formatting
 */
public class TestUtils {
    
    private static final String SCREENSHOT_DIR = "target/screenshots/";
    
    /**
     * Take screenshot and save to file
     * @return path to saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            // Generate filename with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;
            
            // Capture screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            
            // Save to file
            FileUtils.copyFile(srcFile, destFile);
            
            System.out.println("📸 Screenshot saved: " + filePath);
            return filePath;
            
        } catch (IOException e) {
            System.err.println("❌ Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Capture screenshot on test failure
     */
    public static String captureScreenshotOnFailure(WebDriver driver, String testName) {
        String path = captureScreenshot(driver, testName + "_FAILED");
        System.out.println("📸 Failure screenshot captured");
        return path;
    }
    
    /**
     * Simple wait
     */
    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Formatted logging
     */
    public static void log(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println("[" + timestamp + "] " + message);
    }
    
    /**
     * Success message
     */
    public static void logSuccess(String message) {
        log("✓ " + message);
    }
    
    /**
     * Error message
     */
    public static void logError(String message) {
        log("✗ " + message);
    }
    
    /**
     * Test header
     */
    public static void printTestHeader(String testName) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + testName);
        System.out.println("=".repeat(60));
    }
    
    /**
     * Get current timestamp
     */
    public static String getTimestamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }
    
    /**
     * Get formatted date
     */
    public static String getFormattedDate(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }
    
    /**
     * Highlight element (for debugging)
     */
    public static void highlightElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }
    
    /**
     * Scroll to element
     */
    public static void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
    /**
     * Execute JavaScript
     */
    public static Object executeJS(WebDriver driver, String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }
}
