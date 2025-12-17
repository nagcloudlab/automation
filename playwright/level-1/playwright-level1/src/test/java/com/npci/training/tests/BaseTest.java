package com.npci.training.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

/**
 * BaseTest - Parent class for all Playwright Test classes
 * 
 * Provides Playwright, Browser, and Page instances to all tests
 * Handles test lifecycle with JUnit 5
 * 
 * Usage:
 * public class MyTest extends BaseTest {
 *     @Test
 *     void test() {
 *         page.navigate("https://example.com");
 *         // Use page, browser, playwright
 *     }
 * }
 */
public class BaseTest {
    
    // Playwright instances - available to all child test classes
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    
    /**
     * Setup Playwright once for entire test suite
     * Runs before all tests in the class
     */
    @BeforeAll
    public static void setupSuite() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   NPCI PLAYWRIGHT TEST SUITE STARTING     ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        // Create Playwright instance
        playwright = Playwright.create();
        
        // Launch browser (Chromium by default)
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(false) // Change to true for CI/CD
                .setSlowMo(500)     // Slow down for demo (remove for actual tests)
        );
        
        System.out.println("✓ Playwright initialized");
        System.out.println("✓ Browser launched");
    }
    
    /**
     * Setup browser context and page before each test
     * Runs before each individual test method
     */
    @BeforeEach
    public void setUp(TestInfo testInfo) {
        // Create new context for each test (isolation)
        context = browser.newContext(
            new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
        );
        
        // Create new page
        page = context.newPage();
        
        System.out.println("\n▶ Starting test: " + testInfo.getDisplayName());
    }
    
    /**
     * Cleanup after each test
     * Runs after each individual test method
     */
    @AfterEach
    public void tearDown(TestInfo testInfo) {
        // Close context (closes all pages in context)
        if (context != null) {
            context.close();
        }
        
        System.out.println("✓ Test completed: " + testInfo.getDisplayName());
    }
    
    /**
     * Cleanup after all tests
     * Runs once after all tests in the class complete
     */
    @AfterAll
    public static void teardownSuite() {
        // Close browser
        if (browser != null) {
            browser.close();
            System.out.println("✓ Browser closed");
        }
        
        // Close Playwright
        if (playwright != null) {
            playwright.close();
            System.out.println("✓ Playwright closed");
        }
        
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   NPCI PLAYWRIGHT TEST SUITE COMPLETED    ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }
}

/*
 * BASETEST LIFECYCLE:
 * 
 * @BeforeAll (once)
 *   ↓
 * @BeforeEach (before each test)
 *   ↓
 * @Test (actual test)
 *   ↓
 * @AfterEach (after each test)
 *   ↓
 * ... repeat for each test ...
 *   ↓
 * @AfterAll (once)
 * 
 * WHY USE BROWSER CONTEXT PER TEST?
 * - Complete isolation between tests
 * - No shared cookies/storage
 * - Tests can run in parallel
 * - Clean state for each test
 * - Much faster than launching new browser
 * 
 * CUSTOMIZATION:
 * Child classes can override methods:
 * 
 * @BeforeEach
 * @Override
 * public void setUp(TestInfo testInfo) {
 *     super.setUp(testInfo);
 *     // Additional setup
 * }
 * 
 * DIFFERENT BROWSERS:
 * To use Firefox or WebKit, override setupSuite():
 * 
 * @BeforeAll
 * public static void setupSuite() {
 *     playwright = Playwright.create();
 *     browser = playwright.firefox().launch(...);
 * }
 */
