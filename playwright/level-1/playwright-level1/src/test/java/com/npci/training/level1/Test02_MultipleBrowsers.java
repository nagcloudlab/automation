package com.npci.training.level1;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Playwright Level 1 - Test 02: Multiple Browsers
 * 
 * Topics Covered:
 * - Chromium browser
 * - Firefox browser
 * - WebKit browser (Safari engine)
 * - Cross-browser testing
 * - Browser detection
 * 
 * Duration: 25 minutes
 */
@DisplayName("Multiple Browsers Testing")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test02_MultipleBrowsers {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: Chromium browser")
    public void test01_ChromiumBrowser() {
        System.out.println("\n=== Test 01: Chromium Browser ===");
        
        Playwright playwright = Playwright.create();
        
        System.out.println("Launching Chromium browser...");
        Browser browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
        );
        
        Page page = browser.newPage();
        page.navigate("https://www.google.com");
        
        System.out.println("✓ Browser Type: Chromium");
        System.out.println("✓ Page Title: " + page.title());
        System.out.println("✓ User Agent: " + page.evaluate("() => navigator.userAgent"));
        
        assertTrue(page.title().contains("Google"));
        
        browser.close();
        playwright.close();
        
        System.out.println("✓ Chromium test completed!\n");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: Firefox browser")
    public void test02_FirefoxBrowser() {
        System.out.println("\n=== Test 02: Firefox Browser ===");
        
        Playwright playwright = Playwright.create();
        
        System.out.println("Launching Firefox browser...");
        Browser browser = playwright.firefox().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
        );
        
        Page page = browser.newPage();
        page.navigate("https://www.google.com");
        
        System.out.println("✓ Browser Type: Firefox");
        System.out.println("✓ Page Title: " + page.title());
        System.out.println("✓ User Agent: " + page.evaluate("() => navigator.userAgent"));
        
        assertTrue(page.title().contains("Google"));
        
        browser.close();
        playwright.close();
        
        System.out.println("✓ Firefox test completed!\n");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: WebKit browser (Safari)")
    public void test03_WebKitBrowser() {
        System.out.println("\n=== Test 03: WebKit Browser ===");
        
        Playwright playwright = Playwright.create();
        
        System.out.println("Launching WebKit browser (Safari engine)...");
        Browser browser = playwright.webkit().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
        );
        
        Page page = browser.newPage();
        page.navigate("https://www.google.com");
        
        System.out.println("✓ Browser Type: WebKit (Safari)");
        System.out.println("✓ Page Title: " + page.title());
        System.out.println("✓ User Agent: " + page.evaluate("() => navigator.userAgent"));
        
        assertTrue(page.title().contains("Google"));
        
        browser.close();
        playwright.close();
        
        System.out.println("✓ WebKit test completed!\n");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: Run same test on all browsers")
    public void test04_CrossBrowserTesting() {
        System.out.println("\n=== Test 04: Cross-Browser Testing ===");
        
        Playwright playwright = Playwright.create();
        
        // List of browser types to test
        List<BrowserType> browserTypes = Arrays.asList(
            playwright.chromium(),
            playwright.firefox(),
            playwright.webkit()
        );
        
        String[] browserNames = {"Chromium", "Firefox", "WebKit"};
        
        for (int i = 0; i < browserTypes.size(); i++) {
            BrowserType browserType = browserTypes.get(i);
            String browserName = browserNames[i];
            
            System.out.println("\n--- Testing on " + browserName + " ---");
            
            Browser browser = browserType.launch(
                new BrowserType.LaunchOptions().setHeadless(true) // Headless for speed
            );
            
            Page page = browser.newPage();
            page.navigate("https://www.google.com");
            
            // Verify title on all browsers
            String title = page.title();
            System.out.println("✓ " + browserName + " - Title: " + title);
            assertTrue(title.contains("Google"), 
                browserName + " should display Google");
            
            // Verify search box exists on all browsers
            boolean searchBoxExists = page.locator("textarea[name='q']").count() > 0 ||
                                     page.locator("input[name='q']").count() > 0;
            System.out.println("✓ " + browserName + " - Search box exists: " + searchBoxExists);
            assertTrue(searchBoxExists, 
                browserName + " should have search box");
            
            browser.close();
        }
        
        playwright.close();
        
        System.out.println("\n✓ Cross-browser testing completed!\n");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: Compare browser rendering")
    public void test05_CompareBrowserRendering() {
        System.out.println("\n=== Test 05: Browser Rendering Comparison ===");
        
        Playwright playwright = Playwright.create();
        String testUrl = "https://example.com";
        
        // Test on Chromium
        System.out.println("\n1. Testing on Chromium...");
        Browser chromium = playwright.chromium().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
        );
        Page chromiumPage = chromium.newPage();
        chromiumPage.navigate(testUrl);
        
        String chromiumText = chromiumPage.locator("h1").textContent();
        System.out.println("✓ Chromium - H1 text: " + chromiumText);
        
        chromiumPage.waitForTimeout(1000);
        chromium.close();
        
        // Test on Firefox
        System.out.println("\n2. Testing on Firefox...");
        Browser firefox = playwright.firefox().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
        );
        Page firefoxPage = firefox.newPage();
        firefoxPage.navigate(testUrl);
        
        String firefoxText = firefoxPage.locator("h1").textContent();
        System.out.println("✓ Firefox - H1 text: " + firefoxText);
        
        firefoxPage.waitForTimeout(1000);
        firefox.close();
        
        // Test on WebKit
        System.out.println("\n3. Testing on WebKit...");
        Browser webkit = playwright.webkit().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
        );
        Page webkitPage = webkit.newPage();
        webkitPage.navigate(testUrl);
        
        String webkitText = webkitPage.locator("h1").textContent();
        System.out.println("✓ WebKit - H1 text: " + webkitText);
        
        webkitPage.waitForTimeout(1000);
        webkit.close();
        
        // Verify all browsers show same content
        System.out.println("\n4. Comparing results...");
        assertEquals(chromiumText, firefoxText, 
            "Chromium and Firefox should show same text");
        assertEquals(chromiumText, webkitText, 
            "Chromium and WebKit should show same text");
        
        System.out.println("✓ All browsers render content consistently!");
        
        playwright.close();
        
        System.out.println("✓ Browser rendering comparison completed!\n");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: Browser-specific features")
    public void test06_BrowserSpecificFeatures() {
        System.out.println("\n=== Test 06: Browser-Specific Features ===");
        
        Playwright playwright = Playwright.create();
        
        // Chromium with devtools
        System.out.println("\n1. Chromium with DevTools...");
        Browser chromium = playwright.chromium().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setDevtools(true) // Open DevTools (Chromium only)
        );
        Page chromiumPage = chromium.newPage();
        chromiumPage.navigate("https://www.google.com");
        System.out.println("✓ Chromium launched with DevTools");
        
        chromiumPage.waitForTimeout(2000);
        chromium.close();
        
        // Firefox with custom preferences
        System.out.println("\n2. Firefox with custom preferences...");
        Browser firefox = playwright.firefox().launch(
            new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setFirefoxUserPrefs(java.util.Map.of(
                    "browser.cache.disk.enable", false,
                    "browser.cache.memory.enable", false
                ))
        );
        Page firefoxPage = firefox.newPage();
        firefoxPage.navigate("https://www.google.com");
        System.out.println("✓ Firefox launched with custom preferences");
        
        firefoxPage.waitForTimeout(2000);
        firefox.close();
        
        // WebKit
        System.out.println("\n3. WebKit (Safari engine)...");
        Browser webkit = playwright.webkit().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
        );
        Page webkitPage = webkit.newPage();
        webkitPage.navigate("https://www.google.com");
        System.out.println("✓ WebKit launched successfully");
        
        webkitPage.waitForTimeout(2000);
        webkit.close();
        
        playwright.close();
        
        System.out.println("\n✓ Browser-specific features test completed!\n");
    }
}

