package com.npci.level9;

import com.npci.level9.model.*;
import java.util.*;

/**
 * Level 9: Collections Framework - Banking System using Collections
 *
 * Demonstrates practical use of collections in a banking context.
 */
public class BankingCollections {

    // Storage using various collection types
    private Map<String, Customer> customers;           // HashMap - fast lookup
    private Map<String, BankAccount> accounts;         // HashMap - fast lookup
    private Map<String, Branch> branches;              // LinkedHashMap - insertion order
    private Map<String, List<Transaction>> transactionHistory;  // Account -> Transactions
    private TreeMap<Double, Set<String>> balanceIndex; // Balance -> Account numbers (sorted)
    private Set<String> activeChannels;                // HashSet - unique channels
    private Queue<Transaction> pendingTransactions;    // Processing queue
    private Deque<String> auditLog;                    // Recent audit entries

    public BankingCollections() {
        customers = new HashMap<>();
        accounts = new HashMap<>();
        branches = new LinkedHashMap<>();
        transactionHistory = new HashMap<>();
        balanceIndex = new TreeMap<>(Comparator.reverseOrder());
        activeChannels = new HashSet<>();
        pendingTransactions = new LinkedList<>();
        auditLog = new ArrayDeque<>();

        // Initialize channels
        activeChannels.addAll(Arrays.asList("UPI", "IMPS", "NEFT", "RTGS", "ATM", "BRANCH"));
    }

    // ═══════════════════════════════════════════════════════════════
    // BRANCH OPERATIONS
    // ═══════════════════════════════════════════════════════════════

    public void addBranch(Branch branch) {
        branches.put(branch.getBranchCode(), branch);
        audit("BRANCH_ADDED", branch.getBranchCode());
    }

    public Branch getBranch(String branchCode) {
        return branches.get(branchCode);
    }

    public Collection<Branch> getAllBranches() {
        return branches.values();
    }

