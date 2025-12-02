package com.npci.level9.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Level 9: Collections Framework - Bank Account Model
 */
public class BankAccount implements Comparable<BankAccount> {

    private final String accountNumber;
    private String customerId;
    private String accountType;  // SAVINGS, CURRENT, FD
    private double balance;
    private String branchCode;
    private LocalDate openingDate;
    private boolean isActive;
    private String status;  // ACTIVE, DORMANT, BLOCKED, CLOSED

    // ═══════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    public BankAccount(String accountNumber, String customerId,
                       String accountType, double initialBalance, String branchCode) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = initialBalance;
        this.branchCode = branchCode;
        this.openingDate = LocalDate.now();
        this.isActive = true;
        this.status = "ACTIVE";
    }

    // ═══════════════════════════════════════════════════════════════
    // GETTERS AND SETTERS
    // ═══════════════════════════════════════════════════════════════

    public String getAccountNumber() { return accountNumber; }
    public String getCustomerId() { return customerId; }
    public String getAccountType() { return accountType; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }
    public LocalDate getOpeningDate() { return openingDate; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // ═══════════════════════════════════════════════════════════════
    // BANKING OPERATIONS
    // ═══════════════════════════════════════════════════════════════

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    // ═══════════════════════════════════════════════════════════════
    // equals() and hashCode()
    // ═══════════════════════════════════════════════════════════════

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BankAccount account = (BankAccount) obj;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    // ═══════════════════════════════════════════════════════════════
    // Comparable - Natural ordering by account number
    // ═══════════════════════════════════════════════════════════════

    @Override
    public int compareTo(BankAccount other) {
        return this.accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public String toString() {
        return String.format("Account[%s, %s, Rs.%.2f, %s]",
                accountNumber, accountType, balance, status);
    }
}