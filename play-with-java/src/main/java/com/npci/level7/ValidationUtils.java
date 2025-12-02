package com.npci.level7;

import java.util.regex.Pattern;

/**
 * Level 7: Static Members - Validation Utilities
 *
 * Pure utility class with static validation methods.
 */
public class ValidationUtils {

    // ═══════════════════════════════════════════════════════════════
    // STATIC CONSTANTS - Regex Patterns
    // ═══════════════════════════════════════════════════════════════

    private static final Pattern PAN_PATTERN = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]");
    private static final Pattern AADHAR_PATTERN = Pattern.compile("\\d{12}");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{10}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    private static final Pattern IFSC_PATTERN = Pattern.compile("[A-Z]{4}0[A-Z0-9]{6}");
    private static final Pattern PINCODE_PATTERN = Pattern.compile("\\d{6}");
    private static final Pattern UPI_PATTERN = Pattern.compile("[a-zA-Z0-9.\\-_]+@[a-zA-Z]+");
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("\\d{9,18}");

    // Validation counters
    private static int validationCount = 0;
    private static int successCount = 0;
    private static int failureCount = 0;

    // ═══════════════════════════════════════════════════════════════
    // PRIVATE CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC VALIDATION METHODS
    // ═══════════════════════════════════════════════════════════════

    /**
     * Validate PAN number
     */
    public static boolean isValidPAN(String pan) {
        validationCount++;
        boolean valid = pan != null && PAN_PATTERN.matcher(pan).matches();
        if (valid) successCount++; else failureCount++;
        return valid;
    }

    /**
     * Validate Aadhar number
     */
    public static boolean isValidAadhar(String aadhar) {
        validationCount++;
        boolean valid = aadhar != null && AADHAR_PATTERN.matcher(aadhar).matches();
        if (valid) successCount++; else failureCount++;
        return valid;
    }

    /**
     * Validate phone number
     */
    public static boolean isValidPhone(String phone) {
        validationCount++;
        boolean valid = phone != null && PHONE_PATTERN.matcher(phone).matches();
        if (valid) successCount++; else failureCount++;
        return valid;
    }

    /**
     * Validate email
     */
    public static boolean isValidEmail(String email) {
        validationCount++;
        boolean valid = email != null && EMAIL_PATTERN.matcher(email).matches();
        if (valid) successCount++; else failureCount++;
        return valid;
    }

    /**
     * Validate IFSC code
     */
    public static boolean isValidIFSC(String ifsc) {
        validationCount++;
        boolean valid = ifsc != null && IFSC_PATTERN.matcher(ifsc).matches();
        if (valid) successCount++; else failureCount++;
        return valid;
    }

    /**
     * Validate Pincode
     */
    public static boolean isValidPincode(String pincode) {
        validationCount++;
        boolean valid = pincode != null && PINCODE_PATTERN.matcher(pincode).matches();
        if (valid) successCount++; else failureCount++;
        return valid;
    }

    /**
     * Validate UPI ID
     */
    public static boolean isValidUPI(String upiId) {
        validationCount++;
        boolean valid = upiId != null && UPI_PATTERN.matcher(upiId).matches();
        if (valid) successCount++; else failureCount++;
        return valid;
    }

    /**
     * Validate Account Number
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        validationCount++;
        boolean valid = accountNumber != null && ACCOUNT_PATTERN.matcher(accountNumber).matches();
        if (valid) successCount++; else failureCount++;
        return valid;
    }

    /**
     * Validate Amount
     */
    public static boolean isValidAmount(double amount) {
        validationCount++;
        boolean valid = amount > 0;
        if (valid) successCount++; else failureCount++;
        return valid;
    }

    /**
     * Validate Amount within range
     */
    public static boolean isValidAmount(double amount, double min, double max) {
        validationCount++;
        boolean valid = amount >= min && amount <= max;
        if (valid) successCount++; else failureCount++;
        return valid;
    }

    /**
     * Validate Name
     */
    public static boolean isValidName(String name) {
        validationCount++;
        boolean valid = name != null && name.trim().length() >= 2 && name.matches("[a-zA-Z\\s]+");
        if (valid) successCount++; else failureCount++;
        return valid;
    }

    /**
     * Mask PAN for display
     */
    public static String maskPAN(String pan) {
        if (pan == null || pan.length() != 10) return pan;
        return pan.substring(0, 2) + "XXXX" + pan.substring(6);
    }

    /**
     * Mask Aadhar for display
     */
    public static String maskAadhar(String aadhar) {
        if (aadhar == null || aadhar.length() != 12) return aadhar;
        return "XXXX-XXXX-" + aadhar.substring(8);
    }

    /**
     * Mask Phone for display
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) return phone;
        return "XXXXXX" + phone.substring(phone.length() - 4);
    }

    /**
     * Mask Email for display
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        int atIndex = email.indexOf("@");
        if (atIndex <= 2) return email;
        return email.substring(0, 2) + "****" + email.substring(atIndex);
    }

    /**
     * Mask Account Number for display
     */
    public static String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 4) return accountNumber;
        return "XXXX" + accountNumber.substring(accountNumber.length() - 4);
    }

    /**
     * Get validation statistics
     */
    public static void displayStatistics() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║       VALIDATION UTILITY STATISTICS           ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("  Total Validations : " + validationCount);
        System.out.println("  Successful        : " + successCount);
        System.out.println("  Failed            : " + failureCount);
        double successRate = validationCount > 0 ?
                (successCount * 100.0 / validationCount) : 0;
        System.out.println("  Success Rate      : " + String.format("%.2f", successRate) + "%");
        System.out.println("╚═══════════════════════════════════════════════╝");
    }

    /**
     * Reset statistics
     */
    public static void resetStatistics() {
        validationCount = 0;
        successCount = 0;
        failureCount = 0;
    }
}