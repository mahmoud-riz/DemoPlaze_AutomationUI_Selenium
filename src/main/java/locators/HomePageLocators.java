package locators;

/**
 * Locators for Home Page and Product browsing functionality
 */
public class HomePageLocators {

    // Categories
    public static final String CATEGORIES_TITLE = "//a[@id='cat']";
    public static final String PHONES_CATEGORY = "//a[@onclick=\"byCat('phone')\"]";
    public static final String LAPTOPS_CATEGORY = "//a[@onclick=\"byCat('notebook')\"]";
    public static final String MONITORS_CATEGORY = "//a[@onclick=\"byCat('monitor')\"]";

    // Product Grid
    public static final String PRODUCTS_CONTAINER = "//div[@id='tbodyid']";
    public static final String PRODUCT_CARDS = "//div[@class='card h-100']";
    public static final String PRODUCT_TITLES = "//h4[@class='card-title']//a";
    public static final String PRODUCT_PRICES = "//h5[contains(text(),'$')]";
    public static final String PRODUCT_DESCRIPTIONS = "//p[@class='card-text']";
    public static final String PRODUCT_IMAGES = "//img[@class='card-img-top img-fluid']";

    // Individual Product Elements (relative to product card)
    public static final String PRODUCT_LINK_BY_NAME = "//a[contains(text(),'%s')]";
    public static final String PRODUCT_CARD_BY_NAME = "//h4[@class='card-title']//a[contains(text(),'%s')]/ancestor::div[@class='card h-100']";
    public static final String PRODUCT_PRICE_BY_NAME = "//a[contains(text(),'%s')]/ancestor::div[@class='card-block']//h5";

    // Pagination
    public static final String PREVIOUS_BUTTON = "//button[@id='prev2']";
    public static final String NEXT_BUTTON = "//button[@id='next2']";

    // Carousel
    public static final String CAROUSEL = "//div[@id='carouselExampleIndicators']";
    public static final String CAROUSEL_INDICATORS = "//ol[@class='carousel-indicators']//li";
    public static final String CAROUSEL_SLIDES = "//div[@class='carousel-item']";
    public static final String CAROUSEL_PREV = "//a[@class='carousel-control-prev']";
    public static final String CAROUSEL_NEXT = "//a[@class='carousel-control-next']";

    // Search and Filter (if available)
    public static final String SEARCH_BOX = "//input[@placeholder='Search...']";
    public static final String SEARCH_BUTTON = "//button[contains(@class,'search')]";

    // Loading States
    public static final String PRODUCTS_LOADING = "//div[@class='spinner-border' and @role='status']";
}