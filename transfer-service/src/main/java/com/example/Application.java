package com.example;

import com.example.repository.AccountRepository;
import com.example.repository.AccountRepositoryFactory;
import com.example.repository.SqlAccountRepository;
import com.example.service.UPITransferService;

/**
 * Application Entry Point
 *
 * Demonstrates manual Dependency Injection.
 * In Spring Boot, this wiring happens automatically via @Autowired.
 *
 * The key insight: Dependencies flow from outside → inside
 * Application creates Repository, then injects it into Service.
 */
public class Application {

    public static void main(String[] args) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("   UPI TRANSFER SERVICE - LEVEL 0 DEMO");
        System.out.println("   Dependency Injection Example");
        System.out.println("═".repeat(60) + "\n");

        // ═══════════════════════════════════════════════════════════
        // PHASE 1: INITIALIZATION (Dependency Injection)
        // ═══════════════════════════════════════════════════════════
        System.out.println("▶ PHASE 1: Initializing Components\n");

        // Step 1: Create the repository (lowest level dependency)
        AccountRepository accountRepository = AccountRepositoryFactory.getAccountRepository("sql");

        // Step 2: Inject repository into service (Dependency Injection!)
        UPITransferService transferService = new UPITransferService(accountRepository);

        // ═══════════════════════════════════════════════════════════
        // PHASE 2: EXECUTE TRANSFERS
        // ═══════════════════════════════════════════════════════════
        System.out.println("\n▶ PHASE 2: Executing Transfers\n");

        // Transfer 1: Rajesh → Priya (₹5,000)
        try {
            transferService.transfer(5000.0, "ACC001", "ACC002");
        } catch (Exception e) {
            System.out.println("[ERROR] Transfer 1 failed: " + e.getMessage());
        }

        // Transfer 2: Using UPI IDs
        try {
            transferService.transferByUpiId(2500.0, "priya@upi", "amit@upi");
        } catch (Exception e) {
            System.out.println("[ERROR] Transfer 2 failed: " + e.getMessage());
        }

        // Transfer 3: Should FAIL - Insufficient balance
        try {
            transferService.transfer(999999.0, "ACC001", "ACC002");
        } catch (Exception e) {
            System.out.println("[ERROR] Transfer 3 failed (expected): " + e.getMessage());
        }

        // Transfer 4: Should FAIL - Exceeds UPI limit
        try {
            transferService.transfer(150000.0, "ACC003", "ACC001");
        } catch (Exception e) {
            System.out.println("[ERROR] Transfer 4 failed (expected): " + e.getMessage());
        }

        // ═══════════════════════════════════════════════════════════
        // PHASE 3: CLEANUP
        // ═══════════════════════════════════════════════════════════
        System.out.println("\n▶ PHASE 3: Cleanup\n");
        System.out.println("Application completed successfully.");
        System.out.println("═".repeat(60) + "\n");
    }
}