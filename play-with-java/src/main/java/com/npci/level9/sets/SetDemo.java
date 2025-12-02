package com.npci.level9.sets;

import com.npci.level9.model.*;
import java.util.*;

/**
 * Level 9: Collections Framework - Set Demonstrations
 *
 * Set Interface:
 * - No duplicates allowed
 * - At most one null element (HashSet, LinkedHashSet)
 *
 * Implementations:
 * - HashSet: Fast, no order, uses hashCode()
 * - LinkedHashSet: Maintains insertion order
 * - TreeSet: Sorted order, uses Comparable/Comparator
 */
public class SetDemo {

    /**
     * HashSet - Fast, unordered, no duplicates
     */
    public static void demonstrateHashSet() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║             HASHSET DEMO                      ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // HashSet of customers
        Set<Customer> customers = new HashSet<>();

        // Adding elements
        System.out.println("--- Adding Customers ---");
        customers.add(new Customer("C001", "Ramesh Kumar", "ramesh@email.com", "9876543210"));
        customers.add(new Customer("C002", "Priya Sharma", "priya@email.com", "9876543211"));
        customers.add(new Customer("C003", "Amit Patel", "amit@email.com", "9876543212"));

        // Try adding duplicate (same customerId)
        boolean added = customers.add(new Customer("C001", "Ramesh K", "ramesh2@email.com", "9999999999"));
        System.out.println("Duplicate C001 added: " + added);  // false

        System.out.println("Total customers: " + customers.size());  // 3

        // Iteration (order not guaranteed)
        System.out.println("\n--- All Customers (order not guaranteed) ---");
        for (Customer c : customers) {
            System.out.println("  " + c);
        }

        // Contains check
        System.out.println("\n--- Contains Check ---");
        Customer searchCustomer = new Customer("C002", "", "", "");
        System.out.println("Contains C002: " + customers.contains(searchCustomer));

        // Remove
        System.out.println("\n--- Remove C002 ---");
        customers.remove(searchCustomer);
        System.out.println("After removal, size: " + customers.size());

        // HashSet of unique account types
        System.out.println("\n--- Unique Account Types ---");
        Set<String> accountTypes = new HashSet<>();
        accountTypes.add("SAVINGS");
        accountTypes.add("CURRENT");
        accountTypes.add("FD");
        accountTypes.add("SAVINGS");  // Duplicate - ignored
        accountTypes.add("CURRENT");  // Duplicate - ignored
        System.out.println("Unique types: " + accountTypes);

