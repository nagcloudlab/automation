package com.example.timeout;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;
import com.example.service.UPITransferService.TransferResult;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Timeout Demo - LEVEL 8
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                     TIMEOUT MECHANISMS                        ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  JUnit 5 provides multiple timeout mechanisms:                ║
 * ║                                                               ║
 * ║  @Timeout annotation:                                         ║
 * ║    • Declarative timeout at method/class level                ║
 * ║    • Configurable time units                                  ║
 * ║                                                               ║
 * ║  assertTimeout():                                             ║
 * ║    • Waits for completion, then checks duration               ║
 * ║    • Returns result for further assertions                    ║
 * ║                                                               ║
 * ║  assertTimeoutPreemptively():                                 ║
 * ║    • Aborts execution when timeout reached                    ║
 * ║    • Runs in separate thread                                  ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * BANKING CONTEXT:
 * ================
 * NPCI mandates strict SLAs for UPI transactions:
 * - Transaction Request: < 500ms
 * - Balance Inquiry: < 200ms
 * - Complete Transfer: < 2 seconds
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Timeout Demo Tests")
@Timeout(5)  // Default 5-second timeout for all tests in this class
class TimeoutDemoTest {

    private InMemoryAccountRepository repository;
    private UPITransferService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        service = new UPITransferService(repository);
        repository.addAccount(new Account("SENDER", "Rajesh", 100000.0));
        repository.addAccount(new Account("RECEIVER", "Priya", 50000.0));
    }

    // ═══════════════════════════════════════════════════════════
    // @Timeout ANNOTATION EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@Timeout Annotation Examples")
    class TimeoutAnnotationTests {

        @Test
        @Timeout(3)  // 3 seconds (default unit)
        @DisplayName("Should complete within 3 seconds")
        void shouldCompleteWithin3Seconds() {
            // This should pass - transfer is fast
            TransferResult result = service.transfer("SENDER", "RECEIVER", 1000.0);
            assertNotNull(result);
        }

        @Test
        @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
        @DisplayName("Should complete within 500ms (UPI SLA)")
        void shouldCompleteWithin500Ms() {
            // UPI transaction SLA: 500ms
            TransferResult result = service.transfer("SENDER", "RECEIVER", 5000.0);
            assertEquals(5000.0, result.getAmount());
        }

        @Test
        @Timeout(value = 200, unit = TimeUnit.MILLISECONDS)
        @DisplayName("Balance inquiry should complete within 200ms")
        void balanceInquiryShouldBeQuick() {
            // Balance inquiry SLA: 200ms
            double balance = service.getBalance("SENDER");
            assertEquals(100000.0, balance);
        }

        @Test
        @Timeout(value = 1, unit = TimeUnit.MINUTES)
        @DisplayName("Batch processing can take up to 1 minute")
        void batchProcessingWithLongerTimeout() {
            // Batch operations have longer SLA
            for (int i = 0; i < 100; i++) {
                service.transfer("SENDER", "RECEIVER", 10.0);
            }
            assertEquals(99000.0, repository.loadAccountById("SENDER").getBalance());
        }

        // This test demonstrates what happens when timeout is exceeded
        // Uncomment to see timeout failure
        // @Test
        // @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
        // @DisplayName("This test will FAIL due to timeout")
        // void willTimeoutAndFail() throws InterruptedException {
        //     Thread.sleep(500);  // Sleep longer than timeout
        //     assertTrue(true);  // Never reached
        // }
    }

    // ═══════════════════════════════════════════════════════════
    // assertTimeout() EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("assertTimeout() Examples")
    class AssertTimeoutTests {

        @Test
        @DisplayName("Transfer should complete within SLA")
        void transferShouldMeetSLA() {
            TransferResult result = assertTimeout(
                Duration.ofMillis(500),
                () -> service.transfer("SENDER", "RECEIVER", 1000.0),
                "Transfer must complete within 500ms SLA"
            );
            
            // assertTimeout returns the result - can use it!
            assertNotNull(result);
            assertEquals(1000.0, result.getAmount());
            assertEquals(99000.0, result.getSenderBalanceAfter());
        }

        @Test
        @DisplayName("Multiple operations within timeout")
        void multipleOperationsWithinTimeout() {
            assertTimeout(Duration.ofSeconds(2), () -> {
                // All these must complete within 2 seconds total
                service.transfer("SENDER", "RECEIVER", 1000.0);
                service.transfer("SENDER", "RECEIVER", 2000.0);
                service.transfer("SENDER", "RECEIVER", 3000.0);
                
                double senderBalance = service.getBalance("SENDER");
                assertEquals(94000.0, senderBalance);
            });
        }

        @Test
        @DisplayName("Balance inquiry performance test")
        void balanceInquiryPerformance() {
            // 100 balance inquiries should complete within 1 second
            assertTimeout(Duration.ofSeconds(1), () -> {
                for (int i = 0; i < 100; i++) {
                    service.getBalance("SENDER");
                }
            }, "100 balance inquiries should complete within 1 second");
        }

        @Test
        @DisplayName("Get result from timed operation")
        void getResultFromTimedOperation() {
            // assertTimeout returns the value from the lambda
            Double balance = assertTimeout(
                Duration.ofMillis(200),
                () -> service.getBalance("SENDER")
            );
            
            assertEquals(100000.0, balance);
        }

        @Test
        @DisplayName("Verify account exists within timeout")
        void accountExistsWithinTimeout() {
            boolean exists = assertTimeout(
                Duration.ofMillis(100),
                () -> service.accountExists("SENDER")
            );
            
            assertTrue(exists);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // assertTimeoutPreemptively() EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("assertTimeoutPreemptively() Examples")
    class AssertTimeoutPreemptivelyTests {

        @Test
        @DisplayName("Preemptive timeout - aborts execution")
        void preemptiveTimeoutAbortsExecution() {
            // This will ABORT at timeout, not wait for completion
            TransferResult result = assertTimeoutPreemptively(
                Duration.ofMillis(500),
                () -> service.transfer("SENDER", "RECEIVER", 1000.0),
                "Transfer aborted - exceeded 500ms"
            );
            
            assertNotNull(result);
        }

        @Test
        @DisplayName("Preemptive timeout prevents hanging")
        void preemptiveTimeoutPreventsHanging() {
            // Use when operation might hang indefinitely
            assertTimeoutPreemptively(
                Duration.ofSeconds(1),
                () -> {
                    // Simulate quick operation
                    for (int i = 0; i < 10; i++) {
                        service.transfer("SENDER", "RECEIVER", 100.0);
                    }
                }
            );
            
            assertEquals(99000.0, repository.loadAccountById("SENDER").getBalance());
        }

        // Demonstrates the difference: preemptive STOPS execution
        // Uncomment to see the difference
        // @Test
        // @DisplayName("Preemptive timeout STOPS at exactly timeout")
        // void demonstratePreemptiveStop() {
        //     AtomicInteger counter = new AtomicInteger(0);
        //     
        //     assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
        //         while (true) {
        //             counter.incrementAndGet();
        //             Thread.sleep(10);
        //         }
        //     });
        //     
        //     // Counter will be around 10 (100ms / 10ms sleep)
        //     System.out.println("Counter reached: " + counter.get());
        // }
    }

    // ═══════════════════════════════════════════════════════════
    // SLA VERIFICATION TESTS (Banking Context)
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Banking SLA Verification")
    class BankingSLATests {

        @Test
        @DisplayName("UPI P2P Transfer SLA: < 500ms")
        void upiP2PTransferSLA() {
            assertTimeout(
                Duration.ofMillis(500),
                () -> service.transfer("SENDER", "RECEIVER", 5000.0),
                "UPI P2P transfer must complete within 500ms"
            );
        }

        @Test
        @DisplayName("UPI Balance Check SLA: < 200ms")
        void upiBalanceCheckSLA() {
            assertTimeout(
                Duration.ofMillis(200),
                () -> service.getBalance("SENDER"),
                "Balance check must complete within 200ms"
            );
        }

        @Test
        @DisplayName("Account Validation SLA: < 100ms")
        void accountValidationSLA() {
            assertTimeout(
                Duration.ofMillis(100),
                () -> service.accountExists("SENDER"),
                "Account validation must complete within 100ms"
            );
        }

        @Test
        @DisplayName("Multiple transfers under aggregate SLA")
        void multipleTransfersAggregateSLA() {
            // 5 transfers should complete within 2.5 seconds (500ms each)
            assertTimeout(Duration.ofMillis(2500), () -> {
                for (int i = 0; i < 5; i++) {
                    service.transfer("SENDER", "RECEIVER", 1000.0);
                }
            }, "5 transfers should complete within aggregate SLA");
        }

        @Test
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        @DisplayName("End-to-end transfer flow: < 2 seconds")
        void endToEndTransferFlow() {
            // Complete flow: validate → transfer → verify
            assertTrue(service.accountExists("SENDER"));
            assertTrue(service.accountExists("RECEIVER"));
            
            TransferResult result = service.transfer("SENDER", "RECEIVER", 10000.0);
            
            assertEquals(90000.0, result.getSenderBalanceAfter());
            assertEquals(60000.0, result.getReceiverBalanceAfter());
            
            // Verify persistence
            assertEquals(90000.0, service.getBalance("SENDER"));
            assertEquals(60000.0, service.getBalance("RECEIVER"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // TIMEOUT EDGE CASES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Timeout Edge Cases")
    class TimeoutEdgeCases {

        @Test
        @DisplayName("Instant operation completes well under timeout")
        void instantOperationUnderTimeout() {
            long startTime = System.currentTimeMillis();
            
            assertTimeout(Duration.ofSeconds(5), () -> {
                // This completes instantly
                int sum = 1 + 1;
                assertEquals(2, sum);
            });
            
            long elapsed = System.currentTimeMillis() - startTime;
            assertTrue(elapsed < 100, "Should complete almost instantly");
        }

        @Test
        @DisplayName("Timeout includes setup time")
        void timeoutIncludesSetup() {
            assertTimeout(Duration.ofSeconds(1), () -> {
                // Setup
                InMemoryAccountRepository localRepo = new InMemoryAccountRepository();
                localRepo.addAccount(new Account("A", "A", 1000.0));
                localRepo.addAccount(new Account("B", "B", 1000.0));
                UPITransferService localService = new UPITransferService(localRepo);
                
                // Operation
                localService.transfer("A", "B", 500.0);
                
                // Verification
                assertEquals(500.0, localRepo.loadAccountById("A").getBalance());
            });
        }

        @Test
        @DisplayName("Chained assertions within timeout")
        void chainedAssertionsWithinTimeout() {
            assertTimeout(Duration.ofSeconds(1), () -> {
                TransferResult result = service.transfer("SENDER", "RECEIVER", 2500.0);
                
                assertAll("Transfer details",
                    () -> assertEquals(2500.0, result.getAmount()),
                    () -> assertEquals(100000.0, result.getSenderBalanceBefore()),
                    () -> assertEquals(97500.0, result.getSenderBalanceAfter()),
                    () -> assertEquals(50000.0, result.getReceiverBalanceBefore()),
                    () -> assertEquals(52500.0, result.getReceiverBalanceAfter())
                );
            });
        }
    }
}
