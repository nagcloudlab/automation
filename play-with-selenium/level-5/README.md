# 🎓 Selenium Level 5 (JUnit 5) - Advanced JUnit Features & Test Management

## Master JUnit 5 for Production Test Suites

---

## 📚 What You'll Learn

### **Level 5 (JUnit 5) Topics (3-4 hours)**
1. ✅ JUnit 5 Architecture (Jupiter, Vintage, Platform)
2. ✅ @Tag Annotation - Organize tests (like TestNG groups)
3. ✅ @DisplayName - Readable test names
4. ✅ @ParameterizedTest - Data-driven testing
5. ✅ Conditional Execution - OS/Environment specific
6. ✅ Test Lifecycle - @BeforeAll, @BeforeEach, etc.
7. ✅ @Nested Tests - Better organization
8. ✅ TestInfo & TestReporter - Metadata & reporting

---

## 📦 Package Contents

```
selenium-level5-junit/
├── pom.xml
├── README.md (this file)
└── src/
    ├── main/java/com/npci/training/
    │   ├── pages/              # Page Objects (from Level 4)
    │   └── utils/              # Utilities
    └── test/java/com/npci/training/
        ├── tests/
        │   └── BaseTest.java   # JUnit 5 base class
        └── level5/
            ├── Test01_JUnit5TagsAndOrganization.java
            ├── Test02_JUnit5ParameterizedTests.java
            └── Test03_JUnit5ConditionalExecution.java
```

---

## 🎯 **JUnit 5 vs TestNG**

| Feature | JUnit 5 | TestNG |
|---------|---------|--------|
| **Annotations** | @Test, @BeforeEach | @Test, @BeforeMethod | ✅ Similar |
| **Test Groups** | @Tag | @Test(groups) | ✅ Both good |
| **Parameterized** | @ParameterizedTest | @DataProvider | ✅ Both powerful |
| **Conditional** | @EnabledIf, @DisabledIf | Limited | JUnit 5 ✅ |
| **Nested Tests** | @Nested | No | JUnit 5 ✅ |
| **Display Names** | @DisplayName | @Test(description) | ✅ Both good |
| **Dependencies** | No | @Test(dependsOnMethods) | TestNG ✅ |
| **Parallel** | Yes (with config) | Yes (easier) | TestNG ✅ |

**Verdict:** Both are excellent! Choose based on team preference.

---

## 🚀 Quick Start

### Prerequisites
```
✓ Completed Levels 1-4
✓ Java 11+, Maven installed
✓ Banking Portal running
```

### Run Your First JUnit 5 Test
```bash
# Extract and import
unzip selenium-level5-junit.zip

# Run all tests
mvn test

# Run specific class
mvn test -Dtest=Test01_JUnit5TagsAndOrganization

# Run tests with specific tag
mvn test -Dgroups="smoke"

# Run with tag expression
mvn test -Dgroups="smoke & critical"
```

---

## 📖 **JUnit 5 Core Features**

### **1. @Tag Annotation (Test Organization)**

```java
@Test
@Tag("smoke")
@Tag("login")
@Tag("critical")
public void testLogin() {
    // Test code
}
```

**Run specific tags:**
```bash
# Run smoke tests only
mvn test -Dgroups="smoke"

# Run multiple tags (OR)
mvn test -Dgroups="smoke | critical"

# Run multiple tags (AND)
mvn test -Dgroups="smoke & critical"

# Exclude tags
mvn test -DexcludedGroups="slow"
```

**Configure in pom.xml:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.2</version>
    <configuration>
        <groups>smoke, critical</groups>
        <excludedGroups>slow</excludedGroups>
    </configuration>
</plugin>
```

---

### **2. @ParameterizedTest (Data-Driven Testing)**

**Simple with @ValueSource:**
```java
@ParameterizedTest
@ValueSource(strings = {"admin", "user1", "merchant1"})
public void testUsernames(String username) {
    // Test runs 3 times with different usernames
}
```

**Multiple parameters with @CsvSource:**
```java
@ParameterizedTest
@CsvSource({
    "admin, admin123, Customer, success",
    "user1, user123, Customer, success",
    "wrong, wrong, Customer, fail"
})
public void testLogin(String user, String pass, String type, String expected) {
    // Test runs 3 times with different data
}
```

**CSV File with @CsvFileSource:**
```java
@ParameterizedTest
@CsvFileSource(resources = "/testdata/login-data.csv", numLinesToSkip = 1)
public void testWithCsvFile(String user, String pass, String type, String expected) {
    // Reads data from CSV file
}
```

**Complex data with @MethodSource:**
```java
@ParameterizedTest
@MethodSource("provideLoginData")
public void testWithMethod(String user, String pass, boolean shouldSucceed) {
    // Gets data from method
}

