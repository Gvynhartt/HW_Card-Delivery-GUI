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
import java.util.ArrayList;
import java.util.HashMap;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryServiceTestsWithGUI {

    @Test
    public void shdTestPositiveCityDropdown() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        String date4input = TestDataGenerator.generateDateForInput(7, "dd.MM.yyyy");

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Мо");
        // вводим первые две буквы названия нужного города, в данном случае это Москва
        $x("//div[contains(@class,'popup')]/descendant::span[@class='menu-item__control' and text()='Москва']").click();
        // выбираем в сгенерированном выпадающем списке элемент, содержащий нужный нам город
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).sendKeys(Keys.chord(Keys.CONTROL, Keys.BACK_SPACE));
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Вислый-Анков");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).should(Condition.appear, Duration.ofMillis(15000));

        $x("//div[@data-test-id='notification']/descendant::div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно забронирована на " + date4input));
    }

    @Test
    public void shdTestNegativeCityDropdown() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        String date4input = TestDataGenerator.generateDateForInput(7, "dd.MM.yyyy");

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Йъ");
        // вводим такие две буквы, что не дадут ни одного соответствия => выпадающий список не появится
        $x("//div[@class='popup__container']/descendant::span[@class='menu-item__control']").shouldNot(Condition.appear);
        // добавляем проверку видимости выпадающего списка
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
    public void shdTestPositiveCalendarPopupSamePage() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        String date4input = TestDataGenerator.generateDateForInput(7, "dd.MM.yyyy");
        String date4search = TestDataGenerator.generateDateForInput(7, "d"); // новый формат даты (день) для поиска по XPath

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Кызыл");
        $x("//span[@data-test-id='date']/descendant::button[@role='button']").click();
        // отправляем клик по кнопке с иконкой календаря рядом с полем ввода даты
        $x("//div[@class='popup__container']/descendant::div[@role='grid']").should(Condition.appear);
        // проверяем, что элемент календаря стал видим для пользователя

        $x("//table[@class='calendar__layout']/descendant::td[@role='gridcell' and text()='" + date4search + "']").click();
        // собственно, вкорячиваем наш многострадальный день месяца в xpath в виде строковой переменной

        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Вислый-Анков");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).should(Condition.appear, Duration.ofMillis(15000));

        $x("//div[@data-test-id='notification']/descendant::div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно забронирована на " + date4input));
    }

    @Test
    public void shdTestPositiveCalendarPopupTurnThePage() { // гвоздь программы: случай с переворотом страницы
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        String date4input = TestDataGenerator.generateDateForInput(7, "dd.MM.yyyy");
        String date4search = TestDataGenerator.generateDateForInput(7, "d"); // новый формат даты (день) для поиска по XPath

        String currentMonth = TestDataGenerator.generateDateForInput(0, "MM"); // заводим переменную с текущим месяцем (понадобится далее)
        String monthIn7days = TestDataGenerator.generateDateForInput(7, "MM"); // заводим переменную с месяцем для даты через 7 дней

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Кызыл");
        $x("//span[@data-test-id='date']/descendant::button[@role='button']").click();
        // отправляем клик по кнопке с иконкой календаря рядом с полем ввода даты
        $x("//div[@class='popup__container']/descendant::div[@role='grid']").should(Condition.appear);
        // проверяем, что элемент календаря стал видим для пользователя

        // проверка, нужно ли перелистывать календарь,В ДОРАБОТАННОМ ВИДЕ
        if ( !currentMonth.equals(monthIn7days) ) {
            // мы проверяем в условии, попадает ли дата через 7 дней после текущей на следующий (отличный от текущего) иесяц
            $x("//div[@class='calendar__title']/div[@data-step=\"1\"]").click();
            // и если эта проверка проходит, жмём на кнопку, перелистывающую календарь на месяц вперёд
            $x("//div[@class='calendar__title']/div[@data-step=\"-1\"]").should(Condition.appear);
            // затем опеределяем, случился ли переход на следующую его страницу по видимости кнопки перехода на прошлый месяц: она должна появиться
        }

        $x("//table[@class='calendar__layout']/descendant::td[@role='gridcell' and text()='" + date4search + "']").click();
        // собственно, вкорячиваем наш многострадальный день месяца в xpath в виде строковой переменной

        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Вислый-Анков");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).should(Condition.appear, Duration.ofMillis(15000));

        $x("//div[@data-test-id='notification']/descendant::div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно забронирована на " + date4input));
    }

}
