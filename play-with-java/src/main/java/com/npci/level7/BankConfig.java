package com.npci.level7;

import java.util.HashMap;
import java.util.Map;

/**
 * Level 7: Static Members - Bank Configuration
 *
 * Demonstrates using static members for application-wide configuration.
 * Singleton-like pattern using static members.
 */
public class BankConfig {

    // ═══════════════════════════════════════════════════════════════
    // STATIC CONSTANTS - Fixed configuration
    // ═══════════════════════════════════════════════════════════════

    public static final String BANK_NAME = "NPCI Bank Limited";
    public static final String BANK_CODE = "NPCI";
    public static final String COUNTRY_CODE = "IN";
    public static final String CURRENCY = "INR";
    public static final String CURRENCY_SYMBOL = "Rs.";

    // ═══════════════════════════════════════════════════════════════
    // STATIC VARIABLES - Configurable settings
    // ═══════════════════════════════════════════════════════════════

    // Account limits
    private static double savingsMinBalance = 1000.0;
    private static double currentMinBalance = 5000.0;
    private static double dailyWithdrawalLimit = 100000.0;
    private static double dailyTransferLimit = 500000.0;

    // Interest rates
    private static double savingsInterestRate = 4.0;
    private static double fdInterestRate1Year = 6.5;
    private static double fdInterestRate3Year = 7.0;
    private static double fdInterestRate5Year = 7.5;
    private static double loanInterestRate = 10.5;

    // Transaction limits
    private static double upiLimit = 100000.0;
    private static double impsLimit = 500000.0;
    private static double neftLimit = Double.MAX_VALUE;
    private static double rtgsMinimum = 200000.0;

    // Charges
    private static double impsCharges = 5.0;
    private static double neftCharges = 2.5;
    private static double rtgsCharges = 25.0;
    private static double chequeBookCharges = 50.0;

    // Feature flags
    private static boolean upiEnabled = true;
    private static boolean impsEnabled = true;
    private static boolean neftEnabled = true;
    private static boolean rtgsEnabled = true;

    // Dynamic configuration storage
    private static Map<String, String> customConfig = new HashMap<>();

    // ═══════════════════════════════════════════════════════════════
    // STATIC BLOCK - Initialize configuration
    // ═══════════════════════════════════════════════════════════════

    static {
        System.out.println("[BankConfig] Configuration loaded");

        // Initialize custom config
        customConfig.put("support.email", "support@npcibank.com");
        customConfig.put("support.phone", "1800-123-4567");
        customConfig.put("headquarters", "Mumbai");
    }

    // ═══════════════════════════════════════════════════════════════
    // PRIVATE CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    private BankConfig() {
        throw new UnsupportedOperationException("Configuration class");
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC GETTERS - Read configuration
    // ═══════════════════════════════════════════════════════════════

    // Account limits
    public static double getSavingsMinBalance() { return savingsMinBalance; }
    public static double getCurrentMinBalance() { return currentMinBalance; }
    public static double getDailyWithdrawalLimit() { return dailyWithdrawalLimit; }
    public static double getDailyTransferLimit() { return dailyTransferLimit; }

    // Interest rates
    public static double getSavingsInterestRate() { return savingsInterestRate; }
    public static double getFdInterestRate(int years) {
        if (years <= 1) return fdInterestRate1Year;
        if (years <= 3) return fdInterestRate3Year;
        return fdInterestRate5Year;
    }
    public static double getLoanInterestRate() { return loanInterestRate; }

    // Transaction limits
    public static double getUpiLimit() { return upiLimit; }
    public static double getImpsLimit() { return impsLimit; }
    public static double getNeftLimit() { return neftLimit; }
    public static double getRtgsMinimum() { return rtgsMinimum; }

    // Charges
    public static double getImpsCharges() { return impsCharges; }
    public static double getNeftCharges() { return neftCharges; }
    public static double getRtgsCharges() { return rtgsCharges; }
    public static double getChequeBookCharges() { return chequeBookCharges; }

