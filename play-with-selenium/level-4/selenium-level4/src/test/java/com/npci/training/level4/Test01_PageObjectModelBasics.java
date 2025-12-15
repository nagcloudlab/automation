package com.npci.training.level4;

import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import com.npci.training.pages.RegisterPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 4 - Test 01: Page Object Model Basics
 * 
 * Topics Covered:
 * - What is Page Object Model (POM)
 * - Why use POM
 * - Creating Page Objects
 * - Using Page Objects in tests
 * - Method chaining
 * - Benefits of POM
 * 
 * Duration: 20 minutes
 */
public class Test01_PageObjectModelBasics {

    WebDriver driver;
    
    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @DisplayName("POM: Basic login using Page Objects")
    public void testBasicLoginWithPOM() {
        System.out.println("\n=== Basic Login with POM ===");
        
        // Create Page Object
        LoginPage loginPage = new LoginPage(driver);
        
        // Navigate to page
        loginPage.open();
        System.out.println("✓ Opened login page");
        
        // Perform login
        loginPage.enterUsername("admin");
        System.out.println("✓ Entered username");
        
        loginPage.enterPassword("admin123");
        System.out.println("✓ Entered password");
        
        loginPage.selectUserType("Customer");
        System.out.println("✓ Selected user type");
        
        loginPage.acceptTerms();
        System.out.println("✓ Accepted terms");
        
        DashboardPage dashboard = loginPage.clickLogin();
        System.out.println("✓ Clicked login");
        
        // Verify
        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("✓ Dashboard displayed");
        
        System.out.println("\n💡 Notice how clean and readable the test is!");
    }
    
    @Test
    @DisplayName("POM: Login with method chaining")
    public void testLoginWithMethodChaining() {
        System.out.println("\n=== Login with Method Chaining ===");
        
        // All actions in one fluent chain!
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .enterUsername("admin")
            .enterPassword("admin123")
            .selectUserType("Customer")
            .acceptTerms()
            .clickLogin();
        
        System.out.println("✓ Logged in using method chaining");
        
        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("✓ Dashboard displayed");
        
        System.out.println("\n💡 Method chaining makes code concise and elegant!");
    }
    
    @Test
    @DisplayName("POM: Complete login in one method")
    public void testCompleteLoginMethod() {
        System.out.println("\n=== Complete Login Method ===");
        
        // Even simpler - use the complete login method
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        System.out.println("✓ Logged in using loginAs() method");
        
        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("✓ Verification successful");
        
        System.out.println("\n💡 Page Objects can provide high-level business methods!");
    }
    
    @Test
    @DisplayName("POM: Validation error testing")
    public void testValidationErrors() {
        System.out.println("\n=== Validation Errors with POM ===");
        
        LoginPage loginPage = new LoginPage(driver)
            .open()
            .clickLoginExpectingError();
        
        System.out.println("✓ Clicked login with empty fields");
        
        // Check errors using Page Object methods
        assertTrue(loginPage.isUsernameErrorDisplayed());
        System.out.println("✓ Username error displayed");
        System.out.println("  Error: " + loginPage.getUsernameError());
        
        assertTrue(loginPage.isPasswordErrorDisplayed());
        System.out.println("✓ Password error displayed");
        System.out.println("  Error: " + loginPage.getPasswordError());
        
        System.out.println("\n💡 Page Objects encapsulate validation logic!");
    }
    
    @Test
    @DisplayName("POM: Navigation between pages")
    public void testPageNavigation() {
        System.out.println("\n=== Page Navigation with POM ===");
        
        // Start at login page
        LoginPage loginPage = new LoginPage(driver).open();
        System.out.println("Step 1: On login page");
        
        // Navigate to register
        RegisterPage registerPage = loginPage.clickRegister();
        assertTrue(registerPage.isRegisterPageDisplayed());
        System.out.println("Step 2: Navigated to register page");
        
        // Navigate back to login
        loginPage = registerPage.clickLoginLink();
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("Step 3: Back to login page");
        
        System.out.println("\n✓ Page navigation using POM!");
        System.out.println("💡 Each method returns the appropriate Page Object!");
    }
    
