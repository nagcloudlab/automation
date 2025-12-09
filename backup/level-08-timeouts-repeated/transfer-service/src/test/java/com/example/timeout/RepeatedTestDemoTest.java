package com.example.timeout;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;
import com.example.service.UPITransferService.TransferResult;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Repeated Test Demo - LEVEL 8
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    REPEATED TESTS                             ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  @RepeatedTest runs the same test multiple times:             ║
 * ║                                                               ║
 * ║  Use Cases:                                                   ║
 * ║  • Detect flaky tests                                         ║
 * ║  • Find race conditions                                       ║
 * ║  • Statistical confidence                                     ║
 * ║  • Simple load testing                                        ║
 * ║  • Verify consistent behavior                                 ║
 * ║                                                               ║
 * ║  Features:                                                    ║
 * ║  • RepetitionInfo for repetition details                      ║
 * ║  • Custom display names                                       ║
 * ║  • Combine with @Timeout                                      ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Repeated Test Demo")
class RepeatedTestDemoTest {

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
    // BASIC @RepeatedTest EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Basic @RepeatedTest")
    class BasicRepeatedTests {

        @RepeatedTest(5)
        @DisplayName("Should transfer consistently")
        void shouldTransferConsistently() {
            TransferResult result = service.transfer("SENDER", "RECEIVER", 1000.0);
            
            assertNotNull(result);
            assertEquals(1000.0, result.getAmount());
        }

        @RepeatedTest(3)
        @DisplayName("Balance inquiry should be consistent")
        void balanceInquiryShouldBeConsistent() {
            double balance = service.getBalance("SENDER");
            assertEquals(100000.0, balance);
        }

