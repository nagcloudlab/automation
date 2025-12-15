# 🎓 Selenium Level 3 - Waits, Synchronization & Advanced Patterns

## Master Production-Ready Test Automation

---

## 📚 What You'll Learn

### **Level 3 Topics (3-4 hours)**
1. ✅ Implicit Waits - Global waiting strategy
2. ✅ Explicit Waits - WebDriverWait and smart waiting
3. ✅ Expected Conditions - 25+ wait conditions
4. ✅ Wait Best Practices - When to use which wait
5. ✅ Synchronization Patterns - Real-world scenarios

---

## 📦 Package Contents

```
selenium-level3/
├── pom.xml
├── README.md (this file)
└── src/test/java/com/npci/training/level3/
    ├── Test01_ImplicitWaits.java         # 8 tests - Global waits
    ├── Test02_ExplicitWaits.java         # 13 tests - Smart waits
    └── Test03_ExpectedConditions.java    # 22 tests - All conditions
```

**Total: 43 tests** across 3 files

---

## 🚀 Quick Start

### Prerequisites
```
✓ Completed Level 1 & Level 2
✓ Banking Portal running on http://localhost:8000
✓ Java 11+, Maven installed
✓ Understand locators and basic interactions
```

### Run Your First Level 3 Test
```bash
# Extract and import
unzip selenium-level3.zip

# Run Implicit Waits test
mvn test -Dtest=Test01_ImplicitWaits

# Run all Level 3 tests
mvn test
```

---

## 📖 Test Files Explained

### **Test01_ImplicitWaits.java** ⭐ START HERE
**Duration:** 15 minutes | **Tests:** 8

**What it teaches:**
- What implicit waits are
- How to set implicit wait
- Scope of implicit waits
- When to use implicit waits
- Implicit vs Thread.sleep()
- Best practices

**Run it:**
```bash
mvn test -Dtest=Test01_ImplicitWaits
```

**Key Concepts:**
```java
// Set implicit wait (applies to all findElement calls)
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

// Now all findElement() will wait up to 10 seconds
driver.findElement(By.id("username")).sendKeys("admin");
driver.findElement(By.id("password")).sendKeys("pass123");
```

**Benefits:**
- ✅ Set once, works everywhere
- ✅ Waits only as long as needed
- ✅ Simpler than Thread.sleep()
- ✅ Good for static pages

**Limitations:**
- ❌ Less flexible
- ❌ Can't wait for specific conditions
- ❌ Not ideal for dynamic content

---

### **Test02_ExplicitWaits.java** ⭐ PRODUCTION STANDARD
**Duration:** 25 minutes | **Tests:** 13

**What it teaches:**
- WebDriverWait class
- until() method
- Basic Expected Conditions
- Custom timeouts
- Chaining waits
- Explicit vs Implicit comparison
- Handling TimeoutException

**Run it:**
```bash
mvn test -Dtest=Test02_ExplicitWaits
```

**Key Concepts:**
```java
// Create WebDriverWait
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

// Wait for element to be clickable
WebElement button = wait.until(
    ExpectedConditions.elementToBeClickable(By.id("loginBtn"))
);

// Wait for alert
Alert alert = wait.until(ExpectedConditions.alertIsPresent());

// Wait for URL change
wait.until(ExpectedConditions.urlContains("dashboard.html"));

// Wait for title
wait.until(ExpectedConditions.titleContains("Login"));
```

**Benefits:**
- ✅ Wait for specific conditions
- ✅ Very flexible
- ✅ Different timeouts for different elements
- ✅ Perfect for AJAX/dynamic content

**Best For:**
- Modern web applications
- Single Page Applications (SPA)
- AJAX-heavy pages
- Dynamic content loading

---

### **Test03_ExpectedConditions.java** ⭐ COMPREHENSIVE
**Duration:** 30 minutes | **Tests:** 22

**What it teaches:**
- All 25+ Expected Conditions
- Element state conditions
- Alert & frame conditions
- Text & attribute conditions
- Combining conditions (AND, OR, NOT)
- Real-world examples

**Run it:**
```bash
mvn test -Dtest=Test03_ExpectedConditions
```

**All Expected Conditions Covered:**

#### **Element Presence & Visibility**
```java
// Element in DOM (may be hidden)
presenceOfElementLocated(By locator)

// Element visible on page
visibilityOfElementLocated(By locator)

// Element not visible
invisibilityOfElementLocated(By locator)

// Element visible + enabled
elementToBeClickable(By locator)
```

#### **Text & Attributes**
```java
// Element contains text
textToBePresentInElementLocated(By locator, String text)

// Attribute equals value
attributeToBe(By locator, String attribute, String value)

// Attribute contains value
attributeContains(By locator, String attribute, String value)
```

