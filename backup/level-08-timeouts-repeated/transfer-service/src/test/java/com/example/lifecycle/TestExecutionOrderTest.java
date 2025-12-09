package com.example.lifecycle;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Execution Order Demo - LEVEL 6
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                TEST EXECUTION ORDER STRATEGIES                ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  JUnit 5 supports multiple test ordering strategies:          ║
 * ║                                                               ║
 * ║  • OrderAnnotation - Explicit @Order(n) values                ║
 * ║  • MethodName - Alphabetical by method name                   ║
 * ║  • DisplayName - Alphabetical by @DisplayName                 ║
 * ║  • Random - Random order (finds hidden dependencies)          ║
 * ║  • Custom - Implement MethodOrderer interface                 ║
 * ║                                                               ║
 * ║  ⚠️ IMPORTANT: Tests should be independent!                   ║
 * ║     Ordering is for readability, not dependency management.   ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
class TestExecutionOrderTest {

    // ═══════════════════════════════════════════════════════════
    // ORDER BY @Order ANNOTATION
    // ═══════════════════════════════════════════════════════════

    @Nested
    @TestMethodOrder(OrderAnnotation.class)
    @DisplayName("Order by @Order Annotation")
    class OrderAnnotationTest {

        @Test
        @Order(3)
        @DisplayName("Third (Order 3)")
        void third() {
            System.out.println("  @Order(3) - Third");
        }

        @Test
        @Order(1)
        @DisplayName("First (Order 1)")
        void first() {
            System.out.println("  @Order(1) - First");
        }

        @Test
        @Order(2)
        @DisplayName("Second (Order 2)")
        void second() {
            System.out.println("  @Order(2) - Second");
        }

        @Test
        @Order(Integer.MAX_VALUE)
        @DisplayName("Last (MAX_VALUE)")
        void last() {
            System.out.println("  @Order(MAX_VALUE) - Last");
        }

        @Test
        @DisplayName("Default (no @Order)")
        void noOrder() {
            // Tests without @Order run after ordered tests
            System.out.println("  No @Order - Runs after ordered tests");
        }
    }

    // ═══════════════════════════════════════════════════════════
    // ORDER BY METHOD NAME
    // ═══════════════════════════════════════════════════════════

    @Nested
    @TestMethodOrder(MethodName.class)
    @DisplayName("Order by Method Name (Alphabetical)")
    class MethodNameOrderTest {

        @Test
        void zebra() {
            System.out.println("  zebra()");
        }

        @Test
        void apple() {
            System.out.println("  apple()");
        }

        @Test
        void mango() {
            System.out.println("  mango()");
        }

        @Test
        void banana() {
            System.out.println("  banana()");
        }

        // Output order: apple, banana, mango, zebra
    }

    // ═══════════════════════════════════════════════════════════
    // ORDER BY @DisplayName
    // ═══════════════════════════════════════════════════════════

    @Nested
    @TestMethodOrder(DisplayName.class)
    @DisplayName("Order by @DisplayName (Alphabetical)")
    class DisplayNameOrderTest {

        @Test
        @DisplayName("C - Third step")
        void methodZ() {
            System.out.println("  C - Third step");
        }

        @Test
        @DisplayName("A - First step")
        void methodY() {
            System.out.println("  A - First step");
        }

        @Test
        @DisplayName("B - Second step")
        void methodX() {
            System.out.println("  B - Second step");
        }

        // Output order: A, B, C (by display name, not method name)
    }

    // ═══════════════════════════════════════════════════════════
    // RANDOM ORDER (Good for finding dependencies)
    // ═══════════════════════════════════════════════════════════

    @Nested
    @TestMethodOrder(Random.class)
    @DisplayName("Random Order (Finds Hidden Dependencies)")
    class RandomOrderTest {

        /**
         * Random order is useful for:
         * - Finding hidden test dependencies
         * - Ensuring tests are truly isolated
         * - Catching shared state bugs
         * 
         * If tests fail only sometimes with random order,
         * they have hidden dependencies!
         */

        @Test
        void testA() {
            System.out.println("  testA - Random position");
            assertTrue(true);
        }

