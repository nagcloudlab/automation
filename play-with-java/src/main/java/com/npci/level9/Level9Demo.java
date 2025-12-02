package com.npci.level9;

import com.npci.level9.model.*;
import com.npci.level9.lists.*;
import com.npci.level9.sets.*;
import com.npci.level9.maps.*;
import com.npci.level9.queues.*;
import com.npci.level9.comparators.*;
import java.time.LocalDate;

/**
 * Level 9: Demo - Collections Framework
 */
public class Level9Demo {

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║     LEVEL 9: COLLECTIONS FRAMEWORK                   ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        // Demo 1: Lists
        System.out.println("\n▶ DEMO 1: List Interface");
        System.out.println("─────────────────────────────────────────────\n");
        demo1_Lists();

        // Demo 2: Sets
        System.out.println("\n▶ DEMO 2: Set Interface");
        System.out.println("─────────────────────────────────────────────\n");
        demo2_Sets();

        // Demo 3: Maps
        System.out.println("\n▶ DEMO 3: Map Interface");
        System.out.println("─────────────────────────────────────────────\n");
        demo3_Maps();

        // Demo 4: Queues
        System.out.println("\n▶ DEMO 4: Queue and Deque");
        System.out.println("─────────────────────────────────────────────\n");
        demo4_Queues();

        // Demo 5: Comparators
        System.out.println("\n▶ DEMO 5: Comparators");
        System.out.println("─────────────────────────────────────────────\n");
        demo5_Comparators();

        // Demo 6: Complete Banking System
        System.out.println("\n▶ DEMO 6: Complete Banking System");
        System.out.println("─────────────────────────────────────────────\n");
        demo6_BankingSystem();

