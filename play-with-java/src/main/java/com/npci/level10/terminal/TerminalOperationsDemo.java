package com.npci.level10.terminal;

import com.npci.level10.model.*;
import java.util.*;
import java.util.stream.*;
import java.time.LocalDate;

/**
 * Level 10: Streams API - Terminal Operations
 *
 * Terminal operations produce a result or side-effect.
 * After terminal operation, stream cannot be used again.
 *
 * Categories:
 * - Reduction: reduce(), collect()
 * - Search: findFirst(), findAny()
 * - Matching: anyMatch(), allMatch(), noneMatch()
 * - Aggregation: count(), min(), max()
 * - Iteration: forEach(), forEachOrdered()
 * - Collection: toArray()
 */
public class TerminalOperationsDemo {

    /**
     * reduce() - Combine elements into single result
     */
    public static void demonstrateReduce() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║          reduce() OPERATION                   ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<Transaction> transactions = createTransactions();

        // reduce with identity
        System.out.println("--- Sum with Identity Value ---");
        double total = transactions.stream()
                .filter(Transaction::isSuccessful)
                .map(Transaction::getAmount)
                .reduce(0.0, Double::sum);
        System.out.printf("Total: Rs.%,.2f%n", total);

        // reduce without identity (returns Optional)
        System.out.println("\n--- Sum without Identity (Returns Optional) ---");
        Optional<Double> totalOpt = transactions.stream()
                .filter(Transaction::isSuccessful)
                .map(Transaction::getAmount)
                .reduce(Double::sum);
        totalOpt.ifPresent(t -> System.out.printf("Total: Rs.%,.2f%n", t));

        // reduce for max
        System.out.println("\n--- Find Max Amount using reduce ---");
        Optional<Double> maxAmount = transactions.stream()
                .filter(Transaction::isSuccessful)
                .map(Transaction::getAmount)
                .reduce(Double::max);
        maxAmount.ifPresent(max -> System.out.printf("Max: Rs.%,.2f%n", max));

        // reduce for min
        System.out.println("\n--- Find Min Amount using reduce ---");
        Optional<Double> minAmount = transactions.stream()
                .filter(Transaction::isSuccessful)
                .map(Transaction::getAmount)
                .reduce(Double::min);
        minAmount.ifPresent(min -> System.out.printf("Min: Rs.%,.2f%n", min));

        // reduce for concatenation
        System.out.println("\n--- Concatenate Transaction IDs ---");
        String allIds = transactions.stream()
                .map(Transaction::getTransactionId)
                .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
        System.out.println("IDs: " + allIds);

        // reduce with combiner (for parallel streams)
        System.out.println("\n--- Reduce with Combiner (for parallel) ---");
        double parallelSum = transactions.parallelStream()
                .map(Transaction::getAmount)
                .reduce(0.0, Double::sum, Double::sum);
        System.out.printf("Parallel sum: Rs.%,.2f%n", parallelSum);

