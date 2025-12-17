# 🎭 Playwright Level 2 - Complete Package Summary

## Locators & Actions Mastery

**Package:** `playwright-level2.zip` (29 KB)

---

## ✨ What's Included

### **📦 Complete Package:**
```
✅ 5 Test Classes
✅ 45 Executable Tests
✅ BaseTest Framework  
✅ Comprehensive README (800+ lines)
✅ All Locator Strategies
✅ All User Actions
✅ Form Interactions
✅ Keyboard Actions
```

---

## 📚 Test Classes Breakdown

### **Test01: Basic Locators (30 mins)** - 8 Tests
```
Topics:
✅ CSS Selectors (ID, class, attribute)
✅ Text selectors
✅ Descendant and child selectors
✅ Locator chaining

Key Learning:
page.locator("#id")              // ID
page.locator(".class")           // Class
page.locator("[name='value']")   // Attribute
page.locator("form input")       // Descendant
page.locator("text='Login'")     // Text
```

---

### **Test02: Role-based Locators (30 mins)** - 8 Tests ⭐ **Most Important!**
```
Topics:
✅ getByRole() - Playwright's RECOMMENDED method
✅ ARIA roles
✅ Accessibility testing
✅ Role options

Key Learning:
// ✅ RECOMMENDED: Role-based locators
page.getByRole(AriaRole.BUTTON, 
    new Page.GetByRoleOptions().setName("Login"))
    .click();

page.getByRole(AriaRole.TEXTBOX, 
    new Page.GetByRoleOptions().setName("Search"))
    .fill("UPI");

Why Role-based?
✅ Most resilient to UI changes
✅ Tests accessibility automatically
✅ Human-readable
✅ Playwright's BEST PRACTICE
```

---

### **Test03: Advanced Locator Methods (30 mins)** - 9 Tests
```
Topics:
✅ getByText() - Text content
✅ getByLabel() - Form labels
✅ getByPlaceholder() - Input placeholders
✅ getByAltText() - Image alt text
✅ getByTestId() - Test IDs
✅ Locator filtering

Key Learning:
page.getByText("Submit").click();
page.getByLabel("Username").fill("admin");
page.getByPlaceholder("Search...").fill("query");
page.getByAltText("Logo").isVisible();
page.getByTestId("submit-btn").click();

// Filtering
page.locator("a")
    .filter(new Locator.FilterOptions()
        .setHasText("Test"))
    .first()
    .click();
```

---

### **Test04: User Actions (30 mins)** - 10 Tests
```
Topics:
✅ Click variations
✅ Double-click, right-click
✅ Click with modifiers (Ctrl, Shift)
✅ Hover actions
✅ Drag and drop

Key Learning:
// Basic click
page.locator("button").click();

// Right-click
page.locator("#item").click(
    new Locator.ClickOptions()
        .setButton(MouseButton.RIGHT));

// Ctrl+Click (new tab)
page.locator("a").click(
    new Locator.ClickOptions()
        .setModifiers(Arrays.asList(
            KeyboardModifier.CONTROL)));

// Hover
page.locator("#menu").hover();

// Drag and drop
page.locator("#source")
    .dragTo(page.locator("#target"));
```

---

### **Test05: Form Interactions & Keyboard (35 mins)** - 10 Tests
```
Topics:
✅ fill() vs type() comparison
✅ Checkboxes and radio buttons
✅ Dropdown selection
✅ File uploads
✅ Keyboard actions

Key Learning:
// Text input - fill() is RECOMMENDED
page.locator("#input").fill("text");  // ✅ Fast

// Checkbox
page.locator("#agree").check();       // Ensure checked
page.locator("#agree").uncheck();     // Ensure unchecked

// Dropdown
page.locator("select").selectOption("value");

// File upload
page.locator("input[type='file']")
    .setInputFiles(Paths.get("file.pdf"));

// Keyboard
page.locator("#input").press("Enter");
page.locator("#input").press("Control+A");
```

---

## 🎯 Playwright Locator Strategy

