package utils;

import java.time.LocalDateTime;

public class DateTimeUtils {
    private static LocalDateTime currentDateTime = LocalDateTime.now();

    public static LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public static void setCurrentDateTime(LocalDateTime dateTime) {
        currentDateTime = dateTime;
    }
}