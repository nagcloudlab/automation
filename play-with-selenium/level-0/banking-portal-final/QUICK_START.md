# ⚡ Quick Start Guide - 5 Minutes to First Test

## 🎯 Goal
Get the application running and execute your first Selenium test in under 5 minutes!

---

## Step 1: Setup Application (1 minute)

### Download & Extract
```bash
1. Download banking-portal-final.zip
2. Extract to any folder
3. You should see 9 HTML files + 3 MD files
```

### Start Local Server
```bash
# Option 1: Python (recommended)
cd banking-portal-final
python -m http.server 8000

# Option 2: Node.js
npx http-server -p 8000

# Option 3: Just double-click index.html (for demo)
```

### Verify Running
```
Open browser: http://localhost:8000
You should see the welcome page
```

✅ **Done!** Application is running.

---

## Step 2: Create Selenium Project (2 minutes)

### Create Maven Project

**pom.xml:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.npci</groupId>
    <artifactId>banking-automation</artifactId>
    <version>1.0</version>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>4.16.1</version>
        </dependency>

        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
            <version>5.6.3</version>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

✅ **Done!** Project created with dependencies.

---

## Step 3: Your First Test (2 minutes)

### Create Test Class

**src/test/java/FirstTest.java:**
```java
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

public class FirstTest {

    WebDriver driver;
    
    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @Test
    @DisplayName("Test successful login")
    public void testLogin() {
        // Navigate to login page
        driver.get("http://localhost:8000/login.html");
        
        // Enter username
        WebElement username = driver.findElement(By.id("username"));
        username.sendKeys("admin");
        
        // Enter password
        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys("admin123");
        
        // Select user type
        Select userType = new Select(driver.findElement(By.id("userType")));
        userType.selectByVisibleText("Customer");
        
        // Accept terms
        driver.findElement(By.id("terms")).click();
        
        // Click login
        driver.findElement(By.id("loginBtn")).click();
        
        // Wait a moment for navigation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verify dashboard loaded
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("dashboard.html"), 
                  "Should navigate to dashboard");
        
        System.out.println("✓ Login test passed!");
    }
    
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
```

### Run the Test

```bash
# From command line
mvn test

# From IDE
Right-click → Run 'FirstTest'
```

### Expected Output
```
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
✓ Login test passed!
```

✅ **Done!** Your first Selenium test is running!

---

## 🎉 Congratulations!

**You just:**
1. ✅ Setup the banking portal
2. ✅ Created a Selenium project
3. ✅ Wrote and ran your first test

**Time taken:** Less than 5 minutes!

---

## 🚀 Next Steps

### Immediate (Next 10 minutes)
```java
@Test
public void testInvalidLogin() {
    driver.get("http://localhost:8000/login.html");
    
    driver.findElement(By.id("username")).sendKeys("wrong");
    driver.findElement(By.id("password")).sendKeys("wrong");
    
    Select userType = new Select(driver.findElement(By.id("userType")));
    userType.selectByVisibleText("Customer");
    
    driver.findElement(By.id("terms")).click();
    driver.findElement(By.id("loginBtn")).click();
    
    // Should stay on login page - invalid credentials
    assertTrue(driver.getCurrentUrl().contains("login.html"));
}
```

### Short Term (Next Hour)
1. Test empty field validation
2. Test form clearing
3. Test registration page
4. Test dashboard navigation

### Medium Term (Next Day)
1. Create Page Object Models
2. Add more test cases
3. Implement data-driven tests
4. Add reporting

### Long Term (Next Week)
1. Complete all pages
2. Master async scenarios
3. Build complete framework
4. Add CI/CD integration

---

## 📚 Quick Reference

### Test Credentials
```
Username: admin
Password: admin123
User Type: Customer
```

### Key URLs
```
Home:         http://localhost:8000/
Login:        http://localhost:8000/login.html
Register:     http://localhost:8000/register.html
Dashboard:    http://localhost:8000/dashboard.html
Transactions: http://localhost:8000/transactions.html
Accounts:     http://localhost:8000/accounts.html
Reports:      http://localhost:8000/reports.html
Dynamic:      http://localhost:8000/dynamic-data.html
Scenarios:    http://localhost:8000/test-scenarios.html
```

