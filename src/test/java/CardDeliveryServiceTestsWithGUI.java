import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
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
        Configuration.headless = true;
        open("http://localhost:9999");

        LocalDate showDownDate = LocalDate.now();
        LocalDate showDownPlusLimit = showDownDate.plusDays(3);
        DateTimeFormatter time4matter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date4input = String.valueOf(showDownPlusLimit.format(time4matter));

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Мо");
        // вводим первые две буквы названия нужного города, в данном случае это Москва
        $x("//div[contains(@class,'popup')]/descendant::span[@class='menu-item__control' and text()='Москва']").click();
        // выбираем в сгенерированном выпадающем списке элемент, содержащий нужный нам город
        $(By.xpath("//span[@data-test-id='date']/descendant::input[@placeholder='Дата встречи']")).setValue(date4input);
        $(By.xpath("//span[@data-test-id='name']/descendant::input[@name='name']")).setValue("Киктор Вислый-Анков");
        $(By.xpath("//span[@data-test-id='phone']/descendant::input[@name='phone']")).setValue("+88005553535");
        $(By.xpath("//label[@data-test-id='agreement']")).click();
        $(By.xpath("//button[@role='button']/descendant::span[text()=\"Забронировать\"]")).click();
        $(By.xpath("//div[@data-test-id='notification']/descendant::div[text()=\"Успешно!\"]")).should(Condition.appear, Duration.ofMillis(15000));

        String expected = "Встреча успешно забронирована на " + date4input;
        String actual = $x("//div[@data-test-id='notification']/descendant::div[@class='notification__content']").getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shdTestNegativeCityDropdown() {
        Configuration.holdBrowserOpen = true;
        Configuration.headless = true;
        open("http://localhost:9999");

        LocalDate showDownDate = LocalDate.now();
        LocalDate showDownPlusLimit = showDownDate.plusDays(3);
        DateTimeFormatter time4matter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date4input = String.valueOf(showDownPlusLimit.format(time4matter));

        $(By.xpath("//span[@data-test-id='city']/descendant::input[@placeholder='Город']")).setValue("Йъ");
        // вводим такие две буквы, что не дадут ни одного соответствия => выпадающий список не появится
        $x("//div[@class='popup__container']/descendant::span[@class='menu-item__control']").shouldNot(Condition.appear);
        // добавляем проверку видимости выпадающего списка
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
    public void shdTestPositiveCalendarPopup() { // и да, здесь будет ветвление, потому что некоторые вещи лучше не автоматизировать
        Configuration.holdBrowserOpen = true;
        Configuration.headless = true;
        open("http://localhost:9999");

        LocalDate showDownDate = LocalDate.now();
        long daysLimit = 7;  // заводим переменную для требуемого в тестах лага по веремени от текущей даты
        LocalDate showDownPlusLimit = showDownDate.plusDays(daysLimit);
        DateTimeFormatter time4matter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date4input = String.valueOf(showDownPlusLimit.format(time4matter));
        // преобразуем дату так же, как и раньше
        String date4search = date4input.substring(0, 2); // берём из неё день, т.е. первые два символа
        String dzero = "0";
        // заводим переменную для сравниения между ссылочными данными
        if ( date4search.substring(0, 1).equals(dzero) ) {
            date4search = date4input.substring(1, 2);
        }  // если первый символ - ноль (т.е. день записывается одной цифрой), берём только вторую цифру: она понадобится
        // далее для написания xpath до соответствующей ячейки в календаре

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

        String expected = "Встреча успешно забронирована на " + date4input;
        String actual = $x("//div[@data-test-id='notification']/descendant::div[@class='notification__content']").getText().trim();

        Assertions.assertEquals(expected, actual);
        // По-хорошему, для календаря ещё требуется написать проверку, остаётся ли с даннойго числа ещё 7 дней до конца месяца, которая определит,
        // понадобится ли для попадания в указанный лимит перелистывать ручками виджет на следующий месяц. Но в таком случае:
        // а) добавится ещё одна логическая развилка,
        // б) я понятия не имею, как вынуть все нужные данные из страницы, не прибегая ко внешним API с календарными данными,
        // в) за ТАКУЮ работу в реальности должны бы ещё услугами интимного характера прямо на рабочем месте доплачивать.
        // А мораль сей басни такова:
        // иногда проще нанять ещё одну обезьяну-джуна, чтобы та потыкала грабками в ГУЙ, а потом написала всю уставную макулатуру.
    }

}
