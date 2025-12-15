# 🎓 Selenium Level 1 - Fundamentals

## Complete Training Package with JUnit 5

---

## 📚 What You'll Learn

### **Level 1 Topics (2-3 hours)**
1. ✅ WebDriver Setup & Browser Launch
2. ✅ Basic Locator Strategies (ID, Name, ClassName)
3. ✅ Element Interactions (sendKeys, click, clear)
4. ✅ Dropdown Handling (Select class)
5. ✅ Element Properties (getText, getAttribute)
6. ✅ Basic Assertions with JUnit 5
7. ✅ Test Structure (@BeforeEach, @AfterEach)
8. ✅ Complete Login Test Suite

---

## 📦 Package Contents

```
selenium-level1/
├── pom.xml                                    # Maven dependencies
├── src/
│   ├── main/java/com/npci/training/utils/
│   │   └── TestUtils.java                    # Utility methods
│   └── test/java/com/npci/training/level1/
│       ├── Test01_FirstScript.java           # Your first test
│       ├── Test02_LocatingElements.java      # Locator strategies
│       ├── Test03_BasicInteractions.java     # Element interactions
│       ├── Test04_CompleteLoginTests.java    # Full test suite (6 tests)
│       └── Test05_UsingUtilityClass.java     # Clean code patterns
├── README.md                                  # This file
└── EXERCISES.md                               # Practice exercises
```

---

## 🚀 Quick Start (5 Minutes)

### Prerequisites
```bash
✓ Java 11 or higher
✓ Maven installed
✓ Banking Portal running on http://localhost:8000
```

### Step 1: Extract & Import
```bash
# Extract the zip
unzip selenium-level1.zip

# Import in IntelliJ/Eclipse
File → Open → Select selenium-level1 folder
```

### Step 2: Install Dependencies
```bash
# From command line
mvn clean install

# Or let IDE auto-download
```

### Step 3: Run Your First Test
```bash
# Run Test01
mvn test -Dtest=Test01_FirstScript

# Expected output:
# ========== TEST STARTED ==========
# Step 1: Setting up ChromeDriver
# Step 2: Creating Chrome browser instance
# ...
# ✓ SUCCESS: Correct page loaded!
# ========== TEST COMPLETED ==========
```

---

## 📖 Test Files Explained

### **Test01_FirstScript.java** ⭐ START HERE
**Duration:** 10 minutes

**What it teaches:**
- WebDriver setup with WebDriverManager
- Browser launch and maximize
- Navigation to URL
- Getting page properties (title, URL)
- Proper cleanup with try-finally

**Run it:**
```bash
mvn test -Dtest=Test01_FirstScript
```

**What happens:**
1. ChromeDriver is set up automatically
2. Browser opens and maximizes
3. Navigates to login page
4. Prints page title and URL
5. Waits 3 seconds (so you can see it)
6. Closes browser

**Key Concepts:**
```java
// Setup driver
WebDriverManager.chromedriver().setup();
WebDriver driver = new ChromeDriver();

// Navigate
driver.get("http://localhost:8000/login.html");

// Get properties
String title = driver.getTitle();
String url = driver.getCurrentUrl();

// Cleanup
driver.quit();
```

---

### **Test02_LocatingElements.java**
**Duration:** 15 minutes

**What it teaches:**
- By.id() - Most reliable locator
- By.name() - Form elements
- By.className() - CSS classes
- Element properties (isDisplayed, isEnabled, getAttribute)

**Run it:**
```bash
mvn test -Dtest=Test02_LocatingElements
```

**4 Test Methods:**
1. `testLocateById()` - Find elements by ID
2. `testLocateByName()` - Find by name attribute
3. `testLocateByClassName()` - Find by CSS class
4. `testElementProperties()` - Check all properties

**Key Concepts:**
```java
// By ID (most common)
WebElement username = driver.findElement(By.id("username"));

// By Name
WebElement password = driver.findElement(By.name("password"));

// By Class Name
WebElement button = driver.findElement(By.className("btn-primary"));

// Check properties
boolean displayed = username.isDisplayed();
boolean enabled = username.isEnabled();
String type = username.getAttribute("type");
```

---

### **Test03_BasicInteractions.java**
**Duration:** 20 minutes

**What it teaches:**
- sendKeys() - Enter text
- click() - Click elements
- clear() - Clear text fields
- getText() - Get element text
- Select class - Handle dropdowns
- getAttribute() - Get attribute values

**Run it:**
```bash
mvn test -Dtest=Test03_BasicInteractions
```

**6 Test Methods:**
1. `testSendKeys()` - Enter text in fields
2. `testClick()` - Click checkboxes
3. `testDropdown()` - Handle dropdown
4. `testGetText()` - Get element text
5. `testClear()` - Clear text fields
6. `testCompleteFormInteraction()` - Full login flow

