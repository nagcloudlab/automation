package com.npci.training.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * DashboardPage - Page Object for Dashboard
 * 
 * Represents the main dashboard page after login
 */
public class DashboardPage extends BasePage {
    
    // Navigation links
    private By dashboardLink = By.linkText("Dashboard");
    private By transactionsLink = By.linkText("Transactions");
    private By accountsLink = By.linkText("Accounts");
    private By reportsLink = By.linkText("Reports");
    private By logoutLink = By.linkText("Logout");
    
    // Dashboard elements
    private By welcomeMessage = By.cssSelector("h2");
    private By balanceCard = By.cssSelector(".balance-card");
    private By transactionTable = By.id("transactionTable");
    private By transactionRows = By.cssSelector("#transactionTable tbody tr");
    
    // Constructor
    public DashboardPage(WebDriver driver) {
        super(driver);
    }
    
    // Navigation methods
    public TransactionsPage goToTransactions() {
        clickElement(transactionsLink);
        waitForUrl("transactions.html");
        return new TransactionsPage(driver);
    }
    
    public AccountsPage goToAccounts() {
        clickElement(accountsLink);
        waitForUrl("accounts.html");
        return new AccountsPage(driver);
    }
    
    public ReportsPage goToReports() {
        clickElement(reportsLink);
        waitForUrl("reports.html");
        return new ReportsPage(driver);
    }
    
    public LoginPage logout() {
        clickElement(logoutLink);
        waitForUrl("login.html");
        return new LoginPage(driver);
    }
    
    // Information retrieval methods
    public String getWelcomeMessage() {
        return getElementText(welcomeMessage);
    }
    
    public int getTransactionCount() {
        List<WebElement> rows = driver.findElements(transactionRows);
        return rows.size();
    }
    
    public boolean isTransactionTableDisplayed() {
        return isElementDisplayed(transactionTable);
    }
    
    // Verification methods
    public boolean isDashboardPageDisplayed() {
        return isElementDisplayed(welcomeMessage) && 
               getCurrentUrl().contains("dashboard.html");
    }
    
    public boolean isLogoutLinkVisible() {
        return isElementDisplayed(logoutLink);
    }
}
