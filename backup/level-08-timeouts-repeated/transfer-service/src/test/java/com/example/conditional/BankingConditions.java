package com.example.conditional;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Banking-Specific Execution Conditions - LEVEL 7
 * 
 * ╔═══════════════════════════════════════════════════════════════╗
 * ║               CUSTOM EXECUTION CONDITIONS                     ║
 * ╠═══════════════════════════════════════════════════════════════╣
 * ║  Custom conditions for banking-specific test scenarios:       ║
 * ║                                                               ║
 * ║  @EnabledDuringBusinessHours  - 9 AM to 6 PM                  ║
 * ║  @EnabledOnBankingDays        - Monday to Saturday            ║
 * ║  @EnabledDuringRTGSHours      - 7 AM to 6 PM                  ║
 * ║  @EnabledDuringNEFTHours      - 8 AM to 7 PM                  ║
 * ║  @DisabledDuringMaintenance   - 2 AM to 4 AM excluded         ║
 * ╚═══════════════════════════════════════════════════════════════╝
 * 
 * BANKING CONTEXT:
 * ================
 * Different payment systems have different operating hours:
 * - RTGS: 7:00 AM to 6:00 PM on banking days
 * - NEFT: 8:00 AM to 7:00 PM (half-hourly batches)
 * - IMPS: 24x7 (but system maintenance windows exist)
 * - UPI: 24x7
 * 
 * @author NPCI Training Team
 * @version 1.0
 */
public class BankingConditions {

    // ═══════════════════════════════════════════════════════════
    // BUSINESS HOURS CONDITION (9 AM - 6 PM)
    // ═══════════════════════════════════════════════════════════

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ExtendWith(BusinessHoursCondition.class)
    public @interface EnabledDuringBusinessHours {
    }

    public static class BusinessHoursCondition implements ExecutionCondition {
        
        private static final LocalTime BUSINESS_START = LocalTime.of(9, 0);
        private static final LocalTime BUSINESS_END = LocalTime.of(18, 0);

