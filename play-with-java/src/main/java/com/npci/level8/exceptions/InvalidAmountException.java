package com.npci.level8.exceptions;

/**
 * Level 8: Exception Handling - Invalid Amount Exception
 *
 * This is an UNCHECKED exception (extends RuntimeException).
 * Used for programming errors like negative amounts.
 * Caller is not forced to handle it.
 */
public class InvalidAmountException extends RuntimeException {

    private double invalidAmount;
    private String reason;
    private double minAllowed;
    private double maxAllowed;

    public InvalidAmountException(double amount) {
        super("Invalid amount: " + amount);
        this.invalidAmount = amount;
        this.reason = amount <= 0 ? "Amount must be positive" : "Invalid amount";
    }

    public InvalidAmountException(double amount, String reason) {
        super("Invalid amount: " + amount + " - " + reason);
        this.invalidAmount = amount;
        this.reason = reason;
    }

    public InvalidAmountException(double amount, double minAllowed, double maxAllowed) {
        super(String.format("Amount Rs.%.2f out of range. Allowed: Rs.%.2f - Rs.%.2f",
                amount, minAllowed, maxAllowed));
        this.invalidAmount = amount;
        this.minAllowed = minAllowed;
        this.maxAllowed = maxAllowed;
        this.reason = "Amount out of allowed range";
    }

    public double getInvalidAmount() { return invalidAmount; }
    public String getReason() { return reason; }
    public double getMinAllowed() { return minAllowed; }
    public double getMaxAllowed() { return maxAllowed; }
}