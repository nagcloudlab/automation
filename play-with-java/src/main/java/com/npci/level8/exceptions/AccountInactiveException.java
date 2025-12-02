package com.npci.level8.exceptions;

/**
 * Level 8: Exception Handling - Account Inactive Exception
 *
 * Thrown when operation attempted on inactive/blocked/closed account.
 */
public class AccountInactiveException extends BankingException {

    private String accountStatus;
    private String inactiveReason;

    public AccountInactiveException(String accountNumber, String status) {
        super("Account " + accountNumber + " is " + status, "BANK_ERR_003");
        this.withAccountNumber(accountNumber);
        this.accountStatus = status;
        this.inactiveReason = "Account is not active";
    }

    public AccountInactiveException(String accountNumber, String status, String reason) {
        super("Account " + accountNumber + " is " + status + ": " + reason, "BANK_ERR_003");
        this.withAccountNumber(accountNumber);
        this.accountStatus = status;
        this.inactiveReason = reason;
    }

    public String getAccountStatus() { return accountStatus; }
    public String getInactiveReason() { return inactiveReason; }

    @Override
    public String getDetailedMessage() {
        return String.format(
                "╔═══════════════════════════════════════════════╗\n" +
                        "║       ACCOUNT INACTIVE ERROR                  ║\n" +
                        "╠═══════════════════════════════════════════════╣\n" +
                        "  Error Code     : %s\n" +
                        "  Account Number : %s\n" +
                        "  Status         : %s\n" +
                        "  Reason         : %s\n" +
                        "╚═══════════════════════════════════════════════╝",
                getErrorCode(), getAccountNumber(), accountStatus, inactiveReason);
    }
}