# REST Assured Test Suite for Transfer Service

Comprehensive REST Assured test suite for testing the **Account Management** endpoints of the Transfer Service API.

## Prerequisites

1. **Java 17** or higher
2. **Maven 3.6** or higher
3. **Transfer Service** running on `http://localhost:8080`

## Quick Start

### 1. Start the Transfer Service

```bash
# Navigate to transfer-service directory
cd transfer-service

# Start with H2 database (default)
mvn spring-boot:run
```

Wait for the application to start. You should see:
```
Transfer Service Application Started!
```

### 2. Run the Tests

```bash
# Navigate to this test project directory
cd play-with-rest-assured

# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AccountCreateTest

# Run specific test method
mvn test -Dtest=AccountCreateTest#testCreateAccountWithAllFields
```

## Project Structure

```
play-with-rest-assured/
├── pom.xml                          # Maven dependencies
├── README.md                        # This file
└── src/test/java/com/example/
    ├── config/
    │   └── BaseTest.java            # Base configuration class
    ├── pojo/
    │   ├── AccountRequest.java      # Request POJO
    │   └── AccountUpdateRequest.java # Update request POJO
    └── tests/
        ├── AccountCreateTest.java   # POST /accounts tests
        ├── AccountGetTest.java      # GET /accounts tests
        ├── AccountUpdateTest.java   # PUT /accounts tests
        ├── AccountDeleteTest.java   # DELETE /accounts tests
        ├── AccountExistsTest.java   # GET /accounts/{id}/exists tests
        └── AccountIntegrationTest.java # End-to-end tests
```

## Test Categories

### 1. AccountCreateTest (51 test cases)
Tests for `POST /api/v1/accounts`

| Category | Test Cases |
|----------|------------|
| Happy Path | Create with all fields, minimum fields, zero balance |
| Account Types | SAVINGS, CURRENT, SALARY |
| Validation Errors | Missing ID, missing name, invalid email, invalid phone, negative balance |
| Duplicates | Duplicate ID, email, phone |
| Boundary Tests | Min/max ID length, max name length, large balance |
| Special Characters | Names with apostrophes, unicode characters |

### 2. AccountGetTest (61 test cases)
Tests for `GET /api/v1/accounts/{id}` and `GET /api/v1/accounts`

| Category | Test Cases |
|----------|------------|
| Single Account | Get existing accounts (A001-A007) |
| Not Found | Non-existent IDs, invalid IDs |
| Get All | Without filter, verify structure |
| Status Filters | ACTIVE, INACTIVE, SUSPENDED, CLOSED |
| Response Validation | Balance type, timestamp format, enum values |
| Performance | Response time checks |

### 3. AccountUpdateTest (70 test cases)
Tests for `PUT /api/v1/accounts/{id}`

| Category | Test Cases |
|----------|------------|
| Happy Path | Update name, email, phone, multiple fields |
| Status Updates | ACTIVE, INACTIVE, SUSPENDED, BLOCKED |
| Validation Errors | Invalid email, invalid phone, too long name |
| Duplicates | Email/phone already used by another account |
| Not Found | Non-existent account |
| Empty Updates | Empty body, null values |
| Special Characters | Unicode, apostrophes in name |
| Timestamp | Verify updatedAt changes |

### 4. AccountDeleteTest (41 test cases)
Tests for `DELETE /api/v1/accounts/{id}`

| Category | Test Cases |
|----------|------------|
| Happy Path | Delete existing account |
| Soft Delete | Verify status changes to CLOSED |
| Idempotency | Multiple deletes on same account |
| Not Found | Non-existent account |
| Filter Verification | Closed accounts in CLOSED filter |

### 5. AccountExistsTest (70 test cases)
Tests for `GET /api/v1/accounts/{id}/exists`

| Category | Test Cases |
|----------|------------|
| Existing | All sample accounts (A001-A007) |
| Non-Existing | Various invalid IDs |
| Response Structure | Boolean response validation |
| Case Sensitivity | Lowercase, mixed case |
| Performance | Response time checks |

### 6. AccountIntegrationTest (60 test cases)
End-to-end lifecycle tests

| Category | Test Cases |
|----------|------------|
| Full Lifecycle | Create → Read → Update → Delete |
| Data Consistency | Balance unchanged after updates |
| State Transitions | ACTIVE → SUSPENDED → ACTIVE → CLOSED |
| Bulk Operations | Create multiple accounts |
| Error Recovery | Operations after validation failures |

## Test Execution Reports

After running tests, check:
- Console output for immediate results
- `target/surefire-reports/` for detailed test reports

## Key REST Assured Features Demonstrated

1. **Request Specification** - Reusable request configuration
2. **Response Specification** - Reusable response validation
3. **Path Parameters** - `/accounts/{accountId}`
4. **Query Parameters** - `?status=ACTIVE`
5. **Request Body** - JSON serialization with POJOs
6. **Response Validation** - Status codes, JSON path assertions
7. **Hamcrest Matchers** - `equalTo`, `hasItem`, `notNullValue`, etc.
8. **Parameterized Tests** - Testing multiple inputs
9. **Ordered Tests** - Sequential test execution
10. **Logging** - Request/response logging

## Sample Test Output

```
[INFO] Running com.example.tests.AccountCreateTest
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.example.tests.AccountGetTest
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
...
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 150, Failures: 0, Errors: 0, Skipped: 0
```

## Troubleshooting

### Connection Refused
```
java.net.ConnectException: Connection refused
```
**Solution**: Make sure Transfer Service is running on port 8080

### Test Failures Due to Missing Sample Data
**Solution**: Restart Transfer Service to reload sample data (H2 resets on restart)

### Duplicate Account Errors
**Solution**: Restart Transfer Service or use unique account IDs

## Best Practices Demonstrated

1. **Test Independence** - Each test can run independently
2. **Test Data Cleanup** - Tests clean up after themselves
3. **Meaningful Names** - Descriptive test method names
4. **Test Categories** - Organized by endpoint and scenario
5. **Assertions** - Multiple assertions per test where appropriate
6. **Error Scenarios** - Comprehensive negative testing
7. **Boundary Testing** - Min/max value testing

## Extending the Tests

To add new tests:

1. Create new test class in `src/test/java/com/example/tests/`
2. Extend `BaseTest` for common configuration
3. Use `@Test`, `@DisplayName`, `@Order` annotations
4. Follow existing patterns for consistency

```java
@Test
@Order(100)
@DisplayName("TC100: My new test case")
void testMyNewScenario() {
    given()
        .spec(requestSpec)
    .when()
        .get("/accounts/A001")
    .then()
        .statusCode(200);
}
```

---

**Happy Testing! 🧪**
