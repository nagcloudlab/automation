# 🎭 Playwright Level 6 - Complete Summary

## Data-Driven Testing - CSV, Excel, Database

**Package:** `playwright-level6.zip` (21 KB)

---

## 🎯 **The Game-Changer for Test Scalability!**

### **The Problem:**

```java
// Without Data-Driven: 100 scenarios = 100 test methods! 😱

@Test
void testLogin1() { login("user1", "pass1"); }

@Test
void testLogin2() { login("user2", "pass2"); }

@Test
void testLogin3() { login("user3", "pass3"); }

// ... 97 more test methods!
```

### **The Solution:**

```java
// With Data-Driven: 100 scenarios = 1 test method! ✨

@ParameterizedTest
@CsvFileSource(resources = "/csv/login_data.csv", numLinesToSkip = 1)
void testLogin(String username, String password, String expected) {
    login(username, password);
    verify(expected);
}

// Just add more CSV lines!
```

**Result:** 99% less code!

---

## 📦 **What's Included:**

```
✅ 3 Data Provider Utilities
   - CsvDataProvider
   - ExcelDataProvider  
   - DatabaseDataProvider

✅ 15 Comprehensive Tests
   - CSV tests (5)
   - Excel tests (5 - conceptual)
   - Database tests (5 - conceptual)

✅ Sample Test Data
   - login_data.csv
   - transfer_data.csv

✅ Complete Banking Examples
✅ All Three Approaches
✅ Best Practices Guide
```

---

## 🔥 **Three Approaches:**

### **1. CSV (@CsvSource, @CsvFileSource)**

**Inline Data:**
```java
@ParameterizedTest
@CsvSource({
    "admin, password123, success",
    "user, wrongpass, fail"
})
void testLogin(String user, String pass, String expected) {
    // Test logic
}
```

**External File:**
```java
@ParameterizedTest
@CsvFileSource(resources = "/csv/login_data.csv", numLinesToSkip = 1)
void testLogin(String user, String pass, String expected) {
    // Test logic
}
```

**CSV File:**
```csv
username,password,expectedResult
admin,pass123,success
user,wrongpass,fail
guest,guest,success
```

**Benefits:**
- ✅ Easy to maintain
- ✅ Non-technical users can edit
- ✅ Version controlled
- ✅ Perfect for 10-100 scenarios

---

### **2. Excel (Apache POI)**

```java
@ParameterizedTest
@MethodSource("getExcelData")
void testTransfer(String from, String to, String amount, String expected) {
    // Test logic
}

static Stream<Arguments> getExcelData() {
    Object[][] data = ExcelDataProvider.readExcel(
        "src/test/resources/excel/testdata.xlsx",
        "TransferTests",  // Sheet name
        true);
    
    return Arrays.stream(data)
        .map(arr -> Arguments.of(arr[0], arr[1], arr[2], arr[3]));
}
```

**Excel Structure:**
```
testdata.xlsx
├── LoginTests sheet
│   │ username │ password │ expected │
│   │ admin    │ pass123  │ success  │
│
└── TransferTests sheet
    │ from    │ to         │ amount │ expected │
    │ savings │ 9876543210 │ 5000   │ success  │
```

**Benefits:**
- ✅ Multiple sheets per file
- ✅ Rich formatting
- ✅ Business-friendly
- ✅ Perfect for 100-1000 scenarios

---

### **3. Database (JDBC)**

```java
// Setup
@BeforeAll
static void setup() {
    conn = DatabaseDataProvider.getH2Connection();
    DatabaseDataProvider.initializeTestData(conn);
}

// Use in tests
@ParameterizedTest
@MethodSource("getUsersFromDB")
void testLogin(String username, String password, String status) {
    // Test logic
}

static Stream<Arguments> getUsersFromDB() {
    String query = "SELECT username, password, status FROM users";
    Object[][] data = DatabaseDataProvider.executeQuery(conn, query);
    
    return Arrays.stream(data)
        .map(arr -> Arguments.of(arr[0], arr[1], arr[2]));
}
```

**SQL Schema:**
```sql
CREATE TABLE users (
    username VARCHAR(50),
    password VARCHAR(50),
    account_type VARCHAR(20),
    balance DECIMAL(10,2),
    status VARCHAR(20)
);
```

**Benefits:**
- ✅ Production-like data
- ✅ Complex queries (JOINs)
- ✅ Dynamic test data
- ✅ Perfect for 1000+ scenarios

---

## 💰 **Complete Banking Example:**

