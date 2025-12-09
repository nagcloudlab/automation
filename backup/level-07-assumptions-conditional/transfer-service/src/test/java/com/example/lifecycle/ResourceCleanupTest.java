package com.example.lifecycle;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Resource Cleanup Patterns - LEVEL 6
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                  RESOURCE CLEANUP PATTERNS                    ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Proper cleanup ensures:                                      ║
 * ║  • No resource leaks                                          ║
 * ║  • Test isolation                                             ║
 * ║  • Consistent test environment                                ║
 * ║  • Faster test execution                                      ║
 * ║                                                               ║
 * ║  Patterns demonstrated:                                       ║
 * ║  • @AfterEach for per-test cleanup                            ║
 * ║  • @AfterAll for class-level cleanup                          ║
 * ║  • Try-finally for critical cleanup                           ║
 * ║  • Tracking created resources                                 ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Resource Cleanup Patterns")
class ResourceCleanupTest {

    // ═══════════════════════════════════════════════════════════
    // BASIC CLEANUP PATTERN
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Basic Cleanup with @AfterEach")
    class BasicCleanupTest {

        private InMemoryAccountRepository repository;
        private List<String> createdAccountIds;

        @BeforeEach
        void setUp() {
            System.out.println("\n[BASIC] @BeforeEach - Setting up");
            repository = new InMemoryAccountRepository();
            createdAccountIds = new ArrayList<>();
        }

        @AfterEach
        void tearDown() {
            System.out.println("[BASIC] @AfterEach - Cleaning up");
            System.out.println("  → Accounts created: " + createdAccountIds.size());
            
            // Clean up all created accounts
            createdAccountIds.clear();
            repository.clear();
            
            System.out.println("  → Repository cleared");
        }

        @Test
        @DisplayName("Test creates accounts that need cleanup")
        void testCreatesAccounts() {
            Account acc1 = BankingTestFixtures.sender();
            Account acc2 = BankingTestFixtures.receiver();
            
            repository.addAccount(acc1);
            repository.addAccount(acc2);
            
            createdAccountIds.add(acc1.getAccountId());
            createdAccountIds.add(acc2.getAccountId());
            
            System.out.println("[BASIC] Created " + createdAccountIds.size() + " accounts");
            
            assertEquals(2, repository.getAccountCount());
        }

