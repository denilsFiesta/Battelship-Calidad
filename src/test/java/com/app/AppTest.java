package com.app;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;
import com.app.App;

public class AppTest {

    @Test
    public void testParse_Input() {
        String test[] = {"1", "2", "3", "4"};
        int result[] = App.parse(test);
        int expected[] = {1, 2, 3, 4};
        assertArrayEquals(expected, result);
    }
}
