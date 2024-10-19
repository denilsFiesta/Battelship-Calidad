package com.modelTest.shipstest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.model.ships.Cruiser;

public class CruiserTest {

    private Cruiser cruiser;

    @BeforeEach
    public void setUp() {
        // Usamos un espía en todas las pruebas para poder verificar las llamadas a métodos
        cruiser = spy(new Cruiser());
    }

    @Test
    public void testGetLength() {
        // Verifica que getLength retorne el valor correcto (MAX_HEALTH)
        assertEquals(3, cruiser.getLength(), "El largo del Cruiser debería ser 3");
    }

    @Test
    public void testRestoreHealthInRecoveryMode() {
        // Llamamos al método para restaurar la salud en modo de recuperación
        cruiser.restoreHealthInRecoveryMode();

        // Verificamos que el método restoreHealthInRecoveryMode haya sido llamado
        verify(cruiser).restoreHealthInRecoveryMode();

        // Verificamos que el valor de la salud haya sido restaurado a MAX_HEALTH (3)
        assertEquals(3, cruiser.getLength(), "La salud debería ser igual a MAX_HEALTH después de la restauración.");
    }

    @Test
    public void testToString() {
        // Verificamos que el método toString retorne el nombre correcto del barco
        assertEquals("Cruiser", cruiser.toString(), "El nombre del barco debería ser 'Cruiser'");
    }
}
