package com.npci.training.level2;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 2 - Test 06: Working with Tables
 * 
 * Topics Covered:
 * - Finding table elements
 * - Getting row and column counts
 * - Reading cell values
 * - Iterating through tables
 * - Finding specific data in tables
 * - Clicking buttons in table rows
 * 
 * Duration: 25 minutes
 */
public class Test06_WorkingWithTables {

    WebDriver driver;
    
    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        
        // Login to access dashboard
        driver.get("http://127.0.0.1:5500/level-0/banking-portal-final/login.html");
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin123");
        driver.findElement(By.id("userType")).click();
        driver.findElement(By.cssSelector("option[value='customer']")).click();
        driver.findElement(By.id("terms")).click();
        driver.findElement(By.id("loginBtn")).click();
        
        try {
            Thread.sleep(2000);
            Alert alert = driver.switchTo().alert();
            alert.accept();
            Thread.sleep(1000);
        } catch (Exception e) {
            // No alert
        }
    }
    
    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @DisplayName("Get table row count")
    public void testGetRowCount() throws InterruptedException {
        System.out.println("\n=== Get Table Row Count ===");
        
        // Find all rows in tbody
        List<WebElement> rows = driver.findElements(
            By.cssSelector("#transactionTable tbody tr")
        );
        
        System.out.println("✓ Total rows: " + rows.size());
        assertEquals(5, rows.size());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Get table column count")
    public void testGetColumnCount() throws InterruptedException {
        System.out.println("\n=== Get Table Column Count ===");
        
        // Find all header columns
        List<WebElement> headers = driver.findElements(
            By.cssSelector("#transactionTable thead th")
        );
        
        System.out.println("✓ Total columns: " + headers.size());
        
        // Print column names
        System.out.println("\nColumn headers:");
        for (int i = 0; i < headers.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + headers.get(i).getText());
        }
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Read specific cell value")
    public void testReadCellValue() throws InterruptedException {
        System.out.println("\n=== Read Specific Cell Value ===");
        
        // Read cell from row 1, column 1 (Transaction ID)
        WebElement cell = driver.findElement(
            By.cssSelector("#transactionTable tbody tr:nth-child(1) td:nth-child(1)")
        );
        
        String transactionId = cell.getText();
        System.out.println("✓ Row 1, Col 1: " + transactionId);
        assertFalse(transactionId.isEmpty());
        
        // Read cell from row 1, column 2 (Date)
        cell = driver.findElement(
            By.cssSelector("#transactionTable tbody tr:nth-child(1) td:nth-child(2)")
        );
        
        String date = cell.getText();
        System.out.println("✓ Row 1, Col 2: " + date);
        
        // Read amount from row 1
        cell = driver.findElement(
            By.cssSelector("#transactionTable tbody tr:nth-child(1) td:nth-child(4)")
        );
        
        String amount = cell.getText();
        System.out.println("✓ Row 1, Amount: " + amount);
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Iterate through all rows")
    public void testIterateRows() throws InterruptedException {
        System.out.println("\n=== Iterate Through All Rows ===");
        
        List<WebElement> rows = driver.findElements(
            By.cssSelector("#transactionTable tbody tr")
        );
        
        System.out.println("Transaction Details:");
        System.out.println("-".repeat(80));
        
        for (int i = 0; i < rows.size(); i++) {
            WebElement row = rows.get(i);
            
            // Get all cells in this row
            List<WebElement> cells = row.findElements(By.tagName("td"));
            
            String txnId = cells.get(0).getText();
            String date = cells.get(1).getText();
            String type = cells.get(2).getText();
            String amount = cells.get(3).getText();
            String status = cells.get(4).getText();
            
            System.out.println(String.format("Row %d: %s | %s | %s | %s | %s",
                i + 1, txnId, date, type, amount, status));
        }
        
        System.out.println("-".repeat(80));
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Find row by specific text")
    public void testFindRowByText() throws InterruptedException {
        System.out.println("\n=== Find Row by Specific Text ===");
        
        List<WebElement> rows = driver.findElements(
            By.cssSelector("#transactionTable tbody tr")
        );
        
        // Find row containing "UPI"
        boolean found = false;
        for (WebElement row : rows) {
            String rowText = row.getText();
            if (rowText.contains("UPI")) {
                System.out.println("✓ Found UPI transaction:");
                System.out.println("  " + rowText);
                found = true;
                break;
            }
        }
        
        assertTrue(found, "Should find UPI transaction");
        
        // Find row by XPath
        WebElement upiRow = driver.findElement(
            By.xpath("//td[contains(text(), 'UPI')]/parent::tr")
        );
        System.out.println("\n✓ Found using XPath: " + upiRow.getText());
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Click button in table row")
    public void testClickButtonInRow() throws InterruptedException {
        System.out.println("\n=== Click Button in Table Row ===");
        
        // Click View button in first row
        WebElement viewBtn = driver.findElement(
            By.cssSelector("#transactionTable tbody tr:nth-child(1) button")
        );
        
        System.out.println("✓ Clicking View button in row 1");
        viewBtn.click();
        Thread.sleep(1000);
        
        // Handle alert
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("✓ Alert received: " + alertText);
            assertTrue(alertText.contains("TXN"));
            alert.accept();
        } catch (NoAlertPresentException e) {
            fail("Expected alert after clicking View");
        }
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Get sum of amounts in table")
    public void testCalculateSum() throws InterruptedException {
        System.out.println("\n=== Calculate Sum of Amounts ===");
        
        List<WebElement> rows = driver.findElements(
            By.cssSelector("#transactionTable tbody tr")
        );
        
        double totalAmount = 0.0;
        
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            String amountText = cells.get(3).getText();
            
            // Remove currency symbol and commas, parse to double
            String amountStr = amountText.replace("₹", "").replace(",", "").trim();
            double amount = Double.parseDouble(amountStr);
            
            System.out.println("Row amount: ₹" + amount);
            totalAmount += amount;
        }
        
        System.out.println("\n✓ Total Amount: ₹" + totalAmount);
        assertTrue(totalAmount > 0);
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Filter table data")
    public void testFilterTableData() throws InterruptedException {
        System.out.println("\n=== Filter Table Data ===");
        
        List<WebElement> rows = driver.findElements(
            By.cssSelector("#transactionTable tbody tr")
        );
        
        System.out.println("Filtering for 'Completed' status:");
        int completedCount = 0;
        
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            String status = cells.get(4).getText();
            
            if (status.equals("Completed")) {
                completedCount++;
                String txnId = cells.get(0).getText();
                String type = cells.get(2).getText();
                System.out.println("  " + txnId + " - " + type + " - " + status);
            }
        }
        
        System.out.println("\n✓ Total completed transactions: " + completedCount);
        assertTrue(completedCount > 0);
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Work with accounts table")
    public void testAccountsTable() throws InterruptedException {
        System.out.println("\n=== Work with Accounts Table ===");
        
        // Navigate to accounts page
        driver.findElement(By.linkText("Accounts")).click();
        Thread.sleep(1500);
        
        // Get table rows
        List<WebElement> rows = driver.findElements(
            By.cssSelector("#accountsTable tbody tr")
        );
        
        System.out.println("✓ Total accounts: " + rows.size());
        
        // Extract all account holders
        System.out.println("\nAccount Holders:");
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() > 1) {
                String accountNumber = cells.get(1).getText();
                String name = cells.get(2).getText();
                String type = cells.get(3).getText();
                String balance = cells.get(4).getText();
                
                System.out.println(String.format("  %s: %s (%s) - %s",
                    accountNumber, name, type, balance));
            }
        }
        
        Thread.sleep(2000);
    }
    
    @Test
    @DisplayName("Table navigation with pagination")
    public void testTablePagination() throws InterruptedException {
        System.out.println("\n=== Table Pagination ===");
        
        // Navigate to accounts page
        driver.findElement(By.linkText("Accounts")).click();
        Thread.sleep(1500);
        
        // Get initial row count
        List<WebElement> rowsPage1 = driver.findElements(
            By.cssSelector("#accountsTable tbody tr")
        );
        System.out.println("✓ Page 1 rows: " + rowsPage1.size());
        
        // Click next page (if pagination exists)
        try {
            WebElement nextBtn = driver.findElement(
                By.xpath("//button[contains(text(), 'Next')]")
            );
            
            if (nextBtn.isEnabled()) {
                nextBtn.click();
                Thread.sleep(1500);
                System.out.println("✓ Navigated to next page");
                
                List<WebElement> rowsPage2 = driver.findElements(
                    By.cssSelector("#accountsTable tbody tr")
                );
                System.out.println("✓ Page 2 rows: " + rowsPage2.size());
                
                // Go back to page 1
                WebElement prevBtn = driver.findElement(
                    By.xpath("//button[contains(text(), 'Previous')]")
                );
                prevBtn.click();
                Thread.sleep(1500);
                System.out.println("✓ Back to page 1");
            }
        } catch (NoSuchElementException e) {
            System.out.println("ℹ No pagination available");
        }
        
        Thread.sleep(2000);
    }
}
