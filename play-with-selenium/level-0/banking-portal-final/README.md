# 🏦 NPCI Banking Portal v2.0 - Final Edition

## Complete Selenium WebDriver Training Application

---

## 🎯 Purpose

A **professional, lightweight** web application specifically designed for comprehensive Selenium WebDriver training. Covers **ALL** Selenium concepts with **100+ test scenarios** across **8 pages**.

### ✨ What Makes This Special

- ✅ **Zero Dependencies** - Pure HTML/CSS/JS, no frameworks
- ✅ **Instant Setup** - Open and start testing in 30 seconds
- ✅ **Complete Coverage** - All Selenium concepts in one app
- ✅ **Training Optimized** - Clear IDs, predictable behavior
- ✅ **Well Documented** - Every element, every scenario explained

---

## 📦 Package Contents

```
banking-portal-final/
├── index.html              # Welcome/Home page
├── login.html              # Login page (Entry point for testing)
├── register.html           # Registration with all input types
├── dashboard.html          # Dashboard with tables and navigation
├── transactions.html       # Transaction forms with tabs
├── accounts.html           # Table with search, filters, modal
├── reports.html            # Alerts, file upload, iframe
├── dynamic-data.html       # 8 async loading scenarios
├── test-scenarios.html     # Complete test reference guide
├── README.md              # This file
├── QUICK_START.md         # 5-minute setup guide
└── SELENIUM_EXAMPLES.md   # Complete code examples
```

**Total: 12 files** (9 HTML + 3 Documentation)

---

## 🚀 Quick Start (30 Seconds)

### Method 1: Direct Open (For Quick Demo)
```bash
1. Extract the ZIP file
2. Double-click index.html
3. Click "Get Started"
4. Start exploring!
```

### Method 2: Local Server (Recommended for Selenium)
```bash
# Using Python
python -m http.server 8000

# Using Node.js  
npx http-server -p 8000

# Then open
http://localhost:8000
```

### Method 3: VS Code Live Server
```
1. Open folder in VS Code
2. Right-click index.html
3. Select "Open with Live Server"
```

---

## 🔐 Test Credentials

**Universal Login:**
- **Username:** `admin`
- **Password:** `admin123`
- **User Type:** `Customer`

---

## 📚 Page Overview

### 1️⃣ **index.html** - Welcome Page
**Purpose:** Starting point with overview and navigation

**Key Features:**
- Stats display (8 pages, 30+ concepts, 100+ scenarios)
- Quick navigation to all pages
- Test credentials
- Feature overview

**Test IDs:**
- `getStartedBtn` - Main CTA button
- `viewScenariosBtn` - Scenarios page link
- `linkLogin`, `linkRegister`, etc. - Page links

---

### 2️⃣ **login.html** - Login Page ⭐ START HERE
**Purpose:** Forms, validation, basic locators

**Elements (10):**
| Element | ID | Locator Types |
|---------|----|--------------| 
| Username Field | `username` | ID, Name, CSS, XPath |
| Password Field | `password` | ID, Name |
| User Type | `userType` | ID, Select |
| Remember Me | `rememberMe` | ID, Name |
| Terms Checkbox | `terms` | ID |
| Login Button | `loginBtn` | ID, Class |
| Clear Button | - | By.xpath |
| Error Messages | `usernameError` | ID, Class |
| Forgot Password | - | LinkText |
| Register Link | - | PartialLinkText |

**Test Scenarios (8):**
1. ✓ Valid login
2. ✓ Empty username
3. ✓ Empty password
4. ✓ Invalid email format
5. ✓ Missing user type
6. ✓ Terms not accepted
7. ✓ Clear form
8. ✓ Navigation links

---

### 3️⃣ **register.html** - Registration
**Purpose:** All input types, validation

**Input Types Covered (9):**
- Text (`fullName`)
- Email (`email`)
- Tel (`mobile`)
- Date (`dob`)
- Radio (`gender`)
- Select (`accountType`)
- TextArea (`address`)
- Password (`regPassword`, `confirmPassword`)
- Checkbox (`newsletter`, `regTerms`)

**Validations:**
- Name: Min 3 chars
- Email: Format validation
- Mobile: 10 digits
- Password: Min 6 chars, matching
- All required fields

