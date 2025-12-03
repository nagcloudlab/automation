package com.npci.level9.queues;

import com.npci.level9.model.*;
import java.util.*;

/**
 * Level 9: Collections Framework - Queue Demonstrations
 *
 * Queue Interface:
 * - FIFO (First-In-First-Out) ordering
 * - Used for processing elements in order
 *
 * Deque Interface:
 * - Double-ended queue
 * - Can add/remove from both ends
 *
 * Implementations:
 * - LinkedList: General purpose queue
 * - PriorityQueue: Elements ordered by priority
 * - ArrayDeque: Fast double-ended queue
 */
public class QueueDemo {

    /**
     * Queue basics with LinkedList
     */
    public static void demonstrateQueue() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║              QUEUE DEMO                       ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Transaction processing queue
        Queue<Transaction> transactionQueue = new LinkedList<>();

        System.out.println("--- Adding Transactions to Queue ---");
        transactionQueue.offer(new Transaction("TXN001", "ACC001", "CREDIT", 50000, 50000, "UPI"));
        transactionQueue.offer(new Transaction("TXN002", "ACC002", "DEBIT", 10000, 40000, "ATM"));
        transactionQueue.offer(new Transaction("TXN003", "ACC001", "CREDIT", 25000, 75000, "IMPS"));
        transactionQueue.offer(new Transaction("TXN004", "ACC003", "DEBIT", 5000, 20000, "UPI"));

        System.out.println("Queue size: " + transactionQueue.size());

        // Peek - view head without removing
        System.out.println("\n--- Peek (View Head) ---");
        Transaction head = transactionQueue.peek();
        System.out.println("Head: " + head);
        System.out.println("Size after peek: " + transactionQueue.size());

        // Poll - remove and return head
        System.out.println("\n--- Poll (Remove Head) ---");
        Transaction processed = transactionQueue.poll();
        System.out.println("Processed: " + processed);
        System.out.println("Size after poll: " + transactionQueue.size());

        // Process all transactions
        System.out.println("\n--- Processing All Transactions ---");
        while (!transactionQueue.isEmpty()) {
            Transaction txn = transactionQueue.poll();
            System.out.println("  Processing: " + txn.getTransactionId() +
                    " | Rs." + txn.getAmount());
        }

        System.out.println("Queue empty: " + transactionQueue.isEmpty());

