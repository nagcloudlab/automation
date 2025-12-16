# 🎓 Selenium Level 6 - JUnit 5 Data-Driven Testing (Refactored)

## Complete Data-Driven Testing with CSV, Excel & Database

**Version:** 2.0.0 (JUnit 5 Only - Clean Refactor)

---

## 📚 What You'll Learn

### **Level 6 Topics (4-5 hours)**
1. ✅ **CSV Data-Driven Testing** - @CsvFileSource, @CsvSource
2. ✅ **Excel Data-Driven Testing** - Apache POI, @ArgumentsSource
3. ✅ **Database Data-Driven Testing** - JDBC, HikariCP, PostgreSQL, H2
4. ✅ **JUnit 5 Parameterized Tests** - Multiple data sources
5. ✅ **ExtentReports Integration** - Professional HTML reports
6. ✅ **Production Patterns** - Enterprise-ready frameworks

---

## 📦 Package Contents

```
selenium-level6-junit/
├── pom.xml                          # Maven configuration
├── README.md                        # This file
└── src/
    ├── main/
    │   ├── java/com/npci/training/
    │   │   ├── pages/               # 6 Page Objects
    │   │   ├── utils/               # TestUtils
    │   │   ├── providers/           # 3 Data Providers
    │   │   │   ├── CSVArgumentsProvider.java
    │   │   │   ├── ExcelArgumentsProvider.java
    │   │   │   └── DatabaseArgumentsProvider.java
    │   │   └── extensions/          # ExtentReports extension
    │   └── resources/testdata/
    │       └── login-data.csv       # Sample CSV data
    └── test/java/com/npci/training/
        ├── tests/
        │   └── BaseTest.java        # JUnit 5 base class
        └── level6/
            ├── Test01_CSVDataDriven.java
            ├── Test02_ExcelDataDriven.java
            ├── Test03_DatabaseDataDriven.java
            └── Test04_ExtentReportsIntegration.java
```

---

## 🎯 **Key Features**

### **✅ Pure JUnit 5**
- No TestNG dependencies
- Clean JUnit 5 annotations
- @ParameterizedTest everywhere
- JUnit 5 Extensions API

### **✅ Three Data Sources**
1. **CSV** - Simple text files
2. **Excel** - Complex spreadsheets (.xlsx)
3. **Database** - PostgreSQL, H2, MySQL

### **✅ Production Ready**
- HikariCP connection pooling
- ExtentReports HTML reports
- Proper error handling
- Best practices included

---

## 🚀 Quick Start

### **Prerequisites**
```
✓ Java 11+
✓ Maven 3.6+
✓ Chrome browser
✓ Banking Portal running (from Level 1)
✓ (Optional) PostgreSQL for database tests
```

### **Run All Tests**
```bash
mvn clean test
```

### **Run Specific Test**
```bash
# CSV tests
mvn test -Dtest=Test01_CSVDataDriven

# Excel tests
mvn test -Dtest=Test02_ExcelDataDriven

# Database tests (H2 in-memory)
mvn test -Dtest=Test03_DatabaseDataDriven

# ExtentReports
mvn test -Dtest=Test04_ExtentReportsIntegration
```

### **Run by Tags**
```bash
# Smoke tests only
mvn test -Dgroups="smoke"

# Database tests only
mvn test -Dgroups="database"

# CSV + Excel tests
mvn test -Dgroups="csv | excel"
```

---

## 📊 **Data Provider 1: CSV**

### **Usage:**
```java
@ParameterizedTest
@CsvFileSource(resources = "/testdata/login-data.csv", numLinesToSkip = 1)
void testLogin(String user, String pass, String type, String expected) {
    // Test code
}
```

### **CSV File:**
```csv
username,password,usertype,expected
admin,admin123,Customer,success
user1,user123,Customer,success
wrong,wrong,Customer,fail
```

### **Benefits:**
- ✅ Simple text format
- ✅ Easy to edit
- ✅ Version control friendly
- ✅ Quick to create

### **When to Use:**
- Simple test data
- Few columns
- Single test type
- Quick prototyping

---

## 📗 **Data Provider 2: Excel**

### **Usage:**
```java
@ParameterizedTest
@ArgumentsSource(ExcelArgumentsProvider.LoginDataProvider.class)
void testLogin(String user, String pass, String type, String expected) {
    // Test code
}
```

