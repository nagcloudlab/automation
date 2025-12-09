# Level 4: Mockito Basics
## Mocking Dependencies for Isolated Unit Testing

**Objective:** Master Mockito to mock dependencies and test services in isolation  
**Duration:** 90-120 minutes  
**Pre-requisite:** Level 3 (Parameterized Tests) completed

---

## 🎯 What You Will Learn

```
┌─────────────────────────────────────────────────────────────────┐
│                    LEVEL 4 LEARNING PATH                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  4.1  Why Mock? The Problem with Real Dependencies              │
│   ↓                                                             │
│  4.2  Mockito Setup and Annotations                             │
│   ↓                                                             │
│  4.3  @Mock and @InjectMocks                                    │
│   ↓                                                             │
│  4.4  Stubbing with when().thenReturn()                         │
│   ↓                                                             │
│  4.5  Stubbing Exceptions with when().thenThrow()               │
│   ↓                                                             │
│  4.6  Verifying Interactions with verify()                      │
│   ↓                                                             │
│  4.7  Argument Matchers (any(), eq(), argThat())                │
│   ↓                                                             │
│  4.8  ArgumentCaptor for Inspection                             │
│   ↓                                                             │
│  4.9  Complete UPITransferService Test Suite                    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 4.1 Why Mock?

### The Problem: Testing with Real Dependencies

```java
// ❌ PROBLEM: UPITransferService depends on AccountRepository
public class UPITransferService {
    private final AccountRepository accountRepository;  // Database dependency!
    
    public TransferResult transfer(String senderId, String receiverId, double amount) {
        Account sender = accountRepository.loadAccountById(senderId);  // DB call
        Account receiver = accountRepository.loadAccountById(receiverId);  // DB call
        // ... business logic ...
        accountRepository.saveAccount(sender);  // DB call
        accountRepository.saveAccount(receiver);  // DB call
    }
}
```

**Problems with real dependencies:**

| Problem | Impact |
|---------|--------|
| Database required | Complex test setup |
| Slow (network I/O) | Long test execution |
| Flaky tests | Database state issues |
| Hard to test errors | Can't force DB failures |
| Not isolated | Testing multiple components |

### The Solution: Mock Dependencies

```java
// ✅ SOLUTION: Mock the repository
@Mock
AccountRepository mockRepository;

@InjectMocks
UPITransferService service;  // Repository is mocked!

@Test
void shouldTransferSuccessfully() {
    // Control mock behavior
    when(mockRepository.loadAccountById("S001")).thenReturn(senderAccount);
    when(mockRepository.loadAccountById("R001")).thenReturn(receiverAccount);
    
    // Test in isolation - no database!
    service.transfer("S001", "R001", 1000.0);
    
    // Verify interactions
    verify(mockRepository).saveAccount(senderAccount);
}
```

---

## 4.2 Mockito Setup

### Maven Dependencies (Already in pom.xml)

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.8.0</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>5.8.0</version>
    <scope>test</scope>
</dependency>
```

### Test Class Setup

```java
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)  // Enable Mockito annotations
class UPITransferServiceTest {

    @Mock
    AccountRepository accountRepository;  // Create mock

    @InjectMocks
    UPITransferService transferService;  // Inject mock into service
    
    // Tests...
}
```

---

## 4.3 @Mock and @InjectMocks

### @Mock - Creates a Mock Object

```java
@Mock
AccountRepository accountRepository;

// Equivalent to:
AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
```

**Default mock behavior:**
- Methods return default values: `null`, `0`, `false`, empty collections
- No real implementation is executed
- All method calls are tracked for verification

### @InjectMocks - Injects Mocks into Target

```java
@InjectMocks
UPITransferService service;

// Mockito automatically:
// 1. Creates UPITransferService instance
// 2. Finds @Mock fields matching constructor parameters
// 3. Injects mocks via constructor injection
```

### Injection Priority

```
1. Constructor injection (preferred)
2. Setter injection
3. Field injection (last resort)
```

---

## 4.4 Stubbing with when().thenReturn()

### Basic Stubbing

```java
@Test
void shouldLoadAccount() {
    // Arrange - Define mock behavior
    Account mockAccount = new Account("ACC001", "Test User", 10000.0);
    when(accountRepository.loadAccountById("ACC001")).thenReturn(mockAccount);
    
    // Act - Call method that uses repository
    Account result = accountRepository.loadAccountById("ACC001");
    
    // Assert
    assertEquals("ACC001", result.getAccountId());
    assertEquals(10000.0, result.getBalance());
}
```

