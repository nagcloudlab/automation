package com.example.model;

import com.example.exception.InsufficientBalanceException;
import com.example.exception.InvalidAmountException;
import com.example.exception.TransferException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Validation Tests for Account Model - LEVEL 1
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    EXCEPTION TESTING                          ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This test class demonstrates:                                ║
 * ║  • assertThrows for exception verification                    ║
 * ║  • Testing exception messages                                 ║
 * ║  • @Nested classes for test organization                      ║
 * ║  • Negative test scenarios                                    ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * BANKING CONTEXT:
 * ================
 * Payment systems MUST validate all operations:
 * - Prevent negative amounts (fraud prevention)
 * - Prevent overdrafts (insufficient balance)
 * - Validate transaction limits
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Account Model - Validation Tests")
class AccountValidationTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("ACC001", "Rajesh Kumar", 10000.0);
    }

    // ═══════════════════════════════════════════════════════════
    // NESTED CLASS: Debit Validation Tests
    // ═══════════════════════════════════════════════════════════
    
    /**
     * @Nested allows grouping related tests together.
     * Creates a hierarchical test structure in reports.
     */
    @Nested
    @DisplayName("Debit Validation")
    class DebitValidationTests {

        /**
         * TEST: Insufficient balance should throw exception.
         * 
         * DEMONSTRATES:
         * - assertThrows to catch expected exception
         * - Capturing exception to verify message
         * 
         * SCENARIO: Customer tries to pay ₹50,000 with only ₹10,000 balance
         */
        @Test
        @DisplayName("Should throw exception when debiting more than balance")
        void shouldThrowExceptionForInsufficientBalance() {
            // Arrange
            double excessiveAmount = 50000.0; // More than balance

            // Act & Assert
            InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> account.debit(excessiveAmount),
                "Should throw InsufficientBalanceException for insufficient balance"
            );

            // Verify exception details
            assertEquals(10000.0, exception.getAvailableBalance());
            assertEquals(50000.0, exception.getRequestedAmount());
            assertEquals("U30", exception.getErrorCode());
        }

        /**
         * TEST: Negative amount should be rejected.
         * 
         * SCENARIO: Attempt to debit negative amount (potential fraud)
         */
        @Test
        @DisplayName("Should throw exception when debiting negative amount")
        void shouldThrowExceptionForNegativeDebit() {
            // Arrange
            double negativeAmount = -500.0;

            // Act & Assert
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> account.debit(negativeAmount)
            );

            // Verify
            assertTrue(exception.isNegative());
            assertEquals("U09", exception.getErrorCode());
        }

        /**
         * TEST: Zero amount should be rejected.
         * 
         * SCENARIO: Accidental zero amount transaction
         */
        @Test
        @DisplayName("Should throw exception when debiting zero amount")
        void shouldThrowExceptionForZeroDebit() {
            // Act & Assert
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> account.debit(0.0),
                "Zero debit should not be allowed"
            );
            
            assertTrue(exception.isZero());
        }

        /**
         * TEST: Very small negative amount should be rejected.
         * 
         * EDGE CASE: -0.01 should still be rejected
         */
        @Test
        @DisplayName("Should throw exception for very small negative amount (-₹0.01)")
        void shouldThrowExceptionForSmallNegativeDebit() {
            assertThrows(
                InvalidAmountException.class,
                () -> account.debit(-0.01)
            );
        }

        /**
         * TEST: Debit exactly ₹0.01 more than balance should fail.
         * 
         * EDGE CASE: Balance boundary check
         */
        @Test
        @DisplayName("Should throw exception when debiting ₹0.01 more than balance")
        void shouldThrowExceptionWhenDebitingSlightlyMoreThanBalance() {
            // Arrange
            double slightlyMore = account.getBalance() + 0.01;

            // Act & Assert
            assertThrows(
                InsufficientBalanceException.class,
                () -> account.debit(slightlyMore)
            );
        }

        /**
         * TEST: Balance should remain unchanged after failed debit.
         * 
         * IMPORTANT: Failed operations should not alter state!
         */
        @Test
        @DisplayName("Balance should remain unchanged after failed debit attempt")
        void balanceShouldRemainUnchangedAfterFailedDebit() {
            // Arrange
            double originalBalance = account.getBalance();

            // Act - Try to debit excessive amount
            try {
                account.debit(99999.0);
            } catch (InsufficientBalanceException e) {
                // Expected exception
            }

            // Assert - Balance unchanged
            assertEquals(originalBalance, account.getBalance(),
                "Balance should not change after failed debit");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // NESTED CLASS: Credit Validation Tests
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Credit Validation")
    class CreditValidationTests {

        /**
         * TEST: Negative credit amount should be rejected.
         * 
         * SCENARIO: Prevent invalid credit operations
         */
        @Test
        @DisplayName("Should throw exception when crediting negative amount")
        void shouldThrowExceptionForNegativeCredit() {
            // Arrange
            double negativeAmount = -1000.0;

            // Act & Assert
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> account.credit(negativeAmount)
            );

            assertTrue(exception.isNegative());
            assertEquals("U09", exception.getErrorCode());
        }

        /**
         * TEST: Zero credit amount should be rejected.
         */
        @Test
        @DisplayName("Should throw exception when crediting zero amount")
        void shouldThrowExceptionForZeroCredit() {
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> account.credit(0.0)
            );
            
            assertTrue(exception.isZero());
        }

        /**
         * TEST: Balance should remain unchanged after failed credit.
         */
        @Test
        @DisplayName("Balance should remain unchanged after failed credit attempt")
        void balanceShouldRemainUnchangedAfterFailedCredit() {
            // Arrange
            double originalBalance = account.getBalance();

            // Act
            try {
                account.credit(-500.0);
            } catch (InvalidAmountException e) {
                // Expected
            }

            // Assert
            assertEquals(originalBalance, account.getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // NESTED CLASS: Valid Operations (Positive Tests)
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Valid Operations - No Exceptions")
    class ValidOperationTests {

        /**
         * TEST: Minimum valid debit (₹0.01) should succeed.
         */
        @Test
        @DisplayName("Should allow minimum debit of ₹0.01")
        void shouldAllowMinimumDebit() {
            // Arrange
            double minimumAmount = 0.01;
            double expectedBalance = account.getBalance() - minimumAmount;

            // Act & Assert - No exception
            assertDoesNotThrow(() -> account.debit(minimumAmount));
            assertEquals(expectedBalance, account.getBalance(), 0.001);
        }

        /**
         * TEST: Minimum valid credit (₹0.01) should succeed.
         */
        @Test
        @DisplayName("Should allow minimum credit of ₹0.01")
        void shouldAllowMinimumCredit() {
            // Arrange
            double minimumAmount = 0.01;
            double expectedBalance = account.getBalance() + minimumAmount;

            // Act & Assert
            assertDoesNotThrow(() -> account.credit(minimumAmount));
            assertEquals(expectedBalance, account.getBalance(), 0.001);
        }

        /**
         * TEST: Large valid debit (within balance) should succeed.
         */
        @Test
        @DisplayName("Should allow large debit within balance")
        void shouldAllowLargeDebitWithinBalance() {
            // Arrange
            double largeAmount = 9999.99;

            // Act & Assert
            assertDoesNotThrow(() -> account.debit(largeAmount));
            assertTrue(account.getBalance() >= 0);
        }

        /**
         * TEST: Large credit should succeed (no upper limit).
         */
        @Test
        @DisplayName("Should allow large credit amount")
        void shouldAllowLargeCredit() {
            // Arrange
            double largeAmount = 999999.99;
            double expectedBalance = account.getBalance() + largeAmount;

            // Act & Assert
            assertDoesNotThrow(() -> account.credit(largeAmount));
            assertEquals(expectedBalance, account.getBalance(), 0.001);
        }

        /**
         * TEST: Debit exact balance should leave zero (not negative).
         */
        @Test
        @DisplayName("Should leave exactly zero after debiting full balance")
        void shouldLeaveZeroAfterFullDebit() {
            // Arrange
            double fullBalance = account.getBalance();

            // Act
            account.debit(fullBalance);

            // Assert
            assertEquals(0.0, account.getBalance(), 
                "Balance should be exactly zero, not negative");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ADDITIONAL: Exception Type Verification
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Verify correct exception type is thrown.
     * 
     * DEMONSTRATES: assertThrows returns the exception for inspection
     */
    @Test
    @DisplayName("Should throw InvalidAmountException (not other types)")
    void shouldThrowCorrectExceptionType() {
        // Should throw exactly InvalidAmountException for negative amount
        Exception exception = assertThrows(
            InvalidAmountException.class,  // Exact type expected
            () -> account.debit(-100.0)
        );

        // Verify it's exactly InvalidAmountException
        assertEquals(InvalidAmountException.class, exception.getClass());
        
        // Should also be a TransferException
        assertInstanceOf(TransferException.class, exception);
    }

    /**
     * TEST: Multiple failed operations should all throw exceptions.
     */
    @Test
    @DisplayName("Multiple invalid operations should all throw exceptions")
    void multipleInvalidOperationsShouldAllThrow() {
        assertAll("All invalid operations should throw",
            () -> assertThrows(InvalidAmountException.class, () -> account.debit(-1)),
            () -> assertThrows(InvalidAmountException.class, () -> account.debit(0)),
            () -> assertThrows(InsufficientBalanceException.class, () -> account.debit(99999)),
            () -> assertThrows(InvalidAmountException.class, () -> account.credit(-1)),
            () -> assertThrows(InvalidAmountException.class, () -> account.credit(0))
        );
    }
}
