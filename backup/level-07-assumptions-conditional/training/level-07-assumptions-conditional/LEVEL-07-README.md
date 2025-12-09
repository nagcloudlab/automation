# Level 7: Assumptions & Conditional Tests
## Environment-Aware Test Execution

**Objective:** Master JUnit 5 assumptions and conditional annotations for smart test execution  
**Duration:** 60-90 minutes  
**Pre-requisite:** Level 6 (Test Lifecycle & Fixtures) completed

---

## 🎯 What You Will Learn

```
┌─────────────────────────────────────────────────────────────────┐
│                    LEVEL 7 LEARNING PATH                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  7.1  Assumptions vs Assertions                                 │
│   ↓                                                             │
│  7.2  assumeTrue() and assumeFalse()                            │
│   ↓                                                             │
│  7.3  assumingThat() for Conditional Logic                      │
│   ↓                                                             │
│  7.4  @EnabledIf / @DisabledIf                                  │
│   ↓                                                             │
│  7.5  @EnabledOnOs / @DisabledOnOs                              │
│   ↓                                                             │
│  7.6  @EnabledOnJre / @DisabledOnJre                            │
│   ↓                                                             │
│  7.7  @EnabledIfEnvironmentVariable                             │
│   ↓                                                             │
│  7.8  @EnabledIfSystemProperty                                  │
│   ↓                                                             │
│  7.9  Custom Conditions                                         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 7.1 Assumptions vs Assertions

### Key Difference

| Aspect | Assertion | Assumption |
|--------|-----------|------------|
| **Purpose** | Verify test correctness | Check preconditions |
| **On Failure** | Test FAILS ❌ | Test SKIPPED ⚠️ |
| **Use Case** | Expected behavior | Environment requirements |
| **Example** | `assertEquals(expected, actual)` | `assumeTrue(dbConnected)` |

### Visual Comparison

```
┌─────────────────────────────────────────────────────────────────┐
│                    ASSERTION vs ASSUMPTION                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ASSERTION (assertEquals):                                      │
│  ┌─────────────┐                                                │
│  │   Passes    │ → Test PASSES ✅                               │
│  └─────────────┘                                                │
│  ┌─────────────┐                                                │
│  │   Fails     │ → Test FAILS ❌ (Bug found!)                   │
│  └─────────────┘                                                │
│                                                                 │
│  ASSUMPTION (assumeTrue):                                       │
│  ┌─────────────┐                                                │
│  │   Passes    │ → Test CONTINUES → May pass or fail            │
│  └─────────────┘                                                │
│  ┌─────────────┐                                                │
│  │   Fails     │ → Test SKIPPED ⚠️ (Not applicable)             │
│  └─────────────┘                                                │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 7.2 assumeTrue() and assumeFalse()

### Basic Usage

```java
import static org.junit.jupiter.api.Assumptions.*;

@Test
void testOnlyInProduction() {
    // Skip if not in production environment
    assumeTrue("PROD".equals(System.getenv("ENV")),
        "Test only runs in production environment");
    
    // This code only runs if assumption passes
    performProductionTest();
}

@Test
void testNotOnCI() {
    // Skip if running on CI server
    assumeFalse(System.getenv("CI") != null,
        "Skipping on CI - requires manual verification");
    
    performManualTest();
}
```

### Banking Context Example

```java
@Test
void shouldConnectToNPCISwitch() {
    // Assumption: NPCI switch is available
    assumeTrue(isNpciSwitchAvailable(),
        "NPCI switch not available - skipping integration test");
    
    // Only runs if switch is reachable
    Response response = npciClient.healthCheck();
    assertEquals(200, response.getStatusCode());
}

@Test  
void shouldProcessRealTimePayment() {
    // Skip during maintenance window
    assumeFalse(isMaintenanceWindow(),
        "Skipping during scheduled maintenance");
    
    transferService.processRealTimePayment(...);
}
```

---

## 7.3 assumingThat() for Conditional Logic

### Partial Test Execution

