# 🎓 Selenium Level 5 - TestNG & Advanced Test Management

## Master Production Test Suite Organization

---

## 📚 What You'll Learn

### **Level 5 Topics (3-4 hours)**
1. ✅ TestNG Framework - Industry standard
2. ✅ TestNG Annotations - Powerful lifecycle management
3. ✅ Test Groups - Organize & run tests efficiently
4. ✅ Test Configuration - testng.xml mastery
5. ✅ Parallel Execution - Speed up test runs
6. ✅ Test Dependencies - Control execution order
7. ✅ Data Providers - Data-driven testing
8. ✅ Test Reporting - Professional test reports

---

## 📦 Package Contents

```
selenium-level5/
├── pom.xml
├── testng.xml                           # TestNG configuration
├── README.md (this file)
└── src/
    ├── main/java/com/npci/training/
    │   ├── pages/                      # Page Objects (from Level 4)
    │   │   ├── BasePage.java
    │   │   ├── LoginPage.java
    │   │   ├── DashboardPage.java
    │   │   └── ...
    │   └── utils/
    │       └── TestUtils.java          # Utilities (from Level 4)
    └── test/java/com/npci/training/
        ├── tests/
        │   └── BaseTest.java           # TestNG base class
        └── level5/
            ├── Test01_TestNGAnnotations.java
            └── Test02_TestNGGroups.java
```

---

## 🎯 **TestNG vs JUnit 5**

| Feature | JUnit 5 | TestNG | Winner |
|---------|---------|--------|--------|
| **Annotations** | @BeforeEach, @AfterEach | @BeforeMethod, @AfterMethod | Similar |
| **Test Groups** | @Tag | @Test(groups={"smoke"}) | TestNG ✅ |
| **Dependencies** | No | @Test(dependsOnMethods) | TestNG ✅ |
| **Parallel Execution** | Limited | Excellent | TestNG ✅ |
| **Data Driven** | @ParameterizedTest | @DataProvider | TestNG ✅ |
| **XML Configuration** | No | testng.xml | TestNG ✅ |
| **Priority** | @Order | @Test(priority=1) | TestNG ✅ |
| **Soft Assertions** | No | Yes | TestNG ✅ |

**Verdict:** TestNG is more powerful for large test suites!

---

## 🚀 Quick Start

### Prerequisites
```
✓ Completed Levels 1-4
✓ Java 11+, Maven installed
✓ Banking Portal running
```

### Run Your First TestNG Test
```bash
# Extract and import
unzip selenium-level5.zip

# Run using Maven
mvn test

# Run specific test class
mvn test -Dtest=Test01_TestNGAnnotations

# Run with testng.xml
mvn test -DsuiteXmlFile=testng.xml
```

---

## 📖 **TestNG Annotations Explained**

### **Execution Order:**
```
@BeforeSuite       ← Runs ONCE before entire suite
  @BeforeTest      ← Runs ONCE before each <test> in XML
    @BeforeClass   ← Runs ONCE before each test class
      @BeforeMethod  ← Runs BEFORE each @Test method
        @Test        ← YOUR TEST METHOD
      @AfterMethod   ← Runs AFTER each @Test method
    @AfterClass    ← Runs ONCE after each test class
  @AfterTest       ← Runs ONCE after each <test> in XML
@AfterSuite        ← Runs ONCE after entire suite
```

### **Real Example:**
```java
public class LoginTests extends BaseTest {
    
    @BeforeSuite
    public void setupEnvironment() {
        // Setup WebDriverManager (once for all tests)
        WebDriverManager.chromedriver().setup();
    }
    
    @BeforeClass
    public void prepareTestData() {
        // Create test users in database
        // Load test data
    }
    
    @BeforeMethod
    public void openBrowser() {
        // Start browser for each test
        driver = new ChromeDriver();
    }
    
    @Test
    public void testLogin() {
        // Your actual test
    }
    
    @AfterMethod
    public void closeBrowser() {
        // Close browser after each test
        driver.quit();
    }
    
    @AfterClass
    public void cleanupTestData() {
        // Delete test users
        // Clean up database
    }
    
    @AfterSuite
    public void generateReport() {
        // Generate test report
    }
}
```

---

## 🏷️ **Test Groups - Game Changer!**

### **Why Use Groups?**
```
❌ Without Groups:
   Run ALL 500 tests (2 hours) to check one feature

✅ With Groups:
   Run only SMOKE tests (10 minutes) for quick validation
   Run only LOGIN tests when login feature changes
   Run only CRITICAL tests before release
```

