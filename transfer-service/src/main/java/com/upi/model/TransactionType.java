package com.upi.model;

/**
 * Transaction Type enumeration representing different types of transactions.
 */
public enum TransactionType {
    FUND_TRANSFER("Fund Transfer"),
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    BILL_PAYMENT("Bill Payment"),
    REFUND("Refund");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
