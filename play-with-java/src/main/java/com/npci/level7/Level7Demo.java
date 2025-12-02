package com.npci.level7;

/**
 * Level 7: Demo - Static Members
 *
 * Run this file to see all Level 7 concepts in action.
 */
public class Level7Demo {

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║     LEVEL 7: STATIC MEMBERS                          ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        // Demo 1: Static vs Instance
        System.out.println("\n▶ DEMO 1: Static vs Instance Members");
        System.out.println("─────────────────────────────────────────────\n");
        demo1_StaticVsInstance();

        // Demo 2: Static Blocks
        System.out.println("\n▶ DEMO 2: Static Blocks");
        System.out.println("─────────────────────────────────────────────\n");
        demo2_StaticBlocks();

        // Demo 3: Utility Classes
        System.out.println("\n▶ DEMO 3: Utility Classes (All Static)");
        System.out.println("─────────────────────────────────────────────\n");
        demo3_UtilityClasses();

        // Demo 4: Configuration Class
        System.out.println("\n▶ DEMO 4: Configuration with Static Members");
        System.out.println("─────────────────────────────────────────────\n");
        demo4_Configuration();

        // Demo 5: Statistics Tracking
        System.out.println("\n▶ DEMO 5: Statistics Tracking with Static");
        System.out.println("─────────────────────────────────────────────\n");
        demo5_StatisticsTracking();

        // Demo 6: Factory Methods
        System.out.println("\n▶ DEMO 6: Static Factory Methods");
        System.out.println("─────────────────────────────────────────────\n");
        demo6_FactoryMethods();

