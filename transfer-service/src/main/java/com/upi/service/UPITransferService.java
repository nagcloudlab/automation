package com.upi.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.upi.dto.TransferRequest;
import com.upi.dto.TransferResponse;
import com.upi.exception.AccountNotActiveException;
import com.upi.exception.AccountNotFoundException;
import com.upi.exception.InsufficientFundsException;
import com.upi.exception.InvalidTransferException;
import com.upi.model.Account;
import com.upi.model.Transaction;
import com.upi.model.TransactionStatus;
import com.upi.model.TransactionType;
import com.upi.repository.AccountRepository;
import com.upi.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of TransferService for UPI-style fund transfers.
 * Handles fund transfer operations with full transaction management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UPITransferService implements TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransferResponse transferFunds(TransferRequest request) {
        log.info("Initiating fund transfer: From={}, To={}, Amount={}",
                request.getFromAccId(), request.getToAccId(), request.getAmount());

        // Validate request
        validateTransferRequest(request);

        // Fetch accounts
        Account fromAccount = accountRepository.findByAccountId(request.getFromAccId())
                .orElseThrow(() -> new AccountNotFoundException(request.getFromAccId()));

        Account toAccount = accountRepository.findByAccountId(request.getToAccId())
                .orElseThrow(() -> new AccountNotFoundException(request.getToAccId()));

        // Validate accounts
        validateAccounts(fromAccount, toAccount, request.getAmount());

        // Create transaction record
        Transaction transaction = createTransaction(fromAccount, toAccount, request);

        try {
            // Record balances before transfer
            BigDecimal fromBalanceBefore = fromAccount.getBalance();
            BigDecimal toBalanceBefore = toAccount.getBalance();

            // Perform transfer
            fromAccount.debit(request.getAmount());
            toAccount.credit(request.getAmount());

            // Save updated accounts
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            // Update transaction with balance info and mark as completed
            transaction.setFromBalanceBefore(fromBalanceBefore);
            transaction.setFromBalanceAfter(fromAccount.getBalance());
            transaction.setToBalanceBefore(toBalanceBefore);
            transaction.setToBalanceAfter(toAccount.getBalance());
            transaction.markCompleted();

            transactionRepository.save(transaction);

            log.info("Transfer completed successfully: Reference={}", transaction.getReferenceNumber());

            return buildSuccessResponse(transaction, fromAccount, toAccount);

        } catch (Exception e) {
            log.error("Transfer failed: {}", e.getMessage());
            transaction.markFailed(e.getMessage());
            transactionRepository.save(transaction);
            throw e;
        }
    }

    /**
     * Validate transfer request parameters
     */
    private void validateTransferRequest(TransferRequest request) {
        if (request.getFromAccId().equals(request.getToAccId())) {
            throw new InvalidTransferException("Source and destination accounts cannot be the same");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException("Transfer amount must be greater than zero");
        }
    }

    /**
     * Validate account states for transfer
     */
    private void validateAccounts(Account fromAccount, Account toAccount, BigDecimal amount) {
        // Check if source account is active
        if (!fromAccount.isTransactionAllowed()) {
            throw new AccountNotActiveException(fromAccount.getAccountId(), fromAccount.getStatus());
        }

        // Check if destination account is active
        if (!toAccount.isTransactionAllowed()) {
            throw new AccountNotActiveException(toAccount.getAccountId(), toAccount.getStatus());
        }

        // Check sufficient balance
        if (!fromAccount.hasSufficientBalance(amount)) {
            throw new InsufficientFundsException(
                    fromAccount.getAccountId(),
                    fromAccount.getBalance(),
                    amount
            );
        }
    }

    /**
     * Create a new transaction record
     */
    private Transaction createTransaction(Account fromAccount, Account toAccount, TransferRequest request) {
        return Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(request.getAmount())
                .transactionType(TransactionType.FUND_TRANSFER)
                .status(TransactionStatus.PENDING)
                .description(request.getDescription() != null ? 
                        request.getDescription() : "Fund transfer from " + fromAccount.getAccountId())
                .build();
    }

    /**
     * Build success response
     */
    private TransferResponse buildSuccessResponse(Transaction transaction, Account fromAccount, Account toAccount) {
        return TransferResponse.builder()
                .transactionId(transaction.getTransactionId().toString())
                .referenceNumber(transaction.getReferenceNumber())
                .status("SUCCESS")
                .message("Transfer completed successfully")
                .fromAccountId(fromAccount.getAccountId())
                .toAccountId(toAccount.getAccountId())
                .amount(transaction.getAmount())
                .fromAccountBalance(fromAccount.getBalance())
                .toAccountBalance(toAccount.getBalance())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}
