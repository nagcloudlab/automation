package com.npci.level7;

/**
 * Level 7: Static Members - Account Number Generator
 *
 * This class demonstrates a UTILITY class pattern:
 * - All methods are static
 * - No instance variables (or only static)
 * - Private constructor prevents instantiation
 * - Used for generating unique IDs/numbers
 */
public class AccountNumberGenerator {

    // ═══════════════════════════════════════════════════════════════
    // STATIC VARIABLES
    // ═══════════════════════════════════════════════════════════════

    private static final String BANK_CODE = "NPCI";

    // Counters for different account types
    private static int savingsCounter = 0;
    private static int currentCounter = 0;
    private static int fdCounter = 0;
    private static int loanCounter = 0;

    // Branch-wise counters
    private static java.util.Map<String, Integer> branchCounters = new java.util.HashMap<>();

    // ═══════════════════════════════════════════════════════════════
    // STATIC BLOCK - Initialize
    // ═══════════════════════════════════════════════════════════════

    static {
        System.out.println("[AccountNumberGenerator] Initialized");
    }

    // ═══════════════════════════════════════════════════════════════
    // PRIVATE CONSTRUCTOR - Prevents instantiation
    // This is a UTILITY class, no objects should be created
    // ═══════════════════════════════════════════════════════════════

    private AccountNumberGenerator() {
        // Private constructor - cannot create objects
        throw new UnsupportedOperationException("Utility class - cannot instantiate!");
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC METHODS - All functionality via class methods
    // ═══════════════════════════════════════════════════════════════

    /**
     * Generate savings account number
     * Format: NPCI-SAV-XXXXXXXX
     */
    public static String generateSavingsAccountNumber() {
        savingsCounter++;
        return String.format("%s-SAV-%08d", BANK_CODE, savingsCounter);
    }

    /**
     * Generate current account number
     * Format: NPCI-CUR-XXXXXXXX
     */
    public static String generateCurrentAccountNumber() {
        currentCounter++;
        return String.format("%s-CUR-%08d", BANK_CODE, currentCounter);
    }

    /**
     * Generate FD account number
     * Format: NPCI-FD-XXXXXXXX
     */
    public static String generateFDAccountNumber() {
        fdCounter++;
        return String.format("%s-FD-%08d", BANK_CODE, fdCounter);
    }

    /**
     * Generate loan account number
     * Format: NPCI-LN-XXXXXXXX
     */
    public static String generateLoanAccountNumber() {
        loanCounter++;
        return String.format("%s-LN-%08d", BANK_CODE, loanCounter);
    }

    /**
     * Generate branch-specific account number
     * Format: NPCI-BRANCHCODE-XXXXXXXX
     */
    public static String generateBranchAccountNumber(String branchCode) {
        int count = branchCounters.getOrDefault(branchCode, 0) + 1;
        branchCounters.put(branchCode, count);
        return String.format("%s-%s-%08d", BANK_CODE, branchCode, count);
    }

    /**
     * Generate account number with custom format
     */
    public static String generateCustomAccountNumber(String prefix, String type, int sequence) {
        return String.format("%s-%s-%08d", prefix, type, sequence);
    }

    /**
     * Validate account number format
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null) return false;
        // Pattern: XXXX-XXX-XXXXXXXX
        return accountNumber.matches("[A-Z]{4}-[A-Z]{2,3}-\\d{8}");
    }

    /**
     * Extract account type from account number
     */
    public static String getAccountType(String accountNumber) {
        if (!isValidAccountNumber(accountNumber)) return "UNKNOWN";
        String[] parts = accountNumber.split("-");
        switch (parts[1]) {
            case "SAV": return "Savings";
            case "CUR": return "Current";
            case "FD": return "Fixed Deposit";
            case "LN": return "Loan";
            default: return "Other";
        }
    }

    /**
     * Get generation statistics
     */
    public static void displayStatistics() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║    ACCOUNT NUMBER GENERATOR STATISTICS        ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("  Savings Accounts Generated : " + savingsCounter);
        System.out.println("  Current Accounts Generated : " + currentCounter);
        System.out.println("  FD Accounts Generated      : " + fdCounter);
        System.out.println("  Loan Accounts Generated    : " + loanCounter);
        System.out.println("  Branch-wise Accounts       : " + branchCounters);
        System.out.println("╚═══════════════════════════════════════════════╝");
    }

    /**
     * Reset all counters (for testing)
     */
    public static void resetCounters() {
        savingsCounter = 0;
        currentCounter = 0;
        fdCounter = 0;
        loanCounter = 0;
        branchCounters.clear();
        System.out.println("[AccountNumberGenerator] All counters reset");
    }
}