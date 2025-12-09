# Level 0: Making Code Testable
## Pre-requisite: Refactoring for Dependency Injection

**Objective:** Transform tightly-coupled code into loosely-coupled, testable code  
**Duration:** 45-60 minutes  
**Key Concept:** Dependency Injection (DI)

---

## 🎯 Why This Level First?

Before writing any tests, your code must be **testable**. The current code has a critical design flaw that makes unit testing impossible.

```
┌─────────────────────────────────────────────────────────────────┐
│                    THE TESTING PROBLEM                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   Current Code:                                                 │
│   ┌─────────────────────┐      ┌─────────────────────┐         │
│   │ UPITransferService  │──────│ SqlAccountRepository │         │
│   │                     │ new  │                      │         │
│   │ transfer() {        │─────▶│ (Real Database)      │         │
│   │   new SqlAccount... │      │                      │         │
│   │ }                   │      └─────────────────────┘         │
│   └─────────────────────┘                                       │
│                                                                 │
│   ❌ PROBLEM: Cannot test UPITransferService without            │
│              hitting the real database!                         │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   Refactored Code:                                              │
│   ┌─────────────────────┐      ┌───────────────────┐           │
│   │ UPITransferService  │      │<<interface>>      │           │
│   │                     │─────▶│AccountRepository  │           │
│   │ constructor(repo)   │      └───────────────────┘           │
│   │ transfer() {        │              ▲                        │
│   │   this.repo.load... │              │ implements            │
│   │ }                   │      ┌───────┴───────────┐           │
│   └─────────────────────┘      │                   │           │
│                          ┌─────┴─────┐     ┌───────┴───────┐   │
│                          │ SqlAccount│     │ MockAccount   │   │
│                          │ Repository│     │ Repository    │   │
│                          │ (Real DB) │     │ (For Tests)   │   │
│                          └───────────┘     └───────────────┘   │
│                                                                 │
│   ✅ SOLUTION: Inject dependency through constructor            │
│               Tests can inject mock, Production uses real DB    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📋 Current Code Analysis

### Problem 1: Tight Coupling in UPITransferService

```java
// ❌ CURRENT CODE - UPITransferService.java
public class UPITransferService {

    public void transfer(double amount, String fromAccountId, String toAccountId) {
        
        // ⚠️ PROBLEM: Creates dependency internally!
        SqlAccountRepository accountRepository = new SqlAccountRepository();
        
        Account fromAccount = accountRepository.loadAccountById(fromAccountId);
        Account toAccount = accountRepository.loadAccountById(toAccountId);
        // ...
    }
}
```

**Why is this bad?**

| Issue | Impact |
|-------|--------|
| `new SqlAccountRepository()` inside method | Cannot substitute with mock |
| No interface | Cannot create alternative implementations |
| Direct dependency | Testing requires real database |
| Hidden dependency | Not visible in constructor |

### Problem 2: No Abstraction Layer

```java
// ❌ CURRENT CODE - SqlAccountRepository.java
public class SqlAccountRepository {
    
    public Account loadAccountById(String accountId) {
        // Directly coupled to SQL implementation
        return new Account("ACC123", "John Doe", 1000.0);
    }
}
```

**Why is this bad?**
- No interface means no contract
- Cannot swap implementations (SQL → NoSQL → Mock)
- Violates **Dependency Inversion Principle** (SOLID - D)

---

## 🔧 The Refactoring Steps

### Step 1: Create AccountRepository Interface

**Purpose:** Define a contract that any repository must follow

```java
// ✅ NEW FILE: AccountRepository.java
package com.example.repository;

import com.example.model.Account;
import java.util.Optional;

/**
 * Repository interface for Account operations.
 * Follows the Repository Pattern - abstracts data access layer.
 * 
 * Banking Context: This interface allows us to:
 * - Switch between different databases (Oracle CBS, PostgreSQL, etc.)
 * - Mock database calls for unit testing
 * - Implement caching layer transparently
 */
public interface AccountRepository {
    
    /**
     * Load account by account ID
     * @param accountId The unique account identifier
     * @return Account if found
     * @throws AccountNotFoundException if account doesn't exist
     */
    Account loadAccountById(String accountId);
    
    /**
     * Save/Update account details
     * @param account The account to save
     */
    void saveAccount(Account account);
    
    /**
     * Check if account exists
     * @param accountId The account ID to check
     * @return true if account exists
     */
    boolean existsById(String accountId);
    
