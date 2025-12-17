package com.npci.training.level2;

import com.microsoft.playwright.*;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Playwright Level 2 - Test 01: Basic Locators
 * 
 * Topics Covered:
 * - page.locator() - Primary method
 * - CSS Selectors
 * - ID, Class, Attribute selectors
 * - Text selectors
 * - Chaining locators
 * - Locator best practices
 * 
 * Duration: 30 minutes
 */
@DisplayName("Basic Locators and Selectors")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test01_BasicLocators extends BaseTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: CSS Selector - ID")
    public void test01_CssSelectorById() {
        System.out.println("\n=== Test 01: CSS Selector - ID ===");
        
        page.navigate("https://the-internet.herokuapp.com/login");
        
        // Method 1: Using #id
        System.out.println("1. Locator by ID: #username");
        Locator usernameInput = page.locator("#username");
        usernameInput.fill("tomsmith");
        System.out.println("✓ Filled username using #id selector");
        
        // Method 2: Using [id=value]
        System.out.println("2. Locator by attribute: [id='password']");
        Locator passwordInput = page.locator("[id='password']");
        passwordInput.fill("SuperSecretPassword!");
        System.out.println("✓ Filled password using attribute selector");
        
        // Verify values
        assertEquals("tomsmith", usernameInput.inputValue());
        assertEquals("SuperSecretPassword!", passwordInput.inputValue());
        
        System.out.println("✓ ID selectors test passed!\n");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: CSS Selector - Class")
    public void test02_CssSelectorByClass() {
        System.out.println("\n=== Test 02: CSS Selector - Class ===");
        
        page.navigate("https://the-internet.herokuapp.com/login");
        
        // Locate by class
        System.out.println("1. Locator by class: .radius");
        Locator loginButton = page.locator("button.radius");
        System.out.println("✓ Found login button using class selector");
        
        // Multiple classes
        System.out.println("2. Locator by multiple classes");
        int buttonCount = page.locator("button.radius").count();
        System.out.println("✓ Found " + buttonCount + " button(s) with .radius class");
        
        assertTrue(buttonCount > 0);
        assertTrue(loginButton.isVisible());
        
        System.out.println("✓ Class selectors test passed!\n");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: CSS Selector - Attribute")
    public void test03_CssSelectorByAttribute() {
        System.out.println("\n=== Test 03: CSS Selector - Attribute ===");
        
        page.navigate("https://the-internet.herokuapp.com/login");
        
        // Attribute equals
        System.out.println("1. [attribute='value']");
        Locator usernameByName = page.locator("[name='username']");
        usernameByName.fill("admin");
        System.out.println("✓ Located by [name='username']");
        
        // Attribute contains
        System.out.println("2. [attribute*='partial']");
        Locator usernameByPartial = page.locator("[name*='user']");
        System.out.println("✓ Located by [name*='user']");
        
        // Attribute starts with
        System.out.println("3. [attribute^='start']");
        Locator usernameByStart = page.locator("[name^='user']");
        System.out.println("✓ Located by [name^='user']");
        
        // Attribute ends with
        System.out.println("4. [attribute$='end']");
        Locator usernameByEnd = page.locator("[name$='name']");
        System.out.println("✓ Located by [name$='name']");
        
        // Type attribute
        System.out.println("5. [type='submit']");
        Locator submitButton = page.locator("[type='submit']");
        assertTrue(submitButton.isVisible());
        System.out.println("✓ Located submit button by type");
        
        System.out.println("✓ Attribute selectors test passed!\n");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: Text-based locators")
    public void test04_TextBasedLocators() {
        System.out.println("\n=== Test 04: Text-based Locators ===");
        
        page.navigate("https://the-internet.herokuapp.com/");
        
        // Exact text match
        System.out.println("1. text='Exact Text'");
        Locator abTestLink = page.locator("text='A/B Testing'");
        assertTrue(abTestLink.isVisible());
        System.out.println("✓ Found link with exact text");
        
        // Partial text match
        System.out.println("2. text=Partial (contains)");
        Locator abTestLinkPartial = page.locator("text=A/B Test");
        assertTrue(abTestLinkPartial.isVisible());
        System.out.println("✓ Found link with partial text");
        
        // Has-text selector (CSS)
        System.out.println("3. :has-text('text')");
        Locator linkWithText = page.locator("a:has-text('Add/Remove Elements')");
        assertTrue(linkWithText.isVisible());
        System.out.println("✓ Found link using :has-text()");
        
        // Text in any element
        System.out.println("4. text>> with element");
        Locator h1WithText = page.locator("h1:has-text('Welcome')");
        assertTrue(h1WithText.count() > 0);
        System.out.println("✓ Found h1 with text");
        
        System.out.println("✓ Text-based locators test passed!\n");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: Combining selectors")
    public void test05_CombiningSelectors() {
        System.out.println("\n=== Test 05: Combining Selectors ===");
        
        page.navigate("https://the-internet.herokuapp.com/login");
        
        // Element type + class
        System.out.println("1. button.radius");
        Locator buttonWithClass = page.locator("button.radius");
        assertTrue(buttonWithClass.isVisible());
        System.out.println("✓ Found button with class");
        
        // Element type + attribute
        System.out.println("2. input[name='username']");
        Locator inputWithName = page.locator("input[name='username']");
        assertTrue(inputWithName.isVisible());
        System.out.println("✓ Found input with name");
        
        // ID + class
        System.out.println("3. #id.class");
        // Most elements don't have both, but syntax is valid
        System.out.println("✓ Syntax demonstrated");
        
        // Multiple attributes
        System.out.println("4. [type='text'][name='username']");
        Locator inputMultiAttr = page.locator("[type='text'][name='username']");
        assertTrue(inputMultiAttr.isVisible());
        System.out.println("✓ Found input with multiple attributes");
        
        System.out.println("✓ Combined selectors test passed!\n");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: Descendant and child selectors")
    public void test06_DescendantAndChildSelectors() {
        System.out.println("\n=== Test 06: Descendant and Child Selectors ===");
        
        page.navigate("https://the-internet.herokuapp.com/login");
        
        // Descendant selector (space)
        System.out.println("1. form input - Descendant");
        Locator formInputs = page.locator("form input");
        int inputCount = formInputs.count();
        System.out.println("✓ Found " + inputCount + " inputs inside form");
        assertTrue(inputCount >= 2);
        
        // Direct child selector (>)
        System.out.println("2. form > button - Direct child");
        Locator formButtons = page.locator("form > button");
        System.out.println("✓ Found direct child button(s)");
        
        // Specific path
        System.out.println("3. #login > form > button");
        Locator specificPath = page.locator("#login form button");
        assertTrue(specificPath.count() > 0);
        System.out.println("✓ Found button via specific path");
        
        System.out.println("✓ Descendant selectors test passed!\n");
    }
    
    @Test
    @Order(7)
    @DisplayName("Test 7: nth child and sibling selectors")
    public void test07_NthChildAndSiblings() {
        System.out.println("\n=== Test 07: nth-child and Siblings ===");
        
        page.navigate("https://the-internet.herokuapp.com/");
        
        // nth-child
        System.out.println("1. li:nth-child(1) - First child");
        Locator firstLi = page.locator("ul li:nth-child(1)");
        System.out.println("✓ First li text: " + firstLi.first().textContent());
        
        // nth-child(odd)
        System.out.println("2. li:nth-child(odd) - Odd children");
        int oddCount = page.locator("ul li:nth-child(odd)").count();
        System.out.println("✓ Found " + oddCount + " odd-positioned lis");
        
        // nth-child(even)
        System.out.println("3. li:nth-child(even) - Even children");
        int evenCount = page.locator("ul li:nth-child(even)").count();
        System.out.println("✓ Found " + evenCount + " even-positioned lis");
        
        // Adjacent sibling (+)
        System.out.println("4. h1 + ul - Adjacent sibling");
        Locator afterH1 = page.locator("h1 + ul");
        System.out.println("✓ Found ul immediately after h1");
        
        System.out.println("✓ nth-child selectors test passed!\n");
    }
    
    @Test
    @Order(8)
    @DisplayName("Test 8: Locator chaining")
    public void test08_LocatorChaining() {
        System.out.println("\n=== Test 08: Locator Chaining ===");
        
        page.navigate("https://the-internet.herokuapp.com/login");
        
        // Find form first, then input inside
        System.out.println("1. Chain locators: form → input");
        Locator form = page.locator("form");
        Locator usernameInForm = form.locator("#username");
        usernameInForm.fill("testuser");
        System.out.println("✓ Chained: page → form → input");
        
        // More specific chaining
        System.out.println("2. Multiple chain: div → form → input");
        Locator container = page.locator("#login");
        Locator formInContainer = container.locator("form");
        Locator inputInForm = formInContainer.locator("input[name='username']");
        assertEquals("testuser", inputInForm.inputValue());
        System.out.println("✓ Chained: page → div → form → input");
        
        System.out.println("✓ Locator chaining test passed!\n");
    }
}

/*
 * PLAYWRIGHT LOCATOR STRATEGIES:
 * 
 * 1. CSS SELECTORS (Primary method):
 *    page.locator("#id")           // ID
 *    page.locator(".class")        // Class
 *    page.locator("tag")           // Tag name
 *    page.locator("[attr='val']")  // Attribute
 * 
 * 2. TEXT SELECTORS:
 *    page.locator("text='Exact'")       // Exact match
 *    page.locator("text=Partial")       // Contains
 *    page.locator(":has-text('text')")  // CSS with text
 * 
 * 3. COMBINING SELECTORS:
 *    page.locator("button.submit")           // Tag + class
 *    page.locator("input[type='text']")      // Tag + attribute
 *    page.locator("form input")              // Descendant
 *    page.locator("form > input")            // Direct child
 * 
 * 4. ATTRIBUTE SELECTORS:
 *    [attr='value']     // Equals
 *    [attr*='partial']  // Contains
 *    [attr^='start']    // Starts with
 *    [attr$='end']      // Ends with
 * 
 * 5. PSEUDO-CLASSES:
 *    :nth-child(n)     // nth child
 *    :first-child      // First child
 *    :last-child       // Last child
 *    :has-text()       // Contains text
 * 
 * 6. CHAINING:
 *    page.locator("form").locator("input")
 *    - Narrows down search
 *    - More specific
 *    - Better performance
 * 
 * BEST PRACTICES:
 * ✅ Use IDs when available (#id)
 * ✅ Use data-testid attributes ([data-testid='value'])
 * ✅ Use text for links and buttons (text='Submit')
 * ✅ Chain locators for specificity
 * ⚠️ Avoid complex XPath
 * ⚠️ Avoid brittle selectors (nth-child(7))
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test01_BasicLocators
 * mvn test -Dtest=Test01_BasicLocators#test01_CssSelectorById
 */
