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


