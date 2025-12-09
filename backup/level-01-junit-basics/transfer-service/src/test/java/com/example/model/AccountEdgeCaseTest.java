package com.example.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Edge Case Tests for Account Model - LEVEL 1
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    EDGE CASE TESTING                          ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This test class demonstrates:                                ║
 * ║  • Boundary value testing                                     ║
 * ║  • @RepeatedTest for consistency checks                       ║
 * ║  • Floating point precision handling                          ║
 * ║  • Real-world UPI scenarios                                   ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * BANKING CONTEXT:
 * ================
 * Edge cases in UPI transactions:
 * - Minimum amount: ₹1
 * - Maximum amount: ₹1,00,000
 * - Decimal precision: 2 decimal places
 * - Zero balance handling
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Account Model - Edge Case Tests")
class AccountEdgeCaseTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("ACC001", "Rajesh Kumar", 50000.0);
    }

    // ═══════════════════════════════════════════════════════════
    // BOUNDARY VALUE TESTS
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Minimum transaction amount (₹0.01).
     * 
     * BANKING: Smallest possible paisa transaction
     */
    @Test
    @DisplayName("Should handle minimum amount: ₹0.01")
    void shouldHandleMinimumAmount() {
        double initialBalance = account.getBalance();
        
        account.debit(0.01);
        assertEquals(initialBalance - 0.01, account.getBalance(), 0.001);
        
        account.credit(0.01);
        assertEquals(initialBalance, account.getBalance(), 0.001);
    }

    /**
     * TEST: Transaction with typical UPI amounts.
     * 
     * Common UPI payment amounts
     */
    @Test
    @DisplayName("Should handle typical UPI amounts correctly")
    void shouldHandleTypicalUPIAmounts() {
        Account upiAccount = new Account("UPI001", "Customer", 100000.0);
        
        // Typical UPI amounts
        upiAccount.debit(199.0);    // Auto-rickshaw fare
        upiAccount.debit(500.0);    // Grocery payment
        upiAccount.debit(2000.0);   // Restaurant bill
        upiAccount.debit(99.0);     // Subscription
        
        double expectedBalance = 100000.0 - 199.0 - 500.0 - 2000.0 - 99.0;
        assertEquals(expectedBalance, upiAccount.getBalance(), 0.001);
    }

    /**
     * TEST: Maximum UPI transaction amount.
     * 
     * NPCI Limit: ₹1,00,000 per transaction
     */
    @Test
    @DisplayName("Should handle UPI maximum limit: ₹1,00,000")
    void shouldHandleUPIMaximumLimit() {
        Account largeAccount = new Account("LARGE001", "High Value", 500000.0);
        
        // Maximum UPI transaction
        assertDoesNotThrow(() -> largeAccount.debit(100000.0));
        assertEquals(400000.0, largeAccount.getBalance());
    }

    // ═══════════════════════════════════════════════════════════
    // FLOATING POINT PRECISION TESTS
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Floating point arithmetic precision.
     * 
     * IMPORTANT: Financial calculations must handle floating point properly!
     * 
     * Common issue: 0.1 + 0.2 != 0.3 in floating point
     */
    @Test
    @DisplayName("Should handle floating point precision correctly")
    void shouldHandleFloatingPointPrecision() {
        Account precisionAccount = new Account("PREC001", "Precision Test", 100.0);
        
        // Multiple small transactions
        precisionAccount.debit(33.33);
        precisionAccount.debit(33.33);
        precisionAccount.debit(33.33);
        
        // Should be approximately 0.01 (not exactly due to floating point)
        assertTrue(precisionAccount.getBalance() < 0.02, 
            "Balance should be approximately ₹0.01");
        assertTrue(precisionAccount.getBalance() >= 0.0,
            "Balance should not go negative");
    }

    /**
     * TEST: Paisa-level precision.
     */
    @Test
    @DisplayName("Should maintain paisa-level precision")
    void shouldMaintainPaisaPrecision() {
        Account paisaAccount = new Account("PAISA001", "Paisa Test", 10.00);
        
        paisaAccount.debit(0.01);  // 1 paisa
        paisaAccount.debit(0.02);  // 2 paisa
        paisaAccount.debit(0.05);  // 5 paisa
        paisaAccount.debit(0.10);  // 10 paisa
        paisaAccount.debit(0.50);  // 50 paisa
        
        // 10.00 - 0.01 - 0.02 - 0.05 - 0.10 - 0.50 = 9.32
        assertEquals(9.32, paisaAccount.getBalance(), 0.001);
    }

    // ═══════════════════════════════════════════════════════════
    // ZERO BALANCE SCENARIOS
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Operations on zero balance account.
     */
    @Test
    @DisplayName("Should handle zero balance account correctly")
    void shouldHandleZeroBalanceAccount() {
        Account zeroAccount = new Account("ZERO001", "Zero Balance", 0.0);
        
        // Cannot debit from zero balance
        assertThrows(IllegalArgumentException.class, 
            () -> zeroAccount.debit(1.0));
        
        // Can credit to zero balance
        assertDoesNotThrow(() -> zeroAccount.credit(1000.0));
        assertEquals(1000.0, zeroAccount.getBalance());
    }

    /**
     * TEST: Bring balance to exactly zero.
     */
    @Test
    @DisplayName("Should bring balance to exactly zero")
    void shouldBringBalanceToExactlyZero() {
        Account fullDebitAccount = new Account("FULL001", "Full Debit", 12345.67);
        
        fullDebitAccount.debit(12345.67);
        
        assertEquals(0.0, fullDebitAccount.getBalance(), 
            "Balance should be exactly zero");
    }

    // ═══════════════════════════════════════════════════════════
    // REPEATED TESTS (Consistency Check)
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Repeated operations should be consistent.
     * 
     * @RepeatedTest runs the same test multiple times
     * Useful for detecting race conditions or random failures
     */
    @RepeatedTest(value = 5, name = "Repetition {currentRepetition} of {totalRepetitions}")
    @DisplayName("Repeated debit/credit should be consistent")
    void repeatedOperationsShouldBeConsistent(RepetitionInfo repetitionInfo) {
        Account repeatedAccount = new Account("REP001", "Repeated", 10000.0);
        
        // Same operations each time
        repeatedAccount.debit(1000.0);
        repeatedAccount.credit(500.0);
        repeatedAccount.debit(250.0);
        
        // Should always be the same result
        assertEquals(9250.0, repeatedAccount.getBalance(),
            "Repetition " + repetitionInfo.getCurrentRepetition() + " should have same result");
    }

    // ═══════════════════════════════════════════════════════════
    // STRESS TEST SCENARIOS
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Many small transactions.
     * 
     * SCENARIO: Micro-payments or frequent small transactions
     */
    @Test
    @DisplayName("Should handle many small transactions")
    void shouldHandleManySmallTransactions() {
        Account microAccount = new Account("MICRO001", "Micro Payments", 1000.0);
        
        // 100 small debits of ₹1 each
        for (int i = 0; i < 100; i++) {
            microAccount.debit(1.0);
        }
        
        assertEquals(900.0, microAccount.getBalance(), 
            "After 100 debits of ₹1, balance should be ₹900");
    }

    /**
     * TEST: Alternating debit and credit.
     */
    @Test
    @DisplayName("Should handle alternating debit/credit operations")
    void shouldHandleAlternatingOperations() {
        Account altAccount = new Account("ALT001", "Alternating", 5000.0);
        
        for (int i = 0; i < 50; i++) {
            altAccount.debit(100.0);   // -100
            altAccount.credit(80.0);   // +80
            // Net per iteration: -20
        }
        
        // 50 iterations × -20 = -1000
        // 5000 - 1000 = 4000
        assertEquals(4000.0, altAccount.getBalance());
    }

    // ═══════════════════════════════════════════════════════════
    // REAL-WORLD UPI SCENARIOS
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Typical monthly salary account activity.
     */
    @Test
    @DisplayName("Should simulate monthly salary account activity")
    void shouldSimulateMonthlyActivity() {
        // Monthly salary credited
        Account salaryAccount = new Account("SAL001", "Salaried Employee", 0.0);
        salaryAccount.credit(75000.0);  // Salary credit
        
        // Monthly expenses
        salaryAccount.debit(25000.0);   // Rent
        salaryAccount.debit(8000.0);    // EMI
        salaryAccount.debit(5000.0);    // Utilities
        salaryAccount.debit(10000.0);   // Groceries
        salaryAccount.debit(3000.0);    // Transport
        salaryAccount.debit(5000.0);    // Entertainment
        
        // Savings: 75000 - 25000 - 8000 - 5000 - 10000 - 3000 - 5000 = 19000
        assertEquals(19000.0, salaryAccount.getBalance());
    }

    /**
     * TEST: Split bill scenario.
     * 
     * SCENARIO: Restaurant bill split between friends using UPI
     */
    @Test
    @DisplayName("Should handle split bill scenario")
    void shouldHandleSplitBillScenario() {
        // Four friends, each pays their share
        Account friend1 = new Account("F001", "Friend 1", 5000.0);
        Account friend2 = new Account("F002", "Friend 2", 5000.0);
        Account friend3 = new Account("F003", "Friend 3", 5000.0);
        Account friend4 = new Account("F004", "Friend 4", 5000.0);
        Account restaurant = new Account("REST", "Restaurant", 0.0);
        
        // Total bill: ₹2400, split 4 ways = ₹600 each
        double shareAmount = 600.0;
        
        friend1.debit(shareAmount);
        friend2.debit(shareAmount);
        friend3.debit(shareAmount);
        friend4.debit(shareAmount);
        
        restaurant.credit(shareAmount * 4);
        
        // Verify all balances
        assertAll("Split bill verification",
            () -> assertEquals(4400.0, friend1.getBalance()),
            () -> assertEquals(4400.0, friend2.getBalance()),
            () -> assertEquals(4400.0, friend3.getBalance()),
            () -> assertEquals(4400.0, friend4.getBalance()),
            () -> assertEquals(2400.0, restaurant.getBalance())
        );
    }

    /**
     * TEST: Refund scenario.
     * 
     * SCENARIO: Online purchase cancelled, amount refunded
     */
    @Test
    @DisplayName("Should handle purchase and refund scenario")
    void shouldHandlePurchaseAndRefund() {
        Account customer = new Account("CUST001", "Customer", 10000.0);
        Account merchant = new Account("MERCH001", "Merchant", 50000.0);
        
        // Purchase
        double purchaseAmount = 2500.0;
        customer.debit(purchaseAmount);
        merchant.credit(purchaseAmount);
        
        assertEquals(7500.0, customer.getBalance());
        assertEquals(52500.0, merchant.getBalance());
        
        // Refund (reverse the transaction)
        merchant.debit(purchaseAmount);
        customer.credit(purchaseAmount);
        
        // Back to original
        assertEquals(10000.0, customer.getBalance());
        assertEquals(50000.0, merchant.getBalance());
    }
}
