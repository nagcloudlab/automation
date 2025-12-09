package com.example.lifecycle;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Instance Lifecycle Demo - LEVEL 6
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║              TEST INSTANCE LIFECYCLE MODES                    ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  JUnit 5 supports two test instance lifecycles:               ║
 * ║                                                               ║
 * ║  PER_METHOD (default):                                        ║
 * ║  • New test class instance for EACH test method               ║
 * ║  • Complete isolation between tests                           ║
 * ║  • @BeforeAll/@AfterAll must be static                        ║
 * ║                                                               ║
 * ║  PER_CLASS:                                                   ║
 * ║  • Single test class instance for ALL tests                   ║
 * ║  • State shared between tests                                 ║
 * ║  • @BeforeAll/@AfterAll can be non-static                     ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
class TestInstanceLifecycleTest {

    // ═══════════════════════════════════════════════════════════
    // DEFAULT LIFECYCLE: PER_METHOD
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Default Lifecycle (PER_METHOD)")
    class PerMethodLifecycleTest {

        // Instance counter - reset for each test (new instance each time)
        private int counter = 0;

        @Test
        @DisplayName("Test 1: Counter should be 1")
        void test1() {
            counter++;
            System.out.println("PER_METHOD Test 1: counter = " + counter);
            assertEquals(1, counter, "Counter should be 1 (fresh instance)");
        }

        @Test
        @DisplayName("Test 2: Counter should also be 1 (new instance)")
        void test2() {
            counter++;
            System.out.println("PER_METHOD Test 2: counter = " + counter);
            assertEquals(1, counter, "Counter should be 1 (fresh instance, not 2)");
        }

