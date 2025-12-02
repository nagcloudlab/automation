package com.npci.level8.exceptions;

/**
 * Level 8: Exception Handling - Invalid Account Exception
 *
 * Thrown when account number is invalid or account not found.
 */
public class InvalidAccountException extends BankingException {

    private String invalidAccountNumber;
    private String reason;

    public InvalidAccountException(String accountNumber) {
        super("Account not found: " + accountNumber, "BANK_ERR_002");
        this.invalidAccountNumber = accountNumber;
        this.reason = "Account does not exist";
        this.withAccountNumber(accountNumber);
    }

    public InvalidAccountException(String accountNumber, String reason) {
        super("Invalid account: " + accountNumber + " - " + reason, "BANK_ERR_002");
        this.invalidAccountNumber = accountNumber;
        this.reason = reason;
        this.withAccountNumber(accountNumber);
    }

    public String getInvalidAccountNumber() { return invalidAccountNumber; }
    public String getReason() { return reason; }

    @Override
    public String getDetailedMessage() {
        return String.format(
                "╔═══════════════════════════════════════════════╗\n" +
                        "║       INVALID ACCOUNT ERROR                   ║\n" +
                        "╠═══════════════════════════════════════════════╣\n" +
                        "  Error Code     : %s\n" +
                        "  Account Number : %s\n" +
                        "  Reason         : %s\n" +
                        "╚═══════════════════════════════════════════════╝",
                getErrorCode(), invalidAccountNumber, reason);
    }
}