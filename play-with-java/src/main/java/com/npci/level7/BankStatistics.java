package com.npci.level7;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Level 7: Static Members - Bank Statistics Tracker
 *
 * Demonstrates static members for collecting and reporting statistics.
 */
public class BankStatistics {

    // ═══════════════════════════════════════════════════════════════
    // STATIC VARIABLES - Statistics counters
    // ═══════════════════════════════════════════════════════════════

    // Account statistics
    private static int totalAccountsOpened = 0;
    private static int totalAccountsClosed = 0;
    private static int savingsAccounts = 0;
    private static int currentAccounts = 0;
    private static int fdAccounts = 0;

    // Transaction statistics
    private static long totalTransactions = 0;
    private static long depositTransactions = 0;
    private static long withdrawalTransactions = 0;
    private static long transferTransactions = 0;

    // Amount statistics
    private static double totalDepositsAmount = 0;
    private static double totalWithdrawalsAmount = 0;
    private static double totalTransfersAmount = 0;

    // Channel-wise statistics
    private static Map<String, Long> channelTransactions = new HashMap<>();
    private static Map<String, Double> channelAmounts = new HashMap<>();

    // Daily statistics
    private static LocalDate currentDate = LocalDate.now();
    private static long dailyTransactions = 0;
    private static double dailyVolume = 0;

    // Peak statistics
    private static long peakTransactionsPerDay = 0;
    private static double peakVolumePerDay = 0;

    // ═══════════════════════════════════════════════════════════════
    // STATIC BLOCK
    // ═══════════════════════════════════════════════════════════════

    static {
        // Initialize channel maps
        channelTransactions.put("UPI", 0L);
        channelTransactions.put("IMPS", 0L);
        channelTransactions.put("NEFT", 0L);
        channelTransactions.put("RTGS", 0L);
        channelTransactions.put("ATM", 0L);
        channelTransactions.put("BRANCH", 0L);

        channelAmounts.put("UPI", 0.0);
        channelAmounts.put("IMPS", 0.0);
        channelAmounts.put("NEFT", 0.0);
        channelAmounts.put("RTGS", 0.0);
        channelAmounts.put("ATM", 0.0);
        channelAmounts.put("BRANCH", 0.0);

        System.out.println("[BankStatistics] Statistics tracker initialized");
    }

    // ═══════════════════════════════════════════════════════════════
    // PRIVATE CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    private BankStatistics() {
        throw new UnsupportedOperationException("Statistics class");
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC METHODS - Record events
    // ═══════════════════════════════════════════════════════════════

    /**
     * Record account opening
     */
    public static void recordAccountOpening(String accountType) {
        totalAccountsOpened++;
        switch (accountType) {
            case "Savings": savingsAccounts++; break;
            case "Current": currentAccounts++; break;
            case "FD": fdAccounts++; break;
        }
    }

    /**
     * Record account closure
     */
    public static void recordAccountClosure(String accountType) {
        totalAccountsClosed++;
        switch (accountType) {
            case "Savings": savingsAccounts--; break;
            case "Current": currentAccounts--; break;
            case "FD": fdAccounts--; break;
        }
    }

    /**
     * Record deposit
     */
    public static void recordDeposit(double amount, String channel) {
        checkDateRollover();

        totalTransactions++;
        depositTransactions++;
        totalDepositsAmount += amount;

        dailyTransactions++;
        dailyVolume += amount;

        recordChannelTransaction(channel, amount);
    }

    /**
     * Record withdrawal
     */
    public static void recordWithdrawal(double amount, String channel) {
        checkDateRollover();

        totalTransactions++;
        withdrawalTransactions++;
        totalWithdrawalsAmount += amount;

        dailyTransactions++;
        dailyVolume += amount;

        recordChannelTransaction(channel, amount);
    }

    /**
     * Record transfer
     */
    public static void recordTransfer(double amount, String channel) {
        checkDateRollover();

        totalTransactions++;
        transferTransactions++;
        totalTransfersAmount += amount;

        dailyTransactions++;
        dailyVolume += amount;

        recordChannelTransaction(channel, amount);
    }

    /**
     * Record channel transaction
     */
    private static void recordChannelTransaction(String channel, double amount) {
        channelTransactions.merge(channel, 1L, Long::sum);
        channelAmounts.merge(channel, amount, Double::sum);
    }