### **Excel File Structure:**
**File:** `test-data.xlsx`

**Sheet 1: LoginData**
| username | password | usertype | expected |
|----------|----------|----------|----------|
| admin | admin123 | Customer | success |
| user1 | user123 | Customer | success |

**Sheet 2: PaymentData**
| amount | from | to | expected |
|--------|------|-------|----------|
| 500 | ACC001 | ACC002 | success |

### **Benefits:**
- ✅ Multiple sheets in one file
- ✅ Data validation
- ✅ Formulas supported
- ✅ Non-technical friendly
- ✅ Better organization

### **When to Use:**
- Complex test data
- Multiple test types
- Team collaboration
- Large datasets

### **Create Excel File:**
1. Microsoft Excel / Google Sheets / LibreOffice
2. Add headers in row 1
3. Add data in rows below
4. Save as `.xlsx`
5. Place in `src/main/resources/testdata/`

---

## 🗄️ **Data Provider 3: Database**

### **H2 In-Memory (Demo/Testing):**
```java
@ParameterizedTest
@ArgumentsSource(DatabaseArgumentsProvider.H2LoginDataProvider.class)
void testLogin(String user, String pass, String type, String expected) {
    // Test code - data from H2 database
}
```

**Benefits:**
- ✅ No external database needed
- ✅ Automatically initialized
- ✅ Perfect for demos/CI
- ✅ Fast execution

### **PostgreSQL (Production):**
```java
@ParameterizedTest
@ArgumentsSource(DatabaseArgumentsProvider.PostgreSQLLoginDataProvider.class)
void testLogin(String user, String pass, String type, String expected) {
    // Test code - data from PostgreSQL
}
```

**Run with PostgreSQL:**
```bash
mvn test -Dtest=Test03_DatabaseDataDriven \
  -Ddb.url=jdbc:postgresql://localhost:5432/testdata \
  -Ddb.username=postgres \
  -Ddb.password=yourpassword
```

### **Database Setup:**
```sql
-- Create table
CREATE TABLE login_test_data (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100),
    usertype VARCHAR(50),
    expected VARCHAR(20)
);

-- Insert test data
INSERT INTO login_test_data (username, password, usertype, expected) VALUES
('admin', 'admin123', 'Customer', 'success'),
('user1', 'user123', 'Customer', 'success'),
('merchant1', 'merchant123', 'Merchant', 'success'),
('wrong', 'wrong', 'Customer', 'fail');
```

### **Benefits:**
- ✅ Centralized test data
- ✅ Production-like testing
- ✅ Easy updates
- ✅ Team collaboration
- ✅ Connection pooling (HikariCP)

### **When to Use:**
- Enterprise testing
- Shared test data
- Production environments
- Large datasets
- Team collaboration

---

## 📈 **ExtentReports**

### **Enable in Test:**
```java
@ExtendWith(ExtentReportExtension.class)
public class MyTests extends BaseTest {
    
    @Test
    void testSomething() {
        ExtentReportExtension.info("Step 1: Opening page");
        // Test code
        ExtentReportExtension.pass("Test passed!");
    }
}
```

### **View Report:**
```bash
# After running tests
open target/extent-reports/TestReport.html
```

### **Report Features:**
- ✅ Beautiful dashboard
- ✅ Pass/Fail/Skip charts
- ✅ Timeline view
- ✅ Detailed logs
- ✅ System information
- ✅ Stakeholder-ready

---

## 💡 **Comparison: CSV vs Excel vs Database**

| Feature | CSV | Excel | Database |
|---------|-----|-------|----------|
| **Ease of Use** | ✅ Very Easy | ✅ Easy | ⚠️ Moderate |
| **Multiple Sheets** | ❌ No | ✅ Yes | ✅ Yes (tables) |
| **Data Validation** | ❌ No | ✅ Yes | ✅ Yes |
| **Formulas** | ❌ No | ✅ Yes | ✅ Yes (SQL) |
| **Collaboration** | ⚠️ Basic | ✅ Good | ✅ Excellent |
| **Version Control** | ✅ Perfect | ⚠️ Binary | ✅ Schema only |
| **Large Datasets** | ⚠️ Slow | ⚠️ Slow | ✅ Fast |
| **Best For** | Quick tests | Team work | Enterprise |

---

## 🎯 **Usage Guidelines**