    @Test
    @DisplayName("POM: Complete user journey")
    public void testCompleteUserJourney() {
        System.out.println("\n=== Complete User Journey ===");
        
        // Login
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        System.out.println("Step 1: Logged in ✓");
        assertTrue(dashboard.isDashboardPageDisplayed());
        
        // Check welcome message
        String welcome = dashboard.getWelcomeMessage();
        System.out.println("Step 2: Welcome message: " + welcome);
        
        // Go to transactions
        var transactionsPage = dashboard.goToTransactions();
        assertTrue(transactionsPage.isTransactionsPageDisplayed());
        System.out.println("Step 3: Viewed transactions ✓");
        
        // Back to dashboard
        dashboard = transactionsPage.goToDashboard();
        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("Step 4: Back to dashboard ✓");
        
        // Go to accounts
        var accountsPage = dashboard.goToAccounts();
        assertTrue(accountsPage.isAccountsPageDisplayed());
        System.out.println("Step 5: Viewed accounts ✓");
        
        // Back and logout
        dashboard = accountsPage.goToDashboard();
        LoginPage loginPage = dashboard.logout();
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("Step 6: Logged out ✓");
        
        System.out.println("\n✓ Complete journey using Page Objects!");
        System.out.println("💡 Notice how readable and maintainable this is!");
    }
    
    @Test
    @DisplayName("POM vs Non-POM comparison")
    public void testPOMvsNonPOM() {
        System.out.println("\n=== POM vs Non-POM Comparison ===");
        
        System.out.println("\n❌ Without POM (Old way):");
        System.out.println("  driver.get(\"http://localhost:8000/login.html\");");
        System.out.println("  driver.findElement(By.id(\"username\")).sendKeys(\"admin\");");
        System.out.println("  driver.findElement(By.id(\"password\")).sendKeys(\"pass\");");
        System.out.println("  // ... lots of repetitive code");
        System.out.println("  // Locators scattered everywhere");
        System.out.println("  // Hard to maintain");
        
        System.out.println("\n✅ With POM (New way):");
        System.out.println("  new LoginPage(driver)");
        System.out.println("      .open()");
        System.out.println("      .loginAs(\"admin\", \"pass\", \"Customer\");");
        System.out.println("  // Clean, readable, maintainable");
        System.out.println("  // Locators centralized in Page Objects");
        System.out.println("  // Easy to update");
        
        System.out.println("\n✅ Benefits of POM:");
        System.out.println("  1. Code Reusability");
        System.out.println("  2. Better Readability");
        System.out.println("  3. Easy Maintenance");
        System.out.println("  4. Separation of Concerns");
        System.out.println("  5. Reduced Code Duplication");
    }
    
    @Test
    @DisplayName("POM best practices")
    public void testPOMBestPractices() {
        System.out.println("\n=== POM Best Practices ===");
        
        System.out.println("\n📋 DO's:");
        System.out.println("  1. Keep locators private in Page Objects");
        System.out.println("  2. Return Page Objects for method chaining");
        System.out.println("  3. Create one Page Object per page");
        System.out.println("  4. Use meaningful method names");
        System.out.println("  5. Add verification methods");
        System.out.println("  6. Use BasePage for common functionality");
        
        System.out.println("\n📋 DON'Ts:");
        System.out.println("  1. Don't put assertions in Page Objects");
        System.out.println("  2. Don't make locators public");
        System.out.println("  3. Don't put test logic in Page Objects");
        System.out.println("  4. Don't create god objects (too many methods)");
        
        System.out.println("\n💡 Structure:");
        System.out.println("  BasePage (common methods)");
        System.out.println("     ↓ extends");
        System.out.println("  LoginPage, DashboardPage, etc.");
        System.out.println("     ↓ used by");
        System.out.println("  Tests (assertions and verifications)");
    }
}
