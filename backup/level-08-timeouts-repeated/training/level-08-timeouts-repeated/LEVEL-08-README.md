# Level 8: Timeouts & Repeated Tests
## Handling Time-Sensitive Tests and Ensuring Stability

**Objective:** Master JUnit 5 timeout mechanisms and repeated test execution for robust testing  
**Duration:** 60-90 minutes  
**Pre-requisite:** Level 7 (Assumptions & Conditional Tests) completed

---

## 🎯 What You Will Learn

```
┌─────────────────────────────────────────────────────────────────┐
│                    LEVEL 8 LEARNING PATH                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  8.1  Why Timeouts Matter                                       │
│   ↓                                                             │
│  8.2  @Timeout Annotation                                       │
│   ↓                                                             │
│  8.3  assertTimeout() Method                                    │
│   ↓                                                             │
│  8.4  assertTimeoutPreemptively()                               │
│   ↓                                                             │
│  8.5  @RepeatedTest Basics                                      │
│   ↓                                                             │
│  8.6  RepetitionInfo Parameter                                  │
│   ↓                                                             │
│  8.7  Custom Display Names for Repetitions                      │
│   ↓                                                             │
│  8.8  Combining Timeouts and Repetitions                        │
│   ↓                                                             │
│  8.9  Flaky Test Detection                                      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 8.1 Why Timeouts Matter

### Banking Context

```
┌─────────────────────────────────────────────────────────────────┐
│                    UPI TRANSACTION SLAs                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  NPCI mandates strict response times:                           │
│                                                                 │
│  • Transaction Request    → < 500ms                             │
│  • Balance Inquiry        → < 200ms                             │
│  • Account Validation     → < 300ms                             │
│  • Complete Transfer      → < 2 seconds                         │
│                                                                 │
│  Tests MUST verify these SLAs are met!                          │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Problems Without Timeouts

| Issue | Impact |
|-------|--------|
| Infinite loops | Test hangs forever |
| Deadlocks | CI pipeline blocked |
| Slow network calls | Build takes hours |
| Resource leaks | Memory exhaustion |

---

## 8.2 @Timeout Annotation

### Basic Usage

```java
@Test
@Timeout(5)  // 5 seconds (default unit)
void shouldCompleteWithin5Seconds() {
    // Test fails if exceeds 5 seconds
    performOperation();
}

@Test
@Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
void shouldCompleteWithin500Ms() {
    // UPI transaction must complete in 500ms
    processUPITransaction();
}
```

### Time Units

```java
@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
@Timeout(value = 5, unit = TimeUnit.SECONDS)
@Timeout(value = 1, unit = TimeUnit.MINUTES)
```

### Class-Level Timeout

```java
@Timeout(5)  // Apply to ALL tests in class
class TransferServiceTest {

    @Test
    void test1() { /* Max 5 seconds */ }

    @Test
    void test2() { /* Max 5 seconds */ }

    @Test
    @Timeout(10)  // Override: 10 seconds for this test
    void slowTest() { /* Max 10 seconds */ }
}
```

---

## 8.3 assertTimeout() Method

### Non-Preemptive Timeout

```java
@Test
void shouldTransferWithinSLA() {
    // Waits for completion, then checks duration
    TransferResult result = assertTimeout(
        Duration.ofMillis(500),
        () -> service.transfer("A", "B", 1000.0),
        "Transfer must complete within 500ms SLA"
    );
    
    // Can use the result
    assertEquals(1000.0, result.getAmount());
}
```

### Key Behavior

```
┌─────────────────────────────────────────────────────────────────┐
│                    assertTimeout() BEHAVIOR                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Timeline:                                                      │
│  ─────────────────────────────────────────────────────────────  │
│  0ms          500ms        800ms                                │
│  │             │            │                                   │
│  ▼             ▼            ▼                                   │
│  [Start]    [Timeout]   [Completes]                             │
│                            │                                    │
│                    assertTimeout waits for completion           │
│                    then reports: "exceeded by 300ms"            │
│                                                                 │
│  Note: Code continues running until completion!                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 8.4 assertTimeoutPreemptively()

### Preemptive Timeout (Aborts Execution)

```java
@Test
void shouldAbortSlowOperation() {
    // ABORTS execution when timeout reached
    assertTimeoutPreemptively(
        Duration.ofMillis(500),
        () -> {
            // This will be interrupted at 500ms
            Thread.sleep(10000);  // 10 seconds
        },
        "Operation took too long - aborted"
    );
}
```

### Comparison

| Feature | `assertTimeout()` | `assertTimeoutPreemptively()` |
|---------|-------------------|-------------------------------|
| **Execution** | Waits for completion | Aborts at timeout |
| **Thread** | Same thread | Different thread |
| **Result** | Available after completion | May not complete |
| **Use Case** | Verify timing | Prevent hangs |
| **ThreadLocal** | Preserved | NOT preserved |

### ⚠️ Warning: Thread Safety

```java
// ThreadLocal values may not work with assertTimeoutPreemptively!
@Test
void threadLocalIssue() {
    ThreadLocal<String> context = new ThreadLocal<>();
    context.set("main-thread-value");
    
    assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
        // This runs in DIFFERENT thread - context.get() returns null!
        assertNull(context.get());  // ThreadLocal not propagated
    });
}
```

---

## 8.5 @RepeatedTest Basics

### Why Repeat Tests?

| Reason | Explanation |
|--------|-------------|
| **Flaky test detection** | Catch intermittent failures |
| **Concurrency testing** | Race conditions appear randomly |
| **Statistical confidence** | Ensure consistent behavior |
| **Load testing (simple)** | Quick performance baseline |

### Basic Usage

```java
@RepeatedTest(5)
void shouldTransferConsistently() {
    // Runs 5 times
    TransferResult result = service.transfer("A", "B", 100.0);
    assertNotNull(result);
}
```

### Output

```
├─ shouldTransferConsistently() PASSED
│  ├─ repetition 1 of 5 PASSED
│  ├─ repetition 2 of 5 PASSED
│  ├─ repetition 3 of 5 PASSED
│  ├─ repetition 4 of 5 PASSED
│  └─ repetition 5 of 5 PASSED
```

---

## 8.6 RepetitionInfo Parameter

### Accessing Repetition Details

```java
@RepeatedTest(10)
void testWithRepetitionInfo(RepetitionInfo info) {
    int current = info.getCurrentRepetition();
    int total = info.getTotalRepetitions();
    
    System.out.println("Running repetition " + current + " of " + total);
    
    // Use repetition number for varying test data
    double amount = current * 1000.0;  // 1000, 2000, 3000, ...
    service.transfer("A", "B", amount);
}
```

### Progressive Testing

```java
@RepeatedTest(5)
void progressiveLoadTest(RepetitionInfo info) {
    int rep = info.getCurrentRepetition();
    
    // Increase load with each repetition
    int transactions = rep * 10;  // 10, 20, 30, 40, 50
    
    for (int i = 0; i < transactions; i++) {
        service.transfer("A", "B", 100.0);
    }
    
    System.out.println("Processed " + transactions + " transactions");
}
```

---

## 8.7 Custom Display Names

### Name Patterns

```java
@RepeatedTest(value = 5, name = "{displayName} - Run {currentRepetition}/{totalRepetitions}")
@DisplayName("UPI Transfer Test")
void customNamedTest() {
    // Output: "UPI Transfer Test - Run 1/5", "UPI Transfer Test - Run 2/5", etc.
}
```

### Available Placeholders

| Placeholder | Description |
|-------------|-------------|
| `{displayName}` | @DisplayName value |
| `{currentRepetition}` | Current run (1, 2, 3...) |
| `{totalRepetitions}` | Total runs |

### Predefined Patterns

```java
// Long format: "repetition 1 of 5"
@RepeatedTest(value = 5, name = RepeatedTest.LONG_DISPLAY_NAME)

