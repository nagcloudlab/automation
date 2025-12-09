package com.example.tags;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;
import com.example.service.UPITransferService.TransferResult;
import com.example.tags.TestTags.*;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive UPI Test Suite - LEVEL 9
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║              REALISTIC TEST ORGANIZATION                      ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This test class demonstrates how to organize tests for a     ║
 * ║  real banking application with proper tag hierarchies.        ║
 * ║                                                               ║
 * ║  Tag Hierarchy:                                               ║
 * ║    banking                                                    ║
 * ║    ├── transfer                                               ║
 * ║    │   ├── p2p                                                ║
 * ║    │   ├── p2m                                                ║
 * ║    │   ├── rtgs                                               ║
 * ║    │   └── neft                                               ║
 * ║    ├── balance                                                ║
 * ║    ├── account                                                ║
 * ║    │   ├── creation                                           ║
 * ║    │   └── validation                                         ║
 * ║    └── upi                                                    ║
 * ║        ├── collect                                            ║
 * ║        └── pay                                                ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@Tag("banking")
@Tag("upi")
@DisplayName("Comprehensive UPI Test Suite")
class ComprehensiveUPITest {

    private InMemoryAccountRepository repository;
    private UPITransferService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        service = new UPITransferService(repository);
        
        // Individual accounts
        repository.addAccount(new Account("USER001", "Rajesh Kumar", 100000.0));
        repository.addAccount(new Account("USER002", "Priya Sharma", 50000.0));
        repository.addAccount(new Account("USER003", "Amit Patel", 75000.0));
        
        // Merchant accounts
        repository.addAccount(new Account("MERCHANT001", "Amazon Pay", 0.0));
        repository.addAccount(new Account("MERCHANT002", "Flipkart", 0.0));
        repository.addAccount(new Account("MERCHANT003", "Swiggy", 0.0));
        
        // Bank accounts for RTGS/NEFT
        repository.addAccount(new Account("BANK001", "HDFC Corporate", 10000000.0));
        repository.addAccount(new Account("BANK002", "ICICI Corporate", 8000000.0));
        
