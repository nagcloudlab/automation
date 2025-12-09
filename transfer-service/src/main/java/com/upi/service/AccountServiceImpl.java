package com.upi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upi.dto.AccountRequest;
import com.upi.dto.AccountResponse;
import com.upi.dto.AccountUpdateRequest;
import com.upi.exception.AccountNotFoundException;
import com.upi.exception.DuplicateAccountException;
import com.upi.model.Account;
import com.upi.model.AccountStatus;
import com.upi.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of AccountService for account management operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        log.info("Creating new account: {}", request.getAccountId());

        // Check for duplicate account ID
        if (accountRepository.existsByAccountId(request.getAccountId())) {
            throw new DuplicateAccountException(request.getAccountId());
        }

        // Check for duplicate email if provided
        if (request.getEmail() != null && accountRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateAccountException("Email already registered: " + request.getEmail(), request.getEmail());
        }

        // Check for duplicate phone if provided
        if (request.getPhone() != null && accountRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateAccountException("Phone number already registered: " + request.getPhone(), request.getPhone());
        }

        // Create account entity
        Account account = Account.builder()
                .accountId(request.getAccountId())
                .accountHolderName(request.getAccountHolderName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .balance(request.getInitialBalance())
                .accountType(request.getAccountType())
                .status(AccountStatus.ACTIVE)
                .build();

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully: {}", savedAccount.getAccountId());

        return AccountResponse.fromEntity(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccount(String accountId) {
        log.debug("Fetching account: {}", accountId);
        
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        
        return AccountResponse.fromEntity(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {
        log.debug("Fetching all accounts");
        
        return accountRepository.findAll().stream()
                .map(AccountResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByStatus(AccountStatus status) {
        log.debug("Fetching accounts by status: {}", status);
        
        return accountRepository.findByStatus(status).stream()
                .map(AccountResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountResponse updateAccount(String accountId, AccountUpdateRequest request) {
        log.info("Updating account: {}", accountId);

        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        // Update only non-null fields
        if (request.getAccountHolderName() != null) {
            account.setAccountHolderName(request.getAccountHolderName());
        }

        if (request.getEmail() != null) {
            // Check if email is already used by another account
            accountRepository.findByEmail(request.getEmail())
                    .filter(a -> !a.getAccountId().equals(accountId))
                    .ifPresent(a -> {
                        throw new DuplicateAccountException("Email already registered: " + request.getEmail(), request.getEmail());
                    });
            account.setEmail(request.getEmail());
        }

        if (request.getPhone() != null) {
            // Check if phone is already used by another account
            accountRepository.findByPhone(request.getPhone())
                    .filter(a -> !a.getAccountId().equals(accountId))
                    .ifPresent(a -> {
                        throw new DuplicateAccountException("Phone already registered: " + request.getPhone(), request.getPhone());
                    });
            account.setPhone(request.getPhone());
        }

        if (request.getStatus() != null) {
            account.setStatus(request.getStatus());
        }

        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully: {}", accountId);

        return AccountResponse.fromEntity(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountId) {
        log.info("Deleting account (soft delete): {}", accountId);

        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        // Soft delete by changing status to CLOSED
        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);

        log.info("Account closed successfully: {}", accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean accountExists(String accountId) {
        return accountRepository.existsByAccountId(accountId);
    }
}