**Key Concepts:**
```java
// Enter text
username.sendKeys("admin");

// Click checkbox
checkbox.click();

// Handle dropdown
Select dropdown = new Select(driver.findElement(By.id("userType")));
dropdown.selectByVisibleText("Customer");
dropdown.selectByValue("admin");
dropdown.selectByIndex(1);

// Get text
String buttonText = loginBtn.getText();

// Clear field
username.clear();
```

---

### **Test04_CompleteLoginTests.java** ⭐ IMPORTANT
**Duration:** 25 minutes

**What it teaches:**
- Multiple test methods in one class
- Positive testing (valid login)
- Negative testing (validations)
- Assertions with JUnit 5
- @BeforeEach and @AfterEach
- @Order for test sequence
- @DisplayName for readable test names

**Run it:**
```bash
mvn test -Dtest=Test04_CompleteLoginTests
```

**6 Test Cases:**
1. ✅ TC01: Valid login with correct credentials
2. ✅ TC02: Login with empty username (validation)
3. ✅ TC03: Login with empty password (validation)
4. ✅ TC04: Login without selecting user type
5. ✅ TC05: Login without accepting terms
6. ✅ TC06: Test clear button functionality

**Key Concepts:**
```java
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test04_CompleteLoginTests {
    
    WebDriver driver;
    
    @BeforeEach  // Runs before EACH test
    public void setup() {
        driver = new ChromeDriver();
        driver.get("http://localhost:8000/login.html");
    }
    
    @AfterEach  // Runs after EACH test
    public void teardown() {
        driver.quit();
    }
    
    @Test
    @Order(1)
    @DisplayName("TC01: Valid login")
    public void testValidLogin() {
        // Test code
        assertTrue(currentUrl.contains("dashboard.html"));
    }
}
```

**Assertions Used:**
```java
assertEquals(expected, actual);
assertTrue(condition);
assertFalse(condition);
assertNotNull(object);
```

---

### **Test05_UsingUtilityClass.java**
**Duration:** 15 minutes

**What it teaches:**
- Code reusability
- Utility methods
- Cleaner test code
- Better logging
- Cross-browser capability

**Run it:**
```bash
mvn test -Dtest=Test05_UsingUtilityClass
```

**Using TestUtils:**
```java
// Initialize driver
driver = TestUtils.initializeDriver("chrome");

// Logging
TestUtils.log("Starting test");
TestUtils.logSuccess("Login successful");
TestUtils.logError("Test failed");

// Formatted output
TestUtils.printTestHeader("My Test");

// Simple wait (demo only)
TestUtils.sleep(2); // 2 seconds
```

---

## 🎯 Run All Tests

### Run Complete Suite
```bash
# Run all Level 1 tests
mvn test

# Expected output:
# [INFO] Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
```

### Run Specific Test File
```bash
mvn test -Dtest=Test01_FirstScript
mvn test -Dtest=Test02_LocatingElements
mvn test -Dtest=Test03_BasicInteractions
mvn test -Dtest=Test04_CompleteLoginTests
mvn test -Dtest=Test05_UsingUtilityClass
```

### Run Specific Test Method
```bash
mvn test -Dtest=Test04_CompleteLoginTests#testValidLogin
mvn test -Dtest=Test04_CompleteLoginTests#testEmptyUsername
```

### Run from IDE
```
Right-click on test file → Run 'TestFileName'
Right-click on test method → Run 'methodName()'
```

---

## 📊 Test Execution Summary

**Total Tests:** 17

### By File:
- Test01_FirstScript: 1 test
- Test02_LocatingElements: 4 tests
- Test03_BasicInteractions: 6 tests
- Test04_CompleteLoginTests: 6 tests
- Test05_UsingUtilityClass: 2 tests

### Test Duration:
- Fast tests (< 5 sec): 5 tests
- Medium tests (5-10 sec): 8 tests
- Slow tests (10+ sec): 4 tests
- **Total time:** ~3-4 minutes

---

## 🎓 Learning Path

### **Hour 1: Basics**
1. ✅ Read this README (10 min)
2. ✅ Run Test01_FirstScript (5 min)
3. ✅ Understand the code (10 min)
4. ✅ Run Test02_LocatingElements (10 min)
5. ✅ Practice locators (15 min)
6. ✅ Complete Exercise 1 (10 min)

### **Hour 2: Interactions**
1. ✅ Run Test03_BasicInteractions (10 min)
2. ✅ Practice each interaction (20 min)
3. ✅ Complete Exercise 2 (20 min)
4. ✅ Complete Exercise 3 (10 min)

### **Hour 3: Testing**
1. ✅ Run Test04_CompleteLoginTests (10 min)
2. ✅ Understand test structure (15 min)
3. ✅ Write your own test (20 min)
4. ✅ Complete Exercise 4 (15 min)

