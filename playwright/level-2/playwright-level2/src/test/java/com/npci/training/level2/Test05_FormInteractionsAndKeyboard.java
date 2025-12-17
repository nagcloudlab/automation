package com.npci.training.level2;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Playwright Level 2 - Test 05: Form Interactions and Keyboard
 * 
 * Topics Covered:
 * - fill() vs type()
 * - Checkbox and radio buttons
 * - Dropdown selection
 * - File uploads
 * - Keyboard actions
 * - press(), pressSequentially()
 * - Keyboard shortcuts
 * 
 * Duration: 35 minutes
 */
@DisplayName("Form Interactions and Keyboard Actions")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test05_FormInteractionsAndKeyboard extends BaseTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: fill() vs type() comparison")
    public void test01_FillVsType() {
        System.out.println("\n=== Test 01: fill() vs type() ===");
        
        page.navigate("https://the-internet.herokuapp.com/login");
        
        // Method 1: fill() - Fast, recommended
        System.out.println("1. fill() - Instant text entry");
        long startFill = System.currentTimeMillis();
        page.locator("#username").fill("playwright_user");
        long fillTime = System.currentTimeMillis() - startFill;
        System.out.println("✓ fill() took: " + fillTime + "ms");
        
        // Clear
        page.locator("#username").clear();
        
        // Method 2: type() - Character by character
        System.out.println("2. type() - Character by character");
        long startType = System.currentTimeMillis();
        page.locator("#username").type("playwright_user");
        long typeTime = System.currentTimeMillis() - startType;
        System.out.println("✓ type() took: " + typeTime + "ms");
        
        System.out.println("\n📊 Comparison:");
        System.out.println("  fill(): " + fillTime + "ms (FAST ✅)");
        System.out.println("  type(): " + typeTime + "ms (SLOW ⚠️)");
        System.out.println("\n  Use fill() for 99% of cases!");
        System.out.println("  Use type() only for:");
        System.out.println("    - Auto-complete testing");
        System.out.println("    - Character-by-character validation");
        System.out.println("    - Typing animation testing");
        
        System.out.println("\n✓ fill() vs type() test passed!\n");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: Checkbox interactions")
    public void test02_CheckboxInteractions() {
        System.out.println("\n=== Test 02: Checkbox Interactions ===");
        
        page.navigate("https://the-internet.herokuapp.com/checkboxes");
        
        // Get all checkboxes
        Locator checkboxes = page.locator("input[type='checkbox']");
        int count = checkboxes.count();
        System.out.println("Found " + count + " checkboxes");
        
        // Check/Uncheck methods
        Locator checkbox1 = checkboxes.first();
        Locator checkbox2 = checkboxes.nth(1);
        
        // Method 1: check() - Always checks
        System.out.println("\n1. check() - Ensures checked");
        checkbox1.check();
        assertTrue(checkbox1.isChecked());
        System.out.println("✓ Checkbox 1 checked");
        
        // Method 2: uncheck() - Always unchecks
        System.out.println("\n2. uncheck() - Ensures unchecked");
        checkbox2.uncheck();
        assertFalse(checkbox2.isChecked());
        System.out.println("✓ Checkbox 2 unchecked");
        
        // Method 3: setChecked() - Set to specific state
        System.out.println("\n3. setChecked(boolean) - Set specific state");
        checkbox1.setChecked(false);
        assertFalse(checkbox1.isChecked());
        System.out.println("✓ Checkbox 1 set to unchecked");
        
        checkbox1.setChecked(true);
        assertTrue(checkbox1.isChecked());
        System.out.println("✓ Checkbox 1 set to checked");
        
        // Method 4: click() - Toggles
        System.out.println("\n4. click() - Toggles state");
        boolean before = checkbox1.isChecked();
        checkbox1.click();
        boolean after = checkbox1.isChecked();
        assertNotEquals(before, after);
        System.out.println("✓ Checkbox state toggled");
        
        System.out.println("\n✓ Checkbox test passed!\n");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: Radio button selection")
    public void test03_RadioButtons() {
        System.out.println("\n=== Test 03: Radio Buttons ===");
        
        System.out.println("💡 Radio Button Selection:");
        System.out.println("""
            
            HTML:
            <input type="radio" name="gender" value="male"> Male
            <input type="radio" name="gender" value="female"> Female
            
            Playwright:
            // Select by value
            page.locator("[value='male']").check();
            
            // Select by label
            page.getByLabel("Male").check();
            
            // Verify selection
            assertTrue(page.locator("[value='male']").isChecked());
            assertFalse(page.locator("[value='female']").isChecked());
            
            Banking Example:
            page.getByLabel("Account Type");
            page.locator("[value='savings']").check();
            page.locator("[value='current']").check();
            """);
        
        System.out.println("✓ Radio button concept demonstrated!\n");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: Dropdown selection")
    public void test04_DropdownSelection() {
        System.out.println("\n=== Test 04: Dropdown Selection ===");
        
        page.navigate("https://the-internet.herokuapp.com/dropdown");
        
        Locator dropdown = page.locator("#dropdown");
        
        // Method 1: Select by value
        System.out.println("1. selectOption(value)");
        dropdown.selectOption("1");
        assertEquals("1", dropdown.inputValue());
        System.out.println("✓ Selected Option 1 by value");
        
        // Method 2: Select by label
        System.out.println("2. selectOption(label)");
        dropdown.selectOption(new SelectOption().setLabel("Option 2"));
        System.out.println("✓ Selected Option 2 by label");
        
        // Method 3: Select by index
        System.out.println("3. selectOption(index)");
        dropdown.selectOption(new SelectOption().setIndex(1));
        System.out.println("✓ Selected option by index");
        
        // Get selected value
        String selected = dropdown.inputValue();
        System.out.println("✓ Currently selected: " + selected);
        
        System.out.println("✓ Dropdown test passed!\n");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: File upload")
    public void test05_FileUpload() {
        System.out.println("\n=== Test 05: File Upload ===");
        
        page.navigate("https://the-internet.herokuapp.com/upload");
        
        System.out.println("💡 File Upload Methods:");
        System.out.println("""
            
            // Single file upload
            page.locator("input[type='file']")
                .setInputFiles(Paths.get("document.pdf"));
            
            // Multiple files
            page.locator("input[type='file']")
                .setInputFiles(new Path[] {
                    Paths.get("file1.pdf"),
                    Paths.get("file2.pdf")
                });
            
            // Upload from buffer
            page.locator("input[type='file']")
                .setInputFiles(new FilePayload(
                    "statement.pdf",
                    "application/pdf",
                    pdfBytes
                ));
            
            // Clear file input
            page.locator("input[type='file']")
                .setInputFiles(new Path[0]);
            
            Banking Examples:
            // Upload bank statement
            page.getByLabel("Upload Statement")
                .setInputFiles(Paths.get("statement.pdf"));
            
            // Upload KYC documents
            page.locator("#kyc-upload")
                .setInputFiles(new Path[] {
                    Paths.get("aadhar.pdf"),
                    Paths.get("pan.pdf")
                });
            """);
        
        System.out.println("✓ File upload concept demonstrated!\n");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: Keyboard actions - press()")
    public void test06_KeyboardPress() {
        System.out.println("\n=== Test 06: Keyboard press() ===");
        
        page.navigate("https://the-internet.herokuapp.com/key_presses");
        
        Locator input = page.locator("#target");
        Locator result = page.locator("#result");
        
        // Press Enter
        System.out.println("1. Press Enter key");
        input.click();
        input.press("Enter");
        assertTrue(result.textContent().contains("ENTER"));
        System.out.println("✓ Enter key pressed");
        
        // Press Tab
        System.out.println("2. Press Tab key");
        input.press("Tab");
        assertTrue(result.textContent().contains("TAB"));
        System.out.println("✓ Tab key pressed");
        
        // Press Arrow keys
        System.out.println("3. Press Arrow keys");
        input.press("ArrowDown");
        input.press("ArrowUp");
        input.press("ArrowLeft");
        input.press("ArrowRight");
        System.out.println("✓ Arrow keys pressed");
        
        // Press Escape
        System.out.println("4. Press Escape");
        input.press("Escape");
        System.out.println("✓ Escape pressed");
        
        System.out.println("✓ Keyboard press test passed!\n");
    }
    
    @Test
    @Order(7)
    @DisplayName("Test 7: Keyboard shortcuts")
    public void test07_KeyboardShortcuts() {
        System.out.println("\n=== Test 07: Keyboard Shortcuts ===");
        
        System.out.println("⌨️ KEYBOARD SHORTCUTS:");
        System.out.println("""
            
            // Ctrl+A (Select all)
            page.locator("#input").press("Control+A");
            
            // Ctrl+C (Copy)
            page.locator("#input").press("Control+C");
            
            // Ctrl+V (Paste)
            page.locator("#input").press("Control+V");
            
            // Ctrl+X (Cut)
            page.locator("#input").press("Control+X");
            
            // Ctrl+Z (Undo)
            page.locator("#input").press("Control+Z");
            
            // Ctrl+Y (Redo)
            page.locator("#input").press("Control+Y");
            
            // Shift+Tab (Reverse tab)
            page.locator("#input").press("Shift+Tab");
            
            // Alt+F4 (Close window)
            page.press("body", "Alt+F4");
            
            Banking Examples:
            // Quick search: Ctrl+F
            page.press("body", "Control+F");
            page.locator("#search").fill("transaction");
            
            // Refresh: F5 or Ctrl+R
            page.press("body", "F5");
            page.press("body", "Control+R");
            
            // Print: Ctrl+P
            page.press("body", "Control+P");
            """);
        
        System.out.println("✓ Keyboard shortcuts demonstrated!\n");
    }
    
    @Test
    @Order(8)
    @DisplayName("Test 8: pressSequentially() for slow typing")
    public void test08_PressSequentially() {
        System.out.println("\n=== Test 08: pressSequentially() ===");
        
        page.navigate("https://the-internet.herokuapp.com/login");
        
        // Type slowly with delay
        System.out.println("1. pressSequentially() with delay");
        page.locator("#username").pressSequentially(
            "SlowTyping",
            new Locator.PressSequentiallyOptions().setDelay(100)
        );
        System.out.println("✓ Typed with 100ms delay per character");
        
        System.out.println("\n💡 pressSequentially() vs type():");
        System.out.println("""
            
            pressSequentially():
            - New method in Playwright
            - Types character by character
            - Configurable delay
            - Triggers input events
            
            type():
            - Legacy method
            - Similar to pressSequentially()
            - Less control
            
            When to use:
            - Auto-complete fields
            - Search suggestions
            - Real-time validation
            - Character count displays
            
            Example:
            page.locator("#search")
                .pressSequentially("iPhone", 
                    new Locator.PressSequentiallyOptions()
                        .setDelay(200));
            // Wait for suggestions after each character
            """);
        
        System.out.println("✓ pressSequentially() demonstrated!\n");
    }
    
    @Test
    @Order(9)
    @DisplayName("Test 9: Complete form example")
    public void test09_CompleteFormExample() {
        System.out.println("\n=== Test 09: Complete Form Example ===");
        
        System.out.println("\n📋 COMPLETE BANKING FORM:");
        System.out.println("""
            
            // Text inputs
            page.getByLabel("Full Name").fill("Rajesh Kumar");
            page.getByLabel("Email").fill("rajesh@example.com");
            page.getByLabel("Phone").fill("9876543210");
            
            // Dropdown
            page.getByLabel("Account Type").selectOption("savings");
            
            // Radio button
            page.getByLabel("Individual Account").check();
            
            // Checkboxes
            page.getByLabel("Enable SMS alerts").check();
            page.getByLabel("Enable email alerts").check();
            page.getByLabel("I accept terms").check();
            
            // File upload
            page.getByLabel("Upload ID Proof")
                .setInputFiles(Paths.get("aadhar.pdf"));
            
            // Amount with keyboard
            page.getByLabel("Initial Deposit").fill("10000");
            page.getByLabel("Initial Deposit").press("Control+A");
            page.getByLabel("Initial Deposit").press("Control+C");
            
            // Date picker
            page.getByLabel("Date of Birth").fill("1990-01-15");
            
            // Text area
            page.getByLabel("Additional Notes")
                .fill("Request express processing");
            
            // Submit
            page.getByRole(AriaRole.BUTTON, 
                new Page.GetByRoleOptions().setName("Submit Application"))
                .click();
            
            // Verify success
            page.waitForURL("**/success");
            page.getByText("Application submitted successfully")
                .isVisible();
            """);
        
        System.out.println("✓ Complete form example shown!\n");
    }
    
    @Test
    @Order(10)
    @DisplayName("Test 10: Form interactions summary")
    public void test10_FormInteractionsSummary() {
        System.out.println("\n=== Test 10: Summary ===");
        
        System.out.println("\n📚 FORM INTERACTIONS CHEAT SHEET:");
        System.out.println("""
            
            TEXT INPUT:
            ✅ locator.fill("text")           // Fast, recommended
            ⚠️ locator.type("text")           // Slow, legacy
            ⚠️ locator.pressSequentially()    // Character by character
            ✅ locator.clear()                // Clear input
            
            CHECKBOX:
            ✅ locator.check()                // Ensure checked
            ✅ locator.uncheck()              // Ensure unchecked
            ✅ locator.setChecked(boolean)    // Set specific state
            ⚠️ locator.click()                // Toggle (not recommended)
            
            RADIO:
            ✅ locator.check()                // Select radio button
            
            DROPDOWN:
            ✅ locator.selectOption("value")           // By value
            ✅ locator.selectOption(setLabel("text"))  // By label
            ✅ locator.selectOption(setIndex(0))       // By index
            
            FILE UPLOAD:
            ✅ locator.setInputFiles(path)              // Single file
            ✅ locator.setInputFiles(paths[])           // Multiple
            ✅ locator.setInputFiles(new Path[0])       // Clear
            
            KEYBOARD:
            ✅ locator.press("Enter")                   // Single key
            ✅ locator.press("Control+A")               // Shortcut
            ✅ page.keyboard.press("Escape")            // Global key
            
            SPECIAL KEYS:
            Enter, Tab, Escape, Backspace, Delete,
            ArrowUp, ArrowDown, ArrowLeft, ArrowRight,
            PageUp, PageDown, Home, End,
            F1-F12, Control, Shift, Alt, Meta
            
            BEST PRACTICES:
            ✅ Use fill() for text input (not type)
            ✅ Use check()/uncheck() for checkboxes
            ✅ Use selectOption() for dropdowns
            ✅ Use press() for keyboard interactions
            ✅ Let Playwright auto-wait (don't add explicit waits)
            """);
        
        System.out.println("✓ Summary completed!\n");
    }
}

/*
 * FORM INTERACTIONS REFERENCE:
 * 
 * TEXT INPUT:
 * - fill(text) - Instant (recommended)
 * - type(text) - Character by character (legacy)
 * - pressSequentially(text, options) - With delay
 * - clear() - Clear field
 * 
 * CHECKBOX/RADIO:
 * - check() - Ensure checked
 * - uncheck() - Ensure unchecked
 * - setChecked(boolean) - Set state
 * - isChecked() - Get state
 * 
 * DROPDOWN:
 * - selectOption("value")
 * - selectOption(new SelectOption().setLabel("text"))
 * - selectOption(new SelectOption().setIndex(0))
 * 
 * FILE UPLOAD:
 * - setInputFiles(path)
 * - setInputFiles(paths[])
 * - setInputFiles(new Path[0]) to clear
 * 
 * KEYBOARD:
 * - press(key) - Single key
 * - press("Modifier+Key") - Shortcut
 * - keyboard.press() - Global
 * 
 * SPECIAL KEYS:
 * Enter, Tab, Escape, Backspace, Delete,
 * ArrowUp, ArrowDown, ArrowLeft, ArrowRight,
 * Home, End, PageUp, PageDown,
 * F1-F12, Control, Shift, Alt, Meta
 * 
 * BANKING USE CASES:
 * - Login forms
 * - Registration
 * - Transaction entry
 * - Search and filter
 * - File uploads (KYC, statements)
 * - Keyboard shortcuts
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test05_FormInteractionsAndKeyboard
 */
