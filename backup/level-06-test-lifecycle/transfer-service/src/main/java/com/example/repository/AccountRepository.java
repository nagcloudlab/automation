package com.example.repository;

import com.example.model.Account;
import java.util.Optional;

/**
 * Repository interface for Account operations.
 * 
 * WHY INTERFACE?
 * ==============
 * 1. Abstraction: Defines WHAT operations are available, not HOW they work
 * 2. Testability: Tests can use mock implementations
 * 3. Flexibility: Can swap SQL → NoSQL → Cache without changing service
 * 4. SOLID: Follows Dependency Inversion Principle
 * 
 * BANKING CONTEXT:
 * ================
 * In NPCI systems, repositories abstract access to:
 * - Core Banking System (CBS) for account data
 * - NPCI Switch for transaction routing
 * - Fraud Detection systems
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public interface AccountRepository {
    
    /**
     * Load account by unique account ID.
     * 
     * @param accountId The unique account identifier (e.g., "ACC001")
     * @return Account object with current balance
     * @throws RuntimeException if account not found (AccountNotFoundException in production)
     * 
     * Example:
     * <pre>
     *   Account acc = repository.loadAccountById("ACC001");
     *   System.out.println(acc.getBalance()); // 50000.0
     * </pre>
     */
    Account loadAccountById(String accountId);
    
    /**
     * Save or update account details.
     * Used after debit/credit operations to persist new balance.
     * 
     * @param account The account to save
     * 
     * Banking Note: In production, this would be part of a transaction
     * to ensure ACID properties (Atomicity, Consistency, Isolation, Durability)
     */
    void saveAccount(Account account);
    
    /**
     * Check if account exists.
     * Used for validation before transfer operations.
     * 
     * @param accountId The account ID to check
     * @return true if account exists and is active
     */
    boolean existsById(String accountId);
    
    /**
     * Find account by UPI ID (Virtual Payment Address).
     * 
     * @param upiId The UPI ID (e.g., "rajesh@upi", "9876543210@paytm")
     * @return Optional containing account if found, empty otherwise
     * 
     * UPI Context:
     * - UPI ID format: username@bankhandle (e.g., "rajesh@sbi", "priya@icici")
     * - One account can have multiple UPI IDs
     * - UPI ID is resolved to account number by PSP (Payment Service Provider)
     */
    Optional<Account> findByUpiId(String upiId);
}
