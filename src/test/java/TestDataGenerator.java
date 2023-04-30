import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestDataGenerator {

        public static String generateDateForInput(long daysToAdd, String datePattern) {
            return LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern(datePattern));
        }
}
