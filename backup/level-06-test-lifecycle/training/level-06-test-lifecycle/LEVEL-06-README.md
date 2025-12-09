# Level 6: Test Lifecycle & Fixtures
## Managing Test Setup, Teardown, and Shared Resources

**Objective:** Master JUnit 5 lifecycle annotations and test fixtures for efficient test management  
**Duration:** 60-90 minutes  
**Pre-requisite:** Level 5 (Integration Testing) completed

---

## 🎯 What You Will Learn

```
┌─────────────────────────────────────────────────────────────────┐
│                    LEVEL 6 LEARNING PATH                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  6.1  Test Lifecycle Overview                                   │
│   ↓                                                             │
│  6.2  @BeforeEach and @AfterEach                                │
│   ↓                                                             │
│  6.3  @BeforeAll and @AfterAll                                  │
│   ↓                                                             │
│  6.4  Test Instance Lifecycle                                   │
│   ↓                                                             │
│  6.5  Test Execution Order                                      │
│   ↓                                                             │
│  6.6  Nested Test Lifecycle                                     │
│   ↓                                                             │
│  6.7  Test Fixtures and Shared Resources                        │
│   ↓                                                             │
│  6.8  Resource Cleanup Patterns                                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 6.1 Test Lifecycle Overview

### Lifecycle Execution Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                    TEST CLASS LIFECYCLE                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────────────────────────┐                        │
│  │         @BeforeAll (once)           │  Class-level setup     │
│  └─────────────────────────────────────┘                        │
│                    ↓                                            │
│  ┌─────────────────────────────────────┐                        │
│  │  ┌───────────────────────────────┐  │                        │
│  │  │     @BeforeEach (per test)    │  │  Test 1                │
│  │  │              ↓                │  │                        │
│  │  │         @Test method          │  │                        │
│  │  │              ↓                │  │                        │
│  │  │     @AfterEach (per test)     │  │                        │
│  │  └───────────────────────────────┘  │                        │
│  │                                     │                        │
│  │  ┌───────────────────────────────┐  │                        │
│  │  │     @BeforeEach (per test)    │  │  Test 2                │
│  │  │              ↓                │  │                        │
│  │  │         @Test method          │  │                        │
│  │  │              ↓                │  │                        │
│  │  │     @AfterEach (per test)     │  │                        │
│  │  └───────────────────────────────┘  │                        │
│  │                                     │                        │
│  │            ... more tests ...       │                        │
│  └─────────────────────────────────────┘                        │
│                    ↓                                            │
│  ┌─────────────────────────────────────┐                        │
│  │         @AfterAll (once)            │  Class-level cleanup   │
│  └─────────────────────────────────────┘                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Annotation Summary

| Annotation | Execution | Use Case |
|------------|-----------|----------|
| `@BeforeAll` | Once before all tests | Expensive setup (DB connection) |
| `@BeforeEach` | Before each test | Fresh test data |
| `@AfterEach` | After each test | Cleanup per test |
| `@AfterAll` | Once after all tests | Release resources |

---

## 6.2 @BeforeEach and @AfterEach

### Per-Test Setup and Cleanup

```java
class AccountLifecycleTest {

    private Account account;
    private InMemoryAccountRepository repository;

    @BeforeEach
    void setUp() {
        // Runs before EACH test - fresh state
        System.out.println("Setting up test...");
        repository = new InMemoryAccountRepository();
        account = new Account("ACC001", "Test User", 10000.0);
        repository.addAccount(account);
    }

    @AfterEach
    void tearDown() {
        // Runs after EACH test - cleanup
        System.out.println("Cleaning up...");
        repository.clear();
        account = null;
    }

    @Test
    void test1() {
        // Fresh repository and account
        account.debit(1000.0);
        assertEquals(9000.0, account.getBalance());
    }

    @Test
    void test2() {
        // ALSO has fresh repository and account (not affected by test1)
        assertEquals(10000.0, account.getBalance());
    }
}
```

### Multiple Setup Methods

```java
@BeforeEach
void initRepository() {
    repository = new InMemoryAccountRepository();
}

@BeforeEach
void initTestAccounts() {
    // Both methods run before each test
    // Order is not guaranteed between them
    repository.addAccount(new Account("S001", "Sender", 10000.0));
}
```

---

## 6.3 @BeforeAll and @AfterAll

### Class-Level Setup (Must be static)

```java
class ExpensiveResourceTest {

    // Shared across all tests
    private static DatabaseConnection dbConnection;
    private static UPITransferService service;

    @BeforeAll
    static void setUpClass() {
        // Runs ONCE before any test
        System.out.println("Initializing expensive resources...");
        dbConnection = new DatabaseConnection("jdbc:...");
        service = new UPITransferService(dbConnection);
    }

