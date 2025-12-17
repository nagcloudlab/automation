package com.npci.training.tests;

import com.microsoft.playwright.*;
import com.npci.training.listeners.TestListener;
import com.npci.training.reporting.ExtentReportsManager;
import com.npci.training.utils.ScreenshotUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * BaseTest - Base test class with reporting support
 */
@ExtendWith(TestListener.class)
public class BaseTest {
    
    protected static Playwright playwright;
    protected static Browser browser;
    
    protected BrowserContext context;
    protected Page page;
    protected boolean testFailed = false;
    
    @BeforeAll
    public static void setupSuite() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   PLAYWRIGHT LEVEL 7: REPORTS & SCREENSHOTS║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        // Initialize Extent Reports
        ExtentReportsManager.initReports();
        
        // Initialize Playwright
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(300)
        );
        
        System.out.println("✓ Extent Reports initialized");
        System.out.println("✓ Playwright initialized");
        System.out.println("✓ Browser launched");
    }
    
    @BeforeEach
    public void setUp(TestInfo testInfo) {
        testFailed = false;
        
        context = browser.newContext(
            new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
        );
        page = context.newPage();
    }
    
    @AfterEach
    public void tearDown(TestInfo testInfo) {
        // Take screenshot on failure
        if (testFailed && page != null) {
            try {
                // Save to file
                String path = ScreenshotUtils.takeScreenshotOnFailure(
                    page, testInfo.getDisplayName());
                
                // Attach to report
                String base64 = ScreenshotUtils.takeScreenshotBase64(page);
                ExtentReportsManager.attachScreenshotBase64(base64, "Failure Screenshot");
                
                System.out.println("📸 Failure screenshot captured");
            } catch (Exception e) {
                System.err.println("Error capturing screenshot: " + e.getMessage());
            }
        }
        
        if (context != null) {
            context.close();
        }
    }
    
    @AfterAll
    public static void teardownSuite() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        
        // Flush Extent Reports
        ExtentReportsManager.flushReports();
        
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       LEVEL 7 TESTS COMPLETED              ║");
        System.out.println("║   Report: " + ExtentReportsManager.getReportPath());
        System.out.println("╚════════════════════════════════════════════╝\n");
    }
    
    /**
     * Mark test as failed (for screenshot)
     */
    protected void markTestFailed() {
        testFailed = true;
    }
    
    /**
     * Log step to report
     * @param step Step description
     */
    protected void logStep(String step) {
        ExtentReportsManager.logInfo("📝 " + step);
        System.out.println("  → " + step);
    }
    
    /**
     * Log and take screenshot
     * @param description Screenshot description
     */
    protected void logWithScreenshot(String description) {
        logStep(description);
        String base64 = ScreenshotUtils.takeScreenshotBase64(page);
        ExtentReportsManager.attachScreenshotBase64(base64, description);
    }
}
