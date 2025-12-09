package com.example.service;

import com.example.exception.*;
import com.example.model.Account;
import com.example.repository.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

/**
 * UPI Transfer Service Tests - LEVEL 4 Mockito
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    MOCKITO UNIT TESTS                         ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This test class demonstrates:                                ║
 * ║  • @Mock and @InjectMocks annotations                         ║
 * ║  • when().thenReturn() stubbing                               ║
 * ║  • when().thenThrow() for error scenarios                     ║
 * ║  • verify() for interaction verification                      ║
 * ║  • ArgumentCaptor for argument inspection                     ║
 * ║  • Argument matchers (any(), eq())                            ║
 * ║  • InOrder for call sequence verification                     ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)  // Enable Mockito annotations
@DisplayName("UPI Transfer Service - Mockito Tests")
class UPITransferServiceTest {

    // ═══════════════════════════════════════════════════════════
    // MOCK SETUP
    // ═══════════════════════════════════════════════════════════

    /**
     * @Mock creates a mock object of AccountRepository.
     * No real database calls will be made.
     */
    @Mock
    private AccountRepository accountRepository;

    /**
     * @InjectMocks creates the service and injects the mock repository.
     * Mockito automatically matches @Mock fields to constructor parameters.
     */
    @InjectMocks
    private UPITransferService transferService;

    /**
     * @Captor creates an ArgumentCaptor for inspecting method arguments.
     */
    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    // Test data
    private Account sender;
    private Account receiver;

    @BeforeEach
    void setUp() {
        // Create fresh test accounts for each test
        sender = new Account("SENDER001", "Rajesh Kumar", 10000.0);
        receiver = new Account("RECEIVER001", "Priya Sharma", 5000.0);
    }

    // ═══════════════════════════════════════════════════════════
    // SUCCESSFUL TRANSFER TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Successful Transfer Tests")
    class SuccessfulTransferTests {

        /**
         * TEST: Basic successful transfer
         * 
         * DEMONSTRATES:
         * - when().thenReturn() for stubbing repository methods
         * - Verifying balance changes
         * - Using TransferResult for assertions
         */
        @Test
        @DisplayName("Should transfer money successfully between accounts")
        void shouldTransferSuccessfully() {
            // ═══════════════════════════════════════════════
            // ARRANGE - Set up mock behavior
            // ═══════════════════════════════════════════════
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // ═══════════════════════════════════════════════
            // ACT - Execute transfer
            // ═══════════════════════════════════════════════
            UPITransferService.TransferResult result = 
                transferService.transfer("SENDER001", "RECEIVER001", 1000.0);

            // ═══════════════════════════════════════════════
            // ASSERT - Verify results
            // ═══════════════════════════════════════════════
            assertNotNull(result, "Result should not be null");
            assertEquals(1000.0, result.getAmount());
            assertEquals(10000.0, result.getSenderBalanceBefore());
            assertEquals(9000.0, result.getSenderBalanceAfter());
            assertEquals(5000.0, result.getReceiverBalanceBefore());
            assertEquals(6000.0, result.getReceiverBalanceAfter());
        }

        /**
         * TEST: Verify repository interactions
         * 
         * DEMONSTRATES:
         * - verify() to check method calls
         * - verify(times(n)) for call count
         */
        @Test
        @DisplayName("Should call repository methods in correct sequence")
        void shouldCallRepositoryMethodsCorrectly() {
            // Arrange
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act
            transferService.transfer("SENDER001", "RECEIVER001", 1000.0);

            // Assert - Verify interactions
            verify(accountRepository).loadAccountById("SENDER001");
            verify(accountRepository).loadAccountById("RECEIVER001");
            verify(accountRepository, times(2)).saveAccount(any(Account.class));
        }

        /**
         * TEST: Verify call order
         * 
         * DEMONSTRATES:
         * - InOrder for verifying sequence of calls
         */
        @Test
        @DisplayName("Should load accounts before saving them")
        void shouldLoadBeforeSave() {
            // Arrange
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act
            transferService.transfer("SENDER001", "RECEIVER001", 1000.0);

            // Assert - Verify order
            InOrder inOrder = inOrder(accountRepository);
            inOrder.verify(accountRepository).loadAccountById("SENDER001");
            inOrder.verify(accountRepository).loadAccountById("RECEIVER001");
            inOrder.verify(accountRepository, times(2)).saveAccount(any());
        }

        /**
         * TEST: Verify exact amounts using ArgumentCaptor
         * 
         * DEMONSTRATES:
         * - ArgumentCaptor to capture and inspect arguments
         * - getAllValues() for multiple captures
         */
        @Test
        @DisplayName("Should debit sender and credit receiver with correct amounts")
        void shouldDebitAndCreditCorrectAmounts() {
            // Arrange
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act
            transferService.transfer("SENDER001", "RECEIVER001", 2500.0);

            // Assert - Capture saved accounts
            verify(accountRepository, times(2)).saveAccount(accountCaptor.capture());
            List<Account> savedAccounts = accountCaptor.getAllValues();

            // Verify sender was debited
            Account savedSender = savedAccounts.get(0);
            assertEquals("SENDER001", savedSender.getAccountId());
            assertEquals(7500.0, savedSender.getBalance(), 0.01);  // 10000 - 2500

            // Verify receiver was credited
            Account savedReceiver = savedAccounts.get(1);
            assertEquals("RECEIVER001", savedReceiver.getAccountId());
            assertEquals(7500.0, savedReceiver.getBalance(), 0.01);  // 5000 + 2500
        }

        /**
         * TEST: Transfer full balance
         */
        @Test
        @DisplayName("Should allow transferring entire balance")
        void shouldAllowTransferringEntireBalance() {
            // Arrange
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act
            UPITransferService.TransferResult result = 
                transferService.transfer("SENDER001", "RECEIVER001", 10000.0);

            // Assert
            assertEquals(0.0, result.getSenderBalanceAfter());
            assertEquals(15000.0, result.getReceiverBalanceAfter());
        }

        /**
         * TEST: Minimum UPI amount
         */
        @Test
        @DisplayName("Should allow minimum UPI amount of ₹1")
        void shouldAllowMinimumUPIAmount() {
            // Arrange
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act & Assert
            assertDoesNotThrow(() -> 
                transferService.transfer("SENDER001", "RECEIVER001", 1.0));
        }

        /**
         * TEST: Maximum UPI amount
         */
        @Test
        @DisplayName("Should allow maximum UPI amount of ₹1,00,000")
        void shouldAllowMaximumUPIAmount() {
            // Arrange - Need sufficient balance
            Account richSender = new Account("RICH001", "Rich User", 200000.0);
            when(accountRepository.loadAccountById("RICH001")).thenReturn(richSender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act & Assert
            assertDoesNotThrow(() -> 
                transferService.transfer("RICH001", "RECEIVER001", 100000.0));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ACCOUNT NOT FOUND TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Account Not Found Tests")
    class AccountNotFoundTests {

        /**
         * TEST: Sender account not found
         * 
         * DEMONSTRATES:
         * - Returning null from mock to simulate missing account
         */
        @Test
        @DisplayName("Should throw AccountNotFoundException when sender not found")
        void shouldThrowWhenSenderNotFound() {
            // Arrange - Return null for sender
            when(accountRepository.loadAccountById("UNKNOWN")).thenReturn(null);

            // Act & Assert
            AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> transferService.transfer("UNKNOWN", "RECEIVER001", 1000.0)
            );

            assertEquals("UNKNOWN", exception.getAccountId());
            assertEquals("U30", exception.getErrorCode());
        }

        /**
         * TEST: Receiver account not found
         */
        @Test
        @DisplayName("Should throw AccountNotFoundException when receiver not found")
        void shouldThrowWhenReceiverNotFound() {
            // Arrange
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("UNKNOWN")).thenReturn(null);

            // Act & Assert
            AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> transferService.transfer("SENDER001", "UNKNOWN", 1000.0)
            );

            assertEquals("UNKNOWN", exception.getAccountId());
        }

        /**
         * TEST: No save should happen when account not found
         */
        @Test
        @DisplayName("Should not save any accounts when sender not found")
        void shouldNotSaveWhenSenderNotFound() {
            // Arrange
            when(accountRepository.loadAccountById("UNKNOWN")).thenReturn(null);

            // Act
            try {
                transferService.transfer("UNKNOWN", "RECEIVER001", 1000.0);
            } catch (AccountNotFoundException e) {
                // Expected
            }

            // Assert - saveAccount should never be called
            verify(accountRepository, never()).saveAccount(any());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // INSUFFICIENT BALANCE TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Insufficient Balance Tests")
    class InsufficientBalanceTests {

        /**
         * TEST: Transfer amount exceeds balance
         */
        @Test
        @DisplayName("Should throw InsufficientBalanceException when balance is insufficient")
        void shouldThrowWhenInsufficientBalance() {
            // Arrange
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act & Assert
            InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> transferService.transfer("SENDER001", "RECEIVER001", 15000.0)
            );

            assertEquals(10000.0, exception.getAvailableBalance());
            assertEquals(15000.0, exception.getRequestedAmount());
            assertEquals(5000.0, exception.getShortfall());
        }

        /**
         * TEST: No changes when transfer fails
         */
        @Test
        @DisplayName("Should not save when transfer fails due to insufficient funds")
        void shouldNotSaveOnInsufficientFunds() {
            // Arrange
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act
            try {
                transferService.transfer("SENDER001", "RECEIVER001", 50000.0);
            } catch (InsufficientBalanceException e) {
                // Expected
            }

            // Assert
            verify(accountRepository, never()).saveAccount(any());
        }

        /**
         * TEST: Zero balance account
         */
        @Test
        @DisplayName("Should throw when sender has zero balance")
        void shouldThrowWhenSenderHasZeroBalance() {
            // Arrange
            Account emptyAccount = new Account("EMPTY001", "Empty User", 0.0);
            when(accountRepository.loadAccountById("EMPTY001")).thenReturn(emptyAccount);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act & Assert
            InsufficientBalanceException exception = assertThrows(
                InsufficientBalanceException.class,
                () -> transferService.transfer("EMPTY001", "RECEIVER001", 100.0)
            );

            assertTrue(exception.isZeroBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // AMOUNT VALIDATION TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Amount Validation Tests")
    class AmountValidationTests {

        @Test
        @DisplayName("Should throw InvalidAmountException for negative amount")
        void shouldThrowForNegativeAmount() {
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> transferService.transfer("SENDER001", "RECEIVER001", -100.0)
            );

            assertTrue(exception.isNegative());
            assertEquals("U09", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should throw InvalidAmountException for zero amount")
        void shouldThrowForZeroAmount() {
            InvalidAmountException exception = assertThrows(
                InvalidAmountException.class,
                () -> transferService.transfer("SENDER001", "RECEIVER001", 0.0)
            );

            assertTrue(exception.isZero());
        }

        @Test
        @DisplayName("Should throw TransactionLimitExceededException for amount below ₹1")
        void shouldThrowForAmountBelowMinimum() {
            TransactionLimitExceededException exception = assertThrows(
                TransactionLimitExceededException.class,
                () -> transferService.transfer("SENDER001", "RECEIVER001", 0.50)
            );

            assertTrue(exception.isBelowMinimum());
        }

        @Test
        @DisplayName("Should throw TransactionLimitExceededException for amount above ₹1,00,000")
        void shouldThrowForAmountAboveMaximum() {
            TransactionLimitExceededException exception = assertThrows(
                TransactionLimitExceededException.class,
                () -> transferService.transfer("SENDER001", "RECEIVER001", 150000.0)
            );

            assertTrue(exception.isMaxLimitExceeded());
            assertEquals(100000.0, exception.getLimit());
        }

        @Test
        @DisplayName("Should not call repository for invalid amounts")
        void shouldNotCallRepositoryForInvalidAmounts() {
            try {
                transferService.transfer("SENDER001", "RECEIVER001", -100.0);
            } catch (InvalidAmountException e) { }

            verifyNoInteractions(accountRepository);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // SAME ACCOUNT TRANSFER TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Same Account Transfer Tests")
    class SameAccountTransferTests {

        @Test
        @DisplayName("Should throw SameAccountTransferException for self-transfer")
        void shouldThrowForSelfTransfer() {
            SameAccountTransferException exception = assertThrows(
                SameAccountTransferException.class,
                () -> transferService.transfer("SENDER001", "SENDER001", 1000.0)
            );

            assertEquals("SENDER001", exception.getAccountId());
            assertEquals("U16", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should not call repository for same account transfer")
        void shouldNotCallRepositoryForSameAccount() {
            try {
                transferService.transfer("ACC001", "ACC001", 1000.0);
            } catch (SameAccountTransferException e) { }

            verifyNoInteractions(accountRepository);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // UPI ID TRANSFER TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("UPI ID Transfer Tests")
    class UpiIdTransferTests {

        @Test
        @DisplayName("Should transfer using UPI IDs")
        void shouldTransferUsingUpiIds() {
            // Arrange
            when(accountRepository.findByUpiId("rajesh@upi")).thenReturn(Optional.of(sender));
            when(accountRepository.findByUpiId("priya@upi")).thenReturn(Optional.of(receiver));
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act
            UPITransferService.TransferResult result = 
                transferService.transferByUpiId("rajesh@upi", "priya@upi", 1000.0);

            // Assert
            assertEquals(9000.0, result.getSenderBalanceAfter());
            assertEquals(6000.0, result.getReceiverBalanceAfter());
        }

        @Test
        @DisplayName("Should throw AccountNotFoundException for unknown UPI ID")
        void shouldThrowForUnknownUpiId() {
            when(accountRepository.findByUpiId("unknown@upi")).thenReturn(Optional.empty());

            AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> transferService.transferByUpiId("unknown@upi", "priya@upi", 1000.0)
            );

            assertTrue(exception.isUpiIdLookup());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // BALANCE CHECK TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Balance Check Tests")
    class BalanceCheckTests {

        @Test
        @DisplayName("Should return account balance")
        void shouldReturnBalance() {
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);

            double balance = transferService.getBalance("SENDER001");

            assertEquals(10000.0, balance);
        }

        @Test
        @DisplayName("Should throw for non-existent account balance check")
        void shouldThrowForNonExistentAccountBalance() {
            when(accountRepository.loadAccountById("UNKNOWN")).thenReturn(null);

            assertThrows(AccountNotFoundException.class,
                () -> transferService.getBalance("UNKNOWN"));
        }

        @Test
        @DisplayName("Should check if account exists")
        void shouldCheckAccountExists() {
            when(accountRepository.existsById("SENDER001")).thenReturn(true);
            when(accountRepository.existsById("UNKNOWN")).thenReturn(false);

            assertTrue(transferService.accountExists("SENDER001"));
            assertFalse(transferService.accountExists("UNKNOWN"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ARGUMENT MATCHER DEMONSTRATIONS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Argument Matcher Demonstrations")
    class ArgumentMatcherTests {

        @Test
        @DisplayName("Should use custom argument matcher with argThat")
        void shouldUseCustomMatcher() {
            // Arrange
            when(accountRepository.loadAccountById(argThat(id -> id.startsWith("SENDER"))))
                .thenReturn(sender);
            when(accountRepository.loadAccountById(argThat(id -> id.startsWith("RECEIVER"))))
                .thenReturn(receiver);

            // Act
            transferService.transfer("SENDER001", "RECEIVER001", 1000.0);

            // Assert - Verify with custom matcher
            verify(accountRepository).saveAccount(argThat(
                acc -> acc.getAccountId().equals("SENDER001") && acc.getBalance() == 9000.0
            ));
        }
    }
}
