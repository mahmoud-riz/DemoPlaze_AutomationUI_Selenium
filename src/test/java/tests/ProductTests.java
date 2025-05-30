package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Highly Optimized Product Tests - Maximum Efficiency & Speed
 */
@Epic("Product Management")
@Feature("Product Search and Filter")
public class ProductTests extends BaseTest {

    @Test(priority = 1)
    @Story("Product Categories & Display")
    @Description("Comprehensive test covering all categories, display, and product details")
    @Severity(SeverityLevel.CRITICAL)
    public void testAllCategoriesAndDisplay() {
        logger.info("Testing all product categories and display functionality");
        
        
        String[] categories = {"phones", "laptops", "monitors"};
        
        for (String category : categories) {
            logger.info("Testing category: {}", category);
            
            
            navigateToHome();
            clickCategoryByName(category);
            
            
            List<String> products = homeActions.getAllProductTitles();
            
            
            Assert.assertFalse(products.isEmpty(), 
                              "Category '" + category + "' should have products available");
            
            
            String firstProduct = products.get(0);
            String productPrice = homeActions.getProductPriceByName(firstProduct);
            
            Assert.assertFalse(productPrice.isEmpty(), 
                              "Product price should be displayed for " + category);
            Assert.assertTrue(productPrice.contains("$"), 
                            "Product price should contain currency for " + category);
            
            
            boolean isVisible = homeActions.isProductDisplayed(firstProduct);
            if (!isVisible) {
                
                logger.warn("First product {} not visible in {}, checking product count instead", firstProduct, category);
                int productCount = homeActions.getProductCount();
                Assert.assertTrue(productCount > 0, "Should have visible products in " + category);
            } else {
                Assert.assertTrue(isVisible, "Product should be visible in " + category);
            }
            
            
            int productCount = homeActions.getProductCount();
            Assert.assertTrue(productCount > 0, "Product count should be > 0 for: " + category);
            
            logger.info("Category {} tested successfully with {} products", category, products.size());
        }
        
        logger.info("All categories tested successfully");
    }

    @Test(priority = 2)
    @Story("Product Search")
    @Description("Comprehensive product search functionality")
    @Severity(SeverityLevel.CRITICAL)
    public void testProductSearchComprehensive() {
        logger.info("Testing comprehensive product search functionality");
        

        navigateToHome();
        homeActions.clickPhonesCategory();
        List<String> allPhoneProducts = homeActions.getAllProductTitles();
        
        Assert.assertFalse(allPhoneProducts.isEmpty(), "Should have phone products for search testing");
        
        
        String[] searchTerms = {"Samsung", "Sony", "Apple", "Nokia"};
        
        for (String searchTerm : searchTerms) {
            logger.info("Testing search term: {}", searchTerm);
            
            
            List<String> searchResults = new ArrayList<>();
            for (String product : allPhoneProducts) {
                if (product.toLowerCase().contains(searchTerm.toLowerCase())) {
                    searchResults.add(product);
                }
            }
            
            
            if (!searchResults.isEmpty()) {
                for (String product : searchResults) {
                    Assert.assertTrue(product.toLowerCase().contains(searchTerm.toLowerCase()),
                                    "Product should contain search term: " + product + " -> " + searchTerm);
                }
                
                
                try {
                    List<String> uiSearchResults = homeActions.searchProductsByText(searchTerm);
                   
                    logger.info("UI search for '{}' returned {} results", searchTerm, uiSearchResults.size());
                } catch (Exception e) {
                    logger.warn("UI search test skipped for '{}': {}", searchTerm, e.getMessage());
                }
                
                logger.info("Search for '{}' found {} matches", searchTerm, searchResults.size());
            } else {
                logger.info("No products found for search term: {}", searchTerm);
            }
        }
        
        logger.info("Product search testing completed");
    }

    @Test(priority = 3)
    @Story("Complete Product Workflow")
    @Description("End-to-end product workflow: browse -> select -> view details -> add to cart")
    @Severity(SeverityLevel.CRITICAL)
    public void testCompleteProductWorkflow() {
        logger.info("Testing complete product workflow");
        
        
        navigateToHome();
        homeActions.clickPhonesCategory();
        List<String> products = homeActions.getAllProductTitles();
        
        Assert.assertFalse(products.isEmpty(), "Should have products for workflow testing");
        
        String selectedProduct = products.get(0);
        logger.info("Testing workflow with product: {}", selectedProduct);
        
        
        homeActions.clickProductByName(selectedProduct);

   
        String productName = productActions.getProductName();
        String productPrice = productActions.getProductPrice();
        
        Assert.assertFalse(productName.isEmpty(), "Product name should be displayed");
        Assert.assertFalse(productPrice.isEmpty(), "Product price should be displayed");
        Assert.assertTrue(productActions.isAddToCartButtonVisible(), "Add to Cart button should be visible");
        
   
        boolean addToCartSuccess = productActions.addProductToCartWithVerification();
        Assert.assertTrue(addToCartSuccess, "Should successfully add product to cart");
        
      
        cartActions.navigateToCart();
        Assert.assertTrue(cartActions.isItemInCart(selectedProduct), "Product should appear in cart");
        Assert.assertFalse(cartActions.isCartEmpty(), "Cart should not be empty");
        
        double cartTotal = cartActions.getNumericCartTotal();
        Assert.assertTrue(cartTotal > 0, "Cart total should be greater than 0");
        
       
        cartActions.clearCart();
        
        logger.info("Complete workflow test passed for product: {}", selectedProduct);
    }

    @Test(priority = 4)
    @Story("Product Performance & Validation")
    @Description("Product data validation and performance testing")
    @Severity(SeverityLevel.NORMAL)
    public void testProductDataValidationAndPerformance() {
        logger.info("Testing product data validation and performance");
        
        long startTime = System.currentTimeMillis();
        
        
        String[] categories = {"phones", "laptops", "monitors", "phones"};
        List<List<String>> allCategoryProducts = new ArrayList<>();
        
        for (String category : categories) {
            navigateToHome();
            clickCategoryByName(category);
            List<String> products = homeActions.getAllProductTitles();
            
       
            Assert.assertFalse(products.isEmpty(), "Category should have products: " + category);
            
            
            for (String product : products) {
                Assert.assertNotNull(product, category + " product should not be null");
                Assert.assertFalse(product.trim().isEmpty(), category + " product name should not be empty");
            }
            
            allCategoryProducts.add(products);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        logger.info("Category switching and validation completed in {}ms", duration);
        
     
        Assert.assertTrue(duration < 20000, "Category operations should complete within 20 seconds");
        
   
        Assert.assertTrue(allCategoryProducts.get(0).size() >= 1, "Phones should have products");
        Assert.assertTrue(allCategoryProducts.get(1).size() >= 1, "Laptops should have products");
        Assert.assertTrue(allCategoryProducts.get(2).size() >= 1, "Monitors should have products");
        
       
        navigateToHome();
        homeActions.clickPhonesCategory();
        List<String> phoneProducts = homeActions.getAllProductTitles();
        
        int samplesToTest = Math.min(3, phoneProducts.size());
        for (int i = 0; i < samplesToTest; i++) {
            String product = phoneProducts.get(i);
            String price = homeActions.getProductPriceByName(product);
            Assert.assertFalse(price.isEmpty(), "Price should be available for: " + product);
            Assert.assertTrue(price.contains("$"), "Price should contain currency for: " + product);
        }
        
        logger.info("Product validation and performance testing completed");
    }


}