        // HashSet of unique transaction channels
        Set<String> channels = new HashSet<>(Arrays.asList(
                "UPI", "IMPS", "NEFT", "RTGS", "UPI", "IMPS"  // Duplicates ignored
        ));
        System.out.println("Unique channels: " + channels);
    }

    /**
     * LinkedHashSet - Maintains insertion order
     */
    public static void demonstrateLinkedHashSet() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          LINKEDHASHSET DEMO                   ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // LinkedHashSet maintains insertion order
        Set<Branch> branches = new LinkedHashSet<>();

        branches.add(new Branch("BR001", "Mumbai Main", "NPCI0BR001", "Mumbai", "Maharashtra", "WEST"));
        branches.add(new Branch("BR002", "Delhi Central", "NPCI0BR002", "Delhi", "Delhi", "NORTH"));
        branches.add(new Branch("BR003", "Chennai Hub", "NPCI0BR003", "Chennai", "Tamil Nadu", "SOUTH"));
        branches.add(new Branch("BR004", "Kolkata Branch", "NPCI0BR004", "Kolkata", "West Bengal", "EAST"));
        branches.add(new Branch("BR005", "Bangalore Tech", "NPCI0BR005", "Bangalore", "Karnataka", "SOUTH"));

        System.out.println("--- Branches (Insertion Order Preserved) ---");
        for (Branch b : branches) {
            System.out.println("  " + b);
        }

        // Add duplicate
        branches.add(new Branch("BR001", "Mumbai Updated", "NPCI0BR001", "Mumbai", "Maharashtra", "WEST"));
        System.out.println("\nAfter adding duplicate BR001, size: " + branches.size());  // Still 5

        // Use case: Maintaining unique login history in order
        Set<String> loginHistory = new LinkedHashSet<>();
        loginHistory.add("9:00 AM - Login");
        loginHistory.add("10:00 AM - Transaction");
        loginHistory.add("11:00 AM - Logout");
        loginHistory.add("2:00 PM - Login");
        loginHistory.add("9:00 AM - Login");  // Duplicate - ignored but was already first

        System.out.println("\n--- Login History (Ordered) ---");
        loginHistory.forEach(event -> System.out.println("  " + event));
    }

    /**
     * TreeSet - Sorted order
     */
    public static void demonstrateTreeSet() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║            TREESET DEMO                       ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // TreeSet with natural ordering (uses Comparable)
        TreeSet<Customer> customers = new TreeSet<>();

        customers.add(new Customer("C003", "Amit Patel", "amit@email.com", "9876543212"));
        customers.add(new Customer("C001", "Ramesh Kumar", "ramesh@email.com", "9876543210"));
        customers.add(new Customer("C005", "Vikram Reddy", "vikram@email.com", "9876543214"));
        customers.add(new Customer("C002", "Priya Sharma", "priya@email.com", "9876543211"));
        customers.add(new Customer("C004", "Neha Singh", "neha@email.com", "9876543213"));

        System.out.println("--- Customers (Sorted by Customer ID) ---");
        for (Customer c : customers) {
            System.out.println("  " + c);
        }

        // TreeSet specific methods
        System.out.println("\n--- TreeSet Specific Methods ---");
        System.out.println("First: " + customers.first());
        System.out.println("Last: " + customers.last());

        // NavigableSet methods
        Customer c003 = new Customer("C003", "", "", "");
        System.out.println("\nLower than C003: " + customers.lower(c003));   // C002
        System.out.println("Higher than C003: " + customers.higher(c003)); // C004
        System.out.println("Floor of C003: " + customers.floor(c003));     // C003 or lower
        System.out.println("Ceiling of C003: " + customers.ceiling(c003)); // C003 or higher

        // Subsets
        System.out.println("\n--- Subsets ---");
        Customer c002 = new Customer("C002", "", "", "");
        Customer c004 = new Customer("C004", "", "", "");

        System.out.println("HeadSet (before C003): " + customers.headSet(c003));
        System.out.println("TailSet (from C003): " + customers.tailSet(c003));
        System.out.println("SubSet (C002 to C004): " + customers.subSet(c002, c004));

        // Descending order
        System.out.println("\n--- Descending Order ---");
        NavigableSet<Customer> descending = customers.descendingSet();
        descending.forEach(c -> System.out.println("  " + c));

        // TreeSet with Comparator (custom ordering)
        System.out.println("\n--- TreeSet with Custom Comparator (by Name) ---");
        TreeSet<Customer> byName = new TreeSet<>(
                Comparator.comparing(Customer::getName)
        );
        byName.addAll(customers);
        byName.forEach(c -> System.out.println("  " + c));

        // TreeSet of transactions sorted by amount
        System.out.println("\n--- Transactions Sorted by Amount ---");
        TreeSet<Transaction> byAmount = new TreeSet<>(
                Comparator.comparing(Transaction::getAmount).reversed()
        );
        byAmount.add(new Transaction("T1", "A1", "CREDIT", 50000, 50000, "UPI"));
        byAmount.add(new Transaction("T2", "A1", "DEBIT", 10000, 40000, "ATM"));
        byAmount.add(new Transaction("T3", "A1", "CREDIT", 75000, 115000, "NEFT"));
        byAmount.add(new Transaction("T4", "A1", "DEBIT", 25000, 90000, "IMPS"));

        byAmount.forEach(t -> System.out.println("  " + t));
    }

    /**
     * Set operations - union, intersection, difference
     */
    public static void demonstrateSetOperations() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          SET OPERATIONS                       ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Customers who use UPI
        Set<String> upiUsers = new HashSet<>(Arrays.asList("C001", "C002", "C003", "C005"));

        // Customers who use IMPS
        Set<String> impsUsers = new HashSet<>(Arrays.asList("C002", "C003", "C004", "C006"));

        System.out.println("UPI Users: " + upiUsers);
        System.out.println("IMPS Users: " + impsUsers);

        // Union - all users who use either
        Set<String> union = new HashSet<>(upiUsers);
        union.addAll(impsUsers);
        System.out.println("\nUnion (UPI or IMPS): " + union);

        // Intersection - users who use both
        Set<String> intersection = new HashSet<>(upiUsers);
        intersection.retainAll(impsUsers);
        System.out.println("Intersection (UPI and IMPS): " + intersection);

        // Difference - UPI only users
        Set<String> upiOnly = new HashSet<>(upiUsers);
        upiOnly.removeAll(impsUsers);
        System.out.println("Difference (UPI only): " + upiOnly);

        // Difference - IMPS only users
        Set<String> impsOnly = new HashSet<>(impsUsers);
        impsOnly.removeAll(upiUsers);
        System.out.println("Difference (IMPS only): " + impsOnly);

        // Symmetric difference (either but not both)
        Set<String> symmetric = new HashSet<>(upiUsers);
        symmetric.addAll(impsUsers);
        Set<String> both = new HashSet<>(upiUsers);
        both.retainAll(impsUsers);
        symmetric.removeAll(both);
        System.out.println("Symmetric Difference: " + symmetric);

        // Check subset
        System.out.println("\n--- Subset Check ---");
        Set<String> premiumUsers = new HashSet<>(Arrays.asList("C002", "C003"));
        System.out.println("Premium Users: " + premiumUsers);
        System.out.println("Premium subset of UPI users: " + upiUsers.containsAll(premiumUsers));

        // Disjoint check (no common elements)
        Set<String> newUsers = new HashSet<>(Arrays.asList("C007", "C008"));
        System.out.println("New Users disjoint from UPI: " + Collections.disjoint(newUsers, upiUsers));
    }
}