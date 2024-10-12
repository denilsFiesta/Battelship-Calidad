package test.battleshipTest.controllerTest;

import static org.junit.Assert.assertEquals;

import java.util.Scanner;

import org.junit.Test;

import battleship.controller.BattleshipController;

public class BattleshipControllerTest {

    @Test
    public void testRequestForUserInput_correctInput(){
        Scanner scannerSimulado = new Scanner("3\n"); 
        BattleshipController battleshipController = new BattleshipController(scannerSimulado);

        int result = battleshipController.requestForUserInput("", 1, 5);
        assertEquals("La entrada 3 es valida en el rango [1,5]", 3, result);
    }

    @Test
    public void testRequestForUserInput_maxBoundError(){
        Scanner scannerSimulado = new Scanner("7 3\n"); 
        BattleshipController battleshipController = new BattleshipController(scannerSimulado);

        int result = battleshipController.requestForUserInput("", 1, 5);
        assertEquals("La entra 7 no es valida en el rango [1,5]", 3, result);
    }

    @Test
    public void testRequestForUserInput_minBoundError(){
        Scanner scannerSimulado = new Scanner("0 3\n"); 
        BattleshipController battleshipController = new BattleshipController(scannerSimulado);

        int result = battleshipController.requestForUserInput("", 1, 5);
        assertEquals("La entra 0 es valida en el rango [1,5]", 3, result);
    }
}