### **Common Group Strategies:**

```java
@Test(groups = {"smoke"})           // Quick essential tests (10 min)
@Test(groups = {"regression"})      // All tests (full suite)
@Test(groups = {"critical"})        // Must-pass tests
@Test(groups = {"sanity"})          // Basic functionality

// Multiple groups
@Test(groups = {"smoke", "login", "critical"})

// NPCI-specific
@Test(groups = {"upi", "payments"})
@Test(groups = {"api", "integration"})
@Test(groups = {"ui", "end-to-end"})
```

### **Running Specific Groups:**

**Command Line:**
```bash
# Run smoke tests only
mvn test -Dgroups="smoke"

# Run multiple groups
mvn test -Dgroups="smoke,critical"

# Exclude groups
mvn test -DexcludedGroups="slow"
```

**In testng.xml:**
```xml
<test name="Smoke Suite">
    <groups>
        <run>
            <include name="smoke"/>
            <include name="critical"/>
        </run>
    </groups>
    <classes>
        <class name="com.npci.training.level5.Test02_TestNGGroups"/>
    </classes>
</test>
```

---

## ⚙️ **testng.xml Configuration**

### **Basic Structure:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">

<suite name="My Test Suite">
    <test name="My Test">
        <classes>
            <class name="com.npci.training.level5.Test01_TestNGAnnotations"/>
        </classes>
    </test>
</suite>
```

### **Advanced Configuration:**
```xml
<suite name="NPCI Test Suite" parallel="methods" thread-count="3">
    
    <!-- Suite parameters -->
    <parameter name="browser" value="chrome"/>
    <parameter name="baseUrl" value="http://localhost:8000"/>
    
    <!-- Listeners -->
    <listeners>
        <listener class-name="com.npci.training.listeners.TestListener"/>
    </listeners>
    
    <!-- Smoke Tests -->
    <test name="Smoke Tests">
        <groups>
            <run>
                <include name="smoke"/>
            </run>
        </groups>
        <classes>
            <class name="com.npci.training.level5.Test01_TestNGAnnotations"/>
        </classes>
    </test>
    
    <!-- Regression Tests -->
    <test name="Regression Tests">
        <classes>
            <class name="com.npci.training.level5.Test01_TestNGAnnotations"/>
            <class name="com.npci.training.level5.Test02_TestNGGroups"/>
        </classes>
    </test>
    
</suite>
```

---

## 🎯 **Test Priority & Dependencies**

### **Priority - Control Execution Order:**
```java
@Test(priority = 1)
public void test01_Login() {
    // Runs first
}

@Test(priority = 2)
public void test02_Dashboard() {
    // Runs second
}

@Test(priority = 999)
public void test99_Cleanup() {
    // Runs last
}
```

### **Dependencies - Chain Tests:**
```java
@Test
public void testLogin() {
    // Login test
}

@Test(dependsOnMethods = "testLogin")
public void testDashboard() {
    // Only runs if testLogin passes
}

@Test(dependsOnGroups = {"smoke"})
public void testAdvancedFeature() {
    // Only runs if all smoke tests pass
}
```

---

## ⚡ **Parallel Execution**

### **Speed Up Your Tests!**

**Before:**
```
500 tests × 10 seconds each = 5000 seconds (83 minutes)
```

**After (3 threads):**
```
500 tests ÷ 3 threads = ~28 minutes (3x faster!)
```

### **Configuration:**
```xml
<!-- In testng.xml -->
<suite name="Parallel Suite" parallel="methods" thread-count="3">
    <!-- Tests run in parallel using 3 threads -->
</suite>
```

**Options:**
- `parallel="methods"` - Run test methods in parallel
- `parallel="classes"` - Run test classes in parallel
- `parallel="tests"` - Run <test> tags in parallel
- `thread-count="3"` - Use 3 threads

**⚠️ Warning:** Ensure tests are thread-safe!

---

## 📊 **Test Reporting**

### **Built-in Reports:**

After running tests:
```bash
mvn test
```

Reports generated in:
```
target/
├── surefire-reports/
│   ├── index.html              # HTML report
│   ├── testng-results.xml      # XML results
│   └── emailable-report.html   # Email-friendly report
```

Open `target/surefire-reports/index.html` in browser!

---

## 💡 **Real-World Example**

### **NPCI Test Organization:**

```java
// Smoke Tests - Run in 10 minutes
@Test(groups = {"smoke", "upi"})
public void testUPIPayment() {
    // Quick UPI payment test
}

