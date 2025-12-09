package com.example.exception;

/**
 * Exception thrown when transaction amount is invalid.
 * 
 * NPCI Error Code: U09
 * 
 * SCENARIOS:
 * ==========
 * - Negative amount
 * - Zero amount
 * - Amount with too many decimal places
 * - Amount below minimum threshold
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public class InvalidAmountException extends TransferException {

    /** NPCI error code for invalid amount */
    public static final String ERROR_CODE = "U09";

    /** The invalid amount provided */
    private final double amount;
    
    /** Type of validation failure */
    private final AmountValidationType validationType;

    /**
     * Types of amount validation failures.
     */
    public enum AmountValidationType {
        NEGATIVE("Amount cannot be negative"),
        ZERO("Amount cannot be zero"),
        BELOW_MINIMUM("Amount is below minimum limit"),
        ABOVE_MAXIMUM("Amount exceeds maximum limit"),
        INVALID_PRECISION("Amount has invalid decimal precision");

        private final String description;

        AmountValidationType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Constructor for negative/zero amount.
     * 
     * @param amount The invalid amount
     * @param validationType Type of validation failure
     */
    public InvalidAmountException(double amount, AmountValidationType validationType) {
        super(
            String.format("Invalid amount ₹%.2f: %s", amount, validationType.getDescription()),
            ERROR_CODE
        );
        this.amount = amount;
        this.validationType = validationType;
    }

    /**
     * Constructor for simple invalid amount.
     * 
     * @param amount The invalid amount
     */
    public InvalidAmountException(double amount) {
        super(
            String.format("Invalid amount: ₹%.2f", amount),
            ERROR_CODE
        );
        this.amount = amount;
        this.validationType = amount < 0 ? AmountValidationType.NEGATIVE : AmountValidationType.ZERO;
    }

    /**
     * Convenience factory for negative amount.
     * 
     * @param amount The negative amount
     * @return InvalidAmountException
     */
    public static InvalidAmountException negative(double amount) {
        return new InvalidAmountException(amount, AmountValidationType.NEGATIVE);
    }

    /**
     * Convenience factory for zero amount.
     * 
     * @return InvalidAmountException
     */
    public static InvalidAmountException zero() {
        return new InvalidAmountException(0.0, AmountValidationType.ZERO);
    }

    /**
     * Get the invalid amount.
     * 
     * @return The amount that failed validation
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Get the type of validation failure.
     * 
     * @return AmountValidationType
     */
    public AmountValidationType getValidationType() {
        return validationType;
    }

    /**
     * Check if amount was negative.
     * 
     * @return true if amount was negative
     */
    public boolean isNegative() {
        return validationType == AmountValidationType.NEGATIVE;
    }

    /**
     * Check if amount was zero.
     * 
     * @return true if amount was zero
     */
    public boolean isZero() {
        return validationType == AmountValidationType.ZERO;
    }
}