    // ═══════════════════════════════════════════════════════════════
    // CUSTOMER OPERATIONS
    // ═══════════════════════════════════════════════════════════════

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
        audit("CUSTOMER_ADDED", customer.getCustomerId());
    }

    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    public List<Customer> searchCustomersByName(String namePart) {
        List<Customer> results = new ArrayList<>();
        for (Customer c : customers.values()) {
            if (c.getName().toLowerCase().contains(namePart.toLowerCase())) {
                results.add(c);
            }
        }
        return results;
    }

    public List<Customer> getCustomersByType(String type) {
        List<Customer> results = new ArrayList<>();
        for (Customer c : customers.values()) {
            if (c.getCustomerType().equals(type)) {
                results.add(c);
            }
        }
        results.sort(Comparator.comparing(Customer::getName));
        return results;
    }

    // ═══════════════════════════════════════════════════════════════
    // ACCOUNT OPERATIONS
    // ═══════════════════════════════════════════════════════════════

    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
        transactionHistory.put(account.getAccountNumber(), new ArrayList<>());
        updateBalanceIndex(account);
        audit("ACCOUNT_ADDED", account.getAccountNumber());
    }

    public BankAccount getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public List<BankAccount> getAccountsByCustomer(String customerId) {
        List<BankAccount> result = new ArrayList<>();
        for (BankAccount acc : accounts.values()) {
            if (acc.getCustomerId().equals(customerId)) {
                result.add(acc);
            }
        }
        return result;
    }

    public List<BankAccount> getAccountsByBranch(String branchCode) {
        List<BankAccount> result = new ArrayList<>();
        for (BankAccount acc : accounts.values()) {
            if (acc.getBranchCode().equals(branchCode)) {
                result.add(acc);
            }
        }
        return result;
    }

    public List<BankAccount> getTopAccountsByBalance(int n) {
        List<BankAccount> allAccounts = new ArrayList<>(accounts.values());
        allAccounts.sort(Comparator.comparing(BankAccount::getBalance).reversed());
        return allAccounts.subList(0, Math.min(n, allAccounts.size()));
    }

    private void updateBalanceIndex(BankAccount account) {
        // Remove from old balance entry
        balanceIndex.values().forEach(set -> set.remove(account.getAccountNumber()));

        // Add to new balance entry
        balanceIndex.computeIfAbsent(account.getBalance(), k -> new HashSet<>())
                .add(account.getAccountNumber());
    }

    // ═══════════════════════════════════════════════════════════════
    // TRANSACTION OPERATIONS
    // ═══════════════════════════════════════════════════════════════

    public void queueTransaction(Transaction transaction) {
        pendingTransactions.offer(transaction);
        audit("TXN_QUEUED", transaction.getTransactionId());
    }

    public Transaction processNextTransaction() {
        Transaction txn = pendingTransactions.poll();
        if (txn != null) {
            // Record in history
            transactionHistory.get(txn.getAccountNumber()).add(txn);

            // Update account balance
            BankAccount account = accounts.get(txn.getAccountNumber());
            if (account != null) {
                if (txn.isCredit()) {
                    account.deposit(txn.getAmount());
                } else {
                    account.withdraw(txn.getAmount());
                }
                updateBalanceIndex(account);
            }

            audit("TXN_PROCESSED", txn.getTransactionId());
        }
        return txn;
    }

    public void processAllPendingTransactions() {
        while (!pendingTransactions.isEmpty()) {
            processNextTransaction();
        }
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {
        return transactionHistory.getOrDefault(accountNumber, new ArrayList<>());
    }

    public List<Transaction> getRecentTransactions(String accountNumber, int n) {
        List<Transaction> history = getTransactionHistory(accountNumber);
        int size = history.size();
        return new ArrayList<>(history.subList(Math.max(0, size - n), size));
    }

    // ═══════════════════════════════════════════════════════════════
    // ANALYTICS
    // ═══════════════════════════════════════════════════════════════

    public Map<String, Double> getTotalDepositsByBranch() {
        Map<String, Double> result = new HashMap<>();
        for (BankAccount acc : accounts.values()) {
            result.merge(acc.getBranchCode(), acc.getBalance(), Double::sum);
        }
        return result;
    }

    public Map<String, Long> getAccountCountByType() {
        Map<String, Long> result = new HashMap<>();
        for (BankAccount acc : accounts.values()) {
            result.merge(acc.getAccountType(), 1L, Long::sum);
        }
        return result;
    }

    public Map<String, Long> getTransactionCountByChannel() {
        Map<String, Long> result = new HashMap<>();
        for (List<Transaction> txns : transactionHistory.values()) {
            for (Transaction txn : txns) {
                result.merge(txn.getChannel(), 1L, Long::sum);
            }
        }
        return result;
    }

    public double getTotalBankDeposits() {
        double total = 0;
        for (BankAccount acc : accounts.values()) {
            total += acc.getBalance();
        }
        return total;
    }

    // ═══════════════════════════════════════════════════════════════
    // AUDIT
    // ═══════════════════════════════════════════════════════════════

    private void audit(String action, String entityId) {
        String entry = String.format("[%tF %tT] %s: %s",
                new Date(), new Date(), action, entityId);
        auditLog.addFirst(entry);

        // Keep only last 100 entries
        while (auditLog.size() > 100) {
            auditLog.removeLast();
        }
    }

    public List<String> getRecentAuditEntries(int n) {
        List<String> result = new ArrayList<>();
        Iterator<String> iter = auditLog.iterator();
        for (int i = 0; i < n && iter.hasNext(); i++) {
            result.add(iter.next());
        }
        return result;
    }

    // ═══════════════════════════════════════════════════════════════
    // DISPLAY
    // ═══════════════════════════════════════════════════════════════

    public void displaySummary() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              BANKING SYSTEM SUMMARY                       ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("  Branches         : " + branches.size());
        System.out.println("  Customers        : " + customers.size());
        System.out.println("  Accounts         : " + accounts.size());
        System.out.println("  Pending Txns     : " + pendingTransactions.size());
        System.out.printf("  Total Deposits   : Rs.%,.2f%n", getTotalBankDeposits());
        System.out.println("  Active Channels  : " + activeChannels);
        System.out.println("├───────────────────────────────────────────────────────────┤");
        System.out.println("  Accounts by Type:");
        getAccountCountByType().forEach((type, count) ->
                System.out.println("    " + type + ": " + count));
        System.out.println("├───────────────────────────────────────────────────────────┤");
        System.out.println("  Deposits by Branch:");
        getTotalDepositsByBranch().forEach((branch, amount) ->
                System.out.printf("    %s: Rs.%,.2f%n", branch, amount));
        System.out.println("├───────────────────────────────────────────────────────────┤");
        System.out.println("  Top 3 Accounts by Balance:");
        getTopAccountsByBalance(3).forEach(acc ->
                System.out.printf("    %s: Rs.%,.2f%n", acc.getAccountNumber(), acc.getBalance()));
        System.out.println("├───────────────────────────────────────────────────────────┤");
        System.out.println("  Recent Audit Entries:");
        getRecentAuditEntries(5).forEach(entry ->
                System.out.println("    " + entry));
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }
}