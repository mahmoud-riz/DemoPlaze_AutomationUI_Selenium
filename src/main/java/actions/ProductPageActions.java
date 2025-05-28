package actions;

import locators.ProductPageLocators;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Actions for Product Detail Page functionality
 */
public class ProductPageActions extends BaseActions {
    private static final Logger logger = LoggerFactory.getLogger(ProductPageActions.class);

    public ProductPageActions(WebDriver driver) {
        super(driver);
    }

    /**
     * Get product name from detail page
     */
    public String getProductName() {
        try {
            logger.debug("Getting product name from detail page");
            waitForElementVisible(ProductPageLocators.PRODUCT_NAME);
            String productName = getText(ProductPageLocators.PRODUCT_NAME);
            logger.info("Product name: {}", productName);
            return productName;
        } catch (Exception e) {
            logger.error("Error getting product name: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Get product price from detail page
     */
    public String getProductPrice() {
        try {
            logger.debug("Getting product price from detail page");
            waitForElementVisible(ProductPageLocators.PRODUCT_PRICE);
            String price = getText(ProductPageLocators.PRODUCT_PRICE);
            logger.info("Product price: {}", price);
            return price;
        } catch (Exception e) {
            logger.error("Error getting product price: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Get product description
     */
    public String getProductDescription() {
        try {
            logger.debug("Getting product description");
            if (isElementVisible(ProductPageLocators.PRODUCT_DESCRIPTION)) {
                String description = getText(ProductPageLocators.PRODUCT_DESCRIPTION);
                logger.debug("Product description retrieved successfully");
                return description;
            }
            logger.debug("Product description not available");
            return "";
        } catch (Exception e) {
            logger.error("Error getting product description: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Click Add to Cart button
     */
    public void clickAddToCart() {
        try {
            logger.info("Clicking Add to Cart button");
            waitForElementVisible(ProductPageLocators.ADD_TO_CART_BUTTON);
            scrollToElement(ProductPageLocators.ADD_TO_CART_BUTTON);
            clickElement(ProductPageLocators.ADD_TO_CART_BUTTON);
            logger.info("Add to Cart button clicked successfully");

            // Wait for alert to appear and handle it
            String alertText = waitForAlertAndGetText();
            if (!alertText.isEmpty()) {
                acceptAlert();
                logger.info("Cart alert accepted: {}", alertText);
            }
        } catch (Exception e) {
            logger.error("Error clicking Add to Cart: {}", e.getMessage());
            throw new RuntimeException("Failed to add product to cart", e);
        }
    }

    /**
     * Check if Add to Cart button is visible
     */
    public boolean isAddToCartButtonVisible() {
        boolean isVisible = isElementVisible(ProductPageLocators.ADD_TO_CART_BUTTON);
        logger.debug("Add to Cart button visible: {}", isVisible);
        return isVisible;
    }

    /**
     * Check if product image is displayed
     */
    public boolean isProductImageDisplayed() {
        boolean isDisplayed = isElementVisible(ProductPageLocators.PRODUCT_IMAGE);
        logger.debug("Product image displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Get product image source URL
     */
    public String getProductImageSource() {
        try {
            logger.debug("Getting product image source URL");
            String imageSource = getAttributeValue(ProductPageLocators.PRODUCT_IMAGE, "src");
            logger.debug("Product image source: {}", imageSource);
            return imageSource;
        } catch (Exception e) {
            logger.error("Error getting product image source: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Add product to cart and verify success
     */
    public boolean addProductToCartWithVerification() {
        try {
            String productName = getProductName();
            logger.info("Adding product to cart: {}", productName);
            clickAddToCart();

            // Wait for the add to cart operation to complete
            waitForPageLoad();

            logger.info("Successfully added {} to cart", productName);
            return true;
        } catch (Exception e) {
            logger.error("Failed to add product to cart: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get all product details as a formatted string
     */
    public String getProductDetails() {
        StringBuilder details = new StringBuilder();

        try {
            logger.debug("Compiling product details");
            String name = getProductName();
            String price = getProductPrice();
            String description = getProductDescription();

            details.append("Product Name: ").append(name).append("\n");
            details.append("Price: ").append(price).append("\n");

            if (!description.isEmpty()) {
                details.append("Description: ").append(description).append("\n");
            }

            logger.debug("Product details compiled successfully");
            return details.toString();
        } catch (Exception e) {
            logger.error("Error compiling product details: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Extract numeric price value from price string
     */
    public double getNumericPrice() {
        try {
            String priceText = getProductPrice();
            // Remove currency symbols and extract numeric value
            String numericPrice = priceText.replaceAll("[^0-9.]", "");
            double price = Double.parseDouble(numericPrice);
            logger.debug("Extracted numeric price: {}", price);
            return price;
        } catch (Exception e) {
            logger.error("Error extracting numeric price: {}", e.getMessage());
            return 0.0;
        }
    }

    /**
     * Navigate back to home page
     */
    public void navigateBackToHome() {
        try {
            logger.info("Navigating back to home page");
            if (isElementVisible(ProductPageLocators.HOME_BREADCRUMB)) {
                clickElement(ProductPageLocators.HOME_BREADCRUMB);
            } else {
                // Use browser back button as fallback
                logger.debug("Using browser back button as fallback");
                driver.navigate().back();
            }
            waitForPageLoad();
            logger.info("Successfully navigated back to home page");
        } catch (Exception e) {
            logger.error("Error navigating back to home: {}", e.getMessage());
        }
    }
}