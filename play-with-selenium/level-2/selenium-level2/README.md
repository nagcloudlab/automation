# 🎓 Selenium Level 2 - Advanced Locators & Navigation

## Complete Training Package with JUnit 5

---

## 📚 What You'll Learn

### **Level 2 Topics (3-4 hours)**
1. ✅ CSS Selectors (tag, class, ID, attribute, combinations)
2. ✅ XPath Selectors (absolute, relative, functions, axes)
3. ✅ Link Text Locators (exact and partial)
4. ✅ Navigation Commands (back, forward, refresh)
5. ✅ Multiple Windows & Tabs Management
6. ✅ Working with HTML Tables

---

## 📦 Package Contents

```
selenium-level2/
├── pom.xml
├── README.md (this file)
├── EXERCISES.md
├── LOCATOR_GUIDE.md
└── src/
    └── test/java/com/npci/training/level2/
        ├── Test01_CSSSelectors.java          # 8 tests - CSS patterns
        ├── Test02_XPathSelectors.java         # 8 tests - XPath mastery
        ├── Test03_LinkTextLocators.java       # 6 tests - Link navigation
        ├── Test04_NavigationCommands.java     # 7 tests - Browser commands
        ├── Test05_MultipleWindows.java        # 6 tests - Window management
        └── Test06_WorkingWithTables.java      # 10 tests - Table operations
```

**Total: 45 tests** across 6 files

---

## 🚀 Quick Start

### Prerequisites
```
✓ Completed Level 1
✓ Banking Portal running on http://localhost:8000
✓ Java 11+, Maven installed
```

### Run Your First Level 2 Test
```bash
# Extract and import
unzip selenium-level2.zip

# Run CSS Selectors test
mvn test -Dtest=Test01_CSSSelectors

# Run all Level 2 tests
mvn test
```

---

## 📖 Test Files Explained

### **Test01_CSSSelectors.java** ⭐ START HERE
**Duration:** 20 minutes | **Tests:** 8

**What it teaches:**
- CSS tag selectors (`h1`, `input`, `button`)
- ID selector (`#username`)
- Class selector (`.form-group`, `.btn-primary`)
- Attribute selectors (`[type='password']`, `[id^='user']`)
- Combination selectors (`input#username`, `button.btn.btn-primary`)
- Child and descendant selectors (`form > div`, `form input`)
- Pseudo-classes (`:first-child`, `:nth-child(2)`)

**Run it:**
```bash
mvn test -Dtest=Test01_CSSSelectors
```

**Key Concepts:**
```java
// ID selector
By.cssSelector("#username")

// Class selector
By.cssSelector(".btn-primary")

// Attribute selector
By.cssSelector("[type='password']")

// Starts with
By.cssSelector("[id^='user']")

// Ends with
By.cssSelector("[id$='Btn']")

// Contains
By.cssSelector("[id*='login']")

// Combination
By.cssSelector("input#username")
By.cssSelector("button.btn.btn-primary")

// Child
By.cssSelector("form > div")

// Descendant
By.cssSelector("form input")

// Pseudo-class
By.cssSelector(".form-group:first-child")
By.cssSelector(".form-group:nth-child(2)")
```

---

### **Test02_XPathSelectors.java**
**Duration:** 25 minutes | **Tests:** 8

**What it teaches:**
- Basic XPath syntax (`//tagname[@attribute='value']`)
- Attribute-based selection (`//input[@type='password']`)
- text() function (`//button[text()='Login']`)
- contains() function (`//button[contains(@id, 'login')]`)
- starts-with() function (`//input[starts-with(@id, 'user')]`)
- XPath axes (parent, child, sibling)
- Indexing and predicates (`(//input)[1]`, `[last()]`)

**Run it:**
```bash
mvn test -Dtest=Test02_XPathSelectors
```

**Key Concepts:**
```java
// Basic XPath
By.xpath("//input[@id='username']")

// With text
By.xpath("//button[text()='Login']")

// Contains
By.xpath("//button[contains(@id, 'login')]")
By.xpath("//h1[contains(text(), 'Banking')]")

// Starts with
By.xpath("//input[starts-with(@id, 'user')]")

// Multiple conditions
By.xpath("//input[@id='username' and @type='text']")

// Parent axis
By.xpath("//input[@id='username']/parent::div")

// Following sibling
By.xpath("//input[@id='username']/parent::div/following-sibling::div[1]")

// Indexing
By.xpath("(//input)[1]")  // First input
By.xpath("(//input)[last()]")  // Last input

// Complex
By.xpath("//form//button[contains(@class, 'btn-primary') and contains(text(), 'Login')]")
```

