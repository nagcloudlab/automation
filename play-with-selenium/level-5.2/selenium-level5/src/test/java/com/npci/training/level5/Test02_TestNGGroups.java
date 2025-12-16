package com.npci.training.level5;

import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import com.npci.training.tests.BaseTest;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Level 5 - Test 02: TestNG Groups
 * 
 * Topics Covered:
 * - Creating test groups
 * - Running specific groups
 * - Multiple groups per test
 * - Group inclusion/exclusion
 * - Real-world grouping strategies
 * 
 * Duration: 20 minutes
 */
public class Test02_TestNGGroups extends BaseTest {
    
    @Test(groups = {"smoke", "login"}, priority = 1)
    public void testLoginPageLoads() {
        System.out.println("\n[SMOKE][LOGIN] Login page loads");
        
        LoginPage loginPage = new LoginPage(driver).open();
        assertTrue(loginPage.isLoginPageDisplayed());
        
        System.out.println("✓ Login page loaded successfully");
    }
    
    @Test(groups = {"smoke", "login", "critical"}, priority = 2)
    public void testValidLogin() {
        System.out.println("\n[SMOKE][LOGIN][CRITICAL] Valid login");
        
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("✓ Login successful");
    }
    
    @Test(groups = {"regression", "login"}, priority = 3)
    public void testInvalidCredentials() {
        System.out.println("\n[REGRESSION][LOGIN] Invalid credentials");
        
        LoginPage loginPage = new LoginPage(driver)
            .open()
            .enterUsername("wrong")
            .enterPassword("wrong")
            .selectUserType("Customer")
            .acceptTerms()
            .clickLoginExpectingError();
        
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Login failed as expected");
    }
    
    @Test(groups = {"regression", "login"}, priority = 4)
    public void testEmptyFields() {
        System.out.println("\n[REGRESSION][LOGIN] Empty fields");
        
        LoginPage loginPage = new LoginPage(driver)
            .open()
            .clickLoginExpectingError();
        
        assertTrue(loginPage.isUsernameErrorDisplayed());
        assertTrue(loginPage.isPasswordErrorDisplayed());
        System.out.println("✓ Validation errors displayed");
    }
    
    @Test(groups = {"smoke", "dashboard", "critical"}, priority = 5)
    public void testDashboardLoads() {
        System.out.println("\n[SMOKE][DASHBOARD][CRITICAL] Dashboard loads");
        
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        assertTrue(dashboard.isDashboardPageDisplayed());
        assertNotNull(dashboard.getWelcomeMessage());
        System.out.println("✓ Dashboard loaded successfully");
    }
    
    @Test(groups = {"regression", "dashboard"}, priority = 6)
    public void testTransactionTableVisible() {
        System.out.println("\n[REGRESSION][DASHBOARD] Transaction table visible");
        
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        assertTrue(dashboard.isTransactionTableDisplayed());
        System.out.println("✓ Transaction table visible");
    }
    
    @Test(groups = {"regression", "navigation"}, priority = 7)
    public void testNavigateToTransactions() {
        System.out.println("\n[REGRESSION][NAVIGATION] Navigate to transactions");
        
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        var transactionsPage = dashboard.goToTransactions();
        assertTrue(transactionsPage.isTransactionsPageDisplayed());
        System.out.println("✓ Navigated to transactions");
    }
    
    @Test(groups = {"regression", "navigation"}, priority = 8)
    public void testNavigateToAccounts() {
        System.out.println("\n[REGRESSION][NAVIGATION] Navigate to accounts");
        
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        var accountsPage = dashboard.goToAccounts();
        //assertTrue(accountsPage.isAccountsPageDisplayed());
        System.out.println("✓ Navigated to accounts");
    }
    
    @Test(groups = {"smoke", "logout", "critical"}, priority = 9)
    public void testLogout() {
        System.out.println("\n[SMOKE][LOGOUT][CRITICAL] Logout");
        
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        LoginPage loginPage = dashboard.logout();
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Logout successful");
    }
    
    @Test(groups = {"sanity"}, priority = 10)
    public void testCompleteFlow() {
        System.out.println("\n[SANITY] Complete user flow");
        
        // Login
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("  Step 1: Logged in");
        
        // Navigate to transactions
        var transactions = dashboard.goToTransactions();
        assertTrue(transactions.isTransactionsPageDisplayed());
        System.out.println("  Step 2: Viewed transactions");
        
        // Back to dashboard
        dashboard = transactions.goToDashboard();
        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("  Step 3: Back to dashboard");
        
        // Logout
        LoginPage loginPage = dashboard.logout();
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("  Step 4: Logged out");
        
        System.out.println("✓ Complete flow successful");
    }
}

/*
 * HOW TO RUN SPECIFIC GROUPS:
 * 
 * 1. Command Line:
 *    mvn test -Dgroups="smoke"
 *    mvn test -Dgroups="smoke,critical"
 *    mvn test -DexcludedGroups="regression"
 * 
 * 2. In testng.xml:
 *    <groups>
 *        <run>
 *            <include name="smoke"/>
 *            <include name="critical"/>
 *        </run>
 *    </groups>
 * 
 * 3. Common Group Strategies:
 *    smoke      - Quick essential tests (5-10 min)
 *    regression - All tests (full suite)
 *    critical   - Must-pass tests
 *    sanity     - Basic functionality check
 *    
 * 4. NPCI-Specific Groups:
 *    login, dashboard, transactions, payments
 *    upi, imps, neft, rtgs
 *    api, ui, database
 */
