package com.example.exception;

/**
 * Exception thrown when transaction exceeds UPI limits.
 * 
 * NPCI Error Code: U09
 * 
 * UPI LIMITS (as per NPCI/RBI):
 * ==============================
 * - Minimum per transaction: ₹1
 * - Maximum per transaction: ₹1,00,000
 * - Daily limit varies by bank (typically ₹1,00,000 - ₹2,00,000)
 * 
 * SCENARIOS:
 * ==========
 * - Single transaction exceeds ₹1,00,000
 * - Amount below ₹1 minimum
 * - Daily limit exceeded
 * - Monthly limit exceeded
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public class TransactionLimitExceededException extends TransferException {

    /** NPCI error code for limit exceeded */
    public static final String ERROR_CODE = "U09";
    
    /** Standard UPI minimum limit: ₹1 */
    public static final double UPI_MIN_LIMIT = 1.0;
    
    /** Standard UPI maximum limit: ₹1,00,000 */
    public static final double UPI_MAX_LIMIT = 100000.0;

    /** The requested amount */
    private final double requestedAmount;
    
    /** The applicable limit */
    private final double limit;
    
    /** Type of limit that was exceeded */
    private final LimitType limitType;

    /**
     * Types of transaction limits.
     */
    public enum LimitType {
        PER_TRANSACTION_MIN("Minimum per transaction"),
        PER_TRANSACTION_MAX("Maximum per transaction"),
        DAILY("Daily limit"),
        MONTHLY("Monthly limit"),
        BENEFICIARY("Per beneficiary limit");

        private final String description;

        LimitType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Constructor with amount and limit details.
     * 
     * @param requestedAmount Amount attempted to transfer
     * @param limit The limit that was exceeded
     * @param limitType Type of limit
     */
    public TransactionLimitExceededException(double requestedAmount, double limit, LimitType limitType) {
        super(
            String.format("%s exceeded: Requested ₹%.2f, Limit ₹%.2f",
                limitType.getDescription(), requestedAmount, limit),
            ERROR_CODE
        );
        this.requestedAmount = requestedAmount;
        this.limit = limit;
        this.limitType = limitType;
    }

    /**
     * Factory for per-transaction maximum exceeded.
     * 
     * @param amount The requested amount
     * @return TransactionLimitExceededException
     */
    public static TransactionLimitExceededException maxLimitExceeded(double amount) {
        return new TransactionLimitExceededException(amount, UPI_MAX_LIMIT, LimitType.PER_TRANSACTION_MAX);
    }

    /**
     * Factory for minimum amount not met.
     * 
     * @param amount The requested amount
     * @return TransactionLimitExceededException
     */
    public static TransactionLimitExceededException belowMinimum(double amount) {
        return new TransactionLimitExceededException(amount, UPI_MIN_LIMIT, LimitType.PER_TRANSACTION_MIN);
    }

    /**
     * Get the requested amount.
     * 
     * @return Requested amount in rupees
     */
    public double getRequestedAmount() {
        return requestedAmount;
    }

    /**
     * Get the limit that was exceeded.
     * 
     * @return Limit in rupees
     */
    public double getLimit() {
        return limit;
    }

    /**
     * Get the type of limit.
     * 
     * @return LimitType
     */
    public LimitType getLimitType() {
        return limitType;
    }

    /**
     * Get how much the limit was exceeded by.
     * 
     * @return Excess amount (negative if below minimum)
     */
    public double getExcessAmount() {
        if (limitType == LimitType.PER_TRANSACTION_MIN) {
            return limit - requestedAmount;  // How much more needed
        }
        return requestedAmount - limit;  // How much over
    }

    /**
     * Check if this was a maximum limit violation.
     * 
     * @return true if max limit exceeded
     */
    public boolean isMaxLimitExceeded() {
        return limitType == LimitType.PER_TRANSACTION_MAX || 
               limitType == LimitType.DAILY || 
               limitType == LimitType.MONTHLY;
    }

    /**
     * Check if this was a minimum limit violation.
     * 
     * @return true if below minimum
     */
    public boolean isBelowMinimum() {
        return limitType == LimitType.PER_TRANSACTION_MIN;
    }
}