### **Priority Hierarchy:**
```
1️⃣ getByRole()       ← MOST RECOMMENDED
2️⃣ getByLabel()      ← For form inputs
3️⃣ getByPlaceholder() ← For search fields
4️⃣ getByText()       ← For static content
5️⃣ getByTestId()     ← For critical elements
6️⃣ CSS Selectors     ← When above don't work
7️⃣ XPath             ← Last resort
```

### **Real-World Example:**

**Login Button - Which is best?**
```java
✅ page.getByRole(AriaRole.BUTTON, setName("Login"))
   // Resilient, accessible, clear

⚠️ page.getByText("Login")
   // Works, but less specific

❌ page.locator("#login-btn")
   // Fragile, breaks on ID change
```

---

## 🏦 Complete Banking Portal Example

```java
// Navigate to login
page.navigate("https://banking-portal.com");

// Login form - Using role-based locators
page.getByLabel("Username").fill("rajesh.kumar");
page.getByLabel("Password").fill("SecurePass123!");
page.getByRole(AriaRole.BUTTON, 
    new Page.GetByRoleOptions().setName("Login"))
    .click();

// Navigate to transactions
page.getByRole(AriaRole.LINK, 
    new Page.GetByRoleOptions().setName("Transactions"))
    .click();

// Search transactions
page.getByPlaceholder("Search transactions").fill("UPI");

// Filter options
page.getByLabel("Show pending only").check();
page.getByLabel("Date range").selectOption("last-30-days");

// Click search
page.getByRole(AriaRole.BUTTON, 
    new Page.GetByRoleOptions().setName("Search"))
    .click();

// Click first transaction (Ctrl+Click for new tab)
page.locator(".transaction-row").first().click(
    new Locator.ClickOptions()
        .setModifiers(Arrays.asList(KeyboardModifier.CONTROL))
);

// Hover over amount to see details
page.locator(".transaction-amount").first().hover();

// Right-click for options
page.locator(".transaction-row").first().click(
    new Locator.ClickOptions()
        .setButton(MouseButton.RIGHT)
);

// Export statement
page.getByRole(AriaRole.BUTTON, 
    new Page.GetByRoleOptions().setName("Export PDF"))
    .click();

// Upload receipt
page.getByLabel("Upload receipt")
    .setInputFiles(Paths.get("receipt.pdf"));
```

---

## 💡 Key Differences: Selenium vs Playwright

| Feature | Selenium | Playwright |
|---------|----------|------------|
| **Locator Strategy** | By.id, By.class | getByRole() ✅ |
| **Text Locators** | Complex XPath | getByText() ✅ |
| **Accessibility** | Not built-in | Built-in with roles ✅ |
| **Auto-waiting** | ❌ Manual | ✅ Automatic |
| **Hover** | Actions class | .hover() ✅ |
| **Drag & Drop** | Complex Actions | .dragTo() ✅ |
| **File Upload** | sendKeys() | setInputFiles() ✅ |
| **Keyboard** | sendKeys() | .press() ✅ |

**Winner:** Playwright - Simpler, more powerful, more resilient! 🎭

---

## 🎓 What You'll Master

### **After Level 2:**

**Locator Skills:**
- [x] CSS selectors (ID, class, attribute)
- [x] Role-based locators (RECOMMENDED)
- [x] Text-based locators
- [x] Label and placeholder locators
- [x] Test ID locators
- [x] Locator chaining and filtering
- [x] Locator priority strategy

**Action Skills:**
- [x] All click variations
- [x] Hover actions
- [x] Drag and drop
- [x] Form filling
- [x] Checkbox/radio interactions
- [x] Dropdown selection
- [x] File uploads
- [x] Keyboard actions and shortcuts

**Best Practices:**
- [x] When to use which locator
- [x] Role-based locators first
- [x] fill() over type()
- [x] Let Playwright auto-wait
- [x] Accessible testing patterns

---

## 📊 Package Statistics

| Metric | Value |
|--------|-------|
| **Test Classes** | 5 |
| **Total Tests** | 45 |
| **Code Lines** | 3,000+ |
| **Documentation** | 1,200+ lines |
| **Package Size** | 29 KB |
| **Duration** | 3-4 hours |
| **Skill Level** | Beginner → Intermediate |

---

## 🚀 Quick Start

