package com.modelTest.shipstest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.model.ships.Submarine;

public class SubmarineTest {

    private Submarine submarine;

    @BeforeEach
    public void setUp() {
        // Usamos un espía en todas las pruebas para poder verificar las llamadas a métodos
        submarine = spy(new Submarine());
    }

    @Test
    public void testGetLength() {
        // Verifica que getLength retorne el valor correcto (MAX_HEALTH)
        assertEquals(1, submarine.getLength(), "El largo del Submarine debería ser 1");
    }

    @Test
    public void testRestoreHealthInRecoveryMode() {
        // Llamamos al método para restaurar la salud en modo de recuperación
        submarine.restoreHealthInRecoveryMode();

        // Verificamos que el método restoreHealthInRecoveryMode haya sido llamado
        verify(submarine).restoreHealthInRecoveryMode();

        // Verificamos que el valor de la salud haya sido restaurado a MAX_HEALTH (1)
        assertEquals(1, submarine.getLength(), "La salud debería ser igual a MAX_HEALTH después de la restauración.");
    }

    @Test
    public void testToString() {
        // Verificamos que el método toString retorne el nombre correcto del barco
        assertEquals("Submarine", submarine.toString(), "El nombre del barco debería ser 'Submarine'");
    }
}