    /**
     * Find account by UPI ID (e.g., "rajesh@upi")
     * @param upiId The UPI virtual payment address
     * @return Optional containing account if found
     */
    Optional<Account> findByUpiId(String upiId);
}
```

### Step 2: Implement the Interface

**Purpose:** Make SqlAccountRepository implement the new interface

```java
// ✅ REFACTORED: SqlAccountRepository.java
package com.example.repository;

import com.example.model.Account;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL-based implementation of AccountRepository.
 * In production, this would connect to actual database (Oracle/PostgreSQL).
 * 
 * For training purposes, using in-memory HashMap to simulate DB.
 */
public class SqlAccountRepository implements AccountRepository {

    // Simulated database storage
    private final Map<String, Account> accountStore = new HashMap<>();
    private final Map<String, String> upiIdMapping = new HashMap<>(); // upiId -> accountId

    public SqlAccountRepository() {
        System.out.println("[DB] SqlAccountRepository initialized");
        initializeTestData();
    }

    /**
     * Initialize with sample accounts for testing
     */
    private void initializeTestData() {
        // Sample accounts for training
        Account acc1 = new Account("ACC001", "Rajesh Kumar", 50000.0);
        Account acc2 = new Account("ACC002", "Priya Sharma", 75000.0);
        Account acc3 = new Account("ACC003", "Amit Patel", 100000.0);
        
        accountStore.put("ACC001", acc1);
        accountStore.put("ACC002", acc2);
        accountStore.put("ACC003", acc3);
        
        upiIdMapping.put("rajesh@upi", "ACC001");
        upiIdMapping.put("priya@upi", "ACC002");
        upiIdMapping.put("amit@upi", "ACC003");
        
        System.out.println("[DB] Loaded " + accountStore.size() + " test accounts");
    }

    @Override
    public Account loadAccountById(String accountId) {
        System.out.println("[DB] Loading account: " + accountId);
        
        Account account = accountStore.get(accountId);
        if (account == null) {
            throw new RuntimeException("Account not found: " + accountId);
        }
        
        // Return a copy to simulate DB fetch (new object each time)
        return new Account(
            account.getAccountId(),
            account.getAccountHolderName(),
            account.getBalance()
        );
    }

    @Override
    public void saveAccount(Account account) {
        System.out.println("[DB] Saving account: " + account.getAccountId() + 
                          " | New Balance: ₹" + account.getBalance());
        accountStore.put(account.getAccountId(), account);
    }

    @Override
    public boolean existsById(String accountId) {
        return accountStore.containsKey(accountId);
    }

    @Override
    public Optional<Account> findByUpiId(String upiId) {
        System.out.println("[DB] Finding account by UPI ID: " + upiId);
        
        String accountId = upiIdMapping.get(upiId);
        if (accountId == null) {
            return Optional.empty();
        }
        return Optional.of(loadAccountById(accountId));
    }
}
```

### Step 3: Refactor UPITransferService for Dependency Injection

**Purpose:** Accept repository through constructor instead of creating it

```java
// ✅ REFACTORED: UPITransferService.java
package com.example.service;

import com.example.model.Account;
import com.example.repository.AccountRepository;

/**
 * UPI Transfer Service - Core business logic for fund transfers.
 * 
 * REFACTORED: Now uses Dependency Injection
 * - Repository is injected via constructor
 * - Can be tested with mock repository
 * - Follows Single Responsibility Principle
 * 
 * Banking Context: This service handles P2P UPI transfers
 * similar to how NPCI processes inter-bank transactions.
 */
public class UPITransferService {

    // UPI Transaction Limits (as per NPCI guidelines)
    public static final double UPI_MIN_AMOUNT = 1.0;
    public static final double UPI_MAX_AMOUNT = 100000.0;  // ₹1 Lakh per transaction

    // ✅ Dependency is now a field, injected via constructor
    private final AccountRepository accountRepository;

