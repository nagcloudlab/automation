package com.example.dto;

public class TransferRequest {

    private String fromAccId;
    private String toAccId;
    private double amount;

    public TransferRequest(String fromAccId, String toAccId, double amount) {
        this.fromAccId = fromAccId;
        this.toAccId = toAccId;
        this.amount = amount;
    }

    public TransferRequest() {
    }

    public String getFromAccId() {
        return fromAccId;
    }

    public void setFromAccId(String fromAccId) {
        this.fromAccId = fromAccId;
    }

    public String getToAccId() {
        return toAccId;
    }

    public void setToAccId(String toAccId) {
        this.toAccId = toAccId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
