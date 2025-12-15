package com.npci.training.level3;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 3 - Test 03: Expected Conditions
 * 
 * Topics Covered:
 * - All common Expected Conditions
 * - Element state conditions
 * - Alert conditions
 * - Frame conditions
 * - Attribute and text conditions
 * - Complex conditions
 * - Combining conditions
 * 
 * Duration: 30 minutes
 */
public class Test03_ExpectedConditions {

    WebDriver driver;
    WebDriverWait wait;
    
    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @DisplayName("EC: presenceOfElementLocated")
    public void testPresenceOfElementLocated() {
        System.out.println("\n=== presenceOfElementLocated ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for element to be present in DOM (may not be visible)
        WebElement element = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.id("username"))
        );
        
        System.out.println("✓ Element is present in DOM");
        System.out.println("  Use Case: Element exists (visible or hidden)");
        
        assertNotNull(element);
    }
    
    @Test
    @DisplayName("EC: visibilityOfElementLocated")
    public void testVisibilityOfElementLocated() {
        System.out.println("\n=== visibilityOfElementLocated ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for element to be visible
        WebElement element = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("username"))
        );
        
        System.out.println("✓ Element is visible");
        System.out.println("  isDisplayed(): " + element.isDisplayed());
        System.out.println("  Use Case: Element is visible on page");
        
        assertTrue(element.isDisplayed());
    }
    
    @Test
    @DisplayName("EC: elementToBeClickable")
    public void testElementToBeClickable() {
        System.out.println("\n=== elementToBeClickable ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for element to be clickable (visible + enabled)
        WebElement button = wait.until(
            ExpectedConditions.elementToBeClickable(By.id("loginBtn"))
        );
        
        System.out.println("✓ Element is clickable");
        System.out.println("  isDisplayed(): " + button.isDisplayed());
        System.out.println("  isEnabled(): " + button.isEnabled());
        System.out.println("  Use Case: Ready to be clicked");
        
        assertTrue(button.isEnabled());
    }
    
    @Test
    @DisplayName("EC: titleContains")
    public void testTitleContains() {
        System.out.println("\n=== titleContains ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for page title to contain text
        boolean titleCorrect = wait.until(
            ExpectedConditions.titleContains("Login")
        );
        
        System.out.println("✓ Title contains 'Login'");
        System.out.println("  Full title: " + driver.getTitle());
        System.out.println("  Use Case: Verify page loaded correctly");
        
        assertTrue(titleCorrect);
    }
    
    @Test
    @DisplayName("EC: titleIs")
    public void testTitleIs() {
        System.out.println("\n=== titleIs ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for exact title
        boolean titleCorrect = wait.until(
            ExpectedConditions.titleIs("Banking Portal - Login")
        );
        
        System.out.println("✓ Title matches exactly");
        System.out.println("  Title: " + driver.getTitle());
        System.out.println("  Use Case: Exact title verification");
        
        assertTrue(titleCorrect);
    }
    
    @Test
    @DisplayName("EC: urlContains")
    public void testUrlContains() throws InterruptedException {
        System.out.println("\n=== urlContains ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Perform login and wait for URL change
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin123");
        driver.findElement(By.id("userType")).click();
        driver.findElement(By.cssSelector("option[value='customer']")).click();
        driver.findElement(By.id("terms")).click();
        driver.findElement(By.id("loginBtn")).click();
        
        // Handle alert
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
        
        // Wait for URL to contain dashboard
        boolean urlChanged = wait.until(
            ExpectedConditions.urlContains("dashboard.html")
        );
        
        System.out.println("✓ URL contains 'dashboard.html'");
        System.out.println("  Current URL: " + driver.getCurrentUrl());
        System.out.println("  Use Case: Navigation verification");
        
        assertTrue(urlChanged);
    }
    
    @Test
    @DisplayName("EC: urlToBe")
    public void testUrlToBe() {
        System.out.println("\n=== urlToBe ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for exact URL
        boolean urlCorrect = wait.until(
            ExpectedConditions.urlToBe("http://localhost:8000/login.html")
        );
        
        System.out.println("✓ URL matches exactly");
        System.out.println("  URL: " + driver.getCurrentUrl());
        System.out.println("  Use Case: Exact URL verification");
        
        assertTrue(urlCorrect);
    }
    
    @Test
    @DisplayName("EC: alertIsPresent")
    public void testAlertIsPresent() throws InterruptedException {
        System.out.println("\n=== alertIsPresent ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Trigger login to get alert
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin123");
        driver.findElement(By.id("userType")).click();
        driver.findElement(By.cssSelector("option[value='customer']")).click();
        driver.findElement(By.id("terms")).click();
        driver.findElement(By.id("loginBtn")).click();
        
        // Wait for alert
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        
        System.out.println("✓ Alert is present");
        System.out.println("  Alert text: " + alert.getText());
        System.out.println("  Use Case: Wait for JavaScript alerts");
        
        alert.accept();
        Thread.sleep(1000);
    }
    
    @Test
    @DisplayName("EC: textToBePresentInElementLocated")
    public void testTextToBePresentInElement() {
        System.out.println("\n=== textToBePresentInElementLocated ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for specific text in element
        boolean textPresent = wait.until(
            ExpectedConditions.textToBePresentInElementLocated(
                By.tagName("h1"), 
                "Banking Portal"
            )
        );
        
        System.out.println("✓ Text 'Banking Portal' present in h1");
        System.out.println("  Full text: " + driver.findElement(By.tagName("h1")).getText());
        System.out.println("  Use Case: Verify dynamic text loaded");
        
        assertTrue(textPresent);
    }
    
    @Test
    @DisplayName("EC: attributeToBe")
    public void testAttributeToBe() {
        System.out.println("\n=== attributeToBe ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for attribute to have specific value
        boolean attributeCorrect = wait.until(
            ExpectedConditions.attributeToBe(
                By.id("username"),
                "type",
                "text"
            )
        );
        
        System.out.println("✓ Attribute 'type' is 'text'");
        System.out.println("  Use Case: Verify element attributes");
        
        assertTrue(attributeCorrect);
    }
    
    @Test
    @DisplayName("EC: attributeContains")
    public void testAttributeContains() {
        System.out.println("\n=== attributeContains ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for attribute to contain value
        boolean attributeContains = wait.until(
            ExpectedConditions.attributeContains(
                By.id("loginBtn"),
                "class",
                "btn-primary"
            )
        );
        
        System.out.println("✓ Class attribute contains 'btn-primary'");
        WebElement button = driver.findElement(By.id("loginBtn"));
        System.out.println("  Full class: " + button.getAttribute("class"));
        System.out.println("  Use Case: Verify CSS classes");
        
        assertTrue(attributeContains);
    }
    
    @Test
    @DisplayName("EC: elementToBeSelected")
    public void testElementToBeSelected() {
        System.out.println("\n=== elementToBeSelected ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Click checkbox
        WebElement checkbox = driver.findElement(By.id("terms"));
        checkbox.click();
        
        // Wait for checkbox to be selected
        boolean isSelected = wait.until(
            ExpectedConditions.elementToBeSelected(By.id("terms"))
        );
        
        System.out.println("✓ Checkbox is selected");
        System.out.println("  isSelected(): " + checkbox.isSelected());
        System.out.println("  Use Case: Verify checkbox/radio selection");
        
        assertTrue(isSelected);
    }
    
    @Test
    @DisplayName("EC: invisibilityOfElementLocated")
    public void testInvisibilityOfElement() {
        System.out.println("\n=== invisibilityOfElementLocated ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Error messages are initially invisible
        boolean isInvisible = wait.until(
            ExpectedConditions.invisibilityOfElementLocated(By.id("usernameError"))
        );
        
        System.out.println("✓ Error message is invisible");
        System.out.println("  Use Case: Wait for element to disappear");
        
        assertTrue(isInvisible);
    }
    
    @Test
    @DisplayName("EC: numberOfElementsToBeMoreThan")
    public void testNumberOfElements() {
        System.out.println("\n=== numberOfElementsToBeMoreThan ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for multiple input elements - returns List<WebElement>
        List<WebElement> elements = wait.until(
            ExpectedConditions.numberOfElementsToBeMoreThan(
                By.tagName("input"),
                3
            )
        );
        
        System.out.println("✓ More than 3 input elements found");
        System.out.println("  Total inputs: " + elements.size());
        System.out.println("  Use Case: Wait for list to populate");
        
        assertTrue(elements.size() > 3);
    }
    
    @Test
    @DisplayName("EC: numberOfElementsToBe")
    public void testNumberOfElementsToBe() {
        System.out.println("\n=== numberOfElementsToBe ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for exact number of buttons - returns List<WebElement>
        List<WebElement> buttons = wait.until(
            ExpectedConditions.numberOfElementsToBe(
                By.tagName("button"),
                2
            )
        );
        
        System.out.println("✓ Exactly 2 buttons found");
        System.out.println("  Buttons: " + buttons.size());
        System.out.println("  Use Case: Verify exact element count");
        
        assertEquals(2, buttons.size());
    }
    
    @Test
    @DisplayName("EC: stalenessOf")
    public void testStalenessOf() {
        System.out.println("\n=== stalenessOf ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Get reference to element
        WebElement heading = driver.findElement(By.tagName("h1"));
        System.out.println("Original heading: " + heading.getText());
        
        // Navigate away
        driver.get("http://localhost:8000/register.html");
        
        // Wait for original element to become stale
        boolean isStale = wait.until(ExpectedConditions.stalenessOf(heading));
        
        System.out.println("✓ Element became stale");
        System.out.println("  Use Case: Verify page refresh/navigation");
        
        assertTrue(isStale);
    }
    
    @Test
    @DisplayName("Combining conditions with AND")
    public void testCombiningConditionsAnd() {
        System.out.println("\n=== Combining Conditions (AND) ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Element must be both visible AND clickable - returns Boolean
        Boolean result = wait.until(
            ExpectedConditions.and(
                ExpectedConditions.visibilityOfElementLocated(By.id("loginBtn")),
                ExpectedConditions.elementToBeClickable(By.id("loginBtn"))
            )
        );
        
        System.out.println("✓ Button is BOTH visible AND clickable");
        System.out.println("  Use Case: Multiple conditions must be true");
        
        assertTrue(result);
        
        // If you need the element, find it after verification
        WebElement button = driver.findElement(By.id("loginBtn"));
        assertNotNull(button);
    }
    
    @Test
    @DisplayName("Combining conditions with OR")
    public void testCombiningConditionsOr() {
        System.out.println("\n=== Combining Conditions (OR) ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for either element to be visible - returns Boolean
        Boolean result = wait.until(
            ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.id("username")),
                ExpectedConditions.visibilityOfElementLocated(By.id("password"))
            )
        );
        
        System.out.println("✓ At least one element is visible");
        System.out.println("  Use Case: Any condition can be true");
        
        assertTrue(result);
    }
    
    @Test
    @DisplayName("NOT condition")
    public void testNotCondition() {
        System.out.println("\n=== NOT Condition ===");
        
        driver.get("http://localhost:8000/login.html");
        
        // Wait for element NOT to have specific text
        boolean textNotPresent = wait.until(
            ExpectedConditions.not(
                ExpectedConditions.textToBePresentInElementLocated(
                    By.tagName("h1"),
                    "Wrong Text"
                )
            )
        );
        
        System.out.println("✓ Text is NOT 'Wrong Text'");
        System.out.println("  Use Case: Verify something is NOT true");
        
        assertTrue(textNotPresent);
    }
    
    @Test
    @DisplayName("Summary of all Expected Conditions")
    public void testExpectedConditionsSummary() {
        System.out.println("\n=== Expected Conditions Summary ===");
        
        System.out.println("\n📋 ELEMENT PRESENCE & VISIBILITY:");
        System.out.println("  presenceOfElementLocated       - Element in DOM");
        System.out.println("  visibilityOfElementLocated     - Element visible");
        System.out.println("  invisibilityOfElementLocated   - Element not visible");
        System.out.println("  elementToBeClickable           - Visible + Enabled");
        
        System.out.println("\n📋 TEXT & ATTRIBUTES:");
        System.out.println("  textToBePresentInElementLocated - Element contains text");
        System.out.println("  attributeToBe                   - Attribute = value");
        System.out.println("  attributeContains               - Attribute contains value");
        
        System.out.println("\n📋 PAGE & NAVIGATION:");
        System.out.println("  titleIs                        - Exact title");
        System.out.println("  titleContains                  - Title contains text");
        System.out.println("  urlToBe                        - Exact URL");
        System.out.println("  urlContains                    - URL contains text");
        
        System.out.println("\n📋 ELEMENT STATE:");
        System.out.println("  elementToBeSelected            - Checkbox/radio selected");
        System.out.println("  stalenessOf                    - Element became stale");
        
        System.out.println("\n📋 ALERTS & FRAMES:");
        System.out.println("  alertIsPresent                 - Alert popup exists");
        System.out.println("  frameToBeAvailableAndSwitchToIt - Frame ready");
        
        System.out.println("\n📋 MULTIPLE ELEMENTS:");
        System.out.println("  numberOfElementsToBe           - Exact count");
        System.out.println("  numberOfElementsToBeMoreThan   - Greater than count");
        System.out.println("  numberOfElementsToBeLessThan   - Less than count");
        
        System.out.println("\n📋 LOGICAL OPERATORS:");
        System.out.println("  and()                          - All conditions true");
        System.out.println("  or()                           - Any condition true");
        System.out.println("  not()                          - Condition is false");
    }
}
