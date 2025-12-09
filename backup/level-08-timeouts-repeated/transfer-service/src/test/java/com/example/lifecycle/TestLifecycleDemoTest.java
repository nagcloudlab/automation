package com.example.lifecycle;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Lifecycle Demo - LEVEL 6
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                  LIFECYCLE ANNOTATIONS DEMO                   ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This test class demonstrates:                                ║
 * ║  • @BeforeAll - Class-level setup (runs once)                 ║
 * ║  • @BeforeEach - Per-test setup                               ║
 * ║  • @AfterEach - Per-test cleanup                              ║
 * ║  • @AfterAll - Class-level cleanup (runs once)                ║
 * ║  • Execution order visualization                              ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * Run this test and observe the console output to understand
 * the lifecycle execution order.
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Test Lifecycle Demo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestLifecycleDemoTest {

    // ═══════════════════════════════════════════════════════════
    // STATIC RESOURCES (shared across all tests)
    // ═══════════════════════════════════════════════════════════

    /**
     * Static counter to track @BeforeAll/@AfterAll calls.
     * In real tests, this would be expensive resources like DB connections.
     */
    private static int classSetupCount = 0;
    private static int testExecutionCount = 0;

    // ═══════════════════════════════════════════════════════════
    // INSTANCE RESOURCES (fresh for each test)
    // ═══════════════════════════════════════════════════════════

    private InMemoryAccountRepository repository;
    private UPITransferService service;
    private Account testAccount;
    private int instanceSetupCount = 0;

    // ═══════════════════════════════════════════════════════════
    // CLASS-LEVEL LIFECYCLE (@BeforeAll / @AfterAll)
    // ═══════════════════════════════════════════════════════════

    /**
     * @BeforeAll - Runs ONCE before any test method.
     * 
     * MUST be static (unless using @TestInstance(PER_CLASS)).
     * 
     * Use for:
     * - Database connections
     * - Loading configuration files
     * - Starting embedded servers
     * - One-time expensive setup
     */
    @BeforeAll
    static void setUpClass() {
        classSetupCount++;
        System.out.println("\n" + "═".repeat(60));
        System.out.println("@BeforeAll - Class setup (runs ONCE)");
        System.out.println("  → classSetupCount: " + classSetupCount);
        System.out.println("  → Simulating: Opening database connection...");
        System.out.println("═".repeat(60));
    }

    /**
     * @AfterAll - Runs ONCE after all test methods complete.
     * 
     * MUST be static (unless using @TestInstance(PER_CLASS)).
     * 
     * Use for:
     * - Closing database connections
     * - Stopping servers
     * - Final resource cleanup
     */
    @AfterAll
    static void tearDownClass() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("@AfterAll - Class cleanup (runs ONCE)");
        System.out.println("  → Total tests executed: " + testExecutionCount);
        System.out.println("  → Simulating: Closing database connection...");
        System.out.println("═".repeat(60) + "\n");
    }

    // ═══════════════════════════════════════════════════════════
    // METHOD-LEVEL LIFECYCLE (@BeforeEach / @AfterEach)
    // ═══════════════════════════════════════════════════════════

    /**
     * @BeforeEach - Runs before EACH test method.
     * 
     * Use for:
     * - Creating fresh test objects
     * - Resetting state
     * - Setting up test data
     * - Initializing mocks
     */
    @BeforeEach
    void setUp() {
        instanceSetupCount++;
        testExecutionCount++;
        
        System.out.println("\n  ┌─ @BeforeEach - Test setup (runs per test)");
        System.out.println("  │    → instanceSetupCount: " + instanceSetupCount);
        System.out.println("  │    → Creating fresh repository and service...");
        
        // Fresh instances for each test
        repository = new InMemoryAccountRepository();
        service = new UPITransferService(repository);
        testAccount = new Account("TEST001", "Test User", 10000.0);
        repository.addAccount(testAccount);
        
        System.out.println("  │    → Test account balance: ₹" + testAccount.getBalance());
    }

    /**
     * @AfterEach - Runs after EACH test method.
     * 
     * Use for:
     * - Cleaning up test data
     * - Resetting state
     * - Logging test completion
     * - Releasing per-test resources
     */
    @AfterEach
    void tearDown() {
        System.out.println("  │    → Final account balance: ₹" + 
            (testAccount != null ? testAccount.getBalance() : "N/A"));
        System.out.println("  └─ @AfterEach - Test cleanup complete");
    }

    // ═══════════════════════════════════════════════════════════
    // TEST METHODS
    // ═══════════════════════════════════════════════════════════

    @Test
    @Order(1)
    @DisplayName("Test 1: Verify fresh state")
    void test1_verifyFreshState() {
        System.out.println("  │");
        System.out.println("  ├─ @Test: test1_verifyFreshState");
        System.out.println("  │    → Account balance should be 10000 (fresh)");
        
        assertEquals(10000.0, testAccount.getBalance(),
            "Each test should start with fresh account");
        assertEquals(1, instanceSetupCount,
            "This is the first @BeforeEach for this test instance");
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Modify and verify")
    void test2_modifyAndVerify() {
        System.out.println("  │");
        System.out.println("  ├─ @Test: test2_modifyAndVerify");
        System.out.println("  │    → Debiting ₹3000 from account...");
        
        // Modify the account
        testAccount.debit(3000.0);
        
        assertEquals(7000.0, testAccount.getBalance());
        
        // Note: instanceSetupCount is 1 again because this is a NEW instance
        // (default lifecycle is PER_METHOD)
        System.out.println("  │    → instanceSetupCount is: " + instanceSetupCount);
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: Confirm isolation")
    void test3_confirmIsolation() {
        System.out.println("  │");
        System.out.println("  ├─ @Test: test3_confirmIsolation");
        System.out.println("  │    → Verifying test2's changes didn't affect us");
        
        // Even though test2 debited 3000, we have fresh account
        assertEquals(10000.0, testAccount.getBalance(),
            "Test isolation: test2's debit should not affect test3");
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: Multiple operations")
    void test4_multipleOperations() {
        System.out.println("  │");
        System.out.println("  ├─ @Test: test4_multipleOperations");
        
        // Add another account
        Account receiver = new Account("TEST002", "Receiver", 5000.0);
        repository.addAccount(receiver);
        
        System.out.println("  │    → Added receiver with ₹5000");
        System.out.println("  │    → Transferring ₹2500...");
        
        // Transfer
        service.transfer("TEST001", "TEST002", 2500.0);
        
        assertEquals(7500.0, repository.loadAccountById("TEST001").getBalance());
        assertEquals(7500.0, repository.loadAccountById("TEST002").getBalance());
        
        System.out.println("  │    → Transfer complete. Both accounts: ₹7500");
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: Final isolation check")
    void test5_finalIsolationCheck() {
        System.out.println("  │");
        System.out.println("  ├─ @Test: test5_finalIsolationCheck");
        
        // Verify test4's receiver doesn't exist in our fresh repository
        assertFalse(repository.existsById("TEST002"),
            "TEST002 from test4 should not exist in fresh repository");
        
        // Only our setup account exists
        assertEquals(1, repository.getAccountCount());
        
        System.out.println("  │    → Repository has only 1 account (from @BeforeEach)");
        System.out.println("  │    → Test isolation confirmed!");
    }
}
