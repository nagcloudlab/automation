# 🎭 Playwright Level 1 - Playwright Basics

## Welcome to Playwright Automation!

**Level:** Beginner  
**Duration:** 3-4 hours  
**Prerequisites:** Java 11+, Maven, Basic Java knowledge  

---

## 📚 What You'll Learn

### **Core Topics:**
1. ✅ Playwright installation and setup
2. ✅ Browser launch (Chromium, Firefox, WebKit)
3. ✅ Page navigation
4. ✅ Browser Context isolation
5. ✅ Basic interactions (click, type, fill)
6. ✅ BaseTest pattern
7. ✅ Test lifecycle management

### **Skill Level:** Zero → Beginner Playwright Developer

---

## 📦 Package Contents

```
playwright-level1/
├── pom.xml                                  # Maven configuration
├── README.md                                # This file
└── src/
    ├── test/java/com/npci/training/
    │   ├── tests/
    │   │   └── BaseTest.java                # Base class for all tests
    │   └── level1/
    │       ├── Test01_FirstPlaywrightScript.java      # 5 tests
    │       ├── Test02_MultipleBrowsers.java           # 6 tests
    │       ├── Test03_BrowserContext.java             # 6 tests
    │       ├── Test04_BasicInteractions.java          # 6 tests
    │       └── Test05_UsingBaseTest.java              # 6 tests
    └── test/resources/
```

**Total:** 29 executable tests + BaseTest framework

---

## 🚀 Quick Start

### **Step 1: Extract Package**
```bash
unzip playwright-level1.zip
cd playwright-level1
```

### **Step 2: Install Playwright Browsers**
```bash
# This downloads Chromium, Firefox, and WebKit
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

### **Step 3: Run First Test**
```bash
mvn test -Dtest=Test01_FirstPlaywrightScript
```

### **Step 4: Watch the Magic! ✨**
Browser will launch automatically and run tests!

---

## 📖 Test Classes Overview

### **Test01: First Playwright Script (20 mins)**
**File:** `Test01_FirstPlaywrightScript.java`

**Topics:**
- Playwright initialization
- Browser launch
- Page navigation
- Basic assertions
- Headless vs Headed mode

**Tests:**
1. `test01_LaunchBrowserAndNavigate` - Basic browser launch
2. `test02_NavigateToBankingPortal` - Navigate to local app
3. `test03_BrowserOptions` - Custom browser options
4. `test04_MultiplePages` - Multiple pages in one browser
5. `test05_HeadlessVsHeaded` - Headless vs visible browser

**Run:**
```bash
mvn test -Dtest=Test01_FirstPlaywrightScript
```

---

### **Test02: Multiple Browsers (25 mins)**
**File:** `Test02_MultipleBrowsers.java`

**Topics:**
- Chromium browser
- Firefox browser
- WebKit browser (Safari engine)
- Cross-browser testing
- Browser-specific features

**Tests:**
1. `test01_ChromiumBrowser` - Launch Chromium
2. `test02_FirefoxBrowser` - Launch Firefox
3. `test03_WebKitBrowser` - Launch WebKit (Safari)
4. `test04_CrossBrowserTesting` - Same test on all browsers
5. `test05_CompareBrowserRendering` - Compare rendering
6. `test06_BrowserSpecificFeatures` - Browser-specific options

**Run:**
```bash
mvn test -Dtest=Test02_MultipleBrowsers
```

**Key Learning:**
```java
// Chromium (Chrome, Edge)
Browser chromium = playwright.chromium().launch();

// Firefox
Browser firefox = playwright.firefox().launch();

// WebKit (Safari)
Browser webkit = playwright.webkit().launch();
```

---

### **Test03: Browser Context (30 mins)**
**File:** `Test03_BrowserContext.java`

**Topics:**
- What is Browser Context?
- Context isolation
- Multiple users simulation
- Context options (viewport, permissions, geolocation)
- Performance benefits

**Tests:**
1. `test01_SingleContextBasics` - Basic context usage
2. `test02_MultipleContextsIsolation` - Isolation demo
3. `test03_SimulatingMultipleUsers` - Multi-user testing
4. `test04_ContextWithCustomViewport` - Desktop/Mobile/Tablet
5. `test05_ContextOptions` - Permissions, geolocation
6. `test06_PerformanceComparison` - Context vs multiple browsers

**Run:**
```bash
mvn test -Dtest=Test03_BrowserContext
```

**Key Concept:**
```
Browser Context = Incognito Window
- Complete isolation
- Separate cookies, storage
- Multiple users in ONE browser
- MUCH faster than multiple browsers
```

**Real-World Example:**
```java
// Admin user
BrowserContext admin = browser.newContext();
Page adminPage = admin.newPage();

