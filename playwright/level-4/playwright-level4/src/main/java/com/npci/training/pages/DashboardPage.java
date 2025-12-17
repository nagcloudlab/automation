package com.npci.training.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.npci.training.components.HeaderComponent;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * DashboardPage - Page Object for Dashboard
 * 
 * Represents user dashboard after login
 */
public class DashboardPage extends BasePage {
    
    // Components
    private final HeaderComponent header;
    
    // Locators
    private final Locator heading;
    private final Locator accountBalance;
    private final Locator recentTransactions;
    private final Locator transferButton;
    private final Locator viewStatementButton;
    private final Locator quickLinksSection;
    
    // Constructor
    public DashboardPage(Page page) {
        super(page);
        
        // Initialize component
        this.header = new HeaderComponent(page);
        
        // Initialize locators
        this.heading = page.getByRole(AriaRole.HEADING, 
            new Page.GetByRoleOptions().setName("Dashboard"));
        this.accountBalance = page.getByTestId("account-balance");
        this.recentTransactions = page.locator(".recent-transactions");
        this.transferButton = page.getByRole(AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Transfer Money"));
        this.viewStatementButton = page.getByRole(AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("View Statement"));
        this.quickLinksSection = page.locator(".quick-links");
    }
    
    /**
     * Get header component
     * @return HeaderComponent
     */
    public HeaderComponent getHeader() {
        return header;
    }
    
    /**
     * Get account balance
     * @return Balance as string
     */
    public String getAccountBalance() {
        return accountBalance.textContent();
    }
    
    /**
     * Check if dashboard is displayed
     * @return true if displayed
     */
    public boolean isDashboardDisplayed() {
        return heading.isVisible() && accountBalance.isVisible();
    }
    
    /**
     * Navigate to transfer page
     * @return TransferPage
     */
    public TransferPage goToTransfer() {
        transferButton.click();
        return new TransferPage(page);
    }
    
    /**
     * Click view statement
     */
    public void viewStatement() {
        viewStatementButton.click();
    }
    
    /**
     * Get recent transactions count
     * @return Number of recent transactions
     */
    public int getRecentTransactionsCount() {
        return recentTransactions.locator(".transaction-item").count();
    }
    
    /**
     * Get specific transaction text
     * @param index Transaction index (0-based)
     * @return Transaction text
     */
    public String getTransactionText(int index) {
        return recentTransactions
            .locator(".transaction-item")
            .nth(index)
            .textContent();
    }
    
    /**
     * Check if quick links are visible
     * @return true if visible
     */
    public boolean areQuickLinksVisible() {
        return quickLinksSection.isVisible();
    }
    
    /**
     * Click quick link by name
     * @param linkName Link name
     */
    public void clickQuickLink(String linkName) {
        quickLinksSection
            .getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(linkName))
            .click();
    }
    
    /**
     * Verify dashboard is displayed
     * @return this for method chaining
     */
    public DashboardPage verifyDashboardDisplayed() {
        assertThat(heading).isVisible();
        assertThat(accountBalance).isVisible();
        assertThat(page).hasURL(java.util.regex.Pattern.compile(".*/dashboard"));
        return this;
    }
    
    /**
     * Verify account balance is shown
     * @return this for method chaining
     */
    public DashboardPage verifyBalanceDisplayed() {
        assertThat(accountBalance).isVisible();
        assertThat(accountBalance).containsText("₹");
        return this;
    }
    
    /**
     * Verify recent transactions section exists
     * @return this for method chaining
     */
    public DashboardPage verifyRecentTransactionsDisplayed() {
        assertThat(recentTransactions).isVisible();
        return this;
    }
    
    /**
     * Logout (using header component)
     * @return LoginPage
     */
    public LoginPage logout() {
        return header.logout();
    }
}

/*
 * DASHBOARD PAGE PATTERN:
 * 
 * 1. COMPONENT COMPOSITION:
 *    - Uses HeaderComponent (composition)
 *    - Delegates header actions to component
 *    - Keeps page object focused
 * 
 * 2. NAVIGATION METHODS:
 *    public TransferPage goToTransfer() {
 *        transferButton.click();
 *        return new TransferPage(page);
 *    }
 *    // Returns next page object
 * 
 * 3. QUERY METHODS:
 *    - getAccountBalance()
 *    - getRecentTransactionsCount()
 *    - Return data, not locators
 * 
 * 4. VERIFICATION METHODS:
 *    - verifyDashboardDisplayed()
 *    - verifyBalanceDisplayed()
 *    - Keep assertions here
 * 
 * 5. USAGE:
 *    DashboardPage dashboard = loginPage.login("user", "pass");
 *    dashboard
 *        .verifyDashboardDisplayed()
 *        .verifyBalanceDisplayed();
 *    
 *    TransferPage transfer = dashboard.goToTransfer();
 */
