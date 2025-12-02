package com.npci.level7;

/**
 * Level 7: Static Members - Bank Account with Static Members
 *
 * Key Concepts:
 * - Static Variable = Belongs to CLASS, not object (shared by all instances)
 * - Static Method = Belongs to CLASS, can be called without creating object
 * - Static Block = Runs once when class is loaded
 * - Instance Variable = Each object has its own copy
 * - Instance Method = Operates on object's data
 *
 * When to use Static:
 * - Counters (total accounts, total transactions)
 * - Constants (interest rates, limits)
 * - Utility methods (validation, calculation)
 * - Factory methods (creating objects)
 * - Configuration values
 *
 * When NOT to use Static:
 * - Data that varies per object (balance, name)
 * - Methods that need object state
 */
public class BankAccount {

    // ═══════════════════════════════════════════════════════════════
    // STATIC VARIABLES - Shared by ALL BankAccount objects
    // Only ONE copy exists regardless of how many accounts are created
    // ═══════════════════════════════════════════════════════════════

    /**
     * Bank name - same for all accounts
     */
    private static String bankName = "NPCI Bank";

    /**
     * Bank code - same for all accounts
     */
    private static String bankCode = "NPCI";

    /**
     * Total number of accounts created (ever)
     */
    private static int totalAccountsCreated = 0;

    /**
     * Currently active accounts
     */
    private static int activeAccountCount = 0;

    /**
     * Total balance across ALL accounts
     */
    private static double totalBankDeposits = 0;

    /**
     * Minimum balance requirement - policy for all savings accounts
     */
    private static double minimumBalance = 1000.0;

    /**
     * Maximum withdrawal limit per day
     */
    private static final double MAX_DAILY_WITHDRAWAL = 100000.0;  // static final = constant

    /**
     * Interest rate for savings accounts (can be changed by bank)
     */
    private static double savingsInterestRate = 4.0;

    /**
     * Account number prefix
     */
    private static String accountPrefix = "NPCI";

    // ═══════════════════════════════════════════════════════════════
    // STATIC BLOCK - Runs ONCE when class is first loaded
    // Used for complex static initialization
    // ═══════════════════════════════════════════════════════════════

    static {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║    BankAccount Class Initialized (Static)     ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("  Bank Name       : " + bankName);
        System.out.println("  Bank Code       : " + bankCode);
        System.out.println("  Min Balance     : Rs." + minimumBalance);
        System.out.println("  Max Withdrawal  : Rs." + MAX_DAILY_WITHDRAWAL);
        System.out.println("  Interest Rate   : " + savingsInterestRate + "%");
        System.out.println("╚═══════════════════════════════════════════════╝");
        System.out.println("[Static Block] This runs only ONCE when class is loaded!\n");
    }

    // ═══════════════════════════════════════════════════════════════
    // INSTANCE VARIABLES - Each object has its own copy
    // Different for each BankAccount object
    // ═══════════════════════════════════════════════════════════════

    private String accountNumber;      // Different for each account
    private String holderName;         // Different for each account
    private double balance;            // Different for each account
    private String accountType;        // Different for each account
    private boolean isActive;          // Different for each account
    private double dailyWithdrawn;     // Different for each account

    // ═══════════════════════════════════════════════════════════════
    // CONSTRUCTOR - Uses both static and instance members
    // ═══════════════════════════════════════════════════════════════

