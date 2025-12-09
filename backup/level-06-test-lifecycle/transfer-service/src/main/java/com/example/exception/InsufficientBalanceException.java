package com.example.exception;

/**
 * Exception thrown when account has insufficient balance for debit.
 * 
 * NPCI Error Code: U30
 * 
 * SCENARIOS:
 * ==========
 * - Debit amount exceeds available balance
 * - Account is empty (zero balance)
 * - Balance held due to lien/freeze
 * 
 * CONTAINS:
 * =========
 * - Available balance at time of request
 * - Requested amount
 * - Calculated shortfall
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public class InsufficientBalanceException extends TransferException {

    /** NPCI error code for insufficient funds */
    public static final String ERROR_CODE = "U30";
    
    /** Current available balance */
    private final double availableBalance;
    
    /** Amount requested for debit */
    private final double requestedAmount;
    
    /** Account ID with insufficient balance */
    private final String accountId;

    /**
     * Constructor with balance details.
     * 
     * @param availableBalance Current available balance
     * @param requestedAmount Amount attempted to debit
     */
    public InsufficientBalanceException(double availableBalance, double requestedAmount) {
        super(
            String.format("Insufficient balance: Available ₹%.2f, Requested ₹%.2f, Shortfall ₹%.2f",
                availableBalance, requestedAmount, requestedAmount - availableBalance),
            ERROR_CODE
        );
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
        this.accountId = null;
    }

    /**
     * Constructor with account ID and balance details.
     * 
     * @param accountId The account with insufficient balance
     * @param availableBalance Current available balance
     * @param requestedAmount Amount attempted to debit
     */
    public InsufficientBalanceException(String accountId, double availableBalance, double requestedAmount) {
        super(
            String.format("Insufficient balance in account %s: Available ₹%.2f, Requested ₹%.2f",
                accountId, availableBalance, requestedAmount),
            ERROR_CODE
        );
        this.accountId = accountId;
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }

    /**
     * Get the available balance at time of request.
     * 
     * @return Available balance in rupees
     */
    public double getAvailableBalance() {
        return availableBalance;
    }

    /**
     * Get the requested debit amount.
     * 
     * @return Requested amount in rupees
     */
    public double getRequestedAmount() {
        return requestedAmount;
    }

    /**
     * Calculate the shortfall amount.
     * 
     * @return Amount needed to complete transaction
     */
    public double getShortfall() {
        return requestedAmount - availableBalance;
    }

    /**
     * Get the account ID if available.
     * 
     * @return Account ID or null
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Check if balance is zero.
     * 
     * @return true if account has zero balance
     */
    public boolean isZeroBalance() {
        return availableBalance == 0.0;
    }
}
