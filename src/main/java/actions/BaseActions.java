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

    public BaseActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    /**
     * Wait for page to be fully loaded
     */
    protected void waitForPageLoad() {
        wait.until(webDriver ->
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        logger.debug("Page fully loaded");
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
     * Wait for element to be present and visible
     */
    protected void waitForElementToBeVisible(String locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
        logger.debug("Element visible: {}", locator);
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
     * Wait for alert to be present and return its text
     */
    protected String waitForAlertAndGetText() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            logger.info("Alert present with text: {}", alertText);
            return alertText;
        } catch (TimeoutException e) {
            logger.debug("No alert appeared within timeout");
            return "";
        }
    }

    /**
     * Wait for element to be clickable and then click
     */
    protected void waitAndClick(String locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
        element.click();
        logger.info("Clicked element: {}", locator);
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
     * Click element with wait
     */
    protected void clickElement(String locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
            element.click();
            logger.info("Clicked element: {}", locator);
        } catch (TimeoutException e) {
            logger.error("Element not clickable within timeout: {}", locator);
            throw new RuntimeException("Element not clickable: " + locator, e);
        }
    }

    /**
     * Type text into element
     */
    protected void typeText(String locator, String text) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
            element.clear();
            element.sendKeys(text);
            logger.info("Typed text '{}' into element: {}", text, locator);
        } catch (TimeoutException e) {
            logger.error("Element not visible within timeout: {}", locator);
            throw new RuntimeException("Element not visible: " + locator, e);
        }
    }

    /**
     * Get text from element
     */
    protected String getText(String locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
            String text = element.getText();
            logger.debug("Retrieved text '{}' from element: {}", text, locator);
            return text;
        } catch (TimeoutException e) {
            logger.error("Element not visible within timeout: {}", locator);
            throw new RuntimeException("Element not visible: " + locator, e);
        }
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
            WebElement element = driver.findElement(By.xpath(locator));
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Wait for element to be visible
     */
    protected WebElement waitForElementVisible(String locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
    }

    /**
     * Wait for element to be clickable
     */
    protected WebElement waitForElementClickable(String locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
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
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
            logger.info("Alert accepted");
        } catch (TimeoutException e) {
            logger.warn("No alert present to accept");
        }
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
        WebElement element = waitForElementVisible(locator);
        return element.getAttribute(attribute);
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
}