package com.npci.training.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * TransactionsPage - Page Object for Transactions Page
 */
public class TransactionsPage extends BasePage {
    
    private By transactionTable = By.id("transactionTable");
    private By transactionRows = By.cssSelector("#transactionTable tbody tr");
    private By dashboardLink = By.linkText("Dashboard");
    
    public TransactionsPage(WebDriver driver) {
        super(driver);
    }
    
    public int getTransactionCount() {
        List<WebElement> rows = driver.findElements(transactionRows);
        return rows.size();
    }
    
    public DashboardPage goToDashboard() {
        clickElement(dashboardLink);
        waitForUrl("dashboard.html");
        return new DashboardPage(driver);
    }
    
    public boolean isTransactionsPageDisplayed() {
        return isElementDisplayed(transactionTable) && 
               getCurrentUrl().contains("transactions.html");
    }
}

/**
 * AccountsPage - Page Object for Accounts Page
 */
class AccountsPage extends BasePage {
    
    private By accountsTable = By.id("accountsTable");
    private By accountRows = By.cssSelector("#accountsTable tbody tr");
    private By dashboardLink = By.linkText("Dashboard");
    
    public AccountsPage(WebDriver driver) {
        super(driver);
    }
    
    public int getAccountCount() {
        List<WebElement> rows = driver.findElements(accountRows);
        return rows.size();
    }
    
    public DashboardPage goToDashboard() {
        clickElement(dashboardLink);
        waitForUrl("dashboard.html");
        return new DashboardPage(driver);
    }
    
    public boolean isAccountsPageDisplayed() {
        return isElementDisplayed(accountsTable) && 
               getCurrentUrl().contains("accounts.html");
    }
}

/**
 * ReportsPage - Page Object for Reports Page
 */
class ReportsPage extends BasePage {
    
    private By pageHeading = By.tagName("h1");
    private By dashboardLink = By.linkText("Dashboard");
    
    public ReportsPage(WebDriver driver) {
        super(driver);
    }
    
    public DashboardPage goToDashboard() {
        clickElement(dashboardLink);
        waitForUrl("dashboard.html");
        return new DashboardPage(driver);
    }
    
    public boolean isReportsPageDisplayed() {
        return isElementDisplayed(pageHeading) && 
               getCurrentUrl().contains("reports.html");
    }
}
