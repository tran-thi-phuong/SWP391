package Test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.edge.EdgeDriver;

public class LoginPage {

    private WebDriver driver;

    @Before
    public void setUp() {
        // Set the path to your ChromeDriver
        System.setProperty("webdriver.edge.driver", "D:/HOCTAP/FA24/SWT301/edgedriver_win64/msedgedriver.exe");
        driver = new EdgeDriver();
        driver.get("http://localhost:8080/OnlineLearning/login.jsp"); // Update with your actual login page URL
    }

    @Test
    public void testValidLogin() {
        // Locate username and password fields
        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector(".ud-btn"));

        // Input valid credentials
        usernameField.sendKeys("tuan");
        passwordField.sendKeys("123123");

        // Submit the form
        loginButton.click();

        // Verify successful login by checking if redirected or a certain element is present
        assertTrue(driver.getCurrentUrl().contains("courseStat"));
    }

    @Test
    public void testInvalidLogin() {

        WebElement usernameField = driver.findElement(By.name("username"));
        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector(".ud-btn"));

        usernameField.sendKeys("invalidUsername");
        passwordField.sendKeys("invalidPassword");

        loginButton.click();

        WebElement errorAlert = driver.findElement(By.className("alert-danger"));
        assertTrue(errorAlert.isDisplayed());
    }
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
