package com.npci.level8.exceptions;

/**
 * Level 8: Exception Handling - Transaction Failed Exception
 *
 * Thrown when a transaction fails during processing.
 */
public class TransactionFailedException extends BankingException {

    private String transactionType;
    private String failureStage;
    private String sourceAccount;
    private String destinationAccount;
    private boolean reversalRequired;
    private boolean reversalCompleted;

    public TransactionFailedException(String transactionId, String type,
                                      String failureStage, String reason) {
        super("Transaction " + transactionId + " failed at " + failureStage + ": " + reason,
                "BANK_ERR_005");
        this.withTransactionId(transactionId);
        this.transactionType = type;
        this.failureStage = failureStage;
    }

    public TransactionFailedException(String transactionId, String type,
                                      String sourceAccount, String destinationAccount,
                                      double amount, String failureStage, String reason) {
        super("Transaction failed: " + reason, "BANK_ERR_005");
        this.withTransactionId(transactionId);
        this.withAmount(amount);
        this.transactionType = type;
        this.failureStage = failureStage;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }

    public TransactionFailedException(String transactionId, String type,
                                      String failureStage, Throwable cause) {
        super("Transaction " + transactionId + " failed at " + failureStage,
                "BANK_ERR_005", cause);
        this.withTransactionId(transactionId);
        this.transactionType = type;
        this.failureStage = failureStage;
    }

    // Getters
    public String getTransactionType() { return transactionType; }
    public String getFailureStage() { return failureStage; }
    public String getSourceAccount() { return sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public boolean isReversalRequired() { return reversalRequired; }
    public boolean isReversalCompleted() { return reversalCompleted; }

    // Setters
    public void setReversalRequired(boolean required) { this.reversalRequired = required; }
    public void setReversalCompleted(boolean completed) { this.reversalCompleted = completed; }

    @Override
    public String getDetailedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔═══════════════════════════════════════════════╗\n");
        sb.append("║      TRANSACTION FAILED ERROR                 ║\n");
        sb.append("╠═══════════════════════════════════════════════╣\n");
        sb.append(String.format("  Error Code      : %s\n", getErrorCode()));
        sb.append(String.format("  Transaction ID  : %s\n", getTransactionId()));
        sb.append(String.format("  Type            : %s\n", transactionType));
        sb.append(String.format("  Failure Stage   : %s\n", failureStage));
        if (sourceAccount != null) {
            sb.append(String.format("  Source Account  : %s\n", sourceAccount));
        }
        if (destinationAccount != null) {
            sb.append(String.format("  Dest Account    : %s\n", destinationAccount));
        }
        if (getAmount() > 0) {
            sb.append(String.format("  Amount          : Rs.%.2f\n", getAmount()));
        }
        sb.append(String.format("  Reversal Needed : %s\n", reversalRequired ? "Yes" : "No"));
        if (reversalRequired) {
            sb.append(String.format("  Reversal Done   : %s\n", reversalCompleted ? "Yes" : "No"));
        }
        sb.append(String.format("  Message         : %s\n", getMessage()));
        if (getCause() != null) {
            sb.append(String.format("  Root Cause      : %s\n", getCause().getMessage()));
        }
        sb.append("╚═══════════════════════════════════════════════╝");
        return sb.toString();
    }
}