// Customer user
BrowserContext customer = browser.newContext();
Page customerPage = customer.newPage();

// Both running simultaneously, completely isolated!
```

---

### **Test04: Basic Interactions (25 mins)**
**File:** `Test04_BasicInteractions.java`

**Topics:**
- Click actions
- Type vs Fill
- Navigation methods
- Form interactions
- Wait methods

**Tests:**
1. `test01_ClickInteractions` - Click elements
2. `test02_TypeVsFill` - Type vs Fill comparison
3. `test03_NavigationMethods` - Navigate, back, forward, reload
4. `test04_FormInteractions` - Complete form submission
5. `test05_PagePropertiesAndMethods` - Page URL, title, content
6. `test06_WaitMethods` - Different wait strategies

**Run:**
```bash
mvn test -Dtest=Test04_BasicInteractions
```

**Key Learning:**
```java
// FILL vs TYPE
page.fill("#username", "admin");        // ✅ Fast (recommended)
page.type("#username", "admin");        // ⚠️ Slow (character by character)

// Use fill() for 99% of cases!
```

---

### **Test05: Using BaseTest (15 mins)**
**File:** `Test05_UsingBaseTest.java`

**Topics:**
- Extending BaseTest
- No manual setup/teardown
- Test isolation
- Clean test code
- Best practices

**Tests:**
1. `test01_SimpleNavigation` - Using page from BaseTest
2. `test02_FormInteraction` - No setup needed
3. `test03_TestIsolation` - Each test gets fresh context
4. `test04_VerifyIsolation` - Verify no shared state
5. `test05_UsingBrowserContext` - Using context from BaseTest
6. `test06_AdditionalPages` - Creating extra pages

**Run:**
```bash
mvn test -Dtest=Test05_UsingBaseTest
```

**Key Pattern:**
```java
public class MyTests extends BaseTest {
    @Test
    void test() {
        // page, browser, context already available!
        page.navigate("https://example.com");
        // No cleanup needed - automatic!
    }
}
```

---

## 🎯 Learning Path

### **Day 1: Getting Started (1 hour)**
1. Setup project
2. Install Playwright browsers
3. Run Test01_FirstPlaywrightScript
4. Understand Playwright vs Selenium

### **Day 2: Multiple Browsers (1 hour)**
1. Run Test02_MultipleBrowsers
2. Understand cross-browser testing
3. Try each browser
4. Compare browser rendering

### **Day 3: Browser Context (1 hour)**
1. Run Test03_BrowserContext
2. Understand context isolation
3. Try multi-user scenarios
4. Appreciate performance benefits

### **Day 4: Interactions & BaseTest (1 hour)**
1. Run Test04_BasicInteractions
2. Practice form interactions
3. Run Test05_UsingBaseTest
4. Build your own test using BaseTest

---

## 💡 Playwright vs Selenium

| Feature | Selenium | Playwright |
|---------|----------|------------|
| **Browser Support** | Chrome, Firefox, Safari | Chrome, Firefox, WebKit ✅ |
| **Installation** | Different drivers per browser | One command for all ⚡ |
| **Auto-waiting** | ❌ Manual waits everywhere | ✅ Built-in smart waiting |
| **Speed** | ⚠️ Moderate | ✅ Fast |
| **Context Isolation** | ❌ No native support | ✅ Built-in |
| **API Design** | ⚠️ Complex | ✅ Simple, intuitive |
| **Cross-browser** | ⚠️ Setup per browser | ✅ Works out of the box |
| **DevTools** | ⚠️ Limited | ✅ Full support |
| **Modern Features** | ⚠️ Limited | ✅ Screenshots, videos, traces |

**Verdict:** Playwright is modern, fast, and easier to use! 🎭

---

## 📝 Key Concepts Summary

### **1. Playwright Architecture**
```
Playwright
    ↓
Browser (Chromium/Firefox/WebKit)
    ↓
BrowserContext (Isolation)
    ↓
Page (Web Page)
```

### **2. Basic Flow**
```java
// 1. Create Playwright
Playwright playwright = Playwright.create();

// 2. Launch Browser
Browser browser = playwright.chromium().launch();

// 3. Create Context (isolation)
BrowserContext context = browser.newContext();

// 4. Create Page
Page page = context.newPage();

