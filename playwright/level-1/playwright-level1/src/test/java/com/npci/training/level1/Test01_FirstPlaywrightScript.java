package com.npci.training.level1;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.ViewportSize;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Playwright Level 1 - Test 01: First Playwright Script
 * 
 * Topics Covered:
 * - Playwright initialization
 * - Browser launch (Chromium)
 * - Page navigation
 * - Basic assertions
 * - Browser close
 * 
 * Duration: 20 minutes
 */
@DisplayName("First Playwright Script")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test01_FirstPlaywrightScript {

    @Test
    @Order(1)
    @DisplayName("Test 1: Launch browser and navigate to Google")
    public void test01_LaunchBrowserAndNavigate() {
        System.out.println("\n=== Test 01: First Playwright Script ===");

        // Step 1: Create Playwright instance
        System.out.println("Step 1: Creating Playwright instance...");
        Playwright playwright = Playwright.create();

        // Step 2: Launch Chromium browser
        System.out.println("Step 2: Launching Chromium browser...");
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false) // Set to true for headless mode
                        .setSlowMo(1000) // Slow down by 1 second (for demo)
        );

        // Step 3: Create a new page
        System.out.println("Step 3: Creating new page...");
        Page page = browser.newPage();

        // Step 4: Navigate to Google
        System.out.println("Step 4: Navigating to Google...");
        page.navigate("https://www.google.com");

        // Step 5: Verify page title
        System.out.println("Step 5: Verifying page title...");
        String title = page.title();
        System.out.println("✓ Page title: " + title);
        assertTrue(title.contains("Google"), "Title should contain 'Google'");

        // Step 6: Verify URL
        System.out.println("Step 6: Verifying URL...");
        String url = page.url();
        System.out.println("✓ Current URL: " + url);
        assertTrue(url.contains("google.com"), "URL should contain 'google.com'");

        // Step 7: Close browser
        System.out.println("Step 7: Closing browser...");
        browser.close();

        // Step 8: Close Playwright
        System.out.println("Step 8: Closing Playwright...");
        playwright.close();

        System.out.println("✓ Test completed successfully!\n");
    }

    @Test
    @Disabled
    @Order(2)
    @DisplayName("Test 2: Navigate to Banking Portal")
    public void test02_NavigateToBankingPortal() {
        System.out.println("\n=== Test 02: Banking Portal Navigation ===");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        // Navigate to Banking Portal (use actual URL from Level 1 Selenium)
        System.out.println("Navigating to Banking Portal...");
        page.navigate("http://localhost:8080"); // Adjust URL as needed

        // Wait for page to load
        page.waitForLoadState();

        // Verify page loaded
        String title = page.title();
        System.out.println("✓ Page title: " + title);

        // Check if login elements are visible
        boolean isLoginVisible = page.locator("#username").isVisible();
        System.out.println("✓ Login form visible: " + isLoginVisible);

        // Close
        browser.close();
        playwright.close();

        System.out.println("✓ Banking portal test completed!\n");
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Browser options demo")
    public void test03_BrowserOptions() {
        System.out.println("\n=== Test 03: Browser Options ===");

        Playwright playwright = Playwright.create();

        // Launch with different options
        System.out.println("Launching browser with custom options...");
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(500)
                        .setArgs(java.util.Arrays.asList(
                                "--start-maximized",
                                "--disable-blink-features=AutomationControlled")));

        // Create page with viewport
        Page page = browser.newPage(
                new Browser.NewPageOptions()
                        .setViewportSize(1920, 1080));

        System.out.println("✓ Browser launched with custom options");
        System.out.println("✓ Viewport size: 1920x1080");

        page.navigate("https://www.google.com");

        // Get viewport size
        ViewportSize viewport = page.viewportSize();
        System.out.println("✓ Actual viewport: " + viewport.width + "x" + viewport.height);

        assertEquals(1920, viewport.width);
        assertEquals(1080, viewport.height);

        browser.close();
        playwright.close();

        System.out.println("✓ Browser options test completed!\n");
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Multiple pages in one browser")
    public void test04_MultiplePages() {
        System.out.println("\n=== Test 04: Multiple Pages ===");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));

        // Create first page
        System.out.println("Opening Page 1: Google");
        Page page1 = browser.newPage();
        page1.navigate("https://www.google.com");
        System.out.println("✓ Page 1 URL: " + page1.url());

        // Create second page
        System.out.println("Opening Page 2: GitHub");
        Page page2 = browser.newPage();
        page2.navigate("https://github.com");
        System.out.println("✓ Page 2 URL: " + page2.url());

        // Switch between pages
        System.out.println("Switching back to Page 1...");
        System.out.println("✓ Page 1 title: " + page1.title());

        System.out.println("Switching to Page 2...");
        System.out.println("✓ Page 2 title: " + page2.title());

        // Close specific page
        page2.close();
        System.out.println("✓ Page 2 closed");

        // Page 1 still works
        page1.reload();
        System.out.println("✓ Page 1 reloaded successfully");

        browser.close();
        playwright.close();

        System.out.println("✓ Multiple pages test completed!\n");
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Headless vs Headed mode")
    public void test05_HeadlessVsHeaded() {
        System.out.println("\n=== Test 05: Headless vs Headed ===");

        Playwright playwright = Playwright.create();

        // Test 1: Headless mode (faster, for CI/CD)
        System.out.println("Test 1: Headless mode (no visible browser)...");
        Browser headlessBrowser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true));
        Page headlessPage = headlessBrowser.newPage();
        headlessPage.navigate("https://www.google.com");
        System.out.println("✓ Headless navigation successful: " + headlessPage.title());
        headlessBrowser.close();

        // Test 2: Headed mode (visible browser, for debugging)
        System.out.println("Test 2: Headed mode (visible browser)...");
        Browser headedBrowser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        Page headedPage = headedBrowser.newPage();
        headedPage.navigate("https://www.google.com");
        System.out.println("✓ Headed navigation successful: " + headedPage.title());

        // Wait a bit to see the browser
        headedPage.waitForTimeout(2000);

        headedBrowser.close();
        playwright.close();

        System.out.println("✓ Headless vs Headed test completed!\n");
    }
}

/*
 * KEY CONCEPTS:
 * 
 * 1. Playwright Instance:
 * - Playwright playwright = Playwright.create();
 * - Entry point for all Playwright operations
 * - Should be created once and reused
 * 
 * 2. Browser Launch:
 * - Browser browser = playwright.chromium().launch();
 * - Options: headless, slowMo, args, etc.
 * - Browsers: chromium(), firefox(), webkit()
 * 
 * 3. Page Creation:
 * - Page page = browser.newPage();
 * - Multiple pages can be created from one browser
 * - Each page is independent
 * 
 * 4. Navigation:
 * - page.navigate(url);
 * - Waits for page to load automatically
 * - Returns when page is loaded
 * 
 * 5. Cleanup:
 * - browser.close(); // Close browser
 * - playwright.close(); // Close Playwright
 * - Important for resource cleanup
 * 
 * HEADLESS vs HEADED:
 * - Headless (true): Faster, no GUI, good for CI/CD
 * - Headed (false): Visible browser, good for debugging
 * 
 * SLOW MO:
 * - Slows down operations by specified milliseconds
 * - Useful for demos and debugging
 * - Not recommended for actual test execution
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test01_FirstPlaywrightScript
 * mvn test -Dtest=Test01_FirstPlaywrightScript#test01_LaunchBrowserAndNavigate
 */
