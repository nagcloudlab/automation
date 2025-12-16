package com.npci.training;


import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 5 (JUnit) - Test 01: JUnit 5 Tags & Organization
 *
 * Topics Covered:
 * - @Tag annotation (like TestNG groups)
 * - @DisplayName for readable test names
 * - @Order for test execution order
 * - @Disabled to skip tests
 * - @Nested for test organization
 * - Test lifecycle annotations
 *
 * Duration: 25 minutes
 */
@DisplayName("JUnit 5 Tags and Organization Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginPageTest extends BaseTest {

    @Test
    @Order(1)
    @Tag("smoke")
    @Tag("login")
    @DisplayName("Login page should load successfully")
    public void test01_LoginPageLoads() {
        System.out.println("\n=== Test 01: Login Page Loads ===");
        System.out.println("Tags: [smoke, login]");

        LoginPage loginPage = new LoginPage(driver).open();

        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Login page displayed");

        String heading = loginPage.getHeading();
        assertTrue(heading.contains("Banking Portal"));
        System.out.println("✓ Correct heading: " + heading);
    }

    @Test
    @Order(2)
    @Tag("smoke")
    @Tag("login")
    @Tag("critical")
    @DisplayName("Should login successfully with valid credentials")
    public void test02_SuccessfulLogin() {
        System.out.println("\n=== Test 02: Successful Login ===");
        System.out.println("Tags: [smoke, login, critical]");

        DashboardPage dashboard = new LoginPage(driver)
                .open()
                .loginAs("admin", "admin123", "Customer");

        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("✓ Successfully logged in");

        String welcome = dashboard.getWelcomeMessage();
        assertNotNull(welcome);
        System.out.println("✓ Welcome message: " + welcome);
    }

    @Test
    @Order(3)
    @Tag("regression")
    @Tag("login")
    @DisplayName("Should show validation errors for empty fields")
    public void test03_InvalidLogin() {
        System.out.println("\n=== Test 03: Invalid Login ===");
        System.out.println("Tags: [regression, login]");

        LoginPage loginPage = new LoginPage(driver)
                .open()
                .clickLoginExpectingError();

        assertTrue(loginPage.isUsernameErrorDisplayed());
        System.out.println("✓ Username error displayed");

        assertTrue(loginPage.isPasswordErrorDisplayed());
        System.out.println("✓ Password error displayed");
    }

    @Test
    @Order(4)
    @Tag("smoke")
    @Tag("navigation")
    @DisplayName("Should navigate to register page")
    public void test04_NavigateToRegister() {
        System.out.println("\n=== Test 04: Navigate to Register ===");
        System.out.println("Tags: [smoke, navigation]");

        var registerPage = new LoginPage(driver)
                .open()
                .clickRegister();

        assertTrue(registerPage.isRegisterPageDisplayed());
        System.out.println("✓ Register page displayed");
    }

    @Test
    @Order(5)
    @Tag("smoke")
    @Tag("critical")
    @DisplayName("Complete login and logout flow")
    public void test05_LoginLogoutFlow() {
        System.out.println("\n=== Test 05: Login-Logout Flow ===");
        System.out.println("Tags: [smoke, critical]");

        // Login
        DashboardPage dashboard = new LoginPage(driver)
                .open()
                .loginAs("admin", "admin123", "Customer");

        assertTrue(dashboard.isDashboardPageDisplayed());
        System.out.println("✓ Logged in");

        // Logout
        LoginPage loginPage = dashboard.logout();


        assertTrue(loginPage.isLoginPageDisplayed());
        System.out.println("✓ Logged out");
    }

    @Test
    @Disabled("Intentionally disabled test for demonstration")
    @DisplayName("This test is disabled")
    public void test06_DisabledTest() {
        // This test will not run
        System.out.println("This should not print!");
        fail("This test should not execute!");
    }

    @Test
    @Order(999)
    @Tag("cleanup")
    @DisplayName("Cleanup test - always runs last")
    public void test99_AlwaysRunLast() {
        System.out.println("\n=== Test 99: Always Runs Last ===");
        System.out.println("✓ This test has highest order number");
        System.out.println("✓ Tests execute in @Order sequence");
    }

    /**
     * Nested tests for better organization
     */
    @Nested
    @DisplayName("Dashboard Tests")
    class DashboardTests {

        @Test
        @Tag("regression")
        @Tag("dashboard")
        @DisplayName("Dashboard should display welcome message")
        public void testDashboardWelcomeMessage() {
            System.out.println("\n=== Nested: Dashboard Welcome ===");

            DashboardPage dashboard = new LoginPage(driver)
                    .open()
                    .loginAs("admin", "admin123", "Customer");

            String welcome = dashboard.getWelcomeMessage();
            assertNotNull(welcome);
            assertTrue(welcome.length() > 0);
            System.out.println("✓ Welcome message: " + welcome);
        }

        @Test
        @Tag("regression")
        @Tag("dashboard")
        @DisplayName("Dashboard should display transaction table")
        public void testDashboardTransactionTable() {
            System.out.println("\n=== Nested: Transaction Table ===");

            DashboardPage dashboard = new LoginPage(driver)
                    .open()
                    .loginAs("admin", "admin123", "Customer");

            assertTrue(dashboard.isTransactionTableDisplayed());
            System.out.println("✓ Transaction table displayed");
        }
    }

    /**
     * BeforeAll runs once before all tests in this class
     */
    @BeforeAll
    public static void setupTestData() {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║  Test01_JUnit5TagsAndOrganization - Setup   ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("BeforeAll: Setup test data for this class");
    }

    /**
     * AfterAll runs once after all tests in this class
     */
    @AfterAll
    public static void cleanupTestData() {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║  Test01_JUnit5TagsAndOrganization - Done    ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("AfterAll: Cleanup test data for this class");
    }
}

/*
 * JUNIT 5 TAGS (like TestNG Groups):
 *
 * 1. Run tests with specific tag:
 *    mvn test -Dgroups="smoke"
 *    mvn test -Dgroups="smoke | critical"  // OR
 *    mvn test -Dgroups="smoke & critical"  // AND
 *
 * 2. Exclude tags:
 *    mvn test -DexcludedGroups="regression"
 *
 * 3. In IDE:
 *    Right-click class → Run with Coverage
 *    Edit Configurations → Add tag expression
 *
 * 4. Maven Surefire Configuration:
 *    <configuration>
 *        <groups>smoke, critical</groups>
 *        <excludedGroups>slow</excludedGroups>
 *    </configuration>
 *
 * 5. Common Tags:
 *    - smoke: Quick essential tests
 *    - regression: All tests
 *    - critical: Must-pass tests
 *    - slow: Long-running tests
 *    - fast: Quick tests
 */
