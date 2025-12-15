package com.npci.training.level1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 1 - Test 03: Basic Element Interactions
 * <p>
 * Topics Covered:
 * - sendKeys() - Enter text
 * - click() - Click buttons/checkboxes
 * - clear() - Clear text fields
 * - getText() - Get element text
 * - Select class - Handle dropdowns
 * - getAttribute() - Get attribute values
 * <p>
 * Duration: 20 minutes
 */
public class Test03_BasicInteractions {

    WebDriver driver;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/login.html");
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Test sendKeys() - Enter text in fields")
    public void testSendKeys() throws InterruptedException {
        System.out.println("\n=== Testing sendKeys() ===");

        // Enter username
        WebElement username = driver.findElement(By.id("username"));
        username.sendKeys("admin");
        System.out.println("✓ Entered username: admin");

        // Verify text was entered
        String enteredText = username.getAttribute("value");
        assertEquals("admin", enteredText);
        System.out.println("✓ Verified entered text: " + enteredText);

        // Enter password
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("admin123");
        System.out.println("✓ Entered password");

        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Test click() - Click checkboxes and buttons")
    public void testClick() throws InterruptedException {
        System.out.println("\n=== Testing click() ===");

        // Click checkbox - initially unchecked
        WebElement rememberMe = driver.findElement(By.id("rememberMe"));
        System.out.println("Remember Me initially selected: " + rememberMe.isSelected());
        assertFalse(rememberMe.isSelected(), "Should be unchecked initially");

        // Click to check
        rememberMe.click();
        Thread.sleep(500);
        System.out.println("After click, selected: " + rememberMe.isSelected());
        assertTrue(rememberMe.isSelected(), "Should be checked after click");
        System.out.println("✓ Checkbox clicked successfully");

        // Click terms checkbox
        WebElement terms = driver.findElement(By.id("terms"));
        assertFalse(terms.isSelected());
        terms.click();
        Thread.sleep(500);
        assertTrue(terms.isSelected());
        System.out.println("✓ Terms checkbox clicked");

        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Test Select class - Handle dropdown")
    public void testDropdown() throws InterruptedException {
        System.out.println("\n=== Testing Select Dropdown ===");

        // Locate dropdown element
        WebElement userTypeElement = driver.findElement(By.id("userType"));

        // Create Select object
        Select userTypeDropdown = new Select(userTypeElement);

        // Get all options
        System.out.println("Available options:");
        userTypeDropdown.getOptions().forEach(option ->
                System.out.println("  - " + option.getText())
        );

        // Select by visible text
        userTypeDropdown.selectByVisibleText("Customer");
        Thread.sleep(500);
        System.out.println("✓ Selected: Customer");

        // Verify selection
        String selectedText = userTypeDropdown.getFirstSelectedOption().getText();
        assertEquals("Customer", selectedText);
        System.out.println("✓ Verified selection: " + selectedText);

        // Select by value
        userTypeDropdown.selectByValue("admin");
        Thread.sleep(500);
        System.out.println("✓ Selected by value: admin");

        // Select by index
        userTypeDropdown.selectByIndex(1); // Customer
        Thread.sleep(500);
        System.out.println("✓ Selected by index: 1");

        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Test getText() - Get element text")
    public void testGetText() throws InterruptedException {
        System.out.println("\n=== Testing getText() ===");

        // Get button text
        WebElement loginBtn = driver.findElement(By.id("loginBtn"));
        String buttonText = loginBtn.getText();
        System.out.println("Login button text: " + buttonText);
        assertEquals("Login", buttonText);

        // Get heading text
        WebElement heading = driver.findElement(By.tagName("h1"));
        String headingText = heading.getText();
        System.out.println("Page heading: " + headingText);
        assertTrue(headingText.contains("Banking Portal"));

        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Test clear() - Clear text fields")
    public void testClear() throws InterruptedException {
        System.out.println("\n=== Testing clear() ===");

        WebElement username = driver.findElement(By.id("username"));

        // Enter text
        username.sendKeys("testuser");
        Thread.sleep(500);
        System.out.println("Entered text: " + username.getAttribute("value"));
        assertEquals("testuser", username.getAttribute("value"));

        // Clear field
        username.clear();
        Thread.sleep(500);
        System.out.println("After clear: " + username.getAttribute("value"));
        assertEquals("", username.getAttribute("value"));
        System.out.println("✓ Field cleared successfully");

        // Enter again
        username.sendKeys("admin");
        Thread.sleep(500);
        System.out.println("Re-entered text: " + username.getAttribute("value"));

        Thread.sleep(2000);
    }

    @Test
    @DisplayName("Complete form interaction")
    public void testCompleteFormInteraction() throws InterruptedException {
        System.out.println("\n=== Complete Form Interaction ===");

        // Fill all fields
        driver.findElement(By.id("username")).sendKeys("admin");
        System.out.println("✓ Username entered");

        driver.findElement(By.id("password")).sendKeys("admin123");
        System.out.println("✓ Password entered");

        Select userType = new Select(driver.findElement(By.id("userType")));
        userType.selectByVisibleText("Customer");
        System.out.println("✓ User type selected");

        driver.findElement(By.id("rememberMe")).click();
        System.out.println("✓ Remember me checked");

        driver.findElement(By.id("terms")).click();
        System.out.println("✓ Terms accepted");

        Thread.sleep(1000);

        // Click login button
        driver.findElement(By.id("loginBtn")).click();
        System.out.println("✓ Login clicked");

        Thread.sleep(2000);

        // ⭐ NEW: Handle the alert!
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("✓ Alert detected: " + alertText);
            alert.accept();  // Click OK
            System.out.println("✓ Alert accepted");
            Thread.sleep(1000);
        } catch (NoAlertPresentException e) {
            System.out.println("No alert present");
        }

        // Now verify navigation
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
        assertTrue(currentUrl.contains("dashboard.html"),
                "Should navigate to dashboard");
        System.out.println("✓ Successfully navigated to dashboard!");
    }
}
