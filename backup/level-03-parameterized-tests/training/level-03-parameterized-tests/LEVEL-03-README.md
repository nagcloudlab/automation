# Level 3: Parameterized Tests
## Data-Driven Testing with JUnit 5

**Objective:** Master parameterized tests for efficient data-driven testing  
**Duration:** 60-90 minutes  
**Pre-requisite:** Level 2 (Exception Testing) completed

---

## 🎯 What You Will Learn

```
┌─────────────────────────────────────────────────────────────────┐
│                    LEVEL 3 LEARNING PATH                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  3.1  Why Parameterized Tests?                                  │
│   ↓                                                             │
│  3.2  @ParameterizedTest Basics                                 │
│   ↓                                                             │
│  3.3  @ValueSource - Single Parameter                           │
│   ↓                                                             │
│  3.4  @CsvSource - Multiple Parameters                          │
│   ↓                                                             │
│  3.5  @CsvFileSource - External Test Data                       │
│   ↓                                                             │
│  3.6  @MethodSource - Complex Data                              │
│   ↓                                                             │
│  3.7  @EnumSource - Enum-Based Tests                            │
│   ↓                                                             │
│  3.8  Custom Display Names                                      │
│   ↓                                                             │
│  3.9  UPI Transaction Validation Suite                          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 3.1 Why Parameterized Tests?

### The Problem: Repetitive Tests

```java
// ❌ BAD: Writing separate tests for each value
@Test void shouldRejectNegative100() { assertThrows(..., () -> account.debit(-100)); }
@Test void shouldRejectNegative50() { assertThrows(..., () -> account.debit(-50)); }
@Test void shouldRejectNegative1() { assertThrows(..., () -> account.debit(-1)); }
@Test void shouldRejectZero() { assertThrows(..., () -> account.debit(0)); }
// ... 20 more similar tests
```

### The Solution: Parameterized Tests

```java
// ✅ GOOD: One test method, multiple data points
@ParameterizedTest
@ValueSource(doubles = {-100, -50, -1, -0.01, 0})
void shouldRejectInvalidAmounts(double amount) {
    assertThrows(InvalidAmountException.class, () -> account.debit(amount));
}
```

### Benefits

| Benefit | Description |
|---------|-------------|
| **DRY** | Don't Repeat Yourself - one test, many inputs |
| **Coverage** | Easy to add more test cases |
| **Maintenance** | Change logic once, applies to all |
| **Readability** | Clear separation of test data and logic |
| **Reporting** | Each parameter shows as separate test |

---

## 3.2 Maven Dependency

```xml
<!-- Already included in junit-jupiter, but explicitly: -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <version>5.10.1</version>
    <scope>test</scope>
</dependency>
```

---

## 3.3 @ValueSource - Single Parameter

### Supported Types

| Type | Annotation | Example |
|------|------------|---------|
| int | `@ValueSource(ints = {...})` | `ints = {1, 2, 3}` |
| long | `@ValueSource(longs = {...})` | `longs = {1L, 2L}` |
| double | `@ValueSource(doubles = {...})` | `doubles = {1.0, 2.5}` |
| String | `@ValueSource(strings = {...})` | `strings = {"a", "b"}` |
| Class | `@ValueSource(classes = {...})` | `classes = {String.class}` |

### Example: Testing Invalid Amounts

```java
@ParameterizedTest
@ValueSource(doubles = {-1000, -100, -1, -0.01, 0})
@DisplayName("Should reject invalid debit amounts")
void shouldRejectInvalidDebitAmounts(double invalidAmount) {
    Account account = new Account("ACC001", "Test", 10000.0);
    assertThrows(InvalidAmountException.class, () -> account.debit(invalidAmount));
}
```

### @NullSource and @EmptySource

```java
@ParameterizedTest
@NullSource  // Passes null
@EmptySource // Passes "" for strings, empty collection for collections
@NullAndEmptySource // Both null and empty
void shouldRejectNullOrEmptyAccountId(String accountId) {
    assertThrows(IllegalArgumentException.class, 
        () -> new Account(accountId, "Name", 1000.0));
}
```

---

## 3.4 @CsvSource - Multiple Parameters

### Basic Syntax

```java
@ParameterizedTest
@CsvSource({
    "1000, 500, 500",     // balance, debit, expected
    "5000, 3000, 2000",
    "10000, 10000, 0"
})
void shouldDebitCorrectly(double balance, double debit, double expected) {
    Account account = new Account("ACC001", "Test", balance);
    account.debit(debit);
    assertEquals(expected, account.getBalance());
}
```

### With Custom Delimiter

```java
@ParameterizedTest
@CsvSource(value = {
    "1000 | 500 | 500",
    "5000 | 3000 | 2000"
}, delimiter = '|')
void shouldDebitCorrectly(double balance, double debit, double expected) {
    // ...
}
```

### With Null Values

```java
@CsvSource({
    "ACC001, Rajesh, 1000",
    "ACC002, '', 500",       // Empty string
    "ACC003, , 0"            // null (no quotes)
})
void testAccountCreation(String id, String name, double balance) {
    // name will be null for third row
}
```

### Using nullValues Parameter

```java
@CsvSource(value = {
    "ACC001, Rajesh, 1000",
    "ACC002, NULL, 500"      // NULL becomes null
}, nullValues = "NULL")
```

---

## 3.5 @CsvFileSource - External Test Data

### CSV File: `test-data/upi-transactions.csv`

```csv
# UPI Transaction Test Data
senderBalance,amount,expectedSenderBalance,expectedResult
10000,1000,9000,SUCCESS
10000,10000,0,SUCCESS
10000,10001,10000,INSUFFICIENT_BALANCE
5000,100000,5000,LIMIT_EXCEEDED
```

### Using @CsvFileSource

```java
@ParameterizedTest
@CsvFileSource(
    resources = "/test-data/upi-transactions.csv",
    numLinesToSkip = 2,  // Skip header and comment
    delimiter = ','
)
void shouldProcessTransactions(
    double senderBalance, 
    double amount, 
    double expectedBalance, 
    String expectedResult
) {
    // Test logic
}
```

---

## 3.6 @MethodSource - Complex Data

### Basic Method Source

```java
@ParameterizedTest
@MethodSource("provideValidTransferAmounts")
void shouldTransferValidAmounts(double amount) {
    // Test logic
}

