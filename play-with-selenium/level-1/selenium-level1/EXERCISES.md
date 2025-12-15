# 📝 Level 1 Practice Exercises

## Hands-On Practice Tasks

---

## Exercise 1: First Test from Scratch (20 minutes)

### **Task:**
Create a new test file `Exercise01_MyFirstTest.java` that:
1. Opens the browser
2. Navigates to register.html
3. Prints the page title
4. Closes the browser

### **Starter Code:**
```java
package com.npci.training.level1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Exercise01_MyFirstTest {
    
    @Test
    public void testOpenRegisterPage() {
        // TODO: Setup WebDriver
        
        // TODO: Create driver instance
        
        // TODO: Navigate to http://localhost:8000/register.html
        
        // TODO: Print page title
        
        // TODO: Close browser
    }
}
```

### **Expected Output:**
```
Page Title: Register - Banking Portal
```

### **Solution Location:**
solutions/Exercise01_Solution.java (try first!)

---

## Exercise 2: Locate and Print (25 minutes)

### **Task:**
Create `Exercise02_LocateElements.java` that finds and prints properties of:
1. Full Name field (id: "fullName")
2. Email field (id: "email")
3. Mobile field (id: "mobile")
4. Register button (find by class "btn-primary")

### **Print for each element:**
- Tag name
- Placeholder
- Type attribute
- Is displayed
- Is enabled

### **Starter Code:**
```java
@Test
public void testLocateRegistrationElements() {
    WebDriverManager.chromedriver().setup();
    WebDriver driver = new ChromeDriver();
    driver.get("http://localhost:8000/register.html");
    
    // TODO: Find fullName field
    WebElement fullName = driver.findElement(By.id("fullName"));
    
    // TODO: Print all properties
    System.out.println("Full Name Field:");
    System.out.println("  Tag: " + fullName.getTagName());
    // Add more properties
    
    // TODO: Repeat for email, mobile, button
    
    driver.quit();
}
```

---

## Exercise 3: Fill Registration Form (30 minutes)

### **Task:**
Create `Exercise03_FillRegistration.java` that:
1. Fills all registration form fields
2. Selects radio button (gender)
3. Selects dropdown (account type)
4. Checks both checkboxes
5. Clicks register button
6. Verifies navigation to login page

### **Test Data:**
```
Full Name: Rajesh Kumar
Email: rajesh@npci.com
Mobile: 9876543210
DOB: 1990-01-15
Gender: Male
Account Type: Savings
```

### **Starter Code:**
```java
@Test
public void testCompleteRegistration() {
    // Setup
    WebDriverManager.chromedriver().setup();
    WebDriver driver = new ChromeDriver();
    driver.manage().window().maximize();
    driver.get("http://localhost:8000/register.html");
    
    try {
        // TODO: Fill full name
        
        // TODO: Fill email
        
        // TODO: Fill mobile
        
        // TODO: Fill DOB
        
        // TODO: Select gender radio button
        // Hint: By.xpath("//input[@value='male']")
        
        // TODO: Select account type dropdown
        
        // TODO: Enter password
        
        // TODO: Enter confirm password
        
        // TODO: Check newsletter
        
        // TODO: Check terms
        
        // TODO: Click register
        
        // TODO: Wait and verify URL contains "login.html"
        Thread.sleep(2000);
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);
        
        if (currentUrl.contains("login.html")) {
            System.out.println("✓ Registration successful!");
        }
        
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        driver.quit();
    }
}
```

---

## Exercise 4: Validation Tests (35 minutes)

### **Task:**
Create `Exercise04_ValidationTests.java` with 4 test methods:
1. `testEmptyFullName()` - Verify error for empty name
2. `testInvalidEmail()` - Verify error for invalid email format
3. `testShortMobile()` - Verify error for mobile < 10 digits
4. `testPasswordMismatch()` - Verify error when passwords don't match

### **Structure:**
```java
public class Exercise04_ValidationTests {
    
    WebDriver driver;
    
    @BeforeEach
    public void setup() {
        // Setup driver and navigate
    }
    
    @AfterEach
    public void teardown() {
        // Close driver
    }
    
    @Test
    @DisplayName("Test empty full name validation")
    public void testEmptyFullName() {
        // TODO: Leave name empty, fill other required fields
        
        // TODO: Click register
        
        // TODO: Check error message
        WebElement error = driver.findElement(By.id("fullNameError"));
        
        // TODO: Assert error is not empty
    }
    
    // TODO: Add other 3 tests
}
```