**Test Scenarios (10+):**
- Complete registration flow
- Individual field validations
- Password matching
- Radio button selection
- Date picker interaction
- Checkbox states
- Form submission

---

### 4️⃣ **dashboard.html** - Main Dashboard
**Purpose:** Navigation, tables, dynamic content

**Key Elements:**
- Welcome message (`welcomeUser`)
- Logout button with confirm
- Navigation menu (4 items)
- Account cards (4 cards with IDs)
- Transaction table (5 rows)
- View buttons in table

**Table Operations:**
- Read row count
- Extract cell values
- Click row buttons
- Verify headers
- Dynamic data reading

---

### 5️⃣ **transactions.html** - Payment Forms
**Purpose:** Tabs, dynamic forms, radio buttons

**3 Tabs:**
1. **UPI Tab** (`tabUPI`)
   - UPI ID/Phone (`upiId`)
   - Amount (`upiAmount`)
   - Remarks (`upiRemarks`)

2. **NEFT Tab** (`tabNEFT`)
   - Account (`neftAccount`)
   - IFSC (`neftIfsc`)
   - Name (`neftName`)
   - Amount (`neftAmount`)

3. **Withdrawal Tab** (`tabWithdraw`)
   - Amount (`withdrawAmount`)
   - Type - Radio (`withdrawType`)
   - Purpose - Select (`withdrawReason`)

**Features:**
- Tab switching
- Form show/hide
- Validation
- Reset buttons

---

### 6️⃣ **accounts.html** - Data Table
**Purpose:** Search, filters, pagination, modal

**Advanced Features:**
- **Search** (`searchInput`) - Real-time filter
- **Status Filter** (`statusFilter`) - Dropdown
- **Type Filter** (`typeFilter`) - Dropdown
- **Select All** (`selectAll`) - Master checkbox
- **Row Checkboxes** (`.row-checkbox`) - Individual
- **Pagination** - Page buttons
- **Modal Dialog** (`addModal`) - Add account
- **CRUD Buttons** - View/Edit actions

**Table:**
- 5 sample accounts
- Data attributes for filtering
- Dynamic row visibility

**Test Scenarios:**
- Search functionality
- Combined filters
- Checkbox selection
- Pagination
- Modal open/close
- Dynamic row count

---

### 7️⃣ **reports.html** - Advanced Features
**Purpose:** Alerts, file operations, iframes

**JavaScript Alerts (3):**
```java
// Alert
driver.switchTo().alert().accept();

// Confirm
driver.switchTo().alert().dismiss();

// Prompt
Alert alert = driver.switchTo().alert();
alert.sendKeys("text");
alert.accept();
```

**File Upload:**
```java
WebElement fileInput = driver.findElement(By.id("fileUpload"));
fileInput.sendKeys("/path/to/file.pdf");
```

**iFrame:**
```java
driver.switchTo().frame("helpFrame");
// interact with iframe content
driver.switchTo().defaultContent();
```

**Other Features:**
- Range slider (`transactionLimit`)
- Date pickers (`dateFrom`, `dateTo`)
- Report generation
- Window management

---

### 8️⃣ **dynamic-data.html** ⭐ ASYNC/WAITS
**Purpose:** All wait strategies, AJAX, dynamic content

**8 Scenarios:**

| # | Scenario | Delay | Wait Type | Button ID |
|---|----------|-------|-----------|-----------|
| 1 | Simple Delayed | 2s | invisibility/visibility | `loadBtn1` |
| 2 | Progressive | 800ms each | numberOfElementsToBe | `loadBtn2` |
| 3 | AJAX Table | 3s | stalenessOf | `loadBtn3` |
| 4 | Skeleton | 2.5s | Element replacement | `loadBtn4` |
| 5 | Auto-Refresh | 2s interval | textToBePresentIn | `startRefresh` |
| 6 | Lazy Loading | 1s batches | Progressive count | `loadBtn5` |
| 7 | Conditional | 2s | OR conditions | `checkBtn` |
| 8 | Counter | 1s updates | textToBe | `startCounter` |

