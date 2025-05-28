package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.*;
import java.util.List;

/**
 * Test class for Product Search and Filter functionality
 */
@Epic("Product Management")
@Feature("Product Search and Filter")
public class ProductTests extends BaseTest {

    @Test(priority = 1)
    @Story("Product Search")
    @Description("Test Case 3: Verify that searching for a product returns relevant results")
    @Severity(SeverityLevel.CRITICAL)
    public void testProductSearch() {
        logger.info("Starting product search test");

        // Get all products on home page
        List<String> allProducts = homeActions.getAllProductTitles();
        Assert.assertFalse(allProducts.isEmpty(), "Products should be displayed on home page");

        // Search for products containing "Samsung"
        List<String> samsungProducts = homeActions.searchProductsByText("Samsung");

        // Verify search results
        Assert.assertFalse(samsungProducts.isEmpty(), "Search should return Samsung products");

        // Verify all returned products contain "Samsung"
        for (String product : samsungProducts) {
            Assert.assertTrue(product.toLowerCase().contains("samsung"),
                    "Product '" + product + "' should contain 'Samsung' in the name");
        }

        logger.info("Found {} Samsung products: {}", samsungProducts.size(), samsungProducts);
        logger.info("✅ Product search test completed successfully");
    }

    @Test(priority = 2)
    @Story("Product Filter")
    @Description("Test Case 4: Verify that applying filters (e.g., category) correctly updates the product list")
    @Severity(SeverityLevel.CRITICAL)
    public void testProductCategoryFilter() {
        logger.info("Starting product category filter test");

        // Test Phones category filter
        homeActions.clickPhonesCategory();
        waitForPageLoad();

        List<String> phoneProducts = homeActions.getAllProductTitles();
        Assert.assertFalse(phoneProducts.isEmpty(), "Phones category should show products");

        logger.info("Phones category shows {} products: {}", phoneProducts.size(), phoneProducts);

        // Test Laptops category filter
        homeActions.clickLaptopsCategory();
        waitForPageLoad();

        List<String> laptopProducts = homeActions.getAllProductTitles();
        Assert.assertFalse(laptopProducts.isEmpty(), "Laptops category should show products");

        logger.info("Laptops category shows {} products: {}", laptopProducts.size(), laptopProducts);

        // Test Monitors category filter
        homeActions.clickMonitorsCategory();
        waitForPageLoad();

        List<String> monitorProducts = homeActions.getAllProductTitles();
        Assert.assertFalse(monitorProducts.isEmpty(), "Monitors category should show products");

        logger.info("Monitors category shows {} products: {}", monitorProducts.size(), monitorProducts);

        // Verify different categories show different products
        Assert.assertNotEquals(phoneProducts, laptopProducts,
                "Phones and Laptops categories should show different products");
        Assert.assertNotEquals(laptopProducts, monitorProducts,
                "Laptops and Monitors categories should show different products");

        logger.info("✅ Product category filter test completed successfully");
    }

    @Test(priority = 3)
    @Story("Product Display")
    @Description("Verify product details are displayed correctly")
    @Severity(SeverityLevel.NORMAL)
    public void testProductDetailsDisplay() {
        logger.info("Starting product details display test");

        // Get a product from phones category
        homeActions.clickPhonesCategory();
        waitForPageLoad();

        List<String> products = homeActions.getAllProductTitles();
        Assert.assertFalse(products.isEmpty(), "Should have products to test");

        String firstProduct = products.get(0);

        // Check if product price is displayed
        String productPrice = homeActions.getProductPriceByName(firstProduct);
        Assert.assertFalse(productPrice.isEmpty(),
                "Product price should be displayed for: " + firstProduct);
        Assert.assertTrue(productPrice.contains("$"),
                "Product price should contain currency symbol");

        // Verify product is clickable
        Assert.assertTrue(homeActions.isProductDisplayed(firstProduct),
                "Product should be displayed and clickable: " + firstProduct);

        logger.info("Product '{}' has price: {}", firstProduct, productPrice);
        logger.info("✅ Product details display test completed successfully");
    }

