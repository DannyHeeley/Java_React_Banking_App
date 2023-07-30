package com.main.app;

import com.main.app.Bank;
import com.main.app.transactions.TransactionHistory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.main.app.transactions.TransactionType.DEPOSIT;
import static com.main.app.transactions.TransactionType.WITHDRAWAL;
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

}
