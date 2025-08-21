import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-search-engine-choice-screen");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void testLogin() {
        // 1. Відкрити сторінку логіна
        driver.get("https://practicetestautomation.com/practice-test-login/");

        // 2. Ввести логін і пароль
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");

        // 3. Натиснути кнопку входу
        driver.findElement(By.id("submit")).click();

        // 4. Перевірити повідомлення про успіх
        WebElement h1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
        String successMessage = h1.getText().trim();

        if ("Logged In Successfully".equals(successMessage)) {
            System.out.println("Успішний вхід: " + successMessage);
        } else {
            System.out.println("Вхід не виконано. Отримане повідомлення: " + successMessage);
        }

        Assert.assertEquals(successMessage, "Logged In Successfully");

        // 5. Перевірити, що кнопка "Log out" є
        WebElement logout = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Log out")));
        Assert.assertTrue(logout.isDisplayed(), "Кнопка Log out має бути видимою");
    }
}