### **Use CSV When:**
- Simple test data (< 10 columns)
- Single test type
- Quick prototyping
- Small datasets (< 100 rows)
- Version control important

### **Use Excel When:**
- Multiple test types (multiple sheets)
- Complex data structures
- Non-technical team members
- Medium datasets (100-1000 rows)
- Data validation needed

### **Use Database When:**
- Enterprise testing
- Large datasets (1000+ rows)
- Shared test data
- Production environments
- Team collaboration critical
- Connection pooling needed

---

## 🏗️ **Project Structure Explanation**

### **Data Providers (`src/main/java/providers/`):**
```
CSVArgumentsProvider.java
├── LoginDataProvider       → Reads login-data.csv
└── fromCSV()              → Generic CSV reader

ExcelArgumentsProvider.java
├── LoginDataProvider       → Reads Excel LoginData sheet
├── fromExcel()            → Generic Excel reader
└── printExcelData()       → Debug utility

DatabaseArgumentsProvider.java
├── H2LoginDataProvider     → H2 in-memory database
├── PostgreSQLLoginDataProvider → PostgreSQL database
├── executeQuery()         → Generic query executor
└── initializeDataSource() → HikariCP connection pool
```

### **Test Classes (`src/test/java/level6/`):**
```
Test01_CSVDataDriven.java
├── testLoginWithCsvFile()        → @CsvFileSource
├── testLoginWithInlineCsv()      → @CsvSource
└── testLoginWithCustomProvider() → @ArgumentsSource

Test02_ExcelDataDriven.java
├── testLoginWithExcelData()     → Excel @ArgumentsSource
├── testExcelDataReading()       → Verification test
└── ExcelValidation              → Nested validation tests

Test03_DatabaseDataDriven.java
├── testLoginWithH2Data()        → H2 database
├── testLoginWithPostgreSQLData() → PostgreSQL
├── testDatabaseConnection()     → Connection test
└── DatabaseOperations           → Nested DB tests

Test04_ExtentReportsIntegration.java
├── testLoginPageLoad()          → Basic reporting
├── testSuccessfulLogin()        → Login with logs
├── testParameterizedLogin()     → Parameterized + reports
└── NavigationTests              → Nested reporting tests
```

---

## 🎓 **Learning Path**

### **Day 1: CSV (2 hours)**
1. Understand @CsvFileSource
2. Create CSV files
3. Run Test01_CSVDataDriven
4. Modify CSV, add test cases
5. Practice @CsvSource for inline data

### **Day 2: Excel (2 hours)**
1. Create Excel test data file
2. Understand ExcelArgumentsProvider
3. Run Test02_ExcelDataDriven
4. Add multiple sheets
5. Practice complex data structures

### **Day 3: Database (2 hours)**
1. Understand H2 in-memory database
2. Run Test03_DatabaseDataDriven (H2)
3. (Optional) Setup PostgreSQL
4. Create test tables
5. Practice database queries

### **Day 4: Integration (1 hour)**
1. Combine CSV + Excel + Database
2. Add ExtentReports
3. Run full test suite
4. Review HTML reports
5. Build production framework

---

## 💯 **Best Practices**

### **✅ DO:**
1. **Organize by data source**
   - CSV for simple data
   - Excel for complex data
   - Database for production

2. **Use meaningful test names**
   ```java
   @DisplayName("Login should succeed with valid admin credentials")
   ```

3. **Add proper logging**
   ```java
   ExtentReportExtension.info("Step 1: Opening page");
   ```

4. **Close database connections**
   ```java
   @AfterAll
   static void cleanup() {
       DatabaseArgumentsProvider.closeDataSource();
   }
   ```

5. **Validate test data exists**
   ```java
   assertTrue(data.count() > 0, "Should have test data");
   ```

### **❌ DON'T:**
1. **Don't hardcode test data**
   ```java
   // ❌ BAD
   @Test void test() { login("admin", "pass"); }
   
   // ✅ GOOD
   @ParameterizedTest
   @CsvSource({"admin,pass"})
   void test(String user, String pass) { }
   ```

2. **Don't mix data sources unnecessarily**
   - Use one primary source per test class
   - Mix only when needed

3. **Don't leave connections open**
   - Always close in @AfterAll
   - Use HikariCP for pooling

4. **Don't skip error handling**
   - Handle file not found
   - Handle connection failures
   - Validate data structure

---

## 🔧 **Configuration**

### **Database Configuration:**

