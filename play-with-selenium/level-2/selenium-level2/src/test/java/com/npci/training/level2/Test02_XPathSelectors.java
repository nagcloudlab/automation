package com.npci.training.level2;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 2 - Test 02: XPath Selectors
 * 
 * Topics Covered:
 * - XPath absolute vs relative paths
 * - XPath with attributes
 * - XPath text() function
 * - XPath contains() function
 * - XPath starts-with() function
 * - XPath axes (parent, child, following-sibling)
 * - XPath predicates and indexing
 * 
 * Duration: 25 minutes
 */
public class Test02_XPathSelectors {

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
    @DisplayName("XPath: Basic Syntax")
    public void testBasicXPath() throws InterruptedException {
        System.out.println("\n=== Basic XPath Syntax ===");
        
        // Absolute path (NOT recommended - fragile)
        // //html/body/div/form/div[1]/input
        
        // Relative path (RECOMMENDED)
        // //tagname[@attribute='value']
        
        // Simple tag selection
        WebElement heading = driver.findElement(By.xpath("//h1"));
        System.out.println("✓ H1 text: " + heading.getText());
        
        // Tag with attribute
        WebElement username = driver.findElement(By.xpath("//input[@id='username']"));
        System.out.println("✓ Found input with id='username'");
        username.sendKeys("test");
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("XPath: Attribute-based Selection")
    public void testAttributeXPath() throws InterruptedException {
        System.out.println("\n=== XPath Attribute Selection ===");
        
        // [@attribute='value']
        WebElement password = driver.findElement(By.xpath("//input[@type='password']"));
        System.out.println("✓ //input[@type='password']");
        System.out.println("  ID: " + password.getAttribute("id"));
        
        // Multiple attributes with 'and'
        WebElement usernameField = driver.findElement(
            By.xpath("//input[@id='username' and @type='text']")
        );
        System.out.println("✓ //input[@id='username' and @type='text']");
        
        // Multiple attributes with 'or'
        WebElement anyCheckbox = driver.findElement(
            By.xpath("//input[@id='terms' or @id='rememberMe']")
        );
        System.out.println("✓ Found checkbox: " + anyCheckbox.getAttribute("id"));
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("XPath: text() Function")
    public void testTextFunction() throws InterruptedException {
        System.out.println("\n=== XPath text() Function ===");
        
        // Exact text match
        WebElement loginBtn = driver.findElement(By.xpath("//button[text()='Login']"));
        System.out.println("✓ //button[text()='Login']");
        System.out.println("  Button found: " + loginBtn.getText());
        
        // Find heading by text
        WebElement heading = driver.findElement(By.xpath("//h1[text()='🏦 Banking Portal']"));
        System.out.println("✓ //h1[text()='🏦 Banking Portal']");
        
        // Find label by text
        WebElement label = driver.findElement(By.xpath("//label[text()='Username']"));
        System.out.println("✓ //label[text()='Username']");
        System.out.println("  Label for: " + label.getAttribute("for"));
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("XPath: contains() Function")
    public void testContainsFunction() throws InterruptedException {
        System.out.println("\n=== XPath contains() Function ===");
        
        // contains(@attribute, 'value')
        WebElement loginButton = driver.findElement(
            By.xpath("//button[contains(@id, 'login')]")
        );
        System.out.println("✓ //button[contains(@id, 'login')]");
        System.out.println("  Button ID: " + loginButton.getAttribute("id"));
        
        // contains(text(), 'value')
        WebElement portalHeading = driver.findElement(
            By.xpath("//h1[contains(text(), 'Banking')]")
        );
        System.out.println("✓ //h1[contains(text(), 'Banking')]");
        System.out.println("  Text: " + portalHeading.getText());
        
        // contains with class
        WebElement primaryBtn = driver.findElement(
            By.xpath("//button[contains(@class, 'btn-primary')]")
        );
        System.out.println("✓ //button[contains(@class, 'btn-primary')]");
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("XPath: starts-with() Function")
    public void testStartsWithFunction() throws InterruptedException {
        System.out.println("\n=== XPath starts-with() Function ===");
        
        // starts-with(@attribute, 'value')
        WebElement userField = driver.findElement(
            By.xpath("//input[starts-with(@id, 'user')]")
        );
        System.out.println("✓ //input[starts-with(@id, 'user')]");
        System.out.println("  Found: " + userField.getAttribute("id"));
        
        // starts-with with placeholder
        WebElement placeholderField = driver.findElement(
            By.xpath("//input[starts-with(@placeholder, 'Enter')]")
        );
        System.out.println("✓ //input[starts-with(@placeholder, 'Enter')]");
        System.out.println("  Placeholder: " + placeholderField.getAttribute("placeholder"));
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("XPath: Axes (parent, child, sibling)")
    public void testXPathAxes() throws InterruptedException {
        System.out.println("\n=== XPath Axes ===");
        
        // Parent axis
        WebElement usernameParent = driver.findElement(
            By.xpath("//input[@id='username']/parent::div")
        );
        System.out.println("✓ //input[@id='username']/parent::div");
        System.out.println("  Parent class: " + usernameParent.getAttribute("class"));
        
        // Child axis
        List<WebElement> formInputs = driver.findElements(
            By.xpath("//form/child::div")
        );
        System.out.println("✓ //form/child::div count: " + formInputs.size());
        
        // Following-sibling
        WebElement nextField = driver.findElement(
            By.xpath("//input[@id='username']/parent::div/following-sibling::div[1]//input")
        );
        System.out.println("✓ Next input after username: " + nextField.getAttribute("id"));
        
        // Preceding-sibling
        WebElement prevField = driver.findElement(
            By.xpath("//input[@id='password']/parent::div/preceding-sibling::div[1]//input")
        );
        System.out.println("✓ Previous input before password: " + prevField.getAttribute("id"));
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("XPath: Indexing and Predicates")
    public void testIndexingPredicates() throws InterruptedException {
        System.out.println("\n=== XPath Indexing & Predicates ===");
        
        // First element [1]
        WebElement firstInput = driver.findElement(By.xpath("(//input)[1]"));
        System.out.println("✓ (//input)[1] ID: " + firstInput.getAttribute("id"));
        
        // Second element [2]
        WebElement secondInput = driver.findElement(By.xpath("(//input)[2]"));
        System.out.println("✓ (//input)[2] ID: " + secondInput.getAttribute("id"));
        
        // Last element using last()
        WebElement lastInput = driver.findElement(By.xpath("(//input)[last()]"));
        System.out.println("✓ (//input)[last()] ID: " + lastInput.getAttribute("id"));
        
        // Position-based selection
        WebElement thirdInput = driver.findElement(By.xpath("(//input)[position()=3]"));
        System.out.println("✓ (//input)[position()=3] ID: " + thirdInput.getAttribute("id"));
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("XPath: Advanced Combinations")
    public void testAdvancedXPath() throws InterruptedException {
        System.out.println("\n=== Advanced XPath Combinations ===");
        
        // Complex: button inside form with specific class
        WebElement loginBtn = driver.findElement(
            By.xpath("//form//button[contains(@class, 'btn-primary') and contains(text(), 'Login')]")
        );
        System.out.println("✓ Complex XPath found login button");
        
        // Find input by label text
        WebElement passwordField = driver.findElement(
            By.xpath("//label[text()='Password']/following-sibling::input")
        );
        System.out.println("✓ Found password field using label text");
        System.out.println("  Type: " + passwordField.getAttribute("type"));
        
        // NOT condition
        List<WebElement> nonPasswordInputs = driver.findElements(
            By.xpath("//input[@type!='password']")
        );
        System.out.println("✓ Non-password inputs: " + nonPasswordInputs.size());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Complete login using XPath")
    public void testCompleteLoginWithXPath() throws InterruptedException {
        System.out.println("\n=== Complete Login with XPath ===");
        
        // Username with XPath
        driver.findElement(By.xpath("//input[@id='username']")).sendKeys("admin");
        System.out.println("✓ Username: //input[@id='username']");
        
        // Password with XPath
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys("admin123");
        System.out.println("✓ Password: //input[@type='password']");
        
        // Dropdown with XPath
        driver.findElement(By.xpath("//select[@id='userType']")).click();
        driver.findElement(By.xpath("//option[@value='customer']")).click();
        System.out.println("✓ User type: //select[@id='userType'] & //option[@value='customer']");
        
        // Terms checkbox
        driver.findElement(By.xpath("//input[@id='terms']")).click();
        System.out.println("✓ Terms: //input[@id='terms']");
        
        Thread.sleep(1000);
        
        // Login button with text
        driver.findElement(By.xpath("//button[text()='Login']")).click();
        System.out.println("✓ Login: //button[text()='Login']");
        
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
        System.out.println("✓ Login successful using only XPath!");
    }
}
