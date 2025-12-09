package com.example.exception;

import java.time.LocalDateTime;

/**
 * Base Exception for all UPI Transfer related errors.
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║                    TRANSFER EXCEPTION                         ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Base class for all transfer-related exceptions.              ║
 * ║  Provides:                                                    ║
 * ║  • NPCI error code                                            ║
 * ║  • Timestamp for logging/debugging                            ║
 * ║  • Consistent structure across all exceptions                 ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * NPCI ERROR CODES:
 * =================
 * U00 - System error / Unknown error
 * U09 - Transaction limit exceeded
 * U16 - Risk threshold exceeded
 * U30 - Insufficient funds / Account not found
 * U68 - Transaction timeout
 * U91 - Service temporarily unavailable
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public abstract class TransferException extends RuntimeException {

    /** NPCI standard error code */
    private final String errorCode;
    
    /** When the exception occurred */
    private final LocalDateTime timestamp;
    
    /** Transaction reference if available */
    private final String transactionRef;

    /**
     * Constructor with message and error code.
     * 
     * @param message Human-readable error description
     * @param errorCode NPCI error code (e.g., "U30")
     */
    protected TransferException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
        this.transactionRef = null;
    }

    /**
     * Constructor with message, error code, and transaction reference.
     * 
     * @param message Human-readable error description
     * @param errorCode NPCI error code
     * @param transactionRef Transaction reference number
     */
    protected TransferException(String message, String errorCode, String transactionRef) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
        this.transactionRef = transactionRef;
    }

    /**
     * Constructor with cause.
     * 
     * @param message Human-readable error description
     * @param errorCode NPCI error code
     * @param cause The underlying cause
     */
    protected TransferException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
        this.transactionRef = null;
    }

    /**
     * Get the NPCI error code.
     * 
     * @return Error code (e.g., "U30", "U09")
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Get timestamp when exception occurred.
     * 
     * @return LocalDateTime of exception
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Get transaction reference if available.
     * 
     * @return Transaction reference or null
     */
    public String getTransactionRef() {
        return transactionRef;
    }

    /**
     * Get formatted error for logging.
     * 
     * @return Formatted error string
     */
    public String getFormattedError() {
        return String.format("[%s] %s | Time: %s | Ref: %s",
            errorCode,
            getMessage(),
            timestamp,
            transactionRef != null ? transactionRef : "N/A"
        );
    }

    @Override
    public String toString() {
        return String.format("%s{errorCode='%s', message='%s', timestamp=%s}",
            getClass().getSimpleName(),
            errorCode,
            getMessage(),
            timestamp
        );
    }
}