    /**
     * Constructor Injection - The key to testability!
     * 
     * @param accountRepository The repository to use for account operations
     * 
     * Why Constructor Injection?
     * 1. Dependencies are explicit and visible
     * 2. Object is fully initialized after construction
     * 3. Easy to inject mocks for testing
     * 4. Immutable dependency (final field)
     */
    public UPITransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        System.out.println("[SERVICE] UPITransferService initialized");
    }

    /**
     * Execute UPI fund transfer between two accounts.
     * 
     * @param amount Amount to transfer in INR
     * @param fromAccountId Sender's account ID
     * @param toAccountId Receiver's account ID
     * @throws IllegalArgumentException if validation fails
     * @throws RuntimeException if transfer fails
     * 
     * Transaction Flow:
     * 1. Validate amount (UPI limits)
     * 2. Load sender account
     * 3. Load receiver account
     * 4. Debit sender
     * 5. Credit receiver
     * 6. Save both accounts
     */
    public void transfer(double amount, String fromAccountId, String toAccountId) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("[TRANSFER] Initiating UPI Transfer");
        System.out.println("[TRANSFER] Amount: ₹" + amount);
        System.out.println("[TRANSFER] From: " + fromAccountId + " → To: " + toAccountId);
        System.out.println("=".repeat(50));

        // Step 1: Validate transaction amount
        validateAmount(amount);

        // Step 2: Validate accounts are different
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }

        // Step 3: Load accounts (using injected repository)
        Account fromAccount = accountRepository.loadAccountById(fromAccountId);
        Account toAccount = accountRepository.loadAccountById(toAccountId);

        System.out.println("[TRANSFER] Sender Balance: ₹" + fromAccount.getBalance());
        System.out.println("[TRANSFER] Receiver Balance: ₹" + toAccount.getBalance());

        // Step 4: Execute transfer (debit first, then credit)
        fromAccount.debit(amount);   // May throw if insufficient balance
        toAccount.credit(amount);

        // Step 5: Persist changes
        accountRepository.saveAccount(fromAccount);
        accountRepository.saveAccount(toAccount);

        System.out.println("[TRANSFER] ✅ Transfer Successful!");
        System.out.println("[TRANSFER] New Sender Balance: ₹" + fromAccount.getBalance());
        System.out.println("[TRANSFER] New Receiver Balance: ₹" + toAccount.getBalance());
        System.out.println("=".repeat(50) + "\n");
    }

    /**
     * Validate transfer amount against UPI limits.
     * 
     * @param amount Amount to validate
     * @throws IllegalArgumentException if amount is invalid
     */
    private void validateAmount(double amount) {
        if (amount < UPI_MIN_AMOUNT) {
            throw new IllegalArgumentException(
                String.format("Amount ₹%.2f is below minimum UPI limit of ₹%.2f", 
                    amount, UPI_MIN_AMOUNT)
            );
        }
        if (amount > UPI_MAX_AMOUNT) {
            throw new IllegalArgumentException(
                String.format("Amount ₹%.2f exceeds maximum UPI limit of ₹%.2f", 
                    amount, UPI_MAX_AMOUNT)
            );
        }
    }

    /**
     * Transfer using UPI IDs instead of account IDs.
     * 
     * @param amount Amount to transfer
     * @param fromUpiId Sender's UPI ID (e.g., "rajesh@upi")
     * @param toUpiId Receiver's UPI ID (e.g., "priya@upi")
     */
    public void transferByUpiId(double amount, String fromUpiId, String toUpiId) {
        System.out.println("[TRANSFER] Resolving UPI IDs...");
        
        Account fromAccount = accountRepository.findByUpiId(fromUpiId)
            .orElseThrow(() -> new RuntimeException("Sender UPI ID not found: " + fromUpiId));
        
        Account toAccount = accountRepository.findByUpiId(toUpiId)
            .orElseThrow(() -> new RuntimeException("Receiver UPI ID not found: " + toUpiId));
        
        transfer(amount, fromAccount.getAccountId(), toAccount.getAccountId());
    }
}
```

### Step 4: Update Application Entry Point

**Purpose:** Wire dependencies together (manual DI, Spring does this automatically)

```java
// ✅ REFACTORED: Application.java
package com.example;

import com.example.repository.AccountRepository;
import com.example.repository.SqlAccountRepository;
import com.example.service.UPITransferService;

/**
 * Application Entry Point
 * 
 * Demonstrates manual Dependency Injection.
 * In Spring Boot, this wiring happens automatically via @Autowired.
 * 
 * The key insight: Dependencies flow from outside → inside
 * Application creates Repository, then injects it into Service.
 */
public class Application {
    
    public static void main(String[] args) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("   UPI TRANSFER SERVICE - LEVEL 0 DEMO");
        System.out.println("   Dependency Injection Example");
        System.out.println("═".repeat(60) + "\n");

        // ═══════════════════════════════════════════════════════════
        // PHASE 1: INITIALIZATION (Dependency Injection)
        // ═══════════════════════════════════════════════════════════
        System.out.println("▶ PHASE 1: Initializing Components\n");

        // Step 1: Create the repository (lowest level dependency)
        AccountRepository accountRepository = new SqlAccountRepository();

