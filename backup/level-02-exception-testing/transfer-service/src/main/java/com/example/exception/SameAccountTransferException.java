package com.example.exception;

/**
 * Exception thrown when attempting to transfer to the same account.
 * 
 * NPCI Error Code: U16 (Risk threshold exceeded)
 * 
 * SCENARIOS:
 * ==========
 * - Same account ID for sender and receiver
 * - Same UPI ID resolved to same account
 * - Circular transfer detected
 * 
 * WHY PREVENT THIS?
 * =================
 * - No legitimate use case
 * - Potential fraud indicator
 * - Wastes system resources
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public class SameAccountTransferException extends TransferException {

    /** NPCI error code for risk/validation failure */
    public static final String ERROR_CODE = "U16";
    
    /** The account ID involved */
    private final String accountId;

    /**
     * Constructor with account ID.
     * 
     * @param accountId The account attempting self-transfer
     */
    public SameAccountTransferException(String accountId) {
        super(
            String.format("Cannot transfer to the same account: %s", accountId),
            ERROR_CODE
        );
        this.accountId = accountId;
    }

    /**
     * Constructor for UPI ID self-transfer.
     * 
     * @param upiId The UPI ID
     * @param accountId The resolved account ID
     */
    public SameAccountTransferException(String upiId, String accountId) {
        super(
            String.format("UPI IDs resolve to same account: UPI=%s, Account=%s", upiId, accountId),
            ERROR_CODE
        );
        this.accountId = accountId;
    }

    /**
     * Get the account ID involved.
     * 
     * @return Account ID
     */
    public String getAccountId() {
        return accountId;
    }
}
