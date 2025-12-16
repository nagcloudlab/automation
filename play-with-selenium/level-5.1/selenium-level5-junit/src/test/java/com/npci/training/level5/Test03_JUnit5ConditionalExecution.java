package com.npci.training.level5;

import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Level 5 (JUnit) - Test 03: Conditional Execution & Assumptions
 * 
 * Topics Covered:
 * - @EnabledOnOs, @DisabledOnOs
 * - @EnabledOnJre, @DisabledOnJre
 * - @EnabledIf, @DisabledIf
 * - Assumptions (assumeTrue, assumeFalse)
 * - TestInfo and TestReporter
 * - Conditional test execution
 * 
 * Duration: 20 minutes
 */
@DisplayName("Conditional Execution and Assumptions")
public class Test03_JUnit5ConditionalExecution extends BaseTest {
    
    @Test
    @Tag("smoke")
    @DisplayName("Basic login test - always runs")
    public void testBasicLogin() {
        System.out.println("\n=== Basic Login Test ===");
        
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("✓ Login successful");
    }
    
    /**
     * Test runs only on Windows
     */
    @Test
    @EnabledOnOs(OS.WINDOWS)
    @Tag("os-specific")
    @DisplayName("Test enabled only on Windows")
    public void testOnWindowsOnly() {
        System.out.println("\n=== Windows Only Test ===");
        System.out.println("Running on: " + System.getProperty("os.name"));
        
        LoginPage loginPage = new LoginPage(driver).open();
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Test runs on Windows");
    }
    
    /**
     * Test runs only on Mac or Linux
     */
    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    @Tag("os-specific")
    @DisplayName("Test enabled only on Mac or Linux")
    public void testOnMacLinuxOnly() {
        System.out.println("\n=== Mac/Linux Only Test ===");
        System.out.println("Running on: " + System.getProperty("os.name"));
        
        LoginPage loginPage = new LoginPage(driver).open();
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Test runs on Mac/Linux");
    }
    
    /**
     * Test disabled on specific OS
     */
    @Test
    @DisabledOnOs(OS.WINDOWS)
    @Tag("os-specific")
    @DisplayName("Test disabled on Windows")
    public void testDisabledOnWindows() {
        System.out.println("\n=== Disabled on Windows ===");
        System.out.println("This test does NOT run on Windows");
        
        LoginPage loginPage = new LoginPage(driver).open();
        assertTrue(loginPage.isLoginPageDisplayed());
    }
    
    /**
     * Test with assumptions - skip if condition not met
     */
    @Test
    @Tag("assumption")
    @DisplayName("Test with assumption - check environment")
    public void testWithAssumption() {
        System.out.println("\n=== Test with Assumption ===");
        
        String env = System.getProperty("env", "qa");
        System.out.println("Environment: " + env);
        
        // Test only runs if env is "qa"
        assumeTrue("qa".equals(env), "Test only runs in QA environment");
        
        System.out.println("Assumption passed, running test...");
        
        DashboardPage dashboard = new LoginPage(driver)
            .open()
            .loginAs("admin", "admin123", "Customer");
        
        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("✓ Test completed");
    }
    
    /**
     * Test with assumeFalse
     */
    @Test
    @Tag("assumption")
    @DisplayName("Test skips in production")
    public void testSkipInProduction() {
        System.out.println("\n=== Skip in Production ===");
        
        String env = System.getProperty("env", "qa");
        System.out.println("Environment: " + env);
        
        // Skip if production
        assumeFalse("prod".equals(env), "Test skipped in production");
        
        System.out.println("Not production, running test...");
        
        LoginPage loginPage = new LoginPage(driver).open();
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Test completed");
    }
    
    /**
     * Test with TestInfo - access test metadata
     */
    @Test
    @Tag("metadata")
    @DisplayName("Test with TestInfo metadata")
    public void testWithTestInfo(TestInfo testInfo) {
        System.out.println("\n=== Test with TestInfo ===");
        System.out.println("Display Name: " + testInfo.getDisplayName());
        System.out.println("Test Method: " + testInfo.getTestMethod().get().getName());
        System.out.println("Tags: " + testInfo.getTags());
        System.out.println("Test Class: " + testInfo.getTestClass().get().getSimpleName());
        
        LoginPage loginPage = new LoginPage(driver).open();
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Test completed");
    }
    
