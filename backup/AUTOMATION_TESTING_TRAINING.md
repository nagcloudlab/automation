# 🎯 Automation Testing Training Program
## UPI Transfer Service - Incremental Learning Path (25 Levels)

**Target Audience:** NPCI Fresh Employees  
**Tech Stack:** Java 17, Spring Boot, JUnit 5, Mockito, RestAssured, Selenium, JMeter

---

## 📊 Training Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    AUTOMATION TESTING PYRAMID                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│                          ┌───────────┐                                      │
│                          │  Web UI   │  ← Levels 22-25                      │
│                          │  Testing  │    Selenium WebDriver                │
│                        ┌─┴───────────┴─┐                                    │
│                        │ Load Testing  │  ← Levels 19-21                    │
│                        │    JMeter     │    Performance & Stress            │
│                      ┌─┴───────────────┴─┐                                  │
│                      │   Smoke Testing   │  ← Levels 16-18                  │
│                      │  Critical Paths   │    Health Checks                 │
│                    ┌─┴───────────────────┴─┐                                │
│                    │     API Testing       │  ← Levels 11-15                │
│                    │     RestAssured       │    Contract Testing            │
│                  ┌─┴───────────────────────┴─┐                              │
│                  │   Integration Testing     │  ← Levels 6-10               │
│                  │   @SpringBootTest, DB     │    Testcontainers            │
│                ┌─┴───────────────────────────┴─┐                            │
│                │        Unit Testing           │  ← Levels 1-5              │
│                │   JUnit 5, Mockito, TDD       │    Foundation              │
│                └───────────────────────────────┘                            │
│                                                                             │
│                ┌───────────────────────────────┐                            │
│                │      Level 0: Refactoring     │  ← Pre-requisite           │
│                │   Making Code Testable (DI)   │    Design Fix              │
│                └───────────────────────────────┘                            │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📦 Complete Maven Configuration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>

    <groupId>com.npci</groupId>
    <artifactId>upi-transfer-service</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <java.version>17</java.version>
        <testcontainers.version>1.19.3</testcontainers.version>
        <rest-assured.version>5.4.0</rest-assured.version>
        <selenium.version>4.16.1</selenium.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Core -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- Database -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Testing - Core -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Testing - Testcontainers -->
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <version>${testcontainers.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${testcontainers.version}</version>
            <scope>test</scope>
        </dependency>

        <!-- Testing - API -->
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <version>${rest-assured.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>json-schema-validator</artifactId>
            <version>${rest-assured.version}</version>
            <scope>test</scope>
        </dependency>

        <!-- Testing - WireMock -->
        <dependency>
            <groupId>org.wiremock</groupId>
            <artifactId>wiremock-standalone</artifactId>
            <version>3.3.1</version>
            <scope>test</scope>
        </dependency>

        <!-- Testing - Selenium -->
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>${selenium.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
            <version>5.6.2</version>
            <scope>test</scope>
        </dependency>

        <!-- Testing - Visual Regression -->
        <dependency>
            <groupId>ru.yandex.qatools.ashot</groupId>
            <artifactId>ashot</artifactId>
            <version>1.5.4</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <profiles>
        <!-- Smoke Tests Profile -->
        <profile>
            <id>smoke</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-surefire-plugin</artifactId>
                        <configuration>
                            <groups>smoke</groups>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>

        <!-- Integration Tests Profile -->
        <profile>
            <id>integration</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-surefire-plugin</artifactId>
                        <configuration>
                            <groups>integration</groups>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>

        <!-- UI Tests Profile -->
        <profile>
            <id>ui</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-surefire-plugin</artifactId>
                        <configuration>
                            <groups>ui</groups>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
</project>
```

---

# LEVEL 0: MAKING CODE TESTABLE
## Pre-requisite: Refactoring for Dependency Injection

Before writing any tests, the code must be refactored for testability.

### Problem: Tight Coupling

```java
// ❌ CURRENT CODE - Impossible to test in isolation
public class UPITransferService {
    public void transfer(...) {
        SqlAccountRepository repo = new SqlAccountRepository(); // Tight coupling!
        // ...
    }
}
```

### Solution: Interface + Dependency Injection

**Step 1: Create Interface**

```java
package com.example.repository;

import com.example.model.Account;

public interface AccountRepository {
    Account loadAccountById(String accountId);
    void saveAccount(Account account);
    boolean existsById(String accountId);
}
```

**Step 2: Implement Interface**

```java
package com.example.repository;

import com.example.model.Account;
import org.springframework.stereotype.Repository;

@Repository
public class SqlAccountRepository implements AccountRepository {

    @Override
    public Account loadAccountById(String accountId) {
        System.out.println("Loading account: " + accountId);
        // Real DB logic here
        return new Account(accountId, "Account Holder", 10000.0);
    }

    @Override
    public void saveAccount(Account account) {
        System.out.println("Saving account: " + account.getAccountId());
        // Real DB logic here
    }

    @Override
    public boolean existsById(String accountId) {
        return true; // Real DB check
    }
}
```

**Step 3: Refactor Service for DI**

```java
package com.example.service;

import com.example.model.Account;
import com.example.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class UPITransferService {

    private final AccountRepository accountRepository;

    // Constructor Injection - enables mocking!
    public UPITransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void transfer(double amount, String fromAccountId, String toAccountId) {
        Account fromAccount = accountRepository.loadAccountById(fromAccountId);
        Account toAccount = accountRepository.loadAccountById(toAccountId);

        fromAccount.debit(amount);
        toAccount.credit(amount);

        accountRepository.saveAccount(fromAccount);
        accountRepository.saveAccount(toAccount);
    }
}
```

**Why This Matters:**
- Unit tests can inject mock repositories
- Integration tests can use real database
- Follows SOLID principles (D = Dependency Inversion)

---

# LEVEL 1: JUNIT 5 BASICS
## Testing Account Model

**Objective:** Write first unit tests for `Account` class

**Concepts:**
- `@Test` annotation
- Assertions: `assertEquals`, `assertTrue`, `assertThrows`
- Test naming conventions
- AAA Pattern: Arrange-Act-Assert

**Test File:** `AccountTest.java`

```java
package com.example.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    @DisplayName("Should create account with initial balance")
    void shouldCreateAccountWithInitialBalance() {
        // Arrange & Act
        Account account = new Account("ACC001", "Rajesh Kumar", 10000.0);
        
        // Assert
        assertEquals("ACC001", account.getAccountId());
        assertEquals("Rajesh Kumar", account.getAccountHolderName());
        assertEquals(10000.0, account.getBalance());
    }

    @Test
    @DisplayName("Should debit amount from account")
    void shouldDebitAmountFromAccount() {
        // Arrange
        Account account = new Account("ACC001", "Rajesh Kumar", 10000.0);
        
        // Act
        account.debit(3000.0);
        
        // Assert
        assertEquals(7000.0, account.getBalance());
    }

    @Test
    @DisplayName("Should credit amount to account")
    void shouldCreditAmountToAccount() {
        // Arrange
        Account account = new Account("ACC001", "Rajesh Kumar", 10000.0);
        
        // Act
        account.credit(5000.0);
        
        // Assert
        assertEquals(15000.0, account.getBalance());
    }
}
```

**Banking Context:** Testing UPI account balance operations - core of payment processing.

---

# LEVEL 2: TESTING EXCEPTION SCENARIOS
## Verify Error Handling for Invalid Operations

**Concepts:**
- `assertThrows()` for exception testing
- Testing edge cases
- Negative test scenarios

**Test File:** `AccountValidationTest.java`

```java
package com.example.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class AccountValidationTest {

    @Test
    @DisplayName("Should throw exception when debiting more than balance")
    void shouldThrowExceptionForInsufficientBalance() {
        // Arrange
        Account account = new Account("ACC001", "Priya Sharma", 5000.0);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> account.debit(10000.0)
        );
        
        assertEquals("Insufficient balance", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when debiting zero amount")
    void shouldThrowExceptionForZeroDebit() {
        Account account = new Account("ACC001", "Priya Sharma", 5000.0);
        
        assertThrows(
            IllegalArgumentException.class,
            () -> account.debit(0)
        );
    }

    @Test
    @DisplayName("Should throw exception when debiting negative amount")
    void shouldThrowExceptionForNegativeDebit() {
        Account account = new Account("ACC001", "Priya Sharma", 5000.0);
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> account.debit(-500.0)
        );
        
        assertEquals("Amount to debit should be positive", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when crediting negative amount")
    void shouldThrowExceptionForNegativeCredit() {
        Account account = new Account("ACC001", "Priya Sharma", 5000.0);
        
        assertThrows(
            IllegalArgumentException.class,
            () -> account.credit(-100.0)
        );
    }

    @Test
    @DisplayName("Should allow debiting exact balance - zero balance allowed")
    void shouldAllowDebitingExactBalance() {
        Account account = new Account("ACC001", "Priya Sharma", 5000.0);
        
        assertDoesNotThrow(() -> account.debit(5000.0));
        assertEquals(0.0, account.getBalance());
    }
}
```

**Banking Context:** Payment systems MUST reject invalid transactions. These tests ensure regulatory compliance.

---

# LEVEL 3: PARAMETERIZED TESTS
## Test Multiple Scenarios with Single Test Method

**Concepts:**
- `@ParameterizedTest`
- `@ValueSource`, `@CsvSource`, `@MethodSource`
- Data-driven testing

**Test File:** `AccountParameterizedTest.java`

```java
package com.example.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

class AccountParameterizedTest {

    @ParameterizedTest
    @DisplayName("Should handle multiple debit amounts correctly")
    @CsvSource({
        "10000.0, 1000.0, 9000.0",   // Large balance, small debit
        "10000.0, 5000.0, 5000.0",   // Half balance debit
        "10000.0, 10000.0, 0.0",     // Full balance debit
        "500.0, 100.0, 400.0",       // Small balance scenario
        "100000.0, 99999.0, 1.0"     // Near-limit debit
    })
    void shouldDebitVariousAmounts(double initialBalance, double debitAmount, double expectedBalance) {
        // Arrange
        Account account = new Account("ACC001", "Test User", initialBalance);
        
        // Act
        account.debit(debitAmount);
        
        // Assert
        assertEquals(expectedBalance, account.getBalance());
    }

    @ParameterizedTest
    @DisplayName("Should reject invalid debit amounts")
    @ValueSource(doubles = {0, -1, -100, -0.01, -999999})
    void shouldRejectInvalidDebitAmounts(double invalidAmount) {
        Account account = new Account("ACC001", "Test User", 10000.0);
        
        assertThrows(IllegalArgumentException.class, () -> account.debit(invalidAmount));
    }

    @ParameterizedTest
    @DisplayName("Should process UPI transaction amounts within limits")
    @CsvSource({
        "1.0, true",         // Minimum UPI amount
        "100.0, true",       // Common small amount
        "2000.0, true",      // Typical UPI payment
        "10000.0, true",     // Medium amount
        "100000.0, true",    // Maximum UPI limit per transaction
    })
    void shouldProcessUPITransactionAmounts(double amount, boolean shouldSucceed) {
        Account account = new Account("ACC001", "Test User", 200000.0);
        
        if (shouldSucceed) {
            assertDoesNotThrow(() -> account.debit(amount));
        }
    }

    // Using MethodSource for complex test data
    @ParameterizedTest
    @DisplayName("Should handle various credit scenarios")
    @MethodSource("creditTestDataProvider")
    void shouldHandleCreditScenarios(double initial, double credit, double expected) {
        Account account = new Account("ACC001", "Test User", initial);
        account.credit(credit);
        assertEquals(expected, account.getBalance());
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> creditTestDataProvider() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of(0.0, 1000.0, 1000.0),
            org.junit.jupiter.params.provider.Arguments.of(5000.0, 5000.0, 10000.0),
            org.junit.jupiter.params.provider.Arguments.of(99999.0, 1.0, 100000.0)
        );
    }
}
```

**Banking Context:** UPI has specific amount limits (₹1 min, ₹1 lakh max per transaction). Parameterized tests efficiently verify all limits.

---

# LEVEL 4: MOCKITO - MOCKING DEPENDENCIES
## Test UPITransferService in Isolation

**Concepts:**
- `@Mock` and `@InjectMocks`
- `when().thenReturn()` stubbing
- `verify()` method calls
- Argument matchers

**Test File:** `UPITransferServiceTest.java`

```java
package com.example.service;

import com.example.model.Account;
import com.example.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UPITransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    private UPITransferService transferService;

    @BeforeEach
    void setUp() {
        transferService = new UPITransferService(accountRepository);
    }

    @Test
    @DisplayName("Should transfer amount between two accounts")
    void shouldTransferAmountBetweenAccounts() {
        // Arrange
        Account senderAccount = new Account("SENDER001", "Amit Patel", 10000.0);
        Account receiverAccount = new Account("RECEIVER001", "Sunita Reddy", 5000.0);

        when(accountRepository.loadAccountById("SENDER001")).thenReturn(senderAccount);
        when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiverAccount);

        // Act
        transferService.transfer(3000.0, "SENDER001", "RECEIVER001");

        // Assert
        assertEquals(7000.0, senderAccount.getBalance());
        assertEquals(8000.0, receiverAccount.getBalance());
        
        // Verify repository interactions
        verify(accountRepository).loadAccountById("SENDER001");
        verify(accountRepository).loadAccountById("RECEIVER001");
        verify(accountRepository).saveAccount(senderAccount);
        verify(accountRepository).saveAccount(receiverAccount);
    }

    @Test
    @DisplayName("Should fail transfer when sender has insufficient balance")
    void shouldFailTransferForInsufficientBalance() {
        // Arrange
        Account senderAccount = new Account("SENDER001", "Amit Patel", 1000.0);
        Account receiverAccount = new Account("RECEIVER001", "Sunita Reddy", 5000.0);

        when(accountRepository.loadAccountById("SENDER001")).thenReturn(senderAccount);
        when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiverAccount);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> transferService.transfer(5000.0, "SENDER001", "RECEIVER001")
        );
        
        // Verify no save was called (transaction should rollback)
        verify(accountRepository, never()).saveAccount(any());
    }

    @Test
    @DisplayName("Should call repository methods in correct order")
    void shouldCallRepositoryMethodsInOrder() {
        // Arrange
        Account sender = new Account("S1", "Sender", 10000.0);
        Account receiver = new Account("R1", "Receiver", 5000.0);

        when(accountRepository.loadAccountById("S1")).thenReturn(sender);
        when(accountRepository.loadAccountById("R1")).thenReturn(receiver);

        // Act
        transferService.transfer(1000.0, "S1", "R1");

        // Verify order using InOrder
        var inOrder = inOrder(accountRepository);
        inOrder.verify(accountRepository).loadAccountById("S1");
        inOrder.verify(accountRepository).loadAccountById("R1");
        inOrder.verify(accountRepository).saveAccount(sender);
        inOrder.verify(accountRepository).saveAccount(receiver);
    }

    @Test
    @DisplayName("Should handle multiple transfers correctly")
    void shouldHandleMultipleTransfers() {
        Account sender = new Account("S1", "Sender", 10000.0);
        Account receiver = new Account("R1", "Receiver", 5000.0);

        when(accountRepository.loadAccountById("S1")).thenReturn(sender);
        when(accountRepository.loadAccountById("R1")).thenReturn(receiver);

        // Multiple transfers
        transferService.transfer(1000.0, "S1", "R1");
        transferService.transfer(2000.0, "S1", "R1");

        assertEquals(7000.0, sender.getBalance());
        assertEquals(8000.0, receiver.getBalance());
        
        verify(accountRepository, times(4)).loadAccountById(anyString());
        verify(accountRepository, times(4)).saveAccount(any());
    }
}
```

**Banking Context:** UPI transfers involve multiple systems. Mocking isolates tests from external dependencies like CBS (Core Banking System).

---

# LEVEL 5: TEST-DRIVEN DEVELOPMENT (TDD)
## Build New Feature Using TDD Approach

**Concepts:**
- Red-Green-Refactor cycle
- Writing tests before code
- Incremental feature development

**Scenario:** Add UPI transaction limit validation (₹1 lakh per transaction)

### Step 1: Write Failing Test (RED)

```java
@Test
@DisplayName("Should reject transfer exceeding UPI limit of 1 lakh")
void shouldRejectTransferExceedingUPILimit() {
    Account sender = new Account("SENDER001", "Amit Patel", 500000.0);
    Account receiver = new Account("RECEIVER001", "Sunita Reddy", 5000.0);

    when(accountRepository.loadAccountById("SENDER001")).thenReturn(sender);
    when(accountRepository.loadAccountById("RECEIVER001")).thenReturn(receiver);

    // Act & Assert - This test will FAIL initially
    assertThrows(UPILimitExceededException.class, 
        () -> transferService.transfer(150000.0, "SENDER001", "RECEIVER001")
    );
}
```

### Step 2: Write Minimum Code to Pass (GREEN)

```java
// New Exception Class
package com.example.exception;

