package com.npci.level8;

import com.npci.level8.exceptions.*;

/**
 * Level 8: Exception Handling - Transaction Processor
 *
 * Demonstrates various exception handling patterns:
 * - Basic try-catch
 * - Multiple catch blocks
 * - Catch hierarchy
 * - finally block
 * - try-with-resources
 * - Exception wrapping
 * - Rethrowing exceptions
 */
public class TransactionProcessor {

    private int successCount = 0;
    private int failureCount = 0;

    // ═══════════════════════════════════════════════════════════════
    // PATTERN 1: Basic try-catch
    // ═══════════════════════════════════════════════════════════════

    /**
     * Basic try-catch pattern
     */
    public void processDeposit(BankAccount account, double amount) {
        System.out.println("\n=== PATTERN 1: Basic try-catch ===");

        try {
            account.deposit(amount);
            successCount++;
            System.out.println("Deposit successful!");
        } catch (AccountInactiveException e) {
            failureCount++;
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // PATTERN 2: Multiple catch blocks (specific to general)
    // ═══════════════════════════════════════════════════════════════

    /**
     * Multiple catch blocks - order matters (specific first)
     */
    public void processWithdrawal(BankAccount account, double amount, String pin) {
        System.out.println("\n=== PATTERN 2: Multiple catch blocks ===");

        try {
            account.withdraw(amount, pin);
            successCount++;
            System.out.println("Withdrawal successful!");

        } catch (AuthenticationException e) {
            // Most specific - handle PIN errors
            failureCount++;
            System.out.println("❌ Authentication Error:");
            System.out.println("   " + e.getMessage());
            System.out.println("   Remaining attempts: " + e.getRemainingAttempts());
            if (e.isAccountLocked()) {
                System.out.println("   ⚠ ACCOUNT LOCKED!");
            }

        } catch (InsufficientBalanceException e) {
            // Specific - handle balance errors
            failureCount++;
            System.out.println("❌ Insufficient Balance:");
            System.out.println("   Available: Rs." + e.getAvailableBalance());
            System.out.println("   Required: Rs." + e.getRequiredAmount());
            System.out.println("   Shortfall: Rs." + e.getShortfall());

        } catch (DailyLimitExceededException e) {
            // Specific - handle limit errors
            failureCount++;
            System.out.println("❌ Daily Limit Exceeded:");
            System.out.println("   Limit: Rs." + e.getDailyLimit());
            System.out.println("   Remaining: Rs." + e.getRemainingLimit());

        } catch (AccountInactiveException e) {
            // Specific - handle inactive account
            failureCount++;
            System.out.println("❌ Account Inactive:");
            System.out.println("   Status: " + e.getAccountStatus());
            System.out.println("   Reason: " + e.getInactiveReason());

        } catch (BankingException e) {
            // General - catch any other banking exception
            failureCount++;
            System.out.println("❌ Banking Error: " + e.getMessage());
            System.out.println("   Error Code: " + e.getErrorCode());
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // PATTERN 3: Multi-catch (Java 7+)
    // ═══════════════════════════════════════════════════════════════

    /**
     * Multi-catch - handle multiple exceptions the same way
     */
    public void processWithdrawalMultiCatch(BankAccount account, double amount, String pin) {
        System.out.println("\n=== PATTERN 3: Multi-catch ===");

        try {
            account.withdraw(amount, pin);
            successCount++;
            System.out.println("Withdrawal successful!");

        } catch (AuthenticationException | AccountInactiveException e) {
            // Handle both the same way
            failureCount++;
            System.out.println("❌ Access Denied: " + e.getMessage());

        } catch (InsufficientBalanceException | DailyLimitExceededException e) {
            // Handle both amount-related errors the same way
            failureCount++;
            System.out.println("❌ Amount Error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // PATTERN 4: try-catch-finally
    // ═══════════════════════════════════════════════════════════════

    /**
     * try-catch-finally - finally always executes
     */
    public void processTransfer(BankAccount from, BankAccount to,
                                double amount, String pin) {
        System.out.println("\n=== PATTERN 4: try-catch-finally ===");

        Transaction txn = new Transaction("TRANSFER",
                from.getAccountNumber(), to.getAccountNumber(), amount);

        try {
            // Acquire resources
            txn.acquireResources();

            // Validate
            txn.validate();

            // Execute transfer
            from.transfer(to, amount, pin);

            txn.execute();
            successCount++;
            System.out.println("Transfer successful!");

        } catch (ServiceUnavailableException e) {
            txn.markFailed("Service unavailable");
            failureCount++;
            System.out.println("❌ Service Error: " + e.getMessage());

        } catch (BankingException e) {
            txn.markFailed(e.getMessage());
            failureCount++;
            System.out.println("❌ Transfer Error: " + e.getMessage());

        } finally {
            // ALWAYS executes - cleanup resources
            System.out.println("[Finally] Cleaning up resources...");
            txn.releaseResources();
            txn.displayDetails();
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // PATTERN 5: try-with-resources (Java 7+)
    // ═══════════════════════════════════════════════════════════════

    /**
     * try-with-resources - automatic resource management
     * Resource must implement AutoCloseable
     */
    public void processTransferAutoClose(BankAccount from, BankAccount to,
                                         double amount, String pin) {
        System.out.println("\n=== PATTERN 5: try-with-resources ===");

        // Transaction implements AutoCloseable
        // close() is automatically called at the end
        try (Transaction txn = new Transaction("TRANSFER",
                from.getAccountNumber(), to.getAccountNumber(), amount)) {

            txn.acquireResources();
            txn.validate();
            from.transfer(to, amount, pin);
            txn.execute();
            successCount++;
            System.out.println("Transfer successful!");

        } catch (BankingException e) {
            failureCount++;
            System.out.println("❌ Transfer Error: " + e.getMessage());
        }
        // close() automatically called here!
    }

    // ═══════════════════════════════════════════════════════════════
    // PATTERN 6: Exception wrapping/chaining
    // ═══════════════════════════════════════════════════════════════

    /**
     * Exception wrapping - wrap low-level exception in high-level
     */
    public void processPayment(BankAccount account, double amount, String pin)
            throws TransactionFailedException {
        System.out.println("\n=== PATTERN 6: Exception wrapping ===");

        String txnId = "PAY" + System.currentTimeMillis();

        try {
            account.withdraw(amount, pin);
            successCount++;
            System.out.println("Payment successful!");

        } catch (AuthenticationException e) {
            failureCount++;
            // Wrap in TransactionFailedException with original as cause
            throw new TransactionFailedException(txnId, "PAYMENT",
                    "AUTHENTICATION", e);

        } catch (InsufficientBalanceException e) {
            failureCount++;
            TransactionFailedException wrapped = new TransactionFailedException(
                    txnId, "PAYMENT", account.getAccountNumber(), null,
                    amount, "DEBIT", "Insufficient balance");
            wrapped.initCause(e);  // Alternative way to set cause
            throw wrapped;

        } catch (BankingException e) {
            failureCount++;
            throw new TransactionFailedException(txnId, "PAYMENT",
                    "PROCESSING", e);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // PATTERN 7: Rethrowing exceptions
    // ═══════════════════════════════════════════════════════════════

    /**
     * Rethrowing - catch, log, and rethrow
     */
    public void processWithLogging(BankAccount account, double amount, String pin)
            throws BankingException {
        System.out.println("\n=== PATTERN 7: Catch, log, rethrow ===");

        try {
            account.withdraw(amount, pin);
            successCount++;

        } catch (BankingException e) {
            // Log the error
            System.out.println("[LOG] Error occurred: " + e.getErrorCode());
            System.out.println("[LOG] Account: " + account.getAccountNumber());
            System.out.println("[LOG] Amount: " + amount);
            System.out.println("[LOG] Details: " + e.getMessage());

            failureCount++;

            // Rethrow the same exception
            throw e;
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // PATTERN 8: Retry pattern
    // ═══════════════════════════════════════════════════════════════

    /**
     * Retry pattern - retry on certain failures
     */
    public boolean processWithRetry(BankAccount account, double amount, String pin,
                                    int maxRetries) {
        System.out.println("\n=== PATTERN 8: Retry pattern ===");

        int attempts = 0;

        while (attempts < maxRetries) {
            attempts++;
            System.out.println("Attempt " + attempts + " of " + maxRetries);

            try {
                account.withdraw(amount, pin);
                successCount++;
                System.out.println("Success on attempt " + attempts);
                return true;
            } catch (BankingException e) {
                // Don't retry on business errors
                System.out.println("Business error - not retrying: " + e.getMessage());
                failureCount++;
                return false;
            }
        }

        failureCount++;
        System.out.println("Max retries exceeded");
        return false;
    }

    // ═══════════════════════════════════════════════════════════════
    // STATISTICS
    // ═══════════════════════════════════════════════════════════════

    public void displayStatistics() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║      TRANSACTION PROCESSOR STATISTICS         ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("  Successful Transactions : " + successCount);
        System.out.println("  Failed Transactions     : " + failureCount);
        System.out.println("  Total Transactions      : " + (successCount + failureCount));
        double successRate = (successCount + failureCount) > 0 ?
                (successCount * 100.0 / (successCount + failureCount)) : 0;
        System.out.println("  Success Rate            : " + String.format("%.2f", successRate) + "%");
        System.out.println("╚═══════════════════════════════════════════════╝");
    }
}