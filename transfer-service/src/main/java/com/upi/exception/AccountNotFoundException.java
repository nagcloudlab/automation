package com.upi.exception;

/**
 * Exception thrown when an account is not found in the system.
 */
public class AccountNotFoundException extends RuntimeException {

    private final String accountId;

    public AccountNotFoundException(String accountId) {
        super(String.format("Account not found with ID: %s", accountId));
        this.accountId = accountId;
    }

    public AccountNotFoundException(String message, String accountId) {
        super(message);
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
