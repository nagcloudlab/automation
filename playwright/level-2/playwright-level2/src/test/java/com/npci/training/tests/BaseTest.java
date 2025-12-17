package com.npci.training.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

/**
 * BaseTest - Parent class for all Playwright Level 2 tests
 */
public class BaseTest {
    
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    
    @BeforeAll
    public static void setupSuite() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  PLAYWRIGHT LEVEL 2: LOCATORS & ACTIONS   ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(500)
        );
        
        System.out.println("✓ Playwright initialized");
        System.out.println("✓ Browser launched");
    }
    
    @BeforeEach
    public void setUp(TestInfo testInfo) {
        context = browser.newContext(
            new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
        );
        page = context.newPage();
        System.out.println("\n▶ Starting: " + testInfo.getDisplayName());
    }
    
    @AfterEach
    public void tearDown(TestInfo testInfo) {
        if (context != null) {
            context.close();
        }
        System.out.println("✓ Completed: " + testInfo.getDisplayName());
    }
    
    @AfterAll
    public static void teardownSuite() {
        if (browser != null) {
            browser.close();
            System.out.println("✓ Browser closed");
        }
        if (playwright != null) {
            playwright.close();
            System.out.println("✓ Playwright closed");
        }
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       LEVEL 2 TESTS COMPLETED              ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }
}
