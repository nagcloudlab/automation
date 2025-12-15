package com.npci.training.level2;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 2 - Test 01: CSS Selectors
 * 
 * Topics Covered:
 * - CSS Selector syntax and patterns
 * - Tag selectors
 * - Class selectors
 * - ID selectors
 * - Attribute selectors
 * - Combination selectors
 * - Child and descendant selectors
 * 
 * Duration: 20 minutes
 */
public class Test01_CSSSelectors {

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
    @DisplayName("CSS: Tag Selector")
    public void testTagSelector() throws InterruptedException {
        System.out.println("\n=== CSS Tag Selector ===");
        
        // Select by tag name
        WebElement heading = driver.findElement(By.cssSelector("h1"));
        System.out.println("H1 text: " + heading.getText());
        assertTrue(heading.getText().contains("Banking Portal"));
        
        // Select all input elements
        List<WebElement> inputs = driver.findElements(By.cssSelector("input"));
        System.out.println("Total input elements: " + inputs.size());
        assertTrue(inputs.size() > 0);
        
        // Select all buttons
        List<WebElement> buttons = driver.findElements(By.cssSelector("button"));
        System.out.println("Total buttons: " + buttons.size());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("CSS: ID Selector (#id)")
    public void testIdSelector() throws InterruptedException {
        System.out.println("\n=== CSS ID Selector ===");
        
        // CSS: #id
        WebElement username = driver.findElement(By.cssSelector("#username"));
        System.out.println("✓ Found username using #username");
        username.sendKeys("admin");
        assertEquals("admin", username.getAttribute("value"));
        
        // CSS: #id for button
        WebElement loginBtn = driver.findElement(By.cssSelector("#loginBtn"));
        System.out.println("✓ Found button using #loginBtn");
        System.out.println("  Button text: " + loginBtn.getText());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("CSS: Class Selector (.class)")
    public void testClassSelector() throws InterruptedException {
        System.out.println("\n=== CSS Class Selector ===");
        
        // CSS: .class - returns first match
        WebElement formGroup = driver.findElement(By.cssSelector(".form-group"));
        System.out.println("✓ Found first .form-group");
        
        // CSS: .class - get all matches
        List<WebElement> allFormGroups = driver.findElements(By.cssSelector(".form-group"));
        System.out.println("✓ Total .form-group elements: " + allFormGroups.size());
        
        // CSS: button with class
        WebElement primaryBtn = driver.findElement(By.cssSelector(".btn-primary"));
        System.out.println("✓ Found button with .btn-primary");
        System.out.println("  Text: " + primaryBtn.getText());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("CSS: Attribute Selectors")
    public void testAttributeSelectors() throws InterruptedException {
        System.out.println("\n=== CSS Attribute Selectors ===");
        
        // [attribute] - has attribute
        List<WebElement> withPlaceholder = driver.findElements(By.cssSelector("[placeholder]"));
        System.out.println("Elements with placeholder: " + withPlaceholder.size());
        
        // [attribute='value'] - exact match
        WebElement passwordInput = driver.findElement(By.cssSelector("[type='password']"));
        System.out.println("✓ Found password input: [type='password']");
        System.out.println("  ID: " + passwordInput.getAttribute("id"));
        
        // [attribute^='value'] - starts with
        WebElement startsWithUser = driver.findElement(By.cssSelector("[id^='user']"));
        System.out.println("✓ Found element with id starting with 'user': " + startsWithUser.getAttribute("id"));
        
        // [attribute$='value'] - ends with
        WebElement endsWithBtn = driver.findElement(By.cssSelector("[id$='Btn']"));
        System.out.println("✓ Found element with id ending with 'Btn': " + endsWithBtn.getAttribute("id"));
        
        // [attribute*='value'] - contains
        WebElement containsLogin = driver.findElement(By.cssSelector("[id*='login']"));
        System.out.println("✓ Found element with id containing 'login': " + containsLogin.getAttribute("id"));
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("CSS: Combination Selectors")
    public void testCombinationSelectors() throws InterruptedException {
        System.out.println("\n=== CSS Combination Selectors ===");
        
        // tag.class
        WebElement h1Element = driver.findElement(By.cssSelector("h1"));
        System.out.println("✓ h1 text: " + h1Element.getText());
        
        // tag#id
        WebElement inputUsername = driver.findElement(By.cssSelector("input#username"));
        System.out.println("✓ input#username found");
        
        // tag[attribute='value']
        WebElement textInput = driver.findElement(By.cssSelector("input[type='text']"));
        System.out.println("✓ input[type='text'] placeholder: " + textInput.getAttribute("placeholder"));
        
        // Multiple classes: .class1.class2
        WebElement button = driver.findElement(By.cssSelector("button.btn.btn-primary"));
        System.out.println("✓ button.btn.btn-primary text: " + button.getText());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("CSS: Child and Descendant Selectors")
    public void testChildDescendantSelectors() throws InterruptedException {
        System.out.println("\n=== CSS Child & Descendant Selectors ===");
        
        // Descendant: parent descendant (space)
        WebElement formInput = driver.findElement(By.cssSelector("form input"));
        System.out.println("✓ form input found: " + formInput.getAttribute("id"));
        
        // Direct child: parent > child
        List<WebElement> formChildren = driver.findElements(By.cssSelector("form > div"));
        System.out.println("✓ Direct children of form: " + formChildren.size());
        
        // Adjacent sibling: element + adjacent
        // First div + next div
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("CSS: Pseudo-classes")
    public void testPseudoClasses() throws InterruptedException {
        System.out.println("\n=== CSS Pseudo-classes ===");
        
        // :first-child
        WebElement firstFormGroup = driver.findElement(By.cssSelector(".form-group:first-child"));
        System.out.println("✓ First .form-group found");
        
        // :nth-child(n)
        WebElement secondFormGroup = driver.findElement(By.cssSelector(".form-group:nth-child(2)"));
        System.out.println("✓ Second .form-group found");
        
        // Get all input elements
        List<WebElement> inputs = driver.findElements(By.cssSelector("input"));
        System.out.println("✓ Total inputs: " + inputs.size());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Complete login using CSS selectors")
    public void testCompleteLoginWithCSS() throws InterruptedException {
        System.out.println("\n=== Complete Login with CSS Selectors ===");
        
        // Enter username using CSS
        driver.findElement(By.cssSelector("#username")).sendKeys("admin");
        System.out.println("✓ Username entered using #username");
        
        // Enter password using CSS
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys("admin123");
        System.out.println("✓ Password entered using input[type='password']");
        
        // Select dropdown using CSS
        driver.findElement(By.cssSelector("select#userType")).click();
        driver.findElement(By.cssSelector("select#userType option[value='customer']")).click();
        System.out.println("✓ User type selected using CSS");
        
        // Check terms using CSS
        driver.findElement(By.cssSelector("#terms")).click();
        System.out.println("✓ Terms checked using #terms");
        
        Thread.sleep(1000);
        
        // Click login using CSS
        driver.findElement(By.cssSelector("button.btn-primary")).click();
        System.out.println("✓ Login clicked using button.btn-primary");
        
        Thread.sleep(2000);
        
        // Handle alert
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("✓ Alert: " + alert.getText());
            alert.accept();
            Thread.sleep(1000);
        } catch (NoAlertPresentException e) {
            // No alert
        }
        
        // Verify
        assertTrue(driver.getCurrentUrl().contains("dashboard.html"));
        System.out.println("✓ Login successful using only CSS selectors!");
    }
}
