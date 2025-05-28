package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.*;
import java.util.List;

/**
 * Test class for Shopping Cart Management functionality
 */
@Epic("E-commerce")
@Feature("Shopping Cart Management")
public class CartTests extends BaseTest {

    @Test(priority = 1)
    @Story("Add to Cart")
    @Description("Test Case 5: Verify that adding a product to cart works correctly and cart total is updated")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddProductToCart() {
        logger.info("Starting add product to cart test");

        // Use the robust helper method to get products
        List<String> products = getProductsWithFallback("phones");
        Assert.assertFalse(products.isEmpty(), "Should have products available after trying multiple categories");

        String selectedProduct = products.get(0);
        String productPrice = homeActions.getProductPriceByName(selectedProduct);

        // Click on product to go to detail page
        homeActions.clickProductByName(selectedProduct);
        waitForPageLoad();

        // Add product to cart
        productActions.addProductToCartWithVerification();
        waitForPageLoad();

        // Navigate to cart and verify product was added
        cartActions.navigateToCart();

        // Verify product appears in cart
        Assert.assertTrue(cartActions.isItemInCart(selectedProduct),
                "Product should appear in cart: " + selectedProduct);

        // Verify cart is not empty
        Assert.assertFalse(cartActions.isCartEmpty(), "Cart should not be empty after adding product");

        // Verify cart total is updated
        String cartTotal = cartActions.getCartTotal();
        Assert.assertFalse(cartTotal.isEmpty(), "Cart total should be displayed");
        Assert.assertNotEquals(cartTotal, "0", "Cart total should not be zero");

        logger.info("Product '{}' added to cart successfully. Cart total: {}", selectedProduct, cartTotal);
        logger.info("✅ Add product to cart test completed successfully");
    }

    @Test(priority = 2)
    @Story("Remove from Cart")
    @Description("Test Case 6: Verify that removing a product from cart works correctly and cart total is updated")
    @Severity(SeverityLevel.CRITICAL)
    public void testRemoveProductFromCart() {
        logger.info("Starting remove product from cart test");

        // Use the robust helper method to get products
        List<String> products = getProductsWithFallback("phones");
        Assert.assertFalse(products.isEmpty(), "Should have products available");

        String selectedProduct = products.get(0);

        // Add product to cart
        homeActions.clickProductByName(selectedProduct);
        waitForPageLoad();
        productActions.addProductToCartWithVerification();
        waitForPageLoad();

        // Navigate to cart
        cartActions.navigateToCart();

        // Verify product is in cart before removal
        Assert.assertTrue(cartActions.isItemInCart(selectedProduct),
                "Product should be in cart before removal: " + selectedProduct);

        int initialItemCount = cartActions.getCartItemCount();
        String initialTotal = cartActions.getCartTotal();

        // Remove product from cart
        cartActions.removeItemFromCart(selectedProduct);
        waitForPageLoad();

        // Verify product is removed from cart
        Assert.assertFalse(cartActions.isItemInCart(selectedProduct),
                "Product should be removed from cart: " + selectedProduct);

        // Verify cart item count decreased
        int finalItemCount = cartActions.getCartItemCount();
        Assert.assertTrue(finalItemCount < initialItemCount,
                "Cart item count should decrease after removal");

        // If cart is now empty, verify total is 0 or cart is empty
        if (finalItemCount == 0) {
            Assert.assertTrue(cartActions.isCartEmpty(), "Cart should be empty when no items remain");
        }

        logger.info("Product '{}' removed from cart successfully", selectedProduct);
        logger.info("Initial count: {}, Final count: {}", initialItemCount, finalItemCount);
        logger.info("✅ Remove product from cart test completed successfully");
    }

    @Test(priority = 3)
    @Story("Cart Management")
    @Description("Verify multiple products can be added to cart")
    @Severity(SeverityLevel.NORMAL)
    public void testAddMultipleProductsToCart() {
        logger.info("Starting add multiple products to cart test");

        // Add phone product using robust helper
        List<String> phoneProducts = getProductsWithFallback("phones");
        Assert.assertFalse(phoneProducts.isEmpty(), "Should have phone products");

        String phoneProduct = phoneProducts.get(0);
        homeActions.clickProductByName(phoneProduct);
        waitForPageLoad();
        productActions.addProductToCartWithVerification();

        // Navigate back to home
        navigateToHome();

        // Add laptop product using robust helper
        List<String> laptopProducts = getProductsWithFallback("laptops");
        Assert.assertFalse(laptopProducts.isEmpty(), "Should have laptop products");

        String laptopProduct = laptopProducts.get(0);
        homeActions.clickProductByName(laptopProduct);
        waitForPageLoad();
        productActions.addProductToCartWithVerification();

        // Verify both products are in cart
        cartActions.navigateToCart();

        Assert.assertTrue(cartActions.isItemInCart(phoneProduct),
                "Phone product should be in cart: " + phoneProduct);
        Assert.assertTrue(cartActions.isItemInCart(laptopProduct),
                "Laptop product should be in cart: " + laptopProduct);

        // Verify cart has multiple items
        int itemCount = cartActions.getCartItemCount();
        Assert.assertTrue(itemCount >= 2, "Cart should have at least 2 items");

        logger.info("Multiple products added successfully. Cart has {} items", itemCount);
        logger.info("✅ Add multiple products to cart test completed successfully");
    }

