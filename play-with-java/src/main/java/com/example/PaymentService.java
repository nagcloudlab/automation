package com.example;

public abstract class PaymentService {

    protected String transactionId;
    protected double amount;
    protected String status;

    // CONCRETE METHOD - Implemented here, inherited by children
    public void generateTransactionId() {
        this.transactionId = "TXN" + System.currentTimeMillis();
        System.out.println("Transaction ID generated: " + transactionId);
    }

    // CONCRETE METHOD - Common validation for all payment types
    public boolean validateAmount(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount!");
            return false;
        }
        if (amount > 100000) {
            System.out.println("Amount exceeds limit!");
            return false;
        }
        this.amount = amount;
        return true;
    }

    // ABSTRACT METHODS - No body! Child classes MUST implement these
    public abstract boolean validatePaymentDetails();

    public abstract void processPayment();

    public abstract void sendConfirmation();

    // CONCRETE METHOD - Template for payment flow
    public void executePayment() {
        System.out.println("\n=== Starting Payment ===");
        generateTransactionId();
        if (!validatePaymentDetails()) {
            this.status = "FAILED";
            return;
        }
        processPayment();
        sendConfirmation();
        this.status = "SUCCESS";
        System.out.println("=== Payment Complete ===\n");
    }

}
