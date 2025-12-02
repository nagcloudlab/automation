package com.npci.level9.lists;

import com.npci.level9.model.*;
import java.util.*;

/**
 * Level 9: Collections Framework - List Demonstrations
 *
 * List Interface:
 * - Ordered collection (maintains insertion order)
 * - Allows duplicates
 * - Index-based access
 *
 * Implementations:
 * - ArrayList: Fast random access, slow insert/delete in middle
 * - LinkedList: Fast insert/delete, slow random access
 * - Vector: Synchronized (thread-safe), legacy
 */
public class ListDemo {

    // ═══════════════════════════════════════════════════════════════
    // ARRAYLIST DEMONSTRATIONS
    // ═══════════════════════════════════════════════════════════════

    /**
     * ArrayList basics - most commonly used List implementation
     */
    public static void demonstrateArrayList() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║            ARRAYLIST DEMO                     ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Creating ArrayList
        List<Customer> customers = new ArrayList<>();

        // Adding elements
        System.out.println("--- Adding Customers ---");
        customers.add(new Customer("C001", "Ramesh Kumar", "ramesh@email.com", "9876543210"));
        customers.add(new Customer("C002", "Priya Sharma", "priya@email.com", "9876543211"));
        customers.add(new Customer("C003", "Amit Patel", "amit@email.com", "9876543212"));
        customers.add(new Customer("C004", "Neha Singh", "neha@email.com", "9876543213"));
        customers.add(new Customer("C005", "Vikram Reddy", "vikram@email.com", "9876543214"));

        System.out.println("Added " + customers.size() + " customers");

        // Accessing by index
        System.out.println("\n--- Accessing by Index ---");
        System.out.println("First customer: " + customers.get(0));
        System.out.println("Last customer: " + customers.get(customers.size() - 1));

        // Inserting at specific position
        System.out.println("\n--- Inserting at Position 2 ---");
        customers.add(2, new Customer("C006", "Sanjay Gupta", "sanjay@email.com", "9876543215"));
        System.out.println("Customer at index 2: " + customers.get(2));

        // Updating element
        System.out.println("\n--- Updating Element ---");
        Customer oldCustomer = customers.get(0);
        oldCustomer.setCustomerType("PREMIUM");
        System.out.println("Updated: " + customers.get(0));

        // Checking if element exists
        System.out.println("\n--- Contains Check ---");
        Customer searchCustomer = new Customer("C001", "Ramesh Kumar", "ramesh@email.com", "9876543210");
        System.out.println("Contains C001: " + customers.contains(searchCustomer));

        // Finding index
        System.out.println("\n--- Finding Index ---");
        int index = customers.indexOf(searchCustomer);
        System.out.println("Index of C001: " + index);

        // Iterating - for-each
        System.out.println("\n--- Iterating (for-each) ---");
        for (Customer customer : customers) {
            System.out.println("  " + customer);
        }

        // Iterating - Iterator
        System.out.println("\n--- Iterating (Iterator) ---");
        Iterator<Customer> iterator = customers.iterator();
        while (iterator.hasNext()) {
            Customer c = iterator.next();
            System.out.println("  " + c.getName());
        }

        // Sublist
        System.out.println("\n--- Sublist (index 1 to 3) ---");
        List<Customer> subList = customers.subList(1, 4);
        for (Customer c : subList) {
            System.out.println("  " + c);
        }

        // Removing elements
        System.out.println("\n--- Removing Elements ---");
        customers.remove(0);  // Remove by index
        System.out.println("After removing index 0, size: " + customers.size());

        customers.remove(searchCustomer);  // Remove by object
        System.out.println("After removing C001 object, size: " + customers.size());

