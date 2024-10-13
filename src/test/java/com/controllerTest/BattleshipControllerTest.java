package com.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.controller.BattleshipController;

public class BattleshipControllerTest {

    @Test
    public void testRequestForUserInput_correctInput(){
        Scanner scannerSimulado = new Scanner("3\n"); 
        BattleshipController battleshipController = new BattleshipController(scannerSimulado);

        int result = battleshipController.requestForUserInput("", 1, 5);
        assertEquals(3, result, "La entrada 3 es valida en el rango [1,5]");
    }

    @Test
    public void testRequestForUserInput_maxBoundError(){
        Scanner scannerSimulado = new Scanner("7\n 3\n"); 
        BattleshipController battleshipController = new BattleshipController(scannerSimulado);

        int result = battleshipController.requestForUserInput("", 1, 5);
        assertEquals(3, result, "La entra 7 no es valida en el rango [1,5]");
    }

    @Test
    public void testRequestForUserInput_minBoundError(){
        Scanner scannerSimulado = new Scanner("0\n 3\n"); 
        BattleshipController battleshipController = new BattleshipController(scannerSimulado);

        int result = battleshipController.requestForUserInput("", 1, 5);
        assertEquals(3, result, "La entra 0 es valida en el rango [1,5]");
    }

    @Test
    public void testRequestForUserInput_tryCatchError(){
        Scanner scannerSimulado = new Scanner("a 3\n"); 
        BattleshipController battleshipController = new BattleshipController(scannerSimulado);

        int result = battleshipController.requestForUserInput("", 1, 5);
        assertEquals(3, result, "La entra a es valida en el rango [1,5]");
    }
}