---

## Exercise 5: Dashboard Navigation (25 minutes)

### **Task:**
Create `Exercise05_DashboardTest.java` that:
1. Logs in successfully
2. Verifies dashboard page loaded
3. Reads all 4 card values (balance, transactions, etc.)
4. Prints the values
5. Counts transaction table rows
6. Clicks logout
7. Verifies back on login page

### **Starter Code:**
```java
@Test
public void testDashboardNavigation() {
    // Setup
    driver.get("http://localhost:8000/login.html");
    
    // Login
    driver.findElement(By.id("username")).sendKeys("admin");
    driver.findElement(By.id("password")).sendKeys("admin123");
    // TODO: Complete login
    
    // Verify dashboard
    Thread.sleep(2000);
    assertTrue(driver.getCurrentUrl().contains("dashboard.html"));
    
    // Read balance
    WebElement balance = driver.findElement(By.id("balance"));
    System.out.println("Balance: " + balance.getText());
    
    // TODO: Read other card values
    
    // TODO: Count table rows
    List<WebElement> rows = driver.findElements(
        By.cssSelector("#transactionTable tbody tr")
    );
    System.out.println("Transaction count: " + rows.size());
    
    // TODO: Click logout
    
    // TODO: Handle alert
    
    // TODO: Verify back on login page
}
```

---

## Exercise 6: Utility Class Practice (20 minutes)

### **Task:**
Enhance `TestUtils.java` with these methods:

1. `fillLoginForm(WebDriver driver, String username, String password, String userType)`
2. `verifyPageTitle(WebDriver driver, String expectedTitle)`
3. `takeScreenshot(WebDriver driver, String filename)`
4. `getTableRowCount(WebDriver driver, String tableId)`

### **Example Usage:**
```java
TestUtils.fillLoginForm(driver, "admin", "admin123", "Customer");
TestUtils.verifyPageTitle(driver, "Dashboard");
```

### **Starter Code:**
```java
public static void fillLoginForm(WebDriver driver, 
                                 String username, 
                                 String password, 
                                 String userType) {
    // TODO: Implement
}

public static boolean verifyPageTitle(WebDriver driver, 
                                      String expectedTitle) {
    // TODO: Implement
    return driver.getTitle().contains(expectedTitle);
}
```

---

## Exercise 7: Multi-Page Test (30 minutes)

### **Task:**
Create `Exercise07_CompleteJourney.java` - a complete user journey:
1. Register new user
2. Navigate to login
3. Login with registered credentials
4. Verify dashboard
5. Navigate to transactions page
6. Fill UPI form
7. Logout

### **Flow:**
```
register.html → login.html → dashboard.html → transactions.html → login.html
```

---

## Exercise 8: Error Recovery (25 minutes)

### **Task:**
Create `Exercise08_ErrorRecovery.java` that tests:
1. Enter wrong password 3 times
2. Each time verify error message
3. On 3rd attempt, use correct password
4. Verify successful login

### **Pseudo Code:**
```java
for (int i = 1; i <= 3; i++) {
    if (i < 3) {
        // Enter wrong password
        // Verify error
        // Clear fields
    } else {
        // Enter correct password
        // Verify success
    }
}
```

---

## Exercise 9: Checkbox and Radio Testing (20 minutes)

### **Task:**
Test all checkbox and radio button states on register page:

1. Verify all start unchecked/unselected
2. Click each one
3. Verify state changed
4. Uncheck checkboxes
5. Verify state changed back

### **Elements to Test:**
- Gender radio buttons (3)
- Newsletter checkbox
- Terms checkbox

---

## Exercise 10: Dropdown Testing (25 minutes)

### **Task:**
Create comprehensive dropdown tests:

1. Test selecting all options in user type dropdown (login page)
2. Test selecting all options in account type dropdown (register page)
3. Verify each selection
4. Print all available options

### **Methods to Use:**
- `selectByVisibleText()`
- `selectByValue()`
- `selectByIndex()`
- `getOptions()`
- `getFirstSelectedOption()`

