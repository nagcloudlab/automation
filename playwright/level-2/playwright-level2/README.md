# 🎭 Playwright Level 2 - Locators & Actions

## Master Element Selection and User Interactions

**Level:** Intermediate  
**Duration:** 3-4 hours  
**Prerequisites:** Playwright Level 1 completed  

---

## 📚 What You'll Learn

### **Core Topics:**
1. ✅ CSS Selectors (ID, class, attribute)
2. ✅ Role-based locators (ARIA) - **RECOMMENDED**
3. ✅ Advanced locator methods (getByText, getByLabel, etc.)
4. ✅ Locator filtering and chaining
5. ✅ Click variations (click, double-click, right-click)
6. ✅ Hover and drag-and-drop
7. ✅ Form interactions (checkboxes, dropdowns, file uploads)
8. ✅ Keyboard actions and shortcuts

### **Skill Level:** Beginner → Intermediate Playwright Developer

---

## 📦 Package Contents

```
playwright-level2/
├── pom.xml                                  # Maven configuration
├── README.md                                # This file
└── src/
    └── test/java/com/npci/training/
        ├── tests/
        │   └── BaseTest.java                # Base class
        └── level2/
            ├── Test01_BasicLocators.java           # 8 tests
            ├── Test02_RoleBasedLocators.java       # 8 tests
            ├── Test03_AdvancedLocatorMethods.java  # 9 tests
            ├── Test04_UserActions.java             # 10 tests
            └── Test05_FormInteractionsAndKeyboard.java  # 10 tests
```

**Total:** 45 executable tests covering locators and actions

---

## 🚀 Quick Start

### **Step 1: Extract and Setup**
```bash
unzip playwright-level2.zip
cd playwright-level2

# Install browsers (if not done in Level 1)
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

### **Step 2: Run Tests**
```bash
# Run all Level 2 tests
mvn test

# Run specific test class
mvn test -Dtest=Test01_BasicLocators
```

---

## 📖 Test Classes Detailed Overview

### **Test01: Basic Locators (30 mins)**
**File:** `Test01_BasicLocators.java`

**Topics:**
- CSS selectors (ID, class, attribute)
- Text selectors
- Descendant and child selectors
- nth-child and sibling selectors
- Locator chaining

**Tests:**
1. `test01_CssSelectorById` - #id selector
2. `test02_CssSelectorByClass` - .class selector
3. `test03_CssSelectorByAttribute` - [attribute] selectors
4. `test04_TextBasedLocators` - text='...' selectors
5. `test05_CombiningSelectors` - button.class[type='submit']
6. `test06_DescendantAndChildSelectors` - form input, form > input
7. `test07_NthChildAndSiblings` - :nth-child()
8. `test08_LocatorChaining` - Narrow down search

**Key Learning:**
```java
// CSS Selectors
page.locator("#username");              // ID
page.locator(".submit-btn");            // Class
page.locator("[name='email']");         // Attribute
page.locator("button.primary");         // Tag + class
page.locator("form input");             // Descendant
page.locator("form > input");           // Direct child

// Text selectors
page.locator("text='Exact Text'");
page.locator("text=Partial");

// Chaining
page.locator("form").locator("input");
```

**Run:**
```bash
mvn test -Dtest=Test01_BasicLocators
```

---

### **Test02: Role-based Locators (30 mins)** ⭐ **Most Important!**
**File:** `Test02_RoleBasedLocators.java`

**Topics:**
- getByRole() - Playwright's RECOMMENDED method
- ARIA roles
- Accessible locators
- Role options (name, checked, level)
- Why roles are better than CSS

**Tests:**
1. `test01_GetByRoleButton` - Locate buttons
2. `test02_GetByRoleLink` - Locate links
3. `test03_GetByRoleTextbox` - Locate inputs
4. `test04_GetByRoleCheckbox` - Locate checkboxes
5. `test05_GetByRoleWithOptions` - Role options
6. `test06_GetByRoleHeading` - Locate headings
7. `test07_CommonAriaRoles` - All ARIA roles overview
8. `test08_BankingPortalExample` - Real-world usage

**Key Learning:**
```java
// ✅ RECOMMENDED: Role-based locators
page.getByRole(AriaRole.BUTTON, 
    new Page.GetByRoleOptions().setName("Login"))
    .click();

