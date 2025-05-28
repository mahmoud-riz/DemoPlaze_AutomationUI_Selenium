package actions;

import locators.BaseLocators;
import locators.CartPageLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.ArrayList;

/**
 * Actions for Shopping Cart functionality
 */
public class CartPageActions extends BaseActions {
    private static final Logger logger = LoggerFactory.getLogger(CartPageActions.class);

    public CartPageActions(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to cart page
     */
    public void navigateToCart() {
        logger.info("Navigating to cart page");
        try {
            // Handle any alerts that might be present
            try {
                if (driver.switchTo().alert() != null) {
                    logger.info("Alert detected, accepting it before navigating to cart");
                    acceptAlert();
                }
            } catch (Exception e) {
                // No alert present, continue
                logger.debug("No alert present");
            }
            
            // Wait a bit to ensure page is stable
            waitForPageLoad();
            
            // Try to click the cart link with retry logic
            int maxRetries = 3;
            for (int i = 0; i < maxRetries; i++) {
                try {
                    if (isElementVisible(BaseLocators.CART_LINK)) {
                        waitForElementClickable(BaseLocators.CART_LINK);
                        clickElement(BaseLocators.CART_LINK);
                        break;
                    } else if (isElementVisible(BaseLocators.CART_LINK2)) {
                        // Try alternative cart locator
                        logger.warn("Primary cart link not visible, trying alternative");
                        waitForElementClickable(BaseLocators.CART_LINK2);
                        clickElement(BaseLocators.CART_LINK2);
                        break;
                    }
                } catch (Exception e) {
                    logger.warn("Attempt {} to click cart failed: {}", i + 1, e.getMessage());
                    if (i == maxRetries - 1) {
                        throw e; // Re-throw on final attempt
                    }
                    // Wait before retry
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            
            waitForPageLoad();
            
            // Wait for cart container to be visible (with timeout)
            try {
                waitForElementVisible(CartPageLocators.CART_CONTAINER);
                logger.info("Successfully navigated to cart page");
            } catch (Exception e) {
                logger.warn("Cart container not visible after navigation: {}", e.getMessage());
                // Still continue as cart might be functional
            }
            
        } catch (Exception e) {
            logger.error("Error navigating to cart: {}", e.getMessage());
            throw new RuntimeException("Failed to navigate to cart", e);
        }
    }

    /**
     * Get all items in cart
     */
    public List<String> getCartItems() {
        List<String> cartItems = new ArrayList<>();
        try {
            logger.debug("Retrieving all items from cart");
            navigateToCart();

            if (isCartEmpty()) {
                logger.info("Cart is empty - no items to retrieve");
                return cartItems;
            }

            List<WebElement> itemRows = getElements(CartPageLocators.CART_ITEMS);

            for (WebElement row : itemRows) {
                try {
                    WebElement titleElement = row.findElement(org.openqa.selenium.By.xpath(CartPageLocators.CART_ITEM_TITLE));
                    String itemName = titleElement.getText();
                    if (!itemName.trim().isEmpty()) {
                        cartItems.add(itemName);
                        logger.debug("Found cart item: {}", itemName);
                    }
                } catch (Exception e) {
                    logger.warn("Could not get item name from row: {}", e.getMessage());
                }
            }

            logger.info("Retrieved {} items from cart: {}", cartItems.size(), cartItems);
        } catch (Exception e) {
            logger.error("Error getting cart items: {}", e.getMessage());
        }
        return cartItems;
    }

    /**
     * Get cart total amount
     */
    public String getCartTotal() {
        try {
            logger.debug("Getting cart total amount");
            navigateToCart();

            if (isElementVisible(CartPageLocators.TOTAL_AMOUNT)) {
                String total = getText(CartPageLocators.TOTAL_AMOUNT);
                logger.info("Cart total: {}", total);
                return total;
            }
            logger.debug("Cart total not visible, returning 0");
            return "0";
        } catch (Exception e) {
            logger.error("Error getting cart total: {}", e.getMessage());
            return "0";
        }
    }

    /**
     * Check if cart is empty
     */
    public boolean isCartEmpty() {
        try {
            List<WebElement> items = getElements(CartPageLocators.CART_ITEMS);
            boolean isEmpty = items.isEmpty();
            logger.debug("Cart empty status: {}", isEmpty);
            return isEmpty;
        } catch (Exception e) {
            logger.error("Error checking if cart is empty: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Remove item from cart by name
     */
    public void removeItemFromCart(String itemName) {
        try {
            logger.info("Removing item from cart: {}", itemName);
            navigateToCart();

            String deleteLocator = String.format(CartPageLocators.DELETE_ITEM_BY_NAME, itemName);

            if (isElementVisible(deleteLocator)) {
                clickElement(deleteLocator);

                // Wait for item to be removed from cart
                waitForElementToDisappear(String.format(CartPageLocators.ITEM_ROW_BY_NAME, itemName));
                waitForPageLoad();

                logger.info("Successfully removed item from cart: {}", itemName);
            } else {
                logger.warn("Item not found in cart for removal: {}", itemName);
            }
        } catch (Exception e) {
            logger.error("Error removing item from cart {}: {}", itemName, e.getMessage());
        }
    }

    /**
     * Get item price from cart by item name
     */
    public String getItemPriceInCart(String itemName) {
        try {
            logger.debug("Getting price for item in cart: {}", itemName);
            navigateToCart();

            String itemRowLocator = String.format(CartPageLocators.ITEM_ROW_BY_NAME, itemName);

            if (isElementVisible(itemRowLocator)) {
                WebElement itemRow = driver.findElement(org.openqa.selenium.By.xpath(itemRowLocator));
                WebElement priceElement = itemRow.findElement(org.openqa.selenium.By.xpath(CartPageLocators.CART_ITEM_PRICE));
                String price = priceElement.getText();
                logger.debug("Price for {} in cart: {}", itemName, price);
                return price;
            }

            logger.warn("Item not found in cart: {}", itemName);
            return "";
        } catch (Exception e) {
            logger.error("Error getting item price for {}: {}", itemName, e.getMessage());
            return "";
        }
    }

    /**
     * Get count of items in cart
     */
    public int getCartItemCount() {
        try {
            List<String> items = getCartItems();
            int count = items.size();
            logger.info("Cart item count: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Error getting cart item count: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Check if specific item exists in cart
     */
    public boolean isItemInCart(String itemName) {
        try {
            logger.debug("Checking if item is in cart: {}", itemName);
            List<String> cartItems = getCartItems();
            boolean exists = cartItems.stream()
                    .anyMatch(item -> item.toLowerCase().contains(itemName.toLowerCase()));
            logger.info("Item '{}' in cart: {}", itemName, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error checking if item {} is in cart: {}", itemName, e.getMessage());
            return false;
        }
    }

    /**
     * Click Place Order button
     */
    public void clickPlaceOrder() {
        try {
            logger.info("Clicking Place Order button");
            navigateToCart();
            
            // Wait for page to load completely
            waitForPageLoad();
            waitForLoadingToComplete();

            // Try multiple locator strategies for Place Order button
            String[] placeOrderLocators = {
                CartPageLocators.PLACE_ORDER_BUTTON,
                "//button[contains(@class,'btn-success') and contains(text(),'Place Order')]",
                "//button[text()='Place Order']",
                "//button[contains(text(),'Place Order')]",
                "//input[@type='button' and @value='Place Order']",
                "//a[contains(@class,'btn') and contains(text(),'Place Order')]"
            };

            boolean buttonClicked = false;
            for (String locator : placeOrderLocators) {
                try {
                    if (isElementVisible(locator)) {
                        logger.info("Found Place Order button with locator: {}", locator);
                        scrollToElement(locator);
                        
                        // Wait a bit for any animations or loading
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        
                        clickElement(locator);
                        logger.info("Place Order button clicked successfully with locator: {}", locator);
                        buttonClicked = true;
                        break;
                    }
                } catch (Exception e) {
                    logger.debug("Locator {} failed: {}", locator, e.getMessage());
                }
            }

            if (!buttonClicked) {
                logger.error("Place Order button not found with any locator");
                throw new RuntimeException("Place Order button not visible");
            }
            
            // Wait for modal to appear
            try {
                Thread.sleep(2000); // Wait for modal animation
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
        } catch (Exception e) {
            logger.error("Error clicking Place Order: {}", e.getMessage());
            throw new RuntimeException("Failed to click Place Order button", e);
        }
    }

    /**
     * Get numeric total value
     */
    public double getNumericCartTotal() {
        try {
            String totalText = getCartTotal();
            // Remove any non-numeric characters except decimal point
            String numericTotal = totalText.replaceAll("[^0-9.]", "");
            double total = Double.parseDouble(numericTotal);
            logger.debug("Numeric cart total: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error getting numeric cart total: {}", e.getMessage());
            return 0.0;
        }
    }

    /**
     * Clear entire cart by removing all items
     */
    public void clearCart() {
        try {
            logger.info("Clearing entire cart");
            List<String> cartItems = getCartItems();

            if (cartItems.isEmpty()) {
                logger.info("Cart is already empty");
                return;
            }

            for (String item : cartItems) {
                removeItemFromCart(item);
                // Wait for removal to complete before proceeding to next item
                waitForLoadingToComplete();
            }

            logger.info("Cart cleared successfully - removed {} items", cartItems.size());
        } catch (Exception e) {
            logger.error("Error clearing cart: {}", e.getMessage());
        }
    }

    /**
     * Verify cart total matches expected amount
     */
    public boolean verifyCartTotal(double expectedTotal) {
        try {
            double actualTotal = getNumericCartTotal();
            boolean matches = Math.abs(actualTotal - expectedTotal) < 0.01; // Allow small floating point differences
            logger.info("Cart total verification - Expected: {}, Actual: {}, Matches: {}",
                    expectedTotal, actualTotal, matches);
            return matches;
        } catch (Exception e) {
            logger.error("Error verifying cart total: {}", e.getMessage());
            return false;
        }
    }
}