        // Step 2: Inject repository into service (Dependency Injection!)
        UPITransferService transferService = new UPITransferService(accountRepository);

        // ═══════════════════════════════════════════════════════════
        // PHASE 2: EXECUTE TRANSFERS
        // ═══════════════════════════════════════════════════════════
        System.out.println("\n▶ PHASE 2: Executing Transfers\n");

        // Transfer 1: Rajesh → Priya (₹5,000)
        try {
            transferService.transfer(5000.0, "ACC001", "ACC002");
        } catch (Exception e) {
            System.out.println("[ERROR] Transfer 1 failed: " + e.getMessage());
        }

        // Transfer 2: Using UPI IDs
        try {
            transferService.transferByUpiId(2500.0, "priya@upi", "amit@upi");
        } catch (Exception e) {
            System.out.println("[ERROR] Transfer 2 failed: " + e.getMessage());
        }

        // Transfer 3: Should FAIL - Insufficient balance
        try {
            transferService.transfer(999999.0, "ACC001", "ACC002");
        } catch (Exception e) {
            System.out.println("[ERROR] Transfer 3 failed (expected): " + e.getMessage());
        }

        // Transfer 4: Should FAIL - Exceeds UPI limit
        try {
            transferService.transfer(150000.0, "ACC003", "ACC001");
        } catch (Exception e) {
            System.out.println("[ERROR] Transfer 4 failed (expected): " + e.getMessage());
        }

        // ═══════════════════════════════════════════════════════════
        // PHASE 3: CLEANUP
        // ═══════════════════════════════════════════════════════════
        System.out.println("\n▶ PHASE 3: Cleanup\n");
        System.out.println("Application completed successfully.");
        System.out.println("═".repeat(60) + "\n");
    }
}
```

---

## 📁 Final Project Structure

```
transfer-service/
└── src/
    └── main/
        └── java/
            └── com/
                └── example/
                    ├── Application.java              ← Updated (DI wiring)
                    ├── model/
                    │   └── Account.java              ← Unchanged
                    ├── repository/
                    │   ├── AccountRepository.java    ← NEW (Interface)
                    │   └── SqlAccountRepository.java ← Updated (implements interface)
                    └── service/
                        └── UPITransferService.java   ← Updated (Constructor DI)
```

---

## 🧪 Why This Makes Testing Possible

### Before Refactoring (Impossible to Unit Test)

```java
// ❌ Cannot test without real database
@Test
void testTransfer() {
    UPITransferService service = new UPITransferService();
    // This will try to connect to real database!
    service.transfer(1000, "ACC1", "ACC2");  // FAILS!
}
```

### After Refactoring (Easy to Unit Test)

```java
// ✅ Can test with mock repository
@Test
void testTransfer() {
    // Create a mock repository
    AccountRepository mockRepo = mock(AccountRepository.class);
    
    // Define mock behavior
    when(mockRepo.loadAccountById("ACC1"))
        .thenReturn(new Account("ACC1", "Sender", 10000.0));
    when(mockRepo.loadAccountById("ACC2"))
        .thenReturn(new Account("ACC2", "Receiver", 5000.0));
    
    // Inject mock into service
    UPITransferService service = new UPITransferService(mockRepo);
    
    // Test transfer - NO DATABASE NEEDED!
    service.transfer(1000, "ACC1", "ACC2");
    
    // Verify interactions
    verify(mockRepo).saveAccount(any());
}
```

---

## 🎓 Key Takeaways

| Concept | Before | After |
|---------|--------|-------|
| **Dependency Creation** | Inside method | Outside (injected) |
| **Coupling** | Tight (new keyword) | Loose (interface) |
| **Testability** | Impossible | Easy with mocks |
| **Flexibility** | None | Swap implementations |
| **SOLID Principle** | Violated | Follows DIP |

### The Dependency Inversion Principle (DIP)

> "Depend on abstractions, not concretions"

- **High-level modules** (UPITransferService) should not depend on **low-level modules** (SqlAccountRepository)
- Both should depend on **abstractions** (AccountRepository interface)

---

## ✅ Hands-On Exercise

1. **Run the Application** - Verify the refactored code works
2. **Add a new method** to `AccountRepository` interface: `List<Account> findAll()`
3. **Implement it** in `SqlAccountRepository`
4. **Think ahead**: How would you create a `MockAccountRepository` for tests?

---

## 🔜 Next Level Preview

**Level 1: JUnit 5 Basics** - Now that code is testable, we'll write our first unit tests for the `Account` model class.

---

*Level 0 Complete - Foundation for Testability Established*
