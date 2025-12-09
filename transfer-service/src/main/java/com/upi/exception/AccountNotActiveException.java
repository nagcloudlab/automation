package com.upi.exception;

import com.upi.model.AccountStatus;

/**
 * Exception thrown when attempting to perform a transaction on a non-active account.
 */
public class AccountNotActiveException extends RuntimeException {

    private final String accountId;
    private final AccountStatus currentStatus;

    public AccountNotActiveException(String accountId, AccountStatus currentStatus) {
        super(String.format("Account %s is not active. Current status: %s", accountId, currentStatus));
        this.accountId = accountId;
        this.currentStatus = currentStatus;
    }

    public String getAccountId() {
        return accountId;
    }

    public AccountStatus getCurrentStatus() {
        return currentStatus;
    }
}