    @AfterAll
    static void tearDownClass() {
        // Runs ONCE after all tests
        System.out.println("Releasing resources...");
        dbConnection.close();
    }

    @Test
    void test1() {
        // Uses shared dbConnection
    }

    @Test
    void test2() {
        // Also uses same shared dbConnection
    }
}
```

### When to Use @BeforeAll

| Use @BeforeAll For | Use @BeforeEach For |
|--------------------|---------------------|
| Database connections | Test-specific data |
| External service clients | Fresh object state |
| Loading large config files | Resetting mocks |
| One-time expensive computations | Isolated test setup |

---

## 6.4 Test Instance Lifecycle

### Default: PER_METHOD (New instance per test)

```java
// Default behavior - each test gets new instance
class DefaultLifecycleTest {
    
    private int counter = 0;

    @Test
    void test1() {
        counter++;
        assertEquals(1, counter);  // Always 1
    }

    @Test
    void test2() {
        counter++;
        assertEquals(1, counter);  // Also 1 (new instance)
    }
}
```

### PER_CLASS: Single instance for all tests

```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PerClassLifecycleTest {

    private int counter = 0;

    // @BeforeAll/@AfterAll don't need to be static!
    @BeforeAll
    void setUp() {
        System.out.println("Non-static @BeforeAll");
    }

    @Test
    void test1() {
        counter++;
        assertEquals(1, counter);
    }

    @Test
    void test2() {
        counter++;
        assertEquals(2, counter);  // State preserved!
    }
}
```

### When to Use PER_CLASS

```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatefulIntegrationTest {

    private Account persistentAccount;

    @BeforeAll
    void createTestAccount() {
        // Create once, use in multiple tests
        persistentAccount = new Account("PERSIST", "Persistent", 50000.0);
    }

    @Test
    @Order(1)
    void shouldDebitAccount() {
        persistentAccount.debit(10000.0);
        assertEquals(40000.0, persistentAccount.getBalance());
    }

    @Test
    @Order(2)
    void shouldCreditAccount() {
        // Uses state from test1
        persistentAccount.credit(5000.0);
        assertEquals(45000.0, persistentAccount.getBalance());
    }
}
```

---

## 6.5 Test Execution Order

### @Order Annotation

```java
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderedTest {

    @Test
    @Order(1)
    void firstTest() {
        System.out.println("First");
    }

    @Test
    @Order(2)
    void secondTest() {
        System.out.println("Second");
    }

    @Test
    @Order(3)
    void thirdTest() {
        System.out.println("Third");
    }
}
```

### Other Ordering Strategies

```java
// Alphabetical by method name
@TestMethodOrder(MethodOrderer.MethodName.class)

// Random order (good for finding test dependencies)
@TestMethodOrder(MethodOrderer.Random.class)

// By @DisplayName
@TestMethodOrder(MethodOrderer.DisplayName.class)
```

### ⚠️ Warning: Tests Should Be Independent

```java
// ❌ BAD: Tests depend on order
@Test @Order(1)
void createAccount() { /* creates shared account */ }

@Test @Order(2)
void useAccount() { /* assumes account exists */ }

// ✅ GOOD: Each test is independent
@Test
void shouldCreateAndUseAccount() {
    Account account = createAccount();
    useAccount(account);
}
```

---

## 6.6 Nested Test Lifecycle

### Lifecycle in @Nested Classes

```java
@DisplayName("Account Tests")
class NestedLifecycleTest {

    private InMemoryAccountRepository repository;

    @BeforeAll
    static void outerBeforeAll() {
        System.out.println("Outer @BeforeAll");
    }

    @BeforeEach
    void outerBeforeEach() {
        System.out.println("Outer @BeforeEach");
        repository = new InMemoryAccountRepository();
    }

    @Nested
    @DisplayName("Debit Operations")
    class DebitTests {

        private Account account;

        @BeforeEach
        void innerBeforeEach() {
            // Outer @BeforeEach runs FIRST, then this
            System.out.println("Inner @BeforeEach");
            account = new Account("ACC001", "Test", 10000.0);
            repository.addAccount(account);
        }

        @Test
        void shouldDebit() {
            // Both outer and inner setup have run
            account.debit(1000.0);
            assertEquals(9000.0, account.getBalance());
        }
    }

    @Nested
    @DisplayName("Credit Operations")
    class CreditTests {

        @BeforeEach
        void innerBeforeEach() {
            System.out.println("Credit inner @BeforeEach");
        }