        @Test
        void testB() {
            System.out.println("  testB - Random position");
            assertTrue(true);
        }

        @Test
        void testC() {
            System.out.println("  testC - Random position");
            assertTrue(true);
        }

        @Test
        void testD() {
            System.out.println("  testD - Random position");
            assertTrue(true);
        }

        // Each run may have different order!
    }

    // ═══════════════════════════════════════════════════════════
    // PRACTICAL USE: WORKFLOW STEPS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @TestMethodOrder(OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("UPI Registration Workflow (Ordered Steps)")
    class UpiRegistrationWorkflowTest {

        private String phoneNumber;
        private String upiId;
        private String bankAccount;
        private boolean isVerified;
        private boolean isRegistered;

        @BeforeAll
        void initWorkflow() {
            System.out.println("\n[WORKFLOW] Starting UPI Registration");
            phoneNumber = "9876543210";
        }

        @Test
        @Order(1)
        @DisplayName("Step 1: Verify phone number")
        void step1_verifyPhone() {
            System.out.println("[WORKFLOW] Step 1: Verifying phone " + phoneNumber);
            
            // Simulate OTP verification
            isVerified = true;
            
            assertTrue(isVerified);
        }

        @Test
        @Order(2)
        @DisplayName("Step 2: Link bank account")
        void step2_linkBankAccount() {
            System.out.println("[WORKFLOW] Step 2: Linking bank account");
            
            assertTrue(isVerified, "Phone must be verified first");
            bankAccount = "XXXX1234";
            
            assertNotNull(bankAccount);
        }

        @Test
        @Order(3)
        @DisplayName("Step 3: Create UPI ID")
        void step3_createUpiId() {
            System.out.println("[WORKFLOW] Step 3: Creating UPI ID");
            
            assertNotNull(bankAccount, "Bank account must be linked first");
            upiId = phoneNumber + "@upi";
            
            assertEquals("9876543210@upi", upiId);
        }

        @Test
        @Order(4)
        @DisplayName("Step 4: Complete registration")
        void step4_completeRegistration() {
            System.out.println("[WORKFLOW] Step 4: Completing registration");
            
            assertNotNull(upiId, "UPI ID must be created first");
            isRegistered = true;
            
            assertTrue(isRegistered);
            System.out.println("[WORKFLOW] ✓ Registration complete for " + upiId);
        }

        @Test
        @Order(5)
        @DisplayName("Step 5: Verify final state")
        void step5_verifyFinalState() {
            System.out.println("[WORKFLOW] Step 5: Final verification");
            
            assertAll("Registration state",
                () -> assertTrue(isVerified, "Should be verified"),
                () -> assertNotNull(bankAccount, "Should have bank account"),
                () -> assertEquals("9876543210@upi", upiId, "Should have UPI ID"),
                () -> assertTrue(isRegistered, "Should be registered")
            );
        }
    }

    // ═══════════════════════════════════════════════════════════
    // WARNING: BAD PATTERN - DEPENDENT TESTS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("⚠️ Anti-Pattern: Dependent Tests (DON'T DO THIS)")
    class DependentTestsAntiPattern {

        /**
         * This demonstrates what NOT to do.
         * 
         * Tests that depend on each other are:
         * - Fragile (fail if order changes)
         * - Hard to debug
         * - Not independently runnable
         * 
         * Instead, each test should set up its own state.
         */

        // ❌ BAD: Static state shared between tests
        // private static Account sharedAccount;

        // ✅ GOOD: Each test creates its own account
        @Test
        @DisplayName("✅ GOOD: Independent test")
        void independentTest() {
            // Create everything needed for this test
            var account = BankingTestFixtures.sender();
            account.debit(1000.0);
            
            assertEquals(49000.0, account.getBalance());
        }

        @Test
        @DisplayName("✅ GOOD: Another independent test")
        void anotherIndependentTest() {
            // Also creates its own account - doesn't rely on previous test
            var account = BankingTestFixtures.sender();
            account.credit(5000.0);
            
            assertEquals(55000.0, account.getBalance());
        }
    }
}
