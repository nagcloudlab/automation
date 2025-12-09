package com.example.tags;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;
import com.example.service.UPITransferService.TransferResult;
import com.example.tags.TestTags.*;

import org.junit.jupiter.api.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Composed Annotation Demo - LEVEL 9
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║               CUSTOM COMPOSED ANNOTATIONS                     ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Instead of writing:                                          ║
 * ║    @Test                                                      ║
 * ║    @Tag("unit")                                               ║
 * ║    @Tag("fast")                                               ║
 * ║    @Timeout(1)                                                ║
 * ║                                                               ║
 * ║  We can simply write:                                         ║
 * ║    @UnitTest                                                  ║
 * ║                                                               ║
 * ║  Benefits:                                                    ║
 * ║  • Less boilerplate                                           ║
 * ║  • Consistent timeouts                                        ║
 * ║  • Self-documenting                                           ║
 * ║  • Easy to filter                                             ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Composed Annotation Demo")
class ComposedAnnotationTest {

    private InMemoryAccountRepository repository;
    private UPITransferService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        service = new UPITransferService(repository);
        repository.addAccount(new Account("USER001", "Rajesh Kumar", 100000.0));
        repository.addAccount(new Account("USER002", "Priya Sharma", 50000.0));
        repository.addAccount(new Account("MERCHANT", "SuperMart Store", 0.0));
        