public class UPILimitExceededException extends RuntimeException {
    public UPILimitExceededException(String message) {
        super(message);
    }
}

// Updated Service
public void transfer(double amount, String fromAccountId, String toAccountId) {
    if (amount > 100000.0) {
        throw new UPILimitExceededException("Transaction exceeds UPI limit");
    }
    // ... existing code
}
```

### Step 3: Refactor (REFACTOR)

```java
public class UPITransferService {

    public static final double UPI_TRANSACTION_LIMIT = 100000.0;
    public static final double UPI_MINIMUM_AMOUNT = 1.0;

    private final AccountRepository accountRepository;

    public UPITransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void transfer(double amount, String fromAccountId, String toAccountId) {
        validateTransactionAmount(amount);
        
        Account fromAccount = accountRepository.loadAccountById(fromAccountId);
        Account toAccount = accountRepository.loadAccountById(toAccountId);

        fromAccount.debit(amount);
        toAccount.credit(amount);

        accountRepository.saveAccount(fromAccount);
        accountRepository.saveAccount(toAccount);
    }

    private void validateTransactionAmount(double amount) {
        if (amount < UPI_MINIMUM_AMOUNT) {
            throw new IllegalArgumentException(
                String.format("Amount ₹%.2f is below minimum UPI amount of ₹%.2f", 
                    amount, UPI_MINIMUM_AMOUNT)
            );
        }
        if (amount > UPI_TRANSACTION_LIMIT) {
            throw new UPILimitExceededException(
                String.format("Amount ₹%.2f exceeds UPI limit of ₹%.2f", 
                    amount, UPI_TRANSACTION_LIMIT)
            );
        }
    }
}
```

### Complete TDD Test Suite

```java
@Test
@DisplayName("Should reject transfer below minimum amount")
void shouldRejectTransferBelowMinimum() {
    assertThrows(IllegalArgumentException.class, 
        () -> transferService.transfer(0.5, "S1", "R1")
    );
}

