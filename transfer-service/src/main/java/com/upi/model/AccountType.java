package com.upi.model;

/**
 * Account Type enumeration representing different types of bank accounts.
 */
public enum AccountType {
    SAVINGS("Savings Account"),
    CURRENT("Current Account"),
    SALARY("Salary Account"),
    FIXED_DEPOSIT("Fixed Deposit Account"),
    RECURRING_DEPOSIT("Recurring Deposit Account");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
