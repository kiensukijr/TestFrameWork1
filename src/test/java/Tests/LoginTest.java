package Tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.*;
import Pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

import static java.lang.Thread.sleep;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        // WebDriverManager tự động tải ChromeDriver version mới nhất
        WebDriverManager.chromedriver().setup();

        // ChromeOptions (bỏ headless để thấy trình duyệt thao tác)
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // Explicit Wait 10 giây
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Mở trang login
        driver.get("https://www.saucedemo.com/");

        // Đợi input username xuất hiện trước khi khởi tạo Page Object
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));

        // Khởi tạo Page Object
        loginPage = new LoginPage(driver);
    }

    @Test
    public void testInvalidLogin() throws InterruptedException {
        loginPage.login("invalid_user", "wrong_password");
        Thread.sleep(5000);

        // Đợi thông báo lỗi hiển thị
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));

        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"));
    }

    @Test
    public void testValidLogin() throws InterruptedException {
        loginPage.login("standard_user", "secret_sauce");
        Thread.sleep(5000);

        // Đợi URL chuyển sang inventory
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/inventory.html"));

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html");
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) driver.quit();
    }
}