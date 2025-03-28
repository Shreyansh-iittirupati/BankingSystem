package Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Interfaces.AdminControls;
import database.DatabaseConnector;
import utils.EncryptionUtil;

public class AdminService implements AdminControls {
    private Connection connection;

    public AdminService() {
        this.connection = DatabaseConnector.getConnection();
    }

    @Override
    public void addUser(String username, String password) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, EncryptionUtil.encryptPassword(password));
            statement.setString(3, "USER");
            statement.executeUpdate();
            System.out.println("User added: " + username);
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
        }
    }

    @Override
    public void updateUser(String username, String newDetails) {
        String sql = "UPDATE users SET details = ? WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newDetails);
            statement.setString(2, username);
            statement.executeUpdate();
            System.out.println("User updated: " + username);
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
            System.out.println("User deleted: " + username);
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public void monitorSystemLogs() {
        String sql = "SELECT * FROM system_logs ORDER BY timestamp DESC LIMIT 100";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            System.out.println("System Logs:");
            while (rs.next()) {
                System.out.println(rs.getString("timestamp") + " - " +
                        rs.getString("level") + " - " +
                        rs.getString("message"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching logs: " + e.getMessage());
        }
    }

    @Override
    public void assignRole(String username, String role) {
        String sql = "UPDATE users SET role = ? WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, role);
            statement.setString(2, username);
            statement.executeUpdate();
            System.out.println("Assigned role " + role + " to user: " + username);
        } catch (SQLException e) {
            System.err.println("Error assigning role: " + e.getMessage());
        }
    }
}