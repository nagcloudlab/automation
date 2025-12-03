package com.npci.level10.model;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Level 10: Streams API - Transaction Model
 */
public class Transaction {

    private String transactionId;
    private String accountNumber;
    private String type;  // CREDIT, DEBIT
    private double amount;
    private double balanceAfter;
    private LocalDateTime timestamp;
    private String channel;  // UPI, IMPS, NEFT, RTGS, ATM, BRANCH
    private String status;  // SUCCESS, FAILED, PENDING, REVERSED
    private String description;
    private String category;  // SALARY, BILLS, SHOPPING, TRANSFER, EMI, etc.
    private String merchantName;

    // ═══════════════════════════════════════════════════════════════
    // CONSTRUCTORS
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
        this.category = "GENERAL";
    }

    public Transaction(String transactionId, String accountNumber, String type,
                       double amount, double balanceAfter, String channel,
                       String description, String category) {
        this(transactionId, accountNumber, type, amount, balanceAfter, channel);
        this.description = description;
        this.category = category;
    }

    public Transaction(String transactionId, String accountNumber, String type,
                       double amount, double balanceAfter, String channel,
                       LocalDateTime timestamp, String status, String description,
                       String category) {
        this(transactionId, accountNumber, type, amount, balanceAfter, channel, description, category);
        this.timestamp = timestamp;
        this.status = status;
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
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getChannel() { return channel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String name) { this.merchantName = name; }

    // ═══════════════════════════════════════════════════════════════
    // DERIVED PROPERTIES
    // ═══════════════════════════════════════════════════════════════

    public boolean isCredit() {
        return "CREDIT".equals(type);
    }

    public boolean isDebit() {
        return "DEBIT".equals(type);
    }

    public boolean isSuccessful() {
        return "SUCCESS".equals(status);
    }

    public boolean isFailed() {
        return "FAILED".equals(status);
    }

    public LocalDate getDate() {
        return timestamp.toLocalDate();
    }

    public int getHour() {
        return timestamp.getHour();
    }

    public String getMonth() {
        return timestamp.getMonth().toString();
    }

    public int getYear() {
        return timestamp.getYear();
    }

    public boolean isHighValue() {
        return amount >= 100000;
    }

    public boolean isUPI() {
        return "UPI".equals(channel);
    }

    // ═══════════════════════════════════════════════════════════════
    // equals, hashCode, toString
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

    @Override
    public String toString() {
        String sign = isCredit() ? "+" : "-";
        return String.format("%s | %s | %sRs.%.2f | %s | %s",
                transactionId, timestamp.toLocalDate(), sign, amount, channel, status);
    }
}