package com.npci.training.level5;

import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import com.npci.training.tests.BaseTest;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 * Level 5 - Test 01: TestNG Annotations
 * 
 * Topics Covered:
 * - TestNG vs JUnit differences
 * - @BeforeSuite, @BeforeClass, @BeforeMethod
 * - @AfterSuite, @AfterClass, @AfterMethod
 * - Test execution order
 * - Test priority
 * - Test enable/disable
 * - Test descriptions
 * 
 * Duration: 20 minutes
 */
public class Test01_TestNGAnnotations extends BaseTest {
    
    @Test(priority = 1, description = "Verify login page loads")
    public void test01_LoginPageLoads() {
        System.out.println("\n--- Test 01: Login Page Loads ---");
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Login page displayed");
        
        String heading = loginPage.getHeading();
        assertTrue(heading.contains("Banking Portal"));
        System.out.println("✓ Correct heading: " + heading);
    }
    
    @Test(priority = 2, description = "Verify successful login",
          dependsOnMethods = "test01_LoginPageLoads")
    public void test02_SuccessfulLogin() {
        System.out.println("\n--- Test 02: Successful Login ---");
        
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("✓ Successfully logged in");
        
        String welcome = dashboard.getWelcomeMessage();
        assertNotNull(welcome);
        System.out.println("✓ Welcome message: " + welcome);
    }
    
    @Test(priority = 3, description = "Verify invalid login shows errors")
    public void test03_InvalidLogin() {
        System.out.println("\n--- Test 03: Invalid Login ---");
        
        LoginPage loginPage = new LoginPage(driver)
            .open()
            .clickLoginExpectingError();
        
        assertTrue(loginPage.isUsernameErrorDisplayed());
        System.out.println("✓ Username error displayed");
        
        assertTrue(loginPage.isPasswordErrorDisplayed());
        System.out.println("✓ Password error displayed");
    }
    
    @Test(priority = 4, description = "Navigate to register page")
    public void test04_NavigateToRegister() {
        System.out.println("\n--- Test 04: Navigate to Register ---");
        
        var registerPage = new LoginPage(driver)
            .open()
            .clickRegister();
        
        assertTrue(registerPage.isRegisterPageDisplayed());
        System.out.println("✓ Register page displayed");
    }
    
    @Test(priority = 5, description = "Complete login and logout flow")
    public void test05_LoginLogoutFlow() {
        System.out.println("\n--- Test 05: Login-Logout Flow ---");
        
        // Login
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("✓ Logged in");
        
        // Logout
        LoginPage loginPage = dashboard.logout();
        
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Logged out");
    }
    
    @Test(enabled = false, description = "This test is disabled")
    public void test06_DisabledTest() {
        // This test will not run
        System.out.println("This should not print!");
        fail("This test should not execute!");
    }
    
    @Test(priority = 999, description = "This test always runs last")
    public void test99_AlwaysRunLast() {
        System.out.println("\n--- Test 99: Always Runs Last ---");
        System.out.println("✓ This test has highest priority number");
        System.out.println("✓ Tests execute in priority order (1, 2, 3... 999)");
    }
    
    // BeforeClass runs once before all tests in this class
    @BeforeClass
    public void setupTestData() {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║  Test01_TestNGAnnotations - Setup Complete  ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("BeforeClass: Setup test data for this class");
    }
    
    // AfterClass runs once after all tests in this class
    @AfterClass
    public void cleanupTestData() {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║  Test01_TestNGAnnotations - All Tests Done  ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("AfterClass: Cleanup test data for this class");
    }
    
    // BeforeMethod runs before EACH test
    @BeforeMethod
    public void beforeEachTest() {
        System.out.println("\nBeforeMethod: Setting up for next test...");
    }
    
    // AfterMethod runs after EACH test  
    @AfterMethod
    public void afterEachTest() {
        System.out.println("AfterMethod: Cleaning up after test...");
    }
}
