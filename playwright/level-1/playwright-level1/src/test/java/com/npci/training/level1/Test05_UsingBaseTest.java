package com.npci.training.level1;

import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Playwright Level 1 - Test 05: Using BaseTest
 * 
 * Topics Covered:
 * - Extending BaseTest
 * - Using provided page, browser, context
 * - No manual setup/teardown needed
 * - Focus on test logic only
 * 
 * Duration: 15 minutes
 * 
 * NOTE: This class extends BaseTest, so:
 * - playwright, browser, context, page are already available
 * - No need to create/close them manually
 * - Each test gets fresh context and page
 */
@DisplayName("Using BaseTest for Clean Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test05_UsingBaseTest extends BaseTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: Simple navigation test")
    public void test01_SimpleNavigation() {
        System.out.println("\n=== Test 01: Simple Navigation ===");
        
        // page is already available from BaseTest!
        page.navigate("https://example.com");
        
        String title = page.title();
        System.out.println("✓ Page title: " + title);
        
        assertTrue(title.contains("Example"));
        
        System.out.println("✓ Test completed - no cleanup needed!\n");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: Form interaction test")
    public void test02_FormInteraction() {
        System.out.println("\n=== Test 02: Form Interaction ===");
        
        page.navigate("https://the-internet.herokuapp.com/login");
        
        // Fill form
        page.fill("#username", "tomsmith");
        page.fill("#password", "SuperSecretPassword!");
        page.click("button[type='submit']");
        
        // Verify
        page.waitForURL("**/secure");
        assertTrue(page.url().contains("/secure"));
        
        System.out.println("✓ Login successful");
        System.out.println("✓ Test completed - automatic cleanup!\n");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: Multiple test isolation")
    public void test03_TestIsolation() {
        System.out.println("\n=== Test 03: Test Isolation ===");
        
        page.navigate("https://example.com");
        
        // Set some data in localStorage
        page.evaluate("() => localStorage.setItem('test', 'value1')");
        String value = (String) page.evaluate("() => localStorage.getItem('test')");
        
        System.out.println("✓ Set localStorage: test=" + value);
        assertEquals("value1", value);
        
        System.out.println("✓ This data will NOT affect other tests!");
        System.out.println("✓ Each test gets fresh context!\n");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: Verify isolation from previous test")
    public void test04_VerifyIsolation() {
        System.out.println("\n=== Test 04: Verify Isolation ===");
        
        page.navigate("https://example.com");
        
        // Try to get data from previous test
        String value = (String) page.evaluate("() => localStorage.getItem('test')");
        
        System.out.println("✓ localStorage value: " + value);
        assertNull(value, "Should be null - tests are isolated!");
        
        System.out.println("✓ Perfect! No data from previous test!");
        System.out.println("✓ Test isolation verified!\n");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: Using browser context from BaseTest")
    public void test05_UsingBrowserContext() {
        System.out.println("\n=== Test 05: Using Browser Context ===");
        
        page.navigate("https://example.com");
        
        // Set a cookie using context (available from BaseTest)
        context.addCookies(java.util.Arrays.asList(
            new com.microsoft.playwright.options.Cookie("user", "testuser")
                .setDomain("example.com")
                .setPath("/")
        ));
        
        System.out.println("✓ Cookie set via context");
        
        // Verify cookie
        var cookies = context.cookies();
        System.out.println("✓ Cookies in context: " + cookies.size());
        
        assertFalse(cookies.isEmpty(), "Should have cookies");
        
        System.out.println("✓ Context operations work perfectly!\n");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: Creating additional pages")
    public void test06_AdditionalPages() {
        System.out.println("\n=== Test 06: Additional Pages ===");
        
        // Use the default page from BaseTest
        page.navigate("https://example.com");
        System.out.println("✓ Page 1 (from BaseTest): " + page.url());
        
        // Create additional page in same context
        var page2 = context.newPage();
        page2.navigate("https://www.google.com");
        System.out.println("✓ Page 2 (created): " + page2.url());
        
        // Both pages in same context, share cookies/storage
        System.out.println("✓ Both pages share same context");
        
        // Close additional page (BaseTest page auto-closes)
        page2.close();
        System.out.println("✓ Additional page closed");
        
        // Original page still works
        page.reload();
        System.out.println("✓ Original page still active!\n");
    }
}

/*
 * BENEFITS OF USING BASETEST:
 * 
 * 1. Clean Test Code:
 *    - No setup/teardown boilerplate
 *    - Focus on test logic only
 *    - More readable tests
 * 
 * 2. Automatic Resource Management:
 *    - Playwright created once
 *    - Browser created once
 *    - Context per test (isolation)
 *    - All cleaned up automatically
 * 
 * 3. Test Isolation:
 *    - Fresh context for each test
 *    - No shared state between tests
 *    - Tests can run in any order
 *    - Tests can run in parallel
 * 
 * 4. Consistency:
 *    - Same setup for all tests
 *    - Same browser configuration
 *    - Easier to maintain
 * 
 * 5. Flexibility:
 *    - Can override setup methods
 *    - Can add custom configuration
 *    - Can create additional pages
 *    - Can access browser, context, page
 * 
 * AVAILABLE INSTANCES:
 * - playwright: Playwright instance (static)
 * - browser: Browser instance (static)
 * - context: BrowserContext (per test)
 * - page: Page (per test)
 * 
 * LIFECYCLE:
 * Suite Start → Create Playwright & Browser (once)
 *   ↓
 * Test 1 Start → Create Context & Page
 *   ↓
 * Test 1 Logic → Use page, context, browser
 *   ↓
 * Test 1 End → Close Context (auto cleanup)
 *   ↓
 * Test 2 Start → Create NEW Context & Page (fresh!)
 *   ↓
 * ... and so on ...
 *   ↓
 * Suite End → Close Browser & Playwright (once)
 * 
 * CUSTOMIZING BASETEST:
 * 
 * @BeforeEach
 * @Override
 * public void setUp(TestInfo testInfo) {
 *     super.setUp(testInfo);
 *     // Your custom setup
 *     page.setViewportSize(1024, 768);
 * }
 * 
 * REAL-WORLD PATTERN:
 * This is the standard pattern for Playwright test automation:
 * - One BaseTest for entire project
 * - All test classes extend BaseTest
 * - Consistent, clean, maintainable
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test05_UsingBaseTest
 * 
 * Notice: No setup/teardown code in any test!
 * Everything is handled by BaseTest automatically.
 */
