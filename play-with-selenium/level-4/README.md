# 🎓 Selenium Level 4 - Page Object Model & Advanced Patterns

## Production-Ready Test Architecture

---

## 📚 What You'll Learn

### **Level 4 Topics (3-4 hours)**
1. ✅ Page Object Model (POM) - Design pattern
2. ✅ BasePage Pattern - Code reusability
3. ✅ Method Chaining - Fluent interfaces
4. ✅ Screenshot Capture - Visual documentation
5. ✅ Test Utilities - Helper functions
6. ✅ Data-Driven Testing Basics
7. ✅ Production Best Practices

---

## 📦 Package Contents

```
selenium-level4/
├── pom.xml
├── README.md (this file)
└── src/
    ├── main/java/com/npci/training/
    │   ├── pages/
    │   │   ├── BasePage.java              # Foundation for all pages
    │   │   ├── LoginPage.java             # Login page object
    │   │   ├── DashboardPage.java         # Dashboard page object
    │   │   ├── RegisterPage.java          # Register page object
    │   │   └── OtherPages.java            # Transactions, Accounts, Reports
    │   └── utils/
    │       └── TestUtils.java             # Utility methods
    └── test/java/com/npci/training/level4/
        ├── Test01_PageObjectModelBasics.java    # 8 tests - POM intro
        └── Test02_AdvancedPOMPatterns.java      # 9 tests - Advanced patterns
```

**Total: 17 tests | 6 Page Objects | 2 Utility classes**

---

## 🚀 Quick Start

### Prerequisites
```
✓ Completed Level 1, 2, and 3
✓ Banking Portal running on http://localhost:8000
✓ Java 11+, Maven installed
✓ Understanding of waits and locators
```

### Run Your First Level 4 Test
```bash
# Extract and import
unzip selenium-level4.zip

# Run POM basics
mvn test -Dtest=Test01_PageObjectModelBasics

# Run all Level 4 tests
mvn test
```

---

## 🎯 **What is Page Object Model (POM)?**

### **The Problem (Without POM):**

```java
// ❌ OLD WAY - Everything in test
@Test
public void testLogin() {
    driver.get("http://localhost:8000/login.html");
    driver.findElement(By.id("username")).sendKeys("admin");
    driver.findElement(By.id("password")).sendKeys("admin123");
    driver.findElement(By.id("userType")).click();
    driver.findElement(By.cssSelector("option[value='customer']")).click();
    driver.findElement(By.id("terms")).click();
    driver.findElement(By.id("loginBtn")).click();
    // ... more code
}

// Problems:
// 1. Locators scattered everywhere
// 2. Code duplication
// 3. Hard to maintain
// 4. If UI changes, update many tests
// 5. Not readable
```

### **The Solution (With POM):**

```java
// ✅ NEW WAY - Clean and maintainable
@Test
public void testLogin() {
    DashboardPage dashboard = new LoginPage(driver)
        .open()
        .loginAs("admin", "admin123", "Customer");
    
    assertTrue(dashboard.isDashboardPageDisplayed());
}

// Benefits:
// 1. Locators centralized in Page Objects
// 2. No code duplication
// 3. Easy to maintain
// 4. If UI changes, update one place
// 5. Very readable
```

---

## 📖 **Page Object Structure**

### **BasePage.java** - Foundation

```java
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    // Common methods all pages can use
    protected void clickElement(By locator) { ... }
    protected void enterText(By locator, String text) { ... }
    protected String getElementText(By locator) { ... }
    protected void acceptAlert() { ... }
}
```

**Purpose:** Common functionality shared by all pages

---

### **LoginPage.java** - Specific Page

```java
public class LoginPage extends BasePage {
    
    // LOCATORS (private)
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("loginBtn");
    
    // CONSTRUCTOR
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    // NAVIGATION
    public LoginPage open() {
        driver.get("http://localhost:8000/login.html");
        return this;
    }
    
    // ACTIONS (return this for chaining)
    public LoginPage enterUsername(String username) {
        enterText(usernameField, username);
        return this;
    }
    
    public LoginPage enterPassword(String password) {
        enterText(passwordField, password);
        return this;
    }
    
    // Return next page object
    public DashboardPage clickLogin() {
        clickElement(loginButton);
        acceptAlert();
        return new DashboardPage(driver);
    }
    
    // COMPLETE ACTION
    public DashboardPage loginAs(String user, String pass, String type) {
        return enterUsername(user)
               .enterPassword(pass)
               .selectUserType(type)
               .acceptTerms()
               .clickLogin();
    }
    
    // VERIFICATIONS
    public boolean isLoginPageDisplayed() {
        return isElementDisplayed(loginButton);
    }
}
```

