package com.npci.level8;

import com.npci.level8.exceptions.*;

/**
 * Level 8: Exception Handling - Bank Account with Exception Handling
 *
 * Demonstrates:
 * - Throwing custom exceptions
 * - Checked vs Unchecked exceptions
 * - Exception declaration with throws
 * - Validation with exceptions
 */
public class BankAccount {

    private String accountNumber;
    private String holderName;
    private double balance;
    private String accountType;
    private boolean isActive;
    private double minimumBalance;
    private double dailyWithdrawalLimit;
    private double dailyWithdrawn;
    private String pin;
    private int failedPinAttempts;
    private static final int MAX_PIN_ATTEMPTS = 3;

    // ═══════════════════════════════════════════════════════════════
    // CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    public BankAccount(String accountNumber, String holderName, double initialBalance,
                       String accountType, String pin) {
        // Validate inputs using unchecked exceptions (programming errors)
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        if (holderName == null || holderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Holder name cannot be null or empty");
        }
        if (initialBalance < 0) {
            throw new InvalidAmountException(initialBalance, "Initial balance cannot be negative");
        }
        if (pin == null || pin.length() != 4) {
            throw new IllegalArgumentException("PIN must be 4 digits");
        }

        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.pin = pin;
        this.isActive = true;
        this.minimumBalance = accountType.equals("Savings") ? 1000 : 5000;
        this.dailyWithdrawalLimit = 100000;
        this.dailyWithdrawn = 0;
        this.failedPinAttempts = 0;
    }

    // ═══════════════════════════════════════════════════════════════
    // GETTERS
    // ═══════════════════════════════════════════════════════════════

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public boolean isActive() { return isActive; }
    public double getMinimumBalance() { return minimumBalance; }
    public double getDailyWithdrawalLimit() { return dailyWithdrawalLimit; }
    public double getDailyWithdrawn() { return dailyWithdrawn; }
    public double getRemainingDailyLimit() { return dailyWithdrawalLimit - dailyWithdrawn; }
    public double getAvailableBalance() { return Math.max(0, balance - minimumBalance); }

    // ═══════════════════════════════════════════════════════════════
    // METHODS WITH EXCEPTION HANDLING
    // ═══════════════════════════════════════════════════════════════

    /**
     * Verify PIN
     * Throws AuthenticationException if PIN is wrong
     */
    public void verifyPin(String enteredPin) throws AuthenticationException,AccountInactiveException {
        if (!isActive) {
            throw new AccountInactiveException(accountNumber, "BLOCKED",
                    "Too many failed PIN attempts");
        }

        if (!pin.equals(enteredPin)) {
            failedPinAttempts++;
            if (failedPinAttempts >= MAX_PIN_ATTEMPTS) {
                isActive = false;
            }
            throw new AuthenticationException(accountNumber, "PIN",
                    failedPinAttempts, MAX_PIN_ATTEMPTS);
        }

        // Reset on successful verification
        failedPinAttempts = 0;
    }

    /**
     * Deposit money
     * Throws AccountInactiveException if account is inactive
     * Throws InvalidAmountException if amount is invalid (unchecked)
     */
    public void deposit(double amount) throws AccountInactiveException {
        // Check account status (checked exception)
        if (!isActive) {
            throw new AccountInactiveException(accountNumber, "INACTIVE",
                    "Cannot deposit to inactive account");
        }

        // Validate amount (unchecked exception - programming error)
        if (amount <= 0) {
            throw new InvalidAmountException(amount, "Deposit amount must be positive");
        }
        if (amount > 1000000) {
            throw new InvalidAmountException(amount, 1, 1000000);
        }

        balance += amount;
        System.out.println("[Deposit] Rs." + amount + " deposited to " + accountNumber);
        System.out.println("  New Balance: Rs." + balance);
    }

    /**
     * Withdraw money
     * Throws multiple checked exceptions
     */
    public void withdraw(double amount, String pin)
            throws AuthenticationException, AccountInactiveException,
            InsufficientBalanceException, DailyLimitExceededException {

        // 1. Verify PIN (may throw AuthenticationException)
        verifyPin(pin);

        // 2. Check account status (may throw AccountInactiveException)
        if (!isActive) {
            throw new AccountInactiveException(accountNumber, "INACTIVE");
        }

        // 3. Validate amount (unchecked - programming error)
        if (amount <= 0) {
            throw new InvalidAmountException(amount, "Withdrawal amount must be positive");
        }

        // 4. Check daily limit (may throw DailyLimitExceededException)
        if (amount > dailyWithdrawalLimit) {
            throw new DailyLimitExceededException(accountNumber, "WITHDRAWAL",
                    dailyWithdrawalLimit, 0, amount);
        }
        if (dailyWithdrawn + amount > dailyWithdrawalLimit) {
            throw new DailyLimitExceededException(accountNumber, "WITHDRAWAL",
                    dailyWithdrawalLimit, dailyWithdrawn, amount);
        }

        // 5. Check balance (may throw InsufficientBalanceException)
        if (balance - amount < minimumBalance) {
            throw new InsufficientBalanceException(accountNumber, balance,
                    amount, minimumBalance);
        }

        // All validations passed - perform withdrawal
        balance -= amount;
        dailyWithdrawn += amount;

        System.out.println("[Withdraw] Rs." + amount + " withdrawn from " + accountNumber);
        System.out.println("  New Balance: Rs." + balance);
        System.out.println("  Daily Used: Rs." + dailyWithdrawn + " / Rs." + dailyWithdrawalLimit);
    }

    /**
     * Transfer money to another account
     * Demonstrates exception chaining and transaction handling
     */
    public String transfer(BankAccount toAccount, double amount, String pin)
            throws AuthenticationException, AccountInactiveException,
            InsufficientBalanceException, DailyLimitExceededException,
            InvalidAccountException, TransactionFailedException {

        String txnId = "TXN" + System.currentTimeMillis();

        try {
            // 1. Validate destination account
            if (toAccount == null) {
                throw new InvalidAccountException("NULL", "Destination account is null");
            }
            if (!toAccount.isActive()) {
                throw new AccountInactiveException(toAccount.getAccountNumber(),
                        "INACTIVE", "Destination account is inactive");
            }
            if (toAccount.getAccountNumber().equals(this.accountNumber)) {
                throw new InvalidAccountException(toAccount.getAccountNumber(),
                        "Cannot transfer to same account");
            }

            // 2. Withdraw from source (may throw various exceptions)
            withdraw(amount, pin);

            // 3. Deposit to destination
            try {
                toAccount.deposit(amount);
            } catch (AccountInactiveException e) {
                // Reversal needed! Deposit failed after withdrawal
                balance += amount;  // Reverse the withdrawal
                dailyWithdrawn -= amount;

                TransactionFailedException txnFailed = new TransactionFailedException(
                        txnId, "TRANSFER", this.accountNumber, toAccount.getAccountNumber(),
                        amount, "CREDIT", "Destination account became inactive");
                txnFailed.setReversalRequired(true);
                txnFailed.setReversalCompleted(true);
                throw txnFailed;
            }

            System.out.println("[Transfer] Rs." + amount + " transferred");
            System.out.println("  From: " + accountNumber + " → To: " + toAccount.getAccountNumber());
            System.out.println("  Transaction ID: " + txnId);

            return txnId;

        } catch (BankingException e) {
            // Re-throw banking exceptions as-is
            throw e;
        } catch (Exception e) {
            // Wrap unexpected exceptions
            throw new TransactionFailedException(txnId, "TRANSFER", "UNEXPECTED", e);
        }
    }

    /**
     * Close account
     */
    public double closeAccount(String pin)
            throws AuthenticationException, AccountInactiveException {
        verifyPin(pin);

        if (!isActive) {
            throw new AccountInactiveException(accountNumber, "ALREADY_CLOSED");
        }

        double closingBalance = balance;
        balance = 0;
        isActive = false;

        System.out.println("[Close] Account " + accountNumber + " closed");
        System.out.println("  Closing Balance Returned: Rs." + closingBalance);

        return closingBalance;
    }

    /**
     * Reset daily limit (for testing)
     */
    public void resetDailyLimit() {
        dailyWithdrawn = 0;
    }

    /**
     * Reactivate account (admin function)
     */
    public void reactivate() {
        isActive = true;
        failedPinAttempts = 0;
        System.out.println("[Reactivate] Account " + accountNumber + " reactivated");
    }

    /**
     * Display account info
     */
    public void displayInfo() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│         ACCOUNT INFORMATION             │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("  Account Number : " + accountNumber);
        System.out.println("  Holder Name    : " + holderName);
        System.out.println("  Account Type   : " + accountType);
        System.out.println("  Balance        : Rs." + balance);
        System.out.println("  Min Balance    : Rs." + minimumBalance);
        System.out.println("  Available      : Rs." + getAvailableBalance());
        System.out.println("  Status         : " + (isActive ? "Active" : "Inactive"));
        System.out.println("  Daily Limit    : Rs." + dailyWithdrawalLimit);
        System.out.println("  Daily Used     : Rs." + dailyWithdrawn);
        System.out.println("  Daily Remaining: Rs." + getRemainingDailyLimit());
        System.out.println("└─────────────────────────────────────────┘");
    }
}