/*
 * PLAYWRIGHT BROWSERS:
 * 
 * 1. Chromium:
 *    - playwright.chromium().launch()
 *    - Google Chrome, Microsoft Edge
 *    - Most popular for testing
 *    - Best DevTools support
 * 
 * 2. Firefox:
 *    - playwright.firefox().launch()
 *    - Mozilla Firefox
 *    - Second most popular
 *    - Good for cross-browser testing
 * 
 * 3. WebKit:
 *    - playwright.webkit().launch()
 *    - Safari engine
 *    - Important for iOS/macOS testing
 *    - Unique rendering engine
 * 
 * BROWSER INSTALLATION:
 * After adding Playwright dependency, install browsers:
 * 
 * mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI \
 *   -Dexec.args="install"
 * 
 * Or install specific browser:
 * mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI \
 *   -Dexec.args="install chromium"
 * 
 * CROSS-BROWSER TESTING BENEFITS:
 * - Catch browser-specific bugs
 * - Ensure consistent user experience
 * - Test on different rendering engines
 * - Validate CSS/JavaScript compatibility
 * 
 * PLAYWRIGHT vs SELENIUM:
 * - Playwright: Built-in WebKit support ✅
 * - Selenium: Safari support requires separate driver ⚠️
 * - Playwright: Single installation ✅
 * - Selenium: Different drivers per browser ⚠️
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test02_MultipleBrowsers
 * mvn test -Dtest=Test02_MultipleBrowsers#test04_CrossBrowserTesting
 */
