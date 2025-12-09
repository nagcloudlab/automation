package com.example.integration;

import com.example.model.Account;

/**
 * Test Data Factory for Integration Tests.
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    TEST DATA FACTORY                          ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Purpose: Create consistent, reusable test accounts           ║
 * ║                                                               ║
 * ║  Benefits:                                                    ║
 * ║  • Reduces test setup boilerplate                             ║
 * ║  • Ensures consistent test data                               ║
 * ║  • Easy to create accounts for various scenarios              ║
 * ║  • Self-documenting with descriptive method names             ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * BANKING CONTEXT:
 * ================
 * In NPCI testing, common account types:
 * - Savings accounts (typical users)
 * - Current accounts (merchants)
 * - High-value accounts (for limit testing)
 * - Low-balance accounts (for failure testing)
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public class TestAccountFactory {

    // ═══════════════════════════════════════════════════════════
    // COUNTER FOR UNIQUE IDS
    // ═══════════════════════════════════════════════════════════
    
    private static int accountCounter = 0;

    // ═══════════════════════════════════════════════════════════
    // FACTORY METHODS
    // ═══════════════════════════════════════════════════════════

    /**
     * Create account with specific balance.
     * Auto-generates unique ID and name.
     * 
     * @param balance Initial balance
     * @return New Account instance
     */
    public static Account withBalance(double balance) {
        accountCounter++;
        return new Account(
            "ACC" + String.format("%03d", accountCounter),
            "Test User " + accountCounter,
            balance
        );
    }

    /**
     * Create account with custom ID and balance.
     * 
     * @param accountId Account ID
     * @param balance Initial balance
     * @return New Account instance
     */
    public static Account withIdAndBalance(String accountId, double balance) {
        return new Account(accountId, "User " + accountId, balance);
    }

    /**
     * Create fully customized account.
     * 
     * @param accountId Account ID
     * @param name Account holder name
     * @param balance Initial balance
     * @return New Account instance
     */
    public static Account create(String accountId, String name, double balance) {
        return new Account(accountId, name, balance);
    }

    // ═══════════════════════════════════════════════════════════
    // PRESET ACCOUNTS
    // ═══════════════════════════════════════════════════════════

    /**
     * Standard sender account with ₹10,000.
     * Typical for testing successful transfers.
     */
    public static Account sender() {
        return create("SENDER", "Rajesh Kumar", 10000.0);
    }

    /**
     * Standard receiver account with ₹5,000.
     */
    public static Account receiver() {
        return create("RECEIVER", "Priya Sharma", 5000.0);
    }

    /**
     * Rich account with ₹5,00,000.
     * Use for high-value transfer testing.
     */
    public static Account rich() {
        return create("RICH001", "Amit Wealthy", 500000.0);
    }

    /**
     * Poor account with ₹100.
     * Use for insufficient balance testing.
     */
    public static Account poor() {
        return create("POOR001", "Budget User", 100.0);
    }

    /**
     * Zero balance account.
     * Use for edge case testing.
     */
    public static Account empty() {
        return create("EMPTY001", "Empty Account", 0.0);
    }

    /**
     * Account at maximum UPI limit (₹1,00,000).
     */
    public static Account atMaxLimit() {
        return create("MAX001", "Max Balance", 100000.0);
    }

    /**
     * Merchant account (high volume, high balance).
     */
    public static Account merchant() {
        return create("MERCHANT001", "SuperMart Store", 1000000.0);
    }

    // ═══════════════════════════════════════════════════════════
    // ACCOUNT SETS
    // ═══════════════════════════════════════════════════════════

    /**
     * Create standard sender-receiver pair.
     * 
     * @return Array of [sender, receiver]
     */
    public static Account[] senderReceiverPair() {
        return new Account[] { sender(), receiver() };
    }

    /**
     * Create multiple accounts with same balance.
     * 
     * @param count Number of accounts
     * @param balance Balance for each
     * @return Array of accounts
     */
    public static Account[] multiple(int count, double balance) {
        Account[] accounts = new Account[count];
        for (int i = 0; i < count; i++) {
            accounts[i] = withBalance(balance);
        }
        return accounts;
    }

    /**
     * Create accounts for chain transfer testing (A → B → C).
     * 
     * @return Array of 3 accounts [A, B, C]
     */
    public static Account[] chainAccounts() {
        return new Account[] {
            create("CHAIN_A", "Alice", 15000.0),
            create("CHAIN_B", "Bob", 10000.0),
            create("CHAIN_C", "Charlie", 5000.0)
        };
    }

    /**
     * Create accounts for circular transfer testing (X → Y → Z → X).
     * All start with equal balance.
     * 
     * @param balance Initial balance for each
     * @return Array of 3 accounts
     */
    public static Account[] circularAccounts(double balance) {
        return new Account[] {
            create("CIRCLE_X", "X", balance),
            create("CIRCLE_Y", "Y", balance),
            create("CIRCLE_Z", "Z", balance)
        };
    }

    // ═══════════════════════════════════════════════════════════
    // UPI ACCOUNTS
    // ═══════════════════════════════════════════════════════════

    /**
     * Account with associated UPI ID data.
     * Contains account and UPI ID for mapping.
     */
    public static class UpiAccount {
        public final Account account;
        public final String upiId;

        public UpiAccount(Account account, String upiId) {
            this.account = account;
            this.upiId = upiId;
        }
    }

    /**
     * Create account with UPI ID.
     */
    public static UpiAccount withUpiId(String accountId, String name, 
                                       double balance, String upiId) {
        return new UpiAccount(
            new Account(accountId, name, balance),
            upiId
        );
    }

    /**
     * Preset UPI accounts for testing.
     */
    public static UpiAccount rajeshWithUpi() {
        return new UpiAccount(
            create("ACC001", "Rajesh Kumar", 50000.0),
            "rajesh@upi"
        );
    }

    public static UpiAccount priyaWithUpi() {
        return new UpiAccount(
            create("ACC002", "Priya Sharma", 25000.0),
            "priya@upi"
        );
    }

    // ═══════════════════════════════════════════════════════════
    // UTILITY METHODS
    // ═══════════════════════════════════════════════════════════

    /**
     * Reset the account counter.
     * Call in @BeforeEach to ensure unique IDs across tests.
     */
    public static void resetCounter() {
        accountCounter = 0;
    }

    /**
     * Get current counter value.
     */
    public static int getCounter() {
        return accountCounter;
    }
}
