package com.npci.training.level1;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.Geolocation;
import com.microsoft.playwright.options.ViewportSize;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Playwright Level 1 - Test 03: Browser Context
 * 
 * Topics Covered:
 * - What is Browser Context?
 * - Context isolation
 * - Multiple contexts in one browser
 * - Independent sessions
 * - Storage state (cookies, localStorage)
 * 
 * Duration: 30 minutes
 * 
 * KEY CONCEPT:
 * Browser Context = Incognito Window
 * - Complete isolation between contexts
 * - Separate cookies, cache, storage
 * - Multiple users in one browser
 * - Much faster than launching multiple browsers
 */
@DisplayName("Browser Context and Isolation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test03_BrowserContext {

    @Test
    @Order(1)
    @DisplayName("Test 1: Single context basics")
    public void test01_SingleContextBasics() {
        System.out.println("\n=== Test 01: Single Context Basics ===");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));

        // Create a browser context (like incognito window)
        System.out.println("Creating browser context...");
        BrowserContext context = browser.newContext();

        // Create page in this context
        Page page = context.newPage();
        page.navigate("https://www.google.com");

        System.out.println("✓ Context created successfully");
        System.out.println("✓ Page title: " + page.title());

        // Close context (closes all pages in this context)
        context.close();
        System.out.println("✓ Context closed");

        browser.close();
        playwright.close();

        System.out.println("✓ Single context test completed!\n");
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Multiple contexts - Isolation demo")
    public void test02_MultipleContextsIsolation() {
        System.out.println("\n=== Test 02: Multiple Contexts Isolation ===");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));

        // Context 1: User 1 (Admin)
        System.out.println("\n--- Context 1: Admin User ---");
        BrowserContext context1 = browser.newContext();
        Page page1 = context1.newPage();
        page1.navigate("https://example.com");

        // Set a cookie in context 1
        context1.addCookies(java.util.Arrays.asList(
                new Cookie("user", "admin")
                        .setDomain("example.com")
                        .setPath("/")));
        System.out.println("✓ Context 1: Set cookie 'user=admin'");

        // Context 2: User 2 (Regular User)
        System.out.println("\n--- Context 2: Regular User ---");
        BrowserContext context2 = browser.newContext();
        Page page2 = context2.newPage();
        page2.navigate("https://example.com");

        // Set different cookie in context 2
        context2.addCookies(java.util.Arrays.asList(
                new Cookie("user", "regular")
                        .setDomain("example.com")
                        .setPath("/")));
        System.out.println("✓ Context 2: Set cookie 'user=regular'");

        // Verify isolation: Check cookies in each context
        System.out.println("\n--- Verifying Isolation ---");
        var cookies1 = context1.cookies();
        var cookies2 = context2.cookies();

        System.out.println("✓ Context 1 cookies: " + cookies1);
        System.out.println("✓ Context 2 cookies: " + cookies2);

        // Contexts are isolated - different cookies
        assertNotEquals(cookies1.toString(), cookies2.toString(),
                "Contexts should have different cookies");

        System.out.println("✓ Contexts are completely isolated!");

        // Cleanup
        context1.close();
        context2.close();
        browser.close();
        playwright.close();

        System.out.println("✓ Context isolation test completed!\n");
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Simulating multiple users")
    public void test03_SimulatingMultipleUsers() {
        System.out.println("\n=== Test 03: Simulating Multiple Users ===");
        System.out.println("Use Case: Testing multi-user scenarios in banking portal");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));

        // User 1: Admin user
        System.out.println("\n--- User 1: Admin ---");
        BrowserContext adminContext = browser.newContext();
        Page adminPage = adminContext.newPage();
        adminPage.navigate("https://example.com");
        adminPage.evaluate("() => localStorage.setItem('role', 'admin')");
        System.out.println("✓ Admin user: Logged in with admin role");

        // User 2: Customer user
        System.out.println("\n--- User 2: Customer ---");
        BrowserContext customerContext = browser.newContext();
        Page customerPage = customerContext.newPage();
        customerPage.navigate("https://example.com");
        customerPage.evaluate("() => localStorage.setItem('role', 'customer')");
        System.out.println("✓ Customer user: Logged in with customer role");

        // User 3: Merchant user
        System.out.println("\n--- User 3: Merchant ---");
        BrowserContext merchantContext = browser.newContext();
        Page merchantPage = merchantContext.newPage();
        merchantPage.navigate("https://example.com");
        merchantPage.evaluate("() => localStorage.setItem('role', 'merchant')");
        System.out.println("✓ Merchant user: Logged in with merchant role");

        // Verify each user has correct role
        System.out.println("\n--- Verifying User Roles ---");
        String adminRole = (String) adminPage.evaluate("() => localStorage.getItem('role')");
        String customerRole = (String) customerPage.evaluate("() => localStorage.getItem('role')");
        String merchantRole = (String) merchantPage.evaluate("() => localStorage.getItem('role')");

        System.out.println("✓ Admin role: " + adminRole);
        System.out.println("✓ Customer role: " + customerRole);
        System.out.println("✓ Merchant role: " + merchantRole);

        assertEquals("admin", adminRole);
        assertEquals("customer", customerRole);
        assertEquals("merchant", merchantRole);

        System.out.println("✓ All users have correct isolated roles!");

        // Cleanup
        adminContext.close();
        customerContext.close();
        merchantContext.close();
        browser.close();
        playwright.close();

        System.out.println("✓ Multiple users test completed!\n");
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Context with custom viewport")
    public void test04_ContextWithCustomViewport() {
        System.out.println("\n=== Test 04: Context with Custom Viewport ===");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));

        // Desktop context
        System.out.println("\n--- Desktop Context (1920x1080) ---");
        BrowserContext desktopContext = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(1920, 1080));
        Page desktopPage = desktopContext.newPage();
        desktopPage.navigate("https://www.google.com");

        ViewportSize desktopViewport = desktopPage.viewportSize();
        System.out.println("✓ Desktop viewport: " + desktopViewport.width + "x" + desktopViewport.height);

        // Mobile context
        System.out.println("\n--- Mobile Context (375x667) ---");
        BrowserContext mobileContext = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(375, 667)
                        .setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X)"));
        Page mobilePage = mobileContext.newPage();
        mobilePage.navigate("https://www.google.com");

        ViewportSize mobileViewport = mobilePage.viewportSize();
        System.out.println("✓ Mobile viewport: " + mobileViewport.width + "x" + mobileViewport.height);

        // Tablet context
        System.out.println("\n--- Tablet Context (768x1024) ---");
        BrowserContext tabletContext = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(768, 1024)
                        .setUserAgent("Mozilla/5.0 (iPad; CPU OS 14_0 like Mac OS X)"));
        Page tabletPage = tabletContext.newPage();
        tabletPage.navigate("https://www.google.com");

        ViewportSize tabletViewport = tabletPage.viewportSize();
        System.out.println("✓ Tablet viewport: " + tabletViewport.width + "x" + tabletViewport.height);

        // All running simultaneously!
        System.out.println("\n✓ Three different devices running simultaneously!");
        System.out.println("✓ Testing responsive design is easy!");

        desktopPage.waitForTimeout(2000);

        // Cleanup
        desktopContext.close();
        mobileContext.close();
        tabletContext.close();
        browser.close();
        playwright.close();

        System.out.println("✓ Custom viewport test completed!\n");
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Context options - Permissions, Geolocation")
    public void test05_ContextOptions() {
        System.out.println("\n=== Test 05: Context Options ===");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));

        // Context with permissions and geolocation
        System.out.println("Creating context with custom options...");
        BrowserContext context = browser.newContext(
                new Browser.NewContextOptions()
                        .setPermissions(java.util.Arrays.asList("geolocation"))
                        .setGeolocation(new Geolocation(12.9716, 77.5946)) // Bangalore, India
                        .setLocale("en-IN")
                        .setTimezoneId("Asia/Kolkata"));

        Page page = context.newPage();
        page.navigate("https://example.com");

        // Verify geolocation is set
        Object location = page.evaluate("() => new Promise(resolve => " +
                "navigator.geolocation.getCurrentPosition(pos => resolve({" +
                "latitude: pos.coords.latitude, longitude: pos.coords.longitude})))");

        System.out.println("✓ Geolocation set: " + location);
        System.out.println("✓ Permissions granted: geolocation");
        System.out.println("✓ Locale: en-IN");
        System.out.println("✓ Timezone: Asia/Kolkata");

        context.close();
        browser.close();
        playwright.close();

        System.out.println("✓ Context options test completed!\n");
    }

    @Test
    @Order(6)
    @DisplayName("Test 6: Why contexts are faster than multiple browsers")
    public void test06_PerformanceComparison() {
        System.out.println("\n=== Test 06: Performance Comparison ===");

        Playwright playwright = Playwright.create();

        // Method 1: Multiple browsers (SLOW)
        System.out.println("\n--- Method 1: Multiple Browsers (SLOW) ---");
        long startTime1 = System.currentTimeMillis();

        Browser browser1 = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        Page page1 = browser1.newPage();
        page1.navigate("https://example.com");

        Browser browser2 = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        Page page2 = browser2.newPage();
        page2.navigate("https://example.com");

        Browser browser3 = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        Page page3 = browser3.newPage();
        page3.navigate("https://example.com");

        browser1.close();
        browser2.close();
        browser3.close();

        long endTime1 = System.currentTimeMillis();
        long duration1 = endTime1 - startTime1;
        System.out.println("✓ Time taken: " + duration1 + "ms");

        // Method 2: Multiple contexts in one browser (FAST)
        System.out.println("\n--- Method 2: Multiple Contexts in One Browser (FAST) ---");
        long startTime2 = System.currentTimeMillis();

        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

        BrowserContext context1 = browser.newContext();
        Page contextPage1 = context1.newPage();
        contextPage1.navigate("https://example.com");

        BrowserContext context2 = browser.newContext();
        Page contextPage2 = context2.newPage();
        contextPage2.navigate("https://example.com");

        BrowserContext context3 = browser.newContext();
        Page contextPage3 = context3.newPage();
        contextPage3.navigate("https://example.com");

        context1.close();
        context2.close();
        context3.close();
        browser.close();

        long endTime2 = System.currentTimeMillis();
        long duration2 = endTime2 - startTime2;
        System.out.println("✓ Time taken: " + duration2 + "ms");

        // Compare
        System.out.println("\n--- Performance Comparison ---");
        System.out.println("Multiple Browsers: " + duration1 + "ms");
        System.out.println("Multiple Contexts: " + duration2 + "ms");
        System.out.println("Speed Improvement: " + ((duration1 - duration2) * 100 / duration1) + "%");
        System.out.println("✓ Contexts are significantly faster!");

        playwright.close();

        System.out.println("✓ Performance comparison completed!\n");
    }
}