        @Test
        @DisplayName("Test 3: Counter remains 1 (complete isolation)")
        void test3() {
            counter++;
            System.out.println("PER_METHOD Test 3: counter = " + counter);
            assertEquals(1, counter, "Counter should be 1 (tests are isolated)");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // PER_CLASS LIFECYCLE
    // ═══════════════════════════════════════════════════════════

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("PER_CLASS Lifecycle (Shared State)")
    class PerClassLifecycleTest {

        // Instance counter - shared across all tests (same instance)
        private int counter = 0;

        // Repository can be instance field with @BeforeAll
        private InMemoryAccountRepository repository;
        private UPITransferService service;

        /**
         * Non-static @BeforeAll is possible with PER_CLASS!
         */
        @BeforeAll
        void setUpOnce() {
            System.out.println("\n[PER_CLASS] @BeforeAll - Non-static!");
            repository = BankingTestFixtures.standardRepository();
            service = new UPITransferService(repository);
            System.out.println("[PER_CLASS] Initial total balance: ₹" + 
                repository.getTotalBalance());
        }

        @AfterAll
        void tearDownOnce() {
            System.out.println("[PER_CLASS] @AfterAll - Non-static!");
            System.out.println("[PER_CLASS] Final total balance: ₹" + 
                repository.getTotalBalance());
        }

        @Test
        @Order(1)
        @DisplayName("Test 1: Counter increments to 1")
        void test1() {
            counter++;
            System.out.println("PER_CLASS Test 1: counter = " + counter);
            assertEquals(1, counter);
        }

        @Test
        @Order(2)
        @DisplayName("Test 2: Counter increments to 2 (shared state)")
        void test2() {
            counter++;
            System.out.println("PER_CLASS Test 2: counter = " + counter);
            assertEquals(2, counter, "Counter should be 2 (state from test1 preserved)");
        }

        @Test
        @Order(3)
        @DisplayName("Test 3: Counter increments to 3")
        void test3() {
            counter++;
            System.out.println("PER_CLASS Test 3: counter = " + counter);
            assertEquals(3, counter, "Counter should be 3 (state accumulated)");
        }

        @Test
        @Order(4)
        @DisplayName("Test 4: Can access repository from @BeforeAll")
        void test4() {
            System.out.println("PER_CLASS Test 4: Using repository from @BeforeAll");
            
            // Repository was set up in non-static @BeforeAll
            assertNotNull(repository);
            assertEquals(2, repository.getAccountCount());
            
            // Transfers affect shared state
            service.transfer("SENDER001", "RECEIVER001", 5000.0);
            
            assertEquals(45000.0, repository.loadAccountById("SENDER001").getBalance());
        }

        @Test
        @Order(5)
        @DisplayName("Test 5: State from Test 4 is preserved")
        void test5() {
            System.out.println("PER_CLASS Test 5: Checking state from test4");
            
            // Balance change from test4 is visible
            assertEquals(45000.0, repository.loadAccountById("SENDER001").getBalance(),
                "Balance change from test4 should be preserved");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // PRACTICAL USE CASE: STATEFUL INTEGRATION TEST
    // ═══════════════════════════════════════════════════════════

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Stateful Transaction Flow Test")
    class StatefulTransactionFlowTest {

        private InMemoryAccountRepository repository;
        private UPITransferService service;
        private double totalFunds;

        @BeforeAll
        void setUpTransactionFlow() {
            System.out.println("\n[STATEFUL] Setting up transaction flow test");
            
            repository = new InMemoryAccountRepository();
            service = new UPITransferService(repository);
            
            // Create accounts for a realistic flow
            repository.addAccount(new Account("COMPANY", "TechCorp", 100000.0));
            repository.addAccount(new Account("EMP001", "Rajesh", 5000.0));
            repository.addAccount(new Account("EMP002", "Priya", 5000.0));
            repository.addAccount(new Account("MERCHANT", "Cafeteria", 0.0));
            
            totalFunds = repository.getTotalBalance();
            System.out.println("[STATEFUL] Total funds in system: ₹" + totalFunds);
        }

        @Test
        @Order(1)
        @DisplayName("Step 1: Company pays salaries")
        void step1_companySalaries() {
            System.out.println("[STATEFUL] Step 1: Salary disbursement");
            
            // Company pays employees
            service.transfer("COMPANY", "EMP001", 25000.0);
            service.transfer("COMPANY", "EMP002", 30000.0);
            
            assertEquals(45000.0, repository.loadAccountById("COMPANY").getBalance());
            assertEquals(30000.0, repository.loadAccountById("EMP001").getBalance());
            assertEquals(35000.0, repository.loadAccountById("EMP002").getBalance());
            
            // Total unchanged
            assertEquals(totalFunds, repository.getTotalBalance(), 0.001);
        }

        @Test
        @Order(2)
        @DisplayName("Step 2: Employees make purchases")
        void step2_employeePurchases() {
            System.out.println("[STATEFUL] Step 2: Employee purchases");
            
            // After step 1: EMP001 has 30000, EMP002 has 35000
            service.transfer("EMP001", "MERCHANT", 500.0);  // Lunch
            service.transfer("EMP002", "MERCHANT", 750.0);  // Lunch + coffee
            
            assertEquals(29500.0, repository.loadAccountById("EMP001").getBalance());
            assertEquals(34250.0, repository.loadAccountById("EMP002").getBalance());
            assertEquals(1250.0, repository.loadAccountById("MERCHANT").getBalance());
            
            assertEquals(totalFunds, repository.getTotalBalance(), 0.001);
        }

        @Test
        @Order(3)
        @DisplayName("Step 3: Peer-to-peer settlement")
        void step3_p2pSettlement() {
            System.out.println("[STATEFUL] Step 3: P2P settlement");
            
            // EMP002 owes EMP001 some money
            service.transfer("EMP002", "EMP001", 2000.0);
            
            assertEquals(31500.0, repository.loadAccountById("EMP001").getBalance());
            assertEquals(32250.0, repository.loadAccountById("EMP002").getBalance());
            
            assertEquals(totalFunds, repository.getTotalBalance(), 0.001);
        }

        @Test
        @Order(4)
        @DisplayName("Step 4: Verify final state")
        void step4_verifyFinalState() {
            System.out.println("[STATEFUL] Step 4: Final verification");
            
            // Verify all expected final balances
            assertAll("Final balances",
                () -> assertEquals(45000.0, repository.loadAccountById("COMPANY").getBalance()),
                () -> assertEquals(31500.0, repository.loadAccountById("EMP001").getBalance()),
                () -> assertEquals(32250.0, repository.loadAccountById("EMP002").getBalance()),
                () -> assertEquals(1250.0, repository.loadAccountById("MERCHANT").getBalance())
            );
            
            // Total funds conserved
            assertEquals(totalFunds, repository.getTotalBalance(), 0.001,
                "Total funds should be conserved throughout the flow");
            
            System.out.println("[STATEFUL] ✓ All transactions verified!");
            System.out.println("[STATEFUL] Final total: ₹" + repository.getTotalBalance());
        }

        @AfterAll
        void printSummary() {
            System.out.println("\n[STATEFUL] Transaction Flow Complete");
            System.out.println("─".repeat(50));
            System.out.printf("  COMPANY:  ₹%,.2f%n", 
                repository.loadAccountById("COMPANY").getBalance());
            System.out.printf("  EMP001:   ₹%,.2f%n", 
                repository.loadAccountById("EMP001").getBalance());
            System.out.printf("  EMP002:   ₹%,.2f%n", 
                repository.loadAccountById("EMP002").getBalance());
            System.out.printf("  MERCHANT: ₹%,.2f%n", 
                repository.loadAccountById("MERCHANT").getBalance());
            System.out.println("─".repeat(50));
            System.out.printf("  TOTAL:    ₹%,.2f (conserved)%n", 
                repository.getTotalBalance());
        }
    }
}