        // Custom reduction - count credits and debits
        System.out.println("\n--- Custom Reduction: Credit-Debit Summary ---");
        int[] summary = transactions.stream()
                .filter(Transaction::isSuccessful)
                .reduce(
                        new int[]{0, 0},  // {creditCount, debitCount}
                        (arr, t) -> {
                            if (t.isCredit()) arr[0]++;
                            else arr[1]++;
                            return arr;
                        },
                        (arr1, arr2) -> new int[]{arr1[0] + arr2[0], arr1[1] + arr2[1]}
                );
        System.out.println("Credits: " + summary[0] + ", Debits: " + summary[1]);
    }

    /**
     * collect() with Collectors - Transform stream to collection
     */
    public static void demonstrateCollect() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          collect() OPERATION                  ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<BankAccount> accounts = createAccounts();

        // Collect to List
        System.out.println("--- Collect to List ---");
        List<BankAccount> savingsList = accounts.stream()
                .filter(BankAccount::isSavings)
                .collect(Collectors.toList());
        System.out.println("Savings accounts: " + savingsList.size());

        // Collect to Set
        System.out.println("\n--- Collect to Set (Unique) ---");
        Set<String> branchCodes = accounts.stream()
                .map(BankAccount::getBranchCode)
                .collect(Collectors.toSet());
        System.out.println("Unique branches: " + branchCodes);

        // Collect to specific collection type
        System.out.println("\n--- Collect to TreeSet (Sorted) ---");
        TreeSet<String> sortedBranches = accounts.stream()
                .map(BankAccount::getBranchCode)
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println("Sorted branches: " + sortedBranches);

        // Collect to Map
        System.out.println("\n--- Collect to Map ---");
        Map<String, Double> accountBalances = accounts.stream()
                .collect(Collectors.toMap(
                        BankAccount::getAccountNumber,
                        BankAccount::getBalance
                ));
        System.out.println("Account balances: " + accountBalances);

        // Collect to Map with merge function (handle duplicates)
        System.out.println("\n--- Collect to Map with Merge ---");
        Map<String, Double> branchTotals = accounts.stream()
                .collect(Collectors.toMap(
                        BankAccount::getBranchCode,
                        BankAccount::getBalance,
                        Double::sum  // Merge function for duplicate keys
                ));
        branchTotals.forEach((branch, total) ->
                System.out.printf("  %s: Rs.%,.2f%n", branch, total));

        // joining
        System.out.println("\n--- Joining Strings ---");
        String accountList = accounts.stream()
                .map(BankAccount::getAccountNumber)
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("Accounts: " + accountList);

        // counting
        System.out.println("\n--- Counting ---");
        long activeCount = accounts.stream()
                .filter(BankAccount::isActive)
                .collect(Collectors.counting());
        System.out.println("Active accounts: " + activeCount);

        // summing
        System.out.println("\n--- Summing ---");
        double totalBalance = accounts.stream()
                .collect(Collectors.summingDouble(BankAccount::getBalance));
        System.out.printf("Total balance: Rs.%,.2f%n", totalBalance);

        // averaging
        System.out.println("\n--- Averaging ---");
        double avgBalance = accounts.stream()
                .collect(Collectors.averagingDouble(BankAccount::getBalance));
        System.out.printf("Average balance: Rs.%,.2f%n", avgBalance);

        // summarizing
        System.out.println("\n--- Summarizing Statistics ---");
        DoubleSummaryStatistics stats = accounts.stream()
                .collect(Collectors.summarizingDouble(BankAccount::getBalance));
        System.out.println("Count: " + stats.getCount());
        System.out.printf("Sum: Rs.%,.2f%n", stats.getSum());
        System.out.printf("Min: Rs.%,.2f%n", stats.getMin());
        System.out.printf("Max: Rs.%,.2f%n", stats.getMax());
        System.out.printf("Average: Rs.%,.2f%n", stats.getAverage());

        // maxBy / minBy
        System.out.println("\n--- MaxBy / MinBy ---");
        Optional<BankAccount> maxBalance = accounts.stream()
                .collect(Collectors.maxBy(Comparator.comparing(BankAccount::getBalance)));
        maxBalance.ifPresent(a -> System.out.printf("Highest balance: %s - Rs.%,.2f%n",
                a.getAccountNumber(), a.getBalance()));
    }

    /**
     * findFirst(), findAny() - Search operations
     */
    public static void demonstrateFind() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║     findFirst() / findAny() OPERATIONS       ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<Transaction> transactions = createTransactions();

        // findFirst
        System.out.println("--- findFirst() ---");
        Optional<Transaction> firstUPI = transactions.stream()
                .filter(Transaction::isUPI)
                .findFirst();

        firstUPI.ifPresentOrElse(
                t -> System.out.println("First UPI: " + t),
                () -> System.out.println("No UPI transaction found")
        );

        // findAny (useful for parallel streams)
        System.out.println("\n--- findAny() ---");
        Optional<Transaction> anyHighValue = transactions.parallelStream()
                .filter(t -> t.getAmount() > 50000)
                .findAny();

        anyHighValue.ifPresent(t -> System.out.println("Found high value: " + t));

        // Handling Optional
        System.out.println("\n--- Handling Optional ---");

        // orElse - default value
        String channel = transactions.stream()
                .filter(t -> t.getAmount() > 1000000)
                .map(Transaction::getChannel)
                .findFirst()
                .orElse("NOT_FOUND");
        System.out.println("Channel (orElse): " + channel);

        // orElseGet - lazy default
        String channelLazy = transactions.stream()
                .filter(t -> t.getAmount() > 1000000)
                .map(Transaction::getChannel)
                .findFirst()
                .orElseGet(() -> "DEFAULT_" + System.currentTimeMillis());
        System.out.println("Channel (orElseGet): " + channelLazy);

        // orElseThrow
        try {
            Transaction txn = transactions.stream()
                    .filter(t -> t.getAmount() > 1000000)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No high value transaction"));
        } catch (RuntimeException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * anyMatch(), allMatch(), noneMatch() - Matching operations
     */
    public static void demonstrateMatch() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║     anyMatch/allMatch/noneMatch OPERATIONS   ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<BankAccount> accounts = createAccounts();

        // anyMatch - true if any element matches
        System.out.println("--- anyMatch() ---");
        boolean hasHighBalance = accounts.stream()
                .anyMatch(a -> a.getBalance() > 400000);
        System.out.println("Any account with balance > Rs.4,00,000: " + hasHighBalance);

        boolean hasNegativeBalance = accounts.stream()
                .anyMatch(a -> a.getBalance() < 0);
        System.out.println("Any account with negative balance: " + hasNegativeBalance);

        // allMatch - true if all elements match
        System.out.println("\n--- allMatch() ---");
        boolean allActive = accounts.stream()
                .allMatch(BankAccount::isActive);
        System.out.println("All accounts active: " + allActive);

        boolean allAboveMinimum = accounts.stream()
                .allMatch(BankAccount::isAboveMinimum);
        System.out.println("All accounts above minimum balance: " + allAboveMinimum);

        // noneMatch - true if no element matches
        System.out.println("\n--- noneMatch() ---");
        boolean noneNegative = accounts.stream()
                .noneMatch(a -> a.getBalance() < 0);
        System.out.println("No accounts with negative balance: " + noneNegative);

        boolean noneFD = accounts.stream()
                .noneMatch(BankAccount::isFixedDeposit);
        System.out.println("No FD accounts: " + noneFD);

        // Short-circuit behavior
        System.out.println("\n--- Short-Circuit Behavior ---");
        boolean found = accounts.stream()
                .peek(a -> System.out.println("  Checking: " + a.getAccountNumber()))
                .anyMatch(a -> a.getBalance() > 100000);
        System.out.println("Found balance > Rs.1,00,000: " + found);
        System.out.println("(Notice: Stopped checking after finding match)");
    }

    /**
     * count(), min(), max() - Aggregation operations
     */
    public static void demonstrateAggregation() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║     count/min/max OPERATIONS                 ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<Transaction> transactions = createTransactions();

        // count
        System.out.println("--- count() ---");
        long totalCount = transactions.stream().count();
        System.out.println("Total transactions: " + totalCount);

        long successCount = transactions.stream()
                .filter(Transaction::isSuccessful)
                .count();
        System.out.println("Successful transactions: " + successCount);

        // min with Comparator
        System.out.println("\n--- min() ---");
        Optional<Transaction> minTxn = transactions.stream()
                .filter(Transaction::isSuccessful)
                .min(Comparator.comparing(Transaction::getAmount));
        minTxn.ifPresent(t -> System.out.printf("Min transaction: %s - Rs.%,.2f%n",
                t.getTransactionId(), t.getAmount()));

        // max with Comparator
        System.out.println("\n--- max() ---");
        Optional<Transaction> maxTxn = transactions.stream()
                .filter(Transaction::isSuccessful)
                .max(Comparator.comparing(Transaction::getAmount));
        maxTxn.ifPresent(t -> System.out.printf("Max transaction: %s - Rs.%,.2f%n",
                t.getTransactionId(), t.getAmount()));

        // With primitive streams
        System.out.println("\n--- Primitive Stream Aggregations ---");
        OptionalDouble minAmount = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .min();
        minAmount.ifPresent(min -> System.out.printf("Min amount: Rs.%,.2f%n", min));

        OptionalDouble maxAmount = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .max();
        maxAmount.ifPresent(max -> System.out.printf("Max amount: Rs.%,.2f%n", max));

        double sum = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
        System.out.printf("Sum: Rs.%,.2f%n", sum);

        OptionalDouble avg = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .average();
        avg.ifPresent(a -> System.out.printf("Average: Rs.%,.2f%n", a));
    }

    /**
     * forEach(), forEachOrdered() - Iteration operations
     */
    public static void demonstrateForEach() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║    forEach/forEachOrdered OPERATIONS         ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<String> channels = Arrays.asList("UPI", "IMPS", "NEFT", "RTGS", "ATM");

        // forEach
        System.out.println("--- forEach() ---");
        channels.stream()
                .forEach(c -> System.out.println("  " + c));

        // forEach with method reference
        System.out.println("\n--- forEach() with Method Reference ---");
        channels.stream()
                .map(String::toLowerCase)
                .forEach(System.out::println);

        // Parallel stream with forEach (order not guaranteed)
        System.out.println("\n--- Parallel forEach (Order NOT guaranteed) ---");
        channels.parallelStream()
                .forEach(c -> System.out.println("  " + Thread.currentThread().getName() + ": " + c));

        // forEachOrdered (maintains order even in parallel)
        System.out.println("\n--- Parallel forEachOrdered (Order maintained) ---");
        channels.parallelStream()
                .forEachOrdered(c -> System.out.println("  " + c));
    }

    /**
     * toArray() - Convert to array
     */
    public static void demonstrateToArray() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          toArray() OPERATION                  ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<BankAccount> accounts = createAccounts();

        // toArray without generator (returns Object[])
        System.out.println("--- toArray() returns Object[] ---");
        Object[] objectArray = accounts.stream()
                .filter(BankAccount::isSavings)
                .toArray();
        System.out.println("Array type: " + objectArray.getClass().getSimpleName());
        System.out.println("Array length: " + objectArray.length);

        // toArray with generator (returns specific type)
        System.out.println("\n--- toArray(generator) returns BankAccount[] ---");
        BankAccount[] accountArray = accounts.stream()
                .filter(BankAccount::isSavings)
                .toArray(BankAccount[]::new);
        System.out.println("Array type: " + accountArray.getClass().getSimpleName());
        System.out.println("Array length: " + accountArray.length);

        for (BankAccount a : accountArray) {
            System.out.println("  " + a);
        }

        // Primitive array
        System.out.println("\n--- Primitive Array ---");
        double[] balances = accounts.stream()
                .mapToDouble(BankAccount::getBalance)
                .toArray();
        System.out.println("Balances: " + Arrays.toString(balances));
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
}