package com.npci.training.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;

/**
 * ExtentReportListener - TestNG Listener for ExtentReports
 * 
 * Generates beautiful HTML reports for test execution
 * Automatically captures test results, screenshots, logs
 */
public class ExtentReportListener implements ITestListener {
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    @Override
    public void onStart(ITestContext context) {
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
        sparkReporter.config().setReportName("Automation Test Results");
        sparkReporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");
        
        // Initialize ExtentReports
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // System information
        extent.setSystemInfo("Organization", "NPCI");
        extent.setSystemInfo("Test Environment", "QA");
        extent.setSystemInfo("Browser", "Chrome");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        
        ExtentTest extentTest = extent.createTest(testName, description);
        test.set(extentTest);
        
        System.out.println("✓ Started: " + testName);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test PASSED: " + result.getMethod().getMethodName());
        test.get().pass("Test execution successful");
        
        System.out.println("✓ PASSED: " + result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, "Test FAILED: " + result.getMethod().getMethodName());
        test.get().fail(result.getThrowable());
        
        // TODO: Attach screenshot here
        // test.get().addScreenCaptureFromPath(screenshotPath);
        
        System.out.println("✗ FAILED: " + result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, "Test SKIPPED: " + result.getMethod().getMethodName());
        test.get().skip(result.getThrowable());
        
        System.out.println("⊘ SKIPPED: " + result.getMethod().getMethodName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  EXTENT REPORTS - GENERATED           ║");
        System.out.println("║  Location: target/extent-reports/      ║");
        System.out.println("║  File: TestReport.html                 ║");
        System.out.println("╚════════════════════════════════════════╝\n");
    }
    
    public static ExtentTest getTest() {
        return test.get();
    }
}
