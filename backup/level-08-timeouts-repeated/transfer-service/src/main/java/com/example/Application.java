package com.example;

import com.example.repository.AccountRepository;
import com.example.repository.SqlAccountRepository;
import com.example.service.UPITransferService;

/**
 * Application Entry Point - Demonstrates Dependency Injection
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    DEPENDENCY INJECTION DEMO                  ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This class shows MANUAL dependency injection.                ║
 * ║                                                               ║
 * ║  In Spring Boot, this wiring happens automatically:           ║
 * ║    @Autowired                                                 ║
 * ║    private AccountRepository repository;                      ║
 * ║                                                               ║
 * ║  But understanding manual DI helps grasp the concept!         ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * DEPENDENCY FLOW:
 * ================
 *   Application (creates) → SqlAccountRepository
 *        │
 *        └──(injects into)→ UPITransferService
 * 
 * @author NPCI Training Team
 * @version 2.0 (Level 0 - Testability)
 */
public class Application {

    public static void main(String[] args) {
        
        printBanner();

        // ═══════════════════════════════════════════════════════════
        // PHASE 1: DEPENDENCY INJECTION (Wiring Components)
        // ═══════════════════════════════════════════════════════════
        System.out.println("▶ PHASE 1: DEPENDENCY INJECTION");
        System.out.println("─".repeat(55));

        /*
         * Step 1: Create the LOWEST-LEVEL dependency first
         * 
         * AccountRepository is an INTERFACE.
         * SqlAccountRepository is the IMPLEMENTATION.
         * 
         * We declare as interface type (AccountRepository) but 
         * instantiate the concrete class (SqlAccountRepository).
         * This is "programming to an interface".
         */
        System.out.println("\n[DI] Creating AccountRepository...");
        AccountRepository accountRepository = new SqlAccountRepository();

        /*
         * Step 2: INJECT the repository into the service
         * 
         * UPITransferService needs AccountRepository to function.
         * Instead of creating it internally, we INJECT it via constructor.
         * 
         * This is CONSTRUCTOR INJECTION - the most common DI pattern.
         */
        System.out.println("\n[DI] Injecting repository into UPITransferService...");
        UPITransferService transferService = new UPITransferService(accountRepository);

        System.out.println("\n[DI] ✅ Dependency injection complete!");
        System.out.println("[DI] Service is ready to process transfers.\n");

        // ═══════════════════════════════════════════════════════════
        // PHASE 2: EXECUTE TRANSFERS (Happy Path)
        // ═══════════════════════════════════════════════════════════
        System.out.println("▶ PHASE 2: EXECUTE TRANSFERS (Happy Path)");
        System.out.println("─".repeat(55));

        // Transfer 1: Account ID based transfer
        System.out.println("\n📤 Transfer 1: Rajesh → Priya (₹5,000)");
        try {
            transferService.transfer(5000.0, "ACC001", "ACC002");
        } catch (Exception e) {
            System.out.println("❌ Failed: " + e.getMessage());
        }

        // Transfer 2: UPI ID based transfer
        System.out.println("\n📤 Transfer 2: Priya → Amit via UPI ID (₹2,500)");
        try {
            transferService.transferByUpiId(2500.0, "priya@upi", "amit@upi");
        } catch (Exception e) {
            System.out.println("❌ Failed: " + e.getMessage());
        }

        // Transfer 3: Small amount (minimum limit test)
        System.out.println("\n📤 Transfer 3: Amit → Sunita (₹1 - Minimum)");
        try {
            transferService.transfer(1.0, "ACC003", "ACC004");
        } catch (Exception e) {
            System.out.println("❌ Failed: " + e.getMessage());
        }

        // ═══════════════════════════════════════════════════════════
        // PHASE 3: ERROR SCENARIOS (Unhappy Path)
        // ═══════════════════════════════════════════════════════════
        System.out.println("\n▶ PHASE 3: ERROR SCENARIOS (Unhappy Path)");
        System.out.println("─".repeat(55));

        // Error 1: Insufficient Balance
        System.out.println("\n📤 Error Test 1: Transfer more than balance");
        try {
            transferService.transfer(999999.0, "ACC004", "ACC001");
        } catch (Exception e) {
            System.out.println("✓ Expected Error: " + e.getMessage());
        }

        // Error 2: Exceeds UPI Limit
        System.out.println("\n📤 Error Test 2: Exceed UPI limit (₹1.5 Lakh)");
        try {
            transferService.transfer(150000.0, "ACC005", "ACC001");
        } catch (Exception e) {
            System.out.println("✓ Expected Error: " + e.getMessage());
        }

        // Error 3: Below Minimum Amount
        System.out.println("\n📤 Error Test 3: Below minimum (₹0.50)");
        try {
            transferService.transfer(0.50, "ACC001", "ACC002");
        } catch (Exception e) {
            System.out.println("✓ Expected Error: " + e.getMessage());
        }

        // Error 4: Invalid Account
        System.out.println("\n📤 Error Test 4: Invalid account ID");
        try {
            transferService.transfer(100.0, "ACC001", "INVALID_ACC");
        } catch (Exception e) {
            System.out.println("✓ Expected Error: " + e.getMessage());
        }

        // Error 5: Same account transfer
        System.out.println("\n📤 Error Test 5: Transfer to same account");
        try {
            transferService.transfer(100.0, "ACC001", "ACC001");
        } catch (Exception e) {
            System.out.println("✓ Expected Error: " + e.getMessage());
        }

        // ═══════════════════════════════════════════════════════════
        // PHASE 4: FINAL STATE
        // ═══════════════════════════════════════════════════════════
        System.out.println("\n▶ PHASE 4: FINAL STATE");
        System.out.println("─".repeat(55));
        
        // Show final account states
        if (accountRepository instanceof SqlAccountRepository) {
            ((SqlAccountRepository) accountRepository).printAllAccounts();
        }

        printFooter();
    }

    /**
     * Print application banner.
     */
    private static void printBanner() {
        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                           ║");
        System.out.println("║     UPI TRANSFER SERVICE - AUTOMATION TESTING TRAINING    ║");
        System.out.println("║                                                           ║");
        System.out.println("║     Level 0: Making Code Testable                         ║");
        System.out.println("║     Topic:   Dependency Injection                         ║");
        System.out.println("║                                                           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Print application footer.
     */
    private static void printFooter() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                           ║");
        System.out.println("║     ✅ Level 0 Demo Complete!                             ║");
        System.out.println("║                                                           ║");
        System.out.println("║     KEY TAKEAWAYS:                                        ║");
        System.out.println("║     • Dependencies are INJECTED, not created internally   ║");
        System.out.println("║     • Use INTERFACES for abstraction                      ║");
        System.out.println("║     • Constructor injection makes testing possible        ║");
        System.out.println("║                                                           ║");
        System.out.println("║     NEXT: Level 1 - JUnit 5 Basics                        ║");
        System.out.println("║                                                           ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}
