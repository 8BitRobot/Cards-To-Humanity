package seleniumtests.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import seleniumtests.environment.EnvironmentManager;
import seleniumtests.environment.RunEnvironment;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class CreateAccountAndSignInTest {
    final String baseURL = "http://localhost:4567";

    @Before
    public void startBrowser() {
        EnvironmentManager.initialize();
    }

    @Test
    public void createAccountAndSignIn() {
        // Load WebDriver.
        WebDriver driver = RunEnvironment.getWebDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Open login page.
        driver.get(baseURL + "/login.html");
        // Click on the "Sign Up" link.
        driver.findElement(By.id("sign_up_link")).click();
        // Create a user on the "Sign Up" page.
        int randomNumber = (int) (Math.random() * 100000);
        String username = "testuser" + randomNumber;
        driver.findElement(By.name("username")).sendKeys(username);
        String displayName = "Test User " + randomNumber;
        driver.findElement(By.name("display_name")).sendKeys(displayName);
        String password = "bingobongo123;()&...";
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("password_retyped")).sendKeys(password);
        String email = "example" + randomNumber + "@example.com";
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.id("signup_button")).click();
        // After being redirected back to the login page, log in.
        driver.findElement(By.name("username_or_email")).sendKeys(username);
        assertEquals(baseURL + "/login.html", driver.getCurrentUrl());
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.id("login_button")).click();
        // Check that the login succeeded.
        driver.findElement(By.id("cards"));
        assertEquals(baseURL + "/home.html", driver.getCurrentUrl());
    }

    @Test
    public void createAccountWithMissingUsername() {
        // Load WebDriver.
        WebDriver driver = RunEnvironment.getWebDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Open login page.
        driver.get(baseURL + "/login.html");
        // Click on the "Sign Up" link.
        driver.findElement(By.id("sign_up_link")).click();
        // Create a user on the "Sign Up" page, but leave the username field empty.
        int randomNumber = (int) (Math.random() * 100000);
        String displayName = "Test User " + randomNumber;
        driver.findElement(By.name("display_name")).sendKeys(displayName);
        String password = "bingobongo123;()&...";
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("password_retyped")).sendKeys(password);
        String email = "example" + randomNumber + "@example.com";
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.id("signup_button")).click();
        // Wait for the API request to complete and show an error.
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error_message_p")));
        // Check that the error is correct.
        assertEquals("The username, password, and email fields are mandatory.", driver.findElement(By.id("error_message_p")).getText());
    }

    @Test
    public void createAccountWithMissingPassword() {
        // Load WebDriver.
        WebDriver driver = RunEnvironment.getWebDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Open login page.
        driver.get(baseURL + "/login.html");
        // Click on the "Sign Up" link.
        driver.findElement(By.id("sign_up_link")).click();
        // Create a user on the "Sign Up" page, but leave the username field empty.
        int randomNumber = (int) (Math.random() * 100000);
        String username = "testuser" + randomNumber;
        driver.findElement(By.name("username")).sendKeys(username);
        String displayName = "Test User " + randomNumber;
        driver.findElement(By.name("display_name")).sendKeys(displayName);
        String email = "example" + randomNumber + "@example.com";
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.id("signup_button")).click();
        // Wait for the API request to complete and show an error.
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error_message_p")));
        // Check that the error is correct.
        assertEquals("Your password is too short. Passwords should be at least 8 characters long.", driver.findElement(By.id("error_message_p")).getText());
    }

    @Test
    public void createAccountWithMissingEmail() {
        // Load WebDriver.
        WebDriver driver = RunEnvironment.getWebDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Open login page.
        driver.get(baseURL + "/login.html");
        // Click on the "Sign Up" link.
        driver.findElement(By.id("sign_up_link")).click();
        // Create a user on the "Sign Up" page, but leave the username field empty.
        int randomNumber = (int) (Math.random() * 100000);
        String username = "testuser" + randomNumber;
        driver.findElement(By.name("username")).sendKeys(username);
        String password = "bingobongo123;()&...";
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("password_retyped")).sendKeys(password);
        String displayName = "Test User " + randomNumber;
        driver.findElement(By.name("display_name")).sendKeys(displayName);
        driver.findElement(By.id("signup_button")).click();
        // Wait for the API request to complete and show an error.
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error_message_p")));
        // Check that the error is correct.
        assertEquals("The username, password, and email fields are mandatory.", driver.findElement(By.id("error_message_p")).getText());
    }

    @Test
    public void createAccountWithMismatchedPasswords() {
        // Load WebDriver.
        WebDriver driver = RunEnvironment.getWebDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Open login page.
        driver.get(baseURL + "/login.html");
        // Click on the "Sign Up" link.
        driver.findElement(By.id("sign_up_link")).click();
        // Create a user on the "Sign Up" page.
        int randomNumber = (int) (Math.random() * 100000);
        String username = "testuser" + randomNumber;
        driver.findElement(By.name("username")).sendKeys(username);
        String displayName = "Test User " + randomNumber;
        driver.findElement(By.name("display_name")).sendKeys(displayName);
        String password = "bingobongo123;()&...";
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("password_retyped")).sendKeys("thisdoesnotmatch");
        String email = "example" + randomNumber + "@example.com";
        driver.findElement(By.name("email")).sendKeys(email);
        // Wait for the error to appear.
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error_message_p")));
        // Check that the error is correct.
        assertEquals("Passwords do not match.", driver.findElement(By.id("error_message_p")).getText());
    }

    @Test
    public void createAccountWithExistingUsername() {
        // Load WebDriver.
        WebDriver driver = RunEnvironment.getWebDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Open login page.
        driver.get(baseURL + "/login.html");
        // Click on the "Sign Up" link.
        driver.findElement(By.id("sign_up_link")).click();
        // Create a user on the "Sign Up" page.
        int randomNumber = (int) (Math.random() * 100000);
        String username = "anonymous"; // This user is guaranteed to always exist because createschema.sql creates it.
        driver.findElement(By.name("username")).sendKeys(username);
        String displayName = "Test User " + randomNumber;
        driver.findElement(By.name("display_name")).sendKeys(displayName);
        String password = "bingobongo123;()&...";
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.name("password_retyped")).sendKeys(password);
        String email = "example" + randomNumber + "@example.com";
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.id("signup_button")).click();
        // Wait for the API request to complete and show an error.
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error_message_p")));
        // Check that the error is correct.
        assertEquals("A user with this username already exists. Please choose a different username.", driver.findElement(By.id("error_message_p")).getText());
    }

    @After
    public void tearDown() {
        EnvironmentManager.stop();
    }
}
