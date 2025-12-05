package com.example.service;

import com.example.model.Account;
import com.example.repository.AccountRepository;

/**
 * UPI Transfer Service - Core business logic for fund transfers.
 *
 * REFACTORED: Now uses Dependency Injection
 * - Repository is injected via constructor
 * - Can be tested with mock repository
 * - Follows Single Responsibility Principle
 *
 * Banking Context: This service handles P2P UPI transfers
 * similar to how NPCI processes inter-bank transactions.
 */
public class UPITransferService {

    // UPI Transaction Limits (as per NPCI guidelines)
    public static final double UPI_MIN_AMOUNT = 1.0;
    public static final double UPI_MAX_AMOUNT = 100000.0;  // ₹1 Lakh per transaction

    // ✅ Dependency is now a field, injected via constructor
    private final AccountRepository accountRepository;

    /**
     * Constructor Injection - The key to testability!
     *
     * @param accountRepository The repository to use for account operations
     *                          <p>
     *                          Why Constructor Injection?
     *                          1. Dependencies are explicit and visible
     *                          2. Object is fully initialized after construction
     *                          3. Easy to inject mocks for testing
     *                          4. Immutable dependency (final field)
     */
    public UPITransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        System.out.println("[SERVICE] UPITransferService initialized");
    }

    /**
     * Execute UPI fund transfer between two accounts.
     *
     * @param amount        Amount to transfer in INR
     * @param fromAccountId Sender's account ID
     * @param toAccountId   Receiver's account ID
     * @throws IllegalArgumentException if validation fails
     * @throws RuntimeException         if transfer fails
     *                                  <p>
     *                                  Transaction Flow:
     *                                  1. Validate amount (UPI limits)
     *                                  2. Load sender account
     *                                  3. Load receiver account
     *                                  4. Debit sender
     *                                  5. Credit receiver
     *                                  6. Save both accounts
     */
    public void transfer(double amount, String fromAccountId, String toAccountId) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("[TRANSFER] Initiating UPI Transfer");
        System.out.println("[TRANSFER] Amount: ₹" + amount);
        System.out.println("[TRANSFER] From: " + fromAccountId + " → To: " + toAccountId);
        System.out.println("=".repeat(50));

        // Step 1: Validate transaction amount
        validateAmount(amount);

        // Step 2: Validate accounts are different
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }

        // Step 3: Load accounts (using injected repository)
        Account fromAccount = accountRepository.loadAccountById(fromAccountId);
        Account toAccount = accountRepository.loadAccountById(toAccountId);

        System.out.println("[TRANSFER] Sender Balance: ₹" + fromAccount.getBalance());
        System.out.println("[TRANSFER] Receiver Balance: ₹" + toAccount.getBalance());

        // Step 4: Execute transfer (debit first, then credit)
        fromAccount.debit(amount);   // May throw if insufficient balance
        toAccount.credit(amount);

        // Step 5: Persist changes
        accountRepository.saveAccount(fromAccount);
        accountRepository.saveAccount(toAccount);

        System.out.println("[TRANSFER] ✅ Transfer Successful!");
        System.out.println("[TRANSFER] New Sender Balance: ₹" + fromAccount.getBalance());
        System.out.println("[TRANSFER] New Receiver Balance: ₹" + toAccount.getBalance());
        System.out.println("=".repeat(50) + "\n");
    }

    /**
     * Validate transfer amount against UPI limits.
     *
     * @param amount Amount to validate
     * @throws IllegalArgumentException if amount is invalid
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
    }

    /**
     * Transfer using UPI IDs instead of account IDs.
     *
     * @param amount    Amount to transfer
     * @param fromUpiId Sender's UPI ID (e.g., "rajesh@upi")
     * @param toUpiId   Receiver's UPI ID (e.g., "priya@upi")
     */
    public void transferByUpiId(double amount, String fromUpiId, String toUpiId) {
        System.out.println("[TRANSFER] Resolving UPI IDs...");

        Account fromAccount = accountRepository.findByUpiId(fromUpiId)
                .orElseThrow(() -> new RuntimeException("Sender UPI ID not found: " + fromUpiId));

        Account toAccount = accountRepository.findByUpiId(toUpiId)
                .orElseThrow(() -> new RuntimeException("Receiver UPI ID not found: " + toUpiId));

        transfer(amount, fromAccount.getAccountId(), toAccount.getAccountId());
    }
}