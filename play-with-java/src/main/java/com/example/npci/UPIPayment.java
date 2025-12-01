package com.example.npci;

public class UPIPayment extends PaymentService {

    private String senderUPI;
    private String receiverUPI;
    private String pin;

    public UPIPayment(String sender, String receiver, double amount, String pin) {
        this.senderUPI = sender;
        this.receiverUPI = receiver;
        this.amount = amount;
        this.pin = pin;
    }

    @Override
    public boolean validatePaymentDetails() {
        System.out.println("Validating UPI IDs...");
        if (!senderUPI.contains("@") || !receiverUPI.contains("@")) {
            System.out.println("Invalid UPI ID format!");
            return false;
        }
        if (pin.length() != 4) {
            System.out.println("Invalid PIN!");
            return false;
        }
        System.out.println("UPI validation successful");
        return true;
    }

    @Override
    public void processPayment() {
        System.out.println("Processing UPI payment...");
        System.out.println("Debiting from: " + senderUPI);
        System.out.println("Crediting to: " + receiverUPI);
        System.out.println("Amount: Rs." + amount);
    }

    @Override
    public void sendConfirmation() {
        System.out.println("SMS sent: UPI payment of Rs." + amount + " successful. TxnID: " + transactionId);
    }

}