**H2 (Default - No setup needed):**
```properties
# Automatically configured
jdbc:h2:mem:testdb
```

**PostgreSQL:**
```bash
# Via system properties
mvn test -Ddb.url=jdbc:postgresql://localhost:5432/testdata \
         -Ddb.username=postgres \
         -Ddb.password=yourpassword
```

**MySQL:**
```java
// Create custom provider
String jdbcUrl = "jdbc:mysql://localhost:3306/testdata";
DatabaseArgumentsProvider.executeQuery(jdbcUrl, user, pass, query);
```

### **Maven Configuration:**
Already configured in `pom.xml`:
- JUnit 5 (jupiter)
- Apache POI (Excel)
- Commons CSV
- PostgreSQL JDBC
- H2 Database
- HikariCP
- ExtentReports

---

## 📝 **Sample Test Data**

### **CSV Format:**
```csv
username,password,usertype,expected
admin,admin123,Customer,success
user1,user123,Customer,success
wrong,wrong,Customer,fail
```

### **Excel Format:**
**Sheet: LoginData**
| username | password | usertype | expected |
|----------|----------|----------|----------|
| admin | admin123 | Customer | success |
| user1 | user123 | Customer | success |

**Sheet: PaymentData**
| amount | from | to | expected |
|--------|------|-------|----------|
| 500 | ACC001 | ACC002 | success |
| 1000 | ACC003 | ACC004 | success |

### **Database Schema:**
```sql
CREATE TABLE login_test_data (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100),
    usertype VARCHAR(50),
    expected VARCHAR(20)
);
```

---

## 🎯 **Run Commands Summary**

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=Test01_CSVDataDriven
mvn test -Dtest=Test02_ExcelDataDriven
mvn test -Dtest=Test03_DatabaseDataDriven
mvn test -Dtest=Test04_ExtentReportsIntegration

# Run by tags
mvn test -Dgroups="smoke"
mvn test -Dgroups="csv"
mvn test -Dgroups="excel"
mvn test -Dgroups="database"
mvn test -Dgroups="smoke | critical"

# With database configuration
mvn test -Dtest=Test03_DatabaseDataDriven \
  -Ddb.url=jdbc:postgresql://localhost:5432/testdata \
  -Ddb.username=postgres \
  -Ddb.password=yourpassword

# View ExtentReports
open target/extent-reports/TestReport.html
```

---

## 💯 **What You'll Achieve**

After completing Level 6, you will:

- [ ] Master CSV data-driven testing with @CsvFileSource
- [ ] Master Excel data-driven testing with Apache POI
- [ ] Master Database data-driven testing with JDBC
- [ ] Understand HikariCP connection pooling
- [ ] Create production-ready test frameworks
- [ ] Generate professional HTML reports
- [ ] Choose the right data source for each scenario
- [ ] Build enterprise-grade test automation

**Skill Level:** Expert → Enterprise Architect

---

## 🚀 **Next Steps**

### **After Level 6:**
1. **CI/CD Integration** - Jenkins, GitHub Actions
2. **Parallel Execution** - JUnit 5 parallel tests
3. **Docker Integration** - Containerized testing
4. **Cloud Testing** - BrowserStack, Sauce Labs
5. **API Testing** - REST Assured with JUnit 5
6. **Mobile Testing** - Appium with JUnit 5

---

## 📞 **Support**

### **Common Issues:**

**1. CSV file not found:**
```
Solution: Place CSV in src/main/resources/testdata/
Path must be: /testdata/login-data.csv (with leading /)
```

**2. Excel file not found:**
```
Solution: Create Excel file with .xlsx extension
Place in src/main/resources/testdata/
First row must be headers
```

**3. Database connection failed:**
```
Solution: 
- For H2: No setup needed, auto-initialized
- For PostgreSQL: Check database is running
- Verify connection details
```

**4. Tests not running:**
```
Solution:
mvn clean test (clean before test)
Check @Test and @ParameterizedTest annotations
Verify data source files exist
```

---

## 🎉 **Congratulations!**

You've mastered **production-grade data-driven testing** with:
- ✅ CSV files
- ✅ Excel spreadsheets
- ✅ Database connections
- ✅ JUnit 5 parameterized tests
- ✅ ExtentReports
- ✅ Enterprise patterns

**You can now build industrial-strength test automation frameworks!** 🚀

---

**Happy Testing!** 🎓
