package Test;

import java.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Register {

    private WebDriver driver;

    @Before
    public void setUp() {
        // Set the path to the EdgeDriver executable
        System.setProperty("webdriver.edge.driver", "D:/HOCTAP/FA24/SWT301/edgedriver_win64/msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
    
    
        driver.get("http://localhost:8080/OnlineLearning/register.jsp"); // Update with your actual registration page URL
    }

    @Test
    public void testValidRegistration() {
        
        driver.findElement(By.name("name")).sendKeys("Tuan Test");
        driver.findElement(By.name("username")).sendKeys("naisd999");
        driver.findElement(By.name("email")).sendKeys("ng99d9999@example.com");
        driver.findElement(By.name("password")).sendKeys("123123");
        driver.findElement(By.name("repassword")).sendKeys("123123");
        driver.findElement(By.name("phone")).sendKeys("0988982379");
        driver.findElement(By.name("address")).sendKeys("99 Bac Ninh chao ae nha");
        driver.findElement(By.id("male")).click();

        WebElement signUpButton = driver.findElement(By.cssSelector(".ud-btn"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(signUpButton));
        signUpButton.click();

        WebElement verifyPage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("code")));
        assertTrue(verifyPage.isDisplayed());
    }

     @Test
     public void testInvalidRegistration() {
        driver.findElement(By.name("name")).sendKeys("Tuan Test");
        driver.findElement(By.name("username")).sendKeys("tuan");
        driver.findElement(By.name("email")).sendKeys("nghai99@example.com");
        driver.findElement(By.name("password")).sendKeys("123123");
        driver.findElement(By.name("repassword")).sendKeys("123123");
        driver.findElement(By.name("phone")).sendKeys("0988928878");
        driver.findElement(By.name("address")).sendKeys("99 Bac Ninh chao ae nha");
        driver.findElement(By.id("male")).click();

        WebElement signUpButton = driver.findElement(By.cssSelector(".ud-btn"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(signUpButton));
        signUpButton.click();
        
        WebElement errorAlert = driver.findElement(By.className("alert-danger"));
        assertTrue( errorAlert.isDisplayed());
     }
     
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
