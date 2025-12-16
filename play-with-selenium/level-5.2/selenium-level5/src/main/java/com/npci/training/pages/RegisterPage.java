package com.npci.training.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * RegisterPage - Page Object for Registration Page
 */
public class RegisterPage extends BasePage {
    
    // Locators
    private By fullNameField = By.id("fullName");
    private By emailField = By.id("email");
    private By phoneField = By.id("phone");
    private By passwordField = By.id("password");
    private By confirmPasswordField = By.id("confirmPassword");
    private By registerButton = By.id("registerBtn");
    private By loginLink = By.partialLinkText("Login");
    
    // Constructor
    public RegisterPage(WebDriver driver) {
        super(driver);
    }
    
    // Navigation
    public RegisterPage open() {
        driver.get("http://localhost:8000/register.html");
        waitForTitle("Register");
        return this;
    }
    
    // Actions
    public RegisterPage enterFullName(String fullName) {
        enterText(fullNameField, fullName);
        return this;
    }
    
    public RegisterPage enterEmail(String email) {
        enterText(emailField, email);
        return this;
    }
    
    public RegisterPage enterPhone(String phone) {
        enterText(phoneField, phone);
        return this;
    }
    
    public RegisterPage enterPassword(String password) {
        enterText(passwordField, password);
        return this;
    }
    
    public RegisterPage enterConfirmPassword(String confirmPassword) {
        enterText(confirmPasswordField, confirmPassword);
        return this;
    }
    
    public RegisterPage clickRegister() {
        clickElement(registerButton);
        return this;
    }
    
    public LoginPage clickLoginLink() {
        clickElement(loginLink);
        waitForUrl("login.html");
        return new LoginPage(driver);
    }
    
    // Complete registration
    public RegisterPage registerUser(String fullName, String email, String phone, 
                                     String password, String confirmPassword) {
        enterFullName(fullName);
        enterEmail(email);
        enterPhone(phone);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        clickRegister();
        return this;
    }
    
    // Verification
    public boolean isRegisterPageDisplayed() {
        return isElementDisplayed(registerButton) && 
               getCurrentUrl().contains("register.html");
    }
}
