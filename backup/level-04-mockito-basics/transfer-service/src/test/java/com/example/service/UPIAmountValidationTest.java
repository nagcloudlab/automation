package com.example.service;

import com.example.exception.*;
import com.example.model.Account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UPI Amount Validation Tests - LEVEL 3 Advanced
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    ADVANCED PARAMETERIZED TESTS               ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This test class demonstrates:                                ║
 * ║  • @MethodSource for complex test data                        ║
 * ║  • @EnumSource for enum-based tests                           ║
 * ║  • @ArgumentsSource for custom providers                      ║
 * ║  • Testing with Arguments.of()                                ║
 * ║  • Combining multiple sources                                 ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * UPI LIMITS (NPCI):
 * ==================
 * • Minimum: ₹1 per transaction
 * • Maximum: ₹1,00,000 per transaction
 * • Daily limit: Varies by bank (typically ₹1-2 lakh)
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("UPI Amount Validation - Advanced Parameterized Tests")
class UPIAmountValidationTest {

    // UPI Transaction Limits
    private static final double UPI_MIN_AMOUNT = 1.0;
    private static final double UPI_MAX_AMOUNT = 100000.0;

    private Account senderAccount;
    private Account receiverAccount;

    @BeforeEach
    void setUp() {
        senderAccount = new Account("SENDER001", "Sender", 100000.0);
        receiverAccount = new Account("RECEIVER001", "Receiver", 50000.0);
    }

    // ═══════════════════════════════════════════════════════════
    // @MethodSource TESTS - Complex Test Data
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@MethodSource Tests")
    class MethodSourceTests {

        /**
         * TEST: Valid UPI amounts using @MethodSource
         * 
         * @MethodSource references a static method that returns Stream
         */
        @ParameterizedTest(name = "Valid amount: ₹{0}")
        @MethodSource("provideValidUPIAmounts")
        @DisplayName("Should accept all valid UPI amounts")
        void shouldAcceptValidUPIAmounts(double amount) {
            // Act & Assert
            assertDoesNotThrow(() -> validateUPIAmount(amount),
                "Amount ₹" + amount + " should be valid");
        }

        /**
         * Provides valid UPI amounts for testing.
         * Method must be static and return Stream, Iterable, or Array.
         */
        static Stream<Double> provideValidUPIAmounts() {
            return Stream.of(
                1.0,           // Minimum
                1.01,          // Just above minimum
                10.0,          // Small
                100.0,         // Common
                500.0,         // Common
                1000.0,        // Common
                5000.0,        // Medium
                10000.0,       // Large
                50000.0,       // Very large
                99999.0,       // Near maximum
                99999.99,      // Just below maximum
                100000.0       // Maximum
            );
        }

        /**
         * TEST: Invalid UPI amounts with expected exception type
         * 
         * Uses Arguments.of() to provide multiple parameters
         */
        @ParameterizedTest(name = "Amount ₹{0} should throw {1}")
        @MethodSource("provideInvalidAmountsWithExpectedException")
        @DisplayName("Should reject invalid amounts with correct exception")
        void shouldRejectInvalidAmounts(double amount, Class<? extends Exception> expectedType) {
            assertThrows(expectedType, () -> validateUPIAmountStrict(amount));
        }

        /**
         * Provides invalid amounts with their expected exception types.
         */
        static Stream<Arguments> provideInvalidAmountsWithExpectedException() {
            return Stream.of(
                // Negative amounts → InvalidAmountException
                Arguments.of(-1000.0, InvalidAmountException.class),
                Arguments.of(-100.0, InvalidAmountException.class),
                Arguments.of(-1.0, InvalidAmountException.class),
                Arguments.of(-0.01, InvalidAmountException.class),
                
                // Zero → InvalidAmountException
                Arguments.of(0.0, InvalidAmountException.class),
                
                // Below minimum → TransactionLimitExceededException
                Arguments.of(0.5, TransactionLimitExceededException.class),
                Arguments.of(0.99, TransactionLimitExceededException.class),
                
                // Above maximum → TransactionLimitExceededException
                Arguments.of(100000.01, TransactionLimitExceededException.class),
                Arguments.of(150000.0, TransactionLimitExceededException.class),
                Arguments.of(200000.0, TransactionLimitExceededException.class)
            );
        }

