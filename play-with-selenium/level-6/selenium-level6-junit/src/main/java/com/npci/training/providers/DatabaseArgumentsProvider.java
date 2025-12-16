package com.npci.training.providers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * DatabaseArgumentsProvider - Read test data from Database
 * 
 * Supports multiple databases: PostgreSQL, MySQL, H2
 * Uses HikariCP for connection pooling
 * 
 * Usage:
 * @ParameterizedTest
 * @ArgumentsSource(DatabaseArgumentsProvider.LoginDataProvider.class)
 * void test(String user, String pass, String type, String expected) { }
 */
public class DatabaseArgumentsProvider {
    
    private static HikariDataSource dataSource;
    
    /**
     * Initialize database connection pool
     */
    private static void initializeDataSource(String jdbcUrl, String username, String password) {
        if (dataSource == null || dataSource.isClosed()) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            
            dataSource = new HikariDataSource(config);
        }
    }
    
    /**
     * Close database connection pool
     */
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
    
    /**
     * Execute SQL query and return results as Stream of Arguments
     */
    public static Stream<Arguments> executeQuery(String jdbcUrl, String username, 
                                                 String password, String query) {
        initializeDataSource(jdbcUrl, username, password);
        
        List<Arguments> data = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                    if (rowData[i - 1] == null) {
                        rowData[i - 1] = "";
                    } else {
                        rowData[i - 1] = rowData[i - 1].toString();
                    }
                }
                data.add(Arguments.of(rowData));
            }
            
        } catch (SQLException e) {
            System.err.println("Error executing database query: " + e.getMessage());
            e.printStackTrace();
        }
        
        return data.stream();
    }
    
    /**
     * Login data provider from PostgreSQL
     */
    public static class PostgreSQLLoginDataProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            String jdbcUrl = System.getProperty("db.url", 
                "jdbc:postgresql://localhost:5432/testdata");
            String username = System.getProperty("db.username", "postgres");
            String password = System.getProperty("db.password", "postgres");
            
            String query = "SELECT username, password, usertype, expected FROM login_test_data";
            
            return executeQuery(jdbcUrl, username, password, query);
        }
    }
    
    /**
     * Login data provider from H2 (in-memory database for demo)
     */
    public static class H2LoginDataProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            // Initialize H2 in-memory database
            String jdbcUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
            String username = "sa";
            String password = "";
            
            // Create table and insert sample data
            initializeH2TestData(jdbcUrl, username, password);
            
            String query = "SELECT username, password, usertype, expected FROM login_test_data";
            
            return executeQuery(jdbcUrl, username, password, query);
        }
        
        /**
         * Initialize H2 database with test data
         */
        private void initializeH2TestData(String jdbcUrl, String username, String password) {
            initializeDataSource(jdbcUrl, username, password);
            
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                // Drop table if exists
                stmt.execute("DROP TABLE IF EXISTS login_test_data");
                
                // Create table
                stmt.execute(
                    "CREATE TABLE login_test_data (" +
                    "    id INT AUTO_INCREMENT PRIMARY KEY," +
                    "    username VARCHAR(100)," +
                    "    password VARCHAR(100)," +
                    "    usertype VARCHAR(50)," +
                    "    expected VARCHAR(20)" +
                    ")"
                );
                
                // Insert sample data
                stmt.execute(
                    "INSERT INTO login_test_data (username, password, usertype, expected) VALUES " +
                    "('admin', 'admin123', 'Customer', 'success')," +
                    "('user1', 'user123', 'Customer', 'success')," +
                    "('merchant1', 'merchant123', 'Merchant', 'success')," +
                    "('testuser', 'test123', 'Customer', 'success')," +
                    "('wrong', 'wrong', 'Customer', 'fail')," +
                    "('', '', '', 'fail')," +
                    "('admin', 'wrongpass', 'Customer', 'fail')," +
                    "('invaliduser', 'somepass', 'Customer', 'fail')"
                );
                
                System.out.println("✓ H2 test data initialized successfully");
                
            } catch (SQLException e) {
                System.err.println("Error initializing H2 test data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Custom query provider - specify your own query
     */
    public static class CustomQueryProvider implements ArgumentsProvider {
        private final String jdbcUrl;
        private final String username;
        private final String password;
        private final String query;
        
        public CustomQueryProvider(String jdbcUrl, String username, 
                                  String password, String query) {
            this.jdbcUrl = jdbcUrl;
            this.username = username;
            this.password = password;
            this.query = query;
        }
        
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return executeQuery(jdbcUrl, username, password, query);
        }
    }
    
    /**
     * Utility method to get database data directly
     */
    public static Stream<Arguments> fromDatabase(String jdbcUrl, String username, 
                                                 String password, String query) {
        return executeQuery(jdbcUrl, username, password, query);
    }
    
    /**
     * Print database data for debugging
     */
    public static void printDatabaseData(String jdbcUrl, String username, 
                                        String password, String query) {
        List<Arguments> data = executeQuery(jdbcUrl, username, password, query)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        System.out.println("\n=== Database Query Results ===");
        System.out.println("Query: " + query);
        System.out.println("Total rows: " + data.size());
        
        for (int i = 0; i < data.size(); i++) {
            System.out.print("Row " + (i + 1) + ": ");
            Object[] args = data.get(i).get();
            for (Object obj : args) {
                System.out.print(obj + " | ");
            }
            System.out.println();
        }
    }
}
