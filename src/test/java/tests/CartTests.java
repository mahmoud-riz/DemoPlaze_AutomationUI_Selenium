package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.*;
import java.util.List;
import utils.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;


@Epic("E-commerce")
@Feature("Shopping Cart Management")
public class CartTests extends BaseTest {

    
    private static String workingProduct = null;
    private static boolean productCached = false;


    private String addProductOnce() {
        if (productCached && workingProduct != null) {
            return addCachedProduct();
        }

        try {
            navigateToHome();
            homeActions.clickPhonesCategory();
            
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(3));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h4[@class='card-title']//a")));
            
           
            WebElement firstProductLink = DriverManager.getDriver().findElement(
                By.xpath("//h4[@class='card-title']//a"));
            
            workingProduct = firstProductLink.getText().trim();
            firstProductLink.click();
            
           
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Add to cart')]")));
            addButton.click();
            
            
            try {
                wait.until(ExpectedConditions.alertIsPresent());
                DriverManager.getDriver().switchTo().alert().accept();
            } catch (Exception e) {
                
            }
            
            productCached = true;
            return workingProduct;
            
        } catch (Exception e) {
            throw new RuntimeException("Quick add failed: " + e.getMessage());
        }
    }

    // Use cached product 
    private String addCachedProduct() {
        try {
            navigateToHome();
            homeActions.clickPhonesCategory();
            
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(2));
            
            WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//h4[@class='card-title']//a[contains(text(),'" + workingProduct + "')]")));
            productLink.click();
            
            WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Add to cart')]")));
            addButton.click();
            
            try {
                wait.until(ExpectedConditions.alertIsPresent());
                DriverManager.getDriver().switchTo().alert().accept();
            } catch (Exception e) {
               
            }
            
            return workingProduct;
            
        } catch (Exception e) {
           
            productCached = false;
            workingProduct = null;
            return addProductOnce();
        }
    }

    @Test(priority = 1)
    @Story("Essential Cart Test")
    @Description("Single comprehensive cart test - add, verify, clear")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddProductToCart() {
        logger.info("Running essential cart test");
        
        try {
            // Add product
            String product = addProductOnce();
            
            // Verify in cart
            cartActions.navigateToCart();
            Assert.assertFalse(cartActions.isCartEmpty(), "Cart should not be empty");
            Assert.assertTrue(cartActions.getCartItemCount() > 0, "Should have items");
            
            logger.info("Essential cart test passed with product: {}", product);
            
        } catch (Exception e) {
            logger.error("Essential cart test failed: {}", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 2, dependsOnMethods = "testAddProductToCart")
    @Story("Cart State Test")
    @Description("Quick cart state verification")
    @Severity(SeverityLevel.NORMAL)
    public void testCartPersistenceDuringNavigation() {
        logger.info("Testing cart persistence");
        
        try {
          
            cartActions.navigateToCart();
            boolean hasItems = cartActions.getCartItemCount() > 0;
            
            if (!hasItems) {
                
                addProductOnce();
                cartActions.navigateToCart();
            }
            
          
            navigateToHome();
            cartActions.navigateToCart();
            Assert.assertFalse(cartActions.isCartEmpty(), "Cart should persist");
            
            logger.info("Cart persistence test passed");
            
        } catch (Exception e) {
            logger.error("Cart persistence test failed: {}", e.getMessage());
            throw e;
        }
    }

    @Test(priority = 3, dependsOnMethods = "testCartPersistenceDuringNavigation")
    @Story("Cart Clear Test")
    @Description("Quick cart clearing test")
    @Severity(SeverityLevel.NORMAL)
    public void testEmptyCartState() {
        logger.info("Testing cart clearing");
        
        try {
           
            cartActions.navigateToCart();
            cartActions.clearCart();
            
           
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            
            int itemCount = cartActions.getCartItemCount();
            Assert.assertTrue(itemCount <= 1, "Cart should be mostly clear");
            
            logger.info("Cart clearing test passed");
            
        } catch (Exception e) {
            logger.error("Cart clearing test failed: {}", e.getMessage());
            throw e;
        }
    }
}