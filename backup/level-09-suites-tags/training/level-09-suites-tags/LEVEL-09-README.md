# Level 9: Test Suites & Tags
## Organizing and Filtering Tests for Selective Execution

**Objective:** Master JUnit 5 tags and test suites for efficient test organization  
**Duration:** 60-90 minutes  
**Pre-requisite:** Level 8 (Timeouts & Repeated Tests) completed

---

## 🎯 What You Will Learn

```
┌─────────────────────────────────────────────────────────────────┐
│                    LEVEL 9 LEARNING PATH                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  9.1  Why Tags and Suites?                                      │
│   ↓                                                             │
│  9.2  @Tag Annotation Basics                                    │
│   ↓                                                             │
│  9.3  Multiple Tags                                             │
│   ↓                                                             │
│  9.4  Tag Expressions                                           │
│   ↓                                                             │
│  9.5  Custom Composed Annotations                               │
│   ↓                                                             │
│  9.6  Test Suites with @Suite                                   │
│   ↓                                                             │
│  9.7  Filtering by Tags in Maven                                │
│   ↓                                                             │
│  9.8  CI/CD Pipeline Integration                                │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 9.1 Why Tags and Suites?

### The Problem

```
┌─────────────────────────────────────────────────────────────────┐
│                    LARGE TEST SUITES                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Imagine 500+ tests:                                            │
│  • 200 unit tests (fast, < 1 second each)                       │
│  • 150 integration tests (medium, 2-5 seconds)                  │
│  • 100 API tests (require running server)                       │
│  • 50 performance tests (slow, 10+ seconds)                     │
│                                                                 │
│  Running ALL tests every time:                                  │
│  ❌ Slow feedback loop                                          │
│  ❌ Wastes CI resources                                         │
│  ❌ Blocks developers                                           │
│                                                                 │
│  Solution: TAGS and SELECTIVE EXECUTION                         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### The Solution

| Scenario | Run These Tags |
|----------|----------------|
| Local development | `unit`, `fast` |
| PR/Merge request | `unit`, `integration` |
| Nightly build | ALL tags |
| Pre-production | `smoke`, `critical` |
| Performance testing | `performance`, `load` |

---

## 9.2 @Tag Annotation Basics

### Simple Tag Usage

```java
@Test
@Tag("unit")
void unitTest() {
    // Fast, isolated test
}

@Test
@Tag("integration")
void integrationTest() {
    // Requires database/external service
}

@Test
@Tag("slow")
void slowTest() {
    // Takes > 5 seconds
}
```

### Class-Level Tags

```java
@Tag("unit")
@Tag("account")
class AccountTest {

    @Test
    void test1() { }  // Inherits: unit, account

    @Test
    void test2() { }  // Inherits: unit, account

    @Test
    @Tag("edge-case")
    void test3() { }  // Has: unit, account, edge-case
}
```

### Tag Naming Rules

```java
// ✅ VALID tag names
@Tag("unit")
@Tag("integration")
@Tag("slow-test")
@Tag("p2p_transfer")
@Tag("Level1")

// ❌ INVALID tag names (will cause errors)
@Tag("")           // Empty
@Tag("   ")        // Blank
@Tag("test one")   // Contains space
@Tag("test\ttab")  // Contains control character
@Tag(null)         // Null
```

---

## 9.3 Multiple Tags

### Multiple Tags on Same Element

```java
@Test
@Tag("unit")
@Tag("fast")
@Tag("account")
void multiTagTest() {
    // Has all three tags
}
```

### Tag Inheritance

```java
@Tag("banking")
class BankingTests {

    @Nested
    @Tag("transfer")
    class TransferTests {

        @Test
        @Tag("p2p")
        void p2pTransfer() {
            // Tags: banking, transfer, p2p
        }

        @Test
        @Tag("p2m")
        void p2mTransfer() {
            // Tags: banking, transfer, p2m
        }
    }

    @Nested
    @Tag("balance")
    class BalanceTests {

        @Test
        void checkBalance() {
            // Tags: banking, balance
        }
    }
}
```

---

## 9.4 Tag Expressions

### Maven Surefire Tag Expressions

```xml
<!-- Run only unit tests -->
<groups>unit</groups>

<!-- Run unit OR integration -->
<groups>unit | integration</groups>

<!-- Run unit AND fast -->
<groups>unit & fast</groups>

<!-- Run all except slow -->
<excludedGroups>slow</excludedGroups>

<!-- Complex expression -->
<groups>(unit | integration) & !slow</groups>
```

### Expression Operators

| Operator | Meaning | Example |
|----------|---------|---------|
| `!` | NOT | `!slow` |
| `&` | AND | `unit & fast` |
| `\|` | OR | `unit \| integration` |
| `()` | Grouping | `(unit \| integration) & !slow` |

### Command Line Examples

```bash
# Run only unit tests
mvn test -Dgroups=unit

# Run unit OR integration
mvn test -Dgroups="unit | integration"

# Run unit AND fast (both tags required)
mvn test -Dgroups="unit & fast"

# Exclude slow tests
mvn test -DexcludedGroups=slow

# Complex: integration tests except slow ones
mvn test -Dgroups="integration & !slow"
```

---

## 9.5 Custom Composed Annotations