        // Clear all
        // customers.clear();
        // System.out.println("After clear, size: " + customers.size());
    }

    /**
     * LinkedList - Best for frequent insertions/deletions
     */
    public static void demonstrateLinkedList() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║           LINKEDLIST DEMO                     ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // LinkedList as List
        LinkedList<Transaction> transactions = new LinkedList<>();

        // Adding to end
        transactions.add(new Transaction("TXN001", "ACC001", "CREDIT", 50000, 50000, "BRANCH"));
        transactions.add(new Transaction("TXN002", "ACC001", "DEBIT", 10000, 40000, "ATM"));
        transactions.add(new Transaction("TXN003", "ACC001", "CREDIT", 25000, 65000, "UPI"));

        // LinkedList specific methods
        System.out.println("--- LinkedList Specific Methods ---");

        // Add to beginning
        transactions.addFirst(new Transaction("TXN000", "ACC001", "CREDIT", 100000, 100000, "BRANCH", "Opening Deposit"));
        System.out.println("After addFirst: " + transactions.getFirst());

        // Add to end
        transactions.addLast(new Transaction("TXN004", "ACC001", "DEBIT", 5000, 60000, "UPI"));
        System.out.println("After addLast: " + transactions.getLast());

        // Peek (view without removing)
        System.out.println("\n--- Peek Operations ---");
        System.out.println("peekFirst: " + transactions.peekFirst());
        System.out.println("peekLast: " + transactions.peekLast());

        // Poll (remove and return)
        System.out.println("\n--- Poll Operations ---");
        Transaction first = transactions.pollFirst();
        System.out.println("pollFirst removed: " + first);
        System.out.println("New first: " + transactions.getFirst());

        // Size
        System.out.println("\nTotal transactions: " + transactions.size());

        // Iterate in reverse
        System.out.println("\n--- Reverse Iteration ---");
        Iterator<Transaction> descIterator = transactions.descendingIterator();
        while (descIterator.hasNext()) {
            System.out.println("  " + descIterator.next());
        }
    }

    /**
     * List operations - sorting, searching, etc.
     */
    public static void demonstrateListOperations() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          LIST OPERATIONS                      ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(new BankAccount("ACC003", "C001", "SAVINGS", 75000, "BR001"));
        accounts.add(new BankAccount("ACC001", "C002", "CURRENT", 150000, "BR002"));
        accounts.add(new BankAccount("ACC005", "C003", "SAVINGS", 25000, "BR001"));
        accounts.add(new BankAccount("ACC002", "C004", "FD", 500000, "BR003"));
        accounts.add(new BankAccount("ACC004", "C005", "SAVINGS", 100000, "BR002"));

        // Natural sorting (uses Comparable)
        System.out.println("--- Before Sorting ---");
        accounts.forEach(a -> System.out.println("  " + a));

        Collections.sort(accounts);  // Uses compareTo()

        System.out.println("\n--- After Natural Sort (by Account Number) ---");
        accounts.forEach(a -> System.out.println("  " + a));

        // Sort by balance (using Comparator)
        System.out.println("\n--- Sort by Balance (Descending) ---");
        accounts.sort((a1, a2) -> Double.compare(a2.getBalance(), a1.getBalance()));
        accounts.forEach(a -> System.out.println("  " + a));

        // Sort by account type
        System.out.println("\n--- Sort by Account Type ---");
        accounts.sort(Comparator.comparing(BankAccount::getAccountType));
        accounts.forEach(a -> System.out.println("  " + a));

        // Reverse
        System.out.println("\n--- Reversed ---");
        Collections.reverse(accounts);
        accounts.forEach(a -> System.out.println("  " + a));

        // Shuffle
        System.out.println("\n--- Shuffled ---");
        Collections.shuffle(accounts);
        accounts.forEach(a -> System.out.println("  " + a));

        // Binary search (list must be sorted first)
        Collections.sort(accounts);
        BankAccount searchKey = new BankAccount("ACC003", "", "", 0, "");
        int foundIndex = Collections.binarySearch(accounts, searchKey);
        System.out.println("\n--- Binary Search for ACC003 ---");
        System.out.println("Found at index: " + foundIndex);

        // Min and Max
        System.out.println("\n--- Min and Max by Balance ---");
        BankAccount minBalance = Collections.min(accounts,
                Comparator.comparing(BankAccount::getBalance));
        BankAccount maxBalance = Collections.max(accounts,
                Comparator.comparing(BankAccount::getBalance));
        System.out.println("Min Balance: " + minBalance);
        System.out.println("Max Balance: " + maxBalance);

        // Frequency
        accounts.add(new BankAccount("ACC001", "C002", "CURRENT", 150000, "BR002"));
        System.out.println("\n--- Frequency of ACC001 ---");
        int freq = Collections.frequency(accounts,
                new BankAccount("ACC001", "", "", 0, ""));
        System.out.println("ACC001 appears " + freq + " times");

        // Unmodifiable list
        System.out.println("\n--- Unmodifiable List ---");
        List<BankAccount> readOnly = Collections.unmodifiableList(accounts);
        try {
            readOnly.add(new BankAccount("ACC999", "C099", "SAVINGS", 1000, "BR001"));
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiable list!");
        }
    }

    /**
     * ArrayList vs LinkedList performance comparison
     */
    public static void comparePerformance() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║   ARRAYLIST vs LINKEDLIST PERFORMANCE        ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        int size = 100000;

        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        // Add elements
        long start, end;

        start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList add (end): " + (end - start) + " ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            linkedList.add(i);
        }
        end = System.currentTimeMillis();
        System.out.println("LinkedList add (end): " + (end - start) + " ms");

        // Random access
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            arrayList.get(size / 2);
        }
        end = System.currentTimeMillis();
        System.out.println("\nArrayList random access: " + (end - start) + " ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            linkedList.get(size / 2);
        }
        end = System.currentTimeMillis();
        System.out.println("LinkedList random access: " + (end - start) + " ms");

        // Insert at beginning
        start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            arrayList.add(0, i);
        }
        end = System.currentTimeMillis();
        System.out.println("\nArrayList insert at start: " + (end - start) + " ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            linkedList.add(0, i);
        }
        end = System.currentTimeMillis();
        System.out.println("LinkedList insert at start: " + (end - start) + " ms");

        System.out.println("\n--- Summary ---");
        System.out.println("ArrayList: Best for random access, iteration");
        System.out.println("LinkedList: Best for frequent insert/delete at ends");
    }
}