        // Demo 7: Summary
        System.out.println("\n▶ DEMO 7: Collections Framework Summary");
        System.out.println("─────────────────────────────────────────────\n");
        demo7_Summary();
    }

    static void demo1_Lists() {
        ListDemo.demonstrateArrayList();
        ListDemo.demonstrateLinkedList();
        ListDemo.demonstrateListOperations();
        // ListDemo.comparePerformance();  // Uncomment for performance comparison
    }

    static void demo2_Sets() {
        SetDemo.demonstrateHashSet();
        SetDemo.demonstrateLinkedHashSet();
        SetDemo.demonstrateTreeSet();
        SetDemo.demonstrateSetOperations();
    }

    static void demo3_Maps() {
        MapDemo.demonstrateHashMap();
        MapDemo.demonstrateLinkedHashMap();
        MapDemo.demonstrateTreeMap();
        MapDemo.demonstrateAdvancedMapOperations();
    }

    static void demo4_Queues() {
        QueueDemo.demonstrateQueue();
        QueueDemo.demonstratePriorityQueue();
        QueueDemo.demonstrateDeque();
        QueueDemo.demonstrateBankingQueues();
    }

    static void demo5_Comparators() {
        ComparatorDemo.demonstrateComparators();
    }

    static void demo6_BankingSystem() {
        BankingCollections bank = new BankingCollections();

        // Add branches
        bank.addBranch(new Branch("BR001", "Mumbai Main", "NPCI0BR001", "Mumbai", "Maharashtra", "WEST"));
        bank.addBranch(new Branch("BR002", "Delhi Central", "NPCI0BR002", "Delhi", "Delhi", "NORTH"));
        bank.addBranch(new Branch("BR003", "Chennai Hub", "NPCI0BR003", "Chennai", "Tamil Nadu", "SOUTH"));

        // Add customers
        Customer c1 = new Customer("C001", "Ramesh Kumar", "ramesh@email.com", "9876543210");
        c1.setCustomerType("VIP");
        bank.addCustomer(c1);

        Customer c2 = new Customer("C002", "Priya Sharma", "priya@email.com", "9876543211");
        c2.setCustomerType("PREMIUM");
        bank.addCustomer(c2);

        Customer c3 = new Customer("C003", "Amit Patel", "amit@email.com", "9876543212");
        c3.setCustomerType("REGULAR");
        bank.addCustomer(c3);

        // Add accounts
        bank.addAccount(new BankAccount("ACC001", "C001", "SAVINGS", 500000, "BR001"));
        bank.addAccount(new BankAccount("ACC002", "C001", "CURRENT", 1000000, "BR001"));
        bank.addAccount(new BankAccount("ACC003", "C002", "SAVINGS", 250000, "BR002"));
        bank.addAccount(new BankAccount("ACC004", "C003", "SAVINGS", 75000, "BR003"));
        bank.addAccount(new BankAccount("ACC005", "C003", "FD", 200000, "BR003"));

        // Queue some transactions
        bank.queueTransaction(new Transaction("TXN001", "ACC001", "CREDIT", 50000, 550000, "UPI", "Salary"));
        bank.queueTransaction(new Transaction("TXN002", "ACC002", "DEBIT", 100000, 900000, "NEFT", "Vendor Payment"));
        bank.queueTransaction(new Transaction("TXN003", "ACC003", "CREDIT", 25000, 275000, "IMPS", "Transfer"));
        bank.queueTransaction(new Transaction("TXN004", "ACC004", "DEBIT", 5000, 70000, "ATM", "Cash Withdrawal"));

        // Process transactions
        bank.processAllPendingTransactions();

        // Display summary
        bank.displaySummary();
    }

    static void demo7_Summary() {
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║            COLLECTIONS FRAMEWORK SUMMARY                           ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                    ║");
        System.out.println("║  Collection Hierarchy:                                             ║");
        System.out.println("║                                                                    ║");
        System.out.println("║                      Iterable                                      ║");
        System.out.println("║                         │                                          ║");
        System.out.println("║                    Collection                                      ║");
        System.out.println("║                    /    │    \\                                     ║");
        System.out.println("║                 List   Set   Queue                                 ║");
        System.out.println("║                  │      │      │                                   ║");
        System.out.println("║           ArrayList  HashSet  LinkedList                           ║");
        System.out.println("║           LinkedList TreeSet  PriorityQueue                        ║");
        System.out.println("║           Vector     LinkedHashSet  ArrayDeque                     ║");
        System.out.println("║                                                                    ║");
        System.out.println("║  Map (separate hierarchy):                                         ║");
        System.out.println("║                                                                    ║");
        System.out.println("║                       Map                                          ║");
        System.out.println("║                    /   │   \\                                       ║");
        System.out.println("║              HashMap TreeMap LinkedHashMap                         ║");
        System.out.println("║                                                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              WHEN TO USE WHICH COLLECTION                          ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Need                          │ Use                                ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Ordered, indexed, duplicates  │ ArrayList                          ║");
        System.out.println("║ Frequent insert/delete        │ LinkedList                         ║");
        System.out.println("║ Unique elements, no order     │ HashSet                            ║");
        System.out.println("║ Unique elements, sorted       │ TreeSet                            ║");
        System.out.println("║ Unique elements, insertion    │ LinkedHashSet                      ║");
        System.out.println("║ Key-value, fast lookup        │ HashMap                            ║");
        System.out.println("║ Key-value, sorted keys        │ TreeMap                            ║");
        System.out.println("║ Key-value, insertion order    │ LinkedHashMap                      ║");
        System.out.println("║ FIFO processing               │ LinkedList (as Queue)              ║");
        System.out.println("║ Priority processing           │ PriorityQueue                      ║");
        System.out.println("║ Stack (LIFO)                  │ ArrayDeque                         ║");
        System.out.println("║ Thread-safe                   │ ConcurrentHashMap, etc.            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              IMPORTANT METHODS                                     ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ equals() & hashCode()  │ Required for HashSet/HashMap              ║");
        System.out.println("║ compareTo()            │ Required for TreeSet/TreeMap              ║");
        System.out.println("║ Comparator             │ Custom ordering for collections           ║");
        System.out.println("║ Collections.sort()     │ Sort any List                             ║");
        System.out.println("║ Collections.reverse()  │ Reverse a List                            ║");
        System.out.println("║ Collections.shuffle()  │ Randomize a List                          ║");
        System.out.println("║ Collections.min/max()  │ Find min/max element                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");

        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║              BANKING USE CASES                                     ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ Customer lookup        │ HashMap<CustomerId, Customer>             ║");
        System.out.println("║ Account lookup         │ HashMap<AccountNumber, Account>           ║");
        System.out.println("║ Transaction history    │ List<Transaction> per account             ║");
        System.out.println("║ Unique payment channels│ Set<String>                               ║");
        System.out.println("║ Transaction queue      │ Queue<Transaction>                        ║");
        System.out.println("║ Priority processing    │ PriorityQueue<Transaction>                ║");
        System.out.println("║ Undo operations        │ Deque<Action> (Stack)                     ║");
        System.out.println("║ Sorted transactions    │ TreeSet/TreeMap with Comparator           ║");
        System.out.println("║ Analytics/Grouping     │ Map<Key, List<Value>>                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
    }
}