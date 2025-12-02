package com.npci.level8.exceptions;

/**
 * Level 8: Exception Handling - Daily Limit Exceeded Exception
 *
 * Thrown when transaction exceeds daily limit.
 */
public class DailyLimitExceededException extends BankingException {

    private double dailyLimit;
    private double alreadyUsed;
    private double attemptedAmount;
    private String limitType;  // WITHDRAWAL, TRANSFER, UPI, etc.

    public DailyLimitExceededException(String accountNumber, String limitType,
                                       double dailyLimit, double alreadyUsed,
                                       double attemptedAmount) {
        super(buildMessage(limitType, dailyLimit, alreadyUsed, attemptedAmount), "BANK_ERR_004");
        this.withAccountNumber(accountNumber);
        this.withAmount(attemptedAmount);
        this.dailyLimit = dailyLimit;
        this.alreadyUsed = alreadyUsed;
        this.attemptedAmount = attemptedAmount;
        this.limitType = limitType;
    }

    private static String buildMessage(String limitType, double dailyLimit,
                                       double alreadyUsed, double attemptedAmount) {
        double remaining = dailyLimit - alreadyUsed;
        return String.format(
                "Daily %s limit exceeded. Limit: Rs.%.2f, Used: Rs.%.2f, Remaining: Rs.%.2f, Attempted: Rs.%.2f",
                limitType, dailyLimit, alreadyUsed, remaining, attemptedAmount);
    }

    public double getDailyLimit() { return dailyLimit; }
    public double getAlreadyUsed() { return alreadyUsed; }
    public double getAttemptedAmount() { return attemptedAmount; }
    public double getRemainingLimit() { return dailyLimit - alreadyUsed; }
    public String getLimitType() { return limitType; }

    @Override
    public String getDetailedMessage() {
        return String.format(
                "╔═══════════════════════════════════════════════╗\n" +
                        "║      DAILY LIMIT EXCEEDED ERROR               ║\n" +
                        "╠═══════════════════════════════════════════════╣\n" +
                        "  Error Code       : %s\n" +
                        "  Account Number   : %s\n" +
                        "  Limit Type       : %s\n" +
                        "  Daily Limit      : Rs.%.2f\n" +
                        "  Already Used     : Rs.%.2f\n" +
                        "  Remaining        : Rs.%.2f\n" +
                        "  Attempted        : Rs.%.2f\n" +
                        "  Excess Amount    : Rs.%.2f\n" +
                        "╚═══════════════════════════════════════════════╝",
                getErrorCode(), getAccountNumber(), limitType, dailyLimit, alreadyUsed,
                getRemainingLimit(), attemptedAmount, attemptedAmount - getRemainingLimit());
    }
}