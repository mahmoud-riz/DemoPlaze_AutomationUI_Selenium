package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Random;

/**
 * Test Data Provider class for managing test data
 */
public class TestDataProvider {
    private static final System.Logger logger = System.getLogger(TestDataProvider.class.getName());
    private static JsonNode testData;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Random random = new Random();

    static {
        loadTestData();
    }

    /**
     * Load test data from JSON file
     */
    private static void loadTestData() {
        try {
            String testDataFile = ConfigReader.getTestDataFile();
            InputStream inputStream = TestDataProvider.class.getClassLoader().getResourceAsStream(testDataFile);

            if (inputStream != null) {
                testData = objectMapper.readTree(inputStream);
                System.out.println("Test data loaded successfully from: " + testDataFile);
            } else {
                System.out.println("Test data file '" + testDataFile + "' not found. Creating default test data.");
                createDefaultTestData();
            }
        } catch (Exception e) {
            System.err.println("Error loading test data: " + e.getMessage());
            createDefaultTestData();
        }
    }

    /**
     * Create default test data if file is not found
     */
    private static void createDefaultTestData() {
        try {
            // Create a simple default data structure
            String defaultJson = createDefaultJsonString();
            testData = objectMapper.readTree(defaultJson);
            System.out.println("Default test data created");
        } catch (Exception e) {
            System.err.println("Error creating default test data: " + e.getMessage());
            throw new RuntimeException("Failed to initialize test data", e);
        }
    }

    /**
     * Create default JSON string using StringBuilder (Java 11 compatible)
     */
    private static String createDefaultJsonString() {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"users\": {\n");
        json.append("    \"validUser\": {\n");
        json.append("      \"username\": \"testuser123\",\n");
        json.append("      \"password\": \"testpass123\"\n");
        json.append("    },\n");
        json.append("    \"invalidUser\": {\n");
        json.append("      \"username\": \"invaliduser\",\n");
        json.append("      \"password\": \"wrongpass\"\n");
        json.append("    }\n");
        json.append("  },\n");
        json.append("  \"products\": {\n");
        json.append("    \"phones\": [\"Samsung galaxy s6\", \"Nokia lumia 1520\", \"Nexus 6\"],\n");
        json.append("    \"laptops\": [\"Sony vaio i5\", \"Sony vaio i7\", \"MacBook air\"],\n");
        json.append("    \"monitors\": [\"Apple monitor 24\", \"ASUS Full HD\"]\n");
        json.append("  },\n");
        json.append("  \"checkout\": {\n");
        json.append("    \"customerInfo\": {\n");
        json.append("      \"name\": \"John Doe\",\n");
        json.append("      \"country\": \"United States\",\n");
        json.append("      \"city\": \"New York\",\n");
        json.append("      \"creditCard\": \"1234567890123456\",\n");
        json.append("      \"month\": \"12\",\n");
        json.append("      \"year\": \"2025\"\n");
        json.append("    }\n");
        json.append("  },\n");
        json.append("  \"categories\": [\"Phones\", \"Laptops\", \"Monitors\"]\n");
        json.append("}");
        return json.toString();
    }

    /**
     * Get test data by path (e.g., "users.validUser.username")
     */
    public static String getTestData(String path) {
        try {
            String[] pathParts = path.split("\\.");
            JsonNode current = testData;

            for (String part : pathParts) {
                current = current.get(part);
                if (current == null) {
                    System.out.println("WARNING: Test data path '" + path + "' not found");
                    return "";
                }
            }

            return current.asText();
        } catch (Exception e) {
            System.err.println("Error getting test data for path: " + path + " - " + e.getMessage());
            return "";
        }
    }

    /**
     * Get valid user credentials
     */
    public static String[] getValidUserCredentials() {
        String username = getTestData("users.validUser.username");
        String password = getTestData("users.validUser.password");
        return new String[]{username, password};
    }

    /**
     * Get invalid user credentials
     */
    public static String[] getInvalidUserCredentials() {
        String username = getTestData("users.invalidUser.username");
        String password = getTestData("users.invalidUser.password");
        return new String[]{username, password};
    }

    /**
     * Generate unique username
     */
    public static String generateUniqueUsername() {
        long timestamp = System.currentTimeMillis();
        int randomNum = random.nextInt(1000);
        String username = "user_" + timestamp + "_" + randomNum;
        System.out.println("Generated unique username: " + username);
        return username;
    }

    /**
     * Generate unique password
     */
    public static String generateUniquePassword() {
        long timestamp = System.currentTimeMillis();
        String password = "pass_" + timestamp;
        System.out.println("Generated unique password");
        return password;
    }

    /**
     * Get random product name by category
     */
    public static String getRandomProduct(String category) {
        try {
            JsonNode products = testData.get("products").get(category.toLowerCase());
            if (products != null && products.isArray()) {
                int randomIndex = random.nextInt(products.size());
                String product = products.get(randomIndex).asText();
                System.out.println("Selected random " + category + " product: " + product);
                return product;
            }
        } catch (Exception e) {
            System.err.println("Error getting random product for category: " + category + " - " + e.getMessage());
        }
        return "";
    }

    /**
     * Get customer information for checkout
     */
    public static String[] getCustomerInfo() {
        String name = getTestData("checkout.customerInfo.name");
        String country = getTestData("checkout.customerInfo.country");
        String city = getTestData("checkout.customerInfo.city");
        String creditCard = getTestData("checkout.customerInfo.creditCard");
        String month = getTestData("checkout.customerInfo.month");
        String year = getTestData("checkout.customerInfo.year");

        return new String[]{name, country, city, creditCard, month, year};
    }

    /**
     * Get all available categories
     */
    public static String[] getCategories() {
        try {
            JsonNode categories = testData.get("categories");
            if (categories != null && categories.isArray()) {
                String[] categoryArray = new String[categories.size()];
                for (int i = 0; i < categories.size(); i++) {
                    categoryArray[i] = categories.get(i).asText();
                }
                return categoryArray;
            }
        } catch (Exception e) {
            System.err.println("Error getting categories: " + e.getMessage());
        }
        return new String[0];
    }

    /**
     * Get random category
     */
    public static String getRandomCategory() {
        String[] categories = getCategories();
        if (categories.length > 0) {
            String category = categories[random.nextInt(categories.length)];
            System.out.println("Selected random category: " + category);
            return category;
        }
        return "";
    }

    /**
     * Generate random email
     */
    public static String generateRandomEmail() {
        long timestamp = System.currentTimeMillis();
        int randomNum = random.nextInt(1000);
        return "test_" + timestamp + "_" + randomNum + "@test.com";
    }

    /**
     * Generate random phone number
     */
    public static String generateRandomPhoneNumber() {
        StringBuilder phone = new StringBuilder("+1");
        for (int i = 0; i < 10; i++) {
            phone.append(random.nextInt(10));
        }
        return phone.toString();
    }
}