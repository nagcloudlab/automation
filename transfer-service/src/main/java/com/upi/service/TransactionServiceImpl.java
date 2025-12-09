package com.upi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upi.dto.TransactionResponse;
import com.upi.exception.AccountNotFoundException;
import com.upi.exception.TransactionNotFoundException;
import com.upi.model.Transaction;
import com.upi.repository.AccountRepository;
import com.upi.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of TransactionService for transaction history operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(String referenceNumber) {
        log.debug("Fetching transaction: {}", referenceNumber);
        
        Transaction transaction = transactionRepository.findByReferenceNumber(referenceNumber)
                .orElseThrow(() -> new TransactionNotFoundException(referenceNumber));
        
        return TransactionResponse.fromEntity(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByAccount(String accountId) {
        log.debug("Fetching transactions for account: {}", accountId);
        
        // Verify account exists
        if (!accountRepository.existsByAccountId(accountId)) {
            throw new AccountNotFoundException(accountId);
        }
        
        return transactionRepository.findByAccountId(accountId).stream()
                .map(TransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactionsByAccountPaginated(String accountId, Pageable pageable) {
        log.debug("Fetching paginated transactions for account: {}", accountId);
        
        // Verify account exists
        if (!accountRepository.existsByAccountId(accountId)) {
            throw new AccountNotFoundException(accountId);
        }
        
        return transactionRepository.findByAccountIdPaginated(accountId, pageable)
                .map(TransactionResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getSentTransactions(String accountId) {
        log.debug("Fetching sent transactions for account: {}", accountId);
        
        // Verify account exists
        if (!accountRepository.existsByAccountId(accountId)) {
            throw new AccountNotFoundException(accountId);
        }
        
        return transactionRepository.findByFromAccountAccountIdOrderByTransactionDateDesc(accountId).stream()
                .map(TransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getReceivedTransactions(String accountId) {
        log.debug("Fetching received transactions for account: {}", accountId);
        
        // Verify account exists
        if (!accountRepository.existsByAccountId(accountId)) {
            throw new AccountNotFoundException(accountId);
        }
        
        return transactionRepository.findByToAccountAccountIdOrderByTransactionDateDesc(accountId).stream()
                .map(TransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByDateRange(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching transactions for account {} between {} and {}", accountId, startDate, endDate);
        
        // Verify account exists
        if (!accountRepository.existsByAccountId(accountId)) {
            throw new AccountNotFoundException(accountId);
        }
        
        return transactionRepository.findByAccountIdAndDateRange(accountId, startDate, endDate).stream()
                .map(TransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getRecentTransactions() {
        log.debug("Fetching recent transactions");
        
        return transactionRepository.findTop10ByOrderByTransactionDateDesc().stream()
                .map(TransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
