package com.example.lifecycle;

import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;

import java.util.Arrays;
import java.util.List;

/**
 * Banking Test Fixtures - LEVEL 6
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                   CENTRALIZED TEST FIXTURES                   ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Purpose: Provide consistent, reusable test data              ║
 * ║                                                               ║
 * ║  Benefits:                                                    ║
 * ║  • DRY (Don't Repeat Yourself)                                ║
 * ║  • Consistent test data across tests                          ║
 * ║  • Easy to maintain and update                                ║
 * ║  • Self-documenting test scenarios                            ║
 * ║  • Reduces boilerplate in test classes                        ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * BANKING CONTEXT:
 * ================
 * These fixtures represent common account types in UPI ecosystem:
 * - Savings accounts (typical users)
 * - Merchant accounts (business)
 * - High-value accounts (for limit testing)
 * - Edge case accounts (zero balance, exact limits)
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public final class BankingTestFixtures {

    // ═══════════════════════════════════════════════════════════
    // CONSTANTS - UPI Limits
    // ═══════════════════════════════════════════════════════════

    /** Minimum UPI transaction: ₹1 */
    public static final double UPI_MIN_AMOUNT = 1.0;

    /** Maximum UPI transaction: ₹1,00,000 */
    public static final double UPI_MAX_AMOUNT = 100_000.0;

    /** Standard test balance: ₹50,000 */
    public static final double STANDARD_BALANCE = 50_000.0;

    // ═══════════════════════════════════════════════════════════
    // INDIVIDUAL ACCOUNT FIXTURES
    // ═══════════════════════════════════════════════════════════

    /**
     * Standard sender account.
     * Use for typical transfer testing.
     * 
     * @return Account with ₹50,000
     */
    public static Account sender() {
        return new Account("SENDER001", "Rajesh Kumar", 50_000.0);
    }

    /**
     * Standard receiver account.
     * 
     * @return Account with ₹25,000
     */
    public static Account receiver() {
        return new Account("RECEIVER001", "Priya Sharma", 25_000.0);
    }

    /**
     * Rich account for high-value testing.
     * Can handle maximum UPI transfers.
     * 
     * @return Account with ₹5,00,000
     */
    public static Account richAccount() {
        return new Account("RICH001", "Amit Wealthy", 500_000.0);
    }

    /**
     * Poor account for insufficient balance testing.
     * 
     * @return Account with ₹100
     */
    public static Account poorAccount() {
        return new Account("POOR001", "Budget User", 100.0);
    }

    /**
     * Zero balance account.
     * For edge case testing.
     * 
     * @return Account with ₹0
     */
    public static Account zeroBalanceAccount() {
        return new Account("ZERO001", "Empty Account", 0.0);
    }

    /**
     * Account with exactly maximum UPI transfer amount.
     * 
     * @return Account with ₹1,00,000
     */
    public static Account atMaxLimit() {
        return new Account("MAXLIMIT001", "Max Limit", UPI_MAX_AMOUNT);
    }

    /**
     * Account with exactly minimum UPI amount.
     * 
     * @return Account with ₹1
     */
    public static Account atMinLimit() {
        return new Account("MINLIMIT001", "Min Limit", UPI_MIN_AMOUNT);
    }

    /**
     * Merchant account (high volume business).
     * 
     * @return Account with ₹10,00,000
     */
    public static Account merchantAccount() {
        return new Account("MERCHANT001", "SuperMart Store", 1_000_000.0);
    }

    /**
     * Custom account with specified balance.
     * 
     * @param accountId Account ID
     * @param name Account holder name
     * @param balance Initial balance
     * @return New Account
     */
    public static Account custom(String accountId, String name, double balance) {
        return new Account(accountId, name, balance);
    }

    // ═══════════════════════════════════════════════════════════
    // ACCOUNT SETS (Multiple accounts for scenarios)
    // ═══════════════════════════════════════════════════════════

    /**
     * Standard sender-receiver pair.
     * 
     * @return Array of [sender, receiver]
     */
    public static Account[] senderReceiverPair() {
        return new Account[] { sender(), receiver() };
    }

    /**
     * Three accounts for chain transfer testing (A → B → C).
     * 
     * @return Array of [Alice, Bob, Charlie]
     */
    public static Account[] chainAccounts() {
        return new Account[] {
            new Account("ALICE", "Alice", 30_000.0),
            new Account("BOB", "Bob", 20_000.0),
            new Account("CHARLIE", "Charlie", 10_000.0)
        };
    }

    /**
     * Accounts for circular transfer testing (X → Y → Z → X).
     * All start with same balance.
     * 
     * @param balance Initial balance for each
     * @return Array of 3 accounts
     */
    public static Account[] circularAccounts(double balance) {
        return new Account[] {
            new Account("CIRCLE_X", "User X", balance),
            new Account("CIRCLE_Y", "User Y", balance),
            new Account("CIRCLE_Z", "User Z", balance)
        };
    }

    /**
     * Multiple employees for salary disbursement testing.
     * 
     * @param count Number of employees
     * @param initialBalance Starting balance for each
     * @return List of employee accounts
     */
    public static List<Account> employeeAccounts(int count, double initialBalance) {
        Account[] employees = new Account[count];
        for (int i = 0; i < count; i++) {
            employees[i] = new Account(
                "EMP" + String.format("%03d", i + 1),
                "Employee " + (i + 1),
                initialBalance
            );
        }
        return Arrays.asList(employees);
    }

    /**
     * Company account for payroll testing.
     * 
     * @param balance Payroll fund balance
     * @return Company account
     */
    public static Account companyPayroll(double balance) {
        return new Account("COMPANY001", "TechCorp Payroll", balance);
    }

    // ═══════════════════════════════════════════════════════════
    // REPOSITORY FIXTURES (Pre-configured repositories)
    // ═══════════════════════════════════════════════════════════

    /**
     * Empty repository.
     * 
     * @return New empty InMemoryAccountRepository
     */
    public static InMemoryAccountRepository emptyRepository() {
        return new InMemoryAccountRepository();
    }

    /**
     * Repository with sender and receiver.
     * Most common setup for transfer tests.
     * 
     * @return Repository with 2 accounts
     */
    public static InMemoryAccountRepository standardRepository() {
        InMemoryAccountRepository repo = new InMemoryAccountRepository();
        repo.addAccount(sender());
        repo.addAccount(receiver());
        return repo;
    }

    /**
     * Repository with UPI ID mappings.
     * 
     * @return Repository with accounts and UPI mappings
     */
    public static InMemoryAccountRepository repositoryWithUpiMappings() {
        InMemoryAccountRepository repo = standardRepository();
        repo.addUpiMapping("rajesh@upi", "SENDER001");
        repo.addUpiMapping("priya@upi", "RECEIVER001");
        return repo;
    }

    /**
     * Repository with chain accounts.
     * 
     * @return Repository with 3 accounts for chain testing
     */
    public static InMemoryAccountRepository chainRepository() {
        InMemoryAccountRepository repo = new InMemoryAccountRepository();
        for (Account acc : chainAccounts()) {
            repo.addAccount(acc);
        }
        return repo;
    }

    /**
     * Repository with various account types for comprehensive testing.
     * 
     * @return Repository with 5 diverse accounts
     */
    public static InMemoryAccountRepository comprehensiveRepository() {
        InMemoryAccountRepository repo = new InMemoryAccountRepository();
        repo.addAccount(sender());
        repo.addAccount(receiver());
        repo.addAccount(richAccount());
        repo.addAccount(poorAccount());
        repo.addAccount(merchantAccount());
        return repo;
    }

    // ═══════════════════════════════════════════════════════════
    // TRANSFER AMOUNTS
    // ═══════════════════════════════════════════════════════════

    /** Common P2P transfer amounts */
    public static double[] commonP2PAmounts() {
        return new double[] { 10, 50, 100, 200, 500, 1000, 2000, 5000 };
    }

    /** Common merchant payment amounts */
    public static double[] commonMerchantAmounts() {
        return new double[] { 99, 199, 299, 499, 999, 1499, 2499, 4999 };
    }

    /** Boundary test amounts */
    public static double[] boundaryAmounts() {
        return new double[] {
            0.99,           // Below minimum
            UPI_MIN_AMOUNT, // Exactly minimum (₹1)
            1.01,           // Just above minimum
            99_999.99,      // Just below maximum
            UPI_MAX_AMOUNT, // Exactly maximum (₹1,00,000)
            100_000.01      // Above maximum
        };
    }

    // ═══════════════════════════════════════════════════════════
    // PRIVATE CONSTRUCTOR (Utility class)
    // ═══════════════════════════════════════════════════════════

    private BankingTestFixtures() {
        throw new AssertionError("Utility class - do not instantiate");
    }
}
