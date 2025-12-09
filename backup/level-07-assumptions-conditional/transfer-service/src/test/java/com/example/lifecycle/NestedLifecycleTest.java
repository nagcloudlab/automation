package com.example.lifecycle;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Nested Lifecycle Demo - LEVEL 6
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║               NESTED CLASS LIFECYCLE DEMO                     ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Demonstrates how lifecycle methods work with @Nested:        ║
 * ║                                                               ║
 * ║  Outer @BeforeAll                                             ║
 * ║    └─ Outer @BeforeEach                                       ║
 * ║         └─ Inner @BeforeEach                                  ║
 * ║              └─ @Test                                         ║
 * ║              └─ Inner @AfterEach                              ║
 * ║         └─ Outer @AfterEach                                   ║
 * ║  Outer @AfterAll                                              ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Nested Lifecycle Demo")
class NestedLifecycleTest {

    // ═══════════════════════════════════════════════════════════
    // OUTER CLASS RESOURCES
    // ═══════════════════════════════════════════════════════════

    private InMemoryAccountRepository repository;
    private UPITransferService service;

    // ═══════════════════════════════════════════════════════════
    // OUTER CLASS LIFECYCLE
    // ═══════════════════════════════════════════════════════════

    @BeforeAll
    static void outerBeforeAll() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("OUTER @BeforeAll");
        System.out.println("═".repeat(70));
    }

    @AfterAll
    static void outerAfterAll() {
        System.out.println("\n" + "═".repeat(70));
        System.out.println("OUTER @AfterAll");
        System.out.println("═".repeat(70) + "\n");
    }

    @BeforeEach
    void outerBeforeEach() {
        System.out.println("\n  ┌─ OUTER @BeforeEach");
        System.out.println("  │    → Creating repository and service");
        
        repository = new InMemoryAccountRepository();
        service = new UPITransferService(repository);
    }

    @AfterEach
    void outerAfterEach() {
        System.out.println("  │");
        System.out.println("  └─ OUTER @AfterEach");
        System.out.println("       → Repository account count: " + repository.getAccountCount());
    }

    // ═══════════════════════════════════════════════════════════
    // OUTER CLASS TESTS
    // ═══════════════════════════════════════════════════════════

    @Test
    @DisplayName("Outer: Repository should be empty")
    void outerTest_repositoryShouldBeEmpty() {
        System.out.println("  │");
        System.out.println("  ├─ OUTER @Test: outerTest_repositoryShouldBeEmpty");
        
        assertTrue(repository.isEmpty(), 
            "Repository should be empty (no inner setup ran)");
    }

    // ═══════════════════════════════════════════════════════════
    // NESTED CLASS: DEBIT OPERATIONS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Debit Operations")
    class DebitOperationsTest {

        private Account sender;

        /**
         * Inner @BeforeEach runs AFTER outer @BeforeEach.
         */
        @BeforeEach
        void innerBeforeEach() {
            System.out.println("  │");
            System.out.println("  │  ┌─ INNER @BeforeEach (DebitOperations)");
            System.out.println("  │  │    → Adding sender account with ₹50,000");
            
            sender = new Account("SENDER", "Rajesh Kumar", 50000.0);
            repository.addAccount(sender);
        }

        @AfterEach
        void innerAfterEach() {
            System.out.println("  │  │");
            System.out.println("  │  └─ INNER @AfterEach (DebitOperations)");
            System.out.println("  │       → Sender balance: ₹" + sender.getBalance());
        }

        @Test
        @DisplayName("Should debit from sender")
        void shouldDebitFromSender() {
            System.out.println("  │  │");
            System.out.println("  │  ├─ @Test: shouldDebitFromSender");
            System.out.println("  │  │    → Debiting ₹10,000");
            
            sender.debit(10000.0);
            
            assertEquals(40000.0, sender.getBalance());
            assertEquals(40000.0, repository.loadAccountById("SENDER").getBalance());
        }

        @Test
        @DisplayName("Should handle exact balance debit")
        void shouldHandleExactBalanceDebit() {
            System.out.println("  │  │");
            System.out.println("  │  ├─ @Test: shouldHandleExactBalanceDebit");
            System.out.println("  │  │    → Debiting entire balance (₹50,000)");
            
            sender.debit(50000.0);
            
            assertEquals(0.0, sender.getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // NESTED CLASS: CREDIT OPERATIONS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Credit Operations")
    class CreditOperationsTest {

        private Account receiver;

        @BeforeEach
        void innerBeforeEach() {
            System.out.println("  │");
            System.out.println("  │  ┌─ INNER @BeforeEach (CreditOperations)");
            System.out.println("  │  │    → Adding receiver account with ₹25,000");
            
            receiver = new Account("RECEIVER", "Priya Sharma", 25000.0);
            repository.addAccount(receiver);
        }

        @AfterEach
        void innerAfterEach() {
            System.out.println("  │  │");
            System.out.println("  │  └─ INNER @AfterEach (CreditOperations)");
        }

        @Test
        @DisplayName("Should credit to receiver")
        void shouldCreditToReceiver() {
            System.out.println("  │  │");
            System.out.println("  │  ├─ @Test: shouldCreditToReceiver");
            System.out.println("  │  │    → Crediting ₹15,000");
            
            receiver.credit(15000.0);
            
            assertEquals(40000.0, receiver.getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // NESTED CLASS: TRANSFER OPERATIONS (uses both accounts)
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Transfer Operations")
    class TransferOperationsTest {

        private Account sender;
        private Account receiver;

        @BeforeEach
        void innerBeforeEach() {
            System.out.println("  │");
            System.out.println("  │  ┌─ INNER @BeforeEach (TransferOperations)");
            System.out.println("  │  │    → Adding sender (₹50,000) and receiver (₹25,000)");
            
            sender = new Account("SENDER", "Rajesh", 50000.0);
            receiver = new Account("RECEIVER", "Priya", 25000.0);
            
            repository.addAccount(sender);
            repository.addAccount(receiver);
        }

        @AfterEach
        void innerAfterEach() {
            System.out.println("  │  │");
            System.out.println("  │  └─ INNER @AfterEach (TransferOperations)");
            System.out.println("  │       → Final balances - Sender: ₹" + 
                repository.loadAccountById("SENDER").getBalance() + 
                ", Receiver: ₹" + repository.loadAccountById("RECEIVER").getBalance());
        }

        @Test
        @DisplayName("Should transfer between accounts")
        void shouldTransferBetweenAccounts() {
            System.out.println("  │  │");
            System.out.println("  │  ├─ @Test: shouldTransferBetweenAccounts");
            System.out.println("  │  │    → Transferring ₹20,000");
            
            service.transfer("SENDER", "RECEIVER", 20000.0);
            
            assertEquals(30000.0, repository.loadAccountById("SENDER").getBalance());
            assertEquals(45000.0, repository.loadAccountById("RECEIVER").getBalance());
        }

        @Test
        @DisplayName("Should maintain balance conservation")
        void shouldMaintainBalanceConservation() {
            System.out.println("  │  │");
            System.out.println("  │  ├─ @Test: shouldMaintainBalanceConservation");
            
            double totalBefore = repository.getTotalBalance();
            System.out.println("  │  │    → Total before: ₹" + totalBefore);
            
            service.transfer("SENDER", "RECEIVER", 15000.0);
            
            double totalAfter = repository.getTotalBalance();
            System.out.println("  │  │    → Total after: ₹" + totalAfter);
            
            assertEquals(totalBefore, totalAfter, 0.001);
        }

        // ═══════════════════════════════════════════════════════
        // DEEPLY NESTED CLASS
        // ═══════════════════════════════════════════════════════

        @Nested
        @DisplayName("Multiple Transfer Scenarios")
        class MultipleTransferTest {

            @BeforeEach
            void deeplyNestedBeforeEach() {
                System.out.println("  │  │");
                System.out.println("  │  │  ┌─ DEEPLY NESTED @BeforeEach");
                System.out.println("  │  │  │    → All three @BeforeEach methods have run!");
            }

            @Test
            @DisplayName("Should handle chain transfers")
            void shouldHandleChainTransfers() {
                System.out.println("  │  │  │");
                System.out.println("  │  │  ├─ @Test: shouldHandleChainTransfers");
                
                // Add third account
                Account third = new Account("THIRD", "Amit", 10000.0);
                repository.addAccount(third);
                
                // Chain: SENDER → RECEIVER → THIRD
                service.transfer("SENDER", "RECEIVER", 10000.0);
                service.transfer("RECEIVER", "THIRD", 5000.0);
                
                assertEquals(40000.0, repository.loadAccountById("SENDER").getBalance());
                assertEquals(30000.0, repository.loadAccountById("RECEIVER").getBalance());
                assertEquals(15000.0, repository.loadAccountById("THIRD").getBalance());
            }
        }
    }
}
