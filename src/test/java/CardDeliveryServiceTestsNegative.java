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

public class CardDeliveryServiceTestsNegative {

    @Test
    public void shdTestNegativeInvalidCity() {
        Configuration.holdBrowserOpen = true;
        Configuration.headless = true;
        open("http://localhost:9999");

        LocalDate showDownDate= LocalDate.now();
        LocalDate showDownPlusLimit = showDownDate.plusDays(3);
        DateTimeFormatter time4matter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date4input = String.valueOf(showDownPlusLimit.format(time4matter));

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Мусохранск");
        // вводим некорректный город
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Вислый-Анков");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).shouldNot(Condition.appear, Duration.ofMillis(15000));

        String expected = "Доставка в выбранный город недоступна";
        String actual = $x("//span[@data-test-id='city']/descendant::span[text()='Доставка в выбранный город недоступна']").getText();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shdTestNegativeInvalidDateBelowLimit() {
        Configuration.holdBrowserOpen = true;
        Configuration.headless = true;
        open("http://localhost:9999");

        LocalDate showDownDate= LocalDate.now();
        LocalDate showDownPlusLimit = showDownDate.plusDays(2); // плюсуем 2 дня от текущей даты
        DateTimeFormatter time4matter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date4input = String.valueOf(showDownPlusLimit.format(time4matter));

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Чебоксары");
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).sendKeys(Keys.BACK_SPACE);
        // предварительно удаляем введённое по умочанию значение
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        // задаётся дата 2 днями позже текущей
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Вислый-Анков");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).shouldNot(Condition.appear, Duration.ofMillis(15000));

        String expected = "Заказ на выбранную дату невозможен";
        String actual = $x("//span[@data-test-id='date']/descendant::span[text()='Заказ на выбранную дату невозможен']").getText();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shdTestNegativeInvalidName() {
        Configuration.holdBrowserOpen = true;
        Configuration.headless = true;
        open("http://localhost:9999");

        LocalDate showDownDate= LocalDate.now();
        LocalDate showDownPlusLimit = showDownDate.plusDays(3);
        DateTimeFormatter time4matter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date4input = String.valueOf(showDownPlusLimit.format(time4matter));

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Омск");
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("abobus_3450d");
        // вводим некорректное имя
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).shouldNot(Condition.appear, Duration.ofMillis(15000));

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = $x("//span[@data-test-id='name']/descendant::span[text()='Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.']").getText();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shdTestNegativeInvalidPhone() {
        Configuration.holdBrowserOpen = true;
        Configuration.headless = true;
        open("http://localhost:9999");

        LocalDate showDownDate= LocalDate.now();
        LocalDate showDownPlusLimit = showDownDate.plusDays(3);
        DateTimeFormatter time4matter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date4input = String.valueOf(showDownPlusLimit.format(time4matter));

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Челябинск");
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Анков-Вислый");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+100500");
        // вводим некорректный номер
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).shouldNot(Condition.appear, Duration.ofMillis(15000));

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = $x("//span[@data-test-id='phone']/descendant::span[text()='Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.']").getText();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shdTestNegativeWithNoAgreementChaek() {
        Configuration.holdBrowserOpen = true;
        Configuration.headless = true;
        open("http://localhost:9999");

        LocalDate showDownDate= LocalDate.now();
        LocalDate showDownPlusLimit = showDownDate.plusDays(3);
        DateTimeFormatter time4matter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date4input = String.valueOf(showDownPlusLimit.format(time4matter));

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Челябинск");
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Анков-Вислый");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        // не выставляем чекбокс
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).shouldNot(Condition.appear, Duration.ofMillis(15000));

        boolean expected = false;
        boolean actual = $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).isDisplayed();

        Assertions.assertEquals(expected, actual);
    }

}