        @RepeatedTest(10)
        @DisplayName("Account validation should always work")
        void accountValidationAlwaysWorks() {
            assertTrue(service.accountExists("SENDER"));
            assertTrue(service.accountExists("RECEIVER"));
            assertFalse(service.accountExists("UNKNOWN"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // RepetitionInfo EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("RepetitionInfo Usage")
    class RepetitionInfoTests {

        @RepeatedTest(5)
        @DisplayName("Access repetition details")
        void accessRepetitionDetails(RepetitionInfo info) {
            int current = info.getCurrentRepetition();
            int total = info.getTotalRepetitions();
            
            System.out.println("  → Running repetition " + current + " of " + total);
            
            assertTrue(current >= 1 && current <= total);
            assertEquals(5, total);
        }

        @RepeatedTest(5)
        @DisplayName("Vary transfer amount by repetition")
        void varyAmountByRepetition(RepetitionInfo info) {
            // Amount increases with each repetition
            double amount = info.getCurrentRepetition() * 1000.0;
            
            System.out.println("  → Transferring ₹" + amount);
            
            TransferResult result = service.transfer("SENDER", "RECEIVER", amount);
            assertEquals(amount, result.getAmount());
        }

        @RepeatedTest(3)
        @DisplayName("Different scenarios per repetition")
        void differentScenariosPerRepetition(RepetitionInfo info) {
            double amount;
            String description;
            
            switch (info.getCurrentRepetition()) {
                case 1:
                    amount = 100.0;
                    description = "Small transfer";
                    break;
                case 2:
                    amount = 10000.0;
                    description = "Medium transfer";
                    break;
                case 3:
                    amount = 50000.0;
                    description = "Large transfer";
                    break;
                default:
                    amount = 1000.0;
                    description = "Default transfer";
            }
            
            System.out.println("  → " + description + ": ₹" + amount);
            
            TransferResult result = service.transfer("SENDER", "RECEIVER", amount);
            assertEquals(amount, result.getAmount());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // CUSTOM DISPLAY NAMES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Custom Display Names")
    class CustomDisplayNameTests {

        @RepeatedTest(value = 3, name = "{displayName} - Iteration {currentRepetition}/{totalRepetitions}")
        @DisplayName("UPI Transfer Test")
        void customNameWithDisplayName() {
            // Output: "UPI Transfer Test - Iteration 1/3"
            service.transfer("SENDER", "RECEIVER", 1000.0);
        }

        @RepeatedTest(value = 3, name = "Run #{currentRepetition}")
        @DisplayName("Simple numbered test")
        void simpleNumberedTest() {
            // Output: "Run #1", "Run #2", "Run #3"
            assertTrue(service.accountExists("SENDER"));
        }

        @RepeatedTest(value = 3, name = RepeatedTest.LONG_DISPLAY_NAME)
        @DisplayName("Long format test")
        void longFormatTest() {
            // Output: "repetition 1 of 3"
            assertNotNull(service.getBalance("SENDER"));
        }

        @RepeatedTest(value = 3, name = RepeatedTest.SHORT_DISPLAY_NAME)
        @DisplayName("Short format test")
        void shortFormatTest() {
            // Output: "1/3"
            assertTrue(true);
        }

        @RepeatedTest(value = 5, name = "Testing amount ₹{currentRepetition}000")
        @DisplayName("Amount-based naming")
        void amountBasedNaming(RepetitionInfo info) {
            // Output: "Testing amount ₹1000", "Testing amount ₹2000", etc.
            double amount = info.getCurrentRepetition() * 1000.0;
            service.transfer("SENDER", "RECEIVER", amount);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // PROGRESSIVE LOAD TESTING
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Progressive Load Testing")
    class ProgressiveLoadTests {

        @RepeatedTest(value = 5, name = "Load level {currentRepetition}: {currentRepetition}0 transactions")
        @DisplayName("Progressive load test")
        void progressiveLoadTest(RepetitionInfo info) {
            int transactionCount = info.getCurrentRepetition() * 10;
            
            System.out.println("  → Processing " + transactionCount + " transactions");
            
            // Reset for each load level
            repository.loadAccountById("SENDER");  // 100000
            
            for (int i = 0; i < transactionCount; i++) {
                service.transfer("SENDER", "RECEIVER", 100.0);
            }
            
            double expectedSenderBalance = 100000.0 - (transactionCount * 100.0);
            assertEquals(expectedSenderBalance, repository.loadAccountById("SENDER").getBalance());
        }

        @RepeatedTest(value = 3, name = "Concurrent simulation level {currentRepetition}")
        @DisplayName("Simulated concurrent load")
        void simulatedConcurrentLoad(RepetitionInfo info) {
            int operations = info.getCurrentRepetition() * 5;
            List<Double> balances = new ArrayList<>();
            
            for (int i = 0; i < operations; i++) {
                service.transfer("SENDER", "RECEIVER", 100.0);
                balances.add(service.getBalance("SENDER"));
            }
            
            // Each balance should be less than the previous
            for (int i = 1; i < balances.size(); i++) {
                assertTrue(balances.get(i) < balances.get(i - 1),
                    "Balances should decrease: " + balances.get(i) + " < " + balances.get(i - 1));
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // COMBINING TIMEOUTS AND REPETITIONS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Timeouts + Repetitions")
    class TimeoutsAndRepetitionsTests {

        @RepeatedTest(10)
        @Timeout(value = 500, unit = java.util.concurrent.TimeUnit.MILLISECONDS)
        @DisplayName("Each repetition must meet SLA")
        void eachRepetitionMustMeetSLA() {
            TransferResult result = service.transfer("SENDER", "RECEIVER", 1000.0);
            assertNotNull(result);
        }

        @RepeatedTest(5)
        @DisplayName("Timeout assertion per repetition")
        void timeoutAssertionPerRepetition(RepetitionInfo info) {
            // Stricter timeout for later repetitions (system should warm up)
            long timeoutMs = 500 - (info.getCurrentRepetition() * 50);  // 450, 400, 350, 300, 250
            timeoutMs = Math.max(timeoutMs, 100);  // Minimum 100ms
            
            System.out.println("  → Timeout: " + timeoutMs + "ms");
            
            assertTimeout(
                Duration.ofMillis(timeoutMs),
                () -> service.transfer("SENDER", "RECEIVER", 1000.0),
                "Must complete within " + timeoutMs + "ms"
            );
        }

        @RepeatedTest(value = 3, name = "SLA check #{currentRepetition}")
        @DisplayName("Repeated SLA verification")
        void repeatedSLAVerification() {
            // Transfer SLA
            assertTimeout(
                Duration.ofMillis(500),
                () -> service.transfer("SENDER", "RECEIVER", 1000.0)
            );
            
            // Balance SLA
            assertTimeout(
                Duration.ofMillis(200),
                () -> service.getBalance("SENDER")
            );
        }
    }

    // ═══════════════════════════════════════════════════════════
    // FLAKY TEST DETECTION
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Flaky Test Detection")
    class FlakyTestDetection {

        private AtomicInteger passCount = new AtomicInteger(0);
        private AtomicInteger failCount = new AtomicInteger(0);

        @RepeatedTest(value = 20, name = "Consistency check {currentRepetition}/20")
        @DisplayName("Detect inconsistent behavior")
        void detectInconsistentBehavior() {
            // If this fails even once in 20 runs, investigate!
            TransferResult result = service.transfer("SENDER", "RECEIVER", 100.0);
            
            try {
                assertNotNull(result);
                assertTrue(result.getAmount() > 0);
                passCount.incrementAndGet();
            } catch (AssertionError e) {
                failCount.incrementAndGet();
                throw e;
            }
        }

        @RepeatedTest(value = 10, name = "Balance conservation check #{currentRepetition}")
        @DisplayName("Balance conservation should always hold")
        void balanceConservationAlwaysHolds() {
            double totalBefore = repository.getTotalBalance();
            
            service.transfer("SENDER", "RECEIVER", 1000.0);
            
            double totalAfter = repository.getTotalBalance();
            
            assertEquals(totalBefore, totalAfter, 0.001,
                "Total balance must be conserved (flaky if this fails)");
        }

        @RepeatedTest(value = 5, name = "Fresh state verification #{currentRepetition}")
        @DisplayName("Each repetition should have fresh state")
        void freshStatePerRepetition(RepetitionInfo info) {
            // @BeforeEach runs before EACH repetition
            // So sender should always start with 100000
            assertEquals(100000.0, repository.loadAccountById("SENDER").getBalance(),
                "Sender should have fresh balance for repetition " + info.getCurrentRepetition());
            
            // Do a transfer
            service.transfer("SENDER", "RECEIVER", 5000.0);
            
            // Balance is now 95000 for THIS repetition
            assertEquals(95000.0, repository.loadAccountById("SENDER").getBalance());
            
            // Next repetition will reset to 100000 via @BeforeEach
        }
    }

    // ═══════════════════════════════════════════════════════════
    // STATISTICAL TESTING
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Statistical Testing")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class StatisticalTests {

        private List<Long> executionTimes = new ArrayList<>();

        @BeforeAll
        void initStats() {
            executionTimes.clear();
        }

        @RepeatedTest(value = 10, name = "Performance sample #{currentRepetition}")
        @DisplayName("Collect performance samples")
        void collectPerformanceSamples() {
            // Need fresh state per test since we're PER_CLASS
            InMemoryAccountRepository localRepo = new InMemoryAccountRepository();
            localRepo.addAccount(new Account("A", "A", 10000.0));
            localRepo.addAccount(new Account("B", "B", 5000.0));
            UPITransferService localService = new UPITransferService(localRepo);
            
            long start = System.nanoTime();
            localService.transfer("A", "B", 1000.0);
            long elapsed = (System.nanoTime() - start) / 1_000_000;  // ms
            
            executionTimes.add(elapsed);
            System.out.println("  → Execution time: " + elapsed + "ms");
        }

        @AfterAll
        void reportStats() {
            if (executionTimes.isEmpty()) return;
            
            double avg = executionTimes.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);
            
            long max = executionTimes.stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);
            
            long min = executionTimes.stream()
                .mapToLong(Long::longValue)
                .min()
                .orElse(0);
            
            System.out.println("\n╔═══════════════════════════════════════╗");
            System.out.println("║       PERFORMANCE STATISTICS          ║");
            System.out.println("╠═══════════════════════════════════════╣");
            System.out.printf("║  Samples: %d                          ║%n", executionTimes.size());
            System.out.printf("║  Average: %.2f ms                     ║%n", avg);
            System.out.printf("║  Min:     %d ms                        ║%n", min);
            System.out.printf("║  Max:     %d ms                        ║%n", max);
            System.out.println("╚═══════════════════════════════════════╝\n");
        }
    }
}
