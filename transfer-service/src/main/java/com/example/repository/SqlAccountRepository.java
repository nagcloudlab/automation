package com.example.repository;

import com.example.model.Account;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL-based implementation of AccountRepository.
 * In production, this would connect to actual database (Oracle/PostgreSQL).
 *
 * For training purposes, using in-memory HashMap to simulate DB.
 */
public class SqlAccountRepository implements AccountRepository {

    // Simulated database storage
    private final Map<String, Account> accountStore = new HashMap<>();
    private final Map<String, String> upiIdMapping = new HashMap<>(); // upiId -> accountId

    public SqlAccountRepository() {
        System.out.println("[DB] SqlAccountRepository initialized");
        initializeTestData();
    }

    /**
     * Initialize with sample accounts for testing
     */
    private void initializeTestData() {
        // Sample accounts for training
        Account acc1 = new Account("ACC001", "Rajesh Kumar", 50000.0);
        Account acc2 = new Account("ACC002", "Priya Sharma", 75000.0);
        Account acc3 = new Account("ACC003", "Amit Patel", 100000.0);

        accountStore.put("ACC001", acc1);
        accountStore.put("ACC002", acc2);
        accountStore.put("ACC003", acc3);

        upiIdMapping.put("rajesh@upi", "ACC001");
        upiIdMapping.put("priya@upi", "ACC002");
        upiIdMapping.put("amit@upi", "ACC003");

        System.out.println("[DB] Loaded " + accountStore.size() + " test accounts");
    }

    @Override
    public Account loadAccountById(String accountId) {
        System.out.println("[DB] Loading account: " + accountId);

        Account account = accountStore.get(accountId);
        if (account == null) {
            throw new RuntimeException("Account not found: " + accountId);
        }

        // Return a copy to simulate DB fetch (new object each time)
        return new Account(
                account.getAccountId(),
                account.getAccountHolderName(),
                account.getBalance()
        );
    }

    @Override
    public void saveAccount(Account account) {
        System.out.println("[DB] Saving account: " + account.getAccountId() +
                " | New Balance: ₹" + account.getBalance());
        accountStore.put(account.getAccountId(), account);
    }

    @Override
    public boolean existsById(String accountId) {
        return accountStore.containsKey(accountId);
    }

    @Override
    public Optional<Account> findByUpiId(String upiId) {
        System.out.println("[DB] Finding account by UPI ID: " + upiId);

        String accountId = upiIdMapping.get(upiId);
        if (accountId == null) {
            return Optional.empty();
        }
        return Optional.of(loadAccountById(accountId));
    }
}