        // UPI ID mappings
        repository.addUpiMapping("rajesh@upi", "USER001");
        repository.addUpiMapping("priya@upi", "USER002");
        repository.addUpiMapping("amit@upi", "USER003");
        repository.addUpiMapping("amazon@upi", "MERCHANT001");
        repository.addUpiMapping("flipkart@upi", "MERCHANT002");
        repository.addUpiMapping("swiggy@upi", "MERCHANT003");
    }

    // ═══════════════════════════════════════════════════════════
    // P2P TRANSFER TESTS
    // Tags: banking, upi, transfer, p2p
    // Run: mvn test -Dgroups=p2p
    // ═══════════════════════════════════════════════════════════

    @Nested
    @Tag("transfer")
    @Tag("p2p")
    @DisplayName("P2P Transfer Tests")
    class P2PTransferTests {

        @UnitTest
        @DisplayName("Basic P2P transfer validation")
        void basicP2PValidation() {
            assertTrue(service.accountExists("USER001"));
            assertTrue(service.accountExists("USER002"));
            assertTrue(service.getBalance("USER001") >= 1000.0);
        }

        @IntegrationTest
        @DisplayName("P2P transfer execution")
        void p2pTransferExecution() {
            TransferResult result = service.transfer("USER001", "USER002", 5000.0);
            
            assertAll("P2P Transfer Result",
                () -> assertNotNull(result),
                () -> assertEquals(5000.0, result.getAmount()),
                () -> assertEquals(95000.0, result.getSenderBalanceAfter()),
                () -> assertEquals(55000.0, result.getReceiverBalanceAfter())
            );
        }

        @IntegrationTest
        @Tag("upi-id")
        @DisplayName("P2P via UPI ID")
        void p2pViaUpiId() {
            TransferResult result = service.transferByUpiId(
                "rajesh@upi", "priya@upi", 3000.0
            );
            
            assertEquals(3000.0, result.getAmount());
            assertEquals("USER001", result.getSenderId());
            assertEquals("USER002", result.getReceiverId());
        }

        @SmokeTest
        @DisplayName("P2P smoke test - critical path")
        void p2pSmokeTest() {
            TransferResult result = service.transfer("USER001", "USER002", 100.0);
            assertNotNull(result);
            assertNotNull(result.getTransactionId());
        }

        @EdgeCaseTest
        @DisplayName("P2P - minimum amount ₹1")
        void p2pMinimumAmount() {
            TransferResult result = service.transfer("USER001", "USER002", 1.0);
            assertEquals(1.0, result.getAmount());
        }

        @EdgeCaseTest
        @DisplayName("P2P - maximum UPI amount ₹1,00,000")
        void p2pMaximumAmount() {
            TransferResult result = service.transfer("USER001", "USER002", 100000.0);
            assertEquals(100000.0, result.getAmount());
            assertEquals(0.0, result.getSenderBalanceAfter());
        }

        @CriticalTest
        @DisplayName("P2P - money conservation")
        void p2pMoneyConservation() {
            double totalBefore = repository.getTotalBalance();
            
            service.transfer("USER001", "USER002", 10000.0);
            service.transfer("USER002", "USER003", 5000.0);
            service.transfer("USER003", "USER001", 2500.0);
            
            double totalAfter = repository.getTotalBalance();
            assertEquals(totalBefore, totalAfter, 0.001, 
                "CRITICAL: Money must be conserved in P2P!");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // P2M PAYMENT TESTS
    // Tags: banking, upi, transfer, p2m
    // Run: mvn test -Dgroups=p2m
    // ═══════════════════════════════════════════════════════════

    @Nested
    @Tag("transfer")
    @Tag("p2m")
    @DisplayName("P2M Payment Tests")
    class P2MPaymentTests {

        @IntegrationTest
        @DisplayName("Basic P2M payment")
        void basicP2MPayment() {
            TransferResult result = service.transfer("USER001", "MERCHANT001", 999.0);
            
            assertEquals(999.0, result.getAmount());
            assertEquals(999.0, result.getReceiverBalanceAfter());
        }

        @IntegrationTest
        @Tag("upi-id")
        @DisplayName("P2M via UPI ID - Amazon")
        void p2mViaUpiIdAmazon() {
            TransferResult result = service.transferByUpiId(
                "rajesh@upi", "amazon@upi", 2499.0
            );
            assertEquals(2499.0, result.getAmount());
        }

        @IntegrationTest
        @Tag("upi-id")
        @DisplayName("P2M via UPI ID - Swiggy")
        void p2mViaUpiIdSwiggy() {
            TransferResult result = service.transferByUpiId(
                "priya@upi", "swiggy@upi", 350.0
            );
            assertEquals(350.0, result.getAmount());
        }

        @SmokeTest
        @DisplayName("P2M smoke test")
        void p2mSmokeTest() {
            TransferResult result = service.transfer("USER001", "MERCHANT002", 500.0);
            assertNotNull(result);
        }

        @PerformanceTest
        @DisplayName("P2M bulk payments")
        void p2mBulkPayments() {
            long start = System.currentTimeMillis();
            
            for (int i = 0; i < 100; i++) {
                service.transfer("USER001", "MERCHANT001", 100.0);
            }
            
            long elapsed = System.currentTimeMillis() - start;
            assertTrue(elapsed < 2000, "100 P2M payments should complete in < 2s");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // BALANCE INQUIRY TESTS
    // Tags: banking, upi, balance
    // Run: mvn test -Dgroups=balance
    // ═══════════════════════════════════════════════════════════

    @Nested
    @Tag("balance")
    @DisplayName("Balance Inquiry Tests")
    class BalanceInquiryTests {

        @BalanceTest
        @DisplayName("Get balance - fast response")
        void getBalanceFast() {
            double balance = service.getBalance("USER001");
            assertEquals(100000.0, balance);
        }

        @BalanceTest
        @DisplayName("Balance consistency after transfers")
        void balanceConsistency() {
            service.transfer("USER001", "USER002", 10000.0);
            
            assertEquals(90000.0, service.getBalance("USER001"));
            assertEquals(60000.0, service.getBalance("USER002"));
        }

        @PerformanceTest
        @DisplayName("Balance inquiry SLA - 1000 requests")
        void balanceInquirySLA() {
            long start = System.currentTimeMillis();
            
            for (int i = 0; i < 1000; i++) {
                service.getBalance("USER001");
            }
            
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("1000 balance inquiries: " + elapsed + "ms");
            assertTrue(elapsed < 1000, "Balance SLA: 1000 req in < 1s");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ACCOUNT MANAGEMENT TESTS
    // Tags: banking, upi, account
    // Run: mvn test -Dgroups=account
    // ═══════════════════════════════════════════════════════════

    @Nested
    @Tag("account")
    @DisplayName("Account Management Tests")
    class AccountManagementTests {

        @Nested
        @Tag("creation")
        @DisplayName("Account Creation")
        class AccountCreation {

            @UnitTest
            @DisplayName("Create valid account")
            void createValidAccount() {
                Account account = new Account("NEW001", "New User", 5000.0);
                
                assertAll("Account Creation",
                    () -> assertEquals("NEW001", account.getAccountId()),
                    () -> assertEquals("New User", account.getAccountHolderName()),
                    () -> assertEquals(5000.0, account.getBalance())
                );
            }

            @UnitTest
            @DisplayName("Account with zero balance")
            void accountZeroBalance() {
                Account account = new Account("ZERO001", "Zero Balance", 0.0);
                assertEquals(0.0, account.getBalance());
            }
        }

        @Nested
        @Tag("validation")
        @DisplayName("Account Validation")
        class AccountValidation {

            @UnitTest
            @DisplayName("Validate existing account")
            void validateExistingAccount() {
                assertTrue(service.accountExists("USER001"));
                assertTrue(service.accountExists("USER002"));
            }

            @UnitTest
            @DisplayName("Validate non-existent account")
            void validateNonExistentAccount() {
                assertFalse(service.accountExists("FAKE001"));
                assertFalse(service.accountExists("UNKNOWN"));
            }

            @SecurityTest
            @DisplayName("Account validation security")
            void accountValidationSecurity() {
                // Should not expose sensitive info for non-existent accounts
                assertFalse(service.accountExists(""));
                assertFalse(service.accountExists(null));
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // UPI SPECIFIC TESTS
    // Tags: banking, upi, collect/pay
    // Run: mvn test -Dgroups=upi
    // ═══════════════════════════════════════════════════════════

    @Nested
    @Tag("upi-operations")
    @DisplayName("UPI Operations Tests")
    class UPIOperationsTests {

        @Nested
        @Tag("collect")
        @DisplayName("UPI Collect Requests")
        class CollectRequests {

            @IntegrationTest
            @DisplayName("Initiate collect request")
            void initiateCollectRequest() {
                // Simulate collect request (merchant requests payment from user)
                String collectId = UUID.randomUUID().toString();
                assertNotNull(collectId);
                
                // In real implementation, this would create a pending request
                assertTrue(collectId.length() > 0);
            }
        }

        @Nested
        @Tag("pay")
        @DisplayName("UPI Pay Requests")
        class PayRequests {

            @IntegrationTest
            @DisplayName("Direct UPI pay")
            void directUpiPay() {
                TransferResult result = service.transferByUpiId(
                    "amit@upi", "flipkart@upi", 1299.0
                );
                assertEquals(1299.0, result.getAmount());
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // HIGH VALUE TRANSFER TESTS (RTGS/NEFT-like)
    // Tags: banking, transfer, high-value
    // Run: mvn test -Dgroups=high-value
    // ═══════════════════════════════════════════════════════════

    @Nested
    @Tag("transfer")
    @Tag("high-value")
    @DisplayName("High Value Transfer Tests")
    class HighValueTransferTests {

        @RTGSTest
        @DisplayName("RTGS-style high value transfer")
        void rtgsStyleTransfer() {
            // RTGS minimum is typically ₹2 lakh
            TransferResult result = service.transfer("BANK001", "BANK002", 500000.0);
            
            assertEquals(500000.0, result.getAmount());
            assertEquals(9500000.0, result.getSenderBalanceAfter());
            assertEquals(8500000.0, result.getReceiverBalanceAfter());
        }

        @NEFTTest
        @DisplayName("NEFT-style batch transfer")
        void neftStyleTransfer() {
            // NEFT has no minimum amount
            TransferResult result = service.transfer("BANK001", "BANK002", 50000.0);
            assertEquals(50000.0, result.getAmount());
        }

        @CriticalTest
        @DisplayName("High value money conservation")
        void highValueMoneyConservation() {
            double totalBefore = service.getBalance("BANK001") + service.getBalance("BANK002");
            
            service.transfer("BANK001", "BANK002", 1000000.0);
            
            double totalAfter = service.getBalance("BANK001") + service.getBalance("BANK002");
            assertEquals(totalBefore, totalAfter, 0.001);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // CONCURRENT OPERATIONS TESTS
    // Tags: banking, concurrent
    // Run: mvn test -Dgroups=concurrent
    // ═══════════════════════════════════════════════════════════

    @Nested
    @Tag("concurrent")
    @Tag("slow")
    @DisplayName("Concurrent Operations Tests")
    class ConcurrentOperationsTests {

        @SlowTest
        @DisplayName("Concurrent transfers same sender")
        void concurrentTransfersSameSender() throws Exception {
            ExecutorService executor = Executors.newFixedThreadPool(10);
            CountDownLatch latch = new CountDownLatch(10);
            List<TransferResult> results = Collections.synchronizedList(new ArrayList<>());
            
            for (int i = 0; i < 10; i++) {
                String receiver = "USER00" + ((i % 2) + 2);
                executor.submit(() -> {
                    try {
                        results.add(service.transfer("USER001", receiver, 1000.0));
                    } finally {
                        latch.countDown();
                    }
                });
            }
            
            latch.await(10, TimeUnit.SECONDS);
            executor.shutdown();
            
            // All transfers should complete
            assertEquals(10, results.size());
        }

        @SlowTest
        @DisplayName("Concurrent balance inquiries")
        void concurrentBalanceInquiries() throws Exception {
            ExecutorService executor = Executors.newFixedThreadPool(20);
            CountDownLatch latch = new CountDownLatch(100);
            List<Double> balances = Collections.synchronizedList(new ArrayList<>());
            
            for (int i = 0; i < 100; i++) {
                executor.submit(() -> {
                    try {
                        balances.add(service.getBalance("USER001"));
                    } finally {
                        latch.countDown();
                    }
                });
            }
            
            latch.await(5, TimeUnit.SECONDS);
            executor.shutdown();
            
            // All should return same balance
            assertTrue(balances.stream().allMatch(b -> b.equals(100000.0)));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // SECURITY TESTS
    // Tags: banking, security
    // Run: mvn test -Dgroups=security
    // ═══════════════════════════════════════════════════════════

    @Nested
    @Tag("security")
    @DisplayName("Security Tests")
    class SecurityTests {

        @SecurityTest
        @DisplayName("Prevent negative amount transfer")
        void preventNegativeAmount() {
            assertThrows(Exception.class, () -> {
                service.transfer("USER001", "USER002", -1000.0);
            }, "Negative amounts must be rejected");
        }

        @SecurityTest
        @DisplayName("Prevent zero amount transfer")
        void preventZeroAmount() {
            assertThrows(Exception.class, () -> {
                service.transfer("USER001", "USER002", 0.0);
            }, "Zero amount must be rejected");
        }

        @SecurityTest
        @DisplayName("Prevent self transfer")
        void preventSelfTransfer() {
            assertThrows(Exception.class, () -> {
                service.transfer("USER001", "USER001", 1000.0);
            }, "Self transfer must be rejected");
        }

        @SecurityTest
        @DisplayName("Prevent transfer to non-existent account")
        void preventTransferToNonExistent() {
            assertThrows(Exception.class, () -> {
                service.transfer("USER001", "FAKE_ACCOUNT", 1000.0);
            }, "Transfer to fake account must fail");
        }
    }
}