```java
@Test
void testWithConditionalAssertion() {
    Account account = new Account("ACC001", "Test", 10000.0);
    
    // Always runs
    assertNotNull(account);
    assertEquals("ACC001", account.getAccountId());
    
    // Only runs if in development environment
    assumingThat(
        "DEV".equals(System.getenv("ENV")),
        () -> {
            // Additional dev-only assertions
            System.out.println("Running dev-specific checks");
            assertTrue(account.getBalance() > 0);
        }
    );
    
    // Always runs (after assumingThat block)
    assertEquals(10000.0, account.getBalance());
}
```

### Multiple Conditions

```java
@Test
void shouldTestWithMultipleConditions() {
    TransferResult result = service.transfer("A", "B", 1000.0);
    
    // Basic assertions always run
    assertNotNull(result);
    
    // Debug logging only in dev
    assumingThat(isDevelopment(), () -> {
        System.out.println("Transfer details: " + result);
    });
    
    // Performance check only in prod
    assumingThat(isProduction(), () -> {
        assertTrue(result.getProcessingTimeMs() < 100,
            "Production transfers must complete in < 100ms");
    });
    
    // Always verify the transfer worked
    assertEquals(1000.0, result.getAmount());
}
```

---

## 7.4 @EnabledIf / @DisabledIf

### Method-Based Conditions

```java
@Test
@EnabledIf("isWeekday")
void testOnlyOnWeekdays() {
    // Only runs Monday through Friday
    processBusinessDayTransactions();
}

boolean isWeekday() {
    DayOfWeek day = LocalDate.now().getDayOfWeek();
    return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
}

@Test
@DisabledIf("isMaintenanceWindow")
void testNotDuringMaintenance() {
    performCriticalOperation();
}

boolean isMaintenanceWindow() {
    LocalTime now = LocalTime.now();
    return now.isAfter(LocalTime.of(2, 0)) 
        && now.isBefore(LocalTime.of(4, 0));
}
```

### Static Method Conditions

```java
@Test
@EnabledIf("com.example.TestConditions#isProductionReady")
void testInProduction() {
    // Uses static method from another class
}
```

---

## 7.5 @EnabledOnOs / @DisabledOnOs

### Operating System Conditions

```java
@Test
@EnabledOnOs(OS.LINUX)
void testOnLinuxOnly() {
    // NPCI servers typically run on Linux
    verifyLinuxSpecificConfiguration();
}

@Test
@EnabledOnOs({OS.WINDOWS, OS.MAC})
void testOnDesktopOS() {
    // GUI-related tests
    launchDesktopApplication();
}

@Test
@DisabledOnOs(OS.WINDOWS)
void testNotOnWindows() {
    // Unix-specific file permissions test
    verifyUnixFilePermissions();
}
```

### Architecture Conditions

```java
@Test
@EnabledOnOs(architectures = "aarch64")
void testOnARM() {
    // ARM-specific optimizations
}

@Test
@EnabledOnOs(value = OS.LINUX, architectures = "amd64")
void testOnLinuxAMD64() {
    // Specific to Linux on x86-64
}
```

---

## 7.6 @EnabledOnJre / @DisabledOnJre

### Java Version Conditions

```java
@Test
@EnabledOnJre(JRE.JAVA_17)
void testOnJava17Only() {
    // Uses Java 17 specific features
    useRecordPatterns();
}

@Test
@EnabledOnJre({JRE.JAVA_11, JRE.JAVA_17, JRE.JAVA_21})
void testOnLTSVersions() {
    // Runs on LTS Java versions only
}

@Test
@DisabledOnJre(JRE.JAVA_8)
void testNotOnJava8() {
    // Uses features not available in Java 8
    useModuleSystem();
}

@Test
@EnabledForJreRange(min = JRE.JAVA_11, max = JRE.JAVA_17)
void testOnJava11Through17() {
    // Runs on Java 11, 12, 13, 14, 15, 16, 17
}

@Test
@EnabledForJreRange(min = JRE.JAVA_17)
void testOnJava17AndAbove() {
    // Runs on Java 17+
}
```

---

## 7.7 @EnabledIfEnvironmentVariable

### Environment Variable Conditions

