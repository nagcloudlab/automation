package com.npci.training.extensions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.junit.jupiter.api.extension.*;

import java.io.File;
import java.util.Optional;

/**
 * ExtentReportExtension - JUnit 5 Extension for ExtentReports
 * 
 * Generates beautiful HTML reports for test execution
 * Automatically captures test results, logs
 * 
 * Usage:
 * @ExtendWith(ExtentReportExtension.class)
 * public class MyTests { }
 */
public class ExtentReportExtension implements 
        BeforeAllCallback, AfterAllCallback, 
        BeforeEachCallback, AfterEachCallback,
        TestWatcher {
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    @Override
    public void beforeAll(ExtensionContext context) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  EXTENT REPORTS - INITIALIZING        ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Create reports directory
        File reportsDir = new File("target/extent-reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }
        
        // Configure ExtentSparkReporter
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/extent-reports/TestReport.html");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("NPCI Selenium Test Report");
        sparkReporter.config().setReportName("JUnit 5 Automation Test Results");
        sparkReporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");
        
        // Initialize ExtentReports
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // System information
        extent.setSystemInfo("Organization", "NPCI");
        extent.setSystemInfo("Framework", "JUnit 5");
        extent.setSystemInfo("Test Environment", "QA");
        extent.setSystemInfo("Browser", "Chrome");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
    }
    
    @Override
    public void beforeEach(ExtensionContext context) {
        String testName = context.getDisplayName();
        String className = context.getTestClass().map(Class::getSimpleName).orElse("Unknown");
        
        ExtentTest extentTest = extent.createTest(testName, "Test Class: " + className);
        test.set(extentTest);
        
        System.out.println("✓ Started: " + testName);
    }
    
    @Override
    public void afterEach(ExtensionContext context) {
        // TestWatcher handles test results
    }
    
    @Override
    public void afterAll(ExtensionContext context) {
        if (extent != null) {
            extent.flush();
        }
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  EXTENT REPORTS - GENERATED           ║");
        System.out.println("║  Location: target/extent-reports/      ║");
        System.out.println("║  File: TestReport.html                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    @Override
    public void testSuccessful(ExtensionContext context) {
        test.get().log(Status.PASS, "Test PASSED: " + context.getDisplayName());
        test.get().pass("Test execution successful");
        
        System.out.println("✓ PASSED: " + context.getDisplayName());
    }
    
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        test.get().log(Status.FAIL, "Test FAILED: " + context.getDisplayName());
        test.get().fail(cause);
        
        System.out.println("✗ FAILED: " + context.getDisplayName());
    }
    
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        test.get().log(Status.SKIP, "Test ABORTED: " + context.getDisplayName());
        test.get().skip(cause);
        
        System.out.println("⊘ ABORTED: " + context.getDisplayName());
    }
    
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String testName = context.getDisplayName();
        ExtentTest extentTest = extent.createTest(testName);
        extentTest.log(Status.SKIP, "Test DISABLED: " + reason.orElse("No reason provided"));
        
        System.out.println("⊘ DISABLED: " + testName);
    }
    
    /**
     * Get current test instance for logging
     */
    public static ExtentTest getTest() {
        return test.get();
    }
    
    /**
     * Log info message
     */
    public static void info(String message) {
        if (test.get() != null) {
            test.get().info(message);
        }
    }
    
    /**
     * Log pass message
     */
    public static void pass(String message) {
        if (test.get() != null) {
            test.get().pass(message);
        }
    }
    
    /**
     * Log fail message
     */
    public static void fail(String message) {
        if (test.get() != null) {
            test.get().fail(message);
        }
    }
    
    /**
     * Log warning message
     */
    public static void warning(String message) {
        if (test.get() != null) {
            test.get().warning(message);
        }
    }
}
