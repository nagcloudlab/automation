package com.npci.training.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * BasePage - Parent class for all Page Objects
 * 
 * Provides:
 * - Common page functionality
 * - Navigation helpers
 * - Wait utilities
 * - Assertion helpers
 * 
 * All Page Objects should extend this class
 */
public class BasePage {
    
    protected Page page;
    protected String baseUrl;
    
    /**
     * Constructor
     * @param page Playwright Page instance
     */
    public BasePage(Page page) {
        this.page = page;
        this.baseUrl = "https://example.com"; // Configure as needed
    }
    
    /**
     * Get page title
     * @return Page title
     */
    public String getTitle() {
        return page.title();
    }
    
    /**
     * Get current URL
     * @return Current URL
     */
    public String getUrl() {
        return page.url();
    }
    
    /**
     * Navigate to URL
     * @param url URL to navigate to
     */
    public void navigateTo(String url) {
        page.navigate(url);
    }
    
    /**
     * Wait for page to load
     */
    public void waitForPageLoad() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
    
    /**
     * Reload current page
     */
    public void reload() {
        page.reload();
    }
    
    /**
     * Go back
     */
    public void goBack() {
        page.goBack();
    }
    
    /**
     * Go forward
     */
    public void goForward() {
        page.goForward();
    }
    
    /**
     * Click element by role and name
     * @param role ARIA role
     * @param name Element name
     */
    public void clickByRole(AriaRole role, String name) {
        page.getByRole(role, new Page.GetByRoleOptions().setName(name)).click();
    }
    
    /**
     * Fill input by label
     * @param label Label text
     * @param value Value to fill
     */
    public void fillByLabel(String label, String value) {
        page.getByLabel(label).fill(value);
    }
    
    /**
     * Check if element is visible
     * @param selector Element selector
     * @return true if visible
     */
    public boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }
    
    /**
     * Get text content
     * @param selector Element selector
     * @return Text content
     */
    public String getText(String selector) {
        return page.locator(selector).textContent();
    }
    
    /**
     * Assert element is visible
     * @param selector Element selector
     */
    public void assertVisible(String selector) {
        assertThat(page.locator(selector)).isVisible();
    }
    
    /**
     * Assert text content
     * @param selector Element selector
     * @param expectedText Expected text
     */
    public void assertText(String selector, String expectedText) {
        assertThat(page.locator(selector)).hasText(expectedText);
    }
    
    /**
     * Assert URL contains text
     * @param urlPart Expected URL part
     */
    public void assertUrlContains(String urlPart) {
        assertThat(page).hasURL(java.util.regex.Pattern.compile(".*" + urlPart + ".*"));
    }
    
    /**
     * Take screenshot
     * @param name Screenshot name
     */
    public void takeScreenshot(String name) {
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(java.nio.file.Paths.get("screenshots/" + name + ".png"))
            .setFullPage(true));
    }
    
    /**
     * Execute JavaScript
     * @param script JavaScript code
     * @return Result
     */
    public Object executeScript(String script) {
        return page.evaluate(script);
    }
    
    /**
     * Scroll to element
     * @param selector Element selector
     */
    public void scrollToElement(String selector) {
        page.locator(selector).scrollIntoViewIfNeeded();
    }
    
    /**
     * Wait for timeout (use sparingly!)
     * @param milliseconds Milliseconds to wait
     */
    public void waitFor(int milliseconds) {
        page.waitForTimeout(milliseconds);
    }
}

/*
 * BASE PAGE PATTERN:
 * 
 * 1. PURPOSE:
 *    - Common functionality for all pages
 *    - Reduce code duplication
 *    - Consistent API across pages
 * 
 * 2. WHAT TO INCLUDE:
 *    ✅ Navigation methods
 *    ✅ Common actions (click, fill)
 *    ✅ Wait utilities
 *    ✅ Assertion helpers
 *    ✅ Screenshot utilities
 * 
 * 3. WHAT NOT TO INCLUDE:
 *    ❌ Page-specific locators
 *    ❌ Business logic
 *    ❌ Test assertions (put in tests)
 * 
 * 4. USAGE:
 *    public class LoginPage extends BasePage {
 *        public LoginPage(Page page) {
 *            super(page);
 *        }
 *        // Page-specific methods
 *    }
 * 
 * 5. BENEFITS:
 *    ✅ DRY (Don't Repeat Yourself)
 *    ✅ Maintainable
 *    ✅ Consistent
 *    ✅ Reusable
 */
