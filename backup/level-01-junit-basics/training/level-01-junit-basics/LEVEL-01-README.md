# Level 1: JUnit 5 Basics
## Unit Testing the Account Model

**Objective:** Write your first unit tests using JUnit 5  
**Duration:** 60-90 minutes  
**Pre-requisite:** Level 0 (Code Refactoring) completed

---

## 🎯 What You Will Learn

```
┌─────────────────────────────────────────────────────────────────┐
│                    LEVEL 1 LEARNING PATH                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1.1  JUnit 5 Setup & Configuration                             │
│   ↓                                                             │
│  1.2  Your First Test - @Test Annotation                        │
│   ↓                                                             │
│  1.3  Assertions - assertEquals, assertTrue, assertThrows       │
│   ↓                                                             │
│  1.4  Test Lifecycle - @BeforeEach, @AfterEach                  │
│   ↓                                                             │
│  1.5  AAA Pattern - Arrange, Act, Assert                        │
│   ↓                                                             │
│  1.6  Test Naming Conventions                                   │
│   ↓                                                             │
│  1.7  @DisplayName for Readable Test Reports                    │
│   ↓                                                             │
│  1.8  Complete Account Test Suite                               │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📦 Maven Dependencies for Testing

Add these to your `pom.xml`:

```xml
<dependencies>
    <!-- JUnit 5 (Jupiter) -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
    
    <!-- AssertJ - Fluent Assertions (Optional but recommended) -->
    <dependency>
        <groupId>org.assertj</groupId>
        <artifactId>assertj-core</artifactId>
        <version>3.24.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- Surefire Plugin for running tests -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.2.2</version>
        </plugin>
    </plugins>
</build>
```

---

## 1.1 Understanding Unit Tests

### What is a Unit Test?

A **unit test** verifies a single "unit" of code (typically a method) in isolation.

```
┌─────────────────────────────────────────────────────────────────┐
│                    UNIT TEST CHARACTERISTICS                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ✓ FAST        - Runs in milliseconds                          │
│  ✓ ISOLATED    - No external dependencies (DB, network)        │
│  ✓ REPEATABLE  - Same result every time                        │
│  ✓ SELF-VALIDATING - Pass or fail, no manual checking          │
│  ✓ TIMELY      - Written close to the code being tested        │
│                                                                 │
│  These are called the F.I.R.S.T principles                      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Why Test the Account Model First?

The `Account` class is a **pure model** with:
- No external dependencies
- Clear input/output behavior
- Business logic (debit/credit validation)

This makes it perfect for learning unit testing!

---

## 1.2 Your First Test - @Test Annotation

### Basic Test Structure

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void myFirstTest() {
        // This is a test method!
        // JUnit will execute any method annotated with @Test
        
        int result = 2 + 2;
        assertEquals(4, result);
    }
}
```

### Key Points:
- `@Test` marks a method as a test case
- Test class name convention: `<ClassName>Test` (e.g., `AccountTest`)
- Test method should be `void` and take no parameters
- Use `static import` for assertions

---

## 1.3 JUnit 5 Assertions

### Common Assertions

| Assertion | Purpose | Example |
|-----------|---------|---------|
| `assertEquals(expected, actual)` | Check equality | `assertEquals(100, account.getBalance())` |
| `assertNotEquals(unexpected, actual)` | Check inequality | `assertNotEquals(0, balance)` |
| `assertTrue(condition)` | Check boolean true | `assertTrue(account.getBalance() > 0)` |
| `assertFalse(condition)` | Check boolean false | `assertFalse(account.getBalance() < 0)` |
| `assertNull(object)` | Check null | `assertNull(account.getUpiId())` |
| `assertNotNull(object)` | Check not null | `assertNotNull(account.getAccountId())` |
| `assertThrows(Exception.class, () -> ...)` | Check exception | `assertThrows(IllegalArgumentException.class, () -> account.debit(-100))` |
| `assertDoesNotThrow(() -> ...)` | Check no exception | `assertDoesNotThrow(() -> account.credit(100))` |

### Assertion with Custom Message

```java
assertEquals(expected, actual, "Custom failure message");

// Example:
assertEquals(7000.0, account.getBalance(), 
    "Balance should be 7000 after debiting 3000 from 10000");
```

---

## 1.4 The AAA Pattern

Every test should follow the **Arrange-Act-Assert** pattern:

```java
@Test
void shouldDebitAmountFromAccount() {
    // ═══════════════════════════════════════════
    // ARRANGE - Set up the test scenario
    // ═══════════════════════════════════════════
    Account account = new Account("ACC001", "Rajesh Kumar", 10000.0);
    
    // ═══════════════════════════════════════════
    // ACT - Execute the behavior being tested
    // ═══════════════════════════════════════════
    account.debit(3000.0);
    
    // ═══════════════════════════════════════════
    // ASSERT - Verify the expected outcome
    // ═══════════════════════════════════════════
    assertEquals(7000.0, account.getBalance());
}
```

### Why AAA?
- **Clarity**: Easy to understand what's being tested
- **Consistency**: All tests follow same structure
- **Debugging**: Easy to identify which part failed

---

## 1.5 Test Lifecycle Annotations

### @BeforeEach and @AfterEach

```java
class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        // Runs BEFORE each test method
        // Use for common setup
        account = new Account("ACC001", "Rajesh Kumar", 10000.0);
        System.out.println("Setting up test...");
    }

    @AfterEach
    void tearDown() {
        // Runs AFTER each test method
        // Use for cleanup
        account = null;
        System.out.println("Cleaning up test...");
    }

    @Test
    void test1() {
        // account is fresh instance here
    }

    @Test
    void test2() {
        // account is another fresh instance here
    }
}
```

### @BeforeAll and @AfterAll

```java
class AccountTest {

