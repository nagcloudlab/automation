package com.example.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Custom Exception Classes - LEVEL 2
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    EXCEPTION CLASS TESTS                      ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This test class verifies:                                    ║
 * ║  • Exception creation with correct data                       ║
 * ║  • Error codes are set correctly                              ║
 * ║  • Exception messages are descriptive                         ║
 * ║  • Helper methods work correctly                              ║
 * ║  • Exception hierarchy is correct                             ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Custom Exception Tests")
class CustomExceptionTest {

    // ═══════════════════════════════════════════════════════════
    // AccountNotFoundException Tests
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("AccountNotFoundException")
    class AccountNotFoundExceptionTests {

        @Test
        @DisplayName("Should create exception with account ID")
        void shouldCreateWithAccountId() {
            // Arrange & Act
            AccountNotFoundException exception = new AccountNotFoundException("ACC001");

            // Assert
            assertEquals("ACC001", exception.getAccountId());
            assertEquals("U30", exception.getErrorCode());
            assertFalse(exception.isUpiIdLookup());
            assertTrue(exception.getMessage().contains("ACC001"));
            assertTrue(exception.getMessage().contains("not found"));
        }

        @Test
        @DisplayName("Should create exception for UPI ID lookup")
        void shouldCreateForUpiIdLookup() {
            // Arrange & Act
            AccountNotFoundException exception = new AccountNotFoundException("rajesh@upi", true);

            // Assert
            assertEquals("rajesh@upi", exception.getAccountId());
            assertTrue(exception.isUpiIdLookup());
            assertTrue(exception.getMessage().contains("UPI ID"));
        }

        @Test
        @DisplayName("Should include transaction reference when provided")
        void shouldIncludeTransactionRef() {
            // Arrange & Act
            AccountNotFoundException exception = new AccountNotFoundException("ACC001", "TXN123456");

            // Assert
            assertEquals("TXN123456", exception.getTransactionRef());
        }

        @Test
        @DisplayName("Should have timestamp when created")
        void shouldHaveTimestamp() {
            // Arrange & Act
            AccountNotFoundException exception = new AccountNotFoundException("ACC001");

            // Assert
            assertNotNull(exception.getTimestamp());
        }

