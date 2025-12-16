# 🎓 Selenium Level 6 - Data-Driven Testing & Advanced Reporting

## Production-Grade Test Data Management & Beautiful Reports

---

## 📚 What You'll Learn

### **Level 6 Topics (3-4 hours)**
1. ✅ Data-Driven Testing - Test with multiple data sets
2. ✅ TestNG @DataProvider - Parameterized testing
3. ✅ CSV Data Integration - Read test data from files
4. ✅ Custom Test Listeners - Hook into test lifecycle
5. ✅ ExtentReports - Beautiful HTML reports
6. ✅ Test Logging - Detailed execution logs
7. ✅ Professional Reporting - Stakeholder-ready reports

---

## 📦 Package Contents

```
selenium-level6/
├── pom.xml
├── testng.xml
├── README.md (this file)
└── src/
    ├── main/
    │   ├── java/com/npci/training/
    │   │   ├── pages/              # Page Objects
    │   │   ├── utils/              # Utilities
    │   │   ├── listeners/          # Custom listeners
    │   │   │   └── ExtentReportListener.java
    │   │   └── providers/          # Data providers
    │   │       └── CSVDataProvider.java
    │   └── resources/testdata/
    │       └── login-data.csv      # Test data
    └── test/java/com/npci/training/
        ├── tests/
        │   └── BaseTest.java
        └── level6/
            ├── Test01_DataProviderCSV.java
            └── Test02_CustomListeners.java
```

---

## 🎯 **The Data-Driven Revolution**

### **The Problem (Without Data-Driven):**
```java
// ❌ 100 lines for 5 test scenarios!
@Test
public void testAdmin() {
    login("admin", "admin123", "Customer");
    // assertions
}

@Test
public void testUser1() {
    login("user1", "user123", "Customer");
    // assertions
}

@Test
public void testMerchant() {
    login("merchant1", "merchant123", "Merchant");
    // assertions
}

// 97 more methods... 😱
```

### **The Solution (With DataProvider):**
```java
// ✅ 10 lines for 100 test scenarios!
@DataProvider
public Object[][] getData() {
    return CSVDataProvider.getLoginData();  // 100 rows from CSV
}

@Test(dataProvider = "getData")
public void testLogin(String user, String pass, String type, String expected) {
    // One method, 100 executions!
}
```

**Impact:**
- **90% less code**
- **100x more test coverage**
- **Easy to add new scenarios** (just add CSV row!)

---

## 📊 **Data-Driven Testing with CSV**

### **Step 1: Create CSV File**
```csv
# src/main/resources/testdata/login-data.csv
username,password,usertype,expected
admin,admin123,Customer,success
user1,user123,Customer,success
merchant1,merchant123,Merchant,success
wrong,wrong,Customer,fail
"","",,fail
```

### **Step 2: Create DataProvider**
```java
@DataProvider(name = "loginData")
public Object[][] getLoginData() {
    return CSVDataProvider.getLoginData();
}
```

### **Step 3: Use in Test**
```java
@Test(dataProvider = "loginData")
public void testLogin(String username, String password, 
                     String usertype, String expected) {
    LoginPage page = new LoginPage(driver).open();
    
    if ("success".equals(expected)) {
        DashboardPage dashboard = page.loginAs(username, password, usertype);
        assertTrue(dashboard.isDashboardPageDisplayed());
    } else {
        page.enterUsername(username)
            .enterPassword(password)
            .clickLoginExpectingError();
        assertTrue(page.isLoginPageDisplayed());
    }
}
```

**Result:**
- CSV has 6 rows
- Test runs 6 times automatically
- Add 100 more rows → 100 more test executions
- **No code changes needed!**

---

## 📈 **ExtentReports - Beautiful Test Reports**

### **What is ExtentReports?**
```
TestNG Default Report:
├── Basic HTML
├── Green/Red text
├── Stack traces
└── Boring... 😴

ExtentReports:
├── Beautiful dashboard
├── Interactive charts
├── Detailed logs
├── Screenshots
├── Time analysis
├── Professional styling
└── Stakeholder-ready! 🎨
```

