package com.npci.level10.collectors;

import com.npci.level10.model.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.time.LocalDate;

/**
 * Level 10: Streams API - Advanced Collectors
 *
 * Collectors class provides factory methods for creating collectors.
 * Used with Stream.collect() terminal operation.
 */
public class CollectorsDemo {

    /**
     * groupingBy - Group elements by classifier
     */
    public static void demonstrateGroupingBy() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║         groupingBy() COLLECTOR                ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<BankAccount> accounts = createAccounts();
        List<Transaction> transactions = createTransactions();

        // Simple grouping
        System.out.println("--- Group Accounts by Type ---");
        Map<String, List<BankAccount>> byType = accounts.stream()
                .collect(Collectors.groupingBy(BankAccount::getAccountType));

        byType.forEach((type, accs) -> {
            System.out.println(type + ":");
            accs.forEach(a -> System.out.println("  " + a));
        });

        // Group by branch
        System.out.println("\n--- Group Accounts by Branch ---");
        Map<String, List<BankAccount>> byBranch = accounts.stream()
                .collect(Collectors.groupingBy(BankAccount::getBranchCode));

        byBranch.forEach((branch, accs) ->
                System.out.println(branch + ": " + accs.size() + " accounts"));

        // Group with counting
        System.out.println("\n--- Count by Account Type ---");
        Map<String, Long> countByType = accounts.stream()
                .collect(Collectors.groupingBy(
                        BankAccount::getAccountType,
                        Collectors.counting()
                ));
        System.out.println(countByType);

        // Group with summing
        System.out.println("\n--- Total Balance by Branch ---");
        Map<String, Double> totalByBranch = accounts.stream()
                .collect(Collectors.groupingBy(
                        BankAccount::getBranchCode,
                        Collectors.summingDouble(BankAccount::getBalance)
                ));
        totalByBranch.forEach((branch, total) ->
                System.out.printf("  %s: Rs.%,.2f%n", branch, total));

        // Group with averaging
        System.out.println("\n--- Average Balance by Type ---");
        Map<String, Double> avgByType = accounts.stream()
                .collect(Collectors.groupingBy(
                        BankAccount::getAccountType,
                        Collectors.averagingDouble(BankAccount::getBalance)
                ));
        avgByType.forEach((type, avg) ->
                System.out.printf("  %s: Rs.%,.2f%n", type, avg));

        // Group with mapping (get account numbers only)
        System.out.println("\n--- Account Numbers by Branch ---");
        Map<String, List<String>> numbersByBranch = accounts.stream()
                .collect(Collectors.groupingBy(
                        BankAccount::getBranchCode,
                        Collectors.mapping(
                                BankAccount::getAccountNumber,
                                Collectors.toList()
                        )
                ));
        numbersByBranch.forEach((branch, numbers) ->
                System.out.println(branch + ": " + numbers));

        // Group with joining
        System.out.println("\n--- Account Numbers Joined by Branch ---");
        Map<String, String> joinedByBranch = accounts.stream()
                .collect(Collectors.groupingBy(
                        BankAccount::getBranchCode,
                        Collectors.mapping(
                                BankAccount::getAccountNumber,
                                Collectors.joining(", ")
                        )
                ));
        joinedByBranch.forEach((branch, nums) ->
                System.out.println(branch + ": " + nums));

        // Group with max
        System.out.println("\n--- Highest Balance Account per Branch ---");
        Map<String, Optional<BankAccount>> maxByBranch = accounts.stream()
                .collect(Collectors.groupingBy(
                        BankAccount::getBranchCode,
                        Collectors.maxBy(Comparator.comparing(BankAccount::getBalance))
                ));
        maxByBranch.forEach((branch, acc) ->
                acc.ifPresent(a -> System.out.printf("  %s: %s - Rs.%,.2f%n",
                        branch, a.getAccountNumber(), a.getBalance())));

        // Nested grouping
        System.out.println("\n--- Nested Grouping: Branch -> Type ---");
        Map<String, Map<String, List<BankAccount>>> nestedGroup = accounts.stream()
                .collect(Collectors.groupingBy(
                        BankAccount::getBranchCode,
                        Collectors.groupingBy(BankAccount::getAccountType)
                ));

        nestedGroup.forEach((branch, typeMap) -> {
            System.out.println(branch + ":");
            typeMap.forEach((type, accs) ->
                    System.out.println("  " + type + ": " + accs.size()));
        });

