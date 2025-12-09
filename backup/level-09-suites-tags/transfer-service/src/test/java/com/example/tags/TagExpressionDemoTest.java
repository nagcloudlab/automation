package com.example.tags;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;
import com.example.service.UPITransferService.TransferResult;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tag Expression Demo - LEVEL 9
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    TAG EXPRESSIONS                            ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Tag expressions allow complex filtering:                     ║
 * ║                                                               ║
 * ║  Operators:                                                   ║
 * ║    !      NOT       !slow                                     ║
 * ║    &      AND       unit & fast                               ║
 * ║    |      OR        unit | integration                        ║
 * ║    ()     Group     (unit | integration) & !slow              ║
 * ║                                                               ║
 * ║  Examples:                                                    ║
 * ║    mvn test -Dgroups="unit"                                   ║
 * ║    mvn test -Dgroups="unit | integration"                     ║
 * ║    mvn test -Dgroups="banking & transfer & !slow"             ║
 * ║    mvn test -DexcludedGroups="slow | manual"                  ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Tag Expression Demo")
class TagExpressionDemoTest {

    private InMemoryAccountRepository repository;
    private UPITransferService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        service = new UPITransferService(repository);
        repository.addAccount(new Account("USER001", "Rajesh Kumar", 100000.0));
        repository.addAccount(new Account("USER002", "Priya Sharma", 50000.0));
        repository.addAccount(new Account("MERCHANT001", "Amazon Pay", 0.0));
        repository.addAccount(new Account("MERCHANT002", "Flipkart Pay", 0.0));
    }

    // ═══════════════════════════════════════════════════════════
    // EXPRESSION: unit & fast
    // Run: mvn test -Dgroups="unit & fast"
    // These tests MUST have BOTH tags to be included
    // ═══════════════════════════════════════════════════════════

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("[unit & fast] Account validation")
    void unitAndFast_accountValidation() {
        assertTrue(service.accountExists("USER001"));
        assertFalse(service.accountExists("NONEXISTENT"));
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @DisplayName("[unit & fast] Balance check")
    void unitAndFast_balanceCheck() {
        assertEquals(100000.0, service.getBalance("USER001"));
    }

    @Test
    @Tag("unit")
    @Tag("fast")
    @Tag("account")
    @DisplayName("[unit & fast & account] Account creation")
    void unitFastAccount_creation() {
        Account account = new Account("NEW001", "New User", 5000.0);
        assertNotNull(account);
        assertEquals("New User", account.getAccountHolderName());
    }

    // ═══════════════════════════════════════════════════════════
    // EXPRESSION: unit | integration
    // Run: mvn test -Dgroups="unit | integration"
    // These tests need EITHER tag to be included
    // ═══════════════════════════════════════════════════════════

    @Test
    @Tag("unit")
    @DisplayName("[unit] Only unit tag")
    void unitOnly_simpleValidation() {
        Account account = repository.loadAccountById("USER001");
        assertNotNull(account);
    }

    @Test
    @Tag("integration")
    @DisplayName("[integration] Only integration tag")
    void integrationOnly_fullFlow() {
        TransferResult result = service.transfer("USER001", "USER002", 5000.0);
        assertNotNull(result);
        assertEquals(5000.0, result.getAmount());
    }

    @Test
    @Tag("unit")
    @Tag("integration")
    @DisplayName("[unit | integration] Both tags")
    void unitOrIntegration_bothTags() {
        // This test runs for both expressions
        TransferResult result = service.transfer("USER001", "USER002", 1000.0);
        assertEquals(1000.0, result.getAmount());
    }

    // ═══════════════════════════════════════════════════════════
    // EXPRESSION: !slow
    // Run: mvn test -Dgroups="!slow"
    // Run: mvn test -DexcludedGroups="slow"
    // These tests are EXCLUDED when running slow tests
    // ═══════════════════════════════════════════════════════════

    @Test
    @Tag("fast")
    @DisplayName("[!slow] Quick balance inquiry")
    void notSlow_quickBalance() {
        // This runs when excluding slow tests
        double balance = service.getBalance("USER001");
        assertTrue(balance > 0);
    }

    @Test
    @Tag("slow")
    @DisplayName("[slow] Batch processing (excluded by !slow)")
    void slow_batchProcessing() {
        // This is EXCLUDED when running !slow
        for (int i = 0; i < 100; i++) {
            service.transfer("USER001", "USER002", 100.0);
        }
        assertEquals(90000.0, service.getBalance("USER001"));
    }

    // ═══════════════════════════════════════════════════════════
    // EXPRESSION: banking & transfer & !slow
    // Run: mvn test -Dgroups="banking & transfer & !slow"
    // Banking transfer tests that are NOT slow
    // ═══════════════════════════════════════════════════════════

    @Test
    @Tag("banking")
    @Tag("transfer")
    @Tag("fast")
    @DisplayName("[banking & transfer & !slow] Quick P2P")
    void bankingTransferNotSlow_quickP2P() {
        TransferResult result = service.transfer("USER001", "USER002", 1000.0);
        assertEquals(1000.0, result.getAmount());
    }

    @Test
    @Tag("banking")
    @Tag("transfer")
    @Tag("slow")
    @DisplayName("[banking & transfer & slow] EXCLUDED")
    void bankingTransferSlow_excluded() {
        // This is EXCLUDED by the !slow in the expression
        for (int i = 0; i < 50; i++) {
            service.transfer("USER001", "USER002", 100.0);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // EXPRESSION: (p2p | p2m) & banking
    // Run: mvn test -Dgroups="(p2p | p2m) & banking"
    // Either P2P or P2M, but must be banking
    // ═══════════════════════════════════════════════════════════

    @Test
    @Tag("banking")
    @Tag("p2p")
    @DisplayName("[(p2p | p2m) & banking] P2P transfer")
    void p2pOrP2mAndBanking_p2p() {
        TransferResult result = service.transfer("USER001", "USER002", 2500.0);
        assertEquals(2500.0, result.getAmount());
    }

    @Test
    @Tag("banking")
    @Tag("p2m")
    @DisplayName("[(p2p | p2m) & banking] P2M payment")
    void p2pOrP2mAndBanking_p2m() {
        TransferResult result = service.transfer("USER001", "MERCHANT001", 1500.0);
        assertEquals(1500.0, result.getAmount());
    }

    @Test
    @Tag("p2p")
    @DisplayName("[p2p without banking] NOT included")
    void p2pWithoutBanking_notIncluded() {
        // This is NOT included because it lacks 'banking' tag
        TransferResult result = service.transfer("USER001", "USER002", 500.0);
        assertNotNull(result);
    }

    // ═══════════════════════════════════════════════════════════
    // EXPRESSION: smoke | critical
    // Run: mvn test -Dgroups="smoke | critical"
    // Either smoke tests or critical tests
    // ═══════════════════════════════════════════════════════════

    @Test
    @Tag("smoke")
    @DisplayName("[smoke | critical] Basic smoke test")
    void smokeOrCritical_smoke() {
        assertTrue(service.accountExists("USER001"));
        assertTrue(service.getBalance("USER001") > 0);
    }

    @Test
    @Tag("critical")
    @DisplayName("[smoke | critical] Critical integrity test")
    void smokeOrCritical_critical() {
        double before = repository.getTotalBalance();
        service.transfer("USER001", "USER002", 10000.0);
        double after = repository.getTotalBalance();
        
        assertEquals(before, after, 0.001, "Money must be conserved");
    }

    @Test
    @Tag("smoke")
    @Tag("critical")
    @DisplayName("[smoke | critical] Both tags - deployment gate")
    void smokeAndCritical_deploymentGate() {
        // Must pass before any deployment
        TransferResult result = service.transfer("USER001", "USER002", 100.0);
        assertNotNull(result);
        assertNotNull(result.getTransactionId());
    }

    // ═══════════════════════════════════════════════════════════
    // COMPLEX EXPRESSIONS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @Tag("banking")
    @DisplayName("Complex Expression Tests")
    class ComplexExpressionTests {

        /**
         * Expression: (unit | integration) & banking & !manual
         * Run: mvn test -Dgroups="(unit | integration) & banking & !manual"
         */
        @Test
        @Tag("unit")
        @DisplayName("[(unit|integration) & banking & !manual] Unit banking")
        void complexExpr_unitBanking() {
            Account account = repository.loadAccountById("USER001");
            assertEquals("Rajesh Kumar", account.getAccountHolderName());
        }

        @Test
        @Tag("integration")
        @DisplayName("[(unit|integration) & banking & !manual] Integration banking")
        void complexExpr_integrationBanking() {
            TransferResult result = service.transfer("USER001", "MERCHANT001", 3000.0);
            assertEquals(3000.0, result.getReceiverBalanceAfter());
        }

        @Test
        @Tag("unit")
        @Tag("manual")
        @DisplayName("[(unit|integration) & banking & !manual] EXCLUDED manual")
        void complexExpr_excludedManual() {
            // This is EXCLUDED by !manual even though it has unit and banking
            System.out.println("This requires manual verification");
        }

        /**
         * Expression: (p2p & fast) | (p2m & critical)
         * P2P fast tests OR P2M critical tests
         */
        @Test
        @Tag("p2p")
        @Tag("fast")
        @DisplayName("[(p2p & fast) | (p2m & critical)] Fast P2P")
        void complexExpr_fastP2P() {
            TransferResult result = service.transfer("USER001", "USER002", 750.0);
            assertEquals(750.0, result.getAmount());
        }

        @Test
        @Tag("p2m")
        @Tag("critical")
        @DisplayName("[(p2p & fast) | (p2m & critical)] Critical P2M")
        void complexExpr_criticalP2M() {
            double merchantBefore = service.getBalance("MERCHANT001");
            service.transfer("USER001", "MERCHANT001", 2000.0);
            double merchantAfter = service.getBalance("MERCHANT001");
            
            assertEquals(2000.0, merchantAfter - merchantBefore, 0.001);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ENVIRONMENT-SPECIFIC TAGS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Environment-Specific Tags")
    class EnvironmentTags {

        @Test
        @Tag("dev")
        @Tag("unit")
        @DisplayName("[dev] Development environment test")
        void devEnvironment() {
            // Run only in dev: mvn test -Dgroups=dev
            assertTrue(service.accountExists("USER001"));
        }

        @Test
        @Tag("staging")
        @Tag("integration")
        @DisplayName("[staging] Staging environment test")
        void stagingEnvironment() {
            // Run only in staging: mvn test -Dgroups=staging
            TransferResult result = service.transfer("USER001", "USER002", 1500.0);
            assertNotNull(result);
        }

        @Test
        @Tag("prod")
        @Tag("smoke")
        @Tag("critical")
        @DisplayName("[prod] Production smoke test")
        void prodEnvironment() {
            // Run only in prod: mvn test -Dgroups=prod
            // These are typically read-only or very safe operations
            assertTrue(service.accountExists("USER001"));
            assertNotNull(service.getBalance("USER001"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // CI/CD PIPELINE TAGS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("CI/CD Pipeline Tags")
    class CICDTags {

        @Test
        @Tag("ci")
        @Tag("unit")
        @Tag("fast")
        @DisplayName("[ci] Runs on every commit")
        void ciEveryCommit() {
            // mvn test -Dgroups="ci & unit"
            assertTrue(service.accountExists("USER001"));
        }

        @Test
        @Tag("pr")
        @Tag("integration")
        @DisplayName("[pr] Runs on pull request")
        void prPullRequest() {
            // mvn test -Dgroups=pr
            TransferResult result = service.transfer("USER001", "USER002", 500.0);
            assertNotNull(result);
        }

        @Test
        @Tag("nightly")
        @Tag("slow")
        @DisplayName("[nightly] Runs in nightly build")
        void nightlyBuild() {
            // mvn test -Dgroups=nightly
            for (int i = 0; i < 50; i++) {
                service.transfer("USER001", "USER002", 50.0);
            }
        }

        @Test
        @Tag("release")
        @Tag("smoke")
        @Tag("critical")
        @DisplayName("[release] Runs before release")
        void releaseGate() {
            // mvn test -Dgroups="release & critical"
            double total = repository.getTotalBalance();
            service.transfer("USER001", "USER002", 5000.0);
            assertEquals(total, repository.getTotalBalance(), 0.001);
        }
    }
}
