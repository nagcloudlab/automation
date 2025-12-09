# Level 2: Exception Testing
## Custom Exceptions & Validation Patterns

**Objective:** Master exception testing with banking-specific custom exceptions  
**Duration:** 60-90 minutes  
**Pre-requisite:** Level 1 (JUnit 5 Basics) completed

---

## 🎯 What You Will Learn

```
┌─────────────────────────────────────────────────────────────────┐
│                    LEVEL 2 LEARNING PATH                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  2.1  Why Custom Exceptions?                                    │
│   ↓                                                             │
│  2.2  Banking Exception Hierarchy                               │
│   ↓                                                             │
│  2.3  Creating Custom Exceptions                                │
│   ↓                                                             │
│  2.4  Refactoring Code to Use Custom Exceptions                 │
│   ↓                                                             │
│  2.5  Testing assertThrows in Depth                             │
│   ↓                                                             │
│  2.6  Testing Exception Messages & Causes                       │
│   ↓                                                             │
│  2.7  @Nested Test Organization                                 │
│   ↓                                                             │
│  2.8  Complete Exception Test Suite                             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2.1 Why Custom Exceptions?

### The Problem with Generic Exceptions

```java
// ❌ BAD: Using generic exceptions
throw new RuntimeException("Account not found");
throw new IllegalArgumentException("Insufficient balance");
throw new Exception("Transaction failed");
```

**Problems:**
- Hard to catch specific errors
- No semantic meaning
- Poor error handling in calling code
- No additional context (error codes, amounts, etc.)

### The Solution: Custom Banking Exceptions

```java
// ✅ GOOD: Using domain-specific exceptions
throw new AccountNotFoundException("ACC001");
throw new InsufficientBalanceException(10000.0, 50000.0);
throw new TransactionLimitExceededException(150000.0, UPI_MAX_LIMIT);
```

**Benefits:**
- Self-documenting code
- Easy to catch and handle specifically
- Can carry additional context (account ID, amounts)
- Maps to NPCI error codes

---

## 2.2 Banking Exception Hierarchy

```
┌─────────────────────────────────────────────────────────────────┐
│                    EXCEPTION HIERARCHY                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│                    RuntimeException                             │
│                          │                                      │
│                          ▼                                      │
│               TransferException (Base)                          │
│                          │                                      │
│          ┌───────────────┼───────────────┐                      │
│          ▼               ▼               ▼                      │
│   AccountException  AmountException  SystemException            │
│          │               │               │                      │
│     ┌────┴────┐     ┌────┴────┐     ┌────┴────┐                │
│     ▼         ▼     ▼         ▼     ▼         ▼                │
│  Account   Account  Insuff.  Invalid  Timeout  Service         │
│  NotFound  Inactive Balance  Amount   Error    Unavailable     │
│                                                                 │
│  NPCI Codes:                                                    │
│  U30       U16      U30      U09      U68      U91             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### NPCI Error Code Mapping

| Exception | NPCI Code | Description |
|-----------|-----------|-------------|
| AccountNotFoundException | U30 | Beneficiary account not found |
| AccountInactiveException | U16 | Risk threshold exceeded |
| InsufficientBalanceException | U30 | Insufficient funds |
| InvalidAmountException | U09 | Transaction limit exceeded |
| TransactionLimitExceededException | U09 | Exceeds per-transaction limit |
| TimeoutException | U68 | Transaction timeout |
| ServiceUnavailableException | U91 | Service temporarily unavailable |

---

## 2.3 Custom Exception Design

### Base Exception with Error Code

```java
public abstract class TransferException extends RuntimeException {
    
    private final String errorCode;
    private final LocalDateTime timestamp;
    
    protected TransferException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getErrorCode() { return errorCode; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
```

### Domain-Specific Exception

```java
public class InsufficientBalanceException extends TransferException {
    
    private final double availableBalance;
    private final double requestedAmount;
    
    public InsufficientBalanceException(double available, double requested) {
        super(
            String.format("Insufficient balance: Available ₹%.2f, Requested ₹%.2f", 
                available, requested),
            "U30"
        );
        this.availableBalance = available;
        this.requestedAmount = requested;
    }
    
    public double getAvailableBalance() { return availableBalance; }
    public double getRequestedAmount() { return requestedAmount; }
    public double getShortfall() { return requestedAmount - availableBalance; }
}
```

---

## 2.4 Testing Exception Details

### Basic assertThrows

