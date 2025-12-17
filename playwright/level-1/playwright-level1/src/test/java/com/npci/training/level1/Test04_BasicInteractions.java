package com.npci.training.level1;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.ViewportSize;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Playwright Level 1 - Test 04: Basic Interactions
 * 
 * Topics Covered:
 * - Click actions
 * - Typing text (type vs fill)
 * - Navigation methods
 * - Page reload
 * - Go back/forward
 * - Basic element interactions
 * 
 * Duration: 25 minutes
 */
@DisplayName("Basic Page Interactions")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test04_BasicInteractions {

    @Test
    @Order(1)
    @DisplayName("Test 1: Click interactions")
    public void test01_ClickInteractions() {
        System.out.println("\n=== Test 01: Click Interactions ===");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
        Page page = browser.newPage();

        // Navigate to example page
        page.navigate("https://the-internet.herokuapp.com/");
        System.out.println("✓ Navigated to test page");

        // Click on a link
        System.out.println("Clicking 'A/B Testing' link...");
        page.click("text=A/B Testing");

        // Verify navigation
        assertTrue(page.url().contains("abtest"));
        System.out.println("✓ Link clicked successfully");
        System.out.println("✓ Current URL: " + page.url());

        browser.close();
        playwright.close();

        System.out.println("✓ Click test completed!\n");
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Type vs Fill")
    public void test02_TypeVsFill() {
        System.out.println("\n=== Test 02: Type vs Fill ===");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(300));
        Page page = browser.newPage();

        page.navigate("https://the-internet.herokuapp.com/login");
        System.out.println("✓ Navigated to login page");

        // Method 1: type() - Types character by character (slower, more realistic)
        System.out.println("\n--- Method 1: type() ---");
        page.locator("#username").type("tomsmith", new Locator.TypeOptions().setDelay(100));
        System.out.println("✓ Typed username character by character");

        // Clear the field
        page.locator("#username").clear();

        // Method 2: fill() - Sets value directly (faster, recommended)
        System.out.println("\n--- Method 2: fill() ---");
        page.locator("#username").fill("tomsmith");
        System.out.println("✓ Filled username instantly");

        // Fill password
        page.locator("#password").fill("SuperSecretPassword!");
        System.out.println("✓ Filled password");

        // Verify values
        String username = page.locator("#username").inputValue();
        System.out.println("✓ Username value: " + username);
        assertEquals("tomsmith", username);

        browser.close();
        playwright.close();

        System.out.println("✓ Type vs Fill test completed!\n");
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Navigation methods")
    public void test03_NavigationMethods() {
        System.out.println("\n=== Test 03: Navigation Methods ===");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        // Method 1: navigate() - Standard navigation
        System.out.println("\n1. navigate() method:");
        page.navigate("https://example.com");
        System.out.println("✓ Navigated to: " + page.url());

        // Navigate to another page
        page.navigate("https://www.google.com");
        System.out.println("✓ Navigated to: " + page.url());

        // Method 2: goBack()
        System.out.println("\n2. goBack() method:");
        page.goBack();
        System.out.println("✓ Went back to: " + page.url());
        assertTrue(page.url().contains("example.com"));

        // Method 3: goForward()
        System.out.println("\n3. goForward() method:");
        page.goForward();
        System.out.println("✓ Went forward to: " + page.url());
        assertTrue(page.url().contains("google.com"));

        // Method 4: reload()
        System.out.println("\n4. reload() method:");
        page.reload();
        System.out.println("✓ Page reloaded: " + page.url());

        browser.close();
        playwright.close();

        System.out.println("✓ Navigation methods test completed!\n");
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Basic form interactions")
    public void test04_FormInteractions() {
        System.out.println("\n=== Test 04: Form Interactions ===");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
        Page page = browser.newPage();

        page.navigate("https://the-internet.herokuapp.com/login");
        System.out.println("✓ Navigated to login form");

        // Fill username
        System.out.println("\n1. Filling username...");
        page.locator("#username").fill("tomsmith");
        System.out.println("✓ Username filled");

        // Fill password
        System.out.println("\n2. Filling password...");
        page.locator("#password").fill("SuperSecretPassword!");
        System.out.println("✓ Password filled");

        // Click login button
        System.out.println("\n3. Clicking login button...");
        page.click("button[type='submit']");
        System.out.println("✓ Login button clicked");

        // Verify successful login
        page.waitForURL("**/secure");
        System.out.println("\n4. Verifying login success...");
        assertTrue(page.url().contains("/secure"));

        boolean isSuccessMessageVisible = page.locator(".flash.success").isVisible();
        System.out.println("✓ Success message visible: " + isSuccessMessageVisible);
        assertTrue(isSuccessMessageVisible);

        String successText = page.locator(".flash.success").textContent();
        System.out.println("✓ Success message: " + successText.trim());

        browser.close();
        playwright.close();

        System.out.println("✓ Form interactions test completed!\n");
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Page properties and methods")
    public void test05_PagePropertiesAndMethods() {
        System.out.println("\n=== Test 05: Page Properties and Methods ===");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://example.com");

        // Page URL
        System.out.println("\n1. Page URL:");
        String url = page.url();
        System.out.println("✓ URL: " + url);

        // Page Title
        System.out.println("\n2. Page Title:");
        String title = page.title();
        System.out.println("✓ Title: " + title);

        // Page Content
        System.out.println("\n3. Page Content (HTML):");
        String content = page.content();
        System.out.println("✓ HTML length: " + content.length() + " characters");
        assertTrue(content.contains("<html"), "Should contain HTML");

        // Viewport Size
        System.out.println("\n4. Viewport Size:");
        ViewportSize viewport = page.viewportSize();
        System.out.println("✓ Viewport: " + viewport.width + "x" + viewport.height);

        // Execute JavaScript
        System.out.println("\n5. Execute JavaScript:");
        Object result = page.evaluate("() => document.title");
        System.out.println("✓ Title from JS: " + result);

        // Get User Agent
        System.out.println("\n6. User Agent:");
        Object userAgent = page.evaluate("() => navigator.userAgent");
        System.out.println("✓ User Agent: " + userAgent);

        browser.close();
        playwright.close();

        System.out.println("✓ Page properties test completed!\n");
    }

    @Test
    @Order(6)
    @DisplayName("Test 6: Wait methods")
    public void test06_WaitMethods() {
        System.out.println("\n=== Test 06: Wait Methods ===");

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        Page page = browser.newPage();

        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");
        System.out.println("✓ Navigated to dynamic loading page");

        // Click start button
        System.out.println("\n1. Clicking Start button...");
        page.click("button");

        // Method 1: waitForSelector() - Wait for element to appear
        System.out.println("\n2. Waiting for result to appear...");
        page.waitForSelector("#finish", new Page.WaitForSelectorOptions().setTimeout(10000));
        System.out.println("✓ Result appeared!");

        // Verify result
        String resultText = page.locator("#finish h4").textContent();
        System.out.println("✓ Result text: " + resultText);
        assertTrue(resultText.contains("Hello World"));

        // Method 2: waitForTimeout() - Hard wait (use sparingly!)
        System.out.println("\n3. Hard wait for 1 second...");
        page.waitForTimeout(1000);
        System.out.println("✓ Wait completed");

        // Method 3: waitForLoadState()
        System.out.println("\n4. Navigating and waiting for load state...");
        page.navigate("https://example.com");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        System.out.println("✓ Page fully loaded (network idle)");

        browser.close();
        playwright.close();

        System.out.println("✓ Wait methods test completed!\n");
    }
}

/*
 * PLAYWRIGHT BASIC INTERACTIONS:
 * 
 * 1. Click Actions:
 * page.click("selector") - Click element
 * page.dblclick("selector") - Double click
 * page.click("selector", new Page.ClickOptions().setButton(MouseButton.RIGHT))
 * - Right click
 * 
 * 2. Text Input:
 * page.fill("selector", "text") - Instant fill (recommended, fast)
 * page.type("selector", "text") - Character by character (slower, realistic)
 * page.locator("selector").clear() - Clear input field
 * 
 * 3. Navigation:
 * page.navigate(url) - Go to URL
 * page.goBack() - Browser back button
 * page.goForward() - Browser forward button
 * page.reload() - Refresh page
 * 
 * 4. Page Information:
 * page.url() - Get current URL
 * page.title() - Get page title
 * page.content() - Get HTML content
 * page.viewportSize() - Get viewport dimensions
 * 
 * 5. Wait Methods:
 * page.waitForSelector(selector) - Wait for element
 * page.waitForURL(pattern) - Wait for URL change
 * page.waitForLoadState() - Wait for page load
 * page.waitForTimeout(ms) - Hard wait (avoid!)
 * 
 * 6. JavaScript Execution:
 * page.evaluate(script) - Execute JS in page context
 * page.evaluateHandle(script) - Execute and return handle
 * 
 * PLAYWRIGHT AUTO-WAITING:
 * Most Playwright actions auto-wait for elements to be:
 * - Attached to DOM
 * - Visible
 * - Stable (not animating)
 * - Enabled
 * - Not covered by other elements
 * 
 * This is HUGE advantage over Selenium!
 * No need for explicit waits in most cases.
 * 
 * FILL vs TYPE:
 * - fill() - Fast, sets value directly (recommended)
 * - type() - Slow, types character by character (for special cases)
 * 
 * Use fill() for 99% of cases!
 * Use type() only when testing:
 * - Auto-complete behavior
 * - Character-by-character validation
 * - Typing speed dependent features
 * 
 * PLAYWRIGHT vs SELENIUM:
 * - Playwright: Auto-waits for actionability ✅
 * - Selenium: Manual waits everywhere ❌
 * - Playwright: fill() is smart ✅
 * - Selenium: sendKeys() is basic ⚠️
 * - Playwright: Better error messages ✅
 * - Selenium: Generic errors ⚠️
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test04_BasicInteractions
 * mvn test -Dtest=Test04_BasicInteractions#test04_FormInteractions
 */
