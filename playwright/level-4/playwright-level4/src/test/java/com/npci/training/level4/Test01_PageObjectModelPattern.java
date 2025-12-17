package com.npci.training.level4;

import com.npci.training.pages.*;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;

/**
 * Level 4 - Test 01: Page Object Model Pattern Demo
 * 
 * Topics:
 * - POM basics
 * - Method chaining
 * - Page navigation
 * - Readable test code
 * 
 * Duration: 30 minutes
 */
@DisplayName("Page Object Model Pattern")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test01_PageObjectModelPattern extends BaseTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: Without POM (bad practice)")
    public void test01_WithoutPOM() {
        System.out.println("\n=== Test 01: WITHOUT Page Object Model ===");
        
        System.out.println("\n❌ BAD PRACTICE: Direct locator usage in test");
        System.out.println("""
            
            @Test
            void testLoginWithoutPOM() {
                // All locators in test - BAD!
                page.navigate("https://example.com/login");
                page.getByLabel("Username").fill("admin");
                page.getByLabel("Password").fill("password");
                page.getByRole(AriaRole.BUTTON, 
                    new Page.GetByRoleOptions().setName("Login")).click();
                
                // Verification in test
                assertThat(page.getByRole(AriaRole.HEADING, 
                    new Page.GetByRoleOptions().setName("Dashboard")))
                    .isVisible();
                
                assertThat(page.getByTestId("account-balance"))
                    .isVisible();
            }
            
            PROBLEMS:
            ❌ Locators scattered in tests
            ❌ Hard to maintain
            ❌ Code duplication
            ❌ Not reusable
            ❌ Tests break when UI changes
            ❌ Unreadable
            """);
        
        System.out.println("✓ Bad practice explained!\n");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: With POM (best practice)")
    public void test02_WithPOM() {
        System.out.println("\n=== Test 02: WITH Page Object Model ===");
        
        System.out.println("\n✅ BEST PRACTICE: Using Page Objects");
        System.out.println("""
            
            @Test
            void testLoginWithPOM() {
                // Clean, readable, maintainable!
                new LoginPage(page)
                    .navigate()
                    .login("admin", "password123")
                    .verifyDashboardDisplayed()
                    .verifyBalanceDisplayed();
            }
            
            BENEFITS:
            ✅ Locators in page objects
            ✅ Easy to maintain
            ✅ Reusable methods
            ✅ Tests stay clean
            ✅ One place to update
            ✅ Very readable
            """);
        
        System.out.println("✓ Best practice explained!\n");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: Method chaining demo")
    public void test03_MethodChaining() {
        System.out.println("\n=== Test 03: Method Chaining ===");
        
        System.out.println("\n🔗 METHOD CHAINING PATTERN:");
        System.out.println("""
            
            // Each method returns 'this' for chaining
            loginPage
                .navigate()
                .enterUsername("admin")
                .enterPassword("password123")
                .verifyLoginPageDisplayed();
            
            // Fluent, readable API
            loginPage
                .navigate()
                .login("admin", "password")
                .verifyDashboardDisplayed();
            
            // Clean and expressive!
            """);
        
        System.out.println("✓ Method chaining explained!\n");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: Page navigation pattern")
    public void test04_PageNavigation() {
        System.out.println("\n=== Test 04: Page Navigation ===");
        
        System.out.println("\n🔄 PAGE NAVIGATION PATTERN:");
        System.out.println("""
            
            // Methods return next page object
            
            LoginPage loginPage = new LoginPage(page);
            DashboardPage dashboard = loginPage
                .navigate()
                .login("admin", "password");
                // ↑ Returns DashboardPage
            
            TransferPage transfer = dashboard.goToTransfer();
                // ↑ Returns TransferPage
            
            transfer
                .doTransfer("savings", "9876543210", "5000", "Rent")
                .verifyTransferSuccessful();
            
            // Type-safe navigation through pages!
            """);
        
        System.out.println("✓ Page navigation explained!\n");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: Complete banking flow with POM")
    public void test05_CompleteBankingFlow() {
        System.out.println("\n=== Test 05: Complete Banking Flow ===");
        
        System.out.println("\n💰 COMPLETE FLOW WITH POM:");
        System.out.println("""
            
            // Login
            DashboardPage dashboard = new LoginPage(page)
                .navigate()
                .login("rajesh.kumar", "SecurePass123!")
                .verifyDashboardDisplayed();
            
            // Navigate to transfer
            TransferPage transfer = dashboard.goToTransfer();
            
            // Do transfer
            transfer
                .verifyTransferPageDisplayed()
                .doTransfer("savings", "9876543210", "5000", "Rent payment")
                .verifyTransferSuccessful();
            
            // Get transaction ID
            String txnId = transfer.getTransactionId();
            System.out.println("Transaction ID: " + txnId);
            
            // Back to dashboard
            dashboard = transfer.backToDashboard();
            dashboard.verifyDashboardDisplayed();
            
            // Logout
            LoginPage loginPage = dashboard.logout();
            loginPage.verifyLoginPageDisplayed();
            
            // Clean, readable, maintainable! ✨
            """);
        
        System.out.println("✓ Complete flow demonstrated!\n");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: Component composition pattern")
    public void test06_ComponentComposition() {
        System.out.println("\n=== Test 06: Component Composition ===");
        
        System.out.println("\n🧩 COMPONENT COMPOSITION:");
        System.out.println("""
            
            // Pages use reusable components
            
            DashboardPage dashboard = loginPage
                .navigate()
                .login("admin", "password");
            
            // Use header component
            dashboard.getHeader().verifyHeaderDisplayed();
            dashboard.getHeader().navigateToMenuItem("Settings");
            
            // Logout using header
            LoginPage loginPage = dashboard.getHeader().logout();
            
            BENEFITS:
            ✅ Reusable components (Header, Footer, Modal)
            ✅ DRY principle
            ✅ Consistent behavior
            ✅ Easy to maintain
            
            STRUCTURE:
            DashboardPage
              ├── HeaderComponent (shared)
              ├── DashboardContent
              └── FooterComponent (shared)
            
            TransferPage
              ├── HeaderComponent (same!)
              ├── TransferForm
              └── FooterComponent (same!)
            """);
        
        System.out.println("✓ Component composition explained!\n");
    }
    
    @Test
    @Order(7)
    @DisplayName("Test 7: POM benefits summary")
    public void test07_POMBenefits() {
        System.out.println("\n=== Test 07: POM Benefits ===");
        
        System.out.println("\n🎯 PAGE OBJECT MODEL BENEFITS:");
        System.out.println("""
            
            1. MAINTAINABILITY:
               - Locators in one place
               - Change once, affects all tests
               - Easy to update
            
            2. REUSABILITY:
               - Methods used across tests
               - No code duplication
               - DRY principle
            
            3. READABILITY:
               - Tests read like documentation
               - Business language
               - Self-documenting code
            
            4. ABSTRACTION:
               - Tests don't know about locators
               - Hide implementation details
               - Focus on business logic
            
            5. TYPE SAFETY:
               - Page navigation is type-safe
               - IDE autocomplete
               - Compile-time checking
            
            6. TESTABILITY:
               - Easy to write tests
               - Quick to add new tests
               - Consistent pattern
            
            COMPARISON:
            
            Without POM:
            - 50 lines of test code
            - Locators scattered
            - Hard to maintain
            - Code duplication
            
            With POM:
            - 5 lines of test code
            - Locators centralized
            - Easy to maintain
            - Reusable methods
            
            Result: 90% less code! 🎉
            """);
        
        System.out.println("✓ Benefits explained!\n");
    }
    
    @Test
    @Order(8)
    @DisplayName("Test 8: POM structure summary")
    public void test08_POMStructure() {
        System.out.println("\n=== Test 08: POM Structure ===");
        
        System.out.println("\n📁 PROJECT STRUCTURE:");
        System.out.println("""
            
            src/
            ├── main/java/
            │   └── com.npci.training/
            │       ├── pages/
            │       │   ├── BasePage.java         (parent class)
            │       │   ├── LoginPage.java        (page object)
            │       │   ├── DashboardPage.java    (page object)
            │       │   └── TransferPage.java     (page object)
            │       └── components/
            │           ├── HeaderComponent.java  (reusable)
            │           └── FooterComponent.java  (reusable)
            └── test/java/
                └── com.npci.training/
                    ├── tests/
                    │   └── BaseTest.java         (test base)
                    └── level4/
                        └── Test01_*.java         (actual tests)
            
            NAMING CONVENTIONS:
            - Page Objects: [PageName]Page.java
            - Components: [ComponentName]Component.java
            - Tests: Test[Number]_[Description].java
            - Base classes: Base[Type].java
            
            PACKAGE STRUCTURE:
            - pages: All page objects
            - components: Reusable components
            - tests: Test base classes
            - level4: Actual test classes
            """);
        
        System.out.println("✓ Structure explained!\n");
    }
}

/*
 * PAGE OBJECT MODEL SUMMARY:
 * 
 * 1. WHAT IS POM?
 *    - Design pattern for test automation
 *    - Separate page logic from tests
 *    - Locators in page objects
 *    - Tests use page methods
 * 
 * 2. KEY COMPONENTS:
 *    - BasePage: Common functionality
 *    - Page Objects: Represent pages
 *    - Components: Reusable UI parts
 *    - Tests: Use page objects
 * 
 * 3. BENEFITS:
 *    ✅ 90% less code
 *    ✅ Easy maintenance
 *    ✅ Reusable
 *    ✅ Readable
 *    ✅ Type-safe
 * 
 * 4. PATTERNS:
 *    - Method chaining (return this)
 *    - Page navigation (return new page)
 *    - Component composition
 *    - Fluent API
 * 
 * 5. BEST PRACTICES:
 *    ✅ One page object per page
 *    ✅ Locators private
 *    ✅ Methods public
 *    ✅ Return this or next page
 *    ✅ Keep assertions in pages
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test01_PageObjectModelPattern
 */
