import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryServiceTestsNegative {

    @Test
    public void shdTestNegativeInvalidCity() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        String date4input = TestDataGenerator.generateDateForInput(3, "dd.MM.yyyy");

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Мусохранск");
        // вводим некорректный город
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).sendKeys(Keys.chord(Keys.CONTROL, Keys.BACK_SPACE));
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Вислый-Анков");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).shouldNot(Condition.appear, Duration.ofMillis(15000));

        $x("//span[@data-test-id='city']/descendant::span[text()='Доставка в выбранный город недоступна']").shouldHave(Condition.text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shdTestNegativeInvalidDateBelowLimit() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        String date4input = TestDataGenerator.generateDateForInput(2, "dd.MM.yyyy");
        // добавляем два дня от текущей даты вместо трёх
        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Чебоксары");
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).sendKeys(Keys.chord(Keys.CONTROL, Keys.BACK_SPACE));
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Вислый-Анков");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).shouldNot(Condition.appear, Duration.ofMillis(15000));

        $x("//span[@data-test-id='date']/descendant::span[text()='Заказ на выбранную дату невозможен']").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shdTestNegativeInvalidName() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        String date4input = TestDataGenerator.generateDateForInput(3, "dd.MM.yyyy");

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Омск");
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).sendKeys(Keys.chord(Keys.CONTROL, Keys.BACK_SPACE));
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("abobus_3450d");
        // вводим некорректное имя
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).shouldNot(Condition.appear, Duration.ofMillis(15000));

        $x("//span[@data-test-id='name']/descendant::span[text()='Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.']").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));

    }

    @Test
    public void shdTestNegativeInvalidPhone() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        String date4input = TestDataGenerator.generateDateForInput(3, "dd.MM.yyyy");

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Челябинск");
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).sendKeys(Keys.chord(Keys.CONTROL, Keys.BACK_SPACE));
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Анков-Вислый");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+100500");
        // вводим некорректный номер
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).shouldNot(Condition.appear, Duration.ofMillis(15000));

        $x("//span[@data-test-id='phone']/descendant::span[text()='Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.']").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shdTestNegativeWithNoAgreementCheck() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        String date4input = TestDataGenerator.generateDateForInput(3, "dd.MM.yyyy");

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Челябинск");
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).sendKeys(Keys.chord(Keys.CONTROL, Keys.BACK_SPACE));
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Анков-Вислый");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        // не выставляем чекбокс
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).shouldNot(Condition.appear, Duration.ofMillis(15000));
    }

}