### **Setup:**
```java
// In testng.xml
<listeners>
    <listener class-name="com.npci.training.listeners.ExtentReportListener"/>
</listeners>
```

### **Usage in Tests:**
```java
@Test
public void testSomething() {
    // Add detailed logs to report
    ExtentReportListener.getTest().info("Opening login page");
    LoginPage page = new LoginPage(driver).open();
    
    ExtentReportListener.getTest().info("Entering credentials");
    page.enterUsername("admin");
    
    ExtentReportListener.getTest().pass("Test completed successfully");
}
```

### **Result:**
```
After tests run:
target/extent-reports/TestReport.html

Open in browser for:
✓ Dashboard with pass/fail summary
✓ Timeline view of test execution
✓ Detailed logs for each test
✓ Charts and graphs
✓ System information
✓ Professional presentation
```

---

## 🎯 **Key Features**

### **1. @DataProvider Annotation**
```java
// Simple hardcoded data
@DataProvider(name = "data")
public Object[][] getData() {
    return new Object[][] {
        {"test1", "pass1"},
        {"test2", "pass2"}
    };
}

// From CSV file
@DataProvider(name = "csvData")
public Object[][] getCSVData() {
    return CSVDataProvider.getLoginData();
}

// Parallel execution
@DataProvider(name = "parallelData", parallel = true)
public Object[][] getParallelData() {
    return data;
}
```

### **2. CSV Data Integration**
```java
// CSVDataProvider utility class
public class CSVDataProvider {
    public static Object[][] getCSVData(String filePath) {
        // Read CSV
        // Parse to Object[][]
        // Return
    }
    
    public static Object[][] getLoginData() {
        return getCSVData("src/main/resources/testdata/login-data.csv");
    }
}
```

### **3. Custom Listeners**
```java
public class ExtentReportListener implements ITestListener {
    
    @Override
    public void onTestStart(ITestResult result) {
        // Log test start
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        // Log success
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        // Log failure
        // Attach screenshot
    }
}
```

### **4. Test Logging**
```java
// In your test
ExtentReportListener.getTest().info("Step 1: Login");
ExtentReportListener.getTest().pass("Login successful");
ExtentReportListener.getTest().fail("Login failed");
ExtentReportListener.getTest().warning("Warning message");
```

---

## 🚀 **Run Commands**

```bash
# Run all tests
mvn test

# Run specific class
mvn test -Dtest=Test01_DataProviderCSV

# Run with testng.xml
mvn test -DsuiteXmlFile=testng.xml

# After execution, open report:
# target/extent-reports/TestReport.html
```

---

## 📊 **Real-World Example**

### **NPCI UPI Testing:**

**Before (100 tests, 100 methods):**
```java
@Test public void testUPI_Scenario1() { }
@Test public void testUPI_Scenario2() { }
// ... 98 more methods
```

**After (100 tests, 1 method):**
```java
// upi-test-data.csv
vpa,amount,bank,expected
user@upi,500,HDFC,success
merchant@upi,1000,ICICI,success
invalid@upi,50000,SBI,fail
// ... 97 more rows

@DataProvider
public Object[][] getUPIData() {
    return CSVDataProvider.getCSVData("upi-test-data.csv");
}

@Test(dataProvider = "getUPIData")
public void testUPIPayment(String vpa, String amount, 
                          String bank, String expected) {
    // One method tests all scenarios!
}
```

**Impact:**
- 99% less code
- Easy to add scenarios
- Non-technical people can add test cases (just CSV!)
- Runs in parallel
- Beautiful reports for management

---

## 💡 **Advanced DataProvider Patterns**

### **Pattern 1: Multiple Data Sources**
```java
@DataProvider
public Object[][] getTestData() {
    List<Object[]> allData = new ArrayList<>();
    
    // Add CSV data
    allData.addAll(Arrays.asList(CSVDataProvider.getLoginData()));
    
    // Add Excel data
    allData.addAll(Arrays.asList(ExcelDataProvider.getExcelData()));
    
    // Add database data
    allData.addAll(Arrays.asList(DBDataProvider.getDatabaseData()));
    
    return allData.toArray(new Object[0][]);
}
```