### Creating Meta-Annotations

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("unit")
@Tag("fast")
public @interface UnitTest {
}

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("integration")
@Tag("database")
public @interface IntegrationTest {
}

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("slow")
@Tag("performance")
@Timeout(value = 30, unit = TimeUnit.SECONDS)
public @interface PerformanceTest {
}
```

### Using Custom Annotations

```java
class TransferServiceTest {

    @UnitTest
    void shouldValidateAmount() {
        // Tagged: unit, fast
    }

    @IntegrationTest
    void shouldPersistTransfer() {
        // Tagged: integration, database
    }

    @PerformanceTest
    void shouldMeetSLA() {
        // Tagged: slow, performance
        // Also has @Timeout(30 seconds)
    }
}
```

---

## 9.6 Test Suites with @Suite

### Basic Suite

```java
@Suite
@SelectClasses({
    AccountTest.class,
    TransferServiceTest.class,
    BalanceInquiryTest.class
})
class CoreBankingSuite {
}
```

### Suite by Package

```java
@Suite
@SelectPackages("com.example.banking")
class AllBankingTests {
}
```

### Suite by Tags

```java
@Suite
@SelectPackages("com.example")
@IncludeTags("unit")
class UnitTestSuite {
}

@Suite
@SelectPackages("com.example")
@IncludeTags({"integration", "database"})
@ExcludeTags("slow")
class FastIntegrationSuite {
}
```

### Advanced Suite Configuration

```java
@Suite
@SelectPackages("com.example")
@IncludeClassNamePatterns(".*Test")
@ExcludeClassNamePatterns(".*SlowTest")
@IncludeTags("critical")
class CriticalTestSuite {
}
```

---

## 9.7 Filtering by Tags in Maven

### pom.xml Configuration

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.2</version>
    <configuration>
        <!-- Include only these tags -->
        <groups>unit, fast</groups>
        
        <!-- Exclude these tags -->
        <excludedGroups>slow, performance</excludedGroups>
    </configuration>
</plugin>
```

### Maven Profiles for Different Environments

```xml
<profiles>
    <!-- Fast tests for local development -->
    <profile>
        <id>fast</id>
        <build>
            <plugins>
                <plugin>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <configuration>
                        <groups>unit | fast</groups>
                        <excludedGroups>slow | integration</excludedGroups>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </profile>
    
    <!-- All tests for CI -->
    <profile>
        <id>ci</id>
        <build>
            <plugins>
                <plugin>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <configuration>
                        <groups>unit | integration</groups>
                        <excludedGroups>manual</excludedGroups>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </profile>
    
    <!-- Performance tests (nightly) -->
    <profile>
        <id>performance</id>
        <build>
            <plugins>
                <plugin>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <configuration>
                        <groups>performance | load</groups>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

### Running with Profiles

```bash
# Run fast tests only
mvn test -Pfast

# Run CI tests
mvn test -Pci

# Run performance tests
mvn test -Pperformance

# Override at command line
mvn test -Dgroups=smoke
```

---

## 9.8 CI/CD Pipeline Integration

### GitHub Actions Example

```yaml
jobs:
  unit-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Run Unit Tests
        run: mvn test -Dgroups=unit

  integration-tests:
    runs-on: ubuntu-latest
    needs: unit-tests
    steps:
      - uses: actions/checkout@v3
      - name: Run Integration Tests
        run: mvn test -Dgroups=integration

  nightly-performance:
    runs-on: ubuntu-latest
    if: github.event.schedule
    steps:
      - uses: actions/checkout@v3
      - name: Run Performance Tests
        run: mvn test -Dgroups=performance
```

### Jenkins Pipeline Example

```groovy
pipeline {
    stages {
        stage('Unit Tests') {
            steps {
                sh 'mvn test -Dgroups=unit'
            }
        }
        stage('Integration Tests') {
            when { branch 'main' }
            steps {
                sh 'mvn test -Dgroups=integration'
            }
        }
        stage('Performance Tests') {
            when { 
                triggeredBy 'TimerTrigger'  // Nightly
            }
            steps {
                sh 'mvn test -Dgroups=performance'
            }
        }
    }
}
```

---

## 🎓 Recommended Tag Strategy

### Standard Tags for Banking Projects

| Tag | Description | When to Run |
|-----|-------------|-------------|
| `unit` | Isolated, fast tests | Every commit |
| `integration` | Database/service tests | PR merge |
| `api` | REST API tests | PR merge |
| `smoke` | Critical path tests | Pre-deployment |
| `performance` | SLA/load tests | Nightly |
| `security` | Security validation | Weekly |
| `manual` | Requires human intervention | Never automated |

### Example Tag Hierarchy

```
banking
├── transfer
│   ├── p2p
│   ├── p2m
│   └── rtgs
├── balance
├── account
│   ├── creation
│   └── validation
└── upi
    ├── collect
    └── pay
```

---

## ⚠️ Best Practices

1. **Keep tags lowercase** - Consistency across team
2. **Use descriptive names** - `integration` not `int`
3. **Document tag meanings** - In project README
4. **Don't over-tag** - 2-4 tags per test is enough
5. **Use composed annotations** - Reduce boilerplate
6. **Tag at class level** - When all tests share category

---

*Level 9 - Test Suites & Tags*
