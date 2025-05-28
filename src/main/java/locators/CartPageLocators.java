package locators;

/**
 * Locators for Shopping Cart functionality
 */
public class CartPageLocators {

    // Cart Container
    public static final String CART_CONTAINER = "//div[@class='table-responsive']";
    public static final String CART_TABLE = "//table[@class='table table-bordered table-hover table-sm']";
    public static final String CART_TBODY = "//tbody[@id='tbodyid']";

    // Cart Items
    public static final String CART_ITEMS = "//tbody[@id='tbodyid']//tr";
    public static final String CART_ITEM_IMAGE = ".//td[1]//img";
    public static final String CART_ITEM_TITLE = ".//td[2]";
    public static final String CART_ITEM_PRICE = ".//td[3]";
    public static final String CART_DELETE_BUTTON = ".//td[4]//a";

    // Specific Item Operations
    public static final String DELETE_ITEM_BY_NAME = "//td[contains(text(),'%s')]/following-sibling::td//a[text()='Delete']";
    public static final String ITEM_ROW_BY_NAME = "//td[contains(text(),'%s')]/parent::tr";

    // Cart Summary
    public static final String TOTAL_AMOUNT = "//h3[@id='totalp']";
    public static final String CART_TOTAL_LABEL = "//label[text()='Total: ']";

    // Cart Actions
    public static final String PLACE_ORDER_BUTTON = "//button[@class='btn btn-success' and text()='Place Order']";
    public static final String CONTINUE_SHOPPING = "//a[text()='Continue Shopping']";

    // Empty Cart State
    public static final String EMPTY_CART_MESSAGE = "//div[contains(@class,'empty-cart')]";
    public static final String NO_ITEMS_MESSAGE = "//p[contains(text(),'No items in cart')]";

    // Cart Header
    public static final String CART_HEADER = "//h2[contains(text(),'Products')]";
    public static final String CART_COLUMNS = "//thead//th";

    // Loading States
    public static final String CART_LOADING = "//div[@class='spinner-border']";
}