package com.upi.model;

/**
 * Transaction Status enumeration representing the state of a transaction.
 */
public enum TransactionStatus {
    PENDING("Transaction is being processed"),
    COMPLETED("Transaction completed successfully"),
    FAILED("Transaction failed"),
    REVERSED("Transaction was reversed"),
    CANCELLED("Transaction was cancelled");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
