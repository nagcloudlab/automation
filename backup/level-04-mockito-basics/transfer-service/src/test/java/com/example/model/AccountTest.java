package com.example.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for Account Model - LEVEL 1
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    JUNIT 5 BASICS                             ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  This test class demonstrates:                                ║
 * ║  • @Test annotation                                           ║
 * ║  • @DisplayName for readable reports                          ║
 * ║  • Basic assertions (assertEquals, assertTrue, etc.)          ║
 * ║  • Test lifecycle (@BeforeEach, @AfterEach)                   ║
 * ║  • AAA Pattern (Arrange-Act-Assert)                           ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * BANKING CONTEXT:
 * ================
 * Account is the core model in UPI transactions.
 * These tests verify:
 * - Account creation with valid data
 * - Balance operations (debit/credit)
 * - Data integrity
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Account Model - Basic Tests")
class AccountTest {

    // ═══════════════════════════════════════════════════════════
    // TEST FIXTURES (shared test data)
    // ═══════════════════════════════════════════════════════════
    
    private Account account;
    
    // Sample test data - typical NPCI account values
    private static final String TEST_ACCOUNT_ID = "ACC001";
    private static final String TEST_HOLDER_NAME = "Rajesh Kumar";
    private static final double TEST_INITIAL_BALANCE = 10000.0;

    // ═══════════════════════════════════════════════════════════
    // LIFECYCLE METHODS
    // ═══════════════════════════════════════════════════════════

