package com.npci.level8;

import com.npci.level8.exceptions.*;

/**
 * Level 8: Demo - Exception Handling
 *
 * Run this file to see all Level 8 concepts in action.
 */
public class Level8Demo {

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║     LEVEL 8: EXCEPTION HANDLING                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        // Demo 1: Custom Exceptions
        System.out.println("\n▶ DEMO 1: Custom Exceptions");
        System.out.println("─────────────────────────────────────────────\n");
        demo1_CustomExceptions();

        // Demo 2: Throwing and Catching
        System.out.println("\n▶ DEMO 2: Throwing and Catching Exceptions");
        System.out.println("─────────────────────────────────────────────\n");
        demo2_ThrowingAndCatching();

        // Demo 3: Multiple Catch Blocks
        System.out.println("\n▶ DEMO 3: Multiple Catch Blocks");
        System.out.println("─────────────────────────────────────────────\n");
        demo3_MultipleCatch();

        // Demo 4: Finally Block
        System.out.println("\n▶ DEMO 4: Finally Block");
        System.out.println("─────────────────────────────────────────────\n");
        demo4_FinallyBlock();

        // Demo 5: Try-with-resources
        System.out.println("\n▶ DEMO 5: Try-with-resources");
        System.out.println("─────────────────────────────────────────────\n");
        demo5_TryWithResources();

        // Demo 6: Exception Chaining
        System.out.println("\n▶ DEMO 6: Exception Chaining");
        System.out.println("─────────────────────────────────────────────\n");
        demo6_ExceptionChaining();

        // Demo 7: Checked vs Unchecked
        System.out.println("\n▶ DEMO 7: Checked vs Unchecked Exceptions");
        System.out.println("─────────────────────────────────────────────\n");
        demo7_CheckedVsUnchecked();