    public BankAccount(String holderName, double initialDeposit, String accountType) {
        // Update static counters (affects class-level data)
        totalAccountsCreated++;      // Increment class counter
        activeAccountCount++;        // Increment active counter
        totalBankDeposits += initialDeposit;  // Add to bank total

        // Set instance variables (unique to this object)
        this.accountNumber = generateAccountNumber();  // Uses static method
        this.holderName = holderName;
        this.balance = initialDeposit;
        this.accountType = accountType;
        this.isActive = true;
        this.dailyWithdrawn = 0;

        System.out.println("[Account Created] " + accountNumber + " for " + holderName);
        System.out.println("  Total Accounts Ever: " + totalAccountsCreated);
        System.out.println("  Active Accounts: " + activeAccountCount);
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC METHODS - Belong to class, don't need object
    // Called using ClassName.methodName()
    // ═══════════════════════════════════════════════════════════════

    /**
     * Generate unique account number (static - uses class data)
     */
    private static String generateAccountNumber() {
        return accountPrefix + String.format("%08d", totalAccountsCreated);
    }

    /**
     * Get bank name (static - same for all)
     */
    public static String getBankName() {
        return bankName;
    }

    /**
     * Get total accounts created (static - class-level info)
     */
    public static int getTotalAccountsCreated() {
        return totalAccountsCreated;
    }

    /**
     * Get active account count (static - class-level info)
     */
    public static int getActiveAccountCount() {
        return activeAccountCount;
    }

    /**
     * Get total bank deposits (static - class-level info)
     */
    public static double getTotalBankDeposits() {
        return totalBankDeposits;
    }

    /**
     * Get minimum balance requirement (static - bank policy)
     */
    public static double getMinimumBalance() {
        return minimumBalance;
    }

    /**
     * Set minimum balance (static - bank policy change)
     */
    public static void setMinimumBalance(double newMinimum) {
        System.out.println("[Bank Policy] Minimum balance changed from Rs." +
                minimumBalance + " to Rs." + newMinimum);
        minimumBalance = newMinimum;
    }

    /**
     * Get savings interest rate (static - bank rate)
     */
    public static double getSavingsInterestRate() {
        return savingsInterestRate;
    }

    /**
     * Set savings interest rate (static - bank rate change)
     */
    public static void setSavingsInterestRate(double newRate) {
        System.out.println("[Bank Policy] Interest rate changed from " +
                savingsInterestRate + "% to " + newRate + "%");
        savingsInterestRate = newRate;
    }

    /**
     * Display bank statistics (static - class-level info)
     */
    public static void displayBankStatistics() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          BANK STATISTICS (Static Data)        ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("  Bank Name           : " + bankName);
        System.out.println("  Bank Code           : " + bankCode);
        System.out.println("  Total Accounts Ever : " + totalAccountsCreated);
        System.out.println("  Active Accounts     : " + activeAccountCount);
        System.out.println("  Total Deposits      : Rs." + String.format("%,.2f", totalBankDeposits));
        System.out.println("  Minimum Balance     : Rs." + minimumBalance);
        System.out.println("  Max Daily Withdrawal: Rs." + MAX_DAILY_WITHDRAWAL);
        System.out.println("  Interest Rate       : " + savingsInterestRate + "%");
        System.out.println("╚═══════════════════════════════════════════════╝");
    }

    /**
     * Utility method - validate amount (static - no object needed)
     */
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    /**
     * Utility method - check if within withdrawal limit (static)
     */
    public static boolean isWithinDailyLimit(double amount) {
        return amount <= MAX_DAILY_WITHDRAWAL;
    }

    // ═══════════════════════════════════════════════════════════════
    // INSTANCE METHODS - Operate on object's data
    // Need an object to call: account.deposit()
    // ═══════════════════════════════════════════════════════════════

    /**
     * Get account number (instance - unique to each object)
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Get holder name (instance - unique to each object)
     */
    public String getHolderName() {
        return holderName;
    }

    /**
     * Get balance (instance - unique to each object)
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Get account type (instance - unique to each object)
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Check if active (instance - unique to each object)
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Deposit money (instance method - modifies this object's balance)
     * Also updates static totalBankDeposits
     */
    public void deposit(double amount) {
        if (!isActive) {
            System.out.println("Error: Account is inactive!");
            return;
        }
        if (!isValidAmount(amount)) {  // Calling static method from instance method
            System.out.println("Error: Invalid amount!");
            return;
        }

        balance += amount;             // Update instance variable
        totalBankDeposits += amount;   // Update static variable

        System.out.println("[Deposit] Rs." + amount + " to " + accountNumber);
        System.out.println("  New Balance: Rs." + balance);
        System.out.println("  Bank Total Deposits: Rs." + String.format("%,.2f", totalBankDeposits));
    }