/*
 * BROWSER CONTEXT KEY CONCEPTS:
 * 
 * 1. What is Browser Context?
 * - Like an incognito/private browsing window
 * - Complete isolation from other contexts
 * - Separate cookies, cache, localStorage, sessionStorage
 * - Independent browser state
 * 
 * 2. Why Use Contexts?
 * - MUCH faster than launching multiple browsers
 * - Simulate multiple users simultaneously
 * - Parallel test execution
 * - Different device configurations
 * - Different permissions/settings
 * 
 * 3. Context vs Browser:
 * - One Browser -> Multiple Contexts (fast)
 * - Multiple Browsers -> Expensive (slow)
 * - Use contexts whenever possible!
 * 
 * 4. Real-World Use Cases:
 * - Multi-user testing (admin, customer, merchant)
 * - Different device types (desktop, mobile, tablet)
 * - Different permissions (location, notifications)
 * - Different locales/timezones
 * - Parallel test execution
 * 
 * 5. Context Options:
 * - viewport: Screen size
 * - userAgent: Browser identification
 * - permissions: Geolocation, camera, etc.
 * - geolocation: GPS coordinates
 * - locale: Language/region
 * - timezoneId: Timezone
 * - storageState: Save/restore login state
 * 
 * PLAYWRIGHT vs SELENIUM:
 * - Playwright: Built-in context isolation ✅
 * - Selenium: No native context concept ❌
 * - Playwright: Fast parallel execution ✅
 * - Selenium: Slower with multiple browsers ⚠️
 * 
 * BANKING USE CASE:
 * Test scenario: Admin approves transaction, customer receives it
 * 
 * Context 1 (Admin):
 * - Login as admin
 * - Approve pending transaction
 * 
 * Context 2 (Customer):
 * - Login as customer
 * - Verify transaction received
 * 
 * Both in same browser, complete isolation, super fast!
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test03_BrowserContext
 * mvn test -Dtest=Test03_BrowserContext#test02_MultipleContextsIsolation
 */
