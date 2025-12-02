package com.npci.level8.exceptions;

/**
 * Level 8: Exception Handling - Insufficient Balance Exception
 *
 * Thrown when account doesn't have enough balance for a transaction.
 * Extends BankingException (Checked - must be handled).
 */
public class InsufficientBalanceException extends BankingException {

    private double currentBalance;
    private double requiredAmount;
    private double minimumBalance;

    public InsufficientBalanceException(String accountNumber, double currentBalance,
                                        double requiredAmount, double minimumBalance) {
        super(buildMessage(currentBalance, requiredAmount, minimumBalance),
                "BANK_ERR_001");
        this.withAccountNumber(accountNumber);
        this.withAmount(requiredAmount);
        this.currentBalance = currentBalance;
        this.requiredAmount = requiredAmount;
        this.minimumBalance = minimumBalance;
    }

    public InsufficientBalanceException(String accountNumber, double currentBalance,
                                        double requiredAmount) {
        this(accountNumber, currentBalance, requiredAmount, 0);
    }

    private static String buildMessage(double currentBalance, double requiredAmount,
                                       double minimumBalance) {
        double available = currentBalance - minimumBalance;
        return String.format(
                "Insufficient balance. Available: Rs.%.2f, Required: Rs.%.2f, Shortfall: Rs.%.2f",
                Math.max(0, available), requiredAmount, requiredAmount - Math.max(0, available));
    }

    // Getters
    public double getCurrentBalance() { return currentBalance; }
    public double getRequiredAmount() { return requiredAmount; }
    public double getMinimumBalance() { return minimumBalance; }
    public double getShortfall() {
        return requiredAmount - (currentBalance - minimumBalance);
    }
    public double getAvailableBalance() {
        return Math.max(0, currentBalance - minimumBalance);
    }

    @Override
    public String getDetailedMessage() {
        return String.format(
                "╔═══════════════════════════════════════════════╗\n" +
                        "║     INSUFFICIENT BALANCE ERROR                ║\n" +
                        "╠═══════════════════════════════════════════════╣\n" +
                        "  Error Code       : %s\n" +
                        "  Account Number   : %s\n" +
                        "  Current Balance  : Rs.%.2f\n" +
                        "  Minimum Balance  : Rs.%.2f\n" +
                        "  Available Balance: Rs.%.2f\n" +
                        "  Required Amount  : Rs.%.2f\n" +
                        "  Shortfall        : Rs.%.2f\n" +
                        "╚═══════════════════════════════════════════════╝",
                getErrorCode(), getAccountNumber(), currentBalance, minimumBalance,
                getAvailableBalance(), requiredAmount, getShortfall());
    }
}