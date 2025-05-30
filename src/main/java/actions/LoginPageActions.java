package actions;

import locators.BaseLocators;
import locators.LoginPageLocators;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Actions for Login and Registration functionality
 */
public class LoginPageActions extends BaseActions {
    private static final Logger logger = LoggerFactory.getLogger(LoginPageActions.class);

    public LoginPageActions(WebDriver driver) {
        super(driver);
    }

    /**
     * Click Sign Up link to open registration modal
     */
    public void clickSignUpLink() {
        clickElement(BaseLocators.SIGN_UP_LINK);
        waitForElementVisible(LoginPageLocators.SIGNUP_MODAL);
        logger.info("Sign up modal opened");
    }

    /**
     * Enter username in sign up form
     */
    public void enterSignUpUsername(String username) {
        waitForElementVisible(LoginPageLocators.SIGNUP_USERNAME);
        typeText(LoginPageLocators.SIGNUP_USERNAME, username);
        logger.debug("Entered sign up username: {}", username);
    }

    /**
     * Enter password in sign up form
     */
    public void enterSignUpPassword(String password) {
        waitForElementVisible(LoginPageLocators.SIGNUP_PASSWORD);
        typeText(LoginPageLocators.SIGNUP_PASSWORD, password);
        logger.debug("Entered sign up password");
    }

    /**
     * Click Sign Up button
     */
    public void clickSignUpButton() {
        clickElement(LoginPageLocators.SIGNUP_BUTTON);
        logger.info("Sign up button clicked");
    }

    /**
     * Perform complete user registration (streamlined)
     */
    public void registerUser(String username, String password) {
        logger.info("Registering user: {}", username);
        
        clickSignUpLink();
        enterSignUpUsername(username);
        enterSignUpPassword(password);
        clickSignUpButton();

        // Handle registration alert
        String alertText = waitForAlertAndGetText();
        if (!alertText.isEmpty()) {
            logger.info("Registration alert: {}", alertText);
            acceptAlert();
        }

        // Close modal
        waitForElementToDisappear(LoginPageLocators.SIGNUP_MODAL);
        logger.info("Registration completed for: {}", username);
    }

    /**
     * Click Login link to open login modal
     */
    public void clickLoginLink() {
        clickElement(BaseLocators.LOGIN_LINK);
        waitForElementVisible(LoginPageLocators.LOGIN_MODAL);
        logger.info("Login modal opened");
    }

    /**
     * Enter username in login form
     */
    public void enterLoginUsername(String username) {
        waitForElementVisible(LoginPageLocators.LOGIN_USERNAME);
        typeText(LoginPageLocators.LOGIN_USERNAME, username);
        logger.debug("Entered login username: {}", username);
    }

    /**
     * Enter password in login form
     */
    public void enterLoginPassword(String password) {
        waitForElementVisible(LoginPageLocators.LOGIN_PASSWORD);
        typeText(LoginPageLocators.LOGIN_PASSWORD, password);
        logger.debug("Entered login password");
    }

    /**
     * Click Login button
     */
    public void clickLoginButton() {
        clickElement(LoginPageLocators.LOGIN_BUTTON);
        logger.info("Login button clicked");
    }

    /**
     * Perform complete user login (streamlined)
     */
    public void loginUser(String username, String password) {
        logger.info("Logging in user: {}", username);
        
        clickLoginLink();
        enterLoginUsername(username);
        enterLoginPassword(password);
        clickLoginButton();

        // Handle login alert if present
        String alertText = waitForAlertAndGetText();
        if (!alertText.isEmpty()) {
            logger.info("Login alert: {}", alertText);
            acceptAlert();
            if (alertText.toLowerCase().contains("wrong") || alertText.toLowerCase().contains("error")) {
                logger.warn("Login failed: {}", alertText);
                return;
            }
        }

        // Wait for login to complete
        waitForPageLoad();
        
        if (isUserLoggedIn()) {
            logger.info("Login successful for: {}", username);
        } else {
            logger.warn("Login verification failed for: {}", username);
        }
    }

    /**
     * Check if user is logged in (simplified)
     */
    public boolean isUserLoggedIn() {
        // Check primary indicator
        if (isElementVisible(BaseLocators.LOGGED_USER)) {
            return true;
        }
        
        // Check logout link as fallback
        if (isElementVisible(BaseLocators.LOGOUT_LINK)) {
            return true;
        }
        
        return false;
    }

    /**
     * Get logged in username (simplified)
     */
    public String getLoggedInUsername() {
        if (!isUserLoggedIn()) {
            return "";
        }
        
        String welcomeText = getText(BaseLocators.LOGGED_USER);
        return welcomeText.replace("Welcome ", "");
    }

    /**
     * Logout user (simplified)
     */
    public void logoutUser() {
        if (isUserLoggedIn()) {
            clickElement(BaseLocators.LOGOUT_LINK);
            waitForElementToDisappear(BaseLocators.LOGGED_USER);
            logger.info("User logged out successfully");
        }
    }

    /**
     * Close sign up modal (simplified)
     */
    public void closeSignUpModal() {
        if (isElementVisible(LoginPageLocators.SIGNUP_MODAL)) {
            clickElement(BaseLocators.MODAL_CLOSE_X);
            waitForElementToDisappear(LoginPageLocators.SIGNUP_MODAL);
        }
    }

    /**
     * Close login modal (simplified)
     */
    public void closeLoginModal() {
        if (isElementVisible(LoginPageLocators.LOGIN_MODAL)) {
            clickElement(BaseLocators.MODAL_CLOSE_X);
            waitForElementToDisappear(LoginPageLocators.LOGIN_MODAL);
        }
    }

    /**
     * Check if sign up modal is displayed
     */
    public boolean isSignUpModalDisplayed() {
        return isElementVisible(LoginPageLocators.SIGNUP_MODAL);
    }

    /**
     * Check if login modal is displayed
     */
    public boolean isLoginModalDisplayed() {
        return isElementVisible(LoginPageLocators.LOGIN_MODAL);
    }
}