package actions;

import locators.HomePageLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.ArrayList;

/**
 * Actions for Home Page and Product browsing functionality
 */
public class HomePageActions extends BaseActions {
    private static final Logger logger = LoggerFactory.getLogger(HomePageActions.class);

    public HomePageActions(WebDriver driver) {
        super(driver);
    }

    /**
     * Click on Phones category
     */
    public void clickPhonesCategory() {
        logger.info("Clicking on Phones category");
        clickElement(HomePageLocators.PHONES_CATEGORY);
        waitForPageLoad();
        waitForLoadingToComplete();
        logger.info("Phones category selected successfully");
    }

    /**
     * Click on Laptops category
     */
    public void clickLaptopsCategory() {
        logger.info("Clicking on Laptops category");
        clickElement(HomePageLocators.LAPTOPS_CATEGORY);
        waitForPageLoad();
        waitForLoadingToComplete();
        logger.info("Laptops category selected successfully");
    }

    /**
     * Click on Monitors category
     */
    public void clickMonitorsCategory() {
        logger.info("Clicking on Monitors category");
        clickElement(HomePageLocators.MONITORS_CATEGORY);
        waitForPageLoad();
        waitForLoadingToComplete();
        logger.info("Monitors category selected successfully");
    }

    /**
     * Get all product titles on current page
     */
    public List<String> getAllProductTitles() {
        List<String> productTitles = new ArrayList<>();
        try {
            logger.debug("Retrieving all product titles from current page");
            
            // Wait for products container first
            waitForElementVisible(HomePageLocators.PRODUCTS_CONTAINER);
            
            // Add extra wait for products to load dynamically
            try {
                Thread.sleep(2000); // Wait 2 seconds for dynamic content
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Wait for loading to complete
            waitForLoadingToComplete();
            
            List<WebElement> titleElements = getElements(HomePageLocators.PRODUCT_TITLES);
            logger.debug("Found {} title elements", titleElements.size());

            for (WebElement titleElement : titleElements) {
                try {
                    String title = titleElement.getText();
                    if (!title.trim().isEmpty()) {
                        productTitles.add(title);
                        logger.debug("Added product title: {}", title);
                    }
                } catch (Exception e) {
                    logger.warn("Could not get text from title element: {}", e.getMessage());
                }
            }
            logger.info("Retrieved {} product titles", productTitles.size());
            
            // If no products found, log additional debugging info
            if (productTitles.isEmpty()) {
                logger.warn("No product titles found! Debugging info:");
                logger.warn("Products container visible: {}", isElementVisible(HomePageLocators.PRODUCTS_CONTAINER));
                logger.warn("Product cards count: {}", getElements(HomePageLocators.PRODUCT_CARDS).size());
                logger.warn("Product titles elements count: {}", titleElements.size());
                logger.warn("Current URL: {}", driver.getCurrentUrl());
            }
            
        } catch (Exception e) {
            logger.error("Error getting product titles: {}", e.getMessage());
        }
        return productTitles;
    }

    /**
     * Get all product prices on current page
     */
    public List<String> getAllProductPrices() {
        List<String> productPrices = new ArrayList<>();
        try {
            logger.debug("Retrieving all product prices from current page");
            waitForElementVisible(HomePageLocators.PRODUCTS_CONTAINER);
            List<WebElement> priceElements = getElements(HomePageLocators.PRODUCT_PRICES);

            for (WebElement priceElement : priceElements) {
                String price = priceElement.getText();
                if (!price.trim().isEmpty()) {
                    productPrices.add(price);
                }
            }
            logger.info("Retrieved {} product prices", productPrices.size());
        } catch (Exception e) {
            logger.error("Error getting product prices: {}", e.getMessage());
        }
        return productPrices;
    }

    /**
     * Click on a specific product by name
     */
    public void clickProductByName(String productName) {
        try {
            logger.info("Clicking on product: {}", productName);
            String productLocator = String.format(HomePageLocators.PRODUCT_LINK_BY_NAME, productName);
            scrollToElement(productLocator);
            clickElement(productLocator);
            waitForPageLoad();
            logger.info("Successfully clicked on product: {}", productName);
        } catch (Exception e) {
            logger.error("Error clicking product {}: {}", productName, e.getMessage());
            throw new RuntimeException("Could not click on product: " + productName, e);
        }
    }

    /**
     * Get price of a specific product by name
     */
    public String getProductPriceByName(String productName) {
        try {
            logger.debug("Getting price for product: {}", productName);
            String priceLocator = String.format(HomePageLocators.PRODUCT_PRICE_BY_NAME, productName);
            String price = getText(priceLocator);
            logger.debug("Price for product {}: {}", productName, price);
            return price;
        } catch (Exception e) {
            logger.error("Error getting price for product {}: {}", productName, e.getMessage());
            return "";
        }
    }

    /**
     * Check if a product exists on current page
     */
    public boolean isProductDisplayed(String productName) {
        try {
            logger.debug("Checking if product is displayed: {}", productName);
            String productLocator = String.format(HomePageLocators.PRODUCT_LINK_BY_NAME, productName);
            boolean isDisplayed = isElementVisible(productLocator);
            logger.debug("Product {} displayed: {}", productName, isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.error("Error checking if product {} is displayed: {}", productName, e.getMessage());
            return false;
        }
    }

    /**
     * Get count of products on current page
     */
    public int getProductCount() {
        try {
            logger.debug("Getting product count on current page");
            waitForElementVisible(HomePageLocators.PRODUCTS_CONTAINER);
            List<WebElement> products = getElements(HomePageLocators.PRODUCT_CARDS);
            int count = products.size();
            logger.info("Found {} products on current page", count);
            return count;
        } catch (Exception e) {
            logger.error("Error getting product count: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Click Next button for pagination
     */
    public void clickNextPage() {
        try {
            if (isElementVisible(HomePageLocators.NEXT_BUTTON)) {
                logger.info("Clicking next page button");
                clickElement(HomePageLocators.NEXT_BUTTON);
                waitForPageLoad();
                waitForLoadingToComplete();
                logger.info("Successfully navigated to next page");
            } else {
                logger.warn("Next button not visible or available");
            }
        } catch (Exception e) {
            logger.error("Error clicking next page: {}", e.getMessage());
        }
    }

    /**
     * Click Previous button for pagination
     */
    public void clickPreviousPage() {
        try {
            if (isElementVisible(HomePageLocators.PREVIOUS_BUTTON)) {
                logger.info("Clicking previous page button");
                clickElement(HomePageLocators.PREVIOUS_BUTTON);
                waitForPageLoad();
                waitForLoadingToComplete();
                logger.info("Successfully navigated to previous page");
            } else {
                logger.warn("Previous button not visible or available");
            }
        } catch (Exception e) {
            logger.error("Error clicking previous page: {}", e.getMessage());
        }
    }

    /**
     * Search for products containing specific text
     */
    public List<String> searchProductsByText(String searchText) {
        logger.info("Searching for products containing text: {}", searchText);
        List<String> matchingProducts = new ArrayList<>();
        List<String> allProducts = getAllProductTitles();

        for (String product : allProducts) {
            if (product.toLowerCase().contains(searchText.toLowerCase())) {
                matchingProducts.add(product);
            }
        }

        logger.info("Found {} products matching '{}': {}", matchingProducts.size(), searchText, matchingProducts);
        return matchingProducts;
    }

    /**
     * Filter products by category and return filtered list
     */
    public List<String> filterProductsByCategory(String category) {
        logger.info("Filtering products by category: {}", category);

        switch (category.toLowerCase()) {
            case "phones":
                clickPhonesCategory();
                break;
            case "laptops":
                clickLaptopsCategory();
                break;
            case "monitors":
                clickMonitorsCategory();
                break;
            default:
                logger.warn("Unknown category: {}", category);
                return new ArrayList<>();
        }

        // Wait for products to load after category selection
        waitForPageLoad();
        waitForLoadingToComplete();

        List<String> filteredProducts = getAllProductTitles();
        logger.info("Category '{}' filter returned {} products", category, filteredProducts.size());
        return filteredProducts;
    }

    /**
     * Navigate to home page
     */
    public void navigateToHome() {
        logger.info("Navigating to home page");
        try {
            // Try multiple methods to navigate to home
            boolean success = false;
            
            // Method 1: Try clicking the Home link
            try {
                String[] homeLocators = {
                    "//a[@class='nav-link' and text()='Home']",
                    "//a[contains(@class,'nav-link') and contains(text(),'Home')]",
                    "//a[text()='Home']",
                    "//a[contains(text(),'Home')]",
                    "//a[@href='index.html']",
                    locators.BaseLocators.DEMOBLAZE_LOGO
                };
                
                for (String locator : homeLocators) {
                    try {
                        if (isElementVisible(locator)) {
                            logger.debug("Trying home locator: {}", locator);
                            waitForElementClickable(locator);
                            clickElement(locator);
                            waitForPageLoad();
                            success = true;
                            logger.info("Successfully navigated to home using locator: {}", locator);
                            break;
                        }
                    } catch (Exception e) {
                        logger.debug("Failed to click home link with locator '{}': {}", locator, e.getMessage());
                    }
                }
            } catch (Exception e) {
                logger.warn("Failed to click any home links: {}", e.getMessage());
            }
            
            // Method 2: If clicking failed, navigate directly to base URL
            if (!success) {
                logger.info("Home link not clickable, navigating to base URL directly");
                String baseUrl = driver.getCurrentUrl();
                if (baseUrl.contains("demoblaze.com")) {
                    // Extract base URL
                    if (baseUrl.contains("demoblaze.com/")) {
                        baseUrl = baseUrl.substring(0, baseUrl.indexOf("demoblaze.com/") + "demoblaze.com".length());
                    }
                } else {
                    baseUrl = "https://www.demoblaze.com"; // Default fallback
                }
                
                driver.navigate().to(baseUrl);
                waitForPageLoad();
                success = true;
                logger.info("Successfully navigated to home via direct URL: {}", baseUrl);
            }
            
            // Method 3: Final fallback - refresh current page if it's already home
            if (!success) {
                logger.warn("Using final fallback - refresh page");
                driver.navigate().refresh();
                waitForPageLoad();
                logger.info("Page refreshed as fallback");
            }
            
            // Verify we're on home page
            try {
                Thread.sleep(2000); // Wait for page to stabilize
                String currentUrl = driver.getCurrentUrl();
                logger.info("Current URL after home navigation: {}", currentUrl);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
        } catch (Exception e) {
            logger.error("Error navigating to home: {}", e.getMessage());
            // Don't throw exception, let test continue
        }
    }
}