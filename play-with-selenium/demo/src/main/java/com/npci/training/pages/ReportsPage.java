package com.npci.training.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ReportsPage extends BasePage {

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