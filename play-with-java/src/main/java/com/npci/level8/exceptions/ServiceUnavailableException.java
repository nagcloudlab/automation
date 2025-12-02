package com.npci.level8.exceptions;

/**
 * Level 8: Exception Handling - Service Unavailable Exception
 *
 * Thrown when a banking service is temporarily unavailable.
 */
public class ServiceUnavailableException extends BankingException {

    private String serviceName;
    private String reason;
    private long expectedDowntimeMinutes;
    private boolean isScheduledMaintenance;

    public ServiceUnavailableException(String serviceName) {
        super("Service unavailable: " + serviceName, "BANK_ERR_007");
        this.serviceName = serviceName;
        this.reason = "Service temporarily unavailable";
    }

    public ServiceUnavailableException(String serviceName, String reason) {
        super("Service unavailable: " + serviceName + " - " + reason, "BANK_ERR_007");
        this.serviceName = serviceName;
        this.reason = reason;
    }

    public ServiceUnavailableException(String serviceName, String reason,
                                       long expectedDowntimeMinutes, boolean isScheduled) {
        super(buildMessage(serviceName, reason, expectedDowntimeMinutes, isScheduled), "BANK_ERR_007");
        this.serviceName = serviceName;
        this.reason = reason;
        this.expectedDowntimeMinutes = expectedDowntimeMinutes;
        this.isScheduledMaintenance = isScheduled;
    }

    private static String buildMessage(String service, String reason, long minutes, boolean scheduled) {
        String type = scheduled ? "Scheduled maintenance" : "Unplanned outage";
        return String.format("%s: %s - %s. Expected duration: %d minutes", type, service, reason, minutes);
    }

    public String getServiceName() { return serviceName; }
    public String getReason() { return reason; }
    public long getExpectedDowntimeMinutes() { return expectedDowntimeMinutes; }
    public boolean isScheduledMaintenance() { return isScheduledMaintenance; }

    @Override
    public String getDetailedMessage() {
        return String.format(
                "╔═══════════════════════════════════════════════╗\n" +
                        "║      SERVICE UNAVAILABLE ERROR                ║\n" +
                        "╠═══════════════════════════════════════════════╣\n" +
                        "  Error Code       : %s\n" +
                        "  Service          : %s\n" +
                        "  Reason           : %s\n" +
                        "  Type             : %s\n" +
                        "  Expected Duration: %d minutes\n" +
                        "╚═══════════════════════════════════════════════╝",
                getErrorCode(), serviceName, reason,
                isScheduledMaintenance ? "Scheduled Maintenance" : "Unplanned Outage",
                expectedDowntimeMinutes);
    }
}