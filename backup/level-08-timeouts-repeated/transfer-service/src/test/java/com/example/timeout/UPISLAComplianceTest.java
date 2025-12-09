package com.example.timeout;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;
import com.example.service.UPITransferService.TransferResult;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UPI SLA Compliance Tests - LEVEL 8
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║               NPCI UPI SLA COMPLIANCE TESTS                   ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  NPCI mandates specific response times for UPI transactions:  ║
 * ║                                                               ║
 * ║  Transaction Type          │ SLA                              ║
 * ║  ──────────────────────────┼─────────────────                 ║
 * ║  P2P Transfer              │ < 500ms                          ║
 * ║  P2M Payment               │ < 500ms                          ║
 * ║  Balance Inquiry           │ < 200ms                          ║
 * ║  Account Validation        │ < 100ms                          ║
 * ║  Transaction Status        │ < 300ms                          ║
 * ║  End-to-End Flow           │ < 2 seconds                      ║
 * ║                                                               ║
 * ║  These tests verify SLA compliance with repeated execution    ║
 * ║  to ensure consistent performance.                            ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("UPI SLA Compliance Tests")
@Timeout(value = 5, unit = TimeUnit.SECONDS)  // Global safety net
class UPISLAComplianceTest {

    // ═══════════════════════════════════════════════════════════
    // SLA CONSTANTS (as per NPCI guidelines)
    // ═══════════════════════════════════════════════════════════

    private static final Duration P2P_TRANSFER_SLA = Duration.ofMillis(500);
    private static final Duration P2M_PAYMENT_SLA = Duration.ofMillis(500);
    private static final Duration BALANCE_INQUIRY_SLA = Duration.ofMillis(200);
    private static final Duration ACCOUNT_VALIDATION_SLA = Duration.ofMillis(100);
    private static final Duration TRANSACTION_STATUS_SLA = Duration.ofMillis(300);
    private static final Duration END_TO_END_SLA = Duration.ofSeconds(2);

    private static final int SLA_VERIFICATION_RUNS = 10;

    private InMemoryAccountRepository repository;
    private UPITransferService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        service = new UPITransferService(repository);
        
        // Standard test accounts
        repository.addAccount(new Account("USER001", "Rajesh Kumar", 100000.0));
        repository.addAccount(new Account("USER002", "Priya Sharma", 50000.0));
        repository.addAccount(new Account("MERCHANT", "SuperMart", 0.0));
        
