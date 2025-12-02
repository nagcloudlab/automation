package com.npci.level8;

import com.npci.level8.exceptions.*;

/**
 * Level 8: Exception Handling - Payment Service
 *
 * Demonstrates:
 * - throws declaration
 * - Exception propagation
 * - Checked vs unchecked exceptions
 */
public class PaymentService {

    private boolean serviceAvailable = true;
    private String serviceName;

    public PaymentService(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceAvailable(boolean available) {
        this.serviceAvailable = available;
    }

    // ═══════════════════════════════════════════════════════════════
    // METHOD THAT DECLARES EXCEPTIONS (throws clause)
    // ═══════════════════════════════════════════════════════════════

    /**
     * Process UPI payment
     * Declares all possible exceptions - caller must handle
     */
    public String processUPIPayment(BankAccount fromAccount, String toUPI,
                                    double amount, String pin)
            throws AuthenticationException, AccountInactiveException,
            InsufficientBalanceException, DailyLimitExceededException,
            ServiceUnavailableException {

        // Check service availability
        if (!serviceAvailable) {
            throw new ServiceUnavailableException(serviceName,
                    "Service temporarily down for maintenance", 30, true);
        }

        // Validate UPI ID (unchecked - programming error)
        if (toUPI == null || !toUPI.contains("@")) {
            throw new IllegalArgumentException("Invalid UPI ID format");
        }

        // Validate amount
        if (amount <= 0 || amount > 100000) {
            throw new InvalidAmountException(amount, 1, 100000);
        }

        // Verify PIN
        fromAccount.verifyPin(pin);

        // Check account status
        if (!fromAccount.isActive()) {
            throw new AccountInactiveException(fromAccount.getAccountNumber(), "INACTIVE");
        }

        // Check balance
        if (fromAccount.getAvailableBalance() < amount) {
            throw new InsufficientBalanceException(
                    fromAccount.getAccountNumber(),
                    fromAccount.getBalance(),
                    amount,
                    fromAccount.getMinimumBalance());
        }

        // Check daily limit
        if (fromAccount.getRemainingDailyLimit() < amount) {
            throw new DailyLimitExceededException(
                    fromAccount.getAccountNumber(), "UPI",
                    fromAccount.getDailyWithdrawalLimit(),
                    fromAccount.getDailyWithdrawn(),
                    amount);
        }

        // Process payment
        String txnId = "UPI" + System.currentTimeMillis();
        System.out.println("[UPI] Payment processed: " + txnId);
        System.out.println("  From: " + fromAccount.getAccountNumber());
        System.out.println("  To: " + toUPI);
        System.out.println("  Amount: Rs." + amount);

        return txnId;
    }

    /**
     * Process IMPS payment - wraps exceptions
     */
    public String processIMPSPayment(BankAccount fromAccount, String toAccount,
                                     String toIFSC, double amount, String pin)
            throws TransactionFailedException {

        String txnId = "IMPS" + System.currentTimeMillis();

        try {
            // Check service
            if (!serviceAvailable) {
                throw new ServiceUnavailableException(serviceName);
            }

            // Validate
            if (toAccount == null || toAccount.length() < 9) {
                throw new InvalidAccountException(toAccount, "Invalid account number");
            }

            // Process
            fromAccount.withdraw(amount, pin);

            System.out.println("[IMPS] Payment processed: " + txnId);
            return txnId;

        } catch (ServiceUnavailableException e) {
            throw new TransactionFailedException(txnId, "IMPS", "SERVICE_CHECK", e);
        } catch (InvalidAccountException e) {
            throw new TransactionFailedException(txnId, "IMPS", "VALIDATION", e);
        } catch (BankingException e) {
            throw new TransactionFailedException(txnId, "IMPS", "PROCESSING", e);
        }
    }
}