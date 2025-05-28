package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import java.time.Duration;

/**
 * Driver Manager class for WebDriver initialization and management
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Initialize WebDriver based on browser type
     */
    public static void initializeDriver(String browserName) {
        WebDriver driver = null;

        try {
            switch (browserName.toLowerCase()) {
                case "chrome":
                    driver = initializeChromeDriver();
                    break;
                case "firefox":
                    driver = initializeFirefoxDriver();
                    break;
                case "edge":
                    driver = initializeEdgeDriver();
                    break;
                default:
                    logger.warn("Browser '{}' not supported. Using Chrome as default.", browserName);
                    driver = initializeChromeDriver();
            }

            if (driver != null) {
                configureDriver(driver);
                driverThreadLocal.set(driver);
                logger.info("WebDriver initialized successfully for browser: {}", browserName);
            }
        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver for browser: {}", browserName, e);
            throw new RuntimeException("Driver initialization failed", e);
        }
    }

    /**
     * Initialize Chrome Driver
     */
    private static WebDriver initializeChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Add Chrome options for better stability
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");

        // Uncomment for headless mode
        // options.addArguments("--headless");

        logger.info("Initializing Chrome driver with options");
        return new ChromeDriver(options);
    }

    /**
     * Initialize Firefox Driver
     */
    private static WebDriver initializeFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        // Add Firefox options
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Uncomment for headless mode
        // options.addArguments("--headless");

        logger.info("Initializing Firefox driver with options");
        return new FirefoxDriver(options);
    }

    /**
     * Initialize Edge Driver
     */
    private static WebDriver initializeEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();

        // Add Edge options
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*");

        logger.info("Initializing Edge driver with options");
        return new EdgeDriver(options);
    }

    /**
     * Configure common driver settings
     */
    private static void configureDriver(WebDriver driver) {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().deleteAllCookies();
        logger.info("Driver configured with timeouts and window settings");
    }

    /**
     * Get current WebDriver instance
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new RuntimeException("WebDriver is not initialized. Call initializeDriver() first.");
        }
        return driver;
    }

    /**
     * Quit WebDriver and clean up
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting WebDriver", e);
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    /**
     * Navigate to URL
     */
    public static void navigateToUrl(String url) {
        try {
            getDriver().get(url);
            logger.info("Navigated to URL: {}", url);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL: {}", url, e);
            throw new RuntimeException("Navigation failed", e);
        }
    }

    /**
     * Get current page title
     */
    public static String getCurrentPageTitle() {
        try {
            String title = getDriver().getTitle();
            logger.info("Current page title: {}", title);
            return title;
        } catch (Exception e) {
            logger.error("Failed to get page title", e);
            return "";
        }
    }

    /**
     * Get current page URL
     */
    public static String getCurrentUrl() {
        try {
            String url = getDriver().getCurrentUrl();
            logger.info("Current URL: {}", url);
            return url;
        } catch (Exception e) {
            logger.error("Failed to get current URL", e);
            return "";
        }
    }

    /**
     * Refresh current page
     */
    public static void refreshPage() {
        try {
            getDriver().navigate().refresh();
            logger.info("Page refreshed");
        } catch (Exception e) {
            logger.error("Failed to refresh page", e);
        }
    }
}