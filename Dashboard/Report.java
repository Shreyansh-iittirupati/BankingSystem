package Dashboard;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Report {
    private String reportId;
    private ReportType reportType;
    private LocalDateTime generatedAt;
    private LocalDateTime reportStartDate;
    private LocalDateTime reportEndDate;

    // Financial Summary Details
    private int totalAccounts;
    private int totalUsers;
    private BigDecimal totalBalance;
    private int todayTransactionCount;
    private BigDecimal todayTotalDeposits;
    private BigDecimal todayTotalWithdrawals;
    private BigDecimal todayTotalTransfers;

    // List to store detailed transactions (if needed)
    private List<TransactionSummary> transactions;

    // Enum for report types
    public enum ReportType {
        FINANCIAL_SUMMARY,
        TRANSACTION_REPORT,
        ACCOUNT_STATEMENT,
        CUSTOM_REPORT
    }

    // Inner class for transaction summary
    public static class TransactionSummary {
        private String transactionId;
        private String fromAccountId;
        private String toAccountId;
        private BigDecimal amount;
        private String type;
        private String status;
        private LocalDateTime timestamp;

        public TransactionSummary(String transactionId, String fromAccountId, String toAccountId,
                                  BigDecimal amount, String type, String status, LocalDateTime timestamp) {
            this.transactionId = transactionId;
            this.fromAccountId = fromAccountId;
            this.toAccountId = toAccountId;
            this.amount = amount;
            this.type = type;
            this.status = status;
            this.timestamp = timestamp;
        }

        // Getters
        public String getTransactionId() { return transactionId; }
        public String getFromAccountId() { return fromAccountId; }
        public String getToAccountId() { return toAccountId; }
        public BigDecimal getAmount() { return amount; }
        public String getType() { return type; }
        public String getStatus() { return status; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    // Constructor
    public Report(String reportId, ReportType reportType) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.generatedAt = LocalDateTime.now();
        this.transactions = new ArrayList<>();
    }

    // Comprehensive constructor for financial summary
    public Report(String reportId, ReportType reportType,
                  int totalAccounts, int totalUsers, BigDecimal totalBalance,
                  int todayTransactionCount, BigDecimal todayTotalDeposits,
                  BigDecimal todayTotalWithdrawals, BigDecimal todayTotalTransfers) {
        this(reportId, reportType);
        this.totalAccounts = totalAccounts;
        this.totalUsers = totalUsers;
        this.totalBalance = totalBalance;
        this.todayTransactionCount = todayTransactionCount;
        this.todayTotalDeposits = todayTotalDeposits;
        this.todayTotalWithdrawals = todayTotalWithdrawals;
        this.todayTotalTransfers = todayTotalTransfers;
    }

    // Method to add transaction to the report
    public void addTransaction(TransactionSummary transaction) {
        this.transactions.add(transaction);
    }

    // Method to generate a formatted report string
    public String generateReportString() {
        StringBuilder reportBuilder = new StringBuilder();

        reportBuilder.append("Financial Report\n")
                .append("Report ID: ").append(reportId).append("\n")
                .append("Generated on: ").append(generatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n\n")
                .append("Total Accounts: ").append(totalAccounts).append("\n")
                .append("Total Users: ").append(totalUsers).append("\n")
                .append("Total Balance: ").append(totalBalance).append("\n")
                .append("Today's Total Transactions: ").append(todayTransactionCount).append("\n")
                .append("Today's Total Deposits: ").append(todayTotalDeposits).append("\n")
                .append("Today's Total Withdrawals: ").append(todayTotalWithdrawals).append("\n")
                .append("Today's Total Transfers: ").append(todayTotalTransfers).append("\n");

        if (!transactions.isEmpty()) {
            reportBuilder.append("\nTransaction Details:\n");
            for (TransactionSummary transaction : transactions) {
                reportBuilder.append(String.format("ID: %s, From: %s, To: %s, Amount: %.2f, Type: %s, Status: %s, Time: %s\n",
                        transaction.getTransactionId(),
                        transaction.getFromAccountId(),
                        transaction.getToAccountId(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getStatus(),
                        transaction.getTimestamp()));
            }
        }

        return reportBuilder.toString();
    }

    // Getters
    public String getReportId() { return reportId; }
    public ReportType getReportType() { return reportType; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public int getTotalAccounts() { return totalAccounts; }
    public int getTotalUsers() { return totalUsers; }
    public BigDecimal getTotalBalance() { return totalBalance; }
    public int getTodayTransactionCount() { return todayTransactionCount; }
    public BigDecimal getTodayTotalDeposits() { return todayTotalDeposits; }
    public BigDecimal getTodayTotalWithdrawals() { return todayTotalWithdrawals; }
    public BigDecimal getTodayTotalTransfers() { return todayTotalTransfers; }
    public List<TransactionSummary> getTransactions() { return new ArrayList<>(transactions); }

    // ToString method
    @Override
    public String toString() {
        return generateReportString();
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Report report = (Report) o;
        return reportId.equals(report.reportId);
    }

    @Override
    public int hashCode() {
        return reportId.hashCode();
    }
}