**Wait Strategies Practiced:**
- Explicit Wait with ExpectedConditions
- Fluent Wait with custom conditions
- Staleness detection
- Element visibility/invisibility
- Text presence
- Element count
- Custom lambda conditions

---

### 9️⃣ **test-scenarios.html** - Reference Guide
**Purpose:** Quick reference for all test scenarios

**Contents:**
- Complete element list for each page
- Locator examples
- Test scenarios
- Code samples
- Coverage matrix

---

## 📊 Complete Coverage Matrix

### Locator Strategies (8)
- ✅ By.id() - Primary locator
- ✅ By.name() - Form elements
- ✅ By.className() - Multiple elements
- ✅ By.tagName() - Generic elements
- ✅ By.linkText() - Links
- ✅ By.partialLinkText() - Partial links
- ✅ By.cssSelector() - CSS patterns
- ✅ By.xpath() - Complex paths

### WebElement Methods
- ✅ sendKeys() - Input text
- ✅ click() - Click elements
- ✅ clear() - Clear fields
- ✅ getText() - Extract text
- ✅ getAttribute() - Get attributes
- ✅ isDisplayed() - Visibility
- ✅ isEnabled() - Enabled state
- ✅ isSelected() - Selection state
- ✅ submit() - Form submission

### Form Elements (12)
- ✅ Text Input
- ✅ Email Input
- ✅ Password Input
- ✅ Tel Input
- ✅ Date Picker
- ✅ Number Input
- ✅ TextArea
- ✅ Select Dropdown
- ✅ Checkbox
- ✅ Radio Button
- ✅ Range Slider
- ✅ File Upload

### Advanced Concepts
- ✅ Tables - Reading, iteration
- ✅ JavaScript Alerts - Alert, Confirm, Prompt
- ✅ iFrames - Switching contexts
- ✅ Windows - Multiple windows/tabs
- ✅ Navigation - Multi-page flows
- ✅ Waits - Implicit, Explicit, Fluent
- ✅ Dynamic Content - AJAX, progressive loading
- ✅ Stale Elements - Detection and handling
- ✅ Modal Dialogs - Show/hide
- ✅ File Operations - Upload/Download

---

## 🎓 Selenium Training Levels Supported

### **Level 1-2: Basics** (login.html, register.html)
- Environment setup
- Basic locators
- Form interactions
- Simple assertions

### **Level 3-4: Intermediate** (dashboard.html, transactions.html)
- Navigation
- Tables
- Select class
- Tabs
- Multiple forms

### **Level 5-6: Advanced** (accounts.html, reports.html)
- Search functionality
- Filters
- Pagination
- Alerts
- File upload
- iFrames

### **Level 7-8: Expert** (dynamic-data.html)
- Explicit waits
- Fluent waits
- AJAX handling
- Stale elements
- Dynamic content
- Custom wait conditions

### **Level 9-10: Framework**
- Page Object Model
- Data-driven testing
- TestNG/JUnit integration
- Reporting

---

## 💻 Sample Selenium Code

### Basic Test
```java
@Test
public void testLogin() {
    driver.get("http://localhost:8000/login.html");
    
    driver.findElement(By.id("username")).sendKeys("admin");
    driver.findElement(By.id("password")).sendKeys("admin123");
    
    Select userType = new Select(driver.findElement(By.id("userType")));
    userType.selectByVisibleText("Customer");
    
    driver.findElement(By.id("terms")).click();
    driver.findElement(By.id("loginBtn")).click();
    
    assertTrue(driver.getCurrentUrl().contains("dashboard.html"));
}
```

### Async/Wait Test
```java
@Test
public void testAsyncLoading() {
    driver.get("http://localhost:8000/dynamic-data.html");
    
    driver.findElement(By.id("loadBtn1")).click();
    
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.invisibilityOfElementLocated(
        By.id("loadingSpinner1")
    ));
    
    assertTrue(driver.findElement(By.id("content1")).isDisplayed());
}
```

### Table Test
```java
@Test
public void testTableOperations() {
    driver.get("http://localhost:8000/dashboard.html");
    
    List<WebElement> rows = driver.findElements(
        By.cssSelector("#transactionTable tbody tr")
    );
    
    assertEquals(5, rows.size());
    
    // Click first view button
    rows.get(0).findElement(By.tagName("button")).click();
    
    // Handle alert
    driver.switchTo().alert().accept();
}
```

