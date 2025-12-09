# Transfer Service - REST API for Banking Operations

A production-ready Spring Boot REST API application designed for **REST Assured Testing Training**. This service provides endpoints for account management, fund transfers, and transaction history.

## Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Database Profiles](#database-profiles)
- [Sample Data](#sample-data)
- [API Endpoints](#api-endpoints)
- [Error Handling](#error-handling)
- [REST Assured Testing Guide](#rest-assured-testing-guide)

---

## Features

- **Account Management**: Create, read, update, and delete bank accounts
- **Fund Transfers**: Transfer funds between accounts with validation
- **Transaction History**: Complete audit trail of all transactions
- **Dual Database Support**: H2 (development) and PostgreSQL (production)
- **Validation**: Request validation with meaningful error messages
- **Error Handling**: Centralized exception handling with consistent error responses
- **Interactive API Documentation**: Swagger UI for testing APIs

---

## Technology Stack

| Component | Technology |
|-----------|------------|
| Framework | Spring Boot 3.5.0 |
| Language | Java 17 |
| Database | H2 (dev) / PostgreSQL (prod) |
| ORM | Spring Data JPA |
| Validation | Jakarta Validation |
| API Docs | SpringDoc OpenAPI (Swagger) |
| Build Tool | Maven |

---

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL (only if using postgresql profile)

### Running the Application

**Using H2 Database (Default - Recommended for Testing)**
```bash
# From project root directory
mvn spring-boot:run

# Or build and run the JAR
mvn clean package
java -jar target/transfer-service-1.0.0.jar
```

**Using PostgreSQL Database**
```bash
# First, ensure PostgreSQL is running and database 'transferdb' exists
mvn spring-boot:run -Dspring.profiles.active=postgresql

# Or
java -jar target/transfer-service-1.0.0.jar --spring.profiles.active=postgresql
```

---

## API Documentation

### 📚 Swagger UI (Interactive Documentation)
Access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

Swagger UI allows you to:
- View all available endpoints
- See request/response schemas
- Test APIs directly from the browser
- View example requests and responses

### 📋 OpenAPI Specification (JSON)
```
http://localhost:8080/api-docs
```

### 🗄️ H2 Database Console
When running with H2 profile, access the database console at:
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:transferdb`
- **Username**: `sa`
- **Password**: (leave empty)

---

## Database Profiles

### H2 Profile (Default)
- **Profile**: `h2`
- **Type**: In-memory database
- **Use Case**: Development, testing, REST Assured practice
- **Data**: Sample data loaded automatically on startup
- **Console**: Available at `/h2-console`

### PostgreSQL Profile
- **Profile**: `postgresql`
- **Type**: Persistent database
- **Use Case**: Production deployment
- **Configuration**: Update `application-postgresql.properties` with your database credentials

---

## Sample Data

The application loads the following sample data on startup (H2 profile):

### Accounts
| Account ID | Holder Name | Email | Balance | Type | Status |
|------------|-------------|-------|---------|------|--------|
| A001 | John Doe | john.doe@example.com | 15,000.00 | SAVINGS | ACTIVE |
| A002 | Jane Smith | jane.smith@example.com | 25,000.50 | CURRENT | ACTIVE |
| A003 | Alice Johnson | alice.j@example.com | 30,000.75 | SAVINGS | ACTIVE |
| A004 | Bob Wilson | bob.wilson@example.com | 5,000.00 | SALARY | ACTIVE |
| A005 | Carol Davis | carol.davis@example.com | 75,000.00 | CURRENT | ACTIVE |
| A006 | Inactive User | inactive@example.com | 1,000.00 | SAVINGS | INACTIVE |
| A007 | Suspended User | suspended@example.com | 500.00 | SAVINGS | SUSPENDED |

### Sample Transactions
| Reference | From | To | Amount | Status |
|-----------|------|-----|--------|--------|
| TXN1001 | A001 | A002 | 500.00 | COMPLETED |
| TXN1002 | A002 | A003 | 1,000.00 | COMPLETED |
| TXN1003 | A003 | A001 | 250.50 | COMPLETED |
| TXN1004 | A004 | A005 | 100.00 | COMPLETED |
| TXN1005 | A005 | A004 | 2,000.00 | COMPLETED |
| TXN1006 | A001 | A003 | 750.00 | COMPLETED |
| TXN1007 | A004 | A002 | 10,000.00 | FAILED |

---

### Account Endpoints

#### 1. Create Account
```http
POST /api/v1/accounts
Content-Type: application/json

{
    "accountId": "A008",
    "accountHolderName": "New User",
    "email": "new.user@example.com",
    "phone": "9876543217",
    "initialBalance": 1000.00,
    "accountType": "SAVINGS"
}
```

**Response (201 Created)**
```json
{
    "success": true,
    "message": "Account created successfully",
    "data": {
        "accountId": "A008",
        "accountHolderName": "New User",
        "email": "new.user@example.com",
        "phone": "9876543217",
        "balance": 1000.00,
        "accountType": "SAVINGS",
        "status": "ACTIVE",
        "createdAt": "2024-01-15 10:30:00",
        "updatedAt": "2024-01-15 10:30:00"
    },
    "timestamp": "2024-01-15 10:30:00"
}
```

#### 2. Get Account
```http
GET /api/v1/accounts/{accountId}
```

#### 3. Get All Accounts
```http
GET /api/v1/accounts
GET /api/v1/accounts?status=ACTIVE
```

#### 4. Update Account
```http
PUT /api/v1/accounts/{accountId}
Content-Type: application/json

{
    "accountHolderName": "Updated Name",
    "email": "updated@example.com",
    "status": "ACTIVE"
}
```

#### 5. Delete Account (Soft Delete)
```http
DELETE /api/v1/accounts/{accountId}
```

#### 6. Check Account Exists
```http
GET /api/v1/accounts/{accountId}/exists
```

### Transfer Endpoints

#### 1. Initiate Transfer
```http
POST /api/v1/transfers
Content-Type: application/json

{
    "fromAccId": "A001",
    "toAccId": "A002",
    "amount": 500.00,
    "description": "Payment for services"
}
```

**Response (201 Created)**
```json
{
    "success": true,
    "message": "Transfer completed successfully",
    "data": {
        "transactionId": "1",
        "referenceNumber": "TXN1704795000123",
        "status": "SUCCESS",
        "message": "Transfer completed successfully",
        "fromAccountId": "A001",
        "toAccountId": "A002",
        "amount": 500.00,
        "fromAccountBalance": 14500.00,
        "toAccountBalance": 25500.50,
        "transactionDate": "2024-01-15 10:30:00"
    },
    "timestamp": "2024-01-15 10:30:00"
}
```

### Transaction Endpoints

#### 1. Get Transaction by Reference
```http
GET /api/v1/transactions/{referenceNumber}
```

#### 2. Get All Transactions for Account
```http
GET /api/v1/transactions/account/{accountId}
```

#### 3. Get Paginated Transactions
```http
GET /api/v1/transactions/account/{accountId}/paginated?page=0&size=10
```

#### 4. Get Sent Transactions (Debits)
```http
GET /api/v1/transactions/account/{accountId}/sent
```

#### 5. Get Received Transactions (Credits)
```http
GET /api/v1/transactions/account/{accountId}/received
```

#### 6. Get Transactions by Date Range
```http
GET /api/v1/transactions/account/{accountId}/range?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
```

#### 7. Get Recent Transactions
```http
GET /api/v1/transactions/recent
```

### Health Check
```http
GET /actuator/health
```

---

## Error Handling

All errors follow a consistent format:

```json
{
    "success": false,
    "message": "Error description",
    "error": {
        "code": "ERROR_CODE",
        "field": "field_name",
        "rejectedValue": "invalid_value",
        "details": ["Additional error details"]
    },
    "timestamp": "2024-01-15 10:30:00"
}
```

### Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| ACCOUNT_NOT_FOUND | 404 | Account does not exist |
| INSUFFICIENT_FUNDS | 400 | Not enough balance for transfer |
| ACCOUNT_NOT_ACTIVE | 400 | Account is not in ACTIVE status |
| DUPLICATE_ACCOUNT | 409 | Account/Email/Phone already exists |
| INVALID_TRANSFER | 400 | Invalid transfer request |
| TRANSACTION_NOT_FOUND | 404 | Transaction does not exist |
| VALIDATION_ERROR | 400 | Request validation failed |
| INTERNAL_ERROR | 500 | Unexpected server error |

---

## REST Assured Testing Guide

### Maven Dependency for REST Assured
```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.4.0</version>
    <scope>test</scope>
</dependency>
```

### Sample Test Cases

```java
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TransferServiceTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/v1";
    }

    // Test 1: Get Account
    @Test
    public void testGetAccount() {
        given()
        .when()
            .get("/accounts/A001")
        .then()
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo("A001"))
            .body("data.accountHolderName", equalTo("John Doe"));
    }

    // Test 2: Create Account
    @Test
    public void testCreateAccount() {
        String requestBody = """
            {
                "accountId": "TEST01",
                "accountHolderName": "Test User",
                "email": "test@example.com",
                "phone": "9999999999",
                "initialBalance": 1000.00,
                "accountType": "SAVINGS"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/accounts")
        .then()
            .statusCode(201)
            .body("success", equalTo(true))
            .body("data.accountId", equalTo("TEST01"));
    }

    // Test 3: Fund Transfer
    @Test
    public void testFundTransfer() {
        String requestBody = """
            {
                "fromAccId": "A001",
                "toAccId": "A002",
                "amount": 100.00,
                "description": "Test transfer"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/transfers")
        .then()
            .statusCode(201)
            .body("success", equalTo(true))
            .body("data.status", equalTo("SUCCESS"))
            .body("data.referenceNumber", notNullValue());
    }

    // Test 4: Insufficient Funds
    @Test
    public void testInsufficientFunds() {
        String requestBody = """
            {
                "fromAccId": "A004",
                "toAccId": "A001",
                "amount": 100000.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/transfers")
        .then()
            .statusCode(400)
            .body("success", equalTo(false))
            .body("error.code", equalTo("INSUFFICIENT_FUNDS"));
    }

    // Test 5: Account Not Found
    @Test
    public void testAccountNotFound() {
        given()
        .when()
            .get("/accounts/INVALID")
        .then()
            .statusCode(404)
            .body("success", equalTo(false))
            .body("error.code", equalTo("ACCOUNT_NOT_FOUND"));
    }

    // Test 6: Validation Error
    @Test
    public void testValidationError() {
        String requestBody = """
            {
                "fromAccId": "",
                "toAccId": "A002",
                "amount": -100
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/transfers")
        .then()
            .statusCode(400)
            .body("success", equalTo(false))
            .body("error.code", equalTo("VALIDATION_ERROR"));
    }

    // Test 7: Get Transaction History
    @Test
    public void testGetTransactionHistory() {
        given()
        .when()
            .get("/transactions/account/A001")
        .then()
            .statusCode(200)
            .body("success", equalTo(true))
            .body("data", not(empty()));
    }
}
```

### Testing Scenarios to Practice

1. **Happy Path Tests**
   - Create account successfully
   - Get existing account
   - Transfer funds between active accounts
   - View transaction history

2. **Negative Tests**
   - Account not found (404)
   - Insufficient funds (400)
   - Inactive/Suspended account (400)
   - Duplicate account creation (409)
   - Invalid request data (400)

3. **Boundary Tests**
   - Minimum transfer amount (0.01)
   - Maximum transfer amount
   - Empty account balance

4. **Integration Tests**
   - Create account → Transfer → Check balance → View history

---

## Support

For questions or issues, please contact the training team.

**Happy Testing! 🚀**
