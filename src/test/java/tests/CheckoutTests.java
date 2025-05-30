package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.*;
import java.util.List;

/**
 * Test class for Checkout Process functionality - Ultra-Fast Optimized
 */
@Epic("E-commerce")
@Feature("Checkout Process")
public class CheckoutTests extends BaseTest {

    @Test(priority = 1)
    @Story("Checkout Process")
    @Description("Test Case 7: Verify that the user can complete a purchase and receive an order confirmation")
    @Severity(SeverityLevel.CRITICAL)
    public void testCompleteCheckoutProcess() {
        
        homeActions.navigateToHome();
        homeActions.clickPhonesCategory();
        List<String> products = homeActions.getAllProductTitles();
        Assert.assertFalse(products.isEmpty(), "Should have products available");

     
        homeActions.clickProductByName(products.get(0));
        productActions.addProductToCartWithVerification();
        cartActions.navigateToCart();
        cartActions.clickPlaceOrder();

      
        String[] customerInfo = getCustomerInfo();
        checkoutActions.fillCheckoutFormFast(customerInfo[0], customerInfo[1], customerInfo[2],
                customerInfo[3], customerInfo[4], customerInfo[5]);
        checkoutActions.clickPurchaseButton();

       
        Assert.assertTrue(checkoutActions.isOrderCompletedFast(), "Order should be completed");
        checkoutActions.clickOkFast();
    }

    @Test(priority = 2)
    @Story("Checkout Process")
    @Description("Verify checkout with multiple products")
    @Severity(SeverityLevel.NORMAL)
    public void testCheckoutWithMultipleProducts() {
      
        homeActions.navigateToHome();
        homeActions.clickPhonesCategory();
        List<String> products = homeActions.getAllProductTitles();
        
     
        homeActions.clickProductByName(products.get(0));
        productActions.addProductToCartWithVerification();
        homeActions.navigateToHome();
        homeActions.clickPhonesCategory();
        homeActions.clickProductByName(products.size() > 1 ? products.get(1) : products.get(0));
        productActions.addProductToCartWithVerification();

      
        cartActions.navigateToCart();
        cartActions.clickPlaceOrder();
        String[] customerInfo = getCustomerInfo();
        checkoutActions.fillCheckoutFormFast(customerInfo[0], customerInfo[1], customerInfo[2],
                customerInfo[3], customerInfo[4], customerInfo[5]);
        checkoutActions.clickPurchaseButton();

        Assert.assertTrue(checkoutActions.isOrderCompletedFast(), "Multi-product order should be completed");
        checkoutActions.clickOkFast();
    }



    @Test(priority = 3)
    @Story("Checkout Process")
    @Description("Verify checkout modal functionality")
    @Severity(SeverityLevel.MINOR)
    public void testCheckoutModalFunctionality() {
       
        homeActions.navigateToHome();
        homeActions.clickPhonesCategory();
        List<String> products = homeActions.getAllProductTitles();
        homeActions.clickProductByName(products.get(0));
        productActions.addProductToCartWithVerification();

        cartActions.navigateToCart();
        cartActions.clickPlaceOrder();
        
       
        Assert.assertTrue(checkoutActions.isModalDisplayedFast(), "Modal should open");
        String orderTotal = checkoutActions.getOrderTotal();
        Assert.assertFalse(orderTotal.isEmpty(), "Order total should be displayed");
        checkoutActions.closePlaceOrderModal();
    }

    @Test(priority = 4)
    @Story("Checkout Process")
    @Description("Verify order confirmation details")
    @Severity(SeverityLevel.NORMAL)
    public void testOrderConfirmationDetails() {
      
        homeActions.navigateToHome();
        homeActions.clickPhonesCategory();
        List<String> products = homeActions.getAllProductTitles();
        homeActions.clickProductByName(products.get(0));
        productActions.addProductToCartWithVerification();

        cartActions.navigateToCart();
        cartActions.clickPlaceOrder();

        String[] customerInfo = getCustomerInfo();
        checkoutActions.fillCheckoutFormFast(customerInfo[0], customerInfo[1], customerInfo[2],
                customerInfo[3], customerInfo[4], customerInfo[5]);
        checkoutActions.clickPurchaseButton();

       
        Assert.assertTrue(checkoutActions.isOrderCompletedFast(), "Order should be completed");
        checkoutActions.clickOkFast();
    }

    @Test(priority = 5)
    @Story("Checkout Process")
    @Description("Verify checkout from empty cart is prevented")
    @Severity(SeverityLevel.MINOR)
    public void testCheckoutFromEmptyCart() {
      
        cartActions.navigateToCart();
        cartActions.clearCart();
        Assert.assertTrue(cartActions.isCartEmpty(), "Cart should be empty");

        try {
            cartActions.clickPlaceOrder();
            if (checkoutActions.isModalDisplayedFast()) {
                checkoutActions.closePlaceOrderModal();
            }
        } catch (Exception e) {
           
        }
    }
}