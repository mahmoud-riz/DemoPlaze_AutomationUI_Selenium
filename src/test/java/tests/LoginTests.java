package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.qameta.allure.*;


@Epic("User Management")
@Feature("Login and Registration")
public class LoginTests extends BaseTest {

    @Test(priority = 1)
    @Story("User Registration")
    @Description("Test Case 1: Verify that a new user can successfully register with valid details")
    @Severity(SeverityLevel.CRITICAL)
    public void testUserRegistration() {
        logger.info("Starting user registration test");

        
        String[] userCredentials = generateUniqueUser();
        String username = userCredentials[0];
        String password = userCredentials[0];

      
        loginActions.registerUser(username, password);

      
        waitForPageLoad();
        loginActions.loginUser(username, password);

        
        waitForPageLoad();
        Assert.assertTrue(loginActions.isUserLoggedIn(),
                "User should be logged in after successful registration and login");

        String loggedInUser = loginActions.getLoggedInUsername();
        Assert.assertEquals(loggedInUser, username,
                "Logged in username should match registered username");

        logger.info("✅ User registration test completed successfully");
    }

    @Test(priority = 2)
    @Story("User Login")
    @Description("Test Case 2: Verify that the user can log in with correct credentials and is redirected to the home page")
    @Severity(SeverityLevel.CRITICAL)
    public void testValidUserLogin() {
        logger.info("Starting valid user login test");

        
        String[] userCredentials = generateUniqueUser();
        String username = userCredentials[0];
        String password = userCredentials[1];

     
        loginActions.registerUser(username, password);
        waitForPageLoad();

     
        if (loginActions.isUserLoggedIn()) {
            loginActions.logoutUser();
            waitForPageLoad();
        }

       
        loginActions.loginUser(username, password);

       
        waitForPageLoad();
        Assert.assertTrue(loginActions.isUserLoggedIn(),
                "User should be logged in with valid credentials");

        
        String loggedInUser = loginActions.getLoggedInUsername();
        Assert.assertEquals(loggedInUser, username,
                "Displayed username should match login username");

        
        Assert.assertTrue(verifyPageTitle("STORE"),
                "Should be redirected to home page after login");

        logger.info("✅ Valid user login test completed successfully");
    }


    @Test(priority = 3)
    @Story("User Management")
    @Description("Verify user logout functionality")
    @Severity(SeverityLevel.NORMAL)
    public void testUserLogout() {
        logger.info("Starting user logout test");

       
        String[] userCredentials = generateUniqueUser();
        String username = userCredentials[0];
        String password = userCredentials[1];

 
        loginActions.registerUser(username, password);
        waitForPageLoad();
        loginActions.loginUser(username, password);

        
        waitForPageLoad();
        Assert.assertTrue(loginActions.isUserLoggedIn(),
                "User should be logged in before logout test");

       
        loginActions.logoutUser();
        waitForPageLoad();

   
        Assert.assertFalse(loginActions.isUserLoggedIn(),
                "User should be logged out after clicking logout");

        logger.info("✅ User logout test completed successfully");
    }


}