        @Test
        void shouldCredit() {
            // Fresh repository from outer, no account yet
        }
    }
}
```

### Execution Order for Nested Tests

```
Outer @BeforeAll
  │
  ├─ DebitTests
  │    ├─ Outer @BeforeEach
  │    ├─ Inner @BeforeEach (DebitTests)
  │    ├─ @Test shouldDebit
  │    ├─ Inner @AfterEach (if any)
  │    └─ Outer @AfterEach (if any)
  │
  ├─ CreditTests
  │    ├─ Outer @BeforeEach
  │    ├─ Inner @BeforeEach (CreditTests)
  │    ├─ @Test shouldCredit
  │    └─ ...
  │
Outer @AfterAll
```

---

## 6.7 Test Fixtures

### What is a Test Fixture?

A **test fixture** is the fixed state used as a baseline for tests:
- Objects under test
- Input data
- Expected outputs
- Mock configurations

### Fixture Classes

```java
/**
 * Centralized test fixtures for banking tests.
 */
class BankingTestFixtures {

    // Standard accounts
    public static Account standardSender() {
        return new Account("SENDER", "Rajesh Kumar", 50000.0);
    }

    public static Account standardReceiver() {
        return new Account("RECEIVER", "Priya Sharma", 25000.0);
    }

    // Edge case accounts
    public static Account zeroBalanceAccount() {
        return new Account("ZERO", "Empty Account", 0.0);
    }

    public static Account maxBalanceAccount() {
        return new Account("MAX", "Rich Account", 10000000.0);
    }

    // Pre-configured repository
    public static InMemoryAccountRepository standardRepository() {
        InMemoryAccountRepository repo = new InMemoryAccountRepository();
        repo.addAccount(standardSender());
        repo.addAccount(standardReceiver());
        return repo;
    }

    // UPI mappings
    public static void addStandardUpiMappings(InMemoryAccountRepository repo) {
        repo.addUpiMapping("rajesh@upi", "SENDER");
        repo.addUpiMapping("priya@upi", "RECEIVER");
    }
}
```

### Using Fixtures

```java
class TransferServiceTest {

    private InMemoryAccountRepository repository;
    private UPITransferService service;

    @BeforeEach
    void setUp() {
        // Use fixture for consistent setup
        repository = BankingTestFixtures.standardRepository();
        BankingTestFixtures.addStandardUpiMappings(repository);
        service = new UPITransferService(repository);
    }

    @Test
    void shouldTransferBetweenStandardAccounts() {
        service.transfer("SENDER", "RECEIVER", 5000.0);
        
        assertEquals(45000.0, repository.loadAccountById("SENDER").getBalance());
        assertEquals(30000.0, repository.loadAccountById("RECEIVER").getBalance());
    }
}
```

---

## 6.8 Resource Cleanup Patterns

### Try-Finally Pattern

```java
@Test
void testWithManualCleanup() {
    Connection connection = null;
    try {
        connection = openConnection();
        // Use connection
    } finally {
        if (connection != null) {
            connection.close();
        }
    }
}
```

### @AfterEach for Consistent Cleanup

```java
class ResourceCleanupTest {

    private List<Account> createdAccounts = new ArrayList<>();
    private InMemoryAccountRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
    }

    @AfterEach
    void cleanUp() {
        // Always runs, even if test fails
        createdAccounts.clear();
        repository.clear();
        System.out.println("Cleaned up " + createdAccounts.size() + " accounts");
    }

    @Test
    void testThatMightFail() {
        Account acc = new Account("TEST", "Test", 1000.0);
        createdAccounts.add(acc);
        repository.addAccount(acc);
        
        // Even if this fails, cleanup runs
        assertTrue(false);  // Intentional failure
    }
}
```

### Soft Assertions with Cleanup

```java
@Test
void testWithMultipleAssertions() {
    try {
        assertAll(
            () -> assertEquals(expected1, actual1),
            () -> assertEquals(expected2, actual2),
            () -> assertEquals(expected3, actual3)
        );
    } finally {
        // Cleanup always runs
        repository.clear();
    }
}
```

---

## 🎓 Key Takeaways

| Annotation | Scope | Static Required | Use Case |
|------------|-------|-----------------|----------|
| `@BeforeAll` | Class | Yes* | Expensive one-time setup |
| `@BeforeEach` | Method | No | Fresh state per test |
| `@AfterEach` | Method | No | Per-test cleanup |
| `@AfterAll` | Class | Yes* | Release shared resources |

*Not required if `@TestInstance(PER_CLASS)`

### Best Practices

1. **Keep tests independent** - Avoid relying on test execution order
2. **Use @BeforeEach for isolation** - Fresh state prevents flaky tests
3. **Use @BeforeAll sparingly** - Only for truly expensive operations
4. **Always clean up resources** - Prevent memory leaks and interference
5. **Use fixtures for consistency** - Centralize test data creation

---

*Level 6 - Test Lifecycle & Fixtures*
