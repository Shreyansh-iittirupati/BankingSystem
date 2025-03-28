package Services;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import Dashboard.Account;
import Interfaces.AccountOperations;
import database.DatabaseConnector;

public class AccountService implements AccountOperations {
    private Connection connection;

    public AccountService() {
        this.connection = DatabaseConnector.getConnection();
    }

    public boolean createAccount(String userId, String accountNumber, BigDecimal initialBalance,
                                 String details, boolean isActive, String creationDate, String accountType) {
        String sql = "INSERT INTO accounts (user_id, account_number, balance, details, is_active, creation_date, account_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            statement.setString(2, accountNumber);
            statement.setBigDecimal(3, initialBalance);
            statement.setString(4, details);
            statement.setBoolean(5, isActive);
            statement.setString(6, creationDate);
            statement.setString(7, accountType);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
            return false;
        }
    }

    public Account getAccount(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getString("user_id"),
                        rs.getString("account_number"),
                        rs.getBigDecimal("balance"),
                        rs.getString("details"),
                        rs.getBoolean("is_active"),
                        rs.getString("creation_date"),
                        rs.getString("account_type")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving account: " + e.getMessage());
        }
        return null;
    }

    public List<Account> getAccountsByUserId(String userId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                accounts.add(new Account(
                        rs.getString("user_id"),
                        rs.getString("account_number"),
                        rs.getBigDecimal("balance"),
                        rs.getString("details"),
                        rs.getBoolean("is_active"),
                        rs.getString("creation_date"),
                        rs.getString("account_type")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving accounts: " + e.getMessage());
        }
        return accounts;
    }

    @Override
    public void updateAccount(String accountNumber, String newDetails) {
        String sql = "UPDATE accounts SET details = ? WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newDetails);
            statement.setString(2, accountNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating account: " + e.getMessage());
        }
    }

    @Override
    public void deleteAccount(String accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting account: " + e.getMessage());
        }
    }

    @Override
    public void suspendAccount(String accountNumber) {
        String sql = "UPDATE accounts SET is_active = false WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error suspending account: " + e.getMessage());
        }
    }

    @Override
    public void reactivateAccount(String accountNumber) {
        String sql = "UPDATE accounts SET is_active = true WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error reactivating account: " + e.getMessage());
        }
    }

    @Override
    public double getAccountBalance(String accountNumber) {
        String sql = "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving balance: " + e.getMessage());
        }
        return 0.0;
    }

    @Override
    public String getAccountDetails(String accountNumber) {
        String sql = "SELECT details FROM accounts WHERE account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getString("details");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving account details: " + e.getMessage());
        }
        return null;
    }
}