    @Test(priority = 4)
    @Story("Cart Management")
    @Description("Verify cart total calculation with multiple products")
    @Severity(SeverityLevel.NORMAL)
    public void testCartTotalCalculation() {
        logger.info("Starting cart total calculation test");

        // Clear cart first
        cartActions.navigateToCart();
        cartActions.clearCart();

        // Add first product using robust helper
        List<String> phoneProducts = getProductsWithFallback("phones");
        Assert.assertFalse(phoneProducts.isEmpty(), "Should have phone products available");
        
        String product1 = phoneProducts.get(0);

        homeActions.clickProductByName(product1);
        waitForPageLoad();

        double product1Price = productActions.getNumericPrice();
        productActions.addProductToCartWithVerification();

        // Check cart total after first product
        cartActions.navigateToCart();
        double cartTotalAfterFirst = cartActions.getNumericCartTotal();

        // The cart total should match the product price (allowing for small floating point differences)
        Assert.assertTrue(Math.abs(cartTotalAfterFirst - product1Price) < 0.01,
                "Cart total should match product price after adding first product");

        // Add second product using robust helper
        List<String> laptopProducts = getProductsWithFallback("laptops");
        Assert.assertFalse(laptopProducts.isEmpty(), "Should have laptop products available");
        
        String product2 = laptopProducts.get(0);

        homeActions.clickProductByName(product2);
        waitForPageLoad();

        double product2Price = productActions.getNumericPrice();
        productActions.addProductToCartWithVerification();

        // Check final cart total
        cartActions.navigateToCart();
        double finalCartTotal = cartActions.getNumericCartTotal();
        double expectedTotal = product1Price + product2Price;

        // Verify total is sum of both products
        Assert.assertTrue(Math.abs(finalCartTotal - expectedTotal) < 0.01,
                String.format("Cart total (%.2f) should equal sum of product prices (%.2f + %.2f = %.2f)",
                        finalCartTotal, product1Price, product2Price, expectedTotal));

        logger.info("Cart total calculation verified: {} + {} = {}",
                product1Price, product2Price, finalCartTotal);
        logger.info("✅ Cart total calculation test completed successfully");
    }

    @Test(priority = 5)
    @Story("Cart Management")
    @Description("Verify cart persistence during navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testCartPersistenceDuringNavigation() {
        logger.info("Starting cart persistence during navigation test");

        // Add a product to cart using robust helper
        List<String> products = getProductsWithFallback("phones");
        Assert.assertFalse(products.isEmpty(), "Should have products available");
        
        String selectedProduct = products.get(0);

        homeActions.clickProductByName(selectedProduct);
        waitForPageLoad();
        productActions.addProductToCartWithVerification();

        // Navigate to different pages and verify cart persists
        // Try different categories to test persistence
        try {
            getProductsWithFallback("laptops"); // This will navigate to laptops
            getProductsWithFallback("monitors"); // This will navigate to monitors
        } catch (Exception e) {
            logger.warn("Category navigation failed during persistence test: {}", e.getMessage());
            // Continue with test even if navigation fails
        }

        // Check cart - product should still be there
        cartActions.navigateToCart();
        Assert.assertTrue(cartActions.isItemInCart(selectedProduct),
                "Product should persist in cart during navigation: " + selectedProduct);

        int itemCount = cartActions.getCartItemCount();
        Assert.assertTrue(itemCount > 0, "Cart should maintain items during navigation");

        logger.info("Cart persistence verified - product '{}' remained in cart", selectedProduct);
        logger.info("✅ Cart persistence during navigation test completed successfully");
    }

    @Test(priority = 6)
    @Story("Cart Management")
    @Description("Verify empty cart state")
    @Severity(SeverityLevel.MINOR)
    public void testEmptyCartState() {
        logger.info("Starting empty cart state test");

        // Navigate to cart and clear it
        cartActions.navigateToCart();
        cartActions.clearCart();

        // Verify cart is empty
        Assert.assertTrue(cartActions.isCartEmpty(), "Cart should be empty after clearing");

        // Verify cart item count is 0
        int itemCount = cartActions.getCartItemCount();
        Assert.assertEquals(itemCount, 0, "Cart item count should be 0 when empty");

        // Verify cart total is 0
        double cartTotal = cartActions.getNumericCartTotal();
        Assert.assertEquals(cartTotal, 0.0, 0.01, "Cart total should be 0 when empty");

        logger.info("Empty cart state verified successfully");
        logger.info("✅ Empty cart state test completed successfully");
    }

    @Test(priority = 7)
    @Story("Cart Management")
    @Description("Verify cart item details are displayed correctly")
    @Severity(SeverityLevel.MINOR)
    public void testCartItemDetailsDisplay() {
        logger.info("Starting cart item details display test");

        // Add a product to cart using robust helper
        List<String> products = getProductsWithFallback("phones");
        Assert.assertFalse(products.isEmpty(), "Should have products available");
        
        String selectedProduct = products.get(0);
        String productPrice = homeActions.getProductPriceByName(selectedProduct);

        homeActions.clickProductByName(selectedProduct);
        waitForPageLoad();
        productActions.addProductToCartWithVerification();

        // Navigate to cart and verify item details
        cartActions.navigateToCart();

        // Verify product name is displayed in cart
        Assert.assertTrue(cartActions.isItemInCart(selectedProduct),
                "Product name should be displayed in cart");

        // Verify product price is displayed in cart
        String cartItemPrice = cartActions.getItemPriceInCart(selectedProduct);
        Assert.assertFalse(cartItemPrice.isEmpty(),
                "Product price should be displayed in cart");

        logger.info("Cart item details verified - Product: '{}', Price in cart: '{}'",
                selectedProduct, cartItemPrice);
        logger.info("✅ Cart item details display test completed successfully");
    }
}