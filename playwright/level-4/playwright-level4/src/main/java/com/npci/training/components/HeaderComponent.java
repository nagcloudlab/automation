package com.npci.training.components;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.npci.training.pages.LoginPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * HeaderComponent - Reusable header component
 * 
 * Represents the common header across all pages
 * Can be used by any page object
 */
public class HeaderComponent {
    
    private final Page page;
    
    // Locators
    private final Locator logo;
    private final Locator userMenu;
    private final Locator logoutButton;
    private final Locator navigationBar;
    
    /**
     * Constructor
     * @param page Playwright Page instance
     */
    public HeaderComponent(Page page) {
        this.page = page;
        
        // Initialize locators
        this.logo = page.locator(".header-logo");
        this.userMenu = page.locator(".user-menu");
        this.logoutButton = page.getByRole(AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Logout"));
        this.navigationBar = page.locator("nav");
    }
    
    /**
     * Click logo
     */
    public void clickLogo() {
        logo.click();
    }
    
    /**
     * Open user menu
     */
    public void openUserMenu() {
        userMenu.click();
    }
    
    /**
     * Logout
     * @return LoginPage
     */
    public LoginPage logout() {
        openUserMenu();
        logoutButton.click();
        return new LoginPage(page);
    }
    
    /**
     * Navigate using menu
     * @param menuItem Menu item name
     */
    public void navigateToMenuItem(String menuItem) {
        navigationBar
            .getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(menuItem))
            .click();
    }
    
    /**
     * Get username from header
     * @return Username
     */
    public String getLoggedInUsername() {
        return userMenu.locator(".username").textContent();
    }
    
    /**
     * Check if user is logged in
     * @return true if logged in
     */
    public boolean isUserLoggedIn() {
        return userMenu.isVisible();
    }
    
    /**
     * Verify header is displayed
     */
    public void verifyHeaderDisplayed() {
        assertThat(logo).isVisible();
        assertThat(navigationBar).isVisible();
    }
    
    /**
     * Verify user menu shows username
     * @param expectedUsername Expected username
     */
    public void verifyLoggedInUser(String expectedUsername) {
        assertThat(userMenu).containsText(expectedUsername);
    }
}

/*
 * COMPONENT OBJECT PATTERN:
 * 
 * 1. PURPOSE:
 *    - Reusable UI components
 *    - Used across multiple pages
 *    - Encapsulate component logic
 * 
 * 2. WHAT MAKES A COMPONENT:
 *    ✅ Appears on multiple pages
 *    ✅ Has its own locators
 *    ✅ Has its own actions
 *    Examples: Header, Footer, Modal, Sidebar
 * 
 * 3. USAGE IN PAGE OBJECT:
 *    public class DashboardPage extends BasePage {
 *        private final HeaderComponent header;
 *        
 *        public DashboardPage(Page page) {
 *            super(page);
 *            this.header = new HeaderComponent(page);
 *        }
 *        
 *        public HeaderComponent getHeader() {
 *            return header;
 *        }
 *    }
 * 
 * 4. USAGE IN TEST:
 *    dashboardPage.getHeader().logout();
 *    dashboardPage.getHeader().navigateToMenuItem("Settings");
 * 
 * 5. BENEFITS:
 *    ✅ DRY (Don't Repeat Yourself)
 *    ✅ Reusable across pages
 *    ✅ Single source of truth
 *    ✅ Easy to maintain
 *    ✅ Consistent behavior
 * 
 * COMPONENT vs PAGE:
 * - Component: Reusable across pages (Header, Modal)
 * - Page: Represents entire page (LoginPage, DashboardPage)
 */