```bash
# 1. Extract package
unzip playwright-level2.zip
cd playwright-level2

# 2. Run all tests
mvn test

# 3. Run specific class
mvn test -Dtest=Test02_RoleBasedLocators

# 4. Run specific test
mvn test -Dtest=Test02_RoleBasedLocators#test01_GetByRoleButton
```

---

## 🔥 Top 10 Playwright Features in Level 2

1. **getByRole()** - Best locator strategy
2. **Auto-waiting** - No explicit waits needed
3. **fill()** - Fast text input
4. **Hover** - One-line hover actions
5. **dragTo()** - Simple drag and drop
6. **press()** - Easy keyboard actions
7. **Locator chaining** - Narrow down search
8. **Filter()** - Refine locator results
9. **selectOption()** - Easy dropdown selection
10. **setInputFiles()** - Simple file uploads

---

## ✅ Best Practices Summary

### **DO:**
1. ✅ Use getByRole() for buttons, links, inputs
2. ✅ Use getByLabel() for form fields
3. ✅ Use fill() instead of type()
4. ✅ Use check()/uncheck() for checkboxes
5. ✅ Let Playwright auto-wait
6. ✅ Chain locators for specificity

### **DON'T:**
1. ❌ Use complex XPath unnecessarily
2. ❌ Use brittle selectors (nth-child(7))
3. ❌ Use type() by default
4. ❌ Force click without understanding why
5. ❌ Add explicit waits (Playwright auto-waits!)

---

## 🎯 Real-World Use Cases

### **1. E-commerce Checkout:**
```java
page.getByLabel("Email").fill("user@example.com");
page.getByLabel("Card Number").fill("4111111111111111");
page.getByLabel("Expiry").fill("12/25");
page.getByLabel("CVV").fill("123");
page.getByLabel("I agree to terms").check();
page.getByRole(AriaRole.BUTTON, setName("Place Order")).click();
```

### **2. Banking Transfer:**
```java
page.getByLabel("From Account").selectOption("savings");
page.getByPlaceholder("Enter account or UPI").fill("user@upi");
page.getByLabel("Amount").fill("5000");
page.getByLabel("Purpose").selectOption("rent");
page.getByRole(AriaRole.BUTTON, setName("Transfer")).click();
```

### **3. Document Upload:**
```java
page.getByLabel("Upload Documents")
    .setInputFiles(new Path[] {
        Paths.get("aadhar.pdf"),
        Paths.get("pan.pdf")
    });
page.getByRole(AriaRole.BUTTON, setName("Submit")).click();
```

---

## 💯 Self-Check

**Can you confidently:**
- [ ] Use CSS selectors?
- [ ] Explain why getByRole() is best?
- [ ] Use all advanced locator methods?
- [ ] Perform all click variations?
- [ ] Fill complete forms?
- [ ] Use keyboard shortcuts?
- [ ] Choose right locator for each element?
- [ ] Write resilient, maintainable tests?

**All checked? → You've mastered Level 2!** 🎉

---

## 🚀 What's Next?

### **Level 3: Auto-Waiting & Assertions**
**Ready for you!**

**Topics:**
- Playwright auto-waiting mechanisms
- Timeout configuration
- Web-first assertions (assertThat)
- Soft assertions
- Network waiting
- Custom wait strategies

**Duration:** 3-4 hours  
**Tests:** 30+  
**Difficulty:** Intermediate

---

## 🎉 Congratulations!

You've completed **Playwright Level 2!** 🎭

### **You Now Know:**
```
✅ All Locator Strategies
✅ Role-based Locators (Best Practice)
✅ Advanced Locator Methods
✅ All Click Variations
✅ Hover & Drag-Drop
✅ Complete Form Interactions
✅ Keyboard Actions
✅ Production-Ready Patterns
```

### **You Can:**
- ✅ Select elements using best practices
- ✅ Interact with any web element
- ✅ Fill forms efficiently
- ✅ Use keyboard shortcuts
- ✅ Write resilient, maintainable tests
- ✅ Build production-grade automation

**Next:** Level 3 for assertions and advanced waiting!

---

**Package ready for download!** 📦👆

**Happy Testing with Playwright!** 🚀