### **Pattern 2: Conditional Data**
```java
@DataProvider
public Object[][] getDataBasedOnEnvironment() {
    String env = System.getProperty("env", "qa");
    
    if ("prod".equals(env)) {
        return CSVDataProvider.getCSVData("prod-data.csv");
    } else {
        return CSVDataProvider.getCSVData("qa-data.csv");
    }
}
```

### **Pattern 3: Parallel Data Execution**
```java
@DataProvider(parallel = true)
public Object[][] getParallelData() {
    return testData;  // Executes in parallel!
}
```

---

## ✅ **Best Practices**

### **DO's ✅**

1. **Separate Test Data from Test Code**
   ```
   ✓ Data in CSV/Excel
   ✓ Tests in Java
   ✓ Easy to maintain
   ```

2. **Use Descriptive Test Names**
   ```java
   @Test(description = "Verify login with valid admin credentials")
   ```

3. **Add Detailed Logs**
   ```java
   test.info("Step 1: Opening page");
   test.info("Step 2: Entering data");
   test.pass("Test completed");
   ```

4. **Version Control Test Data**
   ```
   ✓ Commit CSV files to Git
   ✓ Track data changes
   ✓ Easy rollback
   ```

5. **Generate Reports After Each Run**
   ```
   ✓ Always check ExtentReports
   ✓ Share with stakeholders
   ✓ Track trends
   ```

### **DON'Ts ❌**

1. **Don't Hardcode Test Data in Tests**
   ```java
   // ❌ BAD
   @Test
   public void test() {
       login("admin", "admin123");  // Hardcoded!
   }
   
   // ✅ GOOD
   @Test(dataProvider = "data")
   public void test(String user, String pass) {
       login(user, pass);  // From data provider
   }
   ```

2. **Don't Ignore Report Logs**
   ```
   ❌ Just pass/fail
   ✅ Detailed step-by-step logs
   ```

3. **Don't Mix Test Logic with Data**
   ```
   ❌ Data in test methods
   ✅ Data in CSV/Excel/Database
   ```

---

## 📈 **Benefits Summary**

### **Data-Driven Testing:**
- ✅ **90% less code**
- ✅ **100x more coverage**
- ✅ **Easy to maintain**
- ✅ **Non-developers can add tests**
- ✅ **Parallel execution ready**

### **ExtentReports:**
- ✅ **Professional presentation**
- ✅ **Stakeholder-ready**
- ✅ **Detailed analytics**
- ✅ **Historical tracking**
- ✅ **Email-friendly**

### **Custom Listeners:**
- ✅ **Automatic logging**
- ✅ **Screenshot on failure**
- ✅ **Flexible hooks**
- ✅ **Centralized reporting**

---

## ✅ **Self-Assessment**

After Level 6, you should be able to:

- [ ] Create @DataProvider methods
- [ ] Read data from CSV files
- [ ] Implement custom listeners
- [ ] Generate ExtentReports
- [ ] Add detailed test logs
- [ ] Organize test data properly
- [ ] Run data-driven tests
- [ ] Share professional reports
- [ ] Handle multiple data sources
- [ ] **Build enterprise test suites!**

**Score:** ___/10

---

## 🚀 **Next Steps**

### **Immediate:**
1. Run Test01 - See DataProvider in action
2. Run Test02 - Open ExtentReport
3. Modify CSV - Add your own data
4. Create your own DataProvider

### **Level 7 Preview:**
- Excel Data Provider (XLSX files)
- Database Data Provider (SQL)
- JSON Data Provider
- Jenkins CI/CD Integration
- Email Reports
- Slack Notifications

---

## 💯 **Level 6 Complete!**

**Congratulations! You've learned:**
- ✅ Data-driven testing mastery
- ✅ Professional test reporting
- ✅ Custom listeners
- ✅ CSV data integration
- ✅ ExtentReports
- ✅ **Production-grade test management!**

**Total Tests:** 15+ with data-driven execution  
**Time Investment:** 3-4 hours  
**Skill Level:** Architect → Enterprise Lead  

---

**Master data-driven testing and lead enterprise projects!** 🚀

```bash
mvn test
# Then open: target/extent-reports/TestReport.html
```

🎓 **Happy Testing!**
