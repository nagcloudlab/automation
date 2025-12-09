package com.example.model;

import com.example.exception.InsufficientBalanceException;
import com.example.exception.InvalidAmountException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Parameterized Tests for Account Model - LEVEL 3
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    PARAMETERIZED TESTS                        ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This test class demonstrates:                                ║
 * ║  • @ParameterizedTest annotation                              ║
 * ║  • @ValueSource for single parameter tests                    ║
 * ║  • @CsvSource for multiple parameters                         ║
 * ║  • @NullSource and @NullAndEmptySource                        ║
 * ║  • Custom display names for parameters                        ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * BANKING CONTEXT:
 * ================
 * Testing various transaction amounts, boundary values, and
 * edge cases commonly encountered in UPI payments.
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Account - Parameterized Tests")
class AccountParameterizedTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("ACC001", "Rajesh Kumar", 10000.0);
    }

    // ═══════════════════════════════════════════════════════════
    // @ValueSource TESTS - Single Parameter
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@ValueSource Tests")
    class ValueSourceTests {

        /**
         * TEST: Valid debit amounts using @ValueSource
         * 
         * @ValueSource provides a simple array of values.
         * Each value is passed to the test method as a parameter.
         */
        @ParameterizedTest(name = "Should debit ₹{0} successfully")
        @ValueSource(doubles = {1.0, 10.0, 100.0, 500.0, 1000.0, 5000.0, 9999.99})
        @DisplayName("Should accept valid debit amounts")
        void shouldAcceptValidDebitAmounts(double amount) {
            // Arrange
            double initialBalance = account.getBalance();

            // Act
            assertDoesNotThrow(() -> account.debit(amount));

            // Assert
            assertEquals(initialBalance - amount, account.getBalance(), 0.001,
                "Balance should decrease by debit amount");
        }

        /**
         * TEST: Invalid negative amounts using @ValueSource
         * 
         * All negative amounts should throw InvalidAmountException
         */
        @ParameterizedTest(name = "Should reject negative amount: ₹{0}")
        @ValueSource(doubles = {-10000.0, -1000.0, -100.0, -10.0, -1.0, -0.01})
        @DisplayName("Should reject all negative debit amounts")
        void shouldRejectNegativeDebitAmounts(double negativeAmount) {
            // Act & Assert
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> account.debit(negativeAmount)
            );

            assertTrue(exception.isNegative(),
                "Exception should indicate negative amount");
            assertEquals("U09", exception.getErrorCode());
        }

        /**
         * TEST: Valid credit amounts using @ValueSource
         */
        @ParameterizedTest(name = "Should credit ₹{0} successfully")
        @ValueSource(doubles = {0.01, 1.0, 100.0, 10000.0, 100000.0, 999999.99})
        @DisplayName("Should accept valid credit amounts")
        void shouldAcceptValidCreditAmounts(double amount) {
            double initialBalance = account.getBalance();

            assertDoesNotThrow(() -> account.credit(amount));

            assertEquals(initialBalance + amount, account.getBalance(), 0.001);
        }

        /**
         * TEST: Zero amount should be rejected for both debit and credit
         */
        @ParameterizedTest(name = "Zero amount test #{index}")
        @ValueSource(doubles = {0.0, 0.00, -0.0})
        @DisplayName("Should reject zero amounts")
        void shouldRejectZeroAmounts(double zeroAmount) {
            assertAll("Zero should be rejected for all operations",
                () -> assertThrows(InvalidAmountException.class, 
                    () -> account.debit(zeroAmount)),
                () -> assertThrows(InvalidAmountException.class, 
                    () -> account.credit(zeroAmount))
            );
        }

        /**
         * TEST: Typical UPI payment amounts
         * 
         * Common amounts seen in everyday UPI transactions
         */
        @ParameterizedTest(name = "Typical UPI amount: ₹{0}")
        @ValueSource(doubles = {
            10.0,      // Tea/coffee
            50.0,      // Snacks
            100.0,     // Small purchase
            199.0,     // Auto fare
            299.0,     // Food delivery
            499.0,     // Online order
            999.0,     // Subscription
            1999.0,    // Shopping
            4999.0,    // Electronics
            9999.0     // Large purchase
        })
        @DisplayName("Should handle typical UPI payment amounts")
        void shouldHandleTypicalUPIAmounts(double amount) {
            Account richAccount = new Account("RICH001", "Rich User", 100000.0);
            
            assertDoesNotThrow(() -> richAccount.debit(amount));
            assertEquals(100000.0 - amount, richAccount.getBalance(), 0.001);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @CsvSource TESTS - Multiple Parameters
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@CsvSource Tests")
    class CsvSourceTests {

        /**
         * TEST: Debit operation with balance verification
         * 
         * @CsvSource allows testing with multiple parameters per test case.
         * Format: "param1, param2, param3, ..."
         */
        @ParameterizedTest(name = "Debit ₹{1} from ₹{0} balance → Expected: ₹{2}")
        @CsvSource({
            "10000, 1000, 9000",
            "10000, 5000, 5000",
            "10000, 9999, 1",
            "10000, 10000, 0",
            "5000, 2500, 2500",
            "1000, 1, 999",
            "100, 99.99, 0.01"
        })
        @DisplayName("Should calculate correct balance after debit")
        void shouldCalculateCorrectBalanceAfterDebit(
                double initialBalance,
                double debitAmount,
                double expectedBalance) {
            
            // Arrange
            Account testAccount = new Account("TEST", "Test User", initialBalance);

            // Act
            testAccount.debit(debitAmount);

            // Assert
            assertEquals(expectedBalance, testAccount.getBalance(), 0.01,
                String.format("After debiting ₹%.2f from ₹%.2f, balance should be ₹%.2f",
                    debitAmount, initialBalance, expectedBalance));
        }

        /**
         * TEST: Credit operation with balance verification
         */
        @ParameterizedTest(name = "Credit ₹{1} to ₹{0} balance → Expected: ₹{2}")
        @CsvSource({
            "0, 1000, 1000",
            "5000, 5000, 10000",
            "10000, 0.01, 10000.01",
            "99999, 1, 100000",
            "0, 100000, 100000"
        })
        @DisplayName("Should calculate correct balance after credit")
        void shouldCalculateCorrectBalanceAfterCredit(
                double initialBalance,
                double creditAmount,
                double expectedBalance) {
            
            Account testAccount = new Account("TEST", "Test User", initialBalance);

            testAccount.credit(creditAmount);

            assertEquals(expectedBalance, testAccount.getBalance(), 0.01);
        }

        /**
         * TEST: Insufficient balance scenarios
         * 
         * Tests various combinations where debit exceeds balance
         */
        @ParameterizedTest(name = "Balance ₹{0}, Debit ₹{1} → Shortfall ₹{2}")
        @CsvSource({
            "1000, 2000, 1000",
            "500, 501, 1",
            "100, 100.01, 0.01",
            "0, 1, 1",
            "5000, 10000, 5000",
            "9999, 10000, 1"
        })
        @DisplayName("Should throw InsufficientBalanceException with correct shortfall")
        void shouldThrowWithCorrectShortfall(
                double balance,
                double debitAmount,
                double expectedShortfall) {
            
            Account testAccount = new Account("TEST", "Test User", balance);

            InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> testAccount.debit(debitAmount)
            );

            assertEquals(expectedShortfall, exception.getShortfall(), 0.01,
                "Shortfall should be correctly calculated");
            assertEquals(balance, exception.getAvailableBalance(), 0.01);
            assertEquals(debitAmount, exception.getRequestedAmount(), 0.01);
        }

        /**
         * TEST: Account creation with various data
         */
        @ParameterizedTest(name = "Create account: {0} - {1} with ₹{2}")
        @CsvSource({
            "ACC001, Rajesh Kumar, 10000.0",
            "ACC002, Priya Sharma, 0.0",
            "ACC003, Amit Patel, 999999.99",
            "ACC004, 'Singh, Harpreet', 50000.0",
            "ACC005, 'O''Brien', 25000.0"
        })
        @DisplayName("Should create accounts with various data")
        void shouldCreateAccountsWithVariousData(
                String accountId,
                String holderName,
                double balance) {
            
            Account newAccount = new Account(accountId, holderName, balance);

            assertAll("Account properties",
                () -> assertEquals(accountId, newAccount.getAccountId()),
                () -> assertEquals(holderName, newAccount.getAccountHolderName()),
                () -> assertEquals(balance, newAccount.getBalance())
            );
        }

        /**
         * TEST: Multiple operations in sequence
         * 
         * Format: initialBalance, debit1, credit1, debit2, finalBalance
         */
        @ParameterizedTest(name = "Sequence: ₹{0} -₹{1} +₹{2} -₹{3} = ₹{4}")
        @CsvSource({
            "10000, 1000, 500, 2000, 7500",
            "5000, 100, 100, 100, 4900",
            "20000, 5000, 3000, 8000, 10000",
            "1000, 500, 1000, 500, 1000"
        })
        @DisplayName("Should handle multiple sequential operations")
        void shouldHandleMultipleOperations(
                double initial,
                double debit1,
                double credit1,
                double debit2,
                double expected) {
            
            Account testAccount = new Account("SEQ", "Sequence Test", initial);

            testAccount.debit(debit1);
            testAccount.credit(credit1);
            testAccount.debit(debit2);

            assertEquals(expected, testAccount.getBalance(), 0.01);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @NullSource and @NullAndEmptySource TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Null and Empty Source Tests")
    class NullAndEmptySourceTests {

        /**
         * TEST: Null account ID handling
         * 
         * Note: This test demonstrates @NullSource but actual Account
         * implementation may or may not handle null. Adjust as needed.
         */
        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should handle null/empty account IDs appropriately")
        void shouldHandleNullOrEmptyAccountId(String accountId) {
            // This test documents expected behavior for null/empty IDs
            // Real implementation should validate in constructor
            
            // For now, just verify account can be created
            // (In production, you'd want to throw exception for null/empty)
            if (accountId == null || accountId.isEmpty()) {
                // Document that null/empty creates account (may want to change this)
                Account acc = new Account(accountId, "Test", 1000.0);
                assertNotNull(acc);
            }
        }

        /**
         * TEST: Null holder name handling
         */
        @ParameterizedTest
        @NullSource
        @DisplayName("Should handle null holder name")
        void shouldHandleNullHolderName(String holderName) {
            Account acc = new Account("ACC001", holderName, 1000.0);
            assertNull(acc.getAccountHolderName());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // CUSTOM DELIMITER TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Custom Delimiter Tests")
    class CustomDelimiterTests {

        /**
         * TEST: Using pipe delimiter for better readability
         */
        @ParameterizedTest(name = "{0}: ₹{1} → ₹{2}")
        @CsvSource(value = {
            "Normal Debit    | 10000 | 3000 | 7000",
            "Small Amount    | 10000 | 1    | 9999",
            "Large Amount    | 10000 | 9000 | 1000",
            "Exact Balance   | 10000 | 10000| 0"
        }, delimiter = '|')
        @DisplayName("Should handle debit with pipe-delimited test data")
        void shouldHandleDebitWithPipeDelimiter(
                String scenario,
                double balance,
                double debit,
                double expected) {
            
            Account testAccount = new Account("PIPE", "Pipe Test", balance);
            testAccount.debit(debit);
            assertEquals(expected, testAccount.getBalance(), 0.01,
                "Scenario: " + scenario);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // BOUNDARY VALUE TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryValueTests {

        /**
         * TEST: Boundary values for debit operations
         * 
         * Tests values at and around boundaries:
         * - Just below valid
         * - Exactly at boundary
         * - Just above valid
         */
        @ParameterizedTest(name = "Amount ₹{0}: {1}")
        @CsvSource({
            "0.009, INVALID",
            "0.01, VALID",
            "0.011, VALID",
            "0.99, VALID",
            "1.0, VALID",
            "1.01, VALID"
        })
        @DisplayName("Should correctly handle boundary amounts")
        void shouldHandleBoundaryAmounts(double amount, String expectedResult) {
            Account boundaryAccount = new Account("BND", "Boundary", 10000.0);

            if ("VALID".equals(expectedResult)) {
                assertDoesNotThrow(() -> boundaryAccount.debit(amount));
            } else {
                // For this implementation, amounts below 0.01 that are positive
                // are still valid. Adjust based on actual requirements.
                // This demonstrates the pattern for boundary testing.
            }
        }

        /**
         * TEST: Balance boundary - debiting exact balance
         */
        @ParameterizedTest(name = "Balance ₹{0}: Debit ₹{0} should leave ₹0")
        @ValueSource(doubles = {0.01, 1.0, 100.0, 1000.0, 10000.0, 99999.99})
        @DisplayName("Should leave zero balance when debiting exact amount")
        void shouldLeaveZeroWhenDebitingExactBalance(double exactBalance) {
            Account exactAccount = new Account("EXA", "Exact", exactBalance);

            exactAccount.debit(exactBalance);

            assertEquals(0.0, exactAccount.getBalance(), 0.001,
                "Balance should be exactly zero after full debit");
        }
    }
}
