package com.npci.level9.maps;

import com.npci.level9.model.*;
import java.util.*;

/**
 * Level 9: Collections Framework - Map Demonstrations
 *
 * Map Interface:
 * - Key-value pairs
 * - Keys must be unique
 * - One null key allowed (HashMap, LinkedHashMap)
 *
 * Implementations:
 * - HashMap: Fast, unordered
 * - LinkedHashMap: Maintains insertion order
 * - TreeMap: Sorted by keys
 * - Hashtable: Synchronized (legacy)
 */
public class MapDemo {

    /**
     * HashMap - Fast key-value storage
     */
    public static void demonstrateHashMap() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║             HASHMAP DEMO                      ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Map of customerId -> Customer
        Map<String, Customer> customerMap = new HashMap<>();

        // Adding entries
        System.out.println("--- Adding Customers ---");
        customerMap.put("C001", new Customer("C001", "Ramesh Kumar", "ramesh@email.com", "9876543210"));
        customerMap.put("C002", new Customer("C002", "Priya Sharma", "priya@email.com", "9876543211"));
        customerMap.put("C003", new Customer("C003", "Amit Patel", "amit@email.com", "9876543212"));

        System.out.println("Map size: " + customerMap.size());

        // Getting value
        System.out.println("\n--- Getting Values ---");
        Customer c001 = customerMap.get("C001");
        System.out.println("C001: " + c001);

        // Key not found
        Customer notFound = customerMap.get("C999");
        System.out.println("C999: " + notFound);  // null

        // getOrDefault
        Customer defaultCustomer = customerMap.getOrDefault("C999",
                new Customer("DEFAULT", "Unknown", "", ""));
        System.out.println("C999 with default: " + defaultCustomer);

        // Check if key exists
        System.out.println("\n--- Contains Check ---");
        System.out.println("Contains key C002: " + customerMap.containsKey("C002"));
        System.out.println("Contains value: " + customerMap.containsValue(c001));

        // Update value
        System.out.println("\n--- Updating Value ---");
        customerMap.put("C001", new Customer("C001", "Ramesh Kumar Updated", "ramesh.new@email.com", "9876543210"));
        System.out.println("Updated C001: " + customerMap.get("C001"));

        // putIfAbsent - only adds if key doesn't exist
        System.out.println("\n--- putIfAbsent ---");
        customerMap.putIfAbsent("C001", new Customer("C001", "Should Not Add", "", ""));
        customerMap.putIfAbsent("C004", new Customer("C004", "New Customer", "new@email.com", "9876543213"));
        System.out.println("C001 unchanged: " + customerMap.get("C001"));
        System.out.println("C004 added: " + customerMap.get("C004"));

