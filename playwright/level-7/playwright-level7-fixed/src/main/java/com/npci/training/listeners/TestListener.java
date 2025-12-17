package com.npci.training.listeners;

import com.npci.training.reporting.ExtentReportsManager;
import org.junit.jupiter.api.extension.*;

import java.util.Optional;

/**
 * TestListener - JUnit 5 Extension for test lifecycle
 * 
 * Features:
 * - Automatic screenshot on failure
 * - Log test start/end
 * - Extent reports integration
 * - Error handling
 */
public class TestListener implements TestWatcher, BeforeEachCallback, AfterEachCallback {
    
    private static ThreadLocal<Long> startTime = new ThreadLocal<>();
    
    @Override
    public void beforeEach(ExtensionContext context) {
        startTime.set(System.currentTimeMillis());
        
        String testName = context.getDisplayName();
        String className = context.getTestClass().map(Class::getSimpleName).orElse("Unknown");
        
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  STARTING: " + testName);
        System.out.println("╚════════════════════════════════════════════╝");
        
        // Create Extent test
        ExtentReportsManager.createTest(
            className + " - " + testName,
            "Test: " + testName
        );
        
        ExtentReportsManager.logInfo("Test started: " + testName);
    }
    
    @Override
    public void afterEach(ExtensionContext context) {
        long duration = System.currentTimeMillis() - startTime.get();
        String testName = context.getDisplayName();
        
        System.out.println("Duration: " + duration + "ms");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        ExtentReportsManager.logInfo("Test completed in " + duration + "ms");
    }
    
    @Override
    public void testSuccessful(ExtensionContext context) {
        String testName = context.getDisplayName();
        System.out.println("✅ PASSED: " + testName);
        
        ExtentReportsManager.logPass("Test passed successfully");
    }
    
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        System.out.println("❌ FAILED: " + testName);
        System.out.println("Reason: " + cause.getMessage());
        
        ExtentReportsManager.logFail("Test failed: " + cause.getMessage());
        
        // Note: Screenshot will be taken in test class @AfterEach
        // because we need access to Page object
    }
    
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        System.out.println("⚠️  ABORTED: " + testName);
        
        ExtentReportsManager.logWarning("Test aborted: " + cause.getMessage());
    }
    
    @Override
    public void testDisabled(ExtensionContext context, java.util.Optional<String> reason) {
        String testName = context.getDisplayName();
        System.out.println("⏭️  SKIPPED: " + testName);
        
        ExtentReportsManager.logSkip("Test disabled: " + reason.orElse("No reason"));
    }
}

/*
 * TEST LISTENER USAGE:
 * 
 * 1. REGISTER EXTENSION:
 *    @ExtendWith(TestListener.class)
 *    public class MyTest extends BaseTest {
 *        // Tests
 *    }
 * 
 * 2. OR USE IN BASE CLASS:
 *    @ExtendWith(TestListener.class)
 *    public class BaseTest {
 *        // Setup
 *    }
 * 
 * 3. AUTOMATIC SCREENSHOT ON FAILURE:
 *    Add in BaseTest @AfterEach:
 *    
 *    @AfterEach
 *    public void tearDown(TestInfo testInfo) {
 *        if (testFailed) {
 *            String path = ScreenshotUtils.takeScreenshotOnFailure(
 *                page, testInfo.getDisplayName());
 *            
 *            String base64 = ScreenshotUtils.takeScreenshotBase64(page);
 *            ExtentReportsManager.attachScreenshotBase64(base64, "Failure");
 *        }
 *    }
 * 
 * FEATURES:
 * ✅ Logs test start/end
 * ✅ Calculates test duration
 * ✅ Updates Extent reports
 * ✅ Pretty console output
 * ✅ Handles all test states
 * 
 * OUTPUT:
 * ╔════════════════════════════════════════════╗
 * ║  STARTING: Test Login
 * ╚════════════════════════════════════════════╝
 * ✅ PASSED: Test Login
 * Duration: 1234ms
 * ╚════════════════════════════════════════════╝
 */
