package com.npci.training.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportsManager - Manage Extent Reports
 * 
 * Features:
 * - HTML reports with screenshots
 * - Test status tracking
 * - Custom configuration
 * - Beautiful themes
 */
public class ExtentReportsManager {
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static String reportPath;
    
    /**
     * Initialize Extent Reports
     */
    public static void initReports() {
        if (extent != null) {
            return;
        }
        
        // Create reports directory
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        reportPath = "reports/ExtentReport_" + timestamp + ".html";
        
        new File("reports").mkdirs();
        
        // Create reporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        
        // Configure reporter
        sparkReporter.config().setDocumentTitle("NPCI Playwright Test Report");
        sparkReporter.config().setReportName("Banking Portal Automation");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setTimeStampFormat("dd-MMM-yyyy HH:mm:ss");
        
        // Create extent reports
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // System info
        extent.setSystemInfo("Application", "Banking Portal");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Browser", "Chromium");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));
        
        System.out.println("✓ Extent Reports initialized: " + reportPath);
    }
    
    /**
     * Create a new test
     * @param testName Test name
     * @param description Test description
     */
    public static void createTest(String testName, String description) {
        ExtentTest extentTest = extent.createTest(testName, description);
        test.set(extentTest);
    }
    
    /**
     * Get current test
     * @return ExtentTest
     */
    public static ExtentTest getTest() {
        return test.get();
    }
    
    /**
     * Log info message
     * @param message Message to log
     */
    public static void logInfo(String message) {
        getTest().log(Status.INFO, message);
    }
    
    /**
     * Log pass message
     * @param message Message to log
     */
    public static void logPass(String message) {
        getTest().log(Status.PASS, message);
    }
    
    /**
     * Log fail message
     * @param message Message to log
     */
    public static void logFail(String message) {
        getTest().log(Status.FAIL, message);
    }
    
    /**
     * Log warning message
     * @param message Message to log
     */
    public static void logWarning(String message) {
        getTest().log(Status.WARNING, message);
    }
    
    /**
     * Log skip message
     * @param message Message to log
     */
    public static void logSkip(String message) {
        getTest().log(Status.SKIP, message);
    }
    
    /**
     * Attach screenshot to report
     * @param screenshotPath Path to screenshot
     * @param title Screenshot title
     */
    public static void attachScreenshot(String screenshotPath, String title) {
        try {
            getTest().addScreenCaptureFromPath(screenshotPath, title);
        } catch (Exception e) {
            System.err.println("Error attaching screenshot: " + e.getMessage());
        }
    }
    
    /**
     * Attach screenshot with base64
     * @param base64 Base64 encoded screenshot
     * @param title Screenshot title
     */
    public static void attachScreenshotBase64(String base64, String title) {
        getTest().addScreenCaptureFromBase64String(base64, title);
    }
    
    /**
     * Flush reports (write to disk)
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            System.out.println("✓ Extent Reports generated: " + reportPath);
        }
    }
    
    /**
     * Get report path
     * @return Report path
     */
    public static String getReportPath() {
        return reportPath;
    }
}

/*
 * EXTENT REPORTS USAGE:
 * 
 * 1. INITIALIZE (in @BeforeAll):
 *    ExtentReportsManager.initReports();
 * 
 * 2. CREATE TEST (in @BeforeEach):
 *    ExtentReportsManager.createTest(
 *        testInfo.getDisplayName(),
 *        "Test banking login functionality"
 *    );
 * 
 * 3. LOG STEPS:
 *    ExtentReportsManager.logInfo("Navigate to login page");
 *    ExtentReportsManager.logInfo("Enter username: admin");
 *    ExtentReportsManager.logPass("Login successful");
 * 
 * 4. ATTACH SCREENSHOT:
 *    byte[] screenshot = page.screenshot();
 *    String base64 = Base64.getEncoder().encodeToString(screenshot);
 *    ExtentReportsManager.attachScreenshotBase64(base64, "Login Page");
 * 
 * 5. FLUSH (in @AfterAll):
 *    ExtentReportsManager.flushReports();
 * 
 * FEATURES:
 * ✅ Beautiful HTML reports
 * ✅ Dark/Light themes
 * ✅ Screenshots attached
 * ✅ Pass/Fail status
 * ✅ Execution time
 * ✅ System info
 * ✅ Test hierarchy
 * ✅ Filters and search
 * 
 * REPORT LOCATION:
 * reports/ExtentReport_2024-12-17_10-30-45.html
 */
