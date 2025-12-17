package com.npci.training.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

/**
 * BaseTest - Advanced configuration for Level 5
 */
public class BaseTest {
    
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected APIRequestContext request;
    
    @BeforeAll
    public static void setupSuite() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   PLAYWRIGHT LEVEL 5: ADVANCED FEATURES   ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        playwright = Playwright.create();
        
        // Check for headless mode from system property
        boolean headless = Boolean.parseBoolean(
            System.getProperty("HEADLESS", "false"));
        
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(headless ? 0 : 300)
        );
        
        System.out.println("✓ Playwright initialized");
        System.out.println("✓ Browser launched (headless: " + headless + ")");
    }
    
    @BeforeEach
    public void setUp(TestInfo testInfo) {
        // Create context with video recording
        context = browser.newContext(
            new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setRecordVideoDir(Paths.get("videos/"))
                .setRecordVideoSize(1920, 1080)
        );
        
        // Start tracing
        context.tracing().start(new Tracing.StartOptions()
            .setScreenshots(true)
            .setSnapshots(true)
            .setSources(true));
        
        page = context.newPage();
        
        // Create API request context
        request = playwright.request().newContext(
            new APIRequest.NewContextOptions()
                .setBaseURL("https://reqres.in/api")
        );
        
        System.out.println("\n▶ Starting: " + testInfo.getDisplayName());
    }
    
    @AfterEach
    public void tearDown(TestInfo testInfo) {
        // Stop tracing and save
        if (context != null) {
            String traceName = testInfo.getDisplayName()
                .replaceAll("[^a-zA-Z0-9]", "_");
            context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("traces/" + traceName + ".zip")));
            context.close();
        }
        
        if (request != null) {
            request.dispose();
        }
        
        System.out.println("✓ Completed: " + testInfo.getDisplayName());
    }
    
    @AfterAll
    public static void teardownSuite() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       LEVEL 5 TESTS COMPLETED              ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }
}
