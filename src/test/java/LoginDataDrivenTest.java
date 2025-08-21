import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LoginDataDrivenTest {

    // Зчитуємо CSV з resources через classpath
    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() throws Exception {
        List<Object[]> rows = new ArrayList<>();

        InputStream is = getClass().getClassLoader().getResourceAsStream("logins.csv");
        if (is == null) throw new IllegalStateException("Не знайдено logins.csv у resources");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;      // пропускаємо порожні/коментарі
                if (!headerSkipped) { headerSkipped = true; continue; }     // пропускаємо заголовок

                String[] parts = line.split(",", -1);
                if (parts.length < 3) continue; // захист від кривих рядків
                rows.add(new Object[]{ parts[0].trim(), parts[1].trim(), parts[2].trim() });
            }
        }
        return rows.toArray(new Object[0][]);
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, String expected) {
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // 1) Відкриваємо сторінку логіна (демо-сайт з ЛР6)
            driver.get("https://practicetestautomation.com/practice-test-login/");

            // 2) Заповнюємо форму
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username"))).sendKeys(username);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("submit")).click();

            // 3) Перевіряємо очікування
            if ("success".equalsIgnoreCase(expected)) {
                String h1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1"))).getText();
                Assert.assertTrue(h1.contains("Logged In Successfully"),
                        "Очікували успішний вхід, але заголовок інший: " + h1);
            } else {
                WebElement error = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("error")));
                Assert.assertTrue(error.isDisplayed(), "Очікували повідомлення про помилку, але його нема");
                // (опційно) можна також перевірити текст:
                // Assert.assertTrue(error.getText().contains("invalid"), "Текст помилки не співпав");
            }

        } finally {
            driver.quit();
        }
    }
}