// 5. Navigate
page.navigate("https://example.com");

// 6. Interact
page.click("button");
page.fill("input", "text");

// 7. Cleanup
context.close();
browser.close();
playwright.close();
```

### **3. Why Browser Context?**
```
Without Context:
Browser 1 (Chrome) → User 1
Browser 2 (Chrome) → User 2
Browser 3 (Chrome) → User 3
❌ Slow, resource-heavy

With Context:
Browser 1 (Chrome)
    ├── Context 1 → User 1
    ├── Context 2 → User 2
    └── Context 3 → User 3
✅ Fast, efficient, isolated
```

---

## 🔧 Configuration

### **pom.xml Dependencies**
```xml
<dependency>
    <groupId>com.microsoft.playwright</groupId>
    <artifactId>playwright</artifactId>
    <version>1.40.0</version>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.1</version>
    <scope>test</scope>
</dependency>
```

### **Browser Installation**
```bash
# Install all browsers
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"

# Install specific browser
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
```

### **Run Commands**
```bash
# Run all tests in a class
mvn test -Dtest=Test01_FirstPlaywrightScript

# Run specific test
mvn test -Dtest=Test01_FirstPlaywrightScript#test01_LaunchBrowserAndNavigate

# Run all Level 1 tests
mvn test
```

---

## 🎓 Best Practices Learned

### **✅ DO:**
1. **Use Browser Context for isolation**
   - Much faster than multiple browsers
   - Perfect isolation between tests

2. **Use fill() for text input**
   - Faster than type()
   - Recommended for 99% of cases

3. **Extend BaseTest**
   - Clean test code
   - Automatic setup/teardown
   - Test isolation

4. **Run tests in headless mode for CI/CD**
   ```java
   .setHeadless(true)  // For automated execution
   ```

5. **Use headed mode for debugging**
   ```java
   .setHeadless(false) // To see what's happening
   ```

### **❌ DON'T:**
1. **Don't launch multiple browsers unnecessarily**
   - Use contexts instead
   - Save resources

2. **Don't use type() by default**
   - Slower than fill()
   - Only for special cases

3. **Don't forget to close resources**
   - Or use BaseTest to handle it

4. **Don't use explicit waits**
   - Playwright auto-waits
   - Will learn more in Level 3

---

## 🚀 What's Next?

### **After completing Level 1, you can:**
- [x] Launch Playwright and browsers
- [x] Navigate to web pages
- [x] Understand browser contexts
- [x] Perform basic interactions
- [x] Write clean tests with BaseTest
- [x] Run tests on multiple browsers

### **Ready for Level 2?**
**Level 2: Locators & Actions**
- Advanced element selection
- Role-based locators
- Complex interactions
- Drag and drop
- File uploads
- Keyboard shortcuts

---

## 📞 Troubleshooting

### **Issue 1: Browsers not installing**
```bash
# Try manual installation
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"

# Check Java version (needs 11+)
java -version
```

### **Issue 2: Tests not running**
```bash
# Clean and rebuild
mvn clean test

# Check test name is correct
mvn test -Dtest=Test01_FirstPlaywrightScript
```

### **Issue 3: Browser not visible**
```java
// Change headless to false
.setHeadless(false)
```

### **Issue 4: Tests running too fast**
```java
// Add slowMo to see actions
.setSlowMo(1000) // 1 second delay per action
```

---

## 💯 Self-Assessment

### **After Level 1, can you:**
- [ ] Explain what Playwright is?
- [ ] Launch Chromium, Firefox, and WebKit?
- [ ] Explain what Browser Context is?
- [ ] Create isolated test contexts?
- [ ] Navigate to web pages?
- [ ] Click elements and fill forms?
- [ ] Use BaseTest pattern?
- [ ] Run tests from command line?

**If you can do all of the above, you're ready for Level 2!** 🎉

---

## 📚 Additional Resources

- **Playwright Docs:** https://playwright.dev/java/
- **API Reference:** https://playwright.dev/java/docs/api/class-playwright
- **GitHub:** https://github.com/microsoft/playwright-java
- **Community:** https://github.com/microsoft/playwright/discussions

---

## 🎉 Congratulations!

You've completed **Playwright Level 1!** 🎭

You now understand:
- ✅ Playwright basics
- ✅ Multi-browser testing
- ✅ Browser context isolation
- ✅ Basic interactions
- ✅ Test framework patterns

**Next:** Move to Level 2 for advanced locators and actions!

---

**Happy Testing with Playwright!** 🚀