---

## 🎯 Challenge Exercises

### **Challenge 1: Data-Driven Login (45 minutes)**
Create a test that reads login credentials from an array and tests each:

```java
String[][] testData = {
    {"admin", "admin123", "Customer", "true"},  // Should pass
    {"user1", "wrong", "Admin", "false"},       // Should fail
    {"", "admin123", "Customer", "false"},      // Should fail
};

for (String[] data : testData) {
    // Run test with data
}
```

### **Challenge 2: Custom Assertions (30 minutes)**
Create your own assertion methods in TestUtils:

```java
public static void assertElementDisplayed(WebElement element, String message) {
    if (!element.isDisplayed()) {
        throw new AssertionError(message);
    }
}

public static void assertTextContains(String actual, String expected) {
    // Implement
}
```

### **Challenge 3: Page Load Verification (40 minutes)**
Create a method that verifies a page loaded correctly by checking:
- Title
- URL
- Presence of key element
- Page readiness

---

## ✅ Exercise Completion Checklist

Track your progress:

- [ ] Exercise 1: First Test from Scratch
- [ ] Exercise 2: Locate and Print
- [ ] Exercise 3: Fill Registration Form
- [ ] Exercise 4: Validation Tests
- [ ] Exercise 5: Dashboard Navigation
- [ ] Exercise 6: Utility Class Practice
- [ ] Exercise 7: Multi-Page Test
- [ ] Exercise 8: Error Recovery
- [ ] Exercise 9: Checkbox and Radio Testing
- [ ] Exercise 10: Dropdown Testing
- [ ] Challenge 1: Data-Driven Login
- [ ] Challenge 2: Custom Assertions
- [ ] Challenge 3: Page Load Verification

**Score:** ___/13

---

## 📊 Difficulty Levels

### **Easy (1-2):** ⭐
Good for beginners, focus on basics

### **Medium (3-7):** ⭐⭐
Require combining multiple concepts

### **Hard (8-10):** ⭐⭐⭐
Complex scenarios, multiple pages

### **Challenge:** ⭐⭐⭐⭐
Advanced concepts, custom implementations

---

## 💡 Tips for Success

1. **Start Simple:** Complete exercises in order
2. **Test Often:** Run tests frequently to verify
3. **Read Errors:** Error messages tell you what's wrong
4. **Use Prints:** Add System.out.println() to debug
5. **Take Breaks:** Don't rush, understanding is key
6. **Ask Questions:** Review test files for examples
7. **Experiment:** Try different approaches
8. **Document:** Add comments to your code

---

## 🆘 Getting Stuck?

### **Strategy:**
1. **Review:** Look at similar test in provided files
2. **Debug:** Add print statements to see values
3. **Simplify:** Break complex task into smaller steps
4. **Search:** Check Selenium documentation
5. **Experiment:** Try different approaches
6. **Move On:** Skip and come back later

### **Common Issues:**
- **Element not found:** Check ID, check page loaded
- **Test fails:** Add Thread.sleep() to slow down
- **Null pointer:** Check if element exists first
- **Wrong page:** Verify navigation successful

---

## 🎓 Learning Outcomes

After completing these exercises, you will:

✅ Be comfortable writing Selenium tests  
✅ Know how to locate elements  
✅ Understand element interactions  
✅ Can handle forms completely  
✅ Can write validation tests  
✅ Can structure tests properly  
✅ Can debug simple issues  
✅ Ready for Level 2!  

---

## ⏱️ Time Estimates

**All Exercises:** 4-5 hours  
**Easy (1-2):** 45 minutes  
**Medium (3-7):** 2.5 hours  
**Hard (8-10):** 1.5 hours  
**Challenges:** 2 hours  

**Recommended:**  
- Day 1: Exercises 1-3 (1.5 hours)
- Day 2: Exercises 4-6 (1.5 hours)
- Day 3: Exercises 7-10 (2 hours)
- Day 4: Challenges (Optional)

---

## 🚀 Ready to Practice?

**Start with Exercise 1 NOW!**

```bash
# Create the file
# Write the code
# Run the test
mvn test -Dtest=Exercise01_MyFirstTest
```

**Good luck!** 🎯
