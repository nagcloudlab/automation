# 🎭 Playwright Level 3 - Auto-Waiting & Assertions

## Master Smart Waits and Web-First Assertions

**Level:** Intermediate  
**Duration:** 3-4 hours  
**Prerequisites:** Playwright Levels 1 & 2 completed  

---

## 📚 What You'll Learn

### **Core Topics:**
1. ✅ Playwright auto-waiting mechanisms
2. ✅ Actionability checks
3. ✅ assertThat() API - Auto-retrying assertions
4. ✅ Web-first assertions
5. ✅ Soft assertions
6. ✅ Wait strategies (waitForSelector, waitForLoadState)
7. ✅ Timeout configuration

### **Skill Level:** Intermediate → Advanced Playwright Developer

---

## 🌟 The Playwright Difference

### **Selenium (Manual Waits Everywhere):**
```java
// Selenium - SO MUCH WAIT CODE! 😫
WebDriverWait wait = new WebDriverWait(driver, 10);

wait.until(ExpectedConditions.elementToBeClickable(button));
button.click();

wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("result")));
wait.until(ExpectedConditions.textToBePresentInElement(result, "Success"));

String text = result.getText();
assertEquals("Success", text);

// 6+ lines for simple interaction!
```

### **Playwright (Auto-Wait Magic):**
```java
// Playwright - CLEAN AND SIMPLE! ✨
page.locator("button").click();

assertThat(page.locator("#result"))
    .hasText("Success");

// 2 lines! No manual waits! Auto-retry! 🎉
```

**Result:** 
- 90% less wait code
- 3x faster tests
- Zero flaky tests
- Much more readable!

---

## 📦 Package Contents

```
playwright-level3/
├── pom.xml
├── README.md
└── src/test/java/com/npci/training/
    ├── tests/
    │   └── BaseTest.java
    └── level3/
        ├── Test01_AutoWaiting.java             # 8 tests
        └── Test02_PlaywrightAssertions.java    # 10 tests
```

**Total:** 18 tests demonstrating auto-waiting and assertions

---

## 📖 Test Classes Overview

### **Test01: Auto-Waiting Basics (30 mins)** - 8 Tests

**Topics:**
- What is auto-waiting?
- Actionability checks (5 checks!)
- No explicit waits needed
- Performance benefits
- When auto-wait is NOT used

**Key Concepts:**
```java
// ✅ PLAYWRIGHT AUTO-WAIT
page.locator("button").click();
// Automatically waits for:
// ✅ Attached to DOM
// ✅ Visible
// ✅ Stable (not animating)
// ✅ Receives events (not covered)
// ✅ Enabled

// No manual wait needed! 🎉
```

**Actionability Checks:**
1. **ATTACHED** - Element in DOM
2. **VISIBLE** - Has bounding box, not display:none
3. **STABLE** - Not animating, position settled
4. **RECEIVES EVENTS** - Not covered by other elements
5. **ENABLED** - Not disabled (for form controls)

**Tests:**
1. `test01_AutoWaitForElementToAppear` - Wait for dynamic element
2. `test02_AutoWaitForVisibility` - Wait for hidden→visible
3. `test03_ActionabilityChecks` - Understand all 5 checks
4. `test04_NoExplicitWaitsNeeded` - Selenium vs Playwright
5. `test05_AutoWaitForDifferentActions` - All actions auto-wait
6. `test06_AutoWaitPerformanceBenefits` - 3x faster!
7. `test07_WhenAutoWaitIsNotUsed` - Exceptions
8. `test08_BankingExample` - Real-world scenario

**Run:**
```bash
mvn test -Dtest=Test01_AutoWaiting
```

**Key Takeaway:**
```
Selenium: 45 seconds (with explicit waits)
Playwright: 15 seconds (with auto-wait)
Result: 3x FASTER! ⚡
```

---

### **Test02: Playwright Assertions (30 mins)** - 10 Tests

**Topics:**
- assertThat() API
- Auto-retrying assertions
- Locator assertions
- Page assertions
- Negation (not())

**Key Concepts:**
```java
// ✅ PLAYWRIGHT ASSERTIONS (Auto-retry!)
assertThat(page.locator("#result"))
    .isVisible();
// Retries every 50ms for up to 5 seconds
// No manual wait needed!

// ❌ JUNIT ASSERTIONS (No retry!)
assertTrue(page.locator("#result").isVisible());
// Checks immediately, fails if not ready
// Requires manual wait before assertion
```

