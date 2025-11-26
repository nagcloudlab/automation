package com.example;

public class SavingsAccount extends Account {

    private double interestRate; // in percentage

    public SavingsAccount(String accountNumber, String holderName, double balance, double interestRate) {
        super(accountNumber, holderName, balance);
        this.interestRate = interestRate;
    }

    // Method to apply interest to the account
    public void applyInterest() {
        double interest = (this.balance * this.interestRate) / 100;
        this.balance += interest;
        System.out.println("Applied interest Rs." + interest + " | New Balance: Rs." + this.balance);
    }

    // Getter for interest rate
    public double getInterestRate() {
        return interestRate;
    }

}
