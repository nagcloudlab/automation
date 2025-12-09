package com.upi.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.upi.model.Transaction;
import com.upi.model.TransactionStatus;
import com.upi.model.TransactionType;

/**
 * Repository interface for Transaction entity operations.
 * Provides methods to query transaction history and audit trails.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find transaction by reference number
     */
    Optional<Transaction> findByReferenceNumber(String referenceNumber);

    /**
     * Find all transactions for an account (as sender or receiver)
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount.accountId = :accountId OR t.toAccount.accountId = :accountId ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccountId(@Param("accountId") String accountId);

    /**
     * Find all transactions for an account with pagination
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount.accountId = :accountId OR t.toAccount.accountId = :accountId ORDER BY t.transactionDate DESC")
    Page<Transaction> findByAccountIdPaginated(@Param("accountId") String accountId, Pageable pageable);

    /**
     * Find sent transactions (debits) for an account
     */
    List<Transaction> findByFromAccountAccountIdOrderByTransactionDateDesc(String accountId);

    /**
     * Find received transactions (credits) for an account
     */
    List<Transaction> findByToAccountAccountIdOrderByTransactionDateDesc(String accountId);

    /**
     * Find transactions by status
     */
    List<Transaction> findByStatus(TransactionStatus status);

    /**
     * Find transactions by type
     */
    List<Transaction> findByTransactionType(TransactionType transactionType);

    /**
     * Find transactions between two dates
     */
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<Transaction> findByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    /**
     * Find transactions for an account between two dates
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount.accountId = :accountId OR t.toAccount.accountId = :accountId) AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccountIdAndDateRange(
        @Param("accountId") String accountId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    /**
     * Find transactions between two specific accounts
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount.accountId = :account1 AND t.toAccount.accountId = :account2) OR (t.fromAccount.accountId = :account2 AND t.toAccount.accountId = :account1) ORDER BY t.transactionDate DESC")
    List<Transaction> findTransactionsBetweenAccounts(
        @Param("account1") String account1,
        @Param("account2") String account2
    );

    /**
     * Count transactions by status
     */
    long countByStatus(TransactionStatus status);

    /**
     * Find recent transactions (last N)
     */
    List<Transaction> findTop10ByOrderByTransactionDateDesc();

    /**
     * Check if reference number exists
     */
    boolean existsByReferenceNumber(String referenceNumber);
}
