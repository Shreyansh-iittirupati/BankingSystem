package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Interfaces.Logger;
import Dashboard.Customer;

public class AuditLogger implements Logger {
    private static final String LOGS_DIRECTORY = "logs/audit/";
    private PrintWriter fileLogger;
    private static AuditLogger instance;

    public enum AuditEventType {
        LOGIN,
        LOGOUT,
        ACCOUNT_CREATION,
        ACCOUNT_UPDATE,
        PASSWORD_CHANGE,
        TRANSACTION,
        UPI_TRANSACTION,
        FAILED_TRANSACTION,
        SECURITY_EVENT,
        ADMIN_ACTION,
        SYSTEM_ERROR,
        UNAUTHORIZED_ACCESS
    }

    private AuditLogger() {
        File logsDir = new File(LOGS_DIRECTORY);
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        try {
            String logFileName = LOGS_DIRECTORY + "audit_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".log";
            this.fileLogger = new PrintWriter(new FileWriter(logFileName, true));
        } catch (IOException e) {
            System.err.println("Could not create audit log file: " + e.getMessage());
        }
    }

    public static synchronized AuditLogger getInstance() {
        if (instance == null) {
            instance = new AuditLogger();
        }
        return instance;
    }

    @Override
    public void log(String message) {
        logEvent(AuditEventType.SYSTEM_ERROR, message);
    }

    public void logEvent(Customer customer, AuditEventType eventType, String description) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String logMessage = String.format(
                "[%s] Customer: %s (ID: %s) | Event: %s | Description: %s",
                timestamp,
                customer != null ? customer.getUsername() : "SYSTEM",
                customer != null ? customer.getUserId() : "N/A",
                eventType,
                description
        );

        System.out.println(logMessage);

        if (fileLogger != null) {
            fileLogger.println(logMessage);
            fileLogger.flush();
        }
    }

    public void logEvent(AuditEventType eventType, String description) {
        logEvent(null, eventType, description);
    }

    public void logSecurityEvent(AuditEventType eventType, String ipAddress, String description) {
        String securityLog = String.format("IP: %s | %s", ipAddress, description);
        logEvent(eventType, securityLog);
    }

    public void logError(String errorMessage, Throwable throwable) {
        String errorLog = errorMessage +
                (throwable != null ? " | Exception: " + throwable.getClass().getSimpleName() +
                        " | Message: " + throwable.getMessage() : "");

        logEvent(AuditEventType.SYSTEM_ERROR, errorLog);

        if (throwable != null) {
            throwable.printStackTrace();
        }
    }

    public void logFailedTransaction(Customer customer, String reason) {
        logEvent(customer, AuditEventType.FAILED_TRANSACTION, "Transaction failed. Reason: " + reason);
    }

    public void logUnauthorizedAccess(String ipAddress, String attemptedAction) {
        logSecurityEvent(AuditEventType.UNAUTHORIZED_ACCESS, ipAddress, "Unauthorized access attempt: " + attemptedAction);
    }

    public void close() {
        if (fileLogger != null) {
            fileLogger.close();
        }
    }
}