        // Difference between add/offer and remove/poll
        System.out.println("\n--- add vs offer, remove vs poll ---");
        System.out.println("offer: returns false if can't add (for bounded queues)");
        System.out.println("add: throws exception if can't add");
        System.out.println("poll: returns null if empty");
        System.out.println("remove: throws exception if empty");
        System.out.println("peek: returns null if empty");
        System.out.println("element: throws exception if empty");
    }

    /**
     * PriorityQueue - Elements processed by priority
     */
    public static void demonstratePriorityQueue() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          PRIORITYQUEUE DEMO                   ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Priority queue with natural ordering (Comparable)
        System.out.println("--- Natural Ordering (by Transaction ID) ---");
        PriorityQueue<Transaction> naturalOrder = new PriorityQueue<>();

        naturalOrder.offer(new Transaction("TXN003", "A1", "CREDIT", 50000, 50000, "UPI"));
        naturalOrder.offer(new Transaction("TXN001", "A1", "DEBIT", 10000, 40000, "ATM"));
        naturalOrder.offer(new Transaction("TXN005", "A1", "CREDIT", 25000, 65000, "IMPS"));
        naturalOrder.offer(new Transaction("TXN002", "A1", "DEBIT", 5000, 60000, "UPI"));

        System.out.println("Processing in priority order:");
        while (!naturalOrder.isEmpty()) {
            System.out.println("  " + naturalOrder.poll());
        }

        // Priority queue with custom comparator (by amount - highest first)
        System.out.println("\n--- Custom Ordering (by Amount, Highest First) ---");
        PriorityQueue<Transaction> byAmount = new PriorityQueue<>(
                Comparator.comparing(Transaction::getAmount).reversed()
        );

        byAmount.offer(new Transaction("T1", "A1", "CREDIT", 50000, 50000, "UPI"));
        byAmount.offer(new Transaction("T2", "A1", "CREDIT", 100000, 150000, "NEFT"));
        byAmount.offer(new Transaction("T3", "A1", "CREDIT", 25000, 175000, "IMPS"));
        byAmount.offer(new Transaction("T4", "A1", "CREDIT", 500000, 675000, "RTGS"));
        byAmount.offer(new Transaction("T5", "A1", "CREDIT", 10000, 685000, "UPI"));

        System.out.println("Processing highest amounts first:");
        while (!byAmount.isEmpty()) {
            Transaction t = byAmount.poll();
            System.out.printf("  %s: Rs.%,.2f%n", t.getTransactionId(), t.getAmount());
        }

        // Customer support queue - VIP first
        System.out.println("\n--- Customer Support Queue (VIP Priority) ---");
        PriorityQueue<Customer> supportQueue = new PriorityQueue<>(
                Comparator.comparing(Customer::getCustomerType, (t1, t2) -> {
                    // VIP > PREMIUM > REGULAR
                    Map<String, Integer> priority = Map.of("VIP", 1, "PREMIUM", 2, "REGULAR", 3);
                    return priority.getOrDefault(t1, 99).compareTo(priority.getOrDefault(t2, 99));
                }).thenComparing(Customer::getCustomerId)
        );

        Customer c1 = new Customer("C001", "Ramesh", "r@e.com", "1234567890");
        c1.setCustomerType("REGULAR");

        Customer c2 = new Customer("C002", "Priya", "p@e.com", "1234567891");
        c2.setCustomerType("VIP");

        Customer c3 = new Customer("C003", "Amit", "a@e.com", "1234567892");
        c3.setCustomerType("PREMIUM");

        Customer c4 = new Customer("C004", "Neha", "n@e.com", "1234567893");
        c4.setCustomerType("REGULAR");

        Customer c5 = new Customer("C005", "Vikram", "v@e.com", "1234567894");
        c5.setCustomerType("VIP");

        supportQueue.offer(c1);
        supportQueue.offer(c2);
        supportQueue.offer(c3);
        supportQueue.offer(c4);
        supportQueue.offer(c5);

        System.out.println("Processing support requests (VIP first):");
        while (!supportQueue.isEmpty()) {
            Customer c = supportQueue.poll();
            System.out.println("  Serving: " + c.getName() + " (" + c.getCustomerType() + ")");
        }
    }

    /**
     * Deque - Double-ended queue
     */
    public static void demonstrateDeque() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║              DEQUE DEMO                       ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // ArrayDeque - fast double-ended queue
        Deque<String> recentTransactions = new ArrayDeque<>();

        System.out.println("--- Adding to Both Ends ---");
        // Add to front
        recentTransactions.addFirst("TXN001 - Opening Balance");
        recentTransactions.addFirst("TXN000 - Account Created");

        // Add to back
        recentTransactions.addLast("TXN002 - First Deposit");
        recentTransactions.addLast("TXN003 - UPI Payment");
        recentTransactions.addLast("TXN004 - ATM Withdrawal");

        System.out.println("Deque contents:");
        recentTransactions.forEach(t -> System.out.println("  " + t));

        // Peek from both ends
        System.out.println("\n--- Peek from Both Ends ---");
        System.out.println("First: " + recentTransactions.peekFirst());
        System.out.println("Last: " + recentTransactions.peekLast());

        // Remove from both ends
        System.out.println("\n--- Remove from Both Ends ---");
        System.out.println("Removed first: " + recentTransactions.pollFirst());
        System.out.println("Removed last: " + recentTransactions.pollLast());

        System.out.println("Remaining: " + recentTransactions);

        // Use as Stack (LIFO)
        System.out.println("\n--- Deque as Stack (LIFO) ---");
        Deque<String> stack = new ArrayDeque<>();
        stack.push("Page 1 - Login");
        stack.push("Page 2 - Dashboard");
        stack.push("Page 3 - Transfer");
        stack.push("Page 4 - Confirm");

        System.out.println("Navigation history:");
        while (!stack.isEmpty()) {
            System.out.println("  Back to: " + stack.pop());
        }

        // Use as Queue (FIFO)
        System.out.println("\n--- Deque as Queue (FIFO) ---");
        Deque<String> queue = new ArrayDeque<>();
        queue.offer("Request 1");
        queue.offer("Request 2");
        queue.offer("Request 3");

        System.out.println("Processing requests:");
        while (!queue.isEmpty()) {
            System.out.println("  Processing: " + queue.poll());
        }

        // Iterate in reverse
        System.out.println("\n--- Reverse Iteration ---");
        Deque<String> txnHistory = new ArrayDeque<>();
        txnHistory.add("Jan - Rs.10000");
        txnHistory.add("Feb - Rs.15000");
        txnHistory.add("Mar - Rs.12000");
        txnHistory.add("Apr - Rs.20000");

        System.out.println("Transactions (newest first):");
        Iterator<String> descIterator = txnHistory.descendingIterator();
        while (descIterator.hasNext()) {
            System.out.println("  " + descIterator.next());
        }
    }

    /**
     * Real-world banking queue scenarios
     */
    public static void demonstrateBankingQueues() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║        BANKING QUEUE SCENARIOS                ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Scenario 1: Transaction processing queue
        System.out.println("--- Scenario 1: Transaction Batch Processing ---");
        Queue<Transaction> batchQueue = new LinkedList<>();

        // Add transactions for batch processing
        for (int i = 1; i <= 5; i++) {
            batchQueue.offer(new Transaction(
                    "BATCH" + String.format("%03d", i),
                    "ACC" + String.format("%03d", i),
                    i % 2 == 0 ? "CREDIT" : "DEBIT",
                    i * 10000,
                    i * 10000,
                    "NEFT"
            ));
        }

        System.out.println("Processing batch of " + batchQueue.size() + " transactions:");
        int processed = 0;
        while (!batchQueue.isEmpty()) {
            Transaction t = batchQueue.poll();
            processed++;
            System.out.printf("  [%d/%d] %s: Rs.%,.2f - %s%n",
                    processed, 5, t.getTransactionId(), t.getAmount(), t.getType());
        }

        // Scenario 2: RTGS priority processing
        System.out.println("\n--- Scenario 2: Payment Channel Priority ---");
        PriorityQueue<Transaction> channelPriority = new PriorityQueue<>(
                Comparator.comparing(Transaction::getChannel, (c1, c2) -> {
                    // RTGS > NEFT > IMPS > UPI
                    Map<String, Integer> priority = Map.of(
                            "RTGS", 1, "NEFT", 2, "IMPS", 3, "UPI", 4
                    );
                    return priority.getOrDefault(c1, 99).compareTo(priority.getOrDefault(c2, 99));
                })
        );

        channelPriority.offer(new Transaction("T1", "A1", "CREDIT", 5000, 0, "UPI"));
        channelPriority.offer(new Transaction("T2", "A2", "CREDIT", 500000, 0, "RTGS"));
        channelPriority.offer(new Transaction("T3", "A3", "CREDIT", 50000, 0, "IMPS"));
        channelPriority.offer(new Transaction("T4", "A4", "CREDIT", 100000, 0, "NEFT"));
        channelPriority.offer(new Transaction("T5", "A5", "CREDIT", 2000, 0, "UPI"));

        System.out.println("Processing by channel priority:");
        while (!channelPriority.isEmpty()) {
            Transaction t = channelPriority.poll();
            System.out.printf("  %s [%s]: Rs.%,.2f%n",
                    t.getTransactionId(), t.getChannel(), t.getAmount());
        }

        // Scenario 3: Undo stack for transactions
        System.out.println("\n--- Scenario 3: Transaction Undo Stack ---");
        Deque<Transaction> undoStack = new ArrayDeque<>();

        // Perform transactions
        undoStack.push(new Transaction("U1", "A1", "DEBIT", 1000, 49000, "UPI", "Bill Payment"));
        System.out.println("Performed: Bill Payment Rs.1000");

        undoStack.push(new Transaction("U2", "A1", "DEBIT", 5000, 44000, "UPI", "Mobile Recharge"));
        System.out.println("Performed: Mobile Recharge Rs.5000");

        undoStack.push(new Transaction("U3", "A1", "DEBIT", 2500, 41500, "UPI", "Shopping"));
        System.out.println("Performed: Shopping Rs.2500");

        // Undo last 2 transactions
        System.out.println("\nUndoing last 2 transactions:");
        for (int i = 0; i < 2 && !undoStack.isEmpty(); i++) {
            Transaction undone = undoStack.pop();
            System.out.println("  Undone: " + undone.getDescription() + " Rs." + undone.getAmount());
        }

        System.out.println("Remaining in stack: " + undoStack.size());
    }
}