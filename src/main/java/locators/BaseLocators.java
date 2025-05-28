package locators;

/**
 * Base locators class containing common elements across pages
 */
public class BaseLocators {

    // Navigation Menu
    public static final String HOME_LINK = "//a[@class='nav-link' and text()='Home']";
    public static final String CONTACT_LINK = "//a[@data-target='#exampleModal']";
    public static final String ABOUT_US_LINK = "//a[@data-target='#videoModal']";
    public static final String CART_LINK = "//a[@id='cartur']";
    public static final String CART_LINK2 = "//*[@id=\"navbarExample\"]/ul/li[4]";
    public static final String LOGIN_LINK = "//a[@id='login2']";
    public static final String SIGN_UP_LINK = "//a[@id='signin2']";
    public static final String LOGOUT_LINK = "//a[@id='logout2']";
    public static final String LOGGED_USER = "//a[@id='nameofuser']";

    // Common Modal Elements
    public static final String MODAL_CLOSE_X = "//button[@class='close']";
    public static final String MODAL_CLOSE_BUTTON = "//button[text()='Close']";

    // Loading and Alert Elements
    public static final String LOADING_SPINNER = "//div[@class='spinner-border']";
    public static final String SUCCESS_ALERT = "//div[@class='alert alert-success']";
    public static final String ERROR_ALERT = "//div[@class='alert alert-danger']";

    // Footer
    public static final String FOOTER = "//footer";

    // Logo
    public static final String DEMOBLAZE_LOGO = "//a[@class='navbar-brand']";
}