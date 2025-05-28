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
     * Perform complete user registration
     */
    public void registerUser(String username, String password) {
        logger.info("Starting user registration for username: {}", username);
        clickSignUpLink();
        enterSignUpUsername(username);
        enterSignUpPassword(password);
        clickSignUpButton();

        // Wait for alert to appear and handle it
        String alertText = waitForAlertAndGetText();
        if (!alertText.isEmpty()) {
            acceptAlert();
            logger.info("Registration alert handled: {}", alertText);
        }

        // Wait for modal to close after registration
        waitForElementToDisappear(LoginPageLocators.SIGNUP_MODAL);
        closeSignUpModal();
        logger.info("User registration completed for: {}", username);
    }

    /**
     * Close sign up modal
     */
    public void closeSignUpModal() {
        try {
            if (isElementVisible(LoginPageLocators.SIGNUP_MODAL)) {
                clickElement(BaseLocators.MODAL_CLOSE_X);
                waitForElementToDisappear(LoginPageLocators.SIGNUP_MODAL);
                logger.info("Sign up modal closed");
            }
        } catch (Exception e) {
            logger.warn("Could not close sign up modal: {}", e.getMessage());
        }
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
     * Perform complete user login
     */
    public void loginUser(String username, String password) {
        logger.info("Starting user login for username: {}", username);
        //logger.info("Starting user login for password: {}", password);
        clickLoginLink();
        enterLoginUsername(username);
        enterLoginPassword(password);
        clickLoginButton();

        // Wait for login to complete - either success or failure
        waitForCondition(driver -> {
            // Check if user is logged in (success case)
            if (isUserLoggedIn()) {
                return true;
            }
            // Check if login modal is still visible (failure case)
            if (isLoginModalDisplayed()) {
                return true;
            }
            // Keep waiting
            return false;
        });

        // If login was successful, wait for modal to disappear
        if (isUserLoggedIn()) {
            waitForElementToDisappear(LoginPageLocators.LOGIN_MODAL);
            logger.info("User logged in successfully: {}", username);
        } else {
            logger.warn("User login failed for: {}", username);
        }
    }

    /**
     * Close login modal
     */
    public void closeLoginModal() {
        try {
            if (isElementVisible(LoginPageLocators.LOGIN_MODAL)) {
                clickElement(BaseLocators.MODAL_CLOSE_X);
                waitForElementToDisappear(LoginPageLocators.LOGIN_MODAL);
                logger.info("Login modal closed");
            }
        } catch (Exception e) {
            logger.warn("Could not close login modal: {}", e.getMessage());
        }
    }

    /**
     * Check if user is logged in
     */
    public boolean isUserLoggedIn() {

        boolean isLoggedIn = isElementVisible(BaseLocators.LOGGED_USER);
        logger.debug("User logged in status: {}", isLoggedIn);
        return isLoggedIn;
    }

    /**
     * Get logged in username
     */
    public String getLoggedInUsername() {
        if (isUserLoggedIn()) {
            String welcomeText = getText(BaseLocators.LOGGED_USER);
            String username = welcomeText.replace("Welcome ", "");
            logger.debug("Logged in username: {}", username);
            return username;
        }
        logger.debug("No user logged in");
        return "";
    }

    /**
     * Logout user
     */
    public void logoutUser() {
        if (isUserLoggedIn()) {
            String username = getLoggedInUsername();
            clickElement(BaseLocators.LOGOUT_LINK);
            waitForElementToDisappear(BaseLocators.LOGGED_USER);
            logger.info("User logged out successfully: {}", username);
        } else {
            logger.warn("No user to logout");
        }
    }

    /**
     * Check if sign up modal is displayed
     */
    public boolean isSignUpModalDisplayed() {
        boolean isDisplayed = isElementVisible(LoginPageLocators.SIGNUP_MODAL);
        logger.debug("Sign up modal displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Check if login modal is displayed
     */
    public boolean isLoginModalDisplayed() {
        boolean isDisplayed = isElementVisible(LoginPageLocators.LOGIN_MODAL);
        logger.debug("Login modal displayed: {}", isDisplayed);
        return isDisplayed;
    }
}