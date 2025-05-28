package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Reader class for managing application properties
 */
public class ConfigReader {
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String CONFIG_FILE = "config.properties";

    static {
        loadProperties();
    }

    /**
     * Load properties from config file
     */
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Configuration properties loaded successfully");
            } else {
                logger.warn("Configuration file '{}' not found. Using default values.", CONFIG_FILE);
                setDefaultProperties();
            }
        } catch (IOException e) {
            logger.error("Error loading configuration properties", e);
            setDefaultProperties();
        }
    }

    /**
     * Set default properties if config file is not found
     */
    private static void setDefaultProperties() {
        properties.setProperty("base.url", "https://www.demoblaze.com");
        properties.setProperty("browser", "chrome");
        properties.setProperty("implicit.wait", "10");
        properties.setProperty("explicit.wait", "10");
        properties.setProperty("page.load.timeout", "30");
        properties.setProperty("test.data.file", "testdata.json");
        logger.info("Default properties set");
    }

    /**
     * Get property value by key
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property '{}' not found", key);
            return "";
        }
        return value;
    }

    /**
     * Get property value with default fallback
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get base URL for the application
     */
    public static String getBaseUrl() {
        return getProperty("base.url", "https://www.demoblaze.com");
    }

    /**
     * Get browser name for testing
     */
    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }

    /**
     * Get implicit wait timeout
     */
    public static int getImplicitWait() {
        try {
            return Integer.parseInt(getProperty("implicit.wait", "10"));
        } catch (NumberFormatException e) {
            logger.warn("Invalid implicit wait value, using default: 10");
            return 10;
        }
    }

    /**
     * Get explicit wait timeout
     */
    public static int getExplicitWait() {
        try {
            return Integer.parseInt(getProperty("explicit.wait", "10"));
        } catch (NumberFormatException e) {
            logger.warn("Invalid explicit wait value, using default: 10");
            return 10;
        }
    }

    /**
     * Get page load timeout
     */
    public static int getPageLoadTimeout() {
        try {
            return Integer.parseInt(getProperty("page.load.timeout", "30"));
        } catch (NumberFormatException e) {
            logger.warn("Invalid page load timeout value, using default: 30");
            return 30;
        }
    }

    /**
     * Get test data file path
     */
    public static String getTestDataFile() {
        return getProperty("test.data.file", "testdata.json");
    }

    /**
     * Get reports directory
     */
    public static String getReportsDirectory() {
        return getProperty("reports.directory", "test-output/reports");
    }

    /**
     * Get screenshots directory
     */
    public static String getScreenshotsDirectory() {
        return getProperty("screenshots.directory", "test-output/screenshots");
    }

    /**
     * Check if headless mode is enabled
     */
    public static boolean isHeadlessMode() {
        return Boolean.parseBoolean(getProperty("headless.mode", "false"));
    }

    /**
     * Get environment (dev, staging, prod)
     */
    public static String getEnvironment() {
        return getProperty("environment", "prod");
    }

    /**
     * Get all properties as string for debugging
     */
    public static String getAllProperties() {
        StringBuilder sb = new StringBuilder();
        sb.append("Configuration Properties:\n");
        properties.forEach((key, value) ->
                sb.append(key).append(" = ").append(value).append("\n"));
        return sb.toString();
    }
}