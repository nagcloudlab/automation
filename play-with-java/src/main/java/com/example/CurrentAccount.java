package com.example;

public class CurrentAccount extends Account {

    private double overdraftLimit;

    public CurrentAccount(String accountNumber, String holderName, double balance, double overdraftLimit) {
        super(accountNumber, holderName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= this.balance + this.overdraftLimit) {
            this.balance = this.balance - amount;
            System.out.println("Withdrawn Rs." + amount + " | New Balance: Rs." + this.balance);
        } else {
            System.out.println("Exceeded overdraft limit!");
        }
    }

    // Getter for overdraft limit
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

}