@Test
@DisplayName("Should accept transfer at exact UPI limit")
void shouldAcceptTransferAtExactLimit() {
    Account sender = new Account("S1", "Sender", 200000.0);
    Account receiver = new Account("R1", "Receiver", 5000.0);

    when(accountRepository.loadAccountById("S1")).thenReturn(sender);
    when(accountRepository.loadAccountById("R1")).thenReturn(receiver);

    assertDoesNotThrow(() -> transferService.transfer(100000.0, "S1", "R1"));
}
```

---

# LEVEL 6: SPRING BOOT TEST SETUP
## Integration Testing Infrastructure

**Concepts:**
- `@SpringBootTest` annotation
- Application context loading
- Test configuration
- Profile-based configuration

**Test File:** `ApplicationIntegrationTest.java`

```java
package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("integration")
class ApplicationIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void transferServiceBeanExists() {
        assertTrue(applicationContext.containsBean("UPITransferService") ||
                   applicationContext.containsBean("uPITransferService"));
    }

    @Test
    void repositoryBeanExists() {
        assertNotNull(applicationContext.getBean("sqlAccountRepository"));
    }
}
```

**Test Configuration:** `application-test.yml`

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  h2:
    console:
      enabled: true

# UPI Configuration for tests
upi:
  transaction:
    limit: 100000
    minimum: 1
```

---

# LEVEL 7: REPOSITORY INTEGRATION TESTS
## Testing with H2 In-Memory Database

**Concepts:**
- `@DataJpaTest` for repository testing
- In-memory H2 database
- Test data setup with `@BeforeEach`
- Transaction rollback in tests

**JPA Entity:** `AccountEntity.java`

```java
package com.example.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @Column(name = "account_id")
    private String accountId;

    @Column(name = "account_holder_name", nullable = false)
    private String accountHolderName;

    @Column(name = "balance", precision = 15, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "upi_id", unique = true)
    private String upiId;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "ifsc_code")
    private String ifscCode;

    // Constructors
    public AccountEntity() {}

    public AccountEntity(String accountId, String accountHolderName, 
                         BigDecimal balance, String upiId) {
        this.accountId = accountId;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.upiId = upiId;
    }

    // Getters and Setters
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getAccountHolderName() { return accountHolderName; }
    public void setAccountHolderName(String name) { this.accountHolderName = name; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
}
```

**JPA Repository:** `JpaAccountRepository.java`

```java
package com.example.repository;

import com.example.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface JpaAccountRepository extends JpaRepository<AccountEntity, String> {
    
    Optional<AccountEntity> findByUpiId(String upiId);
    
    @Query("SELECT a FROM AccountEntity a WHERE a.isActive = true AND a.accountId = :accountId")
    Optional<AccountEntity> findActiveAccountById(String accountId);

    List<AccountEntity> findByIfscCode(String ifscCode);

    @Query("SELECT a FROM AccountEntity a WHERE a.balance >= :minBalance")
    List<AccountEntity> findAccountsWithMinimumBalance(java.math.BigDecimal minBalance);
}
```

**Test File:** `JpaAccountRepositoryTest.java`

```java
package com.example.repository;

import com.example.entity.AccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Tag("integration")
class JpaAccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaAccountRepository accountRepository;

    private AccountEntity senderAccount;
    private AccountEntity receiverAccount;

    @BeforeEach
    void setUp() {
        senderAccount = new AccountEntity(
            "ACC001", 
            "Rajesh Kumar", 
            new BigDecimal("50000.00"),
            "rajesh@upi"
        );
        senderAccount.setIfscCode("SBIN0001234");
        
        receiverAccount = new AccountEntity(
            "ACC002", 
            "Priya Sharma", 
            new BigDecimal("25000.00"),
            "priya@upi"
        );
        receiverAccount.setIfscCode("HDFC0005678");

        entityManager.persist(senderAccount);
        entityManager.persist(receiverAccount);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should find account by ID")
    void shouldFindAccountById() {
        Optional<AccountEntity> found = accountRepository.findById("ACC001");

        assertTrue(found.isPresent());
        assertEquals("Rajesh Kumar", found.get().getAccountHolderName());
        assertEquals(new BigDecimal("50000.00"), found.get().getBalance());
    }

    @Test
    @DisplayName("Should find account by UPI ID")
    void shouldFindAccountByUpiId() {
        Optional<AccountEntity> found = accountRepository.findByUpiId("priya@upi");

        assertTrue(found.isPresent());
        assertEquals("ACC002", found.get().getAccountId());
        assertEquals("Priya Sharma", found.get().getAccountHolderName());
    }

    @Test
    @DisplayName("Should update account balance after transfer")
    void shouldUpdateAccountBalance() {
        senderAccount.setBalance(new BigDecimal("47000.00"));
        accountRepository.save(senderAccount);

        AccountEntity updated = accountRepository.findById("ACC001").orElseThrow();
        assertEquals(new BigDecimal("47000.00"), updated.getBalance());
    }

    @Test
    @DisplayName("Should return empty for non-existent account")
    void shouldReturnEmptyForNonExistentAccount() {
        Optional<AccountEntity> found = accountRepository.findById("INVALID_ID");
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Should find accounts by IFSC code")
    void shouldFindAccountsByIfscCode() {
        List<AccountEntity> accounts = accountRepository.findByIfscCode("SBIN0001234");
        
        assertEquals(1, accounts.size());
        assertEquals("ACC001", accounts.get(0).getAccountId());
    }

    @Test
    @DisplayName("Should find accounts with minimum balance")
    void shouldFindAccountsWithMinimumBalance() {
        List<AccountEntity> accounts = accountRepository
            .findAccountsWithMinimumBalance(new BigDecimal("30000.00"));
        
        assertEquals(1, accounts.size());
        assertEquals("ACC001", accounts.get(0).getAccountId());
    }
}
```

---

# LEVEL 8: TESTCONTAINERS
## Testing with Real PostgreSQL Database

**Concepts:**
- Testcontainers library
- `@Container` annotation
- Dynamic port binding
- Production-like testing

**Test File:** `PostgresIntegrationTest.java`

```java
package com.example.repository;

import com.example.entity.AccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Tag("integration")
class PostgresIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("upi_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private JpaAccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        
        AccountEntity account = new AccountEntity(
            "ACC001",
            "Test User",
            new BigDecimal("10000.00"),
            "test@upi"
        );
        accountRepository.save(account);
    }

    @Test
    @DisplayName("Should persist and retrieve account from PostgreSQL")
    void shouldPersistAndRetrieveAccount() {
        AccountEntity found = accountRepository.findById("ACC001").orElseThrow();

        assertEquals("Test User", found.getAccountHolderName());
        assertEquals(new BigDecimal("10000.00"), found.getBalance());
    }

    @Test
    @DisplayName("Should handle balance updates atomically")
    void shouldHandleBalanceUpdates() {
        AccountEntity account = accountRepository.findById("ACC001").orElseThrow();
        account.setBalance(account.getBalance().subtract(new BigDecimal("1000.00")));
        accountRepository.save(account);

        AccountEntity updated = accountRepository.findById("ACC001").orElseThrow();
        assertEquals(new BigDecimal("9000.00"), updated.getBalance());
    }

    @Test
    @DisplayName("Should enforce unique UPI ID constraint")
    void shouldEnforceUniqueUpiIdConstraint() {
        AccountEntity duplicate = new AccountEntity(
            "ACC002",
            "Another User",
            new BigDecimal("5000.00"),
            "test@upi"  // Same UPI ID - should fail
        );

        assertThrows(Exception.class, () -> {
            accountRepository.save(duplicate);
            accountRepository.flush();
        });
    }
}
```

**Banking Context:** NPCI systems use production PostgreSQL. Testcontainers validates real database behavior including constraints and transactions.

---

# LEVEL 9: SERVICE LAYER INTEGRATION TESTS
## Testing Service with All Dependencies Wired

**Concepts:**
- Full application context testing
- `@Autowired` in tests
- Transaction management
- Multiple component interaction

**Test File:** `TransferServiceIntegrationTest.java`

