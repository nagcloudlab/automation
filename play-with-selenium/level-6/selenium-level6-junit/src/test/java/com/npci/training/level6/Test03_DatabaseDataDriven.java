package com.npci.training.level6;

import com.npci.training.pages.DashboardPage;
import com.npci.training.pages.LoginPage;
import com.npci.training.providers.DatabaseArgumentsProvider;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 6 (JUnit 5) - Test 03: Database Data-Driven Testing
 * 
 * Topics Covered:
 * - Reading test data from databases (PostgreSQL, H2)
 * - JDBC connection management
 * - HikariCP connection pooling
 * - @ArgumentsSource with database provider
 * - Production database testing patterns
 * 
 * Duration: 30 minutes
 */
@DisplayName("Database Data-Driven Testing")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test03_DatabaseDataDriven extends BaseTest {
    
    /**
     * Test using H2 in-memory database (for demo/testing)
     * This creates and populates database automatically
     */
    @ParameterizedTest(name = "H2 database test #{index}: user={0}, expected={3}")
    @ArgumentsSource(DatabaseArgumentsProvider.H2LoginDataProvider.class)
    @Order(1)
    @Tag("smoke")
    @Tag("database")
    @Tag("h2")
    @DisplayName("Login with H2 database data")
    public void testLoginWithH2Data(String username, String password, 
                                    String usertype, String expected) {
        System.out.println("\n=== H2 Database Test ===");
        System.out.println("Username: " + username);
        System.out.println("Expected: " + expected);
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        if ("success".equals(expected)) {
            if (!username.isEmpty() && !password.isEmpty() && !usertype.isEmpty()) {
                DashboardPage dashboard = loginPage.loginAs(username, password, usertype);
                assertTrue(dashboard.isDashboardPageDisplayed());
                System.out.println("✓ Login successful");
            }
        } else {
            if (!username.isEmpty()) loginPage.enterUsername(username);
            if (!password.isEmpty()) loginPage.enterPassword(password);
            if (!usertype.isEmpty()) loginPage.selectUserType(usertype);
            if (!username.isEmpty() || !password.isEmpty()) {
                loginPage.acceptTerms();
            }
            loginPage.clickLoginExpectingError();
            
            assertTrue(loginPage.isLoginPageDisplayed());
            System.out.println("✓ Login failed as expected");
        }
    }
    
    /**
     * Test using PostgreSQL database (requires PostgreSQL running)
     * Skip if database not available
     */
    @ParameterizedTest(name = "PostgreSQL test: user={0}")
    @ArgumentsSource(DatabaseArgumentsProvider.PostgreSQLLoginDataProvider.class)
    @Order(2)
    @Tag("integration")
    @Tag("database")
    @Tag("postgresql")
    @DisplayName("Login with PostgreSQL database data")
    @Disabled("Enable only when PostgreSQL is available")
    public void testLoginWithPostgreSQLData(String username, String password, 
                                           String usertype, String expected) {
        System.out.println("\n=== PostgreSQL Database Test ===");
        System.out.println("Username: " + username);
        
        LoginPage loginPage = new LoginPage(driver).open();
        
        if ("success".equals(expected)) {
            if (!username.isEmpty() && !password.isEmpty() && !usertype.isEmpty()) {
                DashboardPage dashboard = loginPage.loginAs(username, password, usertype);
                assertTrue(dashboard.isDashboardPageDisplayed());
                System.out.println("✓ Success");
            }
        } else {
            if (!username.isEmpty()) {
                loginPage.enterUsername(username)
                        .enterPassword(password);
                if (!usertype.isEmpty()) {
                    loginPage.selectUserType(usertype);
                }
                loginPage.acceptTerms()
                        .clickLoginExpectingError();
            } else {
                loginPage.clickLoginExpectingError();
            }
            assertTrue(loginPage.isLoginPageDisplayed());
            System.out.println("✓ Failed as expected");
        }
    }
    
    /**
     * Test database connectivity
     */
    @Test
    @Order(3)
    @Tag("verification")
    @DisplayName("Verify database connection and data")
    public void testDatabaseConnection() {
        System.out.println("\n=== Database Connection Test ===");
        
        // Test H2 in-memory database
        String jdbcUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        String username = "sa";
        String password = "";
        String query = "SELECT username, password, usertype, expected FROM login_test_data";
        
        // Initialize and query
        var provider = new DatabaseArgumentsProvider.H2LoginDataProvider();
        long count = provider.provideArguments(null).count();
        
        assertTrue(count > 0, "Database should have test data");
        System.out.println("✓ Database connected successfully");
        System.out.println("✓ Total test records: " + count);
        
        // Print data for verification
        DatabaseArgumentsProvider.printDatabaseData(jdbcUrl, username, password, query);
    }
    
    /**
     * Cleanup after all tests
     */
    @AfterAll
    public static void cleanupDatabase() {
        DatabaseArgumentsProvider.closeDataSource();
        System.out.println("✓ Database connections closed");
    }
    
    /**
     * Nested tests for database operations
     */
    @Nested
    @DisplayName("Database Operations")
    class DatabaseOperations {
        
        @Test
        @DisplayName("Verify H2 database initialization")
        public void testH2Initialization() {
            System.out.println("\n=== H2 Initialization Test ===");
            
            var provider = new DatabaseArgumentsProvider.H2LoginDataProvider();
            var data = provider.provideArguments(null);
            
            assertNotNull(data, "Data should not be null");
            assertTrue(data.count() > 0, "Should have test data");
            
            System.out.println("✓ H2 database initialized successfully");
        }
        
        @Test
        @DisplayName("Verify data structure")
        public void testDataStructure() {
            System.out.println("\n=== Data Structure Test ===");
            
            var provider = new DatabaseArgumentsProvider.H2LoginDataProvider();
            var firstRow = provider.provideArguments(null).findFirst();
            
            assertTrue(firstRow.isPresent(), "Should have at least one row");
            
            Object[] data = firstRow.get().get();
            assertEquals(4, data.length, "Each row should have 4 columns");
            
            System.out.println("✓ Data structure verified");
            System.out.println("✓ Columns: username, password, usertype, expected");
        }
    }
}

