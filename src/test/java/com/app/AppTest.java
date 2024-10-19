package com.app;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

import com.CustomInputStream;

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

    @Test
    public void testMain_NoArgsAndInitGame() {
        String args[] = {};
        List<String> inputs = Arrays.asList(
            "1\n",
            "10 10 0 0 0 0 0\n",
            "1 0 1\n",
            "0 0\n"
        );
        CustomInputStream customInputStream = new CustomInputStream(inputs);
        System.setIn(customInputStream);
        App.main(args);
    }

    @Test
    public void testMain_NoArgsAndExitGame() {
        String args[] = {};
        List<String> inputs = Arrays.asList(
            "2\n"
        );
        CustomInputStream customInputStream = new CustomInputStream(inputs);
        System.setIn(customInputStream);
        App.main(args);
    }

    @Test
    public void testMain_OneArg() {
        String args[] = {"1"};
        App.main(args);
    }

    @Test
    public void testMain_LetterArg() {
        String args[] = {"10", "10", "nose", "5", "5", "5", "5"};
        App.main(args);
    }

    @Test
    public void testMain_LargeArg() {
        String args[] = {};
        List<String> inputs = Arrays.asList(
            "1\n",
            "10 10 0 0 0 0\n"
        );
        CustomInputStream customInputStream = new CustomInputStream(inputs);
        System.setIn(customInputStream);
        App.main(args);
    }
}