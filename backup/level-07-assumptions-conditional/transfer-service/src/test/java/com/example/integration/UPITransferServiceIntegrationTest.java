package com.example.integration;

import com.example.exception.*;
import com.example.model.Account;
import com.example.repository.InMemoryAccountRepository;
import com.example.service.UPITransferService;
import com.example.service.UPITransferService.TransferResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UPI Transfer Service Integration Tests - LEVEL 5
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    INTEGRATION TESTS                          ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  KEY DIFFERENCE FROM UNIT TESTS (Level 4):                    ║
 * ║                                                               ║
 * ║  Unit Tests:        Integration Tests:                        ║
 * ║  ────────────       ─────────────────                         ║
 * ║  @Mock Repository   Real Repository                           ║
 * ║  when().thenReturn  repository.addAccount()                   ║
 * ║  verify()           Assert persisted state                    ║
 * ║  Isolated           Tests real interactions                   ║
 * ║                                                               ║
 * ║  NO MOCKS! All components are real implementations.           ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("UPI Transfer Service - Integration Tests")
class UPITransferServiceIntegrationTest {

    // ═══════════════════════════════════════════════════════════
    // REAL IMPLEMENTATIONS - NO MOCKS!
    // ═══════════════════════════════════════════════════════════

    /**
     * Real repository implementation (not a mock).
     * Uses InMemoryAccountRepository for clean, controllable tests.
     */
    private InMemoryAccountRepository repository;

    /**
     * Real service - injected with real repository.
     */
    private UPITransferService transferService;

    // ═══════════════════════════════════════════════════════════
    // TEST SETUP
    // ═══════════════════════════════════════════════════════════

    @BeforeEach
    void setUp() {
        // Create fresh repository for each test (test isolation)
        repository = new InMemoryAccountRepository();
        
        // Create service with real repository
        transferService = new UPITransferService(repository);
    }

    // ═══════════════════════════════════════════════════════════
    // SUCCESSFUL TRANSFER INTEGRATION TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Successful Transfer Integration Tests")
    class SuccessfulTransferTests {

        @Test
        @DisplayName("Should transfer and persist balances correctly")
        void shouldTransferAndPersistBalances() {
            // ═══════════════════════════════════════════════
            // ARRANGE - Add accounts to real repository
            // ═══════════════════════════════════════════════
            repository.addAccount(new Account("SENDER001", "Rajesh Kumar", 10000.0));
            repository.addAccount(new Account("RECEIVER001", "Priya Sharma", 5000.0));

            // ═══════════════════════════════════════════════
            // ACT - Execute real transfer
            // ═══════════════════════════════════════════════
            TransferResult result = transferService.transfer(
                "SENDER001", "RECEIVER001", 2500.0
            );

            // ═══════════════════════════════════════════════
            // ASSERT - Verify both result AND persisted state
            // ═══════════════════════════════════════════════
            
            // 1. Verify transfer result
            assertNotNull(result);
            assertEquals(2500.0, result.getAmount());
            assertEquals(10000.0, result.getSenderBalanceBefore());
            assertEquals(7500.0, result.getSenderBalanceAfter());
            assertEquals(5000.0, result.getReceiverBalanceBefore());
            assertEquals(7500.0, result.getReceiverBalanceAfter());

            // 2. Verify PERSISTED state in repository
            Account sender = repository.loadAccountById("SENDER001");
            Account receiver = repository.loadAccountById("RECEIVER001");
            
            assertEquals(7500.0, sender.getBalance(), 
                "Sender balance should be persisted");
            assertEquals(7500.0, receiver.getBalance(), 
                "Receiver balance should be persisted");
        }

        @Test
        @DisplayName("Should maintain total balance conservation")
        void shouldMaintainTotalBalanceConservation() {
            // Arrange
            repository.addAccount(new Account("A", "User A", 10000.0));
            repository.addAccount(new Account("B", "User B", 10000.0));
            
            double totalBefore = repository.getTotalBalance();

            // Act
            transferService.transfer("A", "B", 3000.0);

            // Assert - Total unchanged
            double totalAfter = repository.getTotalBalance();
            assertEquals(totalBefore, totalAfter, 0.001,
                "Total balance should be conserved after transfer");
        }

