package com.example.model;

import com.example.exception.InsufficientBalanceException;
import com.example.exception.InvalidAmountException;

/**
 * Account Model - Represents a bank account in UPI system.
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    UPDATED FOR LEVEL 2                        ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Changes:                                                     ║
 * ║  • Uses custom exceptions instead of IllegalArgumentException ║
 * ║  • InsufficientBalanceException for debit failures            ║
 * ║  • InvalidAmountException for validation failures             ║
 * ║  • Added account status tracking                              ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 2.0 (Level 2 - Custom Exceptions)
 */
public class Account implements Comparable<Account> {

    private final String accountId;
    private final String accountHolderName;
    private double balance;

    /**
     * Create a new Account.
     * 
     * @param accountId Unique account identifier
     * @param accountHolderName Name of account holder
     * @param balance Initial balance (must be >= 0)
     */
    public Account(String accountId, String accountHolderName, double balance) {
        this.accountId = accountId;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    /**
     * Debit (withdraw) amount from account.
     * 
     * @param amount Amount to debit (must be positive and <= balance)
     * @throws InvalidAmountException if amount is zero or negative
     * @throws InsufficientBalanceException if amount exceeds balance
     * 
     * VALIDATION ORDER:
     * 1. Check amount is positive (not zero, not negative)
     * 2. Check sufficient balance exists
     * 3. Execute debit
     */
    public void debit(double amount) {
        // Validate amount is positive
        if (amount < 0) {
            throw InvalidAmountException.negative(amount);
        }
        if (amount == 0) {
            throw InvalidAmountException.zero();
        }
        
        // Validate sufficient balance
        if (amount > balance) {
            throw new InsufficientBalanceException(accountId, balance, amount);
        }
        
        // Execute debit
        balance -= amount;
    }

    /**
     * Credit (deposit) amount to account.
     * 
     * @param amount Amount to credit (must be positive)
     * @throws InvalidAmountException if amount is zero or negative
     */
    public void credit(double amount) {
        // Validate amount is positive
        if (amount < 0) {
            throw InvalidAmountException.negative(amount);
        }
        if (amount == 0) {
            throw InvalidAmountException.zero();
        }
        
        // Execute credit
        balance += amount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", accountHolderName='" + accountHolderName + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Double.compare(account.balance, balance) == 0 &&
                accountId.equals(account.accountId) &&
                accountHolderName.equals(account.accountHolderName);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = accountId.hashCode();
        result = 31 * result + accountHolderName.hashCode();
        temp = Double.doubleToLongBits(balance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public int compareTo(Account other) {
        return this.accountId.compareTo(other.accountId);
    }
}
