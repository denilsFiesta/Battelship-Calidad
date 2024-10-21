package com.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.CustomInputStream;
import com.controller.BattleshipController;
import com.model.game.user.User;
import com.view.BattleshipView;
public class BattleshipControllerTest {

    private void setInputs(List<String> inputs){
        CustomInputStream customInputStream = new CustomInputStream(inputs);
        System.setIn(customInputStream);
    }

    @Test
    public void testRequestForUserInput_correctInput(){
        List<String> inputs = Arrays.asList("3\n");
        setInputs(inputs);

        BattleshipController battleshipController = new BattleshipController();

        int result = battleshipController.requestForUserInput("", 1, 5);
        assertEquals(3, result, "La entrada 3 es valida en el rango [1,5]");
    }

    @Test
    public void testRequestForUserInput_maxBoundError(){
        List<String> inputs = Arrays.asList("7\n", "3\n");
        setInputs(inputs);

        BattleshipController battleshipController = new BattleshipController();

        int result = battleshipController.requestForUserInput("", 1, 5);
        assertEquals(3, result, "La entra 7 no es valida en el rango [1,5]");
    }

    @Test
    public void testRequestForUserInput_minBoundError(){
        List<String> inputs = Arrays.asList("0\n", "3\n");
        setInputs(inputs);

        BattleshipController battleshipController = new BattleshipController();

        int result = battleshipController.requestForUserInput("", 1, 5);
        assertEquals(3, result, "La entra 0 es valida en el rango [1,5]");
    }

    @Test
    public void testRequestForUserInput_tryCatchError(){
        List<String> inputs = Arrays.asList("a\n", "3\n");
        setInputs(inputs);

        BattleshipController battleshipController = new BattleshipController();

        int result = battleshipController.requestForUserInput("", 1, 5);
        assertEquals(3, result, "La entra a es valida en el rango [1,5]");
    }

    @Test
    public void testStartGame_validInput(){
        List<String> inputs = Arrays.asList(
            "0 0 0 0 0\n",    
            "1 0 1\n"
        );
        setInputs(inputs);
        
        BattleshipController battleshipController = Mockito.spy(new BattleshipController());
        Mockito.doReturn(10).doReturn(15).when(battleshipController).requestForUserInput(anyString(), eq(1), eq(30));
        
        battleshipController.startGame();

        verify(battleshipController, times(2)).requestForUserInput(anyString(), eq(1), eq(30));
    }

    @Test
    public void testStartGame_invalidInput(){
        List<String> inputs = Arrays.asList(
            "5 5 5 5 5\n",    
            "2\n"
        );
        setInputs(inputs);
        
        BattleshipController battleshipController = Mockito.spy(new BattleshipController());
        Mockito.doReturn(1).doReturn(1).when(battleshipController).requestForUserInput(anyString(), eq(1), eq(30));

        battleshipController.startGame();

        verify(battleshipController, times(2)).requestForUserInput(anyString(), eq(1), eq(30));
    }

    @Test
    public void testStartGameArgs_correctInput(){
        int args[] = {10, 10, 0, 0, 0, 0, 1}; 
        List<String> inputs = Arrays.asList("1 1 1\n");
        setInputs(inputs);

        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame(args);
    }

    @Test
    public void testStartGameArgs_invalidInput(){
        int args[] = {1, 1, 5, 5, 5, 5, 5}; 
        List<String> inputs = Arrays.asList("2\n");
        setInputs(inputs);

        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame(args);
    }

    @Test
    public void testGetShipsCounters_correcInput(){
        List<String> inputs = Arrays.asList("10 10 0 0 0 0 1 1 1 1\n");
        setInputs(inputs);

        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame();
    }

    @Test
    public void testGetShipsCounters_incorrectInput(){
        List<String> inputs = Arrays.asList("10 10 -1 0 0 0 0 1 1 1 1\n");
        setInputs(inputs);

        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame();
    }

    @Test
    public void testExecuteCommands_HelpAndWinCommand(){
        int args[] = {10, 10, 0, 0, 0, 0, 0};
        List<String> inputs = Arrays.asList(
            "1 0 1\n",
            "help\n",
            "0 0\n"
        );
        setInputs(inputs);

        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame(args);
        battleshipController.executeCommands();
    }

    @Test
    public void testExecuteCommands_ExitCommand(){
        int args[] = {10, 10, 0, 0, 0, 0, 0};
        List<String> inputs = Arrays.asList(
            "1 0 1\n",
            "exit\n"
        );
        setInputs(inputs);

        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame(args);
        battleshipController.executeCommands();
    }

    @Test
    public void testExecuteCommands_NoInput(){
        int args[] = {10, 10, 0, 0, 0, 0, 0};
        List<String> inputs = Arrays.asList(
            "1 0 1\n"
        );
        setInputs(inputs);
        
        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame(args);
        battleshipController.executeCommands();
    }

    @Test
    public void testExecuteCurrentCommands_HitAShip(){
        int args[] = {2, 2, 0, 0, 0, 1, 0};
        List<String> inputs = Arrays.asList(
            "2 1\n",
            "0 0\n",
            "1 1\n"
        );
        setInputs(inputs);
        
        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame(args);
        battleshipController.executeCommands();
    }

    @Test
    public void testExecuteCurrentCommands_ModeTorpedo(){
        int args[] = {2, 2, 0, 0, 0, 1, 0};
        List<String> inputs = Arrays.asList(
            "1 1 1\n",
            "T 0 0\n"
        );
        setInputs(inputs);
        
        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame(args);
        battleshipController.executeCommands();
    }

    @Test
    public void testExecuteCurrentCommands_HitNoShips(){
        int args[] = {2, 2, 0, 0, 0, 0, 0};
        List<String> inputs = Arrays.asList(
            "2 1\n",
            "0 0\n"
        );
        setInputs(inputs);
        
        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame(args);
        battleshipController.executeCommands();
    }

    @Test
    public void testCorrectInput_TorpedoAttackOrder(){
        int args[] = {2, 2, 0, 0, 0, 1, 0};
        List<String> inputs = Arrays.asList(
            "1 1 1\n",
            "t 0 0\n"
        );
        setInputs(inputs);
        
        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame(args);
        battleshipController.executeCommands();
    }

    @Test
    public void testCorrectInput_TorpedoAttackOrderFailed(){
        int args[] = {2, 2, 0, 0, 0, 1, 0};
        List<String> inputs = Arrays.asList(
            "1 1 1\n",
            "a 0 0\n"
        );
        setInputs(inputs);
        
        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame(args);
        battleshipController.executeCommands();
    }

    @Test
    public void testCorrectInput_TorpedoAttackOrderTwo(){
        int args[] = {2, 2, 0, 0, 0, 1, 0};
        List<String> inputs = Arrays.asList(
            "1 1 1\n",
            "T 0 0\n"
        );
        setInputs(inputs);
        
        BattleshipController battleshipController = new BattleshipController();
        battleshipController.startGame(args);
        battleshipController.executeCommands();
    }
}