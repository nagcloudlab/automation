package com.npci.level10.basics;

import com.npci.level10.model.*;
import java.util.*;
import java.util.stream.*;
import java.time.LocalDate;

/**
 * Level 10: Streams API - Stream Basics
 *
 * What is a Stream?
 * - A sequence of elements supporting sequential and parallel operations
 * - NOT a data structure (doesn't store elements)
 * - Lazy evaluation (operations are not executed until terminal operation)
 * - Can be consumed only once
 *
 * Stream Pipeline:
 * Source → Intermediate Operations → Terminal Operation
 *
 * Characteristics:
 * - Declarative (what to do, not how)
 * - Functional (operations don't modify source)
 * - Lazy (intermediate ops are not executed until terminal op)
 * - Parallelizable (can be processed in parallel)
 */
public class StreamBasicsDemo {

    /**
     * Creating streams from various sources
     */
    public static void demonstrateStreamCreation() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║        STREAM CREATION                        ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // 1. From Collection
        System.out.println("--- From Collection ---");
        List<String> channels = Arrays.asList("UPI", "IMPS", "NEFT", "RTGS");
        Stream<String> streamFromList = channels.stream();
        streamFromList.forEach(c -> System.out.println("  " + c));

        // 2. From Array
        System.out.println("\n--- From Array ---");
        String[] accountTypes = {"SAVINGS", "CURRENT", "FD", "RD"};
        Stream<String> streamFromArray = Arrays.stream(accountTypes);
        streamFromArray.forEach(t -> System.out.println("  " + t));

        // 3. Using Stream.of()
        System.out.println("\n--- Using Stream.of() ---");
        Stream<String> streamOf = Stream.of("UPI", "IMPS", "NEFT");
        streamOf.forEach(c -> System.out.println("  " + c));

        // 4. Empty Stream
        System.out.println("\n--- Empty Stream ---");
        Stream<String> emptyStream = Stream.empty();
        System.out.println("  Empty stream count: " + emptyStream.count());

        // 5. Using Stream.generate() - infinite stream
        System.out.println("\n--- Using Stream.generate() ---");
        Stream<String> generated = Stream.generate(() -> "TXN" + System.nanoTime())
                .limit(3);
        generated.forEach(id -> System.out.println("  " + id));

        // 6. Using Stream.iterate() - infinite stream with seed
        System.out.println("\n--- Using Stream.iterate() ---");
        Stream<Integer> iterated = Stream.iterate(1000, n -> n + 100)
                .limit(5);
        iterated.forEach(n -> System.out.println("  Rs." + n));

        // 7. Using Stream.iterate() with predicate (Java 9+)
        System.out.println("\n--- Using Stream.iterate() with predicate ---");
        Stream<Integer> iteratedWithPredicate = Stream.iterate(100, n -> n <= 500, n -> n + 100);
        iteratedWithPredicate.forEach(n -> System.out.println("  Rs." + n));

        // 8. IntStream, LongStream, DoubleStream (primitive streams)
        System.out.println("\n--- Primitive Streams ---");
        IntStream intStream = IntStream.range(1, 6);  // 1 to 5
        System.out.println("IntStream range(1,6): " + Arrays.toString(intStream.toArray()));

        IntStream intStreamClosed = IntStream.rangeClosed(1, 5);  // 1 to 5 inclusive
        System.out.println("IntStream rangeClosed(1,5): " + Arrays.toString(intStreamClosed.toArray()));

        // 9. From String (chars)
        System.out.println("\n--- From String ---");
        "NPCI".chars().forEach(c -> System.out.println("  " + (char) c));

