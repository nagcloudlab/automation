package com.npci.level10.advanced;

import com.npci.level10.model.*;
import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Level 10: Streams API - Advanced Topics
 *
 * Topics:
 * - Parallel Streams
 * - Optional handling
 * - Infinite streams
 * - Stream performance
 */
public class AdvancedStreamsDemo {

    /**
     * Parallel Streams
     */
    public static void demonstrateParallelStreams() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║          PARALLEL STREAMS                     ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<BankAccount> accounts = createLargeAccountList(100000);

        // Sequential vs Parallel performance
        System.out.println("--- Performance Comparison (100K accounts) ---");

        // Sequential
        long startSeq = System.currentTimeMillis();
        double sumSeq = accounts.stream()
                .filter(BankAccount::isActive)
                .mapToDouble(BankAccount::getBalance)
                .sum();
        long endSeq = System.currentTimeMillis();
        System.out.printf("Sequential: Rs.%,.2f in %d ms%n", sumSeq, (endSeq - startSeq));

        // Parallel
        long startPar = System.currentTimeMillis();
        double sumPar = accounts.parallelStream()
                .filter(BankAccount::isActive)
                .mapToDouble(BankAccount::getBalance)
                .sum();
        long endPar = System.currentTimeMillis();
        System.out.printf("Parallel: Rs.%,.2f in %d ms%n", sumPar, (endPar - startPar));

        // Converting between sequential and parallel
        System.out.println("\n--- Converting Sequential <-> Parallel ---");
        Stream<BankAccount> seqStream = accounts.stream();
        Stream<BankAccount> parStream = seqStream.parallel();
        System.out.println("Is parallel: " + parStream.isParallel());

        Stream<BankAccount> backToSeq = parStream.sequential();
        System.out.println("Is parallel after sequential(): " + backToSeq.isParallel());

        // Thread safety concern
        System.out.println("\n--- Thread Safety in Parallel Streams ---");
        System.out.println("WRONG: Using shared mutable state");

        // Wrong way (DON'T DO THIS)
        // List<Double> balances = new ArrayList<>();  // Not thread-safe!
        // accounts.parallelStream().forEach(a -> balances.add(a.getBalance()));

        // Right way
        System.out.println("RIGHT: Using thread-safe operations");
        List<Double> balances = accounts.parallelStream()
                .map(BankAccount::getBalance)
                .collect(Collectors.toList());  // Thread-safe
        System.out.println("Collected " + balances.size() + " balances safely");

        // Order with parallel
        System.out.println("\n--- Order in Parallel Streams ---");
        List<String> channels = Arrays.asList("UPI", "IMPS", "NEFT", "RTGS", "ATM");

        System.out.println("forEach (order NOT guaranteed):");
        channels.parallelStream()
                .forEach(c -> System.out.println("  " + Thread.currentThread().getName() + ": " + c));

        System.out.println("\nforEachOrdered (order guaranteed):");
        channels.parallelStream()
                .forEachOrdered(c -> System.out.println("  " + c));

