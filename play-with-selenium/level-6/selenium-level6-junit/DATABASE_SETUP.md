# 🗄️ Database Setup Guide

## Setting Up Test Databases for Level 6

---

## Option 1: H2 In-Memory Database (Recommended for Learning)

### **No Setup Required!** ✅

The H2 database is automatically initialized when you run the tests.

```java
@ParameterizedTest
@ArgumentsSource(DatabaseArgumentsProvider.H2LoginDataProvider.class)
void test(String user, String pass, String type, String expected) {
    // H2 database auto-initializes with test data!
}
```

### **How it Works:**
1. H2 creates in-memory database on first access
2. Table `login_test_data` is created automatically
3. Sample test data is inserted
4. Database persists during test execution
5. Database is destroyed when tests complete

### **Benefits:**
- ✅ Zero configuration
- ✅ Perfect for CI/CD
- ✅ Fast execution
- ✅ No external dependencies
- ✅ Automatic cleanup

### **Run H2 Tests:**
```bash
mvn test -Dtest=Test03_DatabaseDataDriven -Dgroups="h2"
```

---

## Option 2: PostgreSQL (Production-like Testing)

### **Step 1: Install PostgreSQL**

**macOS:**
```bash
brew install postgresql@15
brew services start postgresql@15
```

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
```

**Windows:**
- Download from: https://www.postgresql.org/download/windows/
- Run installer
- Start PostgreSQL service

### **Step 2: Create Database**

```bash
# Login as postgres user
sudo -u postgres psql

# Create database
CREATE DATABASE testdata;

# Create user (optional)
CREATE USER testuser WITH PASSWORD 'testpass123';

# Grant privileges
GRANT ALL PRIVILEGES ON DATABASE testdata TO testuser;

# Exit
\q
```

### **Step 3: Create Table and Insert Data**

```bash
# Connect to database
psql -U postgres -d testdata

# Or with custom user
psql -U testuser -d testdata
```

```sql
-- Create table
CREATE TABLE login_test_data (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100),
    usertype VARCHAR(50),
    expected VARCHAR(20)
);

-- Insert test data
INSERT INTO login_test_data (username, password, usertype, expected) VALUES
('admin', 'admin123', 'Customer', 'success'),
('user1', 'user123', 'Customer', 'success'),
('merchant1', 'merchant123', 'Merchant', 'success'),
('testuser', 'test123', 'Customer', 'success'),
('wrong', 'wrong', 'Customer', 'fail'),
('', '', '', 'fail'),
('admin', 'wrongpass', 'Customer', 'fail'),
('invaliduser', 'somepass', 'Customer', 'fail');

-- Verify data
SELECT * FROM login_test_data;

-- Exit
\q
```

### **Step 4: Run Tests with PostgreSQL**

```bash
mvn test -Dtest=Test03_DatabaseDataDriven \
  -Ddb.url=jdbc:postgresql://localhost:5432/testdata \
  -Ddb.username=postgres \
  -Ddb.password=yourpassword
```

**Or with custom user:**
```bash
mvn test -Dtest=Test03_DatabaseDataDriven \
  -Ddb.url=jdbc:postgresql://localhost:5432/testdata \
  -Ddb.username=testuser \
  -Ddb.password=testpass123
```

---

## Option 3: MySQL (Alternative)

### **Step 1: Install MySQL**

**macOS:**
```bash
brew install mysql
brew services start mysql
```

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
```

### **Step 2: Create Database**

```bash
# Login
mysql -u root -p

# Create database
CREATE DATABASE testdata;

# Create user
CREATE USER 'testuser'@'localhost' IDENTIFIED BY 'testpass123';

# Grant privileges
GRANT ALL PRIVILEGES ON testdata.* TO 'testuser'@'localhost';
FLUSH PRIVILEGES;

# Exit
EXIT;
```

### **Step 3: Create Table and Data**

```bash
mysql -u testuser -p testdata
```

