package com.npci.level8;

import java.io.*;

/**
 * Level 8: Exception Handling - File Processor
 *
 * Demonstrates:
 * - Checked exceptions (IOException)
 * - try-with-resources for file handling
 * - Exception handling for I/O operations
 */
public class FileProcessor {

    /**
     * Read transaction file - demonstrates checked IOException
     */
    public void readTransactionFile(String filename) {
        System.out.println("\n=== Reading Transaction File ===");

        // try-with-resources for automatic resource management
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                System.out.println("Line " + lineNumber + ": " + line);
            }

            System.out.println("File read successfully. Total lines: " + lineNumber);

        } catch (FileNotFoundException e) {
            System.out.println("❌ File not found: " + filename);
            System.out.println("   Please check the file path.");

        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }
        // Reader automatically closed here
    }

    /**
     * Write transaction log - demonstrates file writing with exceptions
     */
    public void writeTransactionLog(String filename, String[] transactions) {
        System.out.println("\n=== Writing Transaction Log ===");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String txn : transactions) {
                writer.write(txn);
                writer.newLine();
            }
            System.out.println("Log written successfully to: " + filename);

        } catch (IOException e) {
            System.out.println("❌ Error writing file: " + e.getMessage());
        }
    }

    /**
     * Process transaction file - demonstrates multiple resource handling
     */
    public void processTransactionFile(String inputFile, String outputFile) {
        System.out.println("\n=== Processing Transaction File ===");

        // Multiple resources in try-with-resources
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            int processed = 0;

            while ((line = reader.readLine()) != null) {
                // Process line
                String processedLine = "PROCESSED: " + line;
                writer.write(processedLine);
                writer.newLine();
                processed++;
            }

            System.out.println("Processed " + processed + " transactions");
            System.out.println("Output written to: " + outputFile);

        } catch (FileNotFoundException e) {
            System.out.println("❌ File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ I/O Error: " + e.getMessage());
        }
    }

    /**
     * Demonstrate creating and reading a file
     */
    public void demonstrateFileHandling() {
        String filename = "test_transactions.txt";

        // Write test file
        String[] testTransactions = {
                "TXN001,DEPOSIT,50000,SUCCESS",
                "TXN002,WITHDRAW,10000,SUCCESS",
                "TXN003,TRANSFER,25000,SUCCESS",
                "TXN004,WITHDRAW,5000,FAILED"
        };

        writeTransactionLog(filename, testTransactions);

        // Read it back
        readTransactionFile(filename);

        // Try reading non-existent file
        readTransactionFile("non_existent_file.txt");

        // Cleanup
        new File(filename).delete();
    }
}