### Common Element IDs
```java
// Login
By.id("username")
By.id("password")
By.id("userType")
By.id("terms")
By.id("loginBtn")

// Register
By.id("fullName")
By.id("email")
By.id("mobile")
By.id("dob")
By.name("gender")
By.id("accountType")

// Dashboard
By.id("welcomeUser")
By.id("balance")
By.id("transactionTable")

// Dynamic
By.id("loadBtn1")
By.id("loadBtn2")
By.id("loadBtn3")
```

---

## ❓ Troubleshooting

### Issue: Port 8000 in use
```bash
# Try different port
python -m http.server 8080

# Update test URL
driver.get("http://localhost:8080/login.html");
```

### Issue: ChromeDriver not found
```bash
# WebDriverManager handles this automatically
# If still fails:
mvn clean install
```

### Issue: Element not found
```bash
# Add wait before finding element
Thread.sleep(1000);

# Or use WebDriverWait
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
```

### Issue: Test fails but manual works
```bash
# Add more waits
# Check console for errors
# Verify URL is correct
# Check browser version matches driver
```

---

## 🎯 5 Essential Tests to Write Now

### 1. Login Test (Already done! ✓)

### 2. Empty Field Validation
```java
@Test
public void testEmptyUsername() {
    driver.get("http://localhost:8000/login.html");
    driver.findElement(By.id("password")).sendKeys("admin123");
    driver.findElement(By.id("loginBtn")).click();
    
    WebElement error = driver.findElement(By.id("usernameError"));
    assertFalse(error.getText().isEmpty());
}
```

### 3. Dropdown Selection
```java
@Test
public void testDropdownSelection() {
    driver.get("http://localhost:8000/login.html");
    
    Select dropdown = new Select(driver.findElement(By.id("userType")));
    dropdown.selectByVisibleText("Admin");
    
    assertEquals("admin", dropdown.getFirstSelectedOption().getAttribute("value"));
}
```

### 4. Checkbox Interaction
```java
@Test
public void testCheckbox() {
    driver.get("http://localhost:8000/login.html");
    
    WebElement checkbox = driver.findElement(By.id("rememberMe"));
    assertFalse(checkbox.isSelected());
    
    checkbox.click();
    assertTrue(checkbox.isSelected());
}
```

### 5. Table Reading
```java
@Test
public void testTableReading() {
    driver.get("http://localhost:8000/dashboard.html");
    
    List<WebElement> rows = driver.findElements(
        By.cssSelector("#transactionTable tbody tr")
    );
    
    assertEquals(5, rows.size());
}
```

---

## 📊 Progress Tracker

**Complete these in order:**

- [ ] Setup application (5 min)
- [ ] Create project (5 min)
- [ ] Run first test (5 min)
- [ ] 5 essential tests (30 min)
- [ ] Create Page Objects (1 hour)
- [ ] Add wait strategies (1 hour)
- [ ] Complete login page tests (2 hours)
- [ ] Complete registration tests (2 hours)
- [ ] Complete all pages (1 day)
- [ ] Master async scenarios (2 days)
- [ ] Build complete framework (1 week)

---

## 🏆 Achievement: First Test Complete!

**You've successfully:**
- ✅ Setup Selenium environment
- ✅ Created your first test
- ✅ Automated a login flow
- ✅ Used multiple locators
- ✅ Made assertions

**You're now ready to:**
- ➡️ Learn more locator strategies
- ➡️ Master wait strategies
- ➡️ Build Page Object Model
- ➡️ Create data-driven tests
- ➡️ Implement reporting

---

## 🎓 Resources

**In this package:**
- README.md - Complete documentation
- SELENIUM_EXAMPLES.md - All code examples
- test-scenarios.html - Visual reference

**External:**
- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [JUnit 5 Guide](https://junit.org/junit5/docs/current/user-guide/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)

---

**Ready? Let's automate!** 🚀
