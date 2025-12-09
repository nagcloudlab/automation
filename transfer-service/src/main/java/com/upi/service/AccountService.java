package com.upi.service;

import java.util.List;

import com.upi.dto.AccountRequest;
import com.upi.dto.AccountResponse;
import com.upi.dto.AccountUpdateRequest;
import com.upi.model.AccountStatus;

/**
 * Service interface for account management operations.
 */
public interface AccountService {

    /**
     * Create a new account
     * 
     * @param request Account creation request
     * @return Created account details
     */
    AccountResponse createAccount(AccountRequest request);

    /**
     * Get account by ID
     * 
     * @param accountId Account ID
     * @return Account details
     */
    AccountResponse getAccount(String accountId);

    /**
     * Get all accounts
     * 
     * @return List of all accounts
     */
    List<AccountResponse> getAllAccounts();

    /**
     * Get accounts by status
     * 
     * @param status Account status filter
     * @return List of accounts with given status
     */
    List<AccountResponse> getAccountsByStatus(AccountStatus status);

    /**
     * Update account details
     * 
     * @param accountId Account ID
     * @param request Update request
     * @return Updated account details
     */
    AccountResponse updateAccount(String accountId, AccountUpdateRequest request);

    /**
     * Delete an account (soft delete by changing status to CLOSED)
     * 
     * @param accountId Account ID
     */
    void deleteAccount(String accountId);

    /**
     * Check if account exists
     * 
     * @param accountId Account ID
     * @return true if account exists
     */
    boolean accountExists(String accountId);
}
