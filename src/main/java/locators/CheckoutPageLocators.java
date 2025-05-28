package locators;

/**
 * Locators for Checkout Process functionality
 */
public class CheckoutPageLocators {

    // Place Order Modal
    public static final String PLACE_ORDER_MODAL = "//div[@id='orderModal']";
    public static final String PLACE_ORDER_MODAL_TITLE = "//h4[@class='modal-title' and text()='Place order']";

    // Order Form Fields
    public static final String CUSTOMER_NAME = "//input[@id='name']";
    public static final String CUSTOMER_COUNTRY = "//input[@id='country']";
    public static final String CUSTOMER_CITY = "//input[@id='city']";
    public static final String CREDIT_CARD = "//input[@id='card']";
    public static final String CARD_MONTH = "//input[@id='month']";
    public static final String CARD_YEAR = "//input[@id='year']";

    // Form Labels
    public static final String NAME_LABEL = "//label[@for='name']";
    public static final String COUNTRY_LABEL = "//label[@for='country']";
    public static final String CITY_LABEL = "//label[@for='city']";
    public static final String CARD_LABEL = "//label[@for='card']";
    public static final String MONTH_LABEL = "//label[@for='month']";
    public static final String YEAR_LABEL = "//label[@for='year']";

    // Order Total in Modal
    public static final String ORDER_TOTAL_LABEL = "//label[contains(text(),'Total:')]";
    public static final String ORDER_TOTAL_AMOUNT = "//label[@id='totalm']";

    // Modal Actions
    public static final String PURCHASE_BUTTON = "//button[@onclick='purchaseOrder()']";
    public static final String CLOSE_ORDER_MODAL = "//div[@id='orderModal']//button[@class='close']";
    public static final String CLOSE_ORDER_BUTTON = "//div[@id='orderModal']//button[text()='Close']";

    // Order Confirmation
    public static final String ORDER_CONFIRMATION_MODAL = "//div[@class='sweet-alert show-sweet-alert visible']";
    public static final String CONFIRMATION_TITLE = "//h2[contains(text(),'Thank you for your purchase!')]";
    public static final String CONFIRMATION_MESSAGE = "//p[@class='lead text-muted']";
    public static final String ORDER_ID = "//p[contains(text(),'Id:')]";
    public static final String ORDER_AMOUNT = "//p[contains(text(),'Amount:')]";
    public static final String ORDER_CARD_NUMBER = "//p[contains(text(),'Card Number:')]";
    public static final String ORDER_NAME = "//p[contains(text(),'Name:')]";
    public static final String ORDER_DATE = "//p[contains(text(),'Date:')]";

    // Confirmation Actions
    public static final String OK_BUTTON = "//button[@class='confirm btn btn-lg btn-primary']";
    public static final String CONFIRMATION_CLOSE = "//button[text()='OK']";

    // Form Validation
    public static final String REQUIRED_FIELD_ERROR = "//div[@class='invalid-feedback']";
    public static final String FORM_VALIDATION_MESSAGE = "//div[@class='alert alert-danger']";
}