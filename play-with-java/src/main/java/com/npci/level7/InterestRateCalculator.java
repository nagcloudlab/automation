package com.npci.level7;

/**
 * Level 7: Static Members - Interest Rate Calculator
 *
 * Pure utility class with static methods for interest calculations.
 */
public class InterestRateCalculator {

    // ═══════════════════════════════════════════════════════════════
    // STATIC CONSTANTS
    // ═══════════════════════════════════════════════════════════════

    public static final int DAYS_IN_YEAR = 365;
    public static final int MONTHS_IN_YEAR = 12;
    public static final int QUARTERS_IN_YEAR = 4;

    // ═══════════════════════════════════════════════════════════════
    // PRIVATE CONSTRUCTOR
    // ═══════════════════════════════════════════════════════════════

    private InterestRateCalculator() {
        throw new UnsupportedOperationException("Utility class");
    }

    // ═══════════════════════════════════════════════════════════════
    // STATIC METHODS - Interest Calculations
    // ═══════════════════════════════════════════════════════════════

    /**
     * Calculate Simple Interest
     * SI = P × R × T / 100
     */
    public static double calculateSimpleInterest(double principal, double rate, double timeInYears) {
        return principal * rate * timeInYears / 100;
    }

    /**
     * Calculate Simple Interest (with months)
     */
    public static double calculateSimpleInterestMonths(double principal, double rate, int months) {
        double timeInYears = months / 12.0;
        return calculateSimpleInterest(principal, rate, timeInYears);
    }

    /**
     * Calculate Simple Interest (with days)
     */
    public static double calculateSimpleInterestDays(double principal, double rate, int days) {
        double timeInYears = days / 365.0;
        return calculateSimpleInterest(principal, rate, timeInYears);
    }

    /**
     * Calculate Compound Interest (yearly compounding)
     * A = P × (1 + R/100)^T
     * CI = A - P
     */
    public static double calculateCompoundInterest(double principal, double rate, int years) {
        double amount = principal * Math.pow((1 + rate / 100), years);
        return amount - principal;
    }

    /**
     * Calculate Compound Interest (quarterly compounding)
     */
    public static double calculateCompoundInterestQuarterly(double principal, double rate, int years) {
        int n = QUARTERS_IN_YEAR;
        double amount = principal * Math.pow((1 + rate / (100 * n)), n * years);
        return amount - principal;
    }

    /**
     * Calculate Compound Interest (monthly compounding)
     */
    public static double calculateCompoundInterestMonthly(double principal, double rate, int years) {
        int n = MONTHS_IN_YEAR;
        double amount = principal * Math.pow((1 + rate / (100 * n)), n * years);
        return amount - principal;
    }

    /**
     * Calculate maturity amount for FD
     */
    public static double calculateFDMaturityAmount(double principal, double rate, int months) {
        // Quarterly compounding for FD
        double years = months / 12.0;
        return principal + calculateCompoundInterestQuarterly(principal, rate, (int) Math.ceil(years));
    }

    /**
     * Calculate EMI for loan
     * EMI = [P × R × (1+R)^N] / [(1+R)^N – 1]
     */
    public static double calculateEMI(double principal, double annualRate, int tenureMonths) {
        double monthlyRate = annualRate / (12 * 100);
        double emi = principal * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths) /
                (Math.pow(1 + monthlyRate, tenureMonths) - 1);
        return Math.round(emi * 100.0) / 100.0;
    }

    /**
     * Calculate total interest on loan
     */
    public static double calculateLoanInterest(double principal, double annualRate, int tenureMonths) {
        double emi = calculateEMI(principal, annualRate, tenureMonths);
        double totalPayment = emi * tenureMonths;
        return totalPayment - principal;
    }

    /**
     * Calculate effective annual rate
     */
    public static double calculateEffectiveAnnualRate(double nominalRate, int compoundingPerYear) {
        return (Math.pow(1 + nominalRate / (100 * compoundingPerYear), compoundingPerYear) - 1) * 100;
    }

    /**
     * Display interest calculation details
     */
    public static void displayInterestDetails(double principal, double rate, int years, String type) {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║        INTEREST CALCULATION DETAILS           ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("  Principal         : Rs." + String.format("%,.2f", principal));
        System.out.println("  Rate              : " + rate + "% p.a.");
        System.out.println("  Duration          : " + years + " year(s)");
        System.out.println("├───────────────────────────────────────────────┤");

        double si = calculateSimpleInterest(principal, rate, years);
        double ciYearly = calculateCompoundInterest(principal, rate, years);
        double ciQuarterly = calculateCompoundInterestQuarterly(principal, rate, years);
        double ciMonthly = calculateCompoundInterestMonthly(principal, rate, years);

        System.out.println("  Simple Interest   : Rs." + String.format("%,.2f", si));
        System.out.println("  CI (Yearly)       : Rs." + String.format("%,.2f", ciYearly));
        System.out.println("  CI (Quarterly)    : Rs." + String.format("%,.2f", ciQuarterly));
        System.out.println("  CI (Monthly)      : Rs." + String.format("%,.2f", ciMonthly));
        System.out.println("├───────────────────────────────────────────────┤");
        System.out.println("  MATURITY AMOUNTS:");
        System.out.println("  Simple            : Rs." + String.format("%,.2f", principal + si));
        System.out.println("  Compound (Q)      : Rs." + String.format("%,.2f", principal + ciQuarterly));
        System.out.println("╚═══════════════════════════════════════════════╝");
    }

    /**
     * Display EMI calculation details
     */
    public static void displayEMIDetails(double principal, double rate, int tenureMonths) {
        double emi = calculateEMI(principal, rate, tenureMonths);
        double totalInterest = calculateLoanInterest(principal, rate, tenureMonths);
        double totalPayment = emi * tenureMonths;

        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║           EMI CALCULATION DETAILS             ║");
        System.out.println("╠═══════════════════════════════════════════════╣");
        System.out.println("  Loan Amount       : Rs." + String.format("%,.2f", principal));
        System.out.println("  Interest Rate     : " + rate + "% p.a.");
        System.out.println("  Tenure            : " + tenureMonths + " months");
        System.out.println("├───────────────────────────────────────────────┤");
        System.out.println("  Monthly EMI       : Rs." + String.format("%,.2f", emi));
        System.out.println("  Total Interest    : Rs." + String.format("%,.2f", totalInterest));
        System.out.println("  Total Payment     : Rs." + String.format("%,.2f", totalPayment));
        System.out.println("╚═══════════════════════════════════════════════╝");
    }
}