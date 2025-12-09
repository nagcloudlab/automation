package com.example.service;

import com.example.exception.*;
import com.example.model.Account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Transaction Scenario Tests using External CSV Data - LEVEL 3
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    @CsvFileSource TESTS                       ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This test class demonstrates:                                ║
 * ║  • @CsvFileSource for external test data                      ║
 * ║  • Managing large test datasets                               ║
 * ║  • Test data files in resources folder                        ║
 * ║  • Handling different result types                            ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * BENEFITS OF EXTERNAL TEST DATA:
 * ================================
 * • Separate test logic from test data
 * • Easy to add/modify test cases
 * • Non-developers can contribute test data
 * • Version control friendly
 * • Can generate from production data
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Transaction Scenarios - CSV File Source Tests")
class TransactionScenarioTest {

    // ═══════════════════════════════════════════════════════════
    // @CsvFileSource TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("CSV File Source Tests")
    class CsvFileSourceTests {

        /**
         * TEST: UPI transactions from CSV file
         * 
         * CSV Format:
         * senderBalance,amount,expectedResult,expectedErrorCode,description
         */
        @ParameterizedTest(name = "[{index}] {4}: ₹{0} - ₹{1} → {2}")
        @CsvFileSource(
            resources = "/test-data/upi-transactions.csv",
            numLinesToSkip = 4,  // Skip header comments
            delimiter = ','
        )
        @DisplayName("Should handle all transaction scenarios from CSV")
        void shouldHandleTransactionScenariosFromCSV(
                double senderBalance,
                double amount,
                String expectedResult,
                String expectedErrorCode,
                String description) {
            
            Account sender = new Account("SENDER", "Sender", senderBalance);

            switch (expectedResult) {
                case "SUCCESS":
                    assertDoesNotThrow(() -> sender.debit(amount),
                        "Transaction should succeed: " + description);
                    assertEquals(senderBalance - amount, sender.getBalance(), 0.001);
                    break;
                    
                case "INSUFFICIENT_BALANCE":
                    InsufficientBalanceException ibEx = assertThrows(
                        InsufficientBalanceException.class,
                        () -> sender.debit(amount),
                        "Should throw InsufficientBalanceException: " + description
                    );
                    assertEquals(expectedErrorCode, ibEx.getErrorCode());
                    assertEquals(senderBalance, sender.getBalance(), 0.001,
                        "Balance should remain unchanged");
                    break;
                    
                case "INVALID_AMOUNT":
                    InvalidAmountException iaEx = assertThrows(
                        InvalidAmountException.class,
                        () -> sender.debit(amount),
                        "Should throw InvalidAmountException: " + description
                    );
                    assertEquals(expectedErrorCode, iaEx.getErrorCode());
                    assertEquals(senderBalance, sender.getBalance(), 0.001,
                        "Balance should remain unchanged");
                    break;
                    
                case "LIMIT_EXCEEDED":
                    TransactionLimitExceededException tlEx = assertThrows(
                        TransactionLimitExceededException.class,
                        () -> sender.debit(amount),
                        "Should throw TransactionLimitExceededException: " + description
                    );
                    assertEquals(expectedErrorCode, tlEx.getErrorCode());
                    break;
                    
                default:
                    fail("Unknown expected result: " + expectedResult);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // INLINE CSV SCENARIOS FOR QUICK REFERENCE
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Inline CSV Scenario Tests")
    class InlineCsvScenarioTests {

        /**
         * TEST: Successful UPI transfer scenarios
         * 
         * Tests various successful transaction combinations
         */
        @ParameterizedTest(name = "{4}")
        @CsvSource({
            // senderBal, receiverBal, amount, expectedSenderBal, description
            "10000, 5000, 1000, 9000, Small P2P transfer",
            "50000, 10000, 25000, 25000, Large P2P transfer",
            "100000, 0, 100000, 0, Maximum UPI transfer",
            "1000, 1000, 1, 999, Minimum UPI transfer",
            "5000, 5000, 5000, 0, Full balance transfer"
        })
        @DisplayName("Should complete successful P2P transfers")
        void shouldCompleteSuccessfulP2PTransfers(
                double senderBalance,
                double receiverBalance,
                double amount,
                double expectedSenderBalance,
                String description) {
            
            // Arrange
            Account sender = new Account("S001", "Sender", senderBalance);
            Account receiver = new Account("R001", "Receiver", receiverBalance);

            // Act
            sender.debit(amount);
            receiver.credit(amount);

            // Assert
            assertAll("P2P Transfer: " + description,
                () -> assertEquals(expectedSenderBalance, sender.getBalance(), 0.001,
                    "Sender balance incorrect"),
                () -> assertEquals(receiverBalance + amount, receiver.getBalance(), 0.001,
                    "Receiver balance incorrect")
            );
        }

        /**
         * TEST: Real-world UPI payment scenarios
         */
        @ParameterizedTest(name = "{3}")
        @CsvSource({
            // initialBalance, paymentAmount, expectedBalance, scenario
            "50000, 199, 49801, Auto rickshaw fare",
            "50000, 350, 49650, Food delivery order",
            "50000, 999, 49001, Annual subscription",
            "50000, 1499, 48501, Mobile recharge",
            "50000, 2999, 47001, Online shopping",
            "50000, 5000, 45000, Utility bill payment",
            "50000, 15000, 35000, Insurance premium",
            "50000, 25000, 25000, Rent payment"
        })
        @DisplayName("Should handle real-world UPI payment scenarios")
        void shouldHandleRealWorldPayments(
                double initialBalance,
                double paymentAmount,
                double expectedBalance,
                String scenario) {
            
            Account customer = new Account("CUST001", "Customer", initialBalance);
            
            customer.debit(paymentAmount);
            
            assertEquals(expectedBalance, customer.getBalance(), 0.001,
                "Balance after " + scenario);
        }

        /**
         * TEST: Split bill scenarios
         */
        @ParameterizedTest(name = "Split ₹{0} among {1} people = ₹{2} each")
        @CsvSource({
            "1000, 2, 500",
            "1500, 3, 500",
            "2000, 4, 500",
            "1200, 4, 300",
            "999, 3, 333",
            "2400, 6, 400"
        })
        @DisplayName("Should correctly split bills")
        void shouldCorrectlySplitBills(
                double totalBill,
                int numberOfPeople,
                double expectedShare) {
            
            double share = totalBill / numberOfPeople;
            
            assertEquals(expectedShare, share, 0.01,
                String.format("Split of ₹%.2f among %d people", totalBill, numberOfPeople));
        }

        /**
         * TEST: Merchant category wise transactions
         */
        @ParameterizedTest(name = "MCC {0}: {1} - ₹{2}")
        @CsvSource({
            "5411, Grocery Stores, 500",
            "5812, Restaurants, 350",
            "5541, Fuel Stations, 2000",
            "4121, Taxi/Rideshare, 199",
            "5912, Pharmacy, 450",
            "5311, Department Stores, 3000",
            "4814, Telecom Services, 999",
            "6300, Insurance, 15000"
        })
        @DisplayName("Should process merchant category transactions")
        void shouldProcessMerchantCategoryTransactions(
                String mcc,
                String merchantType,
                double amount) {
            
            Account customer = new Account("CUST", "Customer", 50000.0);
            
            assertDoesNotThrow(() -> customer.debit(amount),
                "Transaction to " + merchantType + " (MCC: " + mcc + ") should succeed");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ERROR SCENARIO TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Error Scenario Tests")
    class ErrorScenarioTests {

        /**
         * TEST: Various failure scenarios with expected error codes
         */
        @ParameterizedTest(name = "{3} (Error: {2})")
        @CsvSource({
            "1000, 2000, U30, Insufficient balance",
            "0, 100, U30, Zero balance withdrawal",
            "500, 500.01, U30, Slightly exceeds balance",
            "10000, -100, U09, Negative amount",
            "10000, 0, U09, Zero amount"
        })
        @DisplayName("Should return correct error codes for failures")
        void shouldReturnCorrectErrorCodes(
                double balance,
                double amount,
                String expectedErrorCode,
                String scenario) {
            
            Account account = new Account("ERR", "Error Test", balance);

            TransferException exception = assertThrows(
                TransferException.class,
                () -> account.debit(amount),
                scenario + " should throw exception"
            );

            assertEquals(expectedErrorCode, exception.getErrorCode(),
                "Error code for: " + scenario);
        }

        /**
         * TEST: Balance unchanged after failed transactions
         */
        @ParameterizedTest(name = "Balance ₹{0} after failed debit of ₹{1}")
        @CsvSource({
            "5000, 10000",
            "1000, 1001",
            "100, 100.01",
            "10000, -500",
            "10000, 0"
        })
        @DisplayName("Balance should remain unchanged after failed debit")
        void balanceShouldRemainUnchangedAfterFailure(
                double initialBalance,
                double failedAmount) {
            
            Account account = new Account("FAIL", "Fail Test", initialBalance);

            try {
                account.debit(failedAmount);
                fail("Should have thrown exception");
            } catch (TransferException e) {
                // Expected
            }

            assertEquals(initialBalance, account.getBalance(), 0.001,
                "Balance must not change after failed debit");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // PRECISION TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Decimal Precision Tests")
    class PrecisionTests {

        /**
         * TEST: Paisa-level precision in transactions
         */
        @ParameterizedTest(name = "₹{0} - ₹{1} = ₹{2}")
        @CsvSource({
            "100.00, 33.33, 66.67",
            "100.00, 0.01, 99.99",
            "1000.50, 500.25, 500.25",
            "99.99, 99.99, 0.00",
            "0.03, 0.01, 0.02"
        })
        @DisplayName("Should maintain paisa-level precision")
        void shouldMaintainPaisaPrecision(
                double initial,
                double debit,
                double expected) {
            
            Account account = new Account("PREC", "Precision", initial);
            
            account.debit(debit);
            
            assertEquals(expected, account.getBalance(), 0.001,
                String.format("%.2f - %.2f should equal %.2f", initial, debit, expected));
        }
    }
}