**Key Concepts:**
- Locators are **private** (encapsulation)
- Methods **return Page Objects** (chaining)
- Page-specific logic **only**
- No assertions (those go in tests)

---

## 🎓 **Core Principles**

### **1. One Page = One Page Object**
```
login.html      →  LoginPage.java
dashboard.html  →  DashboardPage.java
register.html   →  RegisterPage.java
```

### **2. Locators Stay in Page Objects**
```java
// ✅ GOOD - In LoginPage.java
private By usernameField = By.id("username");

// ❌ BAD - In test
driver.findElement(By.id("username"))
```

### **3. Return Page Objects**
```java
// ✅ GOOD - Returns next page
public DashboardPage clickLogin() {
    clickElement(loginButton);
    return new DashboardPage(driver);
}

// ✅ GOOD - Returns same page (chaining)
public LoginPage enterUsername(String username) {
    enterText(usernameField, username);
    return this;
}
```

### **4. Method Chaining**
```java
new LoginPage(driver)
    .open()
    .enterUsername("admin")
    .enterPassword("pass")
    .acceptTerms()
    .clickLogin();
```

### **5. No Assertions in Page Objects**
```java
// ✅ GOOD - Return boolean, let test assert
public boolean isLoginPageDisplayed() {
    return isElementDisplayed(loginButton);
}

// ❌ BAD - Assertion in page object
public void verifyLoginPage() {
    assertTrue(isElementDisplayed(loginButton)); // NO!
}
```

---

## 📝 **Complete Example**

### **Page Object:**
```java
// LoginPage.java
public class LoginPage extends BasePage {
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("loginBtn");
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    public LoginPage open() {
        driver.get("http://localhost:8000/login.html");
        return this;
    }
    
    public DashboardPage loginAs(String user, String pass, String type) {
        enterText(usernameField, user);
        enterText(passwordField, pass);
        // ... select type, accept terms
        clickElement(loginButton);
        acceptAlert();
        return new DashboardPage(driver);
    }
}
```

### **Test Using Page Object:**
```java
@Test
public void testLogin() {
    DashboardPage dashboard = new LoginPage(driver)
        .open()
        .loginAs("admin", "admin123", "Customer");
    
    assertTrue(dashboard.isDashboardPageDisplayed());
}
```

**Compare to non-POM:**
- **90% less code in test**
- **100% more readable**
- **Infinitely more maintainable**

---

## 📸 **Screenshot Capture**

### **During Test:**
```java
@Test
public void testWithScreenshots() {
    LoginPage loginPage = new LoginPage(driver).open();
    
    // Capture screenshot
    TestUtils.captureScreenshot(driver, "login_page");
    
    DashboardPage dashboard = loginPage.loginAs("admin", "pass", "Customer");
    
    // Capture another
    TestUtils.captureScreenshot(driver, "dashboard");
}
```

### **On Failure:**
```java
@Test
public void testWithFailureScreenshot() {
    try {
        // Test code
        assertTrue(someCondition);
    } catch (AssertionError e) {
        TestUtils.captureScreenshotOnFailure(driver, "test_failed");
        throw e;
    }
}
```

**Screenshots saved to:** `target/screenshots/`

---

## 🎯 **Advanced Patterns**

### **1. Data-Driven Testing**
```java
String[][] testData = {
    {"admin", "admin123", "Customer", "true"},
    {"", "", "", "false"},
    {"admin", "wrong", "Customer", "false"}
};

for (String[] data : testData) {
    String username = data[0];
    String password = data[1];
    boolean shouldSucceed = Boolean.parseBoolean(data[3]);
    
    if (shouldSucceed) {
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs(username, password, data[2]);
        assertTrue(dashboard.isDashboardPageDisplayed());
    } else {
        // Verify error
    }
}
```

### **2. Helper Methods**
```java
private DashboardPage performLogin(String user, String pass, String type) {
    return new LoginPage(driver)
        .open()
        .loginAs(user, pass, type);
}

@Test
public void test1() {
    DashboardPage dashboard = performLogin("admin", "pass", "Customer");
}

@Test
public void test2() {
    DashboardPage dashboard = performLogin("user", "pass", "Customer");
}
```

### **3. Complete Workflows**
```java
@Test
public void testCompleteUserJourney() {
    // Login
    DashboardPage dashboard = new LoginPage(driver)
        .open()
        .loginAs("admin", "admin123", "Customer");
    
    // Navigate
    var transactions = dashboard.goToTransactions();
    TestUtils.captureScreenshot(driver, "transactions");
    
    // Back
    dashboard = transactions.goToDashboard();
    
    // Logout
    LoginPage loginPage = dashboard.logout();
    assertTrue(loginPage.isLoginPageDisplayed());
}
```

