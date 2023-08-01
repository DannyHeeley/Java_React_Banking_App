package com.main.app;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HandleDateTimeTest implements HandleDateTime {

    @Test
    void getsDateTimeNow() {
        LocalDateTime time = getDateTimeNow();
        assertTrue(time.isBefore(now()) || time.isEqual(now()));
    }

    @Test
    void parsesDateTimeToString() {
        LocalDateTime time = now();
        assertThat(parseDateTimeToString(time)).isExactlyInstanceOf(String.class);
    }

    @Test
    void getsDateTimeNowAsString() {
        assertThat(getDateTimeNowAsString()).isExactlyInstanceOf(String.class);
    }
}