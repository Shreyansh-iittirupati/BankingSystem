package Services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Dashboard.Report;
import Interfaces.ReportGenerator;
import database.DatabaseConnector;

public class ReportService implements ReportGenerator {
    private Connection connection;
    private static final String REPORTS_DIRECTORY = "reports/";

    public static class FinancialSummary {
        public int totalAccounts;
        public int totalUsers;
        public BigDecimal totalBalance;
        public int todayTransactionCount;
        public BigDecimal todayTotalDeposits;
        public BigDecimal todayTotalWithdrawals;
        public BigDecimal todayTotalTransfers;

        public void print() {
            System.out.println("Financial Summary:");
            System.out.println("Total Accounts: " + totalAccounts);
            System.out.println("Total Users: " + totalUsers);
            System.out.println("Total Balance: " + totalBalance);
            System.out.println("Today's Transactions: " + todayTransactionCount);
            System.out.println("Today's Deposits: " + todayTotalDeposits);
            System.out.println("Today's Withdrawals: " + todayTotalWithdrawals);
            System.out.println("Today's Transfers: " + todayTotalTransfers);
        }
    }

    public ReportService() {
        this.connection = DatabaseConnector.getConnection();
        new File(REPORTS_DIRECTORY).mkdirs();
    }

    @Override
    public void generateFinancialSummary() {
        try {
            FinancialSummary summary = new FinancialSummary();


            summary.totalAccounts = getTotalAccounts();
            summary.totalUsers = getTotalUsers();
            summary.totalBalance = getTotalBalance();

            populateTransactionSummary(summary);

            summary.print();

            generateReportFile(summary);

        } catch (SQLException e) {
            System.err.println("Error generating financial summary: " + e.getMessage());
        }
    }

    private int getTotalAccounts() throws SQLException {
        String sql = "SELECT COUNT(*) AS total_accounts FROM accounts";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt("total_accounts") : 0;
        }
    }

    private int getTotalUsers() throws SQLException {
        String sql = "SELECT COUNT(*) AS total_users FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt("total_users") : 0;
        }
    }

    private BigDecimal getTotalBalance() throws SQLException {
        String sql = "SELECT SUM(balance) AS total_balance FROM accounts";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getBigDecimal("total_balance") : BigDecimal.ZERO;
        }
    }

    private void populateTransactionSummary(FinancialSummary summary) throws SQLException {
        String todaySql = "SELECT " +
                "COUNT(*) AS today_transaction_count, " +
                "SUM(CASE WHEN type = 'DEPOSIT' THEN amount ELSE 0 END) AS total_deposits, " +
                "SUM(CASE WHEN type = 'WITHDRAWAL' THEN amount ELSE 0 END) AS total_withdrawals, " +
                "SUM(CASE WHEN type = 'TRANSFER' THEN amount ELSE 0 END) AS total_transfers " +
                "FROM transactions WHERE DATE(timestamp) = CURRENT_DATE";

        try (PreparedStatement stmt = connection.prepareStatement(todaySql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                summary.todayTransactionCount = rs.getInt("today_transaction_count");
                summary.todayTotalDeposits = rs.getBigDecimal("total_deposits");
                summary.todayTotalWithdrawals = rs.getBigDecimal("total_withdrawals");
                summary.todayTotalTransfers = rs.getBigDecimal("total_transfers");
            }
        }
    }

    private void generateReportFile(FinancialSummary summary) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = REPORTS_DIRECTORY + "financial_summary_" + timestamp + ".txt";

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Financial Report\n");
            writer.write("Generated on: " + LocalDateTime.now() + "\n\n");

            writer.write("Total Accounts: " + summary.totalAccounts + "\n");
            writer.write("Total Users: " + summary.totalUsers + "\n");
            writer.write("Total Balance: " + summary.totalBalance + "\n");
            writer.write("Today's Total Transactions: " + summary.todayTransactionCount + "\n");
            writer.write("Today's Total Deposits: " + summary.todayTotalDeposits + "\n");
            writer.write("Today's Total Withdrawals: " + summary.todayTotalWithdrawals + "\n");
            writer.write("Today's Total Transfers: " + summary.todayTotalTransfers + "\n");

            System.out.println("Report generated: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing report file: " + e.getMessage());
        }
    }

    @Override
    public void exportReport(String format) {
        switch (format.toUpperCase()) {
            case "PDF":
                exportPDFReport();
                break;
            case "CSV":
                exportCSVReport();
                break;
            default:
                System.err.println("Unsupported report format: " + format);
        }
    }

    private void exportPDFReport() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = REPORTS_DIRECTORY + "comprehensive_report_" + timestamp + ".pdf";

            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("Comprehensive Bank Report\n");
                writer.write("This is a simulated PDF export.\n");
            }

            System.out.println("PDF Report exported: " + filename);
        } catch (IOException e) {
            System.err.println("Error exporting PDF report: " + e.getMessage());
        }
    }

    private void exportCSVReport() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = REPORTS_DIRECTORY + "transaction_report_" + timestamp + ".csv";

            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("Transaction ID,From Account,To Account,Amount,Type,Status,Timestamp\n");

                String sql = "SELECT * FROM transactions ORDER BY timestamp DESC LIMIT 1000";
                try (PreparedStatement stmt = connection.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        writer.write(String.format("%s,%s,%s,%.2f,%s,%s,%s\n",
                                rs.getString("transaction_id"),
                                rs.getString("from_account_id"),
                                rs.getString("to_account_id"),
                                rs.getDouble("amount"),
                                rs.getString("type"),
                                rs.getString("status"),
                                rs.getTimestamp("timestamp")
                        ));
                    }
                }
            }

            System.out.println("CSV Report exported: " + filename);
        } catch (SQLException | IOException e) {
            System.err.println("Error exporting CSV report: " + e.getMessage());
        }
    }
}