    /**
     * Runs ONCE before all tests in this class.
     * Use for expensive setup that can be shared.
     */
    @BeforeAll
    static void initializeTestSuite() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("  STARTING: Account Model Test Suite");
        System.out.println("═".repeat(60));
    }

    /**
     * Runs BEFORE each test method.
     * Creates a fresh Account instance for each test.
     * 
     * WHY? Tests should be independent. Each test gets its own account.
     */
    @BeforeEach
    void setUp() {
        account = new Account(TEST_ACCOUNT_ID, TEST_HOLDER_NAME, TEST_INITIAL_BALANCE);
        System.out.println("\n[SETUP] Created test account: " + account.getAccountId());
    }

    /**
     * Runs AFTER each test method.
     * Good for cleanup, logging, releasing resources.
     */
    @AfterEach
    void tearDown() {
        System.out.println("[TEARDOWN] Test completed for: " + account.getAccountId());
        account = null;
    }

    /**
     * Runs ONCE after all tests in this class.
     */
    @AfterAll
    static void completeTestSuite() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("  COMPLETED: Account Model Test Suite");
        System.out.println("═".repeat(60) + "\n");
    }

    // ═══════════════════════════════════════════════════════════
    // TEST 1: ACCOUNT CREATION
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Verify account is created with correct initial values.
     * 
     * DEMONSTRATES:
     * - @Test annotation marks this as a test method
     * - @DisplayName provides human-readable name in reports
     * - assertEquals for checking values
     * - Testing constructor behavior
     */
    @Test
    @DisplayName("Should create account with correct initial values")
    void shouldCreateAccountWithInitialValues() {
        // ─────────────────────────────────────────────────────────
        // ARRANGE - Account already created in @BeforeEach
        // ─────────────────────────────────────────────────────────
        // (Using the account from setUp())

        // ─────────────────────────────────────────────────────────
        // ACT - Nothing to do, testing constructor output
        // ─────────────────────────────────────────────────────────

        // ─────────────────────────────────────────────────────────
        // ASSERT - Verify all properties are set correctly
        // ─────────────────────────────────────────────────────────
        assertEquals(TEST_ACCOUNT_ID, account.getAccountId(), 
            "Account ID should match");
        assertEquals(TEST_HOLDER_NAME, account.getAccountHolderName(), 
            "Account holder name should match");
        assertEquals(TEST_INITIAL_BALANCE, account.getBalance(), 
            "Initial balance should match");
    }

    /**
     * TEST: Verify account can be created with zero balance.
     * 
     * BANKING CONTEXT: New accounts often start with ₹0 balance.
     */
    @Test
    @DisplayName("Should create account with zero balance")
    void shouldCreateAccountWithZeroBalance() {
        // Arrange & Act - Create account with zero balance
        Account zeroBalanceAccount = new Account("ACC999", "New Customer", 0.0);

        // Assert
        assertEquals(0.0, zeroBalanceAccount.getBalance(), 
            "New account should have zero balance");
    }

    // ═══════════════════════════════════════════════════════════
    // TEST 2: DEBIT OPERATIONS (Withdrawals)
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Verify debit reduces balance correctly.
     * 
     * DEMONSTRATES:
     * - AAA Pattern clearly
     * - Simple arithmetic verification
     * 
     * SCENARIO: Debit ₹3,000 from ₹10,000 balance
     * EXPECTED: Balance becomes ₹7,000
     */
    @Test
    @DisplayName("Should debit ₹3,000 from ₹10,000 balance leaving ₹7,000")
    void shouldDebitAmountFromAccount() {
        // Arrange
        double debitAmount = 3000.0;
        double expectedBalance = 7000.0;

        // Act
        account.debit(debitAmount);

        // Assert
        assertEquals(expectedBalance, account.getBalance(), 
            "Balance should be reduced by debit amount");
    }

    /**
     * TEST: Verify multiple debits work correctly.
     * 
     * SCENARIO: Multiple UPI payments from same account
     */
    @Test
    @DisplayName("Should handle multiple sequential debits correctly")
    void shouldHandleMultipleDebits() {
        // Arrange
        double firstDebit = 2000.0;
        double secondDebit = 3000.0;
        double thirdDebit = 1000.0;
        double expectedFinalBalance = 4000.0; // 10000 - 2000 - 3000 - 1000

        // Act
        account.debit(firstDebit);
        account.debit(secondDebit);
        account.debit(thirdDebit);

        // Assert
        assertEquals(expectedFinalBalance, account.getBalance(),
            "Balance should reflect all debits");
    }

    /**
     * TEST: Verify debit of exact balance leaves zero.
     * 
     * EDGE CASE: Customer withdraws entire balance
     */
    @Test
    @DisplayName("Should allow debiting exact balance leaving ₹0")
    void shouldAllowDebitingExactBalance() {
        // Arrange
        double exactBalance = account.getBalance();

        // Act
        account.debit(exactBalance);

        // Assert
        assertEquals(0.0, account.getBalance(), 
            "Balance should be exactly zero after full debit");
    }

    // ═══════════════════════════════════════════════════════════
    // TEST 3: CREDIT OPERATIONS (Deposits)
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Verify credit increases balance correctly.
     * 
     * SCENARIO: Receive ₹5,000 UPI payment
     */
    @Test
    @DisplayName("Should credit ₹5,000 to ₹10,000 balance making ₹15,000")
    void shouldCreditAmountToAccount() {
        // Arrange
        double creditAmount = 5000.0;
        double expectedBalance = 15000.0;

        // Act
        account.credit(creditAmount);

        // Assert
        assertEquals(expectedBalance, account.getBalance(),
            "Balance should increase by credit amount");
    }

    /**
     * TEST: Verify credit to zero balance account.
     * 
     * SCENARIO: First salary credit to new account
     */
    @Test
    @DisplayName("Should credit amount to zero balance account")
    void shouldCreditToZeroBalanceAccount() {
        // Arrange
        Account emptyAccount = new Account("ACC888", "Fresh Account", 0.0);
        double creditAmount = 25000.0;

        // Act
        emptyAccount.credit(creditAmount);

        // Assert
        assertEquals(creditAmount, emptyAccount.getBalance(),
            "Credit to empty account should set balance equal to credit");
    }

    /**
     * TEST: Verify multiple credits work correctly.
     * 
     * SCENARIO: Multiple incoming UPI payments
     */
    @Test
    @DisplayName("Should handle multiple sequential credits correctly")
    void shouldHandleMultipleCredits() {
        // Arrange
        double credit1 = 5000.0;
        double credit2 = 3000.0;
        double credit3 = 2000.0;
        double expectedBalance = 20000.0; // 10000 + 5000 + 3000 + 2000

        // Act
        account.credit(credit1);
        account.credit(credit2);
        account.credit(credit3);

        // Assert
        assertEquals(expectedBalance, account.getBalance(),
            "Balance should reflect all credits");
    }

    // ═══════════════════════════════════════════════════════════
    // TEST 4: MIXED OPERATIONS
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Verify mixed debit and credit operations.
     * 
     * SCENARIO: Real-world usage pattern
     * - Receive payment (+₹5,000)
     * - Pay bill (-₹3,000)
     * - Receive refund (+₹500)
     */
    @Test
    @DisplayName("Should handle mixed debit and credit operations")
    void shouldHandleMixedOperations() {
        // Arrange
        double initialBalance = account.getBalance(); // 10,000

        // Act
        account.credit(5000.0);  // +5,000 → 15,000
        account.debit(3000.0);   // -3,000 → 12,000
        account.credit(500.0);   // +500   → 12,500

        // Assert
        double expectedBalance = initialBalance + 5000.0 - 3000.0 + 500.0;
        assertEquals(expectedBalance, account.getBalance(),
            "Balance should reflect all mixed operations");
        assertEquals(12500.0, account.getBalance());
    }

    // ═══════════════════════════════════════════════════════════
    // TEST 5: OBJECT METHODS (equals, hashCode, toString)
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Verify toString returns meaningful representation.
     */
    @Test
    @DisplayName("Should return meaningful toString representation")
    void shouldReturnMeaningfulToString() {
        // Act
        String result = account.toString();

        // Assert
        assertNotNull(result, "toString should not return null");
        assertTrue(result.contains(TEST_ACCOUNT_ID), 
            "toString should contain account ID");
        assertTrue(result.contains(TEST_HOLDER_NAME), 
            "toString should contain holder name");
    }

    /**
     * TEST: Verify equals for same account data.
     */
    @Test
    @DisplayName("Should consider accounts with same data as equal")
    void shouldBeEqualForSameData() {
        // Arrange
        Account sameAccount = new Account(TEST_ACCOUNT_ID, TEST_HOLDER_NAME, TEST_INITIAL_BALANCE);

        // Assert
        assertEquals(account, sameAccount, 
            "Accounts with same data should be equal");
    }

    /**
     * TEST: Verify equals for different accounts.
     */
    @Test
    @DisplayName("Should not be equal for different account IDs")
    void shouldNotBeEqualForDifferentIds() {
        // Arrange
        Account differentAccount = new Account("ACC002", TEST_HOLDER_NAME, TEST_INITIAL_BALANCE);

        // Assert
        assertNotEquals(account, differentAccount,
            "Accounts with different IDs should not be equal");
    }

    /**
     * TEST: Verify hashCode consistency.
     */
    @Test
    @DisplayName("Should have consistent hashCode for equal objects")
    void shouldHaveConsistentHashCode() {
        // Arrange
        Account sameAccount = new Account(TEST_ACCOUNT_ID, TEST_HOLDER_NAME, TEST_INITIAL_BALANCE);

        // Assert
        assertEquals(account.hashCode(), sameAccount.hashCode(),
            "Equal objects should have same hashCode");
    }

    // ═══════════════════════════════════════════════════════════
    // TEST 6: ASSERTION VARIATIONS (Teaching different assertions)
    // ═══════════════════════════════════════════════════════════

    /**
     * TEST: Demonstrate various assertion types.
     * 
     * TEACHING PURPOSE: Shows different assertions available in JUnit 5
     */
    @Test
    @DisplayName("Demonstrates various JUnit 5 assertions")
    void demonstrateVariousAssertions() {
        // assertNotNull - verify object exists
        assertNotNull(account, "Account should not be null");
        assertNotNull(account.getAccountId(), "Account ID should not be null");
        assertNotNull(account.getAccountHolderName(), "Holder name should not be null");

        // assertTrue / assertFalse - verify conditions
        assertTrue(account.getBalance() > 0, "Balance should be positive");
        assertTrue(account.getBalance() >= TEST_INITIAL_BALANCE, 
            "Balance should be at least initial balance");
        assertFalse(account.getBalance() < 0, "Balance should not be negative");

        // assertEquals with delta for floating point
        assertEquals(10000.0, account.getBalance(), 0.001, 
            "Balance should be approximately 10000");

        // assertSame / assertNotSame - reference equality
        Account sameReference = account;
        assertSame(account, sameReference, "Should be same object reference");
        
        Account differentObject = new Account(TEST_ACCOUNT_ID, TEST_HOLDER_NAME, TEST_INITIAL_BALANCE);
        assertNotSame(account, differentObject, "Should be different object references");
    }

    /**
     * TEST: Demonstrate assertAll for grouped assertions.
     * 
     * BENEFIT: All assertions run even if one fails,
     * giving complete picture of failures.
     */
    @Test
    @DisplayName("Demonstrates assertAll for grouped assertions")
    void demonstrateGroupedAssertions() {
        assertAll("Account properties",
            () -> assertEquals(TEST_ACCOUNT_ID, account.getAccountId()),
            () -> assertEquals(TEST_HOLDER_NAME, account.getAccountHolderName()),
            () -> assertEquals(TEST_INITIAL_BALANCE, account.getBalance()),
            () -> assertNotNull(account.toString())
        );
    }
}
