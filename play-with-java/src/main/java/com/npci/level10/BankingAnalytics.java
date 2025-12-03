package com.npci.level10;

import com.npci.level10.model.*;
import java.util.*;
import java.util.stream.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Level 10: Streams API - Real-World Banking Analytics
 *
 * Demonstrates practical use of Streams for banking analytics.
 */
public class BankingAnalytics {

    private List<Customer> customers;
    private List<BankAccount> accounts;
    private List<Transaction> transactions;
    private List<Branch> branches;

    public BankingAnalytics() {
        initializeData();
    }

    private void initializeData() {
        // Initialize branches
        branches = Arrays.asList(
                new Branch("BR001", "Mumbai Main", "NPCI0BR001", "Mumbai", "Maharashtra", "WEST", "METRO"),
                new Branch("BR002", "Delhi Central", "NPCI0BR002", "Delhi", "Delhi", "NORTH", "METRO"),
                new Branch("BR003", "Chennai Hub", "NPCI0BR003", "Chennai", "Tamil Nadu", "SOUTH", "METRO"),
                new Branch("BR004", "Pune Branch", "NPCI0BR004", "Pune", "Maharashtra", "WEST", "URBAN"),
                new Branch("BR005", "Jaipur Branch", "NPCI0BR005", "Jaipur", "Rajasthan", "NORTH", "URBAN")
        );

        // Initialize customers
        customers = Arrays.asList(
                new Customer("C001", "Ramesh Kumar", "ramesh@email.com", "9876543210",
                        LocalDate.of(1985, 5, 15), "Mumbai", "Maharashtra", "VIP", 1500000),
                new Customer("C002", "Priya Sharma", "priya@gmail.com", "9876543211",
                        LocalDate.of(1990, 8, 22), "Delhi", "Delhi", "PREMIUM", 800000),
                new Customer("C003", "Amit Patel", "amit@yahoo.com", "9876543212",
                        LocalDate.of(1988, 3, 10), "Mumbai", "Maharashtra", "REGULAR", 500000),
                new Customer("C004", "Neha Singh", "neha@email.com", "9876543213",
                        LocalDate.of(1992, 11, 5), "Chennai", "Tamil Nadu", "VIP", 2000000),
                new Customer("C005", "Vikram Reddy", "vikram@gmail.com", "9876543214",
                        LocalDate.of(1995, 7, 18), "Bangalore", "Karnataka", "REGULAR", 400000),
                new Customer("C006", "Anjali Gupta", "anjali@email.com", "9876543215",
                        LocalDate.of(1987, 2, 28), "Pune", "Maharashtra", "PREMIUM", 900000),
                new Customer("C007", "Suresh Menon", "suresh@email.com", "9876543216",
                        LocalDate.of(1980, 9, 12), "Jaipur", "Rajasthan", "REGULAR", 350000)
        );

        // Initialize accounts
        accounts = Arrays.asList(
                new BankAccount("A001", "C001", "SAVINGS", 500000, "BR001"),
                new BankAccount("A002", "C001", "CURRENT", 1500000, "BR001"),
                new BankAccount("A003", "C002", "SAVINGS", 250000, "BR002"),
                new BankAccount("A004", "C002", "FD", 500000, "BR002"),
                new BankAccount("A005", "C003", "SAVINGS", 75000, "BR001"),
                new BankAccount("A006", "C004", "SAVINGS", 800000, "BR003"),
                new BankAccount("A007", "C004", "CURRENT", 2000000, "BR003"),
                new BankAccount("A008", "C005", "SAVINGS", 45000, "BR004"),
                new BankAccount("A009", "C006", "SAVINGS", 300000, "BR004"),
                new BankAccount("A010", "C006", "FD", 600000, "BR004"),
                new BankAccount("A011", "C007", "SAVINGS", 120000, "BR005")
        );

        // Initialize transactions
        transactions = new ArrayList<>();
        String[] channels = {"UPI", "IMPS", "NEFT", "RTGS", "ATM", "BRANCH"};
        String[] categories = {"SALARY", "SHOPPING", "BILLS", "TRANSFER", "EMI", "CASH"};
        Random random = new Random(42);

        LocalDateTime baseTime = LocalDateTime.now().minusDays(30);
        for (int i = 0; i < 100; i++) {
            String accNum = accounts.get(random.nextInt(accounts.size())).getAccountNumber();
            boolean isCredit = random.nextBoolean();
            double amount = (random.nextInt(100) + 1) * 1000;
            LocalDateTime time = baseTime.plusHours(random.nextInt(720));

            Transaction txn = new Transaction(
                    "T" + String.format("%06d", i + 1),
                    accNum,
                    isCredit ? "CREDIT" : "DEBIT",
                    amount,
                    0,
                    channels[random.nextInt(channels.length)],
                    time,
                    random.nextDouble() > 0.05 ? "SUCCESS" : "FAILED",
                    "",
                    categories[random.nextInt(categories.length)]
            );
            transactions.add(txn);
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // ANALYTICS METHODS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Get total deposits by branch
     */
    public Map<String, Double> getTotalDepositsByBranch() {
        return accounts.stream()
                .collect(Collectors.groupingBy(
                        BankAccount::getBranchCode,
                        Collectors.summingDouble(BankAccount::getBalance)
                ));
    }

    /**
     * Get customer count by type
     */
    public Map<String, Long> getCustomerCountByType() {
        return customers.stream()
                .collect(Collectors.groupingBy(
                        Customer::getCustomerType,
                        Collectors.counting()
                ));
    }

    /**
     * Get top N customers by total balance
     */
    public List<Map.Entry<String, Double>> getTopCustomersByBalance(int n) {
        Map<String, Double> customerBalances = accounts.stream()
                .collect(Collectors.groupingBy(
                        BankAccount::getCustomerId,
                        Collectors.summingDouble(BankAccount::getBalance)
                ));

        return customerBalances.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Get transaction volume by channel
     */
    public Map<String, DoubleSummaryStatistics> getTransactionStatsByChannel() {
        return transactions.stream()
                .filter(Transaction::isSuccessful)
                .collect(Collectors.groupingBy(
                        Transaction::getChannel,
                        Collectors.summarizingDouble(Transaction::getAmount)
                ));
    }

    /**
     * Get daily transaction trend
     */
    public Map<LocalDate, Double> getDailyTransactionVolume() {
        return transactions.stream()
                .filter(Transaction::isSuccessful)
                .collect(Collectors.groupingBy(
                        Transaction::getDate,
                        TreeMap::new,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    /**
     * Get hourly transaction distribution
     */
    public Map<Integer, Long> getHourlyTransactionCount() {
        return transactions.stream()
                .filter(Transaction::isSuccessful)
                .collect(Collectors.groupingBy(
                        Transaction::getHour,
                        TreeMap::new,
                        Collectors.counting()
                ));
    }

    /**
     * Get accounts below minimum balance
     */
    public List<BankAccount> getAccountsBelowMinimum() {
        return accounts.stream()
                .filter(a -> !a.isAboveMinimum())
                .sorted(Comparator.comparing(BankAccount::getBalance))
                .collect(Collectors.toList());
    }

    /**
     * Get failed transaction rate by channel
     */
    public Map<String, Double> getFailureRateByChannel() {
        Map<String, Long> total = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getChannel,
                        Collectors.counting()
                ));

        Map<String, Long> failed = transactions.stream()
                .filter(Transaction::isFailed)
                .collect(Collectors.groupingBy(
                        Transaction::getChannel,
                        Collectors.counting()
                ));

        return total.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> failed.getOrDefault(e.getKey(), 0L) * 100.0 / e.getValue()
                ));
    }

    /**
     * Get customer transaction summary
     */
    public Map<String, Map<String, Double>> getCustomerTransactionSummary() {
        // Get account to customer mapping
        Map<String, String> accountToCustomer = accounts.stream()
                .collect(Collectors.toMap(
                        BankAccount::getAccountNumber,
                        BankAccount::getCustomerId
                ));

        // Group transactions by customer and type
        return transactions.stream()
                .filter(Transaction::isSuccessful)
                .collect(Collectors.groupingBy(
                        t -> accountToCustomer.getOrDefault(t.getAccountNumber(), "UNKNOWN"),
                        Collectors.groupingBy(
                                Transaction::getType,
                                Collectors.summingDouble(Transaction::getAmount)
                        )
                ));
    }

    /**
     * Display analytics dashboard
     */
    public void displayDashboard() {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              BANKING ANALYTICS DASHBOARD                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        // Summary
        System.out.println("═══ SUMMARY ═══");
        System.out.println("Total Customers: " + customers.size());
        System.out.println("Total Accounts: " + accounts.size());
        System.out.println("Total Transactions: " + transactions.size());
        System.out.printf("Total Deposits: Rs.%,.2f%n",
                accounts.stream().mapToDouble(BankAccount::getBalance).sum());

        // Deposits by Branch
        System.out.println("\n═══ DEPOSITS BY BRANCH ═══");
        getTotalDepositsByBranch().forEach((branch, total) ->
                System.out.printf("  %s: Rs.%,.2f%n", branch, total));

        // Customer Types
        System.out.println("\n═══ CUSTOMER TYPES ═══");
        getCustomerCountByType().forEach((type, count) ->
                System.out.println("  " + type + ": " + count));

        // Top 5 Customers
        System.out.println("\n═══ TOP 5 CUSTOMERS BY BALANCE ═══");
        getTopCustomersByBalance(5).forEach(entry -> {
            String name = customers.stream()
                    .filter(c -> c.getCustomerId().equals(entry.getKey()))
                    .findFirst()
                    .map(Customer::getName)
                    .orElse("Unknown");
            System.out.printf("  %s (%s): Rs.%,.2f%n", name, entry.getKey(), entry.getValue());
        });

        // Transaction Stats by Channel
        System.out.println("\n═══ TRANSACTION STATS BY CHANNEL ═══");
        getTransactionStatsByChannel().forEach((channel, stats) -> {
            System.out.printf("  %s: %d txns, Total: Rs.%,.2f, Avg: Rs.%,.2f%n",
                    channel, stats.getCount(), stats.getSum(), stats.getAverage());
        });

        // Failure Rates
        System.out.println("\n═══ FAILURE RATE BY CHANNEL ═══");
        getFailureRateByChannel().forEach((channel, rate) ->
                System.out.printf("  %s: %.2f%%%n", channel, rate));

        // Hourly Distribution
        System.out.println("\n═══ PEAK TRANSACTION HOURS ═══");
        getHourlyTransactionCount().entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> System.out.printf("  %02d:00 - %d transactions%n", e.getKey(), e.getValue()));

        // Accounts Below Minimum
        List<BankAccount> belowMin = getAccountsBelowMinimum();
        System.out.println("\n═══ ACCOUNTS BELOW MINIMUM BALANCE ═══");
        if (belowMin.isEmpty()) {
            System.out.println("  None");
        } else {
            belowMin.forEach(a -> System.out.printf("  %s: Rs.%,.2f (Min: Rs.%,.2f)%n",
                    a.getAccountNumber(), a.getBalance(), a.getMinimumBalance()));
        }

        System.out.println("\n════════════════════════════════════════════════════════════════");
    }
}