```java
@Test
void shouldThrowExceptionForInsufficientBalance() {
    assertThrows(InsufficientBalanceException.class,
        () -> account.debit(999999.0));
}
```

### Capturing and Inspecting Exception

```java
@Test
void shouldIncludeBalanceDetailsInException() {
    // Arrange
    Account account = new Account("ACC001", "Test", 1000.0);
    
    // Act
    InsufficientBalanceException exception = assertThrows(
        InsufficientBalanceException.class,
        () -> account.debit(5000.0)
    );
    
    // Assert - Verify exception details
    assertEquals(1000.0, exception.getAvailableBalance());
    assertEquals(5000.0, exception.getRequestedAmount());
    assertEquals(4000.0, exception.getShortfall());
    assertEquals("U30", exception.getErrorCode());
}
```

### Testing Exception Message

```java
@Test
void shouldHaveDescriptiveErrorMessage() {
    InsufficientBalanceException exception = assertThrows(
        InsufficientBalanceException.class,
        () -> account.debit(50000.0)
    );
    
    // Verify message contains key information
    assertAll("Exception message should be descriptive",
        () -> assertTrue(exception.getMessage().contains("Insufficient")),
        () -> assertTrue(exception.getMessage().contains("₹")),
        () -> assertTrue(exception.getMessage().contains("10000")), // available
        () -> assertTrue(exception.getMessage().contains("50000"))  // requested
    );
}
```

---

## 2.5 @Nested Test Organization

### Organizing by Exception Type

```java
@DisplayName("UPI Transfer Service - Exception Tests")
class UPITransferServiceExceptionTest {

    @Nested
    @DisplayName("Account Exceptions")
    class AccountExceptionTests {
        
        @Test
        void shouldThrowWhenSenderNotFound() { }
        
        @Test
        void shouldThrowWhenReceiverNotFound() { }
    }
    
    @Nested
    @DisplayName("Amount Exceptions")
    class AmountExceptionTests {
        
        @Test
        void shouldThrowForInsufficientBalance() { }
        
        @Test
        void shouldThrowForExceedingLimit() { }
    }
    
    @Nested
    @DisplayName("Validation Exceptions")
    class ValidationExceptionTests {
        
        @Test
        void shouldThrowForNegativeAmount() { }
        
        @Test
        void shouldThrowForSameAccount() { }
    }
}
```

---

## 2.6 Test Patterns

### Pattern 1: Exception Type Verification

```java
@Test
void shouldThrowCorrectExceptionType() {
    Exception exception = assertThrows(
        InsufficientBalanceException.class,  // Exact type
        () -> account.debit(999999.0)
    );
    
    // Verify it's exactly this type, not a subclass
    assertEquals(InsufficientBalanceException.class, exception.getClass());
}
```

### Pattern 2: Exception Hierarchy Testing

```java
@Test
void shouldBeInstanceOfParentException() {
    Exception exception = assertThrows(
        InsufficientBalanceException.class,
        () -> account.debit(999999.0)
    );
    
    // Verify inheritance
    assertInstanceOf(TransferException.class, exception);
    assertInstanceOf(RuntimeException.class, exception);
}
```

### Pattern 3: No Exception Expected

```java
@Test
void shouldNotThrowForValidOperation() {
    assertDoesNotThrow(() -> account.debit(100.0));
}
```

### Pattern 4: State Unchanged After Exception

```java
@Test
void stateShouldNotChangeAfterException() {
    double originalBalance = account.getBalance();
    
    assertThrows(InsufficientBalanceException.class,
        () -> account.debit(999999.0));
    
    assertEquals(originalBalance, account.getBalance(),
        "Balance should remain unchanged after failed operation");
}
```

---

## 🎓 Key Takeaways

| Concept | Description |
|---------|-------------|
| Custom Exceptions | Domain-specific with context |
| Error Codes | Map to NPCI codes (U30, U09, etc.) |
| assertThrows | Returns exception for inspection |
| assertDoesNotThrow | Verify no exception thrown |
| @Nested | Organize related tests |
| Exception Details | Test message, code, context |

---

## ✅ Exercises

1. Create `AccountNotFoundException` with account ID
2. Create `TransactionLimitExceededException` with amount and limit
3. Refactor `Account.debit()` to use `InsufficientBalanceException`
4. Write tests verifying exception details
5. Test exception inheritance hierarchy

---

*Level 2 - Exception Testing*