// Method must be static (unless test class is @TestInstance(PER_CLASS))
static Stream<Double> provideValidTransferAmounts() {
    return Stream.of(1.0, 100.0, 1000.0, 50000.0, 100000.0);
}
```

### With Multiple Parameters using Arguments

```java
@ParameterizedTest
@MethodSource("provideTransferScenarios")
void shouldHandleTransferScenarios(
    double balance, 
    double amount, 
    Class<? extends Exception> expectedException
) {
    Account account = new Account("ACC001", "Test", balance);
    assertThrows(expectedException, () -> account.debit(amount));
}

static Stream<Arguments> provideTransferScenarios() {
    return Stream.of(
        Arguments.of(1000.0, 2000.0, InsufficientBalanceException.class),
        Arguments.of(1000.0, -100.0, InvalidAmountException.class),
        Arguments.of(0.0, 100.0, InsufficientBalanceException.class)
    );
}
```

### External Method Source

```java
@ParameterizedTest
@MethodSource("com.example.testdata.TransactionTestData#validAmounts")
void shouldAcceptValidAmounts(double amount) {
    // Test logic
}
```

---

## 3.7 @EnumSource - Enum-Based Tests

### Testing All Enum Values

```java
@ParameterizedTest
@EnumSource(InvalidAmountException.AmountValidationType.class)
void shouldHaveDescriptionForAllValidationTypes(
    InvalidAmountException.AmountValidationType type
) {
    assertNotNull(type.getDescription());
    assertFalse(type.getDescription().isEmpty());
}
```

### Filtering Enum Values

```java
// Include specific values
@EnumSource(value = LimitType.class, names = {"DAILY", "MONTHLY"})

// Exclude specific values
@EnumSource(value = LimitType.class, mode = EnumSource.Mode.EXCLUDE, 
            names = {"PER_TRANSACTION_MIN"})

// Regex matching
@EnumSource(value = LimitType.class, mode = EnumSource.Mode.MATCH_ALL, 
            names = ".*TRANSACTION.*")
```

---

## 3.8 Custom Display Names

### Using @DisplayName with Index

```java
@ParameterizedTest(name = "[{index}] amount={0} should be rejected")
@ValueSource(doubles = {-100, -1, 0})
void shouldRejectInvalidAmounts(double amount) {
    // Test logic
}
// Output:
// [1] amount=-100.0 should be rejected
// [2] amount=-1.0 should be rejected
// [3] amount=0.0 should be rejected
```

### Using Named Arguments

```java
@ParameterizedTest(name = "Transfer ₹{1} from ₹{0} balance → ₹{2}")
@CsvSource({
    "10000, 3000, 7000",
    "5000, 5000, 0"
})
void shouldCalculateBalanceCorrectly(double initial, double transfer, double expected) {
    // ...
}
// Output:
// Transfer ₹3000.0 from ₹10000.0 balance → ₹7000.0
// Transfer ₹5000.0 from ₹5000.0 balance → ₹0.0
```

---

## 3.9 UPI Transaction Scenarios

### Test Data Categories for UPI

```
┌─────────────────────────────────────────────────────────────────┐
│                    UPI TEST DATA CATEGORIES                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  VALID AMOUNTS:                                                 │
│  ├── Minimum: ₹1                                                │
│  ├── Typical: ₹100, ₹500, ₹1000, ₹5000                         │
│  ├── Large: ₹50,000, ₹99,999                                   │
│  └── Maximum: ₹1,00,000                                         │
│                                                                 │
│  INVALID AMOUNTS:                                               │
│  ├── Negative: -₹100, -₹1, -₹0.01                              │
│  ├── Zero: ₹0                                                   │
│  ├── Below minimum: ₹0.50, ₹0.99                               │
│  └── Above maximum: ₹1,00,001, ₹1,50,000                       │
│                                                                 │
│  EDGE CASES:                                                    │
│  ├── Boundary: ₹1 (min), ₹1,00,000 (max)                       │
│  ├── Precision: ₹0.01, ₹99,999.99                              │
│  └── Exact balance: debit == balance                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🎓 Key Takeaways

| Annotation | Use Case | Parameters |
|------------|----------|------------|
| `@ValueSource` | Single primitive/String | One parameter |
| `@CsvSource` | Multiple inline values | Multiple parameters |
| `@CsvFileSource` | External CSV file | Large test datasets |
| `@MethodSource` | Complex objects | Any type, computed values |
| `@EnumSource` | Test enum values | Filter by name/mode |
| `@NullSource` | Test null handling | Null value |
| `@EmptySource` | Test empty handling | Empty string/collection |

---

## ✅ Exercises

1. Create parameterized test for all valid UPI amounts (₹1 to ₹1,00,000)
2. Test invalid amounts using @CsvSource with expected exception type
3. Create CSV file with 20+ transaction scenarios
4. Use @MethodSource to generate boundary test cases
5. Test all NPCI error codes using @EnumSource

---

*Level 3 - Parameterized Tests*