    /**
     * Withdraw money (instance method)
     * Uses both static (limits) and instance (balance) data
     */
    public void withdraw(double amount) {
        if (!isActive) {
            System.out.println("Error: Account is inactive!");
            return;
        }
        if (!isValidAmount(amount)) {
            System.out.println("Error: Invalid amount!");
            return;
        }
        if (!isWithinDailyLimit(amount)) {  // Using static method
            System.out.println("Error: Exceeds daily limit of Rs." + MAX_DAILY_WITHDRAWAL);
            return;
        }
        if (dailyWithdrawn + amount > MAX_DAILY_WITHDRAWAL) {
            System.out.println("Error: Would exceed daily withdrawal limit!");
            System.out.println("  Already withdrawn today: Rs." + dailyWithdrawn);
            System.out.println("  Remaining limit: Rs." + (MAX_DAILY_WITHDRAWAL - dailyWithdrawn));
            return;
        }
        if (accountType.equals("Savings") && balance - amount < minimumBalance) {  // Using static variable
            System.out.println("Error: Must maintain minimum balance of Rs." + minimumBalance);
            System.out.println("  Available for withdrawal: Rs." + (balance - minimumBalance));
            return;
        }
        if (balance < amount) {
            System.out.println("Error: Insufficient balance!");
            return;
        }

        balance -= amount;              // Update instance variable
        dailyWithdrawn += amount;       // Update instance variable
        totalBankDeposits -= amount;    // Update static variable

        System.out.println("[Withdraw] Rs." + amount + " from " + accountNumber);
        System.out.println("  New Balance: Rs." + balance);
    }

    /**
     * Calculate interest (instance method using static rate)
     */
    public double calculateInterest() {
        if (accountType.equals("Savings")) {
            return balance * savingsInterestRate / 100;  // Using static variable
        }
        return 0;
    }

    /**
     * Add interest to balance (instance method)
     */
    public void addInterest() {
        double interest = calculateInterest();
        if (interest > 0) {
            balance += interest;
            totalBankDeposits += interest;
            System.out.println("[Interest] Rs." + String.format("%.2f", interest) +
                    " added to " + accountNumber);
        }
    }

    /**
     * Close account (instance method updating static counters)
     */
    public void closeAccount() {
        if (!isActive) {
            System.out.println("Account already closed!");
            return;
        }

        if (balance > 0) {
            System.out.println("Closing balance Rs." + balance + " will be returned.");
            totalBankDeposits -= balance;
            balance = 0;
        }

        isActive = false;
        activeAccountCount--;  // Update static counter

        System.out.println("[Account Closed] " + accountNumber);
        System.out.println("  Active Accounts: " + activeAccountCount);
    }

    /**
     * Reset daily withdrawal limit (instance method)
     */
    public void resetDailyLimit() {
        dailyWithdrawn = 0;
        System.out.println("[Reset] Daily withdrawal limit reset for " + accountNumber);
    }

    /**
     * Display account info (instance method)
     */
    public void displayAccountInfo() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│         ACCOUNT INFORMATION             │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("  Account Number : " + accountNumber);
        System.out.println("  Holder Name    : " + holderName);
        System.out.println("  Account Type   : " + accountType);
        System.out.println("  Balance        : Rs." + balance);
        System.out.println("  Status         : " + (isActive ? "Active" : "Closed"));
        System.out.println("  Daily Withdrawn: Rs." + dailyWithdrawn);
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("  Bank           : " + bankName + " (static)");
        System.out.println("  Interest Rate  : " + savingsInterestRate + "% (static)");
        System.out.println("  Min Balance    : Rs." + minimumBalance + " (static)");
        System.out.println("└─────────────────────────────────────────┘");
    }

    @Override
    public String toString() {
        return accountNumber + " | " + holderName + " | " + accountType + " | Rs." + balance;
    }
}