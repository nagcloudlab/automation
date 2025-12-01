package com.example.npci;

public class SavingsAccount extends Account {

    private double interestRate; // in percentage

    public SavingsAccount(String accountNumber, String holderName, double balance, double interestRate) {
        super(accountNumber, holderName, balance);
        this.interestRate = interestRate;
    }

    // Overriding withdraw method to include minimum balance check
    @Override
    public void withdraw(double amount) {
        double minimumBalance = 1000.0; // Minimum balance requirement
        if (this.balance - amount >= minimumBalance) {
            this.balance -= amount;
            System.out.println("Withdrew Rs." + amount + " | New Balance: Rs." + this.balance);
        } else {
            System.out.println("Withdrawal of Rs." + amount + " denied! Minimum balance of Rs." + minimumBalance
                    + " must be maintained.");
        }
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
