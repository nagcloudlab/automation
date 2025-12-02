package com.npci.level9.comparators;

import com.npci.level9.model.*;
import java.util.*;

/**
 * Level 9: Collections Framework - Comparator Demonstrations
 *
 * Comparable vs Comparator:
 * - Comparable: Natural ordering, implemented by the class itself
 * - Comparator: External ordering, can have multiple for same class
 */
public class ComparatorDemo {

    public static void demonstrateComparators() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║          COMPARATOR DEMO                      ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Sample data
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("C003", "Amit Patel", "amit@email.com", "9876543212"));
        customers.add(new Customer("C001", "Ramesh Kumar", "ramesh@email.com", "9876543210"));
        customers.add(new Customer("C005", "Priya Sharma", "priya@email.com", "9876543214"));
        customers.add(new Customer("C002", "Neha Singh", "neha@email.com", "9876543211"));
        customers.add(new Customer("C004", "Vikram Reddy", "vikram@email.com", "9876543213"));

        customers.get(0).setCustomerType("PREMIUM");
        customers.get(1).setCustomerType("VIP");
        customers.get(2).setCustomerType("REGULAR");
        customers.get(3).setCustomerType("VIP");
        customers.get(4).setCustomerType("PREMIUM");

        // Natural ordering (Comparable - by customerId)
        System.out.println("--- Natural Ordering (Comparable) ---");
        Collections.sort(customers);
        customers.forEach(c -> System.out.println("  " + c.getCustomerId() + " - " + c.getName()));

        // Sort by name
        System.out.println("\n--- Sort by Name ---");
        customers.sort(Comparator.comparing(Customer::getName));
        customers.forEach(c -> System.out.println("  " + c.getName()));

        // Sort by name (case insensitive)
        System.out.println("\n--- Sort by Name (Case Insensitive) ---");
        customers.sort(Comparator.comparing(Customer::getName, String.CASE_INSENSITIVE_ORDER));
        customers.forEach(c -> System.out.println("  " + c.getName()));

        // Sort by name descending
        System.out.println("\n--- Sort by Name Descending ---");
        customers.sort(Comparator.comparing(Customer::getName).reversed());
        customers.forEach(c -> System.out.println("  " + c.getName()));

        // Sort by customer type then by name
        System.out.println("\n--- Sort by Type, then Name ---");
        customers.sort(
                Comparator.comparing(Customer::getCustomerType)
                        .thenComparing(Customer::getName)
        );
        customers.forEach(c -> System.out.println("  " + c.getCustomerType() + " - " + c.getName()));

        // Custom type ordering (VIP > PREMIUM > REGULAR)
        System.out.println("\n--- Custom Type Ordering (VIP first) ---");
        Map<String, Integer> typePriority = Map.of("VIP", 1, "PREMIUM", 2, "REGULAR", 3);

        customers.sort(Comparator.comparing(
                Customer::getCustomerType,
                (t1, t2) -> typePriority.getOrDefault(t1, 99).compareTo(typePriority.getOrDefault(t2, 99))
        ));
        customers.forEach(c -> System.out.println("  " + c.getCustomerType() + " - " + c.getName()));

        // Null-safe comparators
        System.out.println("\n--- Null-Safe Comparators ---");
        customers.get(2).setEmail(null);  // Set one email to null

        customers.sort(Comparator.comparing(
                Customer::getEmail,
                Comparator.nullsLast(String::compareTo)  // nulls at end
        ));
        customers.forEach(c -> System.out.println("  " + c.getName() + " - " + c.getEmail()));

        customers.get(2).setEmail("priya@email.com");  // Restore email

        // Comparator for transactions
        System.out.println("\n--- Transaction Comparators ---");
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("T3", "A1", "CREDIT", 50000, 150000, "NEFT"));
        transactions.add(new Transaction("T1", "A1", "DEBIT", 10000, 40000, "ATM"));
        transactions.add(new Transaction("T5", "A1", "CREDIT", 25000, 175000, "UPI"));
        transactions.add(new Transaction("T2", "A1", "CREDIT", 100000, 100000, "IMPS"));
        transactions.add(new Transaction("T4", "A1", "DEBIT", 5000, 170000, "UPI"));

        // By amount descending
        System.out.println("By Amount (Highest first):");
        transactions.sort(Comparator.comparing(Transaction::getAmount).reversed());
        transactions.forEach(t -> System.out.printf("  %s: Rs.%,.2f%n", t.getTransactionId(), t.getAmount()));

        // By type then amount
        System.out.println("\nBy Type, then Amount:");
        transactions.sort(
                Comparator.comparing(Transaction::getType)
                        .thenComparing(Transaction::getAmount, Comparator.reverseOrder())
        );
        transactions.forEach(t -> System.out.printf("  %s - %s: Rs.%,.2f%n",
                t.getType(), t.getTransactionId(), t.getAmount()));

        // By channel priority then timestamp
        System.out.println("\nBy Channel Priority:");
        Map<String, Integer> channelPriority = Map.of(
                "RTGS", 1, "NEFT", 2, "IMPS", 3, "UPI", 4, "ATM", 5
        );

        transactions.sort(Comparator.comparing(
                Transaction::getChannel,
                Comparator.comparingInt(c -> channelPriority.getOrDefault(c, 99))
        ));
        transactions.forEach(t -> System.out.printf("  %s [%s]: Rs.%,.2f%n",
                t.getTransactionId(), t.getChannel(), t.getAmount()));

        // Creating reusable comparators
        System.out.println("\n--- Reusable Comparators ---");

        Comparator<Transaction> byAmountDesc = Comparator.comparing(Transaction::getAmount).reversed();
        Comparator<Transaction> byChannel = Comparator.comparing(Transaction::getChannel);
        Comparator<Transaction> byType = Comparator.comparing(Transaction::getType);

        // Chain comparators
        Comparator<Transaction> combined = byType.thenComparing(byChannel).thenComparing(byAmountDesc);

        transactions.sort(combined);
        System.out.println("Combined sort (Type -> Channel -> Amount desc):");
        transactions.forEach(t -> System.out.printf("  %s | %s | %s | Rs.%,.2f%n",
                t.getType(), t.getChannel(), t.getTransactionId(), t.getAmount()));

        // Find min/max using comparator
        System.out.println("\n--- Min/Max with Comparator ---");
        Transaction maxAmount = Collections.max(transactions, byAmountDesc.reversed());
        Transaction minAmount = Collections.min(transactions, byAmountDesc.reversed());
        System.out.printf("Max amount: %s - Rs.%,.2f%n", maxAmount.getTransactionId(), maxAmount.getAmount());
        System.out.printf("Min amount: %s - Rs.%,.2f%n", minAmount.getTransactionId(), minAmount.getAmount());
    }
}