    // Feature flags
    public static boolean isUpiEnabled() { return upiEnabled; }
    public static boolean isImpsEnabled() { return impsEnabled; }
    public static boolean isNeftEnabled() { return neftEnabled; }
    public static boolean isRtgsEnabled() { return rtgsEnabled; }

    // Custom config
    public static String getConfig(String key) {
        return customConfig.get(key);
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC SETTERS - Update configuration (admin functions)
    // ═══════════════════════════════════════════════════════════════

    public static void setSavingsInterestRate(double rate) {
        System.out.println("[Config Update] Savings Interest: " + savingsInterestRate + "% → " + rate + "%");
        savingsInterestRate = rate;
    }

    public static void setDailyWithdrawalLimit(double limit) {
        System.out.println("[Config Update] Daily Withdrawal: Rs." + dailyWithdrawalLimit + " → Rs." + limit);
        dailyWithdrawalLimit = limit;
    }

    public static void setUpiLimit(double limit) {
        System.out.println("[Config Update] UPI Limit: Rs." + upiLimit + " → Rs." + limit);
        upiLimit = limit;
    }

    public static void setUpiEnabled(boolean enabled) {
        System.out.println("[Config Update] UPI: " + (upiEnabled ? "Enabled" : "Disabled") +
                " → " + (enabled ? "Enabled" : "Disabled"));
        upiEnabled = enabled;
    }

    public static void setConfig(String key, String value) {
        customConfig.put(key, value);
        System.out.println("[Config Update] " + key + " = " + value);
    }

    // ═══════════════════════════════════════════════════════════════
    // DISPLAY CONFIGURATION
    // ═══════════════════════════════════════════════════════════════

    public static void displayConfiguration() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              BANK CONFIGURATION                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("  BANK INFORMATION:");
        System.out.println("    Name             : " + BANK_NAME);
        System.out.println("    Code             : " + BANK_CODE);
        System.out.println("    Country          : " + COUNTRY_CODE);
        System.out.println("    Currency         : " + CURRENCY);
        System.out.println("├───────────────────────────────────────────────────────────┤");
        System.out.println("  ACCOUNT LIMITS:");
        System.out.println("    Savings Min Bal  : " + CURRENCY_SYMBOL + savingsMinBalance);
        System.out.println("    Current Min Bal  : " + CURRENCY_SYMBOL + currentMinBalance);
        System.out.println("    Daily Withdrawal : " + CURRENCY_SYMBOL + dailyWithdrawalLimit);
        System.out.println("    Daily Transfer   : " + CURRENCY_SYMBOL + dailyTransferLimit);
        System.out.println("├───────────────────────────────────────────────────────────┤");
        System.out.println("  INTEREST RATES:");
        System.out.println("    Savings Account  : " + savingsInterestRate + "%");
        System.out.println("    FD (1 Year)      : " + fdInterestRate1Year + "%");
        System.out.println("    FD (3 Years)     : " + fdInterestRate3Year + "%");
        System.out.println("    FD (5 Years)     : " + fdInterestRate5Year + "%");
        System.out.println("    Loan             : " + loanInterestRate + "%");
        System.out.println("├───────────────────────────────────────────────────────────┤");
        System.out.println("  TRANSACTION LIMITS:");
        System.out.println("    UPI Limit        : " + CURRENCY_SYMBOL + upiLimit);
        System.out.println("    IMPS Limit       : " + CURRENCY_SYMBOL + impsLimit);
        System.out.println("    NEFT Limit       : No Limit");
        System.out.println("    RTGS Minimum     : " + CURRENCY_SYMBOL + rtgsMinimum);
        System.out.println("├───────────────────────────────────────────────────────────┤");
        System.out.println("  FEATURE STATUS:");
        System.out.println("    UPI              : " + (upiEnabled ? "✓ Enabled" : "✗ Disabled"));
        System.out.println("    IMPS             : " + (impsEnabled ? "✓ Enabled" : "✗ Disabled"));
        System.out.println("    NEFT             : " + (neftEnabled ? "✓ Enabled" : "✗ Disabled"));
        System.out.println("    RTGS             : " + (rtgsEnabled ? "✓ Enabled" : "✗ Disabled"));
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }
}