        @Override
        public ConditionEvaluationResult evaluateExecutionCondition(
                ExtensionContext context) {
            
            LocalTime now = LocalTime.now();
            
            if (now.isAfter(BUSINESS_START) && now.isBefore(BUSINESS_END)) {
                return ConditionEvaluationResult.enabled(
                    "Within business hours (" + now + ")");
            } else {
                return ConditionEvaluationResult.disabled(
                    "Outside business hours (9 AM - 6 PM). Current: " + now);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // BANKING DAYS CONDITION (Monday - Saturday)
    // ═══════════════════════════════════════════════════════════

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ExtendWith(BankingDaysCondition.class)
    public @interface EnabledOnBankingDays {
    }

    public static class BankingDaysCondition implements ExecutionCondition {

        @Override
        public ConditionEvaluationResult evaluateExecutionCondition(
                ExtensionContext context) {
            
            DayOfWeek today = LocalDate.now().getDayOfWeek();
            
            if (today != DayOfWeek.SUNDAY) {
                return ConditionEvaluationResult.enabled(
                    "Banking day (" + today + ")");
            } else {
                return ConditionEvaluationResult.disabled(
                    "Sunday is not a banking day");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // RTGS HOURS CONDITION (7 AM - 6 PM on banking days)
    // ═══════════════════════════════════════════════════════════

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ExtendWith(RTGSHoursCondition.class)
    public @interface EnabledDuringRTGSHours {
    }

    public static class RTGSHoursCondition implements ExecutionCondition {
        
        private static final LocalTime RTGS_START = LocalTime.of(7, 0);
        private static final LocalTime RTGS_END = LocalTime.of(18, 0);

        @Override
        public ConditionEvaluationResult evaluateExecutionCondition(
                ExtensionContext context) {
            
            DayOfWeek today = LocalDate.now().getDayOfWeek();
            LocalTime now = LocalTime.now();
            
            if (today == DayOfWeek.SUNDAY) {
                return ConditionEvaluationResult.disabled(
                    "RTGS not available on Sundays");
            }
            
            if (now.isAfter(RTGS_START) && now.isBefore(RTGS_END)) {
                return ConditionEvaluationResult.enabled(
                    "Within RTGS hours (" + now + " on " + today + ")");
            } else {
                return ConditionEvaluationResult.disabled(
                    "Outside RTGS hours (7 AM - 6 PM). Current: " + now);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // NEFT HOURS CONDITION (8 AM - 7 PM on banking days)
    // ═══════════════════════════════════════════════════════════

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ExtendWith(NEFTHoursCondition.class)
    public @interface EnabledDuringNEFTHours {
    }

    public static class NEFTHoursCondition implements ExecutionCondition {
        
        private static final LocalTime NEFT_START = LocalTime.of(8, 0);
        private static final LocalTime NEFT_END = LocalTime.of(19, 0);

        @Override
        public ConditionEvaluationResult evaluateExecutionCondition(
                ExtensionContext context) {
            
            DayOfWeek today = LocalDate.now().getDayOfWeek();
            LocalTime now = LocalTime.now();
            
            if (today == DayOfWeek.SUNDAY) {
                return ConditionEvaluationResult.disabled(
                    "NEFT not available on Sundays");
            }
            
            if (now.isAfter(NEFT_START) && now.isBefore(NEFT_END)) {
                return ConditionEvaluationResult.enabled(
                    "Within NEFT hours (" + now + " on " + today + ")");
            } else {
                return ConditionEvaluationResult.disabled(
                    "Outside NEFT hours (8 AM - 7 PM). Current: " + now);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // MAINTENANCE WINDOW CONDITION (Skip 2 AM - 4 AM)
    // ═══════════════════════════════════════════════════════════

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ExtendWith(MaintenanceWindowCondition.class)
    public @interface DisabledDuringMaintenance {
    }

    public static class MaintenanceWindowCondition implements ExecutionCondition {
        
        private static final LocalTime MAINTENANCE_START = LocalTime.of(2, 0);
        private static final LocalTime MAINTENANCE_END = LocalTime.of(4, 0);

        @Override
        public ConditionEvaluationResult evaluateExecutionCondition(
                ExtensionContext context) {
            
            LocalTime now = LocalTime.now();
            
            boolean isMaintenance = now.isAfter(MAINTENANCE_START) 
                && now.isBefore(MAINTENANCE_END);
            
            if (isMaintenance) {
                return ConditionEvaluationResult.disabled(
                    "System maintenance window (2 AM - 4 AM). Current: " + now);
            } else {
                return ConditionEvaluationResult.enabled(
                    "Outside maintenance window (" + now + ")");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // WEEKDAY ONLY CONDITION (Monday - Friday)
    // ═══════════════════════════════════════════════════════════

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ExtendWith(WeekdayCondition.class)
    public @interface EnabledOnWeekdays {
    }

    public static class WeekdayCondition implements ExecutionCondition {

        @Override
        public ConditionEvaluationResult evaluateExecutionCondition(
                ExtensionContext context) {
            
            DayOfWeek today = LocalDate.now().getDayOfWeek();
            
            if (today != DayOfWeek.SATURDAY && today != DayOfWeek.SUNDAY) {
                return ConditionEvaluationResult.enabled(
                    "Weekday (" + today + ")");
            } else {
                return ConditionEvaluationResult.disabled(
                    "Weekend (" + today + ") - skipping weekday-only test");
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // HIGH-VALUE TRANSACTION HOURS (9 AM - 3 PM)
    // For transactions above ₹2 lakh requiring additional verification
    // ═══════════════════════════════════════════════════════════

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ExtendWith(HighValueHoursCondition.class)
    public @interface EnabledDuringHighValueHours {
    }

    public static class HighValueHoursCondition implements ExecutionCondition {
        
        private static final LocalTime HV_START = LocalTime.of(9, 0);
        private static final LocalTime HV_END = LocalTime.of(15, 0);

        @Override
        public ConditionEvaluationResult evaluateExecutionCondition(
                ExtensionContext context) {
            
            DayOfWeek today = LocalDate.now().getDayOfWeek();
            LocalTime now = LocalTime.now();
            
            if (today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY) {
                return ConditionEvaluationResult.disabled(
                    "High-value transactions not processed on weekends");
            }
            
            if (now.isAfter(HV_START) && now.isBefore(HV_END)) {
                return ConditionEvaluationResult.enabled(
                    "Within high-value processing hours (9 AM - 3 PM)");
            } else {
                return ConditionEvaluationResult.disabled(
                    "Outside high-value hours. Current: " + now);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // UPI 24x7 CONDITION (Always enabled, but log the status)
    // ═══════════════════════════════════════════════════════════

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ExtendWith(UPI24x7Condition.class)
    public @interface EnabledForUPI {
    }

    public static class UPI24x7Condition implements ExecutionCondition {

        @Override
        public ConditionEvaluationResult evaluateExecutionCondition(
                ExtensionContext context) {
            
            // UPI is 24x7, but we still check for maintenance
            LocalTime now = LocalTime.now();
            boolean isMaintenance = now.isAfter(LocalTime.of(2, 0)) 
                && now.isBefore(LocalTime.of(4, 0));
            
            if (isMaintenance) {
                // Even during maintenance, UPI usually works, but warn
                return ConditionEvaluationResult.enabled(
                    "UPI 24x7 enabled (Note: Running during typical maintenance window)");
            }
            
            return ConditionEvaluationResult.enabled(
                "UPI 24x7 available (" + now + ")");
        }
    }
}
