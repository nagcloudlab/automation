package com.npci.level10.intermediate;

import com.npci.level10.model.*;
import java.util.*;
import java.util.stream.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Level 10: Streams API - Intermediate Operations
 *
 * Intermediate operations return a Stream and are lazy.
 * They are not executed until a terminal operation is invoked.
 *
 * Categories:
 * - Filtering: filter(), distinct(), limit(), skip()
 * - Mapping: map(), flatMap(), mapToInt/Long/Double()
 * - Sorting: sorted()
 * - Peeking: peek()
 */
public class IntermediateOperationsDemo {

    /**
     * filter() - Select elements matching a predicate
     */
    public static void demonstrateFilter() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║           filter() OPERATION                  ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<Transaction> transactions = createTransactions();

        // Simple filter
        System.out.println("--- Successful Transactions ---");
        transactions.stream()
                .filter(Transaction::isSuccessful)
                .forEach(t -> System.out.println("  " + t));

        // Multiple filters (chained)
        System.out.println("\n--- Successful UPI Transactions > Rs.5000 ---");
        transactions.stream()
                .filter(Transaction::isSuccessful)
                .filter(Transaction::isUPI)
                .filter(t -> t.getAmount() > 5000)
                .forEach(t -> System.out.println("  " + t));

        // Complex filter with lambda
        System.out.println("\n--- High Value Credit Transactions ---");
        transactions.stream()
                .filter(t -> t.isCredit() && t.getAmount() >= 50000 && t.isSuccessful())
                .forEach(t -> System.out.println("  " + t));

