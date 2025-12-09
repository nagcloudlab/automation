package com.upi.exception;

/**
 * Exception thrown when attempting to create an account that already exists.
 */
public class DuplicateAccountException extends RuntimeException {

    private final String accountId;

    public DuplicateAccountException(String accountId) {
        super(String.format("Account already exists with ID: %s", accountId));
        this.accountId = accountId;
    }

    public DuplicateAccountException(String message, String identifier) {
        super(message);
        this.accountId = identifier;
    }

    public String getAccountId() {
        return accountId;
    }
}
