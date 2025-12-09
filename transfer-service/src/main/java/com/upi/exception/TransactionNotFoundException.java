package com.upi.exception;

/**
 * Exception thrown when a transaction is not found in the system.
 */
public class TransactionNotFoundException extends RuntimeException {

    private final String referenceNumber;

    public TransactionNotFoundException(String referenceNumber) {
        super(String.format("Transaction not found with reference: %s", referenceNumber));
        this.referenceNumber = referenceNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }
}
