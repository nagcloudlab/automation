package com.example.npci;

public abstract class Account {

    // fields
    private String accountNumber;// null
    private String holderName; // null
    protected double balance; // 0.0

    // constructor overloading
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

    // version 1: Simple deposit
    public void deposit(double amount) {
        this.balance = this.balance + amount;
        System.out.println("Deposited Rs." + amount + " | New Balance: Rs." + this.balance);
    }

    // Version 2: Deposit with remarks
    public void deposit(double amount, String remarks) {
        this.balance = this.balance + amount;
        System.out.println("Deposited Rs." + amount + " | Remarks: " + remarks + " | New Balance: Rs." + this.balance);
    }

    // Version 3: Deposit via cheque
    public void deposit(double amount, String chequeNo, String bankName) {
        balance += amount;
        System.out.println("Cheque deposited: Rs." + amount +
                " | Cheque: " + chequeNo + " | Bank: " + bankName);
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