```java
// 1. CSV for login
@ParameterizedTest
@CsvFileSource(resources = "/csv/login_data.csv", numLinesToSkip = 1)
void testLogin(String username, String password, String expected) {
    page.navigate("/login");
    page.getByLabel("Username").fill(username);
    page.getByLabel("Password").fill(password);
    page.click("#login");
    
    if (expected.equals("success")) {
        assertThat(page.getByRole(AriaRole.HEADING, setName("Dashboard")))
            .isVisible();
    } else {
        assertThat(page.locator(".error-message")).isVisible();
    }
}

// 2. Excel for transfers
@ParameterizedTest
@MethodSource("getTransferData")
void testTransfer(String from, String to, String amount, String remarks, String expected) {
    page.navigate("/transfer");
    page.getByLabel("From").selectOption(from);
    page.getByLabel("To").fill(to);
    page.getByLabel("Amount").fill(amount);
    page.getByLabel("Remarks").fill(remarks);
    page.click("#submit");
    
    if (expected.equals("success")) {
        assertThat(page.getByText("Transfer successful")).isVisible();
    } else {
        assertThat(page.locator(".error-message")).isVisible();
    }
}

// 3. Database for validation
@ParameterizedTest
@MethodSource("getActiveUsers")
void testUserAccess(String username, String accountType, double balance) {
    // Login and verify balance matches DB
    page.navigate("/login");
    page.getByLabel("Username").fill(username);
    page.getByLabel("Password").fill("password");
    page.click("#login");
    
    assertThat(page.getByTestId("balance"))
        .containsText(String.format("₹%.2f", balance));
}

static Stream<Arguments> getActiveUsers() {
    String query = """
        SELECT username, account_type, balance 
        FROM users 
        WHERE status = 'active' AND balance > 10000
        """;
    
    Object[][] data = DatabaseDataProvider.executeQuery(conn, query);
    return Arrays.stream(data)
        .map(arr -> Arguments.of(arr[0], arr[1], arr[2]));
}
```

---

## 📊 **When to Use Each:**

| Approach | Best For | Size | Ease |
|----------|----------|------|------|
| **CSV** | Simple scenarios | 10-100 | ★★★★★ |
| **Excel** | Complex organization | 100-1000 | ★★★★☆ |
| **Database** | Production data | 1000+ | ★★★☆☆ |

**Decision Tree:**

```
Need test data?
├─ < 10 scenarios? → @CsvSource (inline)
├─ 10-100 scenarios? → CSV files
├─ 100-1000 scenarios? → Excel
└─ 1000+ scenarios? → Database
```

---

## ✅ **Best Practices:**

### **CSV:**
1. ✅ Always use headers
2. ✅ One file per test type
3. ✅ Meaningful column names
4. ✅ Include edge cases
5. ✅ Version control

### **Excel:**
1. ✅ One sheet per test type
2. ✅ Descriptive sheet names
3. ✅ Headers in bold
4. ✅ Freeze header row
5. ✅ Business-friendly

### **Database:**
1. ✅ Use test database
2. ✅ Reset before tests
3. ✅ Cleanup after tests
4. ✅ Meaningful test data
5. ✅ Index for performance

---

## 🔥 **Top Advantages:**

1. **99% Less Code** (1 method vs 100 methods)
2. **Easy to Scale** (add data, not code)
3. **Non-Technical Friendly** (CSV/Excel)
4. **Version Controlled** (Git tracks changes)
5. **Production-Like** (Database data)
6. **Clear Reports** (Parameterized names)
7. **Easy Maintenance** (Update data, not code)
8. **Business Collaboration** (Business adds scenarios)

---

## 💯 **Impact:**

### **Code Reduction:**
```
100 test scenarios:

Without Data-Driven: 100 test methods (500+ lines)
With Data-Driven:    1 test method (10 lines)

Reduction: 98% less code!
```

### **Maintenance Time:**
```
Add new scenario:

Without Data-Driven: 30 minutes (write new test)
With Data-Driven:    30 seconds (add CSV line)

Improvement: 60x faster!
```

### **Team Collaboration:**
```
Add business scenarios:

Without Data-Driven: Developer needed
With Data-Driven:    Business user adds CSV/Excel

Result: No developer needed!
```

---

## 🎓 **What You Master:**

**Concepts:**
- [x] Data-driven testing principles
- [x] Separation of data and logic
- [x] Parameterized tests
- [x] JUnit 5 annotations

**Skills:**
- [x] CSV testing (3 approaches)
- [x] Excel testing (Apache POI)
- [x] Database testing (JDBC)
- [x] Custom data providers
- [x] Best practices

**Benefits:**
- [x] 99% less code
- [x] 60x faster maintenance
- [x] Business collaboration
- [x] Professional data management

---

## 🚀 **Quick Start:**

```bash
# Extract
unzip playwright-level6.zip
cd playwright-level6

# Run all tests
mvn test

# Run specific
mvn test -Dtest=Test01_CsvDataDriven
```

---

## 🎉 **Perfect Addition to Your Skills!**

**Level 6 teaches you:**

**The Problem:**
- 100 scenarios = 100 test methods
- Hard to maintain
- Can't scale

**The Solution:**
- 100 scenarios = 1 test method
- Easy to maintain
- Scales infinitely

**The Result:**
- 99% less code
- 60x faster updates
- Business can add data
- Professional testing

---

## 📈 **Complete Journey:**

| Level | Focus | Key Benefit |
|-------|-------|-------------|
| **1** | Browser Basics | Foundation |
| **2** | Locators | Interactions |
| **3** | Auto-Waiting | 90% less code |
| **4** | Page Objects | Professional |
| **5** | Advanced | 50% faster |
| **6** | Data-Driven | 99% less code ⭐ |

---

**Download and scale your tests infinitely!** 🎭

All files ready above! 👆

**Happy Data-Driven Testing!** 🚀
