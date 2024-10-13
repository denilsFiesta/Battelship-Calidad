package com.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.controller.BattleshipController;
import com.model.game.Game;
import com.model.game.user.User;
import com.view.BattleshipView;

public class BattleshipControllerTest {

    private User mockUser;
    private BattleshipView mockView;

    @BeforeEach
    public void setUp() {
        mockUser = mock(User.class);
        mockView = mock(BattleshipView.class);
    }

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

    @Test
    public void testStartGame_validInput(){

        Scanner scannerSimulado = new Scanner("10 10 0 0 0 0 1 1 1 1\n"); 
        BattleshipController battleshipController = new BattleshipController(scannerSimulado);
        battleshipController.startGame();
    }

    @Test
    public void testStartGame_invalidInput(){
        Scanner scannerSimulado = new Scanner("10 10 5 5 5 5 5 2\n"); 
        BattleshipController battleshipController = new BattleshipController(scannerSimulado);
        battleshipController.startGame();
    }
}
