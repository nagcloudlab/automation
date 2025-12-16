package com.npci.training.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage - Parent class for all Page Objects
 * 
 * Contains common functionality used across all pages:
 * - WebDriver instance
 * - WebDriverWait
 * - Common helper methods
 * - Reusable actions
 */
public class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    // Constructor
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    /**
     * Wait for element to be clickable and click
     */
    protected void clickElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }
    
    /**
     * Wait for element to be visible and enter text
     */
    protected void enterText(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Wait for element to be visible and get text
     */
    protected String getElementText(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText();
    }
    
    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
    
    /**
     * Wait for alert and accept
     */
    protected void acceptAlert() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (NoAlertPresentException | TimeoutException e) {
            // No alert present
        }
    }
    
    /**
     * Wait for alert, get text, and accept
     */
    protected String getAlertTextAndAccept() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            alert.accept();
            return alertText;
        } catch (NoAlertPresentException | TimeoutException e) {
            return null;
        }
    }
    
    /**
     * Wait for page title to contain text
     */
    protected void waitForTitle(String titlePart) {
        wait.until(ExpectedConditions.titleContains(titlePart));
    }
    
    /**
     * Wait for URL to contain text
     */
    protected void waitForUrl(String urlPart) {
        wait.until(ExpectedConditions.urlContains(urlPart));
    }
    
    /**
     * Get current page URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Get current page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Check if element is present (in DOM)
     */
    protected boolean isElementPresent(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
    
    /**
     * Wait for element to disappear
     */
    protected void waitForElementToDisappear(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Scroll to element
     */
    protected void scrollToElement(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
