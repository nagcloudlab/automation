package com.upi.exception;

import java.math.BigDecimal;

/**
 * Exception thrown when an account has insufficient funds for a transaction.
 */
public class InsufficientFundsException extends RuntimeException {

    private final String accountId;
    private final BigDecimal availableBalance;
    private final BigDecimal requestedAmount;

    public InsufficientFundsException(String accountId, BigDecimal availableBalance, BigDecimal requestedAmount) {
        super(String.format("Insufficient funds in account %s. Available: %s, Requested: %s",
                accountId, availableBalance, requestedAmount));
        this.accountId = accountId;
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }
}
