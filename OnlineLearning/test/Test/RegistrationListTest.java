import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegistrationListTest {

    private WebDriver driver;
    private static final String BASE_URL = "http://localhost:8080/OnlineLearning";
    private static final String USERNAME = "tuan"; // Update with a valid username
    private static final String PASSWORD = "123123"; // Update with the actual password
     WebDriverWait wait;


    @Before
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
            driver.get(BASE_URL + "/listRegistration");
    }

    @Test
    public void testRegistrationListPage() {
        // Step 1: Load the Registration List Page
        driver.get("http://localhost:8080/OnlineLearning/listRegistration"); // Replace with your app URL

        // Step 2: Verify that the page loads correctly
        assertEquals("Registration List", driver.getTitle());

        // Step 3: Check if the search input fields are present
        assertTrue(driver.findElement(By.id("emailSearch")).isDisplayed());
        assertTrue(driver.findElement(By.id("campaignSearch")).isDisplayed());
        assertTrue(driver.findElement(By.id("subjectSearch")).isDisplayed());
        assertTrue(driver.findElement(By.id("registrationFrom")).isDisplayed());
        assertTrue(driver.findElement(By.id("registrationTo")).isDisplayed());
        assertTrue(driver.findElement(By.id("statusSearch")).isDisplayed());

        // Step 4: Search for registrations (you may adjust the values based on your test cases)
        WebElement emailInput = driver.findElement(By.id("emailSearch"));
        emailInput.sendKeys("test@example.com");
        
        WebElement targetElement = driver.findElement(By.xpath("//button[text()='Save']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", targetElement);


        // Step 5: Check the displayed results
        List<WebElement> rows = driver.findElements(By.cssSelector("#registrationTable tbody tr"));
        assertFalse("No registrations found.", rows.isEmpty());

        // Validate that the search returned expected results
        boolean foundEmail = false;
        for (WebElement row : rows) {
            String email = row.findElement(By.cssSelector("td:nth-child(2)")).getText(); // Adjust index as needed
            if (email.contains("test@example.com")) {
                foundEmail = true;
                break;
            }
        }
        assertTrue("Expected email not found in results.", foundEmail);

        // Step 6: Interact with the pagination if available
        WebElement nextButton = driver.findElement(By.linkText("Next"));
        if (nextButton.isDisplayed()) {
            nextButton.click();
            // Add a small wait to allow results to load
            try {
                Thread.sleep(2000); // Replace with WebDriverWait in real test
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Check for results again after pagination
            List<WebElement> newRows = driver.findElements(By.cssSelector("#registrationTable tbody tr"));
            assertFalse("No registrations found on next page.", newRows.isEmpty());
        }

        // Step 7: Check "New Registration" button if user is Marketing
        // Assuming you have a method to log in as Marketing user
        // loginAsMarketingUser();

        // Verify "New Registration" button is displayed
        if (!driver.findElements(By.cssSelector("button#add")).isEmpty()) {
            WebElement newRegistrationButton = driver.findElement(By.cssSelector("button#add"));
            assertTrue(newRegistrationButton.isDisplayed());
            newRegistrationButton.click(); // Optional: to test button click action
        }
    }

    @After
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}