        // When to use parallel
        System.out.println("\n--- When to Use Parallel Streams ---");
        System.out.println("✓ Large data sets (>10,000 elements)");
        System.out.println("✓ Expensive operations per element");
        System.out.println("✓ Independent operations (no shared state)");
        System.out.println("✓ Splittable source (ArrayList good, LinkedList bad)");
        System.out.println("✗ Small data sets");
        System.out.println("✗ I/O bound operations");
        System.out.println("✗ Operations requiring order");
        System.out.println("✗ Operations with side effects");
    }

    /**
     * Optional handling best practices
     */
    public static void demonstrateOptional() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          OPTIONAL HANDLING                    ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<Customer> customers = createCustomers();

        // Creating Optional
        System.out.println("--- Creating Optional ---");
        Optional<String> empty = Optional.empty();
        Optional<String> present = Optional.of("UPI");
        Optional<String> nullable = Optional.ofNullable(null);

        System.out.println("Empty: " + empty);
        System.out.println("Present: " + present);
        System.out.println("Nullable: " + nullable);

        // Checking and getting
        System.out.println("\n--- Checking and Getting Values ---");
        if (present.isPresent()) {
            System.out.println("Value: " + present.get());
        }

        // Better way - ifPresent
        present.ifPresent(v -> System.out.println("Using ifPresent: " + v));

        // ifPresentOrElse (Java 9+)
        System.out.println("\n--- ifPresentOrElse ---");
        Optional<Customer> vipCustomer = customers.stream()
                .filter(Customer::isVIP)
                .findFirst();

        vipCustomer.ifPresentOrElse(
                c -> System.out.println("VIP found: " + c.getName()),
                () -> System.out.println("No VIP customer")
        );

        // orElse vs orElseGet
        System.out.println("\n--- orElse vs orElseGet ---");
        String channel1 = empty.orElse("DEFAULT");  // Always evaluates default
        System.out.println("orElse: " + channel1);

        String channel2 = empty.orElseGet(() -> "COMPUTED_" + System.currentTimeMillis());
        System.out.println("orElseGet: " + channel2);  // Lazy evaluation

        // orElseThrow
        System.out.println("\n--- orElseThrow ---");
        try {
            String channel3 = empty.orElseThrow(
                    () -> new RuntimeException("Channel not found"));
        } catch (RuntimeException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // map and flatMap on Optional
        System.out.println("\n--- map on Optional ---");
        Optional<String> upperChannel = present.map(String::toUpperCase);
        System.out.println("Mapped: " + upperChannel);

        Optional<Integer> length = present.map(String::length);
        System.out.println("Length: " + length);

        // Chaining with filter
        System.out.println("\n--- filter on Optional ---");
        Optional<String> filteredChannel = present
                .filter(c -> c.length() > 2)
                .map(String::toLowerCase);
        System.out.println("Filtered and mapped: " + filteredChannel);

        // flatMap for nested Optional
        System.out.println("\n--- flatMap for Nested Optional ---");
        Optional<Customer> customer = customers.stream()
                .filter(c -> c.getCustomerId().equals("C001"))
                .findFirst();

        // If getCity() returns Optional<String>
        Optional<String> city = customer.map(Customer::getCity);
        System.out.println("City: " + city);

        // or() - return alternative Optional (Java 9+)
        System.out.println("\n--- or() for Alternative Optional ---");
        Optional<Customer> result = customers.stream()
                .filter(c -> c.getCustomerId().equals("C999"))
                .findFirst()
                .or(() -> customers.stream().findFirst());
        result.ifPresent(c -> System.out.println("Result: " + c.getName()));

        // stream() on Optional (Java 9+)
        System.out.println("\n--- Optional.stream() ---");
        List<String> names = customers.stream()
                .map(c -> Optional.ofNullable(c.getCity()))
                .flatMap(Optional::stream)  // Filters out empty Optionals
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Cities: " + names);
    }

    /**
     * Infinite and generated streams
     */
    public static void demonstrateInfiniteStreams() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║         INFINITE STREAMS                      ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Generate transaction IDs
        System.out.println("--- Generate Transaction IDs ---");
        Stream.generate(() -> "TXN" + System.nanoTime())
                .limit(5)
                .forEach(id -> System.out.println("  " + id));

        // Iterate with seed
        System.out.println("\n--- Generate Amount Series ---");
        Stream.iterate(1000, n -> n + 500)
                .limit(5)
                .forEach(amt -> System.out.println("  Rs." + amt));

        // Iterate with predicate (Java 9+)
        System.out.println("\n--- Iterate with Predicate ---");
        Stream.iterate(100, n -> n <= 500, n -> n + 100)
                .forEach(amt -> System.out.println("  Rs." + amt));

        // Fibonacci sequence
        System.out.println("\n--- Fibonacci Sequence ---");
        Stream.iterate(new long[]{0, 1}, arr -> new long[]{arr[1], arr[0] + arr[1]})
                .limit(10)
                .map(arr -> arr[0])
                .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // Random amounts for testing
        System.out.println("\n--- Random Transaction Amounts ---");
        Random random = new Random();
        Stream.generate(() -> random.nextInt(100000) + 1)
                .limit(5)
                .forEach(amt -> System.out.println("  Rs." + amt));

        // IntStream range
        System.out.println("\n--- IntStream for Account Numbers ---");
        IntStream.rangeClosed(1, 5)
                .mapToObj(i -> "ACC" + String.format("%06d", i))
                .forEach(acc -> System.out.println("  " + acc));
    }

    /**
     * Stream performance tips
     */
    public static void demonstratePerformanceTips() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║       STREAM PERFORMANCE TIPS                 ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<Transaction> transactions = createLargeTransactionList(100000);

        // Tip 1: Order of operations matters
        System.out.println("--- Tip 1: Filter Before Map ---");

        long start1 = System.currentTimeMillis();
        // Less efficient: map then filter
        transactions.stream()
                .map(Transaction::getAmount)
                .filter(a -> a > 50000)
                .count();
        long end1 = System.currentTimeMillis();
        System.out.println("Map then Filter: " + (end1 - start1) + " ms");

        long start2 = System.currentTimeMillis();
        // More efficient: filter then map
        transactions.stream()
                .filter(t -> t.getAmount() > 50000)
                .map(Transaction::getAmount)
                .count();
        long end2 = System.currentTimeMillis();
        System.out.println("Filter then Map: " + (end2 - start2) + " ms");

        // Tip 2: Use primitive streams
        System.out.println("\n--- Tip 2: Use Primitive Streams ---");

        long start3 = System.currentTimeMillis();
        // Boxing overhead
        Double sum1 = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(0.0, Double::sum);
        long end3 = System.currentTimeMillis();
        System.out.println("With boxing: " + (end3 - start3) + " ms");

        long start4 = System.currentTimeMillis();
        // No boxing
        double sum2 = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
        long end4 = System.currentTimeMillis();
        System.out.println("Primitive stream: " + (end4 - start4) + " ms");

        // Tip 3: Avoid stateful operations in parallel
        System.out.println("\n--- Tip 3: Stateless Operations in Parallel ---");
        System.out.println("Prefer: filter, map, flatMap");
        System.out.println("Avoid: sorted, distinct, limit (with unordered)");

        // Tip 4: Choose right source
        System.out.println("\n--- Tip 4: Choose Right Source ---");
        System.out.println("ArrayList: O(1) split, good for parallel");
        System.out.println("LinkedList: O(n) split, poor for parallel");
        System.out.println("IntStream.range: Excellent for parallel");

        // Tip 5: Short-circuit when possible
        System.out.println("\n--- Tip 5: Short-Circuit Operations ---");
        System.out.println("anyMatch, allMatch, noneMatch - stop early");
        System.out.println("findFirst, findAny - stop when found");
        System.out.println("limit - stop after n elements");
    }

    // ═══════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════

    private static List<BankAccount> createLargeAccountList(int size) {
        Random random = new Random();
        List<BankAccount> accounts = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            accounts.add(new BankAccount(
                    "A" + String.format("%08d", i),
                    "C" + String.format("%06d", i % 1000),
                    random.nextBoolean() ? "SAVINGS" : "CURRENT",
                    random.nextDouble() * 500000,
                    "BR" + String.format("%03d", i % 100)
            ));
        }
        return accounts;
    }

    private static List<Transaction> createLargeTransactionList(int size) {
        Random random = new Random();
        String[] channels = {"UPI", "IMPS", "NEFT", "RTGS", "ATM"};
        List<Transaction> transactions = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            transactions.add(new Transaction(
                    "T" + String.format("%010d", i),
                    "A" + String.format("%08d", i % 10000),
                    random.nextBoolean() ? "CREDIT" : "DEBIT",
                    random.nextDouble() * 100000,
                    0,
                    channels[random.nextInt(channels.length)]
            ));
        }
        return transactions;
    }

    private static List<Customer> createCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("C001", "Ramesh Kumar", "ramesh@email.com", "9876543210",
                LocalDate.of(1985, 5, 15), "Mumbai", "Maharashtra", "VIP", 1500000));
        customers.add(new Customer("C002", "Priya Sharma", "priya@gmail.com", "9876543211",
                LocalDate.of(1990, 8, 22), "Delhi", "Delhi", "PREMIUM", 800000));
        customers.add(new Customer("C003", "Amit Patel", "amit@yahoo.com", "9876543212",
                LocalDate.of(1988, 3, 10), null, "Maharashtra", "REGULAR", 500000));
        return customers;
    }
}