---

## 🎯 Test Scenario Examples

### Complete User Journey
```
1. Open index.html
2. Navigate to login.html
3. Login with valid credentials
4. Verify dashboard loads
5. Navigate to transactions
6. Fill UPI form
7. Submit transaction
8. Verify success alert
9. Navigate to accounts
10. Search for account
11. Verify filtered results
12. Logout
```

### Data-Driven Test Scenarios
```
Valid Logins:
- admin / admin123 / Customer ✓
- testuser / test123 / Admin ✓

Invalid Logins:
- empty / empty - Validation error
- admin / wrong - Alert error
- invalid-email / admin123 - Format error
```

---

## 📖 Documentation Files

1. **README.md** (This file)
   - Complete overview
   - Feature documentation
   - Code examples

2. **QUICK_START.md**
   - 5-minute setup
   - First test guide
   - Troubleshooting

3. **SELENIUM_EXAMPLES.md**
   - Complete code examples
   - All scenarios
   - Best practices

---

## ✅ Quality Checklist

- [x] All pages load correctly
- [x] No external dependencies
- [x] All IDs are unique and meaningful
- [x] All forms have validation
- [x] All buttons have clear IDs
- [x] Navigation works across all pages
- [x] Test credentials work
- [x] All async scenarios function
- [x] All alerts/modals work
- [x] File upload configured
- [x] iFrame loads correctly
- [x] Mobile responsive (bonus)

---

## 🚀 Next Steps After Setup

1. **Explore the Application**
   - Open index.html
   - Click through all pages
   - Try all features manually

2. **Start with Level 1**
   - Setup Selenium project
   - Write first test (login)
   - Run and verify

3. **Progress Through Levels**
   - Follow training curriculum
   - Complete practice exercises
   - Build test framework

4. **Master Advanced Concepts**
   - Work with async scenarios
   - Handle alerts and frames
   - Implement Page Object Model

---

## 💡 Tips for Trainers

### Teaching Approach
1. Start with login.html - simplest page
2. Progress to register.html - more inputs
3. Show table operations in dashboard
4. Demonstrate tabs in transactions
5. Advanced features in accounts/reports
6. Master waits with dynamic-data

### Practice Sessions
- Day 1-2: login + register
- Day 3-4: dashboard + transactions
- Day 5-6: accounts + reports
- Day 7-8: dynamic-data + waits
- Day 9-10: Complete framework

### Common Issues
- **Browser compatibility**: Tested on Chrome, Firefox, Edge
- **Server port conflicts**: Try 8000, 8080, 3000
- **Path issues**: Use absolute URLs in tests
- **Wait times**: Adjust based on system speed

---

## 📈 Stats Summary

| Category | Count |
|----------|-------|
| **HTML Pages** | 9 |
| **Test Elements** | 100+ |
| **Locator Types** | 8 |
| **Form Elements** | 12 types |
| **Wait Scenarios** | 8 |
| **Alert Types** | 3 |
| **Tables** | 2 |
| **Test Scenarios** | 100+ |
| **Lines of Code** | ~3000 |
| **File Size** | <150 KB |

---

## 🎉 Why This Application is Perfect for Training

1. **Complete Coverage** - Every Selenium concept in one app
2. **Zero Setup Time** - Works immediately
3. **No Distractions** - Focus on automation, not HTML
4. **Realistic** - Banking domain, real-world patterns
5. **Well Documented** - Every element explained
6. **Scalable** - Basic to advanced in one package
7. **Testable** - Clear IDs, predictable behavior
8. **Framework Ready** - Perfect for POM implementation

---

## 📞 Support & Feedback

**Created for:** NPCI Selenium Training  
**Version:** 2.0 Final  
**Date:** December 2024  
**Platform:** Any browser, any OS  

---

## 🏆 Achievement Unlocked!

**You now have a complete, professional Selenium training application!**

✅ 9 pages covering all concepts  
✅ 100+ test scenarios ready  
✅ Zero dependencies  
✅ Instant setup  
✅ Production-quality code  

**Ready to start automating!** 🚀

---

**Happy Testing!** 🎓
