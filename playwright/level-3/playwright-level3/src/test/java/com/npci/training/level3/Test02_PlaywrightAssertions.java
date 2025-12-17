package com.npci.training.level3;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Playwright Level 3 - Test 02: Playwright Assertions
 * 
 * Topics Covered:
 * - assertThat() API
 * - Auto-retrying assertions
 * - Locator assertions
 * - Page assertions
 * - Why assertThat vs JUnit assertions
 * 
 * Duration: 30 minutes
 * 
 * KEY CONCEPT:
 * Playwright assertions auto-retry until timeout:
 * - Retry every 50ms
 * - Up to 5 seconds (default)
 * - No flaky tests!
 */
@DisplayName("Playwright Assertions (assertThat)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test02_PlaywrightAssertions extends BaseTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: Basic assertThat usage")
    public void test01_BasicAssertThat() {
        System.out.println("\n=== Test 01: Basic assertThat ===");
        
        page.navigate("https://the-internet.herokuapp.com/");
        
        // Assert element is visible
        System.out.println("1. assertThat(locator).isVisible()");
        assertThat(page.getByRole(AriaRole.HEADING))
            .isVisible();
        System.out.println("✓ Heading is visible");
        
        // Assert text content
        System.out.println("2. assertThat(locator).hasText()");
        assertThat(page.getByRole(AriaRole.HEADING))
            .hasText("Welcome to the-internet");
        System.out.println("✓ Heading has correct text");
        
        // Assert element count
        System.out.println("3. assertThat(locator).hasCount()");
        assertThat(page.locator("a"))
            .hasCount(44);
        System.out.println("✓ Found 44 links");
        
        System.out.println("\n💡 assertThat() benefits:");
        System.out.println("  ✅ Auto-retries (no flaky tests!)");
        System.out.println("  ✅ Better error messages");
        System.out.println("  ✅ Wait-free assertions");
        
        System.out.println("\n✓ Basic assertThat test passed!\n");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: Auto-retrying assertions")
    public void test02_AutoRetryingAssertions() {
        System.out.println("\n=== Test 02: Auto-retrying Assertions ===");
        
        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");
        
        // Click start
        page.getByRole(AriaRole.BUTTON).click();
        
        // This assertion auto-retries until element is visible!
        System.out.println("1. Auto-retrying assertion...");
        long startTime = System.currentTimeMillis();
        
        assertThat(page.locator("#finish"))
            .isVisible();
        
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("✓ Element became visible after " + duration + "ms");
        System.out.println("✓ Assertion auto-retried until condition met!");
        
        System.out.println("\n💡 How it works:");
        System.out.println("""
            
            assertThat(locator).isVisible()
            
            Playwright retries this assertion:
            - Check 1 (0ms): Not visible → Retry
            - Check 2 (50ms): Not visible → Retry
            - Check 3 (100ms): Not visible → Retry
            - ...
            - Check N: VISIBLE! → Pass ✅
            
            No manual wait needed!
            No flaky tests!
            """);
        
        System.out.println("✓ Auto-retry test passed!\n");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: assertThat vs JUnit assertions")
    public void test03_AssertThatVsJUnit() {
        System.out.println("\n=== Test 03: assertThat vs JUnit ===");
        
        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/2");
        page.getByRole(AriaRole.BUTTON).click();
        
        System.out.println("\n❌ WRONG: JUnit assertion (flaky!)");
        System.out.println("""
            
            // This will fail! Element not visible yet!
            String text = page.locator("#finish h4").textContent();
            assertEquals("Hello World!", text);
            
            // Need manual wait:
            page.waitForSelector("#finish");
            String text = page.locator("#finish h4").textContent();
            assertEquals("Hello World!", text);
            """);
        
        System.out.println("\n✅ CORRECT: Playwright assertion (auto-retry!)");
        System.out.println("""
            
            // This auto-retries! No manual wait!
            assertThat(page.locator("#finish h4"))
                .hasText("Hello World!");
            
            Simple, reliable, no flakiness!
            """);
        
        // Demonstrate correct way
        assertThat(page.locator("#finish h4"))
            .containsText("Hello World");
        System.out.println("✓ Assertion passed (auto-retried)!");
        
        System.out.println("\n📊 COMPARISON:");
        System.out.println("""
            
            JUnit Assertions:
            ❌ No auto-retry
            ❌ Requires manual waits
            ❌ Flaky tests
            ❌ Generic error messages
            
            Playwright Assertions:
            ✅ Auto-retry (5s default)
            ✅ No manual waits
            ✅ Reliable tests
            ✅ Detailed error messages
            
            Use Playwright assertions for web elements!
            Use JUnit assertions for business logic!
            """);
        
        System.out.println("✓ Comparison test passed!\n");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: Locator assertions - Visibility")
    public void test04_LocatorAssertionsVisibility() {
        System.out.println("\n=== Test 04: Visibility Assertions ===");
        
        page.navigate("https://the-internet.herokuapp.com/");
        
        // isVisible()
        System.out.println("1. assertThat(locator).isVisible()");
        assertThat(page.getByRole(AriaRole.HEADING))
            .isVisible();
        System.out.println("✓ Element is visible");
        
        // isHidden()
        System.out.println("2. assertThat(locator).isHidden()");
        assertThat(page.locator("#non-existent"))
            .isHidden();
        System.out.println("✓ Element is hidden");
        
        // not().isVisible() - Negation
        System.out.println("3. assertThat(locator).not().isVisible()");
        assertThat(page.locator("#non-existent"))
            .not()
            .isVisible();
        System.out.println("✓ Element is NOT visible");
        
        System.out.println("\n✓ Visibility assertions passed!\n");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: Locator assertions - Text")
    public void test05_LocatorAssertionsText() {
        System.out.println("\n=== Test 05: Text Assertions ===");
        
        page.navigate("https://the-internet.herokuapp.com/");
        
        Locator heading = page.getByRole(AriaRole.HEADING);
        
        // hasText() - Exact match
        System.out.println("1. assertThat(locator).hasText('exact')");
        assertThat(heading)
            .hasText("Welcome to the-internet");
        System.out.println("✓ Exact text match");
        
        // containsText() - Partial match
        System.out.println("2. assertThat(locator).containsText('partial')");
        assertThat(heading)
            .containsText("Welcome");
        System.out.println("✓ Contains text");
        
        // hasText with regex
        System.out.println("3. assertThat(locator).hasText(Pattern)");
        assertThat(heading)
            .hasText(java.util.regex.Pattern.compile("Welcome.*internet"));
        System.out.println("✓ Regex text match");
        
        System.out.println("\n✓ Text assertions passed!\n");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: Locator assertions - Attributes")
    public void test06_LocatorAssertionsAttributes() {
        System.out.println("\n=== Test 06: Attribute Assertions ===");
        
        page.navigate("https://the-internet.herokuapp.com/login");
        
        Locator usernameInput = page.locator("#username");
        
        // hasAttribute()
        System.out.println("1. assertThat(locator).hasAttribute(name, value)");
        assertThat(usernameInput)
            .hasAttribute("type", "text");
        System.out.println("✓ Has correct attribute");
        
        // hasId()
        System.out.println("2. assertThat(locator).hasId()");
        assertThat(usernameInput)
            .hasId("username");
        System.out.println("✓ Has correct ID");
        
        // hasClass()
        System.out.println("3. assertThat(locator).hasClass()");
        assertThat(page.locator("button"))
            .hasClass(java.util.regex.Pattern.compile(".*radius.*"));
        System.out.println("✓ Has correct class");
        
        System.out.println("\n✓ Attribute assertions passed!\n");
    }
    
    @Test
    @Order(7)
    @DisplayName("Test 7: Locator assertions - State")
    public void test07_LocatorAssertionsState() {
        System.out.println("\n=== Test 07: State Assertions ===");
        
        page.navigate("https://the-internet.herokuapp.com/checkboxes");
        
        Locator firstCheckbox = page.locator("input[type='checkbox']").first();
        Locator secondCheckbox = page.locator("input[type='checkbox']").nth(1);
        
        // isChecked()
        System.out.println("1. assertThat(locator).isChecked()");
        secondCheckbox.check();
        assertThat(secondCheckbox)
            .isChecked();
        System.out.println("✓ Checkbox is checked");
        
        // not().isChecked()
        System.out.println("2. assertThat(locator).not().isChecked()");
        firstCheckbox.uncheck();
        assertThat(firstCheckbox)
            .not()
            .isChecked();
        System.out.println("✓ Checkbox is NOT checked");
        
        // isEnabled()
        System.out.println("3. assertThat(locator).isEnabled()");
        assertThat(firstCheckbox)
            .isEnabled();
        System.out.println("✓ Element is enabled");
        
        // isDisabled()
        System.out.println("4. assertThat(locator).isDisabled()");
        System.out.println("  (Would check disabled state)");
        
        // isEditable()
        System.out.println("5. assertThat(locator).isEditable()");
        assertThat(firstCheckbox)
            .isEditable();
        System.out.println("✓ Element is editable");
        
        System.out.println("\n✓ State assertions passed!\n");
    }
    
    @Test
    @Order(8)
    @DisplayName("Test 8: Page assertions")
    public void test08_PageAssertions() {
        System.out.println("\n=== Test 08: Page Assertions ===");
        
        page.navigate("https://the-internet.herokuapp.com/");
        
        // hasTitle()
        System.out.println("1. assertThat(page).hasTitle()");
        assertThat(page)
            .hasTitle("The Internet");
        System.out.println("✓ Page has correct title");
        
        // hasURL()
        System.out.println("2. assertThat(page).hasURL()");
        assertThat(page)
            .hasURL(java.util.regex.Pattern.compile(".*herokuapp.*"));
        System.out.println("✓ Page has correct URL");
        
        System.out.println("\n✓ Page assertions passed!\n");
    }
    
    @Test
    @Order(9)
    @DisplayName("Test 9: Banking portal assertions example")
    public void test09_BankingExample() {
        System.out.println("\n=== Test 09: Banking Portal Assertions ===");
        
        System.out.println("\n💰 REAL-WORLD BANKING ASSERTIONS:");
        System.out.println("""
            
            // After login - verify dashboard
            assertThat(page)
                .hasURL(Pattern.compile(".*/dashboard"));
            
            assertThat(page.getByRole(AriaRole.HEADING, 
                setName("Dashboard")))
                .isVisible();
            
            // Verify account balance displayed
            assertThat(page.getByTestId("account-balance"))
                .isVisible();
            
            assertThat(page.getByTestId("account-balance"))
                .containsText("₹");
            
            // Verify transfer button enabled
            assertThat(page.getByRole(AriaRole.BUTTON, 
                setName("Transfer Money")))
                .isEnabled();
            
            // After transfer - verify success
            assertThat(page.getByText("Transfer successful"))
                .isVisible();
            
            // Verify transaction ID displayed
            assertThat(page.getByTestId("txn-id"))
                .hasAttribute("data-status", "success");
            
            // Verify transaction appears in list
            assertThat(page.locator(".transaction-item")
                .filter(hasText("Rent payment")))
                .isVisible();
            
            // Verify updated balance
            assertThat(page.getByTestId("account-balance"))
                .containsText("95,000");  // 1,00,000 - 5,000
            
            All assertions auto-retry!
            No manual waits!
            No flaky tests! 🎉
            """);
        
        System.out.println("✓ Banking example completed!\n");
    }
    
    @Test
    @Order(10)
    @DisplayName("Test 10: All Playwright assertions summary")
    public void test10_AllAssertionsSummary() {
        System.out.println("\n=== Test 10: Assertions Summary ===");
        
        System.out.println("\n📚 ALL PLAYWRIGHT ASSERTIONS:");
        System.out.println("""
            
            VISIBILITY:
            ✅ assertThat(locator).isVisible()
            ✅ assertThat(locator).isHidden()
            ✅ assertThat(locator).not().isVisible()
            
            TEXT:
            ✅ assertThat(locator).hasText("exact")
            ✅ assertThat(locator).containsText("partial")
            ✅ assertThat(locator).hasText(Pattern.compile("regex"))
            
            ATTRIBUTES:
            ✅ assertThat(locator).hasAttribute(name, value)
            ✅ assertThat(locator).hasClass("class-name")
            ✅ assertThat(locator).hasClass(Pattern.compile("regex"))
            ✅ assertThat(locator).hasId("id")
            
            VALUE:
            ✅ assertThat(locator).hasValue("text")
            ✅ assertThat(locator).hasValues(["opt1", "opt2"])
            
            STATE:
            ✅ assertThat(locator).isChecked()
            ✅ assertThat(locator).isEnabled()
            ✅ assertThat(locator).isDisabled()
            ✅ assertThat(locator).isEditable()
            ✅ assertThat(locator).isFocused()
            
            COUNT:
            ✅ assertThat(locator).hasCount(5)
            
            PAGE:
            ✅ assertThat(page).hasTitle("title")
            ✅ assertThat(page).hasURL("url")
            ✅ assertThat(page).hasURL(Pattern.compile("regex"))
            
            NEGATION:
            ✅ assertThat(locator).not().isVisible()
            ✅ assertThat(locator).not().hasText("text")
            ✅ assertThat(locator).not().isChecked()
            
            All assertions:
            - Auto-retry (5s default)
            - Better error messages
            - No manual waits needed
            
            Use for: Web elements, pages
            Don't use for: Business logic (use JUnit)
            """);
        
        System.out.println("✓ Summary completed!\n");
    }
}

/*
 * PLAYWRIGHT ASSERTIONS REFERENCE:
 * 
 * 1. WHY PLAYWRIGHT ASSERTIONS?
 *    ✅ Auto-retry until timeout
 *    ✅ No flaky tests
 *    ✅ Better error messages
 *    ✅ No manual waits
 * 
 * 2. RETRY MECHANISM:
 *    - Default timeout: 5 seconds
 *    - Retry interval: 50ms
 *    - Stops immediately when condition met
 * 
 * 3. COMMON ASSERTIONS:
 *    assertThat(locator).isVisible()
 *    assertThat(locator).hasText("text")
 *    assertThat(locator).isEnabled()
 *    assertThat(page).hasURL("url")
 * 
 * 4. NEGATION:
 *    assertThat(locator).not().isVisible()
 *    assertThat(locator).not().hasText("text")
 * 
 * 5. WHEN TO USE:
 *    ✅ Web elements (visibility, text, state)
 *    ✅ Page state (URL, title)
 *    ❌ Business logic (use JUnit)
 *    ❌ Non-web assertions (use JUnit)
 * 
 * BEST PRACTICES:
 * ✅ Use assertThat() for web elements
 * ✅ Use JUnit for business logic
 * ✅ Use containsText() for partial matches
 * ✅ Use hasText() for exact matches
 * ✅ Use not() for negative assertions
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test02_PlaywrightAssertions
 */
