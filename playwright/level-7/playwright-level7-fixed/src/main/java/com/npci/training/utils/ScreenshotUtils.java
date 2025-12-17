package com.npci.training.utils;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.ScreenshotType;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * ScreenshotUtils - Screenshot utility methods
 * 
 * Features:
 * - Full page screenshots
 * - Element screenshots
 * - Screenshot on failure
 * - Base64 encoding
 * - Custom names
 */
public class ScreenshotUtils {
    
    private static final String SCREENSHOTS_DIR = "screenshots";
    
    static {
        // Create screenshots directory
        new File(SCREENSHOTS_DIR).mkdirs();
    }
    
    /**
     * Take full page screenshot
     * @param page Page object
     * @param name Screenshot name
     * @return Screenshot path
     */
    public static String takeScreenshot(Page page, String name) {
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        
        String fileName = name.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".png";
        String path = SCREENSHOTS_DIR + "/" + fileName;
        
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(Paths.get(path))
            .setFullPage(true));
        
        System.out.println("📸 Screenshot saved: " + path);
        return path;
    }
    
    /**
     * Take full page screenshot (simple)
     * @param page Page object
     * @return Screenshot path
     */
    public static String takeScreenshot(Page page) {
        return takeScreenshot(page, "screenshot");
    }
    
    /**
     * Take element screenshot
     * @param locator Locator object
     * @param name Screenshot name
     * @return Screenshot path
     */
    public static String takeElementScreenshot(Locator locator, String name) {
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        
        String fileName = name.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".png";
        String path = SCREENSHOTS_DIR + "/" + fileName;
        
        locator.screenshot(new Locator.ScreenshotOptions()
            .setPath(Paths.get(path)));
        
        System.out.println("📸 Element screenshot saved: " + path);
        return path;
    }
    
    /**
     * Take screenshot as base64
     * @param page Page object
     * @return Base64 encoded screenshot
     */
    public static String takeScreenshotBase64(Page page) {
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions()
            .setFullPage(true));
        return Base64.getEncoder().encodeToString(screenshot);
    }
    
    /**
     * Take screenshot on failure
     * @param page Page object
     * @param testName Test name
     * @return Screenshot path
     */
    public static String takeScreenshotOnFailure(Page page, String testName) {
        return takeScreenshot(page, "FAILED_" + testName);
    }
    
    /**
     * Take screenshot with custom options
     * @param page Page object
     * @param name Screenshot name
     * @param fullPage Full page screenshot
     * @return Screenshot path
     */
    public static String takeScreenshot(Page page, String name, boolean fullPage) {
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        
        String fileName = name.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".png";
        String path = SCREENSHOTS_DIR + "/" + fileName;
        
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(Paths.get(path))
            .setFullPage(fullPage));
        
        System.out.println("📸 Screenshot saved: " + path);
        return path;
    }
    
    /**
     * Take screenshot with type
     * @param page Page object
     * @param name Screenshot name
     * @param type Screenshot type (PNG or JPEG)
     * @return Screenshot path
     */
    public static String takeScreenshot(Page page, String name, ScreenshotType type) {
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        
        String extension = type == ScreenshotType.JPEG ? ".jpg" : ".png";
        String fileName = name.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + extension;
        String path = SCREENSHOTS_DIR + "/" + fileName;
        
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(Paths.get(path))
            .setType(type)
            .setFullPage(true));
        
        System.out.println("📸 Screenshot saved: " + path);
        return path;
    }
    
    /**
     * Get screenshots directory
     * @return Screenshots directory path
     */
    public static String getScreenshotsDir() {
        return SCREENSHOTS_DIR;
    }
    
    /**
     * Clean old screenshots
     * @param daysOld Delete screenshots older than N days
     */
    public static void cleanOldScreenshots(int daysOld) {
        File screenshotsDir = new File(SCREENSHOTS_DIR);
        if (!screenshotsDir.exists()) {
            return;
        }
        
        long cutoffTime = System.currentTimeMillis() - (daysOld * 24L * 60 * 60 * 1000);
        
        File[] files = screenshotsDir.listFiles();
        if (files == null) {
            return;
        }
        
        int deletedCount = 0;
        for (File file : files) {
            if (file.isFile() && file.lastModified() < cutoffTime) {
                if (file.delete()) {
                    deletedCount++;
                }
            }
        }
        
        if (deletedCount > 0) {
            System.out.println("🗑️  Deleted " + deletedCount + " old screenshots");
        }
    }
}

/*
 * SCREENSHOT UTILS USAGE:
 * 
 * 1. FULL PAGE SCREENSHOT:
 *    String path = ScreenshotUtils.takeScreenshot(page, "login_page");
 * 
 * 2. ELEMENT SCREENSHOT:
 *    Locator errorMsg = page.locator(".error-message");
 *    ScreenshotUtils.takeElementScreenshot(errorMsg, "error");
 * 
 * 3. BASE64 SCREENSHOT:
 *    String base64 = ScreenshotUtils.takeScreenshotBase64(page);
 *    ExtentReportsManager.attachScreenshotBase64(base64, "Login");
 * 
 * 4. ON FAILURE:
 *    try {
 *        // Test logic
 *    } catch (Exception e) {
 *        ScreenshotUtils.takeScreenshotOnFailure(page, testName);
 *        throw e;
 *    }
 * 
 * 5. CUSTOM OPTIONS:
 *    ScreenshotUtils.takeScreenshot(page, "dashboard", false);
 *    ScreenshotUtils.takeScreenshot(page, "image", ScreenshotType.JPEG);
 * 
 * 6. CLEANUP:
 *    ScreenshotUtils.cleanOldScreenshots(7);  // Delete older than 7 days
 * 
 * FEATURES:
 * ✅ Full page screenshots
 * ✅ Element screenshots
 * ✅ Base64 encoding
 * ✅ Custom names with timestamp
 * ✅ PNG/JPEG formats
 * ✅ Automatic directory creation
 * ✅ Cleanup old screenshots
 * 
 * LOCATION:
 * screenshots/login_page_20241217_103045_123.png
 */