static Stream<Arguments> provideLoginData() {
    return Stream.of(
        Arguments.of("admin", "admin123", true),
        Arguments.of("wrong", "wrong", false)
    );
}
```

---

### **3. @DisplayName (Readable Test Names)**

```java
@Test
@DisplayName("Should login successfully with valid admin credentials")
public void testLogin() {
    // Clear, readable test name in reports
}

@ParameterizedTest(name = "Login test: user={0}, expected={3}")
@CsvSource({"admin, admin123, Customer, success"})
public void testParameterized(String u, String p, String t, String e) {
    // Parameterized test with custom display name
}
```

---

### **4. Test Lifecycle Annotations**

```java
public class MyTests extends BaseTest {
    
    @BeforeAll                  // Runs ONCE before all tests
    static void setupAll() {
        // One-time setup
    }
    
    @BeforeEach                 // Runs BEFORE each test
    void setupEach() {
        // Setup for each test
    }
    
    @Test                       // Your test
    void test1() { }
    
    @AfterEach                  // Runs AFTER each test
    void teardownEach() {
        // Cleanup for each test
    }
    
    @AfterAll                   // Runs ONCE after all tests
    static void teardownAll() {
        // One-time cleanup
    }
}
```

**Execution Order:**
```
@BeforeAll
  @BeforeEach
    @Test test1()
  @AfterEach
  @BeforeEach
    @Test test2()
  @AfterEach
@AfterAll
```

---

### **5. @Nested Tests (Organization)**

```java
public class LoginTests extends BaseTest {
    
    @Test
    void testLoginPage() {
        // Top-level test
    }
    
    @Nested
    @DisplayName("Valid Credentials Tests")
    class ValidCredentials {
        
        @Test
        void testAdmin() {
            // Nested test for admin
        }
        
        @Test
        void testUser() {
            // Nested test for user
        }
    }
    
    @Nested
    @DisplayName("Invalid Credentials Tests")
    class InvalidCredentials {
        
        @Test
        void testWrongPassword() {
            // Nested test
        }
    }
}
```

**Benefits:**
- Better test organization
- Grouped related tests
- Clear test reports
- Shared setup per group

---

### **6. Conditional Execution**

**OS-Specific:**
```java
@Test
@EnabledOnOs(OS.WINDOWS)
void testOnWindowsOnly() {
    // Runs only on Windows
}

@Test
@EnabledOnOs({OS.MAC, OS.LINUX})
void testOnMacLinuxOnly() {
    // Runs on Mac or Linux
}

@Test
@DisabledOnOs(OS.WINDOWS)
void testNotOnWindows() {
    // Runs on all except Windows
}
```

**Java Version:**
```java
@Test
@EnabledOnJre(JRE.JAVA_11)
void testOnJava11() {
    // Runs on Java 11 only
}
```

**System Properties:**
```java
@Test
@EnabledIfSystemProperty(named = "env", matches = "qa")
void testOnQA() {
    // Runs if -Denv=qa
}
```

**Assumptions:**
```java
@Test
void testWithAssumption() {
    String env = System.getProperty("env", "qa");
    
    // Skip test if not QA
    assumeTrue("qa".equals(env), "Test only runs in QA");
    
    // Test code here
}
```

---

### **7. TestInfo & TestReporter**

**TestInfo - Access test metadata:**
```java
@Test
void test(TestInfo testInfo) {
    System.out.println("Display Name: " + testInfo.getDisplayName());
    System.out.println("Tags: " + testInfo.getTags());
    System.out.println("Method: " + testInfo.getTestMethod().get().getName());
}
```

**TestReporter - Publish entries:**
```java
@Test
void test(TestReporter reporter) {
    reporter.publishEntry("Step 1", "Opening page");
    // ... test code ...
    reporter.publishEntry("Result", "Success");
}
```

---

### **8. @RepeatedTest**

```java
@RepeatedTest(3)
void testStability() {
    // Runs 3 times to check stability
}

