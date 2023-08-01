package com.main.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;

public interface HandleDateTime {
    default String getDateTimeNowAsString() {
        LocalDateTime dateTimeNow = now();
        return parseDateTimeToString(dateTimeNow);
    }

    default String parseDateTimeToString(LocalDateTime dateTimeNow) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
        return dateTimeNow.format(formatter);
    }

    default LocalDateTime getDateTimeNow() {
        return now();
    }
}
