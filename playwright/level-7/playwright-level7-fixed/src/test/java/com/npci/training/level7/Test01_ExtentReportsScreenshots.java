package com.npci.training.level7;

import com.microsoft.playwright.Locator;
import com.npci.training.reporting.ExtentReportsManager;
import com.npci.training.tests.BaseTest;
import com.npci.training.utils.ScreenshotUtils;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Level 7 - Test 01: Extent Reports with Screenshots
 * <p>
 * Topics:
 * - Extent Reports setup
 * - Screenshot on every step
 * - Screenshot on failure
 * - Beautiful HTML reports
 * - Test status tracking
 * <p>
 * Duration: 35 minutes
 */
@DisplayName("Extent Reports with Screenshots")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test01_ExtentReportsScreenshots extends BaseTest {

    @Test
    @Order(1)
    @DisplayName("Test 1: Basic reporting introduction")
    public void test01_ReportingIntro() {
        logStep("Starting reporting introduction");

        System.out.println("\n=== Test 01: Reporting Introduction ===");

        System.out.println("\n📊 TEST REPORTING:");
        System.out.println("""
                
                WHY REPORTING?
                ✅ Share results with team
                ✅ Track test execution history
                ✅ Identify failing tests quickly
                ✅ Screenshots for debugging
                ✅ Professional presentation
                ✅ Stakeholder communication
                
                WHAT'S INCLUDED IN REPORTS?
                1. Test execution status (Pass/Fail)
                2. Test duration
                3. Error messages
                4. Screenshots
                5. Logs and steps
                6. System information
                7. Test hierarchy
                8. Filters and search
                
                POPULAR REPORTING TOOLS:
                - Extent Reports (HTML, beautiful UI)
                - Allure Reports (Rich features)
                - TestNG Reports (Built-in)
                - Custom HTML Reports
                
                THIS LEVEL USES:
                ✅ Extent Reports (most popular)
                ✅ Beautiful HTML output
                ✅ Screenshots embedded
                ✅ Dark/Light themes
                ✅ Easy to customize
                """);

        logStep("Reporting introduction completed");
        ExtentReportsManager.logPass("Introduction test completed successfully");
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Logging test steps")
    public void test02_LoggingSteps() {
        System.out.println("\n=== Test 02: Logging Steps ===");

        // Navigate and log
        logStep("Navigate to The Internet Herokuapp");
        page.navigate("https://the-internet.herokuapp.com/");

        // Take screenshot
        logWithScreenshot("Home page loaded");

        // Click login and log
        logStep("Click on 'Form Authentication'");
        page.click("text=Form Authentication");

        // Take screenshot
        logWithScreenshot("Login page displayed");


        // Enter credentials and log
        logStep("Enter username: tomsmith");
        page.fill("#username", "tomsmith");

        logStep("Enter password");
        page.fill("#password", "SuperSecretPassword!");

        // Take screenshot before login
        logWithScreenshot("Credentials entered");

        // Login
        logStep("Click login button");
        page.click("button[type='submit']");

        // Verify and log
        logStep("Verify login successful");
        assertThat(page.locator(".flash.success")).isVisible();

        // Take screenshot of success
        logWithScreenshot("Login successful");

        ExtentReportsManager.logPass("All steps logged and verified successfully");

        System.out.println("\n💡 LOGGING STEPS:");
        System.out.println("""
                
                HOW TO LOG:
                
                1. SIMPLE LOG:
                   logStep("Navigate to login page");
                   page.navigate("/login");
                
                2. LOG WITH SCREENSHOT:
                   logWithScreenshot("Login page displayed");
                
                3. DIRECT EXTENT LOG:
                   ExtentReportsManager.logInfo("Step description");
                   ExtentReportsManager.logPass("Verification passed");
                   ExtentReportsManager.logFail("Test failed");
                
                4. LOG AND SCREENSHOT:
                   logStep("Enter username");
                   page.fill("#username", "admin");
                
                   String base64 = ScreenshotUtils.takeScreenshotBase64(page);
                   ExtentReportsManager.attachScreenshotBase64(base64, "Username entered");
                
                BENEFITS:
                ✅ Clear test flow in report
                ✅ Visual evidence with screenshots
                ✅ Easy debugging
                ✅ Stakeholder understanding
                """);
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Screenshot on failure (intentional)")
    public void test03_ScreenshotOnFailure() {
        System.out.println("\n=== Test 03: Screenshot on Failure ===");

        logStep("Navigate to login page");
        page.navigate("https://the-internet.herokuapp.com/login");
        logWithScreenshot("Login page loaded");

        logStep("Enter incorrect credentials");
        page.fill("#username", "wronguser");
        page.fill("#password", "wrongpass");
        logWithScreenshot("Wrong credentials entered");

        logStep("Click login button");
        page.click("button[type='submit']");

        try {
            logStep("Verify login successful (will fail)");
            assertThat(page.locator(".flash.success")).isVisible();
            ExtentReportsManager.logPass("Login successful");
        } catch (AssertionError e) {
            markTestFailed();  // Trigger screenshot in @AfterEach
            ExtentReportsManager.logFail("Login failed as expected: " + e.getMessage());

            // Manual screenshot
            logWithScreenshot("Login failed - Error message displayed");

            System.out.println("\n💡 SCREENSHOT ON FAILURE:");
            System.out.println("""
                    
                    AUTOMATIC SCREENSHOT:
                    - Captured in @AfterEach if testFailed = true
                    - Attached to Extent Report
                    - Saved to screenshots folder
                    
                    HOW IT WORKS:
                    
                    1. In @AfterEach:
                       if (testFailed && page != null) {
                           String path = ScreenshotUtils.takeScreenshotOnFailure(
                               page, testInfo.getDisplayName());
                    
                           String base64 = ScreenshotUtils.takeScreenshotBase64(page);
                           ExtentReportsManager.attachScreenshotBase64(base64, "Failure");
                       }
                    
                    2. Mark test as failed:
                       try {
                           // Test logic
                       } catch (Exception e) {
                           markTestFailed();
                           throw e;
                       }
                    
                    BENEFITS:
                    ✅ Automatic failure screenshots
                    ✅ No manual screenshot code
                    ✅ Attached to reports
                    ✅ Easy debugging
                    ✅ Visual evidence
                    """);

            // Don't rethrow - we want to demonstrate the feature
            return;
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Complete banking flow with reporting")
    public void test04_BankingFlowWithReporting() {
        System.out.println("\n=== Test 04: Banking Flow with Reporting ===");

        // Login
        logStep("Step 1: Navigate to banking portal");
        page.navigate("https://the-internet.herokuapp.com/login");
        logWithScreenshot("Banking portal home page");

        logStep("Step 2: Enter username");
        page.fill("#username", "tomsmith");
        ExtentReportsManager.logInfo("Username: tomsmith");

        logStep("Step 3: Enter password");
        page.fill("#password", "SuperSecretPassword!");
        logWithScreenshot("Credentials entered");

        logStep("Step 4: Click login button");
        page.click("button[type='submit']");
        logWithScreenshot("After login click");

        logStep("Step 5: Verify login successful");
        assertThat(page.locator(".flash.success")).isVisible();
        ExtentReportsManager.logPass("✅ Login successful");
        logWithScreenshot("Dashboard loaded successfully");

        logStep("Step 6: Click logout");
        page.click("a[href='/logout']");
        logWithScreenshot("After logout");

        logStep("Step 7: Verify logout successful");
        assertThat(page.locator(".flash.success")).isVisible();
        ExtentReportsManager.logPass("✅ Logout successful");
        logWithScreenshot("Logged out successfully");

        ExtentReportsManager.logPass("✅ Complete banking flow executed successfully");

        System.out.println("\n💡 COMPLETE TEST REPORTING:");
        System.out.println("""
                
                BEST PRACTICES:
                
                1. LOG EVERY MAJOR STEP:
                   logStep("Navigate to dashboard");
                   logStep("Click transfer button");
                   logStep("Enter amount");
                
                2. SCREENSHOT IMPORTANT STATES:
                   - Before action
                   - After action
                   - On verification
                   - On failure
                
                3. USE PROPER LOG LEVELS:
                   ExtentReportsManager.logInfo("Navigation");
                   ExtentReportsManager.logPass("Verification passed");
                   ExtentReportsManager.logFail("Test failed");
                   ExtentReportsManager.logWarning("Warning message");
                
                4. CLEAR STEP DESCRIPTIONS:
                   ✅ "Step 1: Navigate to login page"
                   ✅ "Step 2: Enter username: admin"
                   ❌ "Do something"
                   ❌ "Test step"
                
                5. ATTACH SCREENSHOTS:
                   - Key checkpoints
                   - Before/after actions
                   - Failure states
                   - Success states
                
                REPORT STRUCTURE:
                
                Test: Banking Login Flow
                ├── Step 1: Navigate to portal [Screenshot]
                ├── Step 2: Enter username [Log]
                ├── Step 3: Enter password [Screenshot]
                ├── Step 4: Click login [Log]
                ├── Step 5: Verify success [Screenshot]
                └── Status: PASSED ✅
                
                Duration: 3.5s
                Screenshots: 5
                """);
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Report features demonstration")
    public void test05_ReportFeatures() {
        System.out.println("\n=== Test 05: Report Features ===");

        System.out.println("\n📊 EXTENT REPORTS FEATURES:");
        System.out.println("""
                
                WHAT'S IN THE REPORT?
                
                1. DASHBOARD:
                   - Total tests
                   - Pass/Fail/Skip count
                   - Pass percentage
                   - Execution time
                   - Environment info
                
                2. TEST DETAILS:
                   - Test name
                   - Test status
                   - Duration
                   - Steps/Logs
                   - Screenshots
                   - Error messages
                   - Stack traces
                
                3. SYSTEM INFO:
                   - Application name
                   - Environment (QA/UAT/Prod)
                   - Browser
                   - OS
                   - Java version
                   - User
                
                4. FEATURES:
                   - Search tests
                   - Filter by status
                   - Timeline view
                   - Category view
                   - Export options
                   - Dark/Light theme
                
                5. SCREENSHOTS:
                   - Embedded in report
                   - Click to enlarge
                   - Multiple per test
                   - Failure screenshots
                   - Step screenshots
                
                HOW TO VIEW REPORT:
                
                1. Run tests:
                   mvn test
                
                2. Report generated at:
                   reports/ExtentReport_2024-12-17_10-30-45.html
                
                3. Open in browser:
                   - Double click HTML file
                   - OR right-click → Open with browser
                
                4. Navigate report:
                   - Click tests to see details
                   - View screenshots
                   - Check logs
                   - Filter by status
                
                CUSTOMIZATION:
                
                1. Change theme:
                   sparkReporter.config().setTheme(Theme.DARK);
                   sparkReporter.config().setTheme(Theme.STANDARD);
                
                2. Add custom info:
                   extent.setSystemInfo("Sprint", "Sprint 23");
                   extent.setSystemInfo("Build", "1.2.3");
                
                3. Custom report name:
                   String name = "BankingTests_" + timestamp + ".html";
                
                4. Custom CSS:
                   sparkReporter.config().setCss("custom.css");
                
                SHARING REPORTS:
                
                1. Email:
                   - Attach HTML file
                   - All screenshots embedded
                
                2. Shared drive:
                   - Copy to network location
                   - Team can access
                
                3. CI/CD:
                   - Archive in Jenkins
                   - Publish as artifact
                   - Email on failure
                
                4. Cloud storage:
                   - Upload to S3/GCS
                   - Share link
                """);

        ExtentReportsManager.logPass("Report features demonstration completed");
    }
}

/*
 * EXTENT REPORTS SUMMARY:
 *
 * 1. SETUP:
 *    - Initialize in @BeforeAll
 *    - Create test in @BeforeEach
 *    - Flush in @AfterAll
 *
 * 2. LOGGING:
 *    - logStep() for steps
 *    - logWithScreenshot() for visual steps
 *    - ExtentReportsManager methods for direct logging
 *
 * 3. SCREENSHOTS:
 *    - Automatic on failure
 *    - Manual on important steps
 *    - Base64 embedded in report
 *
 * 4. REPORT LOCATION:
 *    reports/ExtentReport_timestamp.html
 *
 * 5. BENEFITS:
 *    ✅ Beautiful HTML reports
 *    ✅ Screenshots embedded
 *    ✅ Easy to share
 *    ✅ Professional presentation
 *    ✅ Stakeholder friendly
 *
 * RUN COMMAND:
 * mvn test -Dtest=Test01_ExtentReportsScreenshots
 *
 * VIEW REPORT:
 * Open: reports/ExtentReport_*.html
 */