**Why assertThat() is Better:**
1. **Auto-retry** - Waits until condition met (up to 5s)
2. **No flaky tests** - Retries handle timing issues
3. **Better errors** - Shows what went wrong clearly
4. **No manual waits** - Smart waiting built-in

**Tests:**
1. `test01_BasicAssertThat` - Basic usage
2. `test02_AutoRetryingAssertions` - See retry in action
3. `test03_AssertThatVsJUnit` - Comparison
4. `test04_LocatorAssertionsVisibility` - Visibility checks
5. `test05_LocatorAssertionsText` - Text checks
6. `test06_LocatorAssertionsAttributes` - Attribute checks
7. `test07_LocatorAssertionsState` - State checks
8. `test08_PageAssertions` - Page-level checks
9. `test09_BankingExample` - Real banking assertions
10. `test10_AllAssertionsSummary` - Complete reference

**Common Assertions:**
```java
// VISIBILITY
assertThat(locator).isVisible();
assertThat(locator).isHidden();
assertThat(locator).not().isVisible();

// TEXT
assertThat(locator).hasText("exact");
assertThat(locator).containsText("partial");
assertThat(locator).hasText(Pattern.compile("regex"));

// STATE
assertThat(locator).isChecked();
assertThat(locator).isEnabled();
assertThat(locator).isDisabled();
assertThat(locator).isEditable();

// ATTRIBUTES
assertThat(locator).hasAttribute("name", "value");
assertThat(locator).hasClass("class-name");
assertThat(locator).hasId("id");

// PAGE
assertThat(page).hasTitle("title");
assertThat(page).hasURL("url");
```

**Run:**
```bash
mvn test -Dtest=Test02_PlaywrightAssertions
```

---

## 🎯 Real-World Banking Example

### **Complete Login & Transfer Flow:**

```java
// LOGIN (All auto-wait!)
page.getByLabel("Username").fill("rajesh.kumar");
page.getByLabel("Password").fill("SecurePass123!");
page.getByRole(AriaRole.BUTTON, setName("Login")).click();

// VERIFY DASHBOARD (Auto-retry assertions!)
assertThat(page).hasURL(Pattern.compile(".*/dashboard"));
assertThat(page.getByRole(AriaRole.HEADING, setName("Dashboard")))
    .isVisible();
assertThat(page.getByTestId("account-balance"))
    .isVisible()
    .containsText("₹");

// NAVIGATE TO TRANSFER (Auto-wait!)
page.getByRole(AriaRole.LINK, setName("Transfer")).click();

// FILL TRANSFER FORM (All auto-wait!)
page.getByLabel("From Account").selectOption("savings");
page.getByLabel("To Account").fill("9876543210");
page.getByLabel("Amount").fill("5000");
page.getByLabel("Remarks").fill("Rent payment");

// VERIFY BUTTON ENABLED (Auto-retry!)
assertThat(page.getByRole(AriaRole.BUTTON, setName("Transfer")))
    .isEnabled();

// SUBMIT (Auto-wait!)
page.getByRole(AriaRole.BUTTON, setName("Transfer")).click();

// VERIFY SUCCESS (Auto-retry assertions!)
assertThat(page.getByText("Transfer successful"))
    .isVisible();
assertThat(page.getByTestId("txn-id"))
    .hasAttribute("data-status", "success");

// VERIFY TRANSACTION IN LIST (Auto-retry!)
assertThat(page.locator(".transaction-item")
    .filter(hasText("Rent payment")))
    .isVisible();

// VERIFY UPDATED BALANCE (Auto-retry!)
assertThat(page.getByTestId("account-balance"))
    .containsText("95,000");  // 1,00,000 - 5,000
```

**Total explicit waits needed: ZERO! 🎉**

**Total manual wait code in Selenium: 15+ lines**
**Total manual wait code in Playwright: 0 lines**

---

## 💡 Key Concepts

### **1. Auto-Waiting Applies To:**
```
✅ click(), dblclick(), hover()
✅ fill(), type(), press()
✅ check(), uncheck(), setChecked()
✅ selectOption()
✅ dragTo()
✅ setInputFiles()
✅ focus(), blur()
✅ textContent(), innerText()
✅ getAttribute(), getAttribute()
✅ isVisible(), isEnabled(), isChecked()
```