// Regression - Complete testing
@Test(groups = {"regression", "upi", "integration"})
public void testUPIWithBank() {
    // Complete UPI flow with bank integration
}

// Critical - Pre-release tests
@Test(groups = {"critical", "upi"}, priority = 1)
public void testUPITransaction() {
    // Must-pass before release
}
```

### **Running Different Suites:**

**Before Release (5 min):**
```bash
mvn test -Dgroups="critical"
```

**Daily Build (30 min):**
```bash
mvn test -Dgroups="smoke"
```

**Nightly Build (2 hours):**
```bash
mvn test -Dgroups="regression"
```

**Feature Testing:**
```bash
mvn test -Dgroups="upi"
```

---

## 📋 **Best Practices**

### **DO's ✅**

1. **Use Groups Wisely**
   ```java
   @Test(groups = {"smoke", "critical", "login"})
   ```

2. **Set Priorities**
   ```java
   @Test(priority = 1)  // Critical tests first
   ```

3. **Use testng.xml**
   ```xml
   <suite name="My Suite">
       <!-- Configuration here -->
   </suite>
   ```

4. **Enable/Disable Tests**
   ```java
   @Test(enabled = false)  // Temporarily disable
   ```

5. **Add Descriptions**
   ```java
   @Test(description = "Verify login with valid credentials")
   ```

### **DON'Ts ❌**

1. **Don't Skip testng.xml**
   - XML provides powerful configuration

2. **Don't Ignore Dependencies**
   - Use `dependsOnMethods` for related tests

3. **Don't Over-Parallelize**
   - Start with `thread-count="2"` or `"3"`

4. **Don't Mix JUnit and TestNG**
   - Choose one framework

5. **Don't Forget Test Isolation**
   - Each test should be independent

---

## 🎯 **Migration from JUnit to TestNG**

### **JUnit 5 → TestNG:**

```java
// JUnit 5
@BeforeAll     →  @BeforeSuite / @BeforeClass
@BeforeEach    →  @BeforeMethod
@Test          →  @Test
@AfterEach     →  @AfterMethod
@AfterAll      →  @AfterClass / @AfterSuite
@Disabled      →  @Test(enabled = false)
@DisplayName   →  @Test(description = "...")
```

### **Advantages of TestNG:**
- ✅ Better test organization (groups)
- ✅ Built-in parallel execution
- ✅ Test dependencies
- ✅ Powerful XML configuration
- ✅ Data providers
- ✅ Better reporting

---

## 🔧 **Run Commands**

```bash
# Run all tests
mvn test

# Run specific class
mvn test -Dtest=Test01_TestNGAnnotations

# Run specific method
mvn test -Dtest=Test01_TestNGAnnotations#test01_LoginPageLoads

# Run with groups
mvn test -Dgroups="smoke"
mvn test -Dgroups="smoke,critical"

# Exclude groups
mvn test -DexcludedGroups="slow"

# Run with specific XML
mvn test -DsuiteXmlFile=testng-smoke.xml

# Parallel execution
mvn test -Dparallel=methods -DthreadCount=3

# With custom properties
mvn test -Dbrowser=chrome -DbaseUrl=http://localhost:8000
```

---

## ✅ **Self-Assessment**

After Level 5, you should be able to:

- [ ] Explain TestNG annotations lifecycle
- [ ] Create and use test groups
- [ ] Configure testng.xml
- [ ] Set test priorities and dependencies
- [ ] Run specific test groups
- [ ] Enable parallel execution
- [ ] Generate test reports
- [ ] Organize large test suites
- [ ] Migrate from JUnit to TestNG
- [ ] Build production test framework

**Score:** ___/10

**If 8+:** Ready for Level 6! 🎉  
**If 5-7:** More practice needed  
**If <5:** Review and repeat exercises

---

## 🚀 **Next Steps**

### **Level 6 Preview:**
- Data Providers (CSV, Excel, Database)
- Custom Test Listeners
- ExtentReports integration
- Jenkins CI/CD integration
- Selenium Grid
- Docker containers

---

## 💯 **Level 5 Complete!**

**Congratulations! You've learned:**
- ✅ TestNG framework mastery
- ✅ Test organization strategies
- ✅ Group-based test execution
- ✅ XML configuration
- ✅ Parallel test execution
- ✅ Professional test reporting
- ✅ **Production-ready test suite management!**

**Total Concepts:** 25+  
**Total Tests:** 17+  
**Time Investment:** 3-4 hours  
**Skill Level:** Expert → Architect  

---

**Master TestNG and lead test automation projects!** 🚀

```bash
mvn test
```

🎓 **Happy Testing!**
