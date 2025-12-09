package com.example.repository;

import com.example.model.Account;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * SQL-based implementation of AccountRepository.
 * 
 * IMPLEMENTS INTERFACE:
 * =====================
 * This class now implements AccountRepository interface.
 * - Must provide implementation for ALL interface methods
 * - Can be swapped with any other implementation (Mock, NoSQL, Cache)
 * 
 * SIMULATION NOTE:
 * ================
 * For training, we use in-memory HashMap instead of real database.
 * In production NPCI systems, this would connect to:
 * - Oracle Database for CBS
 * - PostgreSQL for transaction logs
 * - Redis for caching
 * 
 * @author NPCI Training Team
 * @version 2.0 (Refactored for DI)
 */
public class SqlAccountRepository implements AccountRepository {

    // ═══════════════════════════════════════════════════════════
    // In-memory storage (simulates database tables)
    // ═══════════════════════════════════════════════════════════
    
    /** Account storage: accountId → Account */
    private final Map<String, Account> accountStore = new HashMap<>();
    
    /** UPI ID mapping: upiId → accountId */
    private final Map<String, String> upiIdMapping = new HashMap<>();

    /**
     * Constructor - Initialize repository with test data.
     * 
     * In production:
     * - Would receive DataSource/EntityManager via constructor (more DI!)
     * - Would not have test data initialization
     */
    public SqlAccountRepository() {
        System.out.println("[DB] SqlAccountRepository initialized");
        initializeTestData();
    }

    /**
     * Initialize sample accounts for training exercises.
     * 
     * These accounts will be used throughout all levels.
     */
    private void initializeTestData() {
        // Sample NPCI-style accounts
        Account rajesh = new Account("ACC001", "Rajesh Kumar", 50000.0);
        Account priya = new Account("ACC002", "Priya Sharma", 75000.0);
        Account amit = new Account("ACC003", "Amit Patel", 100000.0);
        Account sunita = new Account("ACC004", "Sunita Reddy", 25000.0);
        Account vikram = new Account("ACC005", "Vikram Mehta", 150000.0);

        // Store accounts
        accountStore.put("ACC001", rajesh);
        accountStore.put("ACC002", priya);
        accountStore.put("ACC003", amit);
        accountStore.put("ACC004", sunita);
        accountStore.put("ACC005", vikram);

        // Map UPI IDs to accounts
        upiIdMapping.put("rajesh@upi", "ACC001");
        upiIdMapping.put("rajesh@sbi", "ACC001");
        upiIdMapping.put("priya@upi", "ACC002");
        upiIdMapping.put("amit@upi", "ACC003");
        upiIdMapping.put("sunita@upi", "ACC004");
        upiIdMapping.put("vikram@upi", "ACC005");

        System.out.println("[DB] Loaded " + accountStore.size() + " test accounts");
        System.out.println("[DB] Mapped " + upiIdMapping.size() + " UPI IDs");
    }

    // ═══════════════════════════════════════════════════════════
    // Interface Implementation
    // ═══════════════════════════════════════════════════════════

    /**
     * {@inheritDoc}
     * 
     * Implementation Note:
     * Returns a COPY of the account to simulate database fetch behavior.
     * Each call returns a new object (like a real ORM would).
     */
    @Override
    public Account loadAccountById(String accountId) {
        System.out.println("[DB] SELECT * FROM accounts WHERE account_id = '" + accountId + "'");

        Account account = accountStore.get(accountId);
        if (account == null) {
            System.out.println("[DB] ❌ Account not found: " + accountId);
            throw new RuntimeException("Account not found: " + accountId);
        }

        // Return a copy (simulates ORM behavior)
        Account fetchedAccount = new Account(
            account.getAccountId(),
            account.getAccountHolderName(),
            account.getBalance()
        );

        System.out.println("[DB] ✓ Found: " + fetchedAccount.getAccountHolderName() + 
                          " | Balance: ₹" + fetchedAccount.getBalance());
        return fetchedAccount;
    }

    /**
     * {@inheritDoc}
     * 
     * Implementation Note:
     * Overwrites existing account or inserts new one.
     */
    @Override
    public void saveAccount(Account account) {
        System.out.println("[DB] UPDATE accounts SET balance = " + account.getBalance() + 
                          " WHERE account_id = '" + account.getAccountId() + "'");
        
        accountStore.put(account.getAccountId(), account);
        
        System.out.println("[DB] ✓ Saved: " + account.getAccountId() + 
                          " | New Balance: ₹" + account.getBalance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(String accountId) {
        boolean exists = accountStore.containsKey(accountId);
        System.out.println("[DB] EXISTS check for " + accountId + ": " + exists);
        return exists;
    }

    /**
     * {@inheritDoc}
     * 
     * Implementation Note:
     * First resolves UPI ID to account ID, then loads account.
     */
    @Override
    public Optional<Account> findByUpiId(String upiId) {
        System.out.println("[DB] Resolving UPI ID: " + upiId);

        String accountId = upiIdMapping.get(upiId.toLowerCase());
        if (accountId == null) {
            System.out.println("[DB] ❌ UPI ID not found: " + upiId);
            return Optional.empty();
        }

        System.out.println("[DB] ✓ UPI ID " + upiId + " → Account " + accountId);
        return Optional.of(loadAccountById(accountId));
    }

    // ═══════════════════════════════════════════════════════════
    // Additional Helper Methods (not in interface)
    // ═══════════════════════════════════════════════════════════

    /**
     * Get count of accounts (for testing/debugging).
     * @return Number of accounts in store
     */
    public int getAccountCount() {
        return accountStore.size();
    }

    /**
     * Print all accounts (for debugging).
     */
    public void printAllAccounts() {
        System.out.println("\n[DB] All Accounts:");
        System.out.println("-".repeat(50));
        accountStore.values().forEach(acc -> 
            System.out.printf("  %s | %s | ₹%.2f%n", 
                acc.getAccountId(), 
                acc.getAccountHolderName(), 
                acc.getBalance())
        );
        System.out.println("-".repeat(50));
    }
}
