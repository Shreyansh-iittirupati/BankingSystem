package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
    private static final String CONFIG_FILE = "config/database.properties";
    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());
    private static Properties properties = new Properties();

    private static void loadProperties() {
        if (properties.isEmpty()) {
            try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
                properties.load(fis);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error loading config file: " + CONFIG_FILE, e);
            }
        }
    }

    public static String getDbUrl() {
        loadProperties();
        return properties.getProperty("db.url", "jdbc:mysql://localhost:3306/bank_management");
    }

    public static String getDbUsername() {
        loadProperties();
        return properties.getProperty("db.username", "root");
    }

    public static String getDbPassword() {
        loadProperties();
        return properties.getProperty("db.password", "");
    }
}
