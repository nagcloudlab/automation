# Level 5: Integration Testing
## Testing Real Component Interactions

**Objective:** Master integration testing to verify real component interactions without mocks  
**Duration:** 90-120 minutes  
**Pre-requisite:** Level 4 (Mockito Basics) completed

---

## 🎯 What You Will Learn

```
┌─────────────────────────────────────────────────────────────────┐
│                    LEVEL 5 LEARNING PATH                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  5.1  Unit Tests vs Integration Tests                           │
│   ↓                                                             │
│  5.2  Integration Test Setup with @BeforeEach                   │
│   ↓                                                             │
│  5.3  Testing Real Repository Interactions                      │
│   ↓                                                             │
│  5.4  End-to-End Transfer Scenarios                             │
│   ↓                                                             │
│  5.5  State Management Between Tests                            │
│   ↓                                                             │
│  5.6  Test Data Factories                                       │
│   ↓                                                             │
│  5.7  Testing Transaction Flows                                 │
│   ↓                                                             │
│  5.8  Integration Test Best Practices                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5.1 Unit Tests vs Integration Tests

### Comparison

| Aspect | Unit Test (Level 4) | Integration Test (Level 5) |
|--------|---------------------|---------------------------|
| **Dependencies** | Mocked | Real implementations |
| **Scope** | Single class | Multiple classes |
| **Speed** | Very fast (ms) | Slower (ms-seconds) |
| **Isolation** | Complete | Partial |
| **Purpose** | Test logic in isolation | Test component interaction |
| **Failures** | Logic errors | Integration issues |

### When to Use Each

```
┌─────────────────────────────────────────────────────────────────┐
│                     TESTING PYRAMID                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│                        /\                                       │
│                       /  \     E2E Tests (few)                  │
│                      /    \    - Full system                    │
│                     /──────\                                    │
│                    /        \  Integration Tests (some)         │
│                   /          \ - Real components                │
│                  /────────────\                                 │
│                 /              \ Unit Tests (many)              │
│                /                \ - Mocked dependencies         │
│               /──────────────────\                              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5.2 Integration Test Setup

### Test Class Structure

```java
@DisplayName("UPI Transfer Service - Integration Tests")
class UPITransferServiceIntegrationTest {

    // Real implementations - NO MOCKS!
    private InMemoryAccountRepository repository;
    private UPITransferService transferService;

    @BeforeEach
    void setUp() {
        // Create fresh repository for each test
        repository = new InMemoryAccountRepository();
        transferService = new UPITransferService(repository);
        
        // Set up test data
        repository.addAccount(new Account("S001", "Sender", 10000.0));
        repository.addAccount(new Account("R001", "Receiver", 5000.0));
    }

    @Test
    void shouldTransferBetweenRealAccounts() {
        // Act - Uses REAL repository, not mock
        transferService.transfer("S001", "R001", 1000.0);

        // Assert - Verify actual database state
        assertEquals(9000.0, repository.loadAccountById("S001").getBalance());
        assertEquals(6000.0, repository.loadAccountById("R001").getBalance());
    }
}
```

### Key Differences from Unit Tests

```java
// ❌ UNIT TEST (Level 4) - Mocked repository
@Mock
AccountRepository accountRepository;

@InjectMocks
UPITransferService service;

when(accountRepository.loadAccountById("S001")).thenReturn(sender);

// ✅ INTEGRATION TEST (Level 5) - Real repository
InMemoryAccountRepository repository = new InMemoryAccountRepository();
UPITransferService service = new UPITransferService(repository);

repository.addAccount(new Account("S001", "Sender", 10000.0));
```

---

## 5.3 InMemoryAccountRepository

For clean integration testing, we use a controllable in-memory repository:

```java
public class InMemoryAccountRepository implements AccountRepository {
    
    private final Map<String, Account> accounts = new HashMap<>();
    private final Map<String, String> upiMappings = new HashMap<>();

    // Test helper methods
    public void addAccount(Account account) {
        accounts.put(account.getAccountId(), copyAccount(account));
    }

    public void addUpiMapping(String upiId, String accountId) {
        upiMappings.put(upiId.toLowerCase(), accountId);
    }

    public void clear() {
        accounts.clear();
        upiMappings.clear();
    }

    // Interface implementation...
}
```

