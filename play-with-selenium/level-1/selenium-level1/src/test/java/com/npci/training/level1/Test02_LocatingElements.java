package com.npci.training.level1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Level 1 - Test 02: Locating Elements
 * 
 * Topics Covered:
 * - By.id() - Most common locator
 * - By.name() - Form elements
 * - By.className() - CSS class
 * - Element properties (tag, attributes, displayed, enabled)
 * 
 * Duration: 15 minutes
 */
public class Test02_LocatingElements {

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
    @DisplayName("Locate elements using By.id()")
    public void testLocateById() throws InterruptedException {
        System.out.println("\n=== Locating Elements by ID ===");
        
        // Locate username field
        WebElement usernameField = driver.findElement(By.id("username"));
        System.out.println("✓ Found username field");
        System.out.println("  Tag name: " + usernameField.getTagName());
        System.out.println("  Placeholder: " + usernameField.getAttribute("placeholder"));
        System.out.println("  Is displayed: " + usernameField.isDisplayed());
        System.out.println("  Is enabled: " + usernameField.isEnabled());
        
        // Locate password field
        WebElement passwordField = driver.findElement(By.id("password"));
        System.out.println("\n✓ Found password field");
        System.out.println("  Type: " + passwordField.getAttribute("type"));
        System.out.println("  Is displayed: " + passwordField.isDisplayed());
        
        // Locate dropdown
        WebElement userType = driver.findElement(By.id("userType"));
        System.out.println("\n✓ Found user type dropdown");
        System.out.println("  Tag name: " + userType.getTagName());
        
        // Locate button
        WebElement loginBtn = driver.findElement(By.id("loginBtn"));
        System.out.println("\n✓ Found login button");
        System.out.println("  Button text: " + loginBtn.getText());
        System.out.println("  Is enabled: " + loginBtn.isEnabled());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Locate elements using By.name()")
    public void testLocateByName() throws InterruptedException {
        System.out.println("\n=== Locating Elements by Name ===");
        
        // Using name attribute
        WebElement username = driver.findElement(By.name("username"));
        System.out.println("✓ Found username by name attribute");
        System.out.println("  ID: " + username.getAttribute("id"));
        
        WebElement password = driver.findElement(By.name("password"));
        System.out.println("✓ Found password by name attribute");
        
        WebElement rememberMe = driver.findElement(By.name("rememberMe"));
        System.out.println("✓ Found checkbox by name attribute");
        System.out.println("  Is selected: " + rememberMe.isSelected());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Locate elements using By.className()")
    public void testLocateByClassName() throws InterruptedException {
        System.out.println("\n=== Locating Elements by Class Name ===");
        
        // Find first element with class 'form-control'
        // Note: className finds FIRST matching element
        WebElement firstFormControl = driver.findElement(By.className("form-control"));
        System.out.println("✓ Found element with class 'form-control'");
        System.out.println("  Element ID: " + firstFormControl.getAttribute("id"));
        System.out.println("  Placeholder: " + firstFormControl.getAttribute("placeholder"));
        
        // Find button by class
        WebElement button = driver.findElement(By.className("btn-primary"));
        System.out.println("\n✓ Found button with class 'btn-primary'");
        System.out.println("  Button text: " + button.getText());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Check element properties")
    public void testElementProperties() throws InterruptedException {
        System.out.println("\n=== Checking Element Properties ===");
        
        WebElement usernameField = driver.findElement(By.id("username"));
        
        System.out.println("Username Field Properties:");
        System.out.println("  Tag Name: " + usernameField.getTagName());
        System.out.println("  ID: " + usernameField.getAttribute("id"));
        System.out.println("  Name: " + usernameField.getAttribute("name"));
        System.out.println("  Type: " + usernameField.getAttribute("type"));
        System.out.println("  Placeholder: " + usernameField.getAttribute("placeholder"));
        System.out.println("  Class: " + usernameField.getAttribute("class"));
        System.out.println("  Is Displayed: " + usernameField.isDisplayed());
        System.out.println("  Is Enabled: " + usernameField.isEnabled());
        System.out.println("  Location: " + usernameField.getLocation());
        System.out.println("  Size: " + usernameField.getSize());
        
        Thread.sleep(2000);
    }
}