// Short format: "1/5"
@RepeatedTest(value = 5, name = RepeatedTest.SHORT_DISPLAY_NAME)

// Custom
@RepeatedTest(value = 5, name = "Iteration #{currentRepetition}")
```

---

## 8.8 Combining Timeouts and Repetitions

### Repeated Test with Timeout

```java
@RepeatedTest(10)
@Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
void shouldMeetSLAConsistently() {
    // Each repetition must complete within 500ms
    service.transfer("A", "B", 1000.0);
}
```

### Timeout Inside Repeated Test

```java
@RepeatedTest(5)
void repeatedWithInternalTimeout(RepetitionInfo info) {
    // Different timeout per repetition
    int timeoutMs = 100 + (info.getCurrentRepetition() * 100);
    
    assertTimeout(
        Duration.ofMillis(timeoutMs),
        () -> service.transfer("A", "B", 1000.0),
        "Must complete within " + timeoutMs + "ms"
    );
}
```

---

## 8.9 Flaky Test Detection

### What is a Flaky Test?

```
┌─────────────────────────────────────────────────────────────────┐
│                      FLAKY TEST                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  A test that sometimes passes and sometimes fails               │
│  with the SAME code.                                            │
│                                                                 │
│  Causes:                                                        │
│  • Race conditions                                              │
│  • Time-dependent logic                                         │
│  • External dependencies                                        │
│  • Shared mutable state                                         │
│  • Non-deterministic data                                       │
│                                                                 │
│  Run 1: ✅ PASS                                                 │
│  Run 2: ✅ PASS                                                 │
│  Run 3: ❌ FAIL  ← Flaky!                                       │
│  Run 4: ✅ PASS                                                 │
│  Run 5: ❌ FAIL  ← Flaky!                                       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Detecting Flaky Tests

```java
@RepeatedTest(value = 100, name = "Flaky check {currentRepetition}/100")
void detectFlakyBehavior() {
    // If this fails even once in 100 runs, it's flaky
    Result result = service.processTransaction();
    assertTrue(result.isSuccess());
}
```

### Reporting Flaky Behavior

```java
@RepeatedTest(50)
void trackFlakyFailures(RepetitionInfo info) {
    try {
        service.riskyOperation();
    } catch (Exception e) {
        System.err.println("Failed on repetition " + info.getCurrentRepetition() 
            + ": " + e.getMessage());
        throw e;  // Re-throw to fail the test
    }
}
```

---

## 🎓 Key Takeaways

| Feature | Use Case |
|---------|----------|
| `@Timeout` | Prevent tests from hanging |
| `assertTimeout()` | Verify SLA compliance |
| `assertTimeoutPreemptively()` | Hard abort slow operations |
| `@RepeatedTest` | Detect flaky tests |
| `RepetitionInfo` | Vary test data per run |

### Best Practices

1. **Set appropriate timeouts** - Based on SLAs, not arbitrary values
2. **Use @Timeout at class level** - Default safety net
3. **Repeat critical tests** - Catch intermittent issues
4. **Don't repeat all tests** - Only where flakiness is a concern
5. **Prefer assertTimeout()** - Unless you need hard abort

---

## ⚠️ Common Pitfalls

| Pitfall | Solution |
|---------|----------|
| Timeout too short | Account for CI server variance |
| ThreadLocal with preemptive | Use regular assertTimeout() |
| Too many repetitions | Balance coverage vs. execution time |
| Ignoring flaky tests | Fix the root cause |

---

*Level 8 - Timeouts & Repeated Tests*
