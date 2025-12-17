package com.npci.training.level2;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.MouseButton;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Playwright Level 2 - Test 04: User Actions
 * 
 * Topics Covered:
 * - Click variations (click, dblclick, right-click)
 * - Hover actions
 * - Drag and drop
 * - Mouse coordinates
 * - Force click
 * - Click options (position, modifiers)
 * 
 * Duration: 30 minutes
 */
@DisplayName("User Actions - Clicks, Hover, Drag & Drop")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test04_UserActions extends BaseTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: Basic click")
    public void test01_BasicClick() {
        System.out.println("\n=== Test 01: Basic Click ===");
        
        page.navigate("https://the-internet.herokuapp.com/");
        
        // Simple click
        System.out.println("1. Basic click on link");
        page.locator("text='A/B Testing'").click();
        page.waitForURL("**/abtest");
        System.out.println("✓ Clicked and navigated");
        
        // Go back
        page.goBack();
        
        // Click with wait
        System.out.println("2. Click with auto-wait");
        page.locator("text='Checkboxes'").click();
        System.out.println("✓ Playwright auto-waited before click");
        
        System.out.println("✓ Basic click test passed!\n");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: Double click")
    public void test02_DoubleClick() {
        System.out.println("\n=== Test 02: Double Click ===");
        
        System.out.println("💡 Double Click Example:");
        System.out.println("""
            
            // Double click on element
            page.locator("#element").dblclick();
            
            // With options
            page.locator("#element").dblclick(
                new Locator.DblclickOptions()
                    .setDelay(100)     // Delay between clicks
                    .setButton(MouseButton.LEFT)
            );
            
            Use Cases:
            - Select word in text editor
            - Open file in file explorer
            - Zoom in maps
            """);
        
        System.out.println("✓ Double click concept demonstrated!\n");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: Right click (context menu)")
    public void test03_RightClick() {
        System.out.println("\n=== Test 03: Right Click ===");
        
        page.navigate("https://the-internet.herokuapp.com/context_menu");
        
        // Right click
        System.out.println("1. Right click on element");
        page.locator("#hot-spot").click(
            new Locator.ClickOptions()
                .setButton(MouseButton.RIGHT)
        );
        
        // Handle alert
        page.onDialog(dialog -> {
            System.out.println("✓ Alert appeared: " + dialog.message());
            dialog.accept();
        });
        
        page.waitForTimeout(1000); // Wait for alert
        System.out.println("✓ Right click successful");
        
        System.out.println("✓ Right click test passed!\n");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: Click with modifiers")
    public void test04_ClickWithModifiers() {
        System.out.println("\n=== Test 04: Click with Modifiers ===");
        
        System.out.println("💡 Click with Keyboard Modifiers:");
        System.out.println("""
            
            // Ctrl + Click (new tab)
            page.locator("a").click(
                new Locator.ClickOptions()
                    .setModifiers(Arrays.asList(KeyboardModifier.CONTROL))
            );
            
            // Shift + Click (range selection)
            page.locator(".item").first().click();
            page.locator(".item").last().click(
                new Locator.ClickOptions()
                    .setModifiers(Arrays.asList(KeyboardModifier.SHIFT))
            );
            
            // Alt + Click
            page.locator("button").click(
                new Locator.ClickOptions()
                    .setModifiers(Arrays.asList(KeyboardModifier.ALT))
            );
            
            Modifiers:
            - CONTROL (Ctrl/Cmd)
            - SHIFT
            - ALT
            - META (Windows key)
            """);
        
        System.out.println("✓ Click modifiers demonstrated!\n");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: Click at specific position")
    public void test05_ClickAtPosition() {
        System.out.println("\n=== Test 05: Click at Position ===");
        
        System.out.println("💡 Click at Specific Coordinates:");
        System.out.println("""
            
            // Click at center (default)
            page.locator("#element").click();
            
            // Click at top-left corner
            page.locator("#element").click(
                new Locator.ClickOptions()
                    .setPosition(0, 0)
            );
            
            // Click at specific offset
            page.locator("#element").click(
                new Locator.ClickOptions()
                    .setPosition(10, 20)  // 10px right, 20px down
            );
            
            Use Cases:
            - Canvas/drawing apps
            - Map applications
            - Image editors
            - Graph interactions
            """);
        
        System.out.println("✓ Position click demonstrated!\n");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: Hover action")
    public void test06_HoverAction() {
        System.out.println("\n=== Test 06: Hover Action ===");
        
        page.navigate("https://the-internet.herokuapp.com/hovers");
        
        // Hover over first image
        System.out.println("1. Hover over element");
        Locator firstImage = page.locator(".figure").first();
        firstImage.hover();
        System.out.println("✓ Hovered over first image");
        
        // Verify caption appears
        page.waitForTimeout(500);
        Locator caption = firstImage.locator(".figcaption");
        assertTrue(caption.isVisible());
        System.out.println("✓ Caption appeared on hover");
        
        // Hover over second image
        System.out.println("2. Hover over another element");
        page.locator(".figure").nth(1).hover();
        System.out.println("✓ Hovered over second image");
        
        System.out.println("✓ Hover test passed!\n");
    }
    
    @Test
    @Order(7)
    @DisplayName("Test 7: Drag and drop")
    public void test07_DragAndDrop() {
        System.out.println("\n=== Test 07: Drag and Drop ===");
        
        page.navigate("https://the-internet.herokuapp.com/drag_and_drop");
        
        // Get initial state
        String columnAText = page.locator("#column-a header").textContent();
        String columnBText = page.locator("#column-b header").textContent();
        System.out.println("Before: A=" + columnAText + ", B=" + columnBText);
        
        // Drag A to B
        System.out.println("1. Dragging column A to column B...");
        page.locator("#column-a").dragTo(page.locator("#column-b"));
        
        page.waitForTimeout(500);
        
        // Verify swap
        String newColumnAText = page.locator("#column-a header").textContent();
        String newColumnBText = page.locator("#column-b header").textContent();
        System.out.println("After: A=" + newColumnAText + ", B=" + newColumnBText);
        
        assertEquals(columnBText, newColumnAText);
        assertEquals(columnAText, newColumnBText);
        System.out.println("✓ Drag and drop successful");
        
        System.out.println("✓ Drag and drop test passed!\n");
    }
    
    @Test
    @Order(8)
    @DisplayName("Test 8: Force click (bypass actionability checks)")
    public void test08_ForceClick() {
        System.out.println("\n=== Test 08: Force Click ===");
        
        System.out.println("💡 Force Click - Bypass Actionability:");
        System.out.println("""
            
            // Normal click (waits for element to be actionable)
            page.locator("#element").click();
            
            // Force click (skips actionability checks)
            page.locator("#element").click(
                new Locator.ClickOptions()
                    .setForce(true)
            );
            
            When to use Force:
            ⚠️ Element is covered by another element
            ⚠️ Element is not visible but exists in DOM
            ⚠️ Custom clickable elements without proper ARIA
            
            Warning:
            - Use sparingly!
            - May click elements users can't click
            - Hides real UI issues
            - Debug first, force last!
            
            Banking Example:
            // Modal overlay blocks button
            page.locator("#confirm-btn").click(
                new Locator.ClickOptions().setForce(true)
            );
            """);
        
        System.out.println("✓ Force click concept demonstrated!\n");
    }
    
    @Test
    @Order(9)
    @DisplayName("Test 9: Click combinations for banking")
    public void test09_BankingClickExamples() {
        System.out.println("\n=== Test 09: Banking Portal Click Examples ===");
        
        System.out.println("\n💰 BANKING PORTAL SCENARIOS:");
        System.out.println("""
            
            // 1. Login button - Simple click
            page.getByRole(AriaRole.BUTTON, 
                new Page.GetByRoleOptions().setName("Login"))
                .click();
            
            // 2. Transaction row - Ctrl+Click to open in new tab
            page.locator(".transaction-row").first().click(
                new Locator.ClickOptions()
                    .setModifiers(Arrays.asList(KeyboardModifier.CONTROL))
            );
            
            // 3. Account dropdown - Hover to expand
            page.locator("#account-menu").hover();
            page.getByText("Savings Account").click();
            
            // 4. Drag transaction to category
            page.locator(".transaction-item").first()
                .dragTo(page.locator(".category-food"));
            
            // 5. Right-click for options
            page.locator(".transaction-row").first().click(
                new Locator.ClickOptions()
                    .setButton(MouseButton.RIGHT)
            );
            
            // 6. Select multiple transactions (Shift+Click)
            page.locator(".transaction-checkbox").first().click();
            page.locator(".transaction-checkbox").last().click(
                new Locator.ClickOptions()
                    .setModifiers(Arrays.asList(KeyboardModifier.SHIFT))
            );
            
            // 7. Click chart at specific point
            page.locator("#expense-chart").click(
                new Locator.ClickOptions()
                    .setPosition(150, 200)
            );
            
            // 8. Double-click to edit amount
            page.locator(".editable-amount").dblclick();
            page.locator("#amount-input").fill("5000");
            """);
        
        System.out.println("✓ Banking examples completed!\n");
    }
    
    @Test
    @Order(10)
    @DisplayName("Test 10: Click options summary")
    public void test10_ClickOptionsSummary() {
        System.out.println("\n=== Test 10: Click Options Summary ===");
        
        System.out.println("\n📚 ALL CLICK OPTIONS:");
        System.out.println("""
            
            page.locator("#element").click(
                new Locator.ClickOptions()
                    // Which button to click
                    .setButton(MouseButton.LEFT)     // LEFT, RIGHT, MIDDLE
                    
                    // Click count
                    .setClickCount(1)                // 1=click, 2=dblclick, 3=triple
                    
                    // Delay between mousedown and mouseup
                    .setDelay(100)                   // Milliseconds
                    
                    // Force click (skip actionability)
                    .setForce(true)                  // Default: false
                    
                    // Keyboard modifiers
                    .setModifiers(Arrays.asList(
                        KeyboardModifier.CONTROL,
                        KeyboardModifier.SHIFT
                    ))
                    
                    // Click position (relative to element)
                    .setPosition(10, 20)             // x, y pixels
                    
                    // Timeout
                    .setTimeout(30000)               // Milliseconds
                    
                    // Trial run (don't actually click)
                    .setTrial(true)                  // Default: false
                    
                    // No wait after click
                    .setNoWaitAfter(true)            // Default: false
            );
            
            Most Common:
            .click()                           // Simple click
            .click(setButton(RIGHT))           // Right-click
            .click(setModifiers(CONTROL))      // Ctrl+Click
            .click(setForce(true))             // Force click
            .dblclick()                        // Double-click
            """);
        
        System.out.println("✓ Options summary completed!\n");
    }
}

/*
 * USER ACTIONS REFERENCE:
 * 
 * 1. CLICK VARIATIONS:
 *    locator.click()                    // Left click
 *    locator.dblclick()                 // Double click
 *    locator.click(setButton(RIGHT))    // Right click
 *    locator.click(setButton(MIDDLE))   // Middle click
 * 
 * 2. HOVER:
 *    locator.hover()                    // Hover over element
 *    locator.hover(setPosition(x, y))   // Hover at position
 * 
 * 3. DRAG AND DROP:
 *    source.dragTo(target)              // Drag to target
 * 
 * 4. MODIFIERS:
 *    .setModifiers(CONTROL)             // Ctrl/Cmd
 *    .setModifiers(SHIFT)               // Shift
 *    .setModifiers(ALT)                 // Alt
 *    .setModifiers(META)                // Windows key
 * 
 * 5. POSITION:
 *    .setPosition(x, y)                 // Click at offset
 * 
 * 6. FORCE:
 *    .setForce(true)                    // Skip actionability
 * 
 * PLAYWRIGHT AUTO-ACTIONABILITY:
 * Before click, Playwright waits for:
 * ✅ Element attached to DOM
 * ✅ Element visible
 * ✅ Element stable (not animating)
 * ✅ Element enabled
 * ✅ Element not covered
 * 
 * This is AUTOMATIC! No explicit waits needed!
 * 
 * COMMON PATTERNS:
 * 
 * Simple Click:
 *   page.locator("button").click();
 * 
 * Right-Click Menu:
 *   page.locator("#item").click(setButton(RIGHT));
 *   page.locator("text='Delete'").click();
 * 
 * Ctrl+Click (New Tab):
 *   page.locator("a").click(setModifiers(CONTROL));
 * 
 * Hover then Click:
 *   page.locator("#menu").hover();
 *   page.locator("#submenu-item").click();
 * 
 * Drag to Reorder:
 *   page.locator(".item-1").dragTo(page.locator(".position-5"));
 * 
 * BANKING USE CASES:
 * - Click: Buttons, links, checkboxes
 * - Double-click: Edit inline amounts
 * - Right-click: Context menus
 * - Hover: Show tooltips, expand menus
 * - Drag: Categorize transactions
 * - Ctrl+Click: Open transaction in new tab
 * - Shift+Click: Select range of items
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test04_UserActions
 */