@RepeatedTest(value = 5, name = "Repetition {currentRepetition} of {totalRepetitions}")
void testWithRepInfo(RepetitionInfo repInfo) {
    System.out.println("Repetition: " + repInfo.getCurrentRepetition());
}
```

---

## 💡 **Real-World Examples**

### **Example 1: Organized Test Suite**
```java
@DisplayName("Login Functionality Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTests extends BaseTest {
    
    @Test
    @Order(1)
    @Tag("smoke")
    @Tag("critical")
    @DisplayName("Login page should load")
    void testPageLoad() {
        // Test code
    }
    
    @ParameterizedTest
    @Order(2)
    @Tag("regression")
    @CsvSource({"admin,admin123,success", "wrong,wrong,fail"})
    @DisplayName("Login with various credentials")
    void testLogin(String user, String pass, String expected) {
        // Data-driven test
    }
}
```

### **Example 2: Environment-Specific Tests**
```java
@Nested
@DisplayName("QA Environment Tests")
class QATests {
    
    @Test
    void testInQA() {
        assumeTrue("qa".equals(System.getProperty("env")));
        // QA-specific test
    }
}

@Nested
@DisplayName("Production Smoke Tests")
class ProdTests {
    
    @Test
    void testInProd() {
        assumeTrue("prod".equals(System.getProperty("env")));
        // Prod-safe test
    }
}
```

---

## 🎯 **Run Commands**

```bash
# Run all tests
mvn test

# Run specific class
mvn test -Dtest=Test01_JUnit5TagsAndOrganization

# Run specific method
mvn test -Dtest=Test01_JUnit5TagsAndOrganization#test01_LoginPageLoads

# Run with tags
mvn test -Dgroups="smoke"
mvn test -Dgroups="smoke | critical"
mvn test -Dgroups="smoke & critical"

# Exclude tags
mvn test -DexcludedGroups="slow"

# With system properties
mvn test -Denv=qa
mvn test -Denv=prod

# In IDE (IntelliJ/Eclipse)
Right-click test class/method → Run
```

---

## ✅ **Best Practices**

### **DO's ✅**

1. **Use @DisplayName**
   ```java
   @DisplayName("Should login with valid credentials")
   ```

2. **Tag Your Tests**
   ```java
   @Tag("smoke")
   @Tag("critical")
   ```

3. **Use @ParameterizedTest**
   ```java
   @ParameterizedTest
   @CsvSource({...})
   ```

4. **Organize with @Nested**
   ```java
   @Nested
   class ValidLoginTests { }
   ```

5. **Add Conditional Execution**
   ```java
   @EnabledOnOs(OS.WINDOWS)
   ```

### **DON'Ts ❌**

1. **Don't Ignore Test Organization**
   - Use tags and nested classes

2. **Don't Hardcode Test Data**
   - Use @ParameterizedTest

3. **Don't Skip @DisplayName**
   - Makes reports readable

4. **Don't Mix JUnit 4 and JUnit 5**
   - Use one version

---

## 📊 **JUnit 5 vs TestNG Decision Guide**

### **Choose JUnit 5 If:**
- ✅ Modern Java project (11+)
- ✅ Need nested test organization
- ✅ Want conditional execution
- ✅ Standard Java ecosystem
- ✅ Better IDE integration

### **Choose TestNG If:**
- ✅ Need test dependencies
- ✅ Easier parallel execution
- ✅ Better XML configuration
- ✅ More mature framework
- ✅ Team already knows TestNG

**Both are excellent choices!**

---

## 💯 **After Level 5 (JUnit), You Can:**

- [ ] Organize large test suites with tags
- [ ] Write data-driven tests with @ParameterizedTest
- [ ] Use conditional execution
- [ ] Create nested test structures
- [ ] Add readable test names
- [ ] Run tests by tags
- [ ] Handle environment-specific tests
- [ ] Use test metadata
- [ ] **Build enterprise JUnit 5 frameworks!**

---

## 🚀 **Next Steps**

### **Continue with:**
- Level 6: Data-Driven Testing & ExtentReports
- CSV/Excel data providers
- Custom test listeners
- Beautiful HTML reports
- CI/CD integration

---

## 💯 **Level 5 (JUnit) Complete!**

**Congratulations! You've learned:**
- ✅ JUnit 5 architecture & features
- ✅ Test organization with tags
- ✅ Parameterized testing
- ✅ Conditional execution
- ✅ Test lifecycle management
- ✅ Nested test structures
- ✅ **Professional JUnit 5 test suites!**

**Total Tests:** 20+  
**Time Investment:** 3-4 hours  
**Skill Level:** Expert with JUnit 5  

---

**Master JUnit 5 and lead test automation with modern Java!** 🚀

```bash
mvn test
```

🎓 **Happy Testing!**