    /**
     * Test with TestReporter - publish test entries
     */
    @Test
    @Tag("reporting")
    @DisplayName("Test with TestReporter")
    public void testWithTestReporter(TestReporter testReporter) {
        System.out.println("\n=== Test with TestReporter ===");
        
        testReporter.publishEntry("Step", "Opening login page");
        LoginPage loginPage = new LoginPage(driver).open();
        
        testReporter.publishEntry("Step", "Entering credentials");
        loginPage.enterUsername("admin");
        loginPage.enterPassword("admin123");
        
        testReporter.publishEntry("Step", "Logging in");
        loginPage.selectUserType("Customer");
        loginPage.acceptTerms();
        DashboardPage dashboard = loginPage.clickLogin();
        
        testReporter.publishEntry("Result", "Login successful");
        assertTrue(dashboard.isDashboardPageDisplayed());
        
        System.out.println("✓ Test completed with reporting");
    }
    
    /**
     * Nested tests with assumptions
     */
    @Nested
    @DisplayName("Environment-Specific Tests")
    class EnvironmentTests {
        
        @Test
        @DisplayName("QA Environment Test")
        public void testQAEnvironment() {
            String env = System.getProperty("env", "qa");
            assumeTrue("qa".equals(env));
            
            System.out.println("\n=== QA Specific Test ===");
            LoginPage loginPage = new LoginPage(driver).open();
            assertTrue(loginPage.isLoginPageDisplayed());
            System.out.println("✓ QA test completed");
        }
        
        @Test
        @DisplayName("Non-Production Test")
        public void testNonProduction() {
            String env = System.getProperty("env", "qa");
            assumeFalse("prod".equals(env));
            
            System.out.println("\n=== Non-Production Test ===");
            LoginPage loginPage = new LoginPage(driver).open();
            loginPage.enterUsername("testuser");
            assertTrue(loginPage.isLoginPageDisplayed());
            System.out.println("✓ Non-prod test completed");
        }
    }
    
    /**
     * RepeatedTest - Run test multiple times
     */
    @RepeatedTest(value = 3, name = "Repetition {currentRepetition} of {totalRepetitions}")
    @Tag("stability")
    @DisplayName("Repeated test for stability")
    public void testRepeated(RepetitionInfo repetitionInfo) {
        System.out.println("\n=== Repeated Test ===");
        System.out.println("Repetition: " + repetitionInfo.getCurrentRepetition() + 
                          " of " + repetitionInfo.getTotalRepetitions());
        
        LoginPage loginPage = new LoginPage(driver).open();
        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Repetition completed");
    }
}

/*
 * CONDITIONAL EXECUTION IN JUNIT 5:
 * 
 * 1. Operating System:
 *    @EnabledOnOs(OS.WINDOWS)
 *    @EnabledOnOs({OS.MAC, OS.LINUX})
 *    @DisabledOnOs(OS.WINDOWS)
 * 
 * 2. Java Version:
 *    @EnabledOnJre(JRE.JAVA_11)
 *    @DisabledOnJre(JRE.JAVA_8)
 * 
 * 3. System Properties:
 *    @EnabledIfSystemProperty(named = "env", matches = "qa")
 *    @DisabledIfSystemProperty(named = "env", matches = "prod")
 * 
 * 4. Environment Variables:
 *    @EnabledIfEnvironmentVariable(named = "ENV", matches = "QA")
 * 
 * 5. Assumptions:
 *    assumeTrue(condition)  - Continue if true
 *    assumeFalse(condition) - Continue if false
 *    assumingThat(condition, executable) - Conditional execution
 * 
 * RUN TESTS WITH PROPERTIES:
 * mvn test -Denv=qa
 * mvn test -Denv=prod
 * 
 * BENEFITS:
 * - Environment-specific tests
 * - OS-specific tests
 * - Conditional execution
 * - Test stability checks (repeated tests)
 * - Better test organization
 */