        // Iterating
        System.out.println("\n--- Iterating (entrySet) ---");
        for (Map.Entry<String, Customer> entry : customerMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue().getName());
        }

        // Iterating keys
        System.out.println("\n--- Keys Only ---");
        for (String key : customerMap.keySet()) {
            System.out.println("  " + key);
        }

        // Iterating values
        System.out.println("\n--- Values Only ---");
        for (Customer customer : customerMap.values()) {
            System.out.println("  " + customer.getName());
        }

        // forEach with lambda
        System.out.println("\n--- forEach with Lambda ---");
        customerMap.forEach((key, value) ->
                System.out.println("  " + key + ": " + value.getName()));

        // Remove
        System.out.println("\n--- Remove ---");
        Customer removed = customerMap.remove("C004");
        System.out.println("Removed: " + removed);

        // Remove if matches
        boolean removedIf = customerMap.remove("C002", c001);  // Won't remove - wrong value
        System.out.println("Remove C002 with C001 value: " + removedIf);
    }

    /**
     * LinkedHashMap - Maintains insertion order
     */
    public static void demonstrateLinkedHashMap() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          LINKEDHASHMAP DEMO                   ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Account number -> Account (preserves insertion order)
        Map<String, BankAccount> accounts = new LinkedHashMap<>();

        accounts.put("ACC003", new BankAccount("ACC003", "C001", "SAVINGS", 75000, "BR001"));
        accounts.put("ACC001", new BankAccount("ACC001", "C002", "CURRENT", 150000, "BR002"));
        accounts.put("ACC002", new BankAccount("ACC002", "C003", "FD", 500000, "BR001"));

        System.out.println("--- Accounts (Insertion Order) ---");
        accounts.forEach((key, value) ->
                System.out.println("  " + key + " -> " + value));

        // LinkedHashMap with access order (LRU cache)
        System.out.println("\n--- LinkedHashMap with Access Order (LRU) ---");
        Map<String, String> lruCache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > 3;  // Keep only 3 entries
            }
        };

        lruCache.put("page1", "Content 1");
        lruCache.put("page2", "Content 2");
        lruCache.put("page3", "Content 3");

        System.out.println("Initial cache: " + lruCache.keySet());

        // Access page1 (moves to end)
        lruCache.get("page1");
        System.out.println("After accessing page1: " + lruCache.keySet());

        // Add page4 (removes least recently used - page2)
        lruCache.put("page4", "Content 4");
        System.out.println("After adding page4: " + lruCache.keySet());
    }

    /**
     * TreeMap - Sorted by keys
     */
    public static void demonstrateTreeMap() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║            TREEMAP DEMO                       ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Transactions sorted by transaction ID
        TreeMap<String, Transaction> transactions = new TreeMap<>();

        transactions.put("TXN003", new Transaction("TXN003", "ACC001", "CREDIT", 50000, 150000, "NEFT"));
        transactions.put("TXN001", new Transaction("TXN001", "ACC001", "CREDIT", 100000, 100000, "BRANCH"));
        transactions.put("TXN005", new Transaction("TXN005", "ACC001", "DEBIT", 25000, 125000, "UPI"));
        transactions.put("TXN002", new Transaction("TXN002", "ACC001", "DEBIT", 10000, 90000, "ATM"));
        transactions.put("TXN004", new Transaction("TXN004", "ACC001", "CREDIT", 75000, 200000, "IMPS"));

        System.out.println("--- Transactions (Sorted by ID) ---");
        transactions.forEach((key, value) ->
                System.out.println("  " + key + " -> Rs." + value.getAmount()));

        // TreeMap specific methods
        System.out.println("\n--- TreeMap Specific Methods ---");
        System.out.println("First key: " + transactions.firstKey());
        System.out.println("Last key: " + transactions.lastKey());
        System.out.println("First entry: " + transactions.firstEntry());
        System.out.println("Last entry: " + transactions.lastEntry());

        // Navigation
        System.out.println("\n--- Navigation ---");
        System.out.println("Lower key than TXN003: " + transactions.lowerKey("TXN003"));
        System.out.println("Higher key than TXN003: " + transactions.higherKey("TXN003"));
        System.out.println("Floor key of TXN003: " + transactions.floorKey("TXN003"));
        System.out.println("Ceiling key of TXN003: " + transactions.ceilingKey("TXN003"));

        // Submaps
        System.out.println("\n--- Submaps ---");
        System.out.println("HeadMap (before TXN003): " + transactions.headMap("TXN003").keySet());
        System.out.println("TailMap (from TXN003): " + transactions.tailMap("TXN003").keySet());
        System.out.println("SubMap (TXN002 to TXN004): " + transactions.subMap("TXN002", "TXN004").keySet());

        // Descending
        System.out.println("\n--- Descending Order ---");
        NavigableMap<String, Transaction> descending = transactions.descendingMap();
        descending.forEach((key, value) -> System.out.println("  " + key));

        // Poll (remove and return)
        System.out.println("\n--- Poll Operations ---");
        Map.Entry<String, Transaction> first = transactions.pollFirstEntry();
        System.out.println("Polled first: " + first.getKey());
        System.out.println("Remaining first: " + transactions.firstKey());
    }

    /**
     * Advanced Map operations
     */
    public static void demonstrateAdvancedMapOperations() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║       ADVANCED MAP OPERATIONS                 ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // compute, computeIfAbsent, computeIfPresent
        Map<String, Integer> transactionCounts = new HashMap<>();

        System.out.println("--- compute, computeIfAbsent, computeIfPresent ---");

        // computeIfAbsent - add if key doesn't exist
        transactionCounts.computeIfAbsent("UPI", k -> 0);
        transactionCounts.computeIfAbsent("IMPS", k -> 0);
        System.out.println("Initial: " + transactionCounts);

        // compute - update value
        transactionCounts.compute("UPI", (k, v) -> v + 1);
        transactionCounts.compute("UPI", (k, v) -> v + 1);
        transactionCounts.compute("IMPS", (k, v) -> v + 1);
        System.out.println("After increments: " + transactionCounts);

        // computeIfPresent - only if key exists
        transactionCounts.computeIfPresent("UPI", (k, v) -> v * 2);
        transactionCounts.computeIfPresent("NEFT", (k, v) -> v + 1);  // Won't add
        System.out.println("After computeIfPresent: " + transactionCounts);

        // merge
        System.out.println("\n--- merge ---");
        transactionCounts.merge("UPI", 5, Integer::sum);  // Add 5 to existing
        transactionCounts.merge("NEFT", 3, Integer::sum);  // New key, value = 3
        System.out.println("After merge: " + transactionCounts);

        // replaceAll
        System.out.println("\n--- replaceAll ---");
        transactionCounts.replaceAll((k, v) -> v * 10);
        System.out.println("After replaceAll (*10): " + transactionCounts);

        // Map of Maps (nested)
        System.out.println("\n--- Nested Map ---");
        Map<String, Map<String, Double>> branchWiseDeposits = new HashMap<>();

        branchWiseDeposits.put("BR001", new HashMap<>());
        branchWiseDeposits.get("BR001").put("SAVINGS", 5000000.0);
        branchWiseDeposits.get("BR001").put("CURRENT", 8000000.0);
        branchWiseDeposits.get("BR001").put("FD", 15000000.0);

        branchWiseDeposits.computeIfAbsent("BR002", k -> new HashMap<>());
        branchWiseDeposits.get("BR002").put("SAVINGS", 3000000.0);
        branchWiseDeposits.get("BR002").put("CURRENT", 5000000.0);

        System.out.println("Branch-wise deposits:");
        branchWiseDeposits.forEach((branch, deposits) -> {
            System.out.println("  " + branch + ":");
            deposits.forEach((type, amount) ->
                    System.out.printf("    %s: Rs.%,.2f%n", type, amount));
        });

        // Counting occurrences
        System.out.println("\n--- Counting with Map ---");
        String[] channels = {"UPI", "UPI", "IMPS", "UPI", "NEFT", "IMPS", "UPI", "RTGS"};
        Map<String, Long> channelCount = new HashMap<>();

        for (String channel : channels) {
            channelCount.merge(channel, 1L, Long::sum);
        }
        System.out.println("Channel usage count: " + channelCount);

        // Grouping
        System.out.println("\n--- Grouping with Map ---");
        List<BankAccount> accounts = Arrays.asList(
                new BankAccount("A1", "C1", "SAVINGS", 50000, "BR001"),
                new BankAccount("A2", "C2", "CURRENT", 100000, "BR001"),
                new BankAccount("A3", "C3", "SAVINGS", 75000, "BR002"),
                new BankAccount("A4", "C4", "FD", 200000, "BR001"),
                new BankAccount("A5", "C5", "SAVINGS", 30000, "BR002")
        );

        Map<String, List<BankAccount>> byBranch = new HashMap<>();
        for (BankAccount acc : accounts) {
            byBranch.computeIfAbsent(acc.getBranchCode(), k -> new ArrayList<>()).add(acc);
        }

        System.out.println("Accounts grouped by branch:");
        byBranch.forEach((branch, accs) -> {
            System.out.println("  " + branch + ": " + accs.size() + " accounts");
        });
    }
}