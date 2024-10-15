package com.app;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.CustomInputStream;
import com.app.App;
import com.controller.BattleshipController;

public class AppTest {

    @Test
    public void testParse_Input() {
        String test[] = {"1", "2", "3", "4"};
        int result[] = App.parse(test);
        int expected[] = {1, 2, 3, 4};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testMain_SevenArgs() {
        String args[] = {"10", "10", "0", "0", "0", "0", "0"};
        List<String> inputs = Arrays.asList(
            "1 0 1\n",
            "0 0\n"
        );
        CustomInputStream customInputStream = new CustomInputStream(inputs);
        System.setIn(customInputStream);
        App.main(args);
    }
}
