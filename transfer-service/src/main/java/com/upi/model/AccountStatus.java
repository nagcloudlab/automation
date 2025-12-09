package com.upi.model;

/**
 * Account Status enumeration representing the operational state of an account.
 */
public enum AccountStatus {
    ACTIVE("Account is active and operational"),
    INACTIVE("Account is inactive"),
    SUSPENDED("Account is temporarily suspended"),
    CLOSED("Account is permanently closed"),
    BLOCKED("Account is blocked due to suspicious activity");

    private final String description;

    AccountStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
