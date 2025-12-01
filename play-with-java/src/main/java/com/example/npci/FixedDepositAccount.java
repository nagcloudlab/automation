package com.example.npci;

class FixedDepositAccount extends Account {

    private int tenureMonths;
    private double fdInterestRate;
    private boolean isMatured;

    public FixedDepositAccount(String accNo, String name, double principal, int months, double rate) {
        super(accNo, name, principal);
        this.tenureMonths = months;
        this.fdInterestRate = rate;
        this.isMatured = false;
    }

    @Override
    public void deposit(double amount) {
        System.out.println("Cannot deposit to FD after creation!");
    }

    @Override
    public void withdraw(double amount) {
        if (!isMatured) {
            System.out.println("FD not matured! Premature withdrawal not allowed.");
        } else {
            super.withdraw(amount);
        }
    }

    public void matureFD() {
        double interest = balance * fdInterestRate * tenureMonths / 1200;
        balance += interest;
        isMatured = true;
        System.out.println("FD Matured! Interest earned: Rs." + interest);
    }
}