**Why not use SqlAccountRepository?**
- Pre-loaded test data may interfere
- Hard to control initial state
- Console output clutters test logs

---

## 5.4 End-to-End Scenarios

### Complete Transfer Flow

```java
@Test
@DisplayName("Complete P2P transfer flow")
void shouldCompleteP2PTransferFlow() {
    // Setup - Real accounts
    repository.addAccount(new Account("SENDER", "Rajesh", 50000.0));
    repository.addAccount(new Account("RECEIVER", "Priya", 25000.0));

    // Act - Real transfer
    TransferResult result = transferService.transfer("SENDER", "RECEIVER", 5000.0);

    // Assert - Verify complete flow
    // 1. Transfer result
    assertEquals(5000.0, result.getAmount());
    assertEquals(50000.0, result.getSenderBalanceBefore());
    assertEquals(45000.0, result.getSenderBalanceAfter());

    // 2. Repository state (persisted)
    Account sender = repository.loadAccountById("SENDER");
    Account receiver = repository.loadAccountById("RECEIVER");
    assertEquals(45000.0, sender.getBalance());
    assertEquals(30000.0, receiver.getBalance());
}
```

### Multiple Transfer Scenario

```java
@Test
@DisplayName("Multiple transfers maintain consistency")
void shouldHandleMultipleTransfers() {
    repository.addAccount(new Account("A", "User A", 10000.0));
    repository.addAccount(new Account("B", "User B", 10000.0));
    repository.addAccount(new Account("C", "User C", 10000.0));

    // A → B → C chain
    transferService.transfer("A", "B", 3000.0);  // A: 7000, B: 13000
    transferService.transfer("B", "C", 5000.0);  // B: 8000, C: 15000
    transferService.transfer("C", "A", 2000.0);  // C: 13000, A: 9000

    // Verify final state
    assertEquals(9000.0, repository.loadAccountById("A").getBalance());
    assertEquals(8000.0, repository.loadAccountById("B").getBalance());
    assertEquals(13000.0, repository.loadAccountById("C").getBalance());

    // Total should be conserved: 30000
    double total = 9000 + 8000 + 13000;
    assertEquals(30000.0, total);
}
```

---

## 5.5 State Management

### Test Isolation

```java
class StateManagementTest {

    private InMemoryAccountRepository repository;
    private UPITransferService service;

    @BeforeEach
    void setUp() {
        // Fresh repository for EACH test
        repository = new InMemoryAccountRepository();
        service = new UPITransferService(repository);
    }

    @Test
    void firstTest() {
        repository.addAccount(new Account("A", "Test", 1000.0));
        service.transfer("A", "B", 500.0);  // Will fail - B doesn't exist
    }

    @Test
    void secondTest() {
        // Repository is EMPTY - firstTest's data doesn't leak
        assertFalse(repository.existsById("A"));
    }
}
```

### Shared Setup with @BeforeAll

```java
class SharedSetupTest {

    private static InMemoryAccountRepository sharedRepository;

    @BeforeAll
    static void setUpOnce() {
        // Shared across all tests (use carefully)
        sharedRepository = new InMemoryAccountRepository();
        sharedRepository.addAccount(new Account("SHARED", "Shared Account", 100000.0));
    }

    @AfterEach
    void resetState() {
        // Reset balance after each test
        Account shared = sharedRepository.loadAccountById("SHARED");
        // Note: Would need a setBalance method or recreate
    }
}
```

---

## 5.6 Test Data Factories

### Account Factory

```java
class TestAccountFactory {
    
    private static int counter = 0;

    public static Account createAccount(double balance) {
        counter++;
        return new Account("ACC" + counter, "Test User " + counter, balance);
    }

    public static Account createSender() {
        return createAccount(10000.0);
    }

    public static Account createReceiver() {
        return createAccount(5000.0);
    }

    public static Account createRichAccount() {
        return createAccount(500000.0);  // ₹5 lakh
    }

    public static Account createPoorAccount() {
        return createAccount(100.0);
    }

    public static void resetCounter() {
        counter = 0;
    }
}
```