    @Test(priority = 4)
    @Story("Product Navigation")
    @Description("Verify product detail page navigation")
    @Severity(SeverityLevel.NORMAL)
    public void testProductDetailPageNavigation() {
        logger.info("Starting product detail page navigation test");

        // Navigate to phones category
        homeActions.clickPhonesCategory();
        waitForPageLoad();

        // Get a random phone product
        String phoneProduct = getRandomProduct("phones");
        if (phoneProduct.isEmpty()) {
            // Fallback: get first available product
            List<String> products = homeActions.getAllProductTitles();
            Assert.assertFalse(products.isEmpty(), "Should have products available");
            phoneProduct = products.get(0);
        }

        // Click on the product to go to detail page
        homeActions.clickProductByName(phoneProduct);
        waitForPageLoad();

        // Verify we're on product detail page
        String productName = productActions.getProductName();
        Assert.assertFalse(productName.isEmpty(), "Product name should be displayed on detail page");

        String productPrice = productActions.getProductPrice();
        Assert.assertFalse(productPrice.isEmpty(), "Product price should be displayed on detail page");

        // Verify Add to Cart button is visible
        Assert.assertTrue(productActions.isAddToCartButtonVisible(),
                "Add to Cart button should be visible on product detail page");

        logger.info("Product detail page for '{}' loaded successfully", productName);
        logger.info("✅ Product detail page navigation test completed successfully");
    }

    @Test(priority = 5)
    @Story("Product Search")
    @Description("Verify product count in different categories")
    @Severity(SeverityLevel.MINOR)
    public void testProductCountPerCategory() {
        logger.info("Starting product count per category test");

        // Test each category and verify it has products
        String[] categories = {"phones", "laptops", "monitors"};

        for (String category : categories) {
            List<String> categoryProducts = homeActions.filterProductsByCategory(category);

            Assert.assertFalse(categoryProducts.isEmpty(),
                    "Category '" + category + "' should have products");

            int productCount = homeActions.getProductCount();
            Assert.assertTrue(productCount > 0,
                    "Product count should be greater than 0 for category: " + category);

            logger.info("Category '{}' has {} products", category, productCount);
        }

        logger.info("✅ Product count per category test completed successfully");
    }

    @Test(priority = 6)
    @Story("Product Filter")
    @Description("Verify filtering by multiple categories")
    @Severity(SeverityLevel.NORMAL)
    public void testMultipleCategoryFiltering() {
        logger.info("Starting multiple category filtering test");

        // Start with phones
        homeActions.clickPhonesCategory();
        waitForPageLoad();
        int phonesCount = homeActions.getProductCount();

        // Switch to laptops
        homeActions.clickLaptopsCategory();
        waitForPageLoad();
        int laptopsCount = homeActions.getProductCount();

        // Switch to monitors
        homeActions.clickMonitorsCategory();
        waitForPageLoad();
        int monitorsCount = homeActions.getProductCount();

        // Go back to phones
        homeActions.clickPhonesCategory();
        waitForPageLoad();
        int phonesCountAgain = homeActions.getProductCount();

        // Verify counts are consistent
        Assert.assertEquals(phonesCount, phonesCountAgain,
                "Phones category should show same count when revisited");

        // Verify different categories have different counts (or at least one is different)
        boolean categoriesHaveDifferentCounts = (phonesCount != laptopsCount) ||
                (laptopsCount != monitorsCount) ||
                (phonesCount != monitorsCount);

        Assert.assertTrue(categoriesHaveDifferentCounts,
                "Categories should show different product counts");

        logger.info("Product counts - Phones: {}, Laptops: {}, Monitors: {}",
                phonesCount, laptopsCount, monitorsCount);
        logger.info("✅ Multiple category filtering test completed successfully");
    }

    @Test(priority = 7)
    @Story("Product Search")
    @Description("Verify search functionality with various search terms")
    @Severity(SeverityLevel.NORMAL)
    public void testVariousSearchTerms() {
        logger.info("Starting various search terms test");

        // Test different search terms
        String[] searchTerms = {"Samsung", "Sony", "Apple", "Nokia"};

        for (String searchTerm : searchTerms) {
            List<String> searchResults = homeActions.searchProductsByText(searchTerm);

            if (!searchResults.isEmpty()) {
                // If results found, verify they contain the search term
                for (String product : searchResults) {
                    Assert.assertTrue(product.toLowerCase().contains(searchTerm.toLowerCase()),
                            "Product '" + product + "' should contain search term: " + searchTerm);
                }
                logger.info("Search term '{}' returned {} products", searchTerm, searchResults.size());
            } else {
                logger.info("Search term '{}' returned no products", searchTerm);
            }
        }

        logger.info("✅ Various search terms test completed successfully");
    }
}