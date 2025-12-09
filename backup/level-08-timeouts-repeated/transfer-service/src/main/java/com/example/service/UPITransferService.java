package com.example.service;

import com.example.model.Account;
import com.example.repository.AccountRepository;
import com.example.exception.*;

/**
 * UPI Transfer Service - Core business logic for fund transfers.
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    REFACTORED FOR TESTABILITY                 ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  BEFORE: new SqlAccountRepository() inside method             ║
 * ║  AFTER:  AccountRepository injected via constructor           ║
 * ║                                                               ║
 * ║  Level 4 Updates:                                             ║
 * ║  • Uses custom exceptions                                     ║
 * ║  • Better structured for Mockito testing                      ║
 * ║  • Returns TransferResult for verification                    ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * BANKING CONTEXT:
 * ================
 * This service mimics NPCI's UPI transaction processing:
 * 1. Validate transaction parameters
 * 2. Load payer/payee accounts
 * 3. Execute debit/credit operations
 * 4. Persist updated balances
 * 
 * @author NPCI Training Team
 * @version 3.0 (Level 4 - Custom Exceptions & Mockito Ready)
 */
public class UPITransferService {

    // ═══════════════════════════════════════════════════════════
    // UPI Transaction Limits (as per NPCI/RBI guidelines)
    // ═══════════════════════════════════════════════════════════
    
    /** Minimum UPI transaction amount: ₹1 */
    public static final double UPI_MIN_AMOUNT = 1.0;
    
    /** Maximum UPI transaction amount: ₹1,00,000 */
    public static final double UPI_MAX_AMOUNT = 100000.0;

    // ═══════════════════════════════════════════════════════════
    // DEPENDENCY INJECTION
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Repository dependency - injected via constructor.
     */
    private final AccountRepository accountRepository;

    /**
     * Constructor with Dependency Injection.
     * 
     * @param accountRepository The repository for account operations.
     */
    public UPITransferService(AccountRepository accountRepository) {
        if (accountRepository == null) {
            throw new IllegalArgumentException("AccountRepository cannot be null");
        }
        this.accountRepository = accountRepository;
    }

    /**
     * Execute UPI fund transfer between two accounts.
     * 
     * @param fromAccountId Sender's account ID
     * @param toAccountId Receiver's account ID
     * @param amount Amount to transfer (₹1 to ₹1,00,000)
     * @return TransferResult containing transfer details
     * @throws AccountNotFoundException if either account doesn't exist
     * @throws InsufficientBalanceException if sender has insufficient funds
     * @throws InvalidAmountException if amount is negative or zero
     * @throws TransactionLimitExceededException if amount outside UPI limits
     * @throws SameAccountTransferException if sender and receiver are same
     */
    public TransferResult transfer(String fromAccountId, String toAccountId, double amount) {
        
        // ─────────────────────────────────────────────────────────
        // STEP 1: Validate amount
        // ─────────────────────────────────────────────────────────
        validateAmount(amount);

        // ─────────────────────────────────────────────────────────
        // STEP 2: Validate sender and receiver are different
        // ─────────────────────────────────────────────────────────
        if (fromAccountId.equals(toAccountId)) {
            throw new SameAccountTransferException(fromAccountId);
        }

        // ─────────────────────────────────────────────────────────
        // STEP 3 & 4: Load accounts
        // ─────────────────────────────────────────────────────────
        Account fromAccount = accountRepository.loadAccountById(fromAccountId);
        if (fromAccount == null) {
            throw new AccountNotFoundException(fromAccountId);
        }
        
        Account toAccount = accountRepository.loadAccountById(toAccountId);
        if (toAccount == null) {
            throw new AccountNotFoundException(toAccountId);
        }

        // ─────────────────────────────────────────────────────────
        // STEP 5 & 6: Execute debit and credit
        // Account.debit() throws InsufficientBalanceException
        // ─────────────────────────────────────────────────────────
        double senderBalanceBefore = fromAccount.getBalance();
        double receiverBalanceBefore = toAccount.getBalance();
        
        fromAccount.debit(amount);
        toAccount.credit(amount);

        // ─────────────────────────────────────────────────────────
        // STEP 7 & 8: Persist changes
        // ─────────────────────────────────────────────────────────
        accountRepository.saveAccount(fromAccount);
        accountRepository.saveAccount(toAccount);

        // Return result for verification
        return new TransferResult(
            fromAccountId,
            toAccountId,
            amount,
            senderBalanceBefore,
            fromAccount.getBalance(),
            receiverBalanceBefore,
            toAccount.getBalance()
        );
    }