---

### **Test03_LinkTextLocators.java**
**Duration:** 15 minutes | **Tests:** 6

**What it teaches:**
- By.linkText() for exact match
- By.partialLinkText() for partial match
- When to use link text vs other locators
- Navigation using links
- Multi-page navigation flows

**Run it:**
```bash
mvn test -Dtest=Test03_LinkTextLocators
```

**Key Concepts:**
```java
// Exact link text
By.linkText("Register here")

// Partial link text
By.partialLinkText("Forgot")

// Navigation
driver.findElement(By.linkText("Transactions")).click();
```

---

### **Test04_NavigationCommands.java**
**Duration:** 20 minutes | **Tests:** 7

**What it teaches:**
- navigate().to() vs driver.get()
- navigate().back()
- navigate().forward()
- navigate().refresh()
- getCurrentUrl()
- getTitle()
- getPageSource()

**Run it:**
```bash
mvn test -Dtest=Test04_NavigationCommands
```

**Key Concepts:**
```java
// Navigate
driver.navigate().to("http://localhost:8000/login.html");

// Back
driver.navigate().back();

// Forward
driver.navigate().forward();

// Refresh
driver.navigate().refresh();

// Get properties
String url = driver.getCurrentUrl();
String title = driver.getTitle();
String source = driver.getPageSource();
```

---

### **Test05_MultipleWindows.java**
**Duration:** 20 minutes | **Tests:** 6

**What it teaches:**
- Getting window handles
- Switching between windows
- Parent-child window pattern
- Closing specific windows
- Window management best practices

**Run it:**
```bash
mvn test -Dtest=Test05_MultipleWindows
```

**Key Concepts:**
```java
// Get main window
String mainWindow = driver.getWindowHandle();

// Get all windows
Set<String> allWindows = driver.getWindowHandles();

// Switch to window
driver.switchTo().window(windowHandle);

// Close current window
driver.close();

// Best practice: Use ArrayList
ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
driver.switchTo().window(tabs.get(1));
```

---

### **Test06_WorkingWithTables.java**
**Duration:** 25 minutes | **Tests:** 10

**What it teaches:**
- Getting row and column counts
- Reading specific cell values
- Iterating through all rows
- Finding rows by specific text
- Clicking buttons in table rows
- Calculating sums from table data
- Filtering table data
- Pagination handling

**Run it:**
```bash
mvn test -Dtest=Test06_WorkingWithTables
```

**Key Concepts:**
```java
// Get row count
List<WebElement> rows = driver.findElements(
    By.cssSelector("#transactionTable tbody tr")
);

// Get column count
List<WebElement> headers = driver.findElements(
    By.cssSelector("#transactionTable thead th")
);

// Read specific cell
WebElement cell = driver.findElement(
    By.cssSelector("#transactionTable tbody tr:nth-child(1) td:nth-child(1)")
);

// Iterate rows
for (WebElement row : rows) {
    List<WebElement> cells = row.findElements(By.tagName("td"));
    String value = cells.get(0).getText();
}

// Find row by text with XPath
By.xpath("//td[contains(text(), 'UPI')]/parent::tr")

// Click button in row
driver.findElement(
    By.cssSelector("#transactionTable tbody tr:nth-child(1) button")
).click();
```

---

## 🎯 Run All Tests

```bash
# Complete Level 2 suite
mvn test

# Expected output:
Tests run: 45, Failures: 0, Errors: 0, Skipped: 0

# Individual test files
mvn test -Dtest=Test01_CSSSelectors
mvn test -Dtest=Test02_XPathSelectors
mvn test -Dtest=Test03_LinkTextLocators
mvn test -Dtest=Test04_NavigationCommands
mvn test -Dtest=Test05_MultipleWindows
mvn test -Dtest=Test06_WorkingWithTables
```

---

## 📊 Concepts Coverage

### **Locator Strategies (Complete!)**
| Strategy | Syntax | When to Use |
|----------|--------|-------------|
| ID | `#id` or `[@id='value']` | Most reliable |
| Class | `.class` or `[@class='value']` | Grouped elements |
| Tag | `tag` or `//tag` | Generic selection |
| Attribute | `[attr='value']` | Any attribute |
| Link Text | `By.linkText()` | Navigation links |
| Partial Link | `By.partialLinkText()` | Partial match |
| CSS Selector | `By.cssSelector()` | Fast, powerful |
| XPath | `By.xpath()` | Complex navigation |

