package com.main.test.app;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class bankTest {
    @Test
    void fail() {
        assertThat(42)
                .isEqualTo(0);
    }
}
