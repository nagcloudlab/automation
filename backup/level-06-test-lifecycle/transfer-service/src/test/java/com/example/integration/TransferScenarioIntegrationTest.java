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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Real-World Transfer Scenario Integration Tests.
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║              BANKING SCENARIO INTEGRATION TESTS               ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Tests real-world banking scenarios that occur in NPCI's      ║
 * ║  UPI ecosystem:                                               ║
 * ║                                                               ║
 * ║  • Salary disbursement (one-to-many)                          ║
 * ║  • Bill collection (many-to-one)                              ║
 * ║  • P2P transfers (peer-to-peer)                               ║
 * ║  • Split bills (group payments)                               ║
 * ║  • Merchant payments                                          ║
 * ║  • Refund scenarios                                           ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
@DisplayName("Banking Scenario Integration Tests")
class TransferScenarioIntegrationTest {

    private InMemoryAccountRepository repository;
    private UPITransferService transferService;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        transferService = new UPITransferService(repository);
        TestAccountFactory.resetCounter();
    }

    // ═══════════════════════════════════════════════════════════
    // SALARY DISBURSEMENT SCENARIO
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Salary Disbursement Scenario (One-to-Many)")
    class SalaryDisbursementTests {

        @Test
        @DisplayName("Should disburse salary to multiple employees")
        void shouldDisburseSalaryToEmployees() {
            // Arrange - Company account and 5 employees
            Account company = new Account("COMPANY", "TechCorp India", 500000.0);
            repository.addAccount(company);

            List<Account> employees = new ArrayList<>();
            double[] salaries = {50000.0, 45000.0, 55000.0, 40000.0, 60000.0};
            
            for (int i = 0; i < 5; i++) {
                Account emp = new Account("EMP00" + (i + 1), "Employee " + (i + 1), 10000.0);
                employees.add(emp);
                repository.addAccount(emp);
            }

            double totalSalary = 0;
            for (double sal : salaries) totalSalary += sal;

            // Act - Disburse salaries
            for (int i = 0; i < employees.size(); i++) {
                transferService.transfer("COMPANY", employees.get(i).getAccountId(), salaries[i]);
            }

            // Assert
            // Company balance reduced
            assertEquals(500000.0 - totalSalary, 
                repository.loadAccountById("COMPANY").getBalance());

            // Each employee received correct salary
            for (int i = 0; i < employees.size(); i++) {
                assertEquals(10000.0 + salaries[i],
                    repository.loadAccountById(employees.get(i).getAccountId()).getBalance(),
                    "Employee " + (i + 1) + " should receive ₹" + salaries[i]);
            }
        }

        @Test
        @DisplayName("Should stop disbursement when company funds exhausted")
        void shouldStopWhenFundsExhausted() {
            // Arrange - Company with limited funds
            repository.addAccount(new Account("COMPANY", "SmallCorp", 100000.0));
            repository.addAccount(new Account("EMP001", "Employee 1", 0.0));
            repository.addAccount(new Account("EMP002", "Employee 2", 0.0));
            repository.addAccount(new Account("EMP003", "Employee 3", 0.0));

            // Act - First two succeed, third fails
            transferService.transfer("COMPANY", "EMP001", 50000.0);  // Success
            transferService.transfer("COMPANY", "EMP002", 40000.0);  // Success

            // Third should fail (only 10000 remaining)
            assertThrows(InsufficientBalanceException.class,
                () -> transferService.transfer("COMPANY", "EMP003", 50000.0));

            // Assert - First two got paid, third didn't
            assertEquals(50000.0, repository.loadAccountById("EMP001").getBalance());
            assertEquals(40000.0, repository.loadAccountById("EMP002").getBalance());
            assertEquals(0.0, repository.loadAccountById("EMP003").getBalance());
            assertEquals(10000.0, repository.loadAccountById("COMPANY").getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // BILL COLLECTION SCENARIO
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Bill Collection Scenario (Many-to-One)")
    class BillCollectionTests {

        @Test
        @DisplayName("Should collect electricity bills from multiple customers")
        void shouldCollectElectricityBills() {
            // Arrange - Utility company and customers
            repository.addAccount(new Account("ELECTRIC_CO", "City Power Ltd", 0.0));
            
            String[] customers = {"CUST001", "CUST002", "CUST003", "CUST004"};
            double[] bills = {1500.0, 2200.0, 800.0, 3500.0};
            
            for (String cust : customers) {
                repository.addAccount(new Account(cust, cust, 10000.0));
            }

            // Act - Collect bills
            double totalCollected = 0;
            for (int i = 0; i < customers.length; i++) {
                transferService.transfer(customers[i], "ELECTRIC_CO", bills[i]);
                totalCollected += bills[i];
            }

            // Assert
            assertEquals(totalCollected, 
                repository.loadAccountById("ELECTRIC_CO").getBalance());
            
            // Each customer balance reduced
            for (int i = 0; i < customers.length; i++) {
                assertEquals(10000.0 - bills[i],
                    repository.loadAccountById(customers[i]).getBalance());
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // SPLIT BILL SCENARIO
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Split Bill Scenario")
    class SplitBillTests {

        @Test
        @DisplayName("Should split restaurant bill equally among friends")
        void shouldSplitBillEqually() {
            // Arrange - 4 friends, one paid the bill
            repository.addAccount(new Account("PAYER", "Rajesh (Paid)", 5000.0));
            repository.addAccount(new Account("FRIEND1", "Amit", 5000.0));
            repository.addAccount(new Account("FRIEND2", "Priya", 5000.0));
            repository.addAccount(new Account("FRIEND3", "Sunita", 5000.0));

            // Bill was ₹2000, split 4 ways = ₹500 each
            double totalBill = 2000.0;
            double perPerson = totalBill / 4;  // ₹500

            // Act - 3 friends pay the payer their share
            transferService.transfer("FRIEND1", "PAYER", perPerson);
            transferService.transfer("FRIEND2", "PAYER", perPerson);
            transferService.transfer("FRIEND3", "PAYER", perPerson);

            // Assert
            // Payer: 5000 - 500 (own share, already paid) + 1500 (from friends) = 6500
            // But payer didn't pay themselves, so: 5000 + 1500 = 6500
            assertEquals(6500.0, repository.loadAccountById("PAYER").getBalance());
            
            // Each friend: 5000 - 500 = 4500
            assertEquals(4500.0, repository.loadAccountById("FRIEND1").getBalance());
            assertEquals(4500.0, repository.loadAccountById("FRIEND2").getBalance());
            assertEquals(4500.0, repository.loadAccountById("FRIEND3").getBalance());
        }

        @Test
        @DisplayName("Should handle unequal split")
        void shouldHandleUnequalSplit() {
            // Arrange
            repository.addAccount(new Account("ORGANIZER", "Rajesh", 10000.0));
            repository.addAccount(new Account("P1", "Person 1", 5000.0));
            repository.addAccount(new Account("P2", "Person 2", 5000.0));
            repository.addAccount(new Account("P3", "Person 3", 5000.0));

            // Unequal split: P1 owes 400, P2 owes 600, P3 owes 500
            transferService.transfer("P1", "ORGANIZER", 400.0);
            transferService.transfer("P2", "ORGANIZER", 600.0);
            transferService.transfer("P3", "ORGANIZER", 500.0);

            // Assert
            assertEquals(11500.0, repository.loadAccountById("ORGANIZER").getBalance());
            assertEquals(4600.0, repository.loadAccountById("P1").getBalance());
            assertEquals(4400.0, repository.loadAccountById("P2").getBalance());
            assertEquals(4500.0, repository.loadAccountById("P3").getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // MERCHANT PAYMENT SCENARIO
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Merchant Payment Scenario")
    class MerchantPaymentTests {

        @Test
        @DisplayName("Should handle high-volume merchant payments")
        void shouldHandleHighVolumeMerchantPayments() {
            // Arrange - Merchant (SuperMart) and 10 customers
            repository.addAccount(new Account("SUPERMART", "SuperMart Store", 0.0));

            int numCustomers = 10;
            double[] purchases = {150.0, 2500.0, 899.0, 50.0, 1200.0, 
                                  3500.0, 750.0, 299.0, 1800.0, 499.0};

            for (int i = 0; i < numCustomers; i++) {
                repository.addAccount(new Account("CUST" + i, "Customer " + i, 10000.0));
            }

            // Act - All customers make purchases
            double expectedMerchantBalance = 0;
            for (int i = 0; i < numCustomers; i++) {
                transferService.transfer("CUST" + i, "SUPERMART", purchases[i]);
                expectedMerchantBalance += purchases[i];
            }

            // Assert
            assertEquals(expectedMerchantBalance, 
                repository.loadAccountById("SUPERMART").getBalance());
        }

        @ParameterizedTest(name = "Purchase ₹{0} should succeed")
        @ValueSource(doubles = {10.0, 99.0, 199.0, 499.0, 999.0, 2499.0, 4999.0})
        @DisplayName("Should handle common purchase amounts")
        void shouldHandleCommonPurchaseAmounts(double amount) {
            // Arrange
            repository.addAccount(new Account("BUYER", "Buyer", 10000.0));
            repository.addAccount(new Account("SHOP", "Shop", 0.0));

            // Act
            transferService.transfer("BUYER", "SHOP", amount);

            // Assert
            assertEquals(10000.0 - amount, 
                repository.loadAccountById("BUYER").getBalance());
            assertEquals(amount, 
                repository.loadAccountById("SHOP").getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // REFUND SCENARIO
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Refund Scenario")
    class RefundTests {

        @Test
        @DisplayName("Should process full refund")
        void shouldProcessFullRefund() {
            // Arrange
            repository.addAccount(new Account("CUSTOMER", "Customer", 10000.0));
            repository.addAccount(new Account("MERCHANT", "Merchant", 50000.0));

            // Customer makes purchase
            double purchaseAmount = 2500.0;
            transferService.transfer("CUSTOMER", "MERCHANT", purchaseAmount);

            assertEquals(7500.0, repository.loadAccountById("CUSTOMER").getBalance());
            assertEquals(52500.0, repository.loadAccountById("MERCHANT").getBalance());

            // Act - Merchant processes refund
            transferService.transfer("MERCHANT", "CUSTOMER", purchaseAmount);

            // Assert - Back to original
            assertEquals(10000.0, repository.loadAccountById("CUSTOMER").getBalance());
            assertEquals(50000.0, repository.loadAccountById("MERCHANT").getBalance());
        }

        @Test
        @DisplayName("Should process partial refund")
        void shouldProcessPartialRefund() {
            // Arrange
            repository.addAccount(new Account("CUSTOMER", "Customer", 10000.0));
            repository.addAccount(new Account("MERCHANT", "Merchant", 50000.0));

            // Purchase of ₹5000
            transferService.transfer("CUSTOMER", "MERCHANT", 5000.0);

            // Act - Partial refund of ₹2000 (customer returned some items)
            transferService.transfer("MERCHANT", "CUSTOMER", 2000.0);

            // Assert
            assertEquals(7000.0, repository.loadAccountById("CUSTOMER").getBalance());
            assertEquals(53000.0, repository.loadAccountById("MERCHANT").getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // P2P TRANSFER SCENARIOS
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Peer-to-Peer Transfer Scenarios")
    class P2PTransferTests {

        @ParameterizedTest(name = "Transfer ₹{0} for {3}")
        @CsvSource({
            "10, SENDER, RECEIVER, Tea payment",
            "50, SENDER, RECEIVER, Snacks",
            "100, SENDER, RECEIVER, Lunch share",
            "500, SENDER, RECEIVER, Movie tickets",
            "1000, SENDER, RECEIVER, Party contribution",
            "2000, SENDER, RECEIVER, Gift pooling"
        })
        @DisplayName("Should handle common P2P transfer amounts")
        void shouldHandleCommonP2PAmounts(double amount, String from, String to, String description) {
            // Arrange
            repository.addAccount(new Account("SENDER", "Sender", 10000.0));
            repository.addAccount(new Account("RECEIVER", "Receiver", 5000.0));

            // Act
            TransferResult result = transferService.transfer(from, to, amount);

            // Assert
            assertEquals(amount, result.getAmount());
            assertEquals(10000.0 - amount, result.getSenderBalanceAfter());
            assertEquals(5000.0 + amount, result.getReceiverBalanceAfter());
        }

        @Test
        @DisplayName("Should handle bidirectional transfers")
        void shouldHandleBidirectionalTransfers() {
            // Arrange - Two friends settling debts
            repository.addAccount(new Account("ALICE", "Alice", 5000.0));
            repository.addAccount(new Account("BOB", "Bob", 5000.0));

            // Alice owes Bob ₹300, Bob owes Alice ₹500
            // Net: Bob owes Alice ₹200

            // Act - They settle individually
            transferService.transfer("ALICE", "BOB", 300.0);
            transferService.transfer("BOB", "ALICE", 500.0);

            // Assert - Net effect: Alice +200, Bob -200
            assertEquals(5200.0, repository.loadAccountById("ALICE").getBalance());
            assertEquals(4800.0, repository.loadAccountById("BOB").getBalance());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // STRESS TESTING
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Stress Tests")
    class StressTests {

        @Test
        @DisplayName("Should handle 100 consecutive transfers")
        void shouldHandle100Transfers() {
            // Arrange
            repository.addAccount(new Account("SENDER", "Sender", 100000.0));
            repository.addAccount(new Account("RECEIVER", "Receiver", 0.0));

            // Act - 100 transfers of ₹100 each
            for (int i = 0; i < 100; i++) {
                transferService.transfer("SENDER", "RECEIVER", 100.0);
            }

            // Assert
            assertEquals(90000.0, repository.loadAccountById("SENDER").getBalance());
            assertEquals(10000.0, repository.loadAccountById("RECEIVER").getBalance());
        }

        @Test
        @DisplayName("Should maintain balance conservation with many accounts")
        void shouldMaintainBalanceWithManyAccounts() {
            // Arrange - 20 accounts with ₹1000 each
            int numAccounts = 20;
            double initialBalance = 1000.0;
            double expectedTotal = numAccounts * initialBalance;

            for (int i = 0; i < numAccounts; i++) {
                repository.addAccount(
                    new Account("ACC" + String.format("%02d", i), "User " + i, initialBalance)
                );
            }

            assertEquals(expectedTotal, repository.getTotalBalance());

            // Act - Random transfers between accounts
            transferService.transfer("ACC00", "ACC05", 100.0);
            transferService.transfer("ACC05", "ACC10", 200.0);
            transferService.transfer("ACC10", "ACC15", 150.0);
            transferService.transfer("ACC15", "ACC19", 300.0);
            transferService.transfer("ACC19", "ACC00", 250.0);

            // Assert - Total unchanged
            assertEquals(expectedTotal, repository.getTotalBalance(), 0.001);
        }
    }
}
