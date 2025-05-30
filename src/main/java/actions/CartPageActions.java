package actions;

import locators.BaseLocators;
import locators.CartPageLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import java.util.List;
import java.util.ArrayList;

/**
 * Actions for Shopping Cart functionality - Ultra-Fast Optimized
 */
public class CartPageActions extends BaseActions {

    public CartPageActions(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to cart page (ultra-fast, no waits)
     */
    public void navigateToCart() {
        try {
            clickElement(BaseLocators.CART_LINK);
        } catch (Exception e) {
            try {
                clickElement(BaseLocators.CART_LINK2);
            } catch (Exception ex) {
                clickElement("//a[contains(text(),'Cart')]");
            }
        }
    }

    /**
     * Get all items in cart (ultra-fast, no navigation)
     */
    public List<String> getCartItems() {
        List<String> cartItems = new ArrayList<>();
        
        try {
            List<WebElement> itemRows = driver.findElements(By.xpath(CartPageLocators.CART_ITEMS));
            for (WebElement row : itemRows) {
                try {
                    WebElement titleElement = row.findElement(By.xpath(CartPageLocators.CART_ITEM_TITLE));
                    String itemName = titleElement.getText().trim();
                    if (!itemName.isEmpty()) {
                        cartItems.add(itemName);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {
            // Return empty list
        }
        
        return cartItems;
    }

    /**
     * Check if cart is empty (ultra-fast)
     */
    public boolean isCartEmpty() {
        try {
            List<WebElement> items = driver.findElements(By.xpath(CartPageLocators.CART_ITEMS));
            return items.isEmpty();
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Get cart total (ultra-fast with navigation)
     */
    public String getCartTotal() {
        try {
            // Navigate to cart first to ensure we can read the total
            navigateToCart();
            
            if (isElementPresent(CartPageLocators.TOTAL_AMOUNT)) {
                return getText(CartPageLocators.TOTAL_AMOUNT);
            }
        } catch (Exception e) {
            // Return default
        }
        return "0";
    }

    /**
     * Check if item is in cart (ultra-fast)
     */
    public boolean isItemInCart(String itemName) {
        try {
            List<String> cartItems = getCartItems();
            return cartItems.stream()
                    .anyMatch(item -> item.toLowerCase().contains(itemName.toLowerCase()));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get cart item count (ultra-fast)
     */
    public int getCartItemCount() {
        return getCartItems().size();
    }

    /**
     * Remove item from cart (ultra-fast, no waits)
     */
    public void removeItemFromCart(String itemName) {
        try {
            // Try multiple delete strategies without navigation
            String[] deleteLocators = {
                String.format("//td[contains(text(),'%s')]/following-sibling::td//a[contains(text(),'Delete')]", itemName),
                String.format("//tr[td[contains(text(),'%s')]]//a[contains(text(),'Delete')]", itemName),
                String.format("//tr[contains(.,'%s')]//a", itemName),
                String.format("//tbody[@id='tbodyid']//tr[td[contains(text(),'%s')]]//a", itemName)
            };
            
            for (String locator : deleteLocators) {
                try {
                    if (isElementPresent(locator)) {
                        clickElement(locator);
                        return;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {
            // Continue silently
        }
    }

    /**
     * Click Place Order button (ultra-fast)
     */
    public void clickPlaceOrder() {
        try {
            clickElement(CartPageLocators.PLACE_ORDER_BUTTON);
        } catch (Exception e) {
            try {
                clickElement("//button[contains(@class,'btn-success') and contains(text(),'Place Order')]");
            } catch (Exception ex) {
                clickElement("//button[text()='Place Order']");
            }
        }
    }

    /**
     * Get numeric cart total (ultra-fast)
     */
    public double getNumericCartTotal() {
        try {
            String totalText = getCartTotal();
            String numericTotal = totalText.replaceAll("[^0-9.]", "");
            return numericTotal.isEmpty() ? 0.0 : Double.parseDouble(numericTotal);
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Clear entire cart (ultra-fast)
     */
    public void clearCart() {
        try {
            List<String> cartItems = getCartItems();
            for (String item : cartItems) {
                removeItemFromCart(item);
            }
        } catch (Exception e) {
            // Continue silently
        }
    }

    /**
     * Get item price in cart (ultra-fast)
     */
    public String getItemPriceInCart(String itemName) {
        try {
            String itemRowLocator = String.format(CartPageLocators.ITEM_ROW_BY_NAME, itemName);
            if (isElementPresent(itemRowLocator)) {
                WebElement itemRow = driver.findElement(By.xpath(itemRowLocator));
                WebElement priceElement = itemRow.findElement(By.xpath(CartPageLocators.CART_ITEM_PRICE));
                return priceElement.getText();
            }
        } catch (Exception e) {
            // Return empty
        }
        return "";
    }

    /**
     * Verify cart total matches expected amount (ultra-fast)
     */
    public boolean verifyCartTotal(double expectedTotal) {
        try {
            double actualTotal = getNumericCartTotal();
            return Math.abs(actualTotal - expectedTotal) < 0.01;
        } catch (Exception e) {
            return false;
        }
    }
}