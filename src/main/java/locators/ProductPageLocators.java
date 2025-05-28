package locators;

/**
 * Locators for Product Detail Page functionality
 */
public class ProductPageLocators {

    // Product Details
    public static final String PRODUCT_NAME = "//h2[@class='name']";
    public static final String PRODUCT_PRICE = "//h3[@class='price-container']";
    public static final String PRODUCT_IMAGE = "//img[@class='img-fluid']";
    public static final String PRODUCT_DESCRIPTION = "//div[@id='more-information']//p";

    // Product Actions
    public static final String ADD_TO_CART_BUTTON = "//a[@onclick='addToCart(1)' or contains(@onclick,'addToCart')]";
    public static final String PRODUCT_DETAILS_CONTAINER = "//div[@class='row']";

    // Breadcrumb or Navigation
    public static final String HOME_BREADCRUMB = "//a[text()='Home']";

    // Product Specifications (if available)
    public static final String PRODUCT_SPECS = "//div[@class='product-specs']";
    public static final String PRODUCT_FEATURES = "//ul[@class='product-features']";

    // Related Products
    public static final String RELATED_PRODUCTS = "//div[@class='related-products']";

    // Reviews Section (if available)
    public static final String REVIEWS_SECTION = "//div[@class='reviews']";
    public static final String REVIEW_COUNT = "//span[@class='review-count']";

    // Quantity Selector (if available)
    public static final String QUANTITY_INPUT = "//input[@type='number' and @name='quantity']";
    public static final String QUANTITY_PLUS = "//button[@class='quantity-plus']";
    public static final String QUANTITY_MINUS = "//button[@class='quantity-minus']";
}