---

## ✅ **Best Practices**

### **DO's ✅**

1. **Keep Locators Private**
   ```java
   private By loginButton = By.id("loginBtn"); // ✓
   ```

2. **Return Page Objects**
   ```java
   public DashboardPage clickLogin() { return new DashboardPage(driver); }
   ```

3. **Use Method Chaining**
   ```java
   page.enterUsername("user").enterPassword("pass").clickLogin();
   ```

4. **Create BasePage for Common Methods**
   ```java
   public class BasePage { /* common methods */ }
   ```

5. **One Page Object per Page**
   ```java
   LoginPage, DashboardPage, RegisterPage, etc.
   ```

6. **Add Verification Methods**
   ```java
   public boolean isLoginPageDisplayed() { ... }
   ```

### **DON'Ts ❌**

1. **Don't Put Assertions in Page Objects**
   ```java
   // ❌ BAD
   public void verifyTitle() {
       assertEquals("Login", getTitle()); // NO!
   }
   ```

2. **Don't Make Locators Public**
   ```java
   public By loginButton = By.id("loginBtn"); // ❌
   ```

3. **Don't Put Test Logic in Page Objects**
   ```java
   // ❌ BAD
   public void testLogin() { /* test logic here */ }
   ```

4. **Don't Create God Objects**
   ```java
   // ❌ BAD - 100+ methods in one page object
   ```

---

## 🎯 **Run Tests**

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=Test01_PageObjectModelBasics

# Run specific method
mvn test -Dtest=Test01_PageObjectModelBasics#testBasicLoginWithPOM

# Expected output:
Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## 📁 **Project Structure**

```
selenium-level4/
├── src/main/java/com/npci/training/
│   ├── pages/                      # Page Objects
│   │   ├── BasePage.java          # Base class
│   │   ├── LoginPage.java         # Login page
│   │   ├── DashboardPage.java     # Dashboard page
│   │   ├── RegisterPage.java      # Register page
│   │   └── OtherPages.java        # Other pages
│   └── utils/
│       └── TestUtils.java         # Utilities
└── src/test/java/com/npci/training/level4/
    ├── Test01_PageObjectModelBasics.java
    └── Test02_AdvancedPOMPatterns.java
```

---

## 💯 **After Level 4, You Can:**

- ✅ Explain Page Object Model
- ✅ Create Page Object classes
- ✅ Use BasePage pattern
- ✅ Implement method chaining
- ✅ Capture screenshots
- ✅ Write maintainable tests
- ✅ Organize test code properly
- ✅ Use helper methods
- ✅ Implement data-driven tests
- ✅ **Build production-ready frameworks!**

---

## 🎓 **Learning Path**

### **Hour 1: POM Basics (60 min)**
1. Understand POM concept
2. Study BasePage.java
3. Study LoginPage.java
4. Run Test01_PageObjectModelBasics
5. Understand method chaining

### **Hour 2: Create Page Objects (60 min)**
1. Practice creating page objects
2. Implement method chaining
3. Add verification methods
4. Test your page objects

### **Hour 3: Advanced Patterns (60 min)**
1. Learn screenshot capture
2. Implement data-driven tests
3. Create helper methods
4. Run Test02_AdvancedPOMPatterns

### **Hour 4: Practice (60 min)**
1. Rewrite Level 1-3 tests using POM
2. Create your own page objects
3. Build complete test suite
4. Document with screenshots

---

## 🔜 **Next Steps**

After mastering Level 4:
- TestNG/JUnit 5 advanced features
- Parallel test execution
- CI/CD integration
- Selenium Grid
- Docker containers
- Test reporting
- Cucumber BDD

---

## 💡 **Key Takeaways**

**Before POM:**
```java
// 50 lines of repeated code
driver.findElement(By.id("username")).sendKeys("admin");
driver.findElement(By.id("password")).sendKeys("pass");
// ... everywhere
```

**After POM:**
```java
// 3 lines, reusable, maintainable
new LoginPage(driver)
    .open()
    .loginAs("admin", "pass", "Customer");
```

**Impact:**
- 🚀 **80% less code**
- 📖 **100% more readable**
- 🔧 **10x easier to maintain**
- ✅ **Production-ready!**

---

## 🎉 **You're Now a Selenium Professional!**

**With Level 4 complete, you can:**
- Build enterprise-grade test frameworks
- Write maintainable automation
- Lead automation projects
- Mentor junior testers
- **Ship production-quality code!**

---

**Welcome to professional test automation!** 🚀

```bash
mvn test
```

🎓 **Happy Testing!**
