package com.example;

class IMPSPayment extends PaymentService {
    private String senderAccount;
    private String receiverAccount;
    private String receiverIFSC;

    public IMPSPayment(String sender, String receiver, String ifsc, double amount) {
        this.senderAccount = sender;
        this.receiverAccount = receiver;
        this.receiverIFSC = ifsc;
        this.amount = amount;
    }

    @Override
    public boolean validatePaymentDetails() {
        System.out.println("Validating IMPS details...");
        if (senderAccount.length() < 10 || receiverAccount.length() < 10) {
            System.out.println("Invalid account number!");
            return false;
        }
        if (receiverIFSC.length() != 11) {
            System.out.println("Invalid IFSC code!");
            return false;
        }
        System.out.println("IMPS validation successful");
        return true;
    }

    @Override
    public void processPayment() {
        System.out.println("Processing IMPS transfer...");
        System.out.println("From Account: " + senderAccount);
        System.out.println("To Account: " + receiverAccount + " (IFSC: " + receiverIFSC + ")");
        System.out.println("Amount: Rs." + amount);
    }

    @Override
    public void sendConfirmation() {
        System.out.println("SMS sent: IMPS Rs." + amount + " transferred. Ref: " + transactionId);
    }
}