package com.example.repository;

import com.example.model.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-Memory Account Repository for Integration Testing.
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║              LEVEL 5: INTEGRATION TEST REPOSITORY             ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Purpose: Provide a clean, controllable repository for        ║
 * ║  integration tests without database dependencies.             ║
 * ║                                                               ║
 * ║  Key Features:                                                ║
 * ║  • Starts empty (no pre-loaded data)                          ║
 * ║  • Test helper methods for setup                              ║
 * ║  • No console output (clean test logs)                        ║
 * ║  • Returns copies to simulate ORM behavior                    ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * WHY NOT USE SqlAccountRepository?
 * =================================
 * SqlAccountRepository:
 * - Has pre-loaded test data that interferes with tests
 * - Prints to console (clutters test output)
 * - Harder to control initial state
 * 
 * InMemoryAccountRepository:
 * - Starts empty - you control all data
 * - Silent operation - clean test logs
 * - Easy state management with clear() method
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public class InMemoryAccountRepository implements AccountRepository {

    // ═══════════════════════════════════════════════════════════
    // STORAGE
    // ═══════════════════════════════════════════════════════════

    /** Account storage: accountId → Account */
    private final Map<String, Account> accounts = new HashMap<>();

    /** UPI ID mapping: upiId → accountId */
    private final Map<String, String> upiMappings = new HashMap<>();

    // ═══════════════════════════════════════════════════════════
    // TEST HELPER METHODS
    // ═══════════════════════════════════════════════════════════

    /**
     * Add an account to the repository.
     * Stores a COPY to prevent external modification.
     * 
     * @param account Account to add
     * @return this repository (for chaining)
     */
    public InMemoryAccountRepository addAccount(Account account) {
        accounts.put(account.getAccountId(), copyAccount(account));
        return this;
    }

    /**
     * Add multiple accounts at once.
     * 
     * @param accountsToAdd Accounts to add
     * @return this repository (for chaining)
     */
    public InMemoryAccountRepository addAccounts(Account... accountsToAdd) {
        for (Account account : accountsToAdd) {
            addAccount(account);
        }
        return this;
    }

    /**
     * Add UPI ID mapping.
     * 
     * @param upiId UPI ID (e.g., "rajesh@upi")
     * @param accountId Account ID to map to
     * @return this repository (for chaining)
     */
    public InMemoryAccountRepository addUpiMapping(String upiId, String accountId) {
        upiMappings.put(upiId.toLowerCase(), accountId);
        return this;
    }

    /**
     * Clear all data.
     * Call in @BeforeEach or @AfterEach to ensure test isolation.
     */
    public void clear() {
        accounts.clear();
        upiMappings.clear();
    }

    /**
     * Get count of stored accounts.
     * Useful for test assertions.
     * 
     * @return Number of accounts
     */
    public int getAccountCount() {
        return accounts.size();
    }

    /**
     * Get count of UPI mappings.
     * 
     * @return Number of UPI ID mappings
     */
    public int getUpiMappingCount() {
        return upiMappings.size();
    }

    /**
     * Check if repository is empty.
     * 
     * @return true if no accounts stored
     */
    public boolean isEmpty() {
        return accounts.isEmpty();
    }

    /**
     * Get total balance across all accounts.
     * Useful for verifying conservation in transfers.
     * 
     * @return Sum of all account balances
     */
    public double getTotalBalance() {
        return accounts.values().stream()
            .mapToDouble(Account::getBalance)
            .sum();
    }

    // ═══════════════════════════════════════════════════════════
    // INTERFACE IMPLEMENTATION
    // ═══════════════════════════════════════════════════════════

    /**
     * {@inheritDoc}
     * 
     * Returns a COPY of the stored account (simulates ORM fetch behavior).
     * Returns null if not found (let service handle exception).
     */
    @Override
    public Account loadAccountById(String accountId) {
        Account account = accounts.get(accountId);
        return account != null ? copyAccount(account) : null;
    }

    /**
     * {@inheritDoc}
     * 
     * Stores a COPY to prevent external modification of stored data.
     */
    @Override
    public void saveAccount(Account account) {
        if (account != null) {
            accounts.put(account.getAccountId(), copyAccount(account));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(String accountId) {
        return accounts.containsKey(accountId);
    }

    /**
     * {@inheritDoc}
     * 
     * Resolves UPI ID to account ID, then loads account.
     */
    @Override
    public Optional<Account> findByUpiId(String upiId) {
        String accountId = upiMappings.get(upiId.toLowerCase());
        if (accountId == null) {
            return Optional.empty();
        }
        Account account = loadAccountById(accountId);
        return Optional.ofNullable(account);
    }

    // ═══════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ═══════════════════════════════════════════════════════════

    /**
     * Create a defensive copy of an account.
     * Prevents external code from modifying stored data directly.
     */
    private Account copyAccount(Account original) {
        return new Account(
            original.getAccountId(),
            original.getAccountHolderName(),
            original.getBalance()
        );
    }

    // ═══════════════════════════════════════════════════════════
    // DEBUGGING HELPERS
    // ═══════════════════════════════════════════════════════════

    /**
     * Get string representation of all accounts.
     * Useful for debugging test failures.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InMemoryAccountRepository [\n");
        accounts.forEach((id, acc) -> 
            sb.append(String.format("  %s: %s (₹%.2f)%n", 
                id, acc.getAccountHolderName(), acc.getBalance()))
        );
        sb.append("]");
        return sb.toString();
    }

    /**
     * Print repository state (for debugging).
     */
    public void printState() {
        System.out.println(this);
    }
}
