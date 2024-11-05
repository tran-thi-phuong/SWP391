import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import java.util.concurrent.TimeUnit;

public class SubjectDetailOverviewTest {
    private WebDriver driver;
    private static final String BASE_URL = "http://localhost:8080/OnlineLearning";
    private static final String USERNAME = "adminson";
    private static final String PASSWORD = "24112004";
    private JavascriptExecutor js;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\Users\\sonna\\Downloads\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(BASE_URL + "/login.jsp");
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        
        // Login
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys(USERNAME);
        driver.findElement(By.name("password")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    @Test
    public void testValidSubjectDetails() {
        try {
            // Navigate to Subject Details page
            driver.get(BASE_URL + "/SubjectDetailOverview?id=1");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subjectName")));
            
            // Test valid subject name
            WebElement subjectNameInput = driver.findElement(By.id("subjectName"));
            subjectNameInput.clear();
            subjectNameInput.sendKeys("Java Programming Course");
            
            // Test valid category selection
            Select categoryDropdown = new Select(driver.findElement(By.id("category")));
            categoryDropdown.selectByIndex(1);
            
            // Test valid status selection
            Select statusDropdown = new Select(driver.findElement(By.id("status")));
            statusDropdown.selectByValue("Active");
            
            // Test valid description
            WebElement descriptionInput = driver.findElement(By.id("description"));
            descriptionInput.clear();
            descriptionInput.sendKeys("Complete Java programming course from basics to advanced topics.");
            
            // Test file upload with valid file
            java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("valid-thumbnail", ".jpg");
            java.nio.file.Files.write(tempFile, new byte[]{1, 2, 3, 4, 5});
            WebElement fileInput = driver.findElement(By.name("thumbnail"));
            fileInput.sendKeys(tempFile.toAbsolutePath().toString());
            
            // Submit form
            WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
            js.executeScript("arguments[0].click();", submitButton);
            
            // Test navigation between tabs
          
            // Clean up
            java.nio.file.Files.deleteIfExists(tempFile);
            
        } catch (Exception e) {
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidSubjectDetails() {
        try {
            // Navigate to Subject Details page
            driver.get(BASE_URL + "/SubjectDetailOverview?id=1");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("subjectName")));
            
            // Test empty subject name
            WebElement subjectNameInput = driver.findElement(By.id("subjectName"));
            js.executeScript("arguments[0].value = '';", subjectNameInput);
            
            // Test empty description
            WebElement descriptionInput = driver.findElement(By.id("description"));
            js.executeScript("arguments[0].value = '';", descriptionInput);
            
            // Test invalid file upload (wrong format)
            java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("invalid-file", ".txt");
            java.nio.file.Files.write(tempFile, "invalid content".getBytes());
            WebElement fileInput = driver.findElement(By.name("thumbnail"));
            fileInput.sendKeys(tempFile.toAbsolutePath().toString());
            
            // Submit form
            WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
            js.executeScript("arguments[0].click();", submitButton);
            
            // Verify form validation prevents submission
            boolean isNameInvalid = (boolean) js.executeScript(
                "return arguments[0].validity.valid === false;", subjectNameInput);
            Assert.assertTrue(isNameInvalid, "Form should prevent submission with empty subject name");
            
            // Clean up
            java.nio.file.Files.deleteIfExists(tempFile);
            
        } catch (Exception e) {
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}