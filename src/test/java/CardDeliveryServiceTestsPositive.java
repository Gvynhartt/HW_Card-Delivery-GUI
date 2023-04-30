import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryServiceTestsPositive {

    @Test
    public void shdTestPositiveShortPath() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        String date4input = TestDataGenerator.generateDateForInput(3, "dd.MM.yyyy");

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Чебоксары");
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).sendKeys(Keys.chord(Keys.CONTROL, Keys.BACK_SPACE));
        // предварительно удаляем введённое по умолчанию значение сочетанием клавиш "Ctrl + Backspace"
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Вислый-Анков");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).should(Condition.appear, Duration.ofMillis(15000));
        // проверяем наличие уведомления с нужным текстом
        $x("//div[@data-test-id='notification']/descendant::div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно забронирована на " + date4input));
    }

}
