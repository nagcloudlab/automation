package com.npci.level7;

/**
 * Level 7: Static Members - Bank Class with Static and Instance Members
 *
 * Demonstrates practical use of static members in a Bank class.
 */
public class Bank {

    // ═══════════════════════════════════════════════════════════════
    // STATIC MEMBERS - Shared across all Bank instances (if any)
    // In practice, Bank might be a singleton or utility class
    // ═══════════════════════════════════════════════════════════════

    private static String bankName = BankConfig.BANK_NAME;
    private static String bankCode = BankConfig.BANK_CODE;
    private static int totalBranches = 0;
    private static int totalCustomers = 0;

    // ═══════════════════════════════════════════════════════════════
    // STATIC FACTORY METHOD - Create accounts
    // ═══════════════════════════════════════════════════════════════

    /**
     * Create savings account (static factory method)
     */
    public static BankAccount createSavingsAccount(String holderName, double initialDeposit) {
        if (initialDeposit < BankConfig.getSavingsMinBalance()) {
            System.out.println("Error: Minimum deposit is Rs." + BankConfig.getSavingsMinBalance());
            return null;
        }

        BankAccount account = new BankAccount(holderName, initialDeposit, "Savings");
        BankStatistics.recordAccountOpening("Savings");
        BankStatistics.recordDeposit(initialDeposit, "BRANCH");
        AuditLogger.logAccountEvent(account.getAccountNumber(), "ACCOUNT_OPENED",
                "Savings account opened for " + holderName);

        return account;
    }

    /**
     * Create current account (static factory method)
     */
    public static BankAccount createCurrentAccount(String holderName, double initialDeposit) {
        if (initialDeposit < BankConfig.getCurrentMinBalance()) {
            System.out.println("Error: Minimum deposit is Rs." + BankConfig.getCurrentMinBalance());
            return null;
        }

        BankAccount account = new BankAccount(holderName, initialDeposit, "Current");
        BankStatistics.recordAccountOpening("Current");
        BankStatistics.recordDeposit(initialDeposit, "BRANCH");
        AuditLogger.logAccountEvent(account.getAccountNumber(), "ACCOUNT_OPENED",
                "Current account opened for " + holderName);

        return account;
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC UTILITY METHODS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Transfer between accounts
     */
    public static boolean transfer(BankAccount from, BankAccount to, double amount, String channel) {
        if (from == null || to == null) {
            AuditLogger.error("Transfer failed: Invalid accounts");
            return false;
        }

        if (amount > BankConfig.getDailyTransferLimit()) {
            AuditLogger.error("Transfer failed: Exceeds daily limit");
            return false;
        }

        // Generate transaction ID
        String txnId;
        switch (channel) {
            case "UPI": txnId = TransactionIdGenerator.generateUPITransactionId(); break;
            case "IMPS": txnId = TransactionIdGenerator.generateIMPSTransactionId(); break;
            case "NEFT": txnId = TransactionIdGenerator.generateNEFTTransactionId(); break;
            case "RTGS": txnId = TransactionIdGenerator.generateRTGSTransactionId(); break;
            default: txnId = TransactionIdGenerator.generateTimestampBasedId("TRF"); break;
        }

        // Log start
        AuditLogger.logTransaction(txnId, "TRANSFER_INITIATED", from.getAccountNumber(), amount, "PROCESSING");

        // Perform transfer
        double fromBalanceBefore = from.getBalance();
        from.withdraw(amount);

        if (from.getBalance() == fromBalanceBefore) {
            // Withdrawal failed
            AuditLogger.logTransaction(txnId, "TRANSFER", from.getAccountNumber(), amount, "FAILED");
            return false;
        }

        to.deposit(amount);

        // Record statistics
        BankStatistics.recordTransfer(amount, channel);

        // Log success
        AuditLogger.logTransaction(txnId, "TRANSFER_COMPLETED",
                from.getAccountNumber() + " → " + to.getAccountNumber(), amount, "SUCCESS");

        return true;
    }

    /**
     * Display bank information
     */
    public static void displayBankInfo() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                    BANK INFORMATION                       ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("  Bank Name      : " + bankName);
        System.out.println("  Bank Code      : " + bankCode);
        System.out.println("  Total Branches : " + totalBranches);
        System.out.println("  Total Customers: " + totalCustomers);
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("  Account Stats  : " + BankAccount.getTotalAccountsCreated() + " created, " +
                BankAccount.getActiveAccountCount() + " active");
        System.out.println("  Total Deposits : Rs." + String.format("%,.2f", BankAccount.getTotalBankDeposits()));
        System.out.println("  Transactions   : " + TransactionIdGenerator.getTotalTransactions());
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }

    /**
     * Display all statistics
     */
    public static void displayAllStatistics() {
        displayBankInfo();
        BankConfig.displayConfiguration();
        BankAccount.displayBankStatistics();
        BankStatistics.displayStatistics();
        AccountNumberGenerator.displayStatistics();
        TransactionIdGenerator.displayStatistics();
        ValidationUtils.displayStatistics();
        AuditLogger.displayStatistics();
    }
}