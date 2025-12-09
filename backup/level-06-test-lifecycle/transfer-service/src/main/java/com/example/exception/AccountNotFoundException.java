package com.example.exception;

/**
 * Exception thrown when an account is not found in the system.
 * 
 * NPCI Error Code: U30
 * 
 * SCENARIOS:
 * ==========
 * - Account ID doesn't exist in CBS
 * - UPI ID not mapped to any account
 * - Account deleted or closed
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public class AccountNotFoundException extends TransferException {

    /** NPCI error code for account not found */
    public static final String ERROR_CODE = "U30";
    
    /** The account ID that was not found */
    private final String accountId;
    
    /** Whether it was a UPI ID lookup */
    private final boolean upiIdLookup;

    /**
     * Constructor for account ID lookup failure.
     * 
     * @param accountId The account ID that was not found
     */
    public AccountNotFoundException(String accountId) {
        super(
            String.format("Account not found: %s", accountId),
            ERROR_CODE
        );
        this.accountId = accountId;
        this.upiIdLookup = false;
    }

    /**
     * Constructor for UPI ID lookup failure.
     * 
     * @param upiId The UPI ID that was not found
     * @param isUpiLookup Flag to differentiate from account ID
     */
    public AccountNotFoundException(String upiId, boolean isUpiLookup) {
        super(
            isUpiLookup 
                ? String.format("UPI ID not found: %s", upiId)
                : String.format("Account not found: %s", upiId),
            ERROR_CODE
        );
        this.accountId = upiId;
        this.upiIdLookup = isUpiLookup;
    }

    /**
     * Constructor with transaction reference.
     * 
     * @param accountId The account ID that was not found
     * @param transactionRef Transaction reference for tracing
     */
    public AccountNotFoundException(String accountId, String transactionRef) {
        super(
            String.format("Account not found: %s", accountId),
            ERROR_CODE,
            transactionRef
        );
        this.accountId = accountId;
        this.upiIdLookup = false;
    }

    /**
     * Get the account ID that was not found.
     * 
     * @return The missing account ID or UPI ID
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Check if this was a UPI ID lookup.
     * 
     * @return true if UPI ID lookup, false if account ID lookup
     */
    public boolean isUpiIdLookup() {
        return upiIdLookup;
    }
}
