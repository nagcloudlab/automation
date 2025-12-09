package com.example.service;

import com.example.model.Account;
import com.example.repository.AccountRepository;

/**
 * UPI Transfer Service - Core business logic for fund transfers.
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    REFACTORED FOR TESTABILITY                 ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  BEFORE: new SqlAccountRepository() inside method             ║
 * ║  AFTER:  AccountRepository injected via constructor           ║
 * ║                                                               ║
 * ║  This change enables:                                         ║
 * ║  • Unit testing with mock repositories                        ║
 * ║  • Easy swapping of implementations                           ║
 * ║  • Clear dependency visibility                                ║
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
 * @version 2.0 (Refactored for Dependency Injection)
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
     * 
     * KEY CHANGES:
     * 1. Type is INTERFACE (AccountRepository), not concrete class
     * 2. Field is FINAL - cannot be changed after construction
     * 3. Initialized via CONSTRUCTOR, not inside methods
     */
    private final AccountRepository accountRepository;

    /**
     * Constructor with Dependency Injection.
     * 
     * ┌─────────────────────────────────────────────────────────┐
     * │  THIS IS THE KEY TO TESTABILITY!                        │
     * │                                                         │
     * │  In Production:                                         │
     * │    new UPITransferService(new SqlAccountRepository())   │
     * │                                                         │
     * │  In Tests:                                              │
     * │    new UPITransferService(mockAccountRepository)        │
     * └─────────────────────────────────────────────────────────┘
     * 
     * @param accountRepository The repository for account operations.
     *                          Can be real DB or mock for tests.
     */
    public UPITransferService(AccountRepository accountRepository) {
        // Null check - fail fast if dependency not provided
        if (accountRepository == null) {
            throw new IllegalArgumentException("AccountRepository cannot be null");
        }
        
        this.accountRepository = accountRepository;
        System.out.println("[SERVICE] UPITransferService initialized with " + 
                          accountRepository.getClass().getSimpleName());
    }

    /**
     * Execute UPI fund transfer between two accounts.
     * 
     * @param amount Amount to transfer (₹1 to ₹1,00,000)
     * @param fromAccountId Sender's account ID
     * @param toAccountId Receiver's account ID
     * @throws IllegalArgumentException if validation fails
     * @throws RuntimeException if account not found or insufficient balance
     * 
     * TRANSACTION FLOW:
     * ┌─────────────────────────────────────────────────────────┐
     * │  Step 1: Validate amount (UPI limits)                   │
     * │  Step 2: Validate accounts are different                │
     * │  Step 3: Load sender account from repository            │
     * │  Step 4: Load receiver account from repository          │
     * │  Step 5: Debit sender (may throw if insufficient)       │
     * │  Step 6: Credit receiver                                │
     * │  Step 7: Save sender account                            │
     * │  Step 8: Save receiver account                          │
     * └─────────────────────────────────────────────────────────┘
     */
    public void transfer(double amount, String fromAccountId, String toAccountId) {
        
        printTransferHeader(amount, fromAccountId, toAccountId);

        // ─────────────────────────────────────────────────────────
        // STEP 1: Validate amount against UPI limits
        // ─────────────────────────────────────────────────────────
        validateAmount(amount);

        // ─────────────────────────────────────────────────────────
        // STEP 2: Validate sender and receiver are different
        // ─────────────────────────────────────────────────────────
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        // ─────────────────────────────────────────────────────────
        // STEP 3 & 4: Load accounts using INJECTED repository
        // Note: Using this.accountRepository, NOT new SqlAccountRepository()
        // ─────────────────────────────────────────────────────────
        Account fromAccount = accountRepository.loadAccountById(fromAccountId);
        Account toAccount = accountRepository.loadAccountById(toAccountId);

        System.out.println("[TRANSFER] Sender: " + fromAccount.getAccountHolderName() + 
                          " | Balance: ₹" + fromAccount.getBalance());
        System.out.println("[TRANSFER] Receiver: " + toAccount.getAccountHolderName() + 
                          " | Balance: ₹" + toAccount.getBalance());

        // ─────────────────────────────────────────────────────────
        // STEP 5 & 6: Execute debit and credit
        // ─────────────────────────────────────────────────────────
        fromAccount.debit(amount);   // May throw IllegalArgumentException
        toAccount.credit(amount);

        // ─────────────────────────────────────────────────────────
        // STEP 7 & 8: Persist changes using INJECTED repository
        // ─────────────────────────────────────────────────────────
        accountRepository.saveAccount(fromAccount);
        accountRepository.saveAccount(toAccount);

        printTransferSuccess(fromAccount, toAccount, amount);
    }

    /**
     * Transfer using UPI IDs instead of account IDs.
     * 
     * @param amount Amount to transfer
     * @param fromUpiId Sender's UPI ID (e.g., "rajesh@upi")
     * @param toUpiId Receiver's UPI ID (e.g., "priya@upi")
     */
    public void transferByUpiId(double amount, String fromUpiId, String toUpiId) {
        System.out.println("\n[UPI] Resolving UPI IDs...");
        System.out.println("[UPI] From: " + fromUpiId);
        System.out.println("[UPI] To: " + toUpiId);

        // Resolve UPI IDs to account IDs
        Account fromAccount = accountRepository.findByUpiId(fromUpiId)
            .orElseThrow(() -> new RuntimeException("Sender UPI ID not found: " + fromUpiId));

        Account toAccount = accountRepository.findByUpiId(toUpiId)
            .orElseThrow(() -> new RuntimeException("Receiver UPI ID not found: " + toUpiId));

        // Delegate to main transfer method
        transfer(amount, fromAccount.getAccountId(), toAccount.getAccountId());
    }

    /**
     * Validate transfer amount against UPI limits.
     * 
     * @param amount Amount to validate
     * @throws IllegalArgumentException if amount is outside valid range
     */
    private void validateAmount(double amount) {
        if (amount < UPI_MIN_AMOUNT) {
            throw new IllegalArgumentException(
                String.format("Amount ₹%.2f is below minimum UPI limit of ₹%.2f", 
                    amount, UPI_MIN_AMOUNT)
            );
        }
        if (amount > UPI_MAX_AMOUNT) {
            throw new IllegalArgumentException(
                String.format("Amount ₹%.2f exceeds maximum UPI limit of ₹%.2f", 
                    amount, UPI_MAX_AMOUNT)
            );
        }
        System.out.println("[VALIDATION] ✓ Amount ₹" + amount + " is within UPI limits");
    }

    // ═══════════════════════════════════════════════════════════
    // Helper Methods for Console Output
    // ═══════════════════════════════════════════════════════════

    private void printTransferHeader(double amount, String from, String to) {
        System.out.println("\n" + "═".repeat(55));
        System.out.println("  UPI FUND TRANSFER");
        System.out.println("═".repeat(55));
        System.out.println("[TRANSFER] Amount: ₹" + String.format("%.2f", amount));
        System.out.println("[TRANSFER] From Account: " + from);
        System.out.println("[TRANSFER] To Account: " + to);
        System.out.println("─".repeat(55));
    }

    private void printTransferSuccess(Account from, Account to, double amount) {
        System.out.println("─".repeat(55));
        System.out.println("[TRANSFER] ✅ TRANSFER SUCCESSFUL");
        System.out.println("[TRANSFER] " + from.getAccountHolderName() + 
                          " → " + to.getAccountHolderName() + ": ₹" + amount);
        System.out.println("[TRANSFER] Sender New Balance: ₹" + from.getBalance());
        System.out.println("[TRANSFER] Receiver New Balance: ₹" + to.getBalance());
        System.out.println("═".repeat(55) + "\n");
    }
}