/*
 * DATABASE DATA-DRIVEN TESTING NOTES:
 * 
 * 1. Database Support:
 *    - PostgreSQL (production database)
 *    - H2 (in-memory database for testing)
 *    - MySQL (can be added easily)
 *    - Oracle (can be added easily)
 * 
 * 2. Connection Configuration:
 *    PostgreSQL:
 *    - URL: jdbc:postgresql://localhost:5432/testdata
 *    - Username/Password via System properties
 *    - mvn test -Ddb.url=... -Ddb.username=... -Ddb.password=...
 *    
 *    H2 (in-memory):
 *    - URL: jdbc:h2:mem:testdb
 *    - Automatically initialized with test data
 *    - No external database required
 * 
 * 3. Database Setup:
 *    CREATE TABLE login_test_data (
 *        id SERIAL PRIMARY KEY,
 *        username VARCHAR(100),
 *        password VARCHAR(100),
 *        usertype VARCHAR(50),
 *        expected VARCHAR(20)
 *    );
 *    
 *    INSERT INTO login_test_data VALUES
 *    ('admin', 'admin123', 'Customer', 'success'),
 *    ('user1', 'user123', 'Customer', 'success'),
 *    ...
 * 
 * 4. Benefits:
 *    - Centralized test data
 *    - Real production-like testing
 *    - Easy data updates
 *    - Team collaboration
 *    - Version control of schema
 * 
 * 5. Best Practices:
 *    - Use connection pooling (HikariCP)
 *    - Close connections properly
 *    - Use separate test database
 *    - Parameterize credentials
 *    - Handle connection failures gracefully
 * 
 * Run Commands:
 * mvn test -Dtest=Test03_DatabaseDataDriven
 * mvn test -Dgroups="database"
 * mvn test -Dgroups="h2"
 * 
 * With PostgreSQL:
 * mvn test -Dtest=Test03_DatabaseDataDriven \
 *   -Ddb.url=jdbc:postgresql://localhost:5432/testdata \
 *   -Ddb.username=postgres \
 *   -Ddb.password=yourpassword
 */
