package ru.netology.ordercard;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class OrderCardTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        // options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldSendCorrectData() {
//        List<WebElement> inputElements = driver.findElements(By.cssSelector("input")); вариант кода для открытия страницы и заполнения полей Имя и Номер
//        inputElements.get(0).sendKeys("Василий");
//        inputElements.get(1).sendKeys("+79001234567");

        WebElement formElement = driver.findElement(By.cssSelector("form"));
        formElement.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Васильев Василий");
        formElement.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79001234567");

        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        WebElement actual = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        assertTrue(actual.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actual.getText().trim());
    }

    @Test
    public void shouldSendFormWithEmptyName() {

        WebElement formElement = driver.findElement(By.cssSelector("form"));
        formElement.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("");
        formElement.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");

        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        WebElement nameError = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(nameError.isDisplayed());
        assertEquals("Поле обязательно для заполнения", nameError.getText().trim());
    }

    @Test
    public void shouldSendFormWithTranslitName() {

        WebElement formElement = driver.findElement(By.cssSelector("form"));
        formElement.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("V Vasiliy");
        formElement.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");

        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        WebElement nameError = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(nameError.isDisplayed());
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", nameError.getText().trim());
    }

    @Test
    public void shouldSendWithEmptyNumber() {

        WebElement formElement = driver.findElement(By.cssSelector("form"));
        formElement.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Васильев Василий");
        formElement.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("");

        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        WebElement phoneError = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(phoneError.isDisplayed());
        assertEquals("Поле обязательно для заполнения", phoneError.getText().trim());
    }

    @Test
    public void shouldSendWithInvalidNumber() {

        WebElement formElement = driver.findElement(By.cssSelector("form"));
        formElement.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Васильев Василий");
        formElement.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+7555");

        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        WebElement phoneError = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(phoneError.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", phoneError.getText().trim());
    }

    @Test
    public void shouldSendWithEmptyCheckbox() {

        WebElement formElement = driver.findElement(By.cssSelector("form"));
        formElement.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Васильев Василий");
        formElement.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");

        driver.findElement(By.cssSelector("button")).click();
        WebElement checkboxError = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text"));
        assertTrue(checkboxError.isDisplayed());
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", checkboxError.getText().trim());
    }
}