        // Add UPI mappings
        repository.addUpiMapping("rajesh@upi", "USER001");
        repository.addUpiMapping("priya@upi", "USER002");
        repository.addUpiMapping("supermart@upi", "MERCHANT");
    }

    // ═══════════════════════════════════════════════════════════
    // P2P TRANSFER SLA TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("P2P Transfer SLA (< 500ms)")
    class P2PTransferSLATests {

        @RepeatedTest(value = SLA_VERIFICATION_RUNS, 
            name = "P2P Transfer SLA Check #{currentRepetition}/" + SLA_VERIFICATION_RUNS)
        @DisplayName("P2P transfer should complete within SLA")
        void p2pTransferShouldMeetSLA() {
            TransferResult result = assertTimeout(
                P2P_TRANSFER_SLA,
                () -> service.transfer("USER001", "USER002", 1000.0),
                "P2P transfer exceeded 500ms SLA"
            );
            
            assertNotNull(result);
            assertEquals(1000.0, result.getAmount());
        }

        @RepeatedTest(value = 5, name = "Small P2P (₹{currentRepetition}0)")
        @DisplayName("Small P2P transfers")
        void smallP2PTransfers(RepetitionInfo info) {
            double amount = info.getCurrentRepetition() * 10.0;
            
            assertTimeout(P2P_TRANSFER_SLA, () -> {
                service.transfer("USER001", "USER002", amount);
            }, "Small P2P (₹" + amount + ") exceeded SLA");
        }

        @RepeatedTest(value = 5, name = "Large P2P (₹{currentRepetition}0000)")
        @DisplayName("Large P2P transfers")
        void largeP2PTransfers(RepetitionInfo info) {
            double amount = info.getCurrentRepetition() * 10000.0;
            
            assertTimeout(P2P_TRANSFER_SLA, () -> {
                service.transfer("USER001", "USER002", amount);
            }, "Large P2P (₹" + amount + ") exceeded SLA");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // P2M PAYMENT SLA TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("P2M Payment SLA (< 500ms)")
    class P2MPaymentSLATests {

        @RepeatedTest(value = SLA_VERIFICATION_RUNS,
            name = "P2M Payment SLA Check #{currentRepetition}/" + SLA_VERIFICATION_RUNS)
        @DisplayName("P2M payment should complete within SLA")
        void p2mPaymentShouldMeetSLA() {
            TransferResult result = assertTimeout(
                P2M_PAYMENT_SLA,
                () -> service.transfer("USER001", "MERCHANT", 500.0),
                "P2M payment exceeded 500ms SLA"
            );
            
            assertNotNull(result);
            assertTrue(result.getReceiverBalanceAfter() > 0);
        }

        @RepeatedTest(value = 3, name = "Common purchase amount: ₹{currentRepetition}99")
        @DisplayName("Common purchase amounts")
        void commonPurchaseAmounts(RepetitionInfo info) {
            double[] amounts = {99.0, 199.0, 499.0};
            double amount = amounts[info.getCurrentRepetition() - 1];
            
            assertTimeout(P2M_PAYMENT_SLA, () -> {
                service.transfer("USER001", "MERCHANT", amount);
            }, "Purchase of ₹" + amount + " exceeded SLA");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // BALANCE INQUIRY SLA TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Balance Inquiry SLA (< 200ms)")
    class BalanceInquirySLATests {

        @RepeatedTest(value = SLA_VERIFICATION_RUNS,
            name = "Balance Inquiry #{currentRepetition}/" + SLA_VERIFICATION_RUNS)
        @DisplayName("Balance inquiry should complete within SLA")
        void balanceInquiryShouldMeetSLA() {
            Double balance = assertTimeout(
                BALANCE_INQUIRY_SLA,
                () -> service.getBalance("USER001"),
                "Balance inquiry exceeded 200ms SLA"
            );
            
            assertNotNull(balance);
            assertTrue(balance >= 0);
        }

        @RepeatedTest(value = 20, name = "Rapid balance checks #{currentRepetition}/20")
        @Timeout(value = 200, unit = TimeUnit.MILLISECONDS)
        @DisplayName("Rapid consecutive balance inquiries")
        void rapidBalanceInquiries() {
            // Each individual check must be fast
            double balance = service.getBalance("USER001");
            assertTrue(balance >= 0);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ACCOUNT VALIDATION SLA TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Account Validation SLA (< 100ms)")
    class AccountValidationSLATests {

        @RepeatedTest(value = SLA_VERIFICATION_RUNS,
            name = "Account Validation #{currentRepetition}/" + SLA_VERIFICATION_RUNS)
        @DisplayName("Account validation should complete within SLA")
        void accountValidationShouldMeetSLA() {
            Boolean exists = assertTimeout(
                ACCOUNT_VALIDATION_SLA,
                () -> service.accountExists("USER001"),
                "Account validation exceeded 100ms SLA"
            );
            
            assertTrue(exists);
        }

        @RepeatedTest(value = 5, name = "Validate account {currentRepetition}")
        @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
        @DisplayName("Validation must be instant")
        void validationMustBeInstant() {
            assertTrue(service.accountExists("USER001"));
            assertFalse(service.accountExists("NONEXISTENT"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // END-TO-END FLOW SLA TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("End-to-End Flow SLA (< 2 seconds)")
    class EndToEndSLATests {

        @RepeatedTest(value = 5, name = "E2E Flow #{currentRepetition}/5")
        @DisplayName("Complete transfer flow should meet SLA")
        void completeTransferFlowShouldMeetSLA() {
            assertTimeout(END_TO_END_SLA, () -> {
                // Step 1: Validate sender
                assertTrue(service.accountExists("USER001"));
                
                // Step 2: Validate receiver
                assertTrue(service.accountExists("USER002"));
                
                // Step 3: Check sender balance
                double senderBalance = service.getBalance("USER001");
                assertTrue(senderBalance >= 1000.0);
                
                // Step 4: Execute transfer
                TransferResult result = service.transfer("USER001", "USER002", 1000.0);
                
                // Step 5: Verify transfer
                assertNotNull(result);
                assertEquals(1000.0, result.getAmount());
                
                // Step 6: Verify final balances
                assertEquals(result.getSenderBalanceAfter(), service.getBalance("USER001"));
                assertEquals(result.getReceiverBalanceAfter(), service.getBalance("USER002"));
            }, "End-to-end flow exceeded 2 second SLA");
        }

        @RepeatedTest(value = 3, name = "Complex flow #{currentRepetition}")
        @DisplayName("Complex multi-step flow")
        void complexMultiStepFlow() {
            assertTimeout(END_TO_END_SLA, () -> {
                // Chain of validations and transfers
                for (String accountId : List.of("USER001", "USER002", "MERCHANT")) {
                    assertTrue(service.accountExists(accountId));
                    assertNotNull(service.getBalance(accountId));
                }
                
                // Multiple transfers
                service.transfer("USER001", "USER002", 500.0);
                service.transfer("USER002", "MERCHANT", 200.0);
                
                // Final verification
                assertTrue(service.getBalance("MERCHANT") > 0);
            });
        }
    }

    // ═══════════════════════════════════════════════════════════
    // SLA COMPLIANCE SUMMARY TEST
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("SLA Compliance Summary")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class SLAComplianceSummary {

        private List<Long> p2pTimes = new ArrayList<>();
        private List<Long> balanceTimes = new ArrayList<>();
        private List<Long> validationTimes = new ArrayList<>();

        @BeforeAll
        void initStats() {
            p2pTimes.clear();
            balanceTimes.clear();
            validationTimes.clear();
        }

        @RepeatedTest(value = 10, name = "Measure P2P #{currentRepetition}")
        void measureP2P() {
            InMemoryAccountRepository localRepo = new InMemoryAccountRepository();
            localRepo.addAccount(new Account("A", "A", 10000.0));
            localRepo.addAccount(new Account("B", "B", 5000.0));
            UPITransferService localService = new UPITransferService(localRepo);
            
            long start = System.nanoTime();
            localService.transfer("A", "B", 100.0);
            p2pTimes.add((System.nanoTime() - start) / 1_000_000);
        }

        @RepeatedTest(value = 10, name = "Measure Balance #{currentRepetition}")
        void measureBalance() {
            InMemoryAccountRepository localRepo = new InMemoryAccountRepository();
            localRepo.addAccount(new Account("A", "A", 10000.0));
            UPITransferService localService = new UPITransferService(localRepo);
            
            long start = System.nanoTime();
            localService.getBalance("A");
            balanceTimes.add((System.nanoTime() - start) / 1_000_000);
        }

        @RepeatedTest(value = 10, name = "Measure Validation #{currentRepetition}")
        void measureValidation() {
            InMemoryAccountRepository localRepo = new InMemoryAccountRepository();
            localRepo.addAccount(new Account("A", "A", 10000.0));
            UPITransferService localService = new UPITransferService(localRepo);
            
            long start = System.nanoTime();
            localService.accountExists("A");
            validationTimes.add((System.nanoTime() - start) / 1_000_000);
        }

        @AfterAll
        void printComplianceReport() {
            System.out.println("\n");
            System.out.println("╔════════════════════════════════════════════════════════════╗");
            System.out.println("║              UPI SLA COMPLIANCE REPORT                     ║");
            System.out.println("╠════════════════════════════════════════════════════════════╣");
            System.out.println("║  Operation          │ SLA      │ Avg      │ Status        ║");
            System.out.println("║  ──────────────────────────────────────────────────────── ║");
            
            printMetric("P2P Transfer", 500, p2pTimes);
            printMetric("Balance Inquiry", 200, balanceTimes);
            printMetric("Account Validation", 100, validationTimes);
            
            System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        }

        private void printMetric(String operation, long slaMs, List<Long> times) {
            if (times.isEmpty()) {
                System.out.printf("║  %-18s │ %4dms   │ N/A      │ NO DATA       ║%n", 
                    operation, slaMs);
                return;
            }
            
            double avg = times.stream().mapToLong(Long::longValue).average().orElse(0);
            String status = avg < slaMs ? "✅ COMPLIANT" : "❌ VIOLATION";
            
            System.out.printf("║  %-18s │ %4dms   │ %5.1fms  │ %s ║%n", 
                operation, slaMs, avg, status);
        }
    }
}
