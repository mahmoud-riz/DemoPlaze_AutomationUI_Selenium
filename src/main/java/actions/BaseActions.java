package actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.FluentWait;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * Base Actions class containing common functionality for all page actions
 */
public abstract class BaseActions {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected FluentWait<WebDriver> fluentWait;
    protected static final Logger logger = LoggerFactory.getLogger(BaseActions.class);

    // Ultra-fast wait times for maximum performance
    protected static final int DEFAULT_TIMEOUT = 3; // Reduced from 5
    protected static final int SHORT_TIMEOUT = 2;   // Reduced from 3
    protected static final int POLLING_INTERVAL = 100; // Reduced from 250

    public BaseActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Reduced from 10
        this.fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(8)) // Reduced from 15
                .pollingEvery(Duration.ofMillis(200)) // Reduced from 500
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    /**
     * Ultra-fast page load wait
     */
    protected void waitForPageLoad() {
        try {
            WebDriverWait fastWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            fastWait.until(webDriver ->
                    ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        } catch (TimeoutException e) {
            // Continue immediately if page doesn't load within 2 seconds
        }
    }

    /**
     * Ultra-fast element visibility wait
     */
    protected void waitForElementVisible(String locator) {
        try {
            WebDriverWait ultraFastWait = new WebDriverWait(driver, Duration.ofSeconds(1));
            ultraFastWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
        } catch (TimeoutException e) {
            // Continue immediately if element not visible within 1 second
        }
    }

    /**
     * Ultra-fast clickable element wait
     */
    protected WebElement waitForElementClickable(String locator) {
        try {
            WebDriverWait ultraFastWait = new WebDriverWait(driver, Duration.ofSeconds(1));
            return ultraFastWait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
        } catch (TimeoutException e) {
            // Return element directly if wait fails
            return driver.findElement(By.xpath(locator));
        }
    }

    /**
     * Ultra-fast alert wait
     */
    protected String waitForAlertAndGetText() {
        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(1));
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
            return alert.getText();
        } catch (TimeoutException e) {
            return "";
        }
    }

    /**
     * Fast click with minimal waits
     */
    protected void clickElement(String locator) {
        try {
            driver.findElement(By.xpath(locator)).click();
        } catch (Exception e) {
            // Fallback with brief wait
            try {
                waitForElementClickable(locator).click();
            } catch (Exception ex) {
                throw new RuntimeException("Could not click element: " + locator, ex);
            }
        }
    }

    /**
     * Fast text input
     */
    protected void typeText(String locator, String text) {
        try {
            WebElement element = driver.findElement(By.xpath(locator));
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            waitForElementVisible(locator);
            WebElement element = driver.findElement(By.xpath(locator));
            element.clear();
            element.sendKeys(text);
        }
    }

    /**
     * Fast text retrieval
     */
    protected String getText(String locator) {
        try {
            return driver.findElement(By.xpath(locator)).getText();
        } catch (Exception e) {
            waitForElementVisible(locator);
            return driver.findElement(By.xpath(locator)).getText();
        }
    }

    /**
     * Wait for AJAX requests to complete (if jQuery is available)
     */
    protected void waitForAjaxToComplete() {
        try {
            wait.until(webDriver ->
                    (Boolean) ((JavascriptExecutor) webDriver).executeScript("return typeof jQuery !== 'undefined' && jQuery.active == 0"));
            logger.debug("AJAX requests completed");
        } catch (TimeoutException e) {
            logger.debug("jQuery not available or AJAX check timeout");
        }
    }

    /**
     * Wait for any loading spinners to disappear
     */
    protected void waitForLoadingToComplete() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector(".spinner, .loading, .loader, [class*='loading'], [class*='spinner']")));
            logger.debug("Loading completed");
        } catch (TimeoutException e) {
            logger.debug("No loading spinner found or already completed");
        }
    }

    /**
     * Wait for element to disappear
     */
    protected void waitForElementToDisappear(String locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
        logger.debug("Element disappeared: {}", locator);
    }

    /**
     * Wait for modal to be fully loaded
     */
    protected void waitForModalToLoad(String modalLocator) {
        // Wait for modal to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(modalLocator)));
        // Additional wait for modal animations to complete
        try {
            wait.until(ExpectedConditions.attributeContains(By.xpath(modalLocator), "class", "show"));
        } catch (TimeoutException e) {
            // Some modals might not have 'show' class, continue anyway
            logger.debug("Modal visible but no 'show' class found");
        }
        logger.debug("Modal fully loaded: {}", modalLocator);
    }

    /**
     * Wait for URL to contain specific text
     */
    protected void waitForUrlToContain(String urlFragment) {
        wait.until(ExpectedConditions.urlContains(urlFragment));
        logger.debug("URL contains: {}", urlFragment);
    }

    /**
     * Wait for title to contain specific text
     */
    protected void waitForTitleToContain(String titleFragment) {
        wait.until(ExpectedConditions.titleContains(titleFragment));
        logger.debug("Title contains: {}", titleFragment);
    }

    /**
     * Wait for text to be present in element
     */
    protected void waitForTextToBePresentInElement(String locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath(locator), text));
        logger.debug("Text '{}' present in element: {}", text, locator);
    }

    /**
     * Wait for element count to be a specific number
     */
    protected void waitForElementCount(String locator, int expectedCount) {
        wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath(locator), expectedCount));
        logger.debug("Element count is {} for: {}", expectedCount, locator);
    }

    /**
     * Custom wait condition with fluent wait
     */
    protected <T> T waitForCondition(Function<WebDriver, T> condition) {
        return fluentWait.until(condition);
    }

    /**
     * Check if element is present
     */
    protected boolean isElementPresent(String locator) {
        try {
            driver.findElement(By.xpath(locator));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Check if element is visible
     */
    protected boolean isElementVisible(String locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1)); // Very short wait
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Get all elements matching locator
     */
    protected List<WebElement> getElements(String locator) {
        return driver.findElements(By.xpath(locator));
    }

    /**
     * Handle JavaScript alerts
     */
    protected void acceptAlert() {
        driver.switchTo().alert().accept();
        logger.debug("Alert accepted");
    }

    /**
     * Get alert text
     */
    protected String getAlertText() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            logger.info("Alert text: {}", alertText);
            return alertText;
        } catch (TimeoutException e) {
            logger.warn("No alert present");
            return "";
        }
    }

    /**
     * Scroll to element
     */
    protected void scrollToElement(String locator) {
        WebElement element = driver.findElement(By.xpath(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        logger.debug("Scrolled to element: {}", locator);
    }

    /**
     * Get attribute value
     */
    protected String getAttributeValue(String locator, String attribute) {
        try {
            waitForElementVisible(locator);
            WebElement element = driver.findElement(By.xpath(locator));
            return element.getAttribute(attribute);
        } catch (Exception e) {
            // Try direct access without wait
            WebElement element = driver.findElement(By.xpath(locator));
            return element.getAttribute(attribute);
        }
    }

    /**
     * Take screenshot on failure
     */
    protected byte[] takeScreenshot() {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
            return new byte[0];
        }
    }

    /**
     * Wait for dynamic content to load using proper WebDriver waits
     */
    protected void waitForDynamicContent() {
        try {
            // Wait for document ready state
            WebDriverWait dynamicWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            dynamicWait.until(webDriver ->
                    ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            
            // Wait for jQuery if available
            try {
                dynamicWait.until(webDriver ->
                        (Boolean) ((JavascriptExecutor) webDriver).executeScript("return typeof jQuery === 'undefined' || jQuery.active === 0"));
            } catch (Exception e) {
                // jQuery might not be available, continue
            }
            
        } catch (TimeoutException e) {
            // Continue if timeout - don't fail the test
            logger.debug("Dynamic content wait timeout - continuing");
        }
    }

    /**
     * Wait for any condition with custom timeout using proper WebDriver waits
     */
    protected boolean waitForConditionWithTimeout(Function<WebDriver, Boolean> condition, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return customWait.until(condition);
        } catch (TimeoutException e) {
            logger.debug("Custom condition wait timeout after {} seconds", timeoutSeconds);
            return false;
        }
    }

    /**
     * Wait for multiple elements with any one to be visible
     */
    protected boolean waitForAnyElementVisible(String[] locators, int timeoutSeconds) {
        try {
            WebDriverWait multiWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return multiWait.until(webDriver -> {
                for (String locator : locators) {
                    try {
                        if (isElementVisible(locator)) {
                            return true;
                        }
                    } catch (Exception e) {
                        // Continue checking other locators
                    }
                }
                return false;
            });
        } catch (TimeoutException e) {
            logger.debug("Multi-element visibility wait timeout after {} seconds", timeoutSeconds);
            return false;
        }
    }
}