        // 10. Parallel Stream
        System.out.println("\n--- Parallel Stream ---");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.parallelStream()
                .forEach(n -> System.out.println("  Thread: " + Thread.currentThread().getName() + " - " + n));
    }

    /**
     * Stream pipeline structure
     */
    public static void demonstrateStreamPipeline() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║        STREAM PIPELINE                        ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<Transaction> transactions = createSampleTransactions();

        System.out.println("--- Complete Pipeline Example ---");
        System.out.println("Find total amount of successful UPI transactions:\n");

        // Pipeline breakdown:
        // Source: transactions.stream()
        // Intermediate: filter(), filter(), mapToDouble()
        // Terminal: sum()

        double total = transactions.stream()           // Source
                .filter(t -> t.isSuccessful())             // Intermediate 1
                .filter(t -> t.isUPI())                    // Intermediate 2
                .mapToDouble(Transaction::getAmount)       // Intermediate 3
                .sum();                                    // Terminal

        System.out.printf("Total successful UPI: Rs.%,.2f%n", total);

        System.out.println("\n--- Lazy Evaluation Demo ---");
        System.out.println("Without terminal operation, nothing happens:\n");

        // This does NOT execute filter operations
        Stream<Transaction> lazyStream = transactions.stream()
                .filter(t -> {
                    System.out.println("  Filtering: " + t.getTransactionId());
                    return t.isSuccessful();
                });

        System.out.println("Stream created but filter not yet executed.");
        System.out.println("\nNow adding terminal operation:");

        long count = lazyStream.count();  // Now filters execute
        System.out.println("Count: " + count);

        System.out.println("\n--- Stream Can Only Be Consumed Once ---");
        Stream<String> channelStream = Stream.of("UPI", "IMPS", "NEFT");
        channelStream.forEach(c -> System.out.println("  " + c));

        try {
            channelStream.forEach(c -> System.out.println("  " + c));  // Error!
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Common stream patterns
     */
    public static void demonstrateCommonPatterns() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║        COMMON STREAM PATTERNS                 ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<BankAccount> accounts = createSampleAccounts();

        // Pattern 1: Filter and Collect
        System.out.println("--- Pattern 1: Filter and Collect ---");
        List<BankAccount> savingsAccounts = accounts.stream()
                .filter(BankAccount::isSavings)
                .collect(Collectors.toList());
        System.out.println("Savings accounts: " + savingsAccounts.size());

        // Pattern 2: Map and Collect
        System.out.println("\n--- Pattern 2: Map and Collect ---");
        List<String> accountNumbers = accounts.stream()
                .map(BankAccount::getAccountNumber)
                .collect(Collectors.toList());
        System.out.println("Account numbers: " + accountNumbers);

        // Pattern 3: Filter, Map, Collect
        System.out.println("\n--- Pattern 3: Filter, Map, Collect ---");
        List<String> activeAccountNumbers = accounts.stream()
                .filter(BankAccount::isActive)
                .map(BankAccount::getAccountNumber)
                .collect(Collectors.toList());
        System.out.println("Active account numbers: " + activeAccountNumbers);

        // Pattern 4: Aggregate (sum, average, count)
        System.out.println("\n--- Pattern 4: Aggregations ---");
        double totalBalance = accounts.stream()
                .mapToDouble(BankAccount::getBalance)
                .sum();
        System.out.printf("Total balance: Rs.%,.2f%n", totalBalance);

        OptionalDouble avgBalance = accounts.stream()
                .mapToDouble(BankAccount::getBalance)
                .average();
        System.out.printf("Average balance: Rs.%,.2f%n", avgBalance.orElse(0));

        long activeCount = accounts.stream()
                .filter(BankAccount::isActive)
                .count();
        System.out.println("Active accounts: " + activeCount);

        // Pattern 5: Find first/any
        System.out.println("\n--- Pattern 5: Find First/Any ---");
        Optional<BankAccount> firstHighBalance = accounts.stream()
                .filter(a -> a.getBalance() > 100000)
                .findFirst();
        firstHighBalance.ifPresent(a -> System.out.println("First high balance: " + a));

        // Pattern 6: Check conditions
        System.out.println("\n--- Pattern 6: Check Conditions ---");
        boolean anyInactive = accounts.stream()
                .anyMatch(a -> !a.isActive());
        System.out.println("Any inactive accounts: " + anyInactive);

        boolean allActive = accounts.stream()
                .allMatch(BankAccount::isActive);
        System.out.println("All accounts active: " + allActive);

        boolean noneNegative = accounts.stream()
                .noneMatch(a -> a.getBalance() < 0);
        System.out.println("No negative balances: " + noneNegative);

        // Pattern 7: Sort and limit
        System.out.println("\n--- Pattern 7: Sort and Limit (Top 3 by Balance) ---");
        accounts.stream()
                .sorted(Comparator.comparing(BankAccount::getBalance).reversed())
                .limit(3)
                .forEach(a -> System.out.printf("  %s: Rs.%,.2f%n",
                        a.getAccountNumber(), a.getBalance()));
    }

    // ═══════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════

    public static List<Transaction> createSampleTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("T001", "A001", "CREDIT", 50000, 50000, "UPI", "Salary", "SALARY"));
        transactions.add(new Transaction("T002", "A001", "DEBIT", 5000, 45000, "UPI", "Shopping", "SHOPPING"));
        transactions.add(new Transaction("T003", "A002", "CREDIT", 100000, 100000, "NEFT", "Transfer", "TRANSFER"));
        transactions.add(new Transaction("T004", "A001", "DEBIT", 2000, 43000, "ATM", "Cash", "CASH"));
        transactions.add(new Transaction("T005", "A003", "CREDIT", 75000, 75000, "IMPS", "Refund", "REFUND"));

        Transaction failed = new Transaction("T006", "A001", "DEBIT", 1000, 43000, "UPI", "Failed", "SHOPPING");
        failed.setStatus("FAILED");
        transactions.add(failed);

        return transactions;
    }

    public static List<BankAccount> createSampleAccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new BankAccount("A001", "C001", "SAVINGS", 150000, "BR001"));
        accounts.add(new BankAccount("A002", "C002", "CURRENT", 500000, "BR001"));
        accounts.add(new BankAccount("A003", "C003", "SAVINGS", 75000, "BR002"));
        accounts.add(new BankAccount("A004", "C004", "FD", 200000, "BR002"));
        accounts.add(new BankAccount("A005", "C005", "SAVINGS", 25000, "BR003"));

        BankAccount inactive = new BankAccount("A006", "C006", "SAVINGS", 10000, "BR003");
        inactive.setActive(false);
        inactive.setStatus("DORMANT");
        accounts.add(inactive);

        return accounts;
    }
}