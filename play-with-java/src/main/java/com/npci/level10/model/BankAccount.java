package com.npci.level10.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Level 10: Streams API - Bank Account Model
 */
public class BankAccount implements Comparable<BankAccount>{

    private String accountNumber;
    private String customerId;
    private String accountType;  // SAVINGS, CURRENT, FD, RD
    private double balance;
    private String branchCode;
    private LocalDate openingDate;
    private boolean isActive;
    private String status;  // ACTIVE, DORMANT, BLOCKED, CLOSED
    private double interestRate;
    private double minimumBalance;

    // ═══════════════════════════════════════════════════════════════
    // CONSTRUCTORS
    // ═══════════════════════════════════════════════════════════════

    public BankAccount(String accountNumber, String customerId,
                       String accountType, double balance, String branchCode) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
        this.branchCode = branchCode;
        this.openingDate = LocalDate.now();
        this.isActive = true;
        this.status = "ACTIVE";
        this.interestRate = getDefaultInterestRate(accountType);
        this.minimumBalance = getDefaultMinBalance(accountType);
    }

    public BankAccount(String accountNumber, String customerId, String accountType,
                       double balance, String branchCode, LocalDate openingDate,
                       boolean isActive, String status) {
        this(accountNumber, customerId, accountType, balance, branchCode);
        this.openingDate = openingDate;
        this.isActive = isActive;
        this.status = status;
    }

    private double getDefaultInterestRate(String type) {
        switch (type) {
            case "SAVINGS": return 4.0;
            case "CURRENT": return 0.0;
            case "FD": return 7.0;
            case "RD": return 6.5;
            default: return 0.0;
        }
    }

    private double getDefaultMinBalance(String type) {
        switch (type) {
            case "SAVINGS": return 1000.0;
            case "CURRENT": return 5000.0;
            default: return 0.0;
        }
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
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double rate) { this.interestRate = rate; }
    public double getMinimumBalance() { return minimumBalance; }

    // ═══════════════════════════════════════════════════════════════
    // DERIVED PROPERTIES
    // ═══════════════════════════════════════════════════════════════

    public double getAnnualInterest() {
        return balance * interestRate / 100;
    }

    public boolean isAboveMinimum() {
        return balance >= minimumBalance;
    }

    public boolean isSavings() {
        return "SAVINGS".equals(accountType);
    }

    public boolean isCurrent() {
        return "CURRENT".equals(accountType);
    }

    public boolean isFixedDeposit() {
        return "FD".equals(accountType);
    }

    public int getAccountAgeInDays() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(openingDate, LocalDate.now());
    }

    // ═══════════════════════════════════════════════════════════════
    // OPERATIONS
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
    // equals, hashCode, toString
    // ═══════════════════════════════════════════════════════════════

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BankAccount that = (BankAccount) obj;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return String.format("Account[%s, %s, Rs.%.2f, %s, %s]",
                accountNumber, accountType, balance, branchCode, status);
    }

    @Override
    public int compareTo(BankAccount o) {
        return this.accountNumber.compareTo(o.accountNumber);
    }
}