---

## 🔧 Troubleshooting

### Issue: ChromeDriver not found
```
Solution: WebDriverManager handles this automatically
If still fails: mvn clean install
```

### Issue: Port 8000 not accessible
```
Solution: Update baseUrl in test files
Change: http://localhost:8000/login.html
To: http://localhost:8080/login.html (or your port)
```

### Issue: Tests run too fast
```
Add Thread.sleep() to slow down:
Thread.sleep(2000); // 2 seconds
```

### Issue: Element not found
```
Check:
1. Is banking portal running?
2. Is URL correct?
3. Is element ID correct?
4. Add wait before finding element
```

### Issue: Browser doesn't close
```
Always use try-finally or @AfterEach
driver.quit() in finally block
```

---

## 📝 Key Concepts Summary

### **1. Locator Strategies**
```java
By.id("username")           // Most reliable
By.name("password")         // Form elements
By.className("btn-primary") // CSS class
```

### **2. Element Interactions**
```java
element.sendKeys("text")    // Enter text
element.click()             // Click
element.clear()             // Clear field
element.getText()           // Get text
element.getAttribute("id")  // Get attribute
```

### **3. Dropdown Handling**
```java
Select dropdown = new Select(element);
dropdown.selectByVisibleText("Customer");
dropdown.selectByValue("admin");
dropdown.selectByIndex(1);
```

### **4. Element Properties**
```java
element.isDisplayed()       // Visible?
element.isEnabled()         // Enabled?
element.isSelected()        // Selected? (checkbox/radio)
```

### **5. JUnit 5 Annotations**
```java
@BeforeEach                 // Before each test
@AfterEach                  // After each test
@Test                       // Test method
@DisplayName("...")         // Readable test name
@Order(1)                   // Test order
```

### **6. Assertions**
```java
assertEquals(expected, actual)
assertTrue(condition)
assertFalse(condition)
assertNotNull(object)
```

---

## ✅ Self-Assessment Checklist

After completing Level 1, you should be able to:

- [ ] Setup WebDriver with WebDriverManager
- [ ] Launch and close browser
- [ ] Navigate to a URL
- [ ] Locate elements using ID, Name, ClassName
- [ ] Enter text in input fields
- [ ] Click buttons and checkboxes
- [ ] Handle dropdown selections
- [ ] Get element text and attributes
- [ ] Check element properties (displayed, enabled)
- [ ] Clear text fields
- [ ] Write basic assertions
- [ ] Structure tests with @BeforeEach/@AfterEach
- [ ] Run tests from command line
- [ ] Run tests from IDE
- [ ] Read and understand test output
- [ ] Debug basic issues
- [ ] Write a complete login test

**Score:** ___/17

**If you checked 15+:** Ready for Level 2! 🎉  
**If you checked 10-14:** Practice exercises needed  
**If you checked <10:** Review and repeat Level 1

---

## 🚀 Next Steps

### **Immediate (Next 10 minutes):**
1. Run all tests: `mvn test`
2. Verify all pass
3. Read test output
4. Understand each test

### **Practice (Next 1 hour):**
1. Complete all exercises in EXERCISES.md
2. Modify tests to try different scenarios
3. Add your own test cases
4. Experiment with different locators

### **Ready for Level 2:**
- CSS Selectors
- XPath
- Advanced locators
- Link text
- Partial link text
- Tag name
- Multiple elements

---

## 📚 Resources

**In This Package:**
- README.md (this file)
- EXERCISES.md (practice tasks)
- 5 test files (17 tests total)
- TestUtils.java (utilities)

**External:**
- [Selenium Docs](https://www.selenium.dev/documentation/)
- [JUnit 5 Guide](https://junit.org/junit5/docs/current/user-guide/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)

---

## 🎯 Success Criteria

**You've mastered Level 1 when you can:**

✅ Write a test to login successfully  
✅ Write a test for empty username validation  
✅ Write a test for empty password validation  
✅ Write a test for dropdown selection  
✅ Write a test to verify checkbox states  
✅ Use assertions correctly  
✅ Structure tests properly  
✅ Run tests successfully  
✅ Debug simple issues  
✅ Understand test output  

---

## 💯 Level 1 Complete!

**Congratulations! You've learned:**
- ✅ WebDriver basics
- ✅ 3 locator strategies
- ✅ 8 element interactions
- ✅ JUnit 5 test structure
- ✅ Assertions
- ✅ Complete test suite

**Total Concepts Covered:** 20+  
**Total Tests Written:** 17  
**Time Investment:** 2-3 hours  
**Skill Level:** Beginner → Competent  

---

**Ready to start? Run your first test NOW!**

```bash
mvn test -Dtest=Test01_FirstScript
```

🎓 **Happy Testing!**
