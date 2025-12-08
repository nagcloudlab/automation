package com.example.model;

public class Account implements Comparable<Account> {

    private String accountId;
    private String accountHolderName;
    private double balance;

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

    // debit & credit methods
    public void debit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to debit should be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        balance += amount;
    }

    public void credit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to credit should be positive");
        }
        balance += amount;
    }

    // toString method for easy representation
    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", accountHolderName='" + accountHolderName + '\'' +
                ", balance=" + balance +
                '}';
    }

    // equals and hashCode methods for object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
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

    // compareTo method for natural ordering based on accountId
    @Override
    public int compareTo(Account other) {
        return this.accountId.compareTo(other.accountId);
    }

}