### Stubbing Multiple Methods

```java
// Different returns for different arguments
when(accountRepository.loadAccountById("SENDER")).thenReturn(senderAccount);
when(accountRepository.loadAccountById("RECEIVER")).thenReturn(receiverAccount);
when(accountRepository.loadAccountById("UNKNOWN")).thenReturn(null);

// Stub boolean methods
when(accountRepository.existsById("ACC001")).thenReturn(true);
when(accountRepository.existsById("UNKNOWN")).thenReturn(false);
```

### Consecutive Returns

```java
// Different values for consecutive calls
when(accountRepository.loadAccountById("ACC001"))
    .thenReturn(account1)   // First call
    .thenReturn(account2)   // Second call
    .thenReturn(account3);  // Third call and beyond
```

---

## 4.5 Stubbing Exceptions

### when().thenThrow()

```java
@Test
void shouldHandleDatabaseError() {
    // Stub to throw exception
    when(accountRepository.loadAccountById("ERROR"))
        .thenThrow(new RuntimeException("Database connection failed"));
    
    // Verify exception handling
    assertThrows(RuntimeException.class, 
        () -> transferService.transfer("ERROR", "R001", 1000.0));
}
```

### Throwing Custom Exceptions

```java
// Throw our custom exception
when(accountRepository.loadAccountById("UNKNOWN"))
    .thenThrow(new AccountNotFoundException("UNKNOWN"));

// Test exception handling
@Test
void shouldThrowWhenSenderNotFound() {
    when(accountRepository.loadAccountById("UNKNOWN"))
        .thenThrow(new AccountNotFoundException("UNKNOWN"));
    
    assertThrows(AccountNotFoundException.class,
        () -> transferService.transfer("UNKNOWN", "R001", 1000.0));
}
```

---

## 4.6 Verifying Interactions

### Basic verify()

```java
@Test
void shouldSaveAccountsAfterTransfer() {
    // Arrange
    when(accountRepository.loadAccountById("S001")).thenReturn(sender);
    when(accountRepository.loadAccountById("R001")).thenReturn(receiver);
    
    // Act
    transferService.transfer("S001", "R001", 1000.0);
    
    // Verify - Check that save was called
    verify(accountRepository).saveAccount(sender);
    verify(accountRepository).saveAccount(receiver);
}
```

### Verify Call Count

```java
// Exact count
verify(accountRepository, times(2)).saveAccount(any());

// At least / At most
verify(accountRepository, atLeast(1)).loadAccountById(any());
verify(accountRepository, atMost(3)).saveAccount(any());

// Never called
verify(accountRepository, never()).deleteAccount(any());

// At least once (same as times(1))
verify(accountRepository, atLeastOnce()).saveAccount(any());
```

### Verify Call Order

```java
@Test
void shouldLoadBeforeSave() {
    // Arrange & Act
    when(accountRepository.loadAccountById(any())).thenReturn(account);
    transferService.transfer("S001", "R001", 1000.0);
    
    // Verify order
    InOrder inOrder = inOrder(accountRepository);
    inOrder.verify(accountRepository).loadAccountById("S001");
    inOrder.verify(accountRepository).loadAccountById("R001");
    inOrder.verify(accountRepository, times(2)).saveAccount(any());
}
```

### Verify No More Interactions

```java
@Test
void shouldOnlyCallRequiredMethods() {
    // Arrange & Act
    when(accountRepository.loadAccountById(any())).thenReturn(account);
    transferService.checkBalance("ACC001");
    
    // Verify only load was called, nothing else
    verify(accountRepository).loadAccountById("ACC001");
    verifyNoMoreInteractions(accountRepository);
}
```

---

## 4.7 Argument Matchers

### Common Matchers

```java
import static org.mockito.ArgumentMatchers.*;

// any() - matches any value (including null)
when(accountRepository.loadAccountById(any())).thenReturn(account);

// anyString(), anyDouble(), anyInt(), anyLong()
when(service.transfer(anyString(), anyString(), anyDouble())).thenReturn(result);

// eq() - matches exact value
when(accountRepository.loadAccountById(eq("ACC001"))).thenReturn(account);

// isNull(), isNotNull()
when(accountRepository.loadAccountById(isNull())).thenThrow(new IllegalArgumentException());
```