#### **Page & Navigation**
```java
// Exact title
titleIs(String title)

// Title contains text
titleContains(String title)

// Exact URL
urlToBe(String url)

// URL contains text
urlContains(String fraction)
```

#### **Element State**
```java
// Checkbox/radio selected
elementToBeSelected(By locator)

// Element became stale
stalenessOf(WebElement element)
```

#### **Alerts & Frames**
```java
// Alert popup present
alertIsPresent()

// Frame ready and switch
frameToBeAvailableAndSwitchToIt(By locator)
```

#### **Multiple Elements**
```java
// Exact count
numberOfElementsToBe(By locator, Integer number)

// Greater than
numberOfElementsToBeMoreThan(By locator, Integer number)

// Less than
numberOfElementsToBeLessThan(By locator, Integer number)
```

#### **Logical Operators**
```java
// All conditions must be true
and(ExpectedCondition... conditions)

// Any condition can be true
or(ExpectedCondition... conditions)

// Condition must be false
not(ExpectedCondition condition)
```

---

## 🎯 Complete Examples

### **Example 1: Login with Explicit Waits**
```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

// Wait for each element
WebElement username = wait.until(
    ExpectedConditions.visibilityOfElementLocated(By.id("username"))
);
username.sendKeys("admin");

WebElement password = wait.until(
    ExpectedConditions.visibilityOfElementLocated(By.id("password"))
);
password.sendKeys("admin123");

WebElement loginBtn = wait.until(
    ExpectedConditions.elementToBeClickable(By.id("loginBtn"))
);
loginBtn.click();

// Wait for alert
Alert alert = wait.until(ExpectedConditions.alertIsPresent());
alert.accept();

// Wait for navigation
wait.until(ExpectedConditions.urlContains("dashboard.html"));
```

### **Example 2: Wait for Dynamic Content**
```java
// Wait for table to load
List<WebElement> rows = wait.until(
    ExpectedConditions.presenceOfAllElementsLocatedBy(
        By.cssSelector("#transactionTable tbody tr")
    )
);

// Wait for specific row count
wait.until(
    ExpectedConditions.numberOfElementsToBe(
        By.cssSelector("#transactionTable tbody tr"),
        5
    )
);
```

### **Example 3: Combining Conditions**
```java
// Element must be BOTH visible AND clickable
WebElement button = wait.until(
    ExpectedConditions.and(
        ExpectedConditions.visibilityOfElementLocated(By.id("submitBtn")),
        ExpectedConditions.elementToBeClickable(By.id("submitBtn"))
    )
);

// Wait for EITHER element
WebElement element = wait.until(
    ExpectedConditions.or(
        ExpectedConditions.visibilityOfElementLocated(By.id("username")),
        ExpectedConditions.visibilityOfElementLocated(By.id("email"))
    )
);
```

---

## 📊 Wait Strategies Comparison

| Feature | Implicit Wait | Explicit Wait | Thread.sleep() |
|---------|---------------|---------------|----------------|
| **Set once, applies everywhere** | ✅ Yes | ❌ No | ❌ No |
| **Waits only as needed** | ✅ Yes | ✅ Yes | ❌ No |
| **Wait for conditions** | ❌ No | ✅ Yes | ❌ No |
| **Flexible timeout** | ❌ No | ✅ Yes | ❌ No |
| **Dynamic content** | ⚠️ Limited | ✅ Excellent | ❌ Poor |
| **Complexity** | ⭐ Simple | ⭐⭐ Moderate | ⭐ Simple |
| **Speed** | ⭐⭐⭐ Fast | ⭐⭐⭐ Fast | ⭐ Slow |
| **Production Use** | ⚠️ Limited | ✅ Recommended | ❌ Never |

---

## 💡 **Decision Tree: Which Wait to Use?**

```
Is page completely static?
├─ YES → Implicit Wait (Duration.ofSeconds(10))
└─ NO ↓

Need to wait for AJAX/dynamic content?
├─ YES → Explicit Wait (WebDriverWait)
└─ NO ↓

Need to wait for specific condition?
├─ YES → Explicit Wait with Expected Condition
└─ NO ↓

Just need fixed delay for demo?
├─ YES → Thread.sleep() (NOT for production!)
└─ NO → Use Explicit Wait (safest choice)
```

---

## 🎓 Learning Path

### **Hour 1: Implicit Waits (15 min)**
1. ✅ Run Test01_ImplicitWaits
2. ✅ Understand how it works
3. ✅ See scope examples
4. ✅ Compare with Thread.sleep()
5. ✅ Learn when to use

### **Hour 2: Explicit Waits (25 min)**
1. ✅ Run Test02_ExplicitWaits
2. ✅ Understand WebDriverWait
3. ✅ Learn basic conditions
4. ✅ Practice with alerts
5. ✅ Handle URL/title waits