        @Test
        @DisplayName("Test starts with clean state")
        void testStartsClean() {
            // Previous test's accounts should not exist
            System.out.println("[BASIC] Checking clean state");
            
            assertTrue(repository.isEmpty(), 
                "Repository should be empty (cleanup worked)");
            assertTrue(createdAccountIds.isEmpty(), 
                "Account list should be empty");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // CLEANUP WITH RESOURCE TRACKING
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Resource Tracking Pattern")
    class ResourceTrackingTest {

        private InMemoryAccountRepository repository;
        private List<Account> trackedAccounts;
        private double totalCreatedBalance;

        @BeforeEach
        void setUp() {
            repository = new InMemoryAccountRepository();
            trackedAccounts = new ArrayList<>();
            totalCreatedBalance = 0;
        }

        /**
         * Helper to create and track accounts.
         */
        private Account createTrackedAccount(String id, String name, double balance) {
            Account account = new Account(id, name, balance);
            repository.addAccount(account);
            trackedAccounts.add(account);
            totalCreatedBalance += balance;
            return account;
        }

        @AfterEach
        void tearDown() {
            System.out.println("\n[TRACKING] Cleanup Report:");
            System.out.println("  → Accounts tracked: " + trackedAccounts.size());
            System.out.println("  → Total balance tracked: ₹" + totalCreatedBalance);
            
            // Log each account being cleaned up
            for (Account acc : trackedAccounts) {
                System.out.println("    - " + acc.getAccountId() + ": ₹" + acc.getBalance());
            }
            
            // Clear resources
            trackedAccounts.clear();
            repository.clear();
            totalCreatedBalance = 0;
        }

        @Test
        @DisplayName("Should track all created accounts")
        void shouldTrackAccounts() {
            createTrackedAccount("ACC001", "User 1", 10000.0);
            createTrackedAccount("ACC002", "User 2", 20000.0);
            createTrackedAccount("ACC003", "User 3", 30000.0);
            
            assertEquals(3, trackedAccounts.size());
            assertEquals(60000.0, totalCreatedBalance);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // CLEANUP EVEN ON FAILURE
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Cleanup on Test Failure")
    class CleanupOnFailureTest {

        private InMemoryAccountRepository repository;
        private boolean cleanupCalled;

        @BeforeEach
        void setUp() {
            repository = new InMemoryAccountRepository();
            cleanupCalled = false;
            repository.addAccount(BankingTestFixtures.sender());
        }

        @AfterEach
        void tearDown() {
            cleanupCalled = true;
            System.out.println("[FAILURE] @AfterEach called - cleanup runs even on failure!");
            repository.clear();
        }

        @Test
        @DisplayName("Passing test - cleanup runs")
        void passingTest() {
            assertTrue(repository.existsById("SENDER001"));
            // @AfterEach will run after this
        }

        // Uncomment to see cleanup on failure:
        // @Test
        // @DisplayName("Failing test - cleanup still runs")
        // void failingTest() {
        //     fail("Intentional failure - watch cleanup still run!");
        // }
    }

    // ═══════════════════════════════════════════════════════════
    // TRY-FINALLY FOR CRITICAL CLEANUP
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Try-Finally for Critical Cleanup")
    class TryFinallyCleanupTest {

        @Test
        @DisplayName("Should cleanup even when assertion fails mid-test")
        void shouldCleanupOnMidTestFailure() {
            InMemoryAccountRepository repository = new InMemoryAccountRepository();
            
            try {
                // Setup
                repository.addAccount(BankingTestFixtures.sender());
                repository.addAccount(BankingTestFixtures.receiver());
                
                System.out.println("[TRY-FINALLY] Accounts created: " + 
                    repository.getAccountCount());
                
                // Operations
                UPITransferService service = new UPITransferService(repository);
                service.transfer("SENDER001", "RECEIVER001", 5000.0);
                
                // Assertions
                assertEquals(45000.0, repository.loadAccountById("SENDER001").getBalance());
                
            } finally {
                // ALWAYS runs - even if assertion fails
                System.out.println("[TRY-FINALLY] Finally block - cleaning up");
                repository.clear();
                System.out.println("[TRY-FINALLY] Repository cleared");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // CLASS-LEVEL CLEANUP
    // ═══════════════════════════════════════════════════════════

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Class-Level Resource Management")
    class ClassLevelCleanupTest {

        private InMemoryAccountRepository sharedRepository;
        private int testCount = 0;

        @BeforeAll
        void setUpClass() {
            System.out.println("\n[CLASS-LEVEL] @BeforeAll - Creating shared repository");
            sharedRepository = new InMemoryAccountRepository();
            
            // Add accounts once for all tests
            sharedRepository.addAccount(BankingTestFixtures.sender());
            sharedRepository.addAccount(BankingTestFixtures.receiver());
        }

        @AfterAll
        void tearDownClass() {
            System.out.println("\n[CLASS-LEVEL] @AfterAll - Final cleanup");
            System.out.println("  → Total tests run: " + testCount);
            System.out.println("  → Final account count: " + sharedRepository.getAccountCount());
            
            sharedRepository.clear();
            System.out.println("  → Repository cleared");
        }

        @BeforeEach
        void resetBalances() {
            // Reset to known state without recreating accounts
            Account sender = sharedRepository.loadAccountById("SENDER001");
            Account receiver = sharedRepository.loadAccountById("RECEIVER001");
            
            // Would need setBalance method to fully reset
            // This demonstrates the limitation of shared state
            testCount++;
        }

        @Test
        @Order(1)
        @DisplayName("Test 1: Uses shared repository")
        void test1() {
            System.out.println("[CLASS-LEVEL] Test 1 - testCount: " + testCount);
            assertNotNull(sharedRepository);
            assertEquals(2, sharedRepository.getAccountCount());
        }

        @Test
        @Order(2)
        @DisplayName("Test 2: Also uses shared repository")
        void test2() {
            System.out.println("[CLASS-LEVEL] Test 2 - testCount: " + testCount);
            // Same repository from @BeforeAll
            assertEquals(2, sharedRepository.getAccountCount());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // USING FIXTURES FOR CLEAN TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Using Test Fixtures")
    class UsingFixturesTest {

        private InMemoryAccountRepository repository;
        private UPITransferService service;

        @BeforeEach
        void setUp() {
            // Use fixtures for consistent setup
            repository = BankingTestFixtures.standardRepository();
            service = new UPITransferService(repository);
        }

        @AfterEach
        void tearDown() {
            repository.clear();
        }

        @Test
        @DisplayName("Standard transfer using fixtures")
        void standardTransfer() {
            // Fixtures provide known starting state
            service.transfer("SENDER001", "RECEIVER001", 10000.0);
            
            assertEquals(40000.0, repository.loadAccountById("SENDER001").getBalance());
            assertEquals(35000.0, repository.loadAccountById("RECEIVER001").getBalance());
        }

        @Test
        @DisplayName("Using comprehensive repository fixture")
        void comprehensiveRepositoryTest() {
            // Switch to comprehensive fixture
            repository = BankingTestFixtures.comprehensiveRepository();
            service = new UPITransferService(repository);
            
            // Has 5 accounts: sender, receiver, rich, poor, merchant
            assertEquals(5, repository.getAccountCount());
            
            // Rich to poor transfer
            service.transfer("RICH001", "POOR001", 50000.0);
            
            assertEquals(450000.0, repository.loadAccountById("RICH001").getBalance());
            assertEquals(50100.0, repository.loadAccountById("POOR001").getBalance());
        }

        @Test
        @DisplayName("Using chain accounts fixture")
        void chainAccountsTest() {
            repository = BankingTestFixtures.chainRepository();
            service = new UPITransferService(repository);
            
            // Chain: Alice(30000) → Bob(20000) → Charlie(10000)
            service.transfer("ALICE", "BOB", 5000.0);
            service.transfer("BOB", "CHARLIE", 10000.0);
            
            assertEquals(25000.0, repository.loadAccountById("ALICE").getBalance());
            assertEquals(15000.0, repository.loadAccountById("BOB").getBalance());
            assertEquals(20000.0, repository.loadAccountById("CHARLIE").getBalance());
        }
    }
}
