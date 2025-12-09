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
 * Exception Tests for Account Model - LEVEL 2
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    ACCOUNT EXCEPTION TESTS                    ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This test class demonstrates:                                ║
 * ║  • Testing custom banking exceptions                          ║
 * ║  • Verifying exception details (codes, amounts)               ║
 * ║  • @Nested organization by operation type                     ║
 * ║  • State verification after failed operations                 ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 2.0 (Custom Exceptions)
 */
@DisplayName("Account - Exception Handling Tests")
class AccountExceptionTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("ACC001", "Rajesh Kumar", 10000.0);
    }

    // ═══════════════════════════════════════════════════════════
    // DEBIT EXCEPTION TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Debit Operation Exceptions")
    class DebitExceptionTests {

        // ─────────────────────────────────────────────────────────
        // Insufficient Balance Tests
        // ─────────────────────────────────────────────────────────

        @Test
        @DisplayName("Should throw InsufficientBalanceException when debiting more than balance")
        void shouldThrowInsufficientBalanceException() {
            // Arrange
            double excessAmount = 50000.0;

            // Act & Assert
            InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> account.debit(excessAmount)
            );

            // Verify exception details
            assertEquals("U30", exception.getErrorCode());
            assertEquals(10000.0, exception.getAvailableBalance());
            assertEquals(50000.0, exception.getRequestedAmount());
        }

        @Test
        @DisplayName("Should include correct shortfall in exception")
        void shouldIncludeCorrectShortfall() {
            // Arrange
            double requestedAmount = 15000.0;
            double expectedShortfall = 5000.0; // 15000 - 10000

            // Act
            InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> account.debit(requestedAmount)
            );

            // Assert
            assertEquals(expectedShortfall, exception.getShortfall());
        }

        @Test
        @DisplayName("Should throw for debit exceeding balance by ₹0.01")
        void shouldThrowForSlightlyExceedingBalance() {
            // Arrange
            double slightlyMore = account.getBalance() + 0.01;

            // Act & Assert
            InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> account.debit(slightlyMore)
            );

            // Verify tiny shortfall
            assertEquals(0.01, exception.getShortfall(), 0.001);
        }

        @Test
        @DisplayName("Should throw for debit from zero balance account")
        void shouldThrowForDebitFromZeroBalance() {
            // Arrange
            Account emptyAccount = new Account("EMPTY001", "Empty Account", 0.0);

            // Act
            InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> emptyAccount.debit(1.0)
            );

            // Assert
            assertTrue(exception.isZeroBalance());
            assertEquals(0.0, exception.getAvailableBalance());
        }

        // ─────────────────────────────────────────────────────────
        // Invalid Amount Tests (Negative/Zero)
        // ─────────────────────────────────────────────────────────

        @Test
        @DisplayName("Should throw InvalidAmountException for negative debit")
        void shouldThrowInvalidAmountForNegativeDebit() {
            // Arrange
            double negativeAmount = -500.0;

            // Act & Assert
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> account.debit(negativeAmount)
            );

            // Verify exception details
            assertEquals("U09", exception.getErrorCode());
            assertEquals(negativeAmount, exception.getAmount());
            assertTrue(exception.isNegative());
        }

        @Test
        @DisplayName("Should throw InvalidAmountException for zero debit")
        void shouldThrowInvalidAmountForZeroDebit() {
            // Act & Assert
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> account.debit(0.0)
            );

            // Verify exception details
            assertEquals(0.0, exception.getAmount());
            assertTrue(exception.isZero());
        }

        @Test
        @DisplayName("Should throw InvalidAmountException for very small negative (-0.01)")
        void shouldThrowForVerySmallNegative() {
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> account.debit(-0.01)
            );

            assertTrue(exception.isNegative());
        }

        // ─────────────────────────────────────────────────────────
        // State Preservation Tests
        // ─────────────────────────────────────────────────────────

        @Test
        @DisplayName("Balance should remain unchanged after InsufficientBalanceException")
        void balanceUnchangedAfterInsufficientBalance() {
            // Arrange
            double originalBalance = account.getBalance();

            // Act
            assertThrows(InsufficientBalanceException.class,
                () -> account.debit(99999.0));

            // Assert
            assertEquals(originalBalance, account.getBalance(),
                "Balance must not change after failed debit");
        }

        @Test
        @DisplayName("Balance should remain unchanged after InvalidAmountException")
        void balanceUnchangedAfterInvalidAmount() {
            // Arrange
            double originalBalance = account.getBalance();

            // Act
            assertThrows(InvalidAmountException.class,
                () -> account.debit(-100.0));

            // Assert
            assertEquals(originalBalance, account.getBalance(),
                "Balance must not change after invalid amount");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // CREDIT EXCEPTION TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Credit Operation Exceptions")
    class CreditExceptionTests {

        @Test
        @DisplayName("Should throw InvalidAmountException for negative credit")
        void shouldThrowForNegativeCredit() {
            // Act & Assert
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> account.credit(-1000.0)
            );

            // Verify
            assertTrue(exception.isNegative());
            assertEquals("U09", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should throw InvalidAmountException for zero credit")
        void shouldThrowForZeroCredit() {
            // Act & Assert
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> account.credit(0.0)
            );

            // Verify
            assertTrue(exception.isZero());
        }

        @Test
        @DisplayName("Balance should remain unchanged after failed credit")
        void balanceUnchangedAfterFailedCredit() {
            // Arrange
            double originalBalance = account.getBalance();

            // Act
            assertThrows(InvalidAmountException.class,
                () -> account.credit(-500.0));

            // Assert
            assertEquals(originalBalance, account.getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // EXCEPTION HIERARCHY TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Exception Hierarchy Verification")
    class ExceptionHierarchyTests {

        @Test
        @DisplayName("InsufficientBalanceException should extend TransferException")
        void insufficientBalanceShouldExtendTransferException() {
            // Act
            Exception exception = assertThrows(
                InsufficientBalanceException.class,
                () -> account.debit(99999.0)
            );

            // Assert
            assertInstanceOf(TransferException.class, exception);
            assertInstanceOf(RuntimeException.class, exception);
        }

        @Test
        @DisplayName("InvalidAmountException should extend TransferException")
        void invalidAmountShouldExtendTransferException() {
            // Act
            Exception exception = assertThrows(
                InvalidAmountException.class,
                () -> account.debit(-100.0)
            );

            // Assert
            assertInstanceOf(TransferException.class, exception);
        }

        @Test
        @DisplayName("Should be able to catch as TransferException")
        void shouldCatchAsTransferException() {
            // Both exception types should be catchable as TransferException
            assertThrows(TransferException.class, () -> account.debit(-100.0));
            assertThrows(TransferException.class, () -> account.debit(99999.0));
        }

        @Test
        @DisplayName("Should be able to catch as RuntimeException")
        void shouldCatchAsRuntimeException() {
            // Both should be unchecked exceptions
            assertThrows(RuntimeException.class, () -> account.debit(-100.0));
            assertThrows(RuntimeException.class, () -> account.debit(99999.0));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // EXCEPTION MESSAGE TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Exception Message Verification")
    class ExceptionMessageTests {

        @Test
        @DisplayName("InsufficientBalanceException should have descriptive message")
        void insufficientBalanceShouldHaveDescriptiveMessage() {
            // Act
            InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> account.debit(50000.0)
            );

            // Assert - message should contain key information
            String message = exception.getMessage();
            assertAll("Message content",
                () -> assertTrue(message.contains("Insufficient") || 
                                message.toLowerCase().contains("balance")),
                () -> assertTrue(message.contains("10000") || message.contains("10,000")),
                () -> assertTrue(message.contains("50000") || message.contains("50,000"))
            );
        }

        @Test
        @DisplayName("InvalidAmountException should have descriptive message")
        void invalidAmountShouldHaveDescriptiveMessage() {
            // Act
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> account.debit(-500.0)
            );

            // Assert
            String message = exception.getMessage();
            assertTrue(message.toLowerCase().contains("negative") || 
                      message.toLowerCase().contains("invalid") ||
                      message.contains("-500"));
        }

        @Test
        @DisplayName("Exception should include account ID when available")
        void exceptionShouldIncludeAccountId() {
            // Act
            InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> account.debit(99999.0)
            );

            // Assert
            assertEquals("ACC001", exception.getAccountId());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // NPCI ERROR CODE TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("NPCI Error Code Verification")
    class NPCIErrorCodeTests {

        @Test
        @DisplayName("InsufficientBalanceException should have error code U30")
        void insufficientBalanceShouldHaveU30() {
            InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> account.debit(99999.0)
            );

            assertEquals("U30", exception.getErrorCode());
            assertEquals(InsufficientBalanceException.ERROR_CODE, exception.getErrorCode());
        }

        @Test
        @DisplayName("InvalidAmountException should have error code U09")
        void invalidAmountShouldHaveU09() {
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> account.debit(-100.0)
            );

            assertEquals("U09", exception.getErrorCode());
            assertEquals(InvalidAmountException.ERROR_CODE, exception.getErrorCode());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // EDGE CASE EXCEPTION TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Edge Case Exceptions")
    class EdgeCaseExceptionTests {

        @Test
        @DisplayName("Should NOT throw for valid minimum debit (₹0.01)")
        void shouldNotThrowForMinimumValidDebit() {
            assertDoesNotThrow(() -> account.debit(0.01));
        }

        @Test
        @DisplayName("Should NOT throw for exact balance debit")
        void shouldNotThrowForExactBalanceDebit() {
            assertDoesNotThrow(() -> account.debit(10000.0));
            assertEquals(0.0, account.getBalance());
        }

        @Test
        @DisplayName("Should throw correct exception for negative vs insufficient")
        void shouldDistinguishNegativeFromInsufficient() {
            // Negative should throw InvalidAmountException
            assertThrows(InvalidAmountException.class, () -> account.debit(-100.0));

            // Insufficient should throw InsufficientBalanceException
            assertThrows(InsufficientBalanceException.class, () -> account.debit(99999.0));
        }

        @Test
        @DisplayName("Multiple failed operations should each throw correct exception")
        void multipleFailedOperationsShouldEachThrow() {
            double originalBalance = account.getBalance();

            // All should fail, balance should remain unchanged
            assertAll("Multiple failures",
                () -> assertThrows(InvalidAmountException.class, () -> account.debit(-1)),
                () -> assertThrows(InvalidAmountException.class, () -> account.debit(0)),
                () -> assertThrows(InsufficientBalanceException.class, () -> account.debit(99999)),
                () -> assertThrows(InvalidAmountException.class, () -> account.credit(-1)),
                () -> assertThrows(InvalidAmountException.class, () -> account.credit(0))
            );

            // Balance should be unchanged after all failures
            assertEquals(originalBalance, account.getBalance());
        }
    }
}