### **Hour 3: Expected Conditions (30 min)**
1. ✅ Run Test03_ExpectedConditions
2. ✅ Learn all conditions
3. ✅ Practice element states
4. ✅ Try combining conditions
5. ✅ Build production patterns

### **Hour 4: Practice (60 min)**
1. ✅ Rewrite Level 1 tests with explicit waits
2. ✅ Rewrite Level 2 tests with proper waits
3. ✅ Create your own wait examples
4. ✅ Test on real applications

---

## ✅ Best Practices

### **DO's ✅**

1. **Use Explicit Waits for Production**
   ```java
   WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
   wait.until(ExpectedConditions.elementToBeClickable(By.id("button")));
   ```

2. **Set Reasonable Timeouts**
   - 10 seconds: Standard wait
   - 20 seconds: Slow operations
   - 5 seconds: Quick checks

3. **Wait for Specific Conditions**
   ```java
   // Good: Wait for clickable
   wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
   
   // Not good: Just presence
   wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submit")));
   ```

4. **Handle Exceptions**
   ```java
   try {
       wait.until(ExpectedConditions.alertIsPresent());
   } catch (TimeoutException e) {
       // Alert didn't appear
   }
   ```

### **DON'Ts ❌**

1. **Don't Use Thread.sleep() in Production**
   ```java
   // ❌ BAD
   Thread.sleep(3000);
   
   // ✅ GOOD
   wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
   ```

2. **Don't Mix Implicit and Explicit Waits**
   ```java
   // ❌ BAD - Can cause unexpected behavior
   driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
   WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
   ```

3. **Don't Use Same Timeout for Everything**
   ```java
   // ✅ GOOD - Different timeouts
   WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
   WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(20));
   ```

4. **Don't Ignore Stale Element Exceptions**
   ```java
   try {
       element.click();
   } catch (StaleElementReferenceException e) {
       // Re-find element
       element = driver.findElement(By.id("button"));
       element.click();
   }
   ```

---

## 🎯 Common Patterns

### **Pattern 1: Wait and Interact**
```java
WebElement element = wait.until(
    ExpectedConditions.elementToBeClickable(By.id("button"))
);
element.click();
```

### **Pattern 2: Wait for Page Load**
```java
wait.until(ExpectedConditions.titleContains("Dashboard"));
wait.until(ExpectedConditions.urlContains("dashboard.html"));
```

### **Pattern 3: Wait for AJAX**
```java
wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loader")));
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content")));
```

### **Pattern 4: Wait for Dynamic Count**
```java
wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
    By.cssSelector(".item"),
    5
));
```

---

## 📝 Quick Reference

### **Setup**
```java
@BeforeEach
public void setup() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.manage().window().maximize();
    
    // For explicit waits
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    
    // OR for implicit waits (not both!)
    // driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
}
```

### **Common Waits**
```java
// Element clickable
wait.until(ExpectedConditions.elementToBeClickable(locator))

// Element visible
wait.until(ExpectedConditions.visibilityOfElementLocated(locator))

// Alert present
wait.until(ExpectedConditions.alertIsPresent())

// URL change
wait.until(ExpectedConditions.urlContains("text"))

// Text present
wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, "text"))
```

---

## ✅ Self-Assessment

After Level 3, you should be able to:

- [ ] Explain implicit vs explicit waits
- [ ] Set up WebDriverWait
- [ ] Use 10+ Expected Conditions
- [ ] Choose appropriate wait strategy
- [ ] Handle dynamic content
- [ ] Wait for alerts
- [ ] Wait for URL/title changes
- [ ] Combine multiple conditions
- [ ] Handle timeout exceptions
- [ ] Debug wait-related issues
- [ ] Write production-ready waits
- [ ] Avoid Thread.sleep()

**Score:** ___/12

**If 10+:** Ready for real projects! 🎉  
**If 7-9:** More practice needed  
**If <7:** Review and repeat tests

---

## 🚀 Next Steps

### **Immediate (Next 30 minutes)**
1. Run Test01_ImplicitWaits
2. Understand the concepts
3. Compare with Thread.sleep()
4. Run Test02_ExplicitWaits

### **Practice (Next 2 hours)**
1. Run all tests
2. Modify timeouts
3. Try different conditions
4. Apply to your own pages

### **Level 4 Preview:**
- Page Object Model (POM)
- Test data management
- Screenshot capture
- Reporting
- CI/CD integration

---

## 💯 Level 3 Complete!

**Congratulations! You've learned:**
- ✅ 3 wait strategies
- ✅ 25+ Expected Conditions
- ✅ Production-ready patterns
- ✅ Synchronization best practices

**Total Concepts:** 30+  
**Total Tests:** 43  
**Time Investment:** 3-4 hours  
**Skill Level:** Advanced → Expert  

---

**Master waits and synchronization!** 🚀

```bash
mvn test
```

🎓 **Happy Testing!**