        // Demo 7: Summary
        System.out.println("\n▶ DEMO 7: Static Members Summary");
        System.out.println("─────────────────────────────────────────────\n");
        demo7_Summary();
    }

    /**
     * Demo 1: Difference between static and instance members
     */
    static void demo1_StaticVsInstance() {
        System.out.println("Creating multiple BankAccount objects...\n");

        // Create accounts
        BankAccount acc1 = new BankAccount("Ramesh Kumar", 50000, "Savings");
        BankAccount acc2 = new BankAccount("Priya Sharma", 75000, "Savings");
        BankAccount acc3 = new BankAccount("ABC Corp", 200000, "Current");

        System.out.println("\n--- Instance Members (Different for each object) ---");
        System.out.println("Account 1: " + acc1.getAccountNumber() + " | Balance: Rs." + acc1.getBalance());
        System.out.println("Account 2: " + acc2.getAccountNumber() + " | Balance: Rs." + acc2.getBalance());
        System.out.println("Account 3: " + acc3.getAccountNumber() + " | Balance: Rs." + acc3.getBalance());

        System.out.println("\n--- Static Members (Same for all objects) ---");
        System.out.println("Bank Name (static): " + BankAccount.getBankName());
        System.out.println("Total Accounts (static): " + BankAccount.getTotalAccountsCreated());
        System.out.println("Total Deposits (static): Rs." + BankAccount.getTotalBankDeposits());

        System.out.println("\n--- Modifying Static Member ---");
        System.out.println("Changing interest rate from 4.0% to 4.5%...");
        BankAccount.setSavingsInterestRate(4.5);

        System.out.println("\nAll accounts now use the new rate:");
        System.out.println("Account 1 Interest: Rs." + acc1.calculateInterest());
        System.out.println("Account 2 Interest: Rs." + acc2.calculateInterest());

        // Deposit to one account - affects total deposits (static)
        System.out.println("\n--- Depositing Rs.10000 to Account 1 ---");
        acc1.deposit(10000);
        System.out.println("Account 1 Balance: Rs." + acc1.getBalance() + " (instance)");
        System.out.println("Bank Total Deposits: Rs." + BankAccount.getTotalBankDeposits() + " (static - updated!)");
    }

    /**
     * Demo 2: Static blocks run once when class is loaded
     */
    static void demo2_StaticBlocks() {
        System.out.println("Static blocks run ONCE when class is first loaded.\n");
        System.out.println("Notice: BankAccount's static block already ran in Demo 1.");
        System.out.println("It won't run again, even if we create more objects.\n");

        System.out.println("Creating more accounts...");
        BankAccount acc4 = new BankAccount("New User", 10000, "Savings");
        System.out.println("No static block output - it already ran!\n");

        System.out.println("Static blocks are useful for:");
        System.out.println("  - Complex static variable initialization");
        System.out.println("  - Loading configuration");
        System.out.println("  - Registering drivers (JDBC)");
        System.out.println("  - One-time setup operations");
    }

    /**
     * Demo 3: Utility classes with all static members
     */
    static void demo3_UtilityClasses() {
        System.out.println("Utility classes have:");
        System.out.println("  - Private constructor (cannot create objects)");
        System.out.println("  - All static methods");
        System.out.println("  - Called using ClassName.methodName()\n");

        // AccountNumberGenerator
        System.out.println("--- AccountNumberGenerator ---");
        System.out.println("Savings Account: " + AccountNumberGenerator.generateSavingsAccountNumber());
        System.out.println("Savings Account: " + AccountNumberGenerator.generateSavingsAccountNumber());
        System.out.println("Current Account: " + AccountNumberGenerator.generateCurrentAccountNumber());
        System.out.println("FD Account: " + AccountNumberGenerator.generateFDAccountNumber());
        System.out.println("Branch Account: " + AccountNumberGenerator.generateBranchAccountNumber("MUM001"));

        // TransactionIdGenerator
        System.out.println("\n--- TransactionIdGenerator ---");
        System.out.println("UPI Txn: " + TransactionIdGenerator.generateUPITransactionId());
        System.out.println("IMPS Txn: " + TransactionIdGenerator.generateIMPSTransactionId());
        System.out.println("NEFT Txn: " + TransactionIdGenerator.generateNEFTTransactionId());

        // ValidationUtils
        System.out.println("\n--- ValidationUtils ---");
        System.out.println("PAN 'ABCDE1234F': " + (ValidationUtils.isValidPAN("ABCDE1234F") ? "Valid ✓" : "Invalid ✗"));
        System.out.println("PAN 'INVALID': " + (ValidationUtils.isValidPAN("INVALID") ? "Valid ✓" : "Invalid ✗"));
        System.out.println("Phone '9876543210': " + (ValidationUtils.isValidPhone("9876543210") ? "Valid ✓" : "Invalid ✗"));
        System.out.println("Email 'test@email.com': " + (ValidationUtils.isValidEmail("test@email.com") ? "Valid ✓" : "Invalid ✗"));
        System.out.println("IFSC 'NPCI0001234': " + (ValidationUtils.isValidIFSC("NPCI0001234") ? "Valid ✓" : "Invalid ✗"));
        System.out.println("UPI 'user@upi': " + (ValidationUtils.isValidUPI("user@upi") ? "Valid ✓" : "Invalid ✗"));

        System.out.println("\n--- Masking Utilities ---");
        System.out.println("PAN Masked: " + ValidationUtils.maskPAN("ABCDE1234F"));
        System.out.println("Aadhar Masked: " + ValidationUtils.maskAadhar("123456789012"));
        System.out.println("Phone Masked: " + ValidationUtils.maskPhone("9876543210"));
        System.out.println("Email Masked: " + ValidationUtils.maskEmail("ramesh.kumar@email.com"));

        // InterestRateCalculator
        System.out.println("\n--- InterestRateCalculator ---");
        InterestRateCalculator.displayInterestDetails(100000, 7.0, 3, "FD");
        InterestRateCalculator.displayEMIDetails(500000, 10.5, 36);
    }

    /**
     * Demo 4: Configuration using static members
     */
    static void demo4_Configuration() {
        System.out.println("BankConfig stores all bank-wide settings as static members.\n");

        // Display current configuration
        BankConfig.displayConfiguration();

        // Modify configuration
        System.out.println("\n--- Modifying Configuration ---");
        BankConfig.setSavingsInterestRate(4.25);
        BankConfig.setDailyWithdrawalLimit(150000);
        BankConfig.setConfig("support.email", "help@npcibank.com");

        System.out.println("\nConfiguration changes affect ALL parts of the application!");
        System.out.println("New Savings Rate: " + BankConfig.getSavingsInterestRate() + "%");
        System.out.println("New Withdrawal Limit: Rs." + BankConfig.getDailyWithdrawalLimit());
        System.out.println("Support Email: " + BankConfig.getConfig("support.email"));
    }

    /**
     * Demo 5: Statistics tracking with static members
     */
    static void demo5_StatisticsTracking() {
        System.out.println("BankStatistics tracks all bank activity using static members.\n");

        // Simulate some activity
        System.out.println("Simulating bank activity...\n");

        // Record account openings
        BankStatistics.recordAccountOpening("Savings");
        BankStatistics.recordAccountOpening("Savings");
        BankStatistics.recordAccountOpening("Current");
        BankStatistics.recordAccountOpening("FD");

        // Record transactions
        BankStatistics.recordDeposit(50000, "BRANCH");
        BankStatistics.recordDeposit(25000, "UPI");
        BankStatistics.recordDeposit(100000, "NEFT");
        BankStatistics.recordWithdrawal(10000, "ATM");
        BankStatistics.recordWithdrawal(5000, "UPI");
        BankStatistics.recordTransfer(30000, "IMPS");
        BankStatistics.recordTransfer(200000, "RTGS");

        // Display statistics
        BankStatistics.displayStatistics();

        // Also show ID generator statistics
        AccountNumberGenerator.displayStatistics();
        TransactionIdGenerator.displayStatistics();
        ValidationUtils.displayStatistics();
    }

    /**
     * Demo 6: Static factory methods
     */
    static void demo6_FactoryMethods() {
        System.out.println("Static factory methods create objects without 'new' keyword.\n");

        // Disable console logging temporarily
        AuditLogger.setConsoleOutput(false);

        System.out.println("--- Creating accounts using static factory methods ---\n");

        // Create accounts using Bank's static factory methods
        BankAccount savings1 = Bank.createSavingsAccount("Vikram Singh", 25000);
        BankAccount savings2 = Bank.createSavingsAccount("Neha Patel", 50000);
        BankAccount current1 = Bank.createCurrentAccount("XYZ Traders", 100000);

        if (savings1 != null) savings1.displayAccountInfo();
        if (current1 != null) current1.displayAccountInfo();

        // Try creating with insufficient balance
        System.out.println("\n--- Trying to create account with insufficient deposit ---");
        BankAccount failed = Bank.createSavingsAccount("Test User", 500);  // Will fail

        // Transfer using static method
        System.out.println("\n--- Transfer using static method ---");
        if (savings1 != null && savings2 != null) {
            Bank.transfer(savings1, savings2, 5000, "UPI");
            System.out.println("Savings1 Balance: Rs." + savings1.getBalance());
            System.out.println("Savings2 Balance: Rs." + savings2.getBalance());
        }

        // Re-enable console logging
        AuditLogger.setConsoleOutput(true);

        // Show audit log
        System.out.println("\n--- Last 5 Audit Log Entries ---");
        AuditLogger.displayLastLogs(5);
    }

    /**
     * Demo 7: Summary of static members
     */
    static void demo7_Summary() {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║               STATIC MEMBERS SUMMARY                               ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Type              │ Description                                   ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Static Variable   │ Shared by all objects, one copy exists        ║");
        System.out.println("║ Static Method     │ Belongs to class, called without object       ║");
        System.out.println("║ Static Block      │ Runs once when class is first loaded          ║");
        System.out.println("║ Static Final      │ Constant - cannot be changed                  ║");
        System.out.println("║ Instance Variable │ Each object has its own copy                  ║");
        System.out.println("║ Instance Method   │ Operates on object's data                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║               WHEN TO USE STATIC                                   ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Use Case                    │ Example                              ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Counters/Statistics         │ totalAccounts, transactionCount      ║");
        System.out.println("║ Constants                   │ MAX_WITHDRAWAL, BANK_CODE            ║");
        System.out.println("║ Configuration               │ interestRate, minBalance             ║");
        System.out.println("║ Utility Methods             │ validate(), calculate(), format()    ║");
        System.out.println("║ Factory Methods             │ createAccount(), getInstance()       ║");
        System.out.println("║ ID Generators               │ generateAccountNumber()              ║");
        System.out.println("║ Logging                     │ AuditLogger.log()                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║               WHEN NOT TO USE STATIC                               ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Scenario                              │ Use Instead               ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Data varies per object (balance)      │ Instance variable         ║");
        System.out.println("║ Method needs object state             │ Instance method           ║");
        System.out.println("║ Inheritance/Polymorphism needed       │ Instance method           ║");
        System.out.println("║ Thread-safety concerns                │ Careful design needed     ║");
        System.out.println("║ Testing/Mocking needed                │ Instance or interface     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║               UTILITY CLASS PATTERN                                ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ public class ValidationUtils {                                     ║");
        System.out.println("║     // Private constructor - prevent instantiation                 ║");
        System.out.println("║     private ValidationUtils() { }                                  ║");
        System.out.println("║                                                                    ║");
        System.out.println("║     // All static methods                                          ║");
        System.out.println("║     public static boolean isValidPAN(String pan) { ... }          ║");
        System.out.println("║     public static boolean isValidEmail(String email) { ... }      ║");
        System.out.println("║ }                                                                  ║");
        System.out.println("║                                                                    ║");
        System.out.println("║ // Usage: ValidationUtils.isValidPAN(\"ABCDE1234F\")                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n--- Final Statistics ---");
        Bank.displayAllStatistics();
    }
}