```sql
-- Create table
CREATE TABLE login_test_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100),
    usertype VARCHAR(50),
    expected VARCHAR(20)
);

-- Insert test data (same as PostgreSQL)
INSERT INTO login_test_data (username, password, usertype, expected) VALUES
('admin', 'admin123', 'Customer', 'success'),
('user1', 'user123', 'Customer', 'success'),
('merchant1', 'merchant123', 'Merchant', 'success'),
('wrong', 'wrong', 'Customer', 'fail');

-- Verify
SELECT * FROM login_test_data;

-- Exit
EXIT;
```

### **Step 4: Add MySQL Dependency**

Add to `pom.xml`:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.2.0</version>
</dependency>
```

### **Step 5: Create Custom Provider**

```java
public class MySQLLoginDataProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/testdata";
        String username = System.getProperty("db.username", "testuser");
        String password = System.getProperty("db.password", "testpass123");
        
        String query = "SELECT username, password, usertype, expected FROM login_test_data";
        
        return DatabaseArgumentsProvider.executeQuery(jdbcUrl, username, password, query);
    }
}
```

### **Step 6: Run Tests**

```bash
mvn test -Dtest=Test03_DatabaseDataDriven \
  -Ddb.url=jdbc:mysql://localhost:3306/testdata \
  -Ddb.username=testuser \
  -Ddb.password=testpass123
```

---

## Docker Setup (All Databases)

### **PostgreSQL with Docker:**

```bash
# Start PostgreSQL
docker run --name postgres-test \
  -e POSTGRES_PASSWORD=testpass \
  -e POSTGRES_DB=testdata \
  -p 5432:5432 \
  -d postgres:15

# Connect and create table
docker exec -it postgres-test psql -U postgres -d testdata

# Then run SQL commands from Step 3 above
```

### **MySQL with Docker:**

```bash
# Start MySQL
docker run --name mysql-test \
  -e MYSQL_ROOT_PASSWORD=testpass \
  -e MYSQL_DATABASE=testdata \
  -p 3306:3306 \
  -d mysql:8

# Connect and create table
docker exec -it mysql-test mysql -u root -p testdata

# Then run SQL commands
```

### **Stop Containers:**
```bash
docker stop postgres-test mysql-test
docker rm postgres-test mysql-test
```

---

## Troubleshooting

### **Connection Refused:**
```
Problem: Can't connect to database
Solution:
1. Check database is running
2. Verify port (PostgreSQL: 5432, MySQL: 3306)
3. Check firewall settings
4. Verify username/password
```

### **Table Not Found:**
```
Problem: Table 'login_test_data' doesn't exist
Solution:
1. Run CREATE TABLE commands
2. Verify you're in correct database
3. Check table name spelling
```

### **Driver Not Found:**
```
Problem: JDBC driver not found
Solution:
1. Check pom.xml has correct dependency
2. Run: mvn clean install
3. Verify Maven downloaded driver
```

### **Authentication Failed:**
```
Problem: Authentication failed for user
Solution:
1. Verify username/password
2. Check user has permissions
3. Verify connection string
```

---

## Best Practices

### **For Development:**
- ✅ Use H2 in-memory database
- ✅ Fast, no setup required
- ✅ Perfect for rapid testing

### **For Integration Testing:**
- ✅ Use Docker containers
- ✅ Easy setup/teardown
- ✅ Consistent environment

### **For Production Testing:**
- ✅ Use real database (PostgreSQL/MySQL)
- ✅ Separate test database
- ✅ Real production-like data

---

## Quick Reference

### **H2 (In-Memory):**
```bash
# No setup needed!
mvn test -Dtest=Test03_DatabaseDataDriven -Dgroups="h2"
```

### **PostgreSQL:**
```bash
# With default user
mvn test -Dtest=Test03_DatabaseDataDriven \
  -Ddb.url=jdbc:postgresql://localhost:5432/testdata \
  -Ddb.username=postgres \
  -Ddb.password=yourpassword
```

### **MySQL:**
```bash
# With custom user
mvn test -Dtest=Test03_DatabaseDataDriven \
  -Ddb.url=jdbc:mysql://localhost:3306/testdata \
  -Ddb.username=testuser \
  -Ddb.password=testpass123
```

---

## 💡 Recommendation

**Start with H2** (no setup), then move to **PostgreSQL with Docker** for production-like testing!

---

**Happy Database Testing!** 🗄️
