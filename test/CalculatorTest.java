package test;
import battleship.Calculator;

import static org.junit.Assert.assertEquals;
import org.junit.*;

public class CalculatorTest {
    @Test
    public void testAdd() {
        Calculator calculator = new Calculator();
        int result = calculator.add(2, 3);
        assertEquals("2 + 3 debería ser 5", result, 5);
    }

    @Test
    public void testMayorOne(){
        Calculator calculator = new Calculator();
        int result = calculator.mayor(7, 8);
        assertEquals("7 > 8? retorna 8", result, 8);
    }
    @Test
    public void testMayorTwo(){
        Calculator calculator = new Calculator();
        int result = calculator.mayor(8, 7);
        assertEquals("8 > 7? retorna 8", result, 8);
    }
}