### Using Factory in Tests

```java
@BeforeEach
void setUp() {
    TestAccountFactory.resetCounter();
    repository = new InMemoryAccountRepository();
    service = new UPITransferService(repository);
}

@Test
void shouldTransferFromRichToPoor() {
    Account rich = TestAccountFactory.createRichAccount();
    Account poor = TestAccountFactory.createPoorAccount();
    
    repository.addAccount(rich);
    repository.addAccount(poor);

    service.transfer(rich.getAccountId(), poor.getAccountId(), 50000.0);

    assertEquals(450000.0, repository.loadAccountById(rich.getAccountId()).getBalance());
    assertEquals(50100.0, repository.loadAccountById(poor.getAccountId()).getBalance());
}
```

---

## 5.7 Testing Transaction Flows

### Failure Scenarios

```java
@Test
@DisplayName("Failed transfer should not change any balance")
void shouldNotChangeBalancesOnFailure() {
    repository.addAccount(new Account("S", "Sender", 1000.0));
    repository.addAccount(new Account("R", "Receiver", 500.0));

    // Attempt transfer exceeding balance
    assertThrows(InsufficientBalanceException.class,
        () -> service.transfer("S", "R", 5000.0));

    // Both accounts unchanged
    assertEquals(1000.0, repository.loadAccountById("S").getBalance());
    assertEquals(500.0, repository.loadAccountById("R").getBalance());
}
```

### Partial Failure Testing

```java
@Test
@DisplayName("Second transfer should fail without affecting first")
void shouldHandlePartialBatchFailure() {
    repository.addAccount(new Account("A", "A", 5000.0));
    repository.addAccount(new Account("B", "B", 5000.0));

    // First transfer succeeds
    service.transfer("A", "B", 2000.0);  // A: 3000, B: 7000

    // Second transfer fails
    assertThrows(InsufficientBalanceException.class,
        () -> service.transfer("A", "B", 5000.0));

    // First transfer's effect preserved
    assertEquals(3000.0, repository.loadAccountById("A").getBalance());
    assertEquals(7000.0, repository.loadAccountById("B").getBalance());
}
```

---

## 5.8 Integration Test Best Practices

### Do's ✅

1. **Use fresh state for each test**
   ```java
   @BeforeEach
   void setUp() {
       repository = new InMemoryAccountRepository();  // Fresh
   }
   ```

2. **Test complete flows, not just single operations**
   ```java
   // Good - Tests entire flow
   @Test void shouldCompleteTransferAndVerifyPersistence()
   ```

3. **Verify both result AND persisted state**
   ```java
   TransferResult result = service.transfer(...);
   assertEquals(expected, result.getSenderBalanceAfter());
   assertEquals(expected, repository.loadAccountById(...).getBalance());
   ```

4. **Test real error scenarios**
   ```java
   @Test void shouldHandleAccountNotFoundInRealRepository()
   ```

### Don'ts ❌

1. **Don't share mutable state between tests**
   ```java
   // Bad - State leaks between tests
   private static Account sharedAccount;
   ```

2. **Don't rely on test execution order**
   ```java
   // Bad - Depends on another test
   @Test void step2() { /* assumes step1 ran */ }
   ```

3. **Don't mix mocks and real implementations**
   ```java
   // Bad - Confusing mix
   @Mock Repository repo;
   RealService service = new RealService(repo);  // Why mock?
   ```

---

## 🎓 Key Takeaways

| Concept | Unit Test (L4) | Integration Test (L5) |
|---------|----------------|----------------------|
| Dependencies | `@Mock` | Real instances |
| Setup | `when().thenReturn()` | `repository.addAccount()` |
| Verification | `verify()` | Assert persisted state |
| Scope | Single method | Complete flow |
| Speed | Milliseconds | May be slower |
| Failure diagnosis | Easy (isolated) | May need debugging |

---

## ✅ What Integration Tests Verify

1. **Component wiring** - Service correctly uses repository
2. **Data persistence** - Changes are saved
3. **Transaction flow** - Complete operations work
4. **Error handling** - Failures propagate correctly
5. **State consistency** - Data remains consistent

---

*Level 5 - Integration Testing*