        // Filter with external method
        System.out.println("\n--- Transactions for Account A001 ---");
        String targetAccount = "A001";
        transactions.stream()
                .filter(t -> t.getAccountNumber().equals(targetAccount))
                .forEach(t -> System.out.println("  " + t));
    }

    /**
     * map() - Transform elements
     */
    public static void demonstrateMap() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║            map() OPERATION                    ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<Customer> customers = createCustomers();

        // Extract single property
        System.out.println("--- Customer Names ---");
        customers.stream()
                .map(Customer::getName)
                .forEach(name -> System.out.println("  " + name));

        // Transform to different type
        System.out.println("\n--- Customer IDs (Uppercase) ---");
        customers.stream()
                .map(Customer::getCustomerId)
                .map(String::toUpperCase)
                .forEach(id -> System.out.println("  " + id));

        // Map to computed value
        System.out.println("\n--- Customer Ages ---");
        customers.stream()
                .filter(c -> c.getDateOfBirth() != null)
                .map(c -> c.getName() + ": " + c.getAge() + " years")
                .forEach(s -> System.out.println("  " + s));

        // Map to different object
        System.out.println("\n--- Customer Summary Objects ---");
        customers.stream()
                .map(c -> new CustomerSummary(c.getCustomerId(), c.getName(), c.getCustomerType()))
                .forEach(s -> System.out.println("  " + s));

        // Chained maps
        System.out.println("\n--- Email Domains ---");
        customers.stream()
                .map(Customer::getEmail)
                .filter(email -> email != null && email.contains("@"))
                .map(email -> email.substring(email.indexOf("@") + 1))
                .distinct()
                .forEach(domain -> System.out.println("  " + domain));
    }

    // Helper class for map demonstration
    static class CustomerSummary {
        String id, name, type;
        CustomerSummary(String id, String name, String type) {
            this.id = id; this.name = name; this.type = type;
        }
        @Override
        public String toString() {
            return String.format("Summary[%s, %s, %s]", id, name, type);
        }
    }

    /**
     * flatMap() - Flatten nested structures
     */
    public static void demonstrateFlatMap() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          flatMap() OPERATION                  ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Customer with multiple accounts
        Map<String, List<String>> customerAccounts = new HashMap<>();
        customerAccounts.put("C001", Arrays.asList("A001", "A002", "A003"));
        customerAccounts.put("C002", Arrays.asList("A004", "A005"));
        customerAccounts.put("C003", Arrays.asList("A006"));

        // Without flatMap - Stream of Lists
        System.out.println("--- Without flatMap (Stream of Lists) ---");
        customerAccounts.values().stream()
                .forEach(list -> System.out.println("  " + list));

        // With flatMap - Stream of individual elements
        System.out.println("\n--- With flatMap (Flattened Stream) ---");
        customerAccounts.values().stream()
                .flatMap(List::stream)
                .forEach(acc -> System.out.println("  " + acc));

        // Practical example: Get all transactions across all accounts
        System.out.println("\n--- All Transactions Across Accounts ---");
        List<BankAccountWithTransactions> accountsWithTxns = createAccountsWithTransactions();

        accountsWithTxns.stream()
                .flatMap(a -> a.getTransactions().stream())
                .forEach(t -> System.out.println("  " + t.getTransactionId() +
                        ": Rs." + t.getAmount()));

        // Count all transactions
        long totalTxnCount = accountsWithTxns.stream()
                .flatMap(a -> a.getTransactions().stream())
                .count();
        System.out.println("\nTotal transactions: " + totalTxnCount);

        // Total amount across all accounts
        double totalAmount = accountsWithTxns.stream()
                .flatMap(a -> a.getTransactions().stream())
                .filter(Transaction::isCredit)
                .mapToDouble(Transaction::getAmount)
                .sum();
        System.out.printf("Total credit amount: Rs.%,.2f%n", totalAmount);

        // FlatMap with array
        System.out.println("\n--- FlatMap with String Split ---");
        List<String> descriptions = Arrays.asList(
                "UPI Transfer Payment",
                "NEFT Salary Credit",
                "ATM Cash Withdrawal"
        );

        descriptions.stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .distinct()
                .sorted()
                .forEach(word -> System.out.println("  " + word));
    }

    // Helper class for flatMap
    static class BankAccountWithTransactions {
        private String accountNumber;
        private List<Transaction> transactions;

        BankAccountWithTransactions(String accountNumber) {
            this.accountNumber = accountNumber;
            this.transactions = new ArrayList<>();
        }

        void addTransaction(Transaction t) { transactions.add(t); }
        List<Transaction> getTransactions() { return transactions; }
        String getAccountNumber() { return accountNumber; }
    }

    /**
     * mapToInt, mapToLong, mapToDouble - Primitive streams
     */
    public static void demonstratePrimitiveMapping() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║     mapToInt/Long/Double OPERATIONS          ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<BankAccount> accounts = createAccounts();

        // mapToDouble for sum
        System.out.println("--- mapToDouble for Aggregations ---");
        double sum = accounts.stream()
                .mapToDouble(BankAccount::getBalance)
                .sum();
        System.out.printf("Sum: Rs.%,.2f%n", sum);

        // Statistics
        DoubleSummaryStatistics stats = accounts.stream()
                .mapToDouble(BankAccount::getBalance)
                .summaryStatistics();

        System.out.println("\nStatistics:");
        System.out.println("  Count: " + stats.getCount());
        System.out.printf("  Sum: Rs.%,.2f%n", stats.getSum());
        System.out.printf("  Min: Rs.%,.2f%n", stats.getMin());
        System.out.printf("  Max: Rs.%,.2f%n", stats.getMax());
        System.out.printf("  Average: Rs.%,.2f%n", stats.getAverage());

        // mapToInt for count-based operations
        System.out.println("\n--- mapToInt for Integer Operations ---");
        int totalDays = accounts.stream()
                .mapToInt(BankAccount::getAccountAgeInDays)
                .sum();
        System.out.println("Total account age in days: " + totalDays);

        // Convert back to object stream
        System.out.println("\n--- boxed() to Convert Primitive to Object ---");
        List<Double> balances = accounts.stream()
                .mapToDouble(BankAccount::getBalance)
                .boxed()
                .collect(Collectors.toList());
        System.out.println("Balances as List: " + balances);
    }

    /**
     * sorted() - Sort elements
     */
    public static void demonstrateSorted() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          sorted() OPERATION                   ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<BankAccount> accounts = createAccounts();

        // Natural order (requires Comparable)
        System.out.println("--- Sorted by Account Number (Natural) ---");
        accounts.stream()
                .sorted()  // Uses compareTo if implemented
                .forEach(a -> System.out.println("  " + a.getAccountNumber()));

        // Sort by specific field
        System.out.println("\n--- Sorted by Balance (Ascending) ---");
        accounts.stream()
                .sorted(Comparator.comparing(BankAccount::getBalance))
                .forEach(a -> System.out.printf("  %s: Rs.%,.2f%n",
                        a.getAccountNumber(), a.getBalance()));

        // Sort descending
        System.out.println("\n--- Sorted by Balance (Descending) ---");
        accounts.stream()
                .sorted(Comparator.comparing(BankAccount::getBalance).reversed())
                .forEach(a -> System.out.printf("  %s: Rs.%,.2f%n",
                        a.getAccountNumber(), a.getBalance()));

        // Multiple sort criteria
        System.out.println("\n--- Sorted by Type, then Balance ---");
        accounts.stream()
                .sorted(Comparator
                        .comparing(BankAccount::getAccountType)
                        .thenComparing(BankAccount::getBalance).reversed())
                .forEach(a -> System.out.printf("  %s | %s: Rs.%,.2f%n",
                        a.getAccountType(), a.getAccountNumber(), a.getBalance()));

        // Sort with null handling
        System.out.println("\n--- Null-safe Sorting ---");
        List<Customer> customers = createCustomers();
        customers.stream()
                .sorted(Comparator.comparing(
                        Customer::getCity,
                        Comparator.nullsLast(String::compareTo)))
                .forEach(c -> System.out.println("  " + c.getName() + " - " + c.getCity()));
    }

    /**
     * distinct(), limit(), skip() - Element control
     */
    public static void demonstrateDistinctLimitSkip() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║    distinct(), limit(), skip() OPERATIONS    ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // distinct
        System.out.println("--- distinct() - Remove Duplicates ---");
        List<String> channels = Arrays.asList("UPI", "IMPS", "UPI", "NEFT", "UPI", "IMPS");
        System.out.println("Original: " + channels);

        List<String> uniqueChannels = channels.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Distinct: " + uniqueChannels);

        // limit
        System.out.println("\n--- limit() - Take First N Elements ---");
        List<BankAccount> accounts = createAccounts();
        System.out.println("Top 3 by Balance:");
        accounts.stream()
                .sorted(Comparator.comparing(BankAccount::getBalance).reversed())
                .limit(3)
                .forEach(a -> System.out.printf("  %s: Rs.%,.2f%n",
                        a.getAccountNumber(), a.getBalance()));

        // skip
        System.out.println("\n--- skip() - Skip First N Elements ---");
        System.out.println("Skip first 2, show next 3:");
        accounts.stream()
                .sorted(Comparator.comparing(BankAccount::getBalance).reversed())
                .skip(2)
                .limit(3)
                .forEach(a -> System.out.printf("  %s: Rs.%,.2f%n",
                        a.getAccountNumber(), a.getBalance()));

        // Pagination pattern
        System.out.println("\n--- Pagination Pattern ---");
        int pageSize = 2;
        int pageNumber = 1;  // 0-based

        System.out.println("Page " + (pageNumber + 1) + " (size " + pageSize + "):");
        accounts.stream()
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .forEach(a -> System.out.println("  " + a));
    }

    /**
     * peek() - Debug/inspect stream
     */
    public static void demonstratePeek() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║           peek() OPERATION                    ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<Transaction> transactions = createTransactions();

        System.out.println("--- Using peek() to Debug Stream Pipeline ---\n");

        double total = transactions.stream()
                .peek(t -> System.out.println("Original: " + t.getTransactionId()))
                .filter(Transaction::isSuccessful)
                .peek(t -> System.out.println("  After success filter: " + t.getTransactionId()))
                .filter(Transaction::isCredit)
                .peek(t -> System.out.println("    After credit filter: " + t.getTransactionId()))
                .mapToDouble(Transaction::getAmount)
                .peek(amt -> System.out.println("      Amount: Rs." + amt))
                .sum();

        System.out.printf("\nTotal: Rs.%,.2f%n", total);

        System.out.println("\n--- peek() for Side Effects (Use Carefully) ---");
        List<BankAccount> accounts = createAccounts();

        // Don't do this in production - side effects in streams
        accounts.stream()
                .filter(a -> a.getBalance() < 50000)
                .peek(a -> System.out.println("Low balance alert: " + a.getAccountNumber()))
                .count();
    }

    /**
     * takeWhile() and dropWhile() - Java 9+
     */
    public static void demonstrateTakeDropWhile() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║    takeWhile() and dropWhile() (Java 9+)     ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Ordered list of transactions by amount
        List<Integer> amounts = Arrays.asList(100, 200, 500, 1000, 5000, 2000, 500, 100);

        // takeWhile - take elements while condition is true
        System.out.println("--- takeWhile(amount < 1000) ---");
        System.out.println("Original: " + amounts);
        List<Integer> taken = amounts.stream()
                .takeWhile(a -> a < 1000)
                .collect(Collectors.toList());
        System.out.println("Taken: " + taken);

        // dropWhile - drop elements while condition is true
        System.out.println("\n--- dropWhile(amount < 1000) ---");
        List<Integer> dropped = amounts.stream()
                .dropWhile(a -> a < 1000)
                .collect(Collectors.toList());
        System.out.println("After drop: " + dropped);
    }

    // ═══════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════

    private static List<Transaction> createTransactions() {
        List<Transaction> txns = new ArrayList<>();
        txns.add(new Transaction("T001", "A001", "CREDIT", 50000, 50000, "UPI", "Salary", "SALARY"));
        txns.add(new Transaction("T002", "A001", "DEBIT", 5000, 45000, "UPI", "Shopping", "SHOPPING"));
        txns.add(new Transaction("T003", "A002", "CREDIT", 100000, 100000, "NEFT", "Transfer", "TRANSFER"));
        txns.add(new Transaction("T004", "A001", "DEBIT", 2000, 43000, "ATM", "Cash", "CASH"));
        txns.add(new Transaction("T005", "A003", "CREDIT", 75000, 75000, "IMPS", "Refund", "REFUND"));
        txns.add(new Transaction("T006", "A001", "CREDIT", 10000, 53000, "UPI", "Cashback", "CASHBACK"));

        Transaction failed = new Transaction("T007", "A002", "DEBIT", 1000, 99000, "UPI", "Failed", "SHOPPING");
        failed.setStatus("FAILED");
        txns.add(failed);

        return txns;
    }

    private static List<Customer> createCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("C001", "Ramesh Kumar", "ramesh@email.com", "9876543210",
                LocalDate.of(1985, 5, 15), "Mumbai", "Maharashtra", "VIP", 1500000));
        customers.add(new Customer("C002", "Priya Sharma", "priya@gmail.com", "9876543211",
                LocalDate.of(1990, 8, 22), "Delhi", "Delhi", "PREMIUM", 800000));
        customers.add(new Customer("C003", "Amit Patel", "amit@yahoo.com", "9876543212",
                LocalDate.of(1988, 3, 10), "Mumbai", "Maharashtra", "REGULAR", 500000));
        customers.add(new Customer("C004", "Neha Singh", "neha@email.com", "9876543213",
                LocalDate.of(1992, 11, 5), null, "Karnataka", "VIP", 2000000));
        customers.add(new Customer("C005", "Vikram Reddy", "vikram@gmail.com", "9876543214",
                LocalDate.of(1995, 7, 18), "Chennai", "Tamil Nadu", "REGULAR", 400000));
        return customers;
    }

    private static List<BankAccount> createAccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new BankAccount("A001", "C001", "SAVINGS", 150000, "BR001"));
        accounts.add(new BankAccount("A002", "C002", "CURRENT", 500000, "BR001"));
        accounts.add(new BankAccount("A003", "C003", "SAVINGS", 75000, "BR002"));
        accounts.add(new BankAccount("A004", "C004", "FD", 200000, "BR002"));
        accounts.add(new BankAccount("A005", "C005", "SAVINGS", 25000, "BR003"));
        accounts.add(new BankAccount("A006", "C001", "RD", 50000, "BR001"));
        return accounts;
    }

    private static List<BankAccountWithTransactions> createAccountsWithTransactions() {
        BankAccountWithTransactions a1 = new BankAccountWithTransactions("A001");
        a1.addTransaction(new Transaction("T001", "A001", "CREDIT", 50000, 50000, "UPI"));
        a1.addTransaction(new Transaction("T002", "A001", "DEBIT", 5000, 45000, "ATM"));

        BankAccountWithTransactions a2 = new BankAccountWithTransactions("A002");
        a2.addTransaction(new Transaction("T003", "A002", "CREDIT", 100000, 100000, "NEFT"));
        a2.addTransaction(new Transaction("T004", "A002", "CREDIT", 25000, 125000, "IMPS"));
        a2.addTransaction(new Transaction("T005", "A002", "DEBIT", 10000, 115000, "UPI"));

        return Arrays.asList(a1, a2);
    }
}