```java
package com.example.service;

import com.example.entity.AccountEntity;
import com.example.repository.JpaAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Tag("integration")
class TransferServiceIntegrationTest {

    @Autowired
    private UPITransferService transferService;

    @Autowired
    private JpaAccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();

        AccountEntity sender = new AccountEntity(
            "SENDER001",
            "Vikram Mehta",
            new BigDecimal("100000.00"),
            "vikram@upi"
        );

        AccountEntity receiver = new AccountEntity(
            "RECEIVER001",
            "Anita Desai",
            new BigDecimal("50000.00"),
            "anita@upi"
        );

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }

    @Test
    @DisplayName("Should complete UPI transfer with database persistence")
    void shouldCompleteTransferWithDatabasePersistence() {
        // Act
        transferService.transfer(25000.0, "SENDER001", "RECEIVER001");

        // Assert - Verify database state
        AccountEntity sender = accountRepository.findById("SENDER001").orElseThrow();
        AccountEntity receiver = accountRepository.findById("RECEIVER001").orElseThrow();

        assertEquals(new BigDecimal("75000.00"), sender.getBalance());
        assertEquals(new BigDecimal("75000.00"), receiver.getBalance());
    }

    @Test
    @DisplayName("Should rollback transaction on failure")
    void shouldRollbackOnFailure() {
        // Get initial balances
        BigDecimal initialSenderBalance = accountRepository.findById("SENDER001")
            .orElseThrow().getBalance();
        BigDecimal initialReceiverBalance = accountRepository.findById("RECEIVER001")
            .orElseThrow().getBalance();

        // Act - Try transfer with insufficient balance
        assertThrows(RuntimeException.class, () -> 
            transferService.transfer(200000.0, "SENDER001", "RECEIVER001")
        );

        // Assert - Balances should remain unchanged
        AccountEntity sender = accountRepository.findById("SENDER001").orElseThrow();
        AccountEntity receiver = accountRepository.findById("RECEIVER001").orElseThrow();

        assertEquals(initialSenderBalance, sender.getBalance());
        assertEquals(initialReceiverBalance, receiver.getBalance());
    }

    @Test
    @DisplayName("Should handle concurrent transfers")
    void shouldHandleConcurrentTransfers() throws InterruptedException {
        // Multiple sequential transfers
        transferService.transfer(10000.0, "SENDER001", "RECEIVER001");
        transferService.transfer(15000.0, "SENDER001", "RECEIVER001");
        transferService.transfer(5000.0, "SENDER001", "RECEIVER001");

        // Verify final state
        AccountEntity sender = accountRepository.findById("SENDER001").orElseThrow();
        AccountEntity receiver = accountRepository.findById("RECEIVER001").orElseThrow();

        assertEquals(new BigDecimal("70000.00"), sender.getBalance());  // 100000 - 30000
        assertEquals(new BigDecimal("80000.00"), receiver.getBalance()); // 50000 + 30000
    }
}
```

**Banking Context:** UPI transactions MUST be atomic. Integration tests verify transaction rollback on failures.

---

# LEVEL 10: WIREMOCK FOR EXTERNAL SERVICES
## Mocking External APIs (NPCI Switch, CBS)

**Concepts:**
- WireMock server
- Stubbing HTTP responses
- Simulating network failures
- Response templating

**Test File:** `ExternalServiceIntegrationTest.java`

```java
package com.example.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@Tag("integration")
class ExternalServiceIntegrationTest {

    static WireMockServer npciSwitchMock;

    @BeforeAll
    static void setupWireMock() {
        npciSwitchMock = new WireMockServer(8089);
        npciSwitchMock.start();
        WireMock.configureFor("localhost", 8089);
    }

    @AfterAll
    static void tearDownWireMock() {
        npciSwitchMock.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("npci.switch.url", () -> "http://localhost:8089");
    }

    @BeforeEach
    void resetStubs() {
        npciSwitchMock.resetAll();
    }

    @Test
    @DisplayName("Should process transfer when NPCI switch approves")
    void shouldProcessTransferWhenApproved() {
        // Stub NPCI Switch approval response
        stubFor(post(urlEqualTo("/api/v1/transfer/validate"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "status": "APPROVED",
                        "referenceId": "TXN123456789",
                        "rrn": "123456789012",
                        "timestamp": "2024-01-15T10:30:00Z"
                    }
                    """)));

        // Verify stub was created
        verify(0, postRequestedFor(urlEqualTo("/api/v1/transfer/validate")));
    }

    @Test
    @DisplayName("Should handle NPCI switch timeout gracefully")
    void shouldHandleNPCITimeout() {
        // Stub timeout scenario
        stubFor(post(urlEqualTo("/api/v1/transfer/validate"))
            .willReturn(aResponse()
                .withStatus(200)
                .withFixedDelay(5000))); // 5 second delay

        // Test should handle timeout appropriately
    }

    @Test
    @DisplayName("Should handle NPCI switch rejection with U16 error")
    void shouldHandleNPCIRejectionU16() {
        // Stub rejection response - Risk threshold exceeded
        stubFor(post(urlEqualTo("/api/v1/transfer/validate"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "status": "REJECTED",
                        "errorCode": "U16",
                        "errorMessage": "Risk threshold exceeded",
                        "timestamp": "2024-01-15T10:30:00Z"
                    }
                    """)));
    }

    @Test
    @DisplayName("Should handle NPCI switch rejection with U30 error")
    void shouldHandleNPCIRejectionU30() {
        // Stub rejection response - Insufficient funds
        stubFor(post(urlEqualTo("/api/v1/transfer/validate"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "status": "REJECTED",
                        "errorCode": "U30",
                        "errorMessage": "Debit not permitted. Insufficient funds.",
                        "timestamp": "2024-01-15T10:30:00Z"
                    }
                    """)));
    }

    @Test
    @DisplayName("Should handle NPCI switch 500 error")
    void shouldHandleNPCIServerError() {
        stubFor(post(urlEqualTo("/api/v1/transfer/validate"))
            .willReturn(aResponse()
                .withStatus(500)
                .withBody("Internal Server Error")));

        // Test should retry or fail gracefully
    }

    @Test
    @DisplayName("Should handle network failure to NPCI")
    void shouldHandleNetworkFailure() {
        stubFor(post(urlEqualTo("/api/v1/transfer/validate"))
            .willReturn(aResponse()
                .withFault(com.github.tomakehurst.wiremock.http.Fault.CONNECTION_RESET_BY_PEER)));

        // Test should handle connection reset
    }
}
```

**Banking Context:** UPI transactions interact with multiple external systems (NPCI Switch, CBS, PSP). WireMock simulates these for reliable testing.

---

# LEVEL 11: REST CONTROLLER SETUP
## Create and Test REST API Endpoints

**Concepts:**
- `@RestController` and `@RequestMapping`
- Request/Response DTOs
- Input validation
- HTTP status codes

**DTO Classes:**

```java
// TransferRequest.java
package com.example.api.dto;

import jakarta.validation.constraints.*;

public record TransferRequest(
    @NotBlank(message = "Sender account ID is required")
    String senderAccountId,
    
    @NotBlank(message = "Receiver account ID is required")
    String receiverAccountId,
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Max(value = 100000, message = "Amount exceeds UPI limit of ₹1,00,000")
    Double amount,
    
    @Size(max = 50, message = "Remarks cannot exceed 50 characters")
    String remarks
) {}

// TransferResponse.java
package com.example.api.dto;

import java.time.LocalDateTime;

public record TransferResponse(
    String transactionId,
    String status,
    Double amount,
    String senderAccountId,
    String receiverAccountId,
    LocalDateTime timestamp,
    String message
) {}

// ErrorResponse.java
package com.example.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
    String errorCode,
    String message,
    List<String> errors,
    LocalDateTime timestamp,
    String path
) {}
```

**Controller:** `TransferController.java`

```java
package com.example.api;

import com.example.api.dto.TransferRequest;
import com.example.api.dto.TransferResponse;
import com.example.service.UPITransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

    private final UPITransferService transferService;

    public TransferController(UPITransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> initiateTransfer(
            @Valid @RequestBody TransferRequest request) {
        
        transferService.transfer(
            request.amount(),
            request.senderAccountId(),
            request.receiverAccountId()
        );

        TransferResponse response = new TransferResponse(
            UUID.randomUUID().toString(),
            "SUCCESS",
            request.amount(),
            request.senderAccountId(),
            request.receiverAccountId(),
            LocalDateTime.now(),
            "Transfer completed successfully"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransferResponse> getTransferStatus(
            @PathVariable String transactionId) {
        // Fetch and return transaction status
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Transfer Service is UP");
    }
}
```

---

# LEVEL 12: MOCKMVC CONTROLLER TESTING
## Test Controllers Without Starting Server

**Concepts:**
- `@WebMvcTest` annotation
- `MockMvc` for HTTP simulation
- JSON assertions
- Content type validation

**Test File:** `TransferControllerTest.java`

```java
package com.example.api;

import com.example.api.dto.TransferRequest;
import com.example.service.UPITransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UPITransferService transferService;

    @Test
    @DisplayName("POST /api/v1/transfers - Should return 201 for valid transfer")
    void shouldReturn201ForValidTransfer() throws Exception {
        TransferRequest request = new TransferRequest(
            "SENDER001",
            "RECEIVER001",
            5000.0,
            "Payment for groceries"
        );

        mockMvc.perform(post("/api/v1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.amount").value(5000.0))
            .andExpect(jsonPath("$.senderAccountId").value("SENDER001"))
            .andExpect(jsonPath("$.receiverAccountId").value("RECEIVER001"))
            .andExpect(jsonPath("$.transactionId").exists())
            .andExpect(jsonPath("$.timestamp").exists());

        verify(transferService).transfer(5000.0, "SENDER001", "RECEIVER001");
    }

    @Test
    @DisplayName("POST /api/v1/transfers - Should return 400 for missing sender")
    void shouldReturn400ForMissingSender() throws Exception {
        String invalidRequest = """
            {
                "receiverAccountId": "RECEIVER001",
                "amount": 5000.0
            }
            """;

        mockMvc.perform(post("/api/v1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
            .andExpect(status().isBadRequest());

        verify(transferService, never()).transfer(anyDouble(), anyString(), anyString());
    }

    @Test
    @DisplayName("POST /api/v1/transfers - Should return 400 for amount exceeding limit")
    void shouldReturn400ForAmountExceedingLimit() throws Exception {
        TransferRequest request = new TransferRequest(
            "SENDER001",
            "RECEIVER001",
            150000.0,  // Exceeds UPI limit
            "Large payment"
        );

        mockMvc.perform(post("/api/v1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/transfers - Should return 400 for negative amount")
    void shouldReturn400ForNegativeAmount() throws Exception {
        TransferRequest request = new TransferRequest(
            "SENDER001",
            "RECEIVER001",
            -100.0,
            "Invalid amount"
        );

        mockMvc.perform(post("/api/v1/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/transfers/health - Should return 200")
    void shouldReturnHealthStatus() throws Exception {
        mockMvc.perform(get("/api/v1/transfers/health"))
            .andExpect(status().isOk())
            .andExpect(content().string("Transfer Service is UP"));
    }
}
```

