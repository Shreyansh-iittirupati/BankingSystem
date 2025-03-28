package database;

import java.sql.*;
import utils.Config;
import java.util.logging.*;

public class DatabaseConnector {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnector.class.getName());
    private static volatile Connection connection = null;

    private DatabaseConnector() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (DatabaseConnector.class) {
                if (connection == null) {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection(
                                Config.getDbUrl(),
                                Config.getDbUsername(),
                                Config.getDbPassword()
                        );
                        LOGGER.info("Database connection established successfully.");
                    } catch (ClassNotFoundException e) {
                        LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found. Ensure the MySQL connector is added to classpath.", e);
                        throw new RuntimeException("Database driver error", e);
                    } catch (SQLException e) {
                        LOGGER.log(Level.SEVERE, "Failed to establish database connection. Check credentials and network settings.", e);
                        throw new RuntimeException("Database connection error", e);
                    }
                }
            }
        }
        return connection;
    }

    public static void closeConnection() {
        synchronized (DatabaseConnector.class) {
            if (connection != null) {
                try {
                    connection.close();
                    connection = null;
                    LOGGER.info("Database connection closed successfully.");
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing database connection", e);
                }
            }
        }
    }
}
