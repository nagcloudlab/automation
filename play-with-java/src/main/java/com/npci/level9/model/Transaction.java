package com.npci.level9.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Level 9: Collections Framework - Transaction Model
 */
public class Transaction implements Comparable<Transaction> {

    private final String transactionId;
    private String accountNumber;
    private String type;  // CREDIT, DEBIT, TRANSFER
    private double amount;
    private double balanceAfter;
    private LocalDateTime timestamp;
    private String channel;  // UPI, IMPS, NEFT, RTGS, ATM, BRANCH
    private String status;  // SUCCESS, FAILED, PENDING
    private String description;
    private String referenceNumber;

    // ═══════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    public Transaction(String transactionId, String accountNumber, String type,
                       double amount, double balanceAfter, String channel) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.channel = channel;
        this.timestamp = LocalDateTime.now();
        this.status = "SUCCESS";
    }

    public Transaction(String transactionId, String accountNumber, String type,
                       double amount, double balanceAfter, String channel,
                       String description) {
        this(transactionId, accountNumber, type, amount, balanceAfter, channel);
        this.description = description;
    }

    // ═══════════════════════════════════════════════════════════════
    // GETTERS AND SETTERS
    // ═══════════════════════════════════════════════════════════════

    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getChannel() { return channel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String ref) { this.referenceNumber = ref; }

    public boolean isCredit() {
        return type.equals("CREDIT") || type.equals("TRANSFER_IN");
    }

    public boolean isDebit() {
        return type.equals("DEBIT") || type.equals("TRANSFER_OUT");
    }

    // ═══════════════════════════════════════════════════════════════
    // equals() and hashCode()
    // ═══════════════════════════════════════════════════════════════

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction that = (Transaction) obj;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    // ═══════════════════════════════════════════════════════════════
    // Comparable - Order by timestamp (newest first)
    // ═══════════════════════════════════════════════════════════════

    @Override
    public int compareTo(Transaction other) {
        return other.timestamp.compareTo(this.timestamp);  // Descending
    }

    @Override
    public String toString() {
        String sign = isCredit() ? "+" : "-";
        return String.format("%s | %s | %s Rs.%.2f | %s | %s",
                transactionId, timestamp.toLocalDate(), sign, amount, channel, status);
    }

    public String toDetailedString() {
        return String.format(
                "Transaction ID: %s\n  Account: %s\n  Type: %s\n  Amount: Rs.%.2f\n" +
                        "  Balance After: Rs.%.2f\n  Channel: %s\n  Time: %s\n  Status: %s",
                transactionId, accountNumber, type, amount, balanceAfter,
                channel, timestamp, status);
    }
}