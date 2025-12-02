package com.npci.level7;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Level 7: Static Members - Transaction ID Generator
 *
 * Another utility class demonstrating static members for ID generation.
 */
public class TransactionIdGenerator {

    // ═══════════════════════════════════════════════════════════════
    // STATIC CONSTANTS
    // ═══════════════════════════════════════════════════════════════

    private static final String UPI_PREFIX = "UPI";
    private static final String IMPS_PREFIX = "IMPS";
    private static final String NEFT_PREFIX = "NEFT";
    private static final String RTGS_PREFIX = "RTGS";
    private static final String ATM_PREFIX = "ATM";
    private static final String BRANCH_PREFIX = "BR";

    // ═══════════════════════════════════════════════════════════════
    // STATIC COUNTERS
    // ═══════════════════════════════════════════════════════════════

    private static long upiCounter = 0;
    private static long impsCounter = 0;
    private static long neftCounter = 0;
    private static long rtgsCounter = 0;
    private static long atmCounter = 0;
    private static long branchCounter = 0;

    private static long totalTransactions = 0;

    // ═══════════════════════════════════════════════════════════════
    // PRIVATE CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    private TransactionIdGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC METHODS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Generate UPI transaction ID
     * Format: UPI + YYYYMMDD + 10-digit sequence
     */
    public static String generateUPITransactionId() {
        upiCounter++;
        totalTransactions++;
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return String.format("%s%s%010d", UPI_PREFIX, date, upiCounter);
    }

    /**
     * Generate IMPS transaction ID
     */
    public static String generateIMPSTransactionId() {
        impsCounter++;
        totalTransactions++;
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return String.format("%s%s%010d", IMPS_PREFIX, date, impsCounter);
    }

    /**
     * Generate NEFT transaction ID
     */
    public static String generateNEFTTransactionId() {
        neftCounter++;
        totalTransactions++;
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return String.format("%s%s%010d", NEFT_PREFIX, date, neftCounter);
    }

    /**
     * Generate RTGS transaction ID
     */
    public static String generateRTGSTransactionId() {
        rtgsCounter++;
        totalTransactions++;
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return String.format("%s%s%010d", RTGS_PREFIX, date, rtgsCounter);
    }

    /**
     * Generate ATM transaction ID
     */
    public static String generateATMTransactionId(String atmId) {
        atmCounter++;
        totalTransactions++;
        return String.format("%s%s%08d", ATM_PREFIX, atmId, atmCounter);
    }

    /**
     * Generate Branch transaction ID
     */
    public static String generateBranchTransactionId(String branchCode) {
        branchCounter++;
        totalTransactions++;
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return String.format("%s%s%s%08d", BRANCH_PREFIX, branchCode, date, branchCounter);
    }

    /**
     * Generate generic transaction ID with timestamp
     */
    public static String generateTimestampBasedId(String prefix) {
        totalTransactions++;
        return prefix + System.currentTimeMillis();
    }

    /**
     * Get transaction type from ID
     */
    public static String getTransactionType(String transactionId) {
        if (transactionId == null) return "UNKNOWN";
        if (transactionId.startsWith("UPI")) return "UPI";
        if (transactionId.startsWith("IMPS")) return "IMPS";
        if (transactionId.startsWith("NEFT")) return "NEFT";
        if (transactionId.startsWith("RTGS")) return "RTGS";
        if (transactionId.startsWith("ATM")) return "ATM";
        if (transactionId.startsWith("BR")) return "Branch";
        return "UNKNOWN";
    }

    /**
     * Get total transactions
     */
    public static long getTotalTransactions() {
        return totalTransactions;
    }

    /**
     * Display statistics
     */
    public static void displayStatistics() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║   TRANSACTION ID GENERATOR STATISTICS         ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("  UPI Transactions    : " + upiCounter);
        System.out.println("  IMPS Transactions   : " + impsCounter);
        System.out.println("  NEFT Transactions   : " + neftCounter);
        System.out.println("  RTGS Transactions   : " + rtgsCounter);
        System.out.println("  ATM Transactions    : " + atmCounter);
        System.out.println("  Branch Transactions : " + branchCounter);
        System.out.println("  ─────────────────────────────────────────────");
        System.out.println("  TOTAL TRANSACTIONS  : " + totalTransactions);
        System.out.println("╚═══════════════════════════════════════════════╝");
    }
}