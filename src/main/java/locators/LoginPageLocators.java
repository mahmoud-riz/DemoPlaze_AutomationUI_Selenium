package locators;

/**
 * Corrected locators for Login and Registration functionality on DemoBlaze
 */
public class LoginPageLocators {

    // Sign Up Modal Elements (Corrected)
    public static final String SIGNUP_MODAL = "//div[@id='signInModal']";
    public static final String SIGNUP_USERNAME = "//input[@id='sign-username']";
    public static final String SIGNUP_PASSWORD = "//input[@id='sign-password']";
    public static final String SIGNUP_BUTTON = "//button[@onclick='register()']";
    public static final String SIGNUP_MODAL_TITLE = "//h4[@class='modal-title' and text()='Sign up']";

    // Login Modal Elements (Corrected)
    public static final String LOGIN_MODAL = "//div[@id='logInModal']";
    public static final String LOGIN_USERNAME = "//input[@id='loginusername']";
    public static final String LOGIN_PASSWORD = "//input[@id='loginpassword']";
    public static final String LOGIN_BUTTON = "//button[@onclick='logIn()']";
    public static final String LOGIN_MODAL_TITLE = "//h4[@class='modal-title' and text()='Log in']";

    // Modal Close Buttons (Multiple options)
    public static final String SIGNUP_MODAL_CLOSE = "//div[@id='signInModal']//button[@class='close']";
    public static final String LOGIN_MODAL_CLOSE = "//div[@id='logInModal']//button[@class='close']";

    // Modal States
    public static final String MODAL_BACKDROP = "//div[@class='modal-backdrop fade show']";
}