### Custom Matchers with argThat()

```java
// Match accounts with balance > 1000
when(accountRepository.saveAccount(argThat(acc -> acc.getBalance() > 1000)))
    .thenReturn(true);

// Match IDs starting with "ACC"
when(accountRepository.loadAccountById(argThat(id -> id.startsWith("ACC"))))
    .thenReturn(account);
```

### ⚠️ Important Rule: All or Nothing

```java
// ❌ WRONG: Can't mix matchers and raw values
when(service.transfer("S001", any(), 1000.0));  // Compilation error!

// ✅ CORRECT: Use eq() for exact values when using other matchers
when(service.transfer(eq("S001"), any(), eq(1000.0))).thenReturn(result);
```

---

## 4.8 ArgumentCaptor

### Capturing Arguments for Inspection

```java
@Test
void shouldDebitCorrectAmountFromSender() {
    // Arrange
    ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
    Account sender = new Account("S001", "Sender", 10000.0);
    Account receiver = new Account("R001", "Receiver", 5000.0);
    
    when(accountRepository.loadAccountById("S001")).thenReturn(sender);
    when(accountRepository.loadAccountById("R001")).thenReturn(receiver);
    
    // Act
    transferService.transfer("S001", "R001", 1000.0);
    
    // Capture arguments passed to saveAccount
    verify(accountRepository, times(2)).saveAccount(accountCaptor.capture());
    
    // Inspect captured values
    List<Account> savedAccounts = accountCaptor.getAllValues();
    
    assertEquals(9000.0, savedAccounts.get(0).getBalance());  // Sender: 10000 - 1000
    assertEquals(6000.0, savedAccounts.get(1).getBalance());  // Receiver: 5000 + 1000
}
```

### Using @Captor Annotation

```java
@Captor
ArgumentCaptor<Account> accountCaptor;

@Test
void shouldCaptureWithAnnotation() {
    // ... test code ...
    verify(accountRepository).saveAccount(accountCaptor.capture());
    assertEquals("ACC001", accountCaptor.getValue().getAccountId());
}
```

---

## 4.9 Complete Test Pattern

```java
@ExtendWith(MockitoExtension.class)
class UPITransferServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    UPITransferService transferService;

    private Account sender;
    private Account receiver;

    @BeforeEach
    void setUp() {
        sender = new Account("S001", "Sender", 10000.0);
        receiver = new Account("R001", "Receiver", 5000.0);
    }

    @Test
    @DisplayName("Should transfer money successfully")
    void shouldTransferSuccessfully() {
        // ═══════════════════════════════════════════════
        // ARRANGE
        // ═══════════════════════════════════════════════
        when(accountRepository.loadAccountById("S001")).thenReturn(sender);
        when(accountRepository.loadAccountById("R001")).thenReturn(receiver);

        // ═══════════════════════════════════════════════
        // ACT
        // ═══════════════════════════════════════════════
        transferService.transfer("S001", "R001", 1000.0);

        // ═══════════════════════════════════════════════
        // ASSERT
        // ═══════════════════════════════════════════════
        // Verify balances changed
        assertEquals(9000.0, sender.getBalance());
        assertEquals(6000.0, receiver.getBalance());
        
        // Verify repository interactions
        verify(accountRepository).loadAccountById("S001");
        verify(accountRepository).loadAccountById("R001");
        verify(accountRepository, times(2)).saveAccount(any());
    }
}
```

---

## 🎓 Key Takeaways

| Concept | Purpose | Example |
|---------|---------|---------|
| `@Mock` | Create mock object | `@Mock AccountRepository repo` |
| `@InjectMocks` | Inject mocks into target | `@InjectMocks Service service` |
| `when().thenReturn()` | Define return value | `when(repo.find("X")).thenReturn(obj)` |
| `when().thenThrow()` | Simulate exception | `when(repo.find("X")).thenThrow(ex)` |
| `verify()` | Check method called | `verify(repo).save(account)` |
| `verify(times(n))` | Check call count | `verify(repo, times(2)).save(any())` |
| `any()`, `eq()` | Argument matchers | `when(repo.find(any()))` |
| `ArgumentCaptor` | Capture arguments | Inspect passed values |

---

*Level 4 - Mockito Basics*
