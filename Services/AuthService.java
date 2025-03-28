
package Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DatabaseConnector;
import Interfaces.Authenticator;
import utils.EncryptionUtil;

public class AuthService implements Authenticator {
    private Connection connection;

    public AuthService() {
        this.connection = DatabaseConnector.getConnection();
    }

    @Override
    public boolean authenticate(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return EncryptionUtil.verifyPassword(password, storedPassword);
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, EncryptionUtil.encryptPassword(password));
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
        }
    }

    @Override
    public void resetPassword(String username) {
        String temporaryPassword = EncryptionUtil.generateRandomPassword();
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, EncryptionUtil.encryptPassword(temporaryPassword));
            statement.setString(2, username);
            statement.executeUpdate();

            System.out.println("Password reset for " + username + ". Temporary password: " + temporaryPassword);
        } catch (SQLException e) {
            System.err.println("Password reset error: " + e.getMessage());
        }
    }
}