---

# LEVEL 13: RESTASSURED API TESTING
## Write Expressive API Tests

**Concepts:**
- Given-When-Then syntax
- Response extraction
- Request/Response logging
- Fluent assertions

**Test File:** `TransferApiTest.java`

```java
package com.example.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransferApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/v1";
    }

    @Test
    @DisplayName("Should successfully initiate UPI transfer")
    void shouldInitiateTransfer() {
        String requestBody = """
            {
                "senderAccountId": "SENDER001",
                "receiverAccountId": "RECEIVER001",
                "amount": 5000.0,
                "remarks": "Payment for services"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .log().all()
        .when()
            .post("/transfers")
        .then()
            .log().all()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .body("status", equalTo("SUCCESS"))
            .body("amount", equalTo(5000.0f))
            .body("transactionId", notNullValue())
            .body("timestamp", notNullValue());
    }

    @Test
    @DisplayName("Should return 400 for negative amount")
    void shouldReturn400ForNegativeAmount() {
        String requestBody = """
            {
                "senderAccountId": "SENDER001",
                "receiverAccountId": "RECEIVER001",
                "amount": -100.0,
                "remarks": "Invalid amount"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/transfers")
        .then()
            .statusCode(400);
    }

    @Test
    @DisplayName("Should validate UPI transaction limit")
    void shouldValidateUPILimit() {
        String requestBody = """
            {
                "senderAccountId": "SENDER001",
                "receiverAccountId": "RECEIVER001",
                "amount": 150000.0,
                "remarks": "Large transfer"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/transfers")
        .then()
            .statusCode(400);
    }

    @Test
    @DisplayName("Should extract transaction ID from response")
    void shouldExtractTransactionId() {
        String requestBody = """
            {
                "senderAccountId": "SENDER001",
                "receiverAccountId": "RECEIVER001",
                "amount": 1000.0,
                "remarks": "Test transfer"
            }
            """;

        String transactionId = 
            given()
                .contentType(ContentType.JSON)
                .body(requestBody)
            .when()
                .post("/transfers")
            .then()
                .statusCode(201)
            .extract()
                .path("transactionId");

        System.out.println("Transaction ID: " + transactionId);
        assertThat(transactionId, notNullValue());
    }

    @Test
    @DisplayName("Should handle missing content type")
    void shouldHandleMissingContentType() {
        given()
            .body("{}")
        .when()
            .post("/transfers")
        .then()
            .statusCode(415); // Unsupported Media Type
    }
}
```

---

# LEVEL 14: CONTRACT TESTING WITH JSON SCHEMA
## Validate API Response Contracts

**Concepts:**
- JSON Schema validation
- Contract-first development
- API versioning
- Backward compatibility testing

**Schema File:** `src/test/resources/schemas/transfer-response-schema.json`

```json
{
    "$schema": "http://json-schema.org/draft-07/schema#",
    "title": "TransferResponse",
    "type": "object",
    "required": ["transactionId", "status", "amount", "senderAccountId", "receiverAccountId", "timestamp"],
    "properties": {
        "transactionId": {
            "type": "string",
            "pattern": "^[a-f0-9\\-]{36}$",
            "description": "UUID format transaction identifier"
        },
        "status": {
            "type": "string",
            "enum": ["SUCCESS", "PENDING", "FAILED"],
            "description": "Transaction status"
        },
        "amount": {
            "type": "number",
            "minimum": 1,
            "maximum": 100000,
            "description": "Transfer amount in INR"
        },
        "senderAccountId": {
            "type": "string",
            "minLength": 1,
            "description": "Sender account identifier"
        },
        "receiverAccountId": {
            "type": "string",
            "minLength": 1,
            "description": "Receiver account identifier"
        },
        "timestamp": {
            "type": "string",
            "format": "date-time",
            "description": "ISO 8601 timestamp"
        },
        "message": {
            "type": "string",
            "description": "Optional message"
        }
    }
}
```

**Test File:** `TransferApiContractTest.java`

```java
package com.example.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransferApiContractTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Transfer response should match JSON schema contract")
    void transferResponseShouldMatchContract() {
        String requestBody = """
            {
                "senderAccountId": "SENDER001",
                "receiverAccountId": "RECEIVER001",
                "amount": 5000.0,
                "remarks": "Contract test"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/v1/transfers")
        .then()
            .statusCode(201)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(
                "schemas/transfer-response-schema.json"));
    }

    @Test
    @DisplayName("All successful transfers should conform to contract")
    void allSuccessfulTransfersShouldConformToContract() {
        // Test with minimum amount
        testTransferContract(1.0);
        
        // Test with typical amount
        testTransferContract(5000.0);
        
        // Test with maximum amount
        testTransferContract(100000.0);
    }

    private void testTransferContract(double amount) {
        String requestBody = String.format("""
            {
                "senderAccountId": "SENDER001",
                "receiverAccountId": "RECEIVER001",
                "amount": %.2f,
                "remarks": "Contract test"
            }
            """, amount);

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/v1/transfers")
        .then()
            .statusCode(201)
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(
                "schemas/transfer-response-schema.json"));
    }
}
```

**Banking Context:** UPI APIs must maintain strict contracts for interoperability between PSPs, banks, and NPCI.

---

# LEVEL 15: API ERROR HANDLING TESTS
## Verify Consistent Error Responses

**Concepts:**
- Global exception handling
- Error response format
- HTTP status code mapping
- NPCI error code standardization

**Global Exception Handler:**

```java
package com.example.api.exception;

import com.example.api.dto.ErrorResponse;
import com.example.exception.InsufficientBalanceException;
import com.example.exception.AccountNotFoundException;
import com.example.exception.UPILimitExceededException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();

        ErrorResponse response = new ErrorResponse(
            "VALIDATION_ERROR",
            "Request validation failed",
            errors,
            LocalDateTime.now(),
            request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(
            InsufficientBalanceException ex, HttpServletRequest request) {
        
        ErrorResponse response = new ErrorResponse(
            "U30",  // NPCI UPI error code
            "Debit not permitted. Insufficient funds.",
            List.of(ex.getMessage()),
            LocalDateTime.now(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFound(
            AccountNotFoundException ex, HttpServletRequest request) {
        
        ErrorResponse response = new ErrorResponse(
            "U16",  // NPCI UPI error code
            "Risk threshold exceeded",
            List.of(ex.getMessage()),
            LocalDateTime.now(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UPILimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleUPILimitExceeded(
            UPILimitExceededException ex, HttpServletRequest request) {
        
        ErrorResponse response = new ErrorResponse(
            "U09",  // NPCI UPI error code - Transaction limit exceeded
            "Transaction amount exceeds limit",
            List.of(ex.getMessage()),
            LocalDateTime.now(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        ErrorResponse response = new ErrorResponse(
            "U00",  // NPCI UPI error code - System error
            "An unexpected error occurred",
            List.of(ex.getMessage()),
            LocalDateTime.now(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
```

**Test File:** `ErrorHandlingApiTest.java`

```java
package com.example.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ErrorHandlingApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Should return consistent error format for validation errors")
    void shouldReturnConsistentErrorFormat() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/api/v1/transfers")
        .then()
            .statusCode(400)
            .body("errorCode", notNullValue())
            .body("message", notNullValue())
            .body("errors", notNullValue())
            .body("timestamp", notNullValue())
            .body("path", equalTo("/api/v1/transfers"));
    }

    @Test
    @DisplayName("Should include all validation errors in response")
    void shouldIncludeAllValidationErrors() {
        String invalidRequest = """
            {
                "amount": -100.0
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(invalidRequest)
        .when()
            .post("/api/v1/transfers")
        .then()
            .statusCode(400)
            .body("errors", hasSize(greaterThanOrEqualTo(2))); // Missing sender + receiver
    }

    @Test
    @DisplayName("Should return NPCI U09 error code for limit exceeded")
    void shouldReturnU09ForLimitExceeded() {
        String requestBody = """
            {
                "senderAccountId": "SENDER001",
                "receiverAccountId": "RECEIVER001",
                "amount": 150000.0,
                "remarks": "Large transfer"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/v1/transfers")
        .then()
            .statusCode(400);
    }
}
```

---

# LEVEL 16: SMOKE TEST SUITE DESIGN
## Critical Path Verification

**Concepts:**
- Critical path identification
- Fast execution (< 2 minutes)
- Environment-agnostic tests
- CI/CD integration