### **CSS Selector Patterns**
- Tag: `input`, `button`, `div`
- ID: `#username`
- Class: `.btn-primary`
- Attribute: `[type='password']`
- Starts with: `[id^='user']`
- Ends with: `[id$='Btn']`
- Contains: `[id*='login']`
- Multiple classes: `.btn.btn-primary`
- Child: `form > div`
- Descendant: `form input`
- Pseudo: `:first-child`, `:nth-child(2)`

### **XPath Functions**
- `text()`: `//button[text()='Login']`
- `contains()`: `//button[contains(@id, 'login')]`
- `starts-with()`: `//input[starts-with(@id, 'user')]`
- `last()`: `(//input)[last()]`
- `position()`: `(//input)[position()=3]`

### **XPath Axes**
- `parent`: `//input[@id='username']/parent::div`
- `child`: `//form/child::div`
- `following-sibling`: `//div/following-sibling::div[1]`
- `preceding-sibling`: `//div/preceding-sibling::div[1]`

---

## 🎓 Learning Path

### **Day 1: Locators (2 hours)**
1. ✅ Run Test01_CSSSelectors (20 min)
2. ✅ Practice CSS patterns (20 min)
3. ✅ Run Test02_XPathSelectors (25 min)
4. ✅ Practice XPath patterns (25 min)
5. ✅ Run Test03_LinkTextLocators (15 min)
6. ✅ Complete Exercises 1-3 (35 min)

### **Day 2: Navigation & Windows (2 hours)**
1. ✅ Run Test04_NavigationCommands (20 min)
2. ✅ Practice navigation (15 min)
3. ✅ Run Test05_MultipleWindows (20 min)
4. ✅ Practice window switching (20 min)
5. ✅ Complete Exercises 4-6 (45 min)

### **Day 3: Tables (1.5 hours)**
1. ✅ Run Test06_WorkingWithTables (25 min)
2. ✅ Practice table operations (30 min)
3. ✅ Complete Exercises 7-8 (35 min)

---

## 📝 Quick Reference

### **CSS vs XPath - When to Use**

**Use CSS when:**
- ✅ Performance matters (CSS is faster)
- ✅ Simple, straightforward selection
- ✅ Modern web applications
- ✅ Selecting by class, ID, attribute

**Use XPath when:**
- ✅ Need to navigate up the DOM (parent)
- ✅ Need text-based selection
- ✅ Complex conditions
- ✅ Index-based selection

### **Locator Priority (Best to Worst)**

1. **ID** - Most reliable, fastest
2. **Name** - Good for forms
3. **CSS Selector** - Fast, powerful
4. **XPath** - Flexible but slower
5. **Link Text** - Only for links
6. **Class Name** - Can be unstable
7. **Tag Name** - Too generic

---

## ✅ Self-Assessment

After Level 2, you should be able to:

- [ ] Write CSS selectors for any element
- [ ] Write XPath selectors for complex scenarios
- [ ] Choose appropriate locator strategy
- [ ] Navigate between pages programmatically
- [ ] Handle multiple windows/tabs
- [ ] Switch between windows correctly
- [ ] Read data from HTML tables
- [ ] Iterate through table rows
- [ ] Find specific data in tables
- [ ] Click elements within tables
- [ ] Handle pagination
- [ ] Use contains(), starts-with() in XPath
- [ ] Use parent, child axes in XPath
- [ ] Debug locator issues

**Score:** ___/14

**If 12+:** Ready for Level 3! 🎉  
**If 8-11:** Practice exercises  
**If <8:** Review and repeat

---

## 🚀 Next Steps

### **Immediate (Next 30 minutes)**
1. Run Test01_CSSSelectors
2. Read the output
3. Understand each selector
4. Try modifying selectors

### **Practice (Next 2 hours)**
1. Complete all 8 exercises
2. Create your own test cases
3. Practice on different pages
4. Experiment with selectors

### **Ready for Level 3:**
- Explicit Waits
- Fluent Waits
- Expected Conditions
- Page Object Model
- Data-driven testing

---

## 💯 Level 2 Complete!

**Congratulations! You've learned:**
- ✅ 8 locator strategies
- ✅ CSS selector patterns
- ✅ XPath functions and axes
- ✅ Navigation commands
- ✅ Window management
- ✅ Table operations

**Total Concepts:** 35+  
**Total Tests:** 45  
**Time Investment:** 3-4 hours  
**Skill Level:** Competent → Advanced  

---

**Ready to master advanced locators!** 🚀

```bash
mvn test
```

🎓 **Happy Testing!**
