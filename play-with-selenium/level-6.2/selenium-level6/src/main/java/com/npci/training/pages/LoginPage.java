package com.npci.training.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * LoginPage - Page Object for Login Page
 * 
 * Contains:
 * - All locators for login page elements
 * - All actions that can be performed on login page
 * - Methods return Page Objects for method chaining
 */
public class LoginPage extends BasePage {
    
    // Locators - Private and centralized
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By userTypeDropdown = By.id("userType");
    private By rememberMeCheckbox = By.id("rememberMe");
    private By termsCheckbox = By.id("terms");
    private By loginButton = By.id("loginBtn");
    private By clearButton = By.cssSelector("button.btn-secondary");
    private By registerLink = By.linkText("Register");
    private By forgotPasswordLink = By.partialLinkText("Forgot");
    
    // Error message locators
    private By usernameError = By.id("usernameError");
    private By passwordError = By.id("passwordError");
    private By userTypeError = By.id("userTypeError");
    private By termsError = By.id("termsError");
    
    // Page heading
    private By pageHeading = By.tagName("h1");
    
    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    // Navigation method
    public LoginPage open() {
        driver.get("http://localhost:8000/login.html");
        waitForTitle("Login");
        return this;
    }
    
    // Actions - Return this for method chaining
    public LoginPage enterUsername(String username) {
        enterText(usernameField, username);
        return this;
    }
    
    public LoginPage enterPassword(String password) {
        enterText(passwordField, password);
        return this;
    }
    
    public LoginPage selectUserType(String userType) {
        Select dropdown = new Select(driver.findElement(userTypeDropdown));
        dropdown.selectByVisibleText(userType);
        return this;
    }
    
    public LoginPage checkRememberMe() {
        clickElement(rememberMeCheckbox);
        return this;
    }
    
    public LoginPage acceptTerms() {
        clickElement(termsCheckbox);
        return this;
    }
    
    public DashboardPage clickLogin() {
        clickElement(loginButton);
        acceptAlert(); // Handle "Login Successful" alert
        waitForUrl("dashboard.html");
        return new DashboardPage(driver);
    }
    
    public LoginPage clickLoginExpectingError() {
        clickElement(loginButton);
        return this;
    }
    
    public LoginPage clickClear() {
        clickElement(clearButton);
        return this;
    }
    
    public RegisterPage clickRegister() {
        clickElement(registerLink);
        waitForUrl("register.html");
        return new RegisterPage(driver);
    }
    
    // Complete login method - all in one
    public DashboardPage loginAs(String username, String password, String userType) {
        enterUsername(username);
        enterPassword(password);
        selectUserType(userType);
        acceptTerms();
        return clickLogin();
    }
    
    // Validation methods
    public String getUsernameError() {
        return getElementText(usernameError);
    }
    
    public String getPasswordError() {
        return getElementText(passwordError);
    }
    
    public String getUserTypeError() {
        return getElementText(userTypeError);
    }
    
    public String getTermsError() {
        return getElementText(termsError);
    }
    
    public boolean isUsernameErrorDisplayed() {
        return isElementDisplayed(usernameError);
    }
    
    public boolean isPasswordErrorDisplayed() {
        return isElementDisplayed(passwordError);
    }
    
    public String getHeading() {
        return getElementText(pageHeading);
    }
    
    // Verification methods
    public boolean isLoginPageDisplayed() {
        return isElementDisplayed(loginButton) && 
               getCurrentUrl().contains("login.html");
    }
}