**Test File:** `SmokeTestSuite.java`

```java
package com.example.smoke;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(OrderAnnotation.class)
@Tag("smoke")
class SmokeTestSuite {

    private static String baseUrl;

    @BeforeAll
    static void setup() {
        // Read from environment or default to localhost
        baseUrl = System.getenv().getOrDefault("API_BASE_URL", "http://localhost:8080");
        RestAssured.baseURI = baseUrl;
    }

    @Test
    @Order(1)
    @DisplayName("SMOKE-001: Application health check")
    void healthCheck() {
        given()
        .when()
            .get("/actuator/health")
        .then()
            .statusCode(200)
            .body("status", equalTo("UP"));
    }

    @Test
    @Order(2)
    @DisplayName("SMOKE-002: Database connectivity")
    void databaseConnectivity() {
        given()
        .when()
            .get("/actuator/health/db")
        .then()
            .statusCode(200)
            .body("status", equalTo("UP"));
    }

    @Test
    @Order(3)
    @DisplayName("SMOKE-003: API endpoint reachable")
    void apiEndpointReachable() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .options("/api/v1/transfers")
        .then()
            .statusCode(anyOf(is(200), is(204)));
    }

    @Test
    @Order(4)
    @DisplayName("SMOKE-004: Transfer API accepts valid request format")
    void transferApiAcceptsValidRequest() {
        String request = """
            {
                "senderAccountId": "SMOKE_SENDER",
                "receiverAccountId": "SMOKE_RECEIVER",
                "amount": 1.0,
                "remarks": "Smoke test"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/v1/transfers")
        .then()
            .statusCode(anyOf(is(201), is(422)));  // Either success or business error
    }

    @Test
    @Order(5)
    @DisplayName("SMOKE-005: Error handling works correctly")
    void errorHandlingWorks() {
        given()
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/api/v1/transfers")
        .then()
            .statusCode(400)
            .body("errorCode", notNullValue());
    }

    @Test
    @Order(6)
    @DisplayName("SMOKE-006: Response time is acceptable")
    void responseTimeAcceptable() {
        given()
        .when()
            .get("/actuator/health")
        .then()
            .time(lessThan(2000L)); // Response under 2 seconds
    }
}
```

**Run Command:** `mvn test -Psmoke`

---

# LEVEL 17: HEALTH CHECK ENDPOINTS
## Configure Comprehensive Health Indicators

**Concepts:**
- Spring Boot Actuator
- Custom health indicators
- Readiness vs Liveness probes
- Kubernetes integration

**Custom Health Indicator:** `NPCISwitchHealthIndicator.java`

```java
package com.example.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("npciSwitch")
public class NPCISwitchHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate;
    private final String npciSwitchUrl;

    public NPCISwitchHealthIndicator(
            RestTemplate restTemplate, 
            @Value("${npci.switch.url:http://localhost:8089}") String npciSwitchUrl) {
        this.restTemplate = restTemplate;
        this.npciSwitchUrl = npciSwitchUrl;
    }

    @Override
    public Health health() {
        try {
            restTemplate.getForEntity(npciSwitchUrl + "/health", String.class);
            return Health.up()
                .withDetail("npciSwitch", "Connected")
                .withDetail("url", npciSwitchUrl)
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("npciSwitch", "Disconnected")
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

**Database Health Indicator:** `DatabaseHealthIndicator.java`

```java
package com.example.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component("database")
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                return Health.up()
                    .withDetail("database", "Connected")
                    .withDetail("validationQuery", "SELECT 1")
                    .build();
            }
        } catch (Exception e) {
            return Health.down()
                .withDetail("database", "Disconnected")
                .withDetail("error", e.getMessage())
                .build();
        }
        return Health.down().build();
    }
}
```

**Configuration:** `application.yml`

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health, info, metrics, prometheus
  endpoint:
    health:
      show-details: always
      probes:
        enabled: true
  health:
    livenessstate:
      enabled: true
    readinessstate:
      enabled: true
```

---

# LEVEL 18: CI/CD SMOKE TEST INTEGRATION
## Automate Smoke Tests in Deployment Pipeline

**Concepts:**
- GitHub Actions integration
- Environment-specific configuration
- Test reporting
- Failure notifications

**GitHub Actions Workflow:** `.github/workflows/smoke-tests.yml`

```yaml
name: Smoke Tests

on:
  deployment:
    types: [created]
  workflow_dispatch:
    inputs:
      environment:
        description: 'Target environment'
        required: true
        type: choice
        options:
          - dev
          - staging
          - production

env:
  DEV_URL: https://dev-upi.npci.org.in
  STAGING_URL: https://staging-upi.npci.org.in
  PROD_URL: https://upi.npci.org.in

jobs:
  smoke-test:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Set environment URL
        run: |
          if [ "${{ github.event.inputs.environment }}" == "dev" ]; then
            echo "API_BASE_URL=$DEV_URL" >> $GITHUB_ENV
          elif [ "${{ github.event.inputs.environment }}" == "staging" ]; then
            echo "API_BASE_URL=$STAGING_URL" >> $GITHUB_ENV
          else
            echo "API_BASE_URL=$PROD_URL" >> $GITHUB_ENV
          fi

      - name: Run Smoke Tests
        run: mvn test -Psmoke

      - name: Publish Test Report
        uses: dorny/test-reporter@v1
        if: always()
        with:
          name: Smoke Test Results
          path: target/surefire-reports/*.xml
          reporter: java-junit

      - name: Notify on Failure
        if: failure()
        uses: 8398a7/action-slack@v3
        with:
          status: failure
          text: '🚨 Smoke tests failed on ${{ github.event.inputs.environment }}!'
        env:
          SLACK_WEBHOOK_URL: ${{ secrets.SLACK_WEBHOOK }}
```

---

# LEVEL 19: JMETER BASICS
## Create First Load Test

**Concepts:**
- Thread Groups
- HTTP Samplers
- Listeners (View Results, Aggregate Report)
- Test plan structure

**JMeter CLI Test Script:** `run-load-test.sh`

```bash
#!/bin/bash

# JMeter Load Test Execution Script
# Usage: ./run-load-test.sh <threads> <rampup> <duration>

THREADS=${1:-50}
RAMPUP=${2:-10}
DURATION=${3:-60}

JMETER_HOME=/opt/apache-jmeter-5.6.2
TEST_PLAN=transfer-load-test.jmx
RESULTS_DIR=results/$(date +%Y%m%d_%H%M%S)

mkdir -p $RESULTS_DIR

echo "=========================================="
echo "UPI Transfer Service Load Test"
echo "=========================================="
echo "Threads: $THREADS"
echo "Ramp-up: $RAMPUP seconds"
echo "Duration: $DURATION seconds"
echo "=========================================="

$JMETER_HOME/bin/jmeter -n \
    -t $TEST_PLAN \
    -l $RESULTS_DIR/results.jtl \
    -e -o $RESULTS_DIR/report \
    -Jthreads=$THREADS \
    -Jrampup=$RAMPUP \
    -Jduration=$DURATION \
    -Jhost=localhost \
    -Jport=8080

echo "=========================================="
echo "Test Complete!"
echo "Report: $RESULTS_DIR/report/index.html"
echo "=========================================="
```

**JMeter Test Plan Structure:**

```
Transfer Load Test (Test Plan)
├── Thread Group (${threads} users, ${rampup}s ramp-up, ${duration}s duration)
│   ├── HTTP Header Manager (Content-Type: application/json)
│   ├── HTTP Request (POST /api/v1/transfers)
│   │   └── Body: {"senderAccountId":"...", "amount":...}
│   ├── Response Assertion (status = SUCCESS)
│   └── Duration Assertion (< 500ms)
├── Aggregate Report
├── View Results Tree
└── Summary Report
```

---

# LEVEL 20: PERFORMANCE METRICS & ASSERTIONS
## Define and Validate Performance SLAs

**Concepts:**
- Response time percentiles (P50, P90, P99)
- Throughput (TPS)
- Error rate
- Apdex score

**Performance SLAs for UPI Transfer Service:**

| Metric | Target | Critical | Description |
|--------|--------|----------|-------------|
| P50 Response Time | < 200ms | < 500ms | Median response time |
| P95 Response Time | < 500ms | < 1000ms | 95th percentile |
| P99 Response Time | < 1000ms | < 2000ms | 99th percentile |
| Throughput | > 1000 TPS | > 500 TPS | Transactions per second |
| Error Rate | < 0.1% | < 1% | Failed transactions |
| Availability | 99.99% | 99.9% | Uptime percentage |

**Gatling Performance Test:** `TransferSimulation.scala`

```scala
package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class TransferSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val transferRequest = http("UPI Transfer")
    .post("/api/v1/transfers")
    .body(StringBody("""
      {
        "senderAccountId": "SENDER${userId}",
        "receiverAccountId": "RECEIVER${userId}",
        "amount": ${amount},
        "remarks": "Load test transaction"
      }
    """)).asJson
    .check(status.is(201))
    .check(jsonPath("$.status").is("SUCCESS"))

  val feeder = Iterator.continually(Map(
    "userId" -> java.util.UUID.randomUUID().toString.take(8),
    "amount" -> (scala.util.Random.nextInt(10000) + 1)
  ))

  val scn = scenario("UPI Transfer Load Test")
    .feed(feeder)
    .exec(transferRequest)

  setUp(
    scn.inject(
      // Warm-up phase
      rampUsers(10).during(10.seconds),
      // Steady state
      constantUsersPerSec(50).during(60.seconds),
      // Peak load
      rampUsersPerSec(50).to(100).during(30.seconds),
      // Cool down
      rampUsersPerSec(100).to(10).during(30.seconds)
    )
  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.percentile(50).lt(200),
      global.responseTime.percentile(95).lt(500),
      global.responseTime.percentile(99).lt(1000),
      global.successfulRequests.percent.gt(99.9)
    )
}
```