        @Test
        @DisplayName("Should handle minimum UPI amount (₹1)")
        void shouldHandleMinimumAmount() {
            // Arrange
            repository.addAccount(new Account("S", "Sender", 1000.0));
            repository.addAccount(new Account("R", "Receiver", 1000.0));

            // Act
            TransferResult result = transferService.transfer("S", "R", 1.0);

            // Assert
            assertEquals(999.0, repository.loadAccountById("S").getBalance());
            assertEquals(1001.0, repository.loadAccountById("R").getBalance());
        }

        @Test
        @DisplayName("Should handle maximum UPI amount (₹1,00,000)")
        void shouldHandleMaximumAmount() {
            // Arrange - Need sufficient balance
            repository.addAccount(new Account("RICH", "Rich User", 500000.0));
            repository.addAccount(new Account("LUCKY", "Lucky User", 0.0));

            // Act
            transferService.transfer("RICH", "LUCKY", 100000.0);

            // Assert
            assertEquals(400000.0, repository.loadAccountById("RICH").getBalance());
            assertEquals(100000.0, repository.loadAccountById("LUCKY").getBalance());
        }

        @Test
        @DisplayName("Should handle exact balance transfer")
        void shouldHandleExactBalanceTransfer() {
            // Arrange
            repository.addAccount(new Account("FULL", "Full Transfer", 5000.0));
            repository.addAccount(new Account("RECV", "Receiver", 0.0));

            // Act
            transferService.transfer("FULL", "RECV", 5000.0);

            // Assert
            assertEquals(0.0, repository.loadAccountById("FULL").getBalance());
            assertEquals(5000.0, repository.loadAccountById("RECV").getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // MULTIPLE TRANSFER TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Multiple Transfer Tests")
    class MultipleTransferTests {

        @Test
        @DisplayName("Should handle consecutive transfers correctly")
        void shouldHandleConsecutiveTransfers() {
            // Arrange
            repository.addAccount(new Account("A", "User A", 10000.0));
            repository.addAccount(new Account("B", "User B", 5000.0));

            // Act - Multiple transfers
            transferService.transfer("A", "B", 1000.0);  // A: 9000, B: 6000
            transferService.transfer("A", "B", 2000.0);  // A: 7000, B: 8000
            transferService.transfer("B", "A", 500.0);   // B: 7500, A: 7500

            // Assert
            assertEquals(7500.0, repository.loadAccountById("A").getBalance());
            assertEquals(7500.0, repository.loadAccountById("B").getBalance());
        }

        @Test
        @DisplayName("Should handle transfer chain (A → B → C)")
        void shouldHandleTransferChain() {
            // Arrange
            repository.addAccount(new Account("A", "Alice", 15000.0));
            repository.addAccount(new Account("B", "Bob", 5000.0));
            repository.addAccount(new Account("C", "Charlie", 10000.0));

            double totalBefore = repository.getTotalBalance();  // 30000

            // Act - Chain transfer
            transferService.transfer("A", "B", 5000.0);   // A: 10000, B: 10000
            transferService.transfer("B", "C", 7000.0);   // B: 3000, C: 17000
            transferService.transfer("C", "A", 2000.0);   // C: 15000, A: 12000

            // Assert
            assertEquals(12000.0, repository.loadAccountById("A").getBalance());
            assertEquals(3000.0, repository.loadAccountById("B").getBalance());
            assertEquals(15000.0, repository.loadAccountById("C").getBalance());

            // Total should be conserved
            assertEquals(totalBefore, repository.getTotalBalance(), 0.001);
        }

        @Test
        @DisplayName("Should handle circular transfers")
        void shouldHandleCircularTransfers() {
            // Arrange - All start with 10000
            repository.addAccount(new Account("X", "X", 10000.0));
            repository.addAccount(new Account("Y", "Y", 10000.0));
            repository.addAccount(new Account("Z", "Z", 10000.0));

            // Act - Circular: X → Y → Z → X (same amount)
            transferService.transfer("X", "Y", 3000.0);
            transferService.transfer("Y", "Z", 3000.0);
            transferService.transfer("Z", "X", 3000.0);

            // Assert - All should still have 10000
            assertEquals(10000.0, repository.loadAccountById("X").getBalance());
            assertEquals(10000.0, repository.loadAccountById("Y").getBalance());
            assertEquals(10000.0, repository.loadAccountById("Z").getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ERROR HANDLING INTEGRATION TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Error Handling Integration Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should not modify any balance when transfer fails")
        void shouldNotModifyBalancesOnFailure() {
            // Arrange
            repository.addAccount(new Account("POOR", "Poor User", 100.0));
            repository.addAccount(new Account("RICH", "Rich User", 50000.0));

            // Act - Attempt transfer exceeding balance
            assertThrows(InsufficientBalanceException.class,
                () -> transferService.transfer("POOR", "RICH", 5000.0));

            // Assert - BOTH accounts unchanged
            assertEquals(100.0, repository.loadAccountById("POOR").getBalance(),
                "Sender balance should be unchanged");
            assertEquals(50000.0, repository.loadAccountById("RICH").getBalance(),
                "Receiver balance should be unchanged");
        }

        @Test
        @DisplayName("Should handle sender account not found")
        void shouldHandleSenderNotFound() {
            // Arrange - Only add receiver
            repository.addAccount(new Account("RECEIVER", "Receiver", 5000.0));

            // Act & Assert
            AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> transferService.transfer("UNKNOWN", "RECEIVER", 1000.0)
            );

            assertEquals("UNKNOWN", exception.getAccountId());
            
            // Receiver unchanged
            assertEquals(5000.0, repository.loadAccountById("RECEIVER").getBalance());
        }

        @Test
        @DisplayName("Should handle receiver account not found")
        void shouldHandleReceiverNotFound() {
            // Arrange - Only add sender
            repository.addAccount(new Account("SENDER", "Sender", 10000.0));

            // Act & Assert
            AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> transferService.transfer("SENDER", "UNKNOWN", 1000.0)
            );

            assertEquals("UNKNOWN", exception.getAccountId());
            
            // Sender unchanged
            assertEquals(10000.0, repository.loadAccountById("SENDER").getBalance());
        }

        @Test
        @DisplayName("Should handle same account transfer")
        void shouldHandleSameAccountTransfer() {
            // Arrange
            repository.addAccount(new Account("SAME", "Same User", 10000.0));

            // Act & Assert
            SameAccountTransferException exception = assertThrows(
                SameAccountTransferException.class,
                () -> transferService.transfer("SAME", "SAME", 1000.0)
            );

            assertEquals("SAME", exception.getAccountId());
            assertEquals("U16", exception.getErrorCode());
            
            // Balance unchanged
            assertEquals(10000.0, repository.loadAccountById("SAME").getBalance());
        }

        @Test
        @DisplayName("Failed transfer should not affect subsequent transfers")
        void shouldNotAffectSubsequentTransfers() {
            // Arrange
            repository.addAccount(new Account("A", "A", 1000.0));
            repository.addAccount(new Account("B", "B", 1000.0));

            // Act - First transfer fails
            assertThrows(InsufficientBalanceException.class,
                () -> transferService.transfer("A", "B", 5000.0));

            // Second transfer succeeds
            transferService.transfer("A", "B", 500.0);

            // Assert
            assertEquals(500.0, repository.loadAccountById("A").getBalance());
            assertEquals(1500.0, repository.loadAccountById("B").getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // UPI ID INTEGRATION TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("UPI ID Transfer Integration Tests")
    class UpiIdTransferTests {

        @Test
        @DisplayName("Should transfer using UPI IDs")
        void shouldTransferUsingUpiIds() {
            // Arrange
            repository.addAccount(new Account("ACC001", "Rajesh", 10000.0));
            repository.addAccount(new Account("ACC002", "Priya", 5000.0));
            repository.addUpiMapping("rajesh@upi", "ACC001");
            repository.addUpiMapping("priya@upi", "ACC002");

            // Act
            TransferResult result = transferService.transferByUpiId(
                "rajesh@upi", "priya@upi", 2000.0
            );

            // Assert
            assertEquals(8000.0, result.getSenderBalanceAfter());
            assertEquals(7000.0, result.getReceiverBalanceAfter());
            
            // Verify persisted
            assertEquals(8000.0, repository.loadAccountById("ACC001").getBalance());
            assertEquals(7000.0, repository.loadAccountById("ACC002").getBalance());
        }

        @Test
        @DisplayName("Should handle case-insensitive UPI IDs")
        void shouldHandleCaseInsensitiveUpiIds() {
            // Arrange
            repository.addAccount(new Account("ACC001", "User", 10000.0));
            repository.addAccount(new Account("ACC002", "User", 5000.0));
            repository.addUpiMapping("user@upi", "ACC001");
            repository.addUpiMapping("user2@upi", "ACC002");

            // Act - Use different case
            TransferResult result = transferService.transferByUpiId(
                "USER@UPI", "User2@UPI", 1000.0
            );

            // Assert
            assertEquals(9000.0, result.getSenderBalanceAfter());
        }

        @Test
        @DisplayName("Should throw for unknown UPI ID")
        void shouldThrowForUnknownUpiId() {
            // Arrange
            repository.addAccount(new Account("ACC001", "User", 10000.0));
            repository.addUpiMapping("known@upi", "ACC001");

            // Act & Assert
            AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> transferService.transferByUpiId("unknown@upi", "known@upi", 1000.0)
            );

            assertTrue(exception.isUpiIdLookup());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // BALANCE INQUIRY INTEGRATION TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Balance Inquiry Integration Tests")
    class BalanceInquiryTests {

        @Test
        @DisplayName("Should get account balance")
        void shouldGetAccountBalance() {
            // Arrange
            repository.addAccount(new Account("ACC001", "User", 12345.67));

            // Act
            double balance = transferService.getBalance("ACC001");

            // Assert
            assertEquals(12345.67, balance, 0.001);
        }

        @Test
        @DisplayName("Should reflect balance after transfer")
        void shouldReflectBalanceAfterTransfer() {
            // Arrange
            repository.addAccount(new Account("A", "A", 10000.0));
            repository.addAccount(new Account("B", "B", 5000.0));

            // Act
            transferService.transfer("A", "B", 3000.0);

            // Assert
            assertEquals(7000.0, transferService.getBalance("A"));
            assertEquals(8000.0, transferService.getBalance("B"));
        }

        @Test
        @DisplayName("Should check account existence")
        void shouldCheckAccountExistence() {
            // Arrange
            repository.addAccount(new Account("EXISTS", "Exists", 1000.0));

            // Assert
            assertTrue(transferService.accountExists("EXISTS"));
            assertFalse(transferService.accountExists("NOT_EXISTS"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // PRECISION TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Precision and Edge Case Tests")
    class PrecisionTests {

        @Test
        @DisplayName("Should handle paisa-level precision")
        void shouldHandlePaisaPrecision() {
            // Arrange
            repository.addAccount(new Account("S", "S", 100.50));
            repository.addAccount(new Account("R", "R", 50.25));

            // Act
            transferService.transfer("S", "R", 25.75);

            // Assert
            assertEquals(74.75, repository.loadAccountById("S").getBalance(), 0.001);
            assertEquals(76.00, repository.loadAccountById("R").getBalance(), 0.001);
        }

        @Test
        @DisplayName("Should handle small amounts correctly")
        void shouldHandleSmallAmounts() {
            // Arrange
            repository.addAccount(new Account("S", "S", 10.00));
            repository.addAccount(new Account("R", "R", 0.00));

            // Act - Transfer ₹1 (minimum UPI)
            transferService.transfer("S", "R", 1.0);

            // Assert
            assertEquals(9.0, repository.loadAccountById("S").getBalance());
            assertEquals(1.0, repository.loadAccountById("R").getBalance());
        }
    }
}
