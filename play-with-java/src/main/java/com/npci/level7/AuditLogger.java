package com.npci.level7;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Level 7: Static Members - Audit Logger
 *
 * Demonstrates static members for centralized logging.
 */
public class AuditLogger {

    // ═══════════════════════════════════════════════════════════════
    // STATIC CONSTANTS
    // ═══════════════════════════════════════════════════════════════

    public static final String LEVEL_INFO = "INFO";
    public static final String LEVEL_WARN = "WARN";
    public static final String LEVEL_ERROR = "ERROR";
    public static final String LEVEL_AUDIT = "AUDIT";

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // ═══════════════════════════════════════════════════════════════
    // STATIC VARIABLES
    // ═══════════════════════════════════════════════════════════════

    private static List<String> auditLog = new ArrayList<>();
    private static boolean loggingEnabled = true;
    private static boolean consoleOutput = true;
    private static int maxLogSize = 1000;

    private static long infoCount = 0;
    private static long warnCount = 0;
    private static long errorCount = 0;
    private static long auditCount = 0;

    // ═══════════════════════════════════════════════════════════════
    // PRIVATE CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    private AuditLogger() {
        throw new UnsupportedOperationException("Logger class");
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC METHODS - Logging
    // ═══════════════════════════════════════════════════════════════

    /**
     * Log info message
     */
    public static void info(String message) {
        log(LEVEL_INFO, message);
        infoCount++;
    }

    /**
     * Log warning message
     */
    public static void warn(String message) {
        log(LEVEL_WARN, message);
        warnCount++;
    }

    /**
     * Log error message
     */
    public static void error(String message) {
        log(LEVEL_ERROR, message);
        errorCount++;
    }

    /**
     * Log audit event
     */
    public static void audit(String entityType, String entityId, String action, String details) {
        String message = String.format("[%s:%s] %s - %s", entityType, entityId, action, details);
        log(LEVEL_AUDIT, message);
        auditCount++;
    }

    /**
     * Core logging method
     */
    private static void log(String level, String message) {
        if (!loggingEnabled) return;

        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String logEntry = String.format("[%s] [%s] %s", timestamp, level, message);

        // Add to log
        auditLog.add(logEntry);

        // Trim if exceeds max size
        if (auditLog.size() > maxLogSize) {
            auditLog.remove(0);
        }

        // Console output
        if (consoleOutput) {
            System.out.println(logEntry);
        }
    }

    /**
     * Log transaction
     */
    public static void logTransaction(String txnId, String type, String account,
                                      double amount, String status) {
        String message = String.format("TXN[%s] %s | Account: %s | Amount: Rs.%.2f | Status: %s",
                txnId, type, account, amount, status);
        audit("TRANSACTION", txnId, type, message);
    }

    /**
     * Log account event
     */
    public static void logAccountEvent(String accountNumber, String event, String details) {
        audit("ACCOUNT", accountNumber, event, details);
    }

    /**
     * Log customer event
     */
    public static void logCustomerEvent(String customerId, String event, String details) {
        audit("CUSTOMER", customerId, event, details);
    }

    /**
     * Log security event
     */
    public static void logSecurityEvent(String eventType, String userId, String details) {
        String message = String.format("SECURITY[%s] User: %s | %s", eventType, userId, details);
        log(LEVEL_WARN, message);
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC METHODS - Configuration
    // ═══════════════════════════════════════════════════════════════

    public static void setLoggingEnabled(boolean enabled) {
        loggingEnabled = enabled;
    }

    public static void setConsoleOutput(boolean enabled) {
        consoleOutput = enabled;
    }

    public static void setMaxLogSize(int size) {
        maxLogSize = size;
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC METHODS - Retrieval
    // ═══════════════════════════════════════════════════════════════

    /**
     * Get all logs
     */
    public static List<String> getLogs() {
        return new ArrayList<>(auditLog);
    }

    /**
     * Get last N logs
     */
    public static List<String> getLastLogs(int n) {
        int size = auditLog.size();
        int from = Math.max(0, size - n);
        return new ArrayList<>(auditLog.subList(from, size));
    }

    /**
     * Get logs by level
     */
    public static List<String> getLogsByLevel(String level) {
        List<String> filtered = new ArrayList<>();
        for (String log : auditLog) {
            if (log.contains("[" + level + "]")) {
                filtered.add(log);
            }
        }
        return filtered;
    }

    /**
     * Clear logs
     */
    public static void clearLogs() {
        auditLog.clear();
        System.out.println("[AuditLogger] Logs cleared");
    }

    /**
     * Display log statistics
     */
    public static void displayStatistics() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║          AUDIT LOGGER STATISTICS              ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("  Total Log Entries : " + auditLog.size());
        System.out.println("  INFO Messages     : " + infoCount);
        System.out.println("  WARN Messages     : " + warnCount);
        System.out.println("  ERROR Messages    : " + errorCount);
        System.out.println("  AUDIT Events      : " + auditCount);
        System.out.println("  Logging Enabled   : " + loggingEnabled);
        System.out.println("  Console Output    : " + consoleOutput);
        System.out.println("  Max Log Size      : " + maxLogSize);
        System.out.println("╚═══════════════════════════════════════════════╝");
    }

    /**
     * Display last N logs
     */
    public static void displayLastLogs(int n) {
        System.out.println("\n=== LAST " + n + " LOG ENTRIES ===");
        List<String> logs = getLastLogs(n);
        for (String log : logs) {
            System.out.println(log);
        }
        System.out.println("================================\n");
    }
}