    /**
     * Transfer using UPI IDs instead of account IDs.
     * 
     * @param fromUpiId Sender's UPI ID
     * @param toUpiId Receiver's UPI ID
     * @param amount Amount to transfer
     * @return TransferResult
     */
    public TransferResult transferByUpiId(String fromUpiId, String toUpiId, double amount) {
        
        Account fromAccount = accountRepository.findByUpiId(fromUpiId)
            .orElseThrow(() -> new AccountNotFoundException(fromUpiId, true));

        Account toAccount = accountRepository.findByUpiId(toUpiId)
            .orElseThrow(() -> new AccountNotFoundException(toUpiId, true));

        return transfer(fromAccount.getAccountId(), toAccount.getAccountId(), amount);
    }

    /**
     * Validate transfer amount against UPI limits.
     */
    private void validateAmount(double amount) {
        if (amount < 0) {
            throw InvalidAmountException.negative(amount);
        }
        if (amount == 0) {
            throw InvalidAmountException.zero();
        }
        if (amount < UPI_MIN_AMOUNT) {
            throw TransactionLimitExceededException.belowMinimum(amount);
        }
        if (amount > UPI_MAX_AMOUNT) {
            throw TransactionLimitExceededException.maxLimitExceeded(amount);
        }
    }

    /**
     * Check if account exists.
     */
    public boolean accountExists(String accountId) {
        return accountRepository.existsById(accountId);
    }

    /**
     * Get account balance.
     */
    public double getBalance(String accountId) {
        Account account = accountRepository.loadAccountById(accountId);
        if (account == null) {
            throw new AccountNotFoundException(accountId);
        }
        return account.getBalance();
    }

    // ═══════════════════════════════════════════════════════════
    // TRANSFER RESULT - Immutable result object
    // ═══════════════════════════════════════════════════════════

    /**
     * Immutable result object for transfer operations.
     * Useful for testing and logging.
     */
    public static class TransferResult {
        private final String senderId;
        private final String receiverId;
        private final double amount;
        private final double senderBalanceBefore;
        private final double senderBalanceAfter;
        private final double receiverBalanceBefore;
        private final double receiverBalanceAfter;

        public TransferResult(String senderId, String receiverId, double amount,
                             double senderBalanceBefore, double senderBalanceAfter,
                             double receiverBalanceBefore, double receiverBalanceAfter) {
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.amount = amount;
            this.senderBalanceBefore = senderBalanceBefore;
            this.senderBalanceAfter = senderBalanceAfter;
            this.receiverBalanceBefore = receiverBalanceBefore;
            this.receiverBalanceAfter = receiverBalanceAfter;
        }

        public String getSenderId() { return senderId; }
        public String getReceiverId() { return receiverId; }
        public double getAmount() { return amount; }
        public double getSenderBalanceBefore() { return senderBalanceBefore; }
        public double getSenderBalanceAfter() { return senderBalanceAfter; }
        public double getReceiverBalanceBefore() { return receiverBalanceBefore; }
        public double getReceiverBalanceAfter() { return receiverBalanceAfter; }

        @Override
        public String toString() {
            return String.format("Transfer: %s → %s, Amount: ₹%.2f, " +
                "Sender: ₹%.2f → ₹%.2f, Receiver: ₹%.2f → ₹%.2f",
                senderId, receiverId, amount,
                senderBalanceBefore, senderBalanceAfter,
                receiverBalanceBefore, receiverBalanceAfter);
        }
    }
}
