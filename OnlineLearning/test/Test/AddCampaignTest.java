package Test;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class AddCampaignTest {
    private WebDriver driver;
    private static final String BASE_URL = "http://localhost:8080/OnlineLearning";
    private static final String USERNAME = "tuan"; // Update with a valid username
    private static final String PASSWORD = "123123"; // Update with the actual password
    private static final String MEDIA_FILE_PATH = "D:\\HOCTAP\\FA23\\SSG104\\Student-Stress.jpg";
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "D:/HOCTAP/FA24/SWT301/edgedriver_win64/msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(BASE_URL + "/login.jsp");
            driver.manage().window().maximize();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Login
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys(USERNAME);
            driver.findElement(By.name("password")).sendKeys(PASSWORD);
            driver.findElement(By.xpath("//button[@type='submit']")).click();
            driver.get(BASE_URL + "/AddCampaign.jsp");
    }

    @Test
    public void testAddCampaign() {
        try {
            

            

            // Wait for the Add Campaign page to load
            
            wait.until(ExpectedConditions.urlContains("AddCampaign.jsp"));
            

            // Fill in campaign details
            driver.findElement(By.name("campaignName")).sendKeys("Test Campaign");
            driver.findElement(By.name("description")).sendKeys("This is a test campaign description.");
            driver.findElement(By.name("startDate")).sendKeys("10-01-2024");
            driver.findElement(By.name("endDate")).sendKeys("10-01-2024");

            // Upload a media file
            WebElement mediaFileInput = driver.findElement(By.name("mediaFiles"));
            mediaFileInput.sendKeys(MEDIA_FILE_PATH);

//             Enter media description
            driver.findElement(By.name("mediaDescriptions")).sendKeys("Test media description.");

            // Submit the form
            driver.findElement(By.xpath("//button[text()='Add Campaign']")).click();
            

            // Validate success message
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert-success")));
            Assert.assertTrue(successMessage.isDisplayed(), "Success message should be displayed.");
            Assert.assertEquals(successMessage.getText(), "Campaign added successfully.");

        } catch (WebDriverException e) {
            Assert.fail("WebDriver Exception occurred: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
