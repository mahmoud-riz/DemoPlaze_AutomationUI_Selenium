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
     * Helper method to wait for page load
     */
    protected void waitForPageLoad() {
        try {
            Thread.sleep(2000); // Wait for page transitions
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
     * Helper method to robustly get products from a category with fallback
     */
    protected List<String> getProductsWithFallback(String primaryCategory) {
        List<String> products = new ArrayList<>();
        
        // Try primary category first
        try {
            logger.info("Attempting to get products from primary category: {}", primaryCategory);
            
            // Navigate to home first to ensure clean state
            navigateToHome();
            waitForPageLoad();
            
            switch (primaryCategory.toLowerCase()) {
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
            
            waitForPageLoad();
            
            // Wait with multiple attempts
            for (int attempt = 1; attempt <= 3; attempt++) {
                logger.debug("Attempt {} to load products from {}", attempt, primaryCategory);
                Thread.sleep(2000 * attempt); // Progressive wait: 2s, 4s, 6s
                
                products = homeActions.getAllProductTitles();
                if (!products.isEmpty()) {
                    logger.info("Successfully loaded {} products from {} category on attempt {}", 
                            products.size(), primaryCategory, attempt);
                    return products;
                }
                
                logger.warn("Attempt {} failed - no products found in {} category", attempt, primaryCategory);
            }
            
            // If primary category failed completely, try fallback categories
            String[] fallbackCategories = {"laptops", "phones", "monitors"};
            for (String category : fallbackCategories) {
                if (category.equals(primaryCategory.toLowerCase())) {
                    continue; // Skip the category we already tried
                }
                
                logger.warn("Primary category '{}' failed, trying fallback category: {}", primaryCategory, category);
                
                // Navigate back to home before trying new category
                navigateToHome();
                waitForPageLoad();
                
                switch (category) {
                    case "phones":
                        homeActions.clickPhonesCategory();
                        break;
                    case "laptops":
                        homeActions.clickLaptopsCategory();
                        break;
                    case "monitors":
                        homeActions.clickMonitorsCategory();
                        break;
                }
                
                waitForPageLoad();
                
                // Try multiple times for fallback category too
                for (int attempt = 1; attempt <= 2; attempt++) {
                    Thread.sleep(3000); // Wait 3 seconds between attempts
                    products = homeActions.getAllProductTitles();
                    
                    if (!products.isEmpty()) {
                        logger.info("Successfully loaded {} products from fallback category '{}' on attempt {}", 
                                products.size(), category, attempt);
                        return products;
                    }
                }
            }
            
            // Final desperate attempt - refresh page and try phones
            logger.error("All categories failed, making final attempt with page refresh");
            try {
                DriverManager.getDriver().navigate().refresh();
                waitForPageLoad();
                Thread.sleep(3000);
                
                homeActions.clickPhonesCategory();
                waitForPageLoad();
                Thread.sleep(5000); // Longer wait for final attempt
                
                products = homeActions.getAllProductTitles();
                if (!products.isEmpty()) {
                    logger.info("Final attempt successful - loaded {} products", products.size());
                    return products;
                }
            } catch (Exception finalEx) {
                logger.error("Final attempt also failed: {}", finalEx.getMessage());
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted while loading products");
        } catch (Exception e) {
            logger.error("Error loading products from category {}: {}", primaryCategory, e.getMessage());
        }
        
        return products;
    }
}