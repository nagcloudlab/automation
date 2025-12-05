// ✅ NEW FILE: AccountRepository.java
package com.example.repository;

import com.example.model.Account;
import java.util.Optional;

/**
 * Repository interface for Account operations.
 * Follows the Repository Pattern - abstracts data access layer.
 *
 * Banking Context: This interface allows us to:
 * - Switch between different databases (Oracle CBS, PostgreSQL, etc.)
 * - Mock database calls for unit testing
 * - Implement caching layer transparently
 */
public interface AccountRepository {

    /**
     * Load account by account ID
     * @param accountId The unique account identifier
     * @return Account if found
     * @throws AccountNotFoundException if account doesn't exist
     */
    Account loadAccountById(String accountId);

    /**
     * Save/Update account details
     * @param account The account to save
     */
    void saveAccount(Account account);

    /**
     * Check if account exists
     * @param accountId The account ID to check
     * @return true if account exists
     */
    boolean existsById(String accountId);

    /**
     * Find account by UPI ID (e.g., "rajesh@upi")
     * @param upiId The UPI virtual payment address
     * @return Optional containing account if found
     */
    Optional<Account> findByUpiId(String upiId);
}