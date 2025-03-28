package Services;

import java.math.BigDecimal;
import java.sql.*;
import database.DatabaseConnector;
import Interfaces.PaymentProcessor;

public class UPIService implements PaymentProcessor {
    private Connection connection;
    private TransactionService transactionService;

    public UPIService() {
        this.connection = DatabaseConnector.getConnection();
        this.transactionService = new TransactionService();
    }

    public String registerUPIId(String accountNumber, String upiId) {
        String sql = "INSERT INTO upi_mappings (account_number, upi_id, is_active) VALUES (?, ?, true)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.setString(2, upiId);
            statement.executeUpdate();
            return upiId;
        } catch (SQLException e) {
            System.err.println("Error registering UPI ID: " + e.getMessage());
            return null;
        }
    }

    public String getAccountByUPI(String upiId) {
        String sql = "SELECT account_number FROM upi_mappings WHERE upi_id = ? AND is_active = true";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, upiId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString("account_number");
            }
        } catch (SQLException e) {
            System.err.println("Error finding account by UPI: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean validateUPIDetails(String upiId) {
        String sql = "SELECT COUNT(*) FROM upi_mappings WHERE upi_id = ? AND is_active = true";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, upiId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error validating UPI: " + e.getMessage());
        }
        return false;
    }

    @Override
    public String checkPaymentStatus(String transactionId) {
        String sql = "SELECT status FROM transactions WHERE transaction_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, transactionId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            System.err.println("Error checking payment status: " + e.getMessage());
        }
        return "UNKNOWN";
    }

    @Override
    public void processPayment(String fromAccount, String toAccount, double amount) {
        if (fromAccount.contains("@")) {
            fromAccount = getAccountByUPI(fromAccount);
        }

        if (toAccount.contains("@")) {
            toAccount = getAccountByUPI(toAccount);
        }

        if (fromAccount == null || toAccount == null) {
            System.err.println("Invalid UPI ID provided");
            return;
        }

        transactionService.transferFunds(fromAccount, toAccount, amount);
    }

    public String processUPIPayment(String fromUPIId, String toUPIId, BigDecimal amount, String description) {
        if (!validateUPIDetails(fromUPIId) || !validateUPIDetails(toUPIId)) {
            return "INVALID_UPI";
        }

        String fromAccount = getAccountByUPI(fromUPIId);
        String toAccount = getAccountByUPI(toUPIId);

        if (fromAccount == null || toAccount == null) {
            return "INVALID_ACCOUNT";
        }

        String transactionId = transactionService.createTransaction(
                fromAccount,
                toAccount,
                amount,
                "UPI_TRANSFER",
                description
        );

        if (transactionId != null) {
            transactionService.transferFunds(fromAccount, toAccount, amount.doubleValue());
            return transactionId;
        }

        return "FAILED";
    }
}