/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Test;

import java.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.WebElement;
/**
 *
 * @author 84336
 */
public class AddQuizJTest {
    
    public AddQuizJTest() {
    }
    private WebDriver driver;
    private WebDriverWait wait;
    @Before
    public void setUpClass() {
        System.setProperty("webdriver.edge.driver", "D:/HOCTAP/FA24/SWT301/edgedriver_win64/msedgedriver.exe"); // Update this path
        driver = new EdgeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("http://localhost:8080/OnlineLearning/login.jsp");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("tuan"); // Update with a valid username
        driver.findElement(By.name("password")).sendKeys("123123"); // Update with the actual password
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.get("http://localhost:8080/OnlineLearning/AddNewTest.jsp");
    }
    @Test
    public void testAddQuiz() throws InterruptedException {
        wait.until(ExpectedConditions.urlContains("AddNewTest.jsp"));
        testField("title", "");
        testField("description", "");
        testField("type", "Select Type");
        testField("duration", "");
        testField("passCondition", "");
        testField("level", "Select Level");
        testField("quantity", "0");
        testField("subjectId", "Select Subject");
        
    }
    @Test
     public void testAddQuizSpace() throws InterruptedException {
         wait.until(ExpectedConditions.urlContains("AddNewTest.jsp"));
        testSpaceDesc(driver, wait);
        testSpaceTitle(driver, wait);
                 
     }
      @Test
     public void testAddQuizTrue() throws InterruptedException {
         wait.until(ExpectedConditions.urlContains("AddNewTest.jsp"));
        // Fill in the form
        driver.findElement(By.name("title")).sendKeys("Sample Quiz");
        driver.findElement(By.name("description")).sendKeys("Description of the sample quiz.");
        addImageMedia();

        // Fill additional fields
        driver.findElement(By.name("type")).sendKeys("Quiz");
        driver.findElement(By.name("duration")).sendKeys("30");
        driver.findElement(By.name("passCondition")).sendKeys("70");
        driver.findElement(By.name("level")).sendKeys("Intermediate");
        driver.findElement(By.name("quantity")).sendKeys("10");
        driver.findElement(By.name("subjectId")).sendKeys("Introduction to Programming");

        // Submit the form
        driver.findElement(By.xpath("//button[text()='Add Quiz']")).click();

        // Validate success
        wait.until(ExpectedConditions.urlContains("QuizDetail"));
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Test completed. Current URL: " + currentUrl);
        assertTrue("Expected URL to contain QuizDetail", currentUrl.contains("QuizDetail"));
        
                 
     }
    private void addImageMedia() throws InterruptedException {
        WebElement targetElement = driver.findElement(By.xpath("//button[text()='Add Image']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", targetElement);
        Thread.sleep(500); // Adjust this delay as needed
        targetElement.click();
        WebElement imageInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@accept='image/*']")));
        imageInput.sendKeys("D:\\HOCTAP\\FA23\\SSG104\\Student-Stress.jpg"); // Update with a valid image path
        driver.findElement(By.xpath("//input[@placeholder='Media Description']")).sendKeys("Sample Image Description");
    }
     private static void testSpaceDesc(WebDriver driver, WebDriverWait wait) throws InterruptedException
    {
        driver.findElement(By.name("title")).sendKeys("Sample Quiz");
            driver.findElement(By.name("description")).sendKeys("   ");

            // Add image media
            WebElement targetElement = driver.findElement(By.xpath("//button[text()='Add Image']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", targetElement);
            Thread.sleep(500); // Adjust this delay as needed

            // Now click the button
            targetElement.click();
            WebElement imageInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@accept='image/*']")));
            imageInput.sendKeys("E:/Anime/1306217.jpg"); // Update with a valid image path
            driver.findElement(By.xpath("//input[@placeholder='Media Description']")).sendKeys("Sample Image Description");
            driver.findElement(By.name("type")).sendKeys("Quiz");
            driver.findElement(By.name("duration")).sendKeys("30");
            driver.findElement(By.name("passCondition")).sendKeys("70");
            driver.findElement(By.name("level")).sendKeys("Intermediate");
            driver.findElement(By.name("quantity")).sendKeys("10");
            driver.findElement(By.name("subjectId")).sendKeys("Introduction to Programming"); // Assuming 1 is a valid subject ID

            // Submit the form
            driver.findElement(By.xpath("//button[text()='Add Quiz']")).click();
            String currentUrl = driver.getCurrentUrl();
        assertTrue("Expected URL to contain QuizDetail", currentUrl.contains("AddNewTest.jsp"));
        driver.get("http://localhost:8080/OnlineLearning/AddNewTest.jsp");
    }
     private static void testSpaceTitle(WebDriver driver, WebDriverWait wait) throws InterruptedException
    {
        driver.findElement(By.name("title")).sendKeys("   ");
            driver.findElement(By.name("description")).sendKeys("Description of the sample quiz.");

            // Add image media
            WebElement targetElement = driver.findElement(By.xpath("//button[text()='Add Image']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", targetElement);
            Thread.sleep(500); // Adjust this delay as needed

            // Now click the button
            targetElement.click();
            WebElement imageInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@accept='image/*']")));
            imageInput.sendKeys("E:/Anime/1306217.jpg"); // Update with a valid image path
            driver.findElement(By.xpath("//input[@placeholder='Media Description']")).sendKeys("Sample Image Description");

//            // Add video media
//            driver.findElement(By.xpath("//button[text()='Add Video']")).click();
//            WebElement videoInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@accept='video/*']")));
//            videoInput.sendKeys("E:/Anime/0831.mp4"); // Update with a valid video path
//            driver.findElement(By.xpath("//input[@placeholder='Media Description']")).sendKeys("Sample Video Description");
            // Select type, duration, pass condition, level, number of questions, and subject
            driver.findElement(By.name("type")).sendKeys("Quiz");
            driver.findElement(By.name("duration")).sendKeys("30");
            driver.findElement(By.name("passCondition")).sendKeys("70");
            driver.findElement(By.name("level")).sendKeys("Intermediate");
            driver.findElement(By.name("quantity")).sendKeys("10");
            driver.findElement(By.name("subjectId")).sendKeys("Introduction to Programming"); // Assuming 1 is a valid subject ID

            // Submit the form
            driver.findElement(By.xpath("//button[text()='Add Quiz']")).click();
            String currentUrl = driver.getCurrentUrl();
        assertTrue("Expected URL to contain QuizDetail", currentUrl.contains("AddNewTest.jsp"));
        driver.get("http://localhost:8080/OnlineLearning/AddNewTest.jsp");
    }
    private void testField(String fieldName, String value) {
        driver.findElement(By.name(fieldName)).sendKeys(value);

        // Attempt to submit the form
        WebElement addButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Add Quiz']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addButton);

        wait.until(ExpectedConditions.urlContains("AddNewTest.jsp")); // Wait until the form is loaded
        String currentUrl = driver.getCurrentUrl();
        assertTrue(fieldName + " test passed: Form submission prevented as expected.", currentUrl.contains("AddNewTest.jsp"));
    }
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
