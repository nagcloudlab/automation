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
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

/**
 * Mockito Patterns Demo - LEVEL 4 Advanced
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    MOCKITO PATTERNS DEMO                      ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This test class demonstrates advanced Mockito patterns:      ║
 * ║  • Argument Matchers (any, eq, argThat)                       ║
 * ║  • Stubbing Consecutive Calls                                 ║
 * ║  • Stubbing with Answers                                      ║
 * ║  • ArgumentCaptor advanced usage                              ║
 * ║  • Spies (partial mocks)                                      ║
 * ║  • doReturn/doThrow for void methods                          ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Mockito Patterns Demo")
class MockitoPatternsDemoTest {

    @Mock
    private AccountRepository accountRepository;

    private UPITransferService transferService;

    private Account sender;
    private Account receiver;

    @BeforeEach
    void setUp() {
        transferService = new UPITransferService(accountRepository);
        sender = new Account("SENDER001", "Rajesh Kumar", 10000.0);
        receiver = new Account("RECEIVER001", "Priya Sharma", 5000.0);
    }

    // ═══════════════════════════════════════════════════════════
    // ARGUMENT MATCHERS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Argument Matchers")
    class ArgumentMatcherTests {

        /**
         * TEST: Using any() matcher
         * 
         * any() matches ANY value of the specified type
         */
        @Test
        @DisplayName("Using any() matcher")
        void usingAnyMatcher() {
            // Arrange - Return sender for ANY string argument
            when(accountRepository.loadAccountById(anyString())).thenReturn(sender);

            // Act
            Account result1 = accountRepository.loadAccountById("ACC001");
            Account result2 = accountRepository.loadAccountById("ACC002");
            Account result3 = accountRepository.loadAccountById("ANYTHING");

            // Assert - All return the same sender
            assertAll("All calls return sender",
                () -> assertEquals(sender, result1),
                () -> assertEquals(sender, result2),
                () -> assertEquals(sender, result3)
            );
        }

        /**
         * TEST: Using eq() for exact matching
         * 
         * eq() matches exact value - useful when mixing with any()
         */
        @Test
        @DisplayName("Using eq() for exact matching")
        void usingEqMatcher() {
            // Arrange - Different returns for different inputs
            when(accountRepository.loadAccountById(eq("SENDER001"))).thenReturn(sender);
            when(accountRepository.loadAccountById(eq("RECEIVER001"))).thenReturn(receiver);

            // Act
            Account senderResult = accountRepository.loadAccountById("SENDER001");
            Account receiverResult = accountRepository.loadAccountById("RECEIVER001");

            // Assert
            assertEquals("Rajesh Kumar", senderResult.getAccountHolderName());
            assertEquals("Priya Sharma", receiverResult.getAccountHolderName());
        }

        /**
         * TEST: Using argThat() for custom matching
         * 
         * argThat() allows custom predicate-based matching
         */
        @Test
        @DisplayName("Using argThat() for custom matching")
        void usingArgThatMatcher() {
            // Arrange
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act
            transferService.transfer("SENDER001", "RECEIVER001", 1000.0);

            // Assert - Verify saveAccount was called with account having balance 9000
            verify(accountRepository).saveAccount(
                argThat(account -> account.getBalance() == 9000.0)
            );

            // Verify with account having balance 6000
            verify(accountRepository).saveAccount(
                argThat(account -> account.getBalance() == 6000.0)
            );
        }

        /**
         * TEST: Custom ArgumentMatcher class
         */
        @Test
        @DisplayName("Using custom ArgumentMatcher")
        void usingCustomArgumentMatcher() {
            // Arrange
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act
            transferService.transfer("SENDER001", "RECEIVER001", 1000.0);

            // Assert - Using custom matcher
            verify(accountRepository).saveAccount(argThat(new AccountWithPositiveBalanceMatcher()));
        }

        /**
         * Custom ArgumentMatcher for Account with positive balance
         */
        static class AccountWithPositiveBalanceMatcher implements ArgumentMatcher<Account> {
            @Override
            public boolean matches(Account account) {
                return account != null && account.getBalance() >= 0;
            }

            @Override
            public String toString() {
                return "Account with positive balance";
            }
        }

        /**
         * TEST: Using contains() for string matching
         */
        @Test
        @DisplayName("Using contains() for string matching")
        void usingContainsMatcher() {
            // Arrange - Return sender for any ID containing "SENDER"
            when(accountRepository.loadAccountById(contains("SENDER"))).thenReturn(sender);

            // Act
            Account result1 = accountRepository.loadAccountById("SENDER001");
            Account result2 = accountRepository.loadAccountById("SENDER_NEW");

            // Assert
            assertEquals(sender, result1);
            assertEquals(sender, result2);
        }

        /**
         * TEST: Using startsWith() for string matching
         */
        @Test
        @DisplayName("Using startsWith() for string matching")
        void usingStartsWithMatcher() {
            // Arrange
            when(accountRepository.loadAccountById(startsWith("ACC"))).thenReturn(sender);

            // Act
            Account result = accountRepository.loadAccountById("ACC123");

            // Assert
            assertEquals(sender, result);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // STUBBING PATTERNS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Stubbing Patterns")
    class StubbingPatternTests {

        /**
         * TEST: Consecutive call stubbing
         * 
         * Different returns for consecutive calls to same method
         */
        @Test
        @DisplayName("Stubbing consecutive calls")
        void stubbingConsecutiveCalls() {
            // Arrange - First call returns sender, second returns receiver
            when(accountRepository.loadAccountById(anyString()))
                .thenReturn(sender)
                .thenReturn(receiver);

            // Act
            Account first = accountRepository.loadAccountById("ANY");
            Account second = accountRepository.loadAccountById("ANY");
            Account third = accountRepository.loadAccountById("ANY"); // Last stub repeats

            // Assert
            assertEquals(sender, first);
            assertEquals(receiver, second);
            assertEquals(receiver, third); // Repeats last stubbed value
        }

        /**
         * TEST: Stubbing with thenAnswer for dynamic responses
         */
        @Test
        @DisplayName("Stubbing with thenAnswer for dynamic response")
        void stubbingWithThenAnswer() {
            // Arrange - Dynamic response based on input
            when(accountRepository.loadAccountById(anyString()))
                .thenAnswer(invocation -> {
                    String accountId = invocation.getArgument(0);
                    return new Account(accountId, "Dynamic User", 1000.0);
                });

            // Act
            Account acc1 = accountRepository.loadAccountById("ACC001");
            Account acc2 = accountRepository.loadAccountById("ACC002");

            // Assert - Each gets unique ID
            assertEquals("ACC001", acc1.getAccountId());
            assertEquals("ACC002", acc2.getAccountId());
        }

        /**
         * TEST: Stubbing to throw exception
         */
        @Test
        @DisplayName("Stubbing to throw exception")
        void stubbingToThrowException() {
            // Arrange
            when(accountRepository.loadAccountById("INVALID"))
                .thenThrow(new RuntimeException("Database connection failed"));

            // Act & Assert
            assertThrows(
                RuntimeException.class,
                () -> accountRepository.loadAccountById("INVALID")
            );
        }

        /**
         * TEST: First return then throw
         */
        @Test
        @DisplayName("First return, then throw")
        void firstReturnThenThrow() {
            // Arrange - First call succeeds, second fails
            when(accountRepository.loadAccountById(anyString()))
                .thenReturn(sender)
                .thenThrow(new RuntimeException("Connection lost"));

            // Act
            Account first = accountRepository.loadAccountById("ACC001"); // Works

            // Assert
            assertEquals(sender, first);
            assertThrows(RuntimeException.class, 
                () -> accountRepository.loadAccountById("ACC001")); // Fails
        }

        /**
         * TEST: doReturn for void methods / spies
         */
        @Test
        @DisplayName("Using doReturn().when() syntax")
        void usingDoReturnWhen() {
            // This syntax is needed for:
            // 1. Void methods
            // 2. Spies (to avoid calling real method during stubbing)
            
            // Arrange
            doReturn(sender).when(accountRepository).loadAccountById("SENDER001");

            // Act
            Account result = accountRepository.loadAccountById("SENDER001");

            // Assert
            assertEquals(sender, result);
        }

        /**
         * TEST: doNothing for void methods
         */
        @Test
        @DisplayName("Using doNothing() for void methods")
        void usingDoNothingForVoidMethods() {
            // Arrange - Make saveAccount do nothing (it's void)
            doNothing().when(accountRepository).saveAccount(any(Account.class));

            // Act - Should not throw
            assertDoesNotThrow(() -> accountRepository.saveAccount(sender));

            // Verify it was called
            verify(accountRepository).saveAccount(sender);
        }

        /**
         * TEST: doThrow for void methods
         */
        @Test
        @DisplayName("Using doThrow() for void methods")
        void usingDoThrowForVoidMethods() {
            // Arrange - Make saveAccount throw exception
            doThrow(new RuntimeException("Database error"))
                .when(accountRepository).saveAccount(any(Account.class));

            // Act & Assert
            assertThrows(
                RuntimeException.class,
                () -> accountRepository.saveAccount(sender)
            );
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ARGUMENT CAPTOR PATTERNS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("ArgumentCaptor Patterns")
    class ArgumentCaptorTests {

        /**
         * TEST: Capture single argument
         */
        @Test
        @DisplayName("Capture single argument")
        void captureSingleArgument() {
            // Arrange
            ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act
            transferService.transfer("SENDER001", "RECEIVER001", 1000.0);

            // Assert - Capture first save call
            verify(accountRepository, atLeastOnce()).saveAccount(captor.capture());
            
            Account captured = captor.getValue(); // Gets LAST captured value
            assertNotNull(captured);
        }

        /**
         * TEST: Capture all arguments
         */
        @Test
        @DisplayName("Capture all arguments from multiple calls")
        void captureAllArguments() {
            // Arrange
            ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

            // Act
            transferService.transfer("SENDER001", "RECEIVER001", 1000.0);

            // Assert - Capture all saves
            verify(accountRepository, times(2)).saveAccount(captor.capture());
            
            var allCaptured = captor.getAllValues();
            assertEquals(2, allCaptured.size());
            
            // Verify sender was saved with reduced balance
            Account savedSender = allCaptured.stream()
                .filter(a -> a.getAccountId().equals("SENDER001"))
                .findFirst()
                .orElseThrow();
            assertEquals(9000.0, savedSender.getBalance());
            
            // Verify receiver was saved with increased balance
            Account savedReceiver = allCaptured.stream()
                .filter(a -> a.getAccountId().equals("RECEIVER001"))
                .findFirst()
                .orElseThrow();
            assertEquals(6000.0, savedReceiver.getBalance());
        }

        /**
         * TEST: Capture with @Captor annotation
         */
        @Test
        @DisplayName("Using @Captor annotation")
        void usingCaptorAnnotation() {
            // This test shows the @Captor annotation usage
            // (declared at field level in main test class)
            
            ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
            
            when(accountRepository.loadAccountById(anyString())).thenReturn(sender);
            
            // Act
            accountRepository.loadAccountById("ACC001");
            accountRepository.loadAccountById("ACC002");
            
            // Verify and capture
            verify(accountRepository, times(2)).loadAccountById(idCaptor.capture());
            
            var capturedIds = idCaptor.getAllValues();
            assertTrue(capturedIds.contains("ACC001"));
            assertTrue(capturedIds.contains("ACC002"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // VERIFICATION MODES
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Verification Modes")
    class VerificationModeTests {

        @BeforeEach
        void arrangeCommonStubs() {
            when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
            when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);
        }

        @Test
        @DisplayName("verify times(n)")
        void verifyTimesN() {
            transferService.transfer("SENDER001", "RECEIVER001", 1000.0);
            
            verify(accountRepository, times(2)).loadAccountById(anyString());
            verify(accountRepository, times(2)).saveAccount(any());
        }

        @Test
        @DisplayName("verify atLeast(n)")
        void verifyAtLeastN() {
            transferService.transfer("SENDER001", "RECEIVER001", 1000.0);
            
            verify(accountRepository, atLeast(1)).saveAccount(any());
        }

        @Test
        @DisplayName("verify atMost(n)")
        void verifyAtMostN() {
            transferService.transfer("SENDER001", "RECEIVER001", 1000.0);
            
            verify(accountRepository, atMost(5)).loadAccountById(anyString());
        }

        @Test
        @DisplayName("verify never()")
        void verifyNever() {
            // Act - Invalid amount, should fail before loading accounts
            assertThrows(InvalidAmountException.class, 
                () -> transferService.transfer("SENDER001", "RECEIVER001", -100.0));
            
            // Verify repository was never called
            verify(accountRepository, never()).loadAccountById(anyString());
            verify(accountRepository, never()).saveAccount(any());
        }

        @Test
        @DisplayName("verify atLeastOnce()")
        void verifyAtLeastOnce() {
            transferService.transfer("SENDER001", "RECEIVER001", 1000.0);
            
            verify(accountRepository, atLeastOnce()).saveAccount(any());
        }

        @Test
        @DisplayName("verifyNoInteractions()")
        void verifyNoInteractionsDemo() {
            // Act - Same account transfer fails before using repository
            assertThrows(SameAccountTransferException.class,
                () -> transferService.transfer("ACC001", "ACC001", 1000.0));
            
            // Verify no interactions at all
            verifyNoInteractions(accountRepository);
        }

        @Test
        @DisplayName("verifyNoMoreInteractions()")
        void verifyNoMoreInteractionsDemo() {
            transferService.transfer("SENDER001", "RECEIVER001", 1000.0);
            
            // Verify expected calls
            verify(accountRepository).loadAccountById("SENDER001");
            verify(accountRepository).loadAccountById("RECEIVER001");
            verify(accountRepository, times(2)).saveAccount(any());
            
            // Verify nothing else was called
            verifyNoMoreInteractions(accountRepository);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // SPY DEMONSTRATION
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Spy (Partial Mock)")
    class SpyTests {

        /**
         * TEST: Using spy for partial mocking
         * 
         * Spy wraps a real object - calls real methods unless stubbed
         */
        @Test
        @DisplayName("Spy calls real methods by default")
        void spyCallsRealMethods() {
            // Create real account and spy on it
            Account realAccount = new Account("SPY001", "Spy User", 1000.0);
            Account spyAccount = spy(realAccount);

            // Act - Real method is called
            spyAccount.credit(500.0);

            // Assert - Real implementation executed
            assertEquals(1500.0, spyAccount.getBalance());
            
            // Verify call happened
            verify(spyAccount).credit(500.0);
        }

        /**
         * TEST: Override specific method on spy
         */
        @Test
        @DisplayName("Override specific method on spy")
        void overrideMethodOnSpy() {
            Account realAccount = new Account("SPY001", "Spy User", 1000.0);
            Account spyAccount = spy(realAccount);

            // Stub getBalance to return fake value
            // Note: Use doReturn for spies to avoid calling real method
            doReturn(9999.0).when(spyAccount).getBalance();

            // Assert - Stubbed method returns fake, others work normally
            assertEquals(9999.0, spyAccount.getBalance()); // Stubbed
            assertEquals("SPY001", spyAccount.getAccountId()); // Real
            assertEquals("Spy User", spyAccount.getAccountHolderName()); // Real
        }
    }
}