        // Demo 8: Summary
        System.out.println("\n▶ DEMO 8: Exception Handling Summary");
        System.out.println("─────────────────────────────────────────────\n");
        demo8_Summary();
    }

    /**
     * Demo 1: Creating and using custom exceptions
     */
    static void demo1_CustomExceptions() {
        System.out.println("Custom exceptions provide meaningful error information.\n");

        // Create custom exception objects
        InsufficientBalanceException balanceEx = new InsufficientBalanceException(
                "SAV001", 5000, 10000, 1000);
        System.out.println(balanceEx.getDetailedMessage());

        AuthenticationException authEx = new AuthenticationException(
                "SAV001", "PIN", 2, 3);
        System.out.println(authEx.getDetailedMessage());

        DailyLimitExceededException limitEx = new DailyLimitExceededException(
                "SAV001", "WITHDRAWAL", 100000, 80000, 30000);
        System.out.println(limitEx.getDetailedMessage());
    }

    /**
     * Demo 2: Throwing and catching exceptions
     */
    static void demo2_ThrowingAndCatching() {
        BankAccount account = new BankAccount("SAV001", "Ramesh Kumar",
                50000, "Savings", "1234");
        account.displayInfo();

        TransactionProcessor processor = new TransactionProcessor();

        // Successful deposit
        processor.processDeposit(account, 10000);

        // Failed deposit (negative amount - unchecked exception)
        System.out.println("\nTrying negative deposit:");
        try {
            processor.processDeposit(account, -5000);
        } catch (InvalidAmountException e) {
            System.out.println("Caught unchecked exception: " + e.getMessage());
        }

        // Successful withdrawal
        processor.processWithdrawal(account, 5000, "1234");

        // Failed withdrawal - wrong PIN
        processor.processWithdrawal(account, 5000, "9999");

        // Failed withdrawal - insufficient balance
        processor.processWithdrawal(account, 100000, "1234");

        processor.displayStatistics();
    }

    /**
     * Demo 3: Multiple catch blocks
     */
    static void demo3_MultipleCatch() {
        BankAccount account = new BankAccount("SAV002", "Priya Sharma",
                25000, "Savings", "4321");

        TransactionProcessor processor = new TransactionProcessor();

        System.out.println("Testing different error conditions:\n");

        // 1. Wrong PIN
        System.out.println("1. Testing wrong PIN:");
        processor.processWithdrawal(account, 1000, "0000");

        account.reactivate();  // Reset after PIN lockout

        // 2. Insufficient balance
        System.out.println("\n2. Testing insufficient balance:");
        processor.processWithdrawal(account, 50000, "4321");

        // 3. Daily limit
        System.out.println("\n3. Testing daily limit:");
        processor.processWithdrawal(account, 150000, "4321");

        // 4. Success
        System.out.println("\n4. Testing successful withdrawal:");
        processor.processWithdrawal(account, 5000, "4321");
    }

    /**
     * Demo 4: Finally block always executes
     */
    static void demo4_FinallyBlock() {
        BankAccount from = new BankAccount("SAV003", "User A", 50000, "Savings", "1111");
        BankAccount to = new BankAccount("SAV004", "User B", 10000, "Savings", "2222");

        TransactionProcessor processor = new TransactionProcessor();

        System.out.println("Finally block always executes, even on exception.\n");

        // Successful transfer
        System.out.println("--- Successful Transfer ---");
        processor.processTransfer(from, to, 10000, "1111");

        // Failed transfer
        System.out.println("\n--- Failed Transfer (wrong PIN) ---");
        processor.processTransfer(from, to, 5000, "9999");

        // Reset account
        from.reactivate();
    }

    /**
     * Demo 5: Try-with-resources for automatic cleanup
     */
    static void demo5_TryWithResources() {
        BankAccount from = new BankAccount("SAV005", "User C", 100000, "Savings", "3333");
        BankAccount to = new BankAccount("SAV006", "User D", 50000, "Savings", "4444");

        TransactionProcessor processor = new TransactionProcessor();

        System.out.println("Try-with-resources automatically calls close().\n");

        // Transaction implements AutoCloseable
        processor.processTransferAutoClose(from, to, 20000, "3333");

        System.out.println("\nNotice: close() was automatically called!");

        // File processor example
        FileProcessor fileProcessor = new FileProcessor();
        fileProcessor.demonstrateFileHandling();
    }

    /**
     * Demo 6: Exception chaining (wrapping)
     */
    static void demo6_ExceptionChaining() {
        BankAccount account = new BankAccount("SAV007", "User E", 20000, "Savings", "5555");

        TransactionProcessor processor = new TransactionProcessor();

        System.out.println("Exception chaining preserves root cause.\n");

        try {
            processor.processPayment(account, 50000, "5555");
        } catch (TransactionFailedException e) {
            System.out.println("Caught wrapped exception:");
            System.out.println("  Message: " + e.getMessage());
            System.out.println("  Error Code: " + e.getErrorCode());
            System.out.println("  Failure Stage: " + e.getFailureStage());

            if (e.getCause() != null) {
                System.out.println("  Root Cause: " + e.getCause().getClass().getSimpleName());
                System.out.println("  Root Message: " + e.getCause().getMessage());
            }

            System.out.println("\n--- Full Stack Trace ---");
            e.printStackTrace(System.out);
        }
    }

    /**
     * Demo 7: Checked vs Unchecked exceptions
     */
    static void demo7_CheckedVsUnchecked() {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║        CHECKED vs UNCHECKED EXCEPTIONS                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                ║");
        System.out.println("║  CHECKED EXCEPTIONS (extends Exception):                       ║");
        System.out.println("║    - Must be caught or declared with throws                   ║");
        System.out.println("║    - Represent recoverable conditions                         ║");
        System.out.println("║    - Examples: IOException, BankingException                  ║");
        System.out.println("║                                                                ║");
        System.out.println("║  UNCHECKED EXCEPTIONS (extends RuntimeException):             ║");
        System.out.println("║    - Don't need to be caught or declared                      ║");
        System.out.println("║    - Represent programming errors                             ║");
        System.out.println("║    - Examples: NullPointerException, InvalidAmountException   ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");

        System.out.println("\n--- Unchecked Exception Example ---");
        System.out.println("InvalidAmountException extends RuntimeException.");
        System.out.println("We can catch it, but we're not forced to.\n");

        // Unchecked - optional to catch
        try {
            throw new InvalidAmountException(-100);
        } catch (InvalidAmountException e) {
            System.out.println("Caught (optional): " + e.getMessage());
        }

        System.out.println("\n--- Checked Exception Example ---");
        System.out.println("BankingException extends Exception.");
        System.out.println("We MUST catch or declare it.\n");

        // Checked - must handle
        try {
            throw new BankingException("Test checked exception");
        } catch (BankingException e) {
            System.out.println("Caught (required): " + e.getMessage());
        }

        // Demonstrating why some are unchecked
        System.out.println("\n--- Why InvalidAmountException is Unchecked ---");
        System.out.println("Negative amount is a PROGRAMMING ERROR, not user error.");
        System.out.println("The programmer should validate before calling the method.");
        System.out.println("If it happens, the bug should be fixed in code, not caught.");
    }

    /**
     * Demo 8: Summary
     */
    static void demo8_Summary() {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              EXCEPTION HANDLING SUMMARY                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Keyword     │ Usage                                                ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ try         │ Block that may throw exception                       ║");
        System.out.println("║ catch       │ Handle specific exception                            ║");
        System.out.println("║ finally     │ Always executes (cleanup)                            ║");
        System.out.println("║ throw       │ Throw an exception                                   ║");
        System.out.println("║ throws      │ Declare exceptions method may throw                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              EXCEPTION HIERARCHY                                   ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                        Throwable                                   ║");
        System.out.println("║                       /         \\                                  ║");
        System.out.println("║                   Error          Exception                         ║");
        System.out.println("║               (Don't catch)     /          \\                       ║");
        System.out.println("║                          IOException    RuntimeException           ║");
        System.out.println("║                          SQLException   NullPointerException       ║");
        System.out.println("║                          (Checked)      IllegalArgumentException   ║");
        System.out.println("║                                         (Unchecked)                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              BEST PRACTICES                                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ 1. Catch specific exceptions first, general last                   ║");
        System.out.println("║ 2. Don't catch Exception or Throwable (too broad)                  ║");
        System.out.println("║ 3. Use try-with-resources for AutoCloseable resources              ║");
        System.out.println("║ 4. Don't swallow exceptions (empty catch block)                    ║");
        System.out.println("║ 5. Include context in exception messages                           ║");
        System.out.println("║ 6. Use exception chaining to preserve root cause                   ║");
        System.out.println("║ 7. Create custom exceptions for domain-specific errors             ║");
        System.out.println("║ 8. Use unchecked exceptions for programming errors                 ║");
        System.out.println("║ 9. Use checked exceptions for recoverable conditions               ║");
        System.out.println("║ 10. Clean up resources in finally or try-with-resources            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              BANKING EXCEPTION TYPES                               ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Exception                    │ When to Use                         ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ InsufficientBalanceException │ Not enough funds                    ║");
        System.out.println("║ InvalidAccountException      │ Account not found/invalid           ║");
        System.out.println("║ AccountInactiveException     │ Account blocked/closed              ║");
        System.out.println("║ DailyLimitExceededException  │ Transaction limit reached           ║");
        System.out.println("║ InvalidAmountException       │ Negative/invalid amount             ║");
        System.out.println("║ TransactionFailedException   │ Transaction processing error        ║");
        System.out.println("║ AuthenticationException      │ PIN/OTP/Password failure            ║");
        System.out.println("║ ServiceUnavailableException  │ System/service down                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
    }
}