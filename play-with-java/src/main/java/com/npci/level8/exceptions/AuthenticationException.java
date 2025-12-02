package com.npci.level8.exceptions;

/**
 * Level 8: Exception Handling - Authentication Exception
 *
 * Thrown when authentication fails (PIN, OTP, Password).
 */
public class AuthenticationException extends BankingException {

    private String authType;  // PIN, OTP, PASSWORD, BIOMETRIC
    private int attemptNumber;
    private int maxAttempts;
    private boolean accountLocked;

    public AuthenticationException(String accountNumber, String authType,
                                   int attemptNumber, int maxAttempts) {
        super(buildMessage(authType, attemptNumber, maxAttempts), "BANK_ERR_006");
        this.withAccountNumber(accountNumber);
        this.authType = authType;
        this.attemptNumber = attemptNumber;
        this.maxAttempts = maxAttempts;
        this.accountLocked = attemptNumber >= maxAttempts;
    }

    private static String buildMessage(String authType, int attempt, int max) {
        if (attempt >= max) {
            return authType + " authentication failed. Account locked after " + max + " attempts.";
        }
        return authType + " authentication failed. Attempt " + attempt + " of " + max + ".";
    }

    public String getAuthType() { return authType; }
    public int getAttemptNumber() { return attemptNumber; }
    public int getMaxAttempts() { return maxAttempts; }
    public int getRemainingAttempts() { return Math.max(0, maxAttempts - attemptNumber); }
    public boolean isAccountLocked() { return accountLocked; }

    @Override
    public String getDetailedMessage() {
        return String.format(
                "╔═══════════════════════════════════════════════╗\n" +
                        "║      AUTHENTICATION FAILED ERROR              ║\n" +
                        "╠═══════════════════════════════════════════════╣\n" +
                        "  Error Code       : %s\n" +
                        "  Account Number   : %s\n" +
                        "  Auth Type        : %s\n" +
                        "  Attempt          : %d of %d\n" +
                        "  Remaining        : %d\n" +
                        "  Account Locked   : %s\n" +
                        "╚═══════════════════════════════════════════════╝",
                getErrorCode(), getAccountNumber(), authType, attemptNumber, maxAttempts,
                getRemainingAttempts(), accountLocked ? "YES ⚠" : "No");
    }
}