    @BeforeAll
    static void initAll() {
        // Runs ONCE before all tests
        // Must be static
        System.out.println("Starting test suite...");
    }

    @AfterAll
    static void tearDownAll() {
        // Runs ONCE after all tests
        // Must be static
        System.out.println("Test suite completed.");
    }
}
```

### Lifecycle Execution Order

```
@BeforeAll (once)
    │
    ├── @BeforeEach
    │       └── @Test method1
    │   @AfterEach
    │
    ├── @BeforeEach
    │       └── @Test method2
    │   @AfterEach
    │
    └── ... more tests ...
    
@AfterAll (once)
```

---

## 1.6 Test Naming Conventions

### Good Test Names

Test names should describe:
1. **What** is being tested
2. **Under what conditions**
3. **What is the expected result**

```java
// Pattern: should_ExpectedBehavior_When_Condition
void shouldThrowException_WhenDebitAmountExceedsBalance()

// Pattern: methodName_StateUnderTest_ExpectedBehavior
void debit_WithAmountGreaterThanBalance_ThrowsException()

// Pattern: givenCondition_whenAction_thenResult
void givenInsufficientBalance_whenDebit_thenThrowException()

// Simple descriptive (most common)
void shouldRejectDebitWhenInsufficientBalance()
void shouldCreditAmountToAccount()
void shouldCreateAccountWithInitialBalance()
```

### Using @DisplayName

```java
@Test
@DisplayName("Should debit ₹3,000 from account with ₹10,000 balance")
void shouldDebitAmountFromAccount() {
    // test code
}
```

This produces readable test reports:
```
✓ Should debit ₹3,000 from account with ₹10,000 balance
✓ Should throw exception when debiting more than balance
✓ Should create account with initial balance
```

---

## 1.7 Testing Exception Scenarios

### Using assertThrows

```java
@Test
@DisplayName("Should throw exception when debiting more than balance")
void shouldThrowExceptionForInsufficientBalance() {
    // Arrange
    Account account = new Account("ACC001", "Rajesh", 5000.0);
    
    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> account.debit(10000.0)  // Lambda expression
    );
    
    // Optionally verify exception message
    assertEquals("Insufficient balance", exception.getMessage());
}
```

### Testing Multiple Exception Scenarios

```java
@Test
@DisplayName("Should reject negative debit amount")
void shouldRejectNegativeDebitAmount() {
    Account account = new Account("ACC001", "Rajesh", 10000.0);
    
    assertThrows(IllegalArgumentException.class, 
        () -> account.debit(-500.0));
}

@Test
@DisplayName("Should reject zero debit amount")
void shouldRejectZeroDebitAmount() {
    Account account = new Account("ACC001", "Rajesh", 10000.0);
    
    assertThrows(IllegalArgumentException.class, 
        () -> account.debit(0));
}
```

---

## 1.8 Complete Test Examples

### Test File Structure

```
src/
├── main/java/com/example/model/Account.java
└── test/java/com/example/model/AccountTest.java  ← Mirror structure!
```

### Basic Functionality Tests

See the complete test files in:
- `AccountTest.java` - Basic tests
- `AccountValidationTest.java` - Exception/validation tests
- `AccountEdgeCaseTest.java` - Edge case tests

---

## 🎓 Key Takeaways

| Concept | Description |
|---------|-------------|
| `@Test` | Marks method as test case |
| `@DisplayName` | Human-readable test name |
| `@BeforeEach` | Runs before each test |
| `@AfterEach` | Runs after each test |
| `assertEquals` | Checks two values are equal |
| `assertThrows` | Checks exception is thrown |
| AAA Pattern | Arrange → Act → Assert |

---

## ✅ Exercises

### Exercise 1: Basic Tests
Write tests for:
1. Creating an account with valid data
2. Getting account holder name
3. Getting account ID

### Exercise 2: Debit Tests
Write tests for:
1. Valid debit operation
2. Debit exact balance (should leave 0)
3. Debit more than balance (should throw)
4. Debit negative amount (should throw)
5. Debit zero amount (should throw)

### Exercise 3: Credit Tests
Write tests for:
1. Valid credit operation
2. Credit to zero balance account
3. Credit negative amount (should throw)
4. Credit zero amount (should throw)

### Exercise 4: Edge Cases
Write tests for:
1. Multiple debits in sequence
2. Debit followed by credit
3. Very small amounts (₹0.01)
4. Very large amounts (₹99,999.99)

---

## 🔜 Next Level Preview

**Level 2: Testing Exception Scenarios** - Deep dive into exception testing, custom exceptions, and validation patterns.

---

*Level 1 - JUnit 5 Basics*