        // Group with sorted result
        System.out.println("\n--- Group with TreeMap (Sorted Keys) ---");
        Map<String, Long> sortedCountByType = accounts.stream()
                .collect(Collectors.groupingBy(
                        BankAccount::getAccountType,
                        TreeMap::new,
                        Collectors.counting()
                ));
        System.out.println(sortedCountByType);
    }

    /**
     * partitioningBy - Split into two groups (true/false)
     */
    public static void demonstratePartitioningBy() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║       partitioningBy() COLLECTOR              ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<BankAccount> accounts = createAccounts();
        List<Transaction> transactions = createTransactions();

        // Simple partition
        System.out.println("--- Partition Accounts: Active vs Inactive ---");
        Map<Boolean, List<BankAccount>> activePartition = accounts.stream()
                .collect(Collectors.partitioningBy(BankAccount::isActive));

        System.out.println("Active: " + activePartition.get(true).size());
        System.out.println("Inactive: " + activePartition.get(false).size());

        // Partition with counting
        System.out.println("\n--- Partition Transactions: Success vs Failed ---");
        Map<Boolean, Long> successPartition = transactions.stream()
                .collect(Collectors.partitioningBy(
                        Transaction::isSuccessful,
                        Collectors.counting()
                ));
        System.out.println("Successful: " + successPartition.get(true));
        System.out.println("Failed: " + successPartition.get(false));

        // Partition with sum
        System.out.println("\n--- Partition Balance: Above vs Below Rs.1,00,000 ---");
        Map<Boolean, Double> balancePartition = accounts.stream()
                .collect(Collectors.partitioningBy(
                        a -> a.getBalance() >= 100000,
                        Collectors.summingDouble(BankAccount::getBalance)
                ));
        System.out.printf("Above 1L: Rs.%,.2f%n", balancePartition.get(true));
        System.out.printf("Below 1L: Rs.%,.2f%n", balancePartition.get(false));

        // Partition Credits vs Debits
        System.out.println("\n--- Partition: Credits vs Debits ---");
        Map<Boolean, List<Transaction>> creditDebit = transactions.stream()
                .collect(Collectors.partitioningBy(Transaction::isCredit));

        System.out.println("Credits:");
        creditDebit.get(true).forEach(t -> System.out.println("  " + t));
        System.out.println("Debits:");
        creditDebit.get(false).forEach(t -> System.out.println("  " + t));
    }

    /**
     * collectingAndThen - Transform result after collecting
     */
    public static void demonstrateCollectingAndThen() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║      collectingAndThen() COLLECTOR            ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<BankAccount> accounts = createAccounts();

        // Collect to unmodifiable list
        System.out.println("--- Collect to Unmodifiable List ---");
        List<BankAccount> unmodifiable = accounts.stream()
                .filter(BankAccount::isActive)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList
                ));
        System.out.println("Collected " + unmodifiable.size() + " accounts");

        try {
            unmodifiable.add(new BankAccount("X", "X", "X", 0, "X"));
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiable list!");
        }

        // Collect and get size
        System.out.println("\n--- Collect and Transform ---");
        int count = accounts.stream()
                .filter(BankAccount::isSavings)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        List::size
                ));
        System.out.println("Savings account count: " + count);

        // Collect to array via list
        System.out.println("\n--- Collect to Array via List ---");
        String[] accountNumbers = accounts.stream()
                .map(BankAccount::getAccountNumber)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> list.toArray(new String[0])
                ));
        System.out.println("Account numbers: " + Arrays.toString(accountNumbers));
    }

    /**
     * reducing - Custom reduction as collector
     */
    public static void demonstrateReducingCollector() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║         reducing() COLLECTOR                  ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<Transaction> transactions = createTransactions();

        // Sum as collector
        System.out.println("--- Sum Amounts using reducing ---");
        Double total = transactions.stream()
                .filter(Transaction::isSuccessful)
                .collect(Collectors.reducing(
                        0.0,
                        Transaction::getAmount,
                        Double::sum
                ));
        System.out.printf("Total: Rs.%,.2f%n", total);

        // Max as collector
        System.out.println("\n--- Max Transaction using reducing ---");
        Optional<Transaction> maxTxn = transactions.stream()
                .collect(Collectors.reducing(
                        (t1, t2) -> t1.getAmount() > t2.getAmount() ? t1 : t2
                ));
        maxTxn.ifPresent(t -> System.out.println("Max: " + t));

        // reducing with groupingBy
        System.out.println("\n--- Total per Channel using reducing ---");
        Map<String, Double> totalByChannel = transactions.stream()
                .filter(Transaction::isSuccessful)
                .collect(Collectors.groupingBy(
                        Transaction::getChannel,
                        Collectors.reducing(0.0, Transaction::getAmount, Double::sum)
                ));
        totalByChannel.forEach((ch, amt) ->
                System.out.printf("  %s: Rs.%,.2f%n", ch, amt));
    }

    /**
     * teeing - Combine two collectors (Java 12+)
     */
    public static void demonstrateTeeing() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║           teeing() COLLECTOR (Java 12+)       ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<BankAccount> accounts = createAccounts();

        // Calculate count and sum together
        System.out.println("--- Count and Sum in One Pass ---");
        Map.Entry<Long, Double> countAndSum = accounts.stream()
                .collect(Collectors.teeing(
                        Collectors.counting(),
                        Collectors.summingDouble(BankAccount::getBalance),
                        Map::entry
                ));
        System.out.println("Count: " + countAndSum.getKey());
        System.out.printf("Sum: Rs.%,.2f%n", countAndSum.getValue());
        System.out.printf("Average: Rs.%,.2f%n",
                countAndSum.getValue() / countAndSum.getKey());

        // Min and Max in one pass
        System.out.println("\n--- Min and Max in One Pass ---");
        Map.Entry<Optional<BankAccount>, Optional<BankAccount>> minMax = accounts.stream()
                .collect(Collectors.teeing(
                        Collectors.minBy(Comparator.comparing(BankAccount::getBalance)),
                        Collectors.maxBy(Comparator.comparing(BankAccount::getBalance)),
                        Map::entry
                ));

        minMax.getKey().ifPresent(min ->
                System.out.printf("Min: %s - Rs.%,.2f%n", min.getAccountNumber(), min.getBalance()));
        minMax.getValue().ifPresent(max ->
                System.out.printf("Max: %s - Rs.%,.2f%n", max.getAccountNumber(), max.getBalance()));
    }

    /**
     * Custom collector
     */
    public static void demonstrateCustomCollector() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          CUSTOM COLLECTOR                     ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<Transaction> transactions = createTransactions();

        // Custom collector to separate credits and debits with totals
        System.out.println("--- Custom Transaction Summary Collector ---");

        Collector<Transaction, TransactionSummary, TransactionSummary> summaryCollector =
                Collector.of(
                        TransactionSummary::new,               // Supplier
                        TransactionSummary::add,               // Accumulator
                        TransactionSummary::merge,             // Combiner
                        Collector.Characteristics.IDENTITY_FINISH
                );

        TransactionSummary summary = transactions.stream()
                .filter(Transaction::isSuccessful)
                .collect(summaryCollector);

        System.out.println(summary);
    }

    // Custom accumulator class
    static class TransactionSummary {
        private int creditCount = 0;
        private int debitCount = 0;
        private double creditTotal = 0;
        private double debitTotal = 0;

        void add(Transaction t) {
            if (t.isCredit()) {
                creditCount++;
                creditTotal += t.getAmount();
            } else {
                debitCount++;
                debitTotal += t.getAmount();
            }
        }

        TransactionSummary merge(TransactionSummary other) {
            creditCount += other.creditCount;
            debitCount += other.debitCount;
            creditTotal += other.creditTotal;
            debitTotal += other.debitTotal;
            return this;
        }

        @Override
        public String toString() {
            return String.format(
                    "Transaction Summary:\n" +
                            "  Credits: %d transactions, Rs.%,.2f\n" +
                            "  Debits: %d transactions, Rs.%,.2f\n" +
                            "  Net: Rs.%,.2f",
                    creditCount, creditTotal, debitCount, debitTotal, creditTotal - debitTotal);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════

    private static List<BankAccount> createAccounts() {
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new BankAccount("A001", "C001", "SAVINGS", 150000, "BR001"));
        accounts.add(new BankAccount("A002", "C002", "CURRENT", 500000, "BR001"));
        accounts.add(new BankAccount("A003", "C003", "SAVINGS", 75000, "BR002"));
        accounts.add(new BankAccount("A004", "C004", "FD", 200000, "BR002"));
        accounts.add(new BankAccount("A005", "C005", "SAVINGS", 25000, "BR003"));
        accounts.add(new BankAccount("A006", "C001", "SAVINGS", 50000, "BR001"));
        accounts.add(new BankAccount("A007", "C006", "CURRENT", 300000, "BR002"));

        BankAccount inactive = new BankAccount("A008", "C007", "SAVINGS", 10000, "BR003");
        inactive.setActive(false);
        accounts.add(inactive);

        return accounts;
    }

    private static List<Transaction> createTransactions() {
        List<Transaction> txns = new ArrayList<>();
        txns.add(new Transaction("T001", "A001", "CREDIT", 50000, 50000, "UPI", "Salary", "SALARY"));
        txns.add(new Transaction("T002", "A001", "DEBIT", 5000, 45000, "UPI", "Shopping", "SHOPPING"));
        txns.add(new Transaction("T003", "A002", "CREDIT", 100000, 100000, "NEFT", "Transfer", "TRANSFER"));
        txns.add(new Transaction("T004", "A001", "DEBIT", 2000, 43000, "ATM", "Cash", "CASH"));
        txns.add(new Transaction("T005", "A003", "CREDIT", 75000, 75000, "IMPS", "Refund", "REFUND"));
        txns.add(new Transaction("T006", "A002", "DEBIT", 20000, 80000, "NEFT", "Bill", "BILLS"));
        txns.add(new Transaction("T007", "A001", "CREDIT", 10000, 53000, "UPI", "Cashback", "CASHBACK"));

        Transaction failed = new Transaction("T008", "A002", "DEBIT", 1000, 79000, "UPI", "Failed", "SHOPPING");
        failed.setStatus("FAILED");
        txns.add(failed);

        return txns;
    }
}