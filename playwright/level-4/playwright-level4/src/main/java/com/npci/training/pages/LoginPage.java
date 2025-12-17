package com.npci.training.pages;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * LoginPage - Page Object for Login functionality
 * 
 * Represents the login page and its interactions
 * Follows Page Object Model pattern
 */
public class LoginPage extends BasePage {
    
    // Locators (private, encapsulated)
    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator errorMessage;
    private final Locator heading;
    
    // Constructor
    public LoginPage(Page page) {
        super(page);
        
        // Initialize locators
        this.usernameInput = page.getByLabel("Username");
        this.passwordInput = page.getByLabel("Password");
        this.loginButton = page.getByRole(AriaRole.BUTTON, 
            new Page.GetByRoleOptions().setName("Login"));
        this.errorMessage = page.locator(".error-message");
        this.heading = page.getByRole(AriaRole.HEADING);
    }
    
    /**
     * Navigate to login page
     * @return this for method chaining
     */
    public LoginPage navigate() {
        page.navigate(baseUrl + "/login");
        return this;
    }
    
    /**
     * Enter username
     * @param username Username
     * @return this for method chaining
     */
    public LoginPage enterUsername(String username) {
        usernameInput.fill(username);
        return this;
    }
    
    /**
     * Enter password
     * @param password Password
     * @return this for method chaining
     */
    public LoginPage enterPassword(String password) {
        passwordInput.fill(password);
        return this;
    }
    
    /**
     * Click login button
     * @return DashboardPage (navigation)
     */
    public DashboardPage clickLogin() {
        loginButton.click();
        return new DashboardPage(page);
    }
    
    /**
     * Complete login (all-in-one method)
     * @param username Username
     * @param password Password
     * @return DashboardPage
     */
    public DashboardPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }
    
    /**
     * Attempt login with invalid credentials
     * @param username Username
     * @param password Password
     * @return this (stays on login page)
     */
    public LoginPage loginWithInvalidCredentials(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        loginButton.click();
        return this;
    }
    
    /**
     * Check if error message is displayed
     * @return true if error visible
     */
    public boolean isErrorMessageDisplayed() {
        return errorMessage.isVisible();
    }
    
    /**
     * Get error message text
     * @return Error message text
     */
    public String getErrorMessage() {
        return errorMessage.textContent();
    }
    
    /**
     * Verify login page is displayed
     * @return this for method chaining
     */
    public LoginPage verifyLoginPageDisplayed() {
        assertThat(heading).containsText("Login");
        assertThat(usernameInput).isVisible();
        assertThat(passwordInput).isVisible();
        assertThat(loginButton).isVisible();
        return this;
    }
    
    /**
     * Verify error message is shown
     * @param expectedMessage Expected error message
     * @return this for method chaining
     */
    public LoginPage verifyErrorMessage(String expectedMessage) {
        assertThat(errorMessage).isVisible();
        assertThat(errorMessage).containsText(expectedMessage);
        return this;
    }
    
    /**
     * Check if username field is empty
     * @return true if empty
     */
    public boolean isUsernameEmpty() {
        return usernameInput.inputValue().isEmpty();
    }
    
    /**
     * Check if password field is empty
     * @return true if empty
     */
    public boolean isPasswordEmpty() {
        return passwordInput.inputValue().isEmpty();
    }
    
    /**
     * Clear all fields
     * @return this for method chaining
     */
    public LoginPage clearFields() {
        usernameInput.clear();
        passwordInput.clear();
        return this;
    }
}

/*
 * PAGE OBJECT MODEL PATTERN:
 * 
 * 1. STRUCTURE:
 *    - Extends BasePage
 *    - Locators as private fields
 *    - Public methods for actions
 *    - Method chaining with 'return this'
 *    - Navigation returns new page object
 * 
 * 2. LOCATOR ENCAPSULATION:
 *    ✅ Locators are private
 *    ✅ Only methods are public
 *    ✅ Tests don't access locators directly
 *    ✅ Easy to change locators
 * 
 * 3. METHOD CHAINING:
 *    loginPage
 *        .enterUsername("admin")
 *        .enterPassword("password")
 *        .clickLogin();
 * 
 * 4. PAGE NAVIGATION:
 *    DashboardPage dashboard = loginPage.clickLogin();
 *    // Returns next page object
 * 
 * 5. VERIFICATION METHODS:
 *    - Methods starting with 'verify'
 *    - Return this for chaining
 *    - Keep assertions in page object
 * 
 * 6. BENEFITS:
 *    ✅ Readable tests
 *    ✅ Maintainable (locators in one place)
 *    ✅ Reusable
 *    ✅ Easy to update
 * 
 * USAGE IN TEST:
 * 
 * @Test
 * void testLogin() {
 *     new LoginPage(page)
 *         .navigate()
 *         .login("admin", "password123")
 *         .verifyDashboardDisplayed();
 * }
 */
