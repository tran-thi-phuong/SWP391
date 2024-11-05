import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class NewSubjectTest {
    private WebDriver driver;
    private static final String BASE_URL = "http://localhost:8080/OnlineLearning";
    private static final String USERNAME = "adminson"; // Update with valid username
    private static final String PASSWORD = "24112004"; // Update with actual password
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\Users\\sonna\\Downloads\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Login
        driver.get(BASE_URL + "/login.jsp");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys(USERNAME);
        driver.findElement(By.name("password")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    @Test
    public void testAddSubjectWithValidInput() {
        driver.get(BASE_URL + "/newSubject.jsp");
        wait.until(ExpectedConditions.urlContains("newSubject.jsp"));

        // Fill in Course Name
        String courseName = "New Language Course";
        driver.findElement(By.id("courseName")).sendKeys(courseName);

        // Upload thumbnail image (dummy image link, should be a valid image path on your system)
        String thumbnailImagePath = "C:\\Users\\sonna\\Downloads\\thumbnail.jpg"; // Full path to a specific image file
        driver.findElement(By.id("thumbnail")).sendKeys(thumbnailImagePath);

        // Select a category from dropdown
        
        // Fill in Description
        String description = "This is a description for the new language course.";
        driver.findElement(By.id("description")).sendKeys(description);

        // Check the values are set correctly
        Assert.assertEquals(driver.findElement(By.id("courseName")).getAttribute("value"), courseName, "Course Name did not match");
        Assert.assertEquals(driver.findElement(By.id("description")).getAttribute("value"), description, "Description did not match");
        
        // Verify that the thumbnail field has the correct file path
        Assert.assertEquals(driver.findElement(By.id("thumbnail")).getAttribute("value"), thumbnailImagePath, "Thumbnail image path did not match");
    }

    @Test
    public void testAddSubjectWithEmptyFields() {
        driver.get(BASE_URL + "/newSubject.jsp");
        wait.until(ExpectedConditions.urlContains("newSubject.jsp"));

        // Click submit without filling the form
        driver.findElement(By.xpath("//button[text()='Add Course']")).click();

        // Validate that the appropriate error messages are displayed for empty fields
        Assert.assertTrue(driver.findElement(By.id("courseName")).getAttribute("required").equals("true"), "Course Name should be required");
        Assert.assertTrue(driver.findElement(By.id("category")).getAttribute("required").equals("true"), "Category should be required");
        Assert.assertTrue(driver.findElement(By.id("description")).getAttribute("required").equals("true"), "Description should be required");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
