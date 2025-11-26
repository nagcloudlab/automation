package com.example;

// Open for extension & close for modification principle
public class PayentGateway {

    public void processPayment(double amount, Account account) {
        //
        account.withdraw(amount);
    }

}
