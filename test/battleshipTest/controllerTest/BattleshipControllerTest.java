package test.battleshipTest.controllerTest;

import static org.junit.Assert.assertEquals;

import java.util.Scanner;

import org.junit.Test;

import battleship.controller.BattleshipController;

public class BattleshipControllerTest {

    @Test
    public void testRequestForUserInput_correctInput(){
        Scanner scannerSimulado = new Scanner("1\n"); 
        BattleshipController battleshipController = new BattleshipController(scannerSimulado);

        int result = battleshipController.requestForUserInput("", 1, 2);
        assertEquals("La entra 1 es valida en el rango [1,2]", 1, result);
    }

    @Test
    public void testRequestForUserInput_invalidInput(){
        Scanner scannerSimulado = new Scanner("a 1\n"); 
        BattleshipController battleshipController = new BattleshipController(scannerSimulado);

        int result = battleshipController.requestForUserInput("", 1, 2);
        assertEquals("La entra a no es valida en el rango [1,2]", 1, result);
    }

    @Test
    public void testRequestForUserInput_outOfRangeInput(){
        Scanner scannerSimulado = new Scanner("7 1\n"); 
        BattleshipController battleshipController = new BattleshipController(scannerSimulado);

        int result = battleshipController.requestForUserInput("", 1, 2);
        assertEquals("La entra 1 es valida en el rango [1,2]", 1, result);
    }
}
