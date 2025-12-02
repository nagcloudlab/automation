package com.npci.level8;

import com.npci.level8.exceptions.*;
import java.time.LocalDateTime;

/**
 * Level 8: Exception Handling - Transaction with Try-Catch-Finally
 *
 * Demonstrates:
 * - try-catch blocks
 * - Multiple catch blocks
 * - finally block
 * - try-with-resources (for AutoCloseable)
 */
public class Transaction implements AutoCloseable {

    private String transactionId;
    private String type;
    private String sourceAccount;
    private String destinationAccount;
    private double amount;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String errorMessage;
    private boolean resourcesAcquired;

    // ═══════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    public Transaction(String type, String sourceAccount, String destinationAccount, double amount) {
        this.transactionId = "TXN" + System.currentTimeMillis();
        this.type = type;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.status = "INITIATED";
        this.startTime = LocalDateTime.now();
        this.resourcesAcquired = false;

        System.out.println("[Transaction] Created: " + transactionId);
    }

    // ═══════════════════════════════════════════════════════════════
    // GETTERS
    // ═══════════════════════════════════════════════════════════════

    public String getTransactionId() { return transactionId; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getErrorMessage() { return errorMessage; }

    // ═══════════════════════════════════════════════════════════════
    // TRANSACTION LIFECYCLE METHODS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Acquire resources (database connection, locks, etc.)
     */
    public void acquireResources() throws ServiceUnavailableException {
        System.out.println("[Transaction] Acquiring resources for " + transactionId);

        // Simulate resource acquisition
        if (Math.random() < 0.1) {  // 10% chance of failure
            throw new ServiceUnavailableException("Database",
                    "Connection pool exhausted", 5, false);
        }

        resourcesAcquired = true;
        System.out.println("[Transaction] Resources acquired");
    }

    /**
     * Validate transaction
     */
    public void validate() throws BankingException {
        System.out.println("[Transaction] Validating " + transactionId);

        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        if (sourceAccount == null || sourceAccount.isEmpty()) {
            throw new InvalidAccountException("NULL", "Source account is required");
        }
        if (type.equals("TRANSFER") && (destinationAccount == null || destinationAccount.isEmpty())) {
            throw new InvalidAccountException("NULL", "Destination account is required for transfer");
        }

        status = "VALIDATED";
        System.out.println("[Transaction] Validation passed");
    }

    /**
     * Execute transaction
     */
    public void execute() throws TransactionFailedException {
        System.out.println("[Transaction] Executing " + transactionId);

        if (!resourcesAcquired) {
            throw new TransactionFailedException(transactionId, type,
                    "EXECUTE", "Resources not acquired");
        }

        // Simulate execution
        if (Math.random() < 0.05) {  // 5% chance of failure
            status = "FAILED";
            throw new TransactionFailedException(transactionId, type,
                    sourceAccount, destinationAccount, amount,
                    "EXECUTE", "Network timeout");
        }

        status = "SUCCESS";
        endTime = LocalDateTime.now();
        System.out.println("[Transaction] Execution successful");
    }

    /**
     * Release resources (called in finally or close)
     */
    public void releaseResources() {
        if (resourcesAcquired) {
            System.out.println("[Transaction] Releasing resources for " + transactionId);
            resourcesAcquired = false;
        }
    }

    /**
     * AutoCloseable implementation for try-with-resources
     */
    @Override
    public void close() {
        releaseResources();
        endTime = LocalDateTime.now();
        System.out.println("[Transaction] Closed: " + transactionId + " | Status: " + status);
    }

    /**
     * Mark as failed
     */
    public void markFailed(String error) {
        status = "FAILED";
        errorMessage = error;
        endTime = LocalDateTime.now();
    }

    /**
     * Display transaction details
     */
    public void displayDetails() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│       TRANSACTION DETAILS               │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("  Transaction ID  : " + transactionId);
        System.out.println("  Type            : " + type);
        System.out.println("  Source Account  : " + sourceAccount);
        System.out.println("  Dest Account    : " + destinationAccount);
        System.out.println("  Amount          : Rs." + amount);
        System.out.println("  Status          : " + status);
        System.out.println("  Start Time      : " + startTime);
        System.out.println("  End Time        : " + (endTime != null ? endTime : "In Progress"));
        if (errorMessage != null) {
            System.out.println("  Error           : " + errorMessage);
        }
        System.out.println("└─────────────────────────────────────────┘");
    }
}