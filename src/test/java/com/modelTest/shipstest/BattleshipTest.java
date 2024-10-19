package com.modelTest.shipstest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.model.ships.Battleship;

public class BattleshipTest {

    private Battleship battleship;

    @BeforeEach
    public void setUp() {
        // Usamos un espía en todas las pruebas para poder verificar las llamadas a métodos
        battleship = spy(new Battleship());
    }

    @Test
    public void testGetLength() {
        assertEquals(4, battleship.getLength(), "El largo del Battleship debería ser 4");
    }

    @Test
    public void testRestoreHealthInRecoveryMode() {
        battleship.restoreHealthInRecoveryMode();

        verify(battleship).restoreHealthInRecoveryMode();

        assertEquals(4, battleship.getLength(), "La salud debería ser igual a MAX_HEALTH después de la restauración.");
    }

    @Test
    public void testToString() {
        // Verificamos que el método toString retorne el nombre correcto del barco
        assertEquals("Battleship", battleship.toString(), "El nombre del barco debería ser 'Battleship'");
    }
}