page.getByRole(AriaRole.LINK, 
    new Page.GetByRoleOptions().setName("Transactions"))
    .click();

page.getByRole(AriaRole.TEXTBOX, 
    new Page.GetByRoleOptions().setName("Search"))
    .fill("UPI");

page.getByRole(AriaRole.CHECKBOX, 
    new Page.GetByRoleOptions().setChecked(true));

page.getByRole(AriaRole.HEADING, 
    new Page.GetByRoleOptions().setLevel(1));
```

**Why Role-based Locators?**
- ✅ Most resilient to UI changes
- ✅ Tests accessibility automatically
- ✅ Human-readable
- ✅ Playwright's RECOMMENDED approach
- ✅ Works across implementations

**Run:**
```bash
mvn test -Dtest=Test02_RoleBasedLocators
```

---

### **Test03: Advanced Locator Methods (30 mins)**
**File:** `Test03_AdvancedLocatorMethods.java`

**Topics:**
- getByText() - Text content
- getByLabel() - Form labels
- getByPlaceholder() - Input placeholders
- getByAltText() - Image alt text
- getByTitle() - Element titles
- getByTestId() - Test IDs
- Locator filtering

**Tests:**
1. `test01_GetByText` - Text-based selection
2. `test02_GetByLabel` - Label-based selection
3. `test03_GetByPlaceholder` - Placeholder selection
4. `test04_GetByAltText` - Alt text selection
5. `test05_GetByTitle` - Title selection
6. `test06_GetByTestId` - Test ID selection
7. `test07_FilteringLocators` - Filter results
8. `test08_LocatorMethods` - first(), last(), nth()
9. `test09_BankingExample` - Real-world combinations

**Key Learning:**
```java
// Text
page.getByText("Submit").click();
page.getByText("Login", exact: true);

// Label
page.getByLabel("Username").fill("admin");

// Placeholder
page.getByPlaceholder("Search...").fill("UPI");

// Alt text
page.getByAltText("Company Logo").isVisible();

// Title
page.getByTitle("Help").click();

// Test ID
page.getByTestId("submit-btn").click();

// Filtering
page.locator("a")
    .filter(new Locator.FilterOptions().setHasText("Test"))
    .first()
    .click();

// Collection methods
page.locator("li").count();
page.locator("li").first();
page.locator("li").last();
page.locator("li").nth(2);
```

**Locator Priority:**
1. getByRole() - **RECOMMENDED**
2. getByLabel() - For forms
3. getByPlaceholder() - For search
4. getByText() - For static content
5. getByTestId() - For stability
6. CSS/XPath - Fallback

**Run:**
```bash
mvn test -Dtest=Test03_AdvancedLocatorMethods
```

---

### **Test04: User Actions (30 mins)**
**File:** `Test04_UserActions.java`

**Topics:**
- Click variations
- Double-click and right-click
- Click with modifiers (Ctrl, Shift)
- Click at position
- Hover actions
- Drag and drop
- Force click

**Tests:**
1. `test01_BasicClick` - Standard click
2. `test02_DoubleClick` - Double-click
3. `test03_RightClick` - Right-click / context menu
4. `test04_ClickWithModifiers` - Ctrl+Click, Shift+Click
5. `test05_ClickAtPosition` - Click at coordinates
6. `test06_HoverAction` - Hover / mouse over
7. `test07_DragAndDrop` - Drag element to target
8. `test08_ForceClick` - Bypass actionability checks
9. `test09_BankingClickExamples` - Real scenarios
10. `test10_ClickOptionsSummary` - All options

**Key Learning:**
```java
// Basic click
page.locator("button").click();

// Double-click
page.locator("#element").dblclick();

// Right-click
page.locator("#item").click(
    new Locator.ClickOptions()
        .setButton(MouseButton.RIGHT)
);

// Ctrl+Click (new tab)
page.locator("a").click(
    new Locator.ClickOptions()
        .setModifiers(Arrays.asList(KeyboardModifier.CONTROL))
);

// Shift+Click (range select)
page.locator(".item").last().click(
    new Locator.ClickOptions()
        .setModifiers(Arrays.asList(KeyboardModifier.SHIFT))
);

// Click at position
page.locator("#canvas").click(
    new Locator.ClickOptions()
        .setPosition(150, 200)
);

// Hover
page.locator("#menu").hover();
page.locator("#submenu-item").click();