---

# LEVEL 21: STRESS TESTING & BREAKPOINT ANALYSIS
## Find System Limits and Breaking Points

**Concepts:**
- Gradual load increase
- Resource monitoring (CPU, Memory, DB connections)
- Identifying bottlenecks
- Capacity planning

**Stress Test Scenarios:**

```java
package com.example.performance;

/**
 * Stress Test Configuration Guide
 * 
 * These scenarios help identify system limits and bottlenecks
 */
public class StressTestScenarios {

    /**
     * Scenario 1: Breakpoint Test
     * Purpose: Find the point where system starts failing
     * 
     * Configuration:
     * - Start: 10 concurrent users
     * - Increase: +10 users every 30 seconds
     * - Monitor: Response time, error rate, CPU, memory
     * - Stop when: Error rate > 5% OR P99 > 3 seconds
     */
    public static final String BREAKPOINT_TEST = "breakpoint";

    /**
     * Scenario 2: Spike Test
     * Purpose: Verify system handles sudden traffic spikes
     * 
     * Configuration:
     * - Baseline: 50 users for 2 minutes
     * - Spike: Jump to 500 users instantly
     * - Sustain: 500 users for 1 minute
     * - Recovery: Back to 50 users
     * - Monitor: Recovery time, data integrity
     */
    public static final String SPIKE_TEST = "spike";

    /**
     * Scenario 3: Soak Test (Endurance)
     * Purpose: Find memory leaks and resource exhaustion
     * 
     * Configuration:
     * - Load: 70% of breakpoint capacity
     * - Duration: 4-8 hours
     * - Monitor: Memory trends, connection pool, GC activity
     */
    public static final String SOAK_TEST = "soak";

    /**
     * Scenario 4: Capacity Test
     * Purpose: Validate system meets SLA under expected peak load
     * 
     * Configuration:
     * - Load: Expected peak users (e.g., 1000 concurrent)
     * - Duration: 30 minutes
     * - Assert: All SLAs met
     */
    public static final String CAPACITY_TEST = "capacity";
}
```

**Monitoring Metrics (Prometheus/Grafana):**

```yaml
# Key metrics to monitor during load tests
metrics:
  - name: http_server_requests_seconds
    description: HTTP request duration
    labels: [method, uri, status]
    alert_threshold: P99 > 1s
  
  - name: jvm_memory_used_bytes
    description: JVM heap memory usage
    labels: [area]
    alert_threshold: > 80% of max
  
  - name: hikaricp_connections_active
    description: Active database connections
    alert_threshold: > 90% of pool size
    
  - name: process_cpu_usage
    description: CPU usage percentage
    alert_threshold: > 80%

  - name: jvm_gc_pause_seconds
    description: GC pause duration
    alert_threshold: > 500ms
```

---

# LEVEL 22: SELENIUM WEBDRIVER SETUP
## Configure Selenium for Browser Automation

**Concepts:**
- WebDriver architecture
- Browser driver management
- Page load strategies
- Wait mechanisms

**Base Test Class:** `BaseUITest.java`

```java
package com.example.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Tag("ui")
public abstract class BaseUITest {

    protected static WebDriver driver;
    protected static WebDriverWait wait;
    protected static String baseUrl;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
        baseUrl = System.getenv().getOrDefault("UI_BASE_URL", "http://localhost:3000");
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        
        // Headless mode for CI/CD
        if (System.getenv("CI") != null) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void navigateTo(String path) {
        driver.get(baseUrl + path);
    }
}
```

---

# LEVEL 23: PAGE OBJECT MODEL
## Implement Maintainable UI Tests

**Concepts:**
- Page Object pattern
- Element locators
- Page factory
- Reusable components

**Login Page Object:** `LoginPage.java`

```java
package com.example.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "mobile-number")
    private WebElement mobileNumberInput;

    @FindBy(id = "upi-pin")
    private WebElement upiPinInput;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = ".error-message")
    private WebElement errorMessage;

    @FindBy(css = ".loading-spinner")
    private WebElement loadingSpinner;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public LoginPage enterMobileNumber(String mobileNumber) {
        wait.until(ExpectedConditions.visibilityOf(mobileNumberInput));
        mobileNumberInput.clear();
        mobileNumberInput.sendKeys(mobileNumber);
        return this;
    }

    public LoginPage enterUPIPin(String pin) {
        upiPinInput.clear();
        upiPinInput.sendKeys(pin);
        return this;
    }

    public DashboardPage clickLogin() {
        loginButton.click();
        wait.until(ExpectedConditions.invisibilityOf(loadingSpinner));
        return new DashboardPage(driver);
    }

    public String getErrorMessage() {
        wait.until(ExpectedConditions.visibilityOf(errorMessage));
        return errorMessage.getText();
    }

    public boolean isLoginButtonEnabled() {
        return loginButton.isEnabled();
    }

    // Fluent method for complete login
    public DashboardPage loginAs(String mobileNumber, String pin) {
        return enterMobileNumber(mobileNumber)
            .enterUPIPin(pin)
            .clickLogin();
    }
}
```

**Transfer Page Object:** `TransferPage.java`

```java
package com.example.ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TransferPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "receiver-upi-id")
    private WebElement receiverUpiIdInput;

    @FindBy(id = "amount")
    private WebElement amountInput;

    @FindBy(id = "remarks")
    private WebElement remarksInput;

    @FindBy(id = "proceed-button")
    private WebElement proceedButton;

    @FindBy(id = "confirm-pin")
    private WebElement confirmPinInput;

    @FindBy(id = "confirm-transfer")
    private WebElement confirmTransferButton;

    @FindBy(css = ".success-message")
    private WebElement successMessage;

    @FindBy(css = ".transaction-id")
    private WebElement transactionIdElement;

    @FindBy(css = ".error-message")
    private WebElement errorMessage;

    public TransferPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public TransferPage enterReceiverUpiId(String upiId) {
        wait.until(ExpectedConditions.visibilityOf(receiverUpiIdInput));
        receiverUpiIdInput.clear();
        receiverUpiIdInput.sendKeys(upiId);
        return this;
    }

    public TransferPage enterAmount(String amount) {
        amountInput.clear();
        amountInput.sendKeys(amount);
        return this;
    }

    public TransferPage enterRemarks(String remarks) {
        remarksInput.clear();
        remarksInput.sendKeys(remarks);
        return this;
    }

    public TransferPage clickProceed() {
        proceedButton.click();
        return this;
    }

    public TransferPage enterConfirmPin(String pin) {
        wait.until(ExpectedConditions.visibilityOf(confirmPinInput));
        confirmPinInput.sendKeys(pin);
        return this;
    }

    public TransferPage clickConfirmTransfer() {
        confirmTransferButton.click();
        return this;
    }

    public boolean isTransferSuccessful() {
        wait.until(ExpectedConditions.visibilityOf(successMessage));
        return successMessage.getText().contains("SUCCESS");
    }

    public String getTransactionId() {
        return transactionIdElement.getText();
    }

    public String getErrorMessage() {
        wait.until(ExpectedConditions.visibilityOf(errorMessage));
        return errorMessage.getText();
    }

    // Fluent method for complete transfer
    public String performTransfer(String receiverUpi, String amount, String remarks, String pin) {
        return enterReceiverUpiId(receiverUpi)
            .enterAmount(amount)
            .enterRemarks(remarks)
            .clickProceed()
            .enterConfirmPin(pin)
            .clickConfirmTransfer()
            .getTransactionId();
    }
}
```

---

# LEVEL 24: E2E TEST SCENARIOS
## Write Complete User Journey Tests

**Concepts:**
- Happy path testing
- Error scenarios
- Multi-step workflows
- Screenshot on failure

**Test File:** `UPITransferE2ETest.java`

