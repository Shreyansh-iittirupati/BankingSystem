package Services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import Dashboard.Transaction;
import Interfaces.TransactionOperations;
import database.DatabaseConnector;

public class TransactionService implements TransactionOperations {
    private Connection connection;
    private AccountService accountService;

    public TransactionService() {
        this.connection = DatabaseConnector.getConnection();
        this.accountService = new AccountService();
    }

    public String createTransaction(String fromAccountId, String toAccountId,
                                    BigDecimal amount, String type, String description) {
        String transactionId = UUID.randomUUID().toString();
        String sql = "INSERT INTO transactions (transaction_id, from_account_id, to_account_id, " +
                "amount, type, status, timestamp, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, transactionId);
            statement.setString(2, fromAccountId);
            statement.setString(3, toAccountId);
            statement.setBigDecimal(4, amount);
            statement.setString(5, type);
            statement.setString(6, "PENDING");
            statement.setObject(7, LocalDateTime.now());
            statement.setString(8, description);

            statement.executeUpdate();
            return transactionId;
        } catch (SQLException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            return null;
        }
    }

    public boolean updateTransactionStatus(String transactionId, String status) {
        String sql = "UPDATE transactions SET status = ? WHERE transaction_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setString(2, transactionId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating transaction status: " + e.getMessage());
            return false;
        }
    }

    public Transaction getTransaction(String transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, transactionId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Transaction(
                        rs.getString("transaction_id"),
                        rs.getString("from_account_id"),
                        rs.getString("to_account_id"),
                        rs.getBigDecimal("amount"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getObject("timestamp", LocalDateTime.class),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transaction: " + e.getMessage());
        }
        return null;
    }

    public List<Transaction> getTransactionsByAccountId(String accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE from_account_id = ? OR to_account_id = ? ORDER BY timestamp DESC";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountId);
            statement.setString(2, accountId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getString("transaction_id"),
                        rs.getString("from_account_id"),
                        rs.getString("to_account_id"),
                        rs.getBigDecimal("amount"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getObject("timestamp", LocalDateTime.class),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions: " + e.getMessage());
        }
        return transactions;
    }

    @Override
    public void depositFunds(String accountNumber, double amount) {
        try {
            connection.setAutoCommit(false);
            String transactionId = createTransaction(
                    "DEPOSIT",
                    accountNumber,
                    BigDecimal.valueOf(amount),
                    "DEPOSIT",
                    "Deposit transaction"
            );

            if (transactionId == null) {
                throw new SQLException("Failed to create transaction record");
            }

            // 2. Update account balance
            String updateSql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                updateStmt.setBigDecimal(1, BigDecimal.valueOf(amount));
                updateStmt.setString(2, accountNumber);
                int updated = updateStmt.executeUpdate();

                if (updated == 0) {
                    throw new SQLException("Account not found: " + accountNumber);
                }
            }

            updateTransactionStatus(transactionId, "COMPLETED");

            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Rollback error: " + ex.getMessage());
            }
            System.err.println("Deposit error: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    @Override
    public void withdrawFunds(String accountNumber, double amount) {
        try {
            connection.setAutoCommit(false);

            double currentBalance = accountService.getAccountBalance(accountNumber);
            if (currentBalance < amount) {
                throw new SQLException("Insufficient funds");
            }

            String transactionId = createTransaction(
                    accountNumber,
                    "WITHDRAWAL",
                    BigDecimal.valueOf(amount),
                    "WITHDRAWAL",
                    "Withdrawal transaction"
            );

            if (transactionId == null) {
                throw new SQLException("Failed to create transaction record");
            }

            String updateSql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                updateStmt.setBigDecimal(1, BigDecimal.valueOf(amount));
                updateStmt.setString(2, accountNumber);
                int updated = updateStmt.executeUpdate();

                if (updated == 0) {
                    throw new SQLException("Account not found: " + accountNumber);
                }
            }

            updateTransactionStatus(transactionId, "COMPLETED");

            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Rollback error: " + ex.getMessage());
            }
            System.err.println("Withdrawal error: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    @Override
    public void transferFunds(String fromAccount, String toAccount, double amount) {
        try {
            connection.setAutoCommit(false);

            double currentBalance = accountService.getAccountBalance(fromAccount);
            if (currentBalance < amount) {
                throw new SQLException("Insufficient funds");
            }

            String transactionId = createTransaction(
                    fromAccount,
                    toAccount,
                    BigDecimal.valueOf(amount),
                    "TRANSFER",
                    "Transfer transaction"
            );

            if (transactionId == null) {
                throw new SQLException("Failed to create transaction record");
            }

            String debitSql = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
            try (PreparedStatement debitStmt = connection.prepareStatement(debitSql)) {
                debitStmt.setBigDecimal(1, BigDecimal.valueOf(amount));
                debitStmt.setString(2, fromAccount);
                int updated = debitStmt.executeUpdate();

                if (updated == 0) {
                    throw new SQLException("Source account not found: " + fromAccount);
                }
            }

            String creditSql = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            try (PreparedStatement creditStmt = connection.prepareStatement(creditSql)) {
                creditStmt.setBigDecimal(1, BigDecimal.valueOf(amount));
                creditStmt.setString(2, toAccount);
                int updated = creditStmt.executeUpdate();

                if (updated == 0) {
                    throw new SQLException("Destination account not found: " + toAccount);
                }
            }

            updateTransactionStatus(transactionId, "COMPLETED");

            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Rollback error: " + ex.getMessage());
            }
            System.err.println("Transfer error: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
}