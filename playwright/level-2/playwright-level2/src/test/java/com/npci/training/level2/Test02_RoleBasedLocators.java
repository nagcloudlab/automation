package com.npci.training.level2;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Playwright Level 2 - Test 02: Role-based Locators (Accessibility)
 * 
 * Topics Covered:
 * - getByRole() - Recommended approach!
 * - ARIA roles
 * - Accessible locators
 * - Role options (name, checked, pressed, etc.)
 * - Benefits for accessibility testing
 * 
 * Duration: 30 minutes
 * 
 * WHY ROLE-BASED LOCATORS?
 * - Most resilient to code changes
 * - Accessible by default
 * - Human-readable
 * - Playwright's RECOMMENDED method
 */
@DisplayName("Role-based Locators (Accessibility)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test02_RoleBasedLocators extends BaseTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: getByRole - Button")
    public void test01_GetByRoleButton() {
        System.out.println("\n=== Test 01: getByRole - Button ===");
        
        page.navigate("https://the-internet.herokuapp.com/login");
        
        // Locate button by role
        System.out.println("1. getByRole(BUTTON)");
        Locator loginButton = page.getByRole(AriaRole.BUTTON);
        System.out.println("✓ Found button by role");
        
        // With specific name
        System.out.println("2. getByRole(BUTTON, name='Login')");
        Locator loginButtonByName = page.getByRole(AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Login"));
        
        assertTrue(loginButtonByName.isVisible());
        System.out.println("✓ Found button with specific name");
        
        // Click it
        page.locator("#username").fill("tomsmith");
        page.locator("#password").fill("SuperSecretPassword!");
        loginButtonByName.click();
        
        page.waitForURL("**/secure");
        System.out.println("✓ Button clicked successfully");
        
        System.out.println("✓ Role-based button test passed!\n");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: getByRole - Link")
    public void test02_GetByRoleLink() {
        System.out.println("\n=== Test 02: getByRole - Link ===");
        
        page.navigate("https://the-internet.herokuapp.com/");
        
        // Find link by role
        System.out.println("1. getByRole(LINK, name='A/B Testing')");
        Locator abTestLink = page.getByRole(AriaRole.LINK, 
            new Page.GetByRoleOptions().setName("A/B Testing"));
        
        assertTrue(abTestLink.isVisible());
        System.out.println("✓ Found link by role and name");
        
        // Click it
        abTestLink.click();
        page.waitForURL("**/abtest");
        System.out.println("✓ Link navigation successful");
        
        // Go back
        page.goBack();
        
        // Find another link
        System.out.println("2. getByRole(LINK, name='Checkboxes')");
        Locator checkboxLink = page.getByRole(AriaRole.LINK,
            new Page.GetByRoleOptions().setName("Checkboxes"));
        assertTrue(checkboxLink.isVisible());
        System.out.println("✓ Found another link");
        
        System.out.println("✓ Role-based link test passed!\n");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: getByRole - Textbox")
    public void test03_GetByRoleTextbox() {
        System.out.println("\n=== Test 03: getByRole - Textbox ===");
        
        page.navigate("https://the-internet.herokuapp.com/login");
        
        // Input fields have role "textbox"
        System.out.println("1. getByRole(TEXTBOX) - Find all");
        int textboxCount = page.getByRole(AriaRole.TEXTBOX).count();
        System.out.println("✓ Found " + textboxCount + " textbox(es)");
        
        // Specific textbox by name (if labeled)
        System.out.println("2. Specific textbox by position");
        Locator firstTextbox = page.getByRole(AriaRole.TEXTBOX).first();
        firstTextbox.fill("admin");
        System.out.println("✓ Filled first textbox");
        
        // Note: These inputs don't have proper ARIA labels
        // In real apps with proper accessibility:
        // page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions()
        //     .setName("Username"))
        
        System.out.println("✓ Role-based textbox test passed!\n");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: getByRole - Checkbox")
    public void test04_GetByRoleCheckbox() {
        System.out.println("\n=== Test 04: getByRole - Checkbox ===");
        
        page.navigate("https://the-internet.herokuapp.com/checkboxes");
        
        // Find all checkboxes
        System.out.println("1. getByRole(CHECKBOX) - Find all");
        Locator checkboxes = page.getByRole(AriaRole.CHECKBOX);
        int count = checkboxes.count();
        System.out.println("✓ Found " + count + " checkbox(es)");
        
        // First checkbox
        System.out.println("2. Check first checkbox");
        Locator firstCheckbox = checkboxes.first();
        
        if (!firstCheckbox.isChecked()) {
            firstCheckbox.check();
            System.out.println("✓ First checkbox checked");
        }
        assertTrue(firstCheckbox.isChecked());
        
        // Second checkbox
        System.out.println("3. Uncheck second checkbox");
        Locator secondCheckbox = checkboxes.nth(1);
        
        if (secondCheckbox.isChecked()) {
            secondCheckbox.uncheck();
            System.out.println("✓ Second checkbox unchecked");
        }
        assertFalse(secondCheckbox.isChecked());
        
        System.out.println("✓ Role-based checkbox test passed!\n");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: getByRole with options")
    public void test05_GetByRoleWithOptions() {
        System.out.println("\n=== Test 05: getByRole with Options ===");
        
        page.navigate("https://the-internet.herokuapp.com/checkboxes");
        
        // Find checkbox by checked state
        System.out.println("1. Find checked checkbox");
        Locator checkedBox = page.getByRole(AriaRole.CHECKBOX,
            new Page.GetByRoleOptions().setChecked(true));
        
        if (checkedBox.count() > 0) {
            System.out.println("✓ Found checked checkbox");
        }
        
        // Find unchecked checkbox
        System.out.println("2. Find unchecked checkbox");
        Locator uncheckedBox = page.getByRole(AriaRole.CHECKBOX,
            new Page.GetByRoleOptions().setChecked(false));
        
        if (uncheckedBox.count() > 0) {
            System.out.println("✓ Found unchecked checkbox");
        }
        
        System.out.println("✓ Role options test passed!\n");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: getByRole - Heading")
    public void test06_GetByRoleHeading() {
        System.out.println("\n=== Test 06: getByRole - Heading ===");
        
        page.navigate("https://the-internet.herokuapp.com/");
        
        // Find heading
        System.out.println("1. getByRole(HEADING)");
        Locator heading = page.getByRole(AriaRole.HEADING,
            new Page.GetByRoleOptions().setName("Welcome to the-internet"));
        
        assertTrue(heading.isVisible());
        System.out.println("✓ Found heading: " + heading.textContent());
        
        // Find by level
        System.out.println("2. Find h1 (level 1)");
        Locator h1 = page.getByRole(AriaRole.HEADING,
            new Page.GetByRoleOptions().setLevel(1));
        assertTrue(h1.count() > 0);
        System.out.println("✓ Found level 1 heading(s)");
        
        System.out.println("✓ Role-based heading test passed!\n");
    }
    
    @Test
    @Order(7)
    @DisplayName("Test 7: Common ARIA roles overview")
    public void test07_CommonAriaRoles() {
        System.out.println("\n=== Test 07: Common ARIA Roles Overview ===");
        
        System.out.println("\n📚 COMMON ARIA ROLES:");
        System.out.println("1. BUTTON - <button>, role='button'");
        System.out.println("2. LINK - <a>, role='link'");
        System.out.println("3. TEXTBOX - <input type='text'>, role='textbox'");
        System.out.println("4. CHECKBOX - <input type='checkbox'>, role='checkbox'");
        System.out.println("5. RADIO - <input type='radio'>, role='radio'");
        System.out.println("6. COMBOBOX - <select>, role='combobox'");
        System.out.println("7. HEADING - <h1>-<h6>, role='heading'");
        System.out.println("8. LIST - <ul>, <ol>, role='list'");
        System.out.println("9. LISTITEM - <li>, role='listitem'");
        System.out.println("10. TABLE - <table>, role='table'");
        System.out.println("11. IMG - <img>, role='img'");
        System.out.println("12. NAVIGATION - <nav>, role='navigation'");
        System.out.println("13. MAIN - <main>, role='main'");
        System.out.println("14. BANNER - <header>, role='banner'");
        System.out.println("15. CONTENTINFO - <footer>, role='contentinfo'");
        
        System.out.println("\n✓ ARIA roles overview completed!\n");
    }
    
    @Test
    @Order(8)
    @DisplayName("Test 8: Banking portal example with roles")
    public void test08_BankingPortalExample() {
        System.out.println("\n=== Test 08: Banking Portal Example ===");
        
        System.out.println("\n💡 REAL-WORLD EXAMPLE - Banking Portal:");
        System.out.println("""
            
            // Login button
            page.getByRole(AriaRole.BUTTON, 
                new Page.GetByRoleOptions().setName("Login")).click();
            
            // Navigation links
            page.getByRole(AriaRole.LINK, 
                new Page.GetByRoleOptions().setName("Transactions")).click();
            
            // Search box
            page.getByRole(AriaRole.TEXTBOX, 
                new Page.GetByRoleOptions().setName("Search transactions"))
                .fill("UPI");
            
            // Filter checkbox
            page.getByRole(AriaRole.CHECKBOX, 
                new Page.GetByRoleOptions().setName("Show pending only"))
                .check();
            
            // Amount heading
            page.getByRole(AriaRole.HEADING, 
                new Page.GetByRoleOptions().setName("Total Amount"))
                .isVisible();
            
            """);
        
        System.out.println("✓ Benefits of role-based locators:");
        System.out.println("  ✅ Resilient to UI changes");
        System.out.println("  ✅ Human-readable");
        System.out.println("  ✅ Tests accessibility");
        System.out.println("  ✅ Playwright RECOMMENDED method");
        
        System.out.println("\n✓ Banking example completed!\n");
    }
}

/*
 * ROLE-BASED LOCATORS - PLAYWRIGHT'S RECOMMENDATION:
 * 
 * 1. WHY USE ROLES?
 *    ✅ Most resilient to code changes
 *    ✅ Tests accessibility automatically
 *    ✅ Human-readable
 *    ✅ Works across different implementations
 * 
 * 2. COMMON ROLES:
 *    AriaRole.BUTTON    - Buttons
 *    AriaRole.LINK      - Links
 *    AriaRole.TEXTBOX   - Input fields
 *    AriaRole.CHECKBOX  - Checkboxes
 *    AriaRole.RADIO     - Radio buttons
 *    AriaRole.COMBOBOX  - Dropdowns
 *    AriaRole.HEADING   - Headings (h1-h6)
 * 
 * 3. ROLE OPTIONS:
 *    .setName("text")          - Accessible name
 *    .setChecked(true/false)   - Checkbox/radio state
 *    .setPressed(true/false)   - Button state
 *    .setLevel(1-6)            - Heading level
 *    .setExpanded(true/false)  - Expandable elements
 * 
 * 4. USAGE PATTERNS:
 *    // Simple
 *    page.getByRole(AriaRole.BUTTON)
 *    
 *    // With name
 *    page.getByRole(AriaRole.BUTTON, 
 *        new Page.GetByRoleOptions().setName("Submit"))
 *    
 *    // With state
 *    page.getByRole(AriaRole.CHECKBOX, 
 *        new Page.GetByRoleOptions().setChecked(true))
 * 
 * 5. BENEFITS:
 *    ✅ If button text changes, locator still works
 *    ✅ If CSS classes change, locator still works
 *    ✅ Forces proper ARIA attributes (accessibility)
 *    ✅ Self-documenting tests
 * 
 * 6. WHEN TO USE:
 *    ✅ ALWAYS for buttons, links, form controls
 *    ✅ New projects with proper ARIA
 *    ✅ Accessibility-focused applications
 *    ⚠️ Legacy apps without ARIA (use CSS selectors)
 * 
 * PLAYWRIGHT LOCATOR PRIORITY:
 * 1. getByRole() - RECOMMENDED
 * 2. getByText() - For static text
 * 3. getByTestId() - For test-specific IDs
 * 4. CSS selectors - Fallback
 * 5. XPath - Last resort
 * 
 * BANKING EXAMPLE:
 * Instead of:
 *   page.locator("#login-btn")           // Fragile
 *   page.locator("button.btn-primary")   // Fragile
 * 
 * Use:
 *   page.getByRole(AriaRole.BUTTON, 
 *       new Page.GetByRoleOptions().setName("Login"))
 *   // Resilient, accessible, clear!
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test02_RoleBasedLocators
 */