// Drag and drop
page.locator("#source").dragTo(page.locator("#target"));

// Force click (skip actionability)
page.locator("#element").click(
    new Locator.ClickOptions()
        .setForce(true)
);
```

**Run:**
```bash
mvn test -Dtest=Test04_UserActions
```

---

### **Test05: Form Interactions & Keyboard (35 mins)**
**File:** `Test05_FormInteractionsAndKeyboard.java`

**Topics:**
- fill() vs type() comparison
- Checkbox/radio buttons
- Dropdown selection
- File uploads
- Keyboard actions
- Keyboard shortcuts
- pressSequentially()

**Tests:**
1. `test01_FillVsType` - Performance comparison
2. `test02_CheckboxInteractions` - check(), uncheck()
3. `test03_RadioButtons` - Radio selection
4. `test04_DropdownSelection` - Select options
5. `test05_FileUpload` - Upload files
6. `test06_KeyboardPress` - press() method
7. `test07_KeyboardShortcuts` - Ctrl+A, Ctrl+C, etc.
8. `test08_PressSequentially` - Slow typing
9. `test09_CompleteFormExample` - Full form
10. `test10_FormInteractionsSummary` - Cheat sheet

**Key Learning:**
```java
// Text input - fill() is RECOMMENDED
page.locator("#username").fill("admin");         // ✅ Fast
page.locator("#username").type("admin");         // ⚠️ Slow (legacy)
page.locator("#username").pressSequentially(     // ⚠️ Character by character
    "admin", 
    new Locator.PressSequentiallyOptions().setDelay(100)
);

// Checkbox
page.locator("#agree").check();                  // Ensure checked
page.locator("#agree").uncheck();                // Ensure unchecked
page.locator("#agree").setChecked(true);         // Set state
page.locator("#agree").isChecked();              // Get state

// Dropdown
page.locator("select").selectOption("value");
page.locator("select").selectOption(
    new SelectOption().setLabel("Option 1")
);
page.locator("select").selectOption(
    new SelectOption().setIndex(0)
);

// File upload
page.locator("input[type='file']")
    .setInputFiles(Paths.get("document.pdf"));

// Multiple files
page.locator("input[type='file']")
    .setInputFiles(new Path[] {
        Paths.get("file1.pdf"),
        Paths.get("file2.pdf")
    });

// Keyboard
page.locator("#input").press("Enter");
page.locator("#input").press("Control+A");
page.locator("#input").press("Control+C");
page.locator("#input").press("Tab");
page.locator("#input").press("Escape");

// Arrow keys
page.locator("#input").press("ArrowDown");
page.locator("#input").press("ArrowUp");
```

**Run:**
```bash
mvn test -Dtest=Test05_FormInteractionsAndKeyboard
```

---

## 🎯 Learning Path

### **Session 1: CSS Selectors (45 mins)**
1. Run Test01_BasicLocators
2. Practice CSS selectors
3. Try chaining locators
4. Understand descendant vs child

### **Session 2: Role-based Locators (45 mins)**
1. Run Test02_RoleBasedLocators
2. Learn ARIA roles
3. Practice getByRole()
4. Understand why roles are best

### **Session 3: Advanced Locators (45 mins)**
1. Run Test03_AdvancedLocatorMethods
2. Practice getByText, getByLabel
3. Try filtering locators
4. Master locator priority

### **Session 4: User Actions (45 mins)**
1. Run Test04_UserActions
2. Practice click variations
3. Try hover and drag-drop
4. Learn click options

### **Session 5: Forms & Keyboard (45 mins)**
1. Run Test05_FormInteractionsAndKeyboard
2. Practice form filling
3. Try keyboard shortcuts
4. Complete full form example

---

## 💡 Playwright Locator Strategy

### **Priority Hierarchy:**
```
1️⃣ getByRole()       - MOST RECOMMENDED
2️⃣ getByLabel()      - For form inputs
3️⃣ getByPlaceholder() - For search fields
4️⃣ getByText()       - For static content
5️⃣ getByTestId()     - For critical elements
6️⃣ CSS Selectors    - When above don't work
7️⃣ XPath            - Last resort
```

### **Real-World Examples:**

**Login Button:**
```java
✅ page.getByRole(AriaRole.BUTTON, setName("Login"))
⚠️ page.getByText("Login")
❌ page.locator("#login-btn")
```

**Username Input:**
```java
✅ page.getByLabel("Username")
✅ page.getByPlaceholder("Enter username")
⚠️ page.locator("#username")
```

**Search Field:**
```java
✅ page.getByPlaceholder("Search transactions")
✅ page.getByRole(AriaRole.TEXTBOX, setName("Search"))
⚠️ page.locator("[type='search']")
```

---

## 🏦 Banking Portal Examples

### **Login Flow:**
```java
page.getByLabel("Username").fill("admin");
page.getByLabel("Password").fill("password123");
page.getByRole(AriaRole.BUTTON, 
    new Page.GetByRoleOptions().setName("Login"))
    .click();
