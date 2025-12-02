package com.npci.level8.exceptions;

/**
 * Level 8: Exception Handling - Base Banking Exception
 *
 * Key Concepts:
 * - Exception = Abnormal condition during program execution
 * - Checked Exception = Must be caught or declared (extends Exception)
 * - Unchecked Exception = Optional to catch (extends RuntimeException)
 * - Custom Exception = Application-specific error types
 *
 * Exception Hierarchy:
 *
 *                    Throwable
 *                   /         \
 *              Error          Exception
 *           (Don't catch)    /          \
 *                     IOException    RuntimeException
 *                     SQLException   NullPointerException
 *                     (Checked)      (Unchecked)
 *
 * BankingException is our BASE exception for all banking errors.
 * It extends Exception (Checked) - forces caller to handle it.
 */
public class BankingException extends Exception {

    // ═══════════════════════════════════════════════════════════════
    // FIELDS - Additional context for banking errors
    // ═══════════════════════════════════════════════════════════════

    private String errorCode;
    private String accountNumber;
    private String transactionId;
    private double amount;

    // ═══════════════════════════════════════════════════════════════
    // CONSTRUCTORS - Various ways to create exception
    // ═══════════════════════════════════════════════════════════════

    /**
     * Basic constructor with message only
     */
    public BankingException(String message) {
        super(message);
        this.errorCode = "BANK_ERR_000";
    }

    /**
     * Constructor with message and error code
     */
    public BankingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructor with message and cause (exception chaining)
     */
    public BankingException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "BANK_ERR_000";
    }

    /**
     * Constructor with message, error code, and cause
     */
    public BankingException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Full constructor with all details
     */
    public BankingException(String message, String errorCode,
                            String accountNumber, String transactionId, double amount) {
        super(message);
        this.errorCode = errorCode;
        this.accountNumber = accountNumber;
        this.transactionId = transactionId;
        this.amount = amount;
    }

    // ═══════════════════════════════════════════════════════════════
    // GETTERS
    // ═══════════════════════════════════════════════════════════════

    public String getErrorCode() { return errorCode; }
    public String getAccountNumber() { return accountNumber; }
    public String getTransactionId() { return transactionId; }
    public double getAmount() { return amount; }

    // ═══════════════════════════════════════════════════════════════
    // SETTERS (Builder pattern friendly)
    // ═══════════════════════════════════════════════════════════════

    public BankingException withAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public BankingException withTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public BankingException withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    // ═══════════════════════════════════════════════════════════════
    // METHODS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Get detailed error information
     */
    public String getDetailedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Banking Error [").append(errorCode).append("]\n");
        sb.append("  Message: ").append(getMessage()).append("\n");
        if (accountNumber != null) {
            sb.append("  Account: ").append(accountNumber).append("\n");
        }
        if (transactionId != null) {
            sb.append("  Transaction: ").append(transactionId).append("\n");
        }
        if (amount > 0) {
            sb.append("  Amount: Rs.").append(amount).append("\n");
        }
        if (getCause() != null) {
            sb.append("  Caused by: ").append(getCause().getMessage()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "BankingException{" +
                "errorCode='" + errorCode + '\'' +
                ", message='" + getMessage() + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", amount=" + amount +
                '}';
    }
}