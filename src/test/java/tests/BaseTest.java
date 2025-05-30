package tests;

import actions.*;
import utils.TestDataProvider;
import utils.ConfigReader;
import utils.DriverManager;
import utils.TestDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;
import io.qameta.allure.Attachment;
import java.util.List;
import java.util.ArrayList;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Base Test class containing common setup and teardown methods
 */
public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    // Action classes
    protected LoginPageActions loginActions;
    protected HomePageActions homeActions;
    protected ProductPageActions productActions;
    protected CartPageActions cartActions;
    protected CheckoutPageActions checkoutActions;

    // Test data
    protected String baseUrl;
    protected String browser;

    @BeforeClass
    public void setUpClass() {
        logger.info("Setting up test class: {}", this.getClass().getSimpleName());
        baseUrl = ConfigReader.getBaseUrl();
        browser = ConfigReader.getBrowser();
        logger.info("Base URL: {}, Browser: {}", baseUrl, browser);
    }

    @BeforeMethod
    public void setUp() {
        logger.info("Setting up test method");

        try {
            // Initialize WebDriver
            DriverManager.initializeDriver(browser);

            // Navigate to base URL
            DriverManager.navigateToUrl(baseUrl);

            // Initialize action classes
            initializeActions();

            logger.info("Test setup completed successfully");
        } catch (Exception e) {
            logger.error("Error during test setup", e);
            throw new RuntimeException("Test setup failed", e);
        }
    }

    /**
     * Initialize all action classes
     */
    private void initializeActions() {
        loginActions = new LoginPageActions(DriverManager.getDriver());
        homeActions = new HomePageActions(DriverManager.getDriver());
        productActions = new ProductPageActions(DriverManager.getDriver());
        cartActions = new CartPageActions(DriverManager.getDriver());
        checkoutActions = new CheckoutPageActions(DriverManager.getDriver());

        logger.info("Action classes initialized");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        logger.info("Tearing down test method: {}", result.getMethod().getMethodName());

        try {
            // Take screenshot on failure
            if (result.getStatus() == ITestResult.FAILURE) {
                logger.error("Test failed: {}", result.getThrowable().getMessage());
                takeScreenshotOnFailure();
            }

            // Log test result
            logTestResult(result);

        } catch (Exception e) {
            logger.error("Error during test teardown", e);
        } finally {
            // Quit WebDriver
            DriverManager.quitDriver();
            logger.info("Test teardown completed");
        }
    }

    @AfterClass
    public void tearDownClass() {
        logger.info("Tearing down test class: {}", this.getClass().getSimpleName());
    }

    /**
     * Take screenshot on test failure
     */
    @Attachment(value = "Screenshot", type = "image/png")
    private byte[] takeScreenshotOnFailure() {
        try {
            byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
            logger.info("Screenshot captured for failed test");
            return screenshot;
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
            return new byte[0];
        }
    }

    /**
     * Log test result details
     */
    private void logTestResult(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                logger.info("✅ TEST PASSED: {} (Duration: {}ms)", testName, duration);
                break;
            case ITestResult.FAILURE:
                logger.error("❌ TEST FAILED: {} (Duration: {}ms)", testName, duration);
                logger.error("Failure reason: {}", result.getThrowable().getMessage());
                break;
            case ITestResult.SKIP:
                logger.warn("⏭️ TEST SKIPPED: {} (Duration: {}ms)", testName, duration);
                break;
        }
    }

    /**
     * Helper method to generate unique test user credentials
     */
    protected String[] generateUniqueUser() {
        String username = TestDataProvider.generateUniqueUsername();
        String password = TestDataProvider.generateUniquePassword();
        return new String[]{username, password};
    }

    /**
     * Helper method to get valid user credentials
     */
    protected String[] getValidUser() {
        return TestDataProvider.getValidUserCredentials();
    }

    /**
     * Helper method to get invalid user credentials
     */
    protected String[] getInvalidUser() {
        return TestDataProvider.getInvalidUserCredentials();
    }

    /**
     * Helper method to get customer info for checkout
     */
    protected String[] getCustomerInfo() {
        return TestDataProvider.getCustomerInfo();
    }

    /**
     * Helper method to get random product by category
     */
    protected String getRandomProduct(String category) {
        return TestDataProvider.getRandomProduct(category);
    }

    /**
     * Helper method to wait for page load (using proper WebDriver waits)
     */
    protected void waitForPageLoad() {
        try {
            // Wait for document ready state to be complete
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
            wait.until(webDriver ->
                    ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            
            // Additional wait for any dynamic content to load
            wait.until(webDriver ->
                    ((JavascriptExecutor) webDriver).executeScript("return typeof jQuery === 'undefined' || jQuery.active === 0"));
                    
        } catch (TimeoutException e) {
            // If conditions aren't met within timeout, continue anyway
            logger.debug("Page load wait timeout - continuing execution");
        } catch (Exception e) {
            logger.debug("Page load wait error: {} - continuing execution", e.getMessage());
        }
    }

    /**
     * Helper method to verify page title contains expected text
     */
    protected boolean verifyPageTitle(String expectedTitle) {
        String actualTitle = DriverManager.getCurrentPageTitle();
        boolean contains = actualTitle.toLowerCase().contains(expectedTitle.toLowerCase());
        logger.info("Page title verification - Expected: '{}', Actual: '{}', Contains: {}",
                expectedTitle, actualTitle, contains);
        return contains;
    }

    /**
     * Helper method to navigate back to home page
     */
    protected void navigateToHome() {
        logger.info("BaseTest: Navigating to home page");
        try {
            // Method 1: Try using HomePageActions if available
            if (homeActions != null) {
                homeActions.navigateToHome();
                return;
            }
            
            // Method 2: Direct URL navigation
            if (baseUrl != null && !baseUrl.isEmpty()) {
                DriverManager.navigateToUrl(baseUrl);
                waitForPageLoad();
                logger.info("Navigated to home via base URL: {}", baseUrl);
            } else {
                // Fallback URL
                String fallbackUrl = "https://www.demoblaze.com";
                DriverManager.navigateToUrl(fallbackUrl);
                waitForPageLoad();
                logger.info("Navigated to home via fallback URL: {}", fallbackUrl);
            }
        } catch (Exception e) {
            logger.error("Error in BaseTest navigateToHome: {}", e.getMessage());
            // Don't fail the test, just log the error
        }
    }

    /**
     * Helper method to click category by name (streamlined)
     */
    protected void clickCategoryByName(String category) {
        switch (category.toLowerCase()) {
            case "phones":
                homeActions.clickPhonesCategory();
                break;
            case "laptops":
                homeActions.clickLaptopsCategory();
                break;
            case "monitors":
                homeActions.clickMonitorsCategory();
                break;
            default:
                homeActions.clickPhonesCategory(); // Default to phones
        }
    }
}