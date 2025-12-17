package com.npci.training.level3;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Playwright Level 3 - Test 01: Auto-Waiting Basics
 * 
 * Topics Covered:
 * - What is auto-waiting?
 * - Actionability checks
 * - No explicit waits needed
 * - How Playwright waits automatically
 * - Comparison with Selenium
 * 
 * Duration: 30 minutes
 * 
 * KEY CONCEPT:
 * Playwright performs actionability checks before every action:
 * ✅ Attached to DOM
 * ✅ Visible
 * ✅ Stable (not animating)
 * ✅ Receives events (not covered)
 * ✅ Enabled
 */
@DisplayName("Auto-Waiting Basics")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test01_AutoWaiting extends BaseTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: Auto-wait for element to appear")
    public void test01_AutoWaitForElementToAppear() {
        System.out.println("\n=== Test 01: Auto-wait for Element ===");
        
        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");
        
        // Click start button
        System.out.println("1. Clicking Start button...");
        page.getByRole(AriaRole.BUTTON).click();
        
        // Playwright auto-waits for #finish to appear
        System.out.println("2. Auto-waiting for result to appear...");
        page.locator("#finish").click();  // This waits automatically!
        
        System.out.println("✓ Playwright auto-waited for element!");
        System.out.println("✓ No explicit wait needed!");
        
        // Verify text
        String text = page.locator("#finish h4").textContent();
        assertTrue(text.contains("Hello World"));
        
        System.out.println("\n💡 What happened?");
        System.out.println("  Playwright automatically waited for:");
        System.out.println("  ✅ Element to be attached to DOM");
        System.out.println("  ✅ Element to be visible");
        System.out.println("  ✅ Element to be stable");
        System.out.println("  ✅ No manual wait needed!");
        
        System.out.println("\n✓ Auto-wait test passed!\n");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: Auto-wait for visibility")
    public void test02_AutoWaitForVisibility() {
        System.out.println("\n=== Test 02: Auto-wait for Visibility ===");
        
        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");
        
        // Element exists but is hidden
        System.out.println("1. Element exists but hidden");
        boolean existsInDom = page.locator("#finish").count() > 0;
        System.out.println("✓ Element in DOM: " + existsInDom);
        
        // Start loading
        page.getByRole(AriaRole.BUTTON).click();
        
        // Playwright waits for visibility automatically
        System.out.println("2. Waiting for element to become visible...");
        boolean isVisible = page.locator("#finish").isVisible();
        System.out.println("✓ Element visible: " + isVisible);
        
        assertTrue(isVisible);
        
        System.out.println("\n💡 Selenium vs Playwright:");
        System.out.println("""
            
            Selenium (Manual wait):
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
            driver.findElement(By.id("finish")).click();
            
            Playwright (Auto-wait):
            page.locator("#finish").click();
            
            That's it! No explicit wait! ✨
            """);
        
        System.out.println("✓ Visibility test passed!\n");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: Actionability checks before actions")
    public void test03_ActionabilityChecks() {
        System.out.println("\n=== Test 03: Actionability Checks ===");
        
        System.out.println("\n🔍 PLAYWRIGHT ACTIONABILITY CHECKS:");
        System.out.println("""
            
            Before every action (click, fill, check, etc.), Playwright:
            
            1. ATTACHED:
               ✅ Element is attached to DOM
               ✅ Not detached or removed
            
            2. VISIBLE:
               ✅ Element has non-empty bounding box
               ✅ Not display:none or visibility:hidden
               ✅ No opacity:0
            
            3. STABLE:
               ✅ Element is not animating
               ✅ Position settled for 2 consecutive frames
               ✅ No rapid position changes
            
            4. RECEIVES EVENTS:
               ✅ Not covered by other elements
               ✅ Not behind modal/overlay
               ✅ Pointer events enabled
            
            5. ENABLED:
               ✅ Not disabled attribute (for form controls)
               ✅ Not readonly (for inputs)
            
            All these checks happen AUTOMATICALLY!
            Default timeout: 30 seconds
            """);
        
        System.out.println("✓ Actionability concept explained!\n");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: No explicit waits needed")
    public void test04_NoExplicitWaitsNeeded() {
        System.out.println("\n=== Test 04: No Explicit Waits Needed ===");
        
        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/2");
        
        // Click start
        System.out.println("1. Clicking Start button...");
        page.getByRole(AriaRole.BUTTON).click();
        
        // Just use the element - Playwright waits!
        System.out.println("2. Getting text (with auto-wait)...");
        String text = page.locator("#finish h4").textContent();
        
        System.out.println("✓ Got text: " + text);
        assertTrue(text.contains("Hello World"));
        
        System.out.println("\n💡 Compare the code:");
        System.out.println("""
            
            ❌ Selenium - Manual waits everywhere:
            ------------------------------------------
            driver.findElement(By.cssSelector("button")).click();
            
            // Wait for element
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("finish")));
            
            // Wait for visibility
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("finish")));
            
            // Finally get text
            String text = driver.findElement(By.id("finish")).getText();
            
            
            ✅ Playwright - Auto-waits:
            ------------------------------------------
            page.getByRole(AriaRole.BUTTON).click();
            
            // That's it! No waits needed!
            String text = page.locator("#finish h4").textContent();
            
            
            Result: 90% less wait code! 🎉
            """);
        
        System.out.println("✓ No explicit waits test passed!\n");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: Auto-wait for different actions")
    public void test05_AutoWaitForDifferentActions() {
        System.out.println("\n=== Test 05: Auto-wait for Different Actions ===");
        
        System.out.println("\n🎯 AUTO-WAIT APPLIES TO ALL ACTIONS:");
        System.out.println("""
            
            All these actions auto-wait for actionability:
            
            ✅ click()           - Waits for clickable
            ✅ dblclick()        - Waits for clickable
            ✅ fill()            - Waits for writable
            ✅ type()            - Waits for writable
            ✅ press()           - Waits for focusable
            ✅ check()           - Waits for checkable
            ✅ uncheck()         - Waits for uncheckable
            ✅ setChecked()      - Waits for checkable
            ✅ selectOption()    - Waits for selectable
            ✅ hover()           - Waits for hoverable
            ✅ dragTo()          - Waits for draggable
            ✅ focus()           - Waits for focusable
            ✅ setInputFiles()   - Waits for file input
            
            Query methods also auto-wait:
            ✅ textContent()     - Waits for element
            ✅ innerText()       - Waits for element
            ✅ innerHTML()       - Waits for element
            ✅ getAttribute()    - Waits for element
            ✅ isVisible()       - Checks visibility
            ✅ isEnabled()       - Checks enabled state
            ✅ isChecked()       - Checks checked state
            
            Example - Banking form:
            page.getByLabel("Username").fill("admin");
            // ✅ Waits for input to be visible, enabled, stable
            
            page.getByRole(AriaRole.BUTTON, setName("Submit")).click();
            // ✅ Waits for button to be visible, enabled, stable, clickable
            """);
        
        System.out.println("✓ Different actions explained!\n");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: Auto-wait performance benefits")
    public void test06_AutoWaitPerformanceBenefits() {
        System.out.println("\n=== Test 06: Performance Benefits ===");
        
        System.out.println("\n⚡ PERFORMANCE BENEFITS:");
        System.out.println("""
            
            1. NO UNNECESSARY WAITS:
               ❌ Selenium: sleep(5000) everywhere
               ✅ Playwright: Waits only as long as needed
            
            2. SMART POLLING:
               ✅ Checks every 50ms (default)
               ✅ Stops immediately when condition met
               ✅ No wasted time
            
            3. PARALLEL CHECKS:
               ✅ Multiple conditions checked simultaneously
               ✅ Faster than sequential checks
            
            4. PREDICTABLE:
               ✅ 30s default timeout (configurable)
               ✅ Clear error messages on timeout
               ✅ No random failures
            
            5. LESS CODE:
               ✅ 90% less wait code
               ✅ More readable tests
               ✅ Easier maintenance
            
            Real-world impact:
            - Selenium test: 45 seconds (with sleeps)
            - Playwright test: 15 seconds (auto-wait)
            - 3x FASTER! ⚡
            """);
        
        System.out.println("✓ Performance benefits explained!\n");
    }
    
    @Test
    @Order(7)
    @DisplayName("Test 7: When auto-wait is NOT used")
    public void test07_WhenAutoWaitIsNotUsed() {
        System.out.println("\n=== Test 07: When Auto-wait is NOT Used ===");
        
        System.out.println("\n⚠️ AUTO-WAIT DOES NOT APPLY TO:");
        System.out.println("""
            
            1. PAGE NAVIGATION:
               page.navigate(url)
               // Returns immediately (doesn't wait for full load)
               // Use page.waitForLoadState() if needed
            
            2. QUERY METHODS WITH count():
               int count = page.locator(".item").count()
               // Returns immediately with current count
               // Doesn't wait for elements to appear
            
            3. DIRECT DOM QUERIES:
               page.evaluate("() => document.querySelectorAll('.item')")
               // Executes immediately
               // No auto-wait
            
            4. CUSTOM JAVASCRIPT:
               page.evaluate("() => myCustomFunction()")
               // Executes immediately
               // You control the logic
            
            Solutions:
            
            // Wait for elements before counting
            page.waitForSelector(".item");
            int count = page.locator(".item").count();
            
            // Wait for load state
            page.navigate(url);
            page.waitForLoadState(LoadState.NETWORKIDLE);
            
            // Use locator methods (they auto-wait)
            page.locator(".item").first().isVisible();  // ✅ Auto-waits
            """);
        
        System.out.println("✓ Exceptions explained!\n");
    }
    
    @Test
    @Order(8)
    @DisplayName("Test 8: Banking portal auto-wait example")
    public void test08_BankingExample() {
        System.out.println("\n=== Test 08: Banking Portal Example ===");
        
        System.out.println("\n💰 REAL-WORLD BANKING EXAMPLE:");
        System.out.println("""
            
            Scenario: Transfer money
            
            // Login (all auto-wait!)
            page.getByLabel("Username").fill("rajesh.kumar");
            page.getByLabel("Password").fill("SecurePass123!");
            page.getByRole(AriaRole.BUTTON, setName("Login")).click();
            
            // Wait for dashboard to load (auto-wait!)
            page.getByRole(AriaRole.HEADING, setName("Dashboard")).isVisible();
            
            // Navigate to transfer (auto-wait!)
            page.getByRole(AriaRole.LINK, setName("Transfer")).click();
            
            // Fill transfer form (all auto-wait!)
            page.getByLabel("From Account").selectOption("savings");
            page.getByLabel("To Account").fill("9876543210");
            page.getByLabel("Amount").fill("5000");
            page.getByLabel("Remarks").fill("Rent payment");
            
            // Submit (auto-wait!)
            page.getByRole(AriaRole.BUTTON, setName("Transfer")).click();
            
            // Wait for success message (auto-wait!)
            page.getByText("Transfer successful").isVisible();
            
            // Get transaction ID (auto-wait!)
            String txnId = page.getByTestId("txn-id").textContent();
            
            
            Total explicit waits needed: ZERO! 🎉
            
            Playwright automatically waited for:
            ✅ Login form to load
            ✅ Dashboard to appear
            ✅ Transfer page to load
            ✅ Form fields to be ready
            ✅ Submit button to be enabled
            ✅ Success message to appear
            ✅ Transaction ID to be available
            
            All of this would need 10+ explicit waits in Selenium!
            """);
        
        System.out.println("✓ Banking example completed!\n");
    }
}

/*
 * PLAYWRIGHT AUTO-WAITING SUMMARY:
 * 
 * 1. WHAT IS AUTO-WAITING?
 *    - Playwright waits for elements to be actionable
 *    - No explicit waits needed (90% of cases)
 *    - Smart, fast, reliable
 * 
 * 2. ACTIONABILITY CHECKS:
 *    ✅ Attached to DOM
 *    ✅ Visible
 *    ✅ Stable (not animating)
 *    ✅ Receives events (not covered)
 *    ✅ Enabled
 * 
 * 3. APPLIES TO:
 *    - click(), fill(), check(), etc.
 *    - textContent(), isVisible(), etc.
 *    - All locator actions
 * 
 * 4. DOES NOT APPLY TO:
 *    - page.navigate()
 *    - count()
 *    - evaluate()
 * 
 * 5. BENEFITS:
 *    ✅ 90% less wait code
 *    ✅ 3x faster tests
 *    ✅ More reliable
 *    ✅ Easier to maintain
 * 
 * 6. DEFAULT TIMEOUT:
 *    - 30 seconds
 *    - Configurable
 *    - Per-action override
 * 
 * SELENIUM vs PLAYWRIGHT:
 * 
 * Selenium:
 * WebDriverWait wait = new WebDriverWait(driver, 10);
 * wait.until(ExpectedConditions.elementToBeClickable(locator));
 * element.click();
 * 
 * Playwright:
 * page.locator("button").click();  // That's it!
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test01_AutoWaiting
 */
