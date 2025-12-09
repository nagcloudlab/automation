package com.upi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.upi.model.Account;
import com.upi.model.AccountStatus;
import com.upi.model.AccountType;

/**
 * Repository interface for Account entity operations.
 * Extends JpaRepository for full CRUD and query capabilities.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * Find account by account ID
     */
    Optional<Account> findByAccountId(String accountId);

    /**
     * Find all accounts by status
     */
    List<Account> findByStatus(AccountStatus status);

    /**
     * Find all accounts by account type
     */
    List<Account> findByAccountType(AccountType accountType);

    /**
     * Find accounts by holder name (case-insensitive partial match)
     */
    @Query("SELECT a FROM Account a WHERE LOWER(a.accountHolderName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Account> findByAccountHolderNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find account by email
     */
    Optional<Account> findByEmail(String email);

    /**
     * Find account by phone
     */
    Optional<Account> findByPhone(String phone);

    /**
     * Check if account exists by ID
     */
    boolean existsByAccountId(String accountId);

    /**
     * Check if email is already registered
     */
    boolean existsByEmail(String email);

    /**
     * Check if phone is already registered
     */
    boolean existsByPhone(String phone);

    /**
     * Find all active accounts
     */
    @Query("SELECT a FROM Account a WHERE a.status = 'ACTIVE'")
    List<Account> findAllActiveAccounts();

    /**
     * Count accounts by status
     */
    long countByStatus(AccountStatus status);
}
