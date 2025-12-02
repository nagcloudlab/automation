package com.npci.colln;

public class Account extends Object implements Comparable<Account> {

    private String accountNumber; // // natural property for sorting
    private String accountHolderName;
    private double balance;

    @Override
    public int compareTo(Account o) {
        return this.accountNumber.compareTo(o.accountNumber); // 0 , +ve , -ve
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountHolderName='" + accountHolderName + '\'' +
                ", balance=" + balance +
                '}';
    }

    public Account(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }


}
