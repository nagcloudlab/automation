# 🎭 Playwright Level 7 - Reports & Screenshots

## Professional Test Reporting with Visual Evidence

**Level:** Advanced  
**Duration:** 3-4 hours  
**Prerequisites:** Playwright Levels 1-6 completed  

---

## 🎯 **Why Test Reporting?**

### **Without Reports:**
```
❌ No execution history
❌ Can't share results
❌ Hard to debug failures
❌ No visual evidence
❌ Team doesn't know status
❌ Stakeholders in the dark
```

### **With Professional Reports:**
```
✅ Beautiful HTML reports
✅ Screenshots embedded
✅ Easy to share
✅ Quick debugging
✅ Team visibility
✅ Stakeholder confidence
```

---

## 📦 **What's Included**

```
playwright-level7/
├── pom.xml (Extent Reports, Allure)
├── README.md
└── src/
    ├── main/java/com/npci/training/
    │   ├── reporting/
    │   │   └── ExtentReportsManager.java    # Report manager
    │   ├── utils/
    │   │   └── ScreenshotUtils.java         # Screenshot utility
    │   └── listeners/
    │       └── TestListener.java            # Auto screenshot on failure
    └── test/java/com/npci/training/
        ├── tests/BaseTest.java
        └── level7/
            └── Test01_ExtentReportsScreenshots.java  # 5 tests
```

**Total:** 5 comprehensive tests demonstrating professional reporting

---

## 🔥 **Extent Reports - The Best!**

### **Features:**
- ✅ Beautiful HTML reports
- ✅ Dark/Light themes
- ✅ Screenshots embedded
- ✅ Test hierarchy
- ✅ Filters & search
- ✅ Timeline view
- ✅ Export options
- ✅ Easy to share

### **Setup:**

```java
// 1. Initialize (in @BeforeAll)
@BeforeAll
static void setup() {
    ExtentReportsManager.initReports();
}

// 2. Create test (in @BeforeEach)
@BeforeEach
void setUp(TestInfo testInfo) {
    ExtentReportsManager.createTest(
        testInfo.getDisplayName(),
        "Test description"
    );
}

// 3. Log steps
@Test
void testLogin() {
    ExtentReportsManager.logInfo("Navigate to login");
    page.navigate("/login");
    
    ExtentReportsManager.logInfo("Enter credentials");
    page.fill("#username", "admin");
    page.fill("#password", "pass");
    
    ExtentReportsManager.logPass("Login successful");
}

// 4. Flush reports (in @AfterAll)
@AfterAll
static void teardown() {
    ExtentReportsManager.flushReports();
}
```

### **Report Output:**

```
reports/
└── ExtentReport_2024-12-17_10-30-45.html
```

Open in browser → Beautiful interactive report!

---

## 📸 **Screenshot Utilities**

### **Features:**
- ✅ Full page screenshots
- ✅ Element screenshots
- ✅ Base64 encoding
- ✅ Auto naming with timestamp
- ✅ PNG/JPEG formats
- ✅ Cleanup old screenshots

### **Usage:**

```java
// 1. Full page screenshot
String path = ScreenshotUtils.takeScreenshot(page, "login_page");

// 2. Element screenshot
Locator error = page.locator(".error-message");
ScreenshotUtils.takeElementScreenshot(error, "error_msg");

// 3. Base64 for reports
String base64 = ScreenshotUtils.takeScreenshotBase64(page);
ExtentReportsManager.attachScreenshotBase64(base64, "Login Page");

// 4. Screenshot on failure
try {
    // Test logic
} catch (Exception e) {
    ScreenshotUtils.takeScreenshotOnFailure(page, testName);
    throw e;
}
```

### **Screenshot Location:**

```
screenshots/
├── login_page_20241217_103045_123.png
├── dashboard_20241217_103046_456.png
└── FAILED_test_transfer_20241217_103050_789.png
```

---

## 🎯 **Automatic Screenshot on Failure**

### **How It Works:**

```java
@ExtendWith(TestListener.class)
public class BaseTest {
    
    protected boolean testFailed = false;
    
    @AfterEach
    public void tearDown(TestInfo testInfo) {
        // Automatic screenshot on failure
        if (testFailed && page != null) {
            // Save to file
            String path = ScreenshotUtils.takeScreenshotOnFailure(
                page, testInfo.getDisplayName());
            
            // Attach to report
            String base64 = ScreenshotUtils.takeScreenshotBase64(page);
            ExtentReportsManager.attachScreenshotBase64(
                base64, "Failure Screenshot");
        }
    }
}
```

### **In Your Test:**

```java
@Test
void testTransfer() {
    try {
        // Test logic
        page.navigate("/transfer");
        page.fill("#amount", "5000");
        page.click("#submit");
        
        assertThat(page.getByText("Success")).isVisible();
    } catch (Exception e) {
        markTestFailed();  // Triggers screenshot!
        throw e;
    }
}
```

**Result:** Automatic screenshot + attached to report! 📸

---

## 💰 **Complete Banking Example**

