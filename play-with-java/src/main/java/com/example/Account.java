package com.example;

/**
 * author: Nagendra Kumar
 */

public class Account {
    // fields
    private String accountNumber;// null
    private String holderName; // null
    protected double balance; // 0.0

    // constructor(s)
    public Account(String accountNumber) {
        // validation logics
        // init logics
        // this.accountNumber = accountNumber;
        this(accountNumber, "Unknown", 0.0);
    }

    public Account(String accountNumber, String holderName) {
        // validation logics
        // init logics
        // this.accountNumber = accountNumber;
        // this.holderName = holderName;
        this(accountNumber, holderName, 0.0);
    }

    public Account(String accountNumber, String holderName, double balance) {
        // validation logics
        // init logics
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    // Method to deposit money
    public void deposit(double amount) {
        this.balance = this.balance + amount;
        System.out.println("Deposited Rs." + amount + " | New Balance: Rs." + this.balance);
    }

    // Method to withdraw money
    public void withdraw(double amount) {
        if (amount <= this.balance) {
            this.balance = this.balance - amount;
            System.out.println("Withdrawn Rs." + amount + " | New Balance: Rs." + this.balance);
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    // setters
    public void setHolderName(String newName) {
        if (newName != null && newName.length() > 0) {
            this.holderName = newName;
            System.out.println("Name updated successfully.");
        } else {
            System.out.println("Invalid name!");
        }
    }

}

// default Type(s) ( class declaration )