        // Add UPI mappings
        repository.addUpiMapping("rajesh@upi", "USER001");
        repository.addUpiMapping("priya@upi", "USER002");
        repository.addUpiMapping("supermart@upi", "MERCHANT");
    }

    // ═══════════════════════════════════════════════════════════
    // @UnitTest EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@UnitTest Examples")
    class UnitTestExamples {

        @UnitTest
        @DisplayName("Validate account exists")
        void validateAccountExists() {
            // Tags: unit, fast
            // Timeout: 1 second
            assertTrue(service.accountExists("USER001"));
            assertTrue(service.accountExists("USER002"));
            assertFalse(service.accountExists("UNKNOWN"));
        }

        @UnitTest
        @DisplayName("Validate balance retrieval")
        void validateBalanceRetrieval() {
            double balance = service.getBalance("USER001");
            assertEquals(100000.0, balance);
        }

        @UnitTest
        @DisplayName("Validate account model")
        void validateAccountModel() {
            Account account = new Account("TEST", "Test User", 5000.0);
            
            assertEquals("TEST", account.getAccountId());
            assertEquals("Test User", account.getAccountHolderName());
            assertEquals(5000.0, account.getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @IntegrationTest EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@IntegrationTest Examples")
    class IntegrationTestExamples {

        @IntegrationTest
        @DisplayName("Full transfer flow with persistence")
        void fullTransferFlow() {
            // Tags: integration
            // Timeout: 5 seconds
            TransferResult result = service.transfer("USER001", "USER002", 10000.0);
            
            assertNotNull(result);
            assertEquals(10000.0, result.getAmount());
            
            // Verify persistence
            assertEquals(90000.0, repository.loadAccountById("USER001").getBalance());
            assertEquals(60000.0, repository.loadAccountById("USER002").getBalance());
        }

        @IntegrationTest
        @DisplayName("Multiple transfers integration")
        void multipleTransfersIntegration() {
            double totalBefore = repository.getTotalBalance();
            
            service.transfer("USER001", "USER002", 5000.0);
            service.transfer("USER002", "MERCHANT", 2500.0);
            service.transfer("MERCHANT", "USER001", 1000.0);
            
            double totalAfter = repository.getTotalBalance();
            assertEquals(totalBefore, totalAfter, 0.001);
        }

        @IntegrationTest
        @DisplayName("UPI ID transfer integration")
        void upiIdTransferIntegration() {
            TransferResult result = service.transferByUpiId(
                "rajesh@upi", "priya@upi", 7500.0
            );
            
            assertEquals(7500.0, result.getAmount());
            assertEquals(92500.0, result.getSenderBalanceAfter());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @P2PTransferTest EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@P2PTransferTest Examples")
    class P2PTransferTestExamples {

        @P2PTransferTest
        @DisplayName("Basic P2P transfer")
        void basicP2PTransfer() {
            // Tags: banking, transfer, p2p
            TransferResult result = service.transfer("USER001", "USER002", 5000.0);
            
            assertEquals(5000.0, result.getAmount());
            assertEquals(95000.0, result.getSenderBalanceAfter());
            assertEquals(55000.0, result.getReceiverBalanceAfter());
        }

        @P2PTransferTest
        @DisplayName("P2P with UPI ID")
        void p2pWithUpiId() {
            TransferResult result = service.transferByUpiId(
                "rajesh@upi", "priya@upi", 3000.0
            );
            
            assertEquals(3000.0, result.getAmount());
        }

        @P2PTransferTest
        @DisplayName("P2P minimum amount")
        void p2pMinimumAmount() {
            TransferResult result = service.transfer("USER001", "USER002", 1.0);
            assertEquals(1.0, result.getAmount());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @P2MPaymentTest EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@P2MPaymentTest Examples")
    class P2MPaymentTestExamples {

        @P2MPaymentTest
        @DisplayName("Basic P2M payment")
        void basicP2MPayment() {
            // Tags: banking, transfer, p2m
            TransferResult result = service.transfer("USER001", "MERCHANT", 999.0);
            
            assertEquals(999.0, result.getAmount());
            assertEquals(999.0, result.getReceiverBalanceAfter());
        }

        @P2MPaymentTest
        @DisplayName("P2M with UPI ID")
        void p2mWithUpiId() {
            TransferResult result = service.transferByUpiId(
                "rajesh@upi", "supermart@upi", 2499.0
            );
            
            assertEquals(2499.0, result.getAmount());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @BalanceTest EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@BalanceTest Examples")
    class BalanceTestExamples {

        @BalanceTest
        @DisplayName("Check balance - quick response")
        void checkBalanceQuick() {
            // Tags: banking, balance
            // Timeout: 200ms (SLA requirement)
            double balance = service.getBalance("USER001");
            assertEquals(100000.0, balance);
        }

        @BalanceTest
        @DisplayName("Balance after transactions")
        void balanceAfterTransactions() {
            service.transfer("USER001", "USER002", 15000.0);
            
            assertEquals(85000.0, service.getBalance("USER001"));
            assertEquals(65000.0, service.getBalance("USER002"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @SmokeTest EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@SmokeTest Examples")
    class SmokeTestExamples {

        @SmokeTest
        @DisplayName("System health check")
        void systemHealthCheck() {
            // Tags: smoke, critical
            // Timeout: 5 seconds
            
            // Verify service is operational
            assertNotNull(repository);
            assertNotNull(service);
            
            // Verify basic operations work
            assertTrue(service.accountExists("USER001"));
            assertNotNull(service.getBalance("USER001"));
        }

        @SmokeTest
        @DisplayName("Critical path verification")
        void criticalPathVerification() {
            // The most important flow must work
            TransferResult result = service.transfer("USER001", "USER002", 100.0);
            
            assertNotNull(result);
            assertTrue(result.getAmount() > 0);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @PerformanceTest EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@PerformanceTest Examples")
    class PerformanceTestExamples {

        @PerformanceTest
        @DisplayName("Bulk transfer performance")
        void bulkTransferPerformance() {
            // Tags: performance, slow
            // Timeout: 30 seconds
            
            long start = System.currentTimeMillis();
            
            for (int i = 0; i < 100; i++) {
                service.transfer("USER001", "USER002", 100.0);
            }
            
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("100 transfers completed in " + elapsed + "ms");
            
            assertTrue(elapsed < 5000, "Should complete within 5 seconds");
        }

        @PerformanceTest
        @DisplayName("Balance inquiry SLA")
        void balanceInquirySLA() {
            // 1000 balance inquiries should be fast
            long start = System.currentTimeMillis();
            
            for (int i = 0; i < 1000; i++) {
                service.getBalance("USER001");
            }
            
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("1000 balance inquiries in " + elapsed + "ms");
            
            assertTrue(elapsed < 2000, "Should complete within 2 seconds");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @EdgeCaseTest EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@EdgeCaseTest Examples")
    class EdgeCaseTestExamples {

        @EdgeCaseTest
        @DisplayName("Exact balance transfer")
        void exactBalanceTransfer() {
            // Tags: edge-case, boundary
            repository.addAccount(new Account("EXACT", "Exact User", 5000.0));
            
            TransferResult result = service.transfer("EXACT", "USER002", 5000.0);
            
            assertEquals(0.0, result.getSenderBalanceAfter());
        }

        @EdgeCaseTest
        @DisplayName("Minimum UPI amount")
        void minimumUpiAmount() {
            TransferResult result = service.transfer("USER001", "USER002", 1.0);
            assertEquals(1.0, result.getAmount());
        }

        @EdgeCaseTest
        @DisplayName("Maximum UPI amount")
        void maximumUpiAmount() {
            TransferResult result = service.transfer("USER001", "USER002", 100000.0);
            assertEquals(100000.0, result.getAmount());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @CriticalTest EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@CriticalTest Examples")
    class CriticalTestExamples {

        @CriticalTest
        @DisplayName("Money conservation")
        void moneyConservation() {
            // Tags: critical, priority-high
            double totalBefore = repository.getTotalBalance();
            
            service.transfer("USER001", "USER002", 25000.0);
            service.transfer("USER002", "MERCHANT", 10000.0);
            service.transfer("MERCHANT", "USER001", 5000.0);
            
            double totalAfter = repository.getTotalBalance();
            
            assertEquals(totalBefore, totalAfter, 0.001,
                "CRITICAL: Total money must be conserved!");
        }

        @CriticalTest
        @DisplayName("No partial transfers")
        void noPartialTransfers() {
            double senderBefore = service.getBalance("USER001");
            double receiverBefore = service.getBalance("USER002");
            
            // Transfer succeeds completely or not at all
            service.transfer("USER001", "USER002", 15000.0);
            
            double senderAfter = service.getBalance("USER001");
            double receiverAfter = service.getBalance("USER002");
            
            assertEquals(15000.0, senderBefore - senderAfter, 0.001);
            assertEquals(15000.0, receiverAfter - receiverBefore, 0.001);
        }
    }
}
