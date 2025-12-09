package com.example.tags;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;
import com.example.service.UPITransferService.TransferResult;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tag Demo Test - LEVEL 9
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                      @Tag DEMONSTRATION                       ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Tags allow you to categorize and filter tests:               ║
 * ║                                                               ║
 * ║  Run specific tags:                                           ║
 * ║    mvn test -Dgroups=unit                                     ║
 * ║    mvn test -Dgroups="unit | integration"                     ║
 * ║    mvn test -Dgroups="banking & transfer"                     ║
 * ║                                                               ║
 * ║  Exclude tags:                                                ║
 * ║    mvn test -DexcludedGroups=slow                             ║
 * ║    mvn test -DexcludedGroups="slow | manual"                  ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Tag Demonstration Tests")
class TagDemoTest {

    private InMemoryAccountRepository repository;
    private UPITransferService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        service = new UPITransferService(repository);
        repository.addAccount(new Account("USER001", "Rajesh", 100000.0));
        repository.addAccount(new Account("USER002", "Priya", 50000.0));
        repository.addAccount(new Account("MERCHANT", "SuperMart", 0.0));
    }

    // ═══════════════════════════════════════════════════════════
    // SINGLE TAG EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Test
    @Tag("unit")
    @DisplayName("Unit test - basic validation")
    void unitTest_basicValidation() {
        // Run: mvn test -Dgroups=unit
        Account account = repository.loadAccountById("USER001");
        assertNotNull(account);
        assertEquals("Rajesh", account.getAccountHolderName());
    }

    @Test
    @Tag("integration")
    @DisplayName("Integration test - full transfer")
    void integrationTest_fullTransfer() {
        // Run: mvn test -Dgroups=integration
        TransferResult result = service.transfer("USER001", "USER002", 5000.0);
        
        assertNotNull(result);
        assertEquals(95000.0, repository.loadAccountById("USER001").getBalance());
        assertEquals(55000.0, repository.loadAccountById("USER002").getBalance());
    }

    @Test
    @Tag("slow")
    @DisplayName("Slow test - batch processing")
    void slowTest_batchProcessing() {
        // Run: mvn test -Dgroups=slow
        // Exclude: mvn test -DexcludedGroups=slow
        for (int i = 0; i < 100; i++) {
            service.transfer("USER001", "USER002", 100.0);
        }
        assertEquals(90000.0, repository.loadAccountById("USER001").getBalance());
    }

    @Test
    @Tag("smoke")
    @DisplayName("Smoke test - basic health check")
    void smokeTest_healthCheck() {
        // Run: mvn test -Dgroups=smoke
        assertTrue(service.accountExists("USER001"));
        assertTrue(service.getBalance("USER001") > 0);
    }

    // ═══════════════════════════════════════════════════════════
    // MULTIPLE TAGS EXAMPLES
    // ═══════════════════════════════════════════════════════════

    @Test
    @Tag("unit")
    @Tag("fast")
    @Tag("account")
    @DisplayName("Unit + Fast + Account - balance check")
    void multiTag_unitFastAccount() {
        // Run: mvn test -Dgroups="unit & fast"
        // Run: mvn test -Dgroups=account
        double balance = service.getBalance("USER001");
        assertEquals(100000.0, balance);
    }

    @Test
    @Tag("integration")
    @Tag("banking")
    @Tag("transfer")
    @DisplayName("Integration + Banking + Transfer")
    void multiTag_integrationBankingTransfer() {
        // Run: mvn test -Dgroups="banking & transfer"
        TransferResult result = service.transfer("USER001", "USER002", 10000.0);
        assertEquals(10000.0, result.getAmount());
    }

    @Test
    @Tag("critical")
    @Tag("smoke")
    @Tag("p2p")
    @DisplayName("Critical P2P smoke test")
    void multiTag_criticalSmokeP2P() {
        // Run: mvn test -Dgroups="critical | smoke"
        TransferResult result = service.transfer("USER001", "USER002", 1000.0);
        assertNotNull(result);
        assertTrue(result.getAmount() > 0);
    }

    // ═══════════════════════════════════════════════════════════
    // BANKING DOMAIN TAGS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @Tag("banking")
    @Tag("transfer")
    @DisplayName("Banking Transfer Tests")
    class BankingTransferTests {

        @Test
        @Tag("p2p")
        @DisplayName("P2P Transfer - Rajesh to Priya")
        void p2pTransfer() {
            // Run: mvn test -Dgroups=p2p
            TransferResult result = service.transfer("USER001", "USER002", 5000.0);
            assertEquals(5000.0, result.getAmount());
        }

        @Test
        @Tag("p2m")
        @DisplayName("P2M Payment - User to Merchant")
        void p2mPayment() {
            // Run: mvn test -Dgroups=p2m
            TransferResult result = service.transfer("USER001", "MERCHANT", 2500.0);
            assertEquals(2500.0, result.getReceiverBalanceAfter());
        }

        @Test
        @Tag("p2p")
        @Tag("edge-case")
        @DisplayName("P2P Edge Case - Exact balance transfer")
        void p2pEdgeCase_exactBalance() {
            // Run: mvn test -Dgroups="p2p & edge-case"
            repository.addAccount(new Account("EXACT", "Exact Balance", 1000.0));
            TransferResult result = service.transfer("EXACT", "USER002", 1000.0);
            assertEquals(0.0, result.getSenderBalanceAfter());
        }
    }

    @Nested
    @Tag("banking")
    @Tag("balance")
    @DisplayName("Banking Balance Tests")
    class BankingBalanceTests {

        @Test
        @DisplayName("Get account balance")
        void getBalance() {
            // Run: mvn test -Dgroups=balance
            double balance = service.getBalance("USER001");
            assertEquals(100000.0, balance);
        }

        @Test
        @Tag("critical")
        @DisplayName("Balance after transfer")
        void balanceAfterTransfer() {
            // Run: mvn test -Dgroups="balance & critical"
            service.transfer("USER001", "USER002", 10000.0);
            assertEquals(90000.0, service.getBalance("USER001"));
            assertEquals(60000.0, service.getBalance("USER002"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // PRIORITY/CATEGORY TAGS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Priority Tagged Tests")
    class PriorityTests {

        @Test
        @Tag("critical")
        @Tag("priority-high")
        @DisplayName("Critical - Transaction must not lose money")
        void critical_transactionIntegrity() {
            // Run: mvn test -Dgroups=critical
            double totalBefore = repository.getTotalBalance();
            
            service.transfer("USER001", "USER002", 25000.0);
            
            double totalAfter = repository.getTotalBalance();
            assertEquals(totalBefore, totalAfter, 0.001,
                "CRITICAL: Money must be conserved!");
        }

        @Test
        @Tag("regression")
        @DisplayName("Regression - Bug #123 fix verification")
        void regression_bug123() {
            // Run: mvn test -Dgroups=regression
            // This test ensures a previously fixed bug doesn't return
            Account account = repository.loadAccountById("USER001");
            assertNotNull(account.getAccountHolderName());
        }

        @Test
        @Tag("security")
        @DisplayName("Security - No negative transfers")
        void security_noNegativeTransfers() {
            // Run: mvn test -Dgroups=security
            assertThrows(Exception.class, () -> {
                service.transfer("USER001", "USER002", -1000.0);
            }, "Negative transfers must be rejected");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ENVIRONMENT TAGS
    // ═══════════════════════════════════════════════════════════

    @Test
    @Tag("ci")
    @Tag("automated")
    @DisplayName("CI test - runs on every build")
    void ciTest_everyBuild() {
        // Run: mvn test -Dgroups=ci
        assertNotNull(repository);
        assertNotNull(service);
    }

    @Test
    @Tag("nightly")
    @Tag("slow")
    @DisplayName("Nightly test - extensive validation")
    void nightlyTest_extensiveValidation() {
        // Run: mvn test -Dgroups=nightly
        // Typically excluded in regular builds
        for (int i = 0; i < 50; i++) {
            String senderId = "USER00" + (i % 2 + 1);
            String receiverId = "USER00" + ((i + 1) % 2 + 1);
            service.transfer(senderId, receiverId, 100.0);
        }
        assertEquals(150000.0, repository.getTotalBalance(), 0.001);
    }

    @Test
    @Tag("manual")
    @DisplayName("Manual test - requires human verification")
    void manualTest_humanVerification() {
        // Exclude in CI: mvn test -DexcludedGroups=manual
        System.out.println("This test requires manual verification:");
        System.out.println("  → Check that transfer notification is sent");
        System.out.println("  → Verify audit log entry is created");
        assertTrue(true, "Placeholder - actual verification is manual");
    }
}
