package com.npci.training.tests;

import com.microsoft.playwright.*;
import com.npci.training.pages.*;
import org.junit.jupiter.api.*;

/**
 * BaseTest - Parent class with Page Object fixtures
 * 
 * Provides:
 * - Browser and page setup
 * - Page Object instances
 * - Common test utilities
 */
public class BaseTest {
    
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    
    // Page Objects (available to all tests)
    protected LoginPage loginPage;
    protected DashboardPage dashboardPage;
    protected TransferPage transferPage;
    
    @BeforeAll
    public static void setupSuite() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   PLAYWRIGHT LEVEL 4: PAGE OBJECT MODEL   ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(300)
        );
        
        System.out.println("✓ Playwright initialized");
        System.out.println("✓ Browser launched");
    }
    
    @BeforeEach
    public void setUp(TestInfo testInfo) {
        // Create new context and page
        context = browser.newContext(
            new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
        );
        page = context.newPage();
        
        // Initialize Page Objects
        loginPage = new LoginPage(page);
        dashboardPage = new DashboardPage(page);
        transferPage = new TransferPage(page);
        
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
        System.out.println("║       LEVEL 4 TESTS COMPLETED              ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }
    
    /**
     * Helper: Login with default credentials
     * @return DashboardPage
     */
    protected DashboardPage loginWithDefaultUser() {
        return loginPage
            .navigate()
            .login("admin", "password123");
    }
    
    /**
     * Helper: Take screenshot
     * @param name Screenshot name
     */
    protected void takeScreenshot(String name) {
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(java.nio.file.Paths.get("screenshots/" + name + ".png"))
            .setFullPage(true));
    }
}

/*
 * BASE TEST WITH PAGE OBJECTS:
 * 
 * 1. PAGE OBJECT FIXTURES:
 *    protected LoginPage loginPage;
 *    protected DashboardPage dashboardPage;
 *    
 *    // Initialized in @BeforeEach
 *    loginPage = new LoginPage(page);
 * 
 * 2. HELPER METHODS:
 *    protected DashboardPage loginWithDefaultUser() {
 *        return loginPage.navigate().login("admin", "pass");
 *    }
 *    // Reusable across tests
 * 
 * 3. USAGE IN TESTS:
 *    @Test
 *    void testTransfer() {
 *        DashboardPage dashboard = loginWithDefaultUser();
 *        TransferPage transfer = dashboard.goToTransfer();
 *        transfer.doTransfer(...).verifyTransferSuccessful();
 *    }
 * 
 * 4. BENEFITS:
 *    ✅ Page objects available to all tests
 *    ✅ Common helpers in one place
 *    ✅ Clean test methods
 *    ✅ Easy to maintain
 */
