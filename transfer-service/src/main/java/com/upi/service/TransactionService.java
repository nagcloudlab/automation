package com.upi.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.upi.dto.TransactionResponse;

/**
 * Service interface for transaction history operations.
 */
public interface TransactionService {

    /**
     * Get transaction by reference number
     * 
     * @param referenceNumber Transaction reference number
     * @return Transaction details
     */
    TransactionResponse getTransaction(String referenceNumber);

    /**
     * Get all transactions for an account
     * 
     * @param accountId Account ID
     * @return List of transactions
     */
    List<TransactionResponse> getTransactionsByAccount(String accountId);

    /**
     * Get all transactions for an account with pagination
     * 
     * @param accountId Account ID
     * @param pageable Pagination parameters
     * @return Page of transactions
     */
    Page<TransactionResponse> getTransactionsByAccountPaginated(String accountId, Pageable pageable);

    /**
     * Get sent transactions (debits) for an account
     * 
     * @param accountId Account ID
     * @return List of sent transactions
     */
    List<TransactionResponse> getSentTransactions(String accountId);

    /**
     * Get received transactions (credits) for an account
     * 
     * @param accountId Account ID
     * @return List of received transactions
     */
    List<TransactionResponse> getReceivedTransactions(String accountId);

    /**
     * Get transactions within a date range
     * 
     * @param accountId Account ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of transactions
     */
    List<TransactionResponse> getTransactionsByDateRange(String accountId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get recent transactions
     * 
     * @return List of recent transactions
     */
    List<TransactionResponse> getRecentTransactions();
}