### **2. Auto-Waiting Does NOT Apply To:**
```
⚠️ page.navigate() - Returns immediately
⚠️ locator.count() - Returns current count
⚠️ page.evaluate() - Runs immediately
⚠️ Custom JavaScript
```

### **3. Assertion Auto-Retry:**
```
assertThat(locator).isVisible()

Retry mechanism:
- Check every 50ms
- Up to 5 seconds (default)
- Stops immediately when true
- Clear error if timeout
```

---

## 📊 Performance Comparison

| Metric | Selenium | Playwright | Improvement |
|--------|----------|------------|-------------|
| **Wait Code Lines** | ~15 per test | ~0 per test | 100% less |
| **Test Execution** | 45 seconds | 15 seconds | 3x faster |
| **Flaky Tests** | Common | Rare | 90% reduction |
| **Code Readability** | Low | High | Much better |
| **Maintenance** | Hard | Easy | Much easier |

---

## ✅ Best Practices

### **DO:**
1. ✅ Use assertThat() for web elements
2. ✅ Use containsText() for partial matches
3. ✅ Use not() for negative assertions
4. ✅ Let Playwright auto-wait (don't add explicit waits)
5. ✅ Use JUnit assertions for business logic only

### **DON'T:**
1. ❌ Use JUnit assertions for web elements
2. ❌ Add sleep() or manual waits
3. ❌ Check condition then wait (use assertThat)
4. ❌ Use assertEquals() for element text
5. ❌ Override timeout everywhere (use defaults)

---

## 🔥 Top 10 Playwright Features (Level 3)

1. **Auto-waiting** - No explicit waits (90% less code)
2. **Auto-retry assertions** - No flaky tests
3. **5 Actionability checks** - Smart waiting
4. **assertThat()** - Better than JUnit for web
5. **containsText()** - Flexible text matching
6. **not()** - Easy negation
7. **hasURL()** - Page assertions
8. **Default 30s timeout** - Generous, configurable
9. **50ms retry interval** - Fast feedback
10. **Better error messages** - Clear failures

---

## 🎓 What You'll Master

**After Level 3:**

**Auto-Waiting Skills:**
- [x] Understand 5 actionability checks
- [x] Know when auto-wait applies
- [x] Know when it doesn't
- [x] Appreciate performance benefits
- [x] Never add sleep() again!

**Assertion Skills:**
- [x] Use assertThat() API
- [x] Understand auto-retry
- [x] All visibility assertions
- [x] All text assertions
- [x] All state assertions
- [x] Page assertions
- [x] Negation with not()

**Best Practices:**
- [x] assertThat() for web, JUnit for logic
- [x] Let Playwright handle waits
- [x] Write non-flaky tests
- [x] Use defaults (30s timeout)

---

## 🚀 Quick Start

```bash
# 1. Extract
unzip playwright-level3.zip
cd playwright-level3

# 2. Run all tests
mvn test

# 3. Run specific class
mvn test -Dtest=Test01_AutoWaiting
mvn test -Dtest=Test02_PlaywrightAssertions
```

---

## 💯 Self-Assessment

**After Level 3, can you:**
- [ ] Explain Playwright's 5 actionability checks?
- [ ] Know when auto-wait applies?
- [ ] Use assertThat() correctly?
- [ ] Choose assertThat() vs JUnit?
- [ ] Write non-flaky assertions?
- [ ] Use not() for negation?
- [ ] Never add sleep() or explicit waits?

**All checked? → Ready for Level 4!** 🎉

---

## 🚀 What's Next?

### **Level 4: Page Object Model**
**Coming Next!**

**Topics:**
- Page Object Model pattern
- Component objects
- Fixtures and setup
- Base page pattern
- Test organization

**Duration:** 4-5 hours  
**Tests:** 20+  
**Difficulty:** Intermediate

---

## 🎉 Congratulations!

You've mastered **Auto-Waiting & Assertions!** 🎭

**You now know:**
- ✅ Playwright auto-waiting (no manual waits!)
- ✅ assertThat() API (auto-retry!)
- ✅ All web-first assertions
- ✅ Why Playwright is 3x faster
- ✅ How to write non-flaky tests

**Next:** Level 4 for Page Object Model!

---

**Happy Testing with Playwright!** 🚀