```java
package com.example.ui;

import com.example.ui.pages.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("ui")
class UPITransferE2ETest extends BaseUITest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private TransferPage transferPage;

    @BeforeEach
    void navigateToApp() {
        navigateTo("/");
        loginPage = new LoginPage(driver);
    }

    @Test
    @Order(1)
    @DisplayName("E2E-001: Successful UPI transfer from login to confirmation")
    void shouldCompleteSuccessfulTransfer() {
        // Step 1: Login
        dashboardPage = loginPage.loginAs("9876543210", "1234");
        assertTrue(dashboardPage.isDisplayed(), "Dashboard should be displayed after login");

        // Step 2: Navigate to transfer
        transferPage = dashboardPage.navigateToTransfer();

        // Step 3: Perform transfer
        String txnId = transferPage.performTransfer(
            "receiver@upi",
            "500",
            "E2E Test Payment",
            "1234"
        );

        // Step 4: Verify success
        assertTrue(transferPage.isTransferSuccessful());
        assertNotNull(txnId);
        assertFalse(txnId.isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("E2E-002: Transfer with insufficient balance shows error")
    void shouldShowErrorForInsufficientBalance() {
        dashboardPage = loginPage.loginAs("9876543210", "1234");
        transferPage = dashboardPage.navigateToTransfer();

        transferPage
            .enterReceiverUpiId("receiver@upi")
            .enterAmount("99999999")  // Very large amount
            .enterRemarks("Large transfer")
            .clickProceed()
            .enterConfirmPin("1234")
            .clickConfirmTransfer();

        // Verify error message
        String errorMessage = transferPage.getErrorMessage();
        assertTrue(errorMessage.contains("Insufficient") || errorMessage.contains("U30"));
    }

    @Test
    @Order(3)
    @DisplayName("E2E-003: Invalid UPI ID format shows validation error")
    void shouldValidateInvalidUpiId() {
        dashboardPage = loginPage.loginAs("9876543210", "1234");
        transferPage = dashboardPage.navigateToTransfer();

        transferPage
            .enterReceiverUpiId("invalid-upi-format")
            .enterAmount("100")
            .clickProceed();

        String errorMessage = transferPage.getErrorMessage();
        assertTrue(errorMessage.contains("Invalid UPI") || errorMessage.contains("format"));
    }

    @Test
    @Order(4)
    @DisplayName("E2E-004: Login with invalid credentials shows error")
    void shouldShowErrorForInvalidLogin() {
        loginPage
            .enterMobileNumber("9876543210")
            .enterUPIPin("0000");  // Wrong PIN
        
        loginPage.clickLogin();

        String errorMessage = loginPage.getErrorMessage();
        assertTrue(errorMessage.contains("Invalid") || errorMessage.contains("incorrect"));
    }

    @AfterEach
    void captureScreenshotOnFailure(TestInfo testInfo) {
        // Capture screenshot if test failed
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String testName = testInfo.getDisplayName().replaceAll("[^a-zA-Z0-9]", "_");
            Path destination = Path.of("target/screenshots", testName + ".png");
            Files.createDirectories(destination.getParent());
            Files.copy(screenshot.toPath(), destination);
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
```

---

# LEVEL 25: VISUAL REGRESSION TESTING
## Detect Unintended UI Changes

**Concepts:**
- Screenshot comparison
- Visual diff tools
- Baseline management
- CI integration

**Test File:** `VisualRegressionTest.java`

```java
package com.example.ui.visual;

import com.example.ui.BaseUITest;
import org.junit.jupiter.api.*;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@Tag("ui")
@Tag("visual")
class VisualRegressionTest extends BaseUITest {

    private static final Path BASELINE_DIR = Path.of("src/test/resources/visual-baselines");
    private static final Path ACTUAL_DIR = Path.of("target/visual-actual");
    private static final Path DIFF_DIR = Path.of("target/visual-diffs");
    private static final double DIFF_THRESHOLD = 0.01; // 1% difference allowed

    @BeforeAll
    static void createDirectories() throws Exception {
        Files.createDirectories(BASELINE_DIR);
        Files.createDirectories(ACTUAL_DIR);
        Files.createDirectories(DIFF_DIR);
    }

    @Test
    @DisplayName("VISUAL-001: Login page should match baseline")
    void loginPageShouldMatchBaseline() throws Exception {
        navigateTo("/login");
        Thread.sleep(1000); // Wait for animations

        compareWithBaseline("login-page");
    }

    @Test
    @DisplayName("VISUAL-002: Dashboard page should match baseline")
    void dashboardShouldMatchBaseline() throws Exception {
        navigateTo("/");
        // Perform login...
        Thread.sleep(1000);

        compareWithBaseline("dashboard-page");
    }

    @Test
    @DisplayName("VISUAL-003: Transfer form should match baseline")
    void transferFormShouldMatchBaseline() throws Exception {
        navigateTo("/transfer");
        Thread.sleep(1000);

        compareWithBaseline("transfer-form");
    }

    private void compareWithBaseline(String pageName) throws Exception {
        // Take full page screenshot
        Screenshot screenshot = new AShot()
            .shootingStrategy(ShootingStrategies.viewportPasting(100))
            .takeScreenshot(driver);

        BufferedImage actualImage = screenshot.getImage();
        File actualFile = ACTUAL_DIR.resolve(pageName + ".png").toFile();
        ImageIO.write(actualImage, "PNG", actualFile);

        // Load or create baseline
        File baselineFile = BASELINE_DIR.resolve(pageName + ".png").toFile();
        
        if (!baselineFile.exists()) {
            // First run - create baseline
            ImageIO.write(actualImage, "PNG", baselineFile);
            System.out.println("✅ Created new baseline for: " + pageName);
            return;
        }

        // Compare with baseline
        BufferedImage baselineImage = ImageIO.read(baselineFile);
        ImageDiffer differ = new ImageDiffer();
        ImageDiff diff = differ.makeDiff(baselineImage, actualImage);

        if (diff.hasDiff()) {
            // Calculate diff percentage
            int diffPixels = diff.getDiffSize();
            int totalPixels = actualImage.getWidth() * actualImage.getHeight();
            double diffPercentage = (double) diffPixels / totalPixels;

            // Save diff image
            File diffFile = DIFF_DIR.resolve(pageName + "-diff.png").toFile();
            ImageIO.write(diff.getMarkedImage(), "PNG", diffFile);

            if (diffPercentage > DIFF_THRESHOLD) {
                fail(String.format(
                    "Visual regression detected for %s. Diff: %.2f%% (threshold: %.2f%%). See: %s",
                    pageName, diffPercentage * 100, DIFF_THRESHOLD * 100, diffFile.getAbsolutePath()
                ));
            } else {
                System.out.println(String.format(
                    "⚠️ Minor visual difference for %s: %.2f%% (within threshold)",
                    pageName, diffPercentage * 100
                ));
            }
        }
    }

    @Test
    @DisplayName("VISUAL-004: Update baseline images")
    @Disabled("Run manually to update baselines")
    void updateBaselines() throws Exception {
        String[] pages = {"login-page", "dashboard-page", "transfer-form"};
        String[] urls = {"/login", "/dashboard", "/transfer"};

        for (int i = 0; i < pages.length; i++) {
            navigateTo(urls[i]);
            Thread.sleep(1000);

            Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(driver);

            File baselineFile = BASELINE_DIR.resolve(pages[i] + ".png").toFile();
            ImageIO.write(screenshot.getImage(), "PNG", baselineFile);
            System.out.println("✅ Updated baseline: " + pages[i]);
        }
    }
}
```

---

# 📊 LEVEL SUMMARY

| Level | Topic | Key Concepts |
|-------|-------|--------------|
| 0 | Code Refactoring | Dependency Injection, Interfaces |
| 1 | JUnit 5 Basics | @Test, Assertions, AAA Pattern |
| 2 | Exception Testing | assertThrows, Negative Scenarios |
| 3 | Parameterized Tests | @CsvSource, @ValueSource, Data-Driven |
| 4 | Mockito | @Mock, when/thenReturn, verify |
| 5 | TDD | Red-Green-Refactor Cycle |
| 6 | Spring Boot Test | @SpringBootTest, Context Loading |
| 7 | Repository Tests | @DataJpaTest, H2, TestEntityManager |
| 8 | Testcontainers | PostgreSQL Container, @Container |
| 9 | Service Integration | @Transactional, Full Context |
| 10 | WireMock | External API Mocking, Fault Simulation |
| 11 | REST Controller | @RestController, DTOs, Validation |
| 12 | MockMvc | @WebMvcTest, HTTP Simulation |
| 13 | RestAssured | Given-When-Then, Fluent API |
| 14 | Contract Testing | JSON Schema, API Versioning |
| 15 | Error Handling | Global Exception Handler, NPCI Codes |
| 16 | Smoke Tests | Critical Path, Fast Validation |
| 17 | Health Checks | Actuator, Custom Indicators |
| 18 | CI/CD Integration | GitHub Actions, Deployment Pipeline |
| 19 | JMeter Basics | Thread Groups, Samplers, Listeners |
| 20 | Performance SLAs | P50/P95/P99, Throughput, Gatling |
| 21 | Stress Testing | Breakpoint, Spike, Soak Tests |
| 22 | Selenium Setup | WebDriver, ChromeOptions |
| 23 | Page Object Model | @FindBy, PageFactory, Fluent API |
| 24 | E2E Tests | User Journeys, Screenshots |
| 25 | Visual Regression | Baseline Comparison, Diff Detection |

---

# 🎯 Learning Outcomes

After completing all 25 levels, the team will be able to:

1. ✅ Refactor tightly-coupled code for testability
2. ✅ Write comprehensive unit tests with JUnit 5 and Mockito
3. ✅ Create integration tests with real databases using Testcontainers
4. ✅ Mock external services with WireMock
5. ✅ Build automated API test suites with RestAssured
6. ✅ Implement contract testing with JSON Schema
7. ✅ Design smoke tests for deployment verification
8. ✅ Configure health check endpoints with Actuator
9. ✅ Integrate tests into CI/CD pipelines
10. ✅ Conduct load and stress tests with JMeter/Gatling
11. ✅ Automate UI testing with Selenium and Page Objects
12. ✅ Implement visual regression testing

---

*Document Version: 1.0*  
*Target: NPCI Fresh Employee Training*  
*Technology Focus: Java 17, Spring Boot 3.x, Testing Frameworks*