        /**
         * TEST: Transaction scenarios with full details
         */
        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("provideTransactionScenarios")
        @DisplayName("Should handle various transaction scenarios")
        void shouldHandleTransactionScenarios(
                String scenarioName,
                double senderBalance,
                double amount,
                boolean shouldSucceed,
                String expectedErrorCode) {
            
            Account sender = new Account("S001", "Sender", senderBalance);

            if (shouldSucceed) {
                assertDoesNotThrow(() -> sender.debit(amount),
                    "Scenario '" + scenarioName + "' should succeed");
            } else {
                TransferException exception = assertThrows(
                    TransferException.class,
                    () -> sender.debit(amount),
                    "Scenario '" + scenarioName + "' should fail"
                );
                assertEquals(expectedErrorCode, exception.getErrorCode(),
                    "Error code should match for: " + scenarioName);
            }
        }

        /**
         * Complex test scenarios with named parameters.
         */
        static Stream<Arguments> provideTransactionScenarios() {
            return Stream.of(
                // scenarioName, senderBalance, amount, shouldSucceed, expectedErrorCode
                Arguments.of("Normal transaction", 10000.0, 5000.0, true, null),
                Arguments.of("Minimum amount", 10000.0, 1.0, true, null),
                Arguments.of("Exact balance", 5000.0, 5000.0, true, null),
                Arguments.of("Insufficient balance", 1000.0, 5000.0, false, "U30"),
                Arguments.of("Negative amount", 10000.0, -100.0, false, "U09"),
                Arguments.of("Zero amount", 10000.0, 0.0, false, "U09"),
                Arguments.of("Zero balance debit", 0.0, 100.0, false, "U30")
            );
        }

        /**
         * TEST: Using external method source
         * 
         * References method in another class (for shared test data)
         */
        @ParameterizedTest(name = "Common UPI amount: ₹{0}")
        @MethodSource("com.example.service.UPIAmountValidationTest#commonUPIPaymentAmounts")
        @DisplayName("Should handle common UPI payment amounts")
        void shouldHandleCommonUPIPayments(double amount, String description) {
            assertDoesNotThrow(() -> senderAccount.debit(amount),
                description + " (₹" + amount + ") should be valid");
        }

        /**
         * Common UPI payment amounts seen in daily transactions.
         */
        static Stream<Arguments> commonUPIPaymentAmounts() {
            return Stream.of(
                Arguments.of(10.0, "Tea/Coffee"),
                Arguments.of(20.0, "Auto minimum"),
                Arguments.of(50.0, "Snacks"),
                Arguments.of(100.0, "Street food"),
                Arguments.of(150.0, "Auto fare"),
                Arguments.of(200.0, "Small meal"),
                Arguments.of(300.0, "Food delivery"),
                Arguments.of(500.0, "Dinner for two"),
                Arguments.of(999.0, "Subscription"),
                Arguments.of(1499.0, "Monthly recharge"),
                Arguments.of(2000.0, "Shopping"),
                Arguments.of(5000.0, "Electronics"),
                Arguments.of(10000.0, "Appliances"),
                Arguments.of(25000.0, "Rent partial"),
                Arguments.of(50000.0, "Large purchase")
            );
        }
    }

    // ═══════════════════════════════════════════════════════════
    // @EnumSource TESTS - Enum-Based Tests
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("@EnumSource Tests")
    class EnumSourceTests {

        /**
         * TEST: All InvalidAmountException validation types
         */
        @ParameterizedTest(name = "Validation type: {0}")
        @EnumSource(InvalidAmountException.AmountValidationType.class)
        @DisplayName("Should have description for all validation types")
        void allValidationTypesShouldHaveDescription(
                InvalidAmountException.AmountValidationType type) {
            
            assertNotNull(type.getDescription(),
                "Validation type " + type + " should have description");
            assertFalse(type.getDescription().isEmpty(),
                "Description should not be empty");
        }

        /**
         * TEST: All TransactionLimitExceededException limit types
         */
        @ParameterizedTest(name = "Limit type: {0}")
        @EnumSource(TransactionLimitExceededException.LimitType.class)
        @DisplayName("Should have description for all limit types")
        void allLimitTypesShouldHaveDescription(
                TransactionLimitExceededException.LimitType type) {
            
            assertNotNull(type.getDescription());
            assertFalse(type.getDescription().isEmpty());
        }

        /**
         * TEST: Selected enum values only
         * 
         * Test only specific enum values using 'names' attribute
         */
        @ParameterizedTest(name = "Transaction limit: {0}")
        @EnumSource(
            value = TransactionLimitExceededException.LimitType.class,
            names = {"PER_TRANSACTION_MIN", "PER_TRANSACTION_MAX"}
        )
        @DisplayName("Should test per-transaction limits only")
        void shouldTestPerTransactionLimitsOnly(
                TransactionLimitExceededException.LimitType type) {
            
            assertTrue(
                type == TransactionLimitExceededException.LimitType.PER_TRANSACTION_MIN ||
                type == TransactionLimitExceededException.LimitType.PER_TRANSACTION_MAX
            );
        }

