package com.npci.training.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AccountsPage extends BasePage {

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