```java
@Test
@EnabledIfEnvironmentVariable(named = "ENV", matches = "PROD")
void testInProductionOnly() {
    verifyProductionConfiguration();
}

@Test
@EnabledIfEnvironmentVariable(named = "CI", matches = "true")
void testOnCIOnly() {
    // Runs only on CI servers
    runExtendedTestSuite();
}

@Test
@DisabledIfEnvironmentVariable(named = "SKIP_SLOW_TESTS", matches = "true")
void slowTest() {
    // Skipped if SKIP_SLOW_TESTS=true
    runTimeConsumingTest();
}

@Test
@EnabledIfEnvironmentVariable(named = "DB_HOST", matches = ".*\\.npci\\.org\\.in")
void testWithNPCIDatabase() {
    // Only runs when connected to NPCI database
    verifyDatabaseConnection();
}
```

### Regex Patterns

```java
@Test
@EnabledIfEnvironmentVariable(named = "USER", matches = "(?i)admin.*")
void testForAdminUsers() {
    // Runs for users starting with "admin" (case insensitive)
}
```

---

## 7.8 @EnabledIfSystemProperty

### System Property Conditions

```java
@Test
@EnabledIfSystemProperty(named = "test.environment", matches = "integration")
void integrationTest() {
    // Run with: mvn test -Dtest.environment=integration
    runIntegrationTests();
}

@Test
@EnabledIfSystemProperty(named = "java.vendor", matches = ".*Oracle.*")
void testOnOracleJDK() {
    // Oracle JDK specific tests
}

@Test
@DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
void testNot32Bit() {
    // Requires 64-bit architecture
    use64BitFeatures();
}
```

### Multiple Properties

```java
@Test
@EnabledIfSystemProperty(named = "test.slow", matches = "true")
@EnabledIfSystemProperty(named = "test.environment", matches = "staging")
void slowStagingTest() {
    // Runs only when both properties are set correctly
}
```

---

## 7.9 Custom Conditions

### Creating Custom Conditions

```java
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class BusinessHoursCondition implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(
            ExtensionContext context) {
        
        LocalTime now = LocalTime.now();
        boolean isBusinessHours = now.isAfter(LocalTime.of(9, 0)) 
            && now.isBefore(LocalTime.of(18, 0));
        
        if (isBusinessHours) {
            return ConditionEvaluationResult.enabled("Within business hours");
        } else {
            return ConditionEvaluationResult.disabled(
                "Outside business hours (9 AM - 6 PM)");
        }
    }
}
```

### Using Custom Conditions

```java
@Test
@ExtendWith(BusinessHoursCondition.class)
void testDuringBusinessHours() {
    // Only runs between 9 AM and 6 PM
    processBusinessTransactions();
}
```

### Custom Annotation

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(BusinessHoursCondition.class)
public @interface EnabledDuringBusinessHours {
}

// Usage
@Test
@EnabledDuringBusinessHours
void businessHoursTest() {
    // Clean annotation usage
}
```

---

## 🎓 Key Takeaways

| Feature | Purpose | Example |
|---------|---------|---------|
| `assumeTrue()` | Skip if condition false | `assumeTrue(isDbConnected())` |
| `assumeFalse()` | Skip if condition true | `assumeFalse(isMaintenanceWindow())` |
| `assumingThat()` | Conditional block execution | Partial test execution |
| `@EnabledOnOs` | OS-specific tests | `@EnabledOnOs(OS.LINUX)` |
| `@EnabledOnJre` | Java version specific | `@EnabledOnJre(JRE.JAVA_17)` |
| `@EnabledIfEnvVar` | Environment-based | `@EnabledIfEnvironmentVariable(...)` |
| `@EnabledIf` | Custom method condition | `@EnabledIf("isWeekday")` |

### When to Use

| Scenario | Approach |
|----------|----------|
| Runtime environment check | `assumeTrue()`/`assumeFalse()` |
| OS-specific behavior | `@EnabledOnOs` |
| Java version features | `@EnabledOnJre` |
| CI vs local testing | `@EnabledIfEnvironmentVariable` |
| Feature flags | `@EnabledIfSystemProperty` |
| Complex business rules | Custom `ExecutionCondition` |

---

*Level 7 - Assumptions & Conditional Tests*