        @Test
        @DisplayName("Should extend TransferException")
        void shouldExtendTransferException() {
            // Arrange & Act
            AccountNotFoundException exception = new AccountNotFoundException("ACC001");

            // Assert
            assertInstanceOf(TransferException.class, exception);
            assertInstanceOf(RuntimeException.class, exception);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // InsufficientBalanceException Tests
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("InsufficientBalanceException")
    class InsufficientBalanceExceptionTests {

        @Test
        @DisplayName("Should create exception with balance details")
        void shouldCreateWithBalanceDetails() {
            // Arrange & Act
            InsufficientBalanceException exception = 
                new InsufficientBalanceException(1000.0, 5000.0);

            // Assert
            assertEquals(1000.0, exception.getAvailableBalance());
            assertEquals(5000.0, exception.getRequestedAmount());
            assertEquals(4000.0, exception.getShortfall());
            assertEquals("U30", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should create exception with account ID")
        void shouldCreateWithAccountId() {
            // Arrange & Act
            InsufficientBalanceException exception = 
                new InsufficientBalanceException("ACC001", 1000.0, 5000.0);

            // Assert
            assertEquals("ACC001", exception.getAccountId());
            assertTrue(exception.getMessage().contains("ACC001"));
        }

        @Test
        @DisplayName("Should calculate shortfall correctly")
        void shouldCalculateShortfall() {
            // Test various scenarios
            assertAll("Shortfall calculations",
                () -> assertEquals(4000.0, 
                    new InsufficientBalanceException(1000.0, 5000.0).getShortfall()),
                () -> assertEquals(100.0, 
                    new InsufficientBalanceException(0.0, 100.0).getShortfall()),
                () -> assertEquals(99999.0, 
                    new InsufficientBalanceException(1.0, 100000.0).getShortfall())
            );
        }

        @Test
        @DisplayName("Should detect zero balance")
        void shouldDetectZeroBalance() {
            // Arrange & Act
            InsufficientBalanceException zeroBalance = 
                new InsufficientBalanceException(0.0, 100.0);
            InsufficientBalanceException nonZeroBalance = 
                new InsufficientBalanceException(50.0, 100.0);

            // Assert
            assertTrue(zeroBalance.isZeroBalance());
            assertFalse(nonZeroBalance.isZeroBalance());
        }

        @Test
        @DisplayName("Should include amounts in message")
        void shouldIncludeAmountsInMessage() {
            // Arrange & Act
            InsufficientBalanceException exception = 
                new InsufficientBalanceException(1000.0, 5000.0);

            // Assert
            String message = exception.getMessage();
            assertTrue(message.contains("1000") || message.contains("1,000"));
            assertTrue(message.contains("5000") || message.contains("5,000"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // InvalidAmountException Tests
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("InvalidAmountException")
    class InvalidAmountExceptionTests {

        @Test
        @DisplayName("Should create exception for negative amount")
        void shouldCreateForNegativeAmount() {
            // Arrange & Act
            InvalidAmountException exception = InvalidAmountException.negative(-100.0);

            // Assert
            assertEquals(-100.0, exception.getAmount());
            assertTrue(exception.isNegative());
            assertFalse(exception.isZero());
            assertEquals(InvalidAmountException.AmountValidationType.NEGATIVE, 
                exception.getValidationType());
        }

        @Test
        @DisplayName("Should create exception for zero amount")
        void shouldCreateForZeroAmount() {
            // Arrange & Act
            InvalidAmountException exception = InvalidAmountException.zero();

            // Assert
            assertEquals(0.0, exception.getAmount());
            assertTrue(exception.isZero());
            assertFalse(exception.isNegative());
            assertEquals(InvalidAmountException.AmountValidationType.ZERO, 
                exception.getValidationType());
        }

        @Test
        @DisplayName("Should have correct error code U09")
        void shouldHaveCorrectErrorCode() {
            // Arrange & Act
            InvalidAmountException exception = new InvalidAmountException(-100.0);

            // Assert
            assertEquals("U09", exception.getErrorCode());
            assertEquals(InvalidAmountException.ERROR_CODE, exception.getErrorCode());
        }

        @Test
        @DisplayName("Should support different validation types")
        void shouldSupportDifferentValidationTypes() {
            // Test all validation types
            for (InvalidAmountException.AmountValidationType type : 
                    InvalidAmountException.AmountValidationType.values()) {
                
                InvalidAmountException exception = new InvalidAmountException(100.0, type);
                
                assertEquals(type, exception.getValidationType());
                assertNotNull(type.getDescription());
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // TransactionLimitExceededException Tests
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("TransactionLimitExceededException")
    class TransactionLimitExceededExceptionTests {

        @Test
        @DisplayName("Should create exception for max limit exceeded")
        void shouldCreateForMaxLimitExceeded() {
            // Arrange & Act
            TransactionLimitExceededException exception = 
                TransactionLimitExceededException.maxLimitExceeded(150000.0);

            // Assert
            assertEquals(150000.0, exception.getRequestedAmount());
            assertEquals(100000.0, exception.getLimit());
            assertEquals(50000.0, exception.getExcessAmount());
            assertTrue(exception.isMaxLimitExceeded());
            assertFalse(exception.isBelowMinimum());
        }

        @Test
        @DisplayName("Should create exception for below minimum")
        void shouldCreateForBelowMinimum() {
            // Arrange & Act
            TransactionLimitExceededException exception = 
                TransactionLimitExceededException.belowMinimum(0.5);

            // Assert
            assertEquals(0.5, exception.getRequestedAmount());
            assertEquals(1.0, exception.getLimit());
            assertEquals(0.5, exception.getExcessAmount());  // How much more needed
            assertTrue(exception.isBelowMinimum());
            assertFalse(exception.isMaxLimitExceeded());
        }

        @Test
        @DisplayName("Should have correct UPI limits defined")
        void shouldHaveCorrectUPILimits() {
            // Assert static limit values
            assertEquals(1.0, TransactionLimitExceededException.UPI_MIN_LIMIT);
            assertEquals(100000.0, TransactionLimitExceededException.UPI_MAX_LIMIT);
        }

        @Test
        @DisplayName("Should support different limit types")
        void shouldSupportDifferentLimitTypes() {
            // Test all limit types
            for (TransactionLimitExceededException.LimitType type : 
                    TransactionLimitExceededException.LimitType.values()) {
                
                TransactionLimitExceededException exception = 
                    new TransactionLimitExceededException(100.0, 50.0, type);
                
                assertEquals(type, exception.getLimitType());
                assertNotNull(type.getDescription());
            }
        }

        @Test
        @DisplayName("Should have error code U09")
        void shouldHaveCorrectErrorCode() {
            TransactionLimitExceededException exception = 
                TransactionLimitExceededException.maxLimitExceeded(150000.0);
            
            assertEquals("U09", exception.getErrorCode());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // SameAccountTransferException Tests
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("SameAccountTransferException")
    class SameAccountTransferExceptionTests {

        @Test
        @DisplayName("Should create exception with account ID")
        void shouldCreateWithAccountId() {
            // Arrange & Act
            SameAccountTransferException exception = 
                new SameAccountTransferException("ACC001");

            // Assert
            assertEquals("ACC001", exception.getAccountId());
            assertEquals("U16", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("same account"));
        }

        @Test
        @DisplayName("Should create exception for UPI self-transfer")
        void shouldCreateForUpiSelfTransfer() {
            // Arrange & Act
            SameAccountTransferException exception = 
                new SameAccountTransferException("rajesh@upi", "ACC001");

            // Assert
            assertEquals("ACC001", exception.getAccountId());
            assertTrue(exception.getMessage().contains("UPI"));
        }

        @Test
        @DisplayName("Should extend TransferException")
        void shouldExtendTransferException() {
            SameAccountTransferException exception = 
                new SameAccountTransferException("ACC001");
            
            assertInstanceOf(TransferException.class, exception);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // Exception Hierarchy Tests
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Exception Hierarchy")
    class ExceptionHierarchyTests {

        @Test
        @DisplayName("All exceptions should extend TransferException")
        void allExceptionsShouldExtendTransferException() {
            assertAll("Exception hierarchy",
                () -> assertInstanceOf(TransferException.class, 
                    new AccountNotFoundException("ACC001")),
                () -> assertInstanceOf(TransferException.class, 
                    new InsufficientBalanceException(100.0, 200.0)),
                () -> assertInstanceOf(TransferException.class, 
                    new InvalidAmountException(-100.0)),
                () -> assertInstanceOf(TransferException.class, 
                    TransactionLimitExceededException.maxLimitExceeded(150000.0)),
                () -> assertInstanceOf(TransferException.class, 
                    new SameAccountTransferException("ACC001"))
            );
        }

        @Test
        @DisplayName("All exceptions should extend RuntimeException")
        void allExceptionsShouldExtendRuntimeException() {
            assertAll("RuntimeException hierarchy",
                () -> assertInstanceOf(RuntimeException.class, 
                    new AccountNotFoundException("ACC001")),
                () -> assertInstanceOf(RuntimeException.class, 
                    new InsufficientBalanceException(100.0, 200.0)),
                () -> assertInstanceOf(RuntimeException.class, 
                    new InvalidAmountException(-100.0)),
                () -> assertInstanceOf(RuntimeException.class, 
                    TransactionLimitExceededException.maxLimitExceeded(150000.0)),
                () -> assertInstanceOf(RuntimeException.class, 
                    new SameAccountTransferException("ACC001"))
            );
        }

        @Test
        @DisplayName("Can catch all exceptions as TransferException")
        void canCatchAllAsTransferException() {
            // Simulate catching different exception types
            int caughtCount = 0;

            try {
                throw new AccountNotFoundException("ACC001");
            } catch (TransferException e) {
                caughtCount++;
            }

            try {
                throw new InsufficientBalanceException(100.0, 200.0);
            } catch (TransferException e) {
                caughtCount++;
            }

            try {
                throw InvalidAmountException.negative(-100.0);
            } catch (TransferException e) {
                caughtCount++;
            }

            assertEquals(3, caughtCount, "Should catch all as TransferException");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // Formatted Error Tests
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Formatted Error Output")
    class FormattedErrorTests {

        @Test
        @DisplayName("Should generate formatted error string")
        void shouldGenerateFormattedError() {
            // Arrange
            AccountNotFoundException exception = 
                new AccountNotFoundException("ACC001", "TXN123456");

            // Act
            String formatted = exception.getFormattedError();

            // Assert
            assertTrue(formatted.contains("U30"), "Should contain error code");
            assertTrue(formatted.contains("ACC001"), "Should contain account ID");
            assertTrue(formatted.contains("TXN123456"), "Should contain transaction ref");
        }

        @Test
        @DisplayName("Should have meaningful toString")
        void shouldHaveMeaningfulToString() {
            // Arrange
            InsufficientBalanceException exception = 
                new InsufficientBalanceException(1000.0, 5000.0);

            // Act
            String toString = exception.toString();

            // Assert
            assertTrue(toString.contains("InsufficientBalanceException"));
            assertTrue(toString.contains("U30"));
        }
    }
}
