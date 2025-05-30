package actions;

import locators.CheckoutPageLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Actions for Checkout Process functionality
 */
public class CheckoutPageActions extends BaseActions {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutPageActions.class);

    public CheckoutPageActions(WebDriver driver) {
        super(driver);
    }

    /**
     * Wait for Place Order modal to be visible
     */
    public void waitForPlaceOrderModal() {
        logger.debug("Waiting for Place Order modal to be visible");
        waitForElementVisible(CheckoutPageLocators.PLACE_ORDER_MODAL);
        logger.info("Place Order modal is now visible");
    }

    /**
     * Enter customer name
     */
    public void enterCustomerName(String name) {
        logger.debug("Entering customer name: {}", name);
        waitForElementVisible(CheckoutPageLocators.CUSTOMER_NAME);
        typeText(CheckoutPageLocators.CUSTOMER_NAME, name);
        logger.info("Customer name entered successfully: {}", name);
    }

    /**
     * Enter customer country
     */
    public void enterCustomerCountry(String country) {
        logger.debug("Entering customer country: {}", country);
        waitForElementVisible(CheckoutPageLocators.CUSTOMER_COUNTRY);
        typeText(CheckoutPageLocators.CUSTOMER_COUNTRY, country);
        logger.info("Customer country entered successfully: {}", country);
    }

    /**
     * Enter customer city
     */
    public void enterCustomerCity(String city) {
        logger.debug("Entering customer city: {}", city);
        waitForElementVisible(CheckoutPageLocators.CUSTOMER_CITY);
        typeText(CheckoutPageLocators.CUSTOMER_CITY, city);
        logger.info("Customer city entered successfully: {}", city);
    }

    /**
     * Enter credit card number
     */
    public void enterCreditCard(String cardNumber) {
        logger.debug("Entering credit card number");
        waitForElementVisible(CheckoutPageLocators.CREDIT_CARD);
        typeText(CheckoutPageLocators.CREDIT_CARD, cardNumber);
        logger.info("Credit card number entered successfully");
    }

    /**
     * Enter card expiry month
     */
    public void enterCardMonth(String month) {
        logger.debug("Entering card expiry month: {}", month);
        waitForElementVisible(CheckoutPageLocators.CARD_MONTH);
        typeText(CheckoutPageLocators.CARD_MONTH, month);
        logger.info("Card expiry month entered successfully: {}", month);
    }

    /**
     * Enter card expiry year
     */
    public void enterCardYear(String year) {
        logger.debug("Entering card expiry year: {}", year);
        waitForElementVisible(CheckoutPageLocators.CARD_YEAR);
        typeText(CheckoutPageLocators.CARD_YEAR, year);
        logger.info("Card expiry year entered successfully: {}", year);
    }

    /**
     * Get order total from modal
     */
    public String getOrderTotal() {
        try {
            logger.debug("Getting order total from modal");
            waitForElementVisible(CheckoutPageLocators.ORDER_TOTAL_AMOUNT);
            String total = getText(CheckoutPageLocators.ORDER_TOTAL_AMOUNT);
            logger.info("Order total retrieved: {}", total);
            return total;
        } catch (Exception e) {
            logger.error("Error getting order total: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Click Purchase button
     */
    public void clickPurchaseButton() {
        try {
            logger.info("Clicking Purchase button");
            waitForElementVisible(CheckoutPageLocators.PURCHASE_BUTTON);
            clickElement(CheckoutPageLocators.PURCHASE_BUTTON);
            logger.info("Purchase button clicked successfully");
        } catch (Exception e) {
            logger.error("Error clicking purchase button: {}", e.getMessage());
            throw new RuntimeException("Failed to click purchase button", e);
        }
    }

    /**
     * Fill complete order form
     */
    public void fillOrderForm(String name, String country, String city, String cardNumber, String month, String year) {
        logger.info("Filling order form with customer details");
        waitForPlaceOrderModal();
        enterCustomerName(name);
        enterCustomerCountry(country);
        enterCustomerCity(city);
        enterCreditCard(cardNumber);
        enterCardMonth(month);
        enterCardYear(year);
        logger.info("Order form filled completely");
    }

    /**
     * Complete purchase process
     */
    public void completePurchase(String name, String country, String city, String cardNumber, String month, String year) {
        logger.info("Starting complete purchase process");
        fillOrderForm(name, country, city, cardNumber, month, year);
        clickPurchaseButton();
        logger.info("Purchase process completed successfully");
    }

    /**
     * Wait for order confirmation modal
     */
    public void waitForOrderConfirmation() {
        try {
            // Try multiple locator strategies for order confirmation
            String[] confirmationLocators = {
                CheckoutPageLocators.ORDER_CONFIRMATION_MODAL,
                "//div[contains(@class,'sweet-alert')]",
                "//div[contains(@class,'swal')]",
                "//div[@class='sweet-alert show-sweet-alert visible']",
                "//div[@class='swal-modal']",
                "//div[contains(@class,'alert') and contains(@class,'success')]",
                "//h2[contains(text(),'Thank you')]",
                "//div[contains(text(),'Thank you for your purchase')]"
            };
            
            boolean confirmationFound = false;
            for (String locator : confirmationLocators) {
                try {
                    waitForElementVisible(locator);
                    confirmationFound = true;
                    break;
                } catch (Exception e) {
                    // Try next locator
                }
            }
            
            if (!confirmationFound) {
                // Wait for confirmation using proper WebDriver wait
                try {
                    WebDriverWait confirmationWait = new WebDriverWait(driver, Duration.ofSeconds(8));
                    confirmationWait.until(webDriver -> {
                        for (String locator : confirmationLocators) {
                            try {
                                if (isElementVisible(locator)) {
                                    return true;
                                }
                            } catch (Exception e) {
                                // Continue trying other locators
                            }
                        }
                        return false;
                    });
                    confirmationFound = true;
                } catch (TimeoutException e) {
                    // Continue without confirmation
                }
                
                // Final attempt with individual locator checks
                if (!confirmationFound) {
                    for (String locator : confirmationLocators) {
                        try {
                            if (isElementVisible(locator)) {
                                confirmationFound = true;
                                break;
                            }
                        } catch (Exception e) {
                            // Continue trying
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Continue without throwing
        }
    }

    /**
     * Get order confirmation message
     */
    public String getOrderConfirmationMessage() {
        try {
            logger.debug("Getting order confirmation message");
            waitForOrderConfirmation();
            String confirmationText = getText(CheckoutPageLocators.CONFIRMATION_MESSAGE);
            logger.info("Order confirmation message retrieved: {}", confirmationText);
            return confirmationText;
        } catch (Exception e) {
            logger.error("Error getting confirmation message: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Get order ID from confirmation
     */
    public String getOrderId() {
        try {
            waitForOrderConfirmation();
            
            // Try multiple approaches to get order ID
            String orderIdText = "";
            
            try {
                orderIdText = getText(CheckoutPageLocators.ORDER_ID);
            } catch (Exception e) {
                // Try alternative approaches to get the confirmation text
                String[] confirmationLocators = {
                    "//div[contains(@class,'sweet-alert')]",
                    "//div[contains(@class,'swal')]",
                    "//p[@class='lead text-muted']",
                    "//*[contains(text(),'Id:')]"
                };
                
                for (String locator : confirmationLocators) {
                    try {
                        orderIdText = getText(locator);
                        if (!orderIdText.isEmpty()) {
                            break;
                        }
                    } catch (Exception ex) {
                        // Try next locator
                    }
                }
            }
            
            if (orderIdText.isEmpty()) {
                return "";
            }
            
            // Extract just the ID number from the text
            String orderId = "";
            
            // Look for "Id: " followed by digits
            if (orderIdText.contains("Id: ")) {
                String afterId = orderIdText.substring(orderIdText.indexOf("Id: ") + 4);
                // Extract only the digits from the line containing the ID
                String[] lines = afterId.split("\\n");
                if (lines.length > 0) {
                    // Get the first line after "Id: " and extract digits
                    orderId = lines[0].replaceAll("[^0-9]", "").trim();
                }
            } else {
                // If no "Id: " prefix, try to extract the first sequence of digits from the text
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d+");
                java.util.regex.Matcher matcher = pattern.matcher(orderIdText);
                if (matcher.find()) {
                    orderId = matcher.group();
                }
            }
            
            return orderId;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get order amount from confirmation
     */
    public String getOrderAmount() {
        try {
            waitForOrderConfirmation();
            
            // Try multiple approaches to get order amount
            String amountText = "";
            
            try {
                amountText = getText(CheckoutPageLocators.ORDER_AMOUNT);
            } catch (Exception e) {
                // Try to get from full confirmation text
                String[] confirmationLocators = {
                    "//div[contains(@class,'sweet-alert')]",
                    "//div[contains(@class,'swal')]",
                    "//p[@class='lead text-muted']",
                    "//*[contains(text(),'Amount:')]"
                };
                
                for (String locator : confirmationLocators) {
                    try {
                        String fullText = getText(locator);
                        if (fullText.contains("Amount:")) {
                            amountText = fullText;
                            break;
                        }
                    } catch (Exception ex) {
                        // Try next locator
                    }
                }
            }
            
            if (amountText.isEmpty()) {
                return "";
            }
            
            // Extract just the amount from the text
            String amount = "";
            
            if (amountText.contains("Amount:")) {
                String afterAmount = amountText.substring(amountText.indexOf("Amount:") + 7);
                // Get the first line after "Amount:" and extract the amount
                String[] lines = afterAmount.split("\\n");
                if (lines.length > 0) {
                    amount = lines[0].trim();
                }
            } else {
                // If it's already just the amount
                amount = amountText.trim();
            }
            
            return amount;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get order date from confirmation
     */
    public String getOrderDate() {
        try {
            waitForOrderConfirmation();
            String dateText = getText(CheckoutPageLocators.ORDER_DATE);
            return dateText;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Click OK button to close confirmation
     */
    public void clickOkButton() {
        try {
            waitForElementVisible(CheckoutPageLocators.OK_BUTTON);
            clickElement(CheckoutPageLocators.OK_BUTTON);
        } catch (Exception e) {
            // Continue silently
        }
    }

    /**
     * Check if Place Order modal is displayed
     */
    public boolean isPlaceOrderModalDisplayed() {
        try {
            // Try multiple locator strategies for Place Order modal
            String[] modalLocators = {
                CheckoutPageLocators.PLACE_ORDER_MODAL,
                "//div[@id='orderModal']",
                "//div[contains(@class,'modal') and contains(@style,'display: block')]",
                "//div[@class='modal fade show']",
                "//h4[contains(text(),'Place order')]",
                "//div[contains(@class,'modal-dialog')]"
            };
            
            // First quick check
            for (String locator : modalLocators) {
                try {
                    if (isElementVisible(locator)) {
                        logger.debug("Place Order modal found with locator: {}", locator);
                        return true;
                    }
                } catch (Exception e) {
                    logger.debug("Modal locator {} failed: {}", locator, e.getMessage());
                }
            }
            
            // If not found immediately, wait for modal animation using proper WebDriverWait
            try {
                WebDriverWait modalWait = new WebDriverWait(driver, Duration.ofSeconds(3));
                modalWait.until(webDriver -> {
                    for (String locator : modalLocators) {
                        try {
                            if (isElementVisible(locator)) {
                                return true;
                            }
                        } catch (Exception e) {
                            // Continue trying other locators
                        }
                    }
                    return false;
                });
                
                logger.debug("Place Order modal found after waiting for animation");
                return true;
                
            } catch (TimeoutException e) {
                logger.debug("WebDriverWait timeout for Place Order modal animation");
            }
            
            // Final verification attempt after wait
            for (String locator : modalLocators) {
                try {
                    if (isElementVisible(locator)) {
                        logger.debug("Place Order modal found on final verification with locator: {}", locator);
                        return true;
                    }
                } catch (Exception e) {
                    logger.debug("Final verification locator {} failed: {}", locator, e.getMessage());
                }
            }
            
            logger.debug("Place Order modal not displayed with any locator");
            return false;
        } catch (Exception e) {
            logger.error("Error checking Place Order modal display: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if order confirmation modal is displayed
     */
    public boolean isOrderConfirmationDisplayed() {
        boolean isDisplayed = isElementVisible(CheckoutPageLocators.ORDER_CONFIRMATION_MODAL);
        logger.debug("Order confirmation modal displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Close place order modal
     */
    public void closePlaceOrderModal() {
        try {
            if (isPlaceOrderModalDisplayed()) {
                logger.info("Closing place order modal");
                clickElement(CheckoutPageLocators.CLOSE_ORDER_MODAL);
                waitForElementToDisappear(CheckoutPageLocators.PLACE_ORDER_MODAL);
                logger.info("Place order modal closed successfully");
            }
        } catch (Exception e) {
            logger.warn("Could not close place order modal: {}", e.getMessage());
        }
    }

    /**
     * Get complete order confirmation details
     */
    public String getCompleteOrderDetails() {
        try {
            logger.info("Compiling complete order details");
            waitForOrderConfirmation();
            StringBuilder orderDetails = new StringBuilder();

            orderDetails.append("Order ID: ").append(getOrderId()).append("\n");
            orderDetails.append("Amount: ").append(getOrderAmount()).append("\n");
            orderDetails.append("Date: ").append(getOrderDate()).append("\n");

            String details = orderDetails.toString();
            logger.info("Complete order details compiled: {}", details);
            return details;
        } catch (Exception e) {
            logger.error("Error getting complete order details: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Verify order completion
     */
    public boolean verifyOrderCompletion() {
        try {
            logger.info("Verifying order completion");
            
            // Wait for any confirmation indicators
            waitForOrderConfirmation();
            
            // Check multiple indicators of successful order
            boolean hasConfirmation = false;
            boolean hasOrderId = false;
            boolean hasAmount = false;
            boolean hasThankYouMessage = false;
            
            try {
                hasConfirmation = isOrderConfirmationDisplayed();
            } catch (Exception e) {
                logger.debug("Confirmation modal check failed: {}", e.getMessage());
            }
            
            try {
                String orderId = getOrderId();
                hasOrderId = !orderId.isEmpty();
            } catch (Exception e) {
                logger.debug("Order ID check failed: {}", e.getMessage());
            }
            
            try {
                String amount = getOrderAmount();
                hasAmount = !amount.isEmpty();
            } catch (Exception e) {
                logger.debug("Order amount check failed: {}", e.getMessage());
            }
            
            // Check for thank you message with multiple approaches
            try {
                String[] thankYouLocators = {
                    "//h2[contains(text(),'Thank you')]",
                    "//div[contains(text(),'Thank you')]",
                    "//*[contains(text(),'Thank you for your purchase')]",
                    "//*[contains(text(),'purchase')]",
                    "//div[contains(@class,'sweet-alert')]//h2",
                    "//div[contains(@class,'swal')]//h2"
                };
                
                for (String locator : thankYouLocators) {
                    try {
                        if (isElementVisible(locator)) {
                            hasThankYouMessage = true;
                            logger.info("Found thank you message with locator: {}", locator);
                            break;
                        }
                    } catch (Exception e) {
                        logger.debug("Thank you locator {} failed: {}", locator, e.getMessage());
                    }
                }
            } catch (Exception e) {
                logger.debug("Thank you message check failed: {}", e.getMessage());
            }
            
            // Order is considered complete if we have at least one positive indicator
            boolean isComplete = hasConfirmation || hasOrderId || hasAmount || hasThankYouMessage;
            
            logger.info("Order completion verification result: {} (Confirmation: {}, OrderID: {}, Amount: {}, ThankYou: {})",
                    isComplete, hasConfirmation, hasOrderId, hasAmount, hasThankYouMessage);
            
            return isComplete;
        } catch (Exception e) {
            logger.error("Error verifying order completion: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Fill checkout form (streamlined)
     */
    public void fillCheckoutForm(String name, String country, String city, String creditCard, String month, String year) {
        logger.info("Filling checkout form");
        
        enterCustomerName(name);
        enterCustomerCountry(country);
        enterCustomerCity(city);
        enterCreditCard(creditCard);
        enterCardMonth(month);
        enterCardYear(year);
    }

    /**
     * Complete checkout process (streamlined)
     */
    public void completeCheckout(String name, String country, String city, String creditCard, String month, String year) {
        logger.info("Completing checkout process");
        
        fillCheckoutForm(name, country, city, creditCard, month, year);
        clickPurchaseButton();
        
        // Wait for confirmation
        waitForOrderConfirmation();
    }

    /**
     * Get order confirmation message (streamlined)
     */
    public String getOrderConfirmation() {
        waitForOrderConfirmation();
        return getOrderConfirmationMessage();
    }

    /**
     * Click OK on confirmation modal (streamlined)
     */
    public void clickOkOnConfirmation() {
        logger.info("Clicking OK on confirmation");
        
        String[] okButtonLocators = {
            CheckoutPageLocators.OK_BUTTON,
            CheckoutPageLocators.CONFIRMATION_CLOSE,
            "//button[text()='OK']",
            "//button[contains(@class,'confirm')]"
        };

        for (String locator : okButtonLocators) {
            if (isElementVisible(locator)) {
                clickElement(locator);
                waitForPageLoad();
                return;
            }
        }
    }

    /**
     * Is checkout modal displayed (streamlined)
     */
    public boolean isCheckoutModalDisplayed() {
        return isPlaceOrderModalDisplayed();
    }

    /**
     * Close checkout modal (streamlined)
     */
    public void closeCheckoutModal() {
        if (isElementVisible(CheckoutPageLocators.PLACE_ORDER_MODAL)) {
            clickElement(CheckoutPageLocators.CLOSE_ORDER_MODAL);
            waitForElementToDisappear(CheckoutPageLocators.PLACE_ORDER_MODAL);
        }
    }

    /**
     * Ultra-fast fill checkout form without complex validation
     */
    public void fillCheckoutFormFast(String name, String country, String city, String creditCard, String month, String year) {
        try {
            // Direct form filling without waits
            typeText(CheckoutPageLocators.CUSTOMER_NAME, name);
            typeText(CheckoutPageLocators.CUSTOMER_COUNTRY, country);
            typeText(CheckoutPageLocators.CUSTOMER_CITY, city);
            typeText(CheckoutPageLocators.CREDIT_CARD, creditCard);
            typeText(CheckoutPageLocators.CARD_MONTH, month);
            typeText(CheckoutPageLocators.CARD_YEAR, year);
        } catch (Exception e) {
            // Use fallback method if fast method fails
            fillCheckoutForm(name, country, city, creditCard, month, year);
        }
    }

    /**
     * Ultra-fast order completion check
     */
    public boolean isOrderCompletedFast() {
        // Quick check for common success indicators
        try {
            // Check for thank you message first (most common)
            if (isElementVisible("//h2[contains(text(),'Thank you')]")) {
                return true;
            }
            
            // Check for sweet alert modal
            if (isElementVisible("//div[contains(@class,'sweet-alert')]")) {
                return true;
            }
            
            // Check for order confirmation
            if (isElementVisible(CheckoutPageLocators.ORDER_CONFIRMATION_MODAL)) {
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Ultra-fast modal detection
     */
    public boolean isModalDisplayedFast() {
        try {
            return isElementVisible(CheckoutPageLocators.PLACE_ORDER_MODAL) || 
                   isElementVisible("//div[@id='orderModal']");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Ultra-fast OK button click
     */
    public void clickOkFast() {
        try {
            // Try direct click first
            clickElement(CheckoutPageLocators.OK_BUTTON);
        } catch (Exception e) {
            try {
                // Try alternative locator
                clickElement("//button[text()='OK']");
            } catch (Exception ex) {
                // Continue silently if can't close
            }
        }
    }
}