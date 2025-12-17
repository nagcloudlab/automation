package com.npci.training.level2;

import com.microsoft.playwright.*;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Playwright Level 2 - Test 03: Advanced Locator Methods
 * 
 * Topics Covered:
 * - getByText() - Text content
 * - getByLabel() - Form labels
 * - getByPlaceholder() - Input placeholders
 * - getByAltText() - Image alt text
 * - getByTitle() - Element titles
 * - getByTestId() - Test IDs
 * - Locator filtering methods
 * 
 * Duration: 30 minutes
 */
@DisplayName("Advanced Locator Methods")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test03_AdvancedLocatorMethods extends BaseTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: getByText()")
    public void test01_GetByText() {
        System.out.println("\n=== Test 01: getByText() ===");
        
        page.navigate("https://the-internet.herokuapp.com/");
        
        // Exact text match
        System.out.println("1. getByText('A/B Testing') - Exact");
        Locator abTestLink = page.getByText("A/B Testing");
        assertTrue(abTestLink.isVisible());
        System.out.println("✓ Found element with exact text");
        
        // Partial text match (default)
        System.out.println("2. getByText('A/B Test') - Partial");
        Locator partialMatch = page.getByText("A/B Test");
        assertTrue(partialMatch.isVisible());
        System.out.println("✓ Found element with partial text");
        
        // Case-insensitive (using regex)
        System.out.println("3. getByText with regex");
        Locator regexMatch = page.getByText(java.util.regex.Pattern.compile("a/b testing", 
            java.util.regex.Pattern.CASE_INSENSITIVE));
        assertTrue(regexMatch.count() > 0);
        System.out.println("✓ Found element with case-insensitive match");
        
        // Click using getByText
        page.getByText("Add/Remove Elements").click();
        page.waitForURL("**/add_remove_elements/");
        System.out.println("✓ Clicked element using getByText");
        
        System.out.println("✓ getByText test passed!\n");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: getByLabel()")
    public void test02_GetByLabel() {
        System.out.println("\n=== Test 02: getByLabel() ===");
        
        // Note: The test site doesn't have proper labels
        // Demonstrating with HTML example
        
        System.out.println("💡 getByLabel() - For form inputs with <label>");
        System.out.println("""
            
            HTML:
            <label for="username">Username:</label>
            <input id="username" type="text">
            
            Playwright:
            page.getByLabel("Username:").fill("admin");
            
            Benefits:
            ✅ Links label to input automatically
            ✅ More resilient than ID
            ✅ Human-readable
            """);
        
        System.out.println("✓ getByLabel concept demonstrated!\n");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: getByPlaceholder()")
    public void test03_GetByPlaceholder() {
        System.out.println("\n=== Test 03: getByPlaceholder() ===");
        
        System.out.println("💡 getByPlaceholder() - For inputs with placeholder");
        System.out.println("""
            
            HTML:
            <input placeholder="Enter your email">
            
            Playwright:
            page.getByPlaceholder("Enter your email").fill("user@example.com");
            
            // Or partial match
            page.getByPlaceholder("email").fill("user@example.com");
            
            Benefits:
            ✅ Natural way to locate inputs
            ✅ Matches how users see the form
            """);
        
        System.out.println("✓ getByPlaceholder concept demonstrated!\n");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: getByAltText()")
    public void test04_GetByAltText() {
        System.out.println("\n=== Test 04: getByAltText() ===");
        
        System.out.println("💡 getByAltText() - For images with alt text");
        System.out.println("""
            
            HTML:
            <img src="logo.png" alt="Company Logo">
            
            Playwright:
            page.getByAltText("Company Logo").click();
            
            // Or partial match
            page.getByAltText("Logo").isVisible();
            
            Benefits:
            ✅ Tests accessibility (alt text)
            ✅ Resilient to image changes
            ✅ Clear intent
            """);
        
        System.out.println("✓ getByAltText concept demonstrated!\n");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: getByTitle()")
    public void test05_GetByTitle() {
        System.out.println("\n=== Test 05: getByTitle() ===");
        
        System.out.println("💡 getByTitle() - For elements with title attribute");
        System.out.println("""
            
            HTML:
            <button title="Submit form">✓</button>
            <a href="/help" title="Get help">?</a>
            
            Playwright:
            page.getByTitle("Submit form").click();
            page.getByTitle("Get help").click();
            
            Use Case:
            - Icon buttons with tooltip text
            - Elements with hover descriptions
            """);
        
        System.out.println("✓ getByTitle concept demonstrated!\n");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: getByTestId()")
    public void test06_GetByTestId() {
        System.out.println("\n=== Test 06: getByTestId() ===");
        
        System.out.println("💡 getByTestId() - For test-specific attributes");
        System.out.println("""
            
            HTML:
            <button data-testid="login-button">Login</button>
            <input data-testid="username-input" />
            
            Playwright:
            page.getByTestId("login-button").click();
            page.getByTestId("username-input").fill("admin");
            
            Benefits:
            ✅ Most stable locator
            ✅ Never affected by UI changes
            ✅ Clear separation: test vs production code
            
            Best Practice:
            Use when UI changes frequently
            Coordinate with developers
            """);
        
        System.out.println("✓ getByTestId concept demonstrated!\n");
    }
    
    @Test
    @Order(7)
    @DisplayName("Test 7: Filtering locators")
    public void test07_FilteringLocators() {
        System.out.println("\n=== Test 07: Filtering Locators ===");
        
        page.navigate("https://the-internet.herokuapp.com/");
        
        // Filter by text
        System.out.println("1. filter({ hasText: 'text' })");
        Locator links = page.locator("a");
        Locator filteredLinks = links.filter(
            new Locator.FilterOptions().setHasText("Testing")
        );
        System.out.println("✓ Filtered " + filteredLinks.count() + " links with 'Testing'");
        
        // Filter by another locator
        System.out.println("2. filter({ has: locator })");
        // Example: find list items that contain a link
        
        // Filter NOT having text
        System.out.println("3. filter({ hasNotText: 'text' })");
        Locator linksWithoutTest = links.filter(
            new Locator.FilterOptions().setHasNotText("Test")
        );
        System.out.println("✓ Filtered links without 'Test'");
        
        System.out.println("✓ Filtering test passed!\n");
    }
    
    @Test
    @Order(8)
    @DisplayName("Test 8: Locator methods - first, last, nth")
    public void test08_LocatorMethods() {
        System.out.println("\n=== Test 08: Locator Methods ===");
        
        page.navigate("https://the-internet.herokuapp.com/");
        
        // Count
        System.out.println("1. count() - Count matching elements");
        int linkCount = page.locator("a").count();
        System.out.println("✓ Found " + linkCount + " links");
        assertTrue(linkCount > 0);
        
        // First
        System.out.println("2. first() - Get first element");
        Locator firstLink = page.locator("a").first();
        String firstLinkText = firstLink.textContent();
        System.out.println("✓ First link: " + firstLinkText);
        
        // Last
        System.out.println("3. last() - Get last element");
        Locator lastLink = page.locator("a").last();
        String lastLinkText = lastLink.textContent();
        System.out.println("✓ Last link: " + lastLinkText);
        
        // Nth (0-indexed)
        System.out.println("4. nth(index) - Get specific element");
        Locator thirdLink = page.locator("a").nth(2); // 0-indexed
        String thirdLinkText = thirdLink.textContent();
        System.out.println("✓ Third link (nth(2)): " + thirdLinkText);
        
        // All
        System.out.println("5. all() - Get all as list");
        // In Playwright Java, use count() and nth() to iterate
        
        System.out.println("✓ Locator methods test passed!\n");
    }
    
    @Test
    @Order(9)
    @DisplayName("Test 9: Real-world banking example")
    public void test09_BankingExample() {
        System.out.println("\n=== Test 09: Banking Portal Example ===");
        
        System.out.println("\n💰 REAL-WORLD BANKING PORTAL:");
        System.out.println("""
            
            // Navigate to transactions
            page.getByText("Transactions").click();
            
            // Search by placeholder
            page.getByPlaceholder("Search transactions").fill("UPI");
            
            // Filter by label
            page.getByLabel("Show pending only").check();
            
            // Click transaction by text
            page.getByText("UPI/987654321/NPCI").click();
            
            // Verify amount by test ID
            String amount = page.getByTestId("transaction-amount")
                               .textContent();
            assertEquals("₹500.00", amount);
            
            // Export button by role and text
            page.getByRole(AriaRole.BUTTON)
                .filter(new Locator.FilterOptions().setHasText("Export"))
                .click();
            
            // First transaction in list
            page.locator(".transaction-item").first().click();
            
            // Last 5 transactions
            int totalTransactions = page.locator(".transaction-item").count();
            page.locator(".transaction-item").nth(totalTransactions - 5).scrollIntoViewIfNeeded();
            """);
        
        System.out.println("✓ Best practices demonstrated:");
        System.out.println("  1. Use getByRole for buttons/links");
        System.out.println("  2. Use getByText for static content");
        System.out.println("  3. Use getByPlaceholder for search fields");
        System.out.println("  4. Use getByLabel for form fields");
        System.out.println("  5. Use getByTestId for critical elements");
        System.out.println("  6. Use filter() to narrow down results");
        
        System.out.println("\n✓ Banking example completed!\n");
    }
}

/*
 * ADVANCED LOCATOR METHODS:
 * 
 * 1. TEXT-BASED:
 *    page.getByText("text")           // Exact or partial
 *    page.getByText(Pattern.compile()) // Regex
 * 
 * 2. FORM ELEMENTS:
 *    page.getByLabel("label")         // Form label
 *    page.getByPlaceholder("text")    // Placeholder
 * 
 * 3. MEDIA:
 *    page.getByAltText("alt")         // Image alt text
 *    page.getByTitle("title")         // Title attribute
 * 
 * 4. TESTING:
 *    page.getByTestId("id")           // data-testid
 * 
 * 5. FILTERING:
 *    locator.filter(options)          // Filter results
 *    locator.filter(hasText: "text")  // Has text
 *    locator.filter(hasNotText: "x")  // Doesn't have text
 * 
 * 6. COLLECTION METHODS:
 *    locator.count()                  // Count elements
 *    locator.first()                  // First element
 *    locator.last()                   // Last element
 *    locator.nth(index)               // Specific element
 * 
 * PLAYWRIGHT LOCATOR HIERARCHY (PRIORITY):
 * 
 * 1️⃣ getByRole() - MOST RECOMMENDED
 *    - Accessible, resilient, clear
 * 
 * 2️⃣ getByLabel() - FOR FORMS
 *    - Natural for form inputs
 * 
 * 3️⃣ getByPlaceholder() - FOR SEARCH
 *    - Good for search/filter fields
 * 
 * 4️⃣ getByText() - FOR STATIC CONTENT
 *    - Links, buttons, headings
 * 
 * 5️⃣ getByTestId() - FOR STABILITY
 *    - Most stable, requires dev coordination
 * 
 * 6️⃣ CSS/XPath - FALLBACK
 *    - When above methods don't work
 * 
 * WHEN TO USE WHAT:
 * 
 * Login Button:
 *   ✅ page.getByRole(AriaRole.BUTTON, setName("Login"))
 *   ⚠️ page.getByText("Login")
 *   ❌ page.locator("#login-btn")
 * 
 * Username Input:
 *   ✅ page.getByLabel("Username")
 *   ✅ page.getByPlaceholder("Enter username")
 *   ⚠️ page.locator("#username")
 * 
 * Search Field:
 *   ✅ page.getByPlaceholder("Search")
 *   ✅ page.getByRole(AriaRole.TEXTBOX, setName("Search"))
 *   ⚠️ page.locator("[type='search']")
 * 
 * Logo Image:
 *   ✅ page.getByAltText("Company Logo")
 *   ⚠️ page.locator("img.logo")
 * 
 * Critical Element:
 *   ✅ page.getByTestId("submit-button")
 *   - Most stable for frequently changing UI
 * 
 * BEST PRACTICES:
 * ✅ Prefer getByRole > getByLabel > getByPlaceholder > CSS
 * ✅ Use getByTestId for critical, frequently changing elements
 * ✅ Use filter() to narrow down multiple matches
 * ✅ Use first(), last(), nth() for collections
 * ✅ Avoid brittle selectors (nth-child(7), complex XPath)
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test03_AdvancedLocatorMethods
 */