```java
@Test
void testBankingFlow() {
    // Step 1: Login
    logStep("Step 1: Navigate to banking portal");
    page.navigate("https://bank.com/login");
    logWithScreenshot("Login page loaded");
    
    logStep("Step 2: Enter credentials");
    page.fill("#username", "rajesh.kumar");
    page.fill("#password", "SecurePass123!");
    logWithScreenshot("Credentials entered");
    
    logStep("Step 3: Click login");
    page.click("#login");
    logWithScreenshot("After login");
    
    logStep("Step 4: Verify dashboard");
    assertThat(page.getByRole(AriaRole.HEADING, 
        setName("Dashboard"))).isVisible();
    ExtentReportsManager.logPass("✅ Login successful");
    logWithScreenshot("Dashboard loaded");
    
    // Step 2: Transfer
    logStep("Step 5: Navigate to transfer");
    page.click("text=Transfer");
    logWithScreenshot("Transfer page");
    
    logStep("Step 6: Enter transfer details");
    page.selectOption("#from", "savings");
    page.fill("#to", "9876543210");
    page.fill("#amount", "5000");
    logWithScreenshot("Transfer details entered");
    
    logStep("Step 7: Submit transfer");
    page.click("#submit");
    logWithScreenshot("After submit");
    
    logStep("Step 8: Verify success");
    assertThat(page.getByText("Transfer successful")).isVisible();
    ExtentReportsManager.logPass("✅ Transfer successful");
    logWithScreenshot("Transfer confirmed");
    
    ExtentReportsManager.logPass("✅ Complete banking flow passed");
}
```

### **Report Output:**

```
Test: Banking Flow
├── Step 1: Navigate to portal [Screenshot]
├── Step 2: Enter credentials [Screenshot]
├── Step 3: Click login [Screenshot]
├── Step 4: Verify dashboard ✅ [Screenshot]
├── Step 5: Navigate to transfer [Screenshot]
├── Step 6: Enter details [Screenshot]
├── Step 7: Submit transfer [Screenshot]
└── Step 8: Verify success ✅ [Screenshot]

Status: PASSED ✅
Duration: 5.2s
Screenshots: 8
```

---

## 📊 **Report Features**

### **Dashboard:**
```
Total Tests: 25
Passed: 23 (92%)
Failed: 2 (8%)
Skipped: 0
Duration: 2m 15s
Environment: QA
Browser: Chromium
```

### **Test Details:**
- ✅ Test name & status
- ✅ Duration
- ✅ Steps with logs
- ✅ Screenshots (embedded)
- ✅ Error messages
- ✅ Stack traces

### **Navigation:**
- 🔍 Search tests
- 🎯 Filter by status (Pass/Fail/Skip)
- 📅 Timeline view
- 🗂️ Category view
- 🌓 Dark/Light theme
- 📤 Export options

---

## ✅ **Best Practices**

### **Logging:**
1. ✅ Log every major step
2. ✅ Use proper log levels (Info/Pass/Fail/Warning)
3. ✅ Clear step descriptions
4. ✅ Don't log too much
5. ✅ Consistent naming

### **Screenshots:**
1. ✅ Screenshot important states
2. ✅ Before and after actions
3. ✅ On verification points
4. ✅ Always on failure
5. ✅ Don't screenshot everything (slow)

### **Reports:**
1. ✅ One report per test run
2. ✅ Include system info
3. ✅ Timestamp in filename
4. ✅ Clean old reports
5. ✅ Share with team

### **Organization:**
```
reports/
├── ExtentReport_2024-12-17_10-30-45.html
├── ExtentReport_2024-12-17_14-20-30.html
└── ExtentReport_2024-12-17_16-45-15.html

screenshots/
├── 2024-12-17/
│   ├── login_page_103045_123.png
│   ├── dashboard_103046_456.png
│   └── FAILED_transfer_103050_789.png
```

---

## 🚀 **Quick Start**

```bash
# Extract
unzip playwright-level7.zip
cd playwright-level7

# Run tests
mvn test

# View report
# Open: reports/ExtentReport_*.html
```

---

## 💯 **What You'll Master**

**Reporting:**
- [x] Extent Reports setup
- [x] Test logging
- [x] Report customization
- [x] Report sharing

**Screenshots:**
- [x] Full page screenshots
- [x] Element screenshots
- [x] Base64 encoding
- [x] Auto capture on failure

**Integration:**
- [x] Screenshots in reports
- [x] Test listeners
- [x] Automatic failure handling
- [x] Professional presentation

---

## 🎉 **Benefits**

### **For You:**
- ✅ Quick debugging (screenshots)
- ✅ Visual evidence
- ✅ Professional reports
- ✅ Easy sharing

### **For Team:**
- ✅ Test execution visibility
- ✅ Failure analysis
- ✅ Progress tracking
- ✅ Quality metrics

### **For Stakeholders:**
- ✅ Confidence in testing
- ✅ Easy to understand
- ✅ Visual proof
- ✅ Professional presentation

---

## 📈 **Impact**

### **Debugging Time:**
```
Without screenshots: 30 minutes (reproduce issue)
With screenshots:    30 seconds (see the issue!)

Improvement: 60x faster! ⚡
```

### **Communication:**
```
Without reports: "Trust me, tests passed"
With reports:    "Here's the proof!" (HTML + screenshots)

Result: Team confidence! 🎯
```

### **Professionalism:**
```
Without: Amateur (console output)
With:    Professional (HTML reports)

Result: Stakeholder trust! 🏆
```

---

## 🎯 **Real-World Usage**

### **Daily Testing:**
```bash
# Morning: Run regression
mvn test

# Review: Open report
open reports/ExtentReport_*.html

# Share: Email HTML file to team
```

### **CI/CD Pipeline:**
```yaml
# Jenkins/GitHub Actions
- name: Run tests
  run: mvn test
  
- name: Archive reports
  uses: actions/upload-artifact@v2
  with:
    name: test-reports
    path: reports/

- name: Email on failure
  if: failure()
  run: send-email-with-report
```

### **Sprint Demo:**
```
"Here's our test automation report:
- 95% pass rate
- 250 tests in 5 minutes
- All with screenshots
- Professional presentation"

Stakeholders: 😍
```

---

**Congratulations!** You now know professional test reporting! 🎭

**Happy Reporting!** 🚀