        /**
         * TEST: Exclude specific enum values
         */
        @ParameterizedTest(name = "Aggregate limit: {0}")
        @EnumSource(
            value = TransactionLimitExceededException.LimitType.class,
            mode = EnumSource.Mode.EXCLUDE,
            names = {"PER_TRANSACTION_MIN", "PER_TRANSACTION_MAX", "BENEFICIARY"}
        )
        @DisplayName("Should test aggregate limits (daily/monthly)")
        void shouldTestAggregateLimitsOnly(
                TransactionLimitExceededException.LimitType type) {
            
            assertTrue(
                type == TransactionLimitExceededException.LimitType.DAILY ||
                type == TransactionLimitExceededException.LimitType.MONTHLY
            );
        }

        /**
         * TEST: Enum values matching regex pattern
         */
        @ParameterizedTest(name = "Per-transaction type: {0}")
        @EnumSource(
            value = TransactionLimitExceededException.LimitType.class,
            mode = EnumSource.Mode.MATCH_ALL,
            names = ".*TRANSACTION.*"
        )
        @DisplayName("Should match transaction-related limit types")
        void shouldMatchTransactionLimitTypes(
                TransactionLimitExceededException.LimitType type) {
            
            assertTrue(type.name().contains("TRANSACTION"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // COMBINED SOURCES TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Combined Source Tests")
    class CombinedSourceTests {

        /**
         * TEST: Combine @ValueSource with @NullSource
         */
        @ParameterizedTest(name = "Account ID: {0}")
        @NullSource
        @ValueSource(strings = {"", " ", "   "})
        @DisplayName("Should handle null and blank account IDs")
        void shouldHandleNullAndBlankAccountIds(String accountId) {
            // This test documents behavior - actual validation depends on impl
            if (accountId == null || accountId.isBlank()) {
                // In production, constructor should validate
                Account acc = new Account(accountId, "Test", 1000.0);
                // Verify account was created (or throw if validation added)
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════

    /**
     * Validate UPI amount is within allowed range.
     * Basic validation - amount must be positive.
     */
    private void validateUPIAmount(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
    }

    /**
     * Strict UPI amount validation including limits.
     */
    private void validateUPIAmountStrict(double amount) {
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

    // ═══════════════════════════════════════════════════════════
    // BOUNDARY TESTS WITH @MethodSource
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryTests {

        /**
         * TEST: UPI limit boundary values
         * 
         * Tests values exactly at, just below, and just above limits
         */
        @ParameterizedTest(name = "Boundary: ₹{0} should be {1}")
        @MethodSource("provideBoundaryValues")
        @DisplayName("Should correctly handle boundary values")
        void shouldHandleBoundaryValues(double amount, String expectedResult) {
            if ("VALID".equals(expectedResult)) {
                assertDoesNotThrow(() -> validateUPIAmountStrict(amount));
            } else if ("BELOW_MIN".equals(expectedResult)) {
                TransactionLimitExceededException ex = assertThrows(
                    TransactionLimitExceededException.class,
                    () -> validateUPIAmountStrict(amount)
                );
                assertTrue(ex.isBelowMinimum());
            } else if ("ABOVE_MAX".equals(expectedResult)) {
                TransactionLimitExceededException ex = assertThrows(
                    TransactionLimitExceededException.class,
                    () -> validateUPIAmountStrict(amount)
                );
                assertTrue(ex.isMaxLimitExceeded());
            }
        }

        static Stream<Arguments> provideBoundaryValues() {
            return Stream.of(
                // Below minimum boundary
                Arguments.of(0.50, "BELOW_MIN"),
                Arguments.of(0.99, "BELOW_MIN"),
                Arguments.of(0.999, "BELOW_MIN"),
                
                // At minimum boundary
                Arguments.of(1.0, "VALID"),
                Arguments.of(1.01, "VALID"),
                
                // Normal range
                Arguments.of(50000.0, "VALID"),
                
                // At maximum boundary
                Arguments.of(99999.99, "VALID"),
                Arguments.of(100000.0, "VALID"),
                
                // Above maximum boundary
                Arguments.of(100000.01, "ABOVE_MAX"),
                Arguments.of(100001.0, "ABOVE_MAX"),
                Arguments.of(150000.0, "ABOVE_MAX")
            );
        }
    }
}