```

### **Transaction Search:**
```java
page.getByPlaceholder("Search transactions").fill("UPI");
page.getByLabel("Show pending only").check();
page.getByRole(AriaRole.BUTTON, 
    new Page.GetByRoleOptions().setName("Search"))
    .click();
```

### **Transfer Money:**
```java
page.getByLabel("From Account").selectOption("savings");
page.getByLabel("To Account").fill("9876543210");
page.getByLabel("Amount").fill("5000");
page.getByLabel("Description").fill("Rent payment");
page.getByRole(AriaRole.BUTTON, 
    new Page.GetByRoleOptions().setName("Transfer"))
    .click();
```

---

## 🎓 Key Concepts Summary

### **1. Locator Methods:**
| Method | Use Case | Example |
|--------|----------|---------|
| getByRole() | Buttons, links, inputs | Most recommended |
| getByLabel() | Form inputs | Best for forms |
| getByPlaceholder() | Search fields | Natural selection |
| getByText() | Static text | Links, headings |
| getByTestId() | Critical elements | Most stable |
| CSS | Fallback | When others fail |

### **2. Click Variations:**
- click() - Standard
- dblclick() - Double-click
- click(setButton(RIGHT)) - Right-click
- click(setModifiers(CONTROL)) - Ctrl+Click
- hover() - Mouse over
- dragTo() - Drag and drop

### **3. Form Interactions:**
- fill() - Text input (recommended)
- check() / uncheck() - Checkboxes
- selectOption() - Dropdowns
- setInputFiles() - File uploads
- press() - Keyboard

---

## ✅ Best Practices

### **DO:**
1. ✅ Use getByRole() for buttons, links, inputs
2. ✅ Use getByLabel() for form fields
3. ✅ Use fill() instead of type()
4. ✅ Use check()/uncheck() for checkboxes
5. ✅ Chain locators for specificity
6. ✅ Let Playwright auto-wait

### **DON'T:**
1. ❌ Use complex XPath unnecessarily
2. ❌ Use nth-child() with hard-coded numbers
3. ❌ Use type() by default (use fill())
4. ❌ Force click without understanding why
5. ❌ Add explicit waits (Playwright auto-waits)

---

## 📊 Statistics

| Metric | Value |
|--------|-------|
| **Test Classes** | 5 |
| **Total Tests** | 45 |
| **Code Lines** | ~3,000+ |
| **Duration** | 3-4 hours |
| **Difficulty** | Intermediate |

---

## 💯 Self-Assessment

**After Level 2, can you:**
- [ ] Use CSS selectors effectively?
- [ ] Explain why role-based locators are best?
- [ ] Use getByRole() confidently?
- [ ] Filter and chain locators?
- [ ] Perform all click variations?
- [ ] Fill forms completely?
- [ ] Use keyboard shortcuts?
- [ ] Choose the right locator strategy?

**If yes to all → Ready for Level 3!** 🎉

---

## 🚀 What's Next?

### **Level 3: Auto-Waiting & Assertions**
**Coming Next!**

**Topics:**
- Playwright auto-waiting mechanisms
- Timeout configuration
- Web-first assertions
- assertThat() API
- Soft assertions
- Network waiting
- Custom waits

**Duration:** 3-4 hours  
**Tests:** 30+  
**Difficulty:** Intermediate

---

## 🎉 Congratulations!

You've mastered **Playwright Locators & Actions!** 🎭

You now know:
- ✅ All locator strategies
- ✅ Role-based locators (best practice)
- ✅ Advanced locator methods
- ✅ All user actions
- ✅ Form interactions
- ✅ Keyboard actions

**Next:** Move to Level 3 for assertions and auto-waiting!

---

**Happy Testing with Playwright!** 🚀
