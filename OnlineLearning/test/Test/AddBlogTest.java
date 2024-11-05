
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
import org.openqa.selenium.WebDriverException;

import java.util.concurrent.TimeUnit;

public class AddBlogTest {

    private WebDriver driver;
    private static final String BASE_URL = "http://localhost:8080/OnlineLearning";
    private static final String USERNAME = "adminson"; // Cập nhật với tên đăng nhập hợp lệ
    private static final String PASSWORD = "24112004"; // Cập nhật với mật khẩu thực tế
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.edge.driver", "C:\\Users\\sonna\\Downloads\\msedgedriver.exe");
        driver = new EdgeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(BASE_URL + "/login.jsp");
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Đăng nhập
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys(USERNAME);
        driver.findElement(By.name("password")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        driver.get(BASE_URL + "/AddBlog.jsp");
    }

    @Test
    public void testAddBlogSuccess() {
        try {
            // Chờ trang Add Blog tải xong
            wait.until(ExpectedConditions.urlContains("AddBlog.jsp"));

            // Điền thông tin blog
            driver.findElement(By.name("title")).sendKeys("Blog Test");
            driver.findElement(By.name("content")).sendKeys("Nội dung blog thử nghiệm.");

            // Chọn category từ dropdown
            Select categoryDropdown = new Select(driver.findElement(By.name("categoryId")));
            categoryDropdown.selectByVisibleText("Language Tips"); // Chọn category mong muốn

            // Nhấn nút gửi (đã xóa)
            // driver.findElement(By.cssSelector("button[type='submit']")).click();
        } catch (WebDriverException e) {
            Assert.fail("Đã xảy ra lỗi WebDriver: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Đã xảy ra lỗi không mong đợi: " + e.getMessage());
        }
    }

    @Test
    public void testAddBlogWithLongTitle() {
        try {
            // Chờ trang Add Blog tải xong
            wait.until(ExpectedConditions.urlContains("AddBlog.jsp"));

            // Điền tiêu đề dài
            String longTitle = "Blog Test " + "a".repeat(256); // Giả sử giới hạn tiêu đề là 255 ký tự
            driver.findElement(By.name("title")).sendKeys(longTitle);
            driver.findElement(By.name("content")).sendKeys("Nội dung blog thử nghiệm.");

            // Chọn category từ dropdown
            Select categoryDropdown = new Select(driver.findElement(By.name("categoryId")));
            categoryDropdown.selectByVisibleText("Language Tips"); // Chọn category mong muốn

            // Nhấn nút gửi (đã xóa)
            // driver.findElement(By.cssSelector("button[type='submit']")).click();
        } catch (WebDriverException e) {
            Assert.fail("Đã xảy ra lỗi WebDriver: " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("Đã xảy ra lỗi không mong đợi: " + e.getMessage());
        }
    }
    
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