    /**
     * Check for date rollover and update peaks
     */
    private static void checkDateRollover() {
        LocalDate today = LocalDate.now();
        if (!today.equals(currentDate)) {
            // Update peak values
            if (dailyTransactions > peakTransactionsPerDay) {
                peakTransactionsPerDay = dailyTransactions;
            }
            if (dailyVolume > peakVolumePerDay) {
                peakVolumePerDay = dailyVolume;
            }

            // Reset daily counters
            currentDate = today;
            dailyTransactions = 0;
            dailyVolume = 0;
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC METHODS - Get statistics
    // ═══════════════════════════════════════════════════════════════

    public static int getTotalAccountsOpened() { return totalAccountsOpened; }
    public static int getActiveAccounts() { return totalAccountsOpened - totalAccountsClosed; }
    public static long getTotalTransactions() { return totalTransactions; }
    public static double getTotalVolume() { return totalDepositsAmount + totalWithdrawalsAmount + totalTransfersAmount; }

    /**
     * Get average transaction amount
     */
    public static double getAverageTransactionAmount() {
        if (totalTransactions == 0) return 0;
        return getTotalVolume() / totalTransactions;
    }

    /**
     * Display full statistics
     */
    public static void displayStatistics() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                   BANK STATISTICS                             ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("  ACCOUNT STATISTICS:");
        System.out.println("    Total Opened     : " + totalAccountsOpened);
        System.out.println("    Total Closed     : " + totalAccountsClosed);
        System.out.println("    Active Accounts  : " + getActiveAccounts());
        System.out.println("    Savings          : " + savingsAccounts);
        System.out.println("    Current          : " + currentAccounts);
        System.out.println("    Fixed Deposit    : " + fdAccounts);
        System.out.println("├───────────────────────────────────────────────────────────────┤");
        System.out.println("  TRANSACTION STATISTICS:");
        System.out.println("    Total Transactions : " + totalTransactions);
        System.out.println("    Deposits           : " + depositTransactions);
        System.out.println("    Withdrawals        : " + withdrawalTransactions);
        System.out.println("    Transfers          : " + transferTransactions);
        System.out.println("├───────────────────────────────────────────────────────────────┤");
        System.out.println("  VOLUME STATISTICS:");
        System.out.println("    Total Deposits     : Rs." + String.format("%,.2f", totalDepositsAmount));
        System.out.println("    Total Withdrawals  : Rs." + String.format("%,.2f", totalWithdrawalsAmount));
        System.out.println("    Total Transfers    : Rs." + String.format("%,.2f", totalTransfersAmount));
        System.out.println("    Average Transaction: Rs." + String.format("%,.2f", getAverageTransactionAmount()));
        System.out.println("├───────────────────────────────────────────────────────────────┤");
        System.out.println("  CHANNEL-WISE TRANSACTIONS:");
        for (Map.Entry<String, Long> entry : channelTransactions.entrySet()) {
            if (entry.getValue() > 0) {
                System.out.printf("    %-8s : %,d txns | Rs.%,.2f%n",
                        entry.getKey(), entry.getValue(), channelAmounts.get(entry.getKey()));
            }
        }
        System.out.println("├───────────────────────────────────────────────────────────────┤");
        System.out.println("  TODAY'S STATISTICS (" + currentDate + "):");
        System.out.println("    Transactions       : " + dailyTransactions);
        System.out.println("    Volume             : Rs." + String.format("%,.2f", dailyVolume));
        System.out.println("├───────────────────────────────────────────────────────────────┤");
        System.out.println("  PEAK STATISTICS:");
        System.out.println("    Peak Txns/Day      : " + peakTransactionsPerDay);
        System.out.println("    Peak Volume/Day    : Rs." + String.format("%,.2f", peakVolumePerDay));
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }

    /**
     * Reset all statistics
     */
    public static void resetStatistics() {
        totalAccountsOpened = 0;
        totalAccountsClosed = 0;
        savingsAccounts = 0;
        currentAccounts = 0;
        fdAccounts = 0;
        totalTransactions = 0;
        depositTransactions = 0;
        withdrawalTransactions = 0;
        transferTransactions = 0;
        totalDepositsAmount = 0;
        totalWithdrawalsAmount = 0;
        totalTransfersAmount = 0;
        dailyTransactions = 0;
        dailyVolume = 0;
        channelTransactions.replaceAll((k, v) -> 0L);
        channelAmounts.replaceAll((k, v) -> 0.0);
        